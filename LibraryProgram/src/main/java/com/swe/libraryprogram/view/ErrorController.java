package com.swe.libraryprogram.view;

import com.swe.libraryprogram.HelloApplication;
import com.swe.libraryprogram.view.*;
import com.swe.libraryprogram.dao.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class ErrorController {
    @FXML
    private Label errorLabel;

    public void setErrorText(String message) {
        errorLabel.setText(message); // Imposta il messaggio di errore dinamicamente
    }

    @FXML
    private void onQuit(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onTryAgain(ActionEvent event) throws IOException {
        ConnectionManager cM = ConnectionManager.getInstance();
        if(cM.isConnectionValid()){
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
            stage.setScene(new Scene(fxmlLoader.load(), stage.getMinWidth(), stage.getMinHeight()));
            stage.show();
        }
        else{
            setErrorText("Porco dio");
        }
    }
}