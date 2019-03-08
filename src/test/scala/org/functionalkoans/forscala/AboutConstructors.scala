package org.functionalkoans.forscala

import support.KoanSuite

import scala.collection.mutable.ListBuffer

class AboutConstructors extends KoanSuite {



     koan("Primary constructor specified with a parameter requires that parameter to be" +
          " passed in") {

          class AboutConstructorWithAuxiliaryConstructor(val name: String) {
               // invoke auxiliary constructor
               def this() {
                    // what happens if you comment out the following line?..
                    this ("defaultname")
               }
          }

          val aboutMe = new AboutConstructorWithAuxiliaryConstructor()
          aboutMe.name should be ("defaultname")
     }


     koan("Class with no class parameters is called with no arguments") {

          class AboutClassWithNoClassParameter

          // add parameter to make this fail
          val aboutMe = new AboutClassWithNoClassParameter()

     }



     koan("Constructor can take default values"){

          /**
            * The primary constructor is not explicit; it is a combination of the constructor " +
          "parameters, methods called in the class, and statements/expression executed in the class

            */
          class Coffee(val shots: Int = 2,
                       val decaf: Boolean = false,
                       val milk: Boolean = false,
                       val toGo: Boolean = false,
                       val syrup: String = "") {

               var result = ""

               def getCup(): Unit = {
                    if (toGo) result += "ToGoCup "
                    else result += "HereCup "
               }

               def pourShots(): Unit ={
                    for (s <- 0 until shots){
                         if(decaf) result += "decaf shot "
                         else result += "shot "
                    }
               }

               def addMilk(): Unit ={ if (milk) result += "milk " }

               def addSyrup(): Unit ={ result += syrup }

               getCup()
               pourShots()
               addMilk()
               addSyrup()
          }

          val usual = new Coffee
          usual.result should be("HereCup shot shot ")

          val mocha = new Coffee(decaf=true, toGo = true, syrup = "Chocolate")
          mocha.result should be("ToGoCup decaf shot decaf shot Chocolate")
     }


     koan("Auxiliary constructors can have arguments of different types, as long" +
          "as they call other constructors that connect back to the original " +
          "arguments of the class"){


          class GardenGnome(val height: Double, val weight: Double, val happy: Boolean) {

               //println("Inside primary constructor: GardenGnome")

               var painted = true
               def magic(level: Int): String = "Poof! " + level

               // note: this gets executed if only the height argument is passed
               def this(height: Double) {
                    this(height, 100.0, true)
               }
               // note this auxiliary constructor has different type than
               // all other class arguments but works because it calls first
               // auxiliary constructor, which calls the original class args
               def this(name: String) = {
                    this(15.0)
                    assert(painted)
               }

               def show(): String = height + " " + weight + " " + happy + " " + painted
          }

          val g1 = new GardenGnome(20.0, 110.0, false)
          g1.show() should be("20.0 110.0 false true")

          val g2 = new GardenGnome("Bob")
          g2.show() should be("15.0 100.0 true true")
     }


     koan("Can modify constructor parameters"){

          //intentionally left off the 'private' modifier off _symbol
          class Stock (var _symbol: String){

               //getter
               def symbol = _symbol
               //setter
               def symbol_=(s: String): Unit = {
                    this.symbol = s
                    println(s"symbol was updated, new value is $symbol")
               }
          }

          val stock = new Stock("$")
          stock._symbol = "$$$"
          stock.symbol should be("$$$")
     }

     koan("Classes can be declared in such a way to avoid automatic generation of getters and setters"){

          class Stock(var delayedPrice: Double) {
               // getter and setter are also generated
               var symbol: String = _

               //no getters or setters are generated
               private var currentPrice: Double = _
          }

          val stock = new Stock(100.28)
          stock.delayedPrice should be(100.28)
          stock.delayedPrice += 0.11
          stock.delayedPrice should be(100.39)
          stock.symbol should be(null)
          stock.symbol = "$$$"
          stock.symbol should be ("$$$")

          //not available
          //stock.currentPrice
     }

     koan("A private field can be seen by any instance with the private field. Any other" +
          "instance of the same type can access its own private field, too"){

          class Stock{
               //no getters or setters are generated
               private var price: Double = _

               def setPrice(p: Double) { price = p}
               def isHigher(that: Stock): Boolean = this.price > that.price
          }

          val s1 = new Stock
          s1.setPrice(20)
          val s2 = new Stock
          s2.setPrice(100)

          s2.isHigher(s1) should be(true)
     }


     koan("An object-private field can only be accessed from the object that contains it" +
          "so not even other objects of the same type can see that field. This makes" +
          "the field more private than the plain private setting."){

          class MusicalInstrument {
               //a private[this] var is object-private and can only be seen by the
               //current instance
               private[this] var tonality: Double = _

               def setTune(t: Int) {tonality = t }

               //error: this method won't compile because tonality is now object-private
               //def isHigher(that: MusicalInstrument): Boolean = this.tonality > that.tonality
          }
     }


     koan("Subclasses can control the superclass constructor, because it is called by" +
          " the primary constructor of the subclass"){

          // (1) primary constructor
          class WildCat(var name: String, var age: Int) {

               var roars: ListBuffer[String] = ListBuffer()
               roars += "Wildcat primary"

               // (2) auxiliary constructor
               def this(name: String) {
                    this(name, 0)
                    roars += "Wildcat auxiliary"
               }

               override def toString = s"$name is $age years old"
          }

          //calls the Wildcat auxiliary (this) constructor since only name is passed
          class Leopard(name: String, var numSpots: Int) extends WildCat(name) {
               //now the leopard primary constructor is called
               roars += "Leopard primary"
          }

          //calls the Wildcat primary constructor since both name and age are passed
          class BengalTiger(name: String, var numStripes: Int, age: Int) extends WildCat(name, age){
               roars += "BengalTiger primary"
          }

          val leo = new Leopard("Leo", 23)
          leo.age should be(0)
          leo.roars should be(Seq("Wildcat primary", "Wildcat auxiliary", "Leopard primary"))

          val tigger = new BengalTiger("Tigger", 10, 5)
          tigger.age should be(5)
          tigger.roars should be(Seq("Wildcat primary", "BengalTiger primary"))
     }


     koan("Auxiliary constructors of a class are limited to calling other " +
          "constructors within that same class, never reaching superclass constructors."){

          trait Environment
          case object Marsh extends Environment
          case object Jungle extends Environment

          trait Hemisphere
          case object SouthernHemi extends Hemisphere
          case object NorthernHemi extends Hemisphere

          trait Color
          case object Red extends Color
          case object Green extends Color
          case object Brown extends Color


          case class Habitat(hemisphere: Hemisphere, marshOrJungle: Environment) //true if marsh
          case class Poison(level: Int)

          class Amphibian(var color: Color, var habitat: Habitat) {

               //testing how many times the primary constructor and auxiliary constructors are reached
               var croaks: ListBuffer[String] = ListBuffer("Amphibian primary")

               // no way for the Frog auxiliary constructors to call this
               // auxiliary constructor
               def this (color: Color) {
                    this(color, null) //calls the primary constructor for Amphibian
                    //habitat
                    croaks += "Amphibian auxiliary"
               }

               override def toString = if (habitat == null) color.toString else s"$color @ $habitat"
          }

          class Frog(color: Color, var level: Poison, habitat: Habitat)
               extends Amphibian(color, habitat){

               croaks += "Frog primary"

               def this(color:Color){
                    this(color, null, null) //calls the Frog auxiliary constructor,
                    //can never reach the Amphibian auxiliary constructor above
                    croaks += "Frog auxiliary Color"
               }

               def this(color: Color, level: Poison) {
                    this(color, level, null)

                    croaks += "Frog auxiliary Color Poison"
               }

               def this(color: Color, habitat: Habitat) {
                    this(color, null, habitat)

                    croaks += "Frog auxiliary Color Habitat"
               }
          }

          //Main point: no direct way for subclass to control which superclass constructor
          //is called from the subclass's auxiliary constructor.
          // Each auxiliary constructor is limited to calling a previously defined
          // constructor in the same class.

          val amph = new Amphibian(Green, Habitat(NorthernHemi, Marsh))

          val marshFrog = new Frog(Brown, Poison(0), Habitat(NorthernHemi, Marsh))
          val brightJungleFrog = new Frog(Red, Habitat(SouthernHemi, Jungle))
          brightJungleFrog.level = Poison(9)
          val unknownFrog = new Frog(Green, Poison(7))

          marshFrog.croaks should be(List("Amphibian primary", "Frog primary"))
          brightJungleFrog.croaks should be(List("Amphibian primary", "Frog primary", "Frog auxiliary Color Habitat"))
          unknownFrog.croaks should be(List("Amphibian primary", "Frog primary", "Frog auxiliary Color Poison"))


     }
}
