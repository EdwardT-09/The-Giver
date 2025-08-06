package donatesystem.view

import donatesystem.MainApp
import scalafx.Includes.*
import javafx.scene.control.{TextField,PasswordField}
import donatesystem.model.Administrator
import javafx.event.ActionEvent
import javafx.fxml.FXML
import donatesystem.util.Alert
import scala.util.{Failure, Success}


@FXML
class RegisterController():
  @FXML private var fNameField: TextField  = _
  @FXML private var emailField:TextField = _
  @FXML private var passwordField:PasswordField = _
  @FXML private var passwordConfirmField:PasswordField = _


  def directToLogIn(): Unit =
    MainApp.showLogIn()
  end directToLogIn


  def handleRegister(action:ActionEvent):Unit =
    if(fNameField.text.value.isEmpty|| emailField.text.value.isEmpty || passwordField.text.value.isEmpty) then
      Alert.displayAlert("Empty Field", "FirstName, Email or password is empty", "Please enter the first name, email and password.")
    else if (!validEmail()) then
      Alert.displayAlert("Invalid Email", "Email provided is invalid", "Enter using the following format 'name@example.com")
    else if(!validPassword()) then
      Alert.displayAlert("Invalid Password", "Password provided is invalid.", "Password must have at least one upper case, lower case, number and symbol")
    else if(!passwordConfirmation()) then
      Alert.displayAlert("Password do not match", "Passwords provided is invalid.", "Password must be the same.")
    else if(validEmail() &&validEmail() && validPassword() && passwordConfirmation()) then
      val admin = new Administrator(1, fNameField.text.value, emailField.text.value, passwordField.text.value)
      admin.saveAsRecord match {
        case Success(result) => Alert.displayAlert("Success", "Success", "Password must have at least one upper case, lower case, number and symbol")
        case Failure(error) => Alert.displayAlert("Unsuccessful", "Email is in use", error.getMessage)
      }
    end if
  end handleRegister



  def validEmail():Boolean =
    val email_pattern= "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$".r
    if (!email_pattern.matches(emailField.text.value)) {
      false
    }else{
      true
    }
  end validEmail


  def validPassword():Boolean =
    val password_pattern= "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()-+=]).{8,}$".r
    if(!password_pattern.matches(passwordField.text.value)){
      false
    }else{
      true
    }

  def passwordConfirmation(): Boolean =
    if(!passwordField.text.value.equals(passwordConfirmField.text.value)){
      false
    }else{
      true
    }


