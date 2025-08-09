package donatesystem.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import scalafx.Includes.*
import javafx.scene.control.{DatePicker, TextField}
import donatesystem.util.Alert
import donatesystem.model.Donor
import donatesystem.MainApp

import java.time.LocalDate
import scala.util.{Failure, Success}


@FXML
class AddDonorController:
  @FXML private var nameField: TextField = _
  @FXML private var emailField: TextField = _
  @FXML private var birthdayField: DatePicker = _
  @FXML private var contactNoField: TextField = _
  @FXML private var occupationField: TextField = _
  

  def handleAddDonor(action:ActionEvent):Unit = {
    if(nameField.text.value.isEmpty|| emailField.text.value.isEmpty|| birthdayField.value == null|| contactNoField.text.value.isEmpty ||occupationField.text.value.isEmpty) then
      Alert.displayAlert("Empty Field", "One or more fields are left empty." , "Please fill in the empty field(s).")
    else if (!validEmail()) then
      Alert.displayAlert("Invalid Email", "Email provided is invalid", "Enter using the following format 'name@example.com")
    else if(!validContactNo()) then
      Alert.displayAlert("Invalid Contact Number", "The contact number provided is invalid", "Enter using the following format '012-3456-7890")
    else
      val birthday: LocalDate = birthdayField.value()
      val donor = new Donor(1, nameField.text.value, emailField.text.value, birthday, contactNoField.text.value, occupationField.text.value)
      donor.saveToDonor match{
        case Success(result) => Alert.displayAlert("Success", "Success", "Password must have at least one upper case, lower case, number and symbol")
          MainApp.showHome()
        case Failure(error) => Alert.displayAlert("Unsuccessful", "Email is in use", error.getMessage)
      }
    end if
  }
  end handleAddDonor

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

