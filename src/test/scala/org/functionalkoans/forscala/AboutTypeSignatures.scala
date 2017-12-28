package org.functionalkoans.forscala

import support.KoanSuite

class AboutTypeSignatures extends KoanSuite {

     koan("In Java you declare a generic type within a <>, in Scala it is []") {
          val z: List[String] = "Do" :: "Re" :: "Mi" :: "Fa" :: "So" :: "La" :: "Te" :: "Do" :: Nil
     }

     koan("Most of the time, Scala will infer the type and [] are optional") {
          //Infers that the list assigned to variable is of type List[String]
          val z = "Do" :: "Re" :: "Mi" :: "Fa" :: "So" :: "La" :: "Te" :: "Do" :: Nil
     }

     koan("A trait can be declared containing a type, where a concrete implementer will satisfy the type") {
          trait Randomizer[A] {
               def draw(): A
          }

          class IntRandomizer extends Randomizer[Int] {
               def draw() = {
                    import util.Random
                    Random.nextInt()
               }
          }

          val intRand = new IntRandomizer
          (intRand.draw < Int.MaxValue) should be (true)
     }


     koan("A class can also be declared containing a type"){
          import scala.reflect.runtime.universe._

          class Pair[T: TypeTag, S: TypeTag](val first:T, val second:S) {
               def typeOfFirst = s"type of '$first' is ${typeOf[T].typeSymbol.name.decodedName.toString}"
               def typeOfSecond = s"type of '$second' is ${typeOf[S].typeSymbol.name.decodedName.toString}"
          }

          val p1 = new Pair(42, "argument")
          p1.typeOfFirst should be("type of '42' is Int")
          p1.typeOfSecond should be("type of 'argument' is String")

          val p2 = new Pair[Any, Any](42, "argument")
          p2.typeOfFirst should be("type of '42' is Any")
          p2.typeOfSecond should be("type of 'argument' is Any")
     }


     koan("Functions can also have type parameters"){

          def getMiddle[T](array: Array[T]) = array(array.length / 2)

          val midStr = getMiddle[String]("Mary had a little lamb whose fleece was white as snow".split(" "))
          val midInt = getMiddle[Int]((0 to 10).toArray)
          val midDouble = getMiddle[Double]((0 to 10).map(_.toDouble).toArray)
          midStr should be("whose")
          midInt should be(5)
          midDouble should be(5.0)


          val getMidStr = getMiddle[String] _
          getMidStr("Baa baa black sheep have you any wool?".split(" ")) should be("have")
     }


     koan("Class meta-information can be retrieved by class name by using classOf[className]") {
          classOf[String].getCanonicalName should be("java.lang.String")
          classOf[String].getSimpleName should be("String")
     }

     koan("Class meta-information can be derived from an object reference using getClass()") {
          val zoom = "zoom"
          zoom.getClass should be(classOf[java.lang.String]) // Hint: classOf ...
          zoom.getClass.getCanonicalName should be("java.lang.String")
          zoom.getClass.getSimpleName should be("String")
     }

     koan("isInstanceOf[className] is used to determine the if an object reference is an instance of given class") {
          trait Randomizer[A] {
               def draw(): A
          }

          class IntRandomizer extends Randomizer[Int] {
               def draw() = {
                    import util.Random
                    Random.nextInt()
               }
          }

          val intRand = new IntRandomizer
          intRand.draw.isInstanceOf[Int] should be(true)
     }

     koan("asInstanceOf[className] is used to cast one reference to another") {
          trait Randomizer[A] {
               def draw: A
          }

          class IntRandomizer extends Randomizer[Int] {
               def draw = {
                    import util.Random
                    Random.nextInt()
               }
          }

          val intRand = new IntRandomizer
          val rand = intRand
          val intRand2 = rand
          intRand2.isInstanceOf[IntRandomizer] should be(true)
     }

     koan("asInstanceOf[className] will throw a ClassCastException if a class derived from " +
          "and the class target aren't from the same inheritance branch") {
          trait Randomizer[A] {
               def draw(): A
          }

          class IntRandomizer extends Randomizer[Int] {
               def draw() = {
                    import util.Random
                    Random.nextInt()
               }
          }

          val intRand = new IntRandomizer

          intercept[ClassCastException] {
               intRand.asInstanceOf[String] //intRand cannot be cast to String
          }
     }

     koan("null.asInstanceOf[className] can be used to generate basic default values") {
          null.asInstanceOf[String] should be(null)
          null.asInstanceOf[Int] should be(0)
          null.asInstanceOf[Short] should be(0)
     }


     koan("We can inherit and override properties of other classes using types"){

          class WithF{
               def f(n:Int) = n * 11
          }

          class CallF[T <: WithF](t:T) {
               def g(n:Int) = t.f(n)
          }

          def callF[T <: WithF](t:T, n:Int) = t.f(n)

          callF(new WithF, 2) should be(22)
          callF(new WithF{
               override def f(n:Int) = n*7
          }, 2) should be(14)

          new CallF(new WithF).g(2) should be(22)
          new CallF(new WithF {
               override def f(n:Int) = n*7
          }).g(2) should be(14)
     }

     koan("We can use upper bounds to get inherited functionality"){
          import scala.reflect.runtime.universe._


          //we need T to inherit from Comparable so we are allowed to use compareTo() method
          class Pair[T <: Comparable[T]](val first: T, val second: T)(implicit tt: TypeTag[T]){
               def smaller: T = if(first.compareTo(second) < 0) first else second
               def getTypeOfT: String = s"${tt.tpe.typeSymbol.name.decodedName.toString}"
          }

          val p = new Pair[Integer](10, 20)
          p.smaller should be(10)
          p.getTypeOfT should be("Integer")
     }

     koan("We can use lower bounds to ..."){
          import scala.reflect.runtime.universe._

          //help todo understand better why lower bound is more necessary here, why not just
          //use upper bound on Person instead of lower bound on student... look at dean wampler book

          class Person(val name:String, val age:Int)
          class Student(name:String, age:Int, val study:String) extends Person(name, age)

          //help todo understand why not just say  T >: Student in Pair arg and why say instead
          // as we did in the actual method... works differently too, why?
          //help todo understand how to test the types R, T
          case class Pair[T](first: T, second: T)/*(implicit tt: TypeTag[T])*/{
               def replaceFirst[R >: T](newFirst: R) = new Pair[R](newFirst, second)
               //def getTypeOfT: String = s"${tt.tpe.typeSymbol.name.decodedName.toString}"
               //def getTypeOfR[R >: T](implicit ttr: TypeTag[R]): String =
               //     s"${ttr.tpe.typeSymbol.name.decodedName.toString}"
          }


          val elinor = new Person("Elinor", 25)
          val marianne = new Person("Marianne", 22)
          val franklin = new Person("Franklin", 27)
          val jack = new Student("Jack", 11, "historian")
          val annie = new Student("Annie", 10, "explorer")
          val cleo = new Student("Cleo", 12, "geologist")


          val personPair = new Pair[Person](elinor, marianne)
          val studentPair = new Pair[Student](jack, annie)
          //personPair.getTypeOfT should be("Person")
          //studentPair.getTypeOfT should be("Student")

          personPair.replaceFirst(franklin) should be(new Pair(franklin, marianne))
          //note works because student is-a person
          personPair.replaceFirst(jack) should be(new Pair(jack, marianne))

          studentPair.replaceFirst(cleo) should be(new Pair(cleo, annie))
          //note this works because person=R type is lower bounded by student=T
          //help todo true?
          studentPair.replaceFirst(elinor) should be(new Pair(elinor, annie))
          //studentPair.getTypeOfR should be("Student")
     }
}
