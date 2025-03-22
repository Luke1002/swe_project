package com.swe.libraryprogram.orm;

import com.swe.libraryprogram.service.MainService;

import com.swe.libraryprogram.domainmodel.DigitalMedia;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class DigitalMediaDAOTest {

    @InjectMocks
    private DigitalMediaDAO digitalMediaDAO;

    private static Connection connection;

    private static int generatedId;


    @BeforeAll
    static void setUp() throws SQLException {

        // Get the connection directly
        connection = ConnectionManagerTest.getInstance().getConnection();

        // Ottieni la connessione dal DataSource
        //connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        String insertElementQuery = "INSERT INTO elements (title, releaseyear, description, quantity," +
                " quantityavailable, length) " +
                "VALUES ('Test Media', 2025, 'Test Description', 5, 5, 300)";

        try (Statement stmtInsert = connection.createStatement()) {
            stmtInsert.executeUpdate(insertElementQuery, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = stmtInsert.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
                String insertMediaQuery = "INSERT INTO digitalmedias (id,producer,agerating,director) " +
                        "VALUES (" + generatedId + ", 'producerTest', 'ageratingTest'," +
                        " 'directorTest')";
                stmtInsert.executeUpdate(insertMediaQuery);
            }
        }
        Mockito.clearAllCaches();

        mockStatic(ConnectionManager.class);
        mockStatic(MainService.class);
        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);
        when(ConnectionManager.getInstance()).thenReturn(mockConnectionManager);
        when(mockConnectionManager.getConnection()).thenReturn(connection);
        GenreDAO mockGenreDAO = mock(GenreDAO.class);
        MainService mockMainService = mock(MainService.class);
        when(MainService.getInstance()).thenReturn(mockMainService);
        when(mockMainService.getGenreManager()).thenReturn(mockGenreDAO);
        when(mockGenreDAO.getGenresForElement(anyInt())).thenReturn(new ArrayList<>());

    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
    }

    @Test
    void addDigitalMediaTest() throws SQLException {

        DigitalMedia media = new DigitalMedia("insertedMediaTest",2020,"insertedDescriptionTest",
                5,5,300,new ArrayList<>(), "insertedProducerTest",
                "insertedAgeRatingTest", "insertedDirectorTest");

        Integer id = digitalMediaDAO.addDigitalMedia(media);

        assertNotNull(id);

    }

    @Test
    void updateDigitalMediaTest() throws SQLException {

        assertTrue(digitalMediaDAO.updateDigitalMedia(new DigitalMedia(generatedId,
                "updatedMediaTest",2000,"updatedDescriptionTest",1,1,1,new ArrayList<>(),
                "updatedProducerTest","updatedAgeRatingTest","updatedDirectorTest")));

    }

    @Test
    void getDigitalMediaTest() throws SQLException {

        assertNotNull(digitalMediaDAO.getDigitalMedia(generatedId));

    }



}
