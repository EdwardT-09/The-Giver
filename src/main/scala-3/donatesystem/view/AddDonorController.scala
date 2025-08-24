package donatesystem.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import scalafx.Includes.*
import javafx.scene.control.{DatePicker, TextField}
import donatesystem.util.Alert
import donatesystem.model.Donor
import scalafx.stage.Stage
import donatesystem.util.PatternMatch
import java.time.LocalDate
import scala.util.{Failure, Success}

//provide controls to donor page
@FXML
class AddDonorController:

  //text field for donor 's name
  @FXML private var nameField: TextField = _

  //text field for donor 's email
  @FXML private var emailField: TextField = _

  //date picker for donor 's birthday
  @FXML private var birthdayField: DatePicker = _

  //text field for donor 's contact number
  @FXML private var contactNoField: TextField = _

  //text field for donor's occupation
  @FXML private var occupationField: TextField = _


  //used for Donor objects that are being edited
  private var __donor: Donor = null

  //reference to the dialog window for this controller
  var dialogStage:Stage = null

  //used to indicate the outcome of the dialog
  var result: Option[Donor] = None

  //stores the original name of the donor item for donor information update
  private var originalEmail:String = null

  //getter for the Donor object
  def donor = __donor


  //setter for the Donor object
  //allows the selected donor in donorTable to be edited and populate the add donor page UI with the values from the donor object
  //store the original name for comparison in the database
  def donor_=( donor: Donor):Unit = {
    __donor = donor
    originalEmail = donor.emailProperty.value
    nameField.text = __donor.nameProperty.value
    emailField.text = __donor.emailProperty.value
    birthdayField.value = __donor.birthdayProperty.value
    contactNoField.text = __donor.contactNoProperty.value
    occupationField.text = __donor.occupationProperty.value
  }
  
  def initialize():Unit =
    birthdayField.getEditor().setDisable(true)
  end initialize

  //main function to handle add donor
  def handleAddDonor(action:ActionEvent):Unit =
    if validInput() then
      if patternMatch then
        //assign birthdayField values into LocalDate
        val birthday: LocalDate = birthdayField.value()
        //if required fields are filled and matches pattern, then attempt to retrieve donor record using its original name
        val donor = Donor.getRecordByKey(originalEmail)
        donor match
          //if retrieval is successful, then ensure that the retrieval is a donor object
          //reassign the new values into the respective properties
          case Some(donor) =>
            donor.nameProperty.value = nameField.text.value
            donor.emailProperty.value = emailField.text.value
            donor.birthdayProperty.value = birthdayField.value.value
            donor.contactNoProperty.value = contactNoField.text.value
            donor.occupationProperty.value = occupationField.text.value
            //attempt to save the donor object
            donor.saveAsRecord() match
              //if successful, display success message, assign result to Some(donor) and close the dialog stage
              case Success(x) => Alert.displayInformation("Success", "Success", "The record has been updated")
                result = Some(donor)
                dialogStage.close()
              //else, display the error message
              case Failure(error) => Alert.displayError("Unsuccessful", "Record is not updated", "Please try again")
          //if return None, create a new Donor object
          case None =>
            val donor = new Donor(0, nameField.text.value, emailField.text.value, birthdayField.value.value,contactNoField.text.value,occupationField.text.value)
            donor.saveAsRecord() match
              //if successful, display success message, assign result to Some(donor) and close the dialog stage
              case Success(x) =>  Alert.displayInformation("Success", "Success", "A record has been created")
                result = Some(donor)
                dialogStage.close()
              //else, display error message
              case Failure(error) => Alert.displayError("Unsuccessful", "Record is not created", "Please try again")

  end handleAddDonor



  //check if any fields are left empty
  def validInput(): Boolean =
    //create a variable to store error message(s)
    var errorMessage:String = ""

    //if name field is left empty, then add name empty field error to errorMessage
    if (nameField.text.value.isEmpty) then
      errorMessage += "*Name field is empty\n"
    end if
    //if email field is left empty, then add email empty field error to errorMessage
    if (emailField.text.value.isEmpty) then
      errorMessage += "*Email field is empty\n"
    end if

    //if birthday field is left empty, then add birthday empty field error to errorMessage
    if (birthdayField.value == null) then
      errorMessage += "*Birthday field is empty\n"
    end if

    //if contact number field is left empty, then add contact number empty field error to errorMessage
    if (contactNoField.text.value.isEmpty) then
      errorMessage += "*Contact number field is empty\n"
    end if

    //if occupation field is left empty, then add occupation empty field error to errorMessage
    if (occupationField.text.value.isEmpty) then
      errorMessage += "*Occupation field is empty\n"
    end if

    if(errorMessage.isEmpty) then
      //if errorMessage does not have any error message then return true
      true
    else {
      //if errorMessage has error message(s), then display an error alert, list the errors and return false
      Alert.displayError("Invalid Fields", "Please fill in all fields correctly.", errorMessage)
      false
    }
  end validInput


  def patternMatch:Boolean =
    //create a variable to store error message(s)
    var errorMessage:String = ""

    if (!PatternMatch.validEmail(emailField.text.value)) then {
      //if email provided does not match email pattern, then add error message to errorMessage
      errorMessage += "*Email provided is invalid. Format must be name@example.com\n"
    }
    end if

    if (!PatternMatch.validContactNo(contactNoField.text.value)) then
      //if contact number provided does not match contact number pattern, then add error message to errorMessage
      errorMessage += "*The contact number provided is invalid. Format should be 011-xxx-xxx\n"
    end if

    if (errorMessage.isEmpty) then
      //if errorMessage does not have any error message then return true
      true
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return false
      Alert.displayError("Invalid Fields", "Please fill in all fields correctly.", errorMessage)
      false
    end if
  end patternMatch

end AddDonorController

