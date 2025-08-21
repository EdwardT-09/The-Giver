package donatesystem.view

import donatesystem.RunTheGiver
import donatesystem.model.Administrator
import donatesystem.util.{Alert, PatternMatch, Session}
import javafx.fxml.FXML
import javafx.scene.control.TextField
import scalafx.Includes.*
import scala.util.{Failure, Success}
import javafx.event.ActionEvent

//provide controls to the change email page
@FXML
class ChangeEmailController:
  //text field for administrator's current email
  @FXML private var currentEmailField: TextField = _
  //text field for administrator's new email
  @FXML private var newEmailField: TextField = _


  //main function to handle email change
  def handleChangeEmail(action:ActionEvent): Unit =
    if (!isNull) then
      if (validEmail && compareEmail) then
        // get admin information using original email if fields are not null, emails are valid and current email provided matches the original email in the database
        val admin = Administrator.getRecordByKey(Session.getAdmin.get.emailProperty.value)
        admin match
          //if admin found
          case Some(admin) =>
            //assign the emailProperty with the new email field
            admin.emailProperty.value = newEmailField.text.value
            //attempt to save the new email to admin
            admin.saveAsRecord match
              //if successful, display success message, replace session with the new email and redirect back to home page
              case Success(x) => Alert.displayInformation("Success", "Success", "The email has been updated")
                Session.logIn(admin)
                RunTheGiver.showHome()
              //if unsuccessful, display error message
              case Failure(error) => Alert.displayError("Unsuccessful", "Email is in use", "Please try again")
          case None =>
          //if admin not found, then display error message
            Alert.displayError("Error", "Not found", "Admin record is not found")
  end handleChangeEmail

  def isNull:Boolean =
    //create a variable to store error message(s)
    var errorMessage = ""
    
    //if current email field is left empty, then add current email empty field error to errorMessage
    if(currentEmailField.text.value.isEmpty) then
      errorMessage += "Current email field is empty\n"
    end if
    
    //if new email field is left empty, then add new email empty field error to errorMessage
    if(newEmailField.text.value.isEmpty) then
      errorMessage += "New email field is empty\n"
    end if

    //if errorMessage does not have any error message then return false
    if(errorMessage.isEmpty) then
      false
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return true
      Alert.displayError("Field Empty", errorMessage, "Please try again")
      true
  end isNull

  def compareEmail: Boolean =
    //create a variable to store error message(s)
    var errorMessage = ""
    
    //get the original email
    val currentEmail = Session.getAdmin.get.emailProperty
    
    //compare the original email with the one provided in the currentEmailField
    if (currentEmail.value != currentEmailField.text.value) then
      //if does not match, then add error to errorMessage
      errorMessage += "Current email provided do not match your current email associated with the Giver.\n"
    end if
    
    //if errorMessage does not have any error message then return true
    if (errorMessage.isEmpty) then
      true
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return false
      Alert.displayError("Invalid email", errorMessage, "Please try again")
      false
  end compareEmail

  def validEmail: Boolean =
    //create a variable to store error message(s)
    var errorMessage: String = ""
    
    //if current email provided does not match the email pattern set in Pattern Match then add the message to errorMessage
    if (!PatternMatch.validEmail(currentEmailField.text.value)) then
      errorMessage += "The current email field does not follow the following format: john@example.com\n"
    end if

    //if new email provided does not match the email pattern set in Pattern Match then add the message to errorMessage
    if (!PatternMatch.validEmail(newEmailField.text.value)) then
      errorMessage += "The new email field does not follow the following format: john@example.com\n"
    end if

    //if errorMessage does not have any error message then return true
    if (errorMessage.isEmpty) then
      true
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return false
      Alert.displayError("Invalid Email", errorMessage, "Please try again")
      false
  end validEmail

end ChangeEmailController