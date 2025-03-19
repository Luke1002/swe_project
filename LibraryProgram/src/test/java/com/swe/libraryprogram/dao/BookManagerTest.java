package com.swe.libraryprogram.dao;

import com.swe.libraryprogram.domainmodel.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.sql.*;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class BookManagerTest {

    @InjectMocks
    private BookManager bookManager;

    @Mock
    private ConnectionManager connectionManager;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;


    @BeforeEach
    void setUp() throws SQLException {

        MockitoAnnotations.openMocks(this);
        when(connectionManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);

    }

    @Test
    void testAddBook_Success() throws SQLException {

        Book book = new Book("Il signore degli anelli",1800,"non lo so",
                2,1,600,new ArrayList<>(), "222","Tolkien",
                "yes", 23);

        when(preparedStatement.executeUpdate()).thenReturn(1); // Simula l'inserimento riuscito

        Integer result = bookManager.addBook(book);

        assertNotNull(result, "Il risultato dell'aggiunta dovrebbe essere un ID valido.");
        verify(preparedStatement, times(1)).setInt(anyInt(), anyInt());
        // Verifica che il PreparedStatement sia stato configurato correttamente
        verify(preparedStatement, times(1)).executeUpdate();
        // Verifica che executeUpdate sia stato chiamato

    }

    @Test
    void testAddBook_Failure() throws SQLException {

        Book book = new Book("Il signore degli anelli",1800,"non lo so",
                2,1,600,new ArrayList<>(), "222","Tolkien",
                "yes", 23);

        when(preparedStatement.executeUpdate()).thenReturn(0); // Simula errore nell'inserimento

        Integer result = bookManager.addBook(book);

        assertNull(result, "Se l'inserimento fallisce, il risultato dovrebbe essere null.");
    }
}

