package donatesystem.util

object PatternMatch:

  //check if email matches the pattern 
  def validEmail(email:String): Boolean =
  //an example of an email that matches the pattern is john@example.com
    val email_pattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$".r
    if (!email_pattern.matches(email)) {
      //if pattern does not match the string, return false
      false
    } else {
      //if pattern does match the string, return true
      true
    }
  end validEmail

  //check if password matches the pattern 
  def validPassword(password:String): Boolean =
  //pattern must include at least a lower case and upper case letter, symbol, and number 
  //the password must also be at least 8 characters long
    val password_pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()-+=]).{8,}$".r
    if (!password_pattern.matches(password)) {
      //if pattern does not match the string, return false
      false
    } else {
      //if pattern does match the string, return true
      true
    }
  end validPassword

  //check if contact no matches the pattern 
  def validContactNo(contactNo:String): Boolean =
  //an example of the contact no that matches is 011-123-123
    //- is important and must be present
    val contactNo_pattern = "^[0-9]{3}-[0-9]{3,4}-[0-9]{3,4}$".r
    if (!contactNo_pattern.matches(contactNo)) then
      //if pattern does not match the string, return false
      false
    else
      //if pattern does match the string, return true
      true
  end validContactNo
  
end PatternMatch

