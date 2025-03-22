package com.swe.libraryprogram.orm;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BorrowsDAOTest {

    @InjectMocks
    private BorrowsDAO borrowsDAO;

    private static Connection connection;

    private static int generatedId;

    @BeforeAll
    static void setUp() throws SQLException {
        connection = ConnectionManagerTest.getInstance().getConnection();

        connection.setAutoCommit(false);

        String insertElementQuery = "INSERT INTO elements (title, releaseyear, description, quantity, quantityavailable, length) " +
                "VALUES ('Test Element', 2025, 'Test Description', 5, 5, 300)";

        try (Statement stmtInsert = connection.createStatement()) {
            stmtInsert.executeUpdate(insertElementQuery, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = stmtInsert.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);

                String insertUserQuery = "INSERT INTO users (email, password, name," +
                        " surname, phone, isadmin )" +
                        "VALUES ('testUser', 'testPassword', 'testName', 'testSurname', 'testPhone', false)";
                stmtInsert.executeUpdate(insertUserQuery);


            }
        }
        Mockito.clearAllCaches();

        mockStatic(ConnectionManager.class);

        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);

        when(ConnectionManager.getInstance()).thenReturn(mockConnectionManager);
        when(mockConnectionManager.getConnection()).thenReturn(connection);
        when(mockConnectionManager.isConnectionValid()).thenReturn(true);
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Test
    void addBorrowTest() throws SQLException {
        assertTrue(borrowsDAO.addBorrow(generatedId,"testUser"));
        connection.rollback();
    }

    @Test
    void removeBorrowTest() throws SQLException {


        String insertBorrowQuery = "INSERT INTO borrows (elementid, userid) VALUES" +
                " (" + generatedId + ", 'testUser')";

        try (Statement stmtInsert = connection.createStatement()) {
            stmtInsert.executeUpdate(insertBorrowQuery);

        }

        assertTrue(borrowsDAO.removeBorrow(generatedId,"testUser"));
    }

    @Test
    void getBorrowsedElementsForUserTest() throws SQLException {
        assertNotNull(borrowsDAO.getBorrowedElementsForUser("testUser"));
    }



}
