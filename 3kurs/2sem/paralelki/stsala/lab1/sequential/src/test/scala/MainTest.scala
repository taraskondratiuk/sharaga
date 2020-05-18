import org.scalatest.FunSuite

class MainTest extends FunSuite {
  test("dim 100") {
    Main.main(Array("100"))
  }
  
  test("dim 1000") {
    Main.main(Array("1000"))
  }

  test("dim 5000") {
    Main.main(Array("5000"))
  }
  
  test("dim 10000") {
    Main.main(Array("10000"))
  }
}
