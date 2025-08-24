package donatesystem.model

import donatesystem.util.Database
import scalikejdbc.*
import scala.util.{Failure, Try}
import scalafx.beans.property.{ObjectProperty, StringProperty}


// Food class
// _itemIDI as Food property
// a subclass of CatalogItem
// with Databased trait
class Food(var _itemID :Int, _name:String, _category:String, _perishable:Boolean, _quantity:Int, isVegetarian: Boolean, containsAllergens: String) extends CatalogItem(_itemID, _name, _category, _perishable, _quantity) with Database :

  // create string and object properties for the food form fields
  //override the CatalogItem properties
  override val nameProperty = new StringProperty(_name)
  override val categoryProperty = new StringProperty(_category)
  override val isPerishableProperty = ObjectProperty[Boolean](_perishable)
  override val quantityProperty = ObjectProperty[Int](_quantity)
  val isVegetarianProperty = ObjectProperty[Boolean](isVegetarian)
  val containsAllergensProperty = new StringProperty(containsAllergens)


  //save the food record
  def saveAsRecord(): Try[Int] =
    //if does not have record
    if !hasRecord() then {
      //call CatalogItem method, saveItem to save the name, category, isPerishable and quantity values as catalog item record
      val catalogID = CatalogItem.saveItem(nameProperty.value, categoryProperty.value, isPerishableProperty.value,quantityProperty.value)
      // create a new record for food with the remaining fields
      Try(DB autoCommit { implicit session =>
        sql"""
              INSERT INTO foods (food_id, is_vegetarian, contains_allergens) VALUES
              (${catalogID}, ${isVegetarianProperty.value}, ${containsAllergensProperty.value})
            """.update.apply()
        this._itemID = catalogID
        catalogID
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
                is_perishable = ${isPerishableProperty.value},
                quantity = quantity + ${quantityProperty.value}
              WHERE item_id = $_itemID
            """.update.apply()

        val updatedFood = sql"""
              UPDATE foods
              SET
              is_vegetarian = ${isVegetarianProperty.value},
              contains_allergens = ${containsAllergensProperty.value}
              WHERE food_id = $_itemID
            """.update.apply()
        updatedCatalog + updatedFood
      })
  end saveAsRecord


  // delete food records
  def deleteRecord(): Try[Int] =
    // delete only if record exists
    if (hasRecord()) then
      // delete only if food_id matches
      Try(DB autoCommit { implicit session =>
        sql"""
           DELETE FROM foods
           WHERE food_id=$_itemID
           """.update.apply()
      }
      )
      CatalogItem.deleteItem(_itemID)
    else
      // if no records found, throw an exception
      Failure(new Exception("There are no records of this item. Deletion failed!"))
  end deleteRecord

  //check if record exists
  def hasRecord(): Boolean =
    //select if food_id exists in _itemIDI of foods table
    DB readOnly { implicit session =>
      sql"""
            SELECT * FROM foods WHERE food_id = $_itemID
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
             itemID :Int,
             name:String,
             category:String,
             perishable:Boolean,
             quantity:Int,
             isVegetarian: Boolean,
             containsAllergens: String
           ): Food =
    new Food(itemID, name, category, perishable, quantity, isVegetarian,containsAllergens)

  end apply

  //create food table
  def createTable() =
    DB autoCommit { implicit session =>
      sql"""
               CREATE TABLE foods(
               food_id int PRIMARY KEY REFERENCES catalog_items(item_id),
               is_vegetarian BOOLEAN,
               contains_allergens varchar(64)
               )
             """.execute.apply()
    }
  end createTable

  def getAllRecords(): List[Food] =
    DB readOnly { implicit session =>
      sql"""
      SELECT ci.*, f.is_vegetarian, f.contains_allergens
      FROM foods f
      JOIN catalog_items ci ON f.food_id = ci.item_id
      """.map(rs => Food( // create an object for Food and return it using the retrieved values
        rs.int("item_id"),
        rs.string("name"),
        rs.string("category"),
        rs.boolean("is_perishable"),
        rs.int("quantity"),
        rs.boolean("is_vegetarian"),
        rs.string("contains_allergens")
      )).list.apply()
    }

end Food

