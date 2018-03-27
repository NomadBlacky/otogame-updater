package org.nomadblacky.otogameupdater.game.cbrev.model

sealed trait Diff[T] {
  val before: T
  val after: T
  def diffString: String
}

case class Upper[T](before: T, after: T) extends Diff[T] {
  override def diffString: String = s"$before ↗ $after"
  def unapply(arg: Upper[T]): Option[(T, T)] = ???
}

case class Lower[T](before: T, after: T) extends Diff[T] {
  override def diffString: String = s"$before ↘ $after"
  def unapply(arg: Lower[T]): Option[(T, T)] = ???
}
