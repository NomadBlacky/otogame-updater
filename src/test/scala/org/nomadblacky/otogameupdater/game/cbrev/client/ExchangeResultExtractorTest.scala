package org.nomadblacky.otogameupdater.game.cbrev.client

import org.scalatest.{FunSuite, Matchers}

import scala.io.Source

class ExchangeResultExtractorTest extends FunSuite with Matchers {

  test("testExtract") {
    val html = Source.fromURL(getClass.getResource("exchange_result_extractor.html")).mkString
    val exchangeResult = extract(html)(ExchangeResultExtractor)
    exchangeResult shouldBe ExchangeResult("遷移が正しくありません。")
  }
}
