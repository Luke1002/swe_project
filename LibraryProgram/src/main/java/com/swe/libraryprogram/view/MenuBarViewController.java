package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.MainController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

public class MenuBarViewController extends BaseViewController{

    @FXML
    MenuItem homeMenuItem, addItemMenuItem, borrowedItemsMenuItem, logoutMenuItem, aboutMenuItem;

    @FXML
    protected void initialize() {
        super.initialize();
        homeMenuItem.setOnAction(event -> goToHome());
        addItemMenuItem.setOnAction(event -> goToAddItem());
        borrowedItemsMenuItem.setOnAction(event -> goToBorrowedItems());
        logoutMenuItem.setOnAction(event -> handleLogoutMenuItem());
        aboutMenuItem.setOnAction(event -> showAlert("Informazioni","Library Manager version 1.0"));
        if (MainController.getInstance().getUser() != null){
            if(!MainController.getInstance().getUser().isAdmin()){
                addItemMenuItem.setVisible(false);
            }
            else{
                addItemMenuItem.setVisible(false);
            }
        }

    }

    private void handleLogoutMenuItem() {
        MainController.getInstance().getUserController().logout();
        mainViewController.loadBottomPane("login");
    }

    private void goToAddItem() {
        mainViewController.loadBottomPane("addItem");
    }

    private void goToHome(){
        mainViewController.loadBottomPane("home");
    }

    private void goToBorrowedItems() {
        mainViewController.loadBottomPane("borrowedItems");
    }
}
