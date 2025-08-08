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
  // initialise the database to create table if it does not exists
  Database.dbSetUp()

  println(Administrator.getAllAdminRecord)
  
  
  var roots: Option[jfxs.layout.BorderPane] = None

  override def start():Unit =
    //get the RootResource.fxml to be displayed
    val navigationResource = getClass.getResource("view/RootResource.fxml")

    val loader = new FXMLLoader(navigationResource)
    
    loader.load()

    roots = Option(loader.getRoot[jfxs.layout.BorderPane])
  
    //set the stage to display the pages
    stage = new PrimaryStage():
      title = "Donate System"
      scene = new Scene():
        root = roots.get
      //the first page is the authentication landing page 
      showAuthLanding();
  end start

  // authentication landing page for users to choose between registration or log in
  def showAuthLanding():Unit =
    val resource = getClass.getResource("view/AuthLanding.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.get.center = roots
  end showAuthLanding
  
  // display the registration page 
  def showRegister():Unit =
    val resource = getClass.getResource("view/Register.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.get.center = roots
  end showRegister

  // display the log in page 
  def showLogIn():Unit =
    val resource = getClass.getResource("view/LogIn.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.get.center = roots
  end showLogIn

  // display the log in page 
  def showHome(): Unit =
    val resource = getClass.getResource("view/Home.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.get.center = roots
  end showHome
  
  def showDonor():Unit =
    val resource = getClass.getResource("view/Donors.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.get.center = roots
  end showDonor
  
  def showAddDonor():Unit =
    val resource = getClass.getResource("view/AddDonors.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.get.center = roots
  end showAddDonor
  
  def showAbout(): Unit =
    val resource = getClass.getResource("view/About.fxml")
    val loader = new FXMLLoader(resource)
    loader.load()
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.get.center = roots
  end showAbout



