package main.scala.model

import javafx.beans.property.{SimpleIntegerProperty, SimpleStringProperty}

import scala.util.Random

case class Note(text: String, id: Option[Int] = None) extends Id[Note] {
  override def withId(id: Int): Note = this.copy(id = Some(id))
  
  override def toString: String = s"""[${id.getOrElse("error,no id")}] $text"""
  
  val guiId = new SimpleStringProperty(id.getOrElse(0).toString)
  val guiText = new SimpleStringProperty(text)
}


case class GuiNote(id: SimpleIntegerProperty, text: SimpleStringProperty)

object GuiNote {
  def apply(n: Note): GuiNote = GuiNote(new SimpleIntegerProperty(n.id.getOrElse(0)), new SimpleStringProperty(n.text))
}