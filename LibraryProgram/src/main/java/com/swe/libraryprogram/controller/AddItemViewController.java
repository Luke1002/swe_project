package com.swe.libraryprogram.controller;

import com.swe.libraryprogram.domainmodel.*;
import com.swe.libraryprogram.service.LibraryAdminService;
import com.swe.libraryprogram.service.MainService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.IntStream;


public class AddItemViewController extends ElementCheckViewController {

    @FXML
    private TextField titleField, yearField, quantityField, quantityAvailableField, lengthField, genresField, publisherField;

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
    private HBox authorBox, typeChoice;

    @FXML
    private Label editorText, authorText;

    @FXML
    private Button addButton, cancelButton;

    @FXML
    private TextArea descriptionArea;

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private ChoiceBox<Integer> dayBox, monthBox;

    private String elementType;
    private Element currElement;
    private Integer currElementId;
    private final Integer[] days = IntStream.rangeClosed(1, 31).boxed().toArray(Integer[]::new);

    UnaryOperator<TextFormatter.Change> quantityAvailableFilter = change -> {
        String newText = change.getControlNewText();

        if (newText.matches("\\d*") && !newText.startsWith("0")) {
            if (!newText.isEmpty()) {
                try {
                    Integer value = Integer.parseInt(newText);
                    try {
                        if (value > Integer.valueOf(quantityAvailableField.getText())) {
                            change.setText(quantityField.getText());
                            change.setRange(0, change.getControlText().length());
                        }
                    } catch (NumberFormatException e) {
                        return null;
                    }
                    return change;
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return change;
        } else {
            return null;
        }
    };

    @FXML
    protected void initialize() {
        super.initialize();
        setFieldsRestrictions();
        addButton.setOnAction(event -> handleSaveButton());
        cancelButton.setOnAction(event -> handleCancelButton());
        choiceBox.getItems().setAll("Libro", "Film", "Periodico");
        choiceBox.setOnAction(event -> showFields());
        monthBox.getItems().setAll(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        monthBox.setOnAction(event -> showDays());
        currElementId = MainService.getInstance().getSelectedElementId();
        if (currElementId == null) {
            choiceBox.setValue(choiceBox.getItems().getFirst());
            yearField.setText(String.valueOf(LocalDate.now().getYear()));
        } else {
            currElement = MainService.getInstance().getSelectedElement();
            titleField.setText(currElement.getTitle());
            descriptionArea.setText(currElement.getDescription());
            yearField.setText(currElement.getReleaseYear() == null ? "" : currElement.getReleaseYear().toString());
            lengthField.setText(currElement.getLength() == null ? "" : currElement.getLength().toString());
            quantityField.setText(currElement.getQuantity().toString());
            quantityAvailableField.setText(currElement.getQuantityAvailable().toString());
            genresField.setText(currElement.getGenresAsString());
            if (currElement instanceof Book) {
                choiceBox.setValue(choiceBox.getItems().getFirst());
                authorField.setText(((Book) currElement).getAuthor());
                editionField.setText(((Book) currElement).getEdition() == null ? "" : ((Book) currElement).getEdition().toString());
                publisherField.setText(((Book) currElement).getPublisher());
                isbnField.setText(((Book) currElement).getIsbn());
            } else if (currElement instanceof DigitalMedia) {
                choiceBox.setValue(choiceBox.getItems().get(1));
                producerField.setText(((DigitalMedia) currElement).getProducer());
                ageField.setText(((DigitalMedia) currElement).getAgeRating());
                authorField.setText(((DigitalMedia) currElement).getDirector());
            } else if (currElement instanceof PeriodicPublication) {
                choiceBox.setValue(choiceBox.getItems().get(2));
                publisherField.setText(((PeriodicPublication) currElement).getPublisher());
                frequencyField.setText(((PeriodicPublication) currElement).getFrequency());
                if (((PeriodicPublication) currElement).getReleaseMonth() != null) {
                    monthBox.setValue(monthBox.getItems().get(((PeriodicPublication) currElement).getReleaseMonth() - 1));
                }
                if (((PeriodicPublication) currElement).getReleaseDay() != null) {
                    dayBox.setValue(dayBox.getItems().get(((PeriodicPublication) currElement).getReleaseDay() - 1));
                }
            }
            typeChoice.setDisable(true);
        }
    }

    private void handleCancelButton() {
        if (currElement != null) {
            goBack();
        } else {
            mainViewController.loadBottomPane("home");
        }
    }

    protected void showFields() {
        elementType = choiceBox.getValue();
        if (elementType.equals("Libro")) {
            editorText.setText("Casa editrice: ");
            authorText.setText("Autore: ");
            authorBox.setDisable(false);
            bookBox.setDisable(false);
            digitalMediaBox.setDisable(true);
            periodicPublicationBox.setDisable(true);
        } else if (elementType.equals("Film")) {
            editorText.setText("Casa produttrice: ");
            authorText.setText("Direttore: ");
            authorBox.setDisable(false);
            bookBox.setDisable(true);
            digitalMediaBox.setDisable(false);
            periodicPublicationBox.setDisable(true);

        } else if (elementType.equals("Periodico")) {
            editorText.setText("Casa editrice: ");
            authorBox.setDisable(true);
            bookBox.setDisable(true);
            digitalMediaBox.setDisable(true);
            periodicPublicationBox.setDisable(false);

        }
    }

    protected void showDays() {
        Integer curr_month = monthBox.getValue();
        int curr_year;
        if (yearField.getText().isEmpty()) {
            curr_year = 0;
        } else {
            curr_year = Integer.parseInt(yearField.getText());
        }
        if (curr_month == null || Arrays.asList(new Integer[]{1, 3, 5, 7, 8, 10, 12}).contains(curr_month)) {
            dayBox.getItems().setAll(days);
        } else if (curr_month == 2) {
            dayBox.getItems().setAll(Arrays.copyOfRange(days, 0, 28));
            if (curr_year % 4 == 0 && curr_year % 100 != 0) {
                dayBox.getItems().add(days[28]);
            }
        } else {
            dayBox.getItems().setAll(Arrays.copyOfRange(days, 0, 29));
        }
    }

    public void handleSaveButton() {
        if (checkRequiredValues()) {
            List<Genre> genreList = checkGenres();
            Element element = null;
            Integer year = yearField.getText().isEmpty() ? null : Integer.valueOf(yearField.getText());
            Integer quantity = quantityField.getText().isEmpty() ? 1 : Integer.valueOf(quantityField.getText());
            Integer quantityAvailable = quantityField.getText().isEmpty() ? quantity : Integer.valueOf(quantityField.getText());
            Integer length = lengthField.getText().isEmpty() ? null : Integer.valueOf(lengthField.getText());
            Integer edition = editionField.getText().isEmpty() ? 1 : Integer.valueOf(editionField.getText());
            if (currElement == null) {
                if (elementType.equals("Libro")) {
                    element = new Book(titleField.getText(),
                            year,
                            descriptionArea.getText(),
                            quantity,
                            quantityAvailable,
                            length,
                            genreList,
                            isbnField.getText(),
                            authorField.getText(),
                            publisherField.getText(),
                            edition);
                } else if (elementType.equals("Film")) {
                    element = new DigitalMedia(titleField.getText(),
                            year,
                            descriptionArea.getText(),
                            quantity,
                            quantityAvailable,
                            length,
                            genreList,
                            publisherField.getText(),
                            ageField.getText(),
                            authorField.getText());

                } else if (elementType.equals("Periodico")) {

                    element = new PeriodicPublication(titleField.getText(),
                            year,
                            descriptionArea.getText(),
                            quantity,
                            quantityAvailable,
                            length,
                            genreList,
                            publisherField.getText(),
                            frequencyField.getText(),
                            monthBox.getValue(),
                            dayBox.getValue());
                }
            } else {
                quantityAvailable = quantityField.getText().isEmpty() ? (quantity - (currElement.getQuantity() - MainService.getInstance().getSelectedElement().getQuantityAvailable())) : Integer.valueOf(quantityField.getText());
                currElement.setTitle(titleField.getText());
                currElement.setGenres(genreList);
                currElement.setDescription(descriptionArea.getText());
                currElement.setReleaseYear(year);
                currElement.setLength(length);
                currElement.setQuantity(quantity);
                currElement.setQuantityAvailable(quantityAvailable);
                if (currElement instanceof Book) {
                    ((Book) currElement).setAuthor(authorField.getText());
                    ((Book) currElement).setPublisher(publisherField.getText());
                    ((Book) currElement).setEdition(edition);
                    ((Book) currElement).setIsbn(isbnField.getText());
                } else if (currElement instanceof DigitalMedia) {
                    ((DigitalMedia) currElement).setAgeRating(ageField.getText());
                    ((DigitalMedia) currElement).setDirector(authorField.getText());
                    ((DigitalMedia) currElement).setProducer(publisherField.getText());
                } else if (currElement instanceof PeriodicPublication) {
                    ((PeriodicPublication) currElement).setPublisher(publisherField.getText());
                    ((PeriodicPublication) currElement).setFrequency(frequencyField.getText());
                    ((PeriodicPublication) currElement).setReleaseDay(dayBox.getValue());
                    ((PeriodicPublication) currElement).setReleaseMonth(monthBox.getValue());
                }
            }

            if (MainService.getInstance().getUserService() instanceof LibraryAdminService && currElement == null) {
                if (((LibraryAdminService) MainService.getInstance().getUserService()).addElement(element)) {

                    showAlert("Aggiunta elemento", "Elemento aggiunto con successo");
                    goBack();

                } else {

                    showAlert("Aggiunta elemento", "Errore: elemento non aggiunto");

                }
            } else if (MainService.getInstance().getUserService() instanceof LibraryAdminService && currElement != null) {
                if (((LibraryAdminService) MainService.getInstance().getUserService()).updateElement(currElement)) {

                    showAlert("Modifica elemento", "Elemento modificato con successo");
                    goBack();

                } else {

                    showAlert("Modifica elemento", "Errore: elemento non modificato");

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
        }
        if (elementType.equals("Libro") && isbnField.getText().length() < 13) {
            showAlert("Errore", "Codice ISBN non valido.");
            return false;
        }
        Integer quantity = quantityField.getText().isEmpty() ? 1 : Integer.valueOf(quantityField.getText());
        Integer quantityAvailable = quantityField.getText().isEmpty() ? quantity : Integer.valueOf(quantityField.getText());
        if (currElement != null &&
                ((quantity - quantityAvailable) != (currElement.getQuantity() - currElement.getQuantityAvailable()))) {
            showAlert("Errore", "Differenza tra quantità totale e quantità disponibile minore della precedente.");
            return false;
        }
        return true;
    }

    private List<Genre> checkGenres() {
        if (genresField.getText().isEmpty()) {
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
        List<Genre> allGenres = MainService.getInstance().getAllGenres();
        if (allGenres == null) {
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

    private void setFieldsRestrictions() {

        quantityField.setTextFormatter(new TextFormatter<>(onlyNumbersFilter));
        quantityAvailableField.setTextFormatter(new TextFormatter<>(quantityAvailableFilter));
        editionField.setTextFormatter(new TextFormatter<>(onlyNumbersFilter));
        lengthField.setTextFormatter(new TextFormatter<>(onlyNumbersFilter));
        isbnField.setTextFormatter(new TextFormatter<>(isbnFilter));
        yearField.setTextFormatter(new TextFormatter<>(yearFilter));
        titleField.setTextFormatter(new TextFormatter<>(totalLength64Filter));
        authorField.setTextFormatter(new TextFormatter<>(totalLength64Filter));
        producerField.setTextFormatter(new TextFormatter<>(totalLength64Filter));
        frequencyField.setTextFormatter(new TextFormatter<>(totalLength64Filter));
        ageField.setTextFormatter(new TextFormatter<>(totalLength64Filter));
        descriptionArea.setTextFormatter(new TextFormatter<>(totalLength256Filter));

    }

    UnaryOperator<TextFormatter.Change> yearFilter = change -> {
        String newText = change.getControlNewText();

        if (newText.matches("\\d*") && newText.length() <= 4) {
            showDays();
            return change;
        } else {
            return null;
        }
    };


}
