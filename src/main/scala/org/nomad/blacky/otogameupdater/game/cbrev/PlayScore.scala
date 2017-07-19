package org.nomad.blacky.otogameupdater.game.cbrev

/**
  * Created by blacky on 17/07/19.
  */
case class PlayScore(
  stage: Stage,
  score: Int,
  clearRate: Double,
  rankPoint: Double,
  clearStatus: ClearStatus,
  grade: Grade,
  fullCombo: Boolean
)