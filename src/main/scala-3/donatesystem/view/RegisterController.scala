package donatesystem.view

import donatesystem.RunTheGiver
import scalafx.Includes.*
import javafx.scene.control.{TextField,PasswordField}
import donatesystem.model.Administrator
import javafx.event.ActionEvent
import javafx.fxml.FXML
import donatesystem.util.Alert
import scala.util.{Failure, Success}
import donatesystem.util.PatternMatch

@FXML
class RegisterController():
  @FXML private var fNameField: TextField  = _
  @FXML private var emailField:TextField = _
  @FXML private var passwordField:PasswordField = _
  @FXML private var passwordConfirmField:PasswordField = _


  def directToLogIn(): Unit =
    RunTheGiver.showLogIn()
  end directToLogIn


  def handleRegister(action:ActionEvent):Unit =
    if(fNameField.text.value.isEmpty|| emailField.text.value.isEmpty || passwordField.text.value.isEmpty) then
      Alert.displayAlert("Empty Field", "FirstName, Email or password is empty", "Please enter the first name, email and password.")
    else if (!PatternMatch.validEmail(emailField.text.value)) then
      Alert.displayAlert("Invalid Email", "Email provided is invalid", "Enter using the following format 'name@example.com")
    else if(!PatternMatch.validPassword(passwordField.text.value)) then
      Alert.displayAlert("Invalid Password", "Password provided is invalid.", "Password must have at least one upper case, lower case, number and symbol")
    else if(!passwordConfirmation()) then
      Alert.displayAlert("Password do not match", "Passwords provided is invalid.", "Password must be the same.")
    else if(PatternMatch.validEmail(emailField.text.value) && PatternMatch.validPassword(passwordField.text.value) && passwordConfirmation()) then
      val admin = new Administrator(1, fNameField.text.value, emailField.text.value, passwordField.text.value)
      admin.saveAsRecord match {
        case Success(result) => Alert.displayAlert("Success", "Success", "Password must have at least one upper case, lower case, number and symbol")
                                RunTheGiver.showHome()
        case Failure(error) => Alert.displayAlert("Unsuccessful", "Email is in use", error.getMessage)
      }
    end if
  end handleRegister



  

  def passwordConfirmation(): Boolean =
    if(!passwordField.text.value.equals(passwordConfirmField.text.value)){
      false
    }else{
      true
    }


