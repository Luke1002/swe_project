package com.swe.libraryprogram.view;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;

public class HelloController {
    @FXML
    private Label welcomeText;
    public static String labelText = "";

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText(labelText);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText("Error");
        alert.showAndWait();
    }
}