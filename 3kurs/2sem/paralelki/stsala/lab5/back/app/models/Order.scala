package models

case class Order(row: String, place: Int, status: String) {
  override def equals(obj: Any): Boolean =
    obj match {
      case that: Order => that.canEqual(this) &&
        this.hashCode == that.hashCode
      case _ => false
    }
  
  override def hashCode(): Int = row.## * 23 + place
}
