package donatesystem.util

import scalikejdbc.*
import donatesystem.model.Administrator


trait Database:
  val derbyDriver = "org.apache.derby.jdbc.EmbeddedDriver"
  val dbURL = "jdbc:derby:myDB; create=true;"

  Class.forName(derbyDriver)
  ConnectionPool.singleton(dbURL,"me", "mine")
  given AutoSession = AutoSession

object Database extends Database:
  def dbSetUp() =
    if (!hasDBInitialized) then 
      Administrator.createTable()
//    else
//      Administrator.dropTable()

  def hasDBInitialized: Boolean = {
    DB getTable "administrator" match
      case Some(x) => true
      case None => false

  }

