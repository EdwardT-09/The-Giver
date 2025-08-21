package donatesystem.view

import donatesystem.RunTheGiver
import javafx.fxml.FXML
import javafx.event.ActionEvent

//provide controls to authentication landing page
@FXML
class AuthLandingController:
  
//direct users to register page
  @FXML
  def startRegister(action: ActionEvent): Unit =
    RunTheGiver.showRegister()
  end startRegister

//direct users to login page
  @FXML
  def startLogIn(action:ActionEvent): Unit=
    RunTheGiver.showLogIn()
  end startLogIn


