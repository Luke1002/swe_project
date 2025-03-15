package com.swe.libraryprogram.controller;

//import com.swe.libraryprogram.domainmodel.Element;


import com.swe.libraryprogram.domainmodel.User;
import com.swe.libraryprogram.dao.UserManager;

import java.sql.SQLException;

public class UserController {

    private final UserManager userManager = new UserManager();

    public Boolean login (String email, String password) {
        try{
            return userManager.authenticate(email, password);
        } catch (SQLException e) {
            return false;
        }
    }

    public Boolean signup (String email, String password, String name, String surname, String phone) {
        try{
            if(userManager.getUser(email) != null){
                return false;
            }
            else{
                return userManager.addUser(new User(email, password, name, surname, phone));
            }
        }
        catch(SQLException e){
            return false;
        }
    }
}
