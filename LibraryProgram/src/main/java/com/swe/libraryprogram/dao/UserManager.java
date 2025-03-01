package com.swe.libraryprogram.dao;
import com.swe.libraryprogram.dao.ConnectionManager;
import com.swe.libraryprogram.domainmodel.User;

import java.sql.ResultSet;

public class UserManager {
    ConnectionManager connectionManager;

    public UserManager(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public String getUser(){

        ResultSet set = connectionManager.findTableRowString("luca.lascialfari@edu.unifi.it", "email", "users");

        try{
            if(set.next()){
                return set.getString("password");
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return "Not found";

    }

    public boolean authenticate(String email, String password){

        ResultSet set = connectionManager.findTableRowString(email, "email", "users");

        try{
            if(set.next()){
                return set.getString("password").equals(password);
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        return false;

    }

    public boolean addUser(String email, String password, String name, String surname, String phone){

        try{

            connectionManager.insertUser(new User(email, password, name, surname, phone));
            return true;

        }
        catch(Exception e){

           e.printStackTrace();

    }

        return false;

    }


}
