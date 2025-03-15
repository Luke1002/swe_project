package com.swe.libraryprogram.view;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.swe.libraryprogram.HelloApplication;
import com.swe.libraryprogram.view.*;
import com.swe.libraryprogram.dao.ConnectionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class ContactUsController {

    @FXML
    private TextField contactName;

    @FXML
    private TextField contactEmail;

    @FXML
    private TextArea motivetionCamp;

    @FXML
    private Button confirmButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Label errorLabel;

    @FXML
    private void confirmButton(ActionEvent event){
        sendMessage();
    }

    @FXML
    private void cancelButton(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    //TODO a chi o dove mando il messsaggio?
    private void sendMessage()  {
        ConnectionManager cM = ConnectionManager.getInstance();
        if(cM.isConnectionValid()){
            String name = contactName.getText().trim();
            String email = contactEmail.getText().trim();
            String motivation = motivetionCamp.getText().trim();

            if (name.isEmpty() || email.isEmpty() || motivation.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Tutti i campi devono essere riempiti!");
                return;
            }

            if (!email.contains("@") || !email.contains(".")) {
                showAlert(Alert.AlertType.ERROR, "Invalid Email", "Inserire indirizzo e-mail valido.");
                return;
            }
            System.out.println("Message sent to admins:\n" +
                    "Name: " + name + "\nEmail: " + email + "\nMotivation: " + motivation);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Your message has been sent!");

        }
        else{
            setErrorText("Impossibile mandare il messaggio!");
        }
    }

    //TODO risolvere il reload della scena
    private void reloadScene(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("contactUs-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load(), stage.getMinWidth(), stage.getMinHeight()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void setErrorText(String message) {
        errorLabel.setText(message); // Imposta il messaggio di errore dinamicamente
    }
}