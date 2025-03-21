package com.swe.libraryprogram.controller;


import com.swe.libraryprogram.service.MainService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;


public class LoginViewController extends BaseViewController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginButton, signupButton;

    @FXML
    public void initialize() {
        super.initialize();
        loginButton.setOnAction(event -> onLoginButtonClick());
        signupButton.setOnAction(event -> onSignupButtonClick());
    }

    protected void onLoginButtonClick() {

        String email = emailField.getText();
        String password = passwordField.getText();

        try {
            if (MainService.getInstance().setUserState(email, password)) {
                mainViewController.loadTopPane("menubar");
                mainViewController.loadBottomPane("home");
            } else {
                showAlert("Error", "Email e/o password errate");
            }
        } catch (SQLException e) {

            showAlert("Error", "Database Error");
        }

    }

    private void onSignupButtonClick() {
        mainViewController.loadBottomPane("signup");
    }

}
