package org.nomadblacky.otogameupdater.game.cbrev.model

/**
  * Created by blacky on 17/07/19.
  */
abstract sealed class Difficulty(val order: Int, val name: String) extends Ordered[Difficulty] {
  override def compare(that: Difficulty): Int = Difficulty.ordering.compare(this, that)
}

object Difficulty {
  implicit val ordering: Ordering[Difficulty] = Ordering.by(_.order)
}

object Difficulties {

  object Easy extends Difficulty(0, "Easy")

  object Standard extends Difficulty(1, "Standard")

  object Hard extends Difficulty(2, "Hard")

  object Master extends Difficulty(3, "Master")

  object Unlimited extends Difficulty(4, "Unlimited")

  val valueSet = Set(Easy, Standard, Hard, Master, Unlimited)
}
