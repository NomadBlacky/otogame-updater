package org.nomadblacky.otogameupdater.game.cbrev.model

/**
  * Created by blacky on 17/07/19.
  */
abstract sealed class Difficulty(val name: String)

object Difficulties {

  object Easy extends Difficulty("Easy")

  object Standard extends Difficulty("Standard")

  object Hard extends Difficulty("Hard")

  object Master extends Difficulty("Master")

  object Unlimited extends Difficulty("Unlimited")

  val valueSet = Set(Easy, Standard, Hard, Master, Unlimited)
}
