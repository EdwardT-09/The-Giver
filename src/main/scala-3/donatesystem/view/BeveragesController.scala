package donatesystem.view

import scalafx.Includes.*
import donatesystem.RunTheGiver
import donatesystem.model.{Beverage, Food}
import javafx.fxml.FXML
import javafx.scene.control.{TableColumn, TableView}
import donatesystem.util.Alert

import scala.util.{Failure, Success, Try}

@FXML
class BeveragesController:
  @FXML private var beverageTable: TableView[Beverage] = null
  @FXML private var nameColumn: TableColumn[Beverage, String] = null
  @FXML private var categoryColumn: TableColumn[Beverage, String] = null
  @FXML private var isPerishableColumn: TableColumn[Beverage, Boolean] = null
  @FXML private var quantityColumn: TableColumn[Beverage, Int] = null
  @FXML private var isCarbonatedColumn: TableColumn[Beverage, Boolean] = null
  @FXML private var volumePerUnitColumn: TableColumn[Beverage, Int] = null

  def initialize(): Unit =
    beverageTable.items = RunTheGiver.beverageData
    nameColumn.cellValueFactory = { x => x.value.nameProperty }
    categoryColumn.cellValueFactory = { x => x.value.categoryProperty }
    isPerishableColumn.cellValueFactory = { x => x.value.isPerishableProperty }
    quantityColumn.cellValueFactory = { x => x.value.quantityProperty }
    volumePerUnitColumn.cellValueFactory = { x => x.value.volumePerUnitProperty }
    isCarbonatedColumn.cellValueFactory = { x => x.value.isCarbonatedProperty }
  end initialize


  def directToAddBeverage: Unit =
    val beverage = new Beverage(0, "", "", false, 0,  0, false)
    RunTheGiver.showAddBeverage(beverage) match
      case Some(newBeverage) =>
        beverageTable.items().addLast(newBeverage)
        beverageTable.refresh()
      case None =>

  end directToAddBeverage


  def directToEditBeverage: Unit =
    val selectedIndex = beverageTable.selectionModel().selectedIndex.value
    val selectedBeverage = beverageTable.selectionModel().selectedItem.value

    if (selectedBeverage != null) then
      RunTheGiver.showAddBeverage(selectedBeverage) match
        case Some(updatedBeverage) =>
          beverageTable.items().update(selectedIndex, updatedBeverage)
          beverageTable.refresh()
        case None =>
          Alert.displayError("Update Error", "The beverage record was not updated", "Please try again")
    end if
  end directToEditBeverage

  def deleteBeverage: Unit =
    val selectedIndex = beverageTable.selectionModel().selectedIndex.value
    val selectedBeverage = beverageTable.selectionModel().selectedItem.value
    if (selectedIndex >= 0) then
      selectedBeverage.deleteRecord match
        case Success(x) =>
          beverageTable.items().remove(selectedIndex)
        case Failure(x) =>
          Alert.displayError("Delete unsuccessful", "The record was not deleted", "Please try again")
    else
      Alert.displayError("Invalid beverage", "No beverage record is selected", "Please choose a food")
    end if
  end deleteBeverage


end BeveragesController