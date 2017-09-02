package org.nomadblacky.otogameupdater.game.cbrev

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.{attr, element, elementList, text}
import net.ruippeixotog.scalascraper.scraper.ContentParsers.{asDouble, asInt, regexMatch}
import org.nomadblacky.otogameupdater.game.cbrev.ext.Extractor
import org.nomadblacky.otogameupdater.game.cbrev.model.{MusicInList, UserData}

import scala.util.matching.Regex

package object client {
  val profileSelector: String = "#profile > div > div.blockRight > div > div.loginBlock"
  val challengeClassRegex: Regex = """.*obj_class(\d+)\.png.*""".r

  def extract[A](htmlBody: String, browser: Browser = JsoupBrowser())(implicit extractor: Extractor[A]): A =
    extractor.extract(htmlBody, browser)

  implicit object UserDataExtractor extends Extractor[UserData] {
    override def extract(htmlBody: String, browser: Browser): UserData = {
      val doc = browser.parseString(htmlBody) >> element(profileSelector)

      UserData(
        name          = doc >> extractor("div.m-profile .m-profile__name", text),
        charangeClass = doc >> extractor(
          "div.m-profile .m-profile__class img", attr("src"), regexMatch(challengeClassRegex).captured
        ).map(_.toInt),
        rankPoint     = doc >> extractor("div.m-profile .m-profile__rp dd", text, asDouble),
        revUserId     = doc >> extractor(".p-table__tbody dd", text, asInt)
      )
    }
  }

  implicit object MusicsExtractor extends Extractor[Seq[MusicInList]] {
    override def extract(htmlBody: String, browser: Browser): Seq[MusicInList] = {
      val elements = browser.parseString(htmlBody) >> elementList(".pdMusicData")

      elements.map(e => {
        MusicInList(
          title     = e >>  element(".pdMtitle")  >> text,
          artist    = e >>  element(".pdMauthor") >> text,
          detailUrl = e >?> element("a")          >> attr("href")
        )
      })
    }
  }

  implicit object ExchangeResultExtractor extends Extractor[ExchangeResult] {
    override def extract(htmlBody: String, browser: Browser): ExchangeResult = {
      val message = browser.parseString(htmlBody)
        .extract(extractor("#login > div > div > div > div > p", text))
      ExchangeResult(message)
    }
  }

  implicit object ExchangeTokenExtractor extends Extractor[ExchangeToken] {
    override def extract(htmlBody: String, browser: Browser): ExchangeToken = {
      val token = browser.parseString(htmlBody)
        .extract(extractor("#form_csrf_token", attr("value")))
      ExchangeToken(token)
    }
  }
}
