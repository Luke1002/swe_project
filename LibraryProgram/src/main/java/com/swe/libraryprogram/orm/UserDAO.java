package com.swe.libraryprogram.orm;

import com.swe.libraryprogram.domainmodel.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class UserDAO {

    public UserDAO() {
    }


    public User getUser(String email) throws SQLException, RuntimeException {

        String query = "SELECT * FROM users WHERE email = ?";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);

        stmt.setString(1, email);
        ResultSet rs = stmt.executeQuery();
        User user = null;
        if (rs.next()) {
            user = new User(rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("phone"),
                    rs.getBoolean("isadmin"));

        } else {
            throw new RuntimeException("E-mail non presente.");
        }
        stmt.close();
        return user;
    }

    public User authenticate(String email, String password) throws SQLException, RuntimeException {
        getUser(email);
        String query = "SELECT * FROM users WHERE email = ? AND password = ?";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, email);
        stmt.setString(2, password);
        ResultSet rs = stmt.executeQuery();
        User user = null;
        if (rs.next()) {
            user = new User(rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("name"),
                    rs.getString("surname"),
                    rs.getString("phone"),
                    rs.getBoolean("isadmin"));
        } else {
            throw new RuntimeException("Password errata");
        }
        stmt.close();
        return user;
    }

    public Boolean addUser(User user) throws SQLException {

        String query = "INSERT INTO users (email, password, name, surname, phone, isadmin) VALUES (?, ?, ?, ?, COALESCE(?, ''), ?)";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);

        stmt.setString(1, user.getEmail());
        stmt.setString(2, user.getPassword());
        stmt.setString(3, user.getName());
        stmt.setString(4, user.getSurname());
        if (user.getPhone() == null) {
            stmt.setNull(5, java.sql.Types.VARCHAR);
        } else {
            stmt.setString(5, user.getPhone());
        }
        stmt.setBoolean(6, user.isAdmin());

        int rowsInserted = stmt.executeUpdate();
        stmt.close();
        if (rowsInserted > 0) {

            System.out.println("Utente inserito correttamente.");
            return true;

        } else {

            System.err.println("Errore nell'inserimento dell'utente.");
            return false;

        }

    }

    public Boolean updateUser(User user) throws SQLException {

        String query = "UPDATE users SET password = ?, name = ?, surname = ?, phone = ?, isadmin = ? WHERE email = ?";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);


        stmt.setString(1, user.getPassword());
        stmt.setString(2, user.getName());
        stmt.setString(3, user.getSurname());
        stmt.setString(4, user.getPhone());
        stmt.setBoolean(5, user.isAdmin());
        stmt.setString(6, user.getEmail());

        int rowsUpdated = stmt.executeUpdate();

        if (rowsUpdated > 0) {

            System.out.println("Utente aggiornato correttamente.");
            return true;

        } else {

            System.err.println("Errore nell'aggiornamento dell'utente.");
            return false;

        }

    }

    public Boolean removeUser(String email) throws SQLException {

        String query = "DELETE FROM users WHERE email = ?";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);

        stmt.setString(1, email);

        int rowsDeleted = stmt.executeUpdate();

        if (rowsDeleted > 0) {

            System.out.println("Utente rimosso correttamente.");
            return true;

        } else {

            System.err.println("Nessun utente trovato con email: " + email);
            return false;

        }

    }

    public List<User> getAllUsers() throws SQLException {

        List<User> users = new ArrayList<>();

        String query = "SELECT * FROM users";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);


        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            User user = new User(rs.getString("email"), rs.getString("password"), rs.getString("name"), rs.getString("surname"), rs.getString("phone"), rs.getBoolean("isadmin"));

            users.add(user);

        }

        return users;

    }


}
