package org.nomadblacky.otogameupdater.game.cbrev.lambda.update_user_data

import awscala.{DateTime, Region}
import awscala.dynamodbv2.DynamoDB
import com.amazonaws.services.lambda.runtime.{Context, LambdaLogger, RequestHandler}
import org.nomadblacky.otogameupdater.game.cbrev.client.MyPageClient
import org.nomadblacky.otogameupdater.game.cbrev.lambda.DynamoDbRecord
import org.nomadblacky.otogameupdater.game.cbrev.lambda.Utils._
import org.nomadblacky.otogameupdater.game.cbrev.model.UserData

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

case class UserDataRecord(userData: UserData, updated: DateTime) extends DynamoDbRecord {
  override val key: Int = userData.revUserId
  override val attributes: Seq[(String, Any)] = userData.toMap()
    .filter { case (k,_) => k != "revUserId" }
    .+("updated" -> updated)
    .toSeq
}

class Handler extends RequestHandler[Request, Response] {

  implicit val dynamoDB: DynamoDB = DynamoDB.at(Region.Tokyo)
  val tableName: String = "CbRevRankPoints"

  override def handleRequest(input: Request, context: Context): Response = {
    val logger: LambdaLogger = context.getLogger

    val userData = MyPageClient(input.accessCode, input.password).fetchUserData
    logger.log(s"Fetch user data: $userData")
    
    val table = dynamoDB.table(tableName)
      .getOrElse(throw new IllegalStateException(s"Table not found: $tableName"))

    val record = UserDataRecord(userData, now())

    table.put(record.key, record.attributes: _*)
    logger.log(s"Success to put data: $record")

    Response("OK", input)
  }
}
