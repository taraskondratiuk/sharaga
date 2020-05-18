import scala.collection.mutable

class CpuQueue(val capacity: Int, val threadName: String) {
  private val queue = new mutable.Queue[String]()
  var maxSize = 0
  
  def push(process: String): Unit = this.synchronized {
    while (queue.size == capacity) {
      println(s"queue ${threadName} is full, waiting")
      wait()
    }
    queue += process
    if (maxSize < queue.size) maxSize = queue.size
    
    println(s"process ${process} added")
    println(s"queue ${threadName} : ${queue}")
    notify()
  }
  
  def pop(secondQueue: CpuQueue, secondaryChecked: Boolean = false): String = {
    var checked = secondaryChecked
    this.synchronized {
      if (secondaryChecked && isEmpty) {
        checked = false
        wait()
      }
      if (!isEmpty) {
        val res = queue.dequeue()
        notify()
        return res
      }
    }
    
    println(s"queue ${threadName} is empty, checking secondary")
    
    secondQueue.synchronized {
      if (!secondQueue.isEmpty) {
        println(s"cpu ${threadName} getting element from secondary queue")
        val res = secondQueue.queue.dequeue()
        secondQueue.notify()
        return res
      }
    }
    checked = true
    println(s"both queues empty, waiting")
    pop(secondQueue, checked)
  }
  
  def isEmpty: Boolean = {
    queue.isEmpty
  }
}

object CpuQueue {
  def apply(capacity: Int, threadName: String): CpuQueue = new CpuQueue(capacity, threadName)
}
