package donatesystem.view


import donatesystem.util.PatternMatch
import javafx.fxml.FXML
import javafx.scene.control.TextField
import scalafx.Includes.*
import donatesystem.util.Alert

@FXML
class ChangeEmailController:
  @FXML private var currentEmailField: TextField = _
  @FXML private var newEmailField: TextField = _

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