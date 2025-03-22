package com.swe.libraryprogram.service;

import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.User;

import java.sql.SQLException;
import java.util.List;


public class LibraryUserService extends UserService {

    public LibraryUserService() {
    }

    public List<Element> getBorrowedElements(User user){
        try{
            List<Element> borrowedElements = MainService.getInstance().getBorrowsDAO().getBorrowedElementsForUser(user.getEmail());
            return borrowedElements;
        } catch (SQLException e) {
            return null;
        }
    }

    public Boolean borrowElement(Integer element_id) {
        try {
            Element element = MainService.getInstance().getElementDAO().getElement(element_id);
            if (element != null && element.getQuantityAvailable() > 0) {
                element.setQuantityAvailable(element.getQuantityAvailable() - 1);
                MainService.getInstance().getElementDAO().updateElement(element);
                MainService.getInstance().getBorrowsDAO().addBorrow(element_id, MainService.getInstance().getUser().getEmail());
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

    public Boolean returnElement(Integer element_id) {
        try {
            Element element = MainService.getInstance().getElementDAO().getElement(element_id);
            if (element != null && element.getQuantityAvailable() < element.getQuantity()) {
                element.setQuantityAvailable(element.getQuantityAvailable() + 1);
                MainService.getInstance().getElementDAO().updateElement(element);
                MainService.getInstance().getBorrowsDAO().removeBorrow(element_id, MainService.getInstance().getUser().getEmail());
                return true;
            }
            return false;
        } catch (SQLException e) {
            return false;
        }
    }

}
