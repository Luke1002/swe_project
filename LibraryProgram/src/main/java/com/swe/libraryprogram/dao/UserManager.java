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
}
