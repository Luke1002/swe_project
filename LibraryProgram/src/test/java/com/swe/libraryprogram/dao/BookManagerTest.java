package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.controller.MainController;
import com.swe.libraryprogram.domainmodel.Book;
import com.swe.libraryprogram.domainmodel.Element;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



@ExtendWith(MockitoExtension.class)
class BookManagerTest {

    @InjectMocks
    private BookManager bookManager;

    @Mock
    private static Connection connection;

    private static int generatedId;


    @BeforeAll
    static void setUp() throws SQLException {

        // Crea un DataSource H2 in memoria
        JdbcDataSource dataSource = new JdbcDataSource();
        dataSource.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"); // In memoria
        dataSource.setUser("sa");
        dataSource.setPassword("");

        // Ottieni la connessione dal DataSource
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);

        //crea tabella Elements
        String createElements = "CREATE TABLE elements" +
                "(" +
                "    id                INTEGER AUTO_INCREMENT " +
                "        PRIMARY KEY," +
                "    title             VARCHAR(64)  DEFAULT ''::CHARACTER VARYING NOT NULL," +
                "    releaseyear       INTEGER," +
                "    description       VARCHAR(256) DEFAULT ''::CHARACTER VARYING NOT NULL," +
                "    quantity          INTEGER      DEFAULT 1                     NOT NULL ," +
                "    quantityavailable INTEGER      DEFAULT 1                     NOT NULL ," +
                "    length            INTEGER" +
                ");";

        //crea tabella Books
        String createBooks = "CREATE TABLE books" +
                "(" +
                "    id        INTEGER                                   NOT NULL" +
                "        REFERENCES elements" +
                "            ON DELETE CASCADE ," +
                "    isbn      VARCHAR(13)                               NOT NULL ," +
                "    author    VARCHAR(64) DEFAULT ''::CHARACTER VARYING NOT NULL," +
                "    publisher VARCHAR(64) DEFAULT ''::CHARACTER VARYING NOT NULL," +
                "    edition   INTEGER," +
                "    PRIMARY KEY (isbn, id)" +
                ");";


        //esegui query per creazione di tabelle fittizie
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createElements);
            stmt.execute(createBooks);
        }



        String insertElementQuery = "INSERT INTO elements (title, releaseyear, description, quantity, quantityavailable, length) " +
                "VALUES ('Test Book', 2025, 'Test Description', 5, 5, 300)";

        try (Statement stmtInsert = connection.createStatement()) {
            stmtInsert.executeUpdate(insertElementQuery, Statement.RETURN_GENERATED_KEYS);
            ResultSet generatedKeys = stmtInsert.getGeneratedKeys();
            if (generatedKeys.next()) {
                generatedId = generatedKeys.getInt(1); // Get the generated ID from elements
                // Ora inseriamo un libro nella tabella books con l'ID generato
                String insertBookQuery = "INSERT INTO books (id, isbn, author, publisher, edition) " +
                        "VALUES (" + generatedId + ", 'isbnTest', 'Test Author', 'Test Publisher', 1)";
                stmtInsert.executeUpdate(insertBookQuery);
            }
        }

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

    @AfterEach
    void tearDown() throws SQLException {
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

