package donatesystem.view

import donatesystem.MainApp
import javafx.fxml.FXML

@FXML
class AboutController:
  def directToHome():Unit =
    MainApp.showHome()
  end directToHome
end AboutController
