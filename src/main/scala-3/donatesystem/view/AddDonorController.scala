package donatesystem.view

import javafx.fxml.FXML
import javafx.scene.control.{DatePicker, TextField}


@FXML
class AddDonorController:
  @FXML private var nameField: TextField = _
  @FXML private var emailField: TextField = _
  @FXML private var birthdayField: DatePicker = _
  @FXML private var contactNoField: TextField = _
  @FXML private var occupationField: TextField = _
  
  
end AddDonorController

