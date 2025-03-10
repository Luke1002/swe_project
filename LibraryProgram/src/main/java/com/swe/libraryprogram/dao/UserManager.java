package com.swe.libraryprogram.dao;
import com.swe.libraryprogram.domainmodel.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;



public class UserManager {

    public UserManager() {}


    public User getUser(String email) {

        if (email == null) {

            System.err.println("email utente non valida.");
            return null;

        }

        String query = "SELECT * FROM users WHERE email = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return null;

            }

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    User user = new User(
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("phone"),
                            rs.getBoolean("isadmin")
                    );

                    return user;

                } else {

                    System.err.println("Utente non trovato con la seguente email: " + email);
                    return null;

                }

            }

        } catch (SQLException e) {

            System.err.println("Errore SQL durante il recupero dell'utente: " + e.getMessage());
            e.printStackTrace();
            return null;

        }

    }

    public Boolean authenticate(String email, String password) {

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {

            System.err.println("Credenziali non valide.");
            return false;

        }

        String query = "SELECT password FROM users WHERE email = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    String storedPassword = rs.getString("password");

                    if (password.equals(storedPassword)) {
                        return true;

                    } else {

                        System.err.println("Password errata.");
                        return false;

                    }

                } else {

                    System.err.println("Utente non trovato.");
                    return false;

                }

            }

        } catch (SQLException e) {

            System.err.println("Errore SQL durante l'autenticazione: " + e.getMessage());
            e.printStackTrace();
            return false;

        }

    }

    public Boolean addUser(User user) {

        if (user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getName() == null || user.getName().isEmpty() ||
                user.getSurname() == null || user.getSurname().isEmpty()){

            System.err.println("Dati utente non validi.");
            return false;

        }

        String query = "INSERT INTO users (email, password, name, surname, phone, isadmin) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getName());
            stmt.setString(4, user.getSurname());
            stmt.setString(5, user.getPhone());
            stmt.setBoolean(6, user.isAdmin());

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {

                System.out.println("Utente inserito correttamente.");
                return true;

            } else {

                System.err.println("Errore nell'inserimento dell'utente.");
                return false;

            }

        } catch (SQLException e) {

            System.err.println("Errore SQL durante l'inserimento dell'utente: " + e.getMessage());
            e.printStackTrace();
            return false;

        }

    }

    public Boolean updateUser(User user) {

        if (user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getName() == null || user.getName().isEmpty() ||
                user.getSurname() == null || user.getSurname().isEmpty()) {

            System.err.println("Dati utente non validi.");
            return false;

        }

        String query = "UPDATE users SET password = ?, name = ?, surname = ?, phone = ?, isadmin = ? WHERE email = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setString(1, user.getPassword());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getSurname());
            stmt.setString(4, user.getPhone());
            stmt.setBoolean(5, user.isAdmin());
            stmt.setString(6, user.getEmail());

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {

                System.out.println("Utente aggiornato correttamente.");
                return true;

            } else {

                System.err.println("Errore nell'aggiornamento dell'utente.");
                return false;

            }

        } catch (SQLException e) {

            System.err.println("Errore SQL durante le modifiche all'utente: " + e.getMessage());
            e.printStackTrace();
            return false;

        }

    }

    public Boolean removeUser(String email) {

        if (email == null || email.isEmpty()) {

            System.err.println("Email utente non valida.");
            return false;

        }

        String query = "DELETE FROM users WHERE email = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setString(1, email);

            int rowsDeleted = stmt.executeUpdate();

            if (rowsDeleted > 0) {

                System.out.println("Utente rimosso correttamente.");
                return true;

            } else {

                System.err.println("Nessun utente trovato con email: " + email);
                return false;

            }

        } catch (SQLException e) {

            System.err.println("Errore SQL durante la rimozione dell'utente: " + e.getMessage());
            e.printStackTrace();
            return false;

        }

    }

    public Boolean isEmailTaken(String email) {

        if (email == null || email.isEmpty()) {

            System.err.println("Email utente non valida.");
            return false;

        }

        String query = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return false;

            }

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    int count = rs.getInt(1);
                    return count > 0;

                } else {

                    System.err.println("Email già in uso.");
                    return false;

                }

            }

        } catch (SQLException e) {

            System.err.println("Errore SQL durante il controllo delle email già in uso: " + e.getMessage());
            e.printStackTrace();
            return false;

        }

    }

    public List<User> getAllUsers() {

        List<User> users = new LinkedList<>();

        String query = "SELECT * FROM users";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            if (!ConnectionManager.getInstance().isConnectionValid()) {

                System.err.println("Connessione al database non valida.");
                return null;

            }

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    User user = new User(
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("name"),
                            rs.getString("surname"),
                            rs.getString("phone"),
                            rs.getBoolean("isadmin")
                    );

                    users.add(user);

                }

                return users;

            }

        } catch (SQLException e) {

            System.err.println("Errore SQL durante il recupero degli utenti: " + e.getMessage());
            e.printStackTrace();
            return users;

        }

    }

}
