package com.swe.libraryprogram.orm;


import com.swe.libraryprogram.domainmodel.PeriodicPublication;
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
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class PeriodicPublicationDAOTest {

    @InjectMocks
    private PeriodicPublicationDAO periodicManager;

    private static Connection connection;

    private static int generatedId;

    @BeforeAll
    static void setUp() throws SQLException {

        connection = ConnectionManagerTest.getInstance().getConnection();

        connection.setAutoCommit(false);

        String insertElementQuery = "INSERT INTO elements (title, releaseyear," +
                " description, quantity, quantityavailable, length) " +
                "VALUES ('Test Periodic', 2025, 'Test Description', 5, 5, 300)";

        try (Statement stmtInsert = connection.createStatement()) {
            stmtInsert.executeUpdate(insertElementQuery, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = stmtInsert.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
                String insertPeriodicQuery = "INSERT INTO periodicpublications (id," +
                        " publisher, frequency, releasemonth, releaseday) " +
                        "VALUES (" + generatedId + ", 'publisherTest', 'frequencyTest'," +
                        " 1, 2)";
                stmtInsert.executeUpdate(insertPeriodicQuery);
            }
        }
        Mockito.clearAllCaches();

        mockStatic(ConnectionManager.class);
        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);
        when(ConnectionManager.getInstance()).thenReturn(mockConnectionManager);
        when(mockConnectionManager.getConnection()).thenReturn(connection);
        GenreDAO mockGenreDAO = mock(GenreDAO.class);
        when(mockGenreDAO.getGenresForElement(anyInt())).thenReturn(new ArrayList<>());
    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Test
    void addPeriodicPublicationTest() throws SQLException {

        PeriodicPublication periodic = new PeriodicPublication("insertedPeriodicTest", 2025,
                "insertDescriptionTest", 5, 5, 300, new ArrayList<>(), "insertedPublisherTest",
                "insertedFrequencyTest", 1, 2);

        Integer insertedId = periodicManager.addPeriodicPublication(periodic);

        assertNotNull(insertedId);

    }

    @Test
    void updatePeriodicPublicationTest() throws SQLException {

        assertTrue(periodicManager.updatePeriodicPublication(new PeriodicPublication(generatedId, "updatedPeriodicTest", 2025,
                "updateDescriptionTest", 5, 5, 300, new ArrayList<>(), "updatedPublisherTest",
                "updatedFrequencyTest", 1, 2)));

    }
}
