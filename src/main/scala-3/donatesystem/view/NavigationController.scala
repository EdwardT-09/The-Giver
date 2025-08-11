package donatesystem.view

import donatesystem.RunTheGiver

class NavigationController:

  def directToHome():Unit =
    RunTheGiver.showHome()
  end directToHome

  def directToDonor():Unit =
    RunTheGiver.showDonor()
  end directToDonor

  def directToAbout():Unit =
    RunTheGiver.showAbout()
  end directToAbout

end NavigationController



