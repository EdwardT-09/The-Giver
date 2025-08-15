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
    if (!hasDBInitialized) then
        Administrator.createTable()
        Donor.createTable()
        CatalogItem.createTable()
        Food.createTable()
        Beverage.createTable()
        Donation.createTable()
        DonatedItems.createTable()

  end dbSetUp
  
  def hasDBInitialized:Boolean =
    (DB.getTable ("ADMINISTRATORS"), DB.getTable("DONORS"),DB.getTable("FOODS"), DB.getTable("BEVERAGES"), DB.getTable("DONATIONS"), DB.getTable("DONATED_ITEMS")) match
      case (Some(_), Some(_),Some(_), Some(_), Some(_), Some(_)) => true
      case _ => false
  end hasDBInitialized
end Database