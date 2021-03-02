import org.scalatest.flatspec.AnyFlatSpec

class VMTest extends AnyFlatSpec{
  "compiler" should "turn commands into bytecode" in {
    Compiler.main(Array("src/test/resources/test_commands.source", "src/test/resources/test_commands.bytecode"))
  }
  
  "interpreter" should "run bytecode" in {
    Interpreter.main(Array("src/test/resources/test_commands.bytecode"))
  }
}
