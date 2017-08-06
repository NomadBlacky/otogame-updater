package org.nomadblacky.otogameupdater.game.cbrev.model

/**
  * Created by blacky on 17/07/19.
  */
case class MusicInList(title: String, artist: String, detailUrl: Option[String]) {
  val isUnlocked: Boolean = detailUrl.isDefined
}
