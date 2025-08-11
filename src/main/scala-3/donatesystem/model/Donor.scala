package donatesystem.model

import donatesystem.util.Database
import scalikejdbc.*
import scala.util.{Failure, Try}
import scalafx.beans.property.{ObjectProperty, StringProperty}
import java.time.LocalDate


class Donor(donor_IDI: Int, nameS: String, emailS:String, birthdayD:LocalDate, contactNoS: String, occupationS: String) extends Database:
  def this() = this(0, null, null, null, null, null)
  var nameProperty = new StringProperty(nameS)
  var emailProperty = new StringProperty(emailS)
  var birthdayProperty = ObjectProperty[LocalDate](birthdayD)
  var contactNoProperty = new StringProperty(contactNoS)
  var occupationProperty = new StringProperty(occupationS)
  val originalEmail: String = emailS


  def saveToDonor: Try[Int] =
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
             WHERE email = $originalEmail
           """.update.apply()
      })
  end saveToDonor


  def deleteRecord: Try[Int] =
    if (hasRecord) then
      Try(DB autoCommit { implicit session =>
        sql"""
          DELETE FROM donors
          WHERE email = ${emailProperty.value}
          """.update.apply()
      })
    else
      throw new Exception("There are no records of this email. Deletion failed!")
  end deleteRecord

  def hasRecord: Boolean =
    DB readOnly { implicit session =>
      sql"""
           SELECT * FROM donors WHERE email=${emailProperty.value}
         """.map(_ => ()).single.apply()
    } match
      case Some(x) => true
      case None => false
  end hasRecord

end Donor

object Donor extends Database:
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
               donor_id int NOT NULL GENERATED ALWAYS AS IDENTITY,
               name varchar (32),
               email varchar(32),
               birthday date,
               contactNo varchar(16),
               occupation varchar(32)
               )
             """.execute.apply()
    }
  end createTable

  def getAllDonorRecord: List[Donor] =
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
end Donor