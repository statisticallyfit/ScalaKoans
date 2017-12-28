package org.functionalkoans.forscala

import support.KoanSuite
import scala.collection.mutable

class AboutMutableSets extends KoanSuite {

     koan("Mutable sets can be created easily") {
          val mySet = mutable.Set("Michigan", "Ohio", "Wisconsin", "Iowa")
          mySet.size should be(4)
          mySet += "Oregon"
          mySet contains "Oregon" should be(true)
     }

     koan("Mutable sets can have elements removed") {
          val mySet = mutable.Set("Michigan", "Ohio", "Wisconsin", "Iowa")
          mySet -= "Ohio"
          mySet contains "Ohio" should be(false)
     }

     koan("Mutable sets can have tuples of elements removed") {
          val mySet = mutable.Set("Michigan", "Ohio", "Wisconsin", "Iowa")
          mySet -= ("Iowa", "Ohio")
          mySet contains "Ohio" should be(false)
          mySet.size should be(2)

          var set = mySet
          set.remove("Michigan") should be(true)
          set.remove("halalala") should be(false) // because there doesn't exist element with index 1000

     }

     koan("Mutable sets can have tuples of elements added") {
          val mySet = mutable.Set("Michigan", "Wisconsin")
          mySet += ("Iowa", "Ohio")
          mySet contains "Ohio" should be(true)
          mySet.size should be(4)
     }

     koan("Mutable sets can have Lists of elements added") {
          val mySet = mutable.Set("Michigan", "Wisconsin")
          mySet ++= List("Iowa", "Ohio")
          mySet contains "Ohio" should be(true)
          mySet.size should be(4)
     }

     koan("Mutable sets can have Lists of elements removed") {
          val mySet = mutable.Set("Michigan", "Ohio", "Wisconsin", "Iowa")
          mySet --= List("Iowa", "Ohio")
          mySet contains "Ohio" should be(false)
          mySet.size should be(2)
     }

     koan("Mutable sets can be cleared") {
          val mySet = mutable.Set("Michigan", "Ohio", "Wisconsin", "Iowa")
          mySet.clear() // Convention is to use parens if possible when method called changes state
          mySet contains "Ohio" should be(false)
          mySet.size should be(0)
     }

     // doneby me
     koan ("A set must have distinct elements, and is unordered"){
          var set = mutable.Set[Int]()
          set += 1
          set += (2, 3)
          set += 2                 // silently fails
          set.size should be(3)

          set ++= Vector(4, 5)
          set.size should be (5)

          set.add(6) should be(true) // means element was added
          set.add(5) should be(false) // means element was not added

     }
}
