package donatesystem.util

import scalikejdbc.*
import donatesystem.model.Administrator


trait Database:
  // derby driver
  val derbyDriver = "org.apache.derby.jdbc.EmbeddedDriver"

  // database URL
  val dbURL = "jdbc:derby:myDB; create=true;"

  Class.forName(derbyDriver)

  //create a singleton database connection ppol
  ConnectionPool.singleton(dbURL,"me", "mine")

  // to allows an automatic database session for all the queries
  given AutoSession = AutoSession

//Database object
object Database extends Database:
  //if the database has not been initialized (administrator table does not exists) then create the table
  def dbSetUp() =
    if (!hasDBInitialized) then 
      Administrator.createTable()

  def hasDBInitialized: Boolean = {
    //check if the administrator table exists
    DB getTable "administrator" match
      //if found return true
      case Some(x) => true
      // if none found return false
      case None => false

  }

