package com.swe.libraryprogram.service;

import com.swe.libraryprogram.domainmodel.*;

import java.sql.SQLException;
import java.util.List;


public class LibraryAdminService extends UserService {


    public Boolean addElement(Element element) {
        try {
            if (element.getClass() != Element.class) {
                Integer elementId = null;
                if (element instanceof Book) {
                    if ((MainService.getInstance().getBookDAO().getBookByIsbn(((Book) element).getIsbn())) != null) {
                        return false;
                    }
                    elementId = MainService.getInstance().getBookDAO().addBook((Book) element);
                } else if (element instanceof DigitalMedia) {
                    elementId = MainService.getInstance().getDigitalMediaDAO().addDigitalMedia((DigitalMedia) element);
                } else if (element instanceof PeriodicPublication) {
                    elementId = MainService.getInstance().getPeriodicPublicationDAO().addPeriodicPublication((PeriodicPublication) element);
                }
                if (elementId == null) {
                    return false;
                }
                for (Genre genre : element.getGenres()) {
                    if (!MainService.getInstance().getGenreDAO().associateGenreWithElement(elementId, genre.getCode())) {
                        System.err.println("Impossibile aggiungere " + genre.getName() + " alla lista dei generi associati");
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public Boolean removeElement(Element element) {
        try {
            MainService.getInstance().getElementDAO().removeElement(element.getId());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Boolean updateElement(Element element) {
        try {
            if (element instanceof Book) {
                if ((MainService.getInstance().getBookDAO().getBookByIsbn(((Book) element).getIsbn())) != null) {
                    return false;
                }
                MainService.getInstance().getBookDAO().updateBook((Book) element);
            } else if (element instanceof DigitalMedia) {
                MainService.getInstance().getDigitalMediaDAO().updateDigitalMedia((DigitalMedia) element);
            } else if (element instanceof PeriodicPublication) {
                MainService.getInstance().getPeriodicPublicationDAO().updatePeriodicPublication((PeriodicPublication) element);
            } else {
                return false;
            }
            List<Genre> genresToAdd = element.getGenres();
            genresToAdd.removeAll(MainService.getInstance().getGenreDAO().getGenresForElement(element.getId()));
            List<Genre> genresToRemove = MainService.getInstance().getGenreDAO().getGenresForElement(element.getId());
            genresToRemove.removeAll(element.getGenres());
            for (Genre genre : genresToRemove) {
                try {
                    MainService.getInstance().getGenreDAO().removeGenreFromElement(element.getId(), genre.getCode());
                } catch (SQLException e) {
                    System.err.println("Impossibile rimuovere " + genre.getName() + " dalla lista dei generi associati");
                }
            }
            for (Genre genre : genresToAdd) {
                if (!MainService.getInstance().getGenreDAO().associateGenreWithElement(element.getId(), genre.getCode())) {
                    System.err.println("Impossibile aggiungere " + genre.getName() + " alla lista dei generi associati");
                }
            }
            return true;
        } catch (
                SQLException e) {
            return false;
        }
    }

    public Boolean addGenre(String genreName) {
        if (genreName == null) {
            return false;
        }
        Genre newGenre = new Genre(genreName);
        try {
            return MainService.getInstance().getGenreDAO().addGenre(newGenre);
        } catch (SQLException e) {
            return false;
        }
    }

}
