package donatesystem.view

import scalafx.Includes.*
import donatesystem.model.{CatalogItem, DonatedItems, Donation, Donor}
import javafx.event.ActionEvent
import scalafx.util.StringConverter
import javafx.fxml.FXML
import javafx.scene.control.{ChoiceBox, TableColumn, TableView, TextField}
import scalafx.collections.ObservableBuffer
import donatesystem.util.Alert
import scalafx.beans.property.StringProperty
import scalafx.stage.Stage

import scala.util.{Failure, Success, Try}
import java.time.LocalDate


@FXML
class AddDonationController:
  @FXML private var donorChoiceBox: ChoiceBox[Donor] = _
  @FXML private var itemChoiceBox:ChoiceBox[CatalogItem] =_
  @FXML private var quantityField:TextField = _

  @FXML private var donatedItemsTable: TableView[DonatedItems] = _
  @FXML private var nameColumn: TableColumn[DonatedItems, String] = _
  @FXML private var quantityColumn: TableColumn[DonatedItems, Int] = _

  var dialogStage:Stage = null
  val itemsBuffer = ObservableBuffer[DonatedItems]()
  var result: Option[Donor] = None

  @FXML
  def initialize():Unit =
    donorChoiceBox.items = ObservableBuffer(Donor.getAllRecords(): _*)

    donorChoiceBox.converter = new StringConverter[Donor] {
      override def toString(donor: Donor): String =
        if donor != null then donor.nameProperty.value else ""

      override def fromString(string: String): Donor = null
    }
    itemChoiceBox.items = ObservableBuffer(CatalogItem.getAllCatalogItems(): _*)

    itemChoiceBox.converter = new StringConverter[CatalogItem] {
      override def toString(item: CatalogItem): String =
        if item != null then item.nameProperty.value else ""

      override def fromString(string: String): CatalogItem = null
    }
      donatedItemsTable.items = itemsBuffer

      nameColumn.cellValueFactory = { x =>
        new StringProperty(x.value.item.nameProperty.value)
      }

      quantityColumn.cellValueFactory = {x =>
        x.value.quantityProperty
      }


  end initialize

  def handleAddDonation(event:ActionEvent): Unit =
    if (validInput) then
      val donation = new Donation(0, donorChoiceBox.value.value, LocalDate.now())
      val savedDonationID = donation.saveAsRecord
      savedDonationID match
        case Success(id) =>
          Donation.getRecordByKey(id) match
            case Some(savedDonation) =>
              for i <- itemsBuffer.indices do
                val item = itemsBuffer(i)
                val saveDonatedItem = new DonatedItems(0, savedDonation, item.item, item.quantity)
                itemsBuffer(i) = saveDonatedItem
                item.item.increaseQuantity(item.quantity) match
                  case Success(rows) => println(s"Updated ${item.item.itemIDI} +${item.quantity}")
                  case Failure(err) => println(s"Failed qty update: ${err.getMessage}")
                saveDonatedItem.saveAsRecord match
                  case Success(x) =>
                    Alert.displayError("Yay", "Yay","Yay")
                  case Failure(error) =>
                    Alert.displayError("No", "No",error.getMessage)
            case None =>
              Alert.displayError("Record not found", "Donation record not found","Please try again")
        case Failure(error) =>
          Alert.displayError("Unsuccessful", "Failed to save",error.getMessage)
    end if
  end handleAddDonation

  def handleAddItem(event:ActionEvent):Unit =
    val selectedItem = itemChoiceBox.value.value
    val quantity = quantityField.text.value.toInt

    if validItemInput then
      val donatedItem = new DonatedItems(0, null, selectedItem, quantity)
      itemsBuffer += donatedItem
  end handleAddItem

  def validInput: Boolean =
    var errorMessage: String = ""
    if(donorChoiceBox.value.value == null) then
      errorMessage += "Donor field is empty"
    if(itemsBuffer.isEmpty) then
      errorMessage += "Items Buffer is empty"
    if (errorMessage.isEmpty) then
      true
    else
      Alert.displayError("Invalid Fields", "Please fill in all required fields correctly.", errorMessage)
      false
  end validInput

  def validItemInput:Boolean =
    var errorMessage: String =""
    val quantity = quantityField.text.value.trim
    if(itemChoiceBox == null) then
      errorMessage += "Items Field is empty\n"
    if(quantity.isEmpty) then
      errorMessage += "Quantity Field is empty\n"
    else if (!quantity.matches("""\d+"""))
      errorMessage += "Volume per unit must be a non-negative number\n"
    else if (quantity.toInt < 0) then
      errorMessage += "Volume per unit must be a non-negative number\n"

    if (errorMessage.isEmpty) then
      true
    else
      Alert.displayError("Invalid Fields", "Please fill in all required fields correctly.", errorMessage)
      false

  end validItemInput



