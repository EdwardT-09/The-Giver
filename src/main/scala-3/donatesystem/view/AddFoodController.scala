package donatesystem.view

import scalafx.Includes.*
import donatesystem.model.{CatalogItem, Food}
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.control.{CheckBox, TextField}
import scalafx.stage.Stage
import scala.util.{Failure, Success}
import donatesystem.util.Alert

//provide controls to add food page
@FXML
class AddFoodController:

  //text field for food's name
  @FXML private var nameField: TextField = _
  //text field for food's category
  @FXML private var categoryField: TextField = _
  //check box for food's perishability
  @FXML private var isPerishableField: CheckBox = _
  //check box for whether the food vegetarian
  @FXML private var isVegetarianField: CheckBox = _
  //text field for food's allergens
  @FXML private var allergensField: TextField = _

  //used for Food objects that are being edited
  private var __food: Food = null
  
  //reference to the dialog window for this controller
  var dialogStage: Stage = null
  
  //used to indicate the outcome of the dialog
  var result: Option[Food] = None
  
  //stores the original name of the food item for food information update
  private var originalName: String = null
  
  //getter for the Food object
  def food = __food

  //setter for the Food object
  //allows the selected food in FoodTable to be edited and populate the add food page UI with the values from the food object
  //store the original name for comparison in the database
  def food_=(food: Food): Unit = {
    __food = food
    originalName = food.nameProperty.value
    nameField.text = __food.nameProperty.value
    categoryField.text = __food.categoryProperty.value
    isPerishableField.setSelected(__food.isPerishableProperty.get())
    isVegetarianField.setSelected(__food.isVegetarianProperty.get())
    allergensField.text = __food.containsAllergensProperty.value
  }
  
  //main function to handle add food
  def handleAddFood(action: ActionEvent): Unit =
    if !isNull() then
      //if required fields are filled, then attempt to retrieve food record using its original name
      val food = CatalogItem.getRecordByKey(originalName)
      food match
        //if retrieval is successful, then ensure that the retrieval is a Food object
        //reassign the new values into the respective properties
        case Some(food:Food) =>
          food.nameProperty.value = nameField.text.value
          food.categoryProperty.value = categoryField.text.value
          food.isPerishableProperty.value = isPerishableField.isSelected
          food.isVegetarianProperty.value = isVegetarianField.isSelected
          food.containsAllergensProperty.value = allergensField.text.value
          //attempt to save the food object
          food.saveAsRecord() match
            //if successful, display success message, assign result to Some(food) and close the dialog stage
            case Success(x) => Alert.displayInformation("Success", "Success", "The record has been updated")
              result = Some(food)
              dialogStage.close()
            //else, display the error message
            case Failure(error) => Alert.displayError("Unsuccessful", "Record is not updated", "Please try again")
        //if return result that is not Food, then display error
        case Some(_) =>
          Alert.displayInformation("Type Mismatch", "The selected record is not a food", "Please select a food item")
        //if return None, create a new Food object
        case None =>
          val food = new Food(0, nameField.text.value, categoryField.text.value, isPerishableField.isSelected,0, isVegetarianField.isSelected, allergensField.text.value)
          //attempt to save the food as record in database
          food.saveAsRecord() match
            //if successful, display success message, assign result to Some(food) and close the dialog stage
            case Success(x) => Alert.displayError("Success", "Success", "A record has been created")
              result = Some(food)
              dialogStage.close()
            //else, display error message
            case Failure(error) => Alert.displayError("Unsuccessful", "Record is not created", error.getMessage)

  end handleAddFood


  //check if any fields are left empty
  private def isNull(): Boolean =
    //create a variable to store error message(s)
    var errorMessage: String = ""
    
    //if  name field is left empty, then add  name empty field error to errorMessage
    if (nameField.text.value.isEmpty) then
      errorMessage += "*Name field is empty\n"
    end if
    
    //if category field is left empty, then add category empty field error to errorMessage
    if (categoryField.text.value.isEmpty) then
      errorMessage += "*Category field is empty\n"
    end if 
    
    
    if (errorMessage.isEmpty) then
      //if errorMessage does not have any error message then return false
      false
    else
      //if errorMessage has error message(s), then display an error alert, list the errors and return true
      Alert.displayError("Invalid Fields", "Please fill in all required fields correctly.", errorMessage)
      true

  end isNull

end AddFoodController

