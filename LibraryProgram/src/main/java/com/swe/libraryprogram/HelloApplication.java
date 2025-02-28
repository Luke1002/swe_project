package com.swe.libraryprogram;

import com.swe.libraryprogram.dao.ConnectionManager;
import com.swe.libraryprogram.dao.UserManager;
import com.swe.libraryprogram.view.ErrorWindowController;
import com.swe.libraryprogram.view.HelloController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static boolean error = false;

    @Override
    public void start(Stage stage) throws IOException {
        try{
            if(error){
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("error-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 300, 150);
                ErrorWindowController controller = fxmlLoader.getController();
                controller.setErrorText("Connection not established");
                stage.setMinHeight(240);
                stage.setMinWidth(320);
                stage.setTitle("Error");
                stage.setScene(scene);
                stage.show();
            }else{
                FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 640, 480);
                stage.setMinHeight(480);
                stage.setMinWidth(640);
                stage.setTitle("Library Management System");
                stage.setScene(scene);
                stage.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ConnectionManager conman  = ConnectionManager.getInstance();
        if (conman.startingConnectionCheck()) {
            UserManager userman = new UserManager(conman);
            String res = userman.getUser();
            HelloController.labelText =res;
        }else{
            error = true;
        }

        launch();
    }
}