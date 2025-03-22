package com.swe.libraryprogram.orm;

import java.sql.*;



public class ConnectionManager {


    private static ConnectionManager singleton;
    private static Connection connection = null;

    private final String dbUrl = "jdbc:postgresql://sweproject.ladatap.com:8445/lb_db";
    private static String dbUser = "lb_user";
    private static String dbPass = "5^@34o8c4#X&9$fa";



    private ConnectionManager() {
        try{
            Class.forName("org.postgresql.Driver");
        }
        catch(ClassNotFoundException e){
            throw new RuntimeException("Driver JDBC non trovato", e);
        }
    }

    public synchronized static ConnectionManager getInstance() {

        if (singleton == null) {

            singleton = new ConnectionManager();

        }

        return singleton;

    }

    public static void setDbUser(String dbUser) {
        ConnectionManager.dbUser = dbUser;
    }

    public static void setDbPass(String dbPass) {
        ConnectionManager.dbPass = dbPass;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Errore di connessione al database", e);
        }
        return connection;
    }


    public Boolean isConnectionValid() {
        try {
            if (connection == null || connection.isClosed()) {
                return false;
            }
            return connection.isValid(5);
        } catch (SQLException e) {
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