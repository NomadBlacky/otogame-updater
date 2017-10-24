package org.nomadblacky.otogameupdater.game.cbrev.model

import org.nomadblacky.otogameupdater.game.cbrev.model.Difficulty.DifficultyVal

/**
  * Created by blacky on 17/07/19.
  */
case class Stage(
  difficulty: DifficultyVal,
  level: Int,
  notes: Int
)
