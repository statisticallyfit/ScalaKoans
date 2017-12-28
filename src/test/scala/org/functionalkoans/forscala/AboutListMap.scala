package org.functionalkoans.forscala

import support.KoanSuite
import scala.collection.mutable.ListMap

class AboutListMap extends KoanSuite {


     koan("ListMap can be both immutable and mutable. ListMap inserts at the head"){
          val names = ListMap(1 -> "Marina", 2 -> "Sasha", 3 -> "Roxanne", 4 -> "Julie",
               5 -> "Gwen", 6 -> "Mallory")

          names ++= ListMap(7 -> "Vivian", 8 -> "Brionna", 9 -> "Olga")
          names += (10 -> "Annabeth")

          // @todo: Understand ListMap's weird way of appending order

          names should be(ListMap(10 -> "Annabeth", 7 -> "Vivian", 6 -> "Mallory",
               4 -> "Julie", 2 -> "Sasha", 1 -> "Marina", 3 -> "Roxanne", 5 -> "Gwen",
               9 -> "Olga", 8 -> "Brionna"))
     }
}
