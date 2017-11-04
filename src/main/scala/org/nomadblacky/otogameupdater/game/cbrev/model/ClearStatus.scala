package org.nomadblacky.otogameupdater.game.cbrev.model

/**
  * Created by blacky on 17/07/19.
  */
sealed trait ClearStatus

object ClearStatuses {

  object Failed extends ClearStatus

  object Cleared extends ClearStatus

  object Survival extends ClearStatus

  object Ultimate extends ClearStatus

}