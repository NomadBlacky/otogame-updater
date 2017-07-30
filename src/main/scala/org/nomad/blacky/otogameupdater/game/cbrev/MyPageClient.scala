package org.nomad.blacky.otogameupdater.game.cbrev

import java.net.HttpCookie

object MyPageClient {
  val loginCookieName = "_rst"
}

class MyPageClient() {
  import MyPageClient._
  import scalaj.http._

  def login(accessCode: String, password: String): Option[HttpCookie] = {
    val response =
      Http("https://rev-srw.ac.capcom.jp/webloginconfirm")
        .postForm(Seq(("ac", accessCode), ("passwd", password)))
        .asString
    response.cookies.find(_.getName == loginCookieName)
  }

}
