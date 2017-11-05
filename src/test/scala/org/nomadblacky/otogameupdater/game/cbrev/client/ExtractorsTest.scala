package org.nomadblacky.otogameupdater.game.cbrev.client

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Element
import org.nomadblacky.otogameupdater.game.cbrev.model.ClearStatuses.Ultimate
import org.nomadblacky.otogameupdater.game.cbrev.model.Difficulties._
import org.nomadblacky.otogameupdater.game.cbrev.model.Grades.GradeSpp
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

  private lazy val body01: Element = JsoupBrowser().parseString(playScoreHtml01).body

  test("extractStage01") {
    val actual = extractStage(body01)
    actual shouldBe Stage(
      difficulty = Master,
      level = 51,
      notes = 285
    )
  }

  test("extractHighScore01") {
    val actual = extractHighScore(body01)
    actual shouldBe 27680
  }

  test("extractClearRate01") {
    val actual = extractClearRate(body01)
    actual shouldBe 99.71
  }

  test("extractRankPoint01") {
    val actual = extractRankPoint(body01)
    actual shouldBe Some(60.58)
  }

  test("extractClearStatus01") {
    val actual = extractClearStatus(body01, Some(GradeSpp))
    actual shouldBe Some(Ultimate)
  }

  test("extractGrade01") {
    val actual = extractGrade(body01)
    actual shouldBe Some(GradeSpp)
  }

  test("extractFullCombo01") {
    val actual = extractFullCombo(body01)
    actual shouldBe true
  }
}
