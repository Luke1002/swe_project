package com.swe.libraryprogram;

import com.swe.libraryprogram.dao.ConnectionManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setMinHeight(240);
        stage.setMinWidth(320);
        stage.setTitle("Library Management System");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        ConnectionManager conman  = ConnectionManager.getInstance();
        if (conman.startingConnectionCheck()){
            HelloController.labelText ="Connection established";
        }else{
            HelloController.labelText ="Connection failed";
        }

        launch();
    }
}