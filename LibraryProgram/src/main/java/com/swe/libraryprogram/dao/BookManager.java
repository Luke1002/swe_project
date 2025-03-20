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

        String insertElementQuery = "INSERT INTO elements (title, releaseyear, description, quantity, quantityavailable, length) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmtElement = connection.prepareStatement(insertElementQuery, PreparedStatement.RETURN_GENERATED_KEYS);

        stmtElement.setString(1, book.getTitle());
        if (book.getReleaseYear() != null) {
            stmtElement.setInt(2, book.getReleaseYear());
        } else {
            stmtElement.setNull(2, java.sql.Types.INTEGER);
        }
        stmtElement.setString(3, book.getDescription());
        stmtElement.setInt(4, book.getQuantity());
        stmtElement.setInt(5, book.getQuantityAvailable());
        if (book.getLength() != null) {
            stmtElement.setInt(6, book.getLength());
        } else {
            stmtElement.setNull(6, java.sql.Types.INTEGER);
        }

        // Execute the insert statement for 'elements'
        int rowsAffected = stmtElement.executeUpdate();
        Integer insertedElementId = null;

        // If the insertion was successful, retrieve the generated key for 'elements'
        if (rowsAffected > 0) {
            ResultSet generatedKeys = stmtElement.getGeneratedKeys();
            if (generatedKeys.next()) {
                insertedElementId = generatedKeys.getInt(1);  // Get the 'id' of the inserted element
            }
        }

        if (insertedElementId == null) {
            System.err.println("Error: Failed to insert into 'elements' table.");
            stmtElement.close();
            return null;  // Exit early if the element insertion failed
        }

        // Step 2: Insert into 'books' table using the generated element ID
        String insertBookQuery = "INSERT INTO books (id, isbn, author, publisher, edition) " +
                "VALUES (?, ?, ?, ?, ?)";

        PreparedStatement stmtBook = connection.prepareStatement(insertBookQuery, PreparedStatement.RETURN_GENERATED_KEYS);

        stmtBook.setInt(1, insertedElementId);
        stmtBook.setString(2, book.getIsbn());
        stmtBook.setString(3, book.getAuthor());
        stmtBook.setString(4, book.getPublisher());
        if (book.getLength() != null) {
            stmtBook.setInt(5, book.getEdition());
        } else {
            stmtBook.setNull(5, java.sql.Types.INTEGER);
        }

        // Execute the insert statement for 'books'
        int rowsAffectedBook = stmtBook.executeUpdate();
        Integer result = null;

        // If the insertion into books was successful, get the generated key for 'books'
        if (rowsAffectedBook > 0) {
            ResultSet generatedKeysBook = stmtBook.getGeneratedKeys();
            if (generatedKeysBook.next()) {
                result = generatedKeysBook.getInt(1);  // Get the generated ID for the 'books' table
            }
        }

        if (result == null) {
            System.err.println("Error: Failed to insert into 'books' table.");
        }

        stmtElement.close();
        stmtBook.close();

        return result;
    }

    public Boolean updateBook(Book book) throws SQLException {

        String updateElementQuery = "UPDATE elements SET title = ?, releaseyear = ?, description = ?, quantity = ?, quantityavailable = ?, length = ? WHERE id = ?";
        String updateBookQuery = "UPDATE books SET isbn = ?, author = ?, publisher = ?, edition = ? WHERE id = ?";

        Connection connection = ConnectionManager.getInstance().getConnection();
        PreparedStatement stmt1 = connection.prepareStatement(updateElementQuery);
        PreparedStatement stmt2 = connection.prepareStatement(updateBookQuery);

        stmt1.setString(1, book.getTitle());
        if (book.getReleaseYear() != null) {
            stmt1.setInt(2, book.getReleaseYear());
        } else {
            stmt1.setNull(2, java.sql.Types.INTEGER);
        }
        stmt1.setString(3, book.getDescription());
        stmt1.setInt(4, book.getQuantity());
        stmt1.setInt(5, book.getQuantityAvailable());
        if (book.getLength() != null) {
            stmt1.setInt(6, book.getLength());
        } else {
            stmt1.setNull(6, java.sql.Types.INTEGER);
        }
        stmt1.setInt(7, book.getId());
        int rowsUpdated= stmt1.executeUpdate();
        stmt1.close();

        if (rowsUpdated > 0) {


            stmt2.setString(1, book.getIsbn());
            stmt2.setString(2, book.getAuthor());
            stmt2.setString(3, book.getPublisher());
            if (book.getLength() != null) {
                stmt2.setInt(4, book.getEdition());
            } else {
                stmt2.setNull(4, java.sql.Types.INTEGER);
            }
            stmt2.setInt(5, book.getId());
            stmt2.executeUpdate();
            stmt2.close();

            if (rowsUpdated > 0) {
                return true;
            } else {
                return false;
            }

        } else {
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
            Integer edition = rs.getInt("edition");
            if (rs.wasNull()) {
                edition = null;
            }
            Book book = new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    releaseYear, rs.getString("description"),
                    rs.getInt("quantity"),
                    rs.getInt("quantityavailable"),
                    length,
                    new ArrayList<>(),
                    rs.getString("isbn"),
                    rs.getString("author"),
                    rs.getString("publisher"),
                    edition);

            books.add(book);

        }
        stmt.close();

        GenreManager genreManager = MainController.getInstance().getGenreManager();
        for (Element element : books){
            List<Genre> genres = genreManager.getGenresForElement(element.getId());
            element.setGenres(genres);
        }
        return books;
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
        try {
            return executeQueryWithSingleValue(query, isbn).getFirst();
        } catch (Exception e) {
            return null;
        }

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
            Integer edition = rs.getInt("edition");
            if (rs.wasNull()) {
                edition = null;
            }
            Book book = new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    releaseYear, rs.getString("description"),
                    rs.getInt("quantity"),
                    rs.getInt("quantityavailable"),
                    length,
                    new ArrayList<>(),
                    rs.getString("isbn"),
                    rs.getString("author"),
                    rs.getString("publisher"),
                    edition);

            elements.add(book);

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

