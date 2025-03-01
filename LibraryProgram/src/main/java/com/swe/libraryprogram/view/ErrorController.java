package com.swe.libraryprogram.view;

import com.swe.libraryprogram.HelloApplication;
import com.swe.libraryprogram.view.*;
import com.swe.libraryprogram.dao.ConnectionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ErrorController {
    @FXML
    private Label errorLabel;

    public void setStage(Stage stage, Scene scene) {
        setErrorText("a");
        stage.setMinHeight(480);
        stage.setMinWidth(640);
        stage.setTitle("Error");
        stage.setScene(scene);
    }

    public void setErrorText(String message) {
        errorLabel.setText(message); // Imposta il messaggio di errore dinamicamente
    }

    @FXML
    private void onQuit() {
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close(); // Chiude la finestra
    }

    @FXML
    private void onTryAgain() throws IOException {
        ConnectionManager cM = ConnectionManager.getInstance();
        if(cM.startingConnectionCheck()){
            Stage stage = (Stage) errorLabel.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            HelloController controller = fxmlLoader.getController();
            controller.setStage(stage, scene);
            stage.show();
        }
        else{
            setErrorText("Porco dio");
        }
    }
}