package com.swe.libraryprogram.dao;

import java.sql.*;



public class ConnectionManager {


    private static ConnectionManager singleton;
    private static Connection connection = null;

    private final String dbUrl = "jdbc:postgresql://berrettilove.minecraftnoob.com:5432/postgres";
    private final String dbUser = "postgres";
    private final String dbPass = "postgres";



    private ConnectionManager() {}

    public static ConnectionManager getInstance() {

        if (singleton == null) {

            singleton = new ConnectionManager();

        }

        return singleton;

    }


    public Connection getConnection() {

        try {

            if (connection == null || connection.isClosed()) {

                connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);

            }

        } catch (SQLException e) {

            e.printStackTrace();
            throw new RuntimeException("Errore di connessione al database: " + e.getMessage());

        }

        return connection;

    }

    //TODO: da modificare
    public Boolean isConnectionValid() {

        try {

            if (connection == null || connection.isClosed()) {

                return false;

            }

            return connection.isValid(5);

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    public void closeConnection() {

        try {

            if (connection != null && !connection.isClosed()) {

                connection.close();

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

    }


}