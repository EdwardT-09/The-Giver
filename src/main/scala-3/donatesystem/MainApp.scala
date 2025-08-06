package donatesystem

import donatesystem.model.Administrator
import javafx.fxml.FXMLLoader
import javafx.scene as jfxs
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import scalafx.scene as sfxs
import scalafx.scene.Scene
import donatesystem.util.Database

object MainApp extends JFXApp3:

  Database.dbSetUp()

  println(Administrator.getAllAdminRecord)

  var roots: Option[jfxs.layout.BorderPane] = None

  override def start():Unit =
    val navigationResource = getClass.getResource("view/NavigationResource.fxml")

    val loader = new FXMLLoader(navigationResource)

    loader.load()

    roots = Option(loader.getRoot[jfxs.layout.BorderPane])

    stage = new PrimaryStage():
      title = "Donate System"
      scene = new Scene():
        root = roots.get
      showAuthLanding();
  end start

  def showAuthLanding():Unit =
    val resource = getClass.getResource("view/AuthLanding.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.get.center = roots
  end showAuthLanding

  def showRegister():Unit =
    val resource = getClass.getResource("view/Register.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.get.center = roots
  end showRegister


  def showLogIn():Unit =
    val resource = getClass.getResource("view/LogIn.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.get.center = roots
  end showLogIn



