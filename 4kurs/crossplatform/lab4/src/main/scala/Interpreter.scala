import scala.language.postfixOps
import sys.process._

object Interpreter extends App with Helpers {
  val inputPath = args(0)
  
  val isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows")
  
  val windowsCommands = Map(
    "A" -> "copy nul",
    "B" -> "mkdir",
    "C" -> "dir",
    "D" -> "ping 127.0.0.1 > NUL -n",
    "E" -> "echo"
  )
  
  val linuxCommands = Map(
    "A" -> "touch",
    "B" -> "mkdir",
    "C" -> "ls",
    "D" -> "sleep",
    "E" -> "echo"
  )
  
  readFileCommands(inputPath).foreach { c =>
    val (command, args) = c.split(" ").splitAt(1)
    val resCommand = if (isWindows) {
      s"cmd.exe /c ${windowsCommands(command.head)} ${args.mkString(" ")}"
    } else {
      s"""sh -c "${linuxCommands(command.head)} ${args.mkString(" ")}" """
    }
    println(resCommand)
    resCommand !
  }
}
