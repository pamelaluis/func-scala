package greeter

object scratchpad {
  def factorial(n: Int) = {
  	def fact(acc: Int,n: Int): Int =
  		if(n==0) acc else fact(acc*n ,n-1)
  	
		fact(1,n);
  }                                               //> factorial: (n: Int)Int
  
  factorial(5)                                    //> res0: Int = 120
  
  def abs(x: Double) = if (x < 0) -x else x       //> abs: (x: Double)Double

  def sqrt(x: Double) {
    def sqrtIter(guess: Double): Double =
      if (isGoodEnough(guess)) guess
      else sqrtIter(improve(guess))

    def isGoodEnough(guess: Double) =
      abs(guess * guess - x) / x < 0.001

    def improve(guess: Double) =
      (guess + x / guess) / 2
    sqrtIter(1.0)
  }                                               //> sqrt: (x: Double)Unit
  
  sqrt(2)
  sqrt(4)
  sqrt(1e-6)
  sqrt(1e60)

	def gcd(a: Int, b: Int): Int =
		if (b==0) a else gcd(b, a%b)      //> gcd: (a: Int, b: Int)Int
		
	gcd(14,21)                                //> res1: Int = 7
	
}