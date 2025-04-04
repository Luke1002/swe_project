package com.swe.libraryprogram.service;

import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.Genre;
import com.swe.libraryprogram.domainmodel.User;
import com.swe.libraryprogram.orm.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MainService {


    private BookDAO bookDAO = new BookDAO();
    private BorrowsDAO borrowsDAO = new BorrowsDAO();
    private DigitalMediaDAO digitalMediaDAO = new DigitalMediaDAO();
    private ElementDAO elementDAO = new ElementDAO();
    private GenreDAO genreDAO = new GenreDAO();
    private PeriodicPublicationDAO periodicPublicationDAO = new PeriodicPublicationDAO();
    private UserDAO userDAO = new UserDAO();


    private Integer selectedElementId = null;
    private User session_user = null;
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
        } else {
            return false;
        }
    }

    public User getUser() {
        return session_user;
    }

    public UserService getUserService() {
        return userService;
    }

    BookDAO getBookDAO() {
        return bookDAO;
    }

    BorrowsDAO getBorrowsDAO() {
        return borrowsDAO;
    }

    DigitalMediaDAO getDigitalMediaDAO() {
        return digitalMediaDAO;
    }

    ElementDAO getElementDAO() {
        return elementDAO;
    }

    GenreDAO getGenreDAO() {
        return genreDAO;
    }

    PeriodicPublicationDAO getPeriodicPublicationDAO() {
        return periodicPublicationDAO;
    }

    UserDAO getUserDAO() {
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

    public Element getSelectedElement() {
        return elementDAO.getCompleteElementById(selectedElementId);
    }

    public List<Genre> getAllGenres() {
        try {
            List<Genre> allGenres = new ArrayList<>();
            allGenres = genreDAO.getAllGenres();
            return allGenres;
        } catch (SQLException e) {
            return null;
        }
    }
}
