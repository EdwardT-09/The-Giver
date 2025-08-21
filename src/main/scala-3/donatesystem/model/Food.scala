package donatesystem.model

import donatesystem.util.{Database, GenericObject, GenericModel}
import scalikejdbc.*
import scala.util.Try
import scalafx.beans.property.{ObjectProperty, StringProperty}


// Food class
// _itemIDI as Food property
// a subclass of CatalogItem
// with GenericModel[Food] for generic programming
// with Databased trait
class Food(val _itemIDI :Int, _nameS:String, _categoryS:String, _perishableB:Boolean, _quantityI:Int, isVegetarianB: Boolean, containsAllergensS: String) extends CatalogItem(_itemIDI, _nameS, _categoryS, _perishableB, _quantityI) with Database with GenericModel[Food]:

  // create string and object properties for the food form fields
  //override the CatalogItem properties
  override val nameProperty = new StringProperty(_nameS)
  override val categoryProperty = new StringProperty(_categoryS)
  override val isPerishableProperty = ObjectProperty[Boolean](_perishableB)
  override val quantityProperty = ObjectProperty[Int](_quantityI)
  val isVegetarianProperty = ObjectProperty[Boolean](isVegetarianB)
  val containsAllergensProperty = new StringProperty(containsAllergensS)

  //save the food record
  def saveAsRecord: Try[Int] =
    //if does not have record
    if !hasRecord then {
      //call CatalogItem method, saveItem to save the name, category, isPerishable and quantity values as catalog item record
      val catalogID = CatalogItem.saveItem(nameProperty.value, categoryProperty.value, isPerishableProperty.value,quantityProperty.value)
      // create a new record for food with the remaining fields
      Try(DB autoCommit { implicit session =>
        sql"""
              INSERT INTO foods (food_id, isVegetarian, containsAllergens) VALUES
              (${catalogID}, ${isVegetarianProperty.value}, ${containsAllergensProperty.value})
            """.update.apply()
      })
    } else
      // if record exists, update the respective fields
      Try(DB autoCommit { implicit session =>
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

        val updatedFood = sql"""
              UPDATE foods
              SET
              isVegetarian = ${isVegetarianProperty.value},
              containsAllergens = ${containsAllergensProperty.value}
              WHERE food_id = $_itemIDI
            """.update.apply()
        updatedCatalog + updatedFood
      })
  end saveAsRecord

//  //increase the quantity of items
//  def increaseQuantity(quantity: Int): Try[Int] =
//    // if record exists then update the quantity
//    if (hasRecord) then
//      Try(DB autoCommit {
//        sql"""
//          UPDATE foods
//          SET
//          quantity = quantity + $quantity
//          WHERE food_id = $_itemIDI
//        """.update.apply()
//      })
//    else
//      // if no records found, throw an exception
//      throw new Exception("There was an error. The quantity of the food was not reduced.")
//  end increaseQuantity
//
//  //decrease the quantity of items
//  def reduceQuantity(quantity:Int): Try[Int] =
//    //if record exists then check if quantity provided is under 0 and over the available quantity
//    if(hasRecord) then
//      // if less than zero, throw an exception
//      if (quantity <= 0)
//        throw new IllegalArgumentException("Amount must be positive")
//
//      if (quantity > quantityProperty.value)
//        // if quantity provided exceeds the available quantity, throw an exception
//        throw new IllegalArgumentException("Cannot reduce more than available quantity")
//
//      //if no exceptions are thrown, update the quantity
//      Try(DB autoCommit {
//        sql"""
//          UPDATE foods
//          SET
//          quantity = quantity - $quantityProperty
//          WHERE food_id = $_itemIDI
//        """.update.apply()
//      })
//    else
//      // if no records found, throw an exception
//      throw new Exception("There was an error. The quantity of the food was not reduced.")
//  end reduceQuantity


  // delete food records
  def deleteRecord: Try[Int] =
    // delete only if record exists
    if (hasRecord) then
      // delete only if food_id matches
      Try(DB autoCommit { implicit session =>
        sql"""
           DELETE FROM foods
           WHERE food_id=$_itemIDI
           """.update.apply()
      })
    else
      // if no records found, throw an exception
      throw new Exception("There are no records of this item. Deletion failed!")
  end deleteRecord

  //check if record exists
  def hasRecord: Boolean =
    //select if food_id exists in _itemIDI of foods table
    DB readOnly { implicit session =>
      sql"""
            SELECT * FROM foods WHERE food_id = $_itemIDI
          """.map(_ => ()).single.apply()
    } match
      // if found, return true
      case Some(x) => true
      // if not found, return false
      case None => false
  end hasRecord
  
end Food

//food object
object Food extends Database:
  // create an initialized Food from the provided values
  def apply(
             itemIDI :Int,
             nameS:String,
             categoryS:String,
             perishableB:Boolean,
             quantityI:Int,
             isVegetarianB: Boolean,
             containsAllergensS: String
           ): Food =
    new Food(itemIDI, nameS, categoryS, perishableB, quantityI, isVegetarianB, containsAllergensS)

  end apply

  //create food table
  def createTable() =
    DB autoCommit { implicit session =>
      sql"""
               CREATE TABLE foods(
               food_id int PRIMARY KEY REFERENCES catalog_items(item_id),
               isVegetarian BOOLEAN,
               containsAllergens varchar(64)
               )
             """.execute.apply()
    }
  end createTable

  def getAllRecords(): List[Food] =
    DB readOnly { implicit session =>
      sql"""
      SELECT ci.*, f.isVegetarian, f.containsAllergens
      FROM foods f
      JOIN catalog_items ci ON f.food_id = ci.item_id
      """.map(rs => Food( // create an object for Food and return it using the retrieved values
        rs.int("item_id"),
        rs.string("name"),
        rs.string("category"),
        rs.boolean("perishable"),
        rs.int("quantity"),
        rs.boolean("isVegetarian"),
        rs.string("containsAllergens")
      )).list.apply()
    }

end Food

