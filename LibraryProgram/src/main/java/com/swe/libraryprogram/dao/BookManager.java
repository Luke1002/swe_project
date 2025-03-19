package com.swe.libraryprogram.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.swe.libraryprogram.controller.MainController;
import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.Genre;
import com.swe.libraryprogram.domainmodel.Book;


public class BookManager extends ElementManager {

    public BookManager() {
    }


    public Integer addBook(Book book) throws SQLException {

        String query = "WITH inserted_element AS (" + "    INSERT INTO elements (title, releaseyear, description, quantity, quantityavailable, length)" + "    VALUES (?, ?, ?, ?, ?, ?)" + "    RETURNING id)" + "INSERT INTO books (id, isbn, author, publisher, edition) " + "SELECT id, ?, ?, ?, ? FROM inserted_element " + "RETURNING id;";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);

        stmt.setString(1, book.getTitle());
        if (book.getReleaseYear() != null) {
            stmt.setInt(2, book.getReleaseYear());
        } else {
            stmt.setNull(2, java.sql.Types.INTEGER);
        }
        stmt.setString(3, book.getDescription());
        stmt.setInt(4, book.getQuantity());
        stmt.setInt(5, book.getQuantityAvailable());
        if (book.getLength() != null) {
            stmt.setInt(6, book.getLength());
        } else {
            stmt.setNull(6, java.sql.Types.INTEGER);
        }
        stmt.setString(7, book.getIsbn());
        stmt.setString(8, book.getAuthor());
        stmt.setString(9, book.getPublisher());
        if (book.getLength() != null) {
            stmt.setInt(10, book.getEdition());
        } else {
            stmt.setNull(10, java.sql.Types.INTEGER);
        }

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            System.err.println("Errore: il libro non è stato inserito.");
            return null;
        }
    }

    public Boolean updateBook(Book book) throws SQLException {

        String updateBookQuery = "WITH element_id AS(UPDATE elements SET title = ?, releaseyear = ?, description = ?," +
                " quantity = ?, quantityavailable = ?, length = ? WHERE id = ? RETURNING id) UPDATE books SET isbn = ?," +
                " author = ?, publisher = ?, edition = ? WHERE id = (SELECT id FROM element_id)";
        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(updateBookQuery);

        stmt.setString(1, book.getTitle());
        if (book.getReleaseYear() != null) {
            stmt.setInt(2, book.getReleaseYear());
        } else {
            stmt.setNull(2, java.sql.Types.INTEGER);
        }
        stmt.setString(3, book.getDescription());
        stmt.setInt(4, book.getQuantity());
        stmt.setInt(5, book.getQuantityAvailable());
        if (book.getLength() != null) {
            stmt.setInt(6, book.getLength());
        } else {
            stmt.setNull(6, java.sql.Types.INTEGER);
        }
        stmt.setString(7, book.getIsbn());
        stmt.setString(8, book.getAuthor());
        stmt.setString(9, book.getPublisher());
        if (book.getLength() != null) {
            stmt.setInt(10, book.getEdition());
        } else {
            stmt.setNull(10, java.sql.Types.INTEGER);
        }

        int rowsUpdated = stmt.executeUpdate();

        if (rowsUpdated > 0) {
            return true;

        } else {

            System.err.println("Errore: il libro non è stato aggiornato.");
            return false;

        }

    }

    public Element getBook(Integer id) throws SQLException {

        String query = "SELECT * FROM elements e JOIN books b ON e.id = b.id WHERE e.id = ?";

        List<Element> elements = executeQueryWithSingleValue(query, id);
        return elements.getFirst();

    }

    public List<Book> getAllBooks() throws SQLException {

        List<Book> books = new ArrayList<>();

        String query = "SELECT * FROM elements e JOIN books b ON e.id = b.id";

        try (Connection connection = ConnectionManager.getInstance().getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return books;

            }

            try (ResultSet rs = stmt.executeQuery()) {

                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    List<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    Book book = new Book(rs.getInt("id"), rs.getString("title"), rs.getInt("release_year"), rs.getString("description"), rs.getInt("quantity"), rs.getInt("quantity_available"), rs.getInt("length"), genres, rs.getString("isbn"), rs.getString("author"), rs.getString("publisher"), rs.getInt("edition"));

                    books.add(book);

                }

                return books;

            }

        }

    }

    public List<Element> getBooksByAuthor(String author) throws SQLException {

        String query = "SELECT * FROM elements e JOIN books b ON e.id = b.id WHERE b.author = ?";

        return executeQueryWithSingleValue(query, author);

    }

    public List<Element> getBooksByPublisher(String publisher) throws SQLException {

        String query = "SELECT * FROM elements e JOIN books b ON e.id = b.id WHERE b.publisher = ?";

        return executeQueryWithSingleValue(query, publisher);

    }

    public List<Element> getBooksByEdition(Integer edition) throws SQLException {

        String query = "SELECT * FROM elements e JOIN books b ON e.id = b.id WHERE b.edition = ?";

        return executeQueryWithSingleValue(query, edition);

    }

    public Element getBookByIsbn(String isbn) throws SQLException {

        String query = "SELECT * FROM elements e JOIN books b ON e.id = b.id WHERE b.isbn = ?";
        try{
            return executeQueryWithSingleValue(query, isbn).getFirst();
        }
        catch(Exception e){
            return null;
        }

    }

    @Override
    protected List<Element> executeQueryWithSingleValue(String query, Object value) throws SQLException {

        List<Element> elements = new ArrayList<>();

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt = connection.prepareStatement(query);

        if (!ConnectionManager.getInstance().isConnectionValid()) {

            System.err.println("Connessione al database non valida.");
            return elements;

        }

        stmt.setObject(1, value);

        ResultSet rs = stmt.executeQuery();

        GenreManager genreManager = MainController.getInstance().getGenreManager();

        while (rs.next()) {

            List<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));
            Integer releaseYear = rs.getInt("release_year");
            if (rs.wasNull()) {
                releaseYear = null;
            }
            Integer length = rs.getInt("length");
            if (rs.wasNull()) {
                length = null;
            }
            Integer edition = rs.getInt("edition");
            if (rs.wasNull()) {
                edition = null;
            }
            Book book = new Book(rs.getInt("id"), rs.getString("title"), releaseYear, rs.getString("description"), rs.getInt("quantity"), rs.getInt("quantity_available"), length, genres, rs.getString("isbn"), rs.getString("author"), rs.getString("publisher"), edition);

            elements.add(book);

        }

        return elements;


    }

    @Override
    protected void addCustomFilters(StringBuilder query, List<Object> parameters, Map<String, Object> customFilters) {

        if (customFilters.isEmpty()) {
            return;

        }

        query.append("AND id IN (SELECT id FROM books WHERE 1=1");

        if (customFilters.containsKey("author")) {

            query.append(" AND author = ?");
            parameters.add(customFilters.get("author"));

        }

        if (customFilters.containsKey("publisher")) {

            query.append(" AND publisher = ?");
            parameters.add(customFilters.get("publisher"));

        }

        if (customFilters.containsKey("edition")) {

            query.append(" AND edition = ?");
            parameters.add(customFilters.get("edition"));

        }

        if (customFilters.containsKey("isbn")) {

            query.append(" AND isbn = ?");
            parameters.add(customFilters.get("isbn"));

        }

        query.append(")");

    }


}

