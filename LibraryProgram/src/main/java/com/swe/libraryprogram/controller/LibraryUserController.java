package com.swe.libraryprogram.controller;

import com.swe.libraryprogram.dao.UserManager;
import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.dao.ElementManager;
import com.swe.libraryprogram.dao.BorrowsManager;



public class LibraryUserController {

    private final ElementManager elementManager = new ElementManager();
    private final BorrowsManager borrowManager = new BorrowsManager();

    //TODO: gestire statistiche biblioteca



    public Boolean borrowElement (String element_id) {return null;}

    public Boolean returnElement (Element element) {return null;}

    public void getPersonalStatistics () {}

    public void reportElement () {}

}
