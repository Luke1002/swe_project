package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.*;
import com.swe.libraryprogram.domainmodel.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.IOException;
import java.sql.SQLException;


public class DescriptionElementController extends BaseViewController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label titleLabel, typeLabel, quantityAvailable, yearLabel, lengthLabel, genresLabel;

    @FXML
    private TextFlow descriptionFlow;

    @FXML
    private Label isbnLabel, authorLabel, publisherLabel, editionLabel;

    @FXML
    private Label producerLabel, ageRatingLabel, directorLabel;

    @FXML
    private Label frequencyLabel, releaseMonthLabel, releaseDayLabel;

    @FXML
    private Button borrowButton;

    @FXML
    private Button editButton;

    @FXML
    private Button removeButton;

    @FXML
    private Button returnButton;

    @FXML
    private VBox mainVBox;

    private Element element;

    private Text descriptionText = new Text();


    @FXML
    protected void initialize() {
        super.initialize();
        Integer elementId = MainController.getInstance().getSelectedElementId();
        element = MainController.getInstance().getElementManager().getCompleteElementById(elementId);
        titleLabel.setText("Titolo: " + element.getTitle());
        quantityAvailable.setText("Disponibilità: " + element.getQuantityAvailable().toString());
        if(element instanceof Book){
            typeLabel.setText("Libro");
            lengthLabel.setText("Lunghezza: " + element.getLength().toString()+ " pagine");
            try {
                HBox bookDetails = (new FXMLLoader(getClass().getResource("/com/swe/libraryprogram/book-details.fxml"))).load();
                mainVBox.getChildren().add(2, bookDetails);
                isbnLabel = (Label) bookDetails.lookup("#isbnLabel");
                authorLabel = (Label) bookDetails.lookup("#authorLabel");
                publisherLabel = (Label) bookDetails.lookup("#publisherLabel");
                editionLabel = (Label) bookDetails.lookup("#editionLabel");
                isbnLabel.setText("ISBN: " + ((Book) element).getIsbn());
                authorLabel.setText("Autore: " + ((Book) element).getAuthor());
                publisherLabel.setText("Casa Editrice: " + ((Book) element).getPublisher());
                editionLabel.setText("Edizione: " + ((Book) element).getEdition());
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else if (element instanceof DigitalMedia){
            typeLabel.setText("Film");
            lengthLabel.setText("Durata: " + element.getLength().toString()+ " minuti");
            yearLabel.setText("Anno: " + element.getReleaseYear().toString());
            descriptionText.setText(element.getDescription());
            descriptionFlow.getChildren().setAll(descriptionText);
            try {
                HBox digitalMediaDetails = (new FXMLLoader(getClass().getResource("/com/swe/libraryprogram/digitalmedia-details.fxml"))).load();
                mainVBox.getChildren().add(2, digitalMediaDetails);
                producerLabel = (Label) digitalMediaDetails.lookup("#producerLabel");
                ageRatingLabel = (Label) digitalMediaDetails.lookup("#ageLabel");
                directorLabel = (Label) digitalMediaDetails.lookup("#directorLabel");
                producerLabel.setText("Casa Produttrice: " + ((DigitalMedia) element).getProducer());
                ageRatingLabel.setText("Età consigliata: " + ((DigitalMedia) element).getAgeRating());
                directorLabel.setText("Direttore: " + ((DigitalMedia) element).getDirector());
            }
            catch(IOException e){
                e.printStackTrace();
            }

        }
        else if(element instanceof PeriodicPublication){
            typeLabel.setText("Periodico");
            lengthLabel.setText("Lunghezza: " + element.getLength().toString()+ " pagine");
            try {
                HBox periodicPublicationDetails = (new FXMLLoader(getClass().getResource("/com/swe/libraryprogram/periodicpublication-details.fxml"))).load();
                mainVBox.getChildren().add(2, periodicPublicationDetails);
                publisherLabel = (Label) periodicPublicationDetails.lookup("#publisherLabel");
                frequencyLabel = (Label) periodicPublicationDetails.lookup("#frequencyLabel");
                releaseMonthLabel = (Label) periodicPublicationDetails.lookup("#monthLabel");
                releaseDayLabel = (Label) periodicPublicationDetails.lookup("#dayLabel");
                publisherLabel.setText("Casa Produttrice: " + ((PeriodicPublication) element).getPublisher());
                frequencyLabel.setText("Frequenza: " + ((PeriodicPublication) element).getFrequency());
                releaseMonthLabel.setText("Mese: " + ((PeriodicPublication) element).getReleaseMonth().toString());
                releaseDayLabel.setText("Giorno: " + ((PeriodicPublication) element).getReleaseDay().toString());
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        borrowButton.setOnAction(event -> handleBorrowAction());
        returnButton.setOnAction(event -> handleReturnAction());
        editButton.setOnAction(event -> handleEditAction());
        removeButton.setOnAction(event -> handleRemoveAction());
        updateView();
    }

    private void handleReturnAction() {
        if (((LibraryUserController) MainController.getInstance().getUserController()).returnElement(element.getId())) {
            showAlert("Prestito dell'elemento", "Elemento restituito!");
            updateView();

        } else {
            showAlert("Prestito dell'elemento", "Errore durante il prestito dell'elemento");

        }
    }

    private void updateView() {
        if(MainController.getInstance().getUser().isAdmin()){
            borrowButton.setVisible(false);
            returnButton.setVisible(false);
            editButton.setVisible(true);
            removeButton.setVisible(true);
        }
        else{
            editButton.setVisible(false);
            removeButton.setVisible(false);
            borrowButton.setDisable(false);
            returnButton.setDisable(true);
            for (Element e : MainController.getInstance().getBorrowedElements()) {
                if(e.getId().equals(element.getId())) {
                    borrowButton.setDisable(true);
                    returnButton.setDisable(false);
                }
            }
            if(element.getQuantityAvailable() == 0){
                borrowButton.setDisable(true);
            }
        }
    }

    private void handleBorrowAction() {

        if (((LibraryUserController) MainController.getInstance().getUserController()).borrowElement(element.getId())) {
            showAlert("Prestito dell'elemento", "Elemento preso!");
            updateView();

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
                mainViewController.loadBottomPane("home");
            }

        } catch (SQLException e) {

            showAlert("Rimozione dell'elemento", "Errore durante la rimozione dell'elemento");
            e.printStackTrace();

        }

    }
}
