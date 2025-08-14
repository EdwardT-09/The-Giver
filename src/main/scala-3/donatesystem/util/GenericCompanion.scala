package donatesystem.util

trait GenericCompanion[T]:
  def createTable():Unit
  def getAllRecords():List[T]
  def getRecordByKey(key:Any):Option[T]

