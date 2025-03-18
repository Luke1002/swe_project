package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.domainmodel.Genre;
import com.swe.libraryprogram.domainmodel.PeriodicPublication;
import com.swe.libraryprogram.domainmodel.Element;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.sql.*;
import java.util.Map;



public class PeriodicPublicationManager extends ElementManager {

    public PeriodicPublicationManager() {}


    public Boolean addPeriodicPublication(PeriodicPublication periodic) throws SQLException {

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
            periodicStmt.setString(6, periodic.getIssn());

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
            periodicStmt.setString(5, periodic.getIssn());
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

        String getPeriodicQuery = "SELECT * FROM periodicpublications WHERE id = ?";

        List<Element> elements = executeQueryWithSingleValue(getPeriodicQuery, id);
        return elements.getFirst();

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
                            rs.getString("issn")
                    );

                    periodics.add(periodic);

                }

                return periodics;

            }

        }

    }

    public List<Element> getPeriodicPublicationsByPublisher(String publisher) throws SQLException {

        String query = "SELECT * FROM elements e JOIN periodicpublications p ON e.id = p.id WHERE publisher = ?";

        return executeQueryWithSingleValue(query, publisher);

    }

    public List<Element> getPeriodicPublicationsByFrequency(Integer frequency) throws SQLException {

        String query = "SELECT * FROM elements e JOIN periodicpublications p ON e.id = p.id WHERE frequency = ?";

        return executeQueryWithSingleValue(query, frequency);

    }

    public List<Element> getPeriodicPublicationsByReleaseMonth(Integer releaseMonth) throws SQLException {

        String query = "SELECT * FROM elements e JOIN periodicpublications p ON e.id = p.id WHERE releasemonth = ?";

        return executeQueryWithSingleValue(query, releaseMonth);

    }

    public List<Element> getPeriodicPublicationsByReleaseDay(Integer releaseDay) throws SQLException {

        String query = "SELECT * FROM elements e JOIN periodicpublications p ON e.id = p.id WHERE releaseday = ?";

        return executeQueryWithSingleValue(query, releaseDay);

    }

    public List<Element> getPeriodicPublicationsByIssn(Integer issn) throws SQLException {

        String query = "SELECT * FROM elements e JOIN periodicpublications p ON e.id = p.id WHERE issn = ?";

        return executeQueryWithSingleValue(query, issn);

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
                            rs.getString("issn")
                    );

                    elements.add(periodic);

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

        query.append("AND id IN (SELECT id FROM periodicpublications WHERE 1=1");

        if (customFilters.containsKey("publisher")) {

            query.append(" AND publisher = ?");
            parameters.add(customFilters.get("publisher"));

        }

        if (customFilters.containsKey("frequency")) {

            query.append(" AND frequency = ?");
            parameters.add(customFilters.get("frequency"));

        }

        if (customFilters.containsKey("releaseMonth")) {

            query.append(" AND releasemonth = ?");
            parameters.add(customFilters.get("releaseMonth"));

        }

        if (customFilters.containsKey("releaseDay")) {

            query.append(" AND releaseday = ?");
            parameters.add(customFilters.get("releaseDay"));

        }

        if (customFilters.containsKey("issn")) {

            query.append(" AND issn = ?");
            parameters.add(customFilters.get("issn"));

        }

        query.append(")");

    }


}
