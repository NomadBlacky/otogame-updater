package org.nomadblacky.otogameupdater.game.cbrev.extractor

import net.ruippeixotog.scalascraper.browser.Browser

trait Extractor[A] {
  def extract(htmlBody: String, browser: Browser): A
}
