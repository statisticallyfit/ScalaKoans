package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSuite
import scala.reflect.runtime.universe._

class Candy

class AboutTypeTags extends KoanSuite {


     koan("""TypeTags can be used to determine a type used
            |   before it erased by the VM by using an implicit TypeTag argument.""") {


          // @todo: ---- understand the scala Type API (and other APIs to get simple class name),
          // @todo: ---- understand why "implicit" is needed because TypeTag already takes the type T explicitly

          def inspect[T](l: List[T])(implicit tt: TypeTag[T]) = (tt.tpe.typeSymbol.name.decoded)
          val list1 = 1 :: 2 :: 3 :: 4 :: 5 :: Nil
          inspect(list1) should be("Int")

          // @doneby statisticallyfit
          val list2 = List(1) :: List(2) :: List(3) :: List(4) :: List(5) :: Nil
          inspect(list2) should be("List")

          val list3 = List(1, 2.0, 33D, 4000L)
          inspect(list3) should be("Double")
     }

     // @doneby statisticallyfit
     koan("""TypeTags can have arguments """) {


          // @todo: ---- go back to scala school overview guide and understand what TypeRef and args mean here

          def inspect[T](x: T)(implicit tag: TypeTag[T]) = tag.tpe match {
               case TypeRef(_, _, args) => args
                    println(tag.tpe)
                    println(s"type of $x has type arguments $args")
          }

          val lol = List(1) :: List(2) :: List(3) :: List(4) :: List(5) :: Nil

          //inspect(lol) should be(List[Int])   /*typeOf[List[List[Int]]]*/
          println("============================")
          println()
          println(inspect(lol))
          println()
          println("============================")
          true should be (true)
     }

     koan("""TypeTags can also be """) {

          // no more implicit due to new declaration: TypeTag[T]

          def inspect[T : TypeTag](l: List[T]) = typeOf[T].typeSymbol.name.decoded
          val list = 1 :: 2 :: 3 :: 4 :: 5 :: Nil
          inspect(list) should be("Int")
     }

     koan("""TypeTags can be attached to classes. TypeTags have other meta-information about
            |  the type erased""") {
          class Barrel[T](implicit tt:TypeTag[T]) {
               def +(t: T) = "1 %s has been added".format(tt.tpe.typeSymbol.name.decoded) //Simple-name of the class erased
          }
          val candyBarrel = new Barrel[Candy]
          (candyBarrel + new Candy) should be("1 Candy has been added")
     }
}