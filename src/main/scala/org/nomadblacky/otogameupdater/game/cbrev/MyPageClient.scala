package org.nomadblacky.otogameupdater.game.cbrev

import java.net.HttpCookie

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import org.nomadblacky.otogameupdater.game.cbrev.extractor.Imports._
import org.nomadblacky.otogameupdater.game.cbrev.model.{MusicInList, UserData}

import scalaj.http._


class MyPageClient(val accessCode: String, val password: String) {

  val browser: Browser = JsoupBrowser()

  lazy val loginCookie: HttpCookie =
    Http("https://rev-srw.ac.capcom.jp/webloginconfirm")
      .postForm(Seq(("ac", accessCode), ("passwd", password)))
      .asString
      .cookies
      .find(_.getName == "_rst")
      .getOrElse(throw new IllegalStateException(s"Failed to loged in.(accessCode=$accessCode, password=$password)"))

  def fetchUserData: UserData = extract[UserData](
    Http("https://rev-srw.ac.capcom.jp/profile")
      .cookie(loginCookie)
      .asString
      .body
  )

  def fetchMusics: Seq[MusicInList] = extract[Seq[MusicInList]](
    Http("https://rev-srw.ac.capcom.jp/playdatamusic")
      .cookie(loginCookie)
      .asString
      .body
  )
}

object MyPageClient {
  def apply(accessCode: String, password: String): MyPageClient =
    new MyPageClient(accessCode, password)
}