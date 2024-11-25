package com.swe.libraryprogram.domainmodel;

import java.util.LinkedList;



public class LibraryUser extends User {


    private Integer nStrikes;

    //TODO: aggiungere una classe per le statistiche

    //TODO: aggiungere cronologia elementi presi

    private LinkedList<Element> takenElements; //limitata da una variabile (a 5 elementi)



    public LibraryUser(String email, String password, String name, String surname, String phone, Integer nStrikes, LinkedList<Element> takenElements, Integer nElements) {

        super(email, password, name, surname, phone);

        this.nStrikes = nStrikes;
        this.takenElements = takenElements;

    }

    public LibraryUser(String email, String password, String name, String surname, Integer nStrikes, LinkedList<Element> takenElements, Integer nElements) {

        super(email, password, name, surname);

        this.nStrikes = nStrikes;
        this.takenElements = takenElements;

    }


    public Integer getnStrikes() {
        return nStrikes;
    }

    public void setnStrikes(Integer nStrikes) {
        this.nStrikes = nStrikes;
    }

    public LinkedList<Element> getTakenElements() {
        return takenElements;
    }

    public void setTakenElements(LinkedList<Element> takenElements) { this.takenElements = takenElements; }

}
