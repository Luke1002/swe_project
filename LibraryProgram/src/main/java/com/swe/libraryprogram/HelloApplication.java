package com.swe.libraryprogram;

import com.swe.libraryprogram.controller.MainViewController;
import com.swe.libraryprogram.orm.ConnectionManager;
import com.swe.libraryprogram.service.MainService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        try {
            ConnectionManager.getInstance();
            MainService.getInstance();
            MainViewController mainViewController;
            stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/icon.png"))));
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
        } catch (Exception e) {
            Stage errorStage = new Stage();
            errorStage.setResizable(false);
            errorStage.setWidth(250);
            errorStage.setHeight(140);
            errorStage.setTitle("Errore");
            Label instructionLabel = new Label("Impossibile aprire l'applicazione");
            Button submitButton = new Button("Ok");
            errorStage.initModality(Modality.APPLICATION_MODAL);
            submitButton.setOnAction(_ -> {
                errorStage.close();
            });
            HBox buttonBox = new HBox(submitButton);
            buttonBox.setAlignment(Pos.BOTTOM_RIGHT);
            buttonBox.setStyle("-fx-padding: 10; -fx-spacing: 10;");

            VBox layout = new VBox(10, instructionLabel, buttonBox);
            layout.setStyle("-fx-padding: 20; -fx-alignment: top-center;");
            Scene scene = new Scene(layout, 300, 150);
            errorStage.setScene(scene);
            errorStage.showAndWait();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}