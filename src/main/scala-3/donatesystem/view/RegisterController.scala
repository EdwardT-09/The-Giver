package donatesystem.view

import donatesystem.MainApp
import javafx.fxml.FXML

@FXML
case class RegisterController():
  def directToLogIn(): Unit =
    MainApp.showLogIn()
  end directToLogIn
