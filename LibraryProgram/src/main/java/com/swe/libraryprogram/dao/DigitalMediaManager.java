package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.domainmodel.DigitalMedia;
import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.Genre;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;


public class DigitalMediaManager extends ElementManager {

    public DigitalMediaManager() {}


    public Integer addDigitalMedia(DigitalMedia media) throws SQLException {

        Integer elementId = addElement(media);

        if (elementId == null) {

            System.out.println("Errore: l'inserimento delle informazioni di base del media è fallito.");
            return null;

        }

        String insertMediaQuery = "INSERT INTO digitalmedias (id, producer, director, agerating) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement mediaStmt = connection.prepareStatement(insertMediaQuery)) {

            mediaStmt.setInt(1, elementId);
            mediaStmt.setString(2, media.getProducer());
            mediaStmt.setString(3, media.getDirector());
            mediaStmt.setString(4, media.getAgeRating());

            int rowsInserted = mediaStmt.executeUpdate();

            if (rowsInserted > 0) {
                return elementId;

            } else {

                System.err.println("Errore: il media non è stato inserito.");
                return null;

            }

        }

    }

    public Boolean updateDigitalMedia(DigitalMedia media) throws SQLException {

        if (!updateElement(media)) {

            System.out.println("Errore: l'aggiornamento delle informazioni base del media è fallito.");
            return false;

        }

        String updateMediaQuery = "UPDATE digitalmedias SET producer = ?, director = ?, agerating = ? WHERE id = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement mediaStmt = connection.prepareStatement(updateMediaQuery)) {

            mediaStmt.setString(1, media.getProducer());
            mediaStmt.setString(2, media.getDirector());
            mediaStmt.setString(3, media.getAgeRating());
            mediaStmt.setInt(4, media.getId());

            int rowsUpdated = mediaStmt.executeUpdate();

            if (rowsUpdated > 0) {
                return true;

            } else {

                System.err.println("Errore: il media non è stato aggiornato.");
                return false;

            }

        }

    }

    public Element getDigitalMedia(Integer id) throws SQLException {

        String getMediaQuery = "SELECT * FROM digitalmedias WHERE id = ?";

        List<Element> elements = executeQueryWithSingleValue(getMediaQuery, id);
        return elements.getFirst();

    }

    public List<DigitalMedia> getAllDigitalMedias() throws SQLException {

        List<DigitalMedia> digitalMedias = new ArrayList<>();

        String query = "SELECT * FROM elements e JOIN digitalmedias dm ON e.id = dm.id";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return digitalMedias;

            }

            try (ResultSet rs = stmt.executeQuery()) {

                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    List<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    DigitalMedia media = new DigitalMedia(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres,
                            rs.getString("producer"),
                            rs.getString("age_rating"),
                            rs.getString("director")
                    );

                    digitalMedias.add(media);

                }

                return digitalMedias;

            }

        }

    }

    public List<Element> getDigitalMediasByProducer(String producer) throws SQLException {

        String query = "SELECT * FROM elements e JOIN digitalmedias dm ON e.id = dm.id WHERE dm.producer = ?";

        return executeQueryWithSingleValue(query, producer);

    }

    public List<Element> getDigitalMediasByDirector(String director) throws SQLException {

        String query = "SELECT * FROM elements e JOIN digitalmedias dm ON e.id = dm.id WHERE dm.director = ?";

        return executeQueryWithSingleValue(query, director);

    }

    public List<Element> getDigitalMediasByAgeRating(Integer ageRating) throws SQLException {

        String query = "SELECT * FROM elements e JOIN digitalmedias dm ON e.id = dm.id WHERE dm.agerating = ?";

        return executeQueryWithSingleValue(query, ageRating);

    }

    @Override
    protected List<Element> executeQueryWithSingleValue(String query, Object value) throws SQLException {

        List<Element> elements = new ArrayList<>();

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return elements;

            }

            stmt.setObject(1, value);

            try (ResultSet rs = stmt.executeQuery()) {

                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    List<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    DigitalMedia media = new DigitalMedia(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getInt("releaseyear"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantityavailable"),
                            rs.getInt("length"),
                            genres,
                            rs.getString("producer"),
                            rs.getString("agerating"),
                            rs.getString("director")
                    );

                    elements.add(media);

                }

                return elements;

            }

        }

    }

    @Override
    public void addCustomFilters(StringBuilder query, List<Object> parameters, Map<String, Object> customFilters) {

        if (customFilters.isEmpty()) {
            return;

        }

        query.append("AND id IN (SELECT id FROM digitalmedias WHERE 1=1");

        if (customFilters.containsKey("producer")) {

            query.append(" AND producer = ?");
            parameters.add(customFilters.get("producer"));

        }

        if (customFilters.containsKey("director")) {

            query.append(" AND director = ?");
            parameters.add(customFilters.get("director"));

        }

        if (customFilters.containsKey("ageRating")) {

            query.append(" AND agerating = ?");
            parameters.add(customFilters.get("ageRating"));

        }

        query.append(")");

    }


}
