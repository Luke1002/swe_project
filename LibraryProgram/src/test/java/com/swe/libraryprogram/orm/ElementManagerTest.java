package com.swe.libraryprogram.orm;

import com.swe.libraryprogram.services.MainController;
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
public class ElementManagerTest {

    @InjectMocks
    private ElementManager elementManager;

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
        mockStatic(MainController.class);
        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);
        ElementManager mockElementManager = mock(ElementManager.class);
        when(ConnectionManager.getInstance()).thenReturn(mockConnectionManager);
        when(mockConnectionManager.getConnection()).thenReturn(connection);
        GenreManager mockGenreManager = mock(GenreManager.class);
        MainController mockMainController = mock(MainController.class);
        when(MainController.getInstance()).thenReturn(mockMainController);
        when(mockMainController.getGenreManager()).thenReturn(mockGenreManager);
        when(mockGenreManager.getGenresForElement(anyInt())).thenReturn(new ArrayList<>());
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

        assertTrue(elementManager.removeElement(generatedId));

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

        Element element = elementManager.getElement(generatedId);

        assertEquals(generatedId, element.getId());

    }

    @Test
    public void getCompleteElementByIdTest() throws SQLException {

        int testElementId = 123; // Example ID

        // Mock ElementManager
        ElementManager mockElementManager = mock(ElementManager.class);

        // Mock Book
        int bookType = 1;
        Book mockBook = mock(Book.class);
        when(mockBook.getId()).thenReturn(testElementId);
        when(mockElementManager.getElementTypeById(testElementId)).thenReturn(bookType);

        BookManager mockBookManager = mock(BookManager.class);
        when(mockBookManager.getBook(testElementId)).thenReturn(mockBook);

        // Mock DigitalMedia
        int digitalMediaType = 2;
        DigitalMedia mockDigitalMedia = mock(DigitalMedia.class);
        when(mockDigitalMedia.getId()).thenReturn(testElementId);
        DigitalMediaManager mockDigitalMediaManager = mock(DigitalMediaManager.class);
        when(mockDigitalMediaManager.getDigitalMedia(testElementId)).thenReturn(mockDigitalMedia);

        // Mock PeriodicPublication
        int periodicPublicationType = 3;
        PeriodicPublication mockPeriodicPublication = mock(PeriodicPublication.class);
        when(mockPeriodicPublication.getId()).thenReturn(testElementId);
        PeriodicPublicationManager mockPeriodicPublicationManager = mock(PeriodicPublicationManager.class);
        when(mockPeriodicPublicationManager.getPeriodicPublication(testElementId)).thenReturn(mockPeriodicPublication);

        // Mock MainController
        MainController mockMainController = mock(MainController.class);
        when(MainController.getInstance()).thenReturn(mockMainController);
        when(mockMainController.getElementManager()).thenReturn(mockElementManager);
        when(mockMainController.getBookManager()).thenReturn(mockBookManager);
        when(mockMainController.getDigitalMediaManager()).thenReturn(mockDigitalMediaManager);
        when(mockMainController.getPeriodicPublicationManager()).thenReturn(mockPeriodicPublicationManager);

        //Book
        when(mockElementManager.getElementTypeById(testElementId)).thenReturn(bookType);
        Element bookResult = elementManager.getCompleteElementById(testElementId);
        assertNotNull(bookResult);
        assertEquals(testElementId, bookResult.getId());

        //DigitalMedia
        when(mockElementManager.getElementTypeById(testElementId)).thenReturn(digitalMediaType);
        Element digitalMediaResult = elementManager.getCompleteElementById(testElementId);
        assertNotNull(digitalMediaResult);
        assertEquals(testElementId, digitalMediaResult.getId());

        //PeriodicPublication
        when(mockElementManager.getElementTypeById(testElementId)).thenReturn(periodicPublicationType);
        Element periodicPublicationResult = elementManager.getCompleteElementById(testElementId);
        assertNotNull(periodicPublicationResult);
        assertEquals(testElementId, periodicPublicationResult.getId());

        //Invalid type
        when(mockElementManager.getElementTypeById(testElementId)).thenReturn(99); // Invalid type
        Element nullResult = elementManager.getCompleteElementById(testElementId);
        assertNull(nullResult);

    }

}
