package com.swe.libraryprogram.orm;

import com.swe.libraryprogram.domainmodel.Genre;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GenreDAOTest {

    @InjectMocks
    private GenreDAO genreDAO;

    private static Connection connection;

    private static int generatedElementId;

    private static int generatedGenreCode;

    @BeforeAll
    public static void setUp() throws SQLException {

        connection = ConnectionManagerTest.getInstance().getConnection();

        connection.setAutoCommit(false);

        String insertElementQuery = "INSERT INTO elements (title, releaseyear, description, quantity," +
                " quantityavailable, length) " +
                "VALUES ('Test Element', 2025, 'Test Description', 5, 5, 300)";

        String insertGenreQuery = "INSERT INTO genres (name)" +
                "VALUES ('Test Genre')";

        try (Statement stmtInsert = connection.createStatement()) {
            stmtInsert.executeUpdate(insertElementQuery, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = stmtInsert.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedElementId = generatedKeys.getInt(1);
                System.out.println("Generated Element ID: " + generatedElementId);

            }
        }

        try (Statement stmtGenre = connection.createStatement()) {
            stmtGenre.executeUpdate(insertGenreQuery, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = stmtGenre.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedGenreCode = generatedKeys.getInt(1);
            }
        }
        connection.commit();

        when(ConnectionManager.getInstance().getConnection()).thenReturn(connection);

        when(ConnectionManager.getInstance().isConnectionValid()).thenReturn(true);

    }

    @AfterAll
    public static void tearDown() throws SQLException {
        if (connection != null) {
            try (Statement stmt = connection.createStatement()) {
                // Delete from elementgenres first to avoid foreign key constraint violations
                stmt.executeUpdate("DELETE FROM elementgenres WHERE elementid = " + generatedElementId);
                stmt.executeUpdate("DELETE FROM genres WHERE code = " + generatedGenreCode);
                stmt.executeUpdate("DELETE FROM elements WHERE id = " + generatedElementId);
                System.out.println("Cleanup successful: Deleted test data.");
            } catch (SQLException e) {
                System.err.println("Cleanup failed: " + e.getMessage());
            } finally {
                connection.close();
            }
        }
    }

    @Test
    public void associateGenreWithElementTest() throws SQLException {

        Boolean result = genreDAO.associateGenreWithElement(generatedElementId, generatedGenreCode);

        assertTrue(result);

    }

    @Test
    public void addGenreTest() throws SQLException {

        Boolean result = genreDAO.addGenre(new Genre("Insert Test"));

        assertTrue(result);

    }

    @Test
    public void getAllGenresTest() throws SQLException {

        List<Genre> result = genreDAO.getAllGenres();

        assertNotNull(result);

    }

    @Test
    public void getGenresForElementTest() throws SQLException {

        String insertAssociationQuery = "INSERT INTO elementgenres (elementid, genrecode) " +
                "VALUES (" + generatedElementId + "," + generatedGenreCode + ")";

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(insertAssociationQuery);

        List<Genre> result = genreDAO.getGenresForElement(generatedElementId);

        assertNotNull(result);

    }

    @Test
    public void removeGenreFromElement() throws SQLException {

        String insertAssociationQuery = "INSERT INTO elementgenres (elementid, genrecode) " +
                "VALUES (" + generatedElementId + "," + generatedGenreCode + ")";

        Statement stmt = connection.createStatement();
        stmt.executeUpdate(insertAssociationQuery);

        assertTrue(genreDAO.removeGenreFromElement(generatedElementId,generatedGenreCode));

    }

}
