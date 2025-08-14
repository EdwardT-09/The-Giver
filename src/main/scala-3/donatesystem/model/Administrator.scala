package donatesystem.model

import donatesystem.util.{Database, GenericCompanion, GenericModel}
import scalikejdbc.*

import scala.util.{Failure, Success, Try}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}

class Administrator(val userID: Int, fNameS:String, emailS:String, passwordS:String) extends GenericModel[Administrator] with Database:
  def this() = this(0, null, null, null)
  var fNameProperty = new StringProperty(fNameS)
  var emailProperty = new StringProperty(emailS)
  var passwordProperty = new StringProperty(passwordS)

  def saveAsRecord: Try[Int] = 
    if(!hasRecord) then
      Try (DB autoCommit{ implicit session =>
        sql"""
             INSERT INTO administrators (fName, email, password) VALUES
             (${fNameProperty.value}, ${emailProperty.value}, ${passwordProperty.value})
           """.update.apply()
      })
    else
      Try(DB autoCommit{
        sql"""
             UPDATE administrators
             SET
             fName = ${fNameProperty.value},
             email = ${emailProperty.value},
             password = ${passwordProperty.value}
             WHERE user_id = $userID
           """.update.apply()
      })
  end saveAsRecord


  def deleteRecord: Try[Int] =
    if(hasRecord) then
      Try(DB autoCommit{implicit session =>
        sql"""
          DELETE FROM administrators
          WHERE email = ${emailProperty.value}
          """.update.apply()
      })
    else
      throw new Exception("There are no records of this email. Deletion failed!")
  end deleteRecord

  def hasRecord:Boolean =
    DB readOnly{ implicit session =>
      sql"""
           SELECT * FROM administrators WHERE user_id = $userID
         """.map(_ => ()).single.apply()
    } match
      case Some(x) => true
      case None => false
  end hasRecord
end Administrator


object Administrator extends GenericCompanion[Administrator] with Database:
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
           CREATE TABLE administrators(
           user_id int NOT NULL GENERATED ALWAYS AS IDENTITY,
           fName varchar (40),
           email varchar(64),
           password varchar(64)
           )
         """.execute.apply()
    }
  end createTable



  def getAllRecords(): List[Administrator] =
    DB readOnly{implicit session =>
      sql"""
      SELECT * from administrators
      """.map(rs => Administrator(
        rs.int("user_id"),
        rs.string("fName"),
        rs.string("email"),
        rs.string("password")
      )).list.apply()
    }
  def getRecordByKey(key:Any):Option[Administrator] =
    key match
      case email:String => getRecordByEmail(email)
      case _ => None
  end getRecordByKey
  
  def getRecordByEmail(email: String):Option[Administrator] =
    DB readOnly{ implicit session =>
      sql"""
           SELECT * FROM administrators WHERE email = $email
         """.map(rs =>
      Administrator(
        rs.int("user_id"),
        rs.string("fname"),
        rs.string("email"),
        rs.string("password"),
      )).single.apply()
    }
end Administrator



