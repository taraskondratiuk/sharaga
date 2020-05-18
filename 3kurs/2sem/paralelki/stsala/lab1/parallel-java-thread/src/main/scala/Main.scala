import scala.util.Random

object Main {

  def main(args: Array[String]): Unit = {
    val r = Random
    val matrixDim = args(0).toInt
    val numThreads = args(1).toInt
    val matrix = Array.fill(matrixDim, matrixDim)(r.nextLong(9) + 1)

    println("parallel-java-threads")
    println(s"matrix dimension: ${matrixDim}")
    println(s"number of threads: ${numThreads}")
    printTime {
      val threads = new Array[ColProductCounter](numThreads)
      for (i <- 0 until numThreads) {
        threads(i) = new ColProductCounter(
          matrix,
          matrixDim / numThreads * i,
          if (i == (numThreads - 1)) matrixDim else matrixDim / numThreads * (i + 1)
        )
        threads(i).start()
      }
      threads.foreach(_.join())
    }
  }
  
  def printTime[T](block: => T): T = {
    val startTime = System.currentTimeMillis()
    val res = block
    val endTime = System.currentTimeMillis()
    println(s"seconds elapsed: ${(endTime - startTime.toDouble) / 1000}")
    res
  }
}
