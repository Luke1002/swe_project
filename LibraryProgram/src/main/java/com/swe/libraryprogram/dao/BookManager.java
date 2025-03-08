package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.domainmodel.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.swe.libraryprogram.domainmodel.Genre;


public class BookManager extends ElementManager {

    public BookManager() {}


    // Metodo per aggiungere un libro
    public Boolean addBook(Book book) {

        Integer elementId = addElement(book);

        if (elementId == null) {

            System.out.println("Errore: l'inserimento dell'elemento base è fallito.");
            return false;

        }

        String insertBookQuery = "INSERT INTO books (id, isbn, author, publisher, edition) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement bookStmt = connection.prepareStatement(insertBookQuery)) {

            bookStmt.setInt(1, elementId);
            bookStmt.setInt(2, book.getIsbn());
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

        } catch (SQLException e) {

            System.err.println("Errore SQL durante l'inserimento del libro: " + e.getMessage());
            e.printStackTrace();

        }

        return false;

    }

    //Metodo per aggiornare un libro
    public Boolean updateBook(Book book) {

        if (book == null) {

            System.err.println("Libro non valido.");
            return false;

        }

        if (book.getId() == null || book.getId() <= 0) {

            System.err.println("ID non valido.");
            return false;

        }

        if (!updateElement(book)) {

            System.err.println("Errore durante l'aggiornamento dell'elemento base.");
            return false;

        }

        String updateBookQuery = "UPDATE books SET isbn = ?, author = ?, publisher = ?, edition = ? WHERE id = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement bookStmt = connection.prepareStatement(updateBookQuery)) {

            bookStmt.setInt(1, book.getIsbn());
            bookStmt.setString(2, book.getAuthor());
            bookStmt.setString(3, book.getPublisher());
            bookStmt.setInt(4, book.getEdition());
            bookStmt.setInt(5, book.getId());

            int rowsUpdated = bookStmt.executeUpdate();

            if (rowsUpdated > 0) {
                return true;  //L'aggiornamento è andato a buon fine

            } else {
                System.err.println("Errore: il libro non è stato aggiornato.");

            }

        } catch (SQLException e) {

            System.err.println("Errore SQL durante l'aggiornamento del libro: " + e.getMessage());
            e.printStackTrace();

        }

        return false;

    }

    // Metodo per ottenere un libro tramite ID
    public Book getBook(Integer id) {

        if (id == null || id <= 0) {

            System.err.println("ID non valido.");
            return null;

        }

        String query = "SELECT * FROM elements e JOIN books b ON e.id = b.id WHERE e.id = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return null;

            }

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    GenreManager genreManager = new GenreManager();
                    LinkedList<Genre> genres = genreManager.getGenresForElement(id);

                    Book book = new Book(
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres,
                            rs.getInt("isbn"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getInt("edition")
                    );

                    return book;

                } else {

                    System.err.println("Libro non trovato con ID: " + id);
                    return null;

                }

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero del libro: " + e.getMessage());
            e.printStackTrace();

        }

        return null;

    }

    //Metodo per ottenere tutti i libri
    public List<Book> getAllBooks() {

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
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres,
                            rs.getInt("isbn"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getInt("edition")
                    );

                    books.add(book);

                }

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero dei libri: " + e.getMessage());
            e.printStackTrace();

        }

        return books;

    }

    // Metodo per ottenere tutti i libri di un autore specifico
    public List<Book> getBooksByAuthor(String author) {

        List<Book> books = new ArrayList<>();

        if (author == null || author.isEmpty()) {

            System.err.println("Autore non valido.");
            return books;

        }

        String query = "SELECT * FROM elements e JOIN books b ON e.id = b.id WHERE b.author = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return books;

            }

            stmt.setString(1, author);

            try (ResultSet rs = stmt.executeQuery()) {

                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    LinkedList<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    Book book = new Book(
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres,
                            rs.getInt("isbn"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getInt("edition")
                    );

                    books.add(book);

                }

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero dei libri: " + e.getMessage());
            e.printStackTrace();

        }

        return books;

    }

    // Metodo per ottenere tutti i libri di una casa editrice specifica
    public List<Book> getBooksByPublisher(String publisher) {

        List<Book> books = new ArrayList<>();

        if (publisher == null || publisher.isEmpty()) {

            System.err.println("Casa editrice non valida.");
            return books;

        }

        String query = "SELECT * FROM elements e JOIN books b ON e.id = b.id WHERE b.publisher = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return books;

            }

            stmt.setString(1, publisher);

            try (ResultSet rs = stmt.executeQuery()) {

                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    LinkedList<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    Book book = new Book(
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres,
                            rs.getInt("isbn"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getInt("edition")
                    );

                    books.add(book);

                }

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero dei libri: " + e.getMessage());
            e.printStackTrace();

        }

        return books;

    }

    // Metodo per ottenere i libri di una certa edizione
    public List<Book> getBooksByEdition(Integer edition) {

        List<Book> books = new ArrayList<>();

        if (edition == null || edition <= 0) {

            System.err.println("Edizione non valida.");
            return books;

        }

        String query = "SELECT * FROM elements e JOIN books b ON e.id = b.id WHERE b.edition = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return books;

            }

            stmt.setInt(1, edition);

            try (ResultSet rs = stmt.executeQuery()) {

                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    LinkedList<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    Book book = new Book(
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres,
                            rs.getInt("isbn"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getInt("edition")
                    );

                    books.add(book);

                }

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero dei libri: " + e.getMessage());
            e.printStackTrace();

        }

        return books;

    }

    // Metodo per ottenere i libri via ISBN
    public List<Book> getBooksByIsbn(Integer isbn) {

        List<Book> books = new ArrayList<>();

        if (isbn == null || isbn <= 0) {

            System.err.println("ISBN non valido.");
            return books;

        }

        String query = "SELECT * FROM elements e JOIN books b ON e.id = b.id WHERE b.isbn = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return books;

            }

            stmt.setInt(1, isbn);

            try (ResultSet rs = stmt.executeQuery()) {

                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    LinkedList<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    Book book = new Book(
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres,
                            rs.getInt("isbn"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getInt("edition")
                    );

                    books.add(book);

                }

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero dei libri: " + e.getMessage());
            e.printStackTrace();

        }

        return books;

    }

}

