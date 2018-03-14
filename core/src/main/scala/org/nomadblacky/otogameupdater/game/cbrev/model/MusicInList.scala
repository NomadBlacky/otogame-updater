package org.nomadblacky.otogameupdater.game.cbrev.model

import scala.util.matching.Regex

/**
  * Created by blacky on 17/07/19.
  */
case class MusicInList(title: String, artist: String, detailUrl: Option[String]) {
  import MusicInList._

  val isUnlocked: Boolean = detailUrl.isDefined

  val musicKey: Option[String] = detailUrl.collect {
    case musicKeyRegex(key) => key
  }
}

object MusicInList {
  val musicKeyRegex: Regex = "^http://.+/playdatamusic/(.+)$".r
}
