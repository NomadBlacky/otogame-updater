package org.nomadblacky.otogameupdater.game.cbrev.client

import org.scalatest.{FunSuite, Matchers}

import scala.io.Source
import scalaj.http.HttpResponse

class ExtractorsTest extends FunSuite with Matchers {

  test("exchangeResultExtractor when the exchange was failed") {
    val html = Source.fromURL(getClass.getResource("exchange_result_extractor_01.html")).mkString
    val response = HttpResponse(html, 200, null)
    val exchangeResult = response.extract(exchangeResultExtractor)
    exchangeResult shouldBe ExchangeResult("遷移が正しくありません。")
  }

  test("exchangeResultExtractor when the exchange was successful") {
    val html = Source.fromURL(getClass.getResource("exchange_result_extractor_02.html")).mkString
    val response = HttpResponse(html, 200, null)
    val exchangeResult = response.extract(exchangeResultExtractor)
    exchangeResult shouldBe ExchangeResult("ミュージックエナジー 300MEを プレミアムチケット 15枚に変換しました！")
  }
}
