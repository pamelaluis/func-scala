package funsets

import org.scalatest.FunSuite

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.8/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  test("string take") {
    val message = "hello, world"
    assert(message.take(5) == "hello")
  }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  test("adding ints") {
    assert(1 + 2 === 3)
  }

  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  test("contains for negative set") {
    assert(contains(x => x < 0, -100))
  }

  test("contains for squares set") {
    assert(contains(x => math.sqrt(x) % 1 == 0, 64))
  }

  test("test singleton set") {
    assert(contains(singletonSet(100), 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1 = singletonSet(1)
    val s2 = singletonSet(2)
    val s3 = singletonSet(3)
    val s4 = singletonSet(-4)
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
      assert(contains(union(s, s3), 3), "Union 3 done")
    }
  }

  test("intersect of sets") {
    new TestSets {
      val s = union(s1, s2)
      val i1 = intersect(s, s1)
      val i2 = intersect(union(s, s3), s)
      assert(contains(i1, 1), "Intersect 1 with Union 1 & 2")
      assert(!contains(i1, 2), "Intersect 1 with Union 1 & 2 contains 2")
      assert(!contains(i2, 3), "Intersect 2 contains 3")
      assert(contains(i2, 1), "Intersect 2 does not contain 1")
      assert(contains(i2, 2), "Intersect 2 does not contain 2")
    }
  }

  test("diff of sets") {
    new TestSets {
      val set1 = union(union(s1, s2), s3)
      val diffset = diff(set1, s2)
      assert(contains(diffset, 1))
      assert(!contains(diffset, 2))
      assert(contains(diffset, 3))
    }
  }

  test("filter sets") {
    new TestSets {
      val set1 = union(union(union(s1, s2), s3), s4)
      val filteredSet = filter(set1, x => x > 0)
      assert(contains(set1, 1))
      assert(contains(set1, 2))
      assert(contains(set1, 3))
      assert(contains(set1, -4))

      assert(contains(filteredSet, 1))
      assert(contains(filteredSet, 2))
      assert(contains(filteredSet, 3))
      assert(!contains(filteredSet, -4))
    }
  }

  test("forall") {
    new TestSets {
      val set1 = union(union(union(s1, s2), s3), s4)
      assert(!forall(set1, p => p > 0))
      assert(!forall(set1, p => p < 0))
      val filteredSet = filter(set1, x => x > 0)
      assert(forall(filteredSet, p => p > 0))
    }
  }

  test("exists") {
    new TestSets {
      val set1 = union(union(union(s1, s2), s3), s4)
      assert(exists(set1, p => p < 0), "failed to find a negative value")
      val filteredSet = filter(set1, x => x > 0)
      assert(!exists(filteredSet, p => p < 0), "failed - found a negative value")
      val set2 = union(s => s >= 0 && s <= 100, t => t == -4)
      assert(!exists(set2, p => p == 250), "failed found 250 in the set")
      assert(exists(set2, p => p == -4), "failed to find -4")
      assert(!exists(set2, p => p == -999), "failed - found -999")
      assert(!exists(set2, p => p == 999), "failed - found 999")
      assert(!exists(set2, p => p == -1), "failed - found -1")
    }
  }

  test("map") {
    new TestSets {
      val set1 = union(s => s >= 0 && s <= 100, t => t == -4)
      val mappedSet = map(set1, x => x * 2)
      assert(exists(mappedSet, p => p == 200), "200 is not found in the mappedSet")
      assert(!exists(mappedSet, p => p == 99), "failed 99 is found in the mappedSet")
      assert(!exists(mappedSet, p => p == 250), "failed found 250 in the set")
      assert(!exists(mappedSet, p => p == -4), "failed found -4")
      assert(!exists(mappedSet, p => p == -999), "failed - found -999")
      assert(!exists(mappedSet, p => p == 999), "failed - found 999")
      assert(!exists(mappedSet, p => p == -1), "failed - found -1")
      assert(exists(mappedSet,p=>p == 102),"failed to find 102 in the set")
      assert(exists(mappedSet,p=>p == -8),"failed to find -8 in the set")
      assert(!exists(mappedSet,p=>p == 1),"failed found 1 in the set")
    }
  }
}
