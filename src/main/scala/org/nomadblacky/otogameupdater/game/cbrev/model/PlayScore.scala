package org.nomadblacky.otogameupdater.game.cbrev.model

import org.nomadblacky.otogameupdater.game.cbrev.model.ClearStatus.ClearStatus
import org.nomadblacky.otogameupdater.game.cbrev.model.Grade.Grade

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