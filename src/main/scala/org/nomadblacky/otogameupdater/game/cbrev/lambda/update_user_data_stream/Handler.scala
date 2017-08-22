package org.nomadblacky.otogameupdater.game.cbrev.lambda.update_user_data_stream

import com.amazonaws.services.dynamodbv2.model.{AttributeValue, StreamRecord}
import com.amazonaws.services.lambda.AWSLambdaAsyncClientBuilder
import com.amazonaws.services.lambda.model.InvokeRequest
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent
import com.amazonaws.services.lambda.runtime.{Context, LambdaLogger, RequestHandler}

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

case class Response(
  @BeanProperty message: String,
  @BeanProperty request: DynamodbEvent
)

object Handler {
  val envName = "TWEETING_FUNCTION_NAME"
  lazy val functionName: String =
    sys.env.getOrElse(envName, throw new IllegalStateException(s"ENV '$envName' is not found."))

  def eachRecord(record: StreamRecord)(implicit logger: LambdaLogger): Unit = {
    for {
      oldImage <- Option(record.getOldImage)
      newImage <- Option(record.getNewImage)
    } {
      getTweetText(oldImage.asScala, newImage.asScala).foreach(invokeTweeting)
    }
  }

  def getTweetText(
    oldImage: collection.Map[String, AttributeValue],
    newImage: collection.Map[String, AttributeValue]
  ): Option[String] = {
    for {
      oldRp <- oldImage.get("rankPoint").flatMap(v => Option(v.getN)).map(_.toDouble)
      newRp <- newImage.get("rankPoint").flatMap(v => Option(v.getN)).map(_.toDouble)
      diff  <- Some(newRp - oldRp).filter(0.0 < _)
    } yield {
      "[cbRev - RP was updated!]\n%.2f → %.2f (%+.2f)" format (oldRp, newRp, diff)
    }
  }

  def invokeTweeting(text: String)(implicit logger: LambdaLogger): Unit = {
    val lambda = AWSLambdaAsyncClientBuilder.defaultClient()

    val invokeRequest = new InvokeRequest
    invokeRequest.setFunctionName(functionName)
    invokeRequest.setPayload(s"""{"text":"$text"}""")

    lambda.invokeAsync(invokeRequest)
  }
}

class Handler extends RequestHandler[DynamodbEvent, Response] {
  import Handler._

  override def handleRequest(input: DynamodbEvent, context: Context): Response = {
    implicit val logger: LambdaLogger = context.getLogger

    input.getRecords.asScala.map(_.getDynamodb).foreach(eachRecord)

    Response("OK", input)
  }
}
