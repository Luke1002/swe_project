package com.swe.libraryprogram.view;

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


    @FXML
    private void initialize() {

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

        //TODO: recuperare l'elemento da visualizzare

        //imposta la vista in base al tipo di elemento
        updateView();

    }

    //TODO: da implementare
    private void handleAction1() {
        System.out.println("Azione 1 selezionata.");
    }

    private void handleAction2() {
        System.out.println("Azione 2 selezionata.");
    }

    private void handleTakeAction() {
        System.out.println("Libro preso!");
    }

    private void handleEditAction() {
        System.out.println("Modifica libro.");
    }

    private void handleRemoveAction() {
        System.out.println("Libro rimosso.");
    }

    private void updateView() {

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
