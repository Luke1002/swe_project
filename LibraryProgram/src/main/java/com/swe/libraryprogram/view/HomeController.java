package com.swe.libraryprogram.view;


import com.swe.libraryprogram.dao.ConnectionManager;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import com.swe.libraryprogram.dao.ElementManager;
import com.swe.libraryprogram.domainmodel.Element;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;



public class HomeController {

    @FXML
    private TextField titleField;

    @FXML
    private TextField genreField;

    @FXML
    private TextField yearField;

    @FXML
    private TableView<Element> elementTable;

    @FXML
    private TableColumn<Element, String> titleColumn;

    @FXML
    private TableColumn<Element, String> genreColumn;

    @FXML
    private TableColumn<Element, Integer> yearColumn;


    private ElementManager elementManager = new ElementManager(ConnectionManager.getInstance());



    @FXML
    public void initialize() {

        // Collega le colonne ai campi della classe Book
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        // Carica tutti i libri all'avvio
        loadElementData("", "", "");

    }

    @FXML
    private void onSearchButtonClick() {

        String title = titleField.getText();
        String genre = genreField.getText();
        String year = yearField.getText();

        loadElementData(title, genre, year);

    }

    @FXML
    private void loadElementData(String title, String genre, String year) {

        ObservableList<Element> elementList = FXCollections.observableArrayList();

        // Usa ElementManager per recuperare i dati dal database
        List<Element> elements = elementManager.getAllElements();

        elementList.addAll(elements);
        elementTable.setItems(elementList);

    }

}
