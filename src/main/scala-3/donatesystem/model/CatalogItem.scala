package donatesystem.model

import donatesystem.util.Database
import scalikejdbc.*

import scala.util.{Failure, Success, Try}
import scalafx.beans.property.{BooleanProperty, IntegerProperty, ObjectProperty, StringProperty}

import java.time.LocalDate

abstract class CatalogItem(val itemIDI :Int, nameS:String, categoryS:String, perishableB:Boolean, quantityI:Int):
  
  def this() = this(0, "", "", false, 0)
  val nameProperty = new StringProperty(nameS)
  val categoryProperty = new StringProperty(categoryS)
  val isPerishableProperty =  ObjectProperty[Boolean](perishableB)
  val quantityProperty =  ObjectProperty[Int](quantityI)
  
  def saveAsRecord: Try[Int]

  def increaseQuantity(quantity: Int): Try[Int]

  def reduceQuantity(quantity:Int): Try[Int]
  
  def deleteRecord: Try[Int] 

  def hasRecord: Boolean 
  
end CatalogItem

object CatalogItem extends Database:

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



  def saveItem(name:String, category: String, perishable:Boolean, quantity:Int):Int =
    DB autoCommit{implicit session =>
      sql"""
            INSERT INTO catalog_items(name, category, perishable, quantity)
            VALUES ($name, $category, $perishable, $quantity )
            """.updateAndReturnGeneratedKey().toInt
    }
  end saveItem

  def increaseQuantity(itemID: Int, quantity: Int): Try[Int] =
      Try(DB autoCommit {
        sql"""
           UPDATE foods
           SET
           quantity = quantity + $quantity
           WHERE food_id = $itemID
         """.update.apply()
      })
  end increaseQuantity

  def getAllCatalogItems(): List[CatalogItem] =
    Food.getAllRecords() ++ Beverage.getAllRecords()
  end getAllCatalogItems

  def getRecordByID(itemID: Int): Option[CatalogItem] = {
    DB readOnly { implicit session =>
      sql"""
        SELECT c.itemID, c.name, c.category, c.perishable, c.quantity,
               f.isVegetarian, f.containsAllergens
        FROM catalog_items c
        JOIN food f ON c.itemID = f.itemID
        WHERE c.itemID = $itemID
      """.map { rs =>
        new Food(
          rs.int("itemID"),
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
    DB readOnly { implicit session =>
      sql"""
        SELECT c.itemID, c.name, c.category, c.perishable, c.quantity,
               b.volumePerUnit, b.isCarbonated
        FROM catalog_items c
        JOIN beverage b ON c.itemID = b.itemID
        WHERE c.itemID = $itemID
      """.map { rs =>
        new Beverage(
          rs.int("itemID"),
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
