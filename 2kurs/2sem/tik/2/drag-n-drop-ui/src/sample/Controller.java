package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Controller implements Initializable {

    public ImageView imageView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    @FXML
    private void handleDrop(DragEvent event) throws IOException, InterruptedException, CloneNotSupportedException {


        //long start = System.currentTimeMillis();
        List<File> fileDrag = event.getDragboard().getFiles();

        File file = fileDrag.get(0);
        String name = file.getName();
        if ((name.substring(name.lastIndexOf('.') + 1)).equals("huf")) {

            Decoder.decodeFile(file);
            imageView.setImage(new Image("/bQ.gif", true));
        }
        else {
            Encoder.encodeFile(file);
            imageView.setImage(new Image("/img.jpg", true));

        }






    }

}
