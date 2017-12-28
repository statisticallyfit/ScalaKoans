package org.functionalkoans.forscala

import support.KoanSuite
import scala.collection.mutable.LinkedHashMap

class AboutLinkedHashMap extends KoanSuite {


     koan("Sorted map implementation is used when insertion order matters"){
          val countries = LinkedHashMap("Cz" -> "Czechoslovakia", "Fr" -> "France", "Au" -> "Austria",
               "Ger" -> "Germany")

          countries ++= LinkedHashMap("Ru" -> "Russia", "Po" -> "Poland")
          countries += ("Af" -> "Africa")
          countries should be(LinkedHashMap("Cz" -> "Czechoslovakia", "Fr" -> "France", "Au" -> "Austria",
               "Ger" -> "Germany", "Ru" -> "Russia", "Po" -> "Poland", "Af" -> "Africa"))
     }
}
