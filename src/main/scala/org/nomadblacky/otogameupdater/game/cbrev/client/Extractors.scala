package org.nomadblacky.otogameupdater.game.cbrev.client

import java.net.HttpCookie

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
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
    def extractMusicDetail(doc: browser.DocumentType): MusicDetail = {
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
    def extractPlayScores(doc: browser.DocumentType): Map[Difficulty, PlayScore] = {
      val scoreDivs = doc >> elementList("#profile > div > div.blockRight > div > div > div > div.pdm-result")
      scoreDivs.map { e =>
        val difficultyStr = e >> extractor("div > div.pdm-resultHead", attr("class"), regexMatch("""pdm-resultHead\s+(\w+)""").captured)
        val difficulty = Difficulties.valueSet
          .find(_.name.toLowerCase == difficultyStr)
          .getOrElse(throw new IllegalStateException("element not found."))
        val stage = Stage(
          difficulty = difficulty,
          level = e >> extractor("div > div.pdm-resultHead > p.lv", text, regexMatch("""Lv\.\s*(\d+)""").captured.andThen(_.toInt)),
          notes = e >> extractor("div > div.pdm-resultHead > p.note", text, regexMatch("""Note:\s*(\d+)""").captured.andThen(_.toInt))
        )
        val clearRatePaser: (String => Double) =
          """(\d+\.?\d*)%""".r.findFirstMatchIn(_).map(_.subgroups.head).getOrElse("0.0").toDouble
        val rankPointPaser: (String => Option[Double]) =
          """(\d+\.?\d*)""".r.findFirstMatchIn(_).map(_.subgroups.head.toDouble)
        val maybeGrade = e.extract(extractor(
          "div > div.rightResult > ul.pdResultIco > li.grade > img",
          attr("src"),
          regexMatch("""grade_(\d+)\.png""")
            .captured
            .andThen(s => Grades.values.find(_.id == s))
        ))
        val clearStatus: Option[ClearStatus] = e.tryExtract(extractor(
          "div > div.rightResult > ul > li.clear > p > img",
          attr("src"),
          regexMatch("""bnr_(\w+)_CLEAR\.png""")
            .captured
            .andThen(s => ClearStatuses.values.find(_.displayName.toUpperCase == s).get)
        )).orElse {
          maybeGrade match {
            case Some(g) if g == GradeF => Some(Failed)
            case Some(_)                => Some(Cleared)
            case None                   => None
          }
        }

        val playScore = PlayScore(
          stage       = stage,
          highScore   = e >> extractor("div > div.leftResult > dl > dd:nth-child(2)", text, asInt),
          clearRate   = e >> extractor("div > div.leftResult > dl > dd:nth-child(4)", text, clearRatePaser),
          rankPoint   = e >> extractor("div > div.leftResult > dl > dd:nth-child(6)", text, rankPointPaser),
          clearStatus = clearStatus,
          grade       = maybeGrade,
          fullCombo   = (e >?> element("div > div.rightResult > ul > li.fullcombo > span")).isDefined
        )
        (difficulty, playScore)
      }.toMap
    }

    val doc: browser.DocumentType = browser.parseString(response.body)
    MusicPlayData(extractMusicDetail(doc), extractPlayScores(doc))
  }
}