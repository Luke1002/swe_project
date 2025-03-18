package com.swe.libraryprogram;

import com.swe.libraryprogram.controller.MainController;
import com.swe.libraryprogram.dao.ConnectionManager;
import com.swe.libraryprogram.dao.UserManager;
import com.swe.libraryprogram.view.AddItemController;
import com.swe.libraryprogram.view.ErrorController;
import com.swe.libraryprogram.view.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try{
            MainController.setMainStage(stage);
            MainController mainController = MainController.getInstance();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ConnectionManager.setDbUser(args[0]);
        ConnectionManager.setDbPass(args[1]);
        ConnectionManager conman  = ConnectionManager.getInstance();
        if (!conman.isConnectionValid()) {
        }else{
        }

        launch();
    }
}