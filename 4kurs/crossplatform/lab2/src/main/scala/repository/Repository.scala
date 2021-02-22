package main.scala.repository

import main.scala.model.Id

trait Repository[T <: Id[T]] {
  def save(obj: T): T
  
  def getById(id: Int): Option[T]
  
  def getAll(): Seq[T]
  
  def removeById(id: Int): Option[T]
}
