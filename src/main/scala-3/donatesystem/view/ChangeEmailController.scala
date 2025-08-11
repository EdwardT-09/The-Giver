package donatesystem.view


import donatesystem.util.{Alert, PatternMatch, Session}
import javafx.fxml.FXML
import javafx.scene.control.TextField
import scalafx.Includes.*

@FXML
class ChangeEmailController:
  @FXML private var currentEmailField: TextField = _
  @FXML private var newEmailField: TextField = _

  def compareEmail: Boolean =
    var errorMessage = ""
    val currentEmail = Session.getAdmin.get.emailProperty
    if (currentEmail.value != currentEmailField.text.value) then
      errorMessage += "Current email provided do not match your current email associated with the Giver."
    end if
    if (errorMessage.length() == 0) then
      true
    else
      Alert.displayAlert("Invalid email", errorMessage, "Please try again")
      false
  end compareEmail

  def validEmail(): Boolean =
    var errorMessage: String = ""
    if (!PatternMatch.validEmail(currentEmailField.text.value)) then
      errorMessage += "The current email field does not follow the following format: john@example.com"
    end if
    if (!PatternMatch.validEmail(newEmailField.text.value)) then
      errorMessage += "The new email field does not follow the following format: john@example.com"
    end if
    if (errorMessage.length() == 0) then
      true
    else
      Alert.displayAlert("Invalid Email", errorMessage, "Please try again")
      false
  end validEmail

end ChangeEmailController