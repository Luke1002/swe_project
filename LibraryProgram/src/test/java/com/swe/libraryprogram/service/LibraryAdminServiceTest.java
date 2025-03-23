package com.swe.libraryprogram.service;


import com.swe.libraryprogram.domainmodel.*;
import com.swe.libraryprogram.orm.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LibraryAdminServiceTest {

    @InjectMocks
    private LibraryAdminService libraryAdminService;

    @Mock
    private MainService mainService;

    @Mock
    private BookDAO bookDAO;

    @Mock
    private DigitalMediaDAO digitalMediaDAO;

    @Mock
    private PeriodicPublicationDAO periodicPublicationDAO;

    @Mock
    private ElementDAO elementDAO;

    @Mock
    private GenreDAO genreDAO;

    private Book book;

    private DigitalMedia media;

    private PeriodicPublication periodic;

    @BeforeEach
    void setUp() throws SQLException {

        lenient().when(MainService.getInstance()).thenReturn(mainService);
        lenient().when(mainService.getBookDAO()).thenReturn(bookDAO);
        lenient().when(mainService.getDigitalMediaDAO()).thenReturn(digitalMediaDAO);
        lenient().when(mainService.getPeriodicPublicationDAO()).thenReturn(periodicPublicationDAO);
        lenient().when(mainService.getGenreDAO()).thenReturn(genreDAO);
        lenient().when(mainService.getElementDAO()).thenReturn(elementDAO);

        List<Genre> genres = new ArrayList<>();
        genres.add(new Genre("genretest",1));

        book = new Book(1,"test book", 1, "", 1, 1, 12,
                genres,
                "0000000000000", "", "", 1);

        media = new DigitalMedia(1,"test media", 1, "", 1, 1, 12,
                genres, "", "", "");

        periodic = new PeriodicPublication(1,"test periodic", 1, "", 1, 1, 12,
                genres, "", "", 1, 1);

        lenient().when(bookDAO.addBook(book)).thenReturn(book.getId());
        lenient().when(digitalMediaDAO.addDigitalMedia(media)).thenReturn(media.getId());
        lenient().when(periodicPublicationDAO.addPeriodicPublication(periodic)).thenReturn(periodic.getId());
        lenient().when(genreDAO.associateGenreWithElement(book.getId(),1)).thenReturn(true);
        lenient().when(genreDAO.associateGenreWithElement(media.getId(),1)).thenReturn(true);
        lenient().when(genreDAO.associateGenreWithElement(periodic.getId(),1)).thenReturn(true);
        lenient().when(bookDAO.updateBook(book)).thenReturn(true);
        lenient().when(digitalMediaDAO.updateDigitalMedia(media)).thenReturn(true);
        lenient().when(periodicPublicationDAO.updatePeriodicPublication(periodic)).thenReturn(true);


    }

    @Test
    @Order(1)
    public void addElementTestUC10() throws SQLException {

        System.out.println("-------- TST UC 10 --------");

        lenient().when(bookDAO.getBookByIsbn(book.getIsbn())).thenReturn(null);

        assertTrue(libraryAdminService.addElement(book));
        assertTrue(libraryAdminService.addElement(media));
        assertTrue(libraryAdminService.addElement(periodic));

    }

    @Test
    @Order(2)
    public void addElementTestUC10F4A() throws SQLException {

        System.out.println("TST UC 10 FLOW 4A:");
        System.out.println("Testing per ciascun tipo di elemento...");

        lenient().when(bookDAO.getBookByIsbn(book.getIsbn())).thenReturn(null);

        book.setTitle(null);
        media.setQuantity(0);
        periodic.setReleaseMonth(40);

        assertFalse(libraryAdminService.addElement(book));
        assertFalse(libraryAdminService.addElement(media));
        assertFalse(libraryAdminService.addElement(periodic));

    }

    @Test
    @Order(3)
    public void addElementTestUC10F4B() throws SQLException {

        System.out.println("TST UC 10 FLOW 4B:");

        lenient().when(bookDAO.getBookByIsbn(book.getIsbn())).thenReturn(book);

        assertFalse(libraryAdminService.addElement(book));

    }

    @Test
    @Order(4)
    public void addElementTestUC10F4C() throws SQLException {

        System.out.println("TST UC 10 FLOW 4C:");

        lenient().when(bookDAO.getBookByIsbn(book.getIsbn())).thenThrow(SQLException.class);

        assertFalse(libraryAdminService.addElement(book));

    }

    @Test
    @Order(5)
    public void updateElementTestUC12() throws SQLException {

        System.out.println("-------- TST UC 12 --------");

        lenient().when(bookDAO.getBookByIsbn(book.getIsbn())).thenReturn(null);

        book.setAuthor("updated author");
        book.setReleaseYear(4);
        media.setTitle("updated title");
        media.setProducer("updated producer");
        periodic.setFrequency("updated frequency");
        periodic.setReleaseDay(4);

        assertTrue(libraryAdminService.updateElement(book));
        assertTrue(libraryAdminService.updateElement(media));
        assertTrue(libraryAdminService.updateElement(periodic));

    }

    @Test
    @Order(6)
    public void updateElementTestUC12F6A() throws SQLException {

        System.out.println("TST UC 12 FLOW 6A:");
        System.out.println("Testing per ciascun tipo di elemento...");

        lenient().when(bookDAO.getBookByIsbn(book.getIsbn())).thenReturn(null);

        book.setTitle(null);
        media.setQuantity(0);
        periodic.setReleaseMonth(40);

        assertFalse(libraryAdminService.updateElement(book));
        assertFalse(libraryAdminService.updateElement(media));
        assertFalse(libraryAdminService.updateElement(periodic));

    }

    @Test
    @Order(7)
    public void updateElementTestUC12F6B() throws SQLException {

        System.out.println("TST UC 12 FLOW 6B:");

        lenient().when(bookDAO.getBookByIsbn(book.getIsbn())).thenReturn(book);

        assertFalse(libraryAdminService.updateElement(book));

    }

    @Test
    @Order(8)
    public void updateElementTestUC12F6C() throws SQLException {

        System.out.println("TST UC 12 FLOW 6C:");

        lenient().when(bookDAO.getBookByIsbn(book.getIsbn())).thenThrow(SQLException.class);

        assertFalse(libraryAdminService.updateElement(book));

    }

    @Test
    @Order(9)
    public void addGenreTestUC13() throws SQLException {
        System.out.println("-------- TST UC 13 --------");

        lenient().when(genreDAO.getGenreByName("test genre")).thenThrow(SQLException.class);
        lenient().when(genreDAO.addGenre(any(Genre.class))).thenReturn(true);

        assertTrue(libraryAdminService.addGenre("test genre"));

    }

    @Test
    @Order(10)
    public void addGenreTestUC13F3A() throws SQLException {
        System.out.println("TST UC 13 FLOW 3A:");

        lenient().when(genreDAO.getGenreByName("test genre")).thenReturn(null);

        assertFalse(libraryAdminService.addGenre("test genre"));

    }

    @Test
    @Order(10)
    public void addGenreTestUC13F3B() throws SQLException {
        System.out.println("TST UC 13 FLOW 3B:");

        lenient().when(genreDAO.getGenreByName("test genre")).thenThrow(SQLException.class);
        lenient().when(genreDAO.addGenre(any(Genre.class))).thenThrow(SQLException.class);

        assertFalse(libraryAdminService.addGenre("test genre"));

    }

    @Test
    @Order(11)
    public void removeElementTestUC11() throws SQLException {

        System.out.println("-------- TST UC 11 --------");

        lenient().when(elementDAO.removeElement(book.getId())).thenReturn(true);

        assertTrue(libraryAdminService.removeElement(book));

    }

    @Test
    @Order(12)
    public void removeElementTestUC11F3A() throws SQLException {

        System.out.println("TST UC 11 FLOW 3A:");

        book.setQuantity(5);

        assertFalse(libraryAdminService.removeElement(book));

    }

    @Test
    @Order(13)
    public void removeElementTestUC11F3B() throws SQLException {

        System.out.println("TST UC 11 FLOW 3B:");

        lenient().when(elementDAO.removeElement(book.getId())).thenThrow(SQLException.class);

        assertFalse(libraryAdminService.removeElement(book));

    }

}
