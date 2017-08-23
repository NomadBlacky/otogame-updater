package org.nomadblacky.otogameupdater.game.cbrev.lambda.update_user_data_stream

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor3
import org.scalatest.{FunSuite, Matchers}

class HandlerTest extends FunSuite with Matchers {
  import Handler._

  type Image = Map[String, AttributeValue]

  def image(name: String, rp: Double): Image = {
    Map(
      "name"      -> new AttributeValue().withN(name),
      "rankPoint" -> new AttributeValue().withN(rp.toString)
    )
  }

  val fixtures: TableFor3[Image, Image, Option[String]] = Table[Image, Image, Option[String]](
    ("oldImage", "newImage", "result"),
    (image("hoge", 1234.56), image("hoge", 1234.89), Some("[cbRev - RP was updated!]\nPlayer: hoge\n1234.56 → 1234.89 (+0.33)")),
    (Map()                 , image("hoge", 1234.89), None),
    (Map()                 , Map()                 , None),
    (image("hoge", 2345.00), image("hoge", 1234.00), None),
    (image("hoge", 1234.56), image("foo" , 1234.89), Some("[cbRev - RP was updated!]\nPlayer: foo\n1234.56 → 1234.89 (+0.33)")),
  )

  forAll(fixtures)(getTweetText(_, _) shouldBe _)

  test("testGetPayload") {
    getPayload(Map("text" -> "hoge\nfoo")) shouldBe """{"text":"hoge\nfoo"}"""
  }
}
