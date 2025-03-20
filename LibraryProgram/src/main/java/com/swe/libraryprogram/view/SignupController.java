package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.SQLException;
import java.util.function.UnaryOperator;


public class SignupController extends BaseViewController {

    @FXML
    private TextField emailField, confirmEmailField;

    @FXML
    private PasswordField passwordField, confirmPasswordField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private TextField phoneField;

    @FXML
    private Button signUpButton, backButton;

    private UnaryOperator<TextFormatter.Change> passwordFilter() {
        return change -> {
            // Limitazione di lunghezza massima
            if (change.getControlNewText().length() > 20) {
                return null;
            }
            String regex = "^[a-zA-Z0-9!#$%&=?@]*$";

            if (!change.getControlNewText().matches(regex)) {
                return null;
            }
            return change;
        };
    }

    private UnaryOperator<TextFormatter.Change> emailFilter() {
        return change -> {
            String newText = change.getControlNewText();
            if (newText.length() > 64) {
                return null;
            }
            String initialRegex = "^[a-zA-Z0-9._-]*$";
            String totalRegex = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
            if((!newText.contains("@") && newText.matches(initialRegex)) || (newText.contains("@") && newText.matches(totalRegex))) {
                return change;
            }
            else{
                return null;
            }
        };
    }

    private UnaryOperator<TextFormatter.Change> phoneNumberFilter() {
        return change -> {
            String newText = change.getControlNewText();

            if (newText.length() > 15) {
                return null;
            }
            String regex = "^[+]?[0-9]{0,1}[0-9]{1,4}[ ]?[0-9]{3}[ ]?[0-9]{6,7}$";

            if (newText.matches(regex)) {
                return change;
            }
            return null;
        };
    }

    private UnaryOperator<TextFormatter.Change> lengthFilter() {
        return change -> {
            String newText = change.getControlNewText();

            if (newText.length() > 64) {
                return null;
            }
            return change;
        };
    }

    @FXML
    protected void initialize() {
        super.initialize();
        signUpButton.setOnAction(event -> onSignupButtonClick());
        backButton.setOnAction(event -> onBackButtonClick());
        emailField.setTextFormatter(new TextFormatter<>(emailFilter()));
        confirmEmailField.setTextFormatter(new TextFormatter<>(emailFilter()));
        passwordField.setTextFormatter(new TextFormatter<>(passwordFilter()));
        confirmPasswordField.setTextFormatter(new TextFormatter<>(passwordFilter()));
        nameField.setTextFormatter(new TextFormatter<>(lengthFilter()));
        surnameField.setTextFormatter(new TextFormatter<>(lengthFilter()));
        phoneField.setTextFormatter(new TextFormatter<>(phoneNumberFilter()));
    }

    @FXML
    private void onSignupButtonClick(){
        if(true || checkFields()){
            String email = emailField.getText();
            String password = passwordField.getText();
            String name = nameField.getText();
            String surname = surnameField.getText();
            String phone = phoneField.getText();

            if(MainController.getInstance().getUserController().signup(email, password, name, surname, phone)){
                showAlert("Success", "Registrazione completata con successo");
                try {
                    if(MainController.getInstance().setState(email, password)){
                        mainViewController.loadTopPane("menubar");
                        mainViewController.loadBottomPane("home");
                    }
                    else {
                        throw new SQLException();
                    }
                }catch (SQLException e){
                    showAlert("Error", "Impossibile effettuare il login");
                    mainViewController.loadBottomPane("login");
                }
            }
            else{
                showAlert("Error", "Registrazione fallita");
            }
        }
    }

    private Boolean checkFields() {
        if(emailField.getText().isEmpty() || passwordField.getText().isEmpty() || nameField.getText().isEmpty() || surnameField.getText().isEmpty()){
            showAlert("Error", "Tutti i campi obbligatori devono essere compilati");
            return false;
        }
        if(!emailField.getText().contains("@")){
            showAlert("Error", "Email non valida");
            return false;
        }
        if(!emailField.getText().equals(confirmEmailField.getText())){
            showAlert("Error", "I due campi e-mail non corrispondono");
            return false;
        }
        if(!passwordField.getText().equals(confirmPasswordField.getText())){
            showAlert("Error", "I due campi password non corrispondono");
            return false;
        }
        return true;
    }

    //tasto per tornare alla pagina di login
    @FXML
    private void onBackButtonClick() {
        mainViewController.loadBottomPane("login");
    }


}

//TODO: mettere onAction sui bottoni della view signup-view.fxml
