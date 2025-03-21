package com.swe.libraryprogram.controller;

import com.swe.libraryprogram.domainmodel.Element;

import java.sql.SQLException;


public class LibraryUserController extends UserController {

    public LibraryUserController() {
    }

    public Boolean borrowElement(Integer element_id) {
        try {
            Element element = MainController.getInstance().getElementManager().getElement(element_id);
            if (element != null && element.getQuantityAvailable() > 0) {
                element.setQuantityAvailable(element.getQuantityAvailable() - 1);
                MainController.getInstance().getElementManager().updateElement(element);
                MainController.getInstance().getBorrowsManager().addBorrow(element_id, MainController.getInstance().getUser().getEmail());
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean returnElement(Integer element_id) {
        try {
            Element element = MainController.getInstance().getElementManager().getElement(element_id);
            if (element != null && element.getQuantityAvailable() < element.getQuantity()) {
                element.setQuantityAvailable(element.getQuantityAvailable() + 1);
                MainController.getInstance().getElementManager().updateElement(element);
                MainController.getInstance().getBorrowsManager().removeBorrow(element_id, MainController.getInstance().getUser().getEmail());
                return true;
            }
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
