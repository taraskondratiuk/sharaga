import org.scalatest.FunSuite

class MainTest extends FunSuite {
  test("dim 100 8 threads") {
    Main.main(Array("100", "8"))
  }
  
  test("dim 1000 8 threads") {
    Main.main(Array("1000", "8"))
  }
  
  test("dim 5000 8 threads") {
    Main.main(Array("5000", "8"))
  }
  
  test("dim 10000 1 threads") {
    Main.main(Array("10000", "1"))
  }

  test("dim 10000 2 threads") {
    Main.main(Array("10000", "2"))
  }

  test("dim 10000 3 threads") {
    Main.main(Array("10000", "3"))
  }

  test("dim 10000 4 threads") {
    Main.main(Array("10000", "4"))
  }

  test("dim 10000 6 threads") {
    Main.main(Array("10000", "6"))
  }

  test("dim 10000 8 threads") {
    Main.main(Array("10000", "8"))
  }
  
  test("dim 10000 10 threads") {
    Main.main(Array("10000", "10"))
  }

  test("dim 10000 1000 threads") {
    Main.main(Array("10000", "1000"))
  }

  test("dim 10000 10000 threads") {
    Main.main(Array("10000", "10000"))
  }
}