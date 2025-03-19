package com.swe.libraryprogram.view;


import com.swe.libraryprogram.controller.MainController;
import com.swe.libraryprogram.dao.ConnectionManager;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.swe.libraryprogram.dao.ElementManager;
import com.swe.libraryprogram.domainmodel.Element;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class HomeController extends BaseViewController {

    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TableView<Element> elementsTable;
    @FXML
    private TableColumn<Element, String> titleColumn;
    @FXML
    private TableColumn<Element, Integer> releaseYearColumn;
    @FXML
    private TableColumn<Element, Integer> quantityAvailableColumn;
    @FXML
    private TableColumn<Element, String> genresColumn;
    @FXML
    private TableColumn<Element, Integer> lengthColumn;
    @FXML
    private TextField titleFilter;
    @FXML
    private TextField genreFilter;
    @FXML
    private TextField yearFilter;
    @FXML
    private Button searchButton;

    private ObservableList<Element> elements = FXCollections.observableArrayList();
    private ElementManager elementManager = new ElementManager();


    @FXML
    protected void initialize() {
        super.initialize();
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        quantityAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("quantityAvailable"));
        genresColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGenresAsString()));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
        loadElementData();
    }

    @FXML
    private void onSearchButtonClick() {

        String title = titleFilter.getText().trim();
        String year = yearFilter.getText().trim();
        //String genre = genreFilter.getText().trim();

        //leggo una lista di generi inserita dall'utente e separata da virgole
        List<String> genres = Arrays.stream(genreFilter.getText().trim().split(","))
                .map(String::trim)
                .collect(Collectors.toList());
        //TODO fare getFilteredElements nel manager
        //List<Element> filteredElements = elementManager.getFilteredElements(title, genres, year);
        //elementsTable.setItems(FXCollections.observableArrayList(filteredElements));

    }

    @FXML
    private void loadElementData() {

        //ObservableList<Element> elementList = FXCollections.observableArrayList();
        List<Element> elementsList;
        // Usa ElementManager per recuperare i dati dal database
        try {
            elementsList = MainController.getInstance().getElementManager().getAllElements();
        } catch (SQLException e) {
            showAlert("Errore", "Connessione al database non riuscita");
            elementsList = new ArrayList<>();
        }

        elements.setAll(elementsList);
        elementsTable.setItems(elements);

        System.out.println("Elementi caricati: " + elements.size());

    }

}
