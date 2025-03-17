package com.swe.libraryprogram.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.Genre;
import com.swe.libraryprogram.domainmodel.Book;



public class BookManager extends ElementManager {

    public BookManager() {}


    public Boolean addBook(Book book) throws SQLException {

        Integer elementId = addElement(book);

        if (elementId == null) {

            System.out.println("Errore: l'inserimento delle informazioni di base del libro è fallito.");
            return false;

        }

        String insertBookQuery = "INSERT INTO books (id, isbn, author, publisher, edition) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement bookStmt = connection.prepareStatement(insertBookQuery)) {

            bookStmt.setInt(1, elementId);
            bookStmt.setString(12, book.getIsbn());
            bookStmt.setString(3, book.getAuthor());
            bookStmt.setString(4, book.getPublisher());
            bookStmt.setInt(5, book.getEdition());

            int rowsInserted = bookStmt.executeUpdate();

            if (rowsInserted > 0) {
                return true;

            } else {

                System.err.println("Errore: il libro non è stato inserito.");
                return false;

            }

        }

    }

    public Boolean updateBook(Book book) throws SQLException {

        if (!updateElement(book)) {

            System.err.println("Errore durante l'aggiornamento delle informazioni base del libro.");
            return false;

        }

        String updateBookQuery = "UPDATE books SET isbn = ?, author = ?, publisher = ?, edition = ? WHERE id = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement bookStmt = connection.prepareStatement(updateBookQuery)) {

            bookStmt.setString(1, book.getIsbn());
            bookStmt.setString(2, book.getAuthor());
            bookStmt.setString(3, book.getPublisher());
            bookStmt.setInt(4, book.getEdition());
            bookStmt.setInt(5, book.getId());

            int rowsUpdated = bookStmt.executeUpdate();

            if (rowsUpdated > 0) {
                return true;

            } else {

                System.err.println("Errore: il libro non è stato aggiornato.");
                return false;

            }

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

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return books;

            }

            try (ResultSet rs = stmt.executeQuery()) {

                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    LinkedList<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    Book book = new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres,
                            rs.getString("isbn"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getInt("edition")
                    );

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

    public List<Element> getBooksByIsbn(Integer isbn) throws SQLException {

        String query = "SELECT * FROM elements e JOIN books b ON e.id = b.id WHERE b.isbn = ?";

        return executeQueryWithSingleValue(query, isbn);

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

                    Book book = new Book(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres,
                            rs.getString("isbn"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getInt("edition")
                    );

                    elements.add(book);

                }

                return elements;

            }

        }

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

