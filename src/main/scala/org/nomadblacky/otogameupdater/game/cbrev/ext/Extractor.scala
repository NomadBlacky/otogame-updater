package org.nomadblacky.otogameupdater.game.cbrev.ext

import net.ruippeixotog.scalascraper.browser.Browser

trait Extractor[A] {
  def extract(htmlBody: String, browser: Browser): A
}
