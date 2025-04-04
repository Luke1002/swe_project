package com.swe.libraryprogram.orm;


import com.swe.libraryprogram.domainmodel.Element;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ElementDAOTest {

    @InjectMocks
    private ElementDAO elementDAO;

    @Spy
    private ElementDAO elementDAOCompleteId;

    @Mock
    private BookDAO bookDAO;

    @Mock
    private DigitalMediaDAO digitalMediaDAO;

    @Mock
    private PeriodicPublicationDAO periodicPublicationManager;

    private static Connection connection;

    private static int generatedId;

    @BeforeAll
    static void setUp() throws SQLException {

        connection = ConnectionManagerTest.getInstance().getConnection();

        connection.setAutoCommit(false);

        String insertElementQuery = "INSERT INTO elements (title, releaseyear, description, quantity," +
                " quantityavailable, length) " +
                "VALUES ('Test Element', 2025, 'Test Description', 5, 5, 300)";

        try (Statement stmtInsert = connection.createStatement()) {
            stmtInsert.executeUpdate(insertElementQuery, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = stmtInsert.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }
        }

        Mockito.clearAllCaches();

        mockStatic(ConnectionManager.class);
        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);
        when(ConnectionManager.getInstance()).thenReturn(mockConnectionManager);
        when(mockConnectionManager.getConnection()).thenReturn(connection);
        GenreDAO mockGenreDAO = mock(GenreDAO.class);
        when(mockGenreDAO.getGenresForElement(anyInt())).thenReturn(new ArrayList<>());
        when(mockConnectionManager.isConnectionValid()).thenReturn(true);


    }

    @AfterAll
    static void tearDown() throws SQLException {

        connection.rollback();
        connection.close();
    }

    @Test
    void removeElementTest() throws SQLException {

        String insertElementToRemoveQuery = "INSERT INTO elements (title, releaseyear, description, quantity," +
                " quantityavailable, length) " +
                "VALUES ('Test Element to delete', 2025, 'Test Description', 5, 5, 300)";

        try (Statement stmtInsert = connection.createStatement()) {
            stmtInsert.executeUpdate(insertElementToRemoveQuery, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = stmtInsert.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }
        }

        assertTrue(elementDAO.removeElement(generatedId));

    }

    @Test
    void getElementTest() throws SQLException {

        String insertElementToGet = "INSERT INTO elements (title, releaseyear, description, quantity," +
                " quantityavailable, length) " +
                "VALUES ('Test Element to get', 2025, 'Test Description', 5, 5, 300)";

        try (Statement stmtInsert = connection.createStatement()) {

            stmtInsert.executeUpdate(insertElementToGet, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = stmtInsert.getGeneratedKeys();

            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
            }
        }

        Element element = elementDAO.getElement(generatedId);

        assertEquals(generatedId, element.getId());

    }

}
