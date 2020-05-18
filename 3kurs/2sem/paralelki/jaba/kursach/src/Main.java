import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.*;

public class Main {
    static int NUM_THREADS;
    
    public static void main(String[] args) {
        NUM_THREADS = Integer.parseInt(args[2]);
        
        Map<String, Set<String>> vocab = new ConcurrentHashMap<>();
        try {
            File vocabFile = new File(args[0]);
            Scanner vocabScanner = new Scanner(vocabFile);
            while (vocabScanner.hasNextLine()) {
                vocab.put(vocabScanner.nextLine(), new HashSet<>());
            }
            vocabScanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        File dataFolder = new File(args[1]);
        
        long start = System.currentTimeMillis();
        buildIndex(dataFolder, vocab);
        long end = System.currentTimeMillis();
        
        System.out.println("Duration: " + (double) (end - start) / 1000 + " seconds");
//        vocab.forEach((k, v) -> System.out.println(k + v));
        
    }
    
    public static void buildIndex(File dataFolder, Map<String, Set<String>> vocab) {
        List<File> allFiles = new ArrayList<>();
        fillListWithFilesFromFolder(dataFolder, allFiles);
        
        Thread[] threads = new Thread[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            threads[i] = new IndexBuilder(NUM_THREADS, i, allFiles, vocab);
            threads[i].start();
        }
        for (int i = 0; i < NUM_THREADS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    
    private static void fillListWithFilesFromFolder(File dataFolder, List<File> allFiles) {
        for (File f : dataFolder.listFiles()) {
            if (f.isDirectory()) {
                fillListWithFilesFromFolder(f, allFiles);
            } else {
                allFiles.add(f);
            }
        }
    }
}
