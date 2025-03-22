package com.swe.libraryprogram.view;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class BaseViewController {

    MainViewController mainViewController;
    String lastView = null;

    @FXML
    protected void initialize(){
    }

    public void setMainViewController(MainViewController mainViewController) {
        this.mainViewController = mainViewController;
    }

    public void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(title);
        alert.setContentText(message);
        ButtonType okButton = new ButtonType("Ok");
        alert.getButtonTypes().add(okButton);
        alert.showAndWait();
    }

    public void setLastView(String lastView){
        this.lastView = lastView;
    }

    public void goBack(){
        mainViewController.loadBottomPane(lastView);
    }
}
