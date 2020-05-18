import java.util.*;
import java.util.concurrent.*;
import java.util.stream.*;

public class Main {
    public static void main(String[] args) {
        final List<Integer> a1 = Arrays.asList(0, 9, 6, 2, 5, 1, 7, 4, 3, 8); //first
        final List<Integer> a2 = Arrays.asList(8, 7, 0, 1, 3, 6, 9, 2, 5, 4); //second
        CompletableFuture<List<Integer>> firstFuture, secondFuture, resultFuture;
        firstFuture = CompletableFuture.supplyAsync(() -> a1).thenApplyAsync(
                first -> {
                    double max07 = first.stream().max(Integer::compareTo).get() * 0.7;
                    first = first.stream().filter(v -> v > max07).collect(Collectors.toList());
                    Collections.sort(first);
                    return first;
                });
        secondFuture = CompletableFuture.supplyAsync(() -> a2).thenApplyAsync(
                second -> {
                    second = second.stream().filter(v -> v % 3 == 0).collect(Collectors.toList());
                    Collections.sort(second);
                    return second;
                });
        resultFuture = firstFuture.thenCombine(secondFuture, (first, second) -> {
            List<Integer> a3 = new ArrayList<>(first);
            a3.addAll(second);
            Collections.sort(a3);
            return a3;
        });
        try {
            System.out.println("Result: " + resultFuture.get()); /*blocks until
future completes*/
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
