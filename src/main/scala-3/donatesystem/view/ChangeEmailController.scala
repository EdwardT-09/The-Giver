package donatesystem.view

import donatesystem.model.Administrator
import donatesystem.util.{Alert, PatternMatch, Session}
import javafx.fxml.FXML
import javafx.scene.control.TextField
import scalafx.Includes.*
import scala.util.{Failure, Success}


@FXML
class ChangeEmailController:
  @FXML private var currentEmailField: TextField = _
  @FXML private var newEmailField: TextField = _


  def changeEmail: Unit =
    if (validEmail()) then
      if (compareEmail) then
        val admin = new Administrator(0, Session.getAdmin.get.fNameProperty.value, newEmailField.text.value, Session.getAdmin.get.passwordProperty.value)

        admin.saveAsRecord match
          case Success(x) => Alert.displayAlert("Success", "Success", "The email has been updated")
          case Failure(error) => Alert.displayAlert("Unsuccessful", "Email is in use", error.getMessage)
  end changeEmail


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