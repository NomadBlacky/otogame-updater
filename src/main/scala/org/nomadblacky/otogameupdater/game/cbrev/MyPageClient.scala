package org.nomadblacky.otogameupdater.game.cbrev

import java.net.HttpCookie

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL.Parse._
import net.ruippeixotog.scalascraper.dsl.DSL._

import scala.util.matching.Regex
import scalaj.http._


class MyPageClient(val accessCode: String, val password: String) {

  val browser: Browser = JsoupBrowser()
  val profileSelector: String = "#profile > div > div.blockRight > div > div.loginBlock"
  val challengeClassRegex: Regex = """.*obj_class(\d+)\.png.*""".r

  lazy val loginCookie: HttpCookie =
    Http("https://rev-srw.ac.capcom.jp/webloginconfirm")
      .postForm(Seq(("ac", accessCode), ("passwd", password)))
      .asString
      .cookies
      .find(_.getName == "_rst")
      .getOrElse(throw new IllegalStateException(s"Failed to loged in.(accessCode=$accessCode, password=$password)"))

  def fetchUserData: UserData = {
    val response = Http("https://rev-srw.ac.capcom.jp/profile")
      .cookie(loginCookie)
      .asString
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

  def fetchMusics: Seq[MusicInList] = {
    val response = Http("https://rev-srw.ac.capcom.jp/playdatamusic")
      .cookie(loginCookie)
      .asString
    val elements = browser.parseString(response.body) >> elementList(".pdMusicData")

    elements.map(e => {
      MusicInList(
        title     = e >>  element(".pdMtitle")  >> text,
        artist    = e >>  element(".pdMauthor") >> text,
        detailUrl = e >?> element("a")          >> attr("href")
      )
    })
  }
}

object MyPageClient {
  def apply(accessCode: String, password: String): MyPageClient =
    new MyPageClient(accessCode, password)
}