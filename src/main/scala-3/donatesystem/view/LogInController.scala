package donatesystem.view

import donatesystem.RunTheGiver
import donatesystem.model.Administrator
import javafx.event.ActionEvent
import scalafx.Includes.*
import javafx.fxml.FXML
import javafx.scene.control.{PasswordField, TextField}
import donatesystem.util.{Alert, PatternMatch, Session}

@FXML
class LogInController:
  @FXML private var emailField:TextField = _
  @FXML private var passwordField:PasswordField = _


  def directToRegister():Unit =
    RunTheGiver.showRegister()
  end directToRegister


  def handleLogIn(action: ActionEvent):Unit = 
    if(!isNull) then 
      if(validInput) then 
        if(validateCredentials()) then 
          getAdminRecord()
          RunTheGiver.showHome()
        else
          Alert.displayError("Invalid Credentials", "Email or password provided is invalid" , "Please enter valid credentials")
  end handleLogIn

  def isNull:Boolean =
    var errorMessage:String = ""
    if (emailField.text.value.isEmpty) then
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

  def validInput: Boolean =
    var errorMessage: String = ""
    if (!PatternMatch.validEmail(emailField.text.value)) then
      errorMessage += "Email provided is invalid\n"
    if (errorMessage.isEmpty) then
      true
    else
      Alert.displayError("Invalid Inputs", errorMessage, "Please reenter the fields.")
      false
    end if
  end validInput

  def validateCredentials(): Boolean =
      Administrator.getRecordByEmail(emailField.text.value).exists(admin => admin.passwordProperty.value == passwordField.text.value)
  end validateCredentials

  def getAdminRecord(): Unit =
    Administrator.getRecordByEmail(emailField.text.value) match
      case Some(x) => Session.logIn(x)
      case None =>
  end getAdminRecord
  