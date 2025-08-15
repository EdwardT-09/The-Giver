package donatesystem.model

import java.time.LocalDate
import donatesystem.util.{Database, GenericCompanion, GenericModel}
import scalikejdbc.*
import donatesystem.model.Donor
import scala.util.{Failure, Success, Try}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}

class Donation(val donationID: Int, donor: Donor, donationDate:LocalDate) extends GenericModel[Donation] with Database:
  def this() = this(0, null,  null)

  def donorIDProperty =
    ObjectProperty[Int](if donor != null then donor.donor_IDI else 0)
  var donationDateProperty =  ObjectProperty[LocalDate](donationDate)


  def saveAsRecord: Try[Int] =
    if (!hasRecord) then
      Try(DB autoCommit { implicit session =>
        sql"""
             INSERT INTO donations (donor_id, date) VALUES
             (${donorIDProperty.value},  ${donationDateProperty.value})
           """.update.apply()

      })
    else
      throw new Exception("An error has occured. The record was not saved")
  end saveAsRecord


  def deleteRecord: Try[Int] =
    if (hasRecord) then
      Try(DB autoCommit { implicit session =>
        sql"""
          DELETE FROM donations
          WHERE donation_id = $donationID
          """.update.apply()
      })
    else
      throw new Exception("There are no records of this donation. Deletion failed!")
  end deleteRecord

  def hasRecord: Boolean =
    DB readOnly { implicit session =>
      sql"""
           SELECT * FROM donations WHERE donation_id=${this.donationID}
         """.map(_ => ()).single.apply()
    } match
      case Some(x) => true
      case None => false
  end hasRecord
end Donation


object Donation extends GenericCompanion[Donation] with Database:
  def apply(
             donationID: Int,
             donor: Donor,
             date:LocalDate
           ): Donation = 
    new Donation (donationID, donor, date)

  end apply

  def createTable() =
    DB autoCommit { implicit session =>
      sql"""
           CREATE TABLE donations(
           donation_id int NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
           donor_id INT REFERENCES donors(donor_id),
           date DATE,
           FOREIGN KEY (donor_id) REFERENCES donors(donor_id)
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

  def getAllRecords(): List[Donation] =
    DB readOnly { implicit session =>
      sql"""
      SELECT * from donations
      """.map(rs =>
        val donor = Donor.getRecordByKey(rs.int("donor_id")).getOrElse(Donor(0, null, null, null, null, null))
        Donation(rs.int("donation_id"), donor, rs.localDate("date"))
      ).list.apply()
    }


  def getRecordByKey(key: Any): Option[Donation] =
    key match
      case id:Int => getRecordByID(id)
      case _ => None
  end getRecordByKey

  def getRecordByID(id:Int): Option[Donation] =
    DB readOnly { implicit session =>
      sql"""
           SELECT * FROM donations WHERE donation_id = $id
         """.map(rs =>
        val donor = Donor.getRecordByKey(rs.int("donor_id")).getOrElse(Donor(0, null, null, null, null, null))
        Donation(
          rs.int("donation_id"), 
          donor, 
          rs.localDate("date"))
        ).single.apply()
    }
end Donation
