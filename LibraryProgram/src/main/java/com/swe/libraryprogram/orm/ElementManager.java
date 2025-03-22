package com.swe.libraryprogram.orm;

import com.swe.libraryprogram.services.MainController;
import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;


public class ElementManager {

    public ElementManager() {
    }

    public Boolean removeElement(Integer id) throws SQLException {

        String query = "DELETE FROM elements WHERE id = ?";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);

        if (!ConnectionManager.getInstance().isConnectionValid()) {

            System.err.println("Connessione al database non valida.");
            return false;

        }

        stmt.setInt(1, id);

        int rowsDeleted = stmt.executeUpdate();

        if (rowsDeleted > 0) {

            System.out.println("Elemento con ID " + id + " rimosso correttamente.");
            return true;

        } else {

            System.err.println("Nessun elemento trovato con ID " + id);
            return false;

        }


    }

    public Integer getElementTypeById(Integer id) throws SQLException {
        String query = "SELECT " +
                "       CASE" +
                "           WHEN EXISTS (SELECT 1 FROM books b WHERE b.id = e.id) THEN 1" +
                "           WHEN EXISTS (SELECT 1 FROM digitalmedias d WHERE d.id = e.id) THEN 2" +
                "           WHEN EXISTS (SELECT 1 FROM periodicpublications p WHERE p.id = e.id) THEN 3" +
                "           ELSE 0" +
                "           END AS type " +
                "FROM elements e WHERE e.id = ?;";
        Connection conn = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        Integer type = null;
        if (rs.next()) {
            type = rs.getInt("type");
        }
        stmt.close();
        return type;

    }

    public Boolean updateElement(Element element) throws SQLException {

        String query = "UPDATE elements SET title = ?, releaseyear = ?, description = ?, quantity = ?, quantityavailable = ?, length = ? WHERE id = ?";

        Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setString(1, element.getTitle());
            stmt.setInt(2, element.getReleaseYear());
            stmt.setString(3, element.getDescription());
            stmt.setInt(4, element.getQuantity());
            stmt.setInt(5, element.getQuantityAvailable());
            if(element.getLength() == null) {
                stmt.setNull(6, java.sql.Types.INTEGER);
            }
            else{
                stmt.setInt(6, element.getLength());
            }
            stmt.setInt(7, element.getId());

            int rowsUpdated = stmt.executeUpdate();
            stmt.close();
            if (rowsUpdated > 0) {
                return true;

            } else {

                System.err.println("Errore: informazioni base non aggiornate.");
                return false;

            }

    }

    public Element getElement(Integer id) throws SQLException {

        String query = "SELECT * FROM elements WHERE id = ?";

        List<Element> elements = executeQueryWithSingleValue(query, id);
        return elements.getFirst();

    }

    public List<Element> getAllElements() throws SQLException {

        List<Element> elements = new ArrayList<>();

        String query = "SELECT * FROM elements";

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

            Element element = new Element(
                    rs.getInt("id"),
                    rs.getString("title"),
                    releaseYear,
                    rs.getString("description"),
                    rs.getInt("quantity"),
                    rs.getInt("quantityavailable"),
                    length,
                    new ArrayList<>()
            );

            elements.add(element);

        }
        stmt.close();

        GenreManager genreManager = MainController.getInstance().getGenreManager();
        for (Element element : elements) {
            List<Genre> genres = genreManager.getGenresForElement(element.getId());
            element.setGenres(genres);
        }

        return elements;

    }

    public List<Element> getElementsByGenre(String genreName) throws SQLException {

        String query = "SELECT e.* FROM elements e " +
                "JOIN elementgenres eg ON e.id = eg.elementid " +
                "JOIN genres g ON eg.genrecode = g.genrecode " +
                "WHERE g.name = ?";

        return executeQueryWithSingleValue(query, genreName);

    }

    public List<Element> getElementsByTitle(String title) throws SQLException {

        String query = "SELECT * FROM elements WHERE title = ?";

        return executeQueryWithSingleValue(query, title);

    }

    public List<Element> getElementsByReleaseYear(Integer releaseYear) throws SQLException {

        String query = "SELECT * FROM elements WHERE releaseyear = ?";

        return executeQueryWithSingleValue(query, releaseYear);

    }

    public List<Element> getElementsByLength(Integer length) throws SQLException {

        String query = "SELECT * FROM elements WHERE length = ?";

        return executeQueryWithSingleValue(query, length);

    }

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

            Element element = new Element(
                    rs.getInt("id"),
                    rs.getString("title"),
                    releaseYear,
                    rs.getString("description"),
                    rs.getInt("quantity"),
                    rs.getInt("quantityavailable"),
                    length,
                    new ArrayList<>()
            );

            elements.add(element);

        }
        stmt.close();

        GenreManager genreManager = MainController.getInstance().getGenreManager();
        for (Element element : elements) {
            List<Genre> genres = genreManager.getGenresForElement(element.getId());
            element.setGenres(genres);
        }

        return elements;

    }

    public Boolean isElementAvailable(Integer id) throws SQLException {

        String query = "SELECT quantityavailable FROM elements WHERE id = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    int quantityAvailable = rs.getInt("quantityavailable");

                    if (quantityAvailable > 0) {
                        return true;

                    } else {

                        System.err.println("Elemento non disponibile.");
                        return false;

                    }

                } else {

                    System.err.println("Elemento non trovato.");
                    return false;

                }

            }

        }

    }

    public List<Element> getFilteredElements(String title, Integer releaseYear, List<Genre> genres, Map<String, Object> additionalFilters) throws SQLException {

        List<Element> elements = new ArrayList<>();
        List<Object> parameters = new ArrayList<>();

        // Costruzione dinamica della query,
        // In questo modo tutti i filtri successivi possono iniziare con "AND",
        // senza dover controllare se WHERE esiste già (1=1 è sempre vero)
        StringBuilder query = new StringBuilder("SELECT * FROM elements WHERE 1=1");

        if (title != null && !title.isEmpty()) {

            query.append(" AND title LIKE ?");
            parameters.add("%" + title + "%");

        }

        if (releaseYear != null) {

            query.append(" AND releaseyear = ?");
            parameters.add(releaseYear);

        }

        if (genres != null && !genres.isEmpty()) {

            query.append(" AND id IN (SELECT elementid FROM elementgenres WHERE genreid IN (");

            // Aggiunta di segnaposti "?" per ogni genere nella lista
            // ricavando i dati dalla struttura dati usando stream e collect di java
            query.append(genres.stream().map(g -> "?").collect(Collectors.joining(", ")));
            query.append("))"); // Chiusura della query

            // Estrazione e aggiunta dei codici (id) dei generi alla lista dei parametri
            parameters.addAll(genres.stream().map(Genre::getCode).toList());

        }

        addCustomFilters(query, parameters, additionalFilters);

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query.toString());

        if (!ConnectionManager.getInstance().isConnectionValid()) {

            System.err.println("Connessione al database non valida.");
            return elements;

        }

        // Imposta i parametri dinamici IN ORDINE
        for (int i = 0; i < parameters.size(); i++) {

            if (parameters.get(i) instanceof String) {
                stmt.setString(i + 1, (String) parameters.get(i));

            } else if (parameters.get(i) instanceof Integer) {
                stmt.setInt(i + 1, (Integer) parameters.get(i));

            }

        }

        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {

            Integer releaseYearDB = rs.getInt("releaseyear");
            if (rs.wasNull()) {
                releaseYearDB = null;
            }
            Integer length = rs.getInt("length");
            if (rs.wasNull()) {
                length = null;
            }

            Element element = new Element(
                    rs.getInt("id"),
                    rs.getString("title"),
                    releaseYearDB,
                    rs.getString("description"),
                    rs.getInt("quantity"),
                    rs.getInt("quantityavailable"),
                    length,
                    new ArrayList<>()
            );

            elements.add(element);

        }
        stmt.close();

        GenreManager genreManager = MainController.getInstance().getGenreManager();
        for (Element element : elements) {
            List<Genre> genresList = genreManager.getGenresForElement(element.getId());
            element.setGenres(genresList);
        }

        return elements;

    }

    protected void addCustomFilters(StringBuilder query, List<Object> parameters, Map<String, Object> customFilters) {
        // Metodo vuoto per essere sovrascritto nelle classi figlie
    }

    public Element getCompleteElementById(Integer id) {
        try {
            Integer type = MainController.getInstance().getElementManager().getElementTypeById(id);
            if (type == 1) {
                return MainController.getInstance().getBookManager().getBook(id);
            } else if (type == 2) {
                return MainController.getInstance().getDigitalMediaManager().getDigitalMedia(id);
            } else if (type == 3) {
                return MainController.getInstance().getPeriodicPublicationManager().getPeriodicPublication(id);
            } else {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }


}