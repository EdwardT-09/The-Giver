package donatesystem.view

import javafx.fxml.FXML
import donatesystem.util.Database
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.chart.PieChart
import scalikejdbc.*
import java.time.LocalDate

//controls to for home page to display charts
@FXML
class HomeController extends Database:
  @FXML private var itemChart: PieChart = _
  @FXML private var categoryChart: PieChart = _
  @FXML private var donorChart:PieChart = _

  //initialize when entering the page
  def initialize():Unit =
  //retrieve th item, category and donor data
    val itemData = getItemThisMonth()
    val categoryData = getCategoryThisMonth()
    val donorData = getDonorThisMonth()

    //populate the item, category and donor charts with the data retrieved
    populateItemChart(itemData)
    populateCategoryChart(categoryData)
    populateDonorChart(donorData)
  end initialize

  //retrieve the item data for the month
  private def getItemThisMonth():List[(String,Int)]  =
    //retrieve the date today
    val dateNow = LocalDate.now()
    //retrieve the year based on dateNow
    val currentYear = dateNow.getYear
    //retrieve the month based on dateNow
    val currentMonth = dateNow.getMonthValue

    val rawData = DB readOnly { implicit session =>
      sql"""SELECT ci.name, SUM(di.quantity) as total_quantity
               FROM donated_items di
               JOIN donations d ON di.donation_id = d.donation_id
               JOIN catalog_items ci ON di.item_id = ci.item_id
               WHERE YEAR(d.date) = $currentYear AND MONTH(d.date) = $currentMonth
               GROUP BY ci.name""".map(rs =>
          (rs.string("name"), rs.int("total_quantity")))
        .list()
    }
    //return the raw data
    rawData
  end getItemThisMonth

  //retrieve the category data for the month
  private def getCategoryThisMonth():List[(String,Int)] =
    //retrieve the date today
    val dateNow = LocalDate.now()
    //retrieve the year based on dateNow
    val currentYear = dateNow.getYear
    //retrieve the month based on dateNow
    val currentMonth = dateNow.getMonthValue

    val rawData = DB readOnly { implicit session =>
      sql"""SELECT ci.category, SUM(di.quantity) as total_quantity
               FROM donated_items di
               JOIN donations d ON di.donation_id = d.donation_id
               JOIN catalog_items ci ON di.item_id = ci.item_id
               WHERE YEAR(d.date) = $currentYear AND MONTH(d.date) = $currentMonth
               GROUP BY ci.category""".map(rs =>
          (rs.string("category"), rs.int("total_quantity")))
        .list()

    }
    //return the raw data
    rawData
  end getCategoryThisMonth

  //retrieve the donor data for the month
  private def getDonorThisMonth():List[(String,Int)] =
    //retrieve the date today
    val dateNow = LocalDate.now()
    //retrieve the year based on dateNow
    val currentYear = dateNow.getYear
    //retrieve the month based on dateNow
    val currentMonth = dateNow.getMonthValue

    val rawData = DB readOnly { implicit session =>
      sql"""SELECT do.name, SUM(di.quantity) as total_quantity
               FROM donated_items di
               JOIN donations d ON di.donation_id = d.donation_id
               JOIN donors do ON d.donor_id = do.donor_id
               WHERE YEAR(d.date) = $currentYear AND MONTH(d.date) = $currentMonth
               GROUP BY do.name""".map(rs =>
          (rs.string("name"), rs.int("total_quantity")))
        .list()
    }
    //return the raw data
    rawData
  end getDonorThisMonth

  //populate the itemChart with the item data
  private def populateItemChart(rawData:List[(String,Int)]):Unit =

    //a list to hold pie chart data
    val data: ObservableList[PieChart.Data] = FXCollections.observableArrayList()

    //populate the itemChart with each item and its quantity
    rawData.foreach { case (item_name, quantity) =>
      data.add(new PieChart.Data(s"$item_name ($quantity)", quantity))
    }
    
    //set the prepared data to the itemChart to be rendered
    itemChart.setData(data)
  end populateItemChart

  //populate the categoryChart with the category data
  private def populateCategoryChart(rawData: List[(String, Int)]): Unit =
  
    //a list to hold pie chart data
    val data: ObservableList[PieChart.Data] = FXCollections.observableArrayList()

    //populate the categoryChart with each category and its quantity
    rawData.foreach { case (category, quantity) =>
      data.add(new PieChart.Data(s"$category ($quantity)", quantity))
    }

    //set the prepared data to the categoryChart to be rendered
    categoryChart.setData(data)
  end populateCategoryChart

  //populate the donorChart with the donor data
  private def populateDonorChart(rawData: List[(String, Int)]): Unit =

    //a list to hold pie chart data
    val data: ObservableList[PieChart.Data] = FXCollections.observableArrayList()

    //populate the donorChart with each donor and its quantity
    rawData.foreach { case (donor_name, quantity) =>
      data.add(new PieChart.Data(s"$donor_name ($quantity)", quantity))
    }

    //set the prepared data to the donorChart to be rendered
    donorChart.setData(data)
  end populateDonorChart
end HomeController

