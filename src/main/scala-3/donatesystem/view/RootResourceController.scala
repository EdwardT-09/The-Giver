package donatesystem.view

import javafx.event.ActionEvent
import javafx.fxml.FXML
import donatesystem.util.Alert

//provide controls to the root/menu navigation
@FXML
class RootResourceController():
  
  //close the program
  def handleClose(action:ActionEvent): Unit =
    System.exit(0)
  end handleClose
  
  //provide information about The Giver using information alert.
  def handleAbout(action:ActionEvent):Unit = {
    val content:String =  """The Giver aims to eliminate a huge issue: starvation. In recent years, many individuals have been experiencing 
                            |layoffs, and with the increase in the cost of living, it has caused many people to struggle to afford proper meals. 
                            |As such, we aim to eliminate this issue by 2030, working towards the Sustainable Development Goal.
                            |
                            |Contact Information:
                            |Email          : care@theGiver.com
                            |Office Number  : +603-4563-1212
                            |Office Address : Jalan Sunway 11/12, Sunway 47500, Petaling Jaya, Selangor
                            |Office Hours   : 8 a.m. - 6 p.m.
                            |""".stripMargin
    
    Alert.displayInformation("About", "Welcome!", content)
  }

