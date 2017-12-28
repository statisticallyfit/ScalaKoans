package org.functionalkoans.forscala

import support.KoanSuite


class AboutAbstractClasses extends KoanSuite {

     koan("Abstract classes have both concrete and abstract properties that are" +
          "referenced in all child classes"){

          abstract class Pet (name: String) {
               val greeting: String
               var age: Int
               def sayHello { println(greeting) }
               override def toString = s"I say $greeting, and I'm $age"
          }

          class Dog (name: String) extends Pet (name) {
               val greeting = "Woof"
               var age = 2
          }

          class Cat (name: String) extends Pet (name) {
               val greeting = "Meow"
               var age = 5
          }

          //todo: what is App, what does it do? Why doesn't this compile without it?
          object AbstractFieldsDemo extends App {

               val dog = new Dog("Fido")
               val cat = new Cat("Morris")
               dog.sayHello
               cat.sayHello

               dog.toString should be("I say Woof, and I'm 2")
               cat.age = 10
               cat.age should be (10)
          }
     }



     koan("Concrete val fields assigned in abstract classes must be overridden in the base class. " +
          "Concrete var fields assigned in abstract class don't need the overridden keyword."){

          // NOTE: Once the fields are assigned values in the abstract classs,
          // they exist. But if not assigned values, they do not exist.
          abstract class Animal {
               val greeting = "Hello"
               var age = 0
          }
          class Dog extends Animal {
               override val greeting = "Woof"     // the override keyword to reassign val field
               age = 2                            // the no-keyword var field reassigned
          }
     }
}

