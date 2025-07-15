package donatesystem.model

case class DonationItem(itemID :String, name:String, category:String, perishable:Char, estimatedExpiry: String, quantity:Int) {

}