package com.swe.libraryprogram.view;

import com.swe.libraryprogram.controller.MainController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class MainViewController {

    private Scene currentScene;
    private Scene previousScene;

    @FXML
    private BorderPane root;

    @FXML
    private void initialize() {
    }

    public void loadTopPane(String fxmlView) {
        String fxmlPath = "/com/swe/libraryprogram/" + fxmlView + "-view.fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane view = loader.load();
            ((BaseViewController)loader.getController()).setMainViewController(this);
            root.setTop(view);
            root.layout();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadTopPane() {
        AnchorPane view = new AnchorPane();
        root.setTop(view);
    }

    public void loadBottomPane(String fxmlView) {
        String fxmlPath = "/com/swe/libraryprogram/" + fxmlView + "-view.fxml";
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            AnchorPane view = loader.load();
            ((BaseViewController)loader.getController()).setMainViewController(this);
            root.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
