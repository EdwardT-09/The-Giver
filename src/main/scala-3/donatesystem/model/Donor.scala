package donatesystem.model

import donatesystem.util.{Database, GenericCompanion, GenericModel}
import scalikejdbc.*

import scala.util.{Failure, Try}
import scalafx.beans.property.{IntegerProperty, ObjectProperty, StringProperty}

import java.time.LocalDate


class Donor(val donor_IDI: Int, nameS: String, emailS:String, birthdayD:LocalDate, contactNoS: String, occupationS: String) extends GenericModel[Donor] with Database:
  def this() = this(0, null, null, null, null, null)

  var donorIDProperty =  IntegerProperty(donor_IDI)
  var nameProperty = new StringProperty(nameS)
  var emailProperty = new StringProperty(emailS)
  var birthdayProperty = ObjectProperty[LocalDate](birthdayD)
  var contactNoProperty = new StringProperty(contactNoS)
  var occupationProperty = new StringProperty(occupationS)
  val originalEmail: String = emailS


  def saveAsRecord: Try[Int] =
    if (!hasRecord) then
      Try(DB autoCommit { implicit session =>
        sql"""
             INSERT INTO donors (name, email, birthday, contactNo, occupation) VALUES
             (${nameProperty.value}, ${emailProperty.value}, ${birthdayProperty.value}, ${contactNoProperty.value}, ${occupationProperty.value})
           """.update.apply()
      })
    else
      Try(DB autoCommit {
        sql"""
             UPDATE donors
             SET
             name = ${nameProperty.value},
             email = ${emailProperty.value},
             birthday = ${birthdayProperty.value},
             contactNo = ${contactNoProperty.value},
             occupation = ${occupationProperty.value}
             WHERE donor_id = $donor_IDI
           """.update.apply()
      })
  end saveAsRecord


  def deleteRecord: Try[Int] =
    if (hasRecord) then
      Try(DB autoCommit { implicit session =>
        sql"""
          DELETE FROM donors
          WHERE donor_id = $donor_IDI
          """.update.apply()
      })
    else
      throw new Exception("There are no records of this email. Deletion failed!")
  end deleteRecord

  def hasRecord: Boolean =
    DB readOnly { implicit session =>
      sql"""
           SELECT * FROM donors WHERE donor_id = $donor_IDI
         """.map(_ => ()).single.apply()
    } match
      case Some(x) => true
      case None => false
  end hasRecord

end Donor

object Donor extends GenericCompanion[Donor] with Database:
  def apply(
           donor_IDI:Int,
           nameS: String,
           emailS: String,
           birthdayD: LocalDate,
           contactNoS:String,
           occupationS:String
           ): Donor =
    new Donor(donor_IDI, nameS, emailS, birthdayD, contactNoS, occupationS)

  end apply

  def createTable() =
    DB autoCommit { implicit session =>
      sql"""
               CREATE TABLE donors(
               donor_id int NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1) PRIMARY KEY ,  
               name varchar (32),
               email varchar(32),
               birthday date,
               contactNo varchar(16),
               occupation varchar(32)
               )
             """.execute.apply()
    }
  end createTable

  def getAllRecords(): List[Donor] =
    DB readOnly { implicit session =>
      sql"""
        SELECT * FROM donors
        """.map(rs => Donor(
        rs.int("donor_id"),
        rs.string("name"),
        rs.string("email"),
        rs.localDate("birthday"),
        rs.string("contactNo"),
        rs.string("occupation")
      )).list.apply()
    }

  def getRecordByKey(key: Any): Option[Donor] =
    key match
      case email: String => getRecordByEmail(email)
      case id: Int => getRecordByID(id)
      case _ => None
  end getRecordByKey

  def getRecordByEmail(email: String): Option[Donor] =
    DB readOnly { implicit session =>
      sql"""
        SELECT * FROM donors WHERE email = $email
           """.map(rs => Donor(
        rs.int("donor_id"),
        rs.string("name"),
        rs.string("email"),
        rs.localDate("birthday"),
        rs.string("contactNo"),
        rs.string("occupation")
        )).single.apply()
    }

  def getRecordByID(donorId:Int): Option[Donor] =
    DB readOnly { implicit session =>
      sql"""
      SELECT * FROM donors WHERE donor_id = $donorId
         """.map(rs => Donor(
        rs.int("donor_id"),
        rs.string("name"),
        rs.string("email"),
        rs.localDate("birthday"),
        rs.string("contactNo"),
        rs.string("occupation")
      )).single.apply()
    }
end Donor