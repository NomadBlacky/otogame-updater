package org.nomadblacky.otogameupdater.game.cbrev.lambda

import com.github.nscala_time.time.Imports._

object Utils {

  implicit class RichProduct(p: Product) {
    def toMap(): Map[String, Any] =
      p.getClass.getDeclaredFields.map(_.getName).zip(p.productIterator.toList).toMap
  }

  val defaultZone: DateTimeZone = DateTimeZone.forID("Asia/Tokyo")

  def now(zone: DateTimeZone = defaultZone): DateTime = DateTime.now(zone)
}
