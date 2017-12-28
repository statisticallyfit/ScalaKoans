package org.functionalkoans.forscala

import support.KoanSuite
import org.scalatest.matchers.ShouldMatchers




class AboutTypeVariance extends KoanSuite with ShouldMatchers {

     class Fruit

     abstract class Citrus extends Fruit

     class Orange extends Citrus

     class Tangelo extends Citrus

     class Apple extends Fruit

     class Banana extends Fruit

     koan("""Using type inference the type that you instantiate it will be the val or var reference type""") {
          class MyContainer[A](a: A)(implicit manifest: scala.reflect.Manifest[A]) {
               private[this] var item = a

               def get:A = item
               def set(a: A) = item = a

               def contents = manifest.runtimeClass.getSimpleName
          }

          val fruitBasket = new MyContainer(new Orange())
          fruitBasket.contents should be("Orange")
     }


     koan("""You can explicitly declare the type variable of the object during instantiation""") {
          class MyContainer[A](a: A)(implicit manifest: scala.reflect.Manifest[A]) {
               private[this] var item = a

               def get:A = item
               def set(a: A) = item = a

               def contents = manifest.runtimeClass.getSimpleName
          }

          // @TODO: ---- understand better the role of Manifest here with implicit
          val fruitBasket = new MyContainer[Fruit](new Orange())
          fruitBasket.contents should be("Fruit")

          val appleBasket = new MyContainer[Apple](new Apple())
          appleBasket.contents should be("Apple")
     }

     koan("You can coerce your object to a type.") {
          class MyContainer[A](a: A)(implicit manifest: scala.reflect.Manifest[A]) {
               private[this] var item = a

               def get:A = item
               def set(a: A) = item = a

               def contents = manifest.runtimeClass.getSimpleName
          }

          // @TODO: ---- Understand how object coercing occurs here
          //todo understand difference between this and next one
          val fruitBasket: MyContainer[Fruit] = new MyContainer(new Orange())
          fruitBasket.contents should be("Fruit")
     }

     // That one probably blew your mind. Now if you assign a type to the instantiation,
     // that's different to the variable type, you'll have problems.  You may want to take time after this
     // o compare this koan with the previous koan to compare and contrast. """) {


     koan("variable type must match assigned type") {
          class MyContainer[A](a: A)(implicit manifest: scala.reflect.Manifest[A]) {
               private[this] var item = a

               def get:A = item
               def set(a: A) = item = a

               def contents = manifest.runtimeClass.getSimpleName
          }

          // Uncomment the following line
          //val fruitBasket:MyContainer[Fruit] = new MyContainer[Orange](new Orange())
     }

     // So, if you want to set a Fruit basket to an orange basket how do we fix that? You make it covariant using +.
     // This will allow you to set the your container to a either a variable with the same type or parent type.
     // In other words, you can assign MyContainer[Fruit] or MyContainer[Citrus]."""


     //todo method example (3)
     koan("Covariant class takes +A as parameter, ie " +
          "(1) Box[Orange] is subtype of Box[Fruit]" +
          "(2) var f: Box[Fruit] = new Box[Orange]() is allowed" +
          "Intuition: So we can take a subtype like Orange and pass it to something that expects its " +
          "super type Fruit. In other words, it is the ability to pass in or use a value of a " +
          "narrower (sub) type in place of a value of some wider (super) type." +
          "The purpose is to take a lower type (Orange) and be able to use it as its higher type (Fruit).") {

          class MyContainer[+A](a: A)(implicit manifest: scala.reflect.Manifest[A]) {
               private[this] val item = a

               def get:A = item

               //note at runtime what we remember is the type on the right hand side
               // of the equals sign (Banana, Orange, Tangelo), not the left (Fruit)
               def contents = manifest.runtimeClass.getSimpleName
          }

          // @TODO: ---- The "+" operator allows the second type to be subclass of first
          val fruitBasket: MyContainer[Fruit] = new MyContainer[Orange](new Orange())
          val bananaBasket: MyContainer[Fruit] = new MyContainer[Banana](new Banana())
          val tangeloBasket: MyContainer[Fruit] = new MyContainer[Tangelo](new Tangelo())
          fruitBasket.contents should be("Orange")
          bananaBasket.contents should be("Banana")
          tangeloBasket.contents should be("Tangelo")
     }

     // The problem with covariance is that you can't mutate, set, or change the object since
     // it has to guarantee that what you put in has to be that type.  In other words the reference is a fruit basket,
     // but we still have to make sure that no other fruit can be placed in our orange basket"""

     koan("Mutating an object is not allowed with covariance because adding another type of fruit" +
          "to an orange basket is incorrect, and we need to protect against that possibility") {

          class MyContainer[+A](a: A)(implicit manifest: scala.reflect.Manifest[A]) {
               private[this] val item = a

               def get:A = item

               def contents = manifest.runtimeClass.getSimpleName
          }

          //important context update: passing an orange container to a value that expects a fruit container
          // is like passing an orange container to a method that expects a fruit container.
          val fruitBasket: MyContainer[Fruit] = new MyContainer[Orange](new Orange())
          fruitBasket.contents should be("Orange")


          // @TODO: ---- #1: setting subtype first is wrong
          // @TODO: ---- #2: setting another type that is not parent of second is wrong
          class NavelOrange extends Orange //Creating a subtype to prove a point
          //val navelOrangeBasket: MyContainer[NavelOrange] = new MyContainer[Orange](new Orange()) //Bad!
          //val tangeloBasket: MyContainer[Tangelo] = new MyContainer[Orange](new Orange()) //Bad!
     }

     // Declaring - indicates contravariance variance.
     // Using - you can apply any container with a certain type to a container with a superclass of that type.
     // This is reverse to covariant.  In our example, we can set a citrus basket to
     // an orange or tangelo basket. Since an orange or tangelo basket is a citrus basket

     //todo method example (3)
     koan("Contravariant class takes -A as parameter, ie:" +
          "(1) Box[Fruit] is a subtype of Box[Orange]" +
          "(2) var f: Box[Orange] = new Box[Fruit]() is allowed" +
          "---- method (item volvo example)" +
          "Intuition: " +
          "So we can take a super type like Fruit or Citrus and pass it to something that " +
          "expects its subtype, like Orange. " +
          "In other words, it is the ability to pass in or use a value of a wider (super) type " +
          "in place of a value of some narrower (sub) type" +
          "The purpose is so that we can pass in a higher type Citrus and be able to use it " +
          "as a lower type like Orange or Tangelo.") {

          class MyContainer[-A](a: A)(implicit manifest: scala.reflect.Manifest[A]) {
               private[this] var item = a

               def set(a: A) = item = a

               //what is remembered at runtime is different than the meaning of contra/covariance:
               // at runtime, we remember what is on the right side of the equals (Citrus) but
               //the point here is that the super type can be converted to the lower type (like
               // Citrus to Orange.
               def contents = manifest.runtimeClass.getSimpleName
          }

          //note: ---- The "-" operator allows the second type to be superclass of first type
          //note we remember Citrus at runtime.
          val citrusBasket: MyContainer[Citrus] = new MyContainer[Citrus](new Orange)
          citrusBasket.contents should be("Citrus")
          val orangeBasket: MyContainer[Orange] = new MyContainer[Citrus](new Tangelo)
          orangeBasket.contents should be("Citrus")
          val tangeloBasket: MyContainer[Tangelo] = new MyContainer[Citrus](new Orange)
          tangeloBasket.contents should be("Citrus")

          val orangeBasketReally: MyContainer[Orange] = tangeloBasket.asInstanceOf[MyContainer[Orange]]
          orangeBasketReally.contents should be("Citrus")
          orangeBasketReally.set(new Orange())
          orangeBasketReally.contents should be("Citrus")
     }

     // Declaring contravariance variance with (-) also means that the container cannot be accessed with a getter or
     // or some other accessor, since that would cause type inconsistency.  In our example, you can put an orange
     // or a tangelo into a citrus basket. Problem is, if you have a reference to an orange basket,
     // and if you believe that you have an orange basket then you shouldn't expect to get a
     // tangelo out of it.
     koan("A reference to a parent type means you cannot anticipate getting a more specific type") {

          class MyContainer[-A](a: A)(implicit manifest: scala.reflect.Manifest[A]) {
               private[this] var item = a

               def set(a: A) = item = a

               def contents = manifest.runtimeClass.getSimpleName
          }

          // important context update: passing a citrus container to a value that expects an orange container
          // is like passing a citrus container to a method that was expecting an orange container.
          val citrusBasket: MyContainer[Citrus] = new MyContainer[Citrus](new Orange)
          citrusBasket.contents should be("Citrus")
          val orangeBasket: MyContainer[Orange] = new MyContainer[Citrus](new Orange)
          orangeBasket.contents should be("Citrus")
          val tangeloBasket: MyContainer[Tangelo] = new MyContainer[Citrus](new Tangelo)
          tangeloBasket.contents should be("Citrus")
     }

     // Declaring neither -/+, indicates invariance variance.  You cannot use a superclass
     // variable reference (\"contravariant\" position) or a subclass variable reference (\"covariant\" position)
     // of that type.  In our example, this means that if you create a citrus basket you can only reference that
     // citrus basket with a citrus variable only.

     koan("invariance means you need to specify the type exactly") {

          class MyContainer[A](a: A)(implicit manifest: scala.reflect.Manifest[A]) {
               private[this] var item = a

               def get:A = item
               def set(a: A) = item = a

               def contents = manifest.runtimeClass.getSimpleName
          }

          val citrusBasket: MyContainer[Citrus] = new MyContainer[Citrus](new Orange)
          citrusBasket.contents should be("Citrus")
     }


     koan(
          """Declaring a type as invariant also means that you can both mutate and access elements
            |from an object of generic type""".stripMargin) {

          // @todo: --- Are you allowed to mutate/access from generic type when using variance, too?
          // @todo: --- Or just for invariance?

          class MyContainer[A](a: A)(implicit manifest: scala.reflect.Manifest[A]) {
               private[this] var item = a

               def get:A = item
               def set(a: A) = item = a

               def contents = manifest.runtimeClass.getSimpleName
          }

          val citrusBasket: MyContainer[Citrus] = new MyContainer[Citrus](new Orange)

          citrusBasket.set(new Orange)
          citrusBasket.contents should be("Citrus")

          citrusBasket.set(new Tangelo)
          citrusBasket.contents should be("Citrus")
     }
}