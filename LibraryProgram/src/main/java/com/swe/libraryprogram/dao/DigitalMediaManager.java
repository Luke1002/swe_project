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



public class DigitalMediaManager extends ElementManager {

    public DigitalMediaManager() {}


    public Boolean addDigitalMedia(DigitalMedia media) throws SQLException {

        if (media.getId() == null || media.getId() <=0 ||
                media.getProducer() == null || media.getProducer().isEmpty() ||
                media.getDirector() == null || media.getDirector().isEmpty()) {

            System.out.println("Informazioni del media non valide.");
            return false;

        }

        Integer elementId = addElement(media);

        if (elementId == null) {

            System.out.println("Errore: l'inserimento delle informazioni di base del media è fallito.");
            return false;

        }

        String insertMediaQuery = "INSERT INTO digitalmedias (id, producer, director, agerating) VALUES (?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement mediaStmt = connection.prepareStatement(insertMediaQuery)) {

            mediaStmt.setInt(1, elementId);
            mediaStmt.setString(2, media.getProducer());
            mediaStmt.setString(3, media.getDirector());
            mediaStmt.setInt(4, media.getAgeRating());

            int rowsInserted = mediaStmt.executeUpdate();

            if (rowsInserted > 0) {
                return true;

            } else {

                System.err.println("Errore: il media non è stato inserito.");
                return false;

            }

        }

    }

    public Boolean updateDigitalMedia(DigitalMedia media) throws SQLException {

        if (media.getId() == null || media.getId() <= 0 ||
                media.getProducer() == null || media.getProducer().isEmpty() ||
                media.getDirector() == null || media.getDirector().isEmpty()) {

            System.out.println("Informazioni del media non valide.");
            return false;

        }

        if (!updateElement(media)) {

            System.out.println("Errore: l'aggiornamento delle informazioni base del media è fallito.");
            return false;

        }

        String updateMediaQuery = "UPDATE digitalmedias SET producer = ?, director = ?, agerating = ? WHERE id = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement mediaStmt = connection.prepareStatement(updateMediaQuery)) {

            mediaStmt.setString(1, media.getProducer());
            mediaStmt.setString(2, media.getDirector());
            mediaStmt.setInt(3, media.getAgeRating());
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

        if (id == null || id <= 0) {

            System.err.println("ID del media non valido.");
            return null;

        }

        String getMediaQuery = "SELECT * FROM digitalmedias WHERE id = ?";

        List<Element> elements = executeQueryWithSingleValue(getMediaQuery, id);
        return elements.get(0);

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

                    LinkedList<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

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
                            rs.getInt("age_rating"),
                            rs.getString("director")
                    );

                    digitalMedias.add(media);

                }

                return digitalMedias;

            }

        }

    }

    public List<Element> getDigitalMediasByProducer(String producer) throws SQLException {

        List<Element> elements = new ArrayList<>();

        if (producer == null || producer.isEmpty()) {

            System.err.println("Produttore non valido.");
            return elements;

        }

        String query = "SELECT * FROM elements e JOIN digitalmedias dm ON e.id = dm.id WHERE dm.producer = ?";

        elements = executeQueryWithSingleValue(query, producer);
        return elements;

    }

    public List<Element> getDigitalMediasByDirector(String director) throws SQLException {

        List<Element> elements = new ArrayList<>();

        if (director == null || director.isEmpty()) {

            System.err.println("Regista non valido.");
            return elements;

        }

        String query = "SELECT * FROM elements e JOIN digitalmedias dm ON e.id = dm.id WHERE dm.director = ?";

        elements = executeQueryWithSingleValue(query, director);
        return elements;

    }

    public List<Element> getDigitalMediasByAgeRating(Integer ageRating) throws SQLException {

        List<Element> elements = new ArrayList<>();

        if (ageRating == null || ageRating <= 0) {

            System.err.println("Classificazione per età non valida.");
            return elements;

        }

        String query = "SELECT * FROM elements e JOIN digitalmedias dm ON e.id = dm.id WHERE dm.agerating = ?";

        elements = executeQueryWithSingleValue(query, ageRating);
        return elements;

    }

    @Override
    public List<Element> executeQueryWithSingleValue(String query, Object value) throws SQLException {

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

                    LinkedList<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

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
                            rs.getInt("agerating"),
                            rs.getString("director")
                    );

                    elements.add(media);

                }

                return elements;

            }

        }

    }


}
