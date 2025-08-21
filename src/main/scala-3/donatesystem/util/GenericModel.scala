package donatesystem.util

import scala.util.Try


// A generic trait that defines database operations for type T in a model
trait GenericModel[T] {
  //save the record in the table for type T
  def saveAsRecord: Try[Int]

  //delete the record in the table for type T
  def deleteRecord: Try[Int]
  
  //check if record exists in the database table 
  def hasRecord:Boolean
  
}

