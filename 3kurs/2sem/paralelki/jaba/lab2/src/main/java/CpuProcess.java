public class CpuProcess implements Runnable {
    
    CpuQueue[] queues;
    int generateNumber;
    int maxCounter = 0;
    int[] totalCounts;

    CpuProcess(CpuQueue[] queues, int gN) {
        this.queues = queues;
        this.generateNumber = gN;
        this.totalCounts = new int[queues.length - 1];
    }
    
    public void run() {
        long generateDelay;
        for (int i = 0; i < generateNumber; i++) {
            int randMin = 10;
            int randMax = 40; // rand = [10,50]
            generateDelay = randMin + (int) (Math.random() * randMax);
            try {
                Thread.sleep(generateDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Process generated with delay " + generateDelay);
            int counter;
            for (counter = 0; counter < queues.length; counter++) {
                if (maxCounter < counter + 1) {
                    maxCounter = counter + 1;
                }
                if (queues[counter].put(i)) {
                    totalCounts[counter]++;
                    break;
                }
            }
            if (counter == queues.length) {
                System.out.println("All queues are full, process skipped");
            }
        }
        System.out.println("No more processes. Greatest last queue size was " + queues[queues.length - 1].getMaxSize());
        System.out.println("Total number of used cpus " + maxCounter);
        for (int i = 0; i < totalCounts.length; i++) {
            System.out.println("Percent for " + i + "cpu " + ((double) totalCounts[i] * 100) / generateNumber );
        }
    }
}
