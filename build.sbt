import sbt.Keys._
import sbt._
import sbtrelease.Version

name := "otogame-updater"

organization := "org.nomadblacky"

description:= "This program post the update of rhythm game scores to SNS."

scalaVersion := "2.12.3"

releaseNextVersion := { ver => Version(ver).map(_.bumpMinor.string).getOrElse("Error") }

assemblyJarName in assembly := "otogame.jar"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-target:jvm-1.8",
  "-Ywarn-dead-code",
  "-feature"
)

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-lambda" % "1.11.175",
  "com.amazonaws" % "aws-lambda-java-events" % "1.3.0",
  "com.amazonaws" % "aws-lambda-java-core" % "1.1.0",
  "com.amazonaws" % "aws-java-sdk-dynamodb" % "1.11.172",
  "com.github.seratch" %% "awscala" % "0.6.+",
  "org.scalaj" %% "scalaj-http" % "2.3.0",
  "net.ruippeixotog" %% "scala-scraper" % "2.0.0",
  "com.danielasfregola" %% "twitter4s" % "5.1",

  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)
