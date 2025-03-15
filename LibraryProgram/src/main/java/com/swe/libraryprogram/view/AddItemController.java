package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.LibraryAdminController;
import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.User;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


public class AddItemController {

    @FXML
    private Text welcomeText;

    @FXML
    private TextField titleField, yearField, descriptionField, quantityField, quantityAvailableField, lengthField;

    @FXML
    private TextField ISBNField, authorField, publisherField, editionField;

    @FXML
    private TextField producerField, ageRatingField, directorField;

    @FXML
    private TextField frequencyField, releaseMonthField, releaseDayField, ISSNField;

    @FXML
    private Button addButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox<String> choiceBox;

    private User user = new User();

    private Scene previousScene;

    private Element element;

    private LibraryAdminController libraryAdminController;



    public void initialize() {

        libraryAdminController = new LibraryAdminController(user);

        addButton.setOnAction(event -> handleAddButton());
        cancelButton.setOnAction(event -> goBack());
        choiceBox.getItems().addAll("Book", "Digital Media", "Periodic Publication");

        welcomeText.setText("Benvenuto, " + user.getName() + "!");

    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;

    }

    public void goBack() {

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(previousScene);

    }

    public void handleAddButton() {

        //TODO: element = new Element(...);

        if (libraryAdminController.addElement(element)) {

            showAlert("Aggiunta elemento", "Elemento aggiunto con successo", AlertType.INFORMATION);
            goBack();

        } else {

            showAlert("Aggiunta elemento", "Errore: elemento non aggiunto", AlertType.ERROR);
            goBack();

        }

    }

    private void showAlert(String title, String message, AlertType type) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    public void setUser(User user) {
        this.user = user;

    }


}
