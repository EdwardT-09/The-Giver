package donatesystem.view

import javafx.fxml.FXML
import donatesystem.MainApp

@FXML
class HomeController :

  def directToAbout():Unit=
    MainApp.showAbout()
  end directToAbout
  
end HomeController

