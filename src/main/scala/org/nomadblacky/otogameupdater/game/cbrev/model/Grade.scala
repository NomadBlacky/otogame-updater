package org.nomadblacky.otogameupdater.game.cbrev.model

import scala.collection.SortedSet

/**
  * Created by blacky on 17/07/19.
  */
sealed abstract class Grade(val id: String, val displayName: String)

object Grades {

  object GradeSpp extends Grade("0", "S++")

  object GradeSp extends Grade("1", "S+")

  object GradeS extends Grade("2", "S")

  object GradeAp extends Grade("3", "A+")

  object GradeA extends Grade("4", "A")

  object GradeBp extends Grade("5", "B+")

  object GradeB extends Grade("6", "B")

  object GradeC extends Grade("7", "C")

  object GradeD extends Grade("8", "D")

  object GradeE extends Grade("9", "E")

  object GradeF extends Grade("10", "F")

  val values = Set(
    GradeSpp,
    GradeSp,
    GradeS,
    GradeAp,
    GradeA,
    GradeBp,
    GradeB,
    GradeC,
    GradeD,
    GradeE,
    GradeF
  )
}