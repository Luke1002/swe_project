package com.swe.libraryprogram.orm;

import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.Genre;
import com.swe.libraryprogram.domainmodel.PeriodicPublication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class PeriodicPublicationDAO extends ElementDAO {

    public PeriodicPublicationDAO() {
    }


    public Integer addPeriodicPublication(PeriodicPublication periodicPublication) {
        try {
            String query = "WITH inserted_element AS (" +
                    "    INSERT INTO elements (title, releaseyear, description, quantity, quantityavailable, length)" +
                    "    VALUES (?, ?, ?, ?, ?, ?)" +
                    "    RETURNING id)" +
                    "INSERT INTO periodicpublications (id, publisher, frequency, releasemonth, releaseday) " +
                    "SELECT id, ?, ?, ?, ? FROM inserted_element RETURNING id;";

            Connection connection = ConnectionManager.getInstance().getConnection();
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, periodicPublication.getTitle());
            if (periodicPublication.getReleaseYear() != null) {
                stmt.setInt(2, periodicPublication.getReleaseYear());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.setString(3, periodicPublication.getDescription());
            stmt.setInt(4, periodicPublication.getQuantity());
            stmt.setInt(5, periodicPublication.getQuantityAvailable());
            if (periodicPublication.getLength() != null) {
                stmt.setInt(6, periodicPublication.getLength());
            } else {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }
            stmt.setString(7, periodicPublication.getPublisher());
            stmt.setString(8, periodicPublication.getFrequency());
            if (periodicPublication.getReleaseMonth() != null) {
                stmt.setInt(9, periodicPublication.getReleaseMonth());
            } else {
                stmt.setNull(9, java.sql.Types.INTEGER);
            }
            if (periodicPublication.getReleaseDay() != null) {
                stmt.setInt(10, periodicPublication.getReleaseDay());
            } else {
                stmt.setNull(10, java.sql.Types.INTEGER);
            }

            ResultSet rs = stmt.executeQuery();
            Integer result = null;
            if (rs.next()) {
                result = rs.getInt(1);
            } else {
                System.err.println("Errore: il periodico non è stato inserito.");
            }
            stmt.close();
            return result;
        } catch (SQLException e) {
            return null;
        }
    }

    public Boolean updatePeriodicPublication(PeriodicPublication periodicPublication) throws SQLException {

        String updatePeriodicQuery = "WITH element_id AS(UPDATE elements SET title = ?, releaseyear = ?, description = ?," +
                " quantity = ?, quantityavailable = ?, length = ? WHERE id = ? RETURNING id) UPDATE periodicpublications SET publisher = ?, frequency = ?, releasemonth = ?, releaseday = ? WHERE id = (SELECT id FROM element_id)";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(updatePeriodicQuery);

        stmt.setString(1, periodicPublication.getTitle());
        if (periodicPublication.getReleaseYear() != null) {
            stmt.setInt(2, periodicPublication.getReleaseYear());
        } else {
            stmt.setNull(2, java.sql.Types.INTEGER);
        }
        stmt.setString(3, periodicPublication.getDescription());
        stmt.setInt(4, periodicPublication.getQuantity());
        stmt.setInt(5, periodicPublication.getQuantityAvailable());
        if (periodicPublication.getLength() != null) {
            stmt.setInt(6, periodicPublication.getLength());
        } else {
            stmt.setNull(6, java.sql.Types.INTEGER);
        }
        stmt.setInt(7, periodicPublication.getId());
        stmt.setString(8, periodicPublication.getPublisher());
        stmt.setString(9, periodicPublication.getFrequency());
        if (periodicPublication.getLength() != null) {
            stmt.setInt(10, periodicPublication.getReleaseMonth());
        } else {
            stmt.setNull(10, java.sql.Types.INTEGER);
        }
        if (periodicPublication.getLength() != null) {
            stmt.setInt(11, periodicPublication.getReleaseDay());
        } else {
            stmt.setNull(11, java.sql.Types.INTEGER);
        }

        int rowsUpdated = stmt.executeUpdate();
        stmt.close();
        if (rowsUpdated > 0) {
            return true;
        } else {
            System.err.println("Errore: il periodico non è stata aggiornato.");
            return false;
        }
    }

    public Element getPeriodicPublication(Integer id) throws SQLException {

        String getPeriodicQuery = "SELECT * FROM elements e JOIN periodicpublications p ON e.id = p.id WHERE e.id = ?";

        List<Element> elements = executeQueryWithSingleValue(getPeriodicQuery, id);
        return elements.getFirst();

    }

    public List<PeriodicPublication> getAllPeriodicPublications() throws SQLException {

        List<PeriodicPublication> periodics = new ArrayList<>();

        String query = "SELECT * FROM elements e JOIN periodicpublications p ON e.id = p.id";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            Integer releaseYear = rs.getInt("releaseyear");
            if (rs.wasNull()) {
                releaseYear = null;
            }
            Integer length = rs.getInt("length");
            if (rs.wasNull()) {
                length = null;
            }
            Integer releaseMonth = rs.getInt("releasemonth");
            if (rs.wasNull()) {
                releaseMonth = null;
            }
            Integer releaseDay = rs.getInt("releaseday");
            if (rs.wasNull()) {
                releaseDay = null;
            }
            PeriodicPublication periodic = new PeriodicPublication(
                    rs.getInt("id"),
                    rs.getString("title"),
                    releaseYear,
                    rs.getString("description"),
                    rs.getInt("quantity"),
                    rs.getInt("quantityavailable"),
                    length,
                    new ArrayList<>(),
                    rs.getString("publisher"),
                    rs.getString("frequency"),
                    releaseMonth,
                    releaseDay
            );

            periodics.add(periodic);
        }

        stmt.close();

        GenreDAO genreDAO = new GenreDAO();
        for (Element element : periodics) {
            List<Genre> genres = genreDAO.getGenresForElement(element.getId());
            element.setGenres(genres);
        }
        return periodics;

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

    @Override
    protected List<Element> executeQueryWithSingleValue(String query, Object value) throws SQLException {

        List<Element> elements = new ArrayList<>();

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);

        stmt.setObject(1, value);

        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            Integer releaseYear = rs.getInt("releaseyear");
            if (rs.wasNull()) {
                releaseYear = null;
            }
            Integer length = rs.getInt("length");
            if (rs.wasNull()) {
                length = null;
            }
            Integer releaseMonth = rs.getInt("releasemonth");
            if (rs.wasNull()) {
                releaseMonth = null;
            }
            Integer releaseDay = rs.getInt("releaseday");
            if (rs.wasNull()) {
                releaseDay = null;
            }
            PeriodicPublication periodic = new PeriodicPublication(
                    rs.getInt("id"),
                    rs.getString("title"),
                    releaseYear,
                    rs.getString("description"),
                    rs.getInt("quantity"),
                    rs.getInt("quantityavailable"),
                    length,
                    new ArrayList<>(),
                    rs.getString("publisher"),
                    rs.getString("frequency"),
                    releaseMonth,
                    releaseDay
            );

            elements.add(periodic);

        }
        stmt.close();

        GenreDAO genreDAO = new GenreDAO();
        for (Element element : elements) {
            List<Genre> genres = genreDAO.getGenresForElement(element.getId());
            element.setGenres(genres);
        }
        return elements;

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

        query.append(")");

    }


}
