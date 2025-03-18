package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.domainmodel.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.LinkedList;


public class GenreManager {

    public GenreManager() {
    }


    public Boolean addGenre(Genre genre) throws SQLException {

        String query = "INSERT INTO genres (code, name) VALUES (?, ?)";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setInt(1, genre.getCode());
            stmt.setString(2, genre.getName());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        }

    }

    public Boolean removeGenre(Integer code) throws SQLException {

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

        }

    }

    public Genre getGenreByCode(Integer code) throws SQLException {

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
                            rs.getInt("code")
                    );

                } else {

                    System.err.println("Genere non trovato con codice: " + code);
                    return null;

                }
            }

        }

    }

    public Genre getGenreByName(String name) throws SQLException {

        String query = "SELECT * FROM genres WHERE name = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return null;

            }

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Genre(
                        rs.getString("name"),
                        rs.getInt("code")
                );
            } else {
                System.err.println("Genere non trovato con codice: " + name);
                return null;
            }
        }

    }

    public LinkedList<Genre> getAllGenres() throws SQLException {

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
                        rs.getInt("code")
                ));

            }

            return genres;

        }

    }

    public Boolean associateGenreWithElement(Integer elementId, Integer genreCode) throws SQLException {

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

        }

    }

    public LinkedList<Genre> getGenresForElement(Integer elementId) throws SQLException {

        LinkedList<Genre> genres = new LinkedList<>();

        String query = "SELECT g.code, g.name " +
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
                            rs.getInt("code")
                    ));

                }

            }

            return genres;

        }

    }

    public Boolean removeGenreFromElement(Integer elementId, Integer genreCode) throws SQLException {

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

        }

    }


}
