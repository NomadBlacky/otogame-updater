package org.nomadblacky.otogameupdater.game.cbrev.lambda

trait DynamoDbRecord {
  val key: Any
  val attributes: Seq[(String, Any)]
}
