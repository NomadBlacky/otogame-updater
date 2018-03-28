package org.nomadblacky.otogameupdater.game.cbrev.client

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Element
import org.nomadblacky.otogameupdater.game.cbrev.model.ClearStatus
import org.nomadblacky.otogameupdater.game.cbrev.model.Difficulty
import org.nomadblacky.otogameupdater.game.cbrev.model.Grade
import org.nomadblacky.otogameupdater.game.cbrev.model._
import org.scalatest.{FunSuite, Matchers}

import scala.io.Source
import scalaj.http.HttpResponse

class ExtractorsTest extends FunSuite with Matchers {

  private def buildHttpResponse(resourceFileName: String): HttpResponse[String] = {
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

  private val playScoreHtml01: String =
    """<div class="pdm-result">
      |    <div class="pdm-resultHead master">
      |        <img class="pdm-diff" src="https://rev-srw.ac.capcom.jp/assets/common/img_common/bnr_difficulty_master.jpg?1462847149" alt="" />                                <p class="lv"><span><img src="https://rev-srw.ac.capcom.jp/assets/common/img_common/parts_corner_bl_re.gif?1443581577" alt="" />Lv.</span>
      |            51                                </p>
      |        <p class="note"><span>Note:</span>
      |            285                                </p>
      |    </div>
      |
      |    <div class="leftResult">
      |        <dl class="pdResultList">
      |            <dt>HIGHSCORE</dt>
      |            <dd>
      |                27680                                    </dd>
      |            <dt>CLEAR RATE</dt>
      |            <dd>
      |                99.71%</dd>
      |            <dt>RANK POINT</dt>
      |            <dd>
      |                60.58                                    </dd>
      |        </dl>
      |    </div>
      |
      |    <div class="rightResult">
      |        <ul class="pdResultIco">
      |            <li class="clear">
      |                <p>
      |                    <img src="https://rev-srw.ac.capcom.jp/assets/common/img_common/bnr_ULTIMATE_CLEAR.png?1462847149" alt="" />                                        </p>
      |            </li>
      |            <li class="grade">
      |                <img src="https://rev-srw.ac.capcom.jp/assets/common/img_common/grade_0.png?1462847149" alt="" />                                    </li>
      |                <li class="fullcombo">
      |                    <span class="ico-fullcombo"></span>
      |                </li>
      |        </ul>
      |    </div>
      |</div>
    """.stripMargin

  private lazy val playScoreBody01: Element = JsoupBrowser().parseString(playScoreHtml01).body

  test("extractStage01") {
    val actual = extractStage(playScoreBody01)
    actual shouldBe Stage(
      difficulty = Difficulty.Master,
      level = 51,
      notes = 285
    )
  }

  test("extractHighScore01") {
    val actual = extractHighScore(playScoreBody01)
    actual shouldBe 27680
  }

  test("extractClearRate01") {
    val actual = extractClearRate(playScoreBody01)
    actual shouldBe 99.71
  }

  test("extractRankPoint01") {
    val actual = extractRankPoint(playScoreBody01)
    actual shouldBe Some(60.58)
  }

  test("extractClearStatus01") {
    val actual = extractClearStatus(playScoreBody01, Some(Grade.Spp))
    actual shouldBe Some(ClearStatus.Ultimate)
  }

  test("extractGrade01") {
    val actual = extractGrade(playScoreBody01)
    actual shouldBe Some(Grade.Spp)
  }

  test("extractFullCombo01") {
    val actual = extractFullCombo(playScoreBody01)
    actual shouldBe true
  }


  private val musicDetailHtml01: String =
    """<div class="pdMusicDetail gr-Black">
      |    <img class="pdm-jkt" src="https://rev-srw.ac.capcom.jp/assets/common/img_contents/2/Here_comes_the_sun_For_you.png?1462847149" alt="" />                            <div class="pdm-txt">
      |        <p class="title">
      |            Here comes the sun ~For you~                                </p>
      |        <p class="author">
      |            Z pinkpong                                </p>
      |        <p class="bpm"><span><img src="https://rev-srw.ac.capcom.jp/assets/common/img_common/list_header1.gif?1443581577" alt="" />BPM</span>
      |            130                                </p>
      |    </div>
      |</div>
    """.stripMargin

  private lazy val musicDetailBody01: Element = JsoupBrowser().parseString(musicDetailHtml01).body

  test("extractTitle01") {
    val actual = extractTitle(musicDetailBody01)
    actual shouldBe "Here comes the sun ~For you~"
  }

  test("extractArtist01") {
    val actual = extractArtist(musicDetailBody01)
    actual shouldBe "Z pinkpong"
  }

  test("extractBpm01") {
    val actual = extractBpm(musicDetailBody01)
    actual shouldBe 130
  }
}
