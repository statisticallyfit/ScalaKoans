package org.functionalkoans.forscala

import java.io.{IOException, PrintWriter, PrintStream}
import javax.swing.JFrame

import support.KoanSuite



class AboutTraits extends KoanSuite {
     koan("A class uses the extends keyword to mixin a trait if it is the only relationship the class inherits") {
          case class Event(name: String)

          trait EventListener {
               def listen(event: Event): String
          }


          class MyListener extends EventListener {
               def listen(event: Event): String = {
                    event match {
                         case Event("Moose Stampede") => "An unfortunate moose stampede occurred"
                         case _ => "Nothing of importance occurred"
                    }
               }
          }

          val evt = Event("Moose Stampede")
          val myListener = new MyListener
          myListener.listen(evt) should be ("An unfortunate moose stampede occurred")
     }

     koan("A class can only \'extend\' from one class or trait, any subsequent extension should use the keyword \'with\'") {

          case class Event(name: String)

          trait EventListener {
               def listen(event: Event): String
          }

          class OurListener

          class MyListener extends OurListener with EventListener {
               def listen(event: Event) : String = {
                    event match {
                         case Event("Woodchuck Stampede") => "An unfortunate woodchuck stampede occurred"
                         case _ => "Nothing of importance occurred"
                    }
               }
          }

          val evt = Event("Woodchuck Stampede")
          val myListener = new MyListener
          myListener.listen(evt) should be ("An unfortunate woodchuck stampede occurred")
     }

     koan("Traits are polymorphic. Any type can be referred to by another type if related by extension") {
          case class Event(name: String)

          trait EventListener {
               def listen(event: Event): String
          }

          class MyListener extends EventListener {
               def listen(event: Event) : String = {
                    event match {
                         case Event("Moose Stampede") => "An unfortunate moose stampede occurred"
                         case _ => "Nothing of importance occurred"
                    }
               }
          }

          val myListener = new MyListener

          myListener.isInstanceOf[MyListener] should be(true)
          myListener.isInstanceOf[EventListener] should be(true)
          myListener.isInstanceOf[Any] should be(true)
          myListener.isInstanceOf[AnyRef] should be(true)
     }

     koan("Traits can have concrete implementations that can be mixed into concrete classes with it's own state") {
          trait Logging {
               var logCache = List[String]()

               def log(value: String) = {
                    logCache = logCache :+ value    // @todo: ---- understand what ":+" means, why not just use "+" ?
                    println(value)
               }
          }

          class Welder extends Logging {
               def weld() {
                    log("welding pipe")
               }
          }

          class Baker extends Logging {
               def bake() {
                    log("baking cake")
               }
          }

          val welder = new Welder
          welder.weld()


          val baker = new Baker
          baker.bake()

          welder.logCache.size should be(1)  // does size just mean amount of separate strings?
          baker.logCache.size should be(1)
     }

     koan("""Traits can also be mixed during instantiation after the fact!
            | This is useful if you only want to mixin per instance and not per class""") {

          trait Logging {
               var logCache = List[String]()

               def log(value: String) = {
                    logCache = logCache :+ value
               }

               // @todo: ---- why have two log methods?
               def log = logCache
          }

          class Scientist (val firstName:String, val lastName:String) {
               def discover(item:String) = s"I have discovered $item!"
               def invent(item:String) = s"I have invented $item!"
          }

          val einstein = new Scientist("Albert",  "Einstein") with Logging  //mixin traits during instantiation!
          einstein.discover("Relativity!")
          einstein.log("Although it is utmost of importance that this does not fall into the wrong hands")

          einstein.log.size should be (1)
     }

     //Credit for the next set koans: http://www.artima.com/scalazine/articles/stackable_trait_pattern.html

     abstract class IntQueue {
          def get(): Int
          def put(x: Int)
     }

     import scala.collection.mutable.ArrayBuffer

     class BasicIntQueue extends IntQueue {
          private val buf = new ArrayBuffer[Int]
          def get() = buf.remove(0)
          def put(x: Int) { buf += x }
     }


     koan("Traits are stackable and can change the behavior of methods that the traits are stacked upon") {
          trait Doubling extends IntQueue {
               abstract override def put(x: Int) { super.put(2 * x) } //abstract override is necessary to stack traits
          }

          class MyQueue extends BasicIntQueue with Doubling

          val myQueue = new MyQueue
          myQueue.put(3)
          myQueue.put(10)
          myQueue.get() should be (6)
          myQueue.get() should be (20)
     }



     koan("Using super invokes the next trait in a ladder traits which are stacked" +
          "right when creating the object or class. Also, a class can have just one " +
          "superclass but any number of traits."){

          import java.util.Date


          trait Logged {
               //var message: String = ""
               def log(msg: String) { /*message += msg*/ }
          }

          trait ConsoleLogger extends Logged {
               override def log(msg: String) { println(msg) }
          }

          trait TimestampLogger extends Logged {
               override def log(msg: String) {
                    super.log(new Date() + " " + msg)
               }
          }

          //note truncates overly long messages
          trait ShortLogger extends Logged {
               val maxLength = 15 // see section 10.8 on fields in traits
               override def log(msg: String): Unit = {
                    super.log(
                         if(msg.length <= maxLength) msg
                         else msg.substring(0, maxLength - 3) + "..."
                    )
               }
          }

          class Account {
               protected var balance = 0.0
          }

          class SavingsAccount extends Account with Logged {
               def withdraw(amount: Double) {
                    if (amount > balance) log("Insufficient funds")
                    else balance -= amount
               }
               // More methods ...
          }


          // note adds insufficient funds then truncates then date, resulting in longer message
          val acct1 = new SavingsAccount with ConsoleLogger with TimestampLogger with ShortLogger
          //note: adds insufficient funds then date in front then truncates, leaving just a bit of date
          val acct2 = new SavingsAccount with ConsoleLogger with ShortLogger with TimestampLogger

          acct1.withdraw(100) // Sat Aug 27 08:12:24 UTC 2016 Insufficient...
          acct2.withdraw(100) // Sat Aug 27 0...
          // note help i want to be able to test what the message is - where do i put message
          // variable and how do i update it?
          //acct1.message should be("")
     }




     koan("Class inheriting multiple traits which implement the same method can choose which of" +
          "the trait's method to call using the super[traitName].methodName syntax") {

          trait Human {
               def hello = "the Human trait"
          }

          trait Mother extends Human {
               override def hello = "Mother's hello"
          }

          trait Father extends Human {
               override def hello = "Father's hello"
          }

          class Child extends Human with Mother with Father {
               def printSuper = super.hello
               def printMother = super[Mother].hello
               def printFather = super[Father].hello
               def printHuman = super[Human].hello
          }

          val c = new Child
          c.printSuper should be("Father's hello")
          c.printMother should be("Mother's hello")
          c.printFather should be("Father's hello")
          c.printHuman should be("the Human trait")
     }




     koan("However, you can't continue to reach up through the parent class hierarchy unless" +
          "you directly extend the target class or trait using the extends or with keywords"){

          trait Animal {
               def walk = println("Animal is walking")
          }

          class FourLeggedAnimal extends Animal {
               override def walk = println("I'm walking on all fours")
          }

          class Dog extends FourLeggedAnimal with Animal {
               def walkThenRun: Unit = {
                    super.walk                    // works
                    super[FourLeggedAnimal].walk  // works
                    super[Animal].walk            // does not compile if we don't extend trait Animal
               }
          }

     }


     koan("Concrete fields in traits are fields that are assigned values or are implemented"){

          trait Bird {
               val isWinged: Boolean = true
          }

          class Penguin extends Bird
          class Hawk extends Bird
          class Flamingo extends Bird

          new Penguin().isWinged should be(true)
          new Hawk().isWinged should be(true)
          new Flamingo().isWinged should be(true)
     }



     koan("Abstract fields in traits must be overridden in the concrete subclass"){

          trait Insect {
               val isWinged: Boolean
          }

          // note isWinged val is overridden but no "override" keyword was necessary
          // only needed when the field is a method not var or val.
          class Butterfly extends Insect { val isWinged = true }
          class Ladybug extends Insect { val isWinged = true }
          class Centipede extends Insect { val isWinged = false }

          new Butterfly().isWinged should be(true)
          new Ladybug().isWinged should be(true)
          new Centipede().isWinged should be(false)
     }



     // todo help part 10.11 in scala impatient book - don't understand why the first
     //instantiation wouldn't work.

     /*koan("Trait constructors are parameterless; initializing trait fields can be done" +
          "using the early definition feature"){

          trait Logger {
               def log(msg: String): String
          }

          trait FileLogger extends Logger {
               val filename: String
               //val out = new PrintStream(filename)
               override def log(msg: String) = filename /*out.println(msg); out.flush()*/
          }

          class Account {
               protected var balance = 0.0
          }

          class SavingsAccount extends Account with Logger {
               def withdraw(amount: Double) {
                    if (amount > balance) log("Insufficient funds")
                    else balance -= amount
               }

               override def log(msg: String): String = msg
          }

          // note the below instantiation doesn't work because filename
//          val acct = new SavingsAccount with FileLogger {
//               val filename = "myapp.log"
//          }

          // note this works: early definition feature. We pass in some message to log() and
          // then the filename gets printed, sign that it was passed into FileLogger
          val acct = new { val filename = "myapp.log" } with SavingsAccount with FileLogger
          acct.log("a message") should be("myapp.log")
     }*/



     koan("Traits can take arguments from the class right from the class's primary constructor"){

          trait Aerobic {
               val age: Int
               def minAerobic = 0.5 * (220 - age)
               def isAerobic(heartRate: Int) = {
                    heartRate >= minAerobic
               }
          }

          trait Activity {
               val action:String
               def go:String
          }

          class Person(val age:Int)

          class Exerciser(age:Int, val action:String = "Running", val go:String = "Run!")
               extends Person(age)
                    with Activity
                    with Aerobic

          val bob = new Exerciser(44)
          bob.isAerobic(180) should be (true)
          bob.isAerobic(80) should be (false)
          bob.minAerobic should be (88.0)
     }


     koan("Just like other traits, stackable traits can be mixed after the fact") {
          trait Doubling extends IntQueue {
               abstract override def put(x: Int) { super.put(2 * x) } //abstract override is necessary to stack traits
          }

          val myQueue = new BasicIntQueue with Doubling //mixin during instantiation

          myQueue.put(40)
          myQueue.get() should be (80)
     }

     koan(
          """More traits can be stacked one atop another, make sure that all overrides
            | are labelled, abstract override.  The order of the mixins are important.
            | Traits on the right take effect first.""") {

          trait Doubling extends IntQueue {
               abstract override def put(x: Int) { super.put(2 * x) } //abstract override is necessary to stack traits
          }

          trait Incrementing extends IntQueue {
               abstract override def put(x: Int) { super.put(x + 1) }
          }

          val myQueue = new BasicIntQueue with Doubling with Incrementing //mixin during instantiation
          myQueue put 4
          myQueue put 3

          myQueue.get should be (10)
          myQueue.get should be (8)
     }


     koan(
          """Same koans as before except that we swapped the order of the traits""") {

          trait Doubling extends IntQueue {
               abstract override def put(x: Int) { super.put(2 * x) } //abstract override is necessary to stack traits
          }

          trait Incrementing extends IntQueue {
               abstract override def put(x: Int) { super.put(x + 1) }
          }

          val myQueue = new BasicIntQueue with Incrementing with Doubling //mixin during instantiation
          myQueue put 4
          myQueue put 3
          myQueue.get should be (9)
          myQueue.get should be (7)
     }


     koan(
          """Using three traits to enhance the IntQueue: Doubling, Incrementing, and Filtering!""") {

          trait Doubling extends IntQueue {
               abstract override def put(x: Int) { super.put(2 * x) } //abstract override is necessary to stack traits
          }

          trait Incrementing extends IntQueue {
               abstract override def put(x: Int) { super.put(x + 1) }
          }

          trait Filtering extends IntQueue {
               abstract override def put(x: Int) {
                    if (x >= 0) super.put(x)
               }
          }

          val myQueue = new BasicIntQueue with Incrementing with Doubling with Filtering //mixin during instantiation
          myQueue put 4
          myQueue put -1
          myQueue put 3
          myQueue.get should be (9)
          myQueue.get should be (7)
     }

     koan("Traits are instantiated before a the mixed-in class instantiation") {
          var sb = List[String]()

          trait T1 {
               sb = sb :+ "Instantiated T1"
          }

          class C1 extends T1 {
               sb = sb :+ "Instantiated C1"
          }

          sb = sb :+ "Creating C1"
          new C1
          sb = sb :+ "Created C1"

          sb.mkString(";") should be("Creating C1;Instantiated T1;Instantiated C1;Created C1")
     }



     koan("Traits are instantiated before a classes instantiation from left to right") {
          var sb = List[String]()

          trait T1 {
               sb = sb :+ "Instantiated T1"
          }

          trait T2 {
               sb = sb :+ "Instantiated T2"
          }

          class C1 extends T1 with T2 {
               sb = sb :+ "Instantiated C1"
          }

          sb = sb :+ "Creating C1"
          new C1
          sb = sb :+ "Created C1"

          sb.mkString(";") should be("Creating C1;Instantiated T1;Instantiated T2;Instantiated C1;Created C1")
     }




     koan("Subclass Koala is instantiated last, after the superclass and the rest " +
          "of the traits which are instantiated in left-to-right order"){

          var sb = List[String]()

          trait Vertebrate {
               sb = sb :+ "Instantiated Vertebrate"
          }

          trait BigEared extends Vertebrate {
               sb = sb :+ "Instantiated BigEared"
          }

          trait Furry extends Vertebrate {
               sb = sb :+ "Instantiated Furry"
          }

          class Animal {
               sb = sb :+ "Instantiated Animal"
          }

          class Koala extends Animal with Furry with BigEared  {
               sb = sb :+ "Instantiated Koala"
          }

          sb = sb :+ "Creating Koala"
          new Koala
          sb = sb :+ "Created Koala"

          sb.mkString(";") should be("Creating Koala;Instantiated Animal;Instantiated Vertebrate;" +
               "Instantiated Furry;Instantiated BigEared;Instantiated Koala;Created Koala")
     }




     koan("Instantiations are tracked internally and will not allow a duplicate instantiation. " +
          "Note T1 extends T2, and C1 also extends T2, but T2 is only instantiated once.") {

          var sb = List[String]()

          trait T1 extends T2 {  // Notice: T1 extends T2
               sb = sb :+ "Instantiated T1"
          }

          trait T2 {
               sb = sb :+ "Instantiated T2"
          }

          class C1 extends T1 with T2 {
               sb = sb :+ "Instantiated C1"
          }

          sb = sb :+ "Creating C1"
          new C1
          sb = sb :+ "Created C1"

          sb.mkString(";") should be("Creating C1;Instantiated T2;Instantiated T1;Instantiated C1;Created C1")
     }


     koan("The diamond of death (http://en.wikipedia.org/wiki/Diamond_problem) is avoided since " +
          "instantiations are tracked and will not allow multiple instantiations") {

          var sb = List[String]()

          trait T1 {
               sb = sb :+ "Instantiated T1"
          }

          trait T2 extends T1 {
               sb = sb :+ "Instantiated T2"
          }

          trait T3 extends T1 {
               sb = sb :+ "Instantiated T3"
          }

          class C1 extends T2 with T3 {
               sb = sb :+ "Instantiated C1"
          }

          sb = sb :+ "Creating C1"
          new C1
          sb = sb :+ "Created C1"

          sb.mkString(";") should be("Creating C1;Instantiated T1;Instantiated T2;Instantiated T3;Instantiated C1;Created C1")
     }



     koan("Traits can be abstract, partially abstract, or concrete, and" +
          "classes provide definitions appropriately"){

          trait AllAbstract {
               def f(n: Int): Int
               val d: Double
          }

          trait PartialAbstract {
               def f(n: Int): Int
               val d: Double
               def g(s: String) = s"($s)"
               val j = 42
               //val m: Int
          }

          trait Concrete {
               def f(n: Int) = n*11
               val d = 1.61803
          }

          // ---------------------------------------------------------------
          // Here, class is declared with "abstract" so this tolerates not
          // implementing in ClassAbstractWithPartial, unlike ClassAllAbstract.
          // (It's not saved because Partial implements stuff in AllAbstract
          // since the abstract keyword acts first. To see example of how one
          // trait can implement stuff in another, see third Abstract+Concrete).
          // But we need to do the implementation of abstract methods/ variables
          // in the object instantiation.
          // SHORT: if class is abstract, you can implement in obj instantiation.
          abstract class ClassAllAbstractWithPartial
               extends AllAbstract
               with PartialAbstract

          val x = new ClassAllAbstractWithPartial {
               override def f(n: Int): Int = n*2
               override val d: Double = 10.1
               //override val m: Int = 3
          }

          x.f(3) should be (6)
          x.g("Bike") should be ("(Bike)")
          x.d should be (10.1)
          x.j should be (42)
          //x.m should be (3)


          // ----------------------------------------------------------------
          // Here, all methods/variables are abstract so when we define
          // ClassAllAbstract, we must also implement the methods/variables
          // in the trait AllAbstract that it extends.
          class ClassAllAbstract
               extends AllAbstract {

               def f(n:Int) = n*12
               val d = 3.14159
          }

          val y = new ClassAllAbstract
          y.f(3) should be(36)
          y.d should be(3.14159)


          // ----------------------------------------------------------------
          // Here, I think Concrete implements all stuff in AllAbstract so
          // no abstract keyword is needed.
          class ClassAllAbstractWithConcrete
               extends AllAbstract
               with Concrete

          val z = new ClassAllAbstractWithConcrete
          z.f(4) should be(44)
          z.d should be(1.61803)

          // ----------------------------------------------------------------
          // Same test as one above
          class ClassConcreteWithPartialAbstract
               extends PartialAbstract
                    with Concrete

          val w = new ClassConcreteWithPartialAbstract
          w.f(10) should be(110)
          w.g("Cotton candy") should be("(Cotton candy)")
          w.d should be(1.61803)
          w.j should be(42)


          // -----------------------------------------------------------------
          class ClassAllAbstractWithPartialWithConcrete
               extends AllAbstract
                    with PartialAbstract
                    with Concrete

          val t = new ClassAllAbstractWithPartialWithConcrete
          t.f(10) should be(110)
          t.g("Cotton candy") should be("(Cotton candy)")
          t.d should be(1.61803)
          t.j should be(42)


          // -----------------------------------------------------------------
          trait FromAbstract extends ClassAllAbstractWithPartial
          trait FromConcrete extends ClassAllAbstract

          // -----------------------------------------------------------------
          // Here, this shows that traits can have no constructor arguments but
          // they can have constructor bodies.
          trait Construction { println("Constructor body")}

          class Constructable extends Construction

          val c = new Constructable // when object is constructed, only then it prints

          // ------------------------------------------------------------------
          // Here, creating anonymous class on the fly
          val a = new AllAbstract with PartialAbstract with Concrete
          a.f(5) should be (55)
          a.d should be(1.61803)
          a.g("happy") should be("(happy)")
          a.j should be(42)
     }



     koan("Traits can inherit from each other"){

          trait A           { def f = "f"}
          trait B extends A { def g = "17"}
          trait C extends B { def h = "1.11"}

          class TestTraitInheritance extends C

          val d = new TestTraitInheritance
          d.f should be("f")
          d.g should be("17")
          d.h should be("1.11")
     }



     koan("Trait collisions from method names can be resolved"){

          trait A {
               def f = 1.1
               def g = "A.g"
               val n = 7
               val mixed: Int = 10
               //val moreMixed = "more mixed val"
          }

          trait B {
               def f = 7.7
               def g = "B.g"
               val n = 17
               def mixed: Int = 20
               //val moreMixed = 30
          }

          object TraitCollision extends A with B {
               // todo    why if I write
               // todo      "override def mixed = 100"
               // todo    instead of
               // todo      override val mixed = 200"
               // todo    then error pops up saying that mixed needs to be immutable, stable value?
               override def f = 9.9
               override val n = 27
               override def g = super[A].g + super[B].g // only methods can access base versions of themselves, not
               // fields
               // todo: if I put val in trait B rather than in trait A, and def in trait A,
               // todo: then val is still the one that must be overridden in the object,
               // todo: not the method - why is that true? Why does mixed need to be a stable "val"?
               override val mixed = 200

               // Collisions where identifier is the same but type is different is simply NOT allowed
               //override val moreMixed = "123"

          }

          TraitCollision.f should be (9.9)
          TraitCollision.g should be ("A.gB.g")
          TraitCollision.n should be (27)
          TraitCollision.mixed should be(200)

     }




     koan("Trait fields and methods can be used in calculations even though they haven't" +
          "been defined. "){

          trait Framework {
               val part1: Int
               def part2: Double
               // even without definitions:
               def templateMethod = part1 + part2
          }

          def operation(impl: Framework) = impl.templateMethod

          class Implementation extends Framework {
               val part1 = 42
               val part2 = 2.71828
          }

          operation(new Implementation) should be(44.71828)
     }



     koan("Traits can extend classes. And the superclass of trait LoggedException becomes" +
          "the superclass of UnhappyException, as well. We can have related superclasses " +
          "in the base class but never unrelated ones."){

          trait Logged {
               def log(msg: String) = println(msg)
          }

          trait LoggedException extends Exception with Logged {
               def log() { log(getMessage()) }
          }

          class UnhappyException extends LoggedException {
               override def getMessage() = "arggh!"
          }

          // we can have related class as well
          class UnhappyIOException extends IOException with LoggedException

          // note doesn't work because JFrame and Exception aren't related. Error will be thrown
          // saying that JFrame is not a subclass of Exception.
          //class UnhappyFrame extends JFrame with LoggedException
     }


     koan("Self-types can be used to ensure that the trait is mixed into a " +
          "subclass of the self-type"){

          trait Logged {
               def log(msg: String) = println(msg)
          }

          // note this trait doesn't need to extend the Exception class, it has a self-type instead.
          // Inside the trait, we can call the methods of the self-type (Exception), such as getMessage()
          trait LoggedException extends Logged {
               this: Exception =>
               def log() { log(getMessage()) }
          }


          // note works because Exception is Exception (already the class)
          val e = new Exception with LoggedException
          // note works because IOException is a subclass of Exception
          val io = new IOException with LoggedException
          // note error because JFrame isn't a subclass of Exception.
          //val f = new JFrame with LoggedException
     }

     koan ("Structural types can be used to ensure that the trait is mixed into" +
          "a class that has the specified methods"){

          trait Logged {
               def log(msg: String) = println(msg)
          }

          trait LoggedException extends Logged {
               this: { def getMessage(): String } =>
               def log() { log(getMessage()) }
          }

          // note works because Exception has getMessage() method
          val g = new Exception with LoggedException
          // note doesn't work because JFrame doesn't have getMessage() method
          //val f = new JFrame with LoggedException
     }
}
