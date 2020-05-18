import scala.util.Random

class CpuProcess(queue: CpuQueue,
                 generateNumber: Int,
                 baseDelay: Int,
                 name: String) extends Thread(name) {
  lazy private val r = Random

  override def run(): Unit = {
    for (_ <- 0 until generateNumber) {
      val delay = r.between(10, 50) + baseDelay
      Thread.sleep(delay)
      val process = r.alphanumeric.take(5).mkString("")
      println(s"process ${process} generated with delay ${delay}")
      queue.push(process)
    }
    
    println(s"no more processes for queue ${name}, greatest queue size was ${queue.maxSize}")
  }
}

object CpuProcess {
  def apply(queue: CpuQueue,
            generateNumber: Int,
            baseDelay: Int,
            name: String): CpuProcess = new CpuProcess(queue, generateNumber, baseDelay, name)
}
