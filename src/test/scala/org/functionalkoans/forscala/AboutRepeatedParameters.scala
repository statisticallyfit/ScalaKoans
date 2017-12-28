package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSuite

class AboutRepeatedParameters extends KoanSuite {

     // cool, the Any with * lets you have infinite parameters
     def repeatedParameterMethod(x: Int, y: String, z: Any*) = {

          println("==================================")
          println(z.mkString(", "))
          println("==================================")

          "%d %ss can give you %s".format(x, y, z.mkString(", "))
     }

     koan( """A repeated parameter must be the last parameter and this will
             | let you add as many extra parameters as needed""".stripMargin('|')) {
          repeatedParameterMethod(3, "egg", "a delicious sandwich", "protein", "high cholesterol") should be("3 eggs can " +
               "give you a delicious sandwich, protein, high cholesterol")
     }

     koan("A repeated parameter can accept a collection as the last parameter but will be considered a single object") {
          repeatedParameterMethod(3, "egg", List("a delicious sandwich", "protein", "high cholesterol")) should be("3 eggs " +
               "can give you List(a delicious sandwich, protein, high cholesterol)")
     }


     //todo: http://stackoverflow.com/questions/10842851/scala-expand-list-of-tuples-into-variable-length-argument-list
     // -of-tuples
     koan("A repeated parameter can accept a collection,and if you want it expanded, add :_*") {
          repeatedParameterMethod(3, "egg", List("a delicious sandwich", "protein", "high cholesterol"):_*) should be("3 eggs " +
               "can give you a delicious sandwich, protein, high cholesterol")
     }


     koan("A traversable can be passed to an * argument using :_* operator"){

          def sum (args: Int*): Int = {
               var result = 0
               for (arg <- args)
                    result += arg
               result
          }

          val a = List(1, 10, -9, 4, 3, -2, 3, 4)
          sum(a:_*) should be(14)
     }


     koan("The :_* can even be used in recursion"){

          def recursiveSum(args: Int*) : Int = {
               if (args.length == 0)
                    0
               else
                    args.head + recursiveSum(args.tail: _*)
          }

          val a = List(1, 10, -9, 4, 3, -2, 3, 4)
          recursiveSum(a:_*) should be(14)
     }
}
