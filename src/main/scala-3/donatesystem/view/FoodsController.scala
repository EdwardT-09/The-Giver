package donatesystem.view

import donatesystem.util.Alert
import donatesystem.RunTheGiver
import scalafx.Includes.*
import donatesystem.model.Food

import javafx.beans.value.ObservableValue
import javafx.fxml.FXML
import javafx.scene.control.{Label, TableColumn, TableView}

import scala.util.{Failure, Success, Try}
import scalafx.beans.property.ReadOnlyObjectWrapper

@FXML
class FoodsController:
  @FXML private var foodTable: TableView[Food] = null
  @FXML private var nameColumn: TableColumn[Food, String] = null
  @FXML private var categoryColumn: TableColumn[Food, String] = null
  @FXML private var isPerishableColumn: TableColumn[Food, Boolean] = null
  @FXML private var quantityColumn: TableColumn[Food, Int] = null
  @FXML private var isVegetarianColumn: TableColumn[Food, Boolean] = null
  @FXML private var allergensColumn: TableColumn[Food, String] = null

  def initialize(): Unit =
    foodTable.items = RunTheGiver.foodData
    nameColumn.cellValueFactory = { x => x.value.nameProperty }
    categoryColumn.cellValueFactory = { x => x.value.categoryProperty }
    isPerishableColumn.cellValueFactory = { x => x.value.isPerishableProperty}
    quantityColumn.cellValueFactory = { x => x.value.quantityProperty }
    isVegetarianColumn.cellValueFactory = { x => x.value.isVegetarianProperty }
    allergensColumn.cellValueFactory = { x => x.value.containsAllergensProperty }
  end initialize


  def directToAddFood: Unit =
    val food = new Food(0, "", "", false, 0, false, "")
    RunTheGiver.showAddFood(food) match
      case Some(newFood) =>
        foodTable.items().addLast(newFood)
        foodTable.refresh()
      case None =>

  end directToAddFood


  def directToEditFood: Unit =
    val selectedIndex = foodTable.selectionModel().selectedIndex.value
    val selectedFood = foodTable.selectionModel().selectedItem.value

    if (selectedFood != null) then
      RunTheGiver.showAddFood(selectedFood) match
        case Some(updatedFood) =>
          foodTable.items().update(selectedIndex, updatedFood)
          foodTable.refresh()
        case None =>
          Alert.displayError("Update Error", "The donor record was not updated", "Please try again")
    end if
  end directToEditFood

  def deleteFood: Unit =
    val selectedIndex = foodTable.selectionModel().selectedIndex.value
    val selectedFood = foodTable.selectionModel().selectedItem.value
    if (selectedIndex >= 0) then
      selectedFood.deleteRecord match
        case Success(x) =>
          foodTable.items().remove(selectedIndex)
        case Failure(x) =>
          Alert.displayError("Delete unsuccessful", "The record was not deleted", "Please try again")
    else
      Alert.displayError("Invalid food", "No food record is selected", "Please choose a food")
    end if
  end deleteFood
  

end FoodsController