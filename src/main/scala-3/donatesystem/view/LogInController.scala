package donatesystem.view

import donatesystem.RunTheGiver
import donatesystem.model.Administrator
import javafx.event.ActionEvent
import scalafx.Includes.*
import javafx.fxml.FXML
import javafx.scene.control.{PasswordField, TextField}
import donatesystem.util.{Alert, PatternMatch, Session}

//provide controls to the login page
@FXML
class LogInController:
  //text field for administrator's email  
  @FXML private var emailField:TextField = _
  //text field for administrator's password
  @FXML private var passwordField:PasswordField = _


  //direct users to register page
  def directToRegister():Unit =
    RunTheGiver.showRegister()
  end directToRegister

  //main function to handle login
  def handleLogIn(action: ActionEvent):Unit = 
    if(!isNull) then 
      if(validInput) then 
        if(validateCredentials()) then
          //if not null, inputs are valid and credentials provided are valid, then get current admin record for session and redirect user to home page
          getAdminRecord()
          RunTheGiver.showHome()
        else
          //if credentials provided are not valid, then display error alert.
          Alert.displayError("Invalid Credentials", "Email or password provided is invalid" , "Please enter valid credentials")
  end handleLogIn

  //check if any fields are null
  def isNull:Boolean =
    //create a variable to store error message(s)
    var errorMessage:String = ""
    //if email field is left empty, then add email empty field error to errorMessage
    if (emailField.text.value.isEmpty) then
      errorMessage += "Email field is empty\n"
    end if
    //if password field is left empty, then add password empty field error to errorMessage
    if(passwordField.text.value.isEmpty) then
      errorMessage += "Password field is empty\n"
    end if
    
    
    if(errorMessage.isEmpty) then
      //if errorMessage does not have any error message then return false
      false
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return true
      Alert.displayError("Empty Field", errorMessage, "Please enter the following fields.")
      true
  end isNull


  //check if inputs are valid
  //check if fields are valid by checking the pattern from Pattern Match in util
  def validInput: Boolean =
    var errorMessage: String = ""
    //if email provided does not match the email pattern set in Pattern Match then add the message to errorMessage
    if (!PatternMatch.validEmail(emailField.text.value)) then
      errorMessage += "Email provided is invalid\n"
    if (errorMessage.isEmpty) then
      //if errorMessage does not have any error message then return true
      true
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return false
      Alert.displayError("Invalid Inputs", errorMessage, "Please reenter the fields.")
      false
    end if
  end validInput

  def validateCredentials(): Boolean =
    //compare the login credentials with the actual admin record to see if it matches
      Administrator.getRecordByKey(emailField.text.value).exists(admin => admin.passwordProperty.value == passwordField.text.value)
  end validateCredentials

  //get currently logged in admin record
  def getAdminRecord(): Unit =
    //get the record by passing email into getRecordByKey
    Administrator.getRecordByKey(emailField.text.value) match
      //if records found, call session log in to start session
      case Some(x) => Session.logIn(x)
      //if none, return nothing
      case None =>
  end getAdminRecord
  