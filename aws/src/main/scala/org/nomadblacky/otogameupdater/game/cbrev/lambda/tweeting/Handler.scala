package org.nomadblacky.otogameupdater.game.cbrev.lambda.tweeting

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.danielasfregola.twitter4s.TwitterRestClient
import com.danielasfregola.twitter4s.entities.{AccessToken, ConsumerToken, Tweet}

import scala.beans.BeanProperty
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

case class Request(
  @BeanProperty var text: String = "",
) {
  def this() = this("")
}

case class Response(
  @BeanProperty message: String,
  @BeanProperty request: Request,
  @BeanProperty tweet: Tweet
)

case class Tokens(
  consumerKey: String,
  consumerSecret: String,
  accessToken: String,
  accessTokenSecret: String
)

object Tokens {
  def fromEnv(): Tokens = Tokens(
    sys.env.getOrElse("TWITTER_CONSUMER_KEY", ""),
    sys.env.getOrElse("TWITTER_CONSUMER_SECRET", ""),
    sys.env.getOrElse("TWITTER_ACCESS_TOKEN", ""),
    sys.env.getOrElse("TWITTER_ACCESS_TOKEN_SECRET", "")
  )
}

class Handler extends RequestHandler[Request, Response] {
  override def handleRequest(input: Request, context: Context): Response = {
    val tokens = Tokens.fromEnv()

    context.getLogger.log(input.toString)

    val restClient = TwitterRestClient(
      ConsumerToken(tokens.consumerKey, tokens.consumerSecret),
      AccessToken(tokens.accessToken, tokens.accessTokenSecret)
    )
    val f = restClient.createTweet(status = input.text).map(Response("OK", input, _))

    Await.result(f, Duration.Inf)
  }
}
