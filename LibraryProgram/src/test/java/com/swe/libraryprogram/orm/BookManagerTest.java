package com.swe.libraryprogram.orm;

import com.swe.libraryprogram.services.MainController;
import com.swe.libraryprogram.domainmodel.Book;
import com.swe.libraryprogram.domainmodel.Element;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
public class BookManagerTest {

    @InjectMocks
    private BookManager bookManager;


    private static Connection connection;

    private static int generatedId;


    @BeforeAll
    static void setUp() throws SQLException {

        // Get the connection directly
        connection = ConnectionManagerTest.getInstance().getConnection();

        // Ottieni la connessione dal DataSource
        //connection = dataSource.getConnection();
        connection.setAutoCommit(false);


        String insertElementQuery = "INSERT INTO elements (title, releaseyear, description, quantity, quantityavailable, length) " +
                "VALUES ('Test Book', 2025, 'Test Description', 5, 5, 300)";

        try (Statement stmtInsert = connection.createStatement()) {
            stmtInsert.executeUpdate(insertElementQuery, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = stmtInsert.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1);
                String insertBookQuery = "INSERT INTO books (id, isbn, author, publisher, edition) " +
                        "VALUES (" + generatedId + ", 'isbnTest', 'Test Author', 'Test Publisher', 1)";
                stmtInsert.executeUpdate(insertBookQuery);
            }
        }
        Mockito.clearAllCaches();

        // Mockare staticamente il metodo ConnectionManager.getInstance()
        mockStatic(ConnectionManager.class);
        mockStatic(MainController.class);
        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);
        when(ConnectionManager.getInstance()).thenReturn(mockConnectionManager);
        when(mockConnectionManager.getConnection()).thenReturn(connection);
        GenreManager mockGenreManager = mock(GenreManager.class);
        MainController mockMainController = mock(MainController.class);
        when(MainController.getInstance()).thenReturn(mockMainController);
        when(mockMainController.getGenreManager()).thenReturn(mockGenreManager);
        when(mockGenreManager.getGenresForElement(anyInt())).thenReturn(new ArrayList<>());

    }

    @AfterAll
    static void tearDown() throws SQLException {
        // Esegui il rollback per pulire i dati dopo ogni test
        connection.rollback();
        connection.close();
    }

    @Test
    void addBookTest() throws SQLException {

        Book book = new Book("titleTest",2000,"descriptionTest",1,1,1,new ArrayList<>(),"isbnTest","authorTest",
                "publisherTest",1);

        Integer id = bookManager.addBook(book);

        assertNotNull(id);

    }

    @Test
    void updateBookTest() throws SQLException {

        assertTrue(bookManager.updateBook(new Book(generatedId,"newTitleTest",
                3000,"descriptionTest",1,1,
                1,new ArrayList<>(),"isbnTest","authorTest",
                "newPublisherTest",1)));

    }

    @Test
    void getBookByIsbnTest() throws SQLException {

        Element book1 = bookManager.getBookByIsbn("isbnTest");

        assertEquals("isbnTest", ((Book) book1).getIsbn());

    }


}

