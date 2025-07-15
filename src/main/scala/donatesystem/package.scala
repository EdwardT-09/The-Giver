package donatesystem

import javafx.fxml.FXMLLoader
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage

object MainApp extends JFXApp3{
  override def start():Unit =
    val navigationResource = getClass.getResource("view/NavigationLayout.fxml")

    val loader = new FXMLLoader(navigationResource)

    loader.load()

  end start
}
