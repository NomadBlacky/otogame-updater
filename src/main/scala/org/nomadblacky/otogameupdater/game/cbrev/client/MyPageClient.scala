package org.nomadblacky.otogameupdater.game.cbrev.client

import java.net.HttpCookie

import org.nomadblacky.otogameupdater.game.cbrev.model.{MusicInList, UserData}

import scalaj.http._

case class MyPageClient(accessCode: String, password: String) {

  private def loginException: () => Nothing =
    throw new IllegalStateException(
      s"Failed to logged in.(accessCode=$accessCode, password=$password)"
    )

  lazy val loginCookie: HttpCookie =
    Http("https://rev-srw.ac.capcom.jp/webloginconfirm")
      .postForm(Seq(("ac", accessCode), ("passwd", password)))
      .asString
      .cookies
      .find(_.getName == "_rst")
      .getOrElse(loginException())

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

  def exchangeMusicEnergy: ExchangeResult = {
    val token = extract[ExchangeToken](
      Http("https://rev-srw.ac.capcom.jp/musicenergy")
        .cookie(loginCookie)
        .asString
        .body
    )

    extract[ExchangeResult](
      Http("https://rev-srw.ac.capcom.jp/musicenergyexc")
        .postForm(Seq(("csrf_token", token.asString)))
        .cookie(loginCookie)
        .asString
        .body
    )
  }
}
