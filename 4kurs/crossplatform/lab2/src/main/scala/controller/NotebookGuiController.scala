package main.scala.controller

import javafx.fxml.Initializable
import javafx.scene.{control => jfxsc}
import javafx.{event => jfxe, fxml => jfxf, scene => jfxs}
import main.scala.controller.util.ActionButtonTableCell
import main.scala.model.{GuiNote, Note}
import main.scala.repository.{InMemoryRepository, Repository}
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.{Button, TableColumn}
import scalafx.scene.control.TableColumn._

import java.net.URL
import java.util.ResourceBundle

class NotebookGuiController(repository: Repository[Note]) extends JFXApp with NotebookController with Initializable {
  val resource = this.getClass.getClassLoader.getResource("main/resources/NotebookGuiView.fxml")
  val root: jfxs.Parent = jfxf.FXMLLoader.load(resource)
  
  def this() = this(new InMemoryRepository[Note])
  
  stage = new PrimaryStage() {
    title = "FXML GridPane Demo"
    scene = new Scene(root)
  }
  
  override def initialize(url: URL, resourceBundle: ResourceBundle): Unit = {
    val actionCol = new TableColumn[Note, jfxsc.Button] {
      prefWidth = 100
    }
    
    actionCol.setCellFactory(
      ActionButtonTableCell.forTableColumn[Note]("remove", (v1: Note) => {
        notesTable.getItems.remove(v1)
        repository.removeById(v1.id.getOrElse(0))
        v1
      }))
      
    notesTable.getColumns.addAll(
      new TableColumn[Note, String] {
        text = "id"
        cellValueFactory = (_.value.guiId)
        prefWidth = 100
      },
      new TableColumn[Note, String] {
        text = "note"
        cellValueFactory = (_.value.guiText)
        prefWidth = 700
      },
      actionCol
    )
  }
  
  @jfxf.FXML
  private var notesTable: jfxsc.TableView[Note] = _
  @jfxf.FXML
  private var addBtn: jfxsc.Button = _
  @jfxf.FXML
  private var noteField: jfxsc.TextField = _
  
  @jfxf.FXML
  private def handleAddNoteClick(event: jfxe.ActionEvent) {
    val newNote = repository.save(Note(noteField.getText))
   
    notesTable.getItems.add(newNote)
  }
  
  override def start(): Unit = {
    main(Array.empty)

  }
}
