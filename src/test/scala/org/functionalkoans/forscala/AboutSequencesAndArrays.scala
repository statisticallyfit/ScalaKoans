package org.functionalkoans.forscala

import support.KoanSuite

class AboutSequencesAndArrays extends KoanSuite {


     // doneby me
     koan("An array can be printed as a matrix and can also be made into a matrix"){
          val rows = 2
          val cols = 3
          val matrix = Array.ofDim[String](rows, cols)
          matrix(0)(0) = "a"
          matrix(0)(1) = "b"
          matrix(0)(2) = "c"
          matrix(1)(0) = "d"
          matrix(1)(1) = "e"
          matrix(1)(2) = "f"

          // this is how you print an array to look like a matrix
          //matrix foreach {row => row foreach print; println}

          // this is how you print matrix to look like matrix
          for {
               i <- 0 until rows
               j <- 0 until cols
          } println(s"($i)($j) = ${matrix(i)(j)}")
          // the $ allows the real variables to show up


          /// Three dimensional
          println()
          val x, y, z = 3
          val cube = Array.ofDim[Int](x, y, z)
          for {
               i <- 0 until x
               j <- 0 until y
               k <- 0 until z
          } println(s"($i)($j)($k) = ${cube(i)(j)(k)}")
     }


     koan("A list can be converted to an array") {
          val l = List(1, 2, 3)
          val a = l.toArray
          a should equal(Array(1, 2, 3))
     }

     koan("Any sequence can be converted to a list") {
          val a = Array(1, 2, 3)
          val s = a.toSeq
          val l = s.toList
          l should equal(List(1, 2, 3))
     }

     koan("You can create a sequence from a for comprehension") {
          val s = for (v <- 1 to 4) yield v
          s.toList should be(List(1, 2, 3, 4))
     }

     koan("You can create a sequence from a for comprehension with a condition") {
          val s = for (v <- 1 to 10 if v % 3 == 0) yield v
          s.toList should be(List(3, 6, 9))
     }

     koan("You can filter any sequence based on a predicate") {
          val s = Seq("hello", "to", "you")
          val filtered = s.filter(_.length > 2)
          filtered should be(Seq("hello", "you"))
     }

     koan("You can also filter Arrays in the same way") {
          val a = Array("hello", "to", "you", "again")
          val filtered = a.filter(_.length > 3)
          filtered should be(Array("hello", "again"))
     }

     koan("You can map values in a sequence through a function") {
          val s = Seq("hello", "world")
          val r = s map {
               _.reverse
          }

          r should be(Seq("olleh", "dlrow"))
     }

     koan("You can append to the end and prepend to the beginning of a collection"){

          val array = Array(1,7,22,11,17)
          val seq = Seq(1,7,22,11,17)

          array :+ 99 should be(Array(1,7,22,11,17,99))
          99 +: array should be(Array(99,1,7,22,11,17))

          seq :+ 99 should be(Seq(1,7,22,11,17,99))
          99 +: seq should be(Seq(99,1,7,22,11,17))
     }

     koan("You can update a particular location with a new value"){
          val array = Array(1,7,22,11,17)
          val seq = Seq(1,7,22,11,17)

          array.update(1, 100)
          array should be(Array(1, 100,22,11,17))
          array.updated(2, 22222) should be(Array(1,100,22222,11,17))

          // note seq has no update() method like array
          //seq.update(1, 100)
          seq should be(Seq(1, 7,22,11,17))
          seq.updated(2, 22222) should be(Seq(1,7,22222,11,17))
     }
}
