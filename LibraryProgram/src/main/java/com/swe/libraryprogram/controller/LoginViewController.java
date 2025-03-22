package com.swe.libraryprogram.controller;


import com.swe.libraryprogram.service.MainService;
import javafx.fxml.FXML;
import javafx.scene.control.*;

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

        MainService mC = MainService.getInstance();
        try {
            if (mC.setUserState(email, password)) {
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
