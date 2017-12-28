package org.functionalkoans.forscala

import org.scalatest.matchers.ShouldMatchers
import language.implicitConversions
import support.KoanSuite

import scala.math.Ordering

class AboutImplicits extends KoanSuite with ShouldMatchers {

     koan("""Implicits wrap around existing classes to provide extra functionality
            |   This is similar to \'monkey patching\' in Ruby, and Meta-Programming in Groovy.
            |   Creating a method isOdd for Int, which doesn't exist""") {


          class KoanIntWrapper(val original: Int) {
               def isOdd = original % 2 != 0
          }

          implicit def thisMethodNameIsIrrelevant(value: Int) = new KoanIntWrapper(value)

          //note: implicit returns new KoanIntWrapper obj (hiddenly) so we can call 19.isOdd instead of isOdd(19)
          19.isOdd should be(true)
          20.isOdd should be(false)
     }

     koan("""Implicits rules can be imported into your scope with an import""") {
          object MyPredef {

               class KoanIntWrapper(val original: Int) {
                    def isOdd = original % 2 != 0

                    def isEven = !isOdd
               }

               implicit def thisMethodNameIsIrrelevant(value: Int) = new KoanIntWrapper(value)
          }

          import MyPredef._
          //imported implicits come into effect within this scope
          19.isOdd should be(true)
          20.isOdd should be(false)
     }

     koan("""Implicits can be used to automatically convert one type to another""") {

          import java.math.BigInteger

          // @todo: ---- understand better how this converts int args to biginteger (how does it reach them?)
          implicit def Int2BigIntegerConvert(value: Int): BigInteger = new BigInteger(value.toString)

          def add(a: BigInteger, b: BigInteger) = a.add(b)

          add(3, 6) should be(new BigInteger("9"))
     }



     koan("Implicit conversions can look like we are providing extra functionality to a type. Here," +
          "it looks like we are providing functionality to the Int class by being able to multiply with" +
          "fraction argument"){

          case class Rational(n: Int, d: Int) {
               require(d != 0)
               private val g = gcd(n.abs, d.abs)
               val numer = n / g
               val denom = d / g

               def this(n: Int) = this(n, 1)

               def * (that: Rational): Rational =
                    new Rational(numer * that.numer, denom * that.denom)

               def * (i: Int): Rational =
                    new Rational(numer * i, denom)

               override def toString = numer +"/"+ denom

               private def gcd(a: Int, b: Int): Int =
                    if (b == 0) a else gcd(b, a % b)
          }

          object RationalConversions {
               implicit def intToRational(n: Int): Rational = new Rational(n)
               implicit def rationalToDouble(r: Rational): Double = (r.numer * 1.0) / r.denom
               //BigDecimal((r.numer * 1.0 / r.denom).toString).setScale(1, BigDecimal.RoundingMode.DOWN).toDouble
               def roundTwo(d:Double):Double = Math.round(d * 10.0)/10.0
          }

          {
               import RationalConversions._

               val result = 3 * Rational(4, 5) //calls intToRational(3)
               //help how to do roundTwo implicitly?
               roundTwo(result) should be(2.4) // case classes have well-defined equals methods.
          }

          {
               //note imports everything but rationalToDouble method
               import RationalConversions.{rationalToDouble => _, _}

               val result = 3 * Rational(4, 5) //calls intToRational(3)
               result should be(Rational(12, 5)) // case classes have well-defined equals methods.
          }
     }



     koan("""Implicits can be used declare a value to be provided as a default as
            |   long as an implicit value is set with in the scope.  These are
            |   called implicit function parameters""") {

          def howMuchCanIMake_?(hours: Int)(implicit dollarsPerHour: BigDecimal) = dollarsPerHour * hours

          implicit var hourlyRate = BigDecimal(34.00)

          //note we are seeing the implicit at work here
          howMuchCanIMake_?(30) should be(1020)

          //note we can reassign to implicit vars
          hourlyRate = BigDecimal(95.00)
          howMuchCanIMake_?(95) should be(9025)

          // we can also give an explicit rate like this
          howMuchCanIMake_?(95)(BigDecimal(95)) should be(9025)
     }

     koan("We can also use an implicit method rather than implicit values in order to do more " +
          "complicated operations"){

          def calcTax(amount: Float)(implicit rate: Float): Float = amount * rate

          //scenario 1: using implicit val
          object SimpleStateSalesTax {
               implicit val rate: Float = 0.05F
          }

          //scenario 2: using implicit method
          case class ComplicatedSalesTaxData(baseRate:Float, isTaxHoliday:Boolean, storeID:Int)
          object ComplicatedSalesTax {
               private def extraTaxRateForStore(id:Int):Float ={
                    //from id, determine location, then extra taxes
                    if(id % 3 == 0) 0.02F
                    else if(id % 5 == 0) 0.15F
                    else 0.04F
               }

               implicit def rate(implicit cstd: ComplicatedSalesTaxData): Float ={
                    if(cstd.isTaxHoliday) 0.0F
                    else cstd.baseRate + extraTaxRateForStore(cstd.storeID)
               }
          }


          {
               import SimpleStateSalesTax.rate
               val amount = 100F
               calcTax(amount) should be(5.0)
          }

          {
               import ComplicatedSalesTax.rate
               implicit val myStore = ComplicatedSalesTaxData(0.06F, isTaxHoliday = false, 1010)
               val amount = 100F
               calcTax(amount) should be(21.0)
          }
     }



     koan("""Implicit Function Parameters can contain a list of implicits""") {

          def howMuchCanIMake_?(hours: Int)(implicit amount: BigDecimal, currencyName: String) =
               (amount * hours).toString() + " " + currencyName

          implicit var hourlyRate = BigDecimal(34.00)
          implicit val currencyName = "Dollars"

          howMuchCanIMake_?(30) should be("1020.0 Dollars")

          hourlyRate = BigDecimal(95.00)
          howMuchCanIMake_?(95) should be("9025.0 Dollars")
     }


     koan("Implicit Function Parameters can also be passed as methods directly, like method order() is " +
          "passed into the smaller() function. The order() function is an implicit conversion besides being" +
          "an implicit argument, because it has one argument of type T and returns an Ordered[T]"){

          class Pair[T](val first:T, val second:T){
               def smaller(implicit order: T => Ordered[T]) =
                    if(first < second) first else second
               //note book says this calls order(first) if first doesn't have a (<) operator.
               //help but doesn't it call order(second) as well? todo
          }

          val p1 = new Pair(2, 5)
          val p2 = new Pair("abc", "xyz")

          p1.smaller should be(2)
          p2.smaller should be("abc")
     }


     koan("We can also use a context bound to use implicits in a variety of ways: " +
          "(1) passing implicit objects right into a method, " +
          "(2) using implicitly, or " +
          "(3) relying on implicit conversions from other types"){

          class Pair[T: Ordering](val first:T, val second:T){
               def smaller(implicit ord: Ordering[T]) =
                    if(ord.compare(first, second) < 0) first else second

               //or we can use implicitly
               def smallerWithImplicitly =
                    if (implicitly[Ordering[T]].compare(first, second) < 0) first else second

               def smallerWithOp = {
                    //note there is an implicit conversion from Ordering to Ordered, and importing
                    //it brings it in scope, so we can use relational operators (easier to read)
                    import Ordered._;
                    if(first < second) first else second
               }
          }

          //note the important point is that you can instantiate Pair[T] whenever there is an
          //implicit value of type Ordering[T]. So if you want Pair[Point], make an Ordering[Point]
          val p = new Pair(42, 3)
          p.smaller should be(3)
          p.smallerWithImplicitly should be(3)
          p.smallerWithOp should be(3)
     }


     koan(
          """Default arguments though are preferred to Implicit Function Parameters because there
            |can only be one implicit parameter per data type. If you have two different string
            |arguments, the compiler will only use one implicit definition for both.
          """.stripMargin) {

          def howMuchCanIMake_?(hours: Int, amount: BigDecimal = 34, currencyName: String = "Dollars") =
               (amount * hours).toString() + " " + currencyName

          howMuchCanIMake_?(30) should be("1020 Dollars")

          howMuchCanIMake_?(95, 95) should be("9025 Dollars")
     }


     //todo: rewrite this example using the more tangible code here: http://like-a-boss.net/2013/03/29/polymorphism-and-typeclasses-in-scala.html
     // goal: so that we have (side-by-side) the implicit arg example, implicitly example, and suffix example. (last
     // thing in article)
     koan("Method implicitly can take a parametrized type as argument and in this" +
          "way be used as a shorthand way of defining method signatures that take" +
          "a single implicit argument, like sortBy(). Here we can define a new method" +
          "that wraps sortBy(), with an implicit argument and then pass it, or we" +
          "can use the shortcut implicitly directly in the sortBy() method."){

          case class MyList[A](list: List[A]){
               def sortBy1[B](f: A => B)(implicit ord: Ordering[B]): List[A] =
                    list.sortBy(f)(ord)

               //note here the B: Ordering is known as a context bound and works together
               // with implicitly to do the same thing as above method.
               def sortBy2[B: Ordering](f: A => B): List[A] =
                    list.sortBy(f)(implicitly[Ordering[B]])
          }

          val list = MyList(List(1,3,5,2,4))

          (list sortBy1 (i => -i)) should be(List(5,4,3,2,1))
          (list sortBy2 (i => -i)) should be(List(5,4,3,2,1))
     }

}