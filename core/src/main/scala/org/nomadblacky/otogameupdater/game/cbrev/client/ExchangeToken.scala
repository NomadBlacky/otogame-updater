package org.nomadblacky.otogameupdater.game.cbrev.client

import java.net.HttpCookie

case class ExchangeToken(cookie: HttpCookie) {
  val pair: (String, String) = (cookie.getName, cookie.getValue)
}
