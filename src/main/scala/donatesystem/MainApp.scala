package donatesystem

import javafx.fxml.FXMLLoader
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene as sfxs
import javafx.scene as jfxs
import scalafx.scene.Scene
import scalafx.Includes.*

object MainApp extends JFXApp3:
  var roots: Option[sfxs.layout.BorderPane] = None

  override def start():Unit =
    val navigationResource = getClass.getResource("view/NavigationResource.fxml")

    val loader = new FXMLLoader(navigationResource)

    loader.load()

    roots = Option(loader.getRoot[jfxs.layout.BorderPane])

    stage = new PrimaryStage():
      title = "Donate System"
      scene = new Scene():
        root = roots.get
      showDonationItem()
  end start


  def showDonationItem():Unit =
    val resource = getClass.getResource("view/Register.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.get.center = roots
  end showDonationItem


