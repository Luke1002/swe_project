package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.LibraryAdminController;
import com.swe.libraryprogram.controller.MainController;
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

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;


public class AddItemController extends ElementCheckViewController {

    @FXML
    private TextField titleField, descriptionField, yearField, quantityField, lengthField, genresField, publisherField;

    @FXML
    private TextField isbnField, editionField, authorField;

    @FXML
    private TextField frequencyField;

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

    private LibraryAdminController libraryAdminController;

    private final Integer[] days = IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new);

    @FXML
    protected void initialize() {
        super.initialize();
        setFieldsRestrictions();

        libraryAdminController = new LibraryAdminController();

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

    @Override
    protected void showFields() {
        curr_element_type = choiceBox.getValue();
        if (curr_element_type.equals("Libro")) {
            editorText.setText("Casa editrice: ");
            authorText.setText("Autore: ");
            authorBox.setDisable(false);
            bookBox.setDisable(false);
            digitalMediaBox.setDisable(true);
            periodicPublicationBox.setDisable(true);
        } else if (curr_element_type.equals("Digital Media")) {
            editorText.setText("Casa produttrice: ");
            authorText.setText("Direttore: ");
            authorBox.setDisable(false);
            bookBox.setDisable(true);
            digitalMediaBox.setDisable(false);
            periodicPublicationBox.setDisable(true);

        } else if (curr_element_type.equals("Periodico")) {
            editorText.setText("Casa editrice: ");
            authorBox.setDisable(true);
            bookBox.setDisable(true);
            digitalMediaBox.setDisable(true);
            periodicPublicationBox.setDisable(false);

        }
    }

    @Override
    protected void showDays() {
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

    }

    public void handleAddButton() {
        if (checkRequiredValues()) {
            List<Genre> genreList = checkGenres();
            Element element = null;
            Integer year = yearField.getText().isEmpty() ? null : Integer.valueOf(yearField.getText());
            Integer quantity = quantityField.getText().isEmpty() ? 1 : Integer.valueOf(quantityField.getText());
            Integer length = lengthField.getText().isEmpty() ? null : Integer.valueOf(lengthField.getText());
            if (curr_element_type.equals("Libro")) {
                Integer edition = editionField.getText().isEmpty() ? 1 : Integer.valueOf(editionField.getText());
                element = new Book(titleField.getText(),
                        year,
                        "",
                        quantity,
                        quantity,
                        length,
                        genreList,
                        isbnField.getText(),
                        authorField.getText(),
                        publisherField.getText(),
                        edition);
            } else if (curr_element_type.equals("Digital Media")) {
                element = new DigitalMedia(titleField.getText(),
                        year,
                        "",
                        quantity,
                        quantity,
                        length,
                        genreList,
                        publisherField.getText(),
                        ageField.getText(),
                        authorField.getText());

            } else if (curr_element_type.equals("Periodico")) {
                element = new PeriodicPublication(titleField.getText(),
                        year,
                        "",
                        quantity,
                        quantity,
                        length,
                        genreList,
                        publisherField.getText(),
                        frequencyField.getText(),
                        monthBox.getValue(),
                        dayBox.getValue());
            }
            if (MainController.getInstance().getUserController() instanceof LibraryAdminController) {
                if (((LibraryAdminController) MainController.getInstance().getUserController()).addElement(element)) {

                    showAlert("Aggiunta elemento", "Elemento aggiunto con successo");
                    goBack();

                } else {

                    showAlert("Aggiunta elemento", "Errore: elemento non aggiunto");

                }
            } else {
                showAlert("Errore", "User should not be here. Going back to Home");
                mainViewController.loadBottomPane("home");
            }
        }


    }

    private boolean checkRequiredValues() {
        if (titleField.getText().isEmpty()) {
            showAlert("Errore", "Titolo è un campo obbligatorio.");
            return false;
        } else if (curr_element_type.equals("Libro")) {
            if (isbnField.getText().length() < 13) {
                showAlert("Errore", "Codice ISBN non valido.");
                return false;
            } else {
                try {
                    if ((MainController.getInstance().getBookManager().getBookByIsbn(isbnField.getText())) != null) {
                        showAlert("Errore", "Elemento con stesso ISBN già presente.");
                        return false;
                    }
                } catch (SQLException e) {
                    showAlert("Errore", "Errore nella connessione al database.");
                    return false;
                }
            }
        }
        return true;
    }

    private List<Genre> checkGenres() {
        if(genresField.getText().isEmpty()) {
            return new ArrayList<>();
        }
        String[] gatheredGenres = genresField.getText().split(",");
        List<String> genreNames = new ArrayList<>();

        for (String genreName : gatheredGenres) {
            String formattedName = genreName.toLowerCase().trim();
            if (!genreNames.contains(formattedName)) {
                genreNames.add(formattedName);
            }
        }
        List<Genre> allGenres;

        try {
            allGenres = MainController.getInstance().getGenreManager().getAllGenres();
        } catch (SQLException e) {
            showAlert("Database Connection Error", "Non è possibile collegarsi al database");
            return null;
        }

        String message = "I seguenti generi non sono presenti nel database: ";

        List<String> missingGenres = new ArrayList<>(genreNames);  // Inizializza con tutti i generi
        for (Genre genre : allGenres) {
            missingGenres.removeIf(genreName -> genreName.equals(genre.getName()));
        }
        if (!missingGenres.isEmpty()) {
            message += String.join(", ", missingGenres);
            showAlert("Generi mancanti", message);
            return null;
        } else {
            List<Genre> genreList = new ArrayList<>();
            for (String genreName : genreNames) {
                for (Genre genre : allGenres) {
                    if (genreName.equals(genre.getName().toLowerCase())) {
                        genreList.add(genre);
                    }
                }
            }
            return genreList;
        }
    }
        private void setFieldsRestrictions () {

            quantityField.setTextFormatter(new TextFormatter<>(onlyNumbersFilter));
            editionField.setTextFormatter(new TextFormatter<>(onlyNumbersFilter));
            lengthField.setTextFormatter(new TextFormatter<>(onlyNumbersFilter));
            isbnField.setTextFormatter(new TextFormatter<>(isbnFilter));
            yearField.setTextFormatter(new TextFormatter<>(yearFilter));
            titleField.setTextFormatter(new TextFormatter<>(totalLengthFilter));
            authorField.setTextFormatter(new TextFormatter<>(totalLengthFilter));
            producerField.setTextFormatter(new TextFormatter<>(totalLengthFilter));
            frequencyField.setTextFormatter(new TextFormatter<>(totalLengthFilter));
            ageField.setTextFormatter(new TextFormatter<>(ageFilter));

        }


    }
