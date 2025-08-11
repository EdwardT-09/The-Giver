package donatesystem.view

import donatesystem.RunTheGiver
import donatesystem.model.Donor
import javafx.fxml.FXML
import javafx.scene.control.{Label, TableColumn, TableView}
import scalafx.Includes.*
import donatesystem.util.Alert

import scala.util.{Failure, Success, Try}
import java.time.LocalDate

@FXML
class DonorsController:
  @FXML private var donorTable:TableView[Donor] = null
  @FXML private var nameColumn: TableColumn[Donor, String] = null
  @FXML private var emailColumn: TableColumn[Donor, String] = null
  @FXML private var birthdayColumn :TableColumn[Donor, LocalDate] = null
  @FXML private var contactNoColumn:TableColumn[Donor, String] = null
  @FXML private var occupationColumn:TableColumn[Donor,String] = null
//  @FXML private var donorIDLabel: Label = null
//  @FXML private var nameLabel:Label = null
//  @FXML private var emailLabel: Label
//  @FXML
//  @FXML

  def initialize():Unit =
    donorTable.items = RunTheGiver.donorData
    nameColumn.cellValueFactory = {x=> x.value.nameProperty}
    emailColumn.cellValueFactory = {x=> x.value.emailProperty}
    birthdayColumn.cellValueFactory = { x => x.value.birthdayProperty }
    contactNoColumn.cellValueFactory = { x => x.value.contactNoProperty }
    occupationColumn.cellValueFactory = { x => x.value.occupationProperty }
  end initialize


  def directToAddDonor:Unit =
    val donor = new Donor(0, "", "", LocalDate.now(), "","")
    RunTheGiver.showAddDonor(donor) match
      case Some(newDonor) =>
        donorTable.items().addLast(newDonor)
      case None =>
        
  end directToAddDonor


  def directToEditDonor: Unit =
    val selectedIndex = donorTable.selectionModel().selectedIndex.value
    val selectedDonor = donorTable.selectionModel().selectedItem.value

    if(selectedDonor != null) then
      RunTheGiver.showAddDonor(selectedDonor) match
        case Some(updatedDonor) =>
          donorTable.items().update(selectedIndex, updatedDonor)
        case None =>
          Alert.displayAlert("Update Error", "The donor record was not updated", "Please try again")
    end if
  end directToEditDonor

  def deleteDonor:Unit =
    val selectedIndex = donorTable.selectionModel().selectedIndex.value
    val selectedDonor = donorTable.selectionModel().selectedItem.value
    if(selectedIndex >= 0) then
      selectedDonor.deleteRecord match
        case Success(x) =>
          donorTable.items().remove(selectedIndex)
        case Failure(x) =>
          Alert.displayAlert("Delete unsuccessful", "The record was not deleted", "Please try again")
    else
      Alert.displayAlert("Invalid Donor", "No donor record is selected", "Please choose a donor")
    end if
  end deleteDonor

end DonorsController