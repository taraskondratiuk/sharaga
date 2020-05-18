package controllers

import acotrs.BookingActor
import akka.actor.{ActorRef, ActorSystem}
import akka.stream.Materializer
import javax.inject._
import play.api.libs.streams.ActorFlow
import play.api.mvc._

import scala.collection.mutable

@Singleton
class BookingController @Inject()(val controllerComponents: ControllerComponents)
                                 (implicit system: ActorSystem, mat: Materializer) extends BaseController {
  
  private val actors: mutable.Set[ActorRef] = mutable.Set()
  
  def socket: WebSocket = WebSocket.accept[String, String] { request =>
    ActorFlow.actorRef { out =>
      actors+=out
      BookingActor.props(actors, out)
    }
  }
}
