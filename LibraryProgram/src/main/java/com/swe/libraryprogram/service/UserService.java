package com.swe.libraryprogram.service;

import com.swe.libraryprogram.domainmodel.User;
import com.swe.libraryprogram.orm.UserDAO;

import java.sql.SQLException;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public User login (String email, String password){
        if (email == null || password == null || email.isEmpty() || password.isEmpty()){
            return null;
        }
        try {
            return userDAO.authenticate(email, password);
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
                if (userDAO.getUser(email) != null) {
                    return false;
                }
            else{

                return userDAO.addUser(new User(email, password, name, surname, phone));
            }
        }
        catch(SQLException e){
            return false;
        }
    }

    public void logout(){
        MainService.getInstance().resetUserState();
    }
}
