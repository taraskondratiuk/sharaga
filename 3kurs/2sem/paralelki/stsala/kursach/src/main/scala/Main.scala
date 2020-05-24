import java.io.File
import java.util.concurrent.ForkJoinPool

import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.ForkJoinTaskSupport
import scala.io.{Source, StdIn}

object Main {
  def main(args: Array[String]): Unit = {
    val totalVariants = args(0).toInt
    val variant = args(1).toInt
    val dataDir = new File(args(2))
    val files = getFilesListByVariant(dataDir, variant, totalVariants).toArray
    
    val forkJoinPool = new java.util.concurrent.ForkJoinPool(args(3).toInt)
    println(s"total num of files: ${files.length}")
    
    var index: Map[String, Set[String]] = Map.empty
    printTime {
      index = getInvertedIndex(files, forkJoinPool)
    }(s"time for indexing with ${args(3)} thread${if (args(3) != "1") "s" else ""}")
    forkJoinPool.shutdown()
    
    while (true) {
      println("enter word to look for:")
      val word = StdIn.readLine()
      if (word == "exit") return
      println(index.get(word))
    }
  }
  
  private def getInvertedIndex(files: Array[File], forkJoinPool: ForkJoinPool): Map[String, Set[String]] = {
    val parFiles = files.par
    parFiles.tasksupport = new ForkJoinTaskSupport(forkJoinPool)
    parFiles
      .flatMap { f =>
        val file = Source.fromFile(f)
        val fileContent = file.mkString
        file.close()
        fileContent.toLowerCase.split("[^a-z'0-9]+").map((_, f.getPath))
      }
      .groupBy(_._1)
      .mapValues(v => v.map(_._2).toSet.seq)
      .toMap //without toMap works several times slower
      .seq
  }
  
  private def getFilesListByVariant(f: File, variant: Int, totalVariants: Int, files: List[File] = List.empty): List[File] = {
    (if (f.isDirectory) {
      val size = f.listFiles().length
      val fromIndex = size / totalVariants * (variant - 1)
      val untilIndex = size / totalVariants * variant
      f.listFiles().toList
        .filter {
          v =>
            (v.isDirectory
              || {
              val reviewNum = """\\\d*(?!.*\\\d)""".r.findAllIn(v.getPath).mkString.drop(1).toInt
              fromIndex <= reviewNum && untilIndex > reviewNum
            })
        }
        .flatMap(getFilesListByVariant(_, variant, totalVariants, files))
    }
    else List(f)) ::: files
  }
  
  private def printTime(block: => Unit)(msg: String = ""): Unit = {
    val start = System.currentTimeMillis()
    block
    val end = System.currentTimeMillis()
    println(s"${msg}: ${(end - start).toDouble / 1000}s")
  }
}
