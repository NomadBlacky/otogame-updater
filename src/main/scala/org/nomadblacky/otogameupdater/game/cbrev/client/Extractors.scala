package org.nomadblacky.otogameupdater.game.cbrev.client

import java.net.HttpCookie

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.{attr, element, elementList, text}
import net.ruippeixotog.scalascraper.scraper.ContentParsers.{asDouble, asInt, regexMatch}
import org.nomadblacky.otogameupdater.game.cbrev.model.Difficulties.Easy
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

      Map(
        Easy -> PlayScore(Stage(Easy, 0, 0), 0, 0.0, None, None, None, fullCombo = false)
      )
    }

    val doc: browser.DocumentType = browser.parseString(response.body)
    MusicPlayData(extractMusicDetail(doc), extractPlayScores(doc))
  }
}
