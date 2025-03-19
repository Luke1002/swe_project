package com.swe.libraryprogram.view;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

public class RecoveryPasswordController extends BaseViewController {

    @FXML
    private TextField email;

    @FXML
    private TextField newPassword;

    @FXML
    private TextField confirmNewPassword;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private void confirmButton(ActionEvent event) {
        changePassword();
    }

    @FXML
    private void cancelButton(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void changePassword() {
        String emailInput = email.getText().trim();
        String newPass = newPassword.getText().trim();
        String confirmPass = confirmNewPassword.getText().trim();

        if (emailInput.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            showAlert( "Error", "Tutti i campi devono essere riempiti!");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            showAlert( "Mismatch", "Le password inserite sono diverse!");
            return;
        }

        //TODO richiama metodo per controllare se esiste mail
        if (!isEmailRegistered(emailInput)) {
            showAlert( "Error", "Email non presente!");
            return;
        }

        if (updatePassword(emailInput, newPass)) {
            showAlert( "Success", "Password cambiata correttamente!");
            reloadScene();
        } else {
            showAlert( "Error", "Password non aggiornata! Riprovare");
        }
    }

    //TODO creare metodo per controllare se la mail è presente e richiamarlo
    private boolean isEmailRegistered(String email) {

        return false;
    }


    //TODO fare metodo di aggiornamento pw nel DB e richiamarlo
    private boolean updatePassword(String email, String newPassword){

        return false;
    }

    private void reloadScene() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/view/RecoveryPasswordView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) email.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
