import scala.util.Random

object Main {
//    val MATRIX_DIM = 25000
    
    def main(args: Array[String]): Unit = {
        val r = Random
    
        val matrix_dim = args(0).toInt
        val matrix = Array.fill(matrix_dim, matrix_dim)(r.nextLong(9) + 1)
    
        println("sequential")
        println(s"matrix dimension: ${matrix_dim}")
        printTime {
            for (col <- 0 until matrix_dim) {
                matrix(matrix_dim - col - 1)(col) = getColProduct(col, matrix)
            }
        }
    }
    
    def getColProduct(col: Int, matrix: Array[Array[Long]]): Long = {
        (for (row <- matrix.indices) yield matrix(row)(col)).product
    }
    
    def printTime[T](block: => T): T = {
        val startTime = System.currentTimeMillis()
        val res = block
        val endTime = System.currentTimeMillis()
        println(s"seconds elapsed: ${(endTime - startTime.toDouble) / 1000}")
        res
    }
}

