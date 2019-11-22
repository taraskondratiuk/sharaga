package sample;

import static data.Data.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

public class Controller {


    @FXML
    private Label polynomLabel;

    @FXML
    private TextArea textArea;

    @FXML
    private Label outputLabel;

    @FXML
    private ScrollPane scrollPane;

    public void initialize() {
        polynomLabel.setText("Polynom : " + POLYNOM);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

    }


    public void handleEncodeClick(javafx.event.ActionEvent actionEvent) {
        scrollPane.setContent(null);
        //начальное сообщение может быть любым, но длина равна (n - (длина полинома - 1)) и начинается с 1
        String message = textArea.getText();
        //остаток от деления сообщения на образующий полином.
        String remainder = CyclicCode.getRemainderWithZeros(message);
        //System.out.println("remainder of division : " + remainder);
        //циклический код - начальное сообщение + остаток от деления
        //начального сообщения на полином (проверочные биты)
        //https://www.youtube.com/watch?v=2BfjAIJjneQ (за 10 мин все рассказывается доступно)
        //http://studepedia.org/index.php?vol=1&post=70690 (пример 1)
        String code = message + remainder;

        scrollPane.setContent(new Text( "remainder of division :\n" + remainder + "\n" + "cyclic code :\n" + code));

        //System.out.println("cyclic code : " + code);

    }

    public void handleDecodeClick(ActionEvent actionEvent) {
        scrollPane.setContent(null);

        //полученный циклический код с одной ошибкой
        String inputCode = textArea.getText();
        String correctedCode = CyclicCode.checkErrors(inputCode);

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
        Text text = new Text(correctedCode);
        scrollPane.setContent(text);




    }
}
