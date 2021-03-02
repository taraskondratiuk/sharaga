import scala.io.Source

trait Helpers {
  def readFileCommands(path: String): Seq[String] = {
    val srcFile = path
    Source.fromFile(srcFile).getLines().map(_.trim).toSeq
  }
  
  
  def writeFile(path: String, text: Seq[String]): Unit = {
    import java.io._
    val pw = new PrintWriter(new File(path))
    pw.write(text.mkString("\n"))
    pw.close
  }
}
