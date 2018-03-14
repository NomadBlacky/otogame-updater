package org.nomadblacky.otogameupdater.game.cbrev.model

import org.nomadblacky.otogameupdater.game.cbrev.lambda.DynamoDbRecord

case class UserData(
  name: String,
  rankPoint: Double,
  charangeClass: Int,
  revUserId: Int
)