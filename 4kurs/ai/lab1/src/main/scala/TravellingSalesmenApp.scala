import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.Materializer
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._

import scala.concurrent.ExecutionContextExecutor
import scala.concurrent.duration.DurationInt

object TravellingSalesmenApp extends App {
  case class TravellingSalesmanRequest(inputParameters: InputParameters, cities: Seq[City])
  
  implicit val system: ActorSystem                = ActorSystem()
  implicit val executor: ExecutionContextExecutor = system.dispatcher
  implicit val materializer: Materializer         = Materializer.matFromSystem
  
  def geneticAlgorithmForTravellingSalesman(request: TravellingSalesmanRequest): Route = {
    println(request)
    complete {
      ToResponseMarshallable {
        GeneticAlgorithm.geneticAlgorithm(request.inputParameters, request.cities)
      }
    }
  }
  
  def generateCities(nCities: Int): Route = {
    complete {
      ToResponseMarshallable {
        GeneticAlgorithm.generateCities(nCities)
      }
    }
  }
  
  val routes: Route = {
    post {
      withRequestTimeout(20.minute) {
        (path("travelling_salesman") & entity(as[TravellingSalesmanRequest])) (geneticAlgorithmForTravellingSalesman)
      }
    } ~ get {
      path("generate_cities" / IntNumber ~ PathEnd)(generateCities)
    }
  }
  val bindingFuture = Http().bindAndHandle(routes, "0.0.0.0", 3000)
}