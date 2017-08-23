package org.nomadblacky.otogameupdater.game.cbrev.lambda.update_user_data_stream

import com.amazonaws.services.dynamodbv2.model.AttributeValue
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.prop.TableFor3
import org.scalatest.{FunSuite, Matchers}

class HandlerTest extends FunSuite with Matchers {
  import Handler._

  def attr(n: String): AttributeValue = {
    val attr = new AttributeValue()
    attr.setN(n)
    attr
  }

  val rp = "rankPoint"

  type Image = Map[String, AttributeValue]

  val fixtures: TableFor3[Image, Image, Option[String]] = Table[Image, Image, Option[String]](
    ("oldImage", "newImage", "result"),
    (Map(rp -> attr("1234.56")), Map(rp -> attr("1234.89")), Some("[cbRev - RP was updated!]\n1234.56 → 1234.89 (+0.33)")),
    (Map()                     , Map(rp -> attr("1234.89")), None),
    (Map()                     , Map()                     , None),
    (Map(rp -> attr("2345"))   , Map(rp -> attr("1234"))   , None)
  )

  forAll(fixtures)(getTweetText(_, _) shouldBe _)

  test("testGetPayload") {
    getPayload(Map("text" -> "hoge\nfoo")) shouldBe """{"text":"hoge\nfoo"}"""
  }
}
