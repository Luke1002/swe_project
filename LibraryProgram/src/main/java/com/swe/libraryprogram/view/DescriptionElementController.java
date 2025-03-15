package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.LibraryAdminController;
import com.swe.libraryprogram.controller.LibraryUserController;
import com.swe.libraryprogram.controller.UserController;
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

    @FXML
    private Button returnButton;

    private Scene previousScene;

    private User user;

    private Element element;

    private UserController userController;

    private ElementManager elementManager;


    @FXML
    private void initialize() {

        elementManager = new ElementManager();

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

        welcomeText.setText("Benvenuto, " + user.getName() + "!");

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

        LibraryUserController userController = (LibraryUserController) this.userController;

        if (userController.borrowElement(element.getId())) {
            showAlert("Prestito dell'elemento", "Elemento preso!", AlertType.INFORMATION);

        } else {
            showAlert("Prestito dell'elemento", "Errore durante il prestito dell'elemento", AlertType.ERROR);

        }

    }

    private void handleEditAction() {

        //TODO: implementare la modifica dell'elemento, va chiamata un'altra vista

    }

    private void handleRemoveAction() {

        try {

            if (elementManager.removeElement(element.getId())) {

                showAlert("Rimozione dell'elemento", "Elemento rimosso con successo", AlertType.INFORMATION);
                goBack();

            }

        } catch (SQLException e) {

            showAlert("Rimozione dell'elemento", "Errore durante la rimozione dell'elemento", AlertType.ERROR);
            e.printStackTrace();

        }

    }

    private void updateView() {

        if (user.isAdmin()) {

            editButton.setVisible(true);
            removeButton.setVisible(true);
            takeButton.setVisible(false);

        } else {

            editButton.setVisible(false);
            removeButton.setVisible(false);
            takeButton.setVisible(true);

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

    public void setUser(User user) {

        if (this.user != null && this.user.equals(user)) {

            System.out.println("Utente già impostato.");
            return;

        }

        this.user = user;

        if (user.isAdmin()) {
            userController = new LibraryAdminController(user);

        } else {
            userController = new LibraryUserController(user);

        }

    }

    public void setElement(Element element) {

        this.element = element;

        titleText.setText(element.getTitle());
        yearText.setText(element.getReleaseYear().toString());
        lengthText.setText(element.getLength().toString());
        genresText.setText(element.getGenres().toString());

        descriptionFlow.getChildren().add(new Text(element.getDescription()));

        if (element instanceof Book) {

            Book book = (Book) element;

            ISBNText.setText(book.getIsbn().toString());
            authorText.setText(book.getAuthor());
            publisherText.setText(book.getPublisher());
            editionText.setText(book.getEdition().toString());

        } else if (element instanceof DigitalMedia) {

            DigitalMedia digitalMedia = (DigitalMedia) element;

            producerText.setText(digitalMedia.getProducer());
            ageRatingText.setText(digitalMedia.getAgeRating().toString());
            directorText.setText(digitalMedia.getDirector());

        } else if (element instanceof PeriodicPublication) {

            PeriodicPublication periodicPublication = (PeriodicPublication) element;

            frequencyText.setText(periodicPublication.getFrequency().toString());
            releaseMonthText.setText(periodicPublication.getReleaseMonth().toString());
            releaseDayText.setText(periodicPublication.getReleaseDay().toString());
            ISSNText.setText(periodicPublication.getIssn().toString());
            publisherText.setText(periodicPublication.getPublisher());

        }

    }

    private void showAlert(String title, String message, AlertType type) {

        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    private void goBack() {

        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.setScene(previousScene);
        stage.show();

    }

    private void setPreviousScene(Scene scene) {
        this.previousScene = scene;

    }


}
