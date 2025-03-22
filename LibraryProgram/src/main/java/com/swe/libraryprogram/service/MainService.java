package com.swe.libraryprogram.service;

import com.swe.libraryprogram.orm.*;
import com.swe.libraryprogram.domainmodel.User;

import java.sql.SQLException;

public class MainService {


    BookDAO bookDAO = new BookDAO();
    BorrowsDAO borrowsDAO = new BorrowsDAO();
    DigitalMediaDAO digitalMediaDAO = new DigitalMediaDAO();
    ElementDAO elementDAO = new ElementDAO();
    GenreDAO genreDAO = new GenreDAO();
    PeriodicPublicationDAO periodicPublicationManager = new PeriodicPublicationDAO();
    UserDAO userDAO = new UserDAO();


    Integer selectedElementId = null;
    User session_user = null;
    UserService userService = new UserService();


    private static MainService singleton;

    private MainService() {
    }

    public static MainService getInstance() {
        if (singleton == null) {
            singleton = new MainService();
        }
        return singleton;
    }



    public Boolean setUserState(String email, String password) throws SQLException {
        session_user = userService.login(email, password);
        if (session_user != null) {
            if (session_user.isAdmin()) {
                    userService = new LibraryAdminService();
            } else {
                    userService = new LibraryUserService();
            }
            return true;
        }else{
            return false;
        }
    }

    public User getUser() {
        return session_user;
    }

    public UserService getUserController() {
        return userService;
    }

    public BookDAO getBookManager() {
        return bookDAO;
    }

    public BorrowsDAO getBorrowsManager() {
        return borrowsDAO;
    }

    public DigitalMediaDAO getDigitalMediaManager() {
        return digitalMediaDAO;
    }

    public ElementDAO getElementManager() {
        return elementDAO;
    }

    public GenreDAO getGenreManager() {
        return genreDAO;
    }

    public PeriodicPublicationDAO getPeriodicPublicationManager() {
        return periodicPublicationManager;
    }

    public UserDAO getUserManager() {
        return userDAO;
    }

    public void resetUserState() {
        session_user = null;
        userService = new UserService();
    }

    public Integer getSelectedElementId() {
        return selectedElementId;
    }

    public void setSelectedElementId(Integer elementId) {
        this.selectedElementId = elementId;
    }
}
