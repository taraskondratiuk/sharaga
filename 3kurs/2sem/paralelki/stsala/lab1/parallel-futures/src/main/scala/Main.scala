import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future, Promise}
import scala.util.{Random, Success}

object Main {
  
  def main(args: Array[String]): Unit = {
    val r = Random
    val matrixDim = args(0).toInt
    val matrix = Array.fill(matrixDim, matrixDim)(r.nextLong(9) + 1)
  
    println("parallel-futures")
    println(s"matrix dimension: ${matrixDim}")
    printTimeWithPromise {
      val blockPromise = Promise[Unit]
      val startTime = System.currentTimeMillis()
      val seqOfProductFutures = for (col <- 0 until matrixDim) yield getColProduct(col, matrix)
      val futureOfProductsSeq = Future.sequence(seqOfProductFutures)
    
      futureOfProductsSeq.foreach { productsSeq: IndexedSeq[Long] =>
        for (col <- 0 until matrixDim) {
          matrix(matrixDim - col - 1)(col) = productsSeq(col)
        }
        blockPromise.complete(Success())
      }
      (startTime, blockPromise)
    }
  }
  
  def getColProduct(col: Int, matrix: Array[Array[Long]]): Future[Long] = Future {
    (for (row <- matrix.indices) yield matrix(row)(col)).product
  }
  
  def printTimeWithPromise(startTimeAndPromise: (Long, Promise[Unit])): Unit = {
    Await.result(startTimeAndPromise._2.future, Duration.Inf)
    val endTime = System.currentTimeMillis()
    println(s"seconds elapsed: ${(endTime - startTimeAndPromise._1.toDouble) / 1000}")
  }
}
