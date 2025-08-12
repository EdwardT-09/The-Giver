package donatesystem.model

import donatesystem.util.Database
import scalikejdbc.*

import scala.util.{Failure, Success, Try}
import scalafx.beans.property.{BooleanProperty, IntegerProperty, ObjectProperty, StringProperty}

import java.time.LocalDate

abstract class DonationItem(itemIDI :Int, nameS:String, categoryS:String, perishableB:Boolean, quantityI:Int):
  
  def this() = this(0, "", "", false, 0)
  val nameProperty = new StringProperty(nameS)
  val categoryProperty = new StringProperty(categoryS)
  val perishableProperty =  BooleanProperty(perishableB)
  val quantityProperty =  IntegerProperty(quantityI)
  
  def saveAsRecord: Try[Int]

  def deleteRecord: Try[Int] 

  def hasRecord: Boolean 
  
end DonationItem
  