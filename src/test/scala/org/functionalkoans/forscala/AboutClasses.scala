package org.functionalkoans.forscala

import support.KoanSuite


class AboutClasses extends KoanSuite {

     // you can define class with var or val parameters
     class ClassWithVarParameter(var description: String)

     class ClassWithValParameter(val name: String)

     // you can define class with private fields
     class ClassWithPrivateFields(name: String, private val num1: Int, private var num2: Int)


     koan("val parameters in class definition define getter only, no setter") {
          val aClass = new ClassWithValParameter("name goes here")
          aClass.name should be("name goes here")
     }

     koan("var parameters in class definition define getter and setter") {
          val aClass = new ClassWithVarParameter("description goes here")
          aClass.description should be("description goes here")

          aClass.description = "new description"
          aClass.description should be("new description")
     }


     // name becomes private just by not using either val or var. Or you can use the private keyword
     koan("parameters declared without val or var are private. Same effect as adding private keyword" +
          "to a val or var parameter") {
          val aClass = new ClassWithPrivateFields("name", 23, 25)

          // NOTE: aClass.name is not accessible
          //NOTE: aClass.num1 is not accessible
          //NOTE: aClass.num2 is not accessible
     }


     koan("Primary constructor of a class is made of " +
          "(1) constructor parameters, " +
          "(2) methods called in the class, and " +
          "(3) statements executed in the body of the class. " +
          "Auxiliary constructors allow different ways to make objects"){


          object Pizza {
               val DEFAULT_CRUST_SIZE = 12
               val DEFAULT_CRUST_TYPE = "THIN"
          }

          // primary constructor
          class Pizza(var crustSize: Int, var crustType: String) {

               println("the pizza primary constructor begins")



               // one-arg auxiliary constructor
               def this(crustSize: Int){
                    this(crustSize, Pizza.DEFAULT_CRUST_TYPE)
               }
               // one-arg auxiliary constructor (another way to write it)
               def this(crustType: String) {
                    this(Pizza.DEFAULT_CRUST_SIZE)
                    this.crustType = crustType
               }
               // zero-arg auxiliary constructor
               def this() {
                    this(Pizza.DEFAULT_CRUST_SIZE, Pizza.DEFAULT_CRUST_TYPE)
               }
               override def toString = s"A $crustSize inch pizza with a $crustType crust"


               println("the pizza primary constructor ends")
          }


          object PizzaTest {
               val p1 = new Pizza(Pizza.DEFAULT_CRUST_SIZE, Pizza.DEFAULT_CRUST_TYPE)
               val p2 = new Pizza()
               val p3 = new Pizza(10, "MEDIUM")
               val p4 = new Pizza(crustSize = 11)

               // declaration in primary constructor implies generation of accessor methods
               p1.crustSize should be(12)
               // declaration in primary constructor implies generation of mutator methods
               p1.crustType = "VERY THIN"
               p1.crustType should be("VERY THIN")

               p2.crustSize should be(12)
               p2.crustType should be("THIN")

               p3.crustSize should be(10)
               p3.crustType should be("MEDIUM")

               p4.crustSize should be(11)
               p4.crustSize = 20
               p4.crustSize should be(20)
               p4.crustType should be("THIN")
          }
     }



     // todo: what does scala cookbook page 109 mean when the accessor and mutator methods won't be generated?

     koan("Mutator and accessor methods are still generated even though primary " +
          "constructor is not used."){

          // primary constructor
          class Bread () {

               var crustSize = 0
               var crustType = ""

               //one-arg auxiliary constructor
               def this(crustSize: Int) {
                    this()
                    this.crustSize = crustSize
               }
               //one-arg auxiliary constructor
               def this(crustType: String) {
                    this()
                    this.crustType = crustType
               }
               // todo: why does error occur when including the zero-arg auxiliary constructor?
               /*def this() {
                    this.crustSize = Bread.DEFAULT_CRUST_SIZE
               this.crustType = Bread.DEFAULT_CRUST_TYPE
               }*/
               override def toString = s"A $crustSize inch bread with a $crustType crust"
          }


          object BreadTest {
               val b = new Bread
               // declaration in primary constructor implies generation of accessor methods
               b.crustSize should be(0)
               // declaration in primary constructor implies generation of mutator methods
               b.crustType = "VERY THIN"
               b.crustType should be("VERY THIN")
          }
     }


     // doneby me: todo - why must this stay outside the koan?
     class Brain private{
          override def toString = "This is the brain"
     }

     object Brain {
          val brain = new Brain
          def getInstance = brain
     }

     koan("A private primary constructor does not allow you to make an instance of that class"){


          object BrainTest {
               // this won't compile
               //val brain = new Brain

               // this works
               val brain = Brain.getInstance
               brain.toString should be("This is the brain")
          }
     }


     koan("You can declare instance variables with or without the primary constructor at the same time"){

          class Employee(var firstName: String, var lastName: String){
               // this acts like a default variable, you can initialize it when creating emp object
               var age: Int = 0

               def this(firstName: String, lastName: String, age: Int){
                    // press CTRL B -> this calls the primary constructor
                    this(firstName, lastName)
                    this.age = age
               }
               def this(firstName: String, age: Int){
                    this(firstName, "NO-NAME")
                    this.age = age
               }
               override def toString(): String = {
                    firstName + " " + lastName + " is " + age + " years old."
               }
          }

          object EmployeeTest {
               val e = new Employee("Moby", "Dick", 10)
               e.firstName = "Harry"
               e.lastName = "Potter"
               e.age = 11

               e.firstName should be("Harry")
               e.age should be (11)
          }
     }

     // @todo: is it a good use of scala language to have class + object that runs the class?
     // @todo: how else do you run the class?




     koan ("Declaring a field with private[this] makes the field object-private, " +
          "which means it can only be accessed from the object that contains it." +
          "It cannot be accessed from other instances of the same type."){

          class Stock {
               private[this] var price: Double = _

               def setPrice(p: Double) { price = p}

               // error: the method won't compile since price is now object-private
               //def isHigher(that: Stock): Boolean = this.price > that.price
          }
     }



     koan("The subclass cannot call the superclass constructor directly ") {


          case class Address (city: String, state: String)
          case class Role (role: String)

          class OrdinaryPerson (var name: String, var address: Address) {
               // now way for Employee auxiliary constructors to call this constructor
               def this(name: String){
                    this(name, null)
                    address = null
               }
          }

          class OrdinaryEmployee (name: String, role: Role, address: Address)
          extends OrdinaryPerson (name, address) {

               def this(name: String) { this(name, null, null) }
               def this(name: String, role: Role) { this(name, role, null) }
               def this(name: String, address: Address) { this(name, null, address) }
          }
     }


     koan("(1) Base classes are initialized before derived classes" +
          "(2) The derived class constructor can choose any of the base class auxiliary" +
          "constructors to call"){

          class House(val address:String, val state:String, val zip: String){

               def this(state:String, zip:String) = this("?", state,zip)
               def this(zip:String) = this("?", "?", zip)
          }

          // note uses constructor 1
          class Home(address:String, state:String, zip:String, val name:String)
               extends House(address, state, zip) {
               override def toString = s"$name: $address, $state $zip"
          }

          // note uses constructor 2
          class VacationHouse(state:String, zip:String, val startMonth:Int, val endMonth:Int)
               extends House(state, zip)

          // note uses constructor 3
          class TreeHouse(val name: String, zip:String)
               extends House(zip)



          val h = new Home("888 N. Main St.", "KS", "12345", "Metropolis")
          h.address should be("888 N. Main St.")
          h.state should be("KS")
          h.name should be("Metropolis")
          h.toString should be("Metropolis: 888 N. Main St., KS 12345")

          val v = new VacationHouse("KS", "12345", 6, 8)
          v.state should be("KS")
          v.startMonth should be(6)
          v.endMonth should be(8)

          val t = new TreeHouse("Oak", "48104")
          t.name should be ("Oak")
          t.zip should be ("48104")
     }
}

