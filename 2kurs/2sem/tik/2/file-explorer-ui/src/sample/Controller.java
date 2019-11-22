package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    public ImageView imageView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }





    @FXML
    public void handleClick(ActionEvent actionEvent) throws IOException, CloneNotSupportedException, InterruptedException {
        final FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(new Stage());
        String name = file.getName();
        if ((name.substring(name.lastIndexOf('.') + 1)).equals("archive")) {

            Dearchiver.decodeFile(file);
            Stage stage = new Stage();

            Parent root = FXMLLoader.load(getClass().getResource("../UI/popupdecoded.fxml"));

            stage.setScene(new Scene(root,200,122));

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();

        }
        else {
            Archiver.encodeFile(file);

            Stage stage = new Stage();

            Parent root = FXMLLoader.load(getClass().getResource("../UI/popupencoded.fxml"));

            stage.setScene(new Scene(root,200,122));

            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();


        }
    }
}
