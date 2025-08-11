package donatesystem.view

import donatesystem.RunTheGiver
import javafx.fxml.FXML

@FXML
class AboutController:
  def directToHome():Unit =
    RunTheGiver.showHome()
  end directToHome
end AboutController
