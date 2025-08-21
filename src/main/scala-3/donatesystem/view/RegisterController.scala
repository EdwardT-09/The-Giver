package donatesystem.view

import donatesystem.RunTheGiver
import scalafx.Includes.*
import javafx.scene.control.{PasswordField, TextField}
import donatesystem.model.Administrator
import javafx.event.ActionEvent
import javafx.fxml.FXML
import donatesystem.util.{Alert, PatternMatch, Session}

import scala.util.{Failure, Success}


//provide controls to register page
@FXML
class RegisterController():

  //text field for administrator's first name 
  @FXML private var fNameField: TextField  = _
  //text field for administrator's email
  @FXML private var emailField:TextField = _
  //password field for administrator's password
  @FXML private var passwordField:PasswordField = _
  //password field for confirming the user's password
  @FXML private var passwordConfirmField:PasswordField = _


  //direct the user to the login page
  def directToLogIn(): Unit =
    RunTheGiver.showLogIn()
  end directToLogIn


  //main function to handle registration
  def handleRegister(action:ActionEvent):Unit =
    if(!isNull) then
      if(validInput) then
        //if not null and inputs are valid, then create new Administrator object
        val admin = new Administrator(1, fNameField.text.value, emailField.text.value, passwordField.text.value)
        // save the record to administrator database table
        admin.saveAsRecord match {
          case Success(result) => getAdminRecord()
                                  RunTheGiver.showHome()
                                  //if successful, get admin record for session use and redirect to home page
          case Failure(error) =>  Alert.displayError("Unsuccessful", "Email is in use", error.getMessage)
                                  //if saving fails, provide an error alert to indicate failure
        }
      end if
    end if
  end handleRegister


  //check if any fields are left empty
  def isNull:Boolean =
  //create a variable to store error message(s)
    var errorMessage:String = ""
  
    //if first name field is left empty, then add first name empty field error to errorMessage
    if (fNameField.text.value.isEmpty) then
      errorMessage += "First name field is empty\n"
    end if
    
    //if email field is left empty, then add email empty field error to errorMessage
    if(emailField.text.value.isEmpty) then
      errorMessage += "Email field is empty\n"
    end if
    
    //if password field is left empty, then add password empty field error to errorMessage
    if(passwordField.text.value.isEmpty) then
      errorMessage += "Password field is empty\n"
    end if
    
    //if password confirmation field is left empty, then add password confirmation empty field error to errorMessage
    if (passwordConfirmField.text.value.isEmpty) then
      errorMessage += "Password confirmation field is empty\n"
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
  def validInput:Boolean =
    //create a variable to store error message(s)
    var errorMessage:String = ""
    
    //if email provided does not match the email pattern set in Pattern Match then add the message to errorMessage
    if (!PatternMatch.validEmail(emailField.text.value)) then
      errorMessage += "Email provided is invalid\n"
    end if  
    //if password provided does not match the password pattern set in Pattern Match then add the message to errorMessage
    if (!PatternMatch.validPassword(passwordField.text.value)) then
      errorMessage += "Password provided is invalid.\n"
    end if
    
    //if password confirmation provided does not match the password provided then add the message to errorMessage
    if (!passwordConfirmation()) then
      errorMessage += "Passwords provided is invalid.\n"
    end if 
    
    if(errorMessage.isEmpty) then {
      //if errorMessage does not have any error message then return true
      true
    } else
      //if errorMessage has error message(s), then display an error alert, list the errors and return false
      Alert.displayError("Invalid Inputs", errorMessage, "Please reenter the fields.")
      false
    end if
  end validInput
    
  //check if password confirmation field matches the password provided
  def passwordConfirmation(): Boolean =
    if(!passwordField.text.value.equals(passwordConfirmField.text.value)){
      //if it does not match, return false
      false
    }else{
      //if it matches, return true
      true
    }

  //get currently logged in admin record
  def getAdminRecord(): Unit =
  //get the record by passing email into getRecordByKey
    Administrator.getRecordByKey(emailField.text.value) match
      //if records found, call session log in to start session
      case Some(x) => Session.logIn(x)
      //if none, return nothing
      case None =>
  end getAdminRecord


