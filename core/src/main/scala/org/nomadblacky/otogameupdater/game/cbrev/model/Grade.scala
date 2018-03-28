package org.nomadblacky.otogameupdater.game.cbrev.model

/**
  * Created by blacky on 17/07/19.
  */
sealed abstract class Grade(
  val order: Int,
  val id: String,
  val displayName: String
) extends Ordered[Grade] {
  override def compare(that: Grade): Int = Grade.ordering.compare(this, that)
}

object Grade {
  implicit val ordering: Ordering[Grade] = Ordering.by(_.order)

  object Spp extends Grade(10, "0", "S++")
  object Sp  extends Grade(9, "1", "S+")
  object S   extends Grade(8, "2", "S")
  object Ap  extends Grade(7, "3", "A+")
  object A   extends Grade(6, "4", "A")
  object Bp  extends Grade(5, "5", "B+")
  object B   extends Grade(4, "6", "B")
  object C   extends Grade(3, "7", "C")
  object D   extends Grade(2, "8", "D")
  object E   extends Grade(1, "9", "E")
  object F   extends Grade(0, "10", "F")

  val values = Set(Spp, Sp, S, Ap, A, Bp, B, C, D, E, F)
}
