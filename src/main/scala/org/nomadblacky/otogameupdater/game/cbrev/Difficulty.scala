package org.nomadblacky.otogameupdater.game.cbrev

/**
  * Created by blacky on 17/07/19.
  */
sealed trait Difficulty
object Easy extends Difficulty
object Standard extends Difficulty
object Hard extends Difficulty
object Master extends Difficulty
object Unlimited extends Difficulty
