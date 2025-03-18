package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.UserController;
import com.swe.libraryprogram.domainmodel.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class BaseViewController {

    @FXML
    private BorderPane base;

    @FXML
    AnchorPane header, content;

    @FXML
    Label menubarLabel;

    User usr;
    UserController userController;

    @FXML
    protected void initialize(){
        FXMLLoader headerLoader = new FXMLLoader(getClass().getResource("menubar-view.fxml"));
        try{
            AnchorPane header = headerLoader.load();
            header.prefWidthProperty().bind(base.widthProperty());
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void setTitle(String title) {
        menubarLabel.setText(title);
    }

    public void showAlert(String title, String message) {

        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle(title);
        alert.setContentText(message);
        ButtonType okButton = new ButtonType("Ok");
        alert.getButtonTypes().add(okButton);
        alert.showAndWait();
    }

    public void setUser(User user) {
        usr = user;
    }


    public void setUserController(UserController userController) {
        this.userController = userController;
    }
}
