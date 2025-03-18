package com.swe.libraryprogram.view;


import com.swe.libraryprogram.controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;


public class LoginController extends BaseViewController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton, signupButton;

    public void initialize() {
        loginButton.setOnAction(event -> onLoginButtonClick());
        signupButton.setOnAction(event -> onSignupButtonClick());
    }

    public void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(title);
        alert.setContentText(message);
        ButtonType okButton = new ButtonType("Ok");
        alert.getButtonTypes().add(okButton);
        alert.showAndWait();
    }

    protected void onLoginButtonClick() {

        String email = emailField.getText();
        String password = passwordField.getText();

        MainController mC = MainController.getInstance();
        try {
            if (mC.setUser(email, password)) {
                mC.setScene("home");
            } else {
                showAlert("Error", "Email e/o password errate");
            }
        } catch (SQLException e) {

            showAlert("Error", "Database Error");
        }

    }

    private void onSignupButtonClick() {
        MainController mC = MainController.getInstance();
        mC.setScene("signup");
    }

}
