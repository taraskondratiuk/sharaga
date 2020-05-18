import scala.util.Random

class Cpu(mainQueue: CpuQueue,
          secondaryQueue: CpuQueue,
          baseProcessingTime: Int,
          name: String) extends Thread(name) {
  lazy private val r = Random
  
  override def run(): Unit = {
    while (true) {
      val processingTime = r.between(10, 50) + baseProcessingTime
      val res = mainQueue.pop(secondaryQueue)
      println(s"cpu ${name} processing ${res}")
      Thread.sleep(processingTime)
      println(s"cpu ${name} processed ${res} in time ${processingTime}")
    }
    
  }
}

object Cpu {
  def apply(mainQueue: CpuQueue,
            secondaryQueue: CpuQueue,
            baseProcessingTime: Int,
            name: String): Cpu = new Cpu(mainQueue, secondaryQueue, baseProcessingTime, name)
}
