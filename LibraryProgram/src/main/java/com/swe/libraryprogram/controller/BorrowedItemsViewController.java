package com.swe.libraryprogram.controller;

import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.service.LibraryUserService;
import com.swe.libraryprogram.service.MainService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;


public class BorrowedItemsViewController extends BaseViewController {

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
                    MainService.getInstance().setSelectedElementId(row.getItem().getId());
                    mainViewController.loadBottomPane("descriptionElement");
                }
            });
            return row;
        });
        loadBorrowedElementsData();

    }

    private void loadBorrowedElementsData() {
        List<Element> borrowedElementsList = ((LibraryUserService) MainService.getInstance().getUserService()).getBorrowedElements(MainService.getInstance().getUser());
        if (borrowedElementsList == null) {
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
