name := "lab3"

version := "0.1"

scalaVersion := "2.12.12"

val circeVersion  = "0.13.0"
val akkaV         = "2.6.10"
val akkaHttpV     = "10.2.1"
val sparkV        = "3.0.1"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaV,
  "com.typesafe.akka" %% "akka-stream" % akkaV,
  "com.typesafe.akka" %% "akka-http" % akkaHttpV,
  "io.circe" %% "circe-core" % circeVersion,
  "io.circe" %% "circe-parser" % circeVersion,
  "io.circe" %% "circe-generic" % circeVersion,
  "io.circe" %% "circe-scodec" % circeVersion,
  "de.heikoseeberger" %% "akka-http-circe" % "1.28.0",
  "io.circe" %% "circe-literal" % circeVersion,
)