package org.functionalkoans.forscala

import java.util.NoSuchElementException

import support.KoanSuite

import scala.collection.immutable

class AboutMaps extends KoanSuite {

     koan("Maps can be created easily") {
          val myMap = immutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
          myMap.size should be(4)
     }

     koan("Maps contain distinct pairings") {
          val myMap = immutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "MI" -> "Michigan")
          myMap.size should be(3)
     }

     koan("Maps can be added to easily") {
          val myMap = immutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "MI" -> "Michigan")

          val aNewMap = myMap + ("IL" -> "Illinois")

          aNewMap.contains("IL") should be(true)
     }


     koan("Immutable maps can be added to like mutable maps if the map is declared with 'var' " +
          "and not 'val'"){
          var x = immutable.Map("AL" -> "Alabama")

          // add one element
          x += ("AK" -> "Alaska")
          x should be(immutable.Map("AL" -> "Alabama", "AK" -> "Alaska"))

          // add multiple elements
          x += ("AR" -> "Arkansas", "AZ" -> "Arizona")
          x should be(immutable.Map("AL" -> "Alabama", "AK" -> "Alaska", "AR" -> "Arkansas", "AZ" -> "Arizona"))

          // add a tuple, replacing the previous "AR" key
          x += ("AR" -> "banana")
          x should be(immutable.Map("AL" -> "Alabama", "AK" -> "Alaska", "AR" -> "banana", "AZ" -> "Arizona"))
     }

     koan("Map values can be iterated") {
          val myMap = immutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "MI" -> "Michigan")

          val mapValues = myMap.values

          mapValues.size should be(3)

          mapValues.head should be("Michigan")

          val lastElement = mapValues.last
          lastElement should be("Wisconsin")

          // way 1 to iterate
          for (mval <- mapValues) println(mval)

          // way 2 to iterate
          for ((key, value) <- myMap) println(s"key: $key, value: $value")

          // way 3 to iterate
          myMap.foreach {
               case(abbreviation, name) => println(s"key: $abbreviation, value: $name")
          }

          //way 4 to iterate
          println("\nState abbreviations: ")
          myMap.keys.foreach(println)
          println("\nState names")
          myMap.values.foreach(println)

          // NOTE that the following will not compile, as iterators do not implement "contains"
          //mapValues.contains("Illinois") should be (true)
     }

     koan("Maps insertion with duplicate key updates previous entry with subsequent value") {
          val myMap = immutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "MI" -> "Meechigan")

          val mapValues = myMap.values

          mapValues.size should be(3)

          myMap("MI") should be("Meechigan")
     }

     koan("Map keys may be of mixed type") {
          val myMap = immutable.Map("Ann Arbor" -> "MI", 49931 -> "MI")
          myMap("Ann Arbor") should be("MI")
          myMap(49931) should be("MI")
     }

     koan("Mixed type values can be added to a map ") {
          val myMap = scala.collection.mutable.Map.empty[String, Any]
          myMap("Ann Arbor") = (48103, 48104, 48108)
          myMap("Houghton") = 49931

          myMap("Houghton") should be(49931)
          myMap("Ann Arbor") should be((48103, 48104, 48108))
          // what happens if you change the Any to Int
     }


     koan("Maps may be accessed") {
          val myMap = immutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
          myMap("MI") should be("Michigan")
          myMap("IA") should be("Iowa")
     }

     koan("Map elements can be removed easily") {
          val myMap = immutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
          val aNewMap = myMap - "MI"
          aNewMap.contains("MI") should be(false)

          // removing since x is a var
          var x = immutable.Map("AL" -> "Alabama", "AK" -> "Alaska", "AR" -> "banana", "AZ" -> "Arizona")
          x -= "AR"
          x should be(immutable.Map("AL" -> "Alabama", "AK" -> "Alaska", "AZ" -> "Arizona"))

          //remove multiple elements (uses varargs method
          x -= ("AL", "AZ")
          x should be(immutable.Map("AK" -> "Alaska"))

          // Even though the map is a var and can have added/removed elements,
          // its elements cannot be reassigned
          //x("CO") = "Colorado" // this doesn't work (adding new element)
          //x("AK") = "foo" // this doesn't work (replacing element)
     }

     koan("Accessing a map by key results in an exception if key is not found") {

          val myMap = immutable.Map("OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")

          // Cheat Code (because this is hard to illustrate): uncomment the intercept code to make this pass
          intercept[NoSuchElementException] {

               myMap("MI") should be(__)
          }
     }

     koan("Map elements can be removed in multiple") {
          val myMap = immutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
          val aNewMap = myMap -- List("MI", "OH")

          aNewMap.contains("MI") should be(false)

          aNewMap.contains("WI") should be(true)
          aNewMap.size should be(2)
     }

     koan("Map elements can be removed with a tuple") {
          val myMap = immutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
          val aNewMap = myMap - ("MI", "WI") // Notice: single '-' operator for tuples

          aNewMap.contains("MI") should be(false)
          aNewMap.contains("OH") should be(true)
          aNewMap.size should be(2)
     }

     koan("Attempted removal of nonexistent elements from a map is handled gracefully") {
          val myMap = immutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
          val aNewMap = myMap - "MN"

          aNewMap.equals(myMap) should be(true)
     }

     koan("Map equivalency is independent of order") {

          val myMap1 = immutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
          val myMap2 = immutable.Map("WI" -> "Wisconsin", "MI" -> "Michigan", "IA" -> "Iowa", "OH" -> "Ohio")

          myMap1.equals(myMap2) should be(true)
     }



     koan("Getting the keys and values from a map (works same for immutable and mutable maps)"){

          val myMap = immutable.Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "IA" -> "Iowa")
          myMap.keySet should be(Set("MI", "OH", "WI", "IA"))    // type is Set[String]
          myMap.keys should be(Set("MI", "OH", "WI", "IA"))      // type is Iterable[String]
          myMap.values.toArray should be(Array("Michigan", "Ohio", "Wisconsin", "Iowa"))

          val kit = myMap.keysIterator
          kit.next() should be("MI")
          kit.next() should be("OH")
          kit.next() should be("WI")
          kit.next() should be("IA")

          val vit = myMap.valuesIterator
          vit.next() should be("Michigan")
          vit.next() should be("Ohio")
          vit.next() should be("Wisconsin")
          vit.next() should be("Iowa")
     }


     koan("Switching the keys and values might result in losing some map content"){
          val products = immutable.Map("Breadsticks" -> "$5", "Pizza" -> "$10", "Wings" -> "$5")
          val reverseMap = for((k, v) <- products) yield (v, k)

          reverseMap.size should be (2)

          val vals = reverseMap.values.toArray
          vals(0) should be("Wings")
          vals(1) should be("Pizza")

          val keys = reverseMap.keys.toArray
          keys(0) should be("$5")
          keys(1) should be("$10")
     }


     koan("Maps can be easily filtered"){
          val x = immutable.Map(1 -> "a", 2 -> "b", 3 -> "c")

          x.filter((tuple) => tuple._1 > 1) should be(immutable.Map(2 -> "b", 3 -> "c"))
          x.filterKeys(Set(2, 3)) should be(immutable.Map(2 -> "b", 3 -> "c"))
          x.filterKeys(_ > 2) should be(immutable.Map(3 -> "c"))
          x.take(2) should be(immutable.Map(1 -> "a", 2 -> "b"))
     }
}
