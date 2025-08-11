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
  
end PatternMatch

