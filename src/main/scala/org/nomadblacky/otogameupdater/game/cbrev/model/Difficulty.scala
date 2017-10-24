package org.nomadblacky.otogameupdater.game.cbrev.model

/**
  * Created by blacky on 17/07/19.
  */
object Difficulty extends Enumeration {

  case class DifficultyVal(name: String) extends Val

  val Easy      = DifficultyVal("Easy")
  val Standard  = DifficultyVal("Standard")
  val Hard      = DifficultyVal("Hard")
  val Master    = DifficultyVal("Master")
  val Unlimited = DifficultyVal("Unlimited")

  val valueSet = Set(Easy, Standard, Hard, Master, Unlimited)
}
