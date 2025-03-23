package com.swe.libraryprogram.service;

import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.User;
import com.swe.libraryprogram.orm.BorrowsDAO;
import com.swe.libraryprogram.orm.ConnectionManagerTest;
import com.swe.libraryprogram.orm.ElementDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LibraryUserServiceTest {

    @Spy
    @InjectMocks
    private LibraryUserService libraryUserService;

    @Mock
    private MainService mainService;

    @Mock
    private BorrowsDAO borrowsDAO;

    @Mock
    private ElementDAO elementDAO;

    private User user;

    @BeforeEach
    public void setUp() throws SQLException {

        user = new User("test email", "test password",
                "test name", "test surname" );

        lenient().when(MainService.getInstance()).thenReturn(mainService);
        lenient().when(mainService.getBorrowsDAO()).thenReturn(borrowsDAO);
        lenient().when(mainService.getElementDAO()).thenReturn(elementDAO);
        lenient().when(mainService.getUser()).thenReturn(user);

    }


    @Test
    @Order(1)
    public void getBorrowedElementsTestUC9() throws SQLException {

        System.out.println("-------- TST UC 9 --------");

        Element element1 = new Element("test title", 1, "test description",
                1,1,1,new ArrayList<>());

        List<Element> borrowedElements = new ArrayList<>();
        borrowedElements.add(element1);

        when(borrowsDAO.getBorrowedElementsForUser(anyString())).thenReturn(borrowedElements);

        assertFalse(libraryUserService.getBorrowedElements(user).isEmpty());

    }

    @Test
    @Order(2)
    public void getBorrowedElementsTestUC9F2A() throws SQLException {

        System.out.println("TST UC 9 FLOW 2A:");

        when(borrowsDAO.getBorrowedElementsForUser(anyString())).thenReturn(new ArrayList<>());

        assertTrue(libraryUserService.getBorrowedElements(user).isEmpty());

    }

    @Test
    @Order(3)
    public void getBorrowedElementsTestUC9F2B() throws SQLException {

        System.out.println("TST UC 9 FLOW 2B:");

        when(borrowsDAO.getBorrowedElementsForUser(anyString())).thenThrow(SQLException.class);

        assertNull(libraryUserService.getBorrowedElements(user));

    }

    @Test
    @Order(4)
    public void borrowElementTestUC7() throws SQLException {

        System.out.println("-------- TST UC 7 --------");

        Element element = new Element(1,"test title",1,"test description",1,
                1,1,new ArrayList<>());

        lenient().when(elementDAO.getElement(element.getId())).thenReturn(element);
        lenient().doReturn(new ArrayList<>()).when(libraryUserService).getBorrowedElements(user);
        lenient().when(borrowsDAO.addBorrow(element.getId(), user.getEmail())).thenReturn(true);

        assertTrue(libraryUserService.borrowElement(element.getId()));

    }

    @Test
    @Order(5)
    public void borrowElementTestUC7F3A() throws SQLException {

        System.out.println("TST UC 7 FLOW 3A:");

        Element element = new Element(1,"test title",1,"test description",1,
                1,1,new ArrayList<>());

        List <Element> borrowedElements = new ArrayList<>();
        borrowedElements.add(element);

        lenient().when(elementDAO.getElement(element.getId())).thenReturn(element);
        lenient().doReturn(borrowedElements).when(libraryUserService).getBorrowedElements(user);

        assertFalse(libraryUserService.borrowElement(element.getId()));

    }

    @Test
    @Order(6)
    public void borrowElementTestUC7F3B() throws SQLException {

        System.out.println("TST UC 7 FLOW 3B:");

        Element element = new Element(1,"test title",1,"test description",1,
                0,1,new ArrayList<>());

        lenient().when(elementDAO.getElement(element.getId())).thenReturn(element);
        lenient().doReturn(new ArrayList<>()).when(libraryUserService).getBorrowedElements(user);

        assertFalse(libraryUserService.borrowElement(element.getId()));

    }

    @Test
    @Order(7)
    public void borrowElementTestUC7F3C() throws SQLException {

        System.out.println("TST UC 7 FLOW 3C:");

        Element element = new Element(1,"test title",1,"test description",1,
                0,1,new ArrayList<>());

        lenient().when(elementDAO.getElement(element.getId())).thenThrow(SQLException.class);

        assertFalse(libraryUserService.borrowElement(element.getId()));

    }

    @Test
    @Order(8)
    public void returnElementTestUC8() throws SQLException {

        System.out.println("-------- TST UC 8 --------");

        Element element = new Element(1,"test title",1,"test description",1,
                1,1,new ArrayList<>());

        List<Element> borrowedElements = new ArrayList<>();
        borrowedElements.add(element);

        lenient().when(elementDAO.getElement(element.getId())).thenReturn(element);
        lenient().doReturn(borrowedElements).when(libraryUserService).getBorrowedElements(user);
        lenient().when(borrowsDAO.removeBorrow(element.getId(), user.getEmail())).thenReturn(true);

        assertTrue(libraryUserService.returnElement(element.getId()));

    }

    @Test
    @Order(9)
    public void returnElementTestUC8F3A() throws SQLException {

        System.out.println("TST UC 8 FLOW 3A:");

        Element element = new Element(1,"test title",1,"test description",1,
                1,1,new ArrayList<>());

        lenient().when(elementDAO.getElement(element.getId())).thenReturn(element);
        lenient().doReturn(new ArrayList<>()).when(libraryUserService).getBorrowedElements(user);

        assertFalse(libraryUserService.returnElement(element.getId()));

    }

    @Test
    @Order(10)
    public void returnElementTestUC8F3B() throws SQLException {

        System.out.println("TST UC 8 FLOW 3B:");

        Element element = new Element(1,"test title",1,"test description",1,
                1,1,new ArrayList<>());

        lenient().when(elementDAO.getElement(element.getId())).thenThrow(SQLException.class);

        assertFalse(libraryUserService.returnElement(element.getId()));

    }

}
