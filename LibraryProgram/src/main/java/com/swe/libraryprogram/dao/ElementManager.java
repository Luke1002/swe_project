package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.domainmodel.Element;
import java.util.List;


public class ElementManager {

    ConnectionManager connectionManager;


    public ElementManager(ConnectionManager connectionManager) {

        this.connectionManager = connectionManager;

    }

    //public void addElement(String title, String genre, Integer year){}

    //public void removeElement(Integer id){}

    //public void updateElement(Integer id, String title, String genre, Integer year){}

    public List<Element> getAllElements(){

        return connectionManager.getAllElements();

    }


}
