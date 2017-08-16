package org.nomadblacky.otogameupdater.game.cbrev.lambda

import org.scalatest.Matchers

class DynamoDbRecordTest extends org.scalatest.FunSuite with Matchers {

  case class TestRecord(id: Int, name: String) extends DynamoDbRecord {
    override val key: Any = id
    override val attributes: Seq[(String, Any)] = Seq("name" -> name)
  }

  val target = TestRecord(1, "hoge")

  test("testKey") {
    target.key shouldBe 1
  }

  test("testAttributes") {
    target.attributes shouldBe Seq(("name", "hoge"))
  }
}
