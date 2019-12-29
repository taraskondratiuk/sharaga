import javafx.util.Pair;

import java.util.*;
import java.util.stream.*;


public class ScannerApp {

    public static void main(String[] args) throws Exception {
        List<Integer> fullBinaryCode = PictureReader.getFullBinaryCode("V14s.bmp");

        List<List<Integer>> choppedFullBinaryCode = chopList(fullBinaryCode, 95, fullBinaryCode.size(), 0, new ArrayList<>());

        List<Integer> ean13BinaryCodeFull = choppedFullBinaryCode.stream().map(ScannerApp::convertIntoColor).collect(Collectors.toList());

        List<Integer> ean13BinaryCodeWithoutDelimiters = getCodeWithoutDelimiters(ean13BinaryCodeFull);

        List<List<Integer>> ean13Listed = chopList(ean13BinaryCodeWithoutDelimiters, 12, ean13BinaryCodeWithoutDelimiters.size(), 0, new ArrayList<>());

        List<Integer> finalCode = getFinalCode(ean13Listed);

        checkFinalCode(finalCode);
        finalCode.forEach(System.out::print);
    }

    private static List<Integer> getFinalCode(List<List<Integer>> ean13Listed) {
        List<Integer> finalCode = new ArrayList<>();
        finalCode.add(getFirstDigit(ean13Listed));
        finalCode.addAll(get12Numbers(ean13Listed));
        return finalCode;
    }

    private static List<Integer> getCodeWithoutDelimiters(List<Integer> ean13BinaryCodeFull) {
        List<Integer> res = new ArrayList<>();
        res.addAll(ean13BinaryCodeFull.subList(3, 45));
        res.addAll(ean13BinaryCodeFull.subList(50, 92));
        return res;
    }

    private static void checkFinalCode(List<Integer> finalCode) throws Exception {
        int sum = 0;
        for (int i = 0; i < finalCode.size() - 1; i++) {
            if (i % 2 == 0) {
                sum += finalCode.get(i);
            } else {
                sum += finalCode.get(i) * 3;
            }
        }
        int checkingVal = 10 - sum % 10;
        if (checkingVal != finalCode.get(finalCode.size() - 1)) {
            throw new Exception("Wrong code");
        }
    }

    private static List<Integer> get12Numbers(List<List<Integer>> allNumbersListed) {
        List<Integer> numbers = new ArrayList<>();

        List<List<Integer>> leftNumbersListed = allNumbersListed.subList(0, 6);
        List<List<Integer>> rightNumbersListed = allNumbersListed.subList(6, 12);

        leftNumbersListed.forEach(v -> numbers.add(Ean13Helper.getLeftNumber(v)));
        rightNumbersListed.forEach(v -> numbers.add(Ean13Helper.getRightNumber(v)));

        return numbers;
    }

    private static Integer getFirstDigit(List<List<Integer>> numbersListed) {
        List<List<Integer>> leftNumbersListed = numbersListed.subList(0, 6);
        List<Long> letters = new ArrayList<>();

        for (List<Integer> integers : leftNumbersListed) {
            long lightCounter = integers.stream().filter(v -> v % 2 == 0).count();

            letters.add(lightCounter % 2);
        }

        return Ean13Helper.getFirstNumber(letters);
    }

    private static Integer convertIntoColor(List<Integer> singleNumberPixelsList) {
        int darkCounter = 0, lightCounter = 0;
        for (Integer integer : singleNumberPixelsList) {
            if (integer.equals(AppConstants.DARK_COLOR)) {
                darkCounter++;
            } else {
                lightCounter++;
            }
        }

        return darkCounter > lightCounter ? AppConstants.DARK_COLOR : AppConstants.LIGHT_COLOR;
    }


    private static <T> List<List<T>> chopList(
            List<T> list,
            Integer pieces,
            Integer length,
            Integer iter,
            List<List<T>> result
    ) {
        if (list.isEmpty()) {
            return result;
        } else {
            int splitIndex = length * (iter + 1) / pieces - length * iter / pieces;
            Pair<List<T>, List<T>> ls = new Pair<>(list.subList(0, splitIndex),
                    list.subList(splitIndex, list.size()));
            result.add(ls.getKey());
            return chopList(ls.getValue(), pieces, length, iter + 1, result);
        }
    }
}
