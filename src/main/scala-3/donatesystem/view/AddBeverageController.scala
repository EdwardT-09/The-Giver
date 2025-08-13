package donatesystem.view

import scalafx.Includes.*
import donatesystem.model.Beverage
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{CheckBox, TextField}
import scalafx.stage.Stage
import scala.util.{Failure, Success}
import donatesystem.util.Alert


class AddBeverageController:
  @FXML private var nameField: TextField = _
  @FXML private var categoryField: TextField = _
  @FXML private var isPerishableField: CheckBox = _
  @FXML private var isCarbonatedField: CheckBox = _
  @FXML private var volumePerUnitField: TextField = _

  private var __beverage: Beverage = null
  var dialogStage: Stage = null
  var result: Option[Beverage] = None
  private var originalName: String = null

  def beverage = __beverage

  def beverage_=(beverage: Beverage): Unit =
    __beverage = beverage
    originalName = beverage.nameProperty.value
    nameField.text = __beverage.nameProperty.value
    categoryField.text = __beverage.categoryProperty.value
    isPerishableField.setSelected(__beverage.isPerishableProperty.get())
    isCarbonatedField.setSelected(__beverage.isCarbonatedProperty.get())
    volumePerUnitField.text = __beverage.volumePerUnitProperty.value.toString


  def handleAddFood(action: ActionEvent): Unit =
    if validInput() then
      val beverage = Beverage.getRecordByName(originalName)
      beverage match
        case Some(beverage) =>
          beverage.nameProperty.value = nameField.text.value
          beverage.categoryProperty.value = categoryField.text.value
          beverage.isPerishableProperty.value = isPerishableField.isSelected
          beverage.isCarbonatedProperty.value = isCarbonatedField.isSelected
          beverage.volumePerUnitProperty.value = volumePerUnitField.text.value.toInt
          beverage.saveAsRecord match
            case Success(x) => Alert.displayError("Success", "Success", "The record has been updated")
              result = Some(beverage)
              dialogStage.close()
            case Failure(error) => Alert.displayError("Unsuccessful", "Record is not updated", "Please try again")
        case None =>
          val beverage = new Beverage(0, nameField.text.value, categoryField.text.value, isPerishableField.isSelected, 0, volumePerUnitField.text.value.toInt, isCarbonatedField.isSelected)
          beverage.saveAsRecord match
            case Success(x) => Alert.displayError("Success", "Success", "A record has been created")
              result = Some(beverage)
              dialogStage.close()
            case Failure(error) => Alert.displayError("Unsuccessful", "Record is not created", "Please try again")

  end handleAddFood


  def validInput(): Boolean =
    val volume = volumePerUnitField.text.value.trim
    var errorMessage: String = ""
    if (nameField.text.value.isEmpty) then
      errorMessage += "Name field is empty"
    if (categoryField.text.value.isEmpty) then
      errorMessage += "Category field is empty"
    if (volume.isEmpty) then
      errorMessage += "Volume Per Unit field is empty"
    if (!volume.matches("""\d+"""))
      errorMessage += "Volume per unit must be a non-negative number\n"
    if(volume.toInt < 0) then
      errorMessage += "Volume per unit must be a non-negative number\n"
    
    if (errorMessage.isEmpty) then
      true
    else
      Alert.displayError("Invalid Fields", "Please fill in all required fields correctly.", errorMessage)
      false

  end validInput

end AddBeverageController

