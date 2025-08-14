package donatesystem.view

import scalafx.Includes.*
import donatesystem.model.Food
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{CheckBox, TextField}
import scalafx.stage.Stage
import scala.util.{Failure, Success}
import donatesystem.util.Alert


@FXML
class AddFoodController:
  @FXML private var nameField: TextField = _
  @FXML private var categoryField: TextField = _
  @FXML private var isPerishableField: CheckBox = _
  @FXML private var isVegetarianField: CheckBox = _
  @FXML private var allergensField: TextField = _

  private var __food: Food = null
  var dialogStage: Stage = null
  var result: Option[Food] = None
  private var originalName: String = null

  def food = __food

  def food_=(food: Food): Unit = {
    __food = food
    originalName = food.nameProperty.value
    nameField.text = __food.nameProperty.value
    categoryField.text = __food.categoryProperty.value
    isPerishableField.setSelected(__food.isPerishableProperty.get())
    isVegetarianField.setSelected(__food.isVegetarianProperty.get())
    allergensField.text = __food.containsAllergensProperty.value
  }

  def handleAddFood(action: ActionEvent): Unit =
    if validInput() then
      val food = Food.getRecordByKey(originalName)
      food match
        case Some(food) =>
          food.nameProperty.value = nameField.text.value
          food.categoryProperty.value = categoryField.text.value
          food.isPerishableProperty.value = isPerishableField.isSelected
          food.isVegetarianProperty.value = isVegetarianField.isSelected
          food.containsAllergensProperty.value = allergensField.text.value
          food.saveAsRecord match
            case Success(x) => Alert.displayError("Success", "Success", "The record has been updated")
              result = Some(food)
              dialogStage.close()
            case Failure(error) => Alert.displayError("Unsuccessful", "Record is not updated", "Please try again")
        case None =>
          val food = new Food(0, nameField.text.value, categoryField.text.value, isPerishableField.isSelected,0, isVegetarianField.isSelected, allergensField.text.value)
          food.saveAsRecord match
            case Success(x) => Alert.displayError("Success", "Success", "A record has been created")
              result = Some(food)
              dialogStage.close()
            case Failure(error) => Alert.displayError("Unsuccessful", "Record is not created", "Please try again")

  end handleAddFood



  def validInput(): Boolean =
    var errorMessage: String = ""
    if (nameField.text.value.isEmpty) then
      errorMessage += "Name field is empty"
    if (categoryField.text.value.isEmpty) then
      errorMessage += "Category field is empty"

    if (errorMessage.isEmpty) then
      true
    else 
      Alert.displayError("Invalid Fields", "Please fill in all required fields correctly.", errorMessage)
      false
    
  end validInput

end AddFoodController

