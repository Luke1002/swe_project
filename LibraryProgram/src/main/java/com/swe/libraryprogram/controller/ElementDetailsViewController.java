package com.swe.libraryprogram.controller;

import com.swe.libraryprogram.domainmodel.Book;
import com.swe.libraryprogram.domainmodel.DigitalMedia;
import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.PeriodicPublication;
import com.swe.libraryprogram.service.LibraryAdminService;
import com.swe.libraryprogram.service.LibraryUserService;
import com.swe.libraryprogram.service.MainService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;


public class ElementDetailsViewController extends BaseViewController {

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
        Integer elementId = MainService.getInstance().getSelectedElementId();
        element = MainService.getInstance().getSelectedElement();
        titleLabel.setText("Titolo: " + element.getTitle());
        String yearStringvalue = element.getReleaseYear() == null ? "" : element.getReleaseYear().toString();
        yearLabel.setText("Anno: " + yearStringvalue);
        genresLabel.setText(element.getGenresAsString());
        descriptionLabel.setText(element.getDescription());
        quantityLabel.setText("Disponibilità: " + element.getQuantityAvailable().toString() + "/" + element.getQuantity().toString());
        String lengthStringvalue = element.getLength() == null ? "" : element.getLength().toString() + " pagine";
        String durationStringvalue = element.getLength() == null ? "" : element.getLength().toString() + " minuti";
        if (element instanceof Book) {
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
                String editionStringValue = ((Book) element).getEdition() == null ? "" : ((Book) element).getEdition().toString();
                editionLabel.setText("Edizione: " + editionStringValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (element instanceof DigitalMedia) {
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
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (element instanceof PeriodicPublication) {
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
                String monthStringValue = ((PeriodicPublication) element).getReleaseMonth() == null ? "" : ((PeriodicPublication) element).getReleaseMonth().toString();
                releaseMonthLabel.setText("Mese: " + monthStringValue);
                String dayStringValue = ((PeriodicPublication) element).getReleaseDay() == null ? "" : ((PeriodicPublication) element).getReleaseDay().toString();
                releaseDayLabel.setText("Giorno: " + dayStringValue);
            } catch (IOException e) {
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
        if (MainService.getInstance().getUser().isAdmin()) {
            borrowButton.setVisible(false);
            returnButton.setVisible(false);
            editButton.setVisible(true);
            removeButton.setVisible(true);
        } else {
            editButton.setVisible(false);
            removeButton.setVisible(false);
            borrowButton.setDisable(false);
            returnButton.setDisable(true);

            if (!MainService.getInstance().getUser().isAdmin()) {
                List<Element> elements = ((LibraryUserService) MainService.getInstance().getUserService()).getBorrowedElements(MainService.getInstance().getUser());
                for (Element e : elements) {
                    if (e.getId().equals(element.getId())) {
                        borrowButton.setDisable(true);
                        returnButton.setDisable(false);
                    }
                }
            }
            if (element.getQuantityAvailable() == 0) {
                borrowButton.setDisable(true);
            }
        }
    }

    private void handleReturnAction() {
        if (((LibraryUserService) MainService.getInstance().getUserService()).returnElement(element.getId())) {
            showAlert("Prestito dell'elemento", "Elemento restituito!");
            mainVBox.getChildren().remove(2);
            initialize();
        } else {
            showAlert("Prestito dell'elemento", "Errore durante il prestito dell'elemento");
        }
    }

    private void handleBorrowAction() {

        if (((LibraryUserService) MainService.getInstance().getUserService()).borrowElement(element.getId())) {
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
        MainService.getInstance().setSelectedElementId(null);
        goBack();
    }

    private void handleRemoveAction() {
        if (((LibraryAdminService) MainService.getInstance().getUserService()).removeElement(element)) {
            showAlert("Rimozione dell'elemento", "Elemento rimosso con successo");
            MainService.getInstance().setSelectedElementId(null);
            mainViewController.loadBottomPane("home");
        } else {
            showAlert("Rimozione dell'elemento", "Errore durante la rimozione dell'elemento");
        }

    }
}
