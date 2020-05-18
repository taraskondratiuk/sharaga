public class Cpu implements Runnable {
    
    CpuQueue queue;
    int index;
    
    Cpu(CpuQueue q, int index) {
        this.queue = q;
        this.index = index;
    }
    
    public void run() {
        long processingTime;
        while (true) {
            int randMin = 20;
            int randMax = 80; // rand = [20,100]
            processingTime = randMin + (int) (Math.random() * randMax);
            int res = -1;
            try {
                res = queue.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("CPU " + index + " processed " + res + " in time " + processingTime + "\n");
            try {
                Thread.sleep(processingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
