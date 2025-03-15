package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.domainmodel.Element;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;



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

    public List<Element> getBorrowedElementsForUser(String user_id) throws SQLException {

        if (user_id == null) {
            throw new IllegalArgumentException("ID utente non valido.");

        }

        List<Element> elements = new ArrayList<>();

        String query = "SELECT elementid FROM borrows WHERE userid = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return elements;

            }

            stmt.setString(1, user_id);

            try (ResultSet rs = stmt.executeQuery()) {

                ElementManager elementManager = new ElementManager();

                while (rs.next()) {

                    int elementId = rs.getInt("elementid");

                    Element element = elementManager.getElement(elementId);

                    if (element != null) {
                        elements.add(element);

                    }

                }

            }

        }

        return elements;

    }


}
