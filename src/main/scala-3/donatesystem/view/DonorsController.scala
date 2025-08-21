package donatesystem.view

import donatesystem.RunTheGiver
import donatesystem.model.Donor
import javafx.fxml.FXML
import javafx.scene.control.{TableColumn, TableView}
import scalafx.Includes.*
import donatesystem.util.Alert
import javafx.event.ActionEvent
import scala.util.{Failure, Success}
import java.time.LocalDate

//provide controls to donors management page
@FXML
class DonorsController:

  //TableView displaying the list of Donors
  @FXML private var donorTable:TableView[Donor] = null

  //column showing the name of the donors
  @FXML private var nameColumn: TableColumn[Donor, String] = null

  //column showing the email of the donors
  @FXML private var emailColumn: TableColumn[Donor, String] = null

  //column showing the birthday of the donors
  @FXML private var birthdayColumn :TableColumn[Donor, LocalDate] = null

  //column showing the contact number of the donors
  @FXML private var contactNoColumn:TableColumn[Donor, String] = null

  //column showing the occupation of the donors
  @FXML private var occupationColumn:TableColumn[Donor,String] = null

  //used to set up the Donor TableView
  def initialize():Unit =
    //set the items of the table observable list of donor data
    donorTable.items = RunTheGiver.donorData

    //bind each column to their corresponding properties in the Donor model
    nameColumn.cellValueFactory = {x=> x.value.nameProperty}
    emailColumn.cellValueFactory = {x=> x.value.emailProperty}
    birthdayColumn.cellValueFactory = { x => x.value.birthdayProperty }
    contactNoColumn.cellValueFactory = { x => x.value.contactNoProperty }
    occupationColumn.cellValueFactory = { x => x.value.occupationProperty }
  end initialize

  //direct users to add new donor
  def directToAddDonor(action :ActionEvent):Unit =
    //create new donor object
    val donor = new Donor(0, "", "", LocalDate.now(), "","")
    //pass the donor object to showAddDonor method
    RunTheGiver.showAddDonor(donor) match
      //if newDonor is added, donorTable is updated with the new donor and refreshed to ensure up-to-date table
      case Some(newDonor) =>
        donorTable.items().addLast(newDonor)
        donorTable.refresh()
      //if none, then return nothing
      case None =>
        
  end directToAddDonor

  //direct users to add donor page but with fields pre-filled in with their current information
  def directToEditDonor(action :ActionEvent): Unit =
    //retrieve the index of the currently selected donor from the donorTable
    val selectedIndex = donorTable.selectionModel().selectedIndex.value
    //retrieve the item of the currently selected donor from the donorTable
    val selectedDonor = donorTable.selectionModel().selectedItem.value

    if(selectedDonor != null) then
      //if donor is selected, pass the selected donor to showAddDonor
      RunTheGiver.showAddDonor(selectedDonor) match
        //if updatedDonor is found, then update the donorTable to display new information
        //refresh the table to display up-to-date information
        case Some(updatedDonor) =>
          donorTable.items().update(selectedIndex, updatedDonor)
          donorTable.refresh()
        //if no updatedDonor, then return error alert
        case None =>
          Alert.displayError("Update Error", "The donor record was not updated", "Please try again")
    end if
  end directToEditDonor

  //delete donor
  def handleDeleteDonor(action :ActionEvent):Unit =
    //retrieve the index of the currently selected donor from the donorTable
    val selectedIndex = donorTable.selectionModel().selectedIndex.value
    //retrieve the item of the currently selected donor from the donorTable
    val selectedDonor = donorTable.selectionModel().selectedItem.value

    if(selectedIndex >= 0) then
      //if a donor is selected, then attempt to delete the record
      selectedDonor.deleteRecord match
        //if successful, remove the item from TableView
        case Success(x) =>
          donorTable.items().remove(selectedIndex)
        //if not successful, display an error alert
        case Failure(x) =>
          Alert.displayError("Delete unsuccessful", "The record was not deleted", "Please try again")
    //if no donor was selected, then display the error alert
    else
      Alert.displayError("Invalid Donor", "No donor record is selected", "Please choose a donor")
    end if
  end handleDeleteDonor

end DonorsController