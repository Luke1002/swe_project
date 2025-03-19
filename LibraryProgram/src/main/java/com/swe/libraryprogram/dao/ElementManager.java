package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.Genre;

import java.util.LinkedList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;


public class ElementManager {

    public ElementManager() {
    }

    public Boolean removeElement(Integer id) throws SQLException {

        String query = "DELETE FROM elements WHERE id = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {

                System.out.println("Elemento con ID " + id + " rimosso correttamente.");
                return true;

            } else {

                System.err.println("Nessun elemento trovato con ID " + id);
                return false;

            }

        }

    }

    public Element getElement(Integer id) throws SQLException {

        String query = "SELECT * FROM elements WHERE id = ?";

        List<Element> elements = executeQueryWithSingleValue(query, id);
        return elements.getFirst();

    }

    public List<Element> getAllElements() throws SQLException {

        List<Element> elements = new ArrayList<>();

        String query = "SELECT * FROM elements";

        Connection connection = ConnectionManager.getInstance().getConnection();

        if (!ConnectionManager.getInstance().isConnectionValid()) {

            System.err.println("Connessione al database non valida.");
            return elements;

        }
        PreparedStatement stmt = connection.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();

        GenreManager genreManager = new GenreManager();

        while (rs.next()) {

            List<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

            Integer releaseYear = rs.getInt("release_year");
            if (rs.wasNull()) {
                releaseYear = null;
            }
            Integer length = rs.getInt("length");
            if (rs.wasNull()) {
                length = null;
            }

            Element element = new Element(
                    rs.getInt("id"),
                    rs.getString("title"),
                    releaseYear,
                    rs.getString("description"),
                    rs.getInt("quantity"),
                    rs.getInt("quantity_available"),
                    length,
                    genres
            );

            elements.add(element);

        }

        return elements;

    }

    public List<Element> getElementsByGenre(String genreName) throws SQLException {

        String query = "SELECT e.* FROM elements e " +
                "JOIN elementgenres eg ON e.id = eg.elementid " +
                "JOIN genres g ON eg.genrecode = g.genrecode " +
                "WHERE g.name = ?";

        return executeQueryWithSingleValue(query, genreName);

    }

    public List<Element> getElementsByTitle(String title) throws SQLException {

        String query = "SELECT * FROM elements WHERE title = ?";

        return executeQueryWithSingleValue(query, title);

    }

    public List<Element> getElementsByReleaseYear(Integer releaseYear) throws SQLException {

        String query = "SELECT * FROM elements WHERE releaseyear = ?";

        return executeQueryWithSingleValue(query, releaseYear);

    }

    public List<Element> getElementsByLength(Integer length) throws SQLException {

        String query = "SELECT * FROM elements WHERE length = ?";

        return executeQueryWithSingleValue(query, length);

    }

    protected List<Element> executeQueryWithSingleValue(String query, Object value) throws SQLException {

        List<Element> elements = new ArrayList<>();

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return null;

            }

            stmt.setObject(1, value);

            try (ResultSet rs = stmt.executeQuery()) {

                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    List<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    Element element = new Element(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres
                    );

                    elements.add(element);

                }

                return elements;

            }

        }

    }

    public Boolean isElementAvailable(Integer id) throws SQLException {

        String query = "SELECT quantityavailable FROM elements WHERE id = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    int quantityAvailable = rs.getInt("quantityavailable");

                    if (quantityAvailable > 0) {
                        return true;

                    } else {

                        System.err.println("Elemento non disponibile.");
                        return false;

                    }

                } else {

                    System.err.println("Elemento non trovato.");
                    return false;

                }

            }

        }

    }

    public List<Element> getFilteredElements(String title, Integer releaseYear, List<Genre> genres, Map<String, Object> additionalFilters) throws SQLException {

        List<Element> elements = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        // Costruzione dinamica della query,
        // In questo modo tutti i filtri successivi possono iniziare con "AND",
        // senza dover controllare se WHERE esiste già (1=1 è sempre vero)
        StringBuilder query = new StringBuilder("SELECT * FROM elements WHERE 1=1");

        if (title != null && !title.isEmpty()) {

            query.append(" AND title LIKE ?");
            parameters.add("%" + title + "%");

        }

        if (releaseYear != null) {

            query.append(" AND releaseyear = ?");
            parameters.add(releaseYear);

        }

        if (genres != null && !genres.isEmpty()) {

            query.append(" AND id IN (SELECT elementid FROM elementgenres WHERE genreid IN (");

            // Aggiunta di segnaposti "?" per ogni genere nella lista
            // ricavando i dati dalla struttura dati usando stream e collect di java
            query.append(genres.stream().map(g -> "?").collect(Collectors.joining(", ")));
            query.append("))"); // Chiusura della query

            // Estrazione e aggiunta dei codici (id) dei generi alla lista dei parametri
            parameters.addAll(genres.stream().map(Genre::getCode).toList());

        }

        addCustomFilters(query, parameters, additionalFilters);

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query.toString())) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return elements;

            }

            // Imposta i parametri dinamici IN ORDINE
            for (int i = 0; i < parameters.size(); i++) {

                if (parameters.get(i) instanceof String) {
                    stmt.setString(i + 1, (String) parameters.get(i));

                } else if (parameters.get(i) instanceof Integer) {
                    stmt.setInt(i + 1, (Integer) parameters.get(i));

                }

            }

            try (ResultSet rs = stmt.executeQuery()) {

                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    List<Genre> elementGenres = genreManager.getGenresForElement(rs.getInt("id"));

                    Element element = new Element(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            elementGenres
                    );

                    elements.add(element);

                }

            }

        }

        return elements;

    }

    protected void addCustomFilters(StringBuilder query, List<Object> parameters, Map<String, Object> customFilters) {
        // Metodo vuoto per essere sovrascritto nelle classi figlie
    }


}