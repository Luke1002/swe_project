package com.swe.libraryprogram.service;

import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.User;
import com.swe.libraryprogram.orm.BorrowsDAO;
import com.swe.libraryprogram.orm.ConnectionManagerTest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LibraryUserServiceTest {

    @InjectMocks
    private LibraryUserService libraryUserService;

    @Mock
    private MainService mainService;

    @Mock
    private BorrowsDAO borrowsDAO;

    private User user;

    @BeforeEach
    public void setUp() throws SQLException {

        user = new User("test email", "test password",
                "test name", "test surname" );

        when(MainService.getInstance()).thenReturn(mainService);
        when(mainService.getBorrowsDAO()).thenReturn(borrowsDAO);

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
}
