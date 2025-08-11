package donatesystem.view

import donatesystem.util.{Alert, PatternMatch, Session}
import javafx.fxml.FXML
import javafx.scene.control.PasswordField
import scalafx.Includes.*

@FXML
class ChangePasswordController:
  @FXML private var currentPasswordField: PasswordField = _
  @FXML private var newPasswordField: PasswordField = _
  @FXML private var newPasswordConfirmField: PasswordField = _

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
      true
    else
      Alert.displayAlert("Field Empty", errorMessage, "Please try again")
      false
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
      Alert.displayAlert("Invalid password", errorMessage, "Please try again")
      false
  end comparePassword

  def validPassword(): Boolean =
    var errorMessage: String = ""
    if (!PatternMatch.validPassword(currentPasswordField.text.value)) then
      errorMessage += "The current password field must contain at least one upper case, lower case, number and symbol\n"
    end if
    if (!PatternMatch.validEmail(newPasswordField.text.value)) then
      errorMessage += "The new password field must contain at least one upper case, lower case, number and symbol\n"
    end if
    if(newPasswordField.text.value != newPasswordConfirmField.text.value) then
      errorMessage += "The password confirmation does not match the new password\n"
    end if
    if (errorMessage.length() == 0) then
      true
    else
      Alert.displayAlert("Invalid Password", errorMessage, "Please try again")
      false
  end validPassword

end ChangePasswordController


