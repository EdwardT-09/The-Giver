package donatesystem.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import scalafx.Includes.*
import javafx.scene.control.{DatePicker, TextField}
import donatesystem.util.Alert
import donatesystem.model.Donor
import donatesystem.RunTheGiver
import scalafx.stage.Stage

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

  def donor = __donor

  def donor_=( donor: Donor):Unit = {
    __donor = donor
    nameField.text = __donor.nameProperty.value
    emailField.text = __donor.emailProperty.value
    birthdayField.value = __donor.birthdayProperty.value
    contactNoField.text = __donor.contactNoProperty.value
    occupationField.text = __donor.occupationProperty.value
  }

  def handleAddDonor(action:ActionEvent):Unit =
    if validInput() then
      val birthday: LocalDate = birthdayField.value()
      val donor = new Donor(0, nameField.text.value, emailField.text.value, birthday, contactNoField.text.value, occupationField.text.value)
      donor.saveToDonor match
        case Success(x) => Alert.displayAlert("Success", "Success", "Password must have at least one upper case, lower case, number and symbol")
          result = Some(donor)
          dialogStage.close()
        case Failure(error) => Alert.displayAlert("Unsuccessful", "Email is in use", error.getMessage)
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
    if (!validEmail()) then
      errorMessage += "Email provided is invalid"
    if(!validContactNo()) then
      errorMessage +=  "The contact number provided is invalid"

    if(errorMessage.length() == 0) then
      true
    else {
      Alert.displayAlert("Invalid Fields", "Please fill in all fields correctly.", errorMessage)
      false
    }
  end validInput
  def validEmail(): Boolean =
    val email_pattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$".r
    if (!email_pattern.matches(emailField.text.value)) {
      false
    } else {
      true
    }
  end validEmail

  def validContactNo(): Boolean =
    val contactNo_pattern = "^[0-9]{3}-[0-9]{3,4}-[0-9]{3,4}$".r
    if(!contactNo_pattern.matches(contactNoField.text.value)) then
      false
    else
      true
  end validContactNo

end AddDonorController

