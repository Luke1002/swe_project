package com.swe.libraryprogram.orm;

import com.swe.libraryprogram.service.MainService;
import com.swe.libraryprogram.domainmodel.Book;
import com.swe.libraryprogram.domainmodel.DigitalMedia;
import com.swe.libraryprogram.domainmodel.Element;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ElementDAOTest {

    @InjectMocks
    private ElementDAO elementDAO;

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
        mockStatic(MainService.class);
        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);
        ElementDAO mockElementDAO = mock(ElementDAO.class);
        when(ConnectionManager.getInstance()).thenReturn(mockConnectionManager);
        when(mockConnectionManager.getConnection()).thenReturn(connection);
        GenreDAO mockGenreDAO = mock(GenreDAO.class);
        MainService mockMainService = mock(MainService.class);
        when(MainService.getInstance()).thenReturn(mockMainService);
        when(mockMainService.getGenreManager()).thenReturn(mockGenreDAO);
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

    @Test
    public void getCompleteElementByIdTest() throws SQLException {

        int testElementId = 123; // Example ID

        // Mock ElementManager
        ElementDAO mockElementDAO = mock(ElementDAO.class);

        // Mock Book
        int bookType = 1;
        Book mockBook = mock(Book.class);
        when(mockBook.getId()).thenReturn(testElementId);
        when(mockElementDAO.getElementTypeById(testElementId)).thenReturn(bookType);

        BookDAO mockBookDAO = mock(BookDAO.class);
        when(mockBookDAO.getBook(testElementId)).thenReturn(mockBook);

        // Mock DigitalMedia
        int digitalMediaType = 2;
        DigitalMedia mockDigitalMedia = mock(DigitalMedia.class);
        when(mockDigitalMedia.getId()).thenReturn(testElementId);
        DigitalMediaDAO mockDigitalMediaDAO = mock(DigitalMediaDAO.class);
        when(mockDigitalMediaDAO.getDigitalMedia(testElementId)).thenReturn(mockDigitalMedia);

        // Mock PeriodicPublication
        int periodicPublicationType = 3;
        PeriodicPublication mockPeriodicPublication = mock(PeriodicPublication.class);
        when(mockPeriodicPublication.getId()).thenReturn(testElementId);
        PeriodicPublicationDAO mockPeriodicPublicationManager = mock(PeriodicPublicationDAO.class);
        when(mockPeriodicPublicationManager.getPeriodicPublication(testElementId)).thenReturn(mockPeriodicPublication);

        // Mock MainController
        MainService mockMainService = mock(MainService.class);
        when(MainService.getInstance()).thenReturn(mockMainService);
        when(mockMainService.getElementManager()).thenReturn(mockElementDAO);
        when(mockMainService.getBookManager()).thenReturn(mockBookDAO);
        when(mockMainService.getDigitalMediaManager()).thenReturn(mockDigitalMediaDAO);
        when(mockMainService.getPeriodicPublicationManager()).thenReturn(mockPeriodicPublicationManager);

        //Book
        when(mockElementDAO.getElementTypeById(testElementId)).thenReturn(bookType);
        Element bookResult = elementDAO.getCompleteElementById(testElementId);
        assertNotNull(bookResult);
        assertEquals(testElementId, bookResult.getId());

        //DigitalMedia
        when(mockElementDAO.getElementTypeById(testElementId)).thenReturn(digitalMediaType);
        Element digitalMediaResult = elementDAO.getCompleteElementById(testElementId);
        assertNotNull(digitalMediaResult);
        assertEquals(testElementId, digitalMediaResult.getId());

        //PeriodicPublication
        when(mockElementDAO.getElementTypeById(testElementId)).thenReturn(periodicPublicationType);
        Element periodicPublicationResult = elementDAO.getCompleteElementById(testElementId);
        assertNotNull(periodicPublicationResult);
        assertEquals(testElementId, periodicPublicationResult.getId());

        //Invalid type
        when(mockElementDAO.getElementTypeById(testElementId)).thenReturn(99); // Invalid type
        Element nullResult = elementDAO.getCompleteElementById(testElementId);
        assertNull(nullResult);

    }

}
