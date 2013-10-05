package recfun
import common._

object Main {
  def main(args: Array[String]) {
    println("Pascal's Triangle")
    for (row <- 0 to 10) {
      for (col <- 0 to row)
        print(pascal(col, row) + " ")
      println()
    }
  }

  /**
   * Exercise 1
   */
  def pascal(c: Int, r: Int): Int = {
    if (c > r) throw new IllegalArgumentException
    if (c == 0 || c == r) 1 else pascal(c - 1, r - 1) + pascal(c, r - 1)
  }
  //2,4 = 1,3 + 2,3
  /**
   * Exercise 2
   */
  def balance(chars: List[Char]): Boolean = {
    def balance_remaining(chars: List[Char], openCount: Int): Boolean = {
      if (chars.isEmpty)
        openCount == 0
      else if (openCount < 0)
        false
      else if (chars.head == '(')
        balance_remaining(chars.tail, openCount + 1)
      else if (chars.head == ')')
        return balance_remaining(chars.tail, openCount - 1)
      else
        return balance_remaining(chars.tail, openCount)

    }
    balance_remaining(chars, 0)
  }

  /**
   * Exercise 3
   */
  def countChange(money: Int, coins: List[Int]): Int = ???
}
