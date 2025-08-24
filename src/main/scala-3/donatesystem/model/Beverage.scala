package donatesystem.model

import donatesystem.util.{Database, GenericObject}
import scalikejdbc.*

import scala.util.{Failure, Try}
import scalafx.beans.property.{ObjectProperty, StringProperty}

// Beverage class
// _itemIDI as Beverage property
// a subclass of CatalogItem
// with Databased trait
class Beverage(var _itemID :Int, _name:String, _category:String, _perishable:Boolean, _quantity:Int, volumePerUnit: Int, isCarbonated: Boolean)
extends CatalogItem(_itemID, _name, _category, _perishable, _quantity) with Database :

  //auxiliary constructor
  def this() = this(0, null, null, false, 0, 0, false)
  
  // create string and object properties for the beverage form fields
  //override the CatalogItem properties
  override val nameProperty = new StringProperty(_name)
  override val categoryProperty = new StringProperty(_category)
  override val isPerishableProperty = ObjectProperty[Boolean](_perishable)
  override val quantityProperty = ObjectProperty[Int](_quantity)
  val volumePerUnitProperty = ObjectProperty[Int](volumePerUnit)
  val isCarbonatedProperty =  ObjectProperty[Boolean](isCarbonated)
  
  //save the beverage record
  def saveAsRecord(): Try[Int] =
    //if does not have record
    if (!hasRecord()) then
      //call CatalogItem method, saveItem to save the name, category, isPerishable and quantity values as catalog item record
      val catalogID = CatalogItem.saveItem(nameProperty.value, categoryProperty.value, isPerishableProperty.value,quantityProperty.value)
      // create a new record for beverage with the remaining fields
      Try(DB autoCommit { implicit session =>
        sql"""
              INSERT INTO beverages (beverage_id, volume_per_unit, is_carbonated) VALUES
              (${catalogID}, ${volumePerUnitProperty.value}, ${isCarbonatedProperty.value})
            """.update.apply()
        this._itemID = catalogID
        catalogID
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
                is_perishable = ${isPerishableProperty.value},
                quantity = quantity + ${quantityProperty.value}
              WHERE item_id = $_itemID
            """.update.apply()

        val updatedBeverage = sql"""
              UPDATE beverages
              SET
              volume_per_unit = ${volumePerUnitProperty.value},
              is_carbonated = ${isCarbonatedProperty.value}
              WHERE beverage_id = $_itemID
            """.update.apply()
        updatedCatalog + updatedBeverage // return the number of rows that was updated. successful = 2 rows
      })
  end saveAsRecord
  

  // delete beverage records
  def deleteRecord(): Try[Int] =
    // delete only if record exists
    if (hasRecord()) then
      // delete only if beverage_id matches
      Try(DB autoCommit { implicit session =>
        sql"""
           DELETE FROM beverages
           WHERE beverage_id=$_itemID
           """.update.apply()
      })
      CatalogItem.deleteItem(_itemID)
    else
      // if no records found, throw an exception
      Failure(new Exception("There are no records of this beverage. Deletion failed!"))
  end deleteRecord

  //check if record exists
  def hasRecord(): Boolean =
    //select if beverage_id exists in _itemIDI of beverage table
    DB readOnly { implicit session =>
      sql"""
            SELECT * FROM beverages WHERE beverage_id = $_itemID
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
             itemID: Int,
             name: String,
             category: String,
             perishable: Boolean,
             quantity: Int,
             volumePerUnit: Int,
             isCarbonated: Boolean
           ): Beverage =
    new Beverage(itemID, name, category, perishable, quantity, volumePerUnit, isCarbonated)

  end apply

  //create beverage table
  def createTable() =
    DB autoCommit { implicit session =>
      sql"""
               CREATE TABLE beverages(
               beverage_id INT PRIMARY KEY REFERENCES catalog_items(item_id),
               volume_per_unit INT,
               is_carbonated BOOLEAN
               )
             """.execute.apply()
    }
  end createTable

  //retrieve all beverage records from the beverages table
  // returns records as a list
  def getAllRecords(): List[Beverage] =
    DB readOnly { implicit session =>
      sql"""
      SELECT ci.*, b.volume_per_unit, b.is_carbonated
      FROM beverages b
      JOIN catalog_items ci ON b.beverage_id = ci.item_id
        """.map(rs => Beverage( // create an object for Beverage and return it using the retrieved values
        rs.int("item_id"),
        rs.string("name"),
        rs.string("category"),
        rs.boolean("is_perishable"),
        rs.int("quantity"),
        rs.int("volume_per_unit"),
        rs.boolean("is_carbonated")
      )).list.apply()
    }
end Beverage