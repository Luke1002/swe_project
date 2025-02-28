package com.swe.libraryprogram.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class ErrorWindowController {
    @FXML
    private Label errorLabel;

    public void setErrorText(String message) {
        errorLabel.setText(message); // Imposta il messaggio di errore dinamicamente
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) errorLabel.getScene().getWindow();
        stage.close(); // Chiude la finestra
    }
}