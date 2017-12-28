package org.functionalkoans.forscala

import support.KoanSuite

class AboutTypeBounds extends KoanSuite {

     //todo help see http://jatinpuri.com/2014/03/replace-view-bounds/
     //on how to replace view bounds, as they were deprecated around 2014.
     koan("View bounds allow the type on the left to be implicitly converted to another type" +
          "satisfying the right hand side. In this case, Int does not implement comparable while RichInt" +
          "does. The view bound <% allows implicit conversion from Int to Comparable[RichInt]"){

          class Pair[T <% Comparable[T]](val first:T, val second:T){
               def smaller = if(first.compareTo(second) < 0) first else second
          }

          val p = new Pair(42, 3)
          p.smaller should be(3)
     }

     koan("However, it is nicer to use the Ordered trait which adds relational operators" +
          "to Comparable"){

          class Pair[T <% Ordered[T]](val first:T, val second:T){
               def smaller = if(first < second) first else second
          }

          val p = new Pair(42, 3)
          p.smaller should be(3)
     }


     koan("Context bounds have the form T: M where M is another generic type." +
          "It requires there being an implicit value of type M[T]." +
          "Using T: Ordering requires there being an implicit value of type Ordering[T]." +
          "When you declare a method that uses that implicit value, you must add an " +
          "implicit parameter:"){

          case class Point(x:Int, y:Int)

          implicit object PointOrdering extends Ordering[Point] {
               def compare(a:Point, b:Point) = {
                    if(a.x == b.x) a.y - b.y
                    else a.x - b.x
               }
          }

          class Pair[T: Ordering](val first:T, val second:T){
               def smaller(implicit ord: Ordering[T]): T =
                    if(ord.compare(first, second) < 0) first else second
          }

          //note the important point is that you can instantiate Pair[T] whenever there is an
          //implicit value of type Ordering[T]. So if you want Pair[Point], make an Ordering[Point]
          val p1 = new Pair[Point](Point(1,2), Point(1,5))
          p1.smaller should be(Point(1,2))

          val p2 = new Pair[Point](Point(3, -4), Point(1, 9))
          p2.smaller should be(Point(1,9))
     }

     koan("You need manifest context bound to capture types for runtime evaluation, in order to" +
          "instantiate primitive type arrays as underlying Java uses separate arrays for each type" +
          "and needs to know which types they are at runtime."){

          def makePair[T: Manifest](first:T, second:T) = {
               val arrayPair = new Array[T](2)
               arrayPair(0) = first
               arrayPair(1) = second
               arrayPair
          }

          //note when you call makePair(8, -3) the compiler locates the implicit Manifest[Int]
          //and calls makePair(8, -3)(intManifest).
          makePair(8, -3) should be(Array[Int](8, -3))
     }

     //todo make koan showing multiple bounds (T <: Lower >: Lower) but cannot have
     // multiple upper or lower bounds.

     //todo show we can have more than one view bound (T <% Comparable[T] <% String)

     //todo show more than one context bound (T : Ordering : Manifest)


     koan("Type constraints =:= and <:< and >:> and <&< are used as assertions along with implicits." +
          "(1) T =:= U asserts if type T equals type U"){

          import scala.reflect.runtime.universe._

          def toType[T: TypeTag](t:T): Type = typeOf[T]

          toType(1) =:= typeOf[AnyVal]  should be(false)
          toType(1) =:= toType(1)       should be(true)
          toType(1) =:= toType(true)    should be(false)
          typeOf[Seq[Int]] =:= typeOf[Seq[Any]] should be(false)
     }

     koan("Type constraint (2) T <:< U asserts if type T is a subtype of type U"){

          import scala.reflect.runtime.universe._

          def toType[T: TypeTag](t:T): Type = typeOf[T]

          toType(1) <:< typeOf[AnyVal]  should be(true)
          toType(1) <:< toType(1)       should be(true)
          toType(1) <:< toType(true)    should be(false)
          typeOf[Seq[Int]] <:< typeOf[Seq[Any]] should be(true)
     }

     //todo help
     koan("Type constraint (3) T <%< U asserts if type T is viewable as type U, in other words, if it can be " +
          "implicitly converted to type U" +
          "NOTE: <%< was deprecated, so we are using what the class would have looked like, instead."){

          import scala.reflect.runtime.universe._
          import scala.language.implicitConversions


          object ViewableAs {
               sealed abstract class <%<[-From, +To] extends (From => To)
               implicit object <%< {
                    implicit def conformsOrViewsAs[A <% B, B]: A <%< B = new (A <%< B) {def apply(x: A) = x}
               }
          }
          import ViewableAs._

          def toType[T: TypeTag](t:T)(implicit ev: T <%< Int): Type = typeOf[T]
          //toType(1) <%< toType(1) should be(false)



          case class Pollen(granules:Int)(implicit ev: Pollen <%< Honey) {
               def toHoney = ev(this)
          }
          case class Honey(honeycombs:Int)/*(implicit evidence: T <%< Honey)*/

          //note 1 honeycomb = 1 million granules
          implicit def pollenToHoney(p: Pollen): Honey = new Honey((p.granules * 1.0 / 1000000.0).toInt)

          val pollen = Pollen(2000000) //2 million, 2 honeycombs
          val honey = Honey(2)
          pollen.toHoney should be(honey)

          //help todo why doesn't this work?
          /*
          toType(pollen) <%< toType(honey)
          <%<[toType(pollen), toType(honey)]*/
     }





     // ---------------------------------------------------------------------------------

     trait Instrument
     case class Equity(name: String) extends Instrument
     abstract class FixedIncome(name: String) extends Instrument
     case class DiscountBond(name: String, discount: Int) extends FixedIncome(name)
     case class CouponBond(name: String, coupon: Int) extends FixedIncome(name)

     //note here we are using the separate abstraction, AccruedInterestCalculator class,
     // because we aren't using type constraints and must be verbose about the T being a
     //subtytpe of Trade[CouponBond]
     object VerboseNoTypeConstraint {
          case class Trade[I <: Instrument](id: Int, account: String, instrument: I) {
               def calculateNetValue = "net value"
               def calculateValueDate = "value date"
          }
          class AccruedInterestCalculator[T <: Trade[CouponBond]](trade: T) {
               //weird, fictious implementation, just to return something
               def accruedInterest(convention: String): Double = {
                    val toTakeFrom = convention.split('/')(1).toDouble
                    trade.instrument match {
                         case CouponBond(_, coup) => toTakeFrom - toTakeFrom * (coup / 100.0)
                         case _ => throw new Exception("something other than CouponBond was passed")
                    }
               }
          }
     }

     //note here we have eliminated the AccruedInterestCalculator class and instead we use the =:=
     //type constraint in the accruedInterest() method directly. This means that type I Instrument
     // must be of type CouponBond. Direct and concise.
     object ConciseWithTypeConstraint {
          class Trade[I <: Instrument](id: Int, account: String, instrument: I) {
               def calculateNetValue = "net value"
               def calculateValueDate = "value date"
               //weird, fictious implementation, just to return something
               def accruedInterest(convention: String)(implicit ev: I =:= CouponBond): Double = {
                    val toTakeFrom = convention.split('/')(1).toDouble
                    instrument match {
                         case CouponBond(_, coup) => toTakeFrom - toTakeFrom * (coup / 100.0)
                         case _ => throw new Exception("something other than CouponBond was passed")
                    }
               }
          }
     }

     koan("Example of =:= " +
          "We can minimize code by constraining types where we want them, instead of having to define " +
          "separate code." +
          "In the below example, we want to calculate accrued interest, but this is only applicable for" +
          "CouponBonds. Unfortunately, we cannot specialize the Instrument type I for any specific method " +
          "within Trade beyond what is specified as the constraint in defining the Trade class. " +
          "We could make a separate abstraction, the AccruedInterestCalculator class, but type constraints" +
          "like =:= allow us "){

          {
               import VerboseNoTypeConstraint._

               val cb = CouponBond("IBM", 10)
               val trd = new Trade(1, "account-1", cb)
               val result = new AccruedInterestCalculator(trd).accruedInterest("30U/360")
               result should be(324.0)
          }

          {
               import ConciseWithTypeConstraint._

               //note ev is the type class which the compiler provides that ensures that we invoke
               // accruedInterest only for CouponBond trades. You can now do ..

               val cb = CouponBond("IBM", 10)
               val trd = new Trade(1, "account-1", cb)
               val result = trd.accruedInterest("30U/360")
               result should be(324.0)

               //note Uncomment to see that at runtime we get error: Cannot prove that Equity =:= CouponBond
               /**
                 * val eq = Equity("GOOG")
                 * val trd1 = new Trade(2, "account-1", eq)
                 * trd1.accruedInterest("30U/360")
                 */
          }
     }

     koan("Example of <:< " +
          "we can supply a method in a generic class that can only be used under certain " +
          "conditions. For instance, we can form a Pair[Animal] even though Animal is not " +
          "ordered. We only get an error if we invoke the smaller() method but otherwise, no error"){

          class Animal(val name: String)
          class Giraffe(name:String, val numSpots:Int) extends Animal(name)
          class Koala(name:String, val hoursSlept:Int) extends Animal(name)
          case class Oyster(override val name:String, val numPearls:Int)
               extends Animal(name) with Ordered[Oyster]{

               override def compare(that: Oyster): Int = numPearls - that.numPearls
          }

          //note (<) may not be valid for type T but since ev is a function of implicit
          //conversion from T to Ordered[T] then compiler applies this conversion
          // and gets the (<) method
          class Pair[T](val first: T, val second: T) {
               def smaller(implicit ev: T <:< Ordered[T]) =
                    if(first < second) first else second
          }

          val giraffe = new Giraffe("Spotty", 100)
          val koala = new Koala("Koko", 5)
          val oyster1 = new Oyster("Shiny", 2)
          val oyster2 = new Oyster("Clammy", 15)

          val pairAnimal = new Pair[Animal](giraffe, koala)
          //wrong Cannot prove that Animal <:< Ordered[Animal]
          //pairAnimal.smaller should be(koala)

          //note we can compare oysters because Oyster extends Ordered
          val pairOyster = new Pair[Oyster](oyster1, oyster2)
          pairOyster.smaller should be(oyster1)
     }


     koan("Example of <:< " +
          "We call ev an 'evidence object' - its existence is evidence of the fact that" +
          "here, C is a subtype of Iterable[A]. Here the evidence object is the identity" +
          "function because the compiler applies the implicit identity conversion" +
          "from C to Iterable[A], allowing use of head() and tail() methods"){

          def firstLast[A, C](it: C)(implicit ev: C <:< Iterable[A]) =
               (it.head, it.last)

          firstLast(List(1,2,3,4)) should be(1, 4)

          //note Tip: to test whether implicit object conversion exists, write:
          // implicitly[A <:< B]
          // As example: implicitly[String <:< AnyRef] works because it returns a function, so
          // we know there is an implicit conversion from String => AnyRef but
          // implicitly[AnyRef <:< String] doesn't work so there's no implicit conversion AnyRef => String
     }


     koan("Example of =:=" +
          "Example of <:<" +
          "Example of <%<"){

          //note commented out because compiler said errors there are other implicit conversions from string
          // to int, doesn't know which one to use... use other example koans
          /*object ViewableAs {
               sealed abstract class <%<[-From, +To] extends (From => To)
               implicit object <%< {
                    implicit def conformsOrViewsAs[A <% B, B]: A <%< B = new (A <%< B) {def apply(x: A) = x}
               }
          }
          import ViewableAs._

          import scala.language.implicitConversions

          class ContainerEquable[A](value: A) { def addIt(implicit evidence: A =:= Int) = 123 + value }
          //class ContainerSubtypeAble[A](value: A) { def addIt(implicit evidence: A <:< Int) = 123 + value }
          class ContainerViewable[A](value: A) { def addIt(implicit evidence: A <%< Int) = 123 + value }

          implicit def strToInt(x: String): Int = x.toInt

          new ContainerEquable(123).addIt should be(246)
          //new ContainerSubtypeAble("123").addIt should be(246)
          new ContainerViewable("123").addIt should be(246)*/
     }
}
