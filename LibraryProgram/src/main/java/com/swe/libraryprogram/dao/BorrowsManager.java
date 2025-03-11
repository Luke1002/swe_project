package com.swe.libraryprogram.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class BorrowsManager {


    public BorrowsManager() {}


    public void addBorrow(Integer element_id, String user_id) throws SQLException {

        if (element_id == null || element_id <= 0 || user_id == null) {
            throw new IllegalArgumentException("ID utente o ID elemento non validi.");

        }

        String query = "INSERT INTO borrows (elementid, userid) VALUES (?, ?)";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {
                System.err.println("Connessione al database non valida.");

            }

            stmt.setInt(1, element_id);
            stmt.setString(2, user_id);

            stmt.executeUpdate();

        }

    }

    public void removeBorrow(Integer element_id, String user_id) throws SQLException {

        if (element_id == null || element_id <= 0 || user_id == null) {
            throw new IllegalArgumentException("ID utente o ID elemento non validi.");

        }

        String query = "DELETE FROM borrows WHERE elementid = ? AND userid = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {
                System.err.println("Connessione al database non valida.");

            }

            stmt.setInt(1, element_id);
            stmt.setString(2, user_id);

            stmt.executeUpdate();

        }

    }


}
