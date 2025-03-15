package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.UserController;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.swe.libraryprogram.dao.UserManager;
import com.swe.libraryprogram.dao.ConnectionManager;
import javafx.scene.control.Alert;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;



public class SignupController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField phoneField;


    @FXML
    private void onSignupButtonClick(){

        String email = emailField.getText();
        String password = passwordField.getText();
        String name = nameField.getText();
        String surname = surnameField.getText();
        String phone = phoneField.getText();

        if(email.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty()){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Tutti i campi devono essere compilati");
            alert.showAndWait();
            return;

        }

        UserController usrController = new UserController();

        if(usrController.signup(email, password, name, surname, phone)){

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Success");
            alert.setContentText("Registrazione avvenuta con successo");
            alert.showAndWait();

        }
        else{

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Registrazione fallita");
            alert.showAndWait();

        }

    }

    //tasto per tornare alla pagina di login
    @FXML
    private void onBackButtonClick(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login-view.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login");
            stage.show();

        } catch (IOException e) {

            e.printStackTrace();

        }

    }


}

//TODO: mettere onAction sui bottoni della view signup-view.fxml
