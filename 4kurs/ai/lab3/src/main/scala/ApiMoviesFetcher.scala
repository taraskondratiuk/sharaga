import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.client.RequestBuilding.Get
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.unmarshalling.Unmarshal
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport._
import io.circe.generic.auto._
import model.{Movie, MovieRaw}

import java.io.{File, PrintWriter}
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.util.Try

object ApiMoviesFetcher {

  val url = s"http://www.omdbapi.com/?apikey=${sys.env("OMDB_API_KEY")}&t=" //api key can be generated at http://www.omdbapi.com/apikey.aspx
  
  def movieResp(movieName: String)(
    implicit system: ActorSystem, executor: ExecutionContextExecutor): Future[MovieRaw] = {
    val reqUrl = url + movieName.trim.replace(" ", "+")
  
    Http().singleRequest(Get(reqUrl)).flatMap { resp =>
      resp.status match {
        case StatusCodes.Success(_) => Unmarshal(resp).to[MovieRaw]
        case _                      => Future.failed(new Exception(resp.entity.toString))
      }
    }
  }
  
  def saveMoviesFromApiToFile(movieTitlesFilePath: String, outputMoviesFilePath: String)(
  implicit system: ActorSystem, executor: ExecutionContextExecutor): Unit = {
    val src = scala.io.Source.fromFile(movieTitlesFilePath)
    val movieTitles = src.mkString.split("\n")
    src.close()
    
    val movies = movieTitles
      .map(title => Try(Await.result(movieResp(title), 5.seconds)).toOption)
      .collect {
        case Some(movieRaw) => Movie(movieRaw)
      }
    
    val pw = new PrintWriter(new File(outputMoviesFilePath))
    pw.write("title;year;runtime;genres;director;actors;plot;country;imdbRating\n")
    movies.foreach(m => pw.write(s"${m.title};${m.year.getOrElse("")};${m.runtime.getOrElse("")};${m.genres.mkString("@")};${m.director};${m.actors.mkString("@")};${m.plot};${m.country};${m.imdbRating}\n"))
    pw.close()
  }
  
}
