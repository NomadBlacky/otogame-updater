package org.nomadblacky.otogameupdater.game.cbrev.client

import org.nomadblacky.otogameupdater.game.cbrev.model.ClearStatus._
import org.nomadblacky.otogameupdater.game.cbrev.model.Difficulty._
import org.nomadblacky.otogameupdater.game.cbrev.model.Grade._
import org.nomadblacky.otogameupdater.game.cbrev.model._
import org.scalatest.{FunSuite, Matchers}

import scala.io.Source
import scalaj.http.HttpResponse

class ExtractorsTest extends FunSuite with Matchers {

  def buildHttpResponse(resourceFileName: String): HttpResponse[String] = {
    val html = Source.fromURL(getClass.getResource(resourceFileName)).mkString
    HttpResponse(html, 200, null)
  }

  test("exchangeResultExtractor when the exchange was failed") {
    val exchangeResult =
      buildHttpResponse("exchange_result_extractor_01.html")
      .extract(exchangeResultExtractor)
    exchangeResult shouldBe ExchangeResult("遷移が正しくありません。")
  }

  test("exchangeResultExtractor when the exchange was successful") {
    val exchangeResult =
      buildHttpResponse("exchange_result_extractor_02.html")
      .extract(exchangeResultExtractor)
    exchangeResult shouldBe ExchangeResult("ミュージックエナジー 300MEを プレミアムチケット 15枚に変換しました！")
  }

  test("musicPlayDataExtractor") {
    val result =
      buildHttpResponse("music_play_data_extractor_01.html")
      .extract(musicPlayDataExtractor)
    val expect = MusicPlayData(
      musicInList = MusicDetail(
        title = "Here comes the sun ~For you~",
        artist = "Z pinkpong",
        bpm = 130
      ),
      Map(
        Easy -> PlayScore(
          stage = Stage(Easy, 11, 83),
          highScore = 0,
          clearRate = 0.0,
          rankPoint = None,
          clearStatus = None,
          grade = None,
          fullCombo = false
        ),
        Standard -> PlayScore(
          stage = Stage(Standard, 16, 119),
          highScore = 0,
          clearRate = 0.0,
          rankPoint = None,
          clearStatus = None,
          grade = None,
          fullCombo = false
        ),
        Hard -> PlayScore(
          stage = Stage(Hard, 51, 285),
          highScore = 0,
          clearRate = 0.0,
          rankPoint = None,
          clearStatus = None,
          grade = None,
          fullCombo = false
        ),
        Master -> PlayScore(
          stage = Stage(Master, 51, 285),
          highScore = 27680,
          clearRate = 99.71,
          rankPoint = Some(60.58),
          clearStatus = Some(Ultimate),
          grade = Some(GradeSpp),
          fullCombo = true
        ),
        Unlimited -> PlayScore(
          stage = Stage(Unlimited, 68, 478),
          highScore = 41150,
          clearRate = 87.9,
          rankPoint = Some(65.07),
          clearStatus = Some(Survival),
          grade = Some(GradeSp),
          fullCombo = false
        ),
      )
    )
    result shouldBe expect
  }
}
