package org.nomadblacky.otogameupdater.game.cbrev.client

import org.scalatest.{FunSuite, Matchers}

import scala.io.Source

class ExchangeTokenExtractorTest extends FunSuite with Matchers {

  val token = "335a3cb847b58c0c05831bdc83cc04d3001839ec63d8225193228f6f866195818778c29cedaca7e2789ca16b22b7fc6c1d0b5359cae98383fa12e55c0171ea79"

  test("testExtract") {
    val html = Source.fromURL(getClass.getResource("exchange_token_extractor.html")).mkString
    val exchangeResult = extract(html)(ExchangeTokenExtractor)
    exchangeResult shouldBe ExchangeToken(token)
  }
}
