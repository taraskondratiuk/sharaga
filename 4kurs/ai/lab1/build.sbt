name := "ii1"

version := "0.1"

scalaVersion := "2.13.3"

val catsVersion   = "2.2.0"
val fs2Version    = "2.4.5"
val http4sVersion = "0.21.8"
val circeVersion  = "0.13.0"
val akkaV         = "2.6.10"
val akkaHttpV     = "10.2.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.typesafe.akka" %% "akka-http" % akkaHttpV,
  // Optional for auto-derivation of JSON codecs
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-scodec" % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.28.0",
  // Optional for string interpolation to JSON model
  "io.circe" %% "circe-literal" % circeVersion,
)