package org.functionalkoans.forscala

import support.KoanSuite

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
}
