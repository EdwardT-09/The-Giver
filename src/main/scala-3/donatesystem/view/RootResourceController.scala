package donatesystem.view

import donatesystem.RunTheGiver
import donatesystem.model.Administrator
import javafx.event.ActionEvent
import javafx.fxml.FXML
import donatesystem.util.{Alert, Session}
import scala.util.{Failure, Success}

//provide controls to the root/menu navigation
@FXML
class RootResourceController():
  
  //close the program
  def handleClose(action:ActionEvent): Unit =
    System.exit(0)
  end handleClose

  //direct users to register page
  @FXML
  def directToRegister(action: ActionEvent): Unit =
    //if user is not logged in then direct to login page
    if(!Session.isLoggedIn()) then
     RunTheGiver.showRegister()
    else
      //if user is logged in then display error alert
      Alert.displayError("Redirection Fail", "User is logged in", "Please log out to register")
  end directToRegister

  //direct users to login page
  @FXML
  def directToLogIn(action: ActionEvent): Unit =
    //if user is not logged in then direct to register page
    if(!Session.isLoggedIn()) then
      RunTheGiver.showLogIn()
    else
      //if user is  logged in then display error alert
      Alert.displayError("Redirection Fail", "User is logged in", "Please log out to log in")
  end directToLogIn

  //direct users to home page
  def directToHome(action: ActionEvent): Unit = 
    //if user is logged in then direct to home page
    if(Session.isLoggedIn()) then
      RunTheGiver.showHome()
    else
      //if user is not logged in then display error alert
      Alert.displayError("Redirection Fail", "User is not logged in", "Please log in")
  end directToHome

  //direct users to donor management page
  def directToDonors(action: ActionEvent): Unit =
    //if user is logged in then direct to donor management page
    if(Session.isLoggedIn()) then
      RunTheGiver.showDonors()
    else
      //if user is not logged in then display error alert
    Alert.displayError("Redirection Fail", "User is not logged in", "Please log in")
  end directToDonors

  //direct users to donation management page
  def directToDonations(action: ActionEvent): Unit =
    //if user is logged in then direct to donation management page
    if(Session.isLoggedIn()) then
      RunTheGiver.showDonations()
    else
      //if user is not logged in then display error alert
      Alert.displayError("Redirection Fail", "User is not logged in", "Please log in")
  end directToDonations

  
  //direct users to foods management page
  def directToFoods(action: ActionEvent): Unit =
    //if user is logged in then direct to food management page
    if(Session.isLoggedIn()) then
      RunTheGiver.showFoods()
    else
      //if user is not logged in then display error alert
      Alert.displayError("Redirection Fail", "User is not logged in", "Please log in")
  end directToFoods

  //direct users to beverages management page page
  def directToBeverages(action: ActionEvent): Unit =
    //if user is logged in then direct to beverages management page
    if(Session.isLoggedIn()) then
      RunTheGiver.showBeverages()
    else
      //if user is not logged in then display error alert
      Alert.displayError("Redirection Fail", "User is not logged in", "Please log in")
  end directToBeverages

  //direct users to about page
  def directToAbout(action: ActionEvent): Unit =
    //if user is logged in then direct to about page
    if(Session.isLoggedIn()) then
      RunTheGiver.showAbout()
    else
      //if user is not logged in then display error alert
      Alert.displayError("Redirection Fail", "User is not logged in", "Please log in")
  end directToAbout


  //direct users to add donation page
  def directToAddDonation(action: ActionEvent): Unit =
    //if user is logged in then direct to add donation page
    if(Session.isLoggedIn()) then
      RunTheGiver.showAddDonation()
    else
      //if user is not logged in then display error alert
      Alert.displayError("Redirection Fail", "User is not logged in", "Please log in")
  end directToAddDonation

  //direct users to change email page
  def directToChangeEmail(action: ActionEvent): Unit =
    //if user is logged in then direct to change email page
    if(Session.isLoggedIn()) then
      RunTheGiver.showChangeEmail
    else
      //if user is not logged in then display error alert
      Alert.displayError("Redirection Fail", "User is not logged in", "Please log in")
  end directToChangeEmail

  //direct to change password page
  def directToChangePassword(action: ActionEvent): Unit =
    //if user is logged in then direct to change password page
    if(Session.isLoggedIn()) then
      RunTheGiver.showChangePassword
    else
      //if user is not logged in then display error alert
      Alert.displayError("Redirection Fail", "User is not logged in", "Please log in")
  end directToChangePassword

  //delete current user's account
  def handleDeleteAccount(action: ActionEvent): Unit =
    //if user is logged in then direct to delete account
    if (Session.isLoggedIn()) then
      val confirm = Alert.displayConfirmation("Delete Account",
        "Are you sure you want this account to be deleted",
        "Deleted account cannot be recovered")

      if confirm then
        val admin = Administrator.getRecordByKey(Session.getAdmin().get.emailProperty.value) match
          case Some(admin) => admin.deleteRecord() match
            //if successful, display success message, replace session with the new email and redirect back to home page
            case Success(x) => Alert.displayInformation("Success", "Successfully delete account", "The account has been deleted")
              Session.logOut()
              RunTheGiver.showAuthLanding()
            //if unsuccessful, display error message
            case Failure(error) => Alert.displayError("Unsuccessful", "Account not deleted", "Please try again")
          case None =>
            Alert.displayError("Record not found", "Account not deleted", "Please try again")
      end if
    else
      //if user is not logged in then display error alert
      Alert.displayError("Redirection Fail", "User is not logged in", "Please log in")
  end handleDeleteAccount


  //allow users to logout of the program
  def logOut(action: ActionEvent): Unit =
    //if user is logged in then log out
    if (Session.isLoggedIn()) then
      Session.logOut()
      RunTheGiver.showAuthLanding()
    else
    //if user is not logged in then display error alert
    Alert.displayError("Redirection Fail", "User is not logged in", "Please log in")
  end logOut


  //provide information about The Giver using information alert.
  def handleAbout(action:ActionEvent):Unit = {
    val content:String =  """The Giver aims to eliminate a huge issue: starvation. In recent years, many individuals have been experiencing 
                            |layoffs, and with the increase in the cost of living, it has caused many people to struggle to afford proper meals. 
                            |As such, we aim to eliminate this issue by 2030, working towards the Sustainable Development Goal.
                            |
                            |Contact Information:
                            |Email          : care@theGiver.com
                            |Office Number  : +603-4563-1212
                            |Office Address : Jalan Sunway 11/12, Sunway 47500, Petaling Jaya, Selangor
                            |Office Hours   : 8 a.m. - 6 p.m.
                            |""".stripMargin
    
    Alert.displayInformation("About", "Welcome!", content)
  }

