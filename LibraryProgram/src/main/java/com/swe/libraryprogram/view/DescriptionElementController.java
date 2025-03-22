package com.swe.libraryprogram.view;

import com.swe.libraryprogram.services.*;
import com.swe.libraryprogram.domainmodel.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class DescriptionElementController extends BaseViewController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Label titleLabel, typeLabel, quantityLabel, yearLabel, lengthLabel, genresLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label isbnLabel, authorLabel, publisherLabel, editionLabel;

    @FXML
    private Label producerLabel, ageRatingLabel, directorLabel;

    @FXML
    private Label frequencyLabel, releaseMonthLabel, releaseDayLabel;

    @FXML
    private Button borrowButton, editButton, removeButton, returnButton, closeButton;

    @FXML
    private VBox mainVBox;

    private Element element;


    @FXML
    protected void initialize() {
        super.initialize();
        Integer elementId = MainController.getInstance().getSelectedElementId();
        element = MainController.getInstance().getElementManager().getCompleteElementById(elementId);
        titleLabel.setText("Titolo: " + element.getTitle());
        String yearStringvalue = element.getReleaseYear() == null ? "" : element.getReleaseYear().toString();
        yearLabel.setText("Anno: " + yearStringvalue);
        genresLabel.setText(element.getGenresAsString());
        descriptionLabel.setText(element.getDescription());
        quantityLabel.setText("Disponibilità: "+ element.getQuantityAvailable().toString() + "/" + element.getQuantity().toString());
        String lengthStringvalue = element.getLength() == null ? "" : element.getLength().toString()+" pagine";
        String durationStringvalue = element.getLength() == null ? "" : element.getLength().toString()+" minuti";
        if(element instanceof Book){
            typeLabel.setText("Libro");
            lengthLabel.setText("Lunghezza: " + lengthStringvalue);
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
                String editionStringValue = ((Book) element).getEdition() == null? "" : ((Book) element).getEdition().toString();
                editionLabel.setText("Edizione: " + editionStringValue);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else if (element instanceof DigitalMedia){
            typeLabel.setText("Film");
            lengthLabel.setText("Durata: " + durationStringvalue);
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
            lengthLabel.setText("Lunghezza: " + lengthStringvalue);
            try {
                HBox periodicPublicationDetails = (new FXMLLoader(getClass().getResource("/com/swe/libraryprogram/periodicpublication-details.fxml"))).load();
                mainVBox.getChildren().add(2, periodicPublicationDetails);
                publisherLabel = (Label) periodicPublicationDetails.lookup("#publisherLabel");
                frequencyLabel = (Label) periodicPublicationDetails.lookup("#frequencyLabel");
                releaseMonthLabel = (Label) periodicPublicationDetails.lookup("#monthLabel");
                releaseDayLabel = (Label) periodicPublicationDetails.lookup("#dayLabel");
                publisherLabel.setText("Casa Produttrice: " + ((PeriodicPublication) element).getPublisher());
                frequencyLabel.setText("Frequenza: " + ((PeriodicPublication) element).getFrequency());
                String monthStringValue = ((PeriodicPublication) element).getReleaseMonth() == null? "" : ((PeriodicPublication) element).getReleaseMonth().toString();
                releaseMonthLabel.setText("Mese: " + monthStringValue);
                String dayStringValue = ((PeriodicPublication) element).getReleaseDay() == null? "" : ((PeriodicPublication) element).getReleaseDay().toString();
                releaseDayLabel.setText("Giorno: " + dayStringValue);
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        borrowButton.setOnAction(event -> handleBorrowAction());
        returnButton.setOnAction(event -> handleReturnAction());
        editButton.setOnAction(event -> handleEditAction());
        removeButton.setOnAction(event -> handleRemoveAction());
        closeButton.setOnAction(event -> handleCloseAction());
        updateButtonView();
    }

    private void updateButtonView() {
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
            List<Element> elements = new ArrayList<>();
            try{
                elements = MainController.getInstance().getBorrowsManager().getBorrowedElementsForUser(MainController.getInstance().getUser().getEmail());
            }
            catch(SQLException e){}
            for (Element e : elements) {
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

    private void handleReturnAction() {
        if (((LibraryUserController) MainController.getInstance().getUserController()).returnElement(element.getId())) {
            showAlert("Prestito dell'elemento", "Elemento restituito!");
            mainVBox.getChildren().remove(2);
            initialize();
        } else {
            showAlert("Prestito dell'elemento", "Errore durante il prestito dell'elemento");
        }
    }

    private void handleBorrowAction() {

        if (((LibraryUserController) MainController.getInstance().getUserController()).borrowElement(element.getId())) {
            showAlert("Prestito dell'elemento", "Elemento preso!");
            mainVBox.getChildren().remove(2);
            initialize();
        } else {
            showAlert("Prestito dell'elemento", "Errore durante il prestito dell'elemento");
        }
    }

    private void handleEditAction() {
        mainViewController.loadBottomPane("addItem");

    }

    private void handleCloseAction() {
        MainController.getInstance().setSelectedElementId(null);
        goBack();
    }

    private void handleRemoveAction() {

        try {
            if (MainController.getInstance().getElementManager().removeElement(element.getId())) {
                showAlert("Rimozione dell'elemento", "Elemento rimosso con successo");
                MainController.getInstance().setSelectedElementId(null);
                mainViewController.loadBottomPane("home");
            }

        } catch (SQLException e) {

            showAlert("Rimozione dell'elemento", "Errore durante la rimozione dell'elemento");
            e.printStackTrace();

        }

    }
}
