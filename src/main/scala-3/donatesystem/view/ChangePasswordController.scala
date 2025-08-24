package donatesystem.view

import donatesystem.RunTheGiver
import donatesystem.util.{Alert, PatternMatch, Session}
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.PasswordField
import scalafx.Includes.*
import donatesystem.model.Administrator
import scala.util.{Failure, Success}


@FXML
class ChangePasswordController:
  //text field for administrator's current password
  @FXML private var currentPasswordField: PasswordField = _
  //text field for administrator's new password
  @FXML private var newPasswordField: PasswordField = _
  //text field for administrator's new password confirmation
  @FXML private var newPasswordConfirmField: PasswordField = _

  //main function to handle change password
  def handleChangePassword(action: ActionEvent): Unit =
    if (!isNull) then
      if (validPassword && comparePassword) then
        // get admin information using original email if fields are not null, passwords are valid and current password provided matches the original password in the database
        val admin = Administrator.getRecordByKey(Session.getAdmin().get.emailProperty.value)
        admin match
          //if admin found
          case Some(admin) =>
            //assign the passwordProperty with the new password field
            admin.passwordProperty.value = newPasswordField.text.value
            //attempt to save the new password to admin
            admin.saveAsRecord() match
              //if successful, display success message, replace session with the new password and redirect back to home page
              case Success(x) => Alert.displayInformation("Success", "Success", "The password has been updated")
                RunTheGiver.showHome()
              //if unsuccessful, display error message
              case Failure(error) => Alert.displayError("Unsuccessful", "Password is not updated", "Please try again")
          case None =>
            //if admin not found, then display error message
            Alert.displayError("Error", "Not found", "Admin record is not found")
  end handleChangePassword

  private def isNull: Boolean =
    //create a variable to store error message(s)
    var errorMessage = ""
    
    //if current password field is left empty, then add current password empty field error to errorMessage
    if (currentPasswordField.text.value.isEmpty) then
      errorMessage += "*Current password field is empty\n"
    end if

    //if new password field is left empty, then add new password empty field error to errorMessage
    if (newPasswordField.text.value.isEmpty) then
      errorMessage += "*New password field is empty\n"
    end if

    //if new password confirmation field is left empty, then add new password confirmation empty field error to errorMessage
    if (newPasswordConfirmField.text.value.isEmpty) then
      errorMessage += "*New password confirmation field is empty\n"
    end if

    //if errorMessage does not have any error message then return false
    if (errorMessage.isEmpty) then
      false
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return true
      Alert.displayError("Field Empty", "Please try again", errorMessage)
      true
  end isNull

  private def comparePassword: Boolean =
    //create a variable to store error message(s)
    var errorMessage = ""

    //get the original password
    val currentPassword = Session.getAdmin().get.passwordProperty

    //compare the original password with the one provided in the currentPasswordField
    if (currentPassword.value != currentPasswordField.text.value) then
      //if does not match, then add error to errorMessage
      errorMessage += "*Current password provided do not match your current password associated with the Giver.\n"
    end if
    
    if (errorMessage.isEmpty) then
      //if errorMessage does not have any error message then return true
      true
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return false
      Alert.displayError("Invalid password", "Please try again", errorMessage)
      false
  end comparePassword

  def validPassword: Boolean =
    //create a variable to store error message(s)
    var errorMessage: String = ""
    
    //if current password provided does not match the password pattern set in Pattern Match then add the message to errorMessage
    if (!PatternMatch.validPassword(currentPasswordField.text.value)) then
      errorMessage += "*The current password field must contain at least one upper case, lower case, number and symbol\n"
    end if
    
    //if new password provided does not match the password pattern set in Pattern Match then add the message to errorMessage
    if (!PatternMatch.validPassword(newPasswordField.text.value)) then
      errorMessage += "*The new password field must contain at least one upper case, lower case, number and symbol\n"
    end if
    
    //if new password confirmation provided does not match the password pattern set in Pattern Match then add the message to errorMessage
    if(newPasswordField.text.value != newPasswordConfirmField.text.value) then
      errorMessage += "*The password confirmation does not match the new password\n"
    end if
    
    if (errorMessage.isEmpty) then
      //if errorMessage does not have any error message then return true
      true
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return false
      Alert.displayError("Invalid Password", "Please try again", errorMessage)
      false
  end validPassword

end ChangePasswordController


