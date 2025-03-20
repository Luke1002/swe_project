package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.controller.MainController;
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
import java.util.Map;


public class DigitalMediaManager extends ElementManager {

    public DigitalMediaManager() {
    }


    public Integer addDigitalMedia(DigitalMedia media) throws SQLException {


        String query = "WITH inserted_element AS (" +
                "    INSERT INTO elements (title, releaseyear, description, quantity, quantityavailable, length)" +
                "    VALUES (?, ?, ?, ?, ?, ?)    RETURNING id)" +
                "INSERT INTO digitalmedias (id, producer, director, agerating) " +
                "SELECT id, ?, ?, ? FROM inserted_element " + "RETURNING id";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);

        stmt.setString(1, media.getTitle());
        if (media.getReleaseYear() != null) {
            stmt.setInt(2, media.getReleaseYear());
        } else {
            stmt.setNull(2, java.sql.Types.INTEGER);
        }
        stmt.setString(3, media.getDescription());
        stmt.setInt(4, media.getQuantity());
        stmt.setInt(5, media.getQuantityAvailable());
        if (media.getLength() != null) {
            stmt.setInt(6, media.getLength());
        } else {
            stmt.setNull(6, java.sql.Types.INTEGER);
        }
        stmt.setString(7, media.getProducer());
        stmt.setString(8, media.getDirector());
        stmt.setString(9, media.getAgeRating());

        ResultSet rs = stmt.executeQuery();
        Integer result = null;
        if (rs.next()) {
            result = rs.getInt(1);
        } else {
            System.err.println("Errore: il digital media non è stato inserito.");
        }

        stmt.close();
        return result;

    }

    public Boolean updateDigitalMedia(DigitalMedia media) throws SQLException {

        String updateMediaQuery = "WITH element_id AS(UPDATE elements SET title = ?, releaseyear = ?, description = ?," +
                " quantity = ?, quantityavailable = ?, length = ? WHERE id = ? RETURNING id) UPDATE digitalmedias SET producer = ?, director = ?, agerating = ? WHERE id = (SELECT id FROM element_id)";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(updateMediaQuery);

        stmt.setString(1, media.getTitle());
        if (media.getReleaseYear() != null) {
            stmt.setInt(2, media.getReleaseYear());
        } else {
            stmt.setNull(2, java.sql.Types.INTEGER);
        }
        stmt.setString(3, media.getDescription());
        stmt.setInt(4, media.getQuantity());
        stmt.setInt(5, media.getQuantityAvailable());
        if (media.getLength() != null) {
            stmt.setInt(6, media.getLength());
        } else {
            stmt.setNull(6, java.sql.Types.INTEGER);
        }
        stmt.setInt(7, media.getId());
        stmt.setString(8, media.getProducer());
        stmt.setString(9, media.getDirector());
        stmt.setString(10, media.getAgeRating());

        int rowsUpdated = stmt.executeUpdate();
        stmt.close();
        if (rowsUpdated > 0) {
            return true;

        } else {

            System.err.println("Errore: il digital media non è stato aggiornato.");
            return false;

        }

    }

    public Element getDigitalMedia(Integer id) throws SQLException {

        String getMediaQuery = "SELECT * FROM elements e JOIN digitalmedias d ON e.id = d.id WHERE e.id = ?";

        List<Element> elements = executeQueryWithSingleValue(getMediaQuery, id);
        return elements.getFirst();

    }

    public List<DigitalMedia> getAllDigitalMedias() throws SQLException {

        List<DigitalMedia> digitalMedias = new ArrayList<>();

        String query = "SELECT * FROM elements e JOIN digitalmedias dm ON e.id = dm.id";
        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            Integer releaseYear = rs.getInt("release_year");
            if (rs.wasNull()) {
                releaseYear = null;
            }
            Integer length = rs.getInt("length");
            if (rs.wasNull()) {
                length = null;
            }

            DigitalMedia media = new DigitalMedia(
                    rs.getInt("id"),
                    rs.getString("title"),
                    releaseYear,
                    rs.getString("description"),
                    rs.getInt("quantity"),
                    rs.getInt("quantity_available"),
                    length,
                    new ArrayList<>(),
                    rs.getString("producer"),
                    rs.getString("age_rating"),
                    rs.getString("director")
            );

            digitalMedias.add(media);

        }
        stmt.close();

        GenreManager genreManager = MainController.getInstance().getGenreManager();
        for (Element element : digitalMedias){
            List<Genre> genres = genreManager.getGenresForElement(element.getId());
            element.setGenres(genres);
        }
        return digitalMedias;

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

            DigitalMedia media = new DigitalMedia(
                    rs.getInt("id"),
                    rs.getString("title"),
                    releaseYear,
                    rs.getString("description"),
                    rs.getInt("quantity"),
                    rs.getInt("quantityavailable"),
                    length,
                    new ArrayList<>(),
                    rs.getString("producer"),
                    rs.getString("agerating"),
                    rs.getString("director")
            );

            elements.add(media);

        }
        stmt.close();

        GenreManager genreManager = MainController.getInstance().getGenreManager();
        for (Element element : elements){
            List<Genre> genres = genreManager.getGenresForElement(element.getId());
            element.setGenres(genres);
        }
        return elements;
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
