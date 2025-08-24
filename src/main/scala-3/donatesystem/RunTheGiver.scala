package donatesystem

import donatesystem.model.{Beverage, DonatedItems, Donor, Food}
import javafx.fxml.FXMLLoader
import scalafx.scene as sfxs
import scalafx.Includes.*
import scalafx.application.JFXApp3
import scalafx.application.JFXApp3.PrimaryStage
import javafx.scene as jfxs
import scalafx.scene.Scene
import donatesystem.util.Database
import donatesystem.view.{AddBeverageController, AddDonationController, AddDonorController, AddFoodController, BeveragesController, DonationsController, FoodsController}
import scalafx.collections.ObservableBuffer
import scalafx.scene.image.Image
import scalafx.scene.layout.BorderPane
import scalafx.stage.{Modality, Stage}

object RunTheGiver extends JFXApp3:
  // initialise the database to create table if it does not exists
  Database.dbSetUp()

  //create ObservableBuffer for each model to add all the records
  val donorData = new ObservableBuffer[Donor]()
  val foodData = new ObservableBuffer[Food]()
  val beverageData = new ObservableBuffer[Beverage]()
  val donatedItemData = new ObservableBuffer[DonatedItems]()
  
  
  //assign all data to their respective variable buffers
  donorData ++= Donor.getAllRecords()
  foodData ++= Food.getAllRecords()
  beverageData ++= Beverage.getAllRecords()
  donatedItemData ++= DonatedItems.getAllRecords()
  
  

  //window root pane
  var roots: Option[scalafx.scene.layout.BorderPane] = None
  //assign the stylesheet to a variable
  var cssResource = getClass.getResource("view/style.css")
  override def start():Unit =
    //get the RootResource.fxml to be displayed
    val navigationResource = getClass.getResource("view/RootResource.fxml")

    //initialize the loader object
    val loader = new FXMLLoader(navigationResource)

    val rootJavaFX = loader.load[javafx.scene.layout.BorderPane]()

    //transform from javafx to scalafx
    val rootScalaFX: BorderPane = rootJavaFX

    roots =Some(rootScalaFX)
    //set the stage to display the pages
    stage = new PrimaryStage():
      //the title that will be displayed on the stage
      title = "Donate System"
      //assign an icon for the stage
      icons += new Image(getClass.getResource(
        "/images/Donate.png").toExternalForm)
      scene = new Scene():
        stylesheets = Seq(cssResource.toExternalForm)
        root = roots.get
      //the first page is the authentication landing page 
      showAuthLanding();
  end start

  // authentication landing page for users to choose between registration or log in
  def showAuthLanding():Unit =
    //get the AuthLanding.fxml to be displayed
    val resource = getClass.getResource("view/AuthLanding.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showAuthLanding
  
  // display the registration page 
  def showRegister():Unit =
    //get the Register.fxml to be displayed
    val resource = getClass.getResource("view/Register.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()

    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showRegister

  // display the log in page 
  def showLogIn():Unit =
    //get the LogIn.fxml to be displayed
    val resource = getClass.getResource("view/LogIn.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()

    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showLogIn

  // display the log in page 
  def showHome(): Unit =
    //get the Home.fxml to be displayed
    val resource = getClass.getResource("view/Home.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showHome

  // display the donor page 
  def showDonors():Unit =
    //get the Donors.fxml to be displayed
    val resource = getClass.getResource("view/Donors.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showDonors

  // display the add donor page 
  def showAddDonor(donor:Donor):Option[Donor] =
    //get the AddDonor.fxml to be displayed
    val resource = getClass.getResource("view/AddDonor.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val roots2 = loader.getRoot[jfxs.Parent]
    // get AddDonorController
    val controller = loader.getController[AddDonorController]
    //set the stage to display the add donor page
    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      //the title that will be displayed on the stage is "Add/Edit Donor"
      title= "Add/Edit Donor"
        scene = new Scene:
          icons += new Image(getClass.getResource(
            "/images/Plus.png").toExternalForm)
          stylesheets = Seq(cssResource.toExternalForm)
          root = roots2
    //gain access to the dialogStage from the controller
    controller.dialogStage = dialog
    //gain access to the donor from the controller
    controller.donor = donor
    //pause until the showAddDonor page is closed
    dialog.showAndWait()
    //retrieve the result
    controller.result
  end showAddDonor

  // display the add donation page 
  def showAddDonation(): Unit =
    //get the AddDonation.fxml to be displayed
    val resource = getClass.getResource("view/AddDonation.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val roots2 = loader.getRoot[jfxs.Parent]
    // get AddDonationController
    val controller = loader.getController[AddDonationController]
    //set the stage to display the add donation page
    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      //the title that will be displayed on the stage is "Add/ Edit Donation"
      title = "Add/Edit Donation"
      scene = new Scene:
        icons += new Image(getClass.getResource(
          "/images/Plus.png").toExternalForm)
        stylesheets = Seq(cssResource.toExternalForm)
        root = roots2
    //gain access to the dialogStage from the controller
    controller.dialogStage = dialog
    //pause until the showAddDonation page is closed
    dialog.showAndWait()
  end showAddDonation

  // display the donation page 
  def showDonations(): Unit =
    //get the Donations.fxml to be displayed
    val resource = getClass.getResource("view/Donations.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
    // get donationsController
    val donationsController = loader.getController[DonationsController]()
    //refresh the beveragesTable every time the page is accessed
    donationsController.refreshTable()
  end showDonations
  
  // display the foods page 
  def showFoods(): Unit =
    //get the Foods.fxml to be displayed
    val resource = getClass.getResource("view/Foods.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
    // get FoodsController
    val foodsController = loader.getController[FoodsController]()
    //refresh the foodsTable every time the page is accessed
    foodsController.refreshTable()
  end showFoods

  // display the beverages page 
  def showBeverages(): Unit =
    //get the Beverages.fxml to be displayed
    val resource = getClass.getResource("view/Beverages.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
    // get BeveragesController
    val beveragesController = loader.getController[BeveragesController]()
    //refresh the beveragesTable every time the page is accessed
    beveragesController.refreshTable()
  end showBeverages

  // display the add food page 
  def showAddFood(food:Food):Option[Food] =
    //get the AddFood.fxml to be displayed
    val resource = getClass.getResource("view/AddFood.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val roots2 = loader.getRoot[jfxs.Parent]
    // get AddFoodController
    val controller = loader.getController[AddFoodController]
    //set the stage to display the add food page
    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      //the title that will be displayed on the stage is "Add/Edit Food"
      title = "Add/Edit Food"
      scene = new Scene:
        icons += new Image(getClass.getResource(
          "/images/Plus.png").toExternalForm)
        stylesheets = Seq(cssResource.toExternalForm)
        root = roots2
    //gain access to the dialogStage from the controller
    controller.dialogStage = dialog
    //gain access to the food from the controller
    controller.food = food
    //pause until the showAddFood page is closed
    dialog.showAndWait()
    //retrieve the result
    controller.result
  end showAddFood

  // display the add beverage page 
  def showAddBeverage(beverage: Beverage): Option[Beverage] =
    //get the Beverage.fxml to be displayed
    val resource = getClass.getResource("view/AddBeverage.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    val roots2 = loader.getRoot[jfxs.Parent]
    // get AddBeverageController
    val controller = loader.getController[AddBeverageController]
    //set the stage to display the add beverage page
    val dialog = new Stage():
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      //the title that will be displayed on the stage is "Add/Edit Beverage"
      title = "Add/Edit Beverage"
      scene = new Scene:
        icons += new Image(getClass.getResource(
          "/images/Plus.png").toExternalForm)
        stylesheets = Seq(cssResource.toExternalForm)
        root = roots2
    //gain access to the dialogStage from the controller
    controller.dialogStage = dialog
    //gain access to the beverage from the controller
    controller.beverage = beverage
    //pause until the showAddBeverage page is closed
    dialog.showAndWait()
    //retrieve the result
    controller.result
  end showAddBeverage

  // display the change email page 
  def showChangeEmail:Unit =
    //get the ChangeEmail.fxml to be displayed
    val resource = getClass.getResource("view/ChangeEmail.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showChangeEmail

  // display the change password page 
  def showChangePassword: Unit =
    //get the ChangePassword.fxml to be displayed
    val resource = getClass.getResource("view/ChangePassword.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showChangePassword

  // display the about page 
  def showAbout(): Unit =
    //get the About.fxml to be displayed
    val resource = getClass.getResource("view/About.fxml")
    //initialize the loader object
    val loader = new FXMLLoader(resource)
    val rootJavaFX = loader.load[javafx.scene.layout.AnchorPane]()
    
    val rootScalaFX: scalafx.scene.layout.AnchorPane = rootJavaFX
    var roots: Option[scalafx.scene.layout.AnchorPane] = None
    roots = Some(rootScalaFX)
    this.roots.get.center = roots.get
  end showAbout



