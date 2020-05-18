import java.util.concurrent.atomic.{AtomicInteger, AtomicLong}

import scala.collection.parallel.CollectionConverters._
import scala.util.Random

object Main {
  def main(args: Array[String]): Unit = {
    val r = Random
    
    val arr = Array.fill(15_000_000)(r.nextInt())
    val parArr = arr.par
    
    var lenSeq = new AtomicInteger(0)
    
    var startTime = System.currentTimeMillis()
    arr.foreach(_ => lenSeq.incrementAndGet())
    var endTime = System.currentTimeMillis()
    println("sequential")
    println(s" count: ${lenSeq}")
    println(s"seconds elapsed: ${(endTime - startTime.toDouble) / 1000}\n\n")
    
    val lenPar = new AtomicInteger(0)
    
    startTime = System.currentTimeMillis()
    parArr.foreach(_ => lenPar.incrementAndGet())
    endTime = System.currentTimeMillis()
    println("parallel")
    println(s" count: ${lenPar}")
    println(s"seconds elapsed: ${(endTime - startTime.toDouble) / 1000}\n\n")
    
    
    startTime = System.currentTimeMillis()
    val xorSeq = arr.reduce(_ ^ _)
    endTime = System.currentTimeMillis()
    println("sequential")
    println(s" xor: ${xorSeq}")
    println(s"seconds elapsed: ${(endTime - startTime.toDouble) / 1000}\n\n")
    
    startTime = System.currentTimeMillis()
    val xorPar = parArr.reduce(_ ^ _)
    endTime = System.currentTimeMillis()
    println("parallel reduce")
    println(s" xor: ${xorPar}")
    println(s"seconds elapsed: ${(endTime - startTime.toDouble) / 1000}\n\n")
    
    val xorAtomic = new AtomicInteger(0)
    startTime = System.currentTimeMillis()
    parArr.foreach { v =>
      var expected = 0
      var update = 0
      do {
        expected = xorAtomic.get()
        update = expected ^ v
      }
      while (!xorAtomic.compareAndSet(expected, update))
    }
    endTime = System.currentTimeMillis()
    println("parallel atomic")
    println(s" xor: ${xorAtomic}")
    println(s"seconds elapsed: ${(endTime - startTime.toDouble) / 1000}\n\n")
    
    
    val arrLong = Array.fill(10_000_000)(r.nextLong())
    val parArrLong = arrLong.par
    
    startTime = System.currentTimeMillis()
    val minSeq = arrLong.min
    val maxSeq = arrLong.max
    val indexOfMinSeq = arrLong.indexOf(minSeq)
    val indexOfMaxSeq = arrLong.indexOf(maxSeq)
    endTime = System.currentTimeMillis()
    println("sequential")
    println(s" min: ${minSeq} \n max: ${maxSeq} \n min index: ${indexOfMinSeq} \n max index: ${indexOfMaxSeq}")
    println(s"seconds elapsed: ${(endTime - startTime.toDouble) / 1000}\n\n")
    
    startTime = System.currentTimeMillis()
    val minPar = parArrLong.min
    val maxPar = parArrLong.max
    val indexOfMinPar = parArrLong.indexOf(minPar)
    val indexOfMaxPar = parArrLong.indexOf(maxPar)
    endTime = System.currentTimeMillis()
    println("parallel method")
    println(s" min: ${minPar} \n max: ${maxPar} \n min index: ${indexOfMinPar} \n max index: ${indexOfMaxPar}")
    println(s"seconds elapsed: ${(endTime - startTime.toDouble) / 1000}\n\n")
    
    val minAtomic = new AtomicLong(Long.MaxValue)
    val maxAtomic = new AtomicLong(Long.MinValue)
    val minIndexAtomic = new AtomicInteger(-1)
    val maxIndexAtomic = new AtomicInteger(-1)
    startTime = System.currentTimeMillis()
    parArrLong.zipWithIndex.foreach { v: (Long, Int) =>
      if (v._1 > maxAtomic.get()) {
        var expected = 0L
        var update = 0L
        do {
          expected = maxAtomic.get()
          update = v._1
        } while (!maxAtomic.compareAndSet(expected, update))
        
        do {
          expected = maxIndexAtomic.get()
          update = v._2
        } while (!maxIndexAtomic.compareAndSet(expected.toInt, update.toInt))
      }
      
      if (v._1 < minAtomic.get()) {
        var expected = 0L
        var update = 0L
        do {
          expected = minAtomic.get()
          update = v._1
        } while (!minAtomic.compareAndSet(expected, update))
        
        do {
          expected = minIndexAtomic.get()
          update = v._2
        } while (!minIndexAtomic.compareAndSet(expected.toInt, update.toInt))
      }
    }
    endTime = System.currentTimeMillis()
    println("parallel atomic")
    println(s" min: ${minAtomic.get()} \n max: ${maxAtomic.get()} \n min index: ${minIndexAtomic.get()} \n max index: ${maxIndexAtomic.get()}")
    println(s"seconds elapsed: ${(endTime - startTime.toDouble) / 1000}\n\n")
    
  }
}
