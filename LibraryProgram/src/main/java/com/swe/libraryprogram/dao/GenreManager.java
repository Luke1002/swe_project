package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.domainmodel.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.LinkedList;


public class GenreManager {

    public GenreManager() {}


    public Boolean addGenre(Genre genre) {

        if (genre == null) {

            System.err.println("Il genere è nullo.");
            return false;

        }

        String query = "INSERT INTO genres (code, name, description) VALUES (?, ?, ?)";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setInt(1, genre.getCode());
            stmt.setString(2, genre.getName());
            stmt.setString(3, genre.getDescription());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {

            System.err.println("Errore SQL durante l'inserimento del genere: " + e.getMessage());
            return false;

        }

    }

    public Boolean removeGenre(Integer code) {

        String query = "DELETE FROM genres WHERE code = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setInt(1, code);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {

            System.err.println("Errore SQL durante l'eliminazione del genere: " + e.getMessage());
            return false;

        }

    }

    public Genre getGenreByCode(Integer code) {

        if (code == null || code <= 0) {

            System.err.println("Codice genere non valido.");
            return null;

        }

        String query = "SELECT * FROM genres WHERE code = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return null;

            }

            stmt.setInt(1, code);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    return new Genre(
                            rs.getString("name"),
                            rs.getInt("code"),
                            rs.getString("description")
                    );

                } else {

                    System.err.println("Genere non trovato con codice: " + code);
                    return null;

                }
            }

        } catch (SQLException e) {

            System.err.println("Errore SQL durante il recupero del genere: " + e.getMessage());
            return null;

        }

    }

    public LinkedList<Genre> getAllGenres() {

        LinkedList<Genre> genres = new LinkedList<>();

        String query = "SELECT * FROM genres";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return genres;

            }

            while (rs.next()) {

                genres.add(new Genre(
                        rs.getString("name"),
                        rs.getInt("code"),
                        rs.getString("description")
                ));

            }

            return genres;

        } catch (SQLException e) {

            System.err.println("Errore SQL durante il recupero di tutti i generi: " + e.getMessage());
            return genres;

        }

    }

    public Boolean associateGenreWithElement(Integer elementId, Integer genreCode) {

        if (elementId == null || genreCode == null || elementId <= 0 || genreCode <= 0) {

            System.err.println("ID elemento o codice genere non valido.");
            return false;

        }

        String query = "INSERT INTO elementgenres (elementid, genrecode) VALUES (?, ?)";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setInt(1, elementId);
            stmt.setInt(2, genreCode);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {

            System.err.println("Errore nell'associare il genere all'elemento: " + e.getMessage());
            return false;

        }

    }

    public LinkedList<Genre> getGenresForElement(Integer elementId) {

        LinkedList<Genre> genres = new LinkedList<>();

        String query = "SELECT g.code, g.name, g.description " +
                "FROM genres g " +
                "JOIN elementgenres eg ON g.code = eg.genrecode " +
                "WHERE eg.elementid = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return genres;

            }

            stmt.setInt(1, elementId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    genres.add(new Genre(
                            rs.getString("name"),
                            rs.getInt("code"),
                            rs.getString("description")
                    ));

                }

            }

        } catch (SQLException e) {

            System.err.println("Errore SQL nel recupero dei generi per l'elemento: " + e.getMessage());
            e.printStackTrace();
            return genres;

        }

        return genres;

    }

    public Boolean removeGenreFromElement(Integer elementId, Integer genreCode) {

        if (elementId == null || genreCode == null || elementId <= 0 || genreCode <= 0) {

            System.err.println("ID elemento o codice genere non valido.");
            return false;

        }

        String query = "DELETE FROM elementgenres WHERE elementid = ? AND genrecode = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setInt(1, elementId);
            stmt.setInt(2, genreCode);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {

            System.err.println("Errore nell'eliminare il genere dall'elemento: " + e.getMessage());
            e.printStackTrace();
            return false;

        }

    }

}
