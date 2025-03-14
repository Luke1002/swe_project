package com.swe.libraryprogram.controller;


import com.swe.libraryprogram.dao.*;
import com.swe.libraryprogram.domainmodel.*;

import java.sql.SQLException;


public class LibraryAdminController {


    private final ElementManager elementManager = new ElementManager();
    private final BookManager bookManager = new BookManager();
    private final DigitalMediaManager digitalMediaManager = new DigitalMediaManager();
    private final PeriodicPublicationManager periodicPublicationManager = new PeriodicPublicationManager();
    private final BorrowsManager borrowManager = new BorrowsManager();
    private final ModifiedElementsManager modifiedElements = new ModifiedElementsManager();
    private User usr;

    public LibraryAdminController(User usr) {
        this.usr = usr;
    }



    public Boolean addElement (Element element) {
        ConnectionManager cM = ConnectionManager.getInstance();
        try{
            cM.getConnection().setAutoCommit(false);
            if(element instanceof Book) {
                bookManager.addBook((Book) element);
                cM.getConnection().commit();
            }
            else if(element instanceof DigitalMedia) {
                digitalMediaManager.addDigitalMedia((DigitalMedia) element);
                cM.getConnection().commit();
            }
            else if(element instanceof PeriodicPublication) {
                periodicPublicationManager.addPeriodicPublication((PeriodicPublication) element);
                cM.getConnection().commit();
            }
            else{
                cM.getConnection().setAutoCommit(true);
                return false;
            }
            cM.getConnection().setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            try{
                cM.getConnection().rollback();
                cM.getConnection().setAutoCommit(true);
            }catch (SQLException exception){
                exception.printStackTrace();
            }
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
        ConnectionManager cM = ConnectionManager.getInstance();
        try {
            cM.getConnection().setAutoCommit(false);
            if (element instanceof Book) {
                bookManager.updateBook((Book) element);
            } else if (element instanceof DigitalMedia) {
                digitalMediaManager.updateDigitalMedia((DigitalMedia) element);
            } else if (element instanceof PeriodicPublication) {
                periodicPublicationManager.updatePeriodicPublication((PeriodicPublication) element);
            } else {
                cM.getConnection().setAutoCommit(true);
                return false;
            }
            elementManager.updateElement(element);
            modifiedElements.addEdit(element.getId(), usr.getEmail());
            cM.getConnection().commit();
            cM.getConnection().setAutoCommit(true);
            return true;
        } catch (SQLException e) {
            try{
                cM.getConnection().rollback();
                cM.getConnection().setAutoCommit(true);
            }catch (SQLException exception){
                exception.printStackTrace();
            }
            return false;
        }
    }

    public void getLibraryStatistics () {}

}
