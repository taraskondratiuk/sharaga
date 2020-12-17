import HopfieldNet.Picture
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.Materializer
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

import scala.concurrent.ExecutionContextExecutor

object HopfieldServer extends App {
  implicit val system: ActorSystem                = ActorSystem()
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer         = Materializer.matFromSystem
  
  def savePicture(pic: Picture): Route = {
    HopfieldNet.savePicture(pic)
    complete(StatusCodes.OK)
  }
  
  def recognizePicture(pic: Picture): Route = {
    complete {
      ToResponseMarshallable {
        HopfieldNet.recognizePicture(pic)
      }
    }
  }
  
  val routes: Route = {
    post {
        (path("add_picture") & entity(as[Picture])) (savePicture)
    } ~ post {
      (path("recognize_picture") & entity(as[Picture])) (recognizePicture)
    }
  }
  val bindingFuture = Http().bindAndHandle(routes, "0.0.0.0", 3000)
}
