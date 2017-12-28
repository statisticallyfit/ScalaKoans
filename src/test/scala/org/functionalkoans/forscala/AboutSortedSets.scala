package org.functionalkoans.forscala

import support.KoanSuite
import scala.collection.immutable.SortedSet
import scala.collection.immutable.TreeSet

class AboutSortedSets extends KoanSuite {

     koan ("SortedSets are sorted upon creation"){
          val s = SortedSet(10, 4, 8, 2)
          s.mkString(", ") should be("2, 4, 8, 10")
     }

     koan ("SortedSets allow only distinct elements"){
          val s = SortedSet(10, 4, 8, 2, 3, 4)
          s.mkString(", ") should be("2, 3, 4, 8, 10")
     }

     koan("SortedSets must use an implicit ordering. " +
          "If that ordering is not defined, SortSet will not work"){

          class Person (var name: String)

          val aleka = new Person("Aleka")
          val christina = new Person("Christina")
          val molly = new Person("Molly")
          val tyler = new Person("Tyler")

          //this doesn't work
          //val s = SortedSet(molly, tyler, christina, aleka)
     }

     koan("SortedSets must use an implicit ordering. " +
          "If that ordering is defined, SortSet will work"){

          class Person (var name: String) extends Ordered [Person]{

               override def toString = name

               // return 0 if the same name, negative if this < that, positive if this > that
               def compare (that: Person) = {
                    if (this.name == that.name) 0
                    else if (this.name > that.name) 1
                    else -1
               }
          }

          // now this works
          val aleka = new Person("Aleka")
          val christina = new Person("Christina")
          val molly = new Person("Molly")
          val tyler = new Person("Tyler")

          val s = SortedSet(molly, tyler, christina, aleka)
     }
}
