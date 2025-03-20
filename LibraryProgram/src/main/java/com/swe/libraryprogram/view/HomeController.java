package com.swe.libraryprogram.view;


import com.swe.libraryprogram.controller.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.swe.libraryprogram.domainmodel.Element;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HomeController extends ElementCheckViewController {
    @FXML
    private TableView<Element> elementsTable;
    @FXML
    private TableColumn<Element, String> titleColumn, genresColumn;
    @FXML
    private TableColumn<Element, Integer> releaseYearColumn, quantityAvailableColumn, lengthColumn;
    @FXML
    private TextField titleFilterField, genresFilterField, yearFilterField, lengthFilterField;

    @FXML
    private CheckBox isAvailableFilter;

    FilteredList<Element> filteredElements;


    @FXML
    protected void initialize() {
        super.initialize();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        quantityAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("quantityAvailable"));
        genresColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenresAsString()));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
        elementsTable.setRowFactory(tv -> {
            TableRow<Element> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    MainController.getInstance().setSelectedElementId(row.getItem().getId());
                    mainViewController.loadBottomPane("descriptionElement");
                }
            });
            return row;
        });
        loadElementsData();

        titleFilterField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters(titleFilterField));
        titleFilterField.setTextFormatter(new TextFormatter<>(totalLength64Filter));
        genresFilterField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters(titleFilterField));
        genresFilterField.setTextFormatter(new TextFormatter<>(totalLength256Filter));
        yearFilterField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters(titleFilterField));
        yearFilterField.setTextFormatter(new TextFormatter<>(yearFilter));
        isAvailableFilter.selectedProperty().addListener((observable, oldValue, newValue) -> applyFilters(titleFilterField));
        lengthFilterField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters(titleFilterField));
        lengthFilterField.setTextFormatter(new TextFormatter<>(onlyNumbersFilter));
    }


    @FXML
    private void loadElementsData() {
        ObservableList<Element> elementsList = FXCollections.observableArrayList();

        List<Element> elements;
        try {
            elements = MainController.getInstance().getElementManager().getAllElements();
        } catch (SQLException e) {
            showAlert("Errore", "Connessione al database non riuscita");
            elements = new ArrayList<>();
        }

        elementsList.setAll(elements);
        filteredElements = new FilteredList<>(elementsList, p -> true);
        titleFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredElements.setPredicate(element -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Mostra tutti gli elementi se il campo è vuoto
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return element.getTitle().toLowerCase().contains(lowerCaseFilter);
            });
        });
        elementsTable.setItems(filteredElements);

        System.out.println("Elementi caricati: " + elements.size());

    }

    private void applyFilters(TextField titleFilterField) {
        filteredElements.setPredicate(element -> {
            String titleFilter = titleFilterField.getText().trim().toLowerCase();
            List<String> genresFilter = Arrays.stream(genresFilterField.getText().split(",")).map(String::trim).filter(s -> !s.isEmpty()).map(String::toLowerCase).toList();
            Integer yearFilter = yearFilterField.getText().isEmpty() ? null : Integer.parseInt(yearFilterField.getText());
            Integer lengthFilter = lengthFilterField.getText().isEmpty() ? null : Integer.parseInt(lengthFilterField.getText());
            Boolean isAvailable = isAvailableFilter.isSelected();

            Boolean titleFilterCompliant = titleFilter.isEmpty() || element.getTitle().toLowerCase().contains(titleFilter);
            Boolean genreFilterCompliant = genresFilter.isEmpty() || genresFilter.stream().allMatch(genre -> element.getGenresAsString().toLowerCase().contains(genre));
            Boolean yearFilterCompliant = (yearFilter == null || (element.getReleaseYear() ==  null || yearFilter < element.getReleaseYear()));
            Boolean lengthFilterCompliant = (lengthFilter == null || (element.getLength() == null || lengthFilter < element.getLength()));
            Boolean isAvailableCompliant = (!isAvailable || (element.getQuantityAvailable() != null && element.getQuantityAvailable() > 0));
            return (titleFilterCompliant && genreFilterCompliant && yearFilterCompliant && lengthFilterCompliant && isAvailableCompliant);
        });
    }
}
