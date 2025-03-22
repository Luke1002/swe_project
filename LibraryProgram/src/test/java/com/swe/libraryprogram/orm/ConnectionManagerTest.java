package com.swe.libraryprogram.orm;

import java.sql.*;



public class ConnectionManagerTest {


    private static ConnectionManagerTest singleton;
    private static Connection connection = null;

    private static final String dbUrl = "jdbc:postgresql://sweproject.ladatap.com:8445/test";
    private static String dbUser = "lb_user";
    private static String dbPass = "5^@34o8c4#X&9$fa";



    private ConnectionManagerTest() {
        try{
            Class.forName("org.postgresql.Driver");
        }
        catch(ClassNotFoundException e){
            throw new RuntimeException("Driver JDBC non trovato", e);
        }
    }

    public synchronized static ConnectionManagerTest getInstance() {

        if (singleton == null) {

            singleton = new ConnectionManagerTest();

        }

        return singleton;

    }

    public static void setDbUser(String dbUser) {
        ConnectionManagerTest.dbUser = dbUser;
    }

    public static void setDbPass(String dbPass) {
        ConnectionManagerTest.dbPass = dbPass;
    }

    public static Connection getConnection() {
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