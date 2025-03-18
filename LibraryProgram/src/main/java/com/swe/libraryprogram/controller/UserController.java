package com.swe.libraryprogram.controller;

import com.swe.libraryprogram.domainmodel.User;
import com.swe.libraryprogram.dao.UserManager;

import java.sql.SQLException;

public class UserController {

    private final UserManager userManager = new UserManager();

    public User login (String email, String password) throws SQLException {
        return userManager.authenticate(email, password);
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
