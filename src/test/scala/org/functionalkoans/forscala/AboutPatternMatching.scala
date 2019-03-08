package org.functionalkoans.forscala

import java.io.FileNotFoundException

import support.KoanSuite


class AboutPatternMatching extends KoanSuite {

     koan("Pattern matching returns something") {

          val stuff = "blue"

          val myStuff = stuff match {
               case "red" => 1
               case "blue" => 2
               case "green" => 3
               case _ => 0
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

     koan("Pattern matching can match tuples") {


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

          def separate(list: List[Int]): Any = list match {
               case x :: y :: xs => xs
               case _ => 0
          }

          separate(List(1,2,3)) should be( List(3) )
          separate(List(1,2,3,4,5)) should be(List(3,4,5))
          separate(List(1)) should be(0)
          separate(List(1,2)) should be(List()) //the xs = Nil


          def separateNil(list: List[Int]): Int = list match {
               case x :: y :: Nil => y
               case _ => 0
          }

          separateNil(List(1)) should be(0)
          separateNil(List(1,2)) should be(2)
          separateNil(List(1,2,3)) should be(0)
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

     koan("Pattern matches can use the `switch` annotation to force the compiler to verify" +
          "that all the cases have been compiled the same way (all either tableswitch or lookupswitch). " +
          "Warning is thrown if the cases are not either of these conditions."){

          import scala.annotation.switch

          val monthToMatch = 8
          val five = "May"

          val month = (monthToMatch: @switch) match {
               case 1 => "January"
               case 2 => "February"
               case 3 => "March"
               case 4 => "April"
               //case five => "January" //uncomment to get the warning
               case 5 => "May"
               case 6 => "June"
               case 7 => "July"
               case 8 => "August"
               case 9 => "September"
               case 10 => "October"
               case 11 => "November"
               case 12 => "December"
               case _ => "Invalid month"
          }

          month should be("August")
     }


     koan("Pattern matches can be used to match multiple conditions " +
          "with one case statement, via the pipe | operator"){

          val sweet = "honey"

          val typeOfSweet: String = sweet match {
               case "fudge" | "bun" | "icing" | "caramel" => "bakery sweet"
               case "chocolate" | "honey" | "sugarcane" | "jam" => "natural sweet"
          }

          typeOfSweet should be("natural sweet")
     }

     koan("The pipe operator works with case objects as well"){

          trait Command
          case object Start extends Command
          case object Go extends Command
          case object Begin extends Command
          case object AnyDayNow extends Command
          case object Stop extends Command
          case object Halt extends Command

          def start() = "We are starting"
          def stop() = "We are stopping"

          def executeCommand(cmd: Command) = cmd match {
               case Start | Go | Begin | AnyDayNow => start()
               case Stop | Halt => stop()
          }

          executeCommand(AnyDayNow) should be(start())
          executeCommand(Halt) should be(stop())
          executeCommand(Go) should be (start())
     }


     koan("Pattern matches work with Option types"){

          trait PopGoesTheWeasel
          abstract class Animal
          trait InanimateObject
          case object Weasel extends Animal with PopGoesTheWeasel
          case object BabyWeasel extends Animal with PopGoesTheWeasel
          case object NotPoppin extends PopGoesTheWeasel
          case object EmptyBox extends PopGoesTheWeasel with InanimateObject

          def pop(p: PopGoesTheWeasel): Option[String] = p match {
               case Weasel => Some("POP!")
               case BabyWeasel => Some("peep")
               case NotPoppin | EmptyBox => None

          }

          pop(Weasel) should be(Some("POP!"))
          pop(EmptyBox) should be(None)

          /// ======================

          //Example where we match on the option type

          def gatherWeasels(list: List[Option[PopGoesTheWeasel]]) = {

               val newList = list.map (elem => elem match {
                    case Some(a) => a match {
                         case _:Animal => List(a)
                         case _ => List()
                    }
                    case None => List()
               })
               newList.flatten
          }

          val zoo = List(Some(Weasel), Some(NotPoppin), Some(EmptyBox), None, Some(BabyWeasel), None)

          gatherWeasels(zoo) should be(List(Weasel, BabyWeasel))
     }


     koan("Case statements can have guards") {

          val num = 2

          val optionNum = num match {
               case x if x == 1 => Some(1)
               case x if (x == 2 || x == 3) => Some(x)
               case _ => None
          }

          optionNum should be(Some(2))
     }

     koan("Case statements with case classes can also have guards"){

          object COLOR extends Enumeration {
               val blue = Value
               val yellow = Value
               val red = Value
               val brown = Value
               val pink = Value
               val gold = Value
               val grey = Value
               val green = Value
               val orange = Value
               val purple = Value
               val white = Value

               val other = Value
          }

          trait Bird
          trait BlueBird extends Bird
          trait YellowBird extends Bird
          case class BlueJay(featherColors: COLOR.Value*) extends BlueBird
          case object Canary extends YellowBird
          case object Robin extends Bird
          case object Peacock extends BlueBird
          case object Nightingale extends Bird


          def getMainColor(bird: Bird) = bird match {
               case b:BlueJay if b.featherColors.containsSlice(List(COLOR.blue, COLOR.red)) => COLOR.purple
               case b:BlueJay if b.featherColors.contains(COLOR.blue) => COLOR.blue
               case Canary => COLOR.yellow
               case Robin => COLOR.red
               case Nightingale => COLOR.brown
          }

          getMainColor(Canary) should be(COLOR.yellow)
          getMainColor(BlueJay(COLOR.blue, COLOR.green)) should be(COLOR.blue)
          getMainColor(BlueJay(COLOR.red, COLOR.yellow, COLOR.blue)) should be(COLOR.blue)

     }


     koan("Pattern matches can be used in an exception context - with try/catch/finally"){

          def openAndReadFile(filename: String) = throw new FileNotFoundException(s"$filename was not found")

          val wasFound = try {
               openAndReadFile("poppy")
          } catch {
               case e: FileNotFoundException => false
          }

          wasFound should be(false)
          /*trait WheeledObject
          trait FourWheeledObject extends WheeledObject
          case class Car(color: String) extends FourWheeledObject
          case class Motorcycle(color: String) extends WheeledObject
          case class Buggy(color: String) extends WheeledObject
          case object Bicycle extends WheeledObject
          case object Tricycle extends WheeledObject
          case object Truck extends FourWheeledObject

          case class TwoWheelException(msg: String) extends Exception
          case class ThreeWheelException(msg: String) extends Exception
          case class UnknownNumWheelsException(msg: String) extends Exception


          def createCar(wheeledObject: WheeledObject): Car = wheeledObject match {
               case c: Car => c
               case Truck => Car("white")
               case Bicycle => throw TwoWheelException("This machine has two wheels, not four")
               case Tricycle => throw ThreeWheelException("this machine has three wheels, cannot make a car")
               case _ => throw UnknownNumWheelsException("unknown object")
          }

          def suggestCar(wheeledObject: WheeledObject): Option[WheeledObject] = {

               try {
                    Some(createCar(wheeledObject))
               } catch {
                    case c: Car => Some(c)
                    case e: TwoWheelException => Some(Motorcycle("black"))
                    case e: ThreeWheelException => Some(Buggy("red"))
                    case _ : UnknownNumWheelsException => None
               }
          }

          makeCar(Car("silver")) should be (Some(Car("silver")))
          makeCar(Bicycle) should be(Some(Motorcycle("black")))
          makeCar(Truck) should be(None)*/
     }

}