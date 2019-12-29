import java.util.*;

public class Ean13Helper {
    private static Map<List<Integer>, Integer> ean13LeftValues = new HashMap<>();
    private static Map<List<Integer>, Integer> ean13RightValues = new HashMap<>();
    private static Map<List<Long>, Integer> ean13FirstDigit = new HashMap<>();

    static {
        //a
        ean13LeftValues.put(Arrays.asList(0, 0, 0, 1, 1, 0, 1), 0);
        ean13LeftValues.put(Arrays.asList(0, 0, 1, 1, 0, 0, 1), 1);
        ean13LeftValues.put(Arrays.asList(0, 0, 1, 0, 0, 1, 1), 2);
        ean13LeftValues.put(Arrays.asList(0, 1, 1, 1, 1, 0, 1), 3);
        ean13LeftValues.put(Arrays.asList(0, 1, 0, 0, 0, 1, 1), 4);
        ean13LeftValues.put(Arrays.asList(0, 1, 1, 0, 0, 0, 1), 5);
        ean13LeftValues.put(Arrays.asList(0, 1, 0, 1, 1, 1, 1), 6);
        ean13LeftValues.put(Arrays.asList(0, 1, 1, 1, 0, 1, 1), 7);
        ean13LeftValues.put(Arrays.asList(0, 1, 1, 0, 1, 1, 1), 8);
        ean13LeftValues.put(Arrays.asList(0, 0, 0, 1, 0, 1, 1), 9);

        //b
        ean13LeftValues.put(Arrays.asList(0, 1, 0, 0, 1, 1, 1), 0);
        ean13LeftValues.put(Arrays.asList(0, 1, 1, 0, 0, 1, 1), 1);
        ean13LeftValues.put(Arrays.asList(0, 0, 1, 1, 0, 1, 1), 2);
        ean13LeftValues.put(Arrays.asList(0, 1, 0, 0, 0, 0, 1), 3);
        ean13LeftValues.put(Arrays.asList(0, 0, 1, 1, 1, 0, 1), 4);
        ean13LeftValues.put(Arrays.asList(0, 1, 1, 1, 0, 0, 1), 5);
        ean13LeftValues.put(Arrays.asList(0, 0, 0, 0, 1, 0, 1), 6);
        ean13LeftValues.put(Arrays.asList(0, 0, 1, 0, 0, 0, 1), 7);
        ean13LeftValues.put(Arrays.asList(0, 0, 0, 1, 0, 0, 1), 8);
        ean13LeftValues.put(Arrays.asList(0, 0, 1, 0, 1, 1, 1), 9);

        //с
        ean13RightValues.put(Arrays.asList(1, 1, 1, 0, 0, 1, 0), 0);
        ean13RightValues.put(Arrays.asList(1, 1, 0, 0, 1, 1, 0), 1);
        ean13RightValues.put(Arrays.asList(1, 1, 0, 1, 1, 0, 0), 2);
        ean13RightValues.put(Arrays.asList(1, 0, 0, 0, 0, 1, 0), 3);
        ean13RightValues.put(Arrays.asList(1, 0, 1, 1, 1, 0, 0), 4);
        ean13RightValues.put(Arrays.asList(1, 0, 0, 1, 1, 1, 0), 5);
        ean13RightValues.put(Arrays.asList(1, 0, 1, 0, 0, 0, 0), 6);
        ean13RightValues.put(Arrays.asList(1, 0, 0, 0, 1, 0, 0), 7);
        ean13RightValues.put(Arrays.asList(1, 0, 0, 1, 0, 0, 0), 8);
        ean13RightValues.put(Arrays.asList(1, 1, 1, 0, 1, 0, 0), 9);

        ean13FirstDigit.put(Arrays.asList(0L, 0L, 0L, 0L, 0L, 0L), 0);
        ean13FirstDigit.put(Arrays.asList(0L, 0L, 1L, 0L, 1L, 1L), 1);
        ean13FirstDigit.put(Arrays.asList(0L, 0L, 1L, 1L, 0L, 1L), 2);
        ean13FirstDigit.put(Arrays.asList(0L, 0L, 1L, 1L, 1L, 0L), 3);
        ean13FirstDigit.put(Arrays.asList(0L, 1L, 0L, 0L, 1L, 1L), 4);
        ean13FirstDigit.put(Arrays.asList(0L, 1L, 1L, 0L, 0L, 1L), 5);
        ean13FirstDigit.put(Arrays.asList(0L, 1L, 1L, 1L, 0L, 0L), 6);
        ean13FirstDigit.put(Arrays.asList(0L, 1L, 0L, 1L, 0L, 1L), 7);
        ean13FirstDigit.put(Arrays.asList(0L, 1L, 0L, 1L, 1L, 0L), 8);
        ean13FirstDigit.put(Arrays.asList(0L, 1L, 1L, 0L, 1L, 0L), 9);
    }

    public static Integer getLeftNumber(List<Integer> list) {
        return ean13LeftValues.get(list);
    }

    public static Integer getFirstNumber(List<Long> list) {
        return ean13FirstDigit.get(list);
    }

    public static Integer getRightNumber(List<Integer> list) {
        return ean13RightValues.get(list);
    }
}
