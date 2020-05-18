package acotrs

import akka.actor._
import models.Order
import play.api.libs.json.{Json, OFormat}
import repositories.OrderRepository

import scala.collection.mutable


class BookingActor(outAll: mutable.Set[ActorRef], outSingle: ActorRef) extends Actor {
  implicit val orderFormat: OFormat[Order] = Json.format[Order]
  
  override def receive: Receive = {
    case msg: String =>
      msg match {
        case "start" =>
          OrderRepository.getAllBooked.foreach(outSingle ! Json.toJson(_).toString())
          OrderRepository.getAllBought.foreach(outSingle ! Json.toJson(_).toString())
        case _ =>
          val order = Json.parse(msg).as[Order]
          order.status match {
            case "booked" =>
              OrderRepository += order
            case "unbooked" =>
              OrderRepository -= order
            case "paid" =>
              OrderRepository +=! order
          }
          outAll.foreach(_ ! msg)
        
      }
  }
  
  override def postStop(): Unit = {
    outAll -= outSingle
  }
}

object BookingActor {
  def props(outSet: mutable.Set[ActorRef], out: ActorRef): Props = Props(new BookingActor(outSet, out))
}
