package org.functionalkoans.forscala

import support.KoanSuite

class AboutCaseClasses extends KoanSuite {

     // case classes are very convenient, they give you a lot for free. The following Koans will
     // help you understand some of the conveniences. Case classes are also an integral part of
     // pattern matching which will be the subject of a later

     koan("Case classes have an automatic equals method that works") {

          // @todo: ---- Difference between case class, class, object and when is best to use either?


          case class Person(first: String, last: String)

          val p1 = new Person("Fred", "Jones")
          val p2 = new Person("Shaggy", "Rogers")
          val p3 = new Person("Fred", "Jones")

          (p1 == p2) should be(false)
          (p1 == p3) should be(true)

          (p1 eq p2) should be(false)
          (p1 eq p3) should be(false) // not identical, merely equal
     }

     koan("Case classes have an automatic hashcode method that works") {
          case class Person(first: String, last: String)

          val p1 = new Person("Fred", "Jones")
          val p2 = new Person("Shaggy", "Rogers")
          val p3 = new Person("Fred", "Jones")

          (p1.hashCode == p2.hashCode) should be(false)
          (p1.hashCode == p3.hashCode) should be(true)
          // @todo: why true that hashcodes of different objs are same if same contents?
     }

     koan("Case classes have a convenient way they can be created") {
          case class Dog(name: String, breed: String)

          // @todo: ---- why can case classes be declared without new and why can't objects or normal classes?
          val d1 = Dog("Scooby", "Doberman")
          val d2 = Dog("Rex", "Custom")
          val d3 = new Dog("Scooby", "Doberman") // the old way of creating using new

          (d1 == d3) should be(true)
          (d1 == d2) should be(false)
          (d2 == d3) should be(false)
     }

     koan("Case classes have a convenient toString method defined") {
          case class Dog(name: String, breed: String)
          val d1 = Dog("Scooby", "Doberman")
          d1.toString should be("Dog(Scooby,Doberman)")
     }

     koan("Case classes have automatic properties") {
          case class Dog(name: String, breed: String)

          val d1 = Dog("Scooby", "Doberman")
          d1.name should be("Scooby")
          d1.breed should be("Doberman")

          // what happens if you uncomment the line below? Why?
          //d1.name = "Scooby Doo"
     }

     koan("Case classes can have mutable properties") {
          case class Dog(var name: String, breed: String) // you can rename a dog, but change its breed? nah!
          val d1 = Dog("Scooby", "Doberman")

          d1.name should be("Scooby")
          d1.breed should be("Doberman")

          d1.name = "Scooby Doo" // but is it a good idea?

          d1.name should be("Scooby Doo")
          d1.breed should be("Doberman")
     }

     koan("Safer alternatives exist for altering case classes") {
          case class Dog(name: String, breed: String) // Doberman

          val scooby = Dog("Scooby", "Doberman")

          val doo = scooby.copy(name = "Scooby Doo") // copy the case class but change the name in the copy

          scooby.name should be("Scooby") // original left alone
          scooby.breed should be("Doberman")

          doo.name should be("Scooby Doo")
          doo.breed should be("Doberman") // copied from the original
     }

     // case class has to be defined outside of the test for this one
     case class Person(first: String, last: String, age: Int = 0, ssn: String = "")

     koan("Case classes have default and named parameters") {

          val fred = Person("Fred", "Jones", 23, "111-22-3333")
          val samantha = Person("Samantha", "Jones") // note missing age and ssn
          val anotherFred = Person(last = "Jones", first = "Fred", ssn = "111-22-3333") // note the order can change, and missing age
          val fredCopy = anotherFred.copy(age = 23)

          fred.first should be("Fred")
          fred.last should be("Jones")
          fred.age should be(23)
          fred.ssn should be("111-22-3333")

          samantha.first should be("Samantha")
          samantha.last should be("Jones")
          samantha.age should be(0)
          samantha.ssn should be("")

          anotherFred.first should be("Fred")
          anotherFred.last should be("Jones")
          anotherFred.age should be(0)
          anotherFred.ssn should be("111-22-3333")

          (fred == fredCopy) should be(true)
     }

     koan("Case classes can be disassembled to their constituent parts as a tuple") {
          val p1 = Person("Fred", "Jones", 23, "111-22-3333")

          val parts = Person.unapply(p1).get // this seems weird, but it's critical to other features of Scala

          parts._1 should be("Fred")
          parts._2 should be("Jones")
          parts._3 should be(23)
          parts._4 should be("111-22-3333")
     }



     koan("The apply method can be defined in multiple ways."){

          object Human {
               def apply() = new Human("<no name>", 0)
               def apply(name: String) = new Human(name, 0)
          }

          case class Human(var name: String, var age: Int)

          val a = Human()                         // uses object Human
          val b = Human("Pam")                    // uses object Human
          val c = Human("William Shatner", 82)    // uses case class Human

          a.name should be("<no name>")
          a.age should be(0)

          b.name should be("Pam")
          b.age should be(0)

          c.name should be ("William Shatner")
          c.age should be(82)
          c.name = "Leonard Nimoy"
          c.age = 92
          c.name should be("Leonard Nimoy")
          c.age should be(92)
     }


     koan("Case classes can be used in pattern matching"){

          case class Person(name: String, relation: String)

          val hannah = Person("Hannah", "niece")

          val sentence = hannah match {
               case Person(n, r) => s"$n is my $r"
          }

          sentence should be("Hannah is my niece")
     }
}


// NOTE: difference between classes and case classes
/**
  * case classes provide:
  * * automatic argument access
  * * good toString method
  * * automatic apply() method (no need to use "new" keyword )
  * * automatic unapply() method.
  */