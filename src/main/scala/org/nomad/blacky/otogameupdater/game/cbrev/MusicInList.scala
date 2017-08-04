package org.nomad.blacky.otogameupdater.game.cbrev

/**
  * Created by blacky on 17/07/19.
  */
case class MusicInList(title: String, artist: String, detailUrl: Option[String]) {
  val isUnlocked: Boolean = detailUrl.isDefined
}
