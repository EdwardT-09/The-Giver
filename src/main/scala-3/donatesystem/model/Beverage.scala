package donatesystem.model

import donatesystem.util.{Database, GenericCompanion, GenericModel}
import scalikejdbc.*

import scala.util.Try
import scalafx.beans.property.{BooleanProperty, IntegerProperty, ObjectProperty, StringProperty}

class Beverage(val _itemIDI :Int, _nameS:String, _categoryS:String, _perishableB:Boolean, _quantityI:Int, volumePerUnitI: Int, isCarbonatedB: Boolean)
extends DonationItem(_itemIDI, _nameS, _categoryS, _perishableB, _quantityI) with Database with GenericModel[Beverage]:

  override val nameProperty = new StringProperty(_nameS)
  override val categoryProperty = new StringProperty(_categoryS)
  override val isPerishableProperty = ObjectProperty[Boolean](_perishableB)
  override val quantityProperty = ObjectProperty[Int](_quantityI)
  val volumePerUnitProperty = ObjectProperty[Int](volumePerUnitI)
  val isCarbonatedProperty =  ObjectProperty[Boolean](isCarbonatedB)

  def saveAsRecord: Try[Int] =
    if (!hasRecord) then
      Try(DB autoCommit { implicit session =>
        sql"""
              INSERT INTO beverages (name, category, perishable, quantity, volumePerUnit, isCarbonated) VALUES
              (${nameProperty.value}, ${categoryProperty.value}, ${isPerishableProperty.value}, ${quantityProperty.value}, ${volumePerUnitProperty.value}, ${isCarbonatedProperty.value})
            """.update.apply()
      })
    else
      Try(DB autoCommit {
        sql"""
              UPDATE beverages
              SET
              name = ${nameProperty.value},
              category = ${categoryProperty.value},
              perishable = ${isPerishableProperty.value},
              quantity = quantity + ${quantityProperty.value},
              volumePerUnit = ${volumePerUnitProperty.value},
              isCarbonated = ${isCarbonatedProperty.value}
              WHERE beverage_id = $_itemIDI
            """.update.apply()
      })
  end saveAsRecord

  def reduceQuantity(quantity:Int): Try[Int] =
    if(hasRecord) then
      if (quantity <= 0)
        throw new IllegalArgumentException("Amount must be positive")

      if (quantity > quantityProperty.value)
        throw new IllegalArgumentException("Cannot reduce more than available quantity")
      Try(DB autoCommit {
        sql"""
            UPDATE beverages
            SET
            quantity = quantity - ${quantityProperty.value},
            WHERE beverage_id = $_itemIDI
          """.update.apply()
      })
    else
      throw new Exception("An error has occured. The quantity was not reduced")
  end reduceQuantity


  def deleteRecord: Try[Int] =
    if (hasRecord) then
      Try(DB autoCommit { implicit session =>
        sql"""
           DELETE FROM beverages
           WHERE beverage_id=$_itemIDI
           """.update.apply()
      })
    else
      throw new Exception("There are no records of this beverage. Deletion failed!")
  end deleteRecord

  def hasRecord: Boolean =
    DB readOnly { implicit session =>
      sql"""
            SELECT * FROM beverages WHERE beverage_id = $_itemIDI
          """.map(_ => ()).single.apply()
    } match
      case Some(x) => true
      case None => false
  end hasRecord
end Beverage

object Beverage extends GenericCompanion[Beverage] with Database:
  def apply(
             itemIDI: Int,
             nameS: String,
             categoryS: String,
             perishableB: Boolean,
             quantityI: Int,
             volumePerUnitI: Int,
             isCarbonatedB: Boolean
           ): Beverage =
    new Beverage(itemIDI, nameS, categoryS, perishableB, quantityI, volumePerUnitI, isCarbonatedB)

  end apply

  def createTable() =
    DB autoCommit { implicit session =>
      sql"""
               CREATE TABLE beverages(
               beverage_id INT NOT NULL GENERATED ALWAYS AS IDENTITY,
               name varchar (32),
               category varchar(32),
               perishable BOOLEAN,
               quantity INT,
               volumePerUnit INT,
               isCarbonated BOOLEAN
               )
             """.execute.apply()
    }
  end createTable

  def getAllRecords(): List[Beverage] =
    DB readOnly { implicit session =>
      sql"""
        SELECT * FROM beverages
        """.map(rs => Beverage(
        rs.int("beverage_id"),
        rs.string("name"),
        rs.string("category"),
        rs.boolean("perishable"),
        rs.int("quantity"),
        rs.int("volumePerUnit"),
        rs.boolean("isCarbonated")
      )).list.apply()
    }

  def getRecordByKey(key: Any): Option[Beverage] =
    key match
      case name:String => getRecordByName(name)
      case _ => None
  end getRecordByKey
  
  def getRecordByName(name: String): Option[Beverage] =
    DB readOnly { implicit session =>
      sql"""
        SELECT * FROM beverages WHERE name = $name
           """.map(rs => Beverage(
        rs.int("beverage_id"),
        rs.string("name"),
        rs.string("category"),
        rs.boolean("perishable"),
        rs.int("quantity"),
        rs.int("volumePerUnit"),
        rs.boolean("isCarbonated")
      )).single.apply()
    }
end Beverage