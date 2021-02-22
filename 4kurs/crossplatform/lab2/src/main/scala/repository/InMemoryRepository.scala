package main.scala.repository

import main.scala.model.Id

class InMemoryRepository[T <: Id[T]] extends Repository[T] {
  private val repo = collection.mutable.LinkedHashMap.empty[Int, T]
  private var lastIndex = 0
  
  override def save(obj: T): T = {
    lastIndex += 1
    val resRecord = obj.withId(lastIndex)
    repo += ((lastIndex, resRecord))
    resRecord
  }
  
  override def getById(id: Int): Option[T] = {
    repo.get(id)
  }
  
  override def getAll(): Seq[T] = {
    repo.toSeq.map(_._2)
  }
  
  override def removeById(id: Int): Option[T] = {
    repo.remove(id)
  }
}
