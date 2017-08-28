package org.nomadblacky.otogameupdater.game.cbrev.client

import java.net.HttpCookie

import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import org.nomadblacky.otogameupdater.game.cbrev.model.{MusicInList, UserData}

import scalaj.http._


case class MyPageClient(accessCode: String, password: String) {

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

