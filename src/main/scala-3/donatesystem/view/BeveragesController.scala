package donatesystem.view

import scalafx.Includes.*
import donatesystem.RunTheGiver
import donatesystem.model.{Beverage, CatalogItem}
import javafx.fxml.FXML
import javafx.scene.control.{TableColumn, TableView}
import donatesystem.util.Alert
import javafx.event.ActionEvent
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.TextInputDialog

import scala.util.{Failure, Success}

//provide controls to beverages management page
@FXML
class BeveragesController:
  //TableView displaying the list of Beverage items
  @FXML private var beverageTable: TableView[Beverage] = null
  
  //column showing the name of the beverage item
  @FXML private var nameColumn: TableColumn[Beverage, String] = null
  
  //column showing the category of the beverage item
  @FXML private var categoryColumn: TableColumn[Beverage, String] = null

  //column that shows whether the beverage item is perishable
  @FXML private var isPerishableColumn: TableColumn[Beverage, Boolean] = null

  //column showing the quantity of beverage item available
  @FXML private var quantityColumn: TableColumn[Beverage, Int] = null

  //column that shows whether the beverage is carbonated
  @FXML private var isCarbonatedColumn: TableColumn[Beverage, Boolean] = null

  //column showing the volume per unit of the beverage item
  @FXML private var volumePerUnitColumn: TableColumn[Beverage, Int] = null

  //used to set up the Beverage TableView
  def initialize(): Unit =
    //refresh the table to ensure the information shown are up to date
    beverageTable.refresh()
    //set the items of the table observable list of beverage data
    beverageTable.items = RunTheGiver.beverageData


    //bind each column to their corresponding properties in the Beverage model
    nameColumn.cellValueFactory = { x => x.value.nameProperty }
    categoryColumn.cellValueFactory = { x => x.value.categoryProperty }
    isPerishableColumn.cellValueFactory = { x => x.value.isPerishableProperty }
    quantityColumn.cellValueFactory = { x => x.value.quantityProperty }
    volumePerUnitColumn.cellValueFactory = { x => x.value.volumePerUnitProperty }
    isCarbonatedColumn.cellValueFactory = { x => x.value.isCarbonatedProperty }
  end initialize

  //direct users to add new beverage item
  def directToAddBeverage(action :ActionEvent): Unit =
    //create new beverage object
    val beverage = new Beverage(0, "", "", false, 0,  0, false)
    //pass the beverage object to showAddBeverage method
    RunTheGiver.showAddBeverage(beverage) match
      //if newBeverage is added, beverageTable is updated with the new beverage item and refreshed to ensure up-to-date table
      case Some(newBeverage) =>
        beverageTable.items().addLast(newBeverage)
        beverageTable.refresh()
      //if none, then return nothing
      case None =>
  end directToAddBeverage

  //direct users to add beverage page but with fields pre-filled in with their current information
  def directToEditBeverage(action :ActionEvent): Unit =
    //retrieve the index of the currently selected beverage item from the beverageTable
    val selectedIndex = beverageTable.selectionModel().selectedIndex.value
    //retrieve the item of the currently selected beverage item from the beverageTable
    val selectedBeverage = beverageTable.selectionModel().selectedItem.value
    
    if (selectedBeverage != null) then
      //if beverage item is selected, pass the selected beverage item to showAddBeverage
      RunTheGiver.showAddBeverage(selectedBeverage) match
        //if updatedBeverage is found, then update the beverageTable to display new information
        //refresh the table to display up-to-date information
        case Some(updatedBeverage) =>
          beverageTable.items().update(selectedIndex, updatedBeverage)
          beverageTable.refresh()
        //if no updatedBeverage, then return error alert
        case None =>
          Alert.displayError("Update Error", "The beverage record was not updated", "Please try again")
    else
      //if no beverage item was selected, then display the error alert
      Alert.displayError("Invalid beverage", "No beverage record is selected", "Please choose a beverage")
    end if
  end directToEditBeverage


  //delete beverage item
  def handleDeleteBeverage(action :ActionEvent): Unit =
    //retrieve the index of the beverage item selected from the beverageTable
    val selectedIndex = beverageTable.selectionModel().selectedIndex.value
    //retrieve the item of the beverage item selected from the beverageTable
    val selectedBeverage = beverageTable.selectionModel().selectedItem.value
    
      if (selectedIndex >= 0) then
    //if a beverage is selected, then attempt to delete the record
      val confirm = Alert.displayConfirmation("Delete Donor",
        "Are you sure you want this record to be deleted",
        s"Name: ${selectedBeverage.nameProperty.value}")
  
      if confirm then
        //if a beverage item is selected, then attempt to delete the record
        selectedBeverage.deleteRecord() match
          //if successful, remove the item from TableView
          case Success(x) =>
            beverageTable.items().remove(selectedIndex)
          //if not successful, display an error alert
          case Failure(x) =>
            Alert.displayError("Delete unsuccessful", "The record was not deleted", "Please try again")
    else
      //if no beverage item was selected, then display the error alert
      Alert.displayError("Invalid beverage", "No beverage record is selected", "Please choose a beverage")
    end if
  end handleDeleteBeverage

  //reduce the beverage quantity
  def handleReduceQuantity(action :ActionEvent): Unit =
    //retrieve the index of the beverage item selected from the beverageTable
    val selectedIndex = beverageTable.selectionModel().selectedIndex.value
    //retrieve the item of the beverage item selected from the beverageTable
    val selectedBeverage = beverageTable.selectionModel().selectedItem.value

    if (selectedIndex >= 0) then
    //if a beverage item is selected, then show input dialog for reduce quantity
      val inputDialog = new TextInputDialog("1")
      inputDialog.setTitle("Reduce Quantity")
      inputDialog.setHeaderText(s"Reduce quantity for: ${selectedBeverage.nameProperty.value}")

      val result = inputDialog.showAndWait()

      ////match the result of the input dialog
      result match {
        case Some(input) =>
          //check if quantity is an integer
          if (validQuantity(input.trim)) then
            //turn the input from a string to an integer
            val reduction = input.trim.toInt
  
            //call the reduceQuantity method to reduce quantity
            CatalogItem.reduceQuantity(selectedBeverage.itemID, reduction) match
              case Success(rowsAffected) =>
                //if quantity reduction was successful, update the item in beverageTable
                beverageTable.items().update(selectedIndex, selectedBeverage)
  
                //display a success message to the admin
                Alert.displayInformation("Successful", "The record has been updated", "The quantity has been reduced")
              case Failure(error) =>
                //if unsuccessful due to failure from system(such as database error), display an error message
                Alert.displayError("Unsuccessful", "Quantity was not reduced", "Please try again")
  
        }
    else
      //if no beverage item was selected, then display the error alert
      Alert.displayError("Invalid beverage", "No beverage record is selected", "Please choose a beverage")
  end handleReduceQuantity


  //ensure that the quantity is not null and is an integer
  def validQuantity(input: String): Boolean =
    //create a variable to store error message(s)
    var errorMessage: String = ""

    //if input field is empty, then add error to errorMessage
    if (input.isEmpty) then
      errorMessage += "*Input field is empty\n"
    //if the volume is not a number, then add the error to errorMessage
    else if (!input.matches("""\d+"""))
      errorMessage += "*Input must be a non-negative number\n"
    end if
    //if errorMessage does not have any error message then return true
    if (errorMessage.isEmpty) then
      true
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return false
      Alert.displayError("Invalid Fields", "Please fill in all required fields correctly.", errorMessage)
      false
  end validQuantity


  //refresh the beverageTable
  def refreshTable(): Unit =
    //retrieve all records from the database
    val updateItems = Beverage.getAllRecords()
    //update the table
    beverageTable.items = ObservableBuffer(updateItems: _*)
    //refresh the table
    beverageTable.refresh()
  end refreshTable

end BeveragesController