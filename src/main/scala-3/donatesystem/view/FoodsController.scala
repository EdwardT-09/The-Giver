package donatesystem.view

import donatesystem.util.Alert
import donatesystem.RunTheGiver
import donatesystem.model.{CatalogItem, Food}
import javafx.event.ActionEvent
import scalafx.Includes.*
import javafx.fxml.FXML
import javafx.scene.control.{TableColumn, TableView}
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.TextInputDialog

import scala.util.{Failure, Success}

//provide controls to foods management page
@FXML
class FoodsController:

  //TableView displaying the list of Food items
  @FXML private var foodTable: TableView[Food] = null

  //column showing the name of the food item
  @FXML private var nameColumn: TableColumn[Food, String] = null

  //column showing the category of the food item
  @FXML private var categoryColumn: TableColumn[Food, String] = null

  //column that shows whether the food item is perishable
  @FXML private var isPerishableColumn: TableColumn[Food, Boolean] = null

  //column showing the quantity of food item available
  @FXML private var quantityColumn: TableColumn[Food, Int] = null

  //column that shows whether the food is vegetarian
  @FXML private var isVegetarianColumn: TableColumn[Food, Boolean] = null

  //column listing any allergens associated with the food item
  @FXML private var allergensColumn: TableColumn[Food, String] = null

//used to set up the Food TableView
  def initialize(): Unit =
    //refresh the table to ensure the information shown are up to date
    foodTable.refresh()
    //set the items of the table observable list of food data
    foodTable.items = RunTheGiver.foodData

    //bind each column to their corresponding properties in the Food model
    nameColumn.cellValueFactory = { x => x.value.nameProperty }
    categoryColumn.cellValueFactory = { x => x.value.categoryProperty }
    isPerishableColumn.cellValueFactory = { x => x.value.isPerishableProperty}
    quantityColumn.cellValueFactory = { x => x.value.quantityProperty }
    isVegetarianColumn.cellValueFactory = { x => x.value.isVegetarianProperty }
    allergensColumn.cellValueFactory = { x => x.value.containsAllergensProperty }
  end initialize


//direct users to add new food item
  def directToAddFood(action :ActionEvent): Unit =
  //create new food object
    val food = new Food(0, "", "", false, 0, false, "")
  //pass the food object to showAddFood method
    RunTheGiver.showAddFood(food) match
      //if newFood is added, foodTable is updated with the new food item and refreshed to ensure up-to-date table
      case Some(newFood) =>
        foodTable.items().addLast(newFood)
        foodTable.refresh()
      //if none, then return nothing
      case None =>

  end directToAddFood

  //direct users to add food page but with fields pre-filled in with their current information
  def directToEditFood(action :ActionEvent): Unit =
  //retrieve the index of the currently selected food item from the foodTable
    val selectedIndex = foodTable.selectionModel().selectedIndex.value
    //retrieve the item of the currently selected food item from the foodTable
    val selectedFood = foodTable.selectionModel().selectedItem.value

    if (selectedFood != null) then
      //if food item is selected, pass the selected food item to showAddFood
      RunTheGiver.showAddFood(selectedFood) match
        //if updatedFood is found, then update the foodTable to display new information
        //refresh the table to display up-to-date information
        case Some(updatedFood) =>
          foodTable.items().update(selectedIndex, updatedFood)
          foodTable.refresh()
        //if no updatedFood, then return error alert
        case None =>
          Alert.displayError("Update Error", "The food record was not updated", "Please try again")
    else
      //if no food item was selected, then display the error alert
      Alert.displayError("Invalid food", "No food record is selected", "Please choose a food")
    end if
  end directToEditFood

  //delete food item
  def handleDeleteFood(action :ActionEvent): Unit =
    //retrieve the index of the food item selected from the foodTable
    val selectedIndex = foodTable.selectionModel().selectedIndex.value
    //retrieve the item of the food item selected from the foodTable
    val selectedFood = foodTable.selectionModel().selectedItem.value
    
    if (selectedIndex >= 0) then
      //if a food item is selected, then attempt to delete the record
      selectedFood.deleteRecord match
        //if successful, remove the item from TableView
        case Success(x) =>
          foodTable.items().remove(selectedIndex)
        //if not successful, display an error alert
        case Failure(x) =>
          Alert.displayError("Delete unsuccessful", "The record was not deleted", "Please try again")
    else
      //if no food item was selected, then display the error alert
      Alert.displayError("Invalid food", "No food record is selected", "Please choose a food")
    end if
  end handleDeleteFood

  //reduce the food quantity
  def handleReduceQuantity(action :ActionEvent): Unit =
    //retrieve the index of the food item selected from the foodTable
    val selectedIndex = foodTable.selectionModel().selectedIndex.value
    //retrieve the item of the food item selected from the foodTable
    val selectedFood = foodTable.selectionModel().selectedItem.value

    if (selectedIndex >= 0) then
      //if a food item is selected, then show input dialog for reduce quantity
      val inputDialog = new TextInputDialog("1")
      inputDialog.setTitle("Reduce Quantity")
      inputDialog.setHeaderText(s"Reduce quantity for: ${selectedFood.nameProperty.value}")

      val result = inputDialog.showAndWait()

      ////match the result of the input dialog
      result match {
        case Some(input) =>
          //turn the input from a string to an integer
          val reduction = input.trim.toInt

          //call the reduceQuantity method to reduce quantity
          CatalogItem.reduceQuantity(selectedFood.itemIDI, reduction) match
            case Success(rowsAffected) =>
              //if quantity reduction was successful, update the item in foodTable
              foodTable.items().update(selectedIndex, selectedFood)

              //display a success message to the admin
              Alert.displayInformation("Successful", "The record has been updated", "The quantity has been reduced")
            case Failure(error) =>
              //if unsuccessful due to failure from system(such as database error), display an error message
              Alert.displayError("Unsuccessful", "Quantity was not reduced", "Please try again")
      }
    else
      //if no food item was selected, then display the error alert
      Alert.displayError("Invalid food", "No food record is selected", "Please choose a food")
  end handleReduceQuantity
  //refresh the foodTable
  def refreshTable(): Unit =
  //retrieve all records from the database
    val updateItems = Food.getAllRecords()
  //update the table
    foodTable.items = ObservableBuffer(updateItems: _*)
  //refresh the table
    foodTable.refresh()
  end refreshTable

end FoodsController