package sample;

import static data.Data.*;



abstract class CyclicCode {

    private static String appendZeros(String str, int numZeros) {
        StringBuilder stringBuilder = new StringBuilder(str);
        for (int i = 0; i < numZeros; i++) {
            stringBuilder.append("0");
        }
        return stringBuilder.toString();
    }


    private static String getRemainderOfDivision(String dividend, String divider) {

        StringBuilder d1 = new StringBuilder(dividend);
        StringBuilder d2 = new StringBuilder(divider);
        StringBuilder tmp = new StringBuilder();

        int counter = 0;

        while (counter < dividend.length()) {

            tmp.append(d1.charAt(counter));
            counter++;
            if(tmp.charAt(0) == '0') {
                tmp.deleteCharAt(0);
            }
            else {
                if (tmp.length() == d2.length()) {
                    tmp = xor(tmp, d2);
                }
            }


        }
        return tmp.toString();
    }


    private static String cyclicShiftLeft (String code) {
        StringBuilder stringBuilder = new StringBuilder(code);
        char ch = stringBuilder.charAt(0);
        stringBuilder.deleteCharAt(0);
        stringBuilder.append(ch);
        return stringBuilder.toString();
    }


    private static String cyclicShiftRight (String code) {
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

    private static StringBuilder xor(StringBuilder arg1, StringBuilder arg2){

        int x1 = Integer.parseInt(arg1.toString(), 2);
        int x2 = Integer.parseInt(arg2.toString(), 2);
        return new StringBuilder(Integer.toBinaryString(x1 ^ x2));
    }

    static String checkErrors(String s) {
        StringBuilder stringBuilder = new StringBuilder("checking for errors :\n");
        if(getRemainderOfDivision(s, POLYNOM).charAt(0) == '0') {
            stringBuilder.append("no errors in code\n");
            return stringBuilder.toString();
        }
        int counter;
        for(int numShifts = 0; ;numShifts++) {
            String remainder = getRemainderOfDivision(s, POLYNOM);
            stringBuilder.append("remainder : " + remainder + "\n");
            if(isWeightLessThanTwo(remainder)) {
                stringBuilder.append("error at " + numShifts + " number\n");
                s = swapLastChar(s);
                counter = numShifts;
                break;
            }
            s = cyclicShiftLeft(s);
            stringBuilder.append("Shift left : \n" + s + "\n");



        }
        for (int i = 0; i < counter; i++) {
            s = cyclicShiftRight(s);

        }
        return stringBuilder.toString() + "Corrected code :\n" + s;
        //return s;

    }

    private static String swapLastChar(String s) {
        StringBuilder stringBuilder = new StringBuilder(s);
        if(stringBuilder.charAt(s.length() - 1) == '0') {
            stringBuilder.setCharAt(s.length() - 1, '1');
        }
        else {
            stringBuilder.setCharAt(s.length() - 1, '0');
        }
        return stringBuilder.toString();
    }

    private static boolean isWeightLessThanTwo(String s) {
        int counter = 0;
        for (int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == '1') {
                counter++;
            }
        }
        return counter <= 1;
    }

    static String getRemainderWithZeros(String message) {
        String message1 = appendZeros(message, TOTAL_DIGIT_NUMBER - message.length());
        String addon = getRemainderOfDivision(message1, POLYNOM);
        while (addon.length() < TOTAL_DIGIT_NUMBER - message.length()) {
            addon = appendStartZero(addon);
        }
        return addon;
    }
}
