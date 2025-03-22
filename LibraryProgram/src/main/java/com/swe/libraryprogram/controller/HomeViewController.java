package com.swe.libraryprogram.controller;


import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.service.MainService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Arrays;
import java.util.List;


public class HomeViewController extends ElementCheckViewController {
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

    List<Element> totalElements;
    ObservableList<Element> observableList = FXCollections.observableArrayList();

    @FXML
    protected void initialize() {
        super.initialize();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        quantityAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("quantityAvailable"));
        genresColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenresAsString()));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
        elementsTable.setRowFactory(_ -> {
            TableRow<Element> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    MainService.getInstance().setSelectedElementId(row.getItem().getId());
                    mainViewController.loadBottomPane("descriptionElement");
                }
            });
            return row;
        });
        totalElements = MainService.getInstance().getUserService().getAllElements();
        if (totalElements == null) {
            showAlert("Errore", "Connessione al database non riuscita");
        } else {
            titleFilterField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
            genresFilterField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
            yearFilterField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());
            isAvailableFilter.selectedProperty().addListener((observable, oldValue, newValue) -> applyFilters());
            lengthFilterField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());

            observableList.setAll(totalElements);
        }
        elementsTable.setItems(observableList);

        titleFilterField.setTextFormatter(new TextFormatter<>(totalLength64Filter));
        genresFilterField.setTextFormatter(new TextFormatter<>(totalLength256Filter));
        yearFilterField.setTextFormatter(new TextFormatter<>(yearFilter));
        lengthFilterField.setTextFormatter(new TextFormatter<>(onlyNumbersFilter));
    }

    private void applyFilters() {
        String titleFilter = titleFilterField.getText().trim().toLowerCase();
        List<String> genresFilter = Arrays.stream(genresFilterField.getText().split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
        Integer yearFilter = yearFilterField.getText().isEmpty() ? null : Integer.parseInt(yearFilterField.getText());
        Integer lengthFilter = lengthFilterField.getText().isEmpty() ? null : Integer.parseInt(lengthFilterField.getText());
        Boolean isAvailable = isAvailableFilter.isSelected();

        observableList.setAll(MainService.getInstance().getUserService().searchElements(totalElements, titleFilter, genresFilter, yearFilter, lengthFilter, isAvailable));
        elementsTable.refresh();
    }
}
