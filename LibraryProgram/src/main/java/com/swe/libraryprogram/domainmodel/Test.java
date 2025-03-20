package com.swe.libraryprogram.domainmodel;

import com.swe.libraryprogram.dao.ConnectionManager;
import com.swe.libraryprogram.dao.UserManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Test {

    public static void main(String[] args) {
        String userName = "lb_user";
        String dbPassword = "5^@34o8c4#X&9$fa";
        ConnectionManager.setDbUser(userName);
        ConnectionManager.setDbPass(dbPassword);
        ConnectionManager cM = ConnectionManager.getInstance();
        Connection c = cM.getConnection();
        PreparedStatement stmnt = null;
        try {
            stmnt = c.prepareStatement("select * from elements");
        }
        catch(SQLException e) {
            e.printStackTrace();
        }
        UserManager userManager = new UserManager();
        User usr;
        try{
            usr = userManager.authenticate("luca.lascialfari@edu.unifi.it", "password");
            System.out.println(usr.getName());
            System.out.println(usr.getSurname());
        }
        catch(SQLException e){
            System.out.println(e);
        }
        System.out.println(2);
    }

}
