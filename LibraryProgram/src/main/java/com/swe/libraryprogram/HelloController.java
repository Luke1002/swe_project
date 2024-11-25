package com.swe.libraryprogram;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;
    public static String labelText = "";

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText(labelText);
    }
}