package repositories

import models.Order

import scala.collection.mutable

object OrderRepository {
  private val bookedOrders: mutable.Set[Order] = mutable.Set(Order("D", 5, "booked"), Order("C", 4, "booked"))
  private val paidOrders: mutable.Set[Order] = mutable.Set(Order("D", 5, "paid"), Order("C", 4, "paid"))
  
  def +=(order: Order): Unit = bookedOrders += order
  
  def -=(order: Order): Unit = bookedOrders -= order
  
  def +=!(order: Order): Unit = paidOrders += order
  
  def getAllBooked: List[Order] = bookedOrders.toList
  def getAllBought: List[Order] = paidOrders.toList
}
