package com.swe.libraryprogram.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ModifiedElementsManager {

    public ModifiedElementsManager() {}


    public void addEdit(Integer element_id, String admin_id) throws SQLException {

        String query = "INSERT INTO modifiedelements (adminid, elid) VALUES (?, ?)";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {
                System.err.println("Connessione al database non valida.");

            }

            stmt.setInt(1, element_id);
            stmt.setString(2, admin_id);

            stmt.executeUpdate();

        }

    }


}
