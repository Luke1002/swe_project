package com.swe.libraryprogram.controller;

import com.swe.libraryprogram.domainmodel.*;

import java.sql.SQLException;
import java.util.List;


public class LibraryAdminController extends UserController {




    public Boolean addElement(Element element) {
        try {
            if (element.getClass() != Element.class) {
                Integer elementId = null;
                if (element instanceof Book) {
                    if ((MainController.getInstance().getBookManager().getBookByIsbn(((Book) element).getIsbn())) != null) {
                        return false;
                    }
                    elementId = MainController.getInstance().getBookManager().addBook((Book) element);
                } else if (element instanceof DigitalMedia) {
                    elementId = MainController.getInstance().getDigitalMediaManager().addDigitalMedia((DigitalMedia) element);
                } else if (element instanceof PeriodicPublication) {
                    elementId = MainController.getInstance().getPeriodicPublicationManager().addPeriodicPublication((PeriodicPublication) element);
                }
                if (elementId == null) {
                    return false;
                }
                for (Genre genre : element.getGenres()) {
                    if(!MainController.getInstance().getGenreManager().associateGenreWithElement(elementId, genre.getCode())){
                        System.err.println("Impossibile aggiungere " + genre.getName() +" alla lista dei generi associati");
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
            MainController.getInstance().getElementManager().removeElement(element.getId());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    public Boolean updateElement(Element element) {
        try {
            if (element instanceof Book) {
                if ((MainController.getInstance().getBookManager().getBookByIsbn(((Book) element).getIsbn())) != null) {
                    return false;
                }
                MainController.getInstance().getBookManager().updateBook((Book) element);
            } else if (element instanceof DigitalMedia) {
                MainController.getInstance().getDigitalMediaManager().updateDigitalMedia((DigitalMedia) element);
            } else if (element instanceof PeriodicPublication) {
                MainController.getInstance().getPeriodicPublicationManager().updatePeriodicPublication((PeriodicPublication) element);
            } else {
                return false;
            }
            List<Genre> genresToAdd = element.getGenres();
            genresToAdd.removeAll(MainController.getInstance().getGenreManager().getGenresForElement(element.getId()));
            List<Genre> genresToRemove = MainController.getInstance().getGenreManager().getGenresForElement(element.getId());
            genresToRemove.removeAll(element.getGenres());
            for (Genre genre : genresToRemove) {
                try {
                    MainController.getInstance().getGenreManager().removeGenreFromElement(element.getId(), genre.getCode());
                } catch (SQLException e) {
                    System.err.println("Impossibile rimuovere " + genre.getName() +" dalla lista dei generi associati");
                }
            }
            for (Genre genre : genresToAdd) {
                    if(!MainController.getInstance().getGenreManager().associateGenreWithElement(element.getId(), genre.getCode())){
                        System.err.println("Impossibile aggiungere " + genre.getName() +" alla lista dei generi associati");
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
            return MainController.getInstance().getGenreManager().addGenre(newGenre);
        } catch (SQLException e) {
            return false;
        }
    }

}
