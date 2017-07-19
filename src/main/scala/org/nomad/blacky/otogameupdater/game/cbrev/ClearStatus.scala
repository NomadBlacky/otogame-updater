package org.nomad.blacky.otogameupdater.game.cbrev

/**
  * Created by blacky on 17/07/19.
  */
sealed trait ClearStatus
object Failed extends ClearStatus
object Cleared extends ClearStatus
object Survival extends ClearStatus
object Ultimate extends ClearStatus
