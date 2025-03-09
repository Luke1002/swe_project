package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.domainmodel.Book;
import com.swe.libraryprogram.domainmodel.Genre;
import com.swe.libraryprogram.domainmodel.PeriodicPublication;
import com.swe.libraryprogram.domainmodel.Element;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.sql.*;



public class PeriodicPublicationManager extends ElementManager {

    public PeriodicPublicationManager() {}


    public Boolean addPeriodicPublication(PeriodicPublication periodic) {

        Integer elementId = addElement(periodic);

        if (elementId == null) {

            System.out.println("Errore: l'inserimento dell'elemento base è fallito.");
            return false;

        }

        String insertPeriodicQuery = "INSERT INTO periodic_publications (id, publisher, frequency, release_month, release_day, issn) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement periodicStmt = connection.prepareStatement(insertPeriodicQuery)) {

            periodicStmt.setInt(1, elementId);
            periodicStmt.setString(2, periodic.getPublisher());
            periodicStmt.setInt(3, periodic.getFrequency());
            periodicStmt.setInt(4, periodic.getReleaseMonth());
            periodicStmt.setInt(5, periodic.getReleaseDay());
            periodicStmt.setInt(6, periodic.getIssn());

            int rowsInserted = periodicStmt.executeUpdate();

            if (rowsInserted > 0) {

                return true;

            } else {

                System.err.println("Errore: la pubblicazione periodica non è stata inserita.");
                return false;

            }

        } catch (SQLException e) {

            System.err.println("Errore SQL durante l'inserimento della pubblicazione periodica: " + e.getMessage());
            e.printStackTrace();

        }

        return false;

    }

    public Boolean updatePeriodicPublication(PeriodicPublication periodic) {

        if (periodic == null) {

            System.out.println("Periodico non valido.");
            return false;

        }

        if (periodic.getId() == null || periodic.getId() <= 0) {

            System.out.println("ID non valido.");
            return false;

        }

        if (!updateElement(periodic)) {

            System.out.println("Errore: l'aggiornamento dell'elemento base è fallito.");
            return false;

        }

        String updatePeriodicQuery = "UPDATE periodicpublications SET publisher = ?, frequency = ?, releasemonth = ?, releaseday = ?, issn = ? WHERE id = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement periodicStmt = connection.prepareStatement(updatePeriodicQuery)) {

            periodicStmt.setString(1, periodic.getPublisher());
            periodicStmt.setInt(2, periodic.getFrequency());
            periodicStmt.setInt(3, periodic.getReleaseMonth());
            periodicStmt.setInt(4, periodic.getReleaseDay());
            periodicStmt.setInt(5, periodic.getIssn());
            periodicStmt.setInt(6, periodic.getId());

            int rowsUpdated = periodicStmt.executeUpdate();

            if (rowsUpdated > 0) {

                return true;

            } else {

                System.err.println("Errore: il periodico non è stata aggiornato.");
                return false;

            }

        } catch (SQLException e) {

            System.err.println("Errore SQL durante l'aggiornamento del periodico: " + e.getMessage());
            e.printStackTrace();

        }

        return false;

    }

    public Element getPeriodicPublication(Integer id) {

        if (id == null || id <= 0) {

            System.err.println("ID non valido.");
            return null;

        }

        String getPeriodicQuery = "SELECT * FROM periodic_publications WHERE id = ?";

        List<Element> elements = executeQueryWithSingleValue(getPeriodicQuery, id);
        return elements.get(0);

    }

    public List<PeriodicPublication> getAllPeriodicPublications() {

        List<PeriodicPublication> periodics = new ArrayList<>();

        String query = "SELECT * FROM elements e JOIN periodicpublications p ON e.id = p.id";

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

                    PeriodicPublication periodic = new PeriodicPublication(
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres,
                            rs.getString("publisher"),
                            rs.getInt("frequency"),
                            rs.getInt("release_month"),
                            rs.getInt("release_day"),
                            rs.getInt("issn")
                    );

                    periodics.add(periodic);

                }

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero dei periodici: " + e.getMessage());
            e.printStackTrace();

        }

        return periodics;

    }

    public List<PeriodicPublication> getPeriodicPublicationsByPublisher(String publisher) {
        // TODO implement here
        return null;
    }

    public List<PeriodicPublication> getPeriodicPublicationsByFrequency(Integer frequency) {
        // TODO implement here
        return null;
    }

    public List<PeriodicPublication> getPeriodicPublicationsByReleaseMonth(Integer releaseMonth) {
        // TODO implement here
        return null;
    }

    public List<PeriodicPublication> getPeriodicPublicationsByReleaseDay(Integer releaseDay) {
        // TODO implement here
        return null;
    }

    public List<PeriodicPublication> getPeriodicPublicationsByIssn(Integer issn) {
        // TODO implement here
        return null;
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

                    PeriodicPublication periodic = new PeriodicPublication(
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres,
                            rs.getString("publisher"),
                            rs.getInt("frequency"),
                            rs.getInt("release_month"),
                            rs.getInt("release_day"),
                            rs.getInt("issn")
                    );

                    elements.add(periodic);

                }

                return elements;

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero dei periodici: " + e.getMessage());
            e.printStackTrace();

        }

        return null;

    }

}
