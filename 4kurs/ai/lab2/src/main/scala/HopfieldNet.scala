import breeze.linalg.{DenseMatrix, Transpose}

import scala.annotation.tailrec

object HopfieldNet {
  case class Picture(pixels: Array[Int])
  val matrixSize = 100
  val numOfSavedPictures = 3
  var savedPictures = List.empty[DenseMatrix[Int]]
  val W = DenseMatrix.zeros[Int](matrixSize, matrixSize)
  
  def savePicture(pic: Picture): Unit = {
    val inputVect = new DenseMatrix(1, matrixSize, pic.pixels)
    if (savedPictures.size == numOfSavedPictures) {
      removePicture(savedPictures.head)
      savedPictures = savedPictures.tail
    }
    savedPictures = savedPictures :+ inputVect
    addPicture(inputVect)
  }
  
  def recognizePicture(pic: Picture): Option[Picture] = {
    val inputVect = new DenseMatrix(matrixSize, 1, pic.pixels)
    
    @tailrec
    def recognizeIter(input: DenseMatrix[Int], iterNum: Int = 0): Option[DenseMatrix[Int]] = {
      val resVect = signum(W * input)
      val resPics = savedPictures.collect {
        case mtx if mtx.data sameElements resVect.data => mtx
      }
      if (iterNum == 100) None
      else if (resPics.isEmpty) recognizeIter(resVect, iterNum + 1)
      else Some(resPics.head)
    }
    
    recognizeIter(inputVect).map(m => Picture(m.data))
  }
  
  private def signum(matrix: DenseMatrix[Int]): DenseMatrix[Int] = {
    matrix.map(v => if (v < 0) -1 else 1)
  }
  
  private def addPicture(vect: DenseMatrix[Int]): Unit = {
    W += vect.t * vect
    for (i <- 0 until matrixSize) W(i, i) = 0
  }
  
  private def removePicture(vect: DenseMatrix[Int]): Unit = {
    W -= vect.t * vect
    for (i <- 0 until matrixSize) W(i, i) = 0
  }
}
