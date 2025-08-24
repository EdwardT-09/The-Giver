package donatesystem.util

import donatesystem.model.Administrator

object Session:
  //initialize the currentAdmin as None, this indicates that no admin is logged in
  private var currentAdmin: Option[Administrator] = None

  //assign the admin records to currentAdmin
  def logIn(admin: Administrator): Unit =
    currentAdmin = Some(admin) 
  end logIn

  //allow the current admin records to be obtained
  def getAdmin(): Option[Administrator] = currentAdmin

  //allow admins to log out and assign the current admin back to none
  def logOut():Unit =
    currentAdmin = None
  end logOut

  //checks whether the administrator is logged in
  //if currentAdmin has value, return true. If not, return false
  def isLoggedIn():Boolean = currentAdmin.isDefined
  
  
