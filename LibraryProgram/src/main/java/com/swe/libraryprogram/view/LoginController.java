package com.swe.libraryprogram.view;


import com.swe.libraryprogram.dao.UserManager;
import com.swe.libraryprogram.dao.ConnectionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;



public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;


    @FXML
    protected void onLoginButtonClick() {

        String email = emailField.getText();
        String password = passwordField.getText();
        UserManager userManager = new UserManager(ConnectionManager.getInstance());

        if (email.isEmpty() || password.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Email e password devono essere compilati");
            alert.showAndWait();
            return;

        }

        if (userManager.authenticate(email, password)) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Success");
            alert.setContentText("Login successful");
            alert.showAndWait();

        } else {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Login failed");
            alert.showAndWait();

        }
    }

    @FXML
    private void onSignupButtonClick(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/signup-view.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Ottiene la finestra attuale
        stage.setScene(new Scene(root));
        stage.setTitle("Registrazione");
        stage.show();

    }

}
