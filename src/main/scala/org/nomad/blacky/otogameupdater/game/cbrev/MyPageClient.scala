package org.nomad.blacky.otogameupdater.game.cbrev

import com.softwaremill.sttp.Cookie

class MyPageClient(val accessCode: String, val password: String) {
  // fields
  var cookie: Option[Cookie] = None

  // sttp
  import com.softwaremill.sttp._
  implicit val handler = HttpURLConnectionSttpHandler

  def login(): Boolean = {
    val request = sttp
      .body(Map("ac" -> accessCode, "passwd" -> password), "UTF-8")
      .post(uri"https://rev-srw.ac.capcom.jp/webloginconfirm")
    val response = request.send()
    cookie = response.cookies.find(_.name == "_rst")
    cookie.isDefined
  }

  def isLoggedIn() = cookie.isDefined
}
