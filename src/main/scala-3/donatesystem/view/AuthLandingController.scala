package donatesystem.view

import donatesystem.RunTheGiver
import javafx.fxml.FXML
import javafx.event.ActionEvent

@FXML
class AuthLandingController:
  @FXML
  def startRegister(action: ActionEvent): Unit =
    RunTheGiver.showRegister()
  end startRegister

  @FXML
  def startLogIn(action:ActionEvent): Unit=
    RunTheGiver.showLogIn()
  end startLogIn


