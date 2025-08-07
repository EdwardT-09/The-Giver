package donatesystem.view

import donatesystem.MainApp

class NavigationController:

  def directToHome():Unit =
    MainApp.showHome()
  end directToHome

  def directToDonor():Unit =
    MainApp.showDonor()
  end directToDonor

  def directToAbout():Unit =
    MainApp.showAbout()
  end directToAbout

end NavigationController



