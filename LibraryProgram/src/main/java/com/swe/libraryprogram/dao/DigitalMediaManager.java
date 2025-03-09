package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.domainmodel.Book;
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


    public Boolean addDigitalMedia(DigitalMedia media) {

        Integer elementId = addElement(media);

        if (elementId == null) {

            System.out.println("Errore: l'inserimento dell'elemento base è fallito.");
            return false;

        }

        String insertMediaQuery = "INSERT INTO digital_media (id, producer, director, age_rating) VALUES (?, ?, ?, ?)";

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

        } catch (SQLException e) {

            System.err.println("Errore SQL durante l'inserimento del media: " + e.getMessage());
            e.printStackTrace();

        }

        return false;

    }

    public Boolean updateDigitalMedia(DigitalMedia media) {

        if (media.getId() == null || media.getId() <= 0) {

            System.out.println("Errore: l'ID del media è non valido.");
            return false;

        }

        if (!updateElement(media)) {

            System.out.println("Errore: l'aggiornamento dell'elemento base è fallito.");
            return false;

        }

        String updateMediaQuery = "UPDATE digital_media SET producer = ?, director = ?, age_rating = ? WHERE id = ?";

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

        } catch (SQLException e) {

            System.err.println("Errore SQL durante l'aggiornamento del media: " + e.getMessage());
            e.printStackTrace();

        }

        return false;

    }

    public Element getDigitalMedia(Integer id) {

        if (id == null || id <= 0) {

            System.err.println("ID non valido.");
            return null;

        }

        String getMediaQuery = "SELECT * FROM digital_media WHERE id = ?";

        List<Element> elements = executeQueryWithSingleValue(getMediaQuery, id);
        return elements.get(0);

    }

    public List<DigitalMedia> getAllDigitalMedias() {

        List<DigitalMedia> digitalMedias = new ArrayList<>();

        String query = "SELECT * FROM elements e JOIN digital_media dm ON e.id = dm.id";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return null;

            }

            try (ResultSet rs = stmt.executeQuery()) {

                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    LinkedList<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    DigitalMedia media = new DigitalMedia(
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

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero dei media digitali: " + e.getMessage());
            e.printStackTrace();

        }

        return digitalMedias;

    }

    public List<Element> getDigitalMediasByProducer(String producer) {

        if (producer == null || producer.isEmpty()) {

            System.err.println("Il produttore non può essere vuoto.");
            return null;

        }

        String query = "SELECT * FROM elements e JOIN digital_media dm ON e.id = dm.id WHERE dm.producer = ?";

        return executeQueryWithSingleValue(query, producer);

    }

    public List<Element> getDigitalMediasByDirector(String director) {

        if (director == null || director.isEmpty()) {

            System.err.println("Il regista non può essere vuoto.");
            return null;

        }

        String query = "SELECT * FROM elements e JOIN digital_media dm ON e.id = dm.id WHERE dm.director = ?";

        return executeQueryWithSingleValue(query, director);

    }

    public List<Element> getDigitalMediasByAgeRating(Integer ageRating) {

        if (ageRating == null || ageRating <= 0) {

            System.err.println("Il rating non può essere vuoto o negativo.");
            return null;

        }

        String query = "SELECT * FROM elements e JOIN digital_media dm ON e.id = dm.id WHERE dm.age_rating = ?";

        return executeQueryWithSingleValue(query, ageRating);

    }

    @Override
    public List<Element> executeQueryWithSingleValue(String query, Object value) {

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return null;

            }

            stmt.setObject(1, value);

            try (ResultSet rs = stmt.executeQuery()) {

                List<Element> elements = new ArrayList<>();
                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    LinkedList<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    DigitalMedia media = new DigitalMedia(
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

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero dei media digitali: " + e.getMessage());
            e.printStackTrace();

        }

        return null;

    }

}
