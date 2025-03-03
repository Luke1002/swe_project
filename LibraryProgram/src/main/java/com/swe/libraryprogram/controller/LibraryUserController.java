package com.swe.libraryprogram.controller;

import com.swe.libraryprogram.dao.UserManager;
import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.dao.ElementManager;
import com.swe.libraryprogram.dao.BorrowManager;



public class LibraryUserController {

    private final ElementManager elementManager = new ElementManager();
    private final BorrowManager borrowManager = new BorrowManager();

    //TODO: gestire statistiche biblioteca



    public Boolean borrowElement (String element_id) {

    }

    public Boolean returnElement (Element element) {}

    public void getPersonalStatistics () {}

    public void reportElement () {}

}
