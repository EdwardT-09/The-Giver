package donatesystem.view

import donatesystem.RunTheGiver
import javafx.event.ActionEvent
import javafx.fxml.FXML

//controls provided to the navigation bar
@FXML
class NavigationController:
  
  //direct users to home page
  def directToHome(action :ActionEvent):Unit =
    RunTheGiver.showHome()
  end directToHome

  //direct users to donor management page
  def directToDonors(action :ActionEvent):Unit =
    RunTheGiver.showDonors()
  end directToDonors
  
  def directToDonations(action:ActionEvent):Unit = 
    RunTheGiver.showDonations()
  end directToDonations

  //direct users to foods management page
  def directToFoods(action :ActionEvent):Unit =
    RunTheGiver.showFoods()
  end directToFoods

  //direct users to beverages management page page
  def directToBeverages(action :ActionEvent):Unit =
    RunTheGiver.showBeverages()
  end directToBeverages

  //direct users to about page
  def directToAbout(action :ActionEvent):Unit =
    RunTheGiver.showAbout()
  end directToAbout

end NavigationController



