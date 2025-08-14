package donatesystem.util

import scala.util.Try

trait GenericModel[T] {
  def saveAsRecord: Try[Int]
  def deleteRecord: Try[Int]
  def hasRecord:Boolean
  
}

