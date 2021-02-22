package main.scala.model

trait Id[T] {
  def withId(id: Int): T
}