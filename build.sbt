import sbt.Keys.{scalacOptions, _}
import sbt._

name := "otogame-updater"

val commonSettings = Seq(
  organization := "org.nomadblacky",
  scalaVersion := "2.12.3",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-target:jvm-1.8",
    "-Ywarn-dead-code",
    "-feature"
  ),
  libraryDependencies ++= Seq(
    "org.scalactic" %% "scalactic" % "3.0.1" % "test",
    "org.scalatest" %% "scalatest" % "3.0.1" % "test"
  )
)

lazy val root = (project in file("."))
  .aggregate(core)
  .settings(
    assemblyJarName in assembly := "otogame-updater.jar",
  )

lazy val core = (project in file("core"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "org.scalaj" %% "scalaj-http" % "2.3.0",
      "net.ruippeixotog" %% "scala-scraper" % "2.0.0",
      "com.danielasfregola" %% "twitter4s" % "5.1",
    )
  )

lazy val aws = (project in file("aws"))
  .settings(
    commonSettings,
    libraryDependencies ++= Seq(
      "com.amazonaws" % "aws-java-sdk-lambda" % "1.11.175",
      "com.amazonaws" % "aws-lambda-java-events" % "1.3.0",
      "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
      "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.172",
      "com.github.seratch" %% "awscala" % "0.6.+",
    )
  )
