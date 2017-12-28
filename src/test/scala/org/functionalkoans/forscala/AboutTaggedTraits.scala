package org.functionalkoans.forscala

import support.KoanSuite

class AboutTaggedTraits extends KoanSuite {

     koan("A tagging trait is a way to group classes or objects. The 'sealed' keyword means there are no other " +
          "subtypes of Color other than the ones mentioned. A case object is similar to a case class. " +
          "You get pattern-matching benefits and nice output when you convert it to String"){

          //note case object is just like case class except it produces an object instead
          // of a class. Mostly used for pattern matching? todo help

          //help todo how is this tagging trait? Because both trait and
          // object have same name: Color?
          sealed trait Color
          case object Red extends Color
          case object Green extends Color
          case object Blue extends Color

          object Color {
               val values = Vector(Red, Green, Blue)
          }

          def display(color: Color) = color match {
               case Red => s"It's $color"
               case Green => s"It's $color"
               case Blue => s"It's $color"
          }


          Color.values.map(display) should be (Vector("It's Red", "It's Green", "It's Blue"))
          println(Color.values.map(display))
     }
}
