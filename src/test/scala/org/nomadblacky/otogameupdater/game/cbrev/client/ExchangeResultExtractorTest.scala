package org.nomadblacky.otogameupdater.game.cbrev.client

import org.scalatest.{FunSuite, Matchers}

import scala.io.Source

class ExchangeResultExtractorTest extends FunSuite with Matchers {

  test("#extract when the exchange was failed") {
    val html = Source.fromURL(getClass.getResource("exchange_result_extractor_01.html")).mkString
    val exchangeResult = extract(html)(ExchangeResultExtractor)
    exchangeResult shouldBe ExchangeResult("遷移が正しくありません。")
  }

  test("#extract when the exchange was successful") {
    val html = Source.fromURL(getClass.getResource("exchange_result_extractor_02.html")).mkString
    val exchangeResult = extract(html)(ExchangeResultExtractor)
    exchangeResult shouldBe ExchangeResult("ミュージックエナジー 300MEを プレミアムチケット 15枚に変換しました！")
  }
}
