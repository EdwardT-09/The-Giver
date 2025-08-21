package donatesystem.view

import donatesystem.RunTheGiver
import javafx.fxml.FXML

//controls provided to the navigation bar
@FXML
class NavigationController:
  
  //direct users to home page
  def directToHome():Unit =
    RunTheGiver.showHome()
  end directToHome

  //direct users to donor management page
  def directToDonors():Unit =
    RunTheGiver.showDonors()
  end directToDonors

  //direct users to foods management page
  def directToFoods():Unit =
    RunTheGiver.showFoods()
  end directToFoods

  //direct users to beverages management page page
  def directToBeverages():Unit =
    RunTheGiver.showBeverages()
  end directToBeverages

  //direct users to about page
  def directToAbout():Unit =
    RunTheGiver.showAbout()
  end directToAbout

end NavigationController



