package model

import scala.util.Try

case class RatingRaw(Source: Option[String], Value: Option[String])

case class MovieRaw(Title: String,
                    Year: Option[String],
                    Rated: Option[String],
                    Released: Option[String],
                    Runtime: Option[String],
                    Genre: Option[String],
                    Director: Option[String],
                    Writer: Option[String],
                    Actors: Option[String],
                    Plot: Option[String],
                    Language: Option[String],
                    Country: Option[String],
                    Awards: Option[String],
                    Poster: Option[String],
                    Ratings: Option[List[RatingRaw]],
                    Metascore: Option[String],
                    imdbRating: Option[String],
                    imdbVotes: Option[String],
                    imdbID: Option[String],
                    Type: Option[String],
                    Production: Option[String])

case class Movie(title: String,
                 year: Option[Int],
                 runtime: Option[Int],
                 genres: Set[String],
                 director: String,
                 actors: Set[String],
                 plot: String,
                 country: String,
                 imdbRating: Double)

object Movie {
  def apply(movieRaw: MovieRaw): Movie =
    new Movie(movieRaw.Title,
      movieRaw.Year.flatMap(y => Try(y.toInt).toOption),
      movieRaw.Runtime.flatMap(runtime => Try(runtime.split(" ").head.toInt).toOption),
      movieRaw.Genre.map(genres => genres.split(", ").toSet).getOrElse(Set.empty),
      movieRaw.Director.getOrElse(""),
      movieRaw.Actors.map(actors => actors.split(", ").toSet).getOrElse(Set.empty),
      movieRaw.Plot.getOrElse("").replaceAll("[@;]", ""),
      movieRaw.Country.getOrElse("").split(", ").headOption.getOrElse(""),
      movieRaw.imdbRating.flatMap(rating => Try(rating.toDouble).toOption).getOrElse(0.0f))
}
