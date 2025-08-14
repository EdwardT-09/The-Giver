package donatesystem

import donatesystem.model.{Administrator, Beverage, DonationItem, Donor, Food}
import javafx.fxml.FXMLLoader
import scalafx.scene as sfxs
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import javafx.scene as jfxs
import scalafx.scene.Scene
import donatesystem.util.Database
import donatesystem.view.{AddBeverageController, AddDonorController, AddFoodController}
import scalafx.collections.ObservableBuffer
import scalafx.scene.image.Image
import scalafx.scene.layout.BorderPane
import scalafx.stage.{Modality, Stage}

object RunTheGiver extends JFXApp3:
  // initialise the database to create table if it does not exists

  Database.dbSetUp()
//  println(Administrator.getAllAdminRecord)

  val donorData = new ObservableBuffer[Donor]()
  val foodData = new ObservableBuffer[Food]()
  val beverageData = new ObservableBuffer[Beverage]()
  
  donorData ++= Donor.getAllRecords()
  foodData ++= Food.getAllRecords()
  beverageData ++= Beverage.getAllRecords()
  
  var roots: Option[scalafx.scene.layout.BorderPane] = None

  override def start():Unit =
    //get the RootResource.fxml to be displayed
    val navigationResource = getClass.getResource("view/RootResource.fxml")

    val loader = new FXMLLoader(navigationResource)
    
    val rootJavaFX = loader.load[javafx.scene.layout.BorderPane]()

    val rootScalaFX: BorderPane = rootJavaFX

    roots =Some(rootScalaFX)
    //set the stage to display the pages
    stage = new PrimaryStage():
      title = "Donate System"
      icons += new Image(getClass.getResource(
        "/images/Donate.png").toExternalForm)
      scene = new Scene():
        root = roots.get
      //the first page is the authentication landing page 
      showAuthLanding();
  end start

  // authentication landing page for users to choose between registration or log in
  def showAuthLanding():Unit =
    val resource = getClass.getResource("view/AuthLanding.fxml")
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showAuthLanding
  
  // display the registration page 
  def showRegister():Unit =
    val resource = getClass.getResource("view/Register.fxml")
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showRegister

  // display the log in page 
  def showLogIn():Unit =
    val resource = getClass.getResource("view/LogIn.fxml")
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showLogIn

  // display the log in page 
  def showHome(): Unit =
    val resource = getClass.getResource("view/Home.fxml")
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showHome
  
  def showDonors():Unit =
    val resource = getClass.getResource("view/Donors.fxml")
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showDonors
  
  def showAddDonor(donor:Donor):Option[Donor] =
    val resource = getClass.getResource("view/AddDonor.fxml")
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val roots2 = loader.getRoot[jfxs.Parent]
    val controller = loader.getController[AddDonorController]
    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      title= "Add/Edit Donor"
        scene = new Scene:
          root = roots2
    controller.dialogStage = dialog
    controller.donor = donor
    dialog.showAndWait()
    controller.result
  end showAddDonor

  def showFoods(): Unit =
    val resource = getClass.getResource("view/Foods.fxml")
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showFoods

  def showBeverages(): Unit =
    val resource = getClass.getResource("view/Beverages.fxml")
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showBeverages


  def showAddFood(food:Food):Option[Food] =
    val resource = getClass.getResource("view/AddFood.fxml")
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val roots2 = loader.getRoot[jfxs.Parent]
    val controller = loader.getController[AddFoodController]
    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      title = "Add/Edit Food"
      scene = new Scene:
        root = roots2
    controller.dialogStage = dialog
    controller.food = food
    dialog.showAndWait()
    controller.result
  end showAddFood

  def showAddBeverage(beverage: Beverage): Option[Beverage] =
    val resource = getClass.getResource("view/AddBeverage.fxml")
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val roots2 = loader.getRoot[jfxs.Parent]
    val controller = loader.getController[AddBeverageController]
    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      title = "Add/Edit Beverage"
      scene = new Scene:
        root = roots2
    controller.dialogStage = dialog
    controller.beverage = beverage
    dialog.showAndWait()
    controller.result
  end showAddBeverage

  def showChangeEmail:Unit =
    val resource = getClass.getResource("view/ChangeEmail.fxml")
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showChangeEmail

  def showChangePassword: Unit =
    val resource = getClass.getResource("view/ChangePassword.fxml")
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showChangePassword

  def showAbout(): Unit =
    val resource = getClass.getResource("view/About.fxml")
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showAbout



