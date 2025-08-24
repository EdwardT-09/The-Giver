package donatesystem.model

import donatesystem.util.{Database, GenericObject}
import scalikejdbc.*
import scala.util.{Failure, Try}
import scalafx.beans.property.{ObjectProperty, StringProperty}
import java.time.LocalDate

// Donor class
// donor_IDI as Donor property
// with Database trait
class Donor(var donorID: Int, name: String, email:String, birthday:LocalDate, contactNo: String, occupation: String) extends  Database:
  //auxiliary constructor
  def this() = this(0, null, null, null, null, null)

  // create object properties for the donor form fields
  var nameProperty = new StringProperty(name)
  var emailProperty = new StringProperty(email)
  var birthdayProperty = ObjectProperty[LocalDate](birthday)
  var contactNoProperty = new StringProperty(contactNo)
  var occupationProperty = new StringProperty(occupation)
  
  //assign emailS to original email to be used during editing of email
  private val originalEmail: String = email


  //save the Donor record in donors table
  def saveAsRecord(): Try[Int] =
    //if does not have record
    if (!hasRecord()) then
      // create a new record
      Try(DB autoCommit { implicit session =>
        val newID = sql"""
             INSERT INTO donors (name, email, birthday, contact_no, occupation) VALUES
             (${nameProperty.value}, ${emailProperty.value}, ${birthdayProperty.value}, ${contactNoProperty.value}, ${occupationProperty.value})
           """.updateAndReturnGeneratedKey().toInt
          this.donorID = newID
          newID
      }
     )
    else
      // if record exists, update the respective fields
      Try(DB autoCommit {
        sql"""
             UPDATE donors
             SET
             name = ${nameProperty.value},
             email = ${emailProperty.value},
             birthday = ${birthdayProperty.value},
             contact_no = ${contactNoProperty.value},
             occupation = ${occupationProperty.value}
             WHERE donor_id = $donorID
           """.update.apply()
      })
  end saveAsRecord

  // delete Donor records
  def deleteRecord(): Try[Int] =
    //Delete only if record exists
    if (hasRecord()) then
      Try(DB autoCommit { implicit session =>
        sql"""
          DELETE FROM donors
          WHERE donor_id = $donorID
          """.update.apply()
      })
    else
      // if no records found, throw an exception
      Failure( new Exception("There are no records of this email. Deletion failed!"))
  end deleteRecord

  //check if record exists
  def hasRecord(): Boolean =
    //select if donor_IDI exists in donor_id of donors table
    DB readOnly { implicit session =>
      sql"""
           SELECT * FROM donors WHERE donor_id = $donorID
         """.map(_ => ()).single.apply()
    } match
      // if found return true
      case Some(x) => true
      // if not found, return false
      case None => false
  end hasRecord

end Donor

//Donor object
object Donor extends GenericObject[Donor] with Database:
  // create an initialized Donor from the provided values
  def apply(
           donor_ID:Int,
           name: String,
           email: String,
           birthday: LocalDate,
           contactNo:String,
           occupation:String
           ): Donor =
    new Donor(donor_ID, name, email, birthday, contactNo, occupation)

  end apply

  //create donors table
  def createTable() =
    DB autoCommit { implicit session =>
      sql"""
               CREATE TABLE donors(
               donor_id int NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1) PRIMARY KEY ,  
               name varchar (32),
               email varchar(32),
               birthday date,
               contact_no varchar(16),
               occupation varchar(32)
               )
             """.execute.apply()
    }
  end createTable

  //retrieve all donation records in the table
  def getAllRecords(): List[Donor] =
    DB readOnly { implicit session =>
      sql"""
        SELECT * FROM donors
        """.map(rs => Donor(         // create an object for Donor and return it using the retrieved values
        rs.int("donor_id"),
        rs.string("name"),
        rs.string("email"),
        rs.localDate("birthday"),
        rs.string("contact_no"),
        rs.string("occupation")
      )).list.apply()
    }

  // retrieve single donor record based on key which can be email or id
  // call using this method
  def getRecordByKey(key: Any): Option[Donor] =
    key match
      // treat key as email then pass to getRecordByEmail
      case email: String => getRecordByEmail(email)
      // treat key as id then pass to getRecordByID
      case id: Int => getRecordByID(id)
      case _ => None
  end getRecordByKey

  // retrieve single donation record based on key which is email
  def getRecordByEmail(email: String): Option[Donor] =
    DB readOnly { implicit session =>
      sql"""
        SELECT * FROM donors WHERE email = $email
           """.map(rs => Donor(         // create an object for Donor and return it using the retrieved values
        rs.int("donor_id"),
        rs.string("name"),
        rs.string("email"),
        rs.localDate("birthday"),
        rs.string("contact_no"),
        rs.string("occupation")
        )).single.apply()
    }

  // retrieve single donation record based on key which is donor id
  def getRecordByID(donorId:Int): Option[Donor] =
    DB readOnly { implicit session =>
      sql"""
      SELECT * FROM donors WHERE donor_id = $donorId
         """.map(rs => Donor(  // create an object for Donor and return it using the retrieved values
        rs.int("donor_id"),
        rs.string("name"),
        rs.string("email"),
        rs.localDate("birthday"),
        rs.string("contact_no"),
        rs.string("occupation")
      )).single.apply()
    }
end Donor