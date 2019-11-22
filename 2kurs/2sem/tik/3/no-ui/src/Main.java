import java.util.List;

public class Main {
    public static void main(String[] args) {
        //начальное сообщение. может быть любым, но длина равна (n - (длина полинома - 1))
        String message = "1010010100110110010101";
        //остаток от деления сообщения на образующий полином.
        String remainder = CyclicCode.getCodeRemainderWithZeros(message);

        System.out.println("remainder of division : " + remainder);
        //циклический код - начальное сообщение + остаток от деления
        //начального сообщения на полином (проверочные биты)
        //https://www.youtube.com/watch?v=2BfjAIJjneQ (за 10 мин все рассказывается доступно)
        //http://studepedia.org/index.php?vol=1&post=70690 (пример 1)
        String code = message + remainder;
        System.out.println("cyclic code : " + code);
        /**
         *         намеренно делаем ошибку в коде и проверяем где она находится
         *         методом циклических сдвигов.
         *         то есть находим остаток от деления цикл кода на образующий полином.
         *         если вес остатка больше одного (в остатке больше одной "1")
         *         - циклически сдвигаем код влево.
         *         продолжаем процесс, пока вес остатка не будет меньше двух.
         *         когда остаок меньше двух - запоминаем число итераций и
         *         меняем последнюю цифру кода на противоположную.
         *         после чего циклически сдвигаем код вправо на столько итераций,
         *         на сколько сдвигали влево.
         *         в итоге получаем исходный код.
         *         http://studepedia.org/index.php?vol=1&post=70690  (пример 6)
         *
         */
        String inputCode = "1010110100110110010101010101";
        //String inputCode = "1011110";
        String correctedCode = CyclicCode.cyclicCheckErrors(inputCode);

        System.out.println("input code :     " + inputCode);
        System.out.println("corrected code : " + correctedCode);


        System.out.println("forming matrix : ");
        List<String> formingMatrix = CyclicCode.getFormingMatrix();

        System.out.println("matrix of errors : ");
        List<String> errorMatrix = CyclicCode.getErrorMatrix();

        System.out.println("input code : " + inputCode);
        int index = CyclicCode.findErrorWithMatrix(errorMatrix, inputCode);
        System.out.printf("error at %d number\n", index);
        System.out.println("corrected code : " + CyclicCode.correctNum(inputCode, index));


    }
}
