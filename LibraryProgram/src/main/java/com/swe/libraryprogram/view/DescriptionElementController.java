package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.*;
import com.swe.libraryprogram.dao.ElementManager;
import com.swe.libraryprogram.domainmodel.*;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import java.sql.SQLException;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;



public class DescriptionElementController extends BaseViewController {

    @FXML
    private MenuButton menuButton;

    @FXML
    private MenuItem action1;

    @FXML
    private MenuItem action2;

    @FXML
    private Text welcomeText;

    @FXML
    private Text titleText, yearText, lengthText, genresText;

    @FXML
    private TextFlow descriptionFlow;

    @FXML
    private Text ISBNText, authorText, publisherText, editionText;

    @FXML
    private Text producerText, ageRatingText, directorText;

    @FXML
    private Text frequencyText, releaseMonthText, releaseDayText, ISSNText;

    @FXML
    private Slider ratingSlider;

    @FXML
    private Text ratingText;

    @FXML
    private Button takeButton;

    @FXML
    private Button editButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button returnButton;

    private Scene previousScene;

    private Element element;


    @FXML
    protected void initialize() {
        super.initialize();
        element = MainController.getInstance().getElementManager().getCompleteElementById(MainController.getInstance().getSelectedElementId());

        action1.setOnAction(event -> handleAction1());
        action2.setOnAction(event -> handleAction2());
        takeButton.setOnAction(event -> handleTakeAction());
        editButton.setOnAction(event -> handleEditAction());
        removeButton.setOnAction(event -> handleRemoveAction());
        returnButton.setOnAction(event -> goBack());

        // Imposta il listener per il rating
        ratingSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                ratingText.setText(String.format("%.1f", newVal))
        );

        //imposta la vista in base al tipo di elemento
        updateView();

    }

    //TODO: da implementare e aggiungere appropriate azioni nel menù a tendina (uguale alla home?!)
    private void handleAction1() {
        System.out.println("Azione 1 selezionata.");
    }

    private void handleAction2() {
        System.out.println("Azione 2 selezionata.");
    }

    private void handleBorrowAction() {

        if (((LibraryUserController)MainController.getInstance().getUserController()).borrowElement(element.getId())) {
            showAlert("Prestito dell'elemento", "Elemento preso!");

        } else {
            showAlert("Prestito dell'elemento", "Errore durante il prestito dell'elemento");

        }

    }

    private void handleEditAction() {

        //TODO: implementare la modifica dell'elemento, va chiamata un'altra vista

    }

    private void handleRemoveAction() {

        try {

            if (MainController.getInstance().getElementManager().removeElement(element.getId())) {

                showAlert("Rimozione dell'elemento", "Elemento rimosso con successo");


            }

        } catch (SQLException e) {

            showAlert("Rimozione dell'elemento", "Errore durante la rimozione dell'elemento");
            e.printStackTrace();

        }

    }
}
