import mill._
import mill.scalalib._

trait CommonModule extends SbtModule {
  def scalaVersion = "2.12.3"
  override def scalacOptions = Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-target:jvm-1.8",
    "-Ywarn-dead-code",
    "-feature"
  )

  object test extends Tests {
    override def ivyDeps = Agg(
      ivy"org.scalactic::scalactic:3.0.1",
      ivy"org.scalatest::scalatest:3.0.1"
    )
    def testFrameworks = Seq(
      "org.scalatest.tools.Framework"
    )
  }
}

trait CoreModule extends CommonModule {
  override def ivyDeps = super.ivyDeps() ++ Agg(
    ivy"org.scalaj::scalaj-http:2.3.0",
    ivy"net.ruippeixotog::scala-scraper:2.0.0",
    ivy"com.danielasfregola::twitter4s:5.1",
  )
}
object core extends CoreModule

trait AWSModule extends CommonModule {
  override def moduleDeps = Seq(
    core
  )
  override def ivyDeps = super.ivyDeps() ++ Agg(
    ivy"com.amazonaws:aws-java-sdk-lambda:1.11.175",
    ivy"com.amazonaws:aws-lambda-java-events:1.3.0",
    ivy"com.amazonaws:aws-lambda-java-core:1.1.0",
    ivy"com.amazonaws:aws-java-sdk-dynamodb:1.11.172",
    ivy"com.github.seratch::awscala:0.6.+",
  )
}
object aws extends AWSModule
