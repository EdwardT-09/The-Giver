package donatesystem.view

import donatesystem.util.Session
import donatesystem.RunTheGiver
import javafx.fxml.FXML

//provide controls to the header
@FXML
class HeaderController:
  
  //direct users to home page
  def directToHome:Unit =
    RunTheGiver.showHome()
  end directToHome
  
//direct users to add donation page
  def directToAddDonation:Unit =
    RunTheGiver.showAddDonation()
  end directToAddDonation
  
//direct users to change email page 
  def directToChangeEmail:Unit =
    RunTheGiver.showChangeEmail
  end directToChangeEmail

//direct to change password page
  def directToChangePassword:Unit =
    RunTheGiver.showChangePassword
  end directToChangePassword

//allow users to logout of the program
  def logOut: Unit =
    Session.logOut()
    RunTheGiver.showAuthLanding()
  end logOut
end HeaderController

