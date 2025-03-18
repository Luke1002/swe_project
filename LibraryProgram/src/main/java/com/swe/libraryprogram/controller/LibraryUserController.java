package com.swe.libraryprogram.controller;

import com.swe.libraryprogram.dao.ConnectionManager;
import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.dao.ElementManager;
import com.swe.libraryprogram.dao.BorrowsManager;
import com.swe.libraryprogram.domainmodel.User;

import java.sql.SQLException;


public class LibraryUserController extends UserController {

    private final ElementManager elementManager = new ElementManager();
    private final BorrowsManager borrowManager = new BorrowsManager();
    private User usr;

    public LibraryUserController() {
    }

    public Boolean borrowElement (Integer element_id) {
        ConnectionManager cM = ConnectionManager.getInstance();
        try{
            cM.getConnection().setAutoCommit(false);
            Element element = elementManager.getElement(element_id);
            if (element!=null && element.getQuantityAvailable()>0) {
                element.setQuantityAvailable(element.getQuantityAvailable()-1);
                elementManager.updateElement(element);
                borrowManager.addBorrow(element_id, usr.getEmail());
                cM.getConnection().commit();
                cM.getConnection().setAutoCommit(true);
                return true;
            }
            return false;
        }catch (SQLException e){
            try{
                cM.getConnection().rollback();
                cM.getConnection().setAutoCommit(true);
            }catch (SQLException exception){
                exception.printStackTrace();
            }
            return false;
        }
    }

    public Boolean returnElement (Integer element_id) {
        ConnectionManager cM = ConnectionManager.getInstance();
        try{
            cM.getConnection().setAutoCommit(false);
            Element element = elementManager.getElement(element_id);
            if (element!=null && element.getQuantityAvailable()<element.getQuantity()) {
                element.setQuantityAvailable(element.getQuantityAvailable()+1);
                elementManager.updateElement(element);
                borrowManager.removeBorrow(element_id, usr.getEmail());
                cM.getConnection().commit();
                cM.getConnection().setAutoCommit(true);
                return true;
            }
            return false;
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

    // public void reportLostElement (Integer element_id) {}

}
