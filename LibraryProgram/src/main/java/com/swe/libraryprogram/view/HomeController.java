package com.swe.libraryprogram.view;


import com.swe.libraryprogram.dao.ConnectionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.swe.libraryprogram.dao.ElementManager;
import com.swe.libraryprogram.domainmodel.Element;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class HomeController extends BaseViewController {


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
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        releaseYearColumn.setCellValueFactory(new PropertyValueFactory<>("releaseYear"));
        quantityAvailableColumn.setCellValueFactory(new PropertyValueFactory<>("quantityAvailable"));
        genresColumn.setCellValueFactory(new PropertyValueFactory<>("genres"));

        loadElementData("", "", "");
    }

  /*  @FXML
    public void initialize() {

        // Collega le colonne ai campi della classe Book
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        // Carica tutti i libri all'avvio


    }*/

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
    private void loadElementData(String title, String genre, String year) {

        //ObservableList<Element> elementList = FXCollections.observableArrayList();
        List<Element> elementsList;
        // Usa ElementManager per recuperare i dati dal database
        try {

            elementsList = elementManager.getAllElements();
        } catch (SQLException e) {
            //TODO handle exception better
            elementsList = new ArrayList<>();
        }

        elements.addAll(elementsList);
        elementsTable.setItems(elements);

    }

}
