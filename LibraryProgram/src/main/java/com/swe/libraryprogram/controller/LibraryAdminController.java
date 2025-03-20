package com.swe.libraryprogram.controller;


import com.swe.libraryprogram.dao.*;
import com.swe.libraryprogram.domainmodel.*;

import java.sql.SQLException;


public class LibraryAdminController extends UserController{


    private final ElementManager elementManager = new ElementManager();
    private final BookManager bookManager = new BookManager();
    private final DigitalMediaManager digitalMediaManager = new DigitalMediaManager();
    private final PeriodicPublicationManager periodicPublicationManager = new PeriodicPublicationManager();
    private final GenreManager genreManager = new GenreManager();
    private final BorrowsManager borrowManager = new BorrowsManager();



    public Boolean addElement (Element element) {
        ConnectionManager cM = ConnectionManager.getInstance();
        try{
            if(element.getClass() != Element.class){
                Integer elementId = null;
                if(element instanceof Book) {
                    Book book = (Book)element;


                    elementId = bookManager.addBook((Book) element);
                }
                else if(element instanceof DigitalMedia) {
                    elementId = digitalMediaManager.addDigitalMedia((DigitalMedia) element);
                }
                else if(element instanceof PeriodicPublication) {
                    elementId = periodicPublicationManager.addPeriodicPublication((PeriodicPublication) element);
                }
                if(elementId == null){
                    return false;
                }
                for(Genre genre : element.getGenres()){
                    genreManager.associateGenreWithElement(elementId, genre.getCode());
                }
                return true;
            }
            else{
                return false;
            }
        } catch (SQLException e) {
            return false;
        }
    }

    public Boolean removeElement (Element element) {
        try{
            elementManager.removeElement(element.getId());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean updateElement (Element element) {
        try {
            if (element instanceof Book) {
                bookManager.updateBook((Book) element);
            } else if (element instanceof DigitalMedia) {
                digitalMediaManager.updateDigitalMedia((DigitalMedia) element);
            } else if (element instanceof PeriodicPublication) {
                periodicPublicationManager.updatePeriodicPublication((PeriodicPublication) element);
            } else {
                return false;
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
