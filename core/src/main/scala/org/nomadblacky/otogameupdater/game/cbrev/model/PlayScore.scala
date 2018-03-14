package org.nomadblacky.otogameupdater.game.cbrev.model

/**
  * Created by blacky on 17/07/19.
  */
case class PlayScore(
  stage: Stage,
  highScore: Int,
  clearRate: Double,
  rankPoint: Option[Double],
  clearStatus: Option[ClearStatus],
  grade: Option[Grade],
  fullCombo: Boolean
) extends Ordered[PlayScore] {
  override def compare(that: PlayScore): Int = PlayScore.ordering.compare(this, that)
}

object PlayScore {
  val ordering: Ordering[PlayScore] = Ordering.by((ps: PlayScore) =>
    (ps.highScore, ps.clearRate, ps.rankPoint, ps.clearStatus, ps.grade, ps.fullCombo)
  )
}