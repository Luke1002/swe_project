package com.swe.libraryprogram.controller;

import com.swe.libraryprogram.dao.*;
import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.Genre;
import com.swe.libraryprogram.domainmodel.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainController {


    private BookManager bookManager = new BookManager();
    private BorrowsManager borrowsManager = new BorrowsManager();
    private DigitalMediaManager digitalMediaManager = new DigitalMediaManager();
    private ElementManager elementManager = new ElementManager();
    private GenreManager genreManager = new GenreManager();
    private PeriodicPublicationManager periodicPublicationManager = new PeriodicPublicationManager();
    private UserManager userManager = new UserManager();


    private Integer selectedElementId = null;
    private User session_user = null;
    UserController userController = new UserController();


    private static MainController singleton;

    private MainController() {
    }

    public static MainController getInstance() {
        if (singleton == null) {
            singleton = new MainController();
        }
        return singleton;
    }



    public Boolean setUserState(String email, String password) throws SQLException {
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

    BookManager getBookManager() {
        return bookManager;
    }

    BorrowsManager getBorrowsManager() {
        return borrowsManager;
    }

    DigitalMediaManager getDigitalMediaManager() {
        return digitalMediaManager;
    }

    ElementManager getElementManager() {
        return elementManager;
    }

    GenreManager getGenreManager() {
        return genreManager;
    }

    PeriodicPublicationManager getPeriodicPublicationManager() {
        return periodicPublicationManager;
    }

    UserManager getUserManager() {
        return userManager;
    }

    public void resetUserState() {
        session_user = null;
        userController = new UserController();
    }

    public Integer getSelectedElementId() {
        return selectedElementId;
    }

    public void setSelectedElementId(Integer elementId) {
        this.selectedElementId = elementId;
    }
    public Element getSelectedElement() {
        return elementManager.getCompleteElementById(selectedElementId);
    }

    public List<Genre> getAllGenres(){
        try {
            List<Genre> allGenres = new ArrayList<>();
            allGenres = genreManager.getAllGenres();
            return allGenres;
        } catch (SQLException e) {
            return null;
        }
    }
}
