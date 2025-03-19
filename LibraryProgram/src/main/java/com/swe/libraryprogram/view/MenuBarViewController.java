package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

public class MenuBarViewController extends BaseViewController{
    @FXML
    Label menubarLabel;

    @FXML
    MenuItem addItemMenuItem;

    @FXML
    protected void initialize() {
        super.initialize();
        addItemMenuItem.setOnAction(event -> goToAddItem());
        if (MainController.getInstance().getUser() != null){
            menubarLabel.setText("Benvenuto, "+MainController.getInstance().getUser().getName() + " " + MainController.getInstance().getUser().getSurname() + "!");
            if(MainController.getInstance().getUser().isAdmin()){

            }
            else{
                addItemMenuItem.setVisible(false);
            }
        }

    }

    private void goToAddItem() {
        menubarLabel.setText("Aggiungi elemento");
        mainViewController.loadBottomPane("addItem");
    }
}
