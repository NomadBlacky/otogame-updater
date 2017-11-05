package org.nomadblacky.otogameupdater.game.cbrev.client

import java.net.HttpCookie

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Document, Element}
import org.nomadblacky.otogameupdater.game.cbrev.model.ClearStatuses.{Cleared, Failed}
import org.nomadblacky.otogameupdater.game.cbrev.model.Grades.GradeF
import org.nomadblacky.otogameupdater.game.cbrev.model._

import scala.util.matching.Regex
import scalaj.http.HttpResponse

trait Extractor[T,U] extends ((HttpResponse[T], Browser) => U) {
  override def apply(response: HttpResponse[T], browser: Browser): U = extract(response, browser)
  def extract(response: HttpResponse[T], browser: Browser): U
}

trait Extractors {

  val profileSelector: String = "#profile > div > div.blockRight > div > div.loginBlock"
  val challengeClassRegex: Regex = """.*obj_class(\d+)\.png.*""".r

  implicit class HttpResponseOps[T](val response: HttpResponse[T]) {
    def extract[A](extractor: (HttpResponse[T], Browser) => A, browser: Browser = JsoupBrowser()): A =
      extractor(response, browser)
  }

  val loginCookieExtractor: Extractor[String, Option[HttpCookie]] = (response, _) =>
    response.cookies.find(_.getName == "_rst")

  val userDataExtractor: Extractor[String, UserData] = (response, browser) => {
    val doc = browser.parseString(response.body) >> element(profileSelector)

    UserData(
      name          = doc >> extractor("div.m-profile .m-profile__name", text),
      charangeClass = doc >> extractor(
        "div.m-profile .m-profile__class img", attr("src"), regexMatch(challengeClassRegex).captured
      ).map(_.toInt),
      rankPoint     = doc >> extractor("div.m-profile .m-profile__rp dd", text, asDouble),
      revUserId     = doc >> extractor(".p-table__tbody dd", text, asInt)
    )
  }

  val musicListExtractor: Extractor[String, Seq[MusicInList]] = (response, browser) => {
    val elements = browser.parseString(response.body) >> elementList(".pdMusicData")

    elements.map(e => {
      MusicInList(
        title     = e >>  element(".pdMtitle")  >> text,
        artist    = e >>  element(".pdMauthor") >> text,
        detailUrl = e >?> element("a")          >> attr("href")
      )
    })
  }

  val exchangeTokenExtractor: Extractor[String, Option[ExchangeToken]] = (response, _) =>
    response.cookies.find(_.getName == "csrf_token").map(ExchangeToken)

  val exchangeResultExtractor: Extractor[String, ExchangeResult] = (response, browser) => {
    val doc = browser.parseString(response.body)
    val message = doc
      .tryExtract(extractor("#login > div > div > div > div > p", text))
      .getOrElse(
        doc.extract(extractor("#profile > div > div.blockRight > div > div > div", text))
      )
    ExchangeResult(message)
  }

  val musicPlayDataExtractor: Extractor[String, MusicPlayData] = (response, browser) => {
    def extractPlayScores(doc: browser.DocumentType): Map[Difficulty, PlayScore] = doc
      .extract(elementList("#profile > div > div.blockRight > div > div > div > div.pdm-result"))
      .map(extractPlayScore)
      .map(s => (s.stage.difficulty, s))
      .toMap

    val doc: browser.DocumentType = browser.parseString(response.body)
    MusicPlayData(extractMusicDetail(doc), extractPlayScores(doc))
  }

  private def extractPlayScore(e: Element): PlayScore = {
    val maybeGrade = extractGrade(e)
    PlayScore(
      stage       = extractStage(e),
      highScore   = extractHighScore(e),
      clearRate   = extractClearRate(e),
      rankPoint   = extractRankPoint(e),
      clearStatus = extractClearStatus(e, maybeGrade),
      grade       = maybeGrade,
      fullCombo   = extractFullCombo(e)
    )
  }

  private[client] def extractStage(e: Element): Stage = {
    val difficulty = e.extract(extractor(
      "div > div.pdm-resultHead",
      attr("class"),
      regexMatch("""pdm-resultHead\s+(\w+)""")
        .captured
        .andThen(s => Difficulties.valueSet.find(_.name.toLowerCase == s))
        .andThen(_.getOrElse(throw new IllegalStateException("element not found.")))
    ))
    Stage(
      difficulty = difficulty,
      level = e >> extractor("div > div.pdm-resultHead > p.lv", text, regexMatch("""Lv\.\s*(\d+)""").captured.andThen(_.toInt)),
      notes = e >> extractor("div > div.pdm-resultHead > p.note", text, regexMatch("""Note:\s*(\d+)""").captured.andThen(_.toInt))
    )
  }

  private[client] def extractHighScore(e: Element): Int =
    e >> extractor("div > div.leftResult > dl > dd:nth-child(2)", text, asInt)

  private[client] def extractClearRate(e: Element): Double =
    e >> extractor(
      "div > div.leftResult > dl > dd:nth-child(4)",
      text,
      """(\d+\.?\d*)%""".r.findFirstMatchIn(_:String).map(_.subgroups.head).map(_.toDouble).getOrElse(0.0)
    )

  private[client] def extractRankPoint(e: Element): Option[Double] =
    e >> extractor(
      "div > div.leftResult > dl > dd:nth-child(6)",
      text,
      """(\d+\.?\d*)""".r.findFirstMatchIn(_:String).map(_.subgroups.head.toDouble)
    )

  private[client] def extractClearStatus(e: Element, maybeGrade: Option[Grade]): Option[ClearStatus] =
    maybeGrade match {
      case Some(g) if g == GradeF => Some(Failed)
      case None => None
      case Some(_) =>
        e.tryExtract(extractor(
          "div > div.rightResult > ul > li.clear > p > img",
          attr("src"),
          regexMatch("""bnr_(\w+)_CLEAR\.png""")
            .captured
            .andThen(s => ClearStatuses.values.find(_.displayName.toUpperCase == s).getOrElse(Cleared))
        ))
    }

  private[client] def extractGrade(e: Element): Option[Grade] =
    e.extract(extractor(
      "div > div.rightResult > ul.pdResultIco > li.grade > img",
      attr("src"),
      regexMatch("""grade_(\d+)\.png""")
        .captured
        .andThen(s => Grades.values.find(_.id == s))
    ))

  private[client] def extractFullCombo(e: Element): Boolean =
    (e >?> element("div > div.rightResult > ul > li.fullcombo > span")).isDefined


  private def extractMusicDetail(doc: Document): MusicDetail = {
    val title = doc
      .extract(element("#profile > div > div.blockRight > div > div > div > div.pdMusicDetail.gr-Black > div > p.title"))
      .extract(text)
    val artist = doc
      .extract(element("#profile > div > div.blockRight > div > div > div > div.pdMusicDetail.gr-Black > div > p.author"))
      .extract(text)
    val bpm = doc
      .extract(extractor(
        "#profile > div > div.blockRight > div > div > div > div.pdMusicDetail.gr-Black > div > p.bpm",
        text,
        regexMatch("""BPM\s*(\d+\.?\d*)""").captured.andThen(_.toDouble)
      ))
    MusicDetail(title, artist, bpm)
  }
}
