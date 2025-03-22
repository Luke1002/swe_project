package com.swe.libraryprogram.services;

import com.swe.libraryprogram.domainmodel.User;
import com.swe.libraryprogram.orm.UserManager;

import java.sql.SQLException;

public class UserController {

    private final UserManager userManager = new UserManager();

    public User login (String email, String password){
        if (email == null || password == null || email.isEmpty() || password.isEmpty()){
            return null;
        }
        try {
            return userManager.authenticate(email, password);
        }
        catch (SQLException e){
            return null;
        }
    }

    public Boolean signup (String email, String password, String name, String surname, String phone) {
        if(email == null || password == null || name == null || surname == null ||email.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty() || password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!#$%&=?@])[A-Za-z\\d!#$%&=?@]{8,20}$")) {
            return false;
        }
        try{
                if (userManager.getUser(email) != null) {
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

    public void logout(){
        MainController.getInstance().resetUserState();
    }
}
