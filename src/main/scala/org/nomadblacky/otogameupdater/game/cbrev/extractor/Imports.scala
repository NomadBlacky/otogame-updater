package org.nomadblacky.otogameupdater.game.cbrev.extractor

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.dsl.DSL._
import org.nomadblacky.otogameupdater.game.cbrev.model.{MusicInList, UserData}

import scala.util.matching.Regex

object Imports {
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
}