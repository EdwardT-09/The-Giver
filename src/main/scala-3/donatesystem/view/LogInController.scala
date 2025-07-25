package donatesystem.view

import donatesystem.MainApp
import javafx.fxml.FXML

@FXML
case class LogInController():
  def directToRegister():Unit =
    MainApp.showRegister()
  end directToRegister



  