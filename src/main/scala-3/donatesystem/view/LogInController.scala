package donatesystem.view

import donatesystem.MainApp
import donatesystem.model.Administrator
import javafx.event.ActionEvent
import scalafx.Includes.*
import javafx.fxml.FXML
import javafx.scene.control.{TextField, PasswordField}
import donatesystem.util.Alert

@FXML
class LogInController:
  @FXML private var emailField:TextField = _
  @FXML private var passwordField:PasswordField = _



  def directToRegister():Unit =
    MainApp.showRegister()
  end directToRegister

//  def handleLogIn(action: ActionEvent):Unit =

  def handleLogIn(action: ActionEvent):Unit =
    if(validateCredentials()) then
      MainApp.showHome()
    else
      Alert.displayAlert("Invalid Credentials", "Email or password provided is invalid" , "Please enter valid credentials")
  end handleLogIn
  

  def validateCredentials(): Boolean =
    Administrator.getRecordByEmail(emailField.text.value).exists(admin => admin.passwordProperty.value == passwordField.text.value)
  end validateCredentials



  