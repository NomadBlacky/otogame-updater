package org.nomadblacky.otogameupdater.game.cbrev.model

/**
  * Created by blacky on 17/07/19.
  */
sealed abstract class ClearStatus(val order: Int, val displayName: String) extends Ordered[ClearStatus] {
  override def compare(that: ClearStatus): Int = ClearStatus.ordering.compare(this, that)
}

object ClearStatus {
  implicit val ordering: Ordering[ClearStatus] = Ordering.by(_.order)

  object Failed   extends ClearStatus(0, "Failed")
  object Cleared  extends ClearStatus(1, "Cleared")
  object Survival extends ClearStatus(2, "Survival")
  object Ultimate extends ClearStatus(3, "Ultimate")

  val values = Set(Failed, Cleared, Survival, Ultimate)
}
