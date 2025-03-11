package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.domainmodel.Genre;
import com.swe.libraryprogram.domainmodel.PeriodicPublication;
import com.swe.libraryprogram.domainmodel.Element;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.sql.*;



public class PeriodicPublicationManager extends ElementManager {

    public PeriodicPublicationManager() {}


    public Boolean addPeriodicPublication(PeriodicPublication periodic) throws SQLException {

        if (periodic.getId() == null || periodic.getId() <= 0 ||
                periodic.getPublisher() == null || periodic.getPublisher().isEmpty() ||
                periodic.getFrequency() <= 0 ||
                periodic.getReleaseMonth() <= 0 || periodic.getReleaseMonth() > 12 ||
                periodic.getReleaseDay() <= 0 || periodic.getReleaseDay() > 31 ||
                periodic.getIssn() == null || periodic.getIssn() <= 0) {

            System.out.println("Informazioni del periodico non valide.");
            return false;

        }

        Integer elementId = addElement(periodic);

        if (elementId == null) {

            System.out.println("Errore: l'inserimento delle informazioni base del periodico è fallito.");
            return false;

        }

        String insertPeriodicQuery = "INSERT INTO periodicpublications (id, publisher, frequency, releasemonth, releaseday, issn) VALUES (?, ?, ?, ?, ?, ?)";

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

                System.err.println("Errore: il periodico non è stata inserito.");
                return false;

            }

        }

    }

    public Boolean updatePeriodicPublication(PeriodicPublication periodic) throws SQLException {

        if (periodic.getId() == null || periodic.getId() <= 0 ||
                periodic.getPublisher() == null || periodic.getPublisher().isEmpty() ||
                periodic.getFrequency() <= 0 ||
                periodic.getReleaseMonth() <= 0 || periodic.getReleaseMonth() > 12 ||
                periodic.getReleaseDay() <= 0 || periodic.getReleaseDay() > 31 ||
                periodic.getIssn() == null || periodic.getIssn() <= 0) {

            System.out.println("Informazioni del periodico non valide.");
            return false;

        }

        if (!updateElement(periodic)) {

            System.out.println("Errore: l'aggiornamento delle informazioni base del periodico è fallito.");
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

        }

    }

    public Element getPeriodicPublication(Integer id) throws SQLException {

        if (id == null || id <= 0) {

            System.err.println("ID non valido.");
            return null;

        }

        String getPeriodicQuery = "SELECT * FROM periodicpublications WHERE id = ?";

        List<Element> elements = executeQueryWithSingleValue(getPeriodicQuery, id);
        return elements.get(0);

    }

    public List<PeriodicPublication> getAllPeriodicPublications() throws SQLException {

        List<PeriodicPublication> periodics = new ArrayList<>();

        String query = "SELECT * FROM elements e JOIN periodicpublications p ON e.id = p.id";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return periodics;

            }

            try (ResultSet rs = stmt.executeQuery()) {

                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    LinkedList<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    PeriodicPublication periodic = new PeriodicPublication(
                            rs.getInt("id"),
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

                return periodics;

            }

        }

    }

    public List<Element> getPeriodicPublicationsByPublisher(String publisher) throws SQLException {

        List<Element> elements = new ArrayList<>();

        if (publisher == null || publisher.isEmpty()) {

            System.err.println("Editore non valido.");
            return elements;

        }

        String query = "SELECT * FROM elements e JOIN periodicpublications p ON e.id = p.id WHERE publisher = ?";

        elements = executeQueryWithSingleValue(query, publisher);
        return elements;

    }

    public List<Element> getPeriodicPublicationsByFrequency(Integer frequency) throws SQLException {

        List<Element> elements = new ArrayList<>();

        if (frequency == null || frequency <= 0) {

            System.err.println("Frequenza di pubblicazione non valida.");
            return elements;

        }

        String query = "SELECT * FROM elements e JOIN periodicpublications p ON e.id = p.id WHERE frequency = ?";

        elements = executeQueryWithSingleValue(query, frequency);
        return elements;

    }

    public List<Element> getPeriodicPublicationsByReleaseMonth(Integer releaseMonth) throws SQLException {

        List<Element> elements = new ArrayList<>();

        if (releaseMonth == null || releaseMonth <= 0 || releaseMonth > 12) {

            System.err.println("Mese di rilascio non valido.");
            return elements;

        }

        String query = "SELECT * FROM elements e JOIN periodicpublications p ON e.id = p.id WHERE releasemonth = ?";

        elements = executeQueryWithSingleValue(query, releaseMonth);
        return elements;

    }

    public List<Element> getPeriodicPublicationsByReleaseDay(Integer releaseDay) throws SQLException {

        List<Element> elements = new ArrayList<>();

        if (releaseDay == null || releaseDay <= 0 || releaseDay > 31) {

            System.err.println("Giorno di rilascio non valido.");
            return elements;

        }

        String query = "SELECT * FROM elements e JOIN periodicpublications p ON e.id = p.id WHERE releaseday = ?";

        elements = executeQueryWithSingleValue(query, releaseDay);
        return elements;

    }

    public List<Element> getPeriodicPublicationsByIssn(Integer issn) throws SQLException {

        List<Element> elements = new ArrayList<>();

        if (issn == null || issn <= 0) {

            System.err.println("ISSN non valido.");
            return elements;

        }

        String query = "SELECT * FROM elements e JOIN periodicpublications p ON e.id = p.id WHERE issn = ?";

        elements = executeQueryWithSingleValue(query, issn);
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

                    PeriodicPublication periodic = new PeriodicPublication(
                            rs.getInt("id"),
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

        }

    }


}
