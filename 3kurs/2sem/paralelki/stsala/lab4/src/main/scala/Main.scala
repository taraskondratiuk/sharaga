import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

object Main {
  val ARRAY_LEN = 30
  
  def main(args: Array[String]): Unit = {
    val r = Random
    val arr1 = Array.fill(ARRAY_LEN)(r.nextInt(10) + 1)
    val arr2 = Array.fill(ARRAY_LEN)(r.nextInt(10) + 1)
    println(s"first arr: ${arr1.mkString(" ")}")
    println(s"second arr: ${arr2.mkString(" ")}")
    
    val futureRes1 = getSortedValsMoreThan60PercentOfAvg(arr1)
    val futureRes2 = getSortedEvenVals(arr2)
    val filteredArrays = Future.sequence(ArrayBuffer[Future[Array[Int]]](futureRes1, futureRes2))
    
    val intersectedArrays = ArrayBuffer[Int]()
    filteredArrays.foreach { arrs =>
      println(s"first arr filtered: ${arrs(0).mkString(" ")}")
      println(s"second arr filtered: ${arrs(1).mkString(" ")}")
      intersectedArrays ++= arrs(0) intersect arrs(1)
    }
    
    println(s"intersected arrs at start: ${intersectedArrays.toArray.mkString(" ")}")
    Thread.sleep(1000)
    println(s"intersected arrs after pause: ${intersectedArrays.toArray.mkString(" ")}")
  }

  def getSortedValsMoreThan60PercentOfAvg(arr: Array[Int]): Future[Array[Int]] = {
    Future(arr.filter(_ > arr.sum.toDouble * 0.6 / arr.length).sorted)
  }
  
  def getSortedEvenVals(arr: Array[Int]): Future[Array[Int]] = {
    Future(arr.filter(_ % 2 == 0).sorted)
  }
}
