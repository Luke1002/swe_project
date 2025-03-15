package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.LibraryAdminController;
import com.swe.libraryprogram.domainmodel.*;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.util.LinkedList;
import java.util.function.UnaryOperator;


public class AddItemController {

    @FXML
    private TextField titleField, descriptionField, yearField, quantityField, lengthField, genresField, publisherField;

    @FXML
    private TextField isbnField, editionField, authorField;

    @FXML
    private TextField issnField, frequencyField;

    @FXML
    private TextField producerField;

    @FXML
    private VBox bookBox, digitalMediaBox, periodicPublicationBox;

    @FXML
    private HBox authorBox;

    @FXML
    private Label editorText, authorText;

    @FXML
    private Button addButton, cancelButton;

    @FXML
    private ChoiceBox<String> choiceBox, ageRatingBox;

    @FXML
    private ChoiceBox<Integer> dayBox, monthBox;

    private User user = new User("String email", "String password", "String name", "String surname", "String phone");

    private String curr_element_type;

    private Scene previousScene;

    private Element element;

    private LibraryAdminController libraryAdminController;


    public void initialize() {

        setFieldsRestrictions();

        libraryAdminController = new LibraryAdminController(user);

        addButton.setOnAction(event -> handleAddButton());
        cancelButton.setOnAction(event -> goBack());
        choiceBox.getItems().setAll("Libro", "Digital Media", "Periodico");
        choiceBox.setOnAction(event -> showFields());

    }

    private void showFields() {
        curr_element_type = choiceBox.getValue();
        if(curr_element_type.equals("Libro")){
            authorText.setText("Autore: ");
            authorBox.setVisible(true);
            bookBox.setVisible(true);
        }
        else if (curr_element_type.equals("Digital Media")) {

        }
        else if (curr_element_type.equals("Periodico")) {

        }
        digitalMediaBox.setVisible(curr_element_type.equals("Digital Media"));
        periodicPublicationBox.setVisible(curr_element_type.equals("Periodic Publication"));
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;

    }

    public void goBack() {

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(previousScene);

    }

    public void handleAddButton() {
        if (curr_element_type.equals("Book")) {
            element = new Book(titleField.getText(), Integer.parseInt(yearField.getText()), "", Integer.parseInt(quantityField.getText()), Integer.parseInt(quantityField.getText()), Integer.parseInt(lengthField.getText()), new LinkedList<Genre>(), ISBNField.getValue(), authorField.getText(), publisherField.getText(), editionField.getValue());
        } else if (curr_element_type.equals("Digital Media")) {

        } else if (curr_element_type.equals("Periodic Publication")) {

        }

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

    private void setFieldsRestrictions() {

        UnaryOperator<TextFormatter.Change> onlyNumbersFilter = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("\\d*")) {
                return change;
            } else {
                return null;
            }
        };

        UnaryOperator<TextFormatter.Change> isbnFilter = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("\\d*") && newText.length() <= 13) {
                return change;
            } else {
                return null;
            }
        };

        UnaryOperator<TextFormatter.Change> yearFilter = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("\\d*") && newText.length() <= 4) {
                return change;
            } else {
                return null;
            }
        };

        UnaryOperator<TextFormatter.Change> monthFilter = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("\\d*") && newText.length() < 2) {
                return change;
            } else if (newText.matches("\\d*") && Integer.parseInt(change.getControlNewText()) > 12) {
                change.setText("12");
                return change;
            } else {
                return null;
            }
        };

        UnaryOperator<TextFormatter.Change> dayFilter = change -> {
            String newText = change.getControlNewText();

            if (newText.matches("\\d*") && newText.length() < 2) {
                return change;
            } else if (newText.matches("\\d*") && Integer.parseInt(change.getControlNewText()) > 31) {
                change.setText("31");
                return change;
            } else {
                return null;
            }
        };

        UnaryOperator<TextFormatter.Change> totalLengthFilter = change -> {
            String newText = change.getControlNewText();

            if (newText.length() <= 64) {
                return change;
            } else {
                return null;
            }
        };

        UnaryOperator<TextFormatter.Change> descriptionLengthFilter = change -> {
            String newText = change.getControlNewText();

            if (newText.length() <= 256) {
                return change;
            } else {
                return null;
            }
        };

    }


}
