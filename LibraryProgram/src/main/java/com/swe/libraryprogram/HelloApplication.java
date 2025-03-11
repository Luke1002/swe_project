package com.swe.libraryprogram;

import com.swe.libraryprogram.dao.ConnectionManager;
import com.swe.libraryprogram.dao.UserManager;
import com.swe.libraryprogram.view.ErrorController;
import com.swe.libraryprogram.view.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static boolean error = false;
    FXMLLoader fxmlLoader;

    @Override
    public void start(Stage stage) throws IOException {
        try{
            stage.setMinHeight(480);
            stage.setMinWidth(640);
            stage.setTitle("Library Management System");
            stage.setResizable(false);
            Scene scene;
            if(error){
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("error-view.fxml"));
                scene = new Scene(fxmlLoader.load(), stage.getMinWidth(), stage.getMinHeight());
                ((ErrorController)fxmlLoader.getController()).setErrorText("Cavalli bruni");
            }else{
                fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("login-view.fxml"));
                scene = new Scene(fxmlLoader.load(), stage.getMinWidth(), stage.getMinHeight());
            }
            stage.setScene(scene);
            stage.show();
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
            error = true;
        }

        launch();
    }
}