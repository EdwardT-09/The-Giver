package donatesystem.model


import donatesystem.model
import donatesystem.util.{Database, GenericCompanion, GenericModel}
import scalikejdbc.*
import donatesystem.model.CatalogItem

import scala.util.{Failure, Try}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}

import java.time.LocalDate

class DonatedItems(val id: Int, donation: Donation, val item: CatalogItem, val quantity: Int) extends GenericModel[DonatedItems] with Database:
  def this() = this(0,null,null,0)

  var donationIDProperty =
    ObjectProperty[Int](if donation != null then donation.donationID else 0)
  var itemProperty = ObjectProperty[Int](item.itemIDI)
  var quantityProperty = ObjectProperty[Int](quantity)


  def saveAsRecord: Try[Int] =
    println(s"[DEBUG] donationID = ${donation.donationID}")
    println(s"[DEBUG] itemID = ${item.itemIDI}")
    println(s"[DEBUG] quantity = $quantity")
      Try(DB autoCommit { implicit session =>
        sql"""
             INSERT INTO donated_items (donation_id, item_id, quantity) VALUES
             (${donationIDProperty.value}, ${itemProperty.value}, ${quantityProperty.value})
           """.update.apply()
      })
  end saveAsRecord


  def deleteRecord: Try[Int] =
    if (hasRecord) then
      Try(DB autoCommit { implicit session =>
        sql"""
          DELETE FROM donated_items
          WHERE donated_id = $id
          """.update.apply()
      })
    else
      throw new Exception("There are no records of this record. Deletion failed!")
  end deleteRecord

  def hasRecord: Boolean =
    DB readOnly { implicit session =>
      sql"""
           SELECT * FROM donated_items WHERE donated_id = $id
         """.map(_ => ()).single.apply()
    } match
      case Some(x) => true
      case None => false
  end hasRecord

end DonatedItems


object DonatedItems extends GenericCompanion[DonatedItems] with Database:
  def apply(
             id: Int,
             donation: Donation,
             item: CatalogItem,
             quantity: Int
           ): DonatedItems =
    new DonatedItems(id, donation, item, quantity)

  end apply

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

  def getAllRecords(): List[DonatedItems] =
    DB readOnly { implicit session =>
      sql"""
        SELECT * FROM donated_items
      """.map { rs =>
        val donationID = rs.int("donation_id")
        val itemID = rs.int("item_id")

        val donation = Donation.getRecordByKey(donationID).getOrElse(
          throw new Exception(s"Donation not found: $donationID")
        )

        val item = CatalogItem.getRecordByID(itemID).getOrElse(
          throw new Exception(s"Catalog item not found: $itemID")
        )

        DonatedItems(
          
          rs.int("donated_id"),
          donation,
          item,
          rs.int("quantity")
        )
      }.list()
    }
  def getRecordByKey(key: Any): Option[DonatedItems] =
    key match
        case id: Int => getRecordByID(id)
        case _ => None
  end getRecordByKey


  def getRecordByID(donatedID: Int): Option[DonatedItems] =
    DB.readOnly { implicit session: DBSession =>
      sql"""
      SELECT * FROM donated_items WHERE donated_id = $donatedID
    """.map { rs =>
        val donationID = rs.int("donation_id")
        val itemID = rs.int("item_id")

        val donation = Donation.getRecordByKey(donationID).getOrElse(
          throw new Exception(s"Donation not found: $donationID")
        )

        val item = CatalogItem.getRecordByID(itemID).getOrElse(
          throw new Exception(s"Catalog item not found: $itemID")
        )

        DonatedItems(
          rs.int("donated_id"),
          donation,
          item,
          rs.int("quantity")
        )
      }.single()
    }
end DonatedItems

