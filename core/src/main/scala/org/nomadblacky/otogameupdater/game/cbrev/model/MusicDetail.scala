package org.nomadblacky.otogameupdater.game.cbrev.model

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

case class MusicDetail(
  title: String,
  artist: String,
  bpm: Double
) {
  def id: String = URLEncoder.encode(title, StandardCharsets.UTF_8.toString)
}
