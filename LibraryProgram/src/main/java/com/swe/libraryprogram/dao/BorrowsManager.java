package com.swe.libraryprogram.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class BorrowsManager {

    public BorrowsManager() {}


    public Boolean addBorrow(Integer element_id, String user_id) {

        if (element_id == null || element_id <= 0 || user_id == null) {

            System.err.println("ID utente o ID elemento non validi.");
            return false;

        }

        ElementManager elementManager = new ElementManager();

        if (!elementManager.isElementAvailable(element_id)) {

            System.err.println("Elemento non disponibile per il prestito.");
            return false;

        }

        String query = "INSERT INTO borrows (element_id, user_id) VALUES (?, ?)";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setInt(1, element_id);
            stmt.setString(2, user_id);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {

                String updateQuery = "UPDATE elements SET quantity_available = quantity_available - 1 WHERE id = ?";

                try (PreparedStatement stmtUpdate = connection.prepareStatement(updateQuery)) {

                    stmtUpdate.setInt(1, element_id);
                    stmtUpdate.executeUpdate();

                }

                return true;

            } else {

                System.err.println("Errore: il prestito non è stato registrato.");
                return false;

            }

        } catch (SQLException e) {

            System.err.println("Errore SQL durante la registrazione del prestito: " + e.getMessage());
            //e.printStackTrace();
            return false;

        }

    }

    public Boolean removeBorrow(String user_id, Integer element_id) {

        if (element_id == null || element_id <= 0 || user_id == null) {

            System.err.println("ID utente o ID elemento non validi.");
            return false;

        }

        String query = "DELETE FROM borrows WHERE element_id = ? AND user_id = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setInt(1, element_id);
            stmt.setString(2, user_id);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {

                String updateQuery = "UPDATE elements SET quantity_available = quantity_available + 1 WHERE id = ?";

                try (PreparedStatement stmtUpdate = connection.prepareStatement(updateQuery)) {

                    stmtUpdate.setInt(1, element_id);
                    stmtUpdate.executeUpdate();

                }

                return true;

            } else {

                System.err.println("Errore: il prestito non è stato rimosso.");
                return false;

            }

        } catch (SQLException e) {

            System.err.println("Errore SQL durante la rimozione del prestito: " + e.getMessage());
            //e.printStackTrace();
            return false;

        }

    }

}
