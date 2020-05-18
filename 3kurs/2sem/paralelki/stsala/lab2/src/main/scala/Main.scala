object Main {
  def main(args: Array[String]): Unit = {
    val queueCapacity1 = args(0).toInt
    val queueCapacity2 = args(1).toInt
    val numProcesses1 = args(2).toInt
    val numProcesses2 = args(3).toInt
    val baseDelay1 = args(4).toInt
    val baseDelay2 = args(5).toInt
    val baseProcessingTime1 = args(6).toInt
    val baseProcessingTime2 = args(7).toInt
    
    println(s"Queue 1 capacity = ${queueCapacity1}, ${numProcesses1} processes will be generated \n")
    println(s"Queue 2 capacity = ${queueCapacity2}, ${numProcesses2} processes will be generated \n")

    val cpuQueue1 = CpuQueue(queueCapacity1, "1")
    val cpuQueue2 = CpuQueue(queueCapacity2, "2")
    val cpuProcess1 = CpuProcess(cpuQueue1, numProcesses1, baseDelay1, "1")
    val cpuProcess2 = CpuProcess(cpuQueue2, numProcesses2, baseDelay2, "2")
    val cpu1 = Cpu(cpuQueue1, cpuQueue2, baseProcessingTime1, "1")
    val cpu2 = Cpu(cpuQueue2, cpuQueue1, baseProcessingTime2, "2")
    cpuProcess1.start()
    cpuProcess2.start()
    
    Thread.sleep(100)
    cpu1.start()
    cpu2.start()
    
    cpuProcess1.join()
    cpuProcess2.join()
    cpu1.join()
    cpu2.join()
  }
}
