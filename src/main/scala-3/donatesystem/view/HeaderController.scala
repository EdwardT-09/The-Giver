package donatesystem.view

import donatesystem.util.Session
import donatesystem.RunTheGiver

class HeaderController:
  def directToHome:Unit =
    RunTheGiver.showHome()
  end directToHome

  def directToAddRecordLanding: Unit =
    RunTheGiver.showAddRecordLanding()
  end directToAddRecordLanding

  def directToChangeEmail:Unit =
    RunTheGiver.showChangeEmail
  end directToChangeEmail

  def directToChangePassword:Unit =
    RunTheGiver.showChangePassword
  end directToChangePassword

  def logOut: Unit =
    Session.logOut()
    RunTheGiver.showAuthLanding()
  end logOut
end HeaderController

