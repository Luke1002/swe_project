package com.swe.libraryprogram.domainmodel;

public class LibraryAdmin extends User {
    public LibraryAdmin(String email, String password, String name, String surname, String phone) {
        super(email, password, name, surname, phone);
    }

    public LibraryAdmin(String email, String password, String name, String surname) {
        super(email, password, name, surname);
    }
}
