package main.scala

import main.scala.controller.{NotebookController, NotebookConsoleController, NotebookGuiController}
import main.scala.model.Note
import main.scala.repository.InMemoryRepository

import scala.io.StdIn

object NotebookApp extends App {
  val repo = new InMemoryRepository[Note]
  val controller: NotebookController = {
    println("Choose app mode: \n[1] console \n[2] gui")
    StdIn.readInt() match {
      case 1 => new NotebookConsoleController(repo)
      case 2 => new NotebookGuiController(repo)
      case _ => throw new Exception("invalid app mode, shutting down")
    }
  }
  
  controller.start()
}
