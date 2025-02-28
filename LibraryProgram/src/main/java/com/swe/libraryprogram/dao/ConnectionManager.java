package com.swe.libraryprogram.dao;
import java.sql.*;

public class ConnectionManager {

    private static ConnectionManager singleton;
    private static Connection connection = null;
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
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPass);
            return connection.isValid(5);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return false;
        }
    }

    public ResultSet findTableRowString(String id, String columnName, String tableName) {
        try {
            String query = "select * from " + tableName + " where " + columnName + " = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, id);
            return statement.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            return null;
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
