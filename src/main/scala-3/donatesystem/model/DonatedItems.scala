package donatesystem.model


import donatesystem.model
import donatesystem.util.{Database, GenericObject, GenericModel}
import scalikejdbc.*
import donatesystem.model.CatalogItem

import scala.util.{Failure, Try}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}

import java.time.LocalDate

// DonatedItems class
// donatedItemsIDI as DonatedItems property
// extends GenericModel[DonatedItems] for generic programming
// with Database trait
class DonatedItems(val donatedItemsIDI: Int, donationD: Donation, val itemC: CatalogItem, val quantityI: Int) extends GenericModel[DonatedItems] with Database:
  //auxiliary constructor
  def this() = this(0,null,null,0)

  // create object properties for the donated items form fields
  //check if donation is null
  var donationIDProperty =
  //get donationID if donation is passed in and 0 for null
    ObjectProperty[Int](if donationD != null then donationD.donationIDI else 0)
  var itemProperty = ObjectProperty[Int](itemC.itemIDI)
  var quantityProperty = ObjectProperty[Int](quantityI)

  //save the DonatedItems record in donated_items table
  def saveAsRecord: Try[Int] =
    println(s"[DEBUG] donationID = ${donationD.donationIDI}")
    println(s"[DEBUG] itemID = ${itemC.itemIDI}")
    println(s"[DEBUG] quantity = $quantityI")
      Try(DB autoCommit { implicit session =>
        sql"""
             INSERT INTO donated_items (donation_id, item_id, quantity) VALUES
             (${donationIDProperty.value}, ${itemProperty.value}, ${quantityProperty.value})
           """.update.apply()
      })
  end saveAsRecord


  // delete DonatedItems records
  def deleteRecord: Try[Int] = {
    //Delete only if record exists
    if (hasRecord) then
      Try(DB autoCommit { implicit session =>
        sql"""
          DELETE FROM donated_items
          WHERE donated_id = $donatedItemsIDI
          """.update.apply()
      })
    else
      // if no records found, throw an exception
      throw new Exception("There are no records of this record. Deletion failed!")
  }
  end deleteRecord

  //check if record exists
  def hasRecord: Boolean =
    //select if donatedItemsIDI exists in donated_id of donated_items table
    DB readOnly { implicit session =>
      sql"""
           SELECT * FROM donated_items WHERE donated_id = $donatedItemsIDI
         """.map(_ => ()).single.apply()
    } match
      // if found return true
      case Some(x) => true
      // if not found, return false
      case None => false
  end hasRecord
end DonatedItems

//DonatedItems object
object DonatedItems extends GenericObject[DonatedItems] with Database:
  // create an initialized DonatedItems from the provided values
  def apply(
             id: Int,
             donation: Donation,
             item: CatalogItem,
             quantity: Int
           ): DonatedItems =
    new DonatedItems(id, donation, item, quantity)

  end apply

  //create donated_items table
  //The table links donated items to their corresponding donation and catalog item records with the use of
  //foreign keys
  def createTable() =
    DB autoCommit { implicit session =>
      sql"""
               CREATE TABLE donated_items(
               donated_id int NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1) PRIMARY KEY ,
               donation_id INT,
               item_id INT,
               quantity INT,
               FOREIGN KEY (donation_id) REFERENCES donations(donation_id),
               FOREIGN KEY (item_id) REFERENCES catalog_items(item_id)
               )
             """.execute.apply()
    }
  end createTable

  //retrieve all donated_items records in the table
  def getAllRecords(): List[DonatedItems] =
    DB readOnly { implicit session =>
      sql"""
        SELECT * FROM donated_items
      """.map { rs =>
        //retrieve the donation id
        val donationID = rs.int("donation_id")
        //retrieve the item id
        val itemID = rs.int("item_id")

        //retrieve the donation record using the donation ID or else throw exception if record is not found
        val donation = Donation.getRecordByKey(donationID).getOrElse(
          throw new Exception(s"Donation not found: $donationID")
        )

        //retrieve the catalog item record using the item ID or else throw exception if record is not found
        val item = CatalogItem.getRecordByID(itemID).getOrElse(
          throw new Exception(s"Catalog item not found: $itemID")
        )

        // create an object for DonatedItems and return it using the retrieved values
        DonatedItems(
          rs.int("donated_id"),
          donation,
          item,
          rs.int("quantity")
        )
      }.list() //returns a list 
    }

  // retrieve single donated item record based on key which is id
  // call using this method
  def getRecordByKey(key: Any): Option[DonatedItems] =
    key match
      // treat key as id then pass to getRecordByID
        case id: Int => getRecordByID(id)
        // if nothing return None
        case _ => None
  end getRecordByKey

  // retrieve single donated items record based on key which is donated items id
  def getRecordByID(donatedID: Int): Option[DonatedItems] =
    // retrieve based on the donated item id provided
    DB.readOnly { implicit session: DBSession =>
      sql"""
      SELECT * FROM donated_items WHERE donated_id = $donatedID
    """.map { rs =>
        //retrieve the donation id
        val donationID = rs.int("donation_id")
        //retrieve the item id
        val itemID = rs.int("item_id")

        //retrieve the donation record using the donation ID or else throw exception if record is not found
        val donation = Donation.getRecordByKey(donationID).getOrElse(
          throw new Exception(s"Donation not found: $donationID")
        )

        //retrieve the catalog item record using the item ID or else throw exception if record is not found
        val item = CatalogItem.getRecordByID(itemID).getOrElse(
          throw new Exception(s"Catalog item not found: $itemID")
        )

        // create an object for DonatedItems and return it using the retrieved values
        DonatedItems(
          rs.int("donated_id"),
          donation,
          item,
          rs.int("quantity")
        )
      }.single() //return only one record
    }
end DonatedItems

