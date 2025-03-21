package com.swe.libraryprogram.domainmodel;


public class User {


    private String email;
    private String password;
    private String name;
    private String surname;
    private String phone;
    private Boolean isAdmin = false;

    public User(String email, String password, String name, String surname, String phone, Boolean isAdmin) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
        this.isAdmin = isAdmin;

    }

    public User(String email, String password, String name, String surname, String phone) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.phone = phone;

    }

    public User(String email, String password, String name, String surname) {

        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean isAdmin() {
        return isAdmin;
    }

}
