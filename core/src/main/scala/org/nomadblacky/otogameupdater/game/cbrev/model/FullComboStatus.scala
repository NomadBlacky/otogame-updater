package org.nomadblacky.otogameupdater.game.cbrev.model

sealed abstract class FullComboStatus(val isFullCombo: Boolean) extends Comparable[FullComboStatus] {
  override def compareTo(that: FullComboStatus): Int = FullComboStatus.ordering.compare(this, that)
}

object FullComboStatus {

  object NotFullCombo extends FullComboStatus(false)
  object FullCombo    extends FullComboStatus(true)

  implicit val ordering: Ordering[FullComboStatus] = Ordering.by(_.isFullCombo)

  val values = Set(NotFullCombo, FullCombo)

  def fromBoolean(b: Boolean): FullComboStatus = if (b) FullCombo else NotFullCombo
}