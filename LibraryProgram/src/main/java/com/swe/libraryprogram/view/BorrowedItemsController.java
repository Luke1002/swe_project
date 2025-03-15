package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.LibraryUserController;
import com.swe.libraryprogram.dao.BorrowsManager;
import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.User;
import com.swe.libraryprogram.domainmodel.Genre;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.stream.Collectors;



public class BorrowedItemsController {

    @FXML
    private Button returnButton, reportButton, backButton;

    @FXML
    private TableView<Element> tableView;

    @FXML
    private TableColumn<Element, String> titleColumn;

    @FXML
    private TableColumn<Element, String> yearColumn;

    @FXML
    private TableColumn<Element, String> lenghtColumn;

    @FXML
    private TableColumn<Element, String> genreColumn;

    private User user;

    private Scene previousScene;

    private ObservableList<Element> elements;

    private BorrowsManager borrowsManager;

    private LibraryUserController libraryUserController;


    @FXML
    public void initialize() {

        borrowsManager = new BorrowsManager();

        reportButton.setOnAction(event -> handleReport());
        backButton.setOnAction(event -> goBack());
        returnButton.setOnAction(event -> handleReturn());

        // Collega le colonne ai campi della classe Element
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        yearColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReleaseYear().toString())
        );
        lenghtColumn.setCellValueFactory(new PropertyValueFactory<>("lenght"));

        // Colonna per i generi
        genreColumn.setCellValueFactory(cellData -> {
            Element element = cellData.getValue();
            String genres = element.getGenres().stream()
                    .map(Genre::getName) // Ottiene i nomi dei generi
                    .collect(Collectors.joining(", ")); // Unisce i nomi con una virgola
            return new SimpleStringProperty(genres);
        });

    }

    private void handleReport() {
        System.out.println("Generating report...");

        //TODO: implementare (in modo simile a handleReturn)
    }

    private void handleReturn() {

        Element selectedElement = tableView.getSelectionModel().getSelectedItem();

        if (selectedElement != null) {

            if (libraryUserController.returnElement(selectedElement.getId())) {

                elements.remove(selectedElement);
                tableView.refresh();

                showAlert("Restituzione elemento", "Elemento restituito con successo.", Alert.AlertType.INFORMATION);

            } else {
                showAlert("Restituzione elemento", "Errore durante la restituzione dell'elemento.", Alert.AlertType.ERROR);

            }

        } else {
            showAlert("Restituzione elemento", "Devi selezionare un elemento prima di restituirlo.", Alert.AlertType.WARNING);

        }

    }

    public void setUser(User user) {

        this.user = user;
        libraryUserController = new LibraryUserController(user);

        getBorrowedElements();

    }

    public void goBack() {

        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.setScene(previousScene);

    }

    public void setPreviousScene(Scene scene) {
        this.previousScene = scene;

    }

    public void getBorrowedElements() {

        try {

            elements = FXCollections.observableArrayList(borrowsManager.getBorrowedElementsForUser(user.getEmail()));
            tableView.setItems(elements);
            tableView.refresh();

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }

    private void showAlert(String title, String message, Alert.AlertType type) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }


}
