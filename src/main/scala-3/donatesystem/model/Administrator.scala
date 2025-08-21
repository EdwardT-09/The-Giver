package donatesystem.model

import donatesystem.util.{Database, GenericObject, GenericModel}
import scalikejdbc.*
import scala.util.Try
import scalafx.beans.property. StringProperty

// Administrator class
// adminIDI as Administrator property
// extends GenericModel[Administrator] for generic programming
// with Database trait
class Administrator(val adminIDI: Int,  fNameS:String,  emailS:String,  passwordS:String) extends GenericModel[Administrator] with Database:
  //auxiliary constructor
  def this() = this(0, null, null, null)

  // create string properties for the administrator form fields
  var fNameProperty = new StringProperty(fNameS)
  var emailProperty = new StringProperty(emailS)
  var passwordProperty = new StringProperty(passwordS)

  //save the admin record to administrators table
  def saveAsRecord: Try[Int] = {
    //if does not have record
    if(!hasRecord) then {
      // create a new record
      Try (DB autoCommit{ implicit session =>
        sql"""
             INSERT INTO administrators (fName, email, password) VALUES
             (${fNameProperty.value}, ${emailProperty.value}, ${passwordProperty.value})
           """.update.apply()
      })
    } else {
      // if record exists, update the respective fields
      Try(DB autoCommit{
        sql"""
             UPDATE administrators
             SET
             fName = ${fNameProperty.value},
             email = ${emailProperty.value},
             password = ${passwordProperty.value}
             // to ensure only the current admin is updated
             WHERE admin_id = $adminIDI
           """.update.apply()
      })
    }
  }
  end saveAsRecord

  // delete administrator records
  def deleteRecord: Try[Int] =
  // delete only if record exists
    if(hasRecord) then {
      // delete only if email matches
      Try(DB autoCommit{implicit session =>
        sql"""
          DELETE FROM administrators
          WHERE email = ${emailProperty.value}
          """.update.apply()
      })
    } else {
      // if no records found, throw an exception
      throw new Exception("There are no records of this email. Deletion failed!")
    }
  end deleteRecord

  //check if record exists
  def hasRecord:Boolean =
  //select if adminIDI exists in admin_id of administrator table
    DB readOnly{ implicit session =>
      sql"""
           SELECT * FROM administrators WHERE admin_id = $adminIDI
         """.map(_ => ()).single.apply()
    } match
      // if found return true
      case Some(x) => true
      // if not found, return false
      case None => false
  end hasRecord
end Administrator

//administrator object
object Administrator extends GenericObject[Administrator] with Database:
  // create an initialized Administrator from the provided values
  def apply(
             user_idI:Int,
             fNameS: String,
             emailS: String,
             passwordS: String,
           ): Administrator =
    new Administrator(user_idI, fNameS, emailS, passwordS)

  end apply

  //create administrators table with admin_id, fname, email and password
  def createTable() =
    DB autoCommit{implicit session =>
      sql"""
           CREATE TABLE administrators(
           admin_id int NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1) PRIMARY KEY ,
           fName varchar (40),
           email varchar(64),
           password varchar(64)
           )
         """.execute.apply()
    }
  end createTable


  //retrieve all administrator records from the administrators table
  // returns records as a list
  def getAllRecords(): List[Administrator] =
    DB readOnly{implicit session =>
      sql"""
      SELECT * from administrators
      """.map(rs => Administrator( // create an object for Administrator and return it using the retrieved values
        rs.int("admin_id"),
        rs.string("fName"),
        rs.string("email"),
        rs.string("password")
      )).list.apply()
    }

  // retrieve single administrator record based on key which is email
  // call using this method
  def getRecordByKey(key:Any):Option[Administrator] =
    key match
      // treat key as email then pass to getRecordByEmail
      case email:String => getRecordByEmail(email)
      // if nothing return None
      case _ => None
  end getRecordByKey

  // retrieve single administrator record based on key which is email
  private def getRecordByEmail(email: String):Option[Administrator] = {
    // retrieve based on the email provided
    DB readOnly{ implicit session =>
      sql"""
           SELECT * FROM administrators WHERE email = $email
         """.map(rs =>
      Administrator( // create an object for Administrator and return it using the retrieved values
        rs.int("admin_id"),
        rs.string("fname"),
        rs.string("email"),
        rs.string("password"),
      )).single.apply()
    }
  }
end Administrator



