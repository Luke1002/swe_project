package com.swe.libraryprogram.view;


import com.swe.libraryprogram.HelloApplication;
import com.swe.libraryprogram.controller.LibraryAdminController;
import com.swe.libraryprogram.controller.LibraryUserController;
import com.swe.libraryprogram.dao.UserManager;
import com.swe.libraryprogram.dao.ConnectionManager;
import com.swe.libraryprogram.domainmodel.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;


public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick() {

        String email = emailField.getText();
        String password = passwordField.getText();
        UserManager userManager = new UserManager();

        if (email.isEmpty() || password.isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Error");
            alert.setContentText("Email e password devono essere compilati");
            ButtonType okButton = new ButtonType("Ok");
            alert.getButtonTypes().add(okButton);
            alert.showAndWait();
            return;
        }
        try{
            if (userManager.authenticate(email, password)) {
                User usr = userManager.getUser(email);
                LibraryAdminController adminController = new LibraryAdminController(usr);
                LibraryUserController libraryUserController = new LibraryUserController(usr);
                if (usr.isAdmin()) {

                }
                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("Success");
                alert.setContentText("Login successful");
                ButtonType okButton = new ButtonType("Ok");
                alert.getButtonTypes().add(okButton);
                alert.showAndWait();

            } else {

                Alert alert = new Alert(Alert.AlertType.NONE);
                alert.setTitle("Error");
                alert.setContentText("Email e/o password errate");
                ButtonType okButton = new ButtonType("Ok");
                alert.getButtonTypes().add(okButton);
                alert.showAndWait();

            }
        }
        catch(SQLException e){
            Alert alert = new Alert(Alert.AlertType.NONE);
            alert.setTitle("Error");
            alert.setContentText("Database Error");
            ButtonType okButton = new ButtonType("Ok");
            alert.getButtonTypes().add(okButton);
            alert.showAndWait();
        }

    }

    @FXML
    private void onSignupButtonClick(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("signup-view.fxml"));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow(); // Ottiene la finestra attuale
        Scene scene = new Scene(loader.load(), stage.getMinWidth(), stage.getMinHeight());
        stage.setScene(new Scene(loader.load(), stage.getMinWidth(), stage.getMinHeight()));
        stage.setTitle("Signup");
        stage.show();

    }

}
