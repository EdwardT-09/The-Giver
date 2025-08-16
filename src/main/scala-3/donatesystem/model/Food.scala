package donatesystem.model

import donatesystem.util.{Database, GenericCompanion, GenericModel}
import scalikejdbc.*

import scala.util.{Failure, Success, Try}
import scalafx.beans.property.{BooleanProperty, IntegerProperty, ObjectProperty, StringProperty}

import java.time.LocalDate


class Food(val _itemIDI :Int, _nameS:String, _categoryS:String, _perishableB:Boolean, _quantityI:Int, isVegetarianB: Boolean, containsAllergensS: String) extends CatalogItem(_itemIDI, _nameS, _categoryS, _perishableB, _quantityI) with Database with GenericModel[Food]:
  override val nameProperty = new StringProperty(_nameS)
  override val categoryProperty = new StringProperty(_categoryS)
  override val isPerishableProperty = ObjectProperty[Boolean](_perishableB)
  override val quantityProperty = ObjectProperty[Int](_quantityI)
  val isVegetarianProperty = ObjectProperty[Boolean](isVegetarianB)
  val containsAllergensProperty = new StringProperty(containsAllergensS)

  def saveAsRecord: Try[Int] =
    if (!hasRecord) then {
      val catalogID = CatalogItem.saveItem(nameProperty.value, categoryProperty.value, isPerishableProperty.value,quantityProperty.value)
      Try(DB autoCommit { implicit session =>
        sql"""
              INSERT INTO foods (food_id, name, category, perishable, quantity, isVegetarian, containsAllergens) VALUES
              (${catalogID}, ${nameProperty.value}, ${categoryProperty.value}, ${isPerishableProperty.value}, ${quantityProperty.value}, ${isVegetarianProperty.value}, ${containsAllergensProperty.value})
            """.update.apply()
      })
    } else
      Try(DB autoCommit {
        sql"""
              UPDATE foods
              SET
              name = ${nameProperty.value},
              category = ${categoryProperty.value},
              perishable = ${isPerishableProperty.value},
              quantity = quantity + ${quantityProperty.value},
              isVegetarian = ${isVegetarianProperty.value},
              containsAllergens = ${containsAllergensProperty.value}
              WHERE food_id = $_itemIDI
            """.update.apply()
      })
  end saveAsRecord

  def increaseQuantity(quantity: Int): Try[Int] =

    if (hasRecord) then
      Try(DB autoCommit {
        sql"""
          UPDATE foods
          SET
          quantity = quantity + $quantity
          WHERE food_id = $_itemIDI
        """.update.apply()
      })
    else
      throw new Exception("There was an error. The quantity of the food was not reduced.")
  end increaseQuantity

  def reduceQuantity(quantity:Int): Try[Int] =
    
    if(hasRecord) then
      if (quantity <= 0)
        throw new IllegalArgumentException("Amount must be positive")

      if (quantity > quantityProperty.value)
        throw new IllegalArgumentException("Cannot reduce more than available quantity")
      Try(DB autoCommit {
        sql"""
          UPDATE foods
          SET
          quantity = quantity - $quantityProperty
          WHERE food_id = $_itemIDI
        """.update.apply()
      })
    else
      throw new Exception("There was an error. The quantity of the food was not reduced.")
  end reduceQuantity
  


  def deleteRecord: Try[Int] =
    if (hasRecord) then
      Try(DB autoCommit { implicit session =>
        sql"""
           DELETE FROM foods
           WHERE food_id=$_itemIDI
           """.update.apply()
      })
    else
      throw new Exception("There are no records of this item. Deletion failed!")
  end deleteRecord

  def hasRecord: Boolean =
    DB readOnly { implicit session =>
      sql"""
            SELECT * FROM foods WHERE food_id = $_itemIDI
          """.map(_ => ()).single.apply()
    } match
      case Some(x) => true
      case None => false
  end hasRecord
  
end Food

object Food extends GenericCompanion[Food] with Database:
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
        SELECT * FROM foods
        """.map(rs => Food(
        rs.int("food_id"),
        rs.string("name"),
        rs.string("category"),
        rs.boolean("perishable"),
        rs.int("quantity"),
        rs.boolean("isVegetarian"),
        rs.string("containsAllergens")
      )).list.apply()
    }

  def getRecordByKey(key: Any): Option[Food] =
    key match
      case name: String => getRecordByName(name)
      case _ => None
  end getRecordByKey

  def getRecordByName(name: String): Option[Food] =
    DB readOnly { implicit session =>
      sql"""
        SELECT * FROM foods WHERE name = $name
           """.map(rs => Food(
        rs.int("food_id"),
        rs.string("name"),
        rs.string("category"),
        rs.boolean("perishable"),
        rs.int("quantity"),
        rs.boolean("isVegetarian"),
        rs.string("containsAllergens")
      )).single.apply()
    }
end Food

