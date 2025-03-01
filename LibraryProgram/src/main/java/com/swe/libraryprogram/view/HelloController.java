package com.swe.libraryprogram.view;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class HelloController {
    @FXML
    private Label welcomeText;
    public static String labelText = "";

    public void setStage(Stage stage, Scene scene) {
        stage.setMinHeight(480);
        stage.setMinWidth(640);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText(labelText);
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Error");
        alert.setContentText("Email e/o password errate e/o assenti");
        ButtonType okButton = new ButtonType("Ok");
        alert.getButtonTypes().add(okButton);
        alert.showAndWait();
    }
}