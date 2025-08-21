package donatesystem.view

import donatesystem.util.Session
import donatesystem.RunTheGiver
import donatesystem.model.Administrator
import javafx.fxml.FXML
import donatesystem.util.Alert
import javafx.event.ActionEvent

import scala.util.{Failure, Success}

//provide controls to the header
@FXML
class HeaderController:
  
  //direct users to home page
  def directToHome(action :ActionEvent):Unit =
    RunTheGiver.showHome()
  end directToHome
  
//direct users to add donation page
  def directToAddDonation(action :ActionEvent):Unit =
    RunTheGiver.showAddDonation()
  end directToAddDonation
  
//direct users to change email page 
  def directToChangeEmail(action :ActionEvent):Unit =
    RunTheGiver.showChangeEmail
  end directToChangeEmail

//direct to change password page
  def directToChangePassword(action :ActionEvent):Unit =
    RunTheGiver.showChangePassword
  end directToChangePassword
  
//delete current user's account :Unit = 
  def handleDeleteAccount(action :ActionEvent):Unit =
    val confirm = Alert.displayConfirmation("Delete Account",
      "Are you sure you want this account to be deleted",
      "Deleted account cannot be recovered")
    
    if confirm then
      val admin = Administrator.getRecordByKey(Session.getAdmin.get.emailProperty.value) match 
        case Some(admin) =>   admin.deleteRecord match
          //if successful, display success message, replace session with the new email and redirect back to home page
          case Success(x) => Alert.displayInformation("Success", "Successfully delete account", "The account has been deleted")
            Session.logOut()
            RunTheGiver.showAuthLanding()
          //if unsuccessful, display error message
          case Failure(error) => Alert.displayError("Unsuccessful", "Account not deleted", "Please try again")
        case None =>
          Alert.displayError("Record not found", "Account not deleted", "Please try again")
    end if 
  end handleDeleteAccount


  //allow users to logout of the program
  def logOut(action :ActionEvent): Unit =
    Session.logOut()
    RunTheGiver.showAuthLanding()
  end logOut
end HeaderController

