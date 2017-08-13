package org.nomadblacky.otogameupdater.game.cbrev.lambda

object Utils {

  implicit class RichProduct(p: Product) {
    def toMap(): Map[String, Any] =
      p.getClass.getDeclaredFields.map(_.getName).zip(p.productIterator.toList).toMap
  }

}
