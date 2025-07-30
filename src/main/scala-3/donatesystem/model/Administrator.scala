package donatesystem.model

import donatesystem.util.Database
import scalikejdbc.*
import scala.util.{Failure, Success, Try}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}
import java.time.LocalDateTime

class Administrator(userID: Int, firstNameS:String, emailS:String, passwordS:String, dateOfRegistration:LocalDateTime) extends Database:
  def this() = this(0, null, null, null, null)
  var firstNameProperty = new StringProperty(firstNameS)
  var emailProperty = new StringProperty(emailS)
  var passwordProperty = new StringProperty(passwordS)
  var dateOfRegistrationProperty = ObjectProperty[LocalDateTime](LocalDateTime.now)

  def saveAsRecord: Try[Int] =
    if(!(hasRecord)) then
      Try (DB autoCommit{ implicit session =>
        sql"""
             INSERT INTO administrator (firstName, email, password, dateOfRegistration) VALUES
             (${firstNameProperty.value}, ${emailProperty.value}, ${passwordProperty.value}, ${dateOfRegistrationProperty.value})
           """.update.apply()
      })
    else
      Try(DB autoCommit{
        sql"""
             UPDATE administrator
             SET
             firstName = ${firstNameProperty.value},
             email = ${emailProperty.value},
             password = ${passwordProperty.value},
             dateOfRegistration = ${dateOfRegistrationProperty.value}
             WHERE email = ${emailProperty.value}
           """.update.apply()
      })

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
           SELECT * FROM administration WHERE email=${emailProperty.value}
         """.map(ex => ex.string("")).single.apply()
    } match
      case Some(x) => true
      case None => false
  end hasRecord
end Administrator


object Administrator extends Database:
  def apply(
             user_idI:Int,
             firstNameS: String,
             emailS: String,
             passwordS: String,
             dateOfRegistrationS: LocalDateTime
           ): Administrator =
    new Administrator(user_idI, firstNameS, emailS, passwordS, dateOfRegistrationS):

  end apply

  def createTable() =
    DB autoCommit{implicit session =>
      sql"""
           CREATE TABLE administrator(
           id int NOT NULL PRIMARY KEY AUTO_INCREMENT,
           firstName varchar2 (40),
           email varchar2(64),
           password varchar2(64),
           date timestamp
           )
         """.execute.apply()
    }
  end createTable

  def getAllAdminRecord: List[Administrator] =
    DB readOnly{implicit session =>
      sql"""
      SELECT * from administrator
      """.map(rs => Administrator(
        rs.int("user_id"),
        rs.string("firstName"),
        rs.string("email"),
        rs.string("password"),
        rs.timestamp("dateOfRegistration").toLocalDateTime
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
        rs.timestamp("dateOfRegistration").toLocalDateTime
      )).single.apply()
    }



