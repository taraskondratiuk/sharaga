object Compiler extends App with Helpers {
  
  val (inputPath, outputPath) = (args(0), args(1))
  
  val commandBytecodeMap = Map(
    "createFile" -> "A",
    "createDirectory" -> "B",
    "listFiles" -> "C",
    "sleep" -> "D",
    "echo" -> "E"
  )
  
  val commands = readFileCommands(inputPath)
  
  val bytecode = commands.map { c =>
    val (command, args) = c.split(" ").splitAt(1)
    (commandBytecodeMap(command.head) +: args).mkString(" ")
  }
  
  writeFile(outputPath, bytecode)
}
