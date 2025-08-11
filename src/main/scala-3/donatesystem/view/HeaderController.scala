package donatesystem.view

import donatesystem.MainApp

class HeaderController:
  def directToChangeEmail:Unit =
    MainApp.showChangeEmail
  end directToChangeEmail

  def directToChangePassword:Unit =
    MainApp.showChangePassword
  end directToChangePassword
end HeaderController

