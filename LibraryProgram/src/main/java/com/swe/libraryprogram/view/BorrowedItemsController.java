package com.swe.libraryprogram.view;

import com.swe.libraryprogram.services.MainController;
import com.swe.libraryprogram.domainmodel.Element;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class BorrowedItemsController extends BaseViewController {

    @FXML
    private Button closeButton;

    @FXML
    private TableView<Element> borrowedElementsTable;

    @FXML
    private TableColumn<Element, String> titleColumn;

    private ObservableList<Element> borrowedElements = FXCollections.observableArrayList();

    @FXML
    protected void initialize() {
        super.initialize();
        closeButton.setOnAction(event -> handleCloseButton());

        // Collega le colonne ai campi della classe Element
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        borrowedElementsTable.setRowFactory(tv -> {
            TableRow<Element> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    MainController.getInstance().setSelectedElementId(row.getItem().getId());
                    mainViewController.loadBottomPane("descriptionElement");
                }
            });
            return row;
        });
        loadBorrowedElementsData();

    }

    private void loadBorrowedElementsData() {
        List<Element> borrowedElementsList;
        try {
            borrowedElementsList = MainController.getInstance().getBorrowsManager().getBorrowedElementsForUser(MainController.getInstance().getUser().getEmail());
        } catch (SQLException e) {
            showAlert("Errore", "Connessione al database non riuscita");
            borrowedElementsList = new ArrayList<>();
        }

        borrowedElements.setAll(borrowedElementsList);
        borrowedElementsTable.setItems(borrowedElements);
    }

    private void handleCloseButton() {
        mainViewController.loadBottomPane("home");
    }


}
