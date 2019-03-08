package org.functionalkoans.forscala

import support.KoanSuite

import scala.collection.mutable.ArrayBuffer

class AboutParentClasses extends KoanSuite {
     koan("Class hierarchy is linear, a class can only extend from one parent class") {
          class Worker(val firstName: String, val lastName: String) {}
          class Employee(override val firstName: String, override val lastName: String,
                         val employeeID: Long) extends Worker(firstName, lastName)
          val me = new Employee("Name", "Yourself", 1233)
          me.firstName should be("Name")
          me.lastName should be("Yourself")
     }

     koan("A class that extends from another is polymorphic") {

          //Note: the arguments that are the same in the subclass can be declared in subclass either
          // plainly (like firstName) or with 'override val' (for lastName).
          // New arguments don't have to take a keyword unless you want it to be private, as usual
          class Worker(val firstName: String, val lastName: String) {}
          class Employee(firstName: String, override val lastName: String,
                         employeeID: Long, var employeeRating: Int) extends Worker(firstName, lastName)

          val me = new Employee("Name", "Yourself", 1233, 10)
          val worker: Worker = me

          worker.firstName should be("Name")
          worker.lastName should be("Yourself")

          me.employeeRating = 9 //var args can be changed and accessed as uaual
          me.employeeRating should be(9)

          me.firstName should be("Name") //the subclass retains the superclass getter/setter even if the subclass
          // declares that field private
          me.lastName should be("Yourself")

          // me.employeeID // this doesn't work, is private as usual
     }

     koan("An abstract class, as in Java, cannot be instantiated and only inherited") {
          abstract class Worker(val firstName: String, val lastName: String) {}

          // if you uncomment this line, if will fail compilation
          //val worker = new Worker
     }


     koan("A class can be placed inside an abstract class just like in java") {
          abstract class Worker(val firstName: String, val lastName: String) {
               class Assignment(val hours: Long) {
                    // nothing to do here.  Just observe that it compiles
               }
          }
          class Employee(override val firstName: String, override val lastName: String,
                         val employeeID: Long) extends Worker(firstName, lastName)


          // @todo: ---- what is the class type of assignment value - Assignment or Worker?

          val employee = new Employee("Name", "Yourself", 2291)
          val assignment = new employee.Assignment(22)  //using the employee instance's path, create an assignment for it.
          assignment.hours should be (22)
     }



     koan("Inner classes hide inner concepts"){

          class PandorasBox {

               case class Thing (name: String)

               var things = ArrayBuffer[Thing]()
               things += Thing("Bad Thing #1")
               things += Thing("Bad Thing #2")

               def addThing(name: String) { things += new Thing(name)}
          }

          val pandoraBox = new PandorasBox
          pandoraBox.things.foreach(println)
          pandoraBox.addThing("Bad Thing #3")
          pandoraBox.addThing("Bad Thing #4")

          pandoraBox.things(3) should be(pandoraBox.Thing("Bad Thing #4"))


          //inner classes are bound to the object
          val hopeOutOfPandora: pandoraBox.Thing = pandoraBox.Thing("Hope is left")
          hopeOutOfPandora.name should be("Hope is left")
     }

     koan("Case study of Inner and outer classes"){
          class OuterBubble {

               case class InnerRainbow(intensity: Int)

               case class InnerBubble{
                    var size: Int = 1
               }
          }

          val ob1 = new OuterBubble
          val ir1 = new ob1.InnerRainbow(4)
          val ib1 = new ob1.InnerBubble

          ib1.size should be(1)
          ir1.intensity should be(4)

     }
}
