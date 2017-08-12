package org.nomadblacky.otogameupdater.game.cbrev.lambda

import awscala._
import awscala.dynamodbv2._
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}
import com.github.nscala_time.time.Imports
import org.nomadblacky.otogameupdater.game.cbrev.client.MyPageClient

import scala.beans.BeanProperty

case class Request(
  @BeanProperty var accessCode: String = "",
  @BeanProperty var password: String = ""
) {
  def this() = this("", "")
}

case class Response(
  @BeanProperty message: String,
  @BeanProperty request: Request
)

class UpdateUserData extends RequestHandler[Request, Response] {

  implicit val dynamoDB: DynamoDB = DynamoDB.at(Region.Tokyo)

  override def handleRequest(input: Request, context: Context): Response = {
    val userData = MyPageClient(input.accessCode, input.password).fetchUserData
    val table = dynamoDB.table("CbRevRankPoints").get
    val now = DateTime.now(Imports.DateTimeZone.forID("Asia/Tokyo"))
    val attrs: Seq[(String, Any)] = userData.toMap().+("updated" -> now).toSeq
    table.put(userData.revUserId, attrs: _*)
    Response("OK", input)
  }
}
