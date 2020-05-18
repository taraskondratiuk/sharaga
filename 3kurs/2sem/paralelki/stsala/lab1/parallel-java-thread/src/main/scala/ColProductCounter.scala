class ColProductCounter(val matrix: Array[Array[Long]], val leftIndex: Int, val rightIndex: Int) extends Thread {
  private val MATRIX_DIM = matrix.length
  
  override def run(): Unit = {
    for (col <- leftIndex until rightIndex) {
      matrix(MATRIX_DIM - col - 1)(col) = (for (row <- matrix.indices) yield matrix(row)(col)).product
    }
  }
}
