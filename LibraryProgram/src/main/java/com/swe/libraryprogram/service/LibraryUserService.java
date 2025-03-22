package com.swe.libraryprogram.service;

import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.User;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;


public class LibraryUserService extends UserService {

    public LibraryUserService() {
    }

    public List<Element> getBorrowedElements(User user) {
        try {
            List<Element> borrowedElements = MainService.getInstance().getBorrowsDAO().getBorrowedElementsForUser(user.getEmail());
            return borrowedElements;
        } catch (SQLException e) {
            System.out.println("Impossibile connettersi al database.");
            return null;
        }
    }

    public Boolean borrowElement(Integer elementId) {
        if (elementId == null) {
            System.out.println("Elemento non selezionato");
        }
        try {
            Element element = MainService.getInstance().getElementDAO().getElement(elementId);
            List<Element> elements = getBorrowedElements(MainService.getInstance().getUser());
            if (elements.contains(element)) {
                System.out.println("Elemento già preso in prestito.");
                return false;
            }
            if (element.getQuantityAvailable() <= 0) {
                System.out.println("Elemento non disponibile per il prestito.");
                return false;
            }
                MainService.getInstance().getElementDAO().updateElement(element);
                MainService.getInstance().getBorrowsDAO().addBorrow(elementId, MainService.getInstance().getUser().getEmail());
                return true;
        } catch (SQLException e) {
            System.out.println("Impossibile connettersi al database.");
        } catch (NoSuchElementException e) {
            System.out.println("Elemento non trovato");
        }
        return false;
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
            System.out.println("Impossibile connettersi al database.");
            return false;
        }
    }

}
