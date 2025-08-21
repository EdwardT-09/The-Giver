package donatesystem.view

import donatesystem.util.Alert
import donatesystem.RunTheGiver
import donatesystem.model.DonatedItems
import javafx.event.ActionEvent
import scalafx.Includes.*
import javafx.fxml.FXML
import javafx.scene.control.{TableColumn, TableView}
import scalafx.collections.ObservableBuffer
import scalafx.scene.control.TextInputDialog
import scala.util.{Failure, Success}
import java.time.LocalDate


class DonationsController:
  //TableView displaying the list of Donated Items
  @FXML private var donationTable: TableView[DonatedItems] = null

  //column showing the name of the donor
  @FXML private var donorColumn: TableColumn[DonatedItems, String] = null

  //column showing the name of the donated item
  @FXML private var itemColumn: TableColumn[DonatedItems, String] = null

  //column showing the quantity of the donated item
  @FXML private var quantityColumn: TableColumn[DonatedItems, Int] = null

  //column that shows the date of the donated item
  @FXML private var dateColumn: TableColumn[DonatedItems, LocalDate] = null


  //used to set up the Donations TableView
  def initialize(): Unit =
    //refresh the table to ensure the information shown are up to date
    donationTable.refresh()
    //set the items of the table observable list of donated items data
    donationTable.items = RunTheGiver.donatedItemData

    //bind each column to their corresponding properties in the Donated Item, Donation and Donors model
    donorColumn.cellValueFactory = { x => x.value.donationD.donorD.nameProperty }
    itemColumn.cellValueFactory = { x => x.value.itemC.nameProperty }
    quantityColumn.cellValueFactory = { x => x.value.quantityProperty }
    dateColumn.cellValueFactory = { x => x.value.donationD.donationDateProperty}
  end initialize


  //delete donation item
  def handleDeleteDonation(action: ActionEvent): Unit =
    //retrieve the index of the donation item selected from the donationTable
    val selectedIndex = donationTable.selectionModel().selectedIndex.value
    //retrieve the item of the donation item selected from the donationTable
    val selectedDonation = donationTable.selectionModel().selectedItem.value

    if (selectedIndex >= 0) then
      //if a donation item is selected, then attempt to delete the record
      selectedDonation.deleteRecord match
        //if successful, remove the item from TableView
        case Success(x) =>
          donationTable.items().remove(selectedIndex)
        //if not successful, display an error alert
        case Failure(x) =>
          Alert.displayError("Delete unsuccessful", "The record was not deleted", "Please try again")
    else
      //if no donation item was selected, then display the error alert
      Alert.displayError("Invalid donation", "No donation record is selected", "Please choose a donation")
    end if
  end handleDeleteDonation


  //refresh the donationTable
  def refreshTable(): Unit =
    //retrieve all records from the database
    val updateItems = DonatedItems.getAllRecords()
    //update the table
    donationTable.items = ObservableBuffer(updateItems: _*)
    //refresh the table
    donationTable.refresh()
  end refreshTable
