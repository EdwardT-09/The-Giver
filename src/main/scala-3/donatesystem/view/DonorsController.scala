package donatesystem.view

import donatesystem.MainApp
import donatesystem.model.Donor
import javafx.fxml.FXML
import javafx.scene.control.{Label, TableColumn, TableView}
import scalafx.Includes.*

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
    donorTable.items = MainApp.donorData
    nameColumn.cellValueFactory = {x=> x.value.nameProperty}
    emailColumn.cellValueFactory = {x=> x.value.emailProperty}
    birthdayColumn.cellValueFactory = { x => x.value.birthdayProperty }
    contactNoColumn.cellValueFactory = { x => x.value.contactNoProperty }
    occupationColumn.cellValueFactory = { x => x.value.occupationProperty }

  end initialize



  def directToAddDonor:Unit =
    MainApp.showAddDonor()
  end directToAddDonor
end DonorsController
