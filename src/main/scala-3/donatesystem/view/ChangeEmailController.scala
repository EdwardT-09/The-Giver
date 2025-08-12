package donatesystem.view

import donatesystem.RunTheGiver
import donatesystem.model.Administrator
import donatesystem.util.{Alert, PatternMatch, Session}
import javafx.fxml.FXML
import javafx.scene.control.TextField
import scalafx.Includes.*

import scala.util.{Failure, Success}
import javafx.event.ActionEvent

@FXML
class ChangeEmailController:
  @FXML private var currentEmailField: TextField = _
  @FXML private var newEmailField: TextField = _

  def handleChangeEmail(action:ActionEvent): Unit =
    if (!isNull) then
      if (validEmail && compareEmail) then
        val admin = Administrator.getRecordByEmail(Session.getAdmin.get.emailProperty.value)
        admin match
          case Some(admin) =>
            admin.emailProperty.value = newEmailField.text.value
            admin.saveAsRecord match
              case Success(x) => Alert.displayError("Success", "Success", "The email has been updated")
                RunTheGiver.showHome()
              case Failure(error) => Alert.displayError("Unsuccessful", "Email is in use", "Please try again")
          case None =>
            Alert.displayError("Error", "Not found", "Admin record is not found")
  end handleChangeEmail

  def isNull:Boolean =
    var errorMessage = ""
    if(currentEmailField.text.value.isEmpty) then
      errorMessage += "Current email field is empty\n"
    end if
    if(newEmailField.text.value.isEmpty) then
      errorMessage += "New email field is empty\n"
    end if
    if(errorMessage.isEmpty) then
      false
    else
      Alert.displayError("Field Empty", errorMessage, "Please try again")
      true
  end isNull

  def compareEmail: Boolean =
    var errorMessage = ""
    val currentEmail = Session.getAdmin.get.emailProperty
    if (currentEmail.value != currentEmailField.text.value) then
      errorMessage += "Current email provided do not match your current email associated with the Giver.\n"
    end if
    if (errorMessage.length() == 0) then
      true
    else
      Alert.displayError("Invalid email", errorMessage, "Please try again")
      false
  end compareEmail

  def validEmail: Boolean =
    var errorMessage: String = ""
    if (!PatternMatch.validEmail(currentEmailField.text.value)) then
      errorMessage += "The current email field does not follow the following format: john@example.com\n"
    end if
    if (!PatternMatch.validEmail(newEmailField.text.value)) then
      errorMessage += "The new email field does not follow the following format: john@example.com\n"
    end if
    if (errorMessage.length() == 0) then
      true
    else
      Alert.displayError("Invalid Email", errorMessage, "Please try again")
      false
  end validEmail

end ChangeEmailController