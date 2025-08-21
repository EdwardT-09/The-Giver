package donatesystem.model

import java.time.LocalDate
import donatesystem.util.{Database, GenericObject, GenericModel}
import scalikejdbc.*
import donatesystem.model.Donor
import scala.util.{Failure, Success, Try}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}


// Donation class
// donationID and donorD as Donation property
// extends GenericModel[Donation] for generic programming
// with Database trait
class Donation(val donationIDI: Int, val donorD: Donor, donationDateLD:LocalDate) extends GenericModel[Donation] with Database:
  //auxiliary constructor
  def this() = this(0, null,  null)

  // create object properties for the donation form fields
  //check if donor is null
  def donorIDProperty =
    //get donor id if donor is passed in and 0 for null
    ObjectProperty[Int](if donorD != null then donorD.donor_IDI else 0)
  var donationDateProperty =  ObjectProperty[LocalDate](donationDateLD)


  //save the Donation record in donations table
  def saveAsRecord: Try[Int] =
    //if does not have record
    if (!hasRecord) then
      // create a new record
      Try(DB autoCommit { implicit session =>
        sql"""
             INSERT INTO donations (donor_id, date) VALUES
             (${donorIDProperty.value},  ${donationDateProperty.value})
           """.update.apply()

      })
    else
      // if records found, return Success(0)
      Success(0)
  end saveAsRecord

  // delete Donation records
  def deleteRecord: Try[Int] =
    //Delete only if record exists
    if (hasRecord) then
      Try(DB autoCommit { implicit session =>
        sql"""
          DELETE FROM donations
          WHERE donation_id = $donationIDI
          """.update.apply()
      })
    else
      // if no records found, throw an exception
      throw new Exception("There are no records of this donation. Deletion failed!")
  end deleteRecord

  //check if record exists
  def hasRecord: Boolean =
    //select if donationIDI exists in donation_id of donations table
    DB readOnly { implicit session =>
      sql"""
           SELECT * FROM donations WHERE donation_id=${this.donationIDI}
         """.map(_ => ()).single.apply()
    } match
      // if found return true
      case Some(x) => true
      // if not found, return false
      case None => false
  end hasRecord
end Donation


//Donation object
object Donation extends GenericObject[Donation] with Database:
  // create an initialized Donation from the provided values
  def apply(
             donationID: Int,
             donor: Donor,
             date:LocalDate
           ): Donation = 
    new Donation (donationID, donor, date)

  end apply

  //create donations table
  //The table links donation to their corresponding donors using foreign keys
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

  //retrieve all donation records in the table
  def getAllRecords(): List[Donation] =
    DB readOnly { implicit session =>
      sql"""
      SELECT * from donations
      """.map(rs =>
        //retrieve the associated donor record using donor_id
        //if no record is found, a default donor object is created to avoid errors
        val donor = Donor.getRecordByKey(rs.int("donor_id")).getOrElse(Donor(0, null, null, null, null, null))
        // create an object for Donation and return it using the retrieved values
        Donation(rs.int("donation_id"), donor, rs.localDate("date"))
      ).list.apply()
    }

  // retrieve single donation record based on key which is id
  // call using this method
  def getRecordByKey(key: Any): Option[Donation] =
    key match
      // treat key as id then pass to getRecordByID
      case id:Int => getRecordByID(id)
      // if nothing return None
      case _ => None
  end getRecordByKey

  // retrieve single donation record based on key which is donation id
  def getRecordByID(id:Int): Option[Donation] =
    DB readOnly { implicit session =>
      sql"""
           SELECT * FROM donations WHERE donation_id = $id
         """.map(rs =>
        //retrieve the associated donor record using donor_id
        //if no record is found, a default donor object is created to avoid errors
        val donor = Donor.getRecordByKey(rs.int("donor_id")).getOrElse(Donor(0, null, null, null, null, null))
        // create an object for Donation and return it using the retrieved values
        Donation(
          rs.int("donation_id"), 
          donor, 
          rs.localDate("date"))
        ).single.apply()
    }
end Donation
