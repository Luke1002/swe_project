package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.LibraryAdminController;
import com.swe.libraryprogram.controller.LibraryUserController;
import com.swe.libraryprogram.dao.ElementManager;
import com.swe.libraryprogram.dao.UserManager;
import com.swe.libraryprogram.domainmodel.*;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.sql.SQLException;


public class DescriptionElementController {

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

    // utente loggato
    private User user;

    private UserManager userManager;

    //elemento da visualizzare
    private Element element;

    private ElementManager elementManager;

    private LibraryAdminController libraryAdminController;

    private LibraryUserController libraryUserController;


    @FXML
    private void initialize(Integer element_id) {

        action1.setOnAction(event -> handleAction1());
        action2.setOnAction(event -> handleAction2());
        takeButton.setOnAction(event -> handleTakeAction());
        editButton.setOnAction(event -> handleEditAction());
        removeButton.setOnAction(event -> handleRemoveAction());

        // Imposta il listener per il rating
        ratingSlider.valueProperty().addListener((obs, oldVal, newVal) ->
                ratingText.setText(String.format("%.1f", newVal))
        );

        //TODO: chiama il controller per recuperare l'utente loggato
        userManager = new UserManager();

        //TODO: imposta messaggio di benvenuto

        //Recuperare l'elemento da visualizzare
        try {

            elementManager = new ElementManager();
            element = elementManager.getElement(element_id);

        } catch (SQLException e) {

            e.printStackTrace();
            //TODO: handle here

        }

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

    private void handleTakeAction() {

        libraryUserController = new LibraryUserController(user);

        if (libraryUserController.borrowElement(element.getId())) {
            //TODO: handle succesful borrow message

        } else {
           //TODO: handle unsuccesful borrow message

        }

    }

    private void handleEditAction() {

        libraryAdminController = new LibraryAdminController(user);

        if (libraryAdminController.updateElement(element)) {
            updateView();
            //TODO: handle succesful edit message

        } else {
            //TODO: handle unsuccesful edit message

        }

    }

    private void handleRemoveAction() {

        try {

            if (elementManager.removeElement(element.getId())) {
                //TODO: handle removal of element
                //TODO: go back to home view

            }

        } catch (SQLException e) {

            e.printStackTrace();
            //TODO: handle failure

        }

    }

    private void updateView() {

        if (user.isAdmin()) {
            //TODO: handle buttons

        } else {
            //TODO: handle buttons

        }

        if (element instanceof Book) {

            authorText.setVisible(true);
            publisherText.setVisible(true);
            genresText.setVisible(true);
            ISBNText.setVisible(true);

            producerText.setVisible(false);
            ageRatingText.setVisible(false);
            directorText.setVisible(false);

            frequencyText.setVisible(false);
            releaseMonthText.setVisible(false);
            releaseDayText.setVisible(false);
            ISSNText.setVisible(false);

        } else if (element instanceof DigitalMedia) {

            producerText.setVisible(true);
            ageRatingText.setVisible(true);
            directorText.setVisible(true);

            authorText.setVisible(false);
            publisherText.setVisible(false);
            genresText.setVisible(false);

            frequencyText.setVisible(false);
            releaseMonthText.setVisible(false);
            releaseDayText.setVisible(false);
            ISSNText.setVisible(false);

        } else if (element instanceof PeriodicPublication) {

            frequencyText.setVisible(true);
            releaseMonthText.setVisible(true);
            releaseDayText.setVisible(true);
            ISSNText.setVisible(true);
            publisherText.setVisible(true);

            authorText.setVisible(false);
            genresText.setVisible(false);

            producerText.setVisible(false);
            ageRatingText.setVisible(false);
            directorText.setVisible(false);

        }

    }


}
