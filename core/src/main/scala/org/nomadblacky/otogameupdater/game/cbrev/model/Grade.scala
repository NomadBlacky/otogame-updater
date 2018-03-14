package org.nomadblacky.otogameupdater.game.cbrev.model

/**
  * Created by blacky on 17/07/19.
  */
sealed abstract class Grade(
  val order: Int,
  val id: String,
  val displayName: String
) extends Ordered[Grade] {
  override def compare(that: Grade): Int = Grade.ordering.compare(this, that)
}

object Grade {
  implicit val ordering: Ordering[Grade] = Ordering.by(_.order)
}

object Grades {

  object GradeSpp extends Grade(10, "0", "S++")

  object GradeSp extends Grade(9, "1", "S+")

  object GradeS extends Grade(8, "2", "S")

  object GradeAp extends Grade(7, "3", "A+")

  object GradeA extends Grade(6, "4", "A")

  object GradeBp extends Grade(5, "5", "B+")

  object GradeB extends Grade(4, "6", "B")

  object GradeC extends Grade(3, "7", "C")

  object GradeD extends Grade(2, "8", "D")

  object GradeE extends Grade(1, "9", "E")

  object GradeF extends Grade(0, "10", "F")

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