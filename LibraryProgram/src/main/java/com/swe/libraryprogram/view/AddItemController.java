package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.LibraryAdminController;
import com.swe.libraryprogram.dao.GenreManager;
import com.swe.libraryprogram.domainmodel.*;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;


public class AddItemController {

    @FXML
    private TextField titleField, descriptionField, yearField, quantityField, lengthField, genresField, publisherField;

    @FXML
    private TextField isbnField, editionField, authorField;

    @FXML
    private TextField issnField, frequencyField;

    @FXML
    private TextField producerField, ageField;

    @FXML
    private VBox generalBox, additionalFieldsBox;

    @FXML
    private VBox bookBox, digitalMediaBox, periodicPublicationBox;

    @FXML
    private HBox authorBox;

    @FXML
    private Label editorText, authorText;

    @FXML
    private Button addButton, cancelButton;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private ChoiceBox<Integer> dayBox, monthBox;

    private User user = new User("String email", "String password", "String name", "String surname", "String phone");

    private String curr_element_type;

    private Scene previousScene;

    private Element element;

    private LibraryAdminController libraryAdminController;

    private final Integer[] days = IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new);

    UnaryOperator<TextFormatter.Change> onlyNumbersFilter = change -> {
        String newText = change.getControlNewText();

        if (newText.matches("\\d*") && !newText.startsWith("0")) {
            if (!newText.isEmpty() && (Long.valueOf(Integer.MAX_VALUE) < Long.parseLong(newText))) {
                return null;
            }
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
            showDays();
            return change;
        } else {
            return null;
        }
    };

    UnaryOperator<TextFormatter.Change> ageFilter = change -> {
        String newText = change.getControlNewText();

        if (newText.matches("\\d*") && !newText.startsWith("0")) {
            if(!newText.isEmpty() && Integer.parseInt(newText) < 120) {
                return change;
            }
            return null;
        }
        return null;
    };

    UnaryOperator<TextFormatter.Change> totalLengthFilter = change -> {
        String newText = change.getControlNewText();

        if (newText.length() <= 64) {
            return change;
        } else {
            return null;
        }
    };


    public void initialize() {
        setFieldsRestrictions();

        libraryAdminController = new LibraryAdminController(user);

        addButton.setOnAction(event -> handleAddButton());
        cancelButton.setOnAction(event -> goBack());
        choiceBox.getItems().setAll("Libro", "Digital Media", "Periodico");
        choiceBox.setOnAction(event -> showFields());
        choiceBox.setValue(choiceBox.getItems().getFirst());
        monthBox.getItems().setAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        monthBox.setOnAction(event -> showDays());
        monthBox.setValue(monthBox.getItems().getFirst());
        yearField.setText(String.valueOf(LocalDate.now().getYear()));
        dynamicPadding(generalBox, 0.10f);
        dynamicPadding(additionalFieldsBox, 0.10f);
    }

    private void dynamicPadding(Region viewRegion, float paddingValue) {
        viewRegion.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double width = newValue.doubleValue();
                // Set padding as a percentage of the width (e.g., 10% padding)
                double padding = width * paddingValue;  // 10% padding
                viewRegion.setPadding(new Insets(padding, padding, padding, padding));
            }
        });

        viewRegion.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double height = newValue.doubleValue();
                // You could also calculate padding based on height if needed
                double padding = height * paddingValue;  // 10% padding
                viewRegion.setPadding(new Insets(padding, padding, padding, padding));
            }
        });
    }

    private void showFields() {
        curr_element_type = choiceBox.getValue();
        if (curr_element_type.equals("Libro")) {
            editorText.setText("Casa editrice: ");
            authorText.setText("Autore: ");
            authorBox.setVisible(true);
            bookBox.setVisible(true);
            digitalMediaBox.setVisible(false);
            periodicPublicationBox.setVisible(false);
        } else if (curr_element_type.equals("Digital Media")) {
            editorText.setText("Casa produttrice: ");
            authorText.setText("Direttore: ");
            authorBox.setVisible(true);
            bookBox.setVisible(false);
            digitalMediaBox.setVisible(true);
            periodicPublicationBox.setVisible(false);

        } else if (curr_element_type.equals("Periodico")) {
            editorText.setText("Casa editrice: ");
            authorBox.setVisible(false);
            bookBox.setVisible(false);
            digitalMediaBox.setVisible(false);
            periodicPublicationBox.setVisible(true);

        }
    }

    private void showDays() {
        int curr_month = monthBox.getValue();
        int curr_year;
        if (yearField.getText().isEmpty()) {
            curr_year = 0;
        } else {
            curr_year = Integer.parseInt(yearField.getText());
        }
        if (curr_month == 2) {
            dayBox.getItems().setAll(Arrays.copyOfRange(days, 0, 28));
            if (curr_year % 4 == 0 && curr_year % 100 != 0) {
                dayBox.getItems().add(days[28]);
            }
        } else if (curr_month == 4 || curr_month == 6 || curr_month == 9 || curr_month == 11) {
            dayBox.getItems().setAll(Arrays.copyOfRange(days, 0, 29));
        } else {
            dayBox.getItems().setAll(days);
        }
    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;

    }

    public void goBack() {

        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.setScene(previousScene);

    }

    public void handleAddButton() {
        checkGenres();
        if (curr_element_type.equals("Libro")) {
            element = new Book(titleField.getText(),
                    Integer.valueOf(yearField.getText()),
                    "",
                    Integer.valueOf(quantityField.getText()),
                    Integer.valueOf(quantityField.getText()),
                    Integer.valueOf(lengthField.getText()),
                    new LinkedList<Genre>(),
                    isbnField.getText(),
                    authorField.getText(),
                    publisherField.getText(),
                    Integer.valueOf(editionField.getText()));
        } else if (curr_element_type.equals("Digital Media")) {
            element = new DigitalMedia(titleField.getText(),
                    Integer.valueOf(yearField.getText()),
                    "",
                    Integer.valueOf(quantityField.getText()),
                    Integer.valueOf(quantityField.getText()),
                    Integer.valueOf(lengthField.getText()),
                    new LinkedList<Genre>(),
                    publisherField.getText(),
                    0,
                    authorField.getText());

        } else if (curr_element_type.equals("Periodico")) {
            element = new PeriodicPublication(titleField.getText(),
                    Integer.valueOf(yearField.getText()),
                    "",
                    Integer.valueOf(quantityField.getText()),
                    Integer.valueOf(quantityField.getText()),
                    Integer.valueOf(lengthField.getText()),
                    new LinkedList<Genre>(),
                    publisherField.getText(),
                    Integer.valueOf(frequencyField.getText()),
                    monthBox.getValue(),
                    dayBox.getValue(),
                    issnField.getText());
        }

        if (libraryAdminController.addElement(element)) {

            showAlert("Aggiunta elemento", "Elemento aggiunto con successo", AlertType.INFORMATION);

        } else {

            showAlert("Aggiunta elemento", "Errore: elemento non aggiunto", AlertType.ERROR);

        }

    }

    private Boolean checkGenres() {
        String[] genres = genresField.getText().split(",");
        GenreManager genreManager = new GenreManager();
        LinkedList<Genre> genresList;
        try{
            genresList = genreManager.getAllGenres();
        } catch (SQLException e) {
            showAlert("Database Connection Error", "Non è possibile collegarsi al database", AlertType.NONE);
            return false;
        }
        String startingMessage = "I seguenti generi non sono presenti nel database: ";
        String message = new String(startingMessage);
        for (String genre : genres) {
            genre = genre.toLowerCase().trim();
            if (!genresList.contains(genre)){
                message = message + (genre + " ");
            }
        }
        if(!startingMessage.equals(message)) {
            showAlert("Generi mancanti", message, AlertType.NONE);
            return false;
        }
        else {
            return true;
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

        quantityField.setTextFormatter(new TextFormatter<>(onlyNumbersFilter));
        editionField.setTextFormatter(new TextFormatter<>(onlyNumbersFilter));
        lengthField.setTextFormatter(new TextFormatter<>(onlyNumbersFilter));
        isbnField.setTextFormatter(new TextFormatter<>(isbnFilter));
        issnField.setTextFormatter(new TextFormatter<>(isbnFilter));
        yearField.setTextFormatter(new TextFormatter<>(yearFilter));
        titleField.setTextFormatter(new TextFormatter<>(totalLengthFilter));
        authorField.setTextFormatter(new TextFormatter<>(totalLengthFilter));
        producerField.setTextFormatter(new TextFormatter<>(totalLengthFilter));
        frequencyField.setTextFormatter(new TextFormatter<>(totalLengthFilter));
        ageField.setTextFormatter(new TextFormatter<>(ageFilter));

    }


}
