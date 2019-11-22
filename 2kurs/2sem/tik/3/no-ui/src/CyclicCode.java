import java.util.ArrayList;
import java.util.List;

import static data.Data.POLYNOM;
import static data.Data.TOTAL_DIGIT_NUMBER;


abstract class CyclicCode {

    private static String appendZeros(String str, int numZeros) {
        StringBuilder stringBuilder = new StringBuilder(str);
        for (int i = 0; i < numZeros; i++) {
            stringBuilder.append("0");
        }
        return stringBuilder.toString();
    }


    public static String getRemainderOfDivision(String dividend, String divider) {

        StringBuilder d1 = new StringBuilder(dividend);
        StringBuilder d2 = new StringBuilder(divider);
        StringBuilder tmp = new StringBuilder();

        int counter = 0;

        while (counter < dividend.length()) {

            tmp.append(d1.charAt(counter));
            counter++;
            if (tmp.charAt(0) == '0') {
                tmp.deleteCharAt(0);
            } else {
                if (tmp.length() == d2.length()) {
                    tmp = xor(tmp, d2);
                }
            }


        }
        return tmp.toString();
    }


    private static String cyclicShiftLeft(String code) {
        StringBuilder stringBuilder = new StringBuilder(code);
        char ch = stringBuilder.charAt(0);
        stringBuilder.deleteCharAt(0);
        stringBuilder.append(ch);
        return stringBuilder.toString();
    }


    private static String cyclicShiftRight(String code) {
        StringBuilder stringBuilder = new StringBuilder(code);
        char ch = stringBuilder.charAt(code.length() - 1);
        stringBuilder.deleteCharAt(code.length() - 1);
        stringBuilder.insert(0, ch);
        return stringBuilder.toString();
    }


    private static String appendStartZero(String res) {
        StringBuilder stringBuilder = new StringBuilder(res);
        stringBuilder.reverse();
        stringBuilder.append(0);
        stringBuilder.reverse();
        res = stringBuilder.toString();
        return res;
    }

    private static StringBuilder xor(StringBuilder arg1, StringBuilder arg2) {

        int x1 = Integer.parseInt(arg1.toString(), 2);
        int x2 = Integer.parseInt(arg2.toString(), 2);
        return new StringBuilder(Integer.toBinaryString(x1 ^ x2));
    }

    public static String cyclicCheckErrors(String s) {

        System.out.println("checking for errors : " + s);
        if (getRemainderOfDivision(s, POLYNOM).charAt(0) == '0') {
            System.out.println("no errors in code");
            return s;
        }
        int counter;
        for (int numShifts = 0; ; numShifts++) {
            String remainder = getRemainderOfDivision(s, POLYNOM);
            System.out.println("remainder : " + remainder);
            if (isWeightLessThanTwo(remainder)) {
                System.out.println("error at " + numShifts + " number");
                s = swapLastChar(s);
                counter = numShifts;
                break;
            }
            s = cyclicShiftLeft(s);
            System.out.println("Shift left : " + s);


        }
        for (int i = 0; i < counter; i++) {
            s = cyclicShiftRight(s);

        }
        return s;

    }

    private static String swapLastChar(String s) {
        StringBuilder stringBuilder = new StringBuilder(s);
        if (stringBuilder.charAt(s.length() - 1) == '0') {
            stringBuilder.setCharAt(s.length() - 1, '1');
        } else {
            stringBuilder.setCharAt(s.length() - 1, '0');
        }
        return stringBuilder.toString();
    }

    private static boolean isWeightLessThanTwo(String s) {
        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '1') {
                counter++;
            }
        }
        return counter <= 1;
    }

    public static String getCodeRemainderWithZeros(String message) {
        String message1 = appendZeros(message, TOTAL_DIGIT_NUMBER - message.length());
        String addon = getRemainderOfDivision(message1, POLYNOM);
        addon = addZeros(addon);
        return addon;
    }

    public static List<String> getFormingMatrix() {
        List<String> matrix = new ArrayList<>();
        StringBuilder x = new StringBuilder("1");
        for (int k = 1; k < TOTAL_DIGIT_NUMBER - POLYNOM.length() + 1; k++) {
            x.append("0");
        }
        String tmp = x.toString();
        for (int i = 0; i < TOTAL_DIGIT_NUMBER - (POLYNOM.length() - 1); i++) {
            String rem = getCodeRemainderWithZeros(tmp);

            matrix.add(rem);
            System.out.println(tmp + rem);
            tmp = cyclicShiftRight(tmp);
        }
        return matrix;
    }


    public static List<String> getErrorMatrix() {
        List<String> matrix = new ArrayList<>();
        StringBuilder x = new StringBuilder("1");
        for (int k = 1; k < TOTAL_DIGIT_NUMBER; k++) {
            x.append("0");
        }
        String tmp = x.toString();
        for (int i = 0; i < TOTAL_DIGIT_NUMBER; i++) {
            String rem = getRemainderOfDivision(tmp, POLYNOM);
            rem = addZeros(rem);
            matrix.add(rem);
            System.out.println(tmp + rem);
            tmp = cyclicShiftRight(tmp);
        }
        return matrix;
    }

    public static int findErrorWithMatrix(List<String> errorMatrix, String inputCode) {
        String tmp = getRemainderOfDivision(inputCode, POLYNOM);
        tmp = addZeros(tmp);
        for (int i = 0; i < errorMatrix.size(); i++) {
            if (tmp.equals(errorMatrix.get(i))) {
                return i + 1;
            }
        }
        return -1;
    }

    private static String addZeros(String str) {
        while (str.length() < POLYNOM.length() - 1) {
            str = appendStartZero(str);
        }
        return str;
    }

    public static String correctNum(String inputCode, int index) {
        StringBuilder str = new StringBuilder(inputCode);
        if (str.charAt(index - 1) == '1') {
            str.setCharAt(index - 1, '0');
        } else {
            str.setCharAt(index - 1, '1');
        }
        return str.toString();
    }
}
