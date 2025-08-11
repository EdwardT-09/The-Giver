package donatesystem.util

object PatternMatch:

  def validEmail(email:String): Boolean =
    val email_pattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$".r
    if (!email_pattern.matches(email)) {
      false
    } else {
      true
    }
  end validEmail
  
  
  def validPassword(password:String): Boolean =
    val password_pattern = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()-+=]).{8,}$".r
    if (!password_pattern.matches(password)) {
      false
    } else {
      true
    }
  end validPassword

  def validContactNo(contactNo:String): Boolean =
    val contactNo_pattern = "^[0-9]{3}-[0-9]{3,4}-[0-9]{3,4}$".r
    if (!contactNo_pattern.matches(contactNo)) then
      false
    else
      true
  end validContactNo
  
end PatternMatch

