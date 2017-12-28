package org.functionalkoans.forscala

import support.KoanSuite


class AboutPatternMatching extends KoanSuite {

     koan("Pattern matching returns something") {

          val stuff = "blue"

          val myStuff = stuff match {
               case "red" => println("RED"); 1
               case "blue" => println("BLUE"); 2
               case "green" => println("GREEN"); 3
               case _ => println(stuff); 0 //case _ will trigger if all other cases fail.
          }

          myStuff should be(2)

     }

     koan("Pattern matching can return complex somethings") {
          val stuff = "blue"

          val myStuff = stuff match {
               case "red" => (255, 0, 0)
               case "green" => (0, 255, 0)
               case "blue" => (0, 0, 255)
               case _ => println(stuff); 0
          }

          myStuff should be(0, 0, 255)

     }

     koan("Pattern matching can match complex expressions") {


          def goldilocks(expr: Any) = expr match {
               case ("porridge", "Papa") => "Papa eating porridge"
               case ("porridge", "Mama") => "Mama eating porridge"
               case ("porridge", "Baby") => "Baby eating porridge"
               case _ => "what?"
          }

          goldilocks(("porridge", "Mama")) should be("Mama eating porridge")

     }

     koan("Pattern matching can wildcard parts of expressions") {

          def goldilocks(expr: Any) = expr match {
               case ("porridge", _) => "eating"
               case ("chair", "Mama") => "sitting"
               case ("bed", "Baby") => "sleeping"
               case _ => "what?"
          }

          goldilocks(("porridge", "Papa")) should be("eating")
          goldilocks(("chair", "Mama")) should be("sitting")

     }

     koan("Pattern matching can substitute parts of expressions") {

          def goldilocks(expr: Any) = expr match {
               case ("porridge", bear) => bear + " said someone's been eating my porridge"
               case ("chair", bear) => bear + " said someone's been sitting in my chair"
               case ("bed", bear) => bear + " said someone's been sleeping in my bed"
               case _ => "what?"
          }

          goldilocks(("porridge", "Papa")) should be("Papa said someone's been eating my porridge")
          goldilocks(("chair", "Mama")) should be("Mama said someone's been sitting in my chair")
     }


     koan("Pattern matching can done on regular expression groups") {
          val EatingRegularExpression = """Eating Alert: bear=([^,]+),\s+source=(.+)""".r //.r turns a String to a regular expression
          val SittingRegularExpression = """Sitting Alert: bear=([^,]+),\s+source=(.+)""".r
          val SleepingRegularExpression = """Sleeping Alert: bear=([^,]+),\s+source=(.+)""".r

          def goldilocks(expr: String) = expr match {
               case (EatingRegularExpression(bear, source)) => "%s said someone's been eating my %s".format(bear, source)
               case (SittingRegularExpression(bear, source)) => "%s said someone's been sitting on my %s".format(bear, source)
               case (SleepingRegularExpression(bear, source)) => "%s said someone's been sleeping in my %s".format(bear, source)
               case _ => "what?"
          }

          goldilocks("Eating Alert: bear=Papa, source=porridge") should be("Papa said someone's been eating my porridge")
          goldilocks("Sitting Alert: bear=Mama, source=chair") should be("Mama said someone's been sitting on my chair")
     }

     koan( """A backquote can be used to refer to a stable variable in scope to create a case statement.
             | This prevents what is called \'Variable Shadowing\'""") {
          val foodItem = "porridge"

          def goldilocks(expr: Any) = expr match {
               case (`foodItem`, _) => "eating"
               case ("chair", "Mama") => "sitting"
               case ("bed", "Baby") => "sleeping"
               case _ => "what?"
          }

          goldilocks(("porridge", "Papa")) should be("eating")
          goldilocks(("chair", "Mama")) should be("sitting")
          goldilocks(("porridge", "Cousin")) should be("eating")
          goldilocks(("beer", "Cousin")) should be("what?")
     }

     koan("A backquote can be used to refer to a method parameter as a stable variable to create a case statement.") {

          def patternEquals(i: Int, j: Int) = j match {
               case `i` => true
               case _ => false
          }

          // @todo: ---- understand better since i and j are not same variable names, so how does it check if equal?

          patternEquals(3, 3) should be(true)
          patternEquals(7, 9) should be(false)
          patternEquals(9, 9) should be(true)
     }

     koan(
          """To pattern match against a List, the list can be broken out into parts,
            | in this case the head (x) and the tail(xs). Since the case doesn't terminate in Nil,
            | xs is interpreted as the rest of the list""") {
          val secondElement = List(1, 2, 3) match {
               case x :: xs => xs.head
               case _ => 0
          }

          secondElement should be(2)
     }

     koan(
          """To obtain the second you can expand on the pattern. Where x is the first element, y
            | is the second element, and xs is the rest. """.stripMargin) {
          val secondElement = List(1,2,3) match {
               case x :: y :: xs => xs
               case _ => 0
          }

          secondElement should be( List(3) )
     }

     koan(
          """Same koan as above, but we are pattern matching of a list with only one item!""".stripMargin) {
          val secondElement = List(1) match {
               case x :: y :: xs => xs
               case _ => 0
          }

          // @todo: ---- does it go in case _  ?
          secondElement should be(0)
     }

     koan(
          """To pattern match against List, you can also establish a pattern match
            | if you know the exact number of elements in a List""") {

          val r = List(1, 2, 3) match {
               case x :: y :: Nil => y
               case _ => 0
          }


          // @todo: ---- is this because second element is not Nil?
          r should be(0)
     }

     koan("unapplySeq is automatically used to extract array contents"){
          val arr = (0 to 10).toArray

          val result = arr match {
               case Array(0) => "0"
               case Array(x, y) => x + " " + y
               case Array(0, xs@_*) => "0 and " + xs
               case _ => "Something else"
          }
          result should be("0 and Vector(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)")
     }

     koan("Pattern matching against types is better than using asInstanceOf"){
          case class Passenger(first:String, last:String)
          case class Train(travelers:Vector[Passenger], line:String)
          case class Plane(passengers:Vector[Passenger], plane:String)
          case class Bus(passengers:Vector[Passenger], capacity:Int)

          def travel(transport:Any):String = {
               transport match {
                    case Train(travelers, line) => "Train line " + line + " " + travelers
                    case Plane(travelers, equipment) => "Plane " + equipment + " " + travelers
                    case Bus(travelers, capacity) => "Bus size " + capacity + " " + travelers
                    case p:Passenger => s"${p.first} is walking along"
                    case thingamajig => thingamajig + " is in limbo!"
               }
          }
          travel(Passenger("Jolie", "Swifting")) should be("Jolie is walking along")
     }

     //help todo how to test?
     /*koan("Using the type explicitly is different than pattern matching against the object itself"){
          case class Passenger(first:String, last:String)
          case class Train(travelers:Vector[Passenger], line:String)
          case class Plane(passengers:Vector[Passenger], plane:String)
          case class Bus(passengers:Vector[Passenger], capacity:Int)

          var coll = ArrayBuffer[String]()

          def travel(transport:Any):String = {
               transport match {
                    case p:Passenger => coll += "here"
                    case thingamajig => thingamajig + " is in limbo!"
               }
               transport match {
                    case Passenger => coll += "there"
               }
               coll.mkString(", ")
          }
          travel(Passenger("Jolie", "Swifting")) should be("here, there")
     }*/

     koan("You can make case objects for prettier pattern-matching"){

          abstract class Amount
          case class Dollar(value: Double) extends Amount
          case class Currency(value: Double, unit: String) extends Amount
          case object Nothing extends Amount

          def moneyMatcher(amt: Any): String ={
               amt match {
                    case Dollar(v) => "$" + v
                    case Currency(_, u) => "Oh noes, I got " + u
                    case Nothing => ""
                    case _ => "error"
               }
          }

          moneyMatcher(Dollar(15.49)) should be("$15.49")
          moneyMatcher(Currency(10.78, "euro")) should be("Oh noes, I got euro")
          moneyMatcher(Nothing) should be("")
          moneyMatcher("hi") should be("error")
     }


     koan("The sealed keyword applied to classes allows the compiler to check " +
          "the pattern match cases for completeness. Also, all subclasses of the " +
          "sealed class must be declared in the same file"){

          sealed abstract class TrafficLightColor
          case object Red extends TrafficLightColor
          case object Yellow extends TrafficLightColor
          case object Green extends TrafficLightColor

          def colorMatcher(color: TrafficLightColor): String ={
               color match {
                    case Red => "stop"
                    case Yellow => "slow down"
                    case Green => "go"
                    //case _ => "not defined" //todo help don't need this then? if sealed does its job?
               }
          }
          colorMatcher(Green) should be ("go")
     }

}