package donatesystem.view

import donatesystem.RunTheGiver
import donatesystem.model.Administrator
import javafx.event.ActionEvent
import scalafx.Includes.*
import javafx.fxml.FXML
import javafx.scene.control.{TextField, PasswordField}
import donatesystem.util.Alert
import donatesystem.util.Session

@FXML
class LogInController:
  @FXML private var emailField:TextField = _
  @FXML private var passwordField:PasswordField = _


  def directToRegister():Unit =
    RunTheGiver.showRegister()
  end directToRegister


  def handleLogIn(action: ActionEvent):Unit =
    if(validateCredentials()) then 
      getAdminRecord()
      RunTheGiver.showHome()
    else
      Alert.displayAlert("Invalid Credentials", "Email or password provided is invalid" , "Please enter valid credentials")
  end handleLogIn
  

  def validateCredentials(): Boolean =
      Administrator.getRecordByEmail(emailField.text.value).exists(admin => admin.passwordProperty.value == passwordField.text.value)
  end validateCredentials

  def getAdminRecord(): Unit =
    Administrator.getRecordByEmail(emailField.text.value) match
      case Some(x) => Session.logIn(x)
      case None =>
  end getAdminRecord
  