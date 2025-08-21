package donatesystem.model

import donatesystem.util.Database
import scalikejdbc.*
import scala.util.{Failure, Success, Try}
import scalafx.beans.property.{BooleanProperty, IntegerProperty, ObjectProperty, StringProperty}
import donatesystem.util.Alert
import java.time.LocalDate

//abstract class: Catalog Item that is the superclass of Food and Beverage class
abstract class CatalogItem(val itemIDI :Int, nameS:String, categoryS:String, perishableB:Boolean, quantityI:Int):
  //auxiliary constructor
  def this() = this(0, null, null, false, 0)

  // string and object properties for the catalog item form fields that are shared by the Food and Beverage class
  val nameProperty = new StringProperty(nameS)
  val categoryProperty = new StringProperty(categoryS)
  val isPerishableProperty =  ObjectProperty[Boolean](perishableB)
  val quantityProperty =  ObjectProperty[Int](quantityI)

  //methods that Food and Beverage shares
  def saveAsRecord: Try[Int]

//  def increaseQuantity(quantity: Int): Try[Int]
//
//  def reduceQuantity(quantity:Int): Try[Int]
//  
  def deleteRecord: Try[Int] 

  def hasRecord: Boolean 
  
end CatalogItem

//object of CatalogItem
object CatalogItem extends Database:

  //create the catalog_items table
  def createTable()=
    DB autoCommit { implicit session =>
      sql"""
                  CREATE TABLE catalog_items(
                  item_id int NOT NULL GENERATED ALWAYS AS IDENTITY  (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
                  name varchar (32),
                  category varchar(32),
                  perishable BOOLEAN,
                  quantity INT)
                """.execute.apply()
    }
  end createTable

  //save the catalog_items information in the table and use the generated key to connect to food and beverage records
  def saveItem(name:String, category: String, perishable:Boolean, quantity:Int):Int =
    DB autoCommit{implicit session =>
      sql"""
            INSERT INTO catalog_items(name, category, perishable, quantity)
            VALUES ($name, $category, $perishable, $quantity )
            """.updateAndReturnGeneratedKey().toInt
    }
  end saveItem

  //increase the quantity
  def increaseQuantity(itemID: Int, quantity: Int): Try[Int] =
      Try(DB autoCommit {
        sql"""
           UPDATE catalog_items
           SET
           quantity = quantity + $quantity
           WHERE item_id = $itemID
         """.update.apply()
      })
  end increaseQuantity

  //decrease the quantity of items
  def reduceQuantity(itemID:Int, quantity: Int): Try[Int] =
    //if inputs are valid, update the quantity
    if validReduceInput(itemID:Int,quantity:Int) then
      Try(DB autoCommit {
        sql"""
            UPDATE catalog_items
            SET
            quantity = quantity - $quantity
            WHERE item_id = $itemID
          """.update.apply()
      })
    else
      // if unsuccessful quantity reduction, throw an exception
      throw new Exception("An error has occured. The quantity was not reduced")
  end reduceQuantity


  def validReduceInput(itemID:Int, quantity:Int):Boolean  =
    //create a variable to store error message(s)
    var errorMessage: String = ""

    //if record exists then check if quantity provided is under 0 and over the available quantity
    if (quantity <= 0)
      // if less than zero, throw an exception
      errorMessage += "Quantity must be above 0\n"

    val itemRecord = CatalogItem.getRecordByKey(itemID) match
      case Some(item: Food) =>
        if (quantity > item.quantityProperty.value)
          // if quantity provided exceeds the available quantity, throw an exception
          errorMessage += "Item reduction exceeds the available amount\n"
      case Some(item: Beverage) =>
        if (quantity > item.quantityProperty.value)
          // if quantity provided exceeds the available quantity, throw an exception
          errorMessage += "Item reduction exceeds the available amount\n"
      case Some(_) =>
        errorMessage +="Item is invalid"

    //if errorMessage does not have any error message then return true
    if errorMessage.isEmpty then
      true
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return false
      Alert.displayError("Invalid", errorMessage, "Please try again")
      false
  end validReduceInput

  //get all records of the catalog items
  def getAllCatalogItems(): List[CatalogItem] =
    Food.getAllRecords() ++ Beverage.getAllRecords()
  end getAllCatalogItems

  //determine which key and call the method
  def getRecordByKey(key: Any): Option[CatalogItem] =
    key match
      // treat key as name then pass to getRecordByName
      case name: String => getRecordByName(name)
      // treat key as itemID then pass to getRecordByID
      case itemID:Int => getRecordByID(itemID)
      case _ => None
  end getRecordByKey
  
  //get a single record from the catalogItems and food or beverage table based on the name
  def getRecordByName(name: String): Option[CatalogItem] = {
    //join table for catalog item and food 
    DB readOnly { implicit session =>
      sql"""
        SELECT c.item_id, c.name, c.category, c.perishable, c.quantity,
               f.isVegetarian, f.containsAllergens
        FROM catalog_items c
        JOIN foods f ON c.item_id = f.food_id
        WHERE c.name = $name
      """.map { rs =>
        new Food( // create an object for Food and return it using the retrieved values
          rs.int("item_id"),
          rs.string("name"),
          rs.string("category"),
          rs.boolean("perishable"),
          rs.int("quantity"),
          rs.boolean("isVegetarian"),
          rs.string("containsAllergens")
        )
      }.single()
    }
  }.orElse {
    //join table for catalog item and beverage 
    DB readOnly { implicit session =>
      sql"""
        SELECT c.item_id, c.name, c.category, c.perishable, c.quantity,
               b.volumePerUnit, b.isCarbonated
        FROM catalog_items c
        JOIN beverages b ON c.item_id = b.beverage_id
        WHERE c.name = $name
      """.map { rs =>
        new Beverage( // create an object for Beverage and return it using the retrieved values
          rs.int("item_id"),
          rs.string("name"),
          rs.string("category"),
          rs.boolean("perishable"),
          rs.int("quantity"),
          rs.int("volumePerUnit"),
          rs.boolean("isCarbonated")
        )
      }.single()
    }
  }

  //get a single record from the catalogItems and food or beverage table based on the id
  def getRecordByID(itemID: Int): Option[CatalogItem] = {
    //join table for catalog item and food 
    DB readOnly { implicit session =>
      sql"""
        SELECT c.item_id, c.name, c.category, c.perishable, c.quantity,
               f.isVegetarian, f.containsAllergens
        FROM catalog_items c
        JOIN foods f ON c.item_id = f.food_id
        WHERE c.item_id = $itemID
      """.map { rs =>
        new Food( // create an object for Food and return it using the retrieved values
          rs.int("item_id"),
          rs.string("name"),
          rs.string("category"),
          rs.boolean("perishable"),
          rs.int("quantity"),
          rs.boolean("isVegetarian"),
          rs.string("containsAllergens")
        )
      }.single()
    }
  }.orElse {
    //join table for catalog item and beverage 
    DB readOnly { implicit session =>
      sql"""
        SELECT c.item_id, c.name, c.category, c.perishable, c.quantity,
               b.volumePerUnit, b.isCarbonated
        FROM catalog_items c
        JOIN beverages b ON c.item_id = b.beverage_id
        WHERE c.item_id = $itemID
      """.map { rs =>
        new Beverage( // create an object for Beverage and return it using the retrieved values
          rs.int("item_id"),
          rs.string("name"),
          rs.string("category"),
          rs.boolean("perishable"),
          rs.int("quantity"),
          rs.int("volumePerUnit"),
          rs.boolean("isCarbonated")
        )
      }.single()
    }
  }

end CatalogItem
