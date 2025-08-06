package donatesystem.model

import donatesystem.util.Database
import scalikejdbc.*
import scala.util.{Failure, Success, Try}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}

class Administrator(userID: Int, fNameS:String, emailS:String, passwordS:String) extends Database:
  def this() = this(0, null, null, null)
  var fNameProperty = new StringProperty(fNameS)
  var emailProperty = new StringProperty(emailS)
  var passwordProperty = new StringProperty(passwordS)

  def saveAsRecord: Try[Int] = {
    if(!hasRecord) then
      Try (DB autoCommit{ implicit session =>
        sql"""
             INSERT INTO administrator (fName, email, password) VALUES
             (${fNameProperty.value}, ${emailProperty.value}, ${passwordProperty.value})
           """.update.apply()
      })
    else
      Failure(new Exception ("The email that you have provided is already registered. Please provide another email."))
  }

  def updateRecord: Try[Int] =
    if(hasRecord) then
      Try(DB autoCommit{
        sql"""
             UPDATE administrator
             SET
             fName = ${fNameProperty.value},
             email = ${emailProperty.value},
             password = ${passwordProperty.value},
             WHERE email = ${emailProperty.value}
           """.update.apply()
      })
      else
        throw new Exception ("There are no records of this user.")


  def deleteRecord: Try[Int] =
    if(hasRecord) then
      Try(DB autoCommit{implicit session =>
        sql"""
          DELETE FROM administrator
          WHERE email = ${emailProperty.value}
          """.update.apply()
      })
    else
      Failure( new Exception("There are no records of this email. Deletion failed!"))
  end deleteRecord

  def hasRecord:Boolean =
    DB readOnly{ implicit session =>
      sql"""
           SELECT * FROM administrator WHERE email=${emailProperty.value}
         """.map(_ => ()).single.apply()
    } match
      case Some(x) => true
      case None => false
  end hasRecord
end Administrator


object Administrator extends Database:
  def apply(
             user_idI:Int,
             fNameS: String,
             emailS: String,
             passwordS: String,
           ): Administrator =
    new Administrator(user_idI, fNameS, emailS, passwordS)

  end apply

  def createTable() =
    DB autoCommit{implicit session =>
      sql"""
           CREATE TABLE administrator(
           user_id int NOT NULL GENERATED ALWAYS AS IDENTITY ,
           fName varchar (40),
           email varchar(64),
           password varchar(64)
           )
         """.execute.apply()
    }
  end createTable

  def dropTable() =
    DB autoCommit { implicit session =>
      sql"""
           DROP TABLE administrator
         """.execute.apply()
    }
  end dropTable

  def getAllAdminRecord: List[Administrator] =
    DB readOnly{implicit session =>
      sql"""
      SELECT * from administrator
      """.map(rs => Administrator(
        rs.int("user_id"),
        rs.string("fName"),
        rs.string("email"),
        rs.string("password")
      )).list.apply()
    }
    
  def getRecordByEmail(email: String):Option[Administrator] =
    DB readOnly{ implicit session =>
      sql"""
           SELECT * FROM administrator WHERE email = $email
         """.map(rs =>
      Administrator(
        rs.int("user_id"),
        rs.string("fname"),
        rs.string("email"),
        rs.string("password"),
      )).single.apply()
    }



