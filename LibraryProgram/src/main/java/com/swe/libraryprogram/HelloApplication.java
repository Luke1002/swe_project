package com.swe.libraryprogram;

import com.swe.libraryprogram.controller.MainController;
import com.swe.libraryprogram.dao.ConnectionManager;
import com.swe.libraryprogram.view.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try{
            ConnectionManager.getInstance();
            MainController.getInstance();
            MainViewController mainViewController;
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-view.fxml"));
            Scene mainScene = new Scene(loader.load(), stage.getMinWidth(), stage.getMinHeight());
            mainViewController = loader.getController();
            mainViewController.setStage(stage);
            mainViewController.loadTopPane();
            mainViewController.loadBottomPane("login");
            stage.setScene(mainScene);
            stage.setResizable(true);
            stage.setTitle("Library Management System");
            stage.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}