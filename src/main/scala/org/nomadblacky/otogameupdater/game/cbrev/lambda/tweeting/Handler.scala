package org.nomadblacky.otogameupdater.game.cbrev.lambda.tweeting

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

import scala.beans.BeanProperty

case class Request(
  @BeanProperty var text: String = "",
) {
  def this() = this("")
}

case class Response(
  @BeanProperty message: String,
  @BeanProperty request: Request
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
    val logger = context.getLogger

    val tokens = Tokens.fromEnv()
    logger.log(tokens.toString)

    // TODO: Implement it.

    Response("OK", input)
  }
}
