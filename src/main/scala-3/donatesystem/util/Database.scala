package donatesystem.util

import scalikejdbc.*
import donatesystem.model.Administrator
import donatesystem.model.Donor


trait Database:
  // derby driver
  val derbyDriver = "org.apache.derby.jdbc.EmbeddedDriver"

  // database URL
  val dbURL = "jdbc:derby:myDB;create=true;"

  Class.forName(derbyDriver)

  //create a singleton database connection ppol
  ConnectionPool.singleton(dbURL,"ME", "MINE")

  // to allows an automatic database session for all the queries
  given AutoSession = AutoSession

//Database object
object Database extends Database:
  //if the database has not been initialized (administrator table does not exists) then create the table
  def dbSetUp() =
    if (!hasDBInitialized) then
        Administrator.createTable()
        Donor.createTable()
  end dbSetUp
  
  def hasDBInitialized:Boolean =
    (DB.getTable ("ADMINISTRATOR"), DB.getTable("DONOR")) match
      case (Some(_), Some(_)) => true
      case _ => false
  end hasDBInitialized
end Database