package org.functionalkoans.forscala

import support.KoanSuite

import scala.collection.immutable
import scala.collection.mutable.{ArrayBuffer, ListBuffer}

class AboutForExpressions extends KoanSuite {


     koan("For loops can be simple") {
          val someNumbers = Range(0, 10)
          var sum = 0
          for (i <- someNumbers)
               sum += i

          sum should equal(45)
     }


     koan("For loops can contain additional logic") {
          val someNumbers = Range(0, 10)
          var sum = 0
          // sum only the even numbers
          for (i <- someNumbers)
               if (i % 2 == 0) sum += i

          sum should equal(20)
     }

     // doneby me
     koan("For loops can have multiple generators"){
          var value = ArrayBuffer[Int]()
          for (i <- 1 to 3; j <- 1 to 3)
               value += (10*i + j)

          value.mkString(", ") should be("11, 12, 13, 21, 22, 23, 31, 32, 33")
     }

     // doneby me
     koan("For loops can have multiple generators with a guard"){
          var value = ArrayBuffer[Int]()
          for (i <- 1 to 3; j <- 1 to 3 if i != j)
               value += (10*i + j)

          value.mkString(", ") should be("12, 13, 21, 23, 31, 32")
     }


     // doneby me
     koan("For loops can have multiple generators with an additional variable"){
          var value = ArrayBuffer[Int]()
          for (i <- 1 to 3; from = 4 - i; j <- from to 3)
               value += (10*i + j)

          value.mkString(", ") should be("13, 22, 23, 31, 32, 33")
     }


     // doneby me
     koan("For expressions can nest, with later generators varying more rapidly than earlier ones") {

       val xValues = Range(1, 5)
       val yValues = Range(1, 3)
       val coordinates = for {
            x <- xValues
            y <- yValues} yield (x, y)

       coordinates(4) should be(3, 1)
     }


     koan("For loops can have counters"){
          val counters: Seq[Int] = 0 until 10

          for(value <- 0 until 10){
               value should be(counters(value))
          }
     }


     koan("Can return values into a new collection using 'yield'"){

          val fruit: Array[String] = Array("apple", "banana", "orange")

          val uppercaseFruit = for(theFruit <- fruit) yield theFruit.toUpperCase()


          uppercaseFruit(0) should be("APPLE")
          uppercaseFruit(1) should be("BANANA")
          uppercaseFruit(2) should be("ORANGE")
     }


     koan("Foreach loops are condensed versions of for loops"){
          val fruit: Array[String] = Array("apple", "banana", "orange")
          val uppercaseFruit: ListBuffer[String] = ListBuffer()

          fruit.foreach(theFruit => uppercaseFruit += theFruit.toUpperCase())

          uppercaseFruit(0) should be("APPLE")
          uppercaseFruit(1) should be("BANANA")
          uppercaseFruit(2) should be("ORANGE")
     }


     // doneby me
     koan("For expressions can use the method 'until' to make the range end-exclusive"){

          val word = "Hello"
          var sum = ""
          for (i <- 0 until word.length){
               sum += word(i)
          }
          sum should equal("Hello")


          //Another example
          val values: Seq[Int] = for (i <- 1 until 5) yield i

          values.length should be(4)
          values should be(Seq(1, 2, 3, 4))
     }


     koan("For expressions can use the method 'to' to make the range end-inclusive"){

          val values = for(i <- 1 to 5) yield i

          values.length should be(5)
          values should be(Seq(1, 2, 3, 4, 5))
     }

     koan("For loops can have multiple counters"){

          val listOfTuples: ListBuffer[(Int, Int, Int)] = ListBuffer()

          for(i <- 0 to 1; j <- 4 to 5; k <- 1 to 3){
               //need extra paranetheses or else compiler thinks we are trying to pass
               // three separate arguments
               listOfTuples += ( (i, j, k) )
          }

          listOfTuples(0) should be((0, 4, 1))
          listOfTuples(1) should be((0, 4, 2))
          listOfTuples(2) should be((0, 4, 3))
          listOfTuples(3) should be((0, 5, 1))
          listOfTuples(4) should be((0, 5, 2))
          listOfTuples(5) should be((0, 5, 3))

          listOfTuples(6) should be((1, 4, 1))
          listOfTuples(7) should be((1, 4, 2))
          listOfTuples(8) should be((1, 4, 3))
          listOfTuples(9) should be((1, 5, 1))
          listOfTuples(10) should be((1, 5, 2))
          listOfTuples(11) should be((1, 5, 3))
     }

     koan("For loops using multiple counters can be used to initialize multi-dimensional collections"){

          val array = Array.ofDim[Int](3, 4)

          for(r <- 0 to 2; c <- 0 to 3){
               array(r)(c) = r + c
          }

          array(0)(0) should be(0)
          array(0)(1) should be(1)
          array(0)(2) should be(2)
          array(0)(3) should be(3)

          array(1)(0) should be(1)
          array(1)(1) should be(2)
          array(1)(2) should be(3)
          array(1)(3) should be(4)

          array(2)(0) should be(2)
          array(2)(1) should be(3)
          array(2)(2) should be(4)
          array(2)(3) should be(5)


          //Can also use another type of notation:
          for {
               i <- 0 until 1
               j <- 0 until 3
          } array(i)(j) += 10

          array(0)(0) should be(10)
          array(0)(1) should be(11)
          array(0)(2) should be(12)
          array(0)(3) should be(3)

     }


     koan("For loop can have Guards (embedded if-statements)"){

          val evenNumbers = for(number <- 1 to 10 if number % 2 == 0) yield number

          evenNumbers should be(Seq(2, 4, 6, 8, 10))

          ////another notation
          val oddNumbers = for {
               number <- 1 to 10
               if number % 2 != 0
          } yield number

          oddNumbers should be(Seq(1,3,5,7,9))

          /// a hard way to return number 4
          val mysteryNum = for {
               num <- 1 to 10
               if num > 3
               if num < 6
               if num % 2 == 0
          } yield num

          mysteryNum.head should be(4)
     }


     koan("For loops can use `break` to break out of the loop"){

          import util.control.Breaks._

          val numbers: ListBuffer[Int] = ListBuffer()

          breakable {
               for(i <- 1 to 10) {
                    numbers += i
                    if (i > 4) break //break out of the for loop
               }
          }

          numbers should be(ListBuffer(1,2,3,4,5))
     }

     koan("For loops can use break and continue patterns in loops"){
          // note: there is no 'continue' keyword in scala, like in java


          import util.control.Breaks._

          val searchMe = "peter piper picked a peck of pickled peppers"
          var numberOfPs = 0

          for(i <- 0 until searchMe.length) {

               breakable {
                    if(searchMe.charAt(i) != 'p'){
                         break //break out of the 'breakable' and continue to outside the loop
                    } else {
                         numberOfPs += 1
                    }
               }
          }

          numberOfPs should be(9)

     }

     koan("For loops can use Nested breaks"){

          import scala.util.control._

          val Inner = new Breaks
          val Outer = new Breaks

          val pairsAfterNestedBreaks: ListBuffer[(Int, Char)] = ListBuffer()

          Outer.breakable {
               for(i <- 1 to 5) {
                    Inner.breakable {
                         for(j <- 'a' to 'e'){
                              if(i == 1 && j == 'c') Inner.break else pairsAfterNestedBreaks += ((i, j))
                              if(i == 2 && j == 'b') Outer.break
                         }
                    }
               }
          }

          pairsAfterNestedBreaks should be(Seq((1, 'a'), (1, 'b'), (2, 'a'), (2, 'b')))


          //Using just one labeled break
          val pairsAfterOneBreak: ListBuffer[(Int, Char)] = ListBuffer()

          val Exit = new Breaks
          Exit.breakable {
               for(i <- 1 to 5){
                    for (j <- 'a' to 'e'){
                         if(j == 'c') Exit.break else pairsAfterOneBreak += ((i, j))
                    }
               }
          }
          pairsAfterOneBreak should be(Seq((1, 'a'), (1, 'b')))
     }


}
