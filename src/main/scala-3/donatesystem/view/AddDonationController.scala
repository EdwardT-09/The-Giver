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
import scala.util.{Failure, Success}
import java.time.LocalDate


@FXML
class AddDonationController:

  //choice box for donors
  @FXML private var donorChoiceBox: ChoiceBox[Donor] = _

  //choice box for catalog items
  @FXML private var itemChoiceBox:ChoiceBox[CatalogItem] =_

  //text field for quantity of items
  @FXML private var quantityField:TextField = _

  //TableView for the donatedItems
  @FXML private var donatedItemsTable: TableView[DonatedItems] = _

  //column for the donated catalog item name
  @FXML private var nameColumn: TableColumn[DonatedItems, String] = _

  //column for the donated catalog item quantity
  @FXML private var quantityColumn: TableColumn[DonatedItems, Int] = _


  //reference to the dialog window for this controller
  var dialogStage:Stage = null

  //create items buffer for items to be temporarily placed in
  val itemsBuffer = ObservableBuffer[DonatedItems]()

  //used to indicate the outcome of the dialog
  var result: Option[Donor] = None

  @FXML
  def initialize():Unit =
    //populate the donor choice box with all donor records
    donorChoiceBox.items = ObservableBuffer(Donor.getAllRecords(): _*)

    //define the names as how the donors are to be identified
    donorChoiceBox.converter = new StringConverter[Donor] {
      override def toString(donor: Donor): String =
        if donor != null then donor.nameProperty.value else ""

      override def fromString(string: String): Donor = null
    }

    //populate the item choice box with catalog items
    itemChoiceBox.items = ObservableBuffer(CatalogItem.getAllCatalogItems(): _*)

    //define the name as how the items are to be identified
    itemChoiceBox.converter = new StringConverter[CatalogItem] {
      override def toString(item: CatalogItem): String =
        if item != null then item.nameProperty.value else ""

      override def fromString(string: String): CatalogItem = null
    }

    //bind donated items table to observable list(itemsBuffer)
      donatedItemsTable.items = itemsBuffer

    //set up table column to display item name
      nameColumn.cellValueFactory = { x =>
        new StringProperty(x.value.item.nameProperty.value)
      }

    //set up table column to display item quantity
      quantityColumn.cellValueFactory = {x =>
        x.value.quantityProperty
      }

  end initialize

  //main function to handle add donation
  def handleAddDonation(event:ActionEvent): Unit =
    if (validInput) then
      //retrieve the selected donor
      val selectedDonor = donorChoiceBox.getValue
      println(s"Selected donor is: ${if selectedDonor != null then selectedDonor.nameProperty.value else "null"}")
      //create new donation object if input is valid
      val donation = new Donation(0, selectedDonor, LocalDate.now())

      //attempt to save the donation record
      val savedDonationID = donation.saveAsRecord()

      savedDonationID match
        case Success(id) =>
          //if saving is successful, retrieve the saved donation record
          Donation.getRecordByKey(id) match
            case Some(savedDonation) =>
            //if record found, then loop through all the donated items to save them individually
              for i <- itemsBuffer.indices do
                val item = itemsBuffer(i)

                //create a new DonatedItem object associated with the saved Donation
                val saveDonatedItem = new DonatedItems(0, savedDonation, item.item, item.quantity)

                //update the itemsBuffer with the new DonatedItems object
                itemsBuffer(i) = saveDonatedItem

                //increase the catalog quantity accordingly
                CatalogItem.increaseQuantity(item.item.itemID,item.quantity) match
                  case Success(rows) => println(s"Updated ${item.item.itemID} +${item.quantity}")
                  case Failure(err) => println(s"Failed qty update: ${err.getMessage}")

                //save the donated items into the database
                saveDonatedItem.saveAsRecord() match
                  case Success(x) =>
                    //display alert when successful
                    Alert.displayInformation("Successful", "Donated Item saved","The donated item has been saved")
                  case Failure(error) =>
                    //display error when unsuccessful
                    Alert.displayError("Unsuccessful", "The donated items are not saved","Try again")
            case None =>
              //if not records are retrieved, then display error message
              Alert.displayError("Record not found", "Donation record not found","Please try again")
        case Failure(error) =>
          //if failed to save record to database, then display error
          Alert.displayError("Unsuccessful", "Failed to save","Please try again")
    end if
  end handleAddDonation


  //used to add item into items buffer
  def handleAddItem(event:ActionEvent):Unit =
    //check if item inputs are valid
    if validItemInput then
      //assign item choice box value into selected item
      val selectedItem = itemChoiceBox.value.value

      //assign the quantity field into quantity
      val quantity = quantityField.text.value.toInt

      //if valid, then create DonatedItems object
      val donatedItem = new DonatedItems(0, null, selectedItem, quantity)
      //assign the donated item to items buffer
      itemsBuffer += donatedItem
  end handleAddItem

  def handleDeleteItem(event:ActionEvent): Unit =
    //retrieve the index of the donated item selected from the donatedItemsTable
    val selectedIndex = donatedItemsTable.selectionModel().selectedIndex.value
    //retrieve the item of the donated item selected from the donatedItemsTable
    val selectedDonatedItem = donatedItemsTable.selectionModel().selectedItem.value
    if (selectedIndex >= 0) then
    //if a donated item is selected, then prompt for confirmation
      val confirm = Alert.displayConfirmation("Delete Donated Item",
        "Are you sure you want this record to be deleted",
        s"Name: ${selectedDonatedItem.item.nameProperty.value} Quantity: ${selectedDonatedItem.quantityProperty.value}")

      if confirm then
        //if confirm is true then remove from buffer with the selected index
        itemsBuffer.remove(selectedIndex)
    else
      Alert.displayError("Invalid donation item", "No donation item record is selected", "Please choose a donation item")
  end handleDeleteItem


  //check if inputs are valid
  def validInput: Boolean =
    //create a variable to store error message(s)
    var errorMessage: String = ""

    //if donor choice box is null, then add error message to errorMessage
    if(donorChoiceBox.getValue == null) then
      errorMessage += "*Donor field is empty\n"
    end if

    //if item buffer is null, then add error message to errorMessage
    if(itemsBuffer.isEmpty) then
      errorMessage += "*Items Buffer is empty\n"
    end if

    //if errorMessage does not have any error message then return true
    if (errorMessage.isEmpty) then
      true
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return false
      Alert.displayError("Invalid Fields", "Please fill in all required fields correctly.", errorMessage)
      false
    end if
  end validInput

  //check if item input is valid
  def validItemInput:Boolean =
    //create a variable to store error message(s)
    var errorMessage: String =""

    //trim the quantity
    val quantity = quantityField.text.value.trim

    //if item choice box is null, then add error message to errorMessage
    if(itemChoiceBox.getValue == null) then
      errorMessage += "*Items Field is empty\n"
    end if

    //if quantity is empty, then add quantity field error to errorMessage
    if(quantity.isEmpty) then
      errorMessage += "*Quantity Field is empty\n"
    //if the quantity is not a number, then add the error to errorMessage
    else if (!quantity.matches("""\d+"""))
      errorMessage += "*Quantity must be a non-negative number\n"
    //if the quantity is not a positive number, then add the error to errorMessage
    else if (quantity.toInt < 0) then
      errorMessage += "*Quantity must be a non-negative number\n"
    end if

    //if errorMessage does not have any error message then return true
    if (errorMessage.isEmpty) then
      true
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return false
      Alert.displayError("Invalid Fields", "Please fill in all required fields correctly.", errorMessage)
      false
    end if

  end validItemInput



