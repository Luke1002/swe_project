package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.LibraryAdminController;
import com.swe.libraryprogram.controller.MainController;
import com.swe.libraryprogram.domainmodel.Genre;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class MenuBarViewController extends BaseViewController{

    @FXML
    MenuItem homeMenuItem, addItemMenuItem, addGenresMenuItem, borrowedItemsMenuItem, logoutMenuItem, aboutMenuItem;

    @FXML
    protected void initialize() {
        super.initialize();
        homeMenuItem.setOnAction(event -> goToHome());
        addItemMenuItem.setOnAction(event -> goToAddItem());
        addGenresMenuItem.setOnAction(event -> {
            if(MainController.getInstance().getUser().isAdmin()){

                Stage popupStage = new Stage();
                popupStage.initModality(Modality.WINDOW_MODAL);
                popupStage.initOwner(mainViewController.getStage());
                popupStage.setResizable(false);
                popupStage.setWidth(600);
                popupStage.setHeight(200);
                popupStage.setTitle("Aggiungi Generi");

                Label instructionLabel = new Label("Inserisci nomi dei generi da inserire separati da \",\"");
                TextField inputField = new TextField();
                Button submitButton = new Button("Aggiungi");

                submitButton.setOnAction(e -> {
                    String inputText = inputField.getText();
                    ArrayList<String> genresToAddList = new ArrayList<>();
                    if (inputText != null && !inputText.trim().isEmpty()) {
                        genresToAddList = Arrays.stream(inputText.split(","))
                                .map(String::trim)
                                .map(String::toLowerCase)
                                .collect(Collectors.toCollection(ArrayList::new));
                    }
                    for(String newGenreName : genresToAddList) {
                        if(!((LibraryAdminController)MainController.getInstance().getUserController()).addGenre(newGenreName)){
                            showAlert("Errore", "Impossibile inserire il genere "+ newGenreName+ ". Forse è già presente?");
                        }
                    }
                    popupStage.close();
                });

                HBox buttonBox = new HBox(submitButton);
                buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
                buttonBox.setStyle("-fx-padding: 10; -fx-spacing: 10;");

                VBox layout = new VBox(10, instructionLabel, inputField, buttonBox);
                layout.setStyle("-fx-padding: 20; -fx-alignment: top-center;");

                Scene scene = new Scene(layout, 300, 150);
                popupStage.setScene(scene);
                popupStage.showAndWait();

            }
        });
        borrowedItemsMenuItem.setOnAction(event -> goToBorrowedItems());
        logoutMenuItem.setOnAction(event -> handleLogoutMenuItem());
        aboutMenuItem.setOnAction(event -> showAlert("Informazioni","Library Manager version 1.0"));
        if (MainController.getInstance().getUser() != null){
            if(!MainController.getInstance().getUser().isAdmin()){
                addItemMenuItem.setVisible(false);
                borrowedItemsMenuItem.setVisible(true);
                addGenresMenuItem.setVisible(false);
            }
            else{
                addItemMenuItem.setVisible(true);
                borrowedItemsMenuItem.setVisible(false);
                addGenresMenuItem.setVisible(true);
            }
        }

    }

    private void handleLogoutMenuItem() {
        MainController.getInstance().getUserController().logout();
        mainViewController.loadTopPane();
        mainViewController.loadBottomPane("login");
    }

    private void goToAddItem() {
        MainController.getInstance().setSelectedElementId(null);
        mainViewController.loadBottomPane("addItem");
    }

    private void goToHome(){
        mainViewController.loadBottomPane("home");
    }

    private void goToBorrowedItems() {
        mainViewController.loadBottomPane("borrowedItems");
    }
}
