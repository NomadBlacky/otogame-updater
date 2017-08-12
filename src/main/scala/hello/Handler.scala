package hello

import awscala.dynamodbv2.{DynamoDB, Table}
import com.amazonaws.services.lambda.runtime.{Context, RequestHandler}

import scala.beans.BeanProperty
import scala.collection.JavaConverters

case class HelloModel(@BeanProperty var id: String, @BeanProperty var name: String, @BeanProperty var age: Int) {
  def this() = this("", "", 0)
}

class Handler extends RequestHandler[Request, Response] {

  implicit val dynamoDb: DynamoDB = DynamoDB.at(awscala.Region.Tokyo)

  def handleRequest(input: Request, context: Context): Response = {
    val table: Table = dynamoDb.table("dev-hello").get

    table.put("hoge", "name" -> "foo", "age" -> 10)

    Response("Go Serverless v1.0! Your function executed successfully!", input)
  }
}

class ApiGatewayHandler extends RequestHandler[Request, ApiGatewayResponse] {

  def handleRequest(input: Request, context: Context): ApiGatewayResponse = {
    val headers = Map("x-custom-response-header" -> "my custom response header value")
    ApiGatewayResponse(200, "Go Serverless v1.0! Your function executed successfully!",
      JavaConverters.mapAsJavaMap[String, Object](headers),
      true)
  }
}
