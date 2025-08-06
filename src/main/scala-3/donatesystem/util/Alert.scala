package donatesystem.util

import donatesystem.MainApp
import scalafx.scene.control.Alert
import scalafx.scene.control.Alert.AlertType

object Alert :
  def displayAlert(titleS: String, headerTextS: String, contentTextS: String): Unit =
    val alert = new Alert(AlertType.Error):
      initOwner(MainApp.stage)
      title = titleS
      headerText = headerTextS
      contentText = contentTextS
    alert.showAndWait()
  end displayAlert
end Alert