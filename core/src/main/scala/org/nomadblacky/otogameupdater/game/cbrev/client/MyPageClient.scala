package org.nomadblacky.otogameupdater.game.cbrev.client

import java.net.HttpCookie

import org.nomadblacky.otogameupdater.game.cbrev.model.{MusicInList, MusicPlayData, UserData}

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
      .extract(loginCookieExtractor)
      .getOrElse(loginException())

  def fetchUserData: UserData =
    Http("https://rev-srw.ac.capcom.jp/profile")
      .cookie(loginCookie)
      .asString
      .extract(userDataExtractor)

  def fetchMusics: Seq[MusicInList] =
    Http("https://rev-srw.ac.capcom.jp/playdatamusic")
      .cookie(loginCookie)
      .asString
      .extract(musicListExtractor)

  def fetchMusicPlayData(musics: Seq[MusicInList]): Seq[MusicPlayData] =
    musics
      .flatMap(_.detailUrl)
      .map(Http(_).cookie(loginCookie).asString.extract(musicPlayDataExtractor))

  def fetchExchangeToken: Option[ExchangeToken] =
    Http("https://rev-srw.ac.capcom.jp/musicenergy")
      .cookie(loginCookie)
      .asString
      .extract(exchangeTokenExtractor)

  def exchangeMusicEnergy(token: ExchangeToken): ExchangeResult =
    Http("https://rev-srw.ac.capcom.jp/musicenergyexc")
      .postForm(Seq(token.pair))
      .cookies(Seq(loginCookie, token.cookie))
      .option(HttpOptions.followRedirects(true))
      .asString
      .extract(exchangeResultExtractor)
}
