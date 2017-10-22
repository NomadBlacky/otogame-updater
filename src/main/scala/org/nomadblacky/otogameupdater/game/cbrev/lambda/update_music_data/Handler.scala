package org.nomadblacky.otogameupdater.game.cbrev.lambda.update_music_data

import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

import scala.beans.BeanProperty

case class Request(
  @BeanProperty var accessCode: String = "",
  @BeanProperty var password: String = ""
) {
  def this() = this("", "")
}

case class Response(
  @BeanProperty message: String
)

class Handler extends RequestHandler[Request, Response] {
  override def handleRequest(input: Request, context: Context) = {

    Response("OK")
  }
}
