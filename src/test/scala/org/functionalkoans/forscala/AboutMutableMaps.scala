package org.functionalkoans.forscala

import support.KoanSuite
import scala.collection.mutable

class AboutMutableMaps extends KoanSuite {

     koan("Mutable maps can be created easily") {
          val myMap = mutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
          myMap.size should be(4)
          myMap += "OR" -> "Oregon"
          myMap contains "OR" should be(true)
     }

     koan("Mutable maps can have elements updated at a certain location, even if declared with 'val'"){
          val myMap = mutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "")
          val myMap2 = myMap

          myMap.size should be(4)
          myMap("IA") = "Iowa"
          myMap("IA") should be("Iowa")
          myMap.size should be(4)

          // note another way, using update() and apply()
          myMap2.size should be(4)
          myMap2.update("IA", "Iowa")
          myMap2.apply("IA") should be("Iowa")
          myMap2.size should be(4)
     }

     koan("Mutable maps can have elements removed, even if declared with 'val'") {
          val myMap = mutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
          myMap -= "OH"
          myMap contains "OH" should be(false)
     }

     koan("Mutable maps can have tuples of elements removed") {
          val myMap = mutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
          myMap -= ("IA", "OH")
          myMap contains "OH" should be(false)
          myMap.size should be(2)
     }

     koan("Mutable maps can have tuples of elements added") {
          val myMap = mutable.Map("MI" -> "Michigan", "WI" -> "Wisconsin")
          myMap += ("IA" -> "Iowa", "OH" -> "Ohio")
          myMap contains "OH" should be(true)
          myMap.size should be(4)
     }

     koan("Mutable maps can have Lists of elements added") {
          val myMap = mutable.Map("MI" -> "Michigan", "WI" -> "Wisconsin")
          myMap ++= List("IA" -> "Iowa", "OH" -> "Ohio")
          myMap contains "OH" should be(true)
          myMap.size should be(4)
     }

     koan("Mutable maps can have Lists of elements removed") {
          val myMap = mutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
          myMap --= List("IA", "OH")
          myMap contains "OH" should be(false)
          myMap.size should be(2)
     }

     koan("Mutable maps can be cleared") {
          val myMap = mutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
          myMap.clear() // Convention is to use parens if possible when method called changes state
          myMap contains "OH" should be(false)
          myMap.size should be(0)
     }



     // doneby me
     koan("put() returns an option of none if there is no element like the one being put, " +
          "and returns an option of the element being replaced when the new element is put"){

          val states = mutable.Map("AK" -> "Alaska", "IL" -> "Illinois", "KY" -> "Kentucky")
          states.put("CO", "Colorado") should be(None)
          states.put("CO", "COLORADO") should be(Some("Colorado"))
     }

     koan("remove() returns an option that enfolds the element removed"){
          val states = mutable.Map("AK" -> "Alaska", "IL" -> "Illinois", "KY" -> "Kentucky")
          states.remove("AK") should be(Some("Alaska"))

          val numbers = mutable.Map(0 -> 100, 1 -> 19, 2 -> 30, 3 -> 452)
          numbers.remove(2) should be(Some(30))
     }


     koan("Getting the keys and values from a map (works same for immutable and mutable maps)"){

          val myMap = scala.collection.mutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
          myMap.keySet should be(Set("MI", "OH", "WI", "IA"))    // type is Set[String]
          myMap.keys should be(Set("MI", "OH", "WI", "IA"))      // type is Iterable[String]
          myMap.values.toArray should be(Array("Michigan", "Iowa", "Wisconsin", "Ohio"))

          val kit = myMap.keysIterator
          kit.next() should be("MI")
          kit.next() should be("IA")
          kit.next() should be("WI")
          kit.next() should be("OH")

          val vit = myMap.valuesIterator
          vit.next() should be("Michigan")
          vit.next() should be("Iowa")
          vit.next() should be("Wisconsin")
          vit.next() should be("Ohio")
     }

     koan("Switching the keys and values might result in losing some map content"){
          val products = Map("Breadsticks" -> "$5", "Pizza" -> "$10", "Wings" -> "$5")
          val reverseMap = for((k, v) <- products) yield (v, k)

          reverseMap.size should be (2)

          val vals = reverseMap.values.toArray
          vals(0) should be("Wings")
          vals(1) should be("Pizza")

          val keys = reverseMap.keys.toArray
          keys(0) should be("$5")
          keys(1) should be("$10")
     }
}
