import model.Movie

import scala.util.Try

object FileMoviesFetcher {
  def getMoviesFromFile(moviesFilePath: String): Seq[Movie] = {
    val src = scala.io.Source.fromFile(moviesFilePath)
    val movieStrings = src.mkString.split("\n").tail
    src.close()
    
    movieStrings.toSeq.map { strMovie =>
      val movieFieldsArr = strMovie.replace(";", "; ").split(";")
      Movie(
        movieFieldsArr(0).trim,
        Try(movieFieldsArr(1).trim.toInt).toOption,
        Try(movieFieldsArr(2).trim.toInt).toOption,
        movieFieldsArr(3).trim.split("@").toSet,
        movieFieldsArr(4).trim,
        movieFieldsArr(5).trim.split("@").toSet,
        movieFieldsArr(6).trim,
        movieFieldsArr(7).trim,
        movieFieldsArr(8).trim.toDouble
      )
    }
  }
}
