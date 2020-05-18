import java.util.*;

public class CpuQueue {
    
    private Queue<Integer> queue = new LinkedList<>();
    private int capacity;
    private int index;
    private int maxSize = 0;
    
    public CpuQueue(int capacity, int index) {
        this.capacity = capacity;
        this.index = index;
    }
    
    public int getMaxSize() {
        return maxSize;
    }
    
    public synchronized int get() throws InterruptedException {
        while (queue.isEmpty()) {
            System.out.println("Queue is EMPTY, waiting..");
            this.wait();
        }
        int item = queue.remove();
        System.out.println("Process removed, queue size = [" + queue.size() + "]");
        return item;
    }
    
    public synchronized boolean put(int element) {
        if (this.queue.size() == capacity) {
            System.out.println("Queue " + index + " is FULL");
            return false;
        }
        this.queue.add(element);
        
        if (this.queue.size() > maxSize) {
            maxSize = this.queue.size();
        }
        
        System.out.println("Process added, queue [" + index + "] size = [" + this.queue.size() + "]\n");
        this.notify();
        return true;
    }
}
