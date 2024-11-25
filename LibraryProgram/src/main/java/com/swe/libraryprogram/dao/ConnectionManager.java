package com.swe.libraryprogram.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConnectionManager {

    private static ConnectionManager singleton;
    private final String dbUrl = "jdbc:postgresql://berrettilove.minecraftnoob.com:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPass = "postgres";

    private ConnectionManager() {
    }

    public synchronized static ConnectionManager getInstance() {
        if (singleton == null) {
            singleton = new ConnectionManager();
        }

        return singleton;
    }

    public Boolean startingConnectionCheck() {
        Connection c;
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            return c.isValid(5);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }

    }

}
