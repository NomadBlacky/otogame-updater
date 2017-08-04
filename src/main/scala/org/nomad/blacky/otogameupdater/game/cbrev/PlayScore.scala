package org.nomad.blacky.otogameupdater.game.cbrev

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
)