package donatesystem.view

import donatesystem.RunTheGiver
import scalafx.Includes.*
import javafx.scene.control.{PasswordField, TextField}
import donatesystem.model.Administrator
import javafx.event.ActionEvent
import javafx.fxml.FXML
import donatesystem.util.{Alert, PatternMatch, Session}

import scala.util.{Failure, Success}

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

    if(!isNull) then
      if(validInput) then 
        val admin = new Administrator(1, fNameField.text.value, emailField.text.value, passwordField.text.value)
        admin.saveAsRecord match {
          case Success(result) => Alert.displayError("Success", "Success", "Password must have at least one upper case, lower case, number and symbol")
                                  getAdminRecord()
                                  RunTheGiver.showHome()
          case Failure(error) => Alert.displayError("Unsuccessful", "Email is in use", error.getMessage)
        }
      end if
    end if
  end handleRegister

  def isNull:Boolean =
    var errorMessage:String = ""
    if (fNameField.text.value.isEmpty) then
      errorMessage += "First name field is empty\n"
    end if
    if(emailField.text.value.isEmpty) then
      errorMessage += "Email field is empty\n"
    end if
    if(passwordField.text.value.isEmpty) then
      errorMessage += "Password field is empty\n"
    end if
    if(errorMessage.isEmpty) then
      false
    else
      Alert.displayError("Empty Field", errorMessage, "Please enter the following fields.")
      true
  end isNull

  def validInput:Boolean =
    var errorMessage:String = ""
    if (!PatternMatch.validEmail(emailField.text.value)) then
      errorMessage += "Email provided is invalid\n"
    if (!PatternMatch.validPassword(passwordField.text.value)) then
      errorMessage += "Password provided is invalid.\n"
    if (!passwordConfirmation()) then
      errorMessage += "Passwords provided is invalid.\n"
    if(errorMessage.isEmpty) then 
      true
    else 
      Alert.displayError("Invalid Inputs", errorMessage, "Please reenter the fields.")
      false
    end if
  end validInput
    

  def passwordConfirmation(): Boolean =
    if(!passwordField.text.value.equals(passwordConfirmField.text.value)){
      false
    }else{
      true
    }

  def getAdminRecord(): Unit =
    Administrator.getRecordByEmail(emailField.text.value) match
      case Some(x) => Session.logIn(x)
      case None =>
  end getAdminRecord


