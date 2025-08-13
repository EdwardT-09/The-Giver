package donatesystem.model

import java.time.LocalDate
import donatesystem.util.Database
import scalikejdbc.*

import scala.util.{Failure, Success, Try}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import scalafx.collections.ObservableBuffer

class Donation(val donationID: Int, donor: Donor, items:List[DonationItem], donationDate:LocalDate) extends Database:
  def this() = this(0, null, null, null)

  var donorIDProperty = IntegerProperty(donor.donor_IDI)
  val itemsProperty: ObservableBuffer[DonationItem] = ObservableBuffer(items: _*)
  var donationDateProperty =  ObjectProperty[LocalDate](donationDate)


  def saveAsRecord: Try[Int] =
    if (!hasRecord) then
      Try(DB autoCommit { implicit session =>
        sql"""
             INSERT INTO donations (donorID, items, date) VALUES
             (${donorIDProperty.value}, ${itemsProperty} ${donationDateProperty.value})
           """.update.apply()

      })
    else
      throw new Exception("An error has occured. The record was not saved")
  end saveAsRecord


  def deleteRecord(donationID:Int): Try[Int] =
    if (hasRecord) then
      Try(DB autoCommit { implicit session =>
        sql"""
          DELETE FROM donations
          WHERE donationID = $donationID
          """.update.apply()
      })
    else
      throw new Exception("There are no records of this email. Deletion failed!")
  end deleteRecord

  def hasRecord: Boolean =
    DB readOnly { implicit session =>
      sql"""
           SELECT * FROM donations WHERE email=${this.donationID}
         """.map(_ => ()).single.apply()
    } match
      case Some(x) => true
      case None => false
  end hasRecord
end Donation


object Donation extends Database:
  def apply(
             donationID: Int,
             donor: Donor,
             items:List[DonationItem],
             donationDate:LocalDate
           ): Donation =
    new Donation (donationID, donor, items, donationDate)

  end apply

  def createTable() =
    DB autoCommit { implicit session =>
      sql"""
           CREATE TABLE donations(
           donationID int NOT NULL GENERATED ALWAYS AS IDENTITY,
           donorID INT REFERENCES donors(donor_id),
           email varchar(64),
           password varchar(64)
           )
         """.execute.apply()
    }
  end createTable

  def dropTable() =
    DB autoCommit { implicit session =>
      sql"""
           DROP TABLE donations
         """.execute.apply()
    }
  end dropTable

  def getAllAdminRecord: List[Administrator] =
    DB readOnly { implicit session =>
      sql"""
      SELECT * from donations
      """.map(rs => Administrator(
        rs.int("user_id"),
        rs.string("fName"),
        rs.string("email"),
        rs.string("password")
      )).list.apply()
    }

  def getRecordByEmail(email: String): Option[Administrator] =
    DB readOnly { implicit session =>
      sql"""
           SELECT * FROM donations WHERE email = $email
         """.map(rs =>
        Administrator(
          rs.int("user_id"),
          rs.string("fname"),
          rs.string("email"),
          rs.string("password"),
        )).single.apply()
    }
end Donation
