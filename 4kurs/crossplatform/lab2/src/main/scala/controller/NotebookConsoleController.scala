package main.scala.controller

import main.scala.model.Note
import main.scala.repository.Repository

import scala.io.StdIn

class NotebookConsoleController(repo: Repository[Note]) extends NotebookController {
  
  override def start(): Unit = {

    while(true) {
      printAllNotes()
      println("Choose action \n[1] add note \n[2] delete note \n[q] quit")
      StdIn.readLine() match {
        case "1" =>
          println("print note")
          val newNote = Note(StdIn.readLine())
          repo.save(newNote)
        case "2" =>
          println("choose id to delete")
          val idToDelete = StdIn.readInt()
          repo.removeById(idToDelete)
        case "q" => System.exit(0)
        case  _  => println("wrong input")
      }
    }
  }
  
  def printAllNotes(): Unit = println(repo.getAll().mkString("\n"))
}
