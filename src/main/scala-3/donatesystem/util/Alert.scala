package donatesystem.util

import donatesystem.RunTheGiver
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

object Alert :
  //display alerts 
  def displayAlert(titleS: String, headerTextS: String, contentTextS: String): Unit =
    val alert = new Alert(AlertType.Error):
      initOwner(RunTheGiver.stage)
      title = titleS
      headerText = headerTextS
      contentText = contentTextS
    alert.showAndWait()
  end displayAlert
end Alert