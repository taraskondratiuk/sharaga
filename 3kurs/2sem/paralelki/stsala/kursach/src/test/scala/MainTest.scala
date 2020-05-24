import org.scalatest.{BeforeAndAfter, FunSuite}

class MainTest extends FunSuite with BeforeAndAfter {
  
  before {
    System.gc()
  }
  
  test("2000 files 1 thread") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "1"))
  }
  
  test("2000 files 2 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "2"))
  }
  
  test("2000 files 4 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "4"))
  }
  
  test("2000 files 6 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "6"))
  }
  
  test("2000 files 8 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "8"))
  }
  
  test("2000 files 10 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "10"))
  }
  
  test("2000 files 12 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "12"))
  }
  
  test("2000 files 14 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "14"))
  }
  
  test("2000 files 16 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "16"))
  }
  
  test("2000 files 20 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "20"))
  }
  
  test("2000 files 30 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "30"))
  }
  
  test("2000 files 40 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "40"))
  }
  
  test("2000 files 50 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "50"))
  }
  
  test("2000 files 60 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "60"))
  }
  
  test("2000 files 70 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "70"))
  }
  
  test("2000 files 80 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "80"))
  }
  
  test("2000 files 90 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "90"))
  }
  
  test("2000 files 100 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "100"))
  }
  
  test("2000 files 120 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "120"))
  }
  
  test("2000 files 140 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "140"))
  }
  
  test("2000 files 160 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "160"))
  }
  
  test("2000 files 180 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "180"))
  }
  
  test("2000 files 200 threads") {
    Main.main(Array("50", "36", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "200"))
  }
  
  test("10000 files 1 thread") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "1"))
  }
  
  test("10000 files 2 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "2"))
  }
  
  test("10000 files 4 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "4"))
  }
  
  test("10000 files 6 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "6"))
  }
  
  test("10000 files 8 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "8"))
  }
  
  test("10000 files 10 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "10"))
  }
  
  test("10000 files 12 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "12"))
  }
  
  test("10000 files 14 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "14"))
  }
  
  test("10000 files 16 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "16"))
  }
  
  test("10000 files 20 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "20"))
  }
  
  test("10000 files 30 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "30"))
  }
  
  test("10000 files 40 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "40"))
  }
  
  test("10000 files 50 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "50"))
  }
  
  test("10000 files 60 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "60"))
  }
  
  test("10000 files 70 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "70"))
  }
  
  test("10000 files 80 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "80"))
  }
  
  test("10000 files 90 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "90"))
  }
  
  test("10000 files 100 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "100"))
  }
  
  test("10000 files 120 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "120"))
  }
  
  test("10000 files 140 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "140"))
  }
  
  test("10000 files 160 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "160"))
  }
  
  test("10000 files 180 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "180"))
  }
  
  test("10000 files 200 threads") {
    Main.main(Array("10", "6", "F:\\1PROGA\\sharaga\\3kurs\\kursach\\aclImdb\\", "200"))
  }
}