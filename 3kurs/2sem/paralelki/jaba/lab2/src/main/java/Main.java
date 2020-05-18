public class Main {
    
    public static void main(String[] args) {
        int queueCapacity = 3;
        int processesToGenerate = 15;
        int numCpus = 3;
        System.out.println("\nQueue capacity = [" + queueCapacity + "], will be generated " + processesToGenerate + " processes\n");
        
        Cpu[] cpus = new Cpu[numCpus];
        CpuQueue[] queues = new CpuQueue[numCpus];
        
        for (int i = 0; i < numCpus; i++) {
            queues[i] = new CpuQueue(queueCapacity, i);
            cpus[i] = new Cpu(queues[i], i);
        }
        
        CpuProcess process = new CpuProcess(queues, processesToGenerate);
        new Thread(process).start();
        for (int i = 0; i < numCpus; i++) {
            new Thread(cpus[i]).start();
        }
    }
}
