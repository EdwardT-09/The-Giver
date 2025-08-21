package donatesystem.util

import scalikejdbc.*
import donatesystem.model.{Administrator, Beverage, CatalogItem, DonatedItems, Donation, Donor, Food}


trait Database:
  // derby driver
  val derbyDriver = "org.apache.derby.jdbc.EmbeddedDriver"

  // database URL
  val dbURL = "jdbc:derby:myDB;create=true;"

  Class.forName(derbyDriver)

  //create a singleton database connection ppol
  ConnectionPool.singleton(dbURL,"DONATESYSTEM", "DONATESYSTEM")

  // to allows an automatic database session for all the queries
  given AutoSession = AutoSession

//Database object
object Database extends Database:
  //if the database has not been initialized (administrator table does not exists) then create the table
  def dbSetUp() =
  //check if all table has been initialized
  //this reduces computation when all table has been initialized
    if (!hasDBInitialized) then
      //if hasDBInitialized is false then check which table does not exists
      //if exists, move on. if it does not exists, then create a table.
      if(!hasTable("ADMINISTRATORS"))
        Administrator.createTable()
      if (!hasTable("DONORS"))
        Donor.createTable()
      if (!hasTable("CATALOG_ITEMS"))
        CatalogItem.createTable()
      if (!hasTable("FOODS"))
        Food.createTable()
      if (!hasTable("BEVERAGES"))
        Beverage.createTable()
      if (!hasTable("DONATIONS"))
        Donation.createTable()
      if (!hasTable("DONATED_ITEMS"))
        DonatedItems.createTable()

  end dbSetUp

  //check if any database does not exists
  def hasDBInitialized:Boolean =
    (DB.getTable ("ADMINISTRATORS"), DB.getTable("DONORS"),DB.getTable("FOODS"), DB.getTable("BEVERAGES"), DB.getTable("DONATIONS"), DB.getTable("DONATED_ITEMS"),DB.getTable("CATALOG_ITEMS")) match
      //if all are found, return true
      case (Some(_), Some(_),Some(_), Some(_), Some(_), Some(_), Some(_)) => true
      //if any are not found, return false
      case _ => false
  end hasDBInitialized

  //check if table exists
  def hasTable(tableName:String):Boolean =
    DB.getTable(tableName.toUpperCase()) match
      //if table exists then return true
      case Some(_) => true
      //if does not exists then return false
      case None => false
end Database
