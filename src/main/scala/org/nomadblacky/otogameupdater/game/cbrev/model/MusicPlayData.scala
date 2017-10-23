package org.nomadblacky.otogameupdater.game.cbrev.model

import org.nomadblacky.otogameupdater.game.cbrev.model.Difficulty.Difficulty

case class MusicPlayData(
  musicInList: MusicDetail,
  playScores: Map[Difficulty, PlayScore]
)
