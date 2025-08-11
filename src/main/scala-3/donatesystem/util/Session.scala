package donatesystem.util

import donatesystem.model.Administrator

object Session:
  private var currentAdmin: Option[Administrator] = None
  
  def logIn(admin: Administrator): Unit =
    currentAdmin = Some(admin)
  end logIn
  
  def getAdmin: Option[Administrator] = currentAdmin
  
  def logOut():Unit =
    currentAdmin = None
  end logOut
  
  def isLoggedIn:Boolean = currentAdmin.isDefined
  
  
