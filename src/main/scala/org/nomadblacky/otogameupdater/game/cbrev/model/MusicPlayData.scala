package org.nomadblacky.otogameupdater.game.cbrev.model

case class MusicPlayData(
  musicInList: MusicDetail,
  playScores: Map[Difficulty, PlayScore]
)
