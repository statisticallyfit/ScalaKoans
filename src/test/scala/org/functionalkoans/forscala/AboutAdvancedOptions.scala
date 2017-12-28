package org.functionalkoans.forscala

import org.functionalkoans.forscala.support.KoanSuite

class AboutAdvancedOptions extends KoanSuite {

     koan("Option is more than just a replacement of null, its also a collection") {

          // @todo: ---- Is this Some(10) actually a list of 1 to 10?
          // @todo: ---- Understand how these maps are applied to the Some...
          Some(10) map { _ + 10} should be(Some(20))
          Some(10) filter { _ == 10} should be(Some(10))
          Some(Some(10)) flatMap { _ map { _ + 10}} should be(Some(20))

          var newValue1 = 0
          Some(20) foreach { newValue1 = _} // @todo: ---- why when printing out in console it gives 0 not 20?
          newValue1 should be(20)
          // @todo: ---- understand how values are extracted from Somes

          // @todo: ---- understand options better, how value 0 is inferred
          var newValue2 = 0
          None foreach { newValue2 = _}
          newValue2 should be(0)
     }

     koan("Using Option to avoid if checks for null") {
          //the ugly version
          def makeFullName(firstName: String, lastName: String) = {
               if (firstName != null) {
                    if (lastName != null) {
                         firstName + " " + lastName
                    } else {
                         null
                    }
               } else {
                    null
               }
          }
          makeFullName("Nilanjan", "Raychaudhuri") should be("Nilanjan Raychaudhuri")
          makeFullName("Nilanjan", null) should be(null)

          //the pretty version
          def makeFullNamePrettyVersion(firstName: Option[String], lastName: Option[String]) = {
               firstName flatMap {
                    fname =>
                         lastName flatMap {
                              lname =>
                                   Some(fname + " " + lname)
                         }
               }
          }

          // @todo: --- understand this method better - how do fname and lname variables get assigned?
          makeFullNamePrettyVersion(Some("Nilanjan"), Some("Raychaudhuri")) should be(Some("Nilanjan Raychaudhuri"))
          makeFullNamePrettyVersion(Some("Nilanjan"), None) should be(None)
     }

     koan("Using in for comprehension") {
          val values = List(Some(10), Some(20), None, Some(15))
          val newValues = for {
               someValue <- values
               value <- someValue
          } yield value
          newValues should be(List(10, 20, 15))
     }
}
