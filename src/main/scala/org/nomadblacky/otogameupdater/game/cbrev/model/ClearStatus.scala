package org.nomadblacky.otogameupdater.game.cbrev.model

/**
  * Created by blacky on 17/07/19.
  */
sealed abstract class ClearStatus(val displayName: String)

object ClearStatuses {

  object Failed extends ClearStatus("Failed")

  object Cleared extends ClearStatus("Cleared")

  object Survival extends ClearStatus("Survival")

  object Ultimate extends ClearStatus("Ultimate")

  val values = Set(Failed, Cleared, Survival, Ultimate)
}