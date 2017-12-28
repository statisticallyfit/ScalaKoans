package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSuite

/**
  * AboutByNameParameter
  * Koan to help understand by name parameters in Scala
  * Prerequisites: AboutEither, AboutHigherOrderFunctions, AboutExceptions,
  *                About Pattern Matching, AboutApply
  */
class AboutByNameParameter extends KoanSuite {

     koan(
          """() => Int is a Function type that takes a Unit type. Unit is known as 'void' to a Java programmer. The function
            | and returns an Int. You can place this as a method parameter so that you can you use it as a block, but still
            | it doesn't look quite right.""") {

          def calc(x: () => Int): Either[Throwable, Int] = {
               try {
                    Right(x()) //An explicit call the the x function
               } catch {
                    case b: Throwable => Left(b)
               }
          }

          val y = calc {() => //Having explicitly declaring that Unit is a parameter with ()
               14 + 15
          }

          // @todo: ---- understand where "case **b**" came from - is 'b' a keyword?
          // @todo: ---- understand what "x: () => Int" means
          y should be (Right(29))

          // --------------------------------------------------------------------------------------------
          // todo: ---- study this example to learn Left, Right, Either
          // todo: ---- at website: http://alvinalexander.com/scala/scala-either-left-right-example-option-some-none-null
          /*object EitherLeftRightExample  {

            /**
              * A simple method to demonstrate how to declare that a method returns an Either,
              * and code that returns a Left or Right.
              */
            def divideXByY(x: Int, y: Int): Either[String, Int] = {
              if (y == 0) Left("Dude, can't divide by 0")
              else Right(x / y)
            }

            // a few different ways to use Either, Left, and Right
            println("==================================================")
            println()
            println(divideXByY(1, 0))
            println(divideXByY(1, 1))
            println()
            println("==================================================")
            divideXByY(1, 0) match {
              case Left(s) => println("Answer: " + s)
              case Right(i) => println("Answer: " + i)
            }

          }*/
     }


     koan(
          """A by-name parameter does the same thing as a previous koan but there is no need to explicitly
            | handle Unit or (). This is used extensively in scala to create blocks.""") {


          // @todo: ---- so call by name means no () before the arrow? Why did we do that before?
          // @todo: ---- meaning of call by name?

          def calc(x: => Int): Either[Throwable, Int] = {   //x is a call by name parameter
               try {
                    Right(x)
               } catch {
                    case b: Throwable => Left(b)
               }
          }

          val y = calc {                                    //This looks like a natural block
               println("Here we go!")                          //Some superfluous call
               val z = List(1, 2, 3, 4)                        //Another superfluous call
               49 + 20
          }

          y should be (Right(49+20))

     }

     koan("""By name parameters can also be used with an Object and apply to make interesting block-like calls""") {
          object PigLatinizer {
               def apply(x: => String) = x.tail + x.head + "ay"
          }

          val result = PigLatinizer {
               val x = "pret"
               val z = "zel"
               x ++ z //concatenate the strings
          }

          result should be ("retzelpay")
     }
}
