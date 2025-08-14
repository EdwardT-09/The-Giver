package donatesystem.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import scalafx.Includes.*
import javafx.scene.control.{DatePicker, TextField}
import donatesystem.util.Alert
import donatesystem.model.Donor
import donatesystem.RunTheGiver
import scalafx.stage.Stage
import donatesystem.util.PatternMatch
import java.time.LocalDate
import scala.util.{Failure, Success}


@FXML
class AddDonorController:
  @FXML private var nameField: TextField = _
  @FXML private var emailField: TextField = _
  @FXML private var birthdayField: DatePicker = _
  @FXML private var contactNoField: TextField = _
  @FXML private var occupationField: TextField = _

  private var __donor: Donor = null
  var dialogStage:Stage = null
  var result: Option[Donor] = None
  private var originalEmail:String = null
  def donor = __donor

  def donor_=( donor: Donor):Unit = {
    __donor = donor
    originalEmail = donor.emailProperty.value
    nameField.text = __donor.nameProperty.value
    emailField.text = __donor.emailProperty.value
    birthdayField.value = __donor.birthdayProperty.value
    contactNoField.text = __donor.contactNoProperty.value
    occupationField.text = __donor.occupationProperty.value
  }

  def handleAddDonor(action:ActionEvent):Unit =
    if validInput() then
      val birthday: LocalDate = birthdayField.value()
      val donor = Donor.getRecordByKey(originalEmail)
      donor match
        case Some(donor) =>
          donor.nameProperty.value = nameField.text.value
          donor.emailProperty.value = emailField.text.value
          donor.birthdayProperty.value = birthdayField.value.value
          donor.contactNoProperty.value = contactNoField.text.value
          donor.occupationProperty.value = occupationField.text.value
          donor.saveAsRecord match
            case Success(x) => Alert.displayError("Success", "Success", "The record has been updated")
              result = Some(donor)
              dialogStage.close()
            case Failure(error) => Alert.displayError("Unsuccessful", "Record is not updated", "Please try again")
        case None =>
          val donor = new Donor(0, nameField.text.value, emailField.text.value, birthdayField.value.value,contactNoField.text.value,occupationField.text.value)
          donor.saveAsRecord match
            case Success(x) =>  Alert.displayError("Success", "Success", "A record has been created")
              result = Some(donor)
              dialogStage.close()
            case Failure(error) => Alert.displayError("Unsuccessful", "Record is not created", "Please try again")
            
  end handleAddDonor
  
  def assignToDonor():Unit = {
    __donor.nameProperty <= nameField.text
    __donor.emailProperty <= emailField.text
    __donor.birthdayProperty <== birthdayField.valueProperty()
    __donor.contactNoProperty <= contactNoField.text
    __donor.occupationProperty <= occupationField.text

  }
  end assignToDonor
  def validInput(): Boolean =
    var errorMessage:String = ""
    if (nameField.text.value.isEmpty) then
      errorMessage += "Name field is empty"
    if (emailField.text.value.isEmpty) then
      errorMessage += "Email field is empty"
    if (birthdayField.value == null) then
      errorMessage += "Birthday field is empty"
    if (contactNoField.text.value.isEmpty) then
      errorMessage += "Contact number field is empty"
    if (occupationField.text.value.isEmpty) then
      errorMessage += "Occupation field is empty"
    if (!PatternMatch.validEmail(emailField.text.value)) then
      errorMessage += "Email provided is invalid"
    if(!PatternMatch.validContactNo(contactNoField.text.value)) then
      errorMessage +=  "The contact number provided is invalid"

    if(errorMessage.isEmpty) then
      true
    else {
      Alert.displayError("Invalid Fields", "Please fill in all fields correctly.", errorMessage)
      false
    }
  end validInput

end AddDonorController

