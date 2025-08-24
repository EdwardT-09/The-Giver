package donatesystem.util

// A generic trait that defines database operations for type T
trait GenericObject[T]:
  
  //retrieve all record of type T 
  def getAllRecords():List[T]
  
  //retrieve record by key
  def getRecordByKey(key:Any):Option[T]

