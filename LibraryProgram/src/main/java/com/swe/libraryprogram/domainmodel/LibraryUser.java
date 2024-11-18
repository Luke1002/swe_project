package com.swe.libraryprogram.domainmodel;

import java.util.LinkedList;

public class LibraryUser extends User {

    private Integer nStrikes;
    private LinkedList<Element> currTakenElements; //limitata da una variabile (a 5 elementi)
    private Integer nElements;

    public LibraryUser(String email, String password, String name, String surname, String phone, Integer nStrikes, LinkedList<Element> currTakenElements, Integer nElements) {
        super(email, password, name, surname, phone);
        this.nStrikes = nStrikes;
        this.currTakenElements = currTakenElements;
        this.nElements = nElements;
    }

    public LibraryUser(String email, String password, String name, String surname, Integer nStrikes, LinkedList<Element> currTakenElements, Integer nElements) {
        super(email, password, name, surname);
        this.nStrikes = nStrikes;
        this.currTakenElements = currTakenElements;
        this.nElements = nElements;
    }

    public Integer getnStrikes() {
        return nStrikes;
    }

    public void setnStrikes(Integer nStrikes) {
        this.nStrikes = nStrikes;
    }

    public LinkedList<Element> getCurrTakenElements() {
        return currTakenElements;
    }

    public void setCurrTakenElements(LinkedList<Element> currTakenElements) {
        this.currTakenElements = currTakenElements;
    }

    public Integer getnElements() {
        return nElements;
    }

    public void setnElements(Integer nElements) {
        this.nElements = nElements;
    }
}
