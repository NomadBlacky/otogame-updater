package org.nomadblacky.otogameupdater.game.cbrev.lambda.update_user_data_stream

import com.amazonaws.services.lambda.runtime.events.DynamodbEvent
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

import scala.beans.BeanProperty

case class Response(
  @BeanProperty message: String,
  @BeanProperty request: DynamodbEvent
)

class Handler extends RequestHandler[DynamodbEvent, Response] {

  override def handleRequest(input: DynamodbEvent, context: Context): Response = {
    Response("OK", input)
  }
}
