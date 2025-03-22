package com.swe.libraryprogram.service;

import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService {


    public User login (String email, String password){
        if (email == null || password == null || email.isEmpty() || password.isEmpty()){
            return null;
        }
        try {
            return MainService.getInstance().getUserManager().authenticate(email, password);
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
                if (MainService.getInstance().getUserManager().getUser(email) != null) {
                    return false;
                }
            else{

                return MainService.getInstance().getUserManager().addUser(new User(email, password, name, surname, phone));
            }
        }
        catch(SQLException e){
            return false;
        }
    }

    public void logout(){
        MainService.getInstance().resetUserState();
    }

    public List<Element> getAllElements(){
        List<Element> elements = new ArrayList<>();
        try {
            elements = MainService.getInstance().getElementManager().getAllElements();
            return elements;
        } catch (SQLException _) {
            return null;
        }
    }
}
