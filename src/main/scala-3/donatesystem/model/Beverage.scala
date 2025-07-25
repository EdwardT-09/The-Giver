package donatesystem.model

class Beverage(_itemID :String, _name:String, _category:String, _perishable:Char, _estimatedExpiry: String, _quantity:Int)
extends DonationItem(_itemID, _name, _category, _perishable, _estimatedExpiry, _quantity){

}
