package com.swe.libraryprogram.dao;
import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.Genre;
import com.swe.libraryprogram.domainmodel.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
            //Class.forName("org.postgresql.Driver");
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

    public boolean insertUser(User user) {

        String query = "INSERT INTO users (email, password, name, surname, phone) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getName());
            statement.setString(4, user.getSurname());
            statement.setString(5, user.getPhone());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0; // True se almeno una riga è stata inserita

        } catch (SQLException e) {

            System.err.println("Errore durante l'inserimento dell'utente: " + e.getMessage());
            return false;

        }

    }

    public boolean insertElement (Element element) {

        String query = "INSERT INTO elements (title, release_year, description, quantity, quantity_available, length) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, element.getTitle());
            statement.setInt(2, element.getReleaseYear());
            statement.setString(3, element.getDescription());
            statement.setInt(4, element.getQuantity());
            statement.setInt(5, element.getQuantityAvailable());
            statement.setInt(6, element.getLength());

            int rowsInserted = statement.executeUpdate();
            return rowsInserted > 0; // True se almeno una riga è stata inserita

        } catch (SQLException e) {

            System.err.println("Errore durante l'inserimento dell'elemento: " + e.getMessage());
            return false;

        }

    }

    public List<Element> getAllElements() {
        List<Element> elements = new ArrayList<>();
        String query = "SELECT * FROM elements";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Integer elementId = rs.getInt("id");

                // Recupero i generi associati all'elemento
                LinkedList<Genre> genres = getGenresForElement(conn, elementId);

                // Creo l'oggetto Element con i dati estratti
                elements.add(new Element(
                        rs.getString("title"),
                        rs.getInt("release_year"),
                        rs.getString("description"),
                        rs.getInt("quantity"),
                        rs.getInt("quantity_available"),
                        rs.getInt("length"),
                        genres
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore durante il recupero degli elementi: " + e.getMessage());
        }

        return elements;
    }

    private LinkedList<Genre> getGenresForElement(Connection conn, int elementId) {
        LinkedList<Genre> genres = new LinkedList<>();
        String query = "SELECT g.name, g.code, g.description FROM genres g " +
                "JOIN element_genres eg ON g.code = eg.genre_code " +
                "WHERE eg.element_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, elementId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                genres.add(new Genre(
                        rs.getString("name"),
                        rs.getInt("code"),
                        rs.getString("description")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Errore nel recupero dei generi per l'elemento " + elementId + ": " + e.getMessage());
        }

        return genres;
    }

}
