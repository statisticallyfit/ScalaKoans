package org.functionalkoans.forscala

import support.KoanSuite
import scala.collection.SortedMap

class AboutSortedMaps extends KoanSuite {


     koan("Sorted map implementation is used when sorting or insertion order DO matter"){
          val grades1 = SortedMap("Kim" -> 90, "Al" -> 85, "Melissa" -> 95,
               "Emily" -> 91, "Hannah" -> 92)

          grades1 should be(Map("Al" -> 85, "Emily" -> 91, "Hannah" -> 92, "Kim" -> 90, "Melissa" -> 95))

          val grades2 = SortedMap(90 -> "Kim", 85 -> "Al", 95 -> "Melissa",
               91 -> "Emily", 92 -> "Hannah")

          grades2 should be(Map(85 -> "Al", 90 -> "Kim", 91 -> "Emily", 92 -> "Hannah", 95 -> "Melissa"))
     }
}
