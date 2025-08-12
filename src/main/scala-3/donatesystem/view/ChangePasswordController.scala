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
  @FXML private var currentPasswordField: PasswordField = _
  @FXML private var newPasswordField: PasswordField = _
  @FXML private var newPasswordConfirmField: PasswordField = _

  def handleChangePassword(action: ActionEvent): Unit =
    if (!isNull) then
      if (validPassword && comparePassword) then
        val admin = Administrator.getRecordByEmail(Session.getAdmin.get.emailProperty.value)
        admin match
          case Some(admin) =>
            admin.passwordProperty.value = newPasswordField.text.value
            admin.saveAsRecord match
              case Success(x) => Alert.displayError("Success", "Success", "The password has been updated")
                RunTheGiver.showHome()
              case Failure(error) => Alert.displayError("Unsuccessful", "Password is not updated", "Please try again")
          case None =>
            Alert.displayError("Error", "Not found", "Admin record is not found")
  end handleChangePassword

  def isNull: Boolean =
    var errorMessage = ""
    if (currentPasswordField.text.value.isEmpty) then
      errorMessage += "Current password field is empty\n"
    end if
    if (newPasswordField.text.value.isEmpty) then
      errorMessage += "New password field is empty\n"
    end if
    if (newPasswordConfirmField.text.value.isEmpty) then
      errorMessage += "New password confirmation field is empty\n"
    end if
    if (errorMessage.isEmpty) then
      false
    else
      Alert.displayError("Field Empty", errorMessage, "Please try again")
      true
  end isNull

  def comparePassword: Boolean =
    var errorMessage = ""
    val currentPassword = Session.getAdmin.get.passwordProperty
    if (currentPassword.value != currentPasswordField.text.value) then
      errorMessage += "Current password provided do not match your current password associated with the Giver.\n"
    end if
    if (errorMessage.isEmpty) then
      true
    else
      Alert.displayError("Invalid password", errorMessage, "Please try again")
      false
  end comparePassword

  def validPassword: Boolean =
    var errorMessage: String = ""
    if (!PatternMatch.validPassword(currentPasswordField.text.value)) then
      errorMessage += "The current password field must contain at least one upper case, lower case, number and symbol\n"
    end if
    if (!PatternMatch.validPassword(newPasswordField.text.value)) then
      errorMessage += "The new password field must contain at least one upper case, lower case, number and symbol\n"
    end if
    if(newPasswordField.text.value != newPasswordConfirmField.text.value) then
      errorMessage += "The password confirmation does not match the new password\n"
    end if
    if (errorMessage.isEmpty) then
      true
    else
      Alert.displayError("Invalid Password", errorMessage, "Please try again")
      false
  end validPassword

end ChangePasswordController


