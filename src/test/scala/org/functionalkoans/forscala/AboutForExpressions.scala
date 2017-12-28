package org.functionalkoans.forscala

import support.KoanSuite

import scala.collection.mutable.ArrayBuffer

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


     // doneby me
     // @todo: FIX
     koan("Yield expressions"){
          val seq = {
               for (i <- 1 to 10)
                    yield i % 3
          }
          seq.mkString(", ") should equal("1, 2, 0, 1, 2, 0, 1, 2, 0, 1")
          //should be(Vector(1, 2, 0, 1, 2, 0, 1, 2, 0, 1))

          /*val h1 = {
               for (c <- "Hello"; i <- 0 to 1)
                    yield (c + i).toChar
          }
          h1.mkString(", ") should equal("HIeflmlmop")
          //IndexedSeq('H', 'I', 'e', 'f', 'l', 'm', 'l', 'm', 'o', 'p')


          val h2 = {
               for {i <- 0 to 1
                    c <- "Hello"}
                    yield (c + i).toChar
          }
          h2.mkString(", ") should equal("HelloIfmmp")//IndexedSeq('H', 'e', 'l', 'l', 'o', 'I', 'f', 'm', 'm', 'p'))*/

     }



     // doneby me
     koan("For expressions can use the method 'until'. " +
          "This means the range is end-exclusive"){

          val word = "Hello"
          var sum = ""
          for (i <- 0 until word.length){
               sum += word(i)
          }
          sum should equal("Hello")
     }

}
