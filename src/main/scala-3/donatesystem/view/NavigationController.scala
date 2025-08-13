package donatesystem.view

import donatesystem.RunTheGiver

class NavigationController:

  def directToHome():Unit =
    RunTheGiver.showHome()
  end directToHome

  def directToDonors():Unit =
    RunTheGiver.showDonors()
  end directToDonors

  def directToFoods():Unit =
    RunTheGiver.showFoods()
  end directToFoods

  def directToBeverages():Unit =
    RunTheGiver.showBeverages()
  end directToBeverages

  def directToAbout():Unit =
    RunTheGiver.showAbout()
  end directToAbout

end NavigationController



