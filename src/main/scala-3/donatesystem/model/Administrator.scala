package donatesystem.model

import donatesystem.util.{Database, GenericObject}
import scalikejdbc.*

import scala.util.{Failure, Try}
import scalafx.beans.property.StringProperty

// Administrator class
// adminIDI as Administrator property
// with Database trait
case class Administrator(adminID: Int,  fName:String,  email:String,  password:String) extends Database:
  //auxiliary constructor
  def this() = this(0, null, null, null)

  // create string properties for the administrator form fields
  var fNameProperty = new StringProperty(fName)
  var emailProperty = new StringProperty(email)
  var passwordProperty = new StringProperty(password)

  //save the admin record to administrators table
  def saveAsRecord(): Try[Int] = {
    //if does not have record
    if(!hasRecord()) then {
      // create a new record
      Try (DB autoCommit{ implicit session =>
        sql"""
             INSERT INTO administrators (f_name, email, password) VALUES
             (${fNameProperty.value}, ${emailProperty.value}, ${passwordProperty.value})
           """.update.apply()
      })
    } else {
      // if record exists, update the respective fields
      // to ensure only the current admin is updated
      Try(DB autoCommit{
        sql"""
             UPDATE administrators
             SET
             f_name = ${fNameProperty.value},
             email = ${emailProperty.value},
             password = ${passwordProperty.value}
             WHERE admin_id = $adminID
           """.update.apply()
      })
    }
  }
  end saveAsRecord

  // delete administrator records
  def deleteRecord(): Try[Int] =
  // delete only if record exists
    if(hasRecord()) then {
      // delete only if email matches
      Try(DB autoCommit{implicit session =>
        sql"""
          DELETE FROM administrators
          WHERE email = ${emailProperty.value}
          """.update.apply()
      })
    } else {
      // if no records found, throw an exception
      Failure(new Exception("There are no records of this email. Deletion failed!"))
    }
  end deleteRecord

  //check if record exists
  def hasRecord():Boolean =
  //select if adminIDI exists in admin_id of administrator table
    DB readOnly{ implicit session =>
      sql"""
           SELECT * FROM administrators WHERE admin_id = $adminID
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
             user_id:Int,
             fName: String,
             email: String,
             password: String,
           ): Administrator =
    new Administrator(user_id, fName, email, password)

  end apply

  //create administrators table with admin_id, fname, email and password
  def createTable() =
    DB autoCommit{implicit session =>
      sql"""
           CREATE TABLE administrators(
           admin_id int NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1) PRIMARY KEY ,
           f_name varchar (40),
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
        rs.string("f_name"),
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
        rs.string("f_name"),
        rs.string("email"),
        rs.string("password"),
      )).single.apply()
    }
  }
end Administrator



