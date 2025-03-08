package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.Genre;

import java.util.LinkedList;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.List;
import java.util.ArrayList;


public class ElementManager {



    public ElementManager() {}


    public Integer addElement(Element element) {

        String query = "INSERT INTO elements (title, release_year, description, quantity, quantity_available, length) VALUES (?, ?, ?, ?, ?, ?)";

        //Connessione dal ConnectionManager
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return null;

            }

            stmt.setString(1, element.getTitle());
            stmt.setInt(2, element.getReleaseYear());
            stmt.setString(3, element.getDescription());
            stmt.setInt(4, element.getQuantity());
            stmt.setInt(5, element.getQuantityAvailable());
            stmt.setInt(6, element.getLength());

            //Inserimento e ritorno del numero di righe inserite
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {

                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {

                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);  // Restituisce l'ID generato
                    }

                }

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return null;

    }

    public Boolean removeElement(Integer id) {

        String query = "DELETE FROM elements WHERE id = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            //Imposta l'ID dell'elemento da rimuovere
            stmt.setInt(1, id);

            //Query di rimozione
            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {

                System.out.println("Elemento con ID " + id + " rimosso correttamente.");
                return true;

            } else {

                System.err.println("Nessun elemento trovato con ID " + id);
                return false;

            }

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

    public Boolean updateElement(Element element){

        //Verifica la validità dei dati
        if (element.getTitle() == null || element.getTitle().isEmpty() ||
                element.getReleaseYear() == null || element.getReleaseYear() <= 0 ||
                element.getQuantity() < 0 ||
                element.getQuantityAvailable() < 0 ||
                element.getLength() == null || element.getLength() <= 0) {

            System.err.println("Dati non validi per l'elemento.");
            return false;

        }

        String query = "UPDATE elements SET title = ?, release_year = ?, description = ?, quantity = ?, quantity_available = ?, length = ? WHERE id = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection()) {

            //Inizia una transazione
            connection.setAutoCommit(false);

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            //PreparedStatement per eseguire l'update
            try (PreparedStatement stmt = connection.prepareStatement(query)) {

                stmt.setString(1, element.getTitle());
                stmt.setInt(2, element.getReleaseYear());
                stmt.setString(3, element.getDescription());
                stmt.setInt(4, element.getQuantity());
                stmt.setInt(5, element.getQuantityAvailable());
                stmt.setInt(6, element.getLength());

                int rowsUpdated = stmt.executeUpdate();

                //Se non è stato aggiornato alcun elemento annulla la transazione
                if (rowsUpdated == 0) {

                    connection.rollback();
                    System.err.println("Nessun elemento trovato con l'ID fornito.");
                    return false;

                }

                //Se tutto va a buon fine, conferma la transazione
                connection.commit();
                System.out.println("Elemento aggiornato con successo.");
                return true;

            } catch (SQLException e) {

                //Se c'è un errore, fai il rollback della transazione
                connection.rollback();
                System.err.println("Errore durante l'aggiornamento dell'elemento: " + e.getMessage());
                return false;

            }

        } catch (SQLException e) {

            System.err.println("Errore nella connessione al database: " + e.getMessage());
            return false;

        }

    }

    public Element getElement(Integer id){

        if (id == null || id <= 0) {

            System.err.println("ID non valido.");
            return null;

        }

        String query = "SELECT * FROM elements WHERE id = ?";

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

                    Element element = new Element(
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres
                    );

                    return element;

                } else {

                    System.err.println("Elemento non trovato con ID: " + id);
                    return null;

                }

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero dell'elemento: " + e.getMessage());
            return null;

        }

    }

    public List<Element> getAllElements() {

        List<Element> elements = new ArrayList<>();
        String query = "SELECT * FROM elements";

        try (Connection connection = ConnectionManager.getInstance().getConnection()) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return elements;

            }

            try (PreparedStatement stmt = connection.prepareStatement(query);
                 ResultSet rs = stmt.executeQuery()) {

                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    Integer elementId = rs.getInt("id");

                    LinkedList<Genre> genres = genreManager.getGenresForElement(elementId);

                    Element element = new Element(
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres
                    );

                    elements.add(element);

                }

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero degli elementi: " + e.getMessage());
            e.printStackTrace();

        }

        return elements;

    }

    public List<Element> getElementsByGenre (Integer genreCode) {

        if (genreCode == null || genreCode <= 0) {

            System.err.println("Codice genere non valido.");
            return null;

        }

        String query = "SELECT e.*, g.genre_code, g.genre_name FROM elements e JOIN elements_genre eg ON e.id = eg.element_id JOIN genres g ON eg.genre_code = g.genre_code WHERE g.genre_code = ?";
        //TODO: fare tabella

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return null;

            }

            stmt.setInt(1, genreCode);

            try (ResultSet rs = stmt.executeQuery()) {

                List<Element> elements = new ArrayList<>();
                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    LinkedList<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    Element element = new Element(
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres
                    );

                    elements.add(element);

                }

                return elements;

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero degli elementi per genere: " + e.getMessage());
            return null;

        }

    }

    public List<Element> getElementsByTitle (String title) {

        if (title == null || title.isEmpty()) {

            System.err.println("Titolo non valido.");
            return null;

        }

        String query = "SELECT * FROM elements WHERE title = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return null;

            }

            stmt.setString(1, title);

            try (ResultSet rs = stmt.executeQuery()) {

                List<Element> elements = new ArrayList<>();
                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    LinkedList<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    Element element = new Element(
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres
                    );

                    elements.add(element);

                }

                return elements;

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero degli elementi per titolo: " + e.getMessage());
            return null;

        }

    }

    public List<Element> getElementsByReleaseYear (Integer releaseYear) {

        if (releaseYear == null || releaseYear <= 0) {

            System.err.println("Anno di rilascio non valido.");
            return null;

        }

        String query = "SELECT * FROM elements WHERE release_year = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return null;

            }

            stmt.setInt(1, releaseYear);

            try (ResultSet rs = stmt.executeQuery()) {

                List<Element> elements = new ArrayList<>();
                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    LinkedList<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    Element element = new Element(
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres
                    );

                    elements.add(element);

                }

                return elements;

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero degli elementi per anno di rilascio: " + e.getMessage());
            return null;

        }

    }

    public List<Element> getElementsByLength (Integer length) {

        if (length == null || length <= 0) {

            System.err.println("Durata non valida.");
            return null;

        }

        String query = "SELECT * FROM elements WHERE length = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return null;

            }

            stmt.setInt(1, length);

            try (ResultSet rs = stmt.executeQuery()) {

                List<Element> elements = new ArrayList<>();
                GenreManager genreManager = new GenreManager();

                while (rs.next()) {

                    LinkedList<Genre> genres = genreManager.getGenresForElement(rs.getInt("id"));

                    Element element = new Element(
                            rs.getString("title"),
                            rs.getInt("release_year"),
                            rs.getString("description"),
                            rs.getInt("quantity"),
                            rs.getInt("quantity_available"),
                            rs.getInt("length"),
                            genres
                    );

                    elements.add(element);

                }

                return elements;

            }

        } catch (SQLException e) {

            System.err.println("Errore durante il recupero degli elementi per durata: " + e.getMessage());
            return null;

        }

    }

}