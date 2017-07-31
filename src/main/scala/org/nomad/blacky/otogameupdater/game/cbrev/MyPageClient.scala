package org.nomad.blacky.otogameupdater.game.cbrev

import java.net.HttpCookie

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

import scala.util.matching.Regex
import scalaj.http._


class MyPageClient() {

  val browser: Browser = JsoupBrowser()
  val profileSelector: String = "#profile > div > div.blockRight > div > div.loginBlock"
  val charangeClassRegexp: Regex = """.*obj_class(\d+)\.png.*""".r

  def login(accessCode: String, password: String): Option[HttpCookie] =
    Http("https://rev-srw.ac.capcom.jp/webloginconfirm")
      .postForm(Seq(("ac", accessCode), ("passwd", password)))
      .asString
      .cookies
      .find(_.getName == "_rst")

  def getUserData(loginCookie: HttpCookie): UserData = {
    val response = Http("https://rev-srw.ac.capcom.jp/profile")
      .cookie(loginCookie)
      .asString
    val doc = browser.parseString(response.body) >> element(profileSelector)

    UserData(
      name = doc >> element("div.m-profile .m-profile__name") >> text,
      rankPoint = doc >> element("div.m-profile .m-profile__rp dd") >> text toDouble,
      charangeClass = doc >> element("div.m-profile .m-profile__class img") >> attr("src") match {
        case charangeClassRegexp(x) => x.toInt
      },
      revUserId = doc >> element(".p-table__tbody dd") >> text toInt
    )
  }
}
