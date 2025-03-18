package com.swe.libraryprogram.controller;

import com.swe.libraryprogram.dao.UserManager;
import com.swe.libraryprogram.domainmodel.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainController {

    User session_user = null;
    UserController userController;
    UserManager userManager;

    private static MainController singleton;
    private static Stage mainStage = new Stage();
    private Scene mainScene = null;
    private Scene previousScene = null;

    private MainController() {
        userController = new UserController();
        userManager = new UserManager();
        mainStage.setMinHeight(480);
        mainStage.setMinWidth(640);
        mainStage.setTitle("Library Management System");
        setScene("login");
        previousScene = mainScene;
    }

    public static MainController getInstance() {
        if (singleton == null) {
            singleton = new MainController();
        }
        return singleton;
    }

    public static void setMainStage(Stage mainStage) {
        MainController.mainStage = mainStage;
    }

    public void setScene(String fxmlView) {
        String fxmlPath = "/com/swe/libraryprogram/" + fxmlView + "-view.fxml";
        try {
            previousScene = mainScene;
            mainScene = new Scene(new FXMLLoader(getClass().getResource(fxmlPath)).load(), mainStage.getMinWidth(), mainStage.getMinHeight());
            mainStage.setScene(mainScene);
            mainStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public Boolean setUser(String email, String password) throws SQLException {
        session_user = userController.login(email, password);
        if (session_user != null) {
            if (session_user.isAdmin()) {
                    userController = new LibraryAdminController();
            } else {
                    userController = new LibraryUserController();
            }
            return true;
        }else{
            return false;
        }
    }
}
