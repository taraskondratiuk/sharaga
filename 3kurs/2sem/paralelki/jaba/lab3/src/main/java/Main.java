import java.util.*;
import java.util.concurrent.atomic.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        Random r = new Random();
        int[] ints = new int[3_000_000];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = r.nextInt();
        }
        
        AtomicInteger countAtomic = new AtomicInteger(0);
        IntStream.of(ints).parallel().forEach(v -> countAtomic.incrementAndGet());
        
        System.out.println("atomic count: " + countAtomic);
        AtomicInteger xorAtomic = new AtomicInteger(0);
        
        IntStream.of(ints).parallel().forEach(element -> {
            int oldValue;
            int newValue;
            do {
                oldValue = xorAtomic.get();
                newValue = oldValue ^ element;
            } while (!xorAtomic.compareAndSet(oldValue, newValue));
        });
        System.out.println("atomic xor: " + xorAtomic.get());
        
        long[] longs = new long[3_000_000];
        for (int i = 0; i < longs.length; i++) {
            longs[i] = r.nextLong();
        }
        
        AtomicLong maxAtomic = new AtomicLong(Long.MIN_VALUE);
        AtomicLong minAtomic = new AtomicLong(Long.MAX_VALUE);
        
        LongStream.of(longs).parallel().forEach(element -> {
            long max = maxAtomic.get();
            while (element > max) {
                maxAtomic.compareAndSet(max, element);
                max = maxAtomic.get();
            }
            
            long min = minAtomic.get();
            while (element < min) {
                minAtomic.compareAndSet(min, element);
                min = minAtomic.get();
            }
        });
        
        System.out.println("max atomic: " + maxAtomic.get());
        System.out.println("min atomic: " + minAtomic.get());
        
        AtomicInteger minIndexAtomic = new AtomicInteger(-1);
        AtomicInteger maxIndexAtomic = new AtomicInteger(-1);
        
        IntStream.range(0, 3_000_000).parallel().forEach(index -> {
            if (longs[index] == maxAtomic.get()) {
                int oldValue;
                do {
                    oldValue = maxIndexAtomic.get();
                } while (!maxIndexAtomic.compareAndSet(oldValue, index));
            }
    
            if (longs[index] == minAtomic.get()) {
                int oldValue;
                do {
                    oldValue = minIndexAtomic.get();
                } while (!minIndexAtomic.compareAndSet(oldValue, index));
            }
        });
    
        System.out.println("index of min element atomic: " + minIndexAtomic.get());
        System.out.println("index of max element atomic: " + maxIndexAtomic.get());
    }
}
