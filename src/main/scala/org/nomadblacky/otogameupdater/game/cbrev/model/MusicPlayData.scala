package org.nomadblacky.otogameupdater.game.cbrev.model

case class MusicPlayData(
  musicDetail: MusicDetail,
  playScores: Map[Difficulty, PlayScore]
)
