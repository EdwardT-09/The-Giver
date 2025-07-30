package donatesystem.view

import donatesystem.MainApp
import donatesystem.model.Administrator
import javafx.event.ActionEvent
import javafx.fxml.FXML
import scalafx.scene.control.TextField

@FXML
class LogInController():
  @FXML  val emailField:TextField = null
  @FXML  val passwordField:TextField = null

  def directToRegister():Unit =
    MainApp.showRegister()
  end directToRegister

//  def handleLogIn(action: ActionEvent):Unit =


  def validateCredentials(emailS:String, passwordS:String): Boolean =
    Administrator.getRecordByEmail(emailS).exists(admin => admin.passwordProperty.value == passwordS)
  end validateCredentials




  