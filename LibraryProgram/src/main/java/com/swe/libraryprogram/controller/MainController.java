package com.swe.libraryprogram.controller;

import com.swe.libraryprogram.dao.*;
import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class MainController {

    User session_user = null;
    UserController userController = new UserController();
    BookManager bookManager = new BookManager();
    BorrowsManager borrowsManager = new BorrowsManager();
    DigitalMediaManager digitalMediaManager = new DigitalMediaManager();
    ElementManager elementManager = new ElementManager();
    GenreManager genreManager = new GenreManager();
    ModifiedElementsManager modifiedElementsManager = new ModifiedElementsManager();
    PeriodicPublicationManager periodicPublicationManager = new PeriodicPublicationManager();
    UserManager userManager = new UserManager();
    Integer selectedElementId = null;

    public Integer getSelectedElementId() {
        return selectedElementId;
    }

    public void setSelectedElementId(Integer elementId) {
        this.selectedElementId = elementId;
    }


    private static MainController singleton;
    private Scene mainScene = null;
    private Scene previousScene = null;

    private MainController() {
    }

    public static MainController getInstance() {
        if (singleton == null) {
            singleton = new MainController();
        }
        return singleton;
    }



    public Boolean setUserAndController(String email, String password) throws SQLException {
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

    public User getUser() {
        return session_user;
    }

    public UserController getUserController() {
        return userController;
    }

    public BookManager getBookManager() {
        return bookManager;
    }

    public BorrowsManager getBorrowsManager() {
        return borrowsManager;
    }

    public DigitalMediaManager getDigitalMediaManager() {
        return digitalMediaManager;
    }

    public ElementManager getElementManager() {
        return elementManager;
    }

    public GenreManager getGenreManager() {
        return genreManager;
    }

    public ModifiedElementsManager getModifiedElementsManager() {
        return modifiedElementsManager;
    }

    public PeriodicPublicationManager getPeriodicPublicationManager() {
        return periodicPublicationManager;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
