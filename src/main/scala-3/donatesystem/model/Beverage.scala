package donatesystem.model

import donatesystem.util.{Database, GenericObject, GenericModel}
import scalikejdbc.*
import scala.util.Try
import scalafx.beans.property.{ObjectProperty, StringProperty}

// Beverage class
// _itemIDI as Beverage property
// a subclass of CatalogItem
// with GenericModel[Beverage] for generic programming
// with Databased trait
class Beverage(val _itemIDI :Int, _nameS:String, _categoryS:String, _perishableB:Boolean, _quantityI:Int, volumePerUnitI: Int, isCarbonatedB: Boolean)
extends CatalogItem(_itemIDI, _nameS, _categoryS, _perishableB, _quantityI) with Database with GenericModel[Beverage]:

  //auxiliary constructor
  def this() = this(0, null, null, false, 0, 0, false)
  
  // create string and object properties for the beverage form fields
  //override the CatalogItem properties
  override val nameProperty = new StringProperty(_nameS)
  override val categoryProperty = new StringProperty(_categoryS)
  override val isPerishableProperty = ObjectProperty[Boolean](_perishableB)
  override val quantityProperty = ObjectProperty[Int](_quantityI)
  val volumePerUnitProperty = ObjectProperty[Int](volumePerUnitI)
  val isCarbonatedProperty =  ObjectProperty[Boolean](isCarbonatedB)

  //save the beverage record
  def saveAsRecord: Try[Int] =
    //if does not have record
    if (!hasRecord) then
      //call CatalogItem method, saveItem to save the name, category, isPerishable and quantity values as catalog item record
      val catalogID = CatalogItem.saveItem(nameProperty.value, categoryProperty.value, isPerishableProperty.value,quantityProperty.value)
      // create a new record for beverage with the remaining fields
      Try(DB autoCommit { implicit session =>
        sql"""
              INSERT INTO beverages (beverage_id, volumePerUnit, isCarbonated) VALUES
              (${catalogID}, ${volumePerUnitProperty.value}, ${isCarbonatedProperty.value})
            """.update.apply()
      })
    else
      // if record exists, update the respective fields
      Try(DB autoCommit {implicit session =>
        val updatedCatalog =
          sql"""
              UPDATE catalog_items
              SET
                name = ${nameProperty.value},
                category = ${categoryProperty.value},
                perishable = ${isPerishableProperty.value},
                quantity = quantity + ${quantityProperty.value}
              WHERE item_id = $_itemIDI
            """.update.apply()

        val updatedBeverage = sql"""
              UPDATE beverages
              SET
              volumePerUnit = ${volumePerUnitProperty.value},
              isCarbonated = ${isCarbonatedProperty.value}
              WHERE beverage_id = $_itemIDI
            """.update.apply()
        updatedCatalog + updatedBeverage // return the number of rows that was updated. successful = 2 rows
      })
  end saveAsRecord
  
  //decrease the quantity of items
  def reduceQuantity(quantity:Int): Try[Int] =
    if(hasRecord) then
      //if record exists then check if quantity provided is under 0 and over the available quantity
      if (quantity <= 0)
        // if less than zero, throw an exception
        throw new IllegalArgumentException("Amount must be positive")

      if (quantity > quantityProperty.value)
        // if quantity provided exceeds the available quantity, throw an exception
        throw new IllegalArgumentException("Cannot reduce more than available quantity")

      //if no exceptions are thrown, update the quantity
      Try(DB autoCommit {
        sql"""
            UPDATE beverages
            SET
            quantity = quantity - $quantity
            WHERE beverage_id = $_itemIDI
          """.update.apply()
      })
    else
      // if no records found, throw an exception
      throw new Exception("An error has occured. The quantity was not reduced")
  end reduceQuantity

  // delete beverage records
  def deleteRecord: Try[Int] =
    // delete only if record exists
    if (hasRecord) then
      // delete only if beverage_id matches
      Try(DB autoCommit { implicit session =>
        sql"""
           DELETE FROM beverages
           WHERE beverage_id=$_itemIDI
           """.update.apply()
      })
    else
      // if no records found, throw an exception
      throw new Exception("There are no records of this beverage. Deletion failed!")
  end deleteRecord

  //check if record exists
  def hasRecord: Boolean =
    //select if beverage_id exists in _itemIDI of beverage table
    DB readOnly { implicit session =>
      sql"""
            SELECT * FROM beverages WHERE beverage_id = $_itemIDI
          """.map(_ => ()).single.apply()
    } match
      // if found, return true
      case Some(x) => true
      // if not found, return false
      case None => false
  end hasRecord
end Beverage

//beverage object
object Beverage extends Database:
  // create an initialized Beverage from the provided values
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

  //create beverage table
  def createTable() =
    DB autoCommit { implicit session =>
      sql"""
               CREATE TABLE beverages(
               beverage_id INT PRIMARY KEY REFERENCES catalog_items(item_id),
               volumePerUnit INT,
               isCarbonated BOOLEAN
               )
             """.execute.apply()
    }
  end createTable

  //retrieve all beverage records from the beverages table
  // returns records as a list
  def getAllRecords(): List[Beverage] =
    DB readOnly { implicit session =>
      sql"""
      SELECT ci.*, b.volumePerUnit, b.isCarbonated
      FROM beverages b
      JOIN catalog_items ci ON b.beverage_id = ci.item_id
        """.map(rs => Beverage( // create an object for Beverage and return it using the retrieved values
        rs.int("item_id"),
        rs.string("name"),
        rs.string("category"),
        rs.boolean("perishable"),
        rs.int("quantity"),
        rs.int("volumePerUnit"),
        rs.boolean("isCarbonated")
      )).list.apply()
    }
end Beverage