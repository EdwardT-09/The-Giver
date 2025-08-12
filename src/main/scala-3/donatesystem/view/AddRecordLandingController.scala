package donatesystem.view

import donatesystem.RunTheGiver
class AddRecordLandingController:
  def directToAddFood():Unit =
    RunTheGiver.showAddFood()
  end directToAddFood

  def directToAddBeverage(): Unit =
    RunTheGiver.showAddBeverage()
  end directToAddBeverage


end AddRecordLandingController
