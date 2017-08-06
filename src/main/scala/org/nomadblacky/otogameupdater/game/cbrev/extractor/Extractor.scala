package org.nomadblacky.otogameupdater.game.cbrev.extractor

trait Extractor[A] {
  def extract(htmlBody: String): A
}
