package donatesystem.view

import donatesystem.MainApp
import javafx.fxml.FXML
import scalafx.scene.control.{Alert, TextField}
import scalafx.scene.control.Alert.AlertType
import donatesystem.model.Administrator
import javafx.event.ActionEvent

import java.time.LocalDateTime


@FXML
class RegisterController():
  @FXML private val fNameField: TextField  = null
  @FXML private val emailField:TextField = null
  @FXML private val passwordField:TextField = null
  @FXML private val passwordConfirmField:TextField = null


  def directToLogIn(): Unit =
    MainApp.showLogIn()
  end directToLogIn

  def validateInputs(action:ActionEvent):Unit =
    val admin = new Administrator(1, fNameField.toString, emailField.toString, passwordField.toString, LocalDateTime.now())
    if(emailField.toString.isEmpty || passwordField.toString.isEmpty) then
      displayAlert("Empty Field", "Email or password is empty", "Please enter the email and password.")
    else if (!validEmail()) then
      displayAlert("Invalid Email", "Email provided is invalid", "Enter using the following format 'name@example.com")
    else if(!validPassword()) then
      displayAlert("Invalid Password", "Password provided is invalid.", "Password must have at least one upper case, lower case, number and symbol")
    else if(validEmail() && validPassword() && passwordConfirmation()) then
      admin.saveAsRecord
    end if

  end validateInputs


  def validEmail():Boolean =
    val email_pattern= "^[A-z0-9._%+-]+@[A-z0-9.-]+\\.com{2,}$".r
    if (!email_pattern.matches(emailField.toString)) {
      false
    }else{
      true
    }
  end validEmail


  def validPassword():Boolean =
    val password_pattern= "/^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*()-+=]).{8,}$".r
    if(!password_pattern.matches(passwordField.toString)){
      false
    }else{
      true
    }

  def passwordConfirmation(): Boolean =
    if(!passwordField.toString.equals(passwordConfirmField.toString)){
      false
    }else{
      true
    }

  def displayAlert(titleS:String, headerTextS: String, contentTextS:String): Unit =
    val alert = new Alert(AlertType.Error):
      initOwner(MainApp.stage)
      title = titleS
      headerText = headerTextS
      contentText = contentTextS
    alert.showAndWait()
  end displayAlert
  
//  def handleRegisterButton(action: ActionEvent): Unit =
//    val registerClicked = MainApp.show
