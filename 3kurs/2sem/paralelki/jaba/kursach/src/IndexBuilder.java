import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class IndexBuilder extends Thread {
    int numThreads;
    int threadIndex;
    List<File> files;
    Map<String, Set<String>> vocab;
    int len;
    
    public IndexBuilder(int numThreads, int threadIndex, List<File> files, Map<String, Set<String>> vocab) {
        this.numThreads = numThreads;
        this.threadIndex = threadIndex;
        this.files = files;
        this.vocab = vocab;
        this.len = files.size();
    }
    
    private static void indexFile(File file, Map<String, Set<String>> vocab) {
        try {
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                String[] words = fileScanner
                        .nextLine()
                        .toLowerCase()
                        .split("[ ,.;:\"?!]+");
                
                for (String w : words) {
                    vocab.computeIfPresent(w, (word, docsSet) -> {
                        Set<String> docs = docsSet;
                        docs.add(file.getAbsolutePath());
                        return docs;
                    });
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void run() {
        for (int i = len / numThreads * threadIndex;
             i < (threadIndex == (numThreads - 1) ? len : len / numThreads * (threadIndex + 1));
             i++) {
            indexFile(files.get(i), vocab);
        }
    }
}
