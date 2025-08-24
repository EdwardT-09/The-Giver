package donatesystem.view

import scalafx.Includes.*
import donatesystem.model.{Beverage, CatalogItem}
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{CheckBox, TextField}
import scalafx.stage.Stage
import scala.util.{Failure, Success}
import donatesystem.util.Alert

//provide controls to beverage page
@FXML
class AddBeverageController:

  //text field for beverage 's name
  @FXML private var nameField: TextField = _
  //text field for beverage's category
  @FXML private var categoryField: TextField = _
  //check box for beverage's perishability
  @FXML private var isPerishableField: CheckBox = _
  //check box for whether the beverage carbonated
  @FXML private var isCarbonatedField: CheckBox = _
  //text field for beverage's volume per unit
  @FXML private var volumePerUnitField: TextField = _

  //used for Beverage objects that are being edited
  private var __beverage: Beverage = null

  //reference to the dialog window for this controller
  var dialogStage: Stage = null

  //used to indicate the outcome of the dialog
  var result: Option[Beverage] = None

  //stores the original name of the beverage item for beverage information update
  private var originalName: String = null

  //getter for the Beverage object
  def beverage = __beverage

  //setter for the Beverage object
  //allows the selected Beverage in beverageTable to be edited and populate the add beverage page UI with the values from the beverage object
  //store the original name for comparison in the database
  def beverage_=(beverage: Beverage): Unit =
    __beverage = beverage
    originalName = beverage.nameProperty.value
    nameField.text = __beverage.nameProperty.value
    categoryField.text = __beverage.categoryProperty.value
    isPerishableField.setSelected(__beverage.isPerishableProperty.get())
    isCarbonatedField.setSelected(__beverage.isCarbonatedProperty.get())
    volumePerUnitField.text = __beverage.volumePerUnitProperty.value.toString

  //main function to handle add beverage
  def handleAddBeverage(action: ActionEvent): Unit =
    if validInput() then
      //if required fields are filled, then attempt to retrieve beverage record using its original name
      val beverage = CatalogItem.getRecordByKey(originalName)
      beverage match
        //if retrieval is successful, then ensure that the retrieval is a Beverage object
        //reassign the new values into the respective properties
        case Some(beverage:Beverage) =>
          beverage.nameProperty.value = nameField.text.value
          beverage.categoryProperty.value = categoryField.text.value
          beverage.isPerishableProperty.value = isPerishableField.isSelected
          beverage.isCarbonatedProperty.value = isCarbonatedField.isSelected
          beverage.volumePerUnitProperty.value = volumePerUnitField.text.value.toInt
          //attempt to save the beverage object
          beverage.saveAsRecord() match
            //if successful, display success message, assign result to Some(beverage) and close the dialog stage
            case Success(x) => Alert.displayInformation("Success", "Success", "The record has been updated")
              result = Some(beverage)
              dialogStage.close()
            //else, display the error message
            case Failure(error) => Alert.displayError("Unsuccessful", "Record is not updated", "Please try again")
        //if return result that is not Beverage, then display error
        case Some(_) =>
          Alert.displayInformation("Type Mismatch", "The selected record is not a beverage", "Please select a beverage item")
        //if return None, create a new Beverage object
        case None =>
          val beverage = new Beverage(0, nameField.text.value, categoryField.text.value, isPerishableField.isSelected, 0, volumePerUnitField.text.value.toInt, isCarbonatedField.isSelected)
          //attempt to save the beverage as record in database
          beverage.saveAsRecord() match
            //if successful, display success message, assign result to Some(beverage) and close the dialog stage
            case Success(x) => Alert.displayInformation("Success", "Success", "A record has been created")
              result = Some(beverage)
              dialogStage.close()
            //else, display error message
            case Failure(error) => Alert.displayError("Unsuccessful", "Record is not created", "Please try again")
  end handleAddBeverage


  //check if any fields are left empty
  private def validInput(): Boolean =
    //trim the volume per unit 
    val volume = volumePerUnitField.text.value.trim

    //create a variable to store error message(s)
    var errorMessage: String = ""

    //if name field is left empty, then add name empty field error to errorMessage
    if (nameField.text.value.isEmpty) then
      errorMessage += "*Name field is empty\n"
    end if 
    
    //if category field is left empty, then add category empty field error to errorMessage
    if (categoryField.text.value.isEmpty) then
      errorMessage += "*Category field is empty\n"
    end if 
    
    //if volume field is empty, then add volume per unit field error to errorMessage  
    if (volume.isEmpty) then
      errorMessage += "*Volume Per Unit field is empty\n"
    //if the volume is not a number, then add the error to errorMessage
    else if (!volume.matches("""\d+"""))
      errorMessage += "*Volume per unit must be a non-negative number\n"
    //if the volume is not a positive number, then add the error to errorMessage
    else if(volume.toInt < 0) then
      errorMessage += "*Volume per unit must be a non-negative number\n"
    end if
    
    //if errorMessage does not have any error message then return true
    if (errorMessage.isEmpty) then
      true
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return false
      Alert.displayError("Invalid Fields", "Please fill in all required fields correctly.", errorMessage)
      false

  end validInput
    
end AddBeverageController

