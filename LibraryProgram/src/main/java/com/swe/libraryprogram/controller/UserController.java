package com.swe.libraryprogram.controller;

//import com.swe.libraryprogram.domainmodel.Element;


import com.swe.libraryprogram.domainmodel.User;
import com.swe.libraryprogram.dao.UserManager;

public class UserController {

    private final UserManager userManager = new UserManager();

    public Boolean login (String email, String password) {
        return userManager.authenticate(email, password);
    }

    public Boolean signup (String email, String password, String name, String surname, String phone) {
        if(userManager.getUser(email) != null){
            return false;
        }
        else{
            return userManager.addUser(new User(email, password, name, surname, phone));
        }
    }
}
