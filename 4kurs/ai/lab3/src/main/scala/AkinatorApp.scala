import akka.actor.ActorSystem
import model.Movie

import java.nio.file.{Files, Paths}
import scala.annotation.tailrec
import scala.concurrent.ExecutionContextExecutor

object AkinatorApp extends App {
  implicit val system: ActorSystem                = ActorSystem()
  implicit val executor: ExecutionContextExecutor = system.dispatcher

  if (!Files.exists(Paths.get(sys.env("MOVIES_FILE_PATH")))) {
    ApiMoviesFetcher.saveMoviesFromApiToFile(sys.env("MOVIE_TITLES_FILE_PATH"), sys.env("MOVIES_FILE_PATH"))
  }
  
  val allMovies = collection.mutable.Set(FileMoviesFetcher.getMoviesFromFile(sys.env("MOVIES_FILE_PATH")) :_*)
  
  val moviesGroups = Map(
    "country" -> allMovies.groupBy(_.country),
    "title"   -> allMovies.groupBy(_.title),
    "runtime" -> allMovies
      .map(m => (m.runtime, m))
      .toSeq
      .collect { case (Some(runtime), movie) => if (runtime < 120) ("<2h", movie) else (">=2h", movie) }
      .groupBy { case (runtime, movie) => runtime }
      .map { case (runtime, runtimeMovieSeq) => (runtime, collection.mutable.Set(runtimeMovieSeq.map { case (runtime, movie) => movie } :_*)) },
    "director" -> allMovies.groupBy(_.director),
    "genre"    -> allMovies
      .flatMap(m => m.genres.map(g => (g, m)))
      .groupBy { case (genre, movie) => genre }
      .map { case (genre, genreMovieSet) => (genre, genreMovieSet.map { case (genre, movie) => movie }) },
    "actor"   -> allMovies
      .flatMap(m => m.actors.map(a => (a, m)))
      .groupBy { case (actor, movie) => actor }
      .map { case (actor, actorMovieSet) => (actor, actorMovieSet.map { case (actor, movie) => movie }) },
    "rating"  -> allMovies
      .map(m => (m.imdbRating, m))
      .toSeq
      .map { case (rating, movie) => if (rating < 7.0d) ("<7", movie) else (">=7", movie) }
      .groupBy { case (rating, movie) => rating }
      .map { case (rating, ratingMovieSeq) => (rating, collection.mutable.Set(ratingMovieSeq.map { case (rating, movie) => movie } :_*)) },
  )
  
  def removeMovieFromAllGroups(m: Movie): Unit = {
    allMovies -= m
    moviesGroups.foreach { case (property, valuesWithMovies) =>
      valuesWithMovies.foreach { case (value, movies) => movies.remove(m) }
    }
  }
  
  def getMostCommonProperty(ignoredProperties: Set[String] = Set.empty, ignoredValues: Set[String] = Set.empty): (String, (String, collection.mutable.Set[Movie])) = {
    moviesGroups.toSeq.map { case (property, valuesMap) =>
      (property, valuesMap
        .filter { case (value, moviesSet) => !ignoredValues.contains(value) }
        .maxBy { case (value, moviesSet) => moviesSet.size })
    }
      .filter { case (property, _) => !ignoredProperties.contains(property) }
      .maxBy { case (property, (value, moviesSet)) => moviesSet.size }
  }
  
  def maybeGetMoviesThatAreTwoOrLessForProperty(): List[Movie] = {
    val minMoviesForProperty =
      moviesGroups
        .toSeq
        .map { case (property, valueMoviesMap) => valueMoviesMap.values.flatten.toSet }
        .minBy(_.size)
    if (minMoviesForProperty.size < 3) minMoviesForProperty.toList
    else List.empty
  }
  
  @tailrec
  def guessMovie(ignoredProperties: Set[String] = Set("title"), ignoredValues: Set[String] = Set.empty): Option[Movie] = {
    val (property, (value, movies)) = getMostCommonProperty(ignoredProperties, ignoredValues)
    println(s"$property $value? y/n/idk")
    val answer = scala.io.StdIn.readLine()
    val (resIgnoredProps, resIgnoredValues) = answer match {
      case "y"   =>
        (allMovies diff movies).foreach(m => removeMovieFromAllGroups(m))
        (ignoredProperties + property, ignoredValues)
      case "n"   =>
        movies.foreach(m => removeMovieFromAllGroups(m))
        (ignoredProperties, ignoredValues + value)
      case  _ => (ignoredProperties, ignoredValues + value)
    }
    maybeGetMoviesThatAreTwoOrLessForProperty() match {
      case (m1 :: m2 :: Nil) =>
        println(s"is your movie ${m1.title}? y/n")
        scala.io.StdIn.readLine() match {
          case "y" => Some(m1)
          case "n" => Some(m2)
        }
      case (m1 :: Nil)       => Some(m1)
      case _                 =>
      if (resIgnoredProps.size == 7) {
        println(s"failed to guess movie, possible answers are ${allMovies.map(_.title)}")
        None
      } else guessMovie(resIgnoredProps, resIgnoredValues)
    }
  }
  
  println(guessMovie())
}
