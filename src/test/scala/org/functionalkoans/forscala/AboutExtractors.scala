package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSuite


class AboutExtractors extends KoanSuite {

     koan("When you create a case class, it automatically can be used with " +
          "pattern matching since it has an extractor") {

          case class Employee(firstName: String, lastName: String)

          // @todo: ---- get list of all other scala types that have an extractor.

          val rob = new Employee("Robin", "Williams")
          val result = rob match {
               case Employee("Robin", _) => "Where's Batman?"
               case _ => "No Batman Joke For You"
          }

          result should be("Where's Batman?")
     }


     koan(
          """What's an extractor? In Scala it's a method in any `object` called `unapply`, and that method
            | is used to disassemble the object given by returning a tuple wrapped in an option. Extractors can be used
            | to assign values.""") {

          class Car(val make: String, val model: String, val year: Short, val topSpeed: Short)


          object ChopShop {
               def unapply(x: Car) = Some(x.make, x.model, x.year, x.topSpeed)
          }

          // @todo: ---- Understand how the argument 'new Car(...)' is passed to the extractor unapply
          // @todo: ---- in ChopShop and how the a,b,c,d get assigned the Car properties.
          val ChopShop(a, b, c, d) = new Car("Chevy", "Camaro", 1978, 120)

          a should be("Chevy")
          b should be("Camaro")
          c should be(1978)
          d should be(120)
     }

     koan( """Of course an extractor can be used in pattern matching...""") {
          class Car(val make: String, val model: String, val year: Short, val topSpeed: Short)

          object ChopShop {
               def unapply(x: Car) = Some(x.make, x.model, x.year, x.topSpeed)
          }

          val x = new Car("Chevy", "Camaro", 1978, 120) match {
               case ChopShop(s, t, u, v) => (s, t)
               case _ => ("Ford", "Edsel")
          }

          x._1 should be("Chevy")
          x._2 should be("Camaro")
     }

     koan(
          """Since we aren't really using u and v in the previous pattern matching with can replace them with _.""") {
          class Car(val make: String, val model: String, val year: Short, val topSpeed: Short)

          object ChopShop {
               def unapply(x: Car): Option[(String, String, Short, Short)] =
                    Some(x.make, x.model, x.year, x.topSpeed)
          }


          val x = new Car("Chevy", "Camaro", 1978, 120) match {
               case ChopShop(s, t, _, _) => (s, t)
               case _ => ("Ford", "Edsel")
          }

          x._1 should be("Chevy")
          x._2 should be("Camaro")
     }

     koan("As long as the method signatures aren't the same, " +
          "you can have as many unapply methods as you want") {
          class Car(val make: String, val model: String, val year: Short, val topSpeed: Short)
          class Employee(val firstName: String, val middleName: Option[String], val lastName: String)

          object Tokenizer {
               def unapply(x: Car) = Some(x.make, x.model, x.year, x.topSpeed)

               def unapply(x: Employee) = Some(x.firstName, x.lastName)
          }

          val result = new Employee("Kurt", None, "Vonnegut") match {
               case Tokenizer(c, d) => "c: %s, d: %s".format(c, d)
               case _ => "Not found"
          }

          result should be("c: Kurt, d: Vonnegut")
     }

     koan(
          """An extractor can be any stable object, including instantiated classes with an unapply method.""") {



          // @todo: ---- understand if there are other ways in which extractors can be represented (declared in class, in
          // @todo: ---- companion object...)
          // @todo: ---- USES SO FAR:
          // @todo:       (1) case class has default unapply method so is an extractor
          // @todo:       (2) class with different-named (or same-named) companion object
          // @todo:       (3) class with declared unapply method

          // the extractor becomes the class if the class has unapply method
          class Car(val make: String, val model: String, val year: Short, val topSpeed: Short) {
               def unapply(x: Car) = Some(x.make, x.model)
          }

          val camaro = new Car("Chevy", "Camaro", 1978, 122)

          // see? camaro class becomes extractor, no companion object is used.
          val result = camaro match {
               case camaro(make, model) => "make: %s, model: %s".format(make, model)
               case _ => "unknown"
          }

          result should be("make: Chevy, model: Camaro")
     }

     koan(
          """What is typical is to create a custom extractor in the companion object of the class.
            | In this koan, we use it as an assignment""") {

          class Employee(val firstName: String,
                         val middleName: Option[String],
                         val lastName: String)/*{
      println(middleName) // printed as None
    }*/


          object Employee {
               //factory methods, extractors, apply
               //Extractor: Create tokens that represent your object
               def unapply(x: Employee) =
                    Some(x.lastName, x.middleName, x.firstName)
          }

          val singri = new Employee("Singri", None, "Keerthi")

          val Employee(a, b, c) = singri

          a should be("Keerthi")
          b should be(None)
          c should be("Singri")
     }

     koan("In this koan we use the unapply for pattern matching employee objects") {

          class Employee(val firstName: String,
                         val middleName: Option[String],
                         val lastName: String)

          object Employee {
               //factory methods, extractors, apply
               //Extractor: Create tokens that represent your object
               def unapply(x: Employee) =
                    Some(x.lastName, x.middleName, x.firstName)
          }

          val singri = new Employee("Singri", None, "Keerthi")

          val result = singri match {
               case Employee("Singri", None, x) => "Yay, Singri %s! with no middle name!".format(x)
               case Employee("Singri", Some(x), _) => "Yay, Singri with a middle name of %s".format(x)
               case _ => "I don't care, going on break"
          }

          result should be("I don't care, going on break")
     }


     koan("It is possible for the unapply() and apply() methods to be inverses of each other"){

          class Fraction(val num: Int, val denom: Int) {
               def multiply(other: Fraction) = new Fraction(num * other.num, denom * other.denom)
               def ==(other: Fraction) = num == other.num && denom == other.denom
          }

          object Fraction {
               def apply(num: Int, denom: Int): Fraction = new Fraction(num, denom)
               def unapply(f: Fraction): Option[(Int, Int)] = Some(f.num, f.denom)
          }


          val frac1 = Fraction.apply(2, 3)
          val frac2 = Fraction(2, 3)
          val frac3 = new Fraction(2, 3)

          frac1 == frac2 should be(true)
          frac1 == frac3 should be(true)
          frac2 == frac3 should be(true)

          val frac4 = Fraction(5, 8)
          val result = frac4 multiply frac2
          result == Fraction(10, 24) should be(true)
     }


     //note todo help  don't know what purpose of @ is and how this is all supposed to work in
     // the pattern matching

     /*koan("An extractor can just test its input without extracting any value. Here, " +
          "the unapply method tests the input and returns Boolean"){

          object Name {
               def unapply(input: String): Option[(String, String)] = {
                    val pos = input.indexOf(" ")
                    if (pos == -1) None
                    else Some((input.substring(0, pos), input.substring(pos + 1)))
               }
          }

          class IsCompound(input: String)

          object IsCompound {
               def unapply(input: String) = input.contains(" ")
          }
/*
          val name = "Peter van der Linden"
          val result = name match {
               case Name(first, last @ IsCompound) => "last name is compound"
               case Name(first, last) => "last name is not compound"
          }*/
     }*/


     koan("The unapplySeq method extracts an arbitrary sequence of values." +
          "It returns Option[Seq[A]] where A is the type of the extract values"){

          object Name {
               def unapplySeq(input: String): Option[Seq[String]] ={
                    if(input.trim == "") None
                    else Some(input.trim.split(" "))
               }
          }

          val name = "Peter van der Linden"
          val numberOfParts = name match {
               case Name(first, last) => "Two parts"
               case Name(first, middle, last) => "Three parts"
               case _ => "More than three parts"
          }

          numberOfParts should be("More than three parts")
     }
}
