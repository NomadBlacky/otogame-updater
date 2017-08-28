package org.nomadblacky.otogameupdater.game.cbrev.lambda.exchange_music_energy

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import org.nomadblacky.otogameupdater.game.cbrev.client.{ExchangeResult, MyPageClient}

import scala.beans.BeanProperty

case class Request(
  @BeanProperty var accessCode: String = "",
  @BeanProperty var password: String = ""
) {
  def this() = this("", "")
}

case class Response(
  @BeanProperty message: String,
  @BeanProperty result: ExchangeResult
)

class Handler extends RequestHandler[Request, Response] {

  override def handleRequest(input: Request, context: Context): Response = {
    val logger = context.getLogger

    logger.log(s"input: $input")

    val client = MyPageClient(input.accessCode, input.password)
    val result = client.exchangeMusicEnergy
    logger.log(s"result: $result")

    Response("OK", result)
  }
}
