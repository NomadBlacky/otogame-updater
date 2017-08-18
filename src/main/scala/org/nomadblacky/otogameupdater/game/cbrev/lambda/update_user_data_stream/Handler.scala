package org.nomadblacky.otogameupdater.game.cbrev.lambda.update_user_data_stream

import com.amazonaws.services.dynamodbv2.model.{AttributeValue, StreamRecord}
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

import scala.beans.BeanProperty
import scala.collection.JavaConverters._

case class Response(
  @BeanProperty message: String,
  @BeanProperty request: DynamodbEvent
)

class Handler extends RequestHandler[DynamodbEvent, Response] {

  override def handleRequest(input: DynamodbEvent, context: Context): Response = {
    val logger = context.getLogger

    input.getRecords.asScala.map(_.getDynamodb).foreach(eachRecord)

    Response("OK", input)
  }

  def eachRecord(record: StreamRecord): Unit = {
    for {
      oldImage <- Option(record.getOldImage)
      newImage <- Option(record.getNewImage)
    } {
      getTweetText(oldImage.asScala, newImage.asScala).foreach { text =>
        // TODO: Invoke `tweeting`
      }
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
      // TODO: Implement it
      s"oldRp: $oldRp, newRp: $newRp, diff: $diff"
    }
  }
}
