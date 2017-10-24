package org.nomadblacky.otogameupdater.game.cbrev.model

import org.nomadblacky.otogameupdater.game.cbrev.model.Difficulty.DifficultyVal

case class MusicPlayData(
  musicInList: MusicDetail,
  playScores: Map[DifficultyVal, PlayScore]
)
