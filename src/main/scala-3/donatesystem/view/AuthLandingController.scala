package donatesystem.view

import donatesystem.MainApp
import javafx.fxml.FXML
import javafx.event.ActionEvent

@FXML
class AuthLandingController:
  @FXML
  def startRegister(action: ActionEvent): Unit =
    MainApp.showRegister()
  end startRegister

  @FXML
  def startLogIn(action:ActionEvent): Unit=
    MainApp.showLogIn()
  end startLogIn


