package donatesystem.view

import donatesystem.RunTheGiver

class HeaderController:
  def directToChangeEmail:Unit =
    RunTheGiver.showChangeEmail
  end directToChangeEmail

  def directToChangePassword:Unit =
    RunTheGiver.showChangePassword
  end directToChangePassword
end HeaderController

