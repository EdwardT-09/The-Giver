package donatesystem.util

import donatesystem.RunTheGiver
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

//allow reusability of alerts
object Alert :
  //display error alerts 
  def displayError(titleS: String, headerTextS: String, contentTextS: String): Unit =
    val alert = new Alert(AlertType.Error):
      initOwner(RunTheGiver.stage)
      title = titleS
      headerText = headerTextS
      contentText = contentTextS
    alert.showAndWait()
  end displayError
  
  //display the information alerts
  def displayInformation(titleS: String, headerTextS: String, contentTextS: String): Unit =
    val alert = new Alert(AlertType.Information):
      initOwner(RunTheGiver.stage)
      title = titleS
      headerText = headerTextS
      contentText = contentTextS
    alert.showAndWait()
  end displayInformation
  
end Alert