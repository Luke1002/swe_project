package com.swe.libraryprogram.service;

import com.swe.libraryprogram.domainmodel.Element;
import com.swe.libraryprogram.domainmodel.Genre;
import com.swe.libraryprogram.domainmodel.User;
import com.swe.libraryprogram.orm.ElementDAO;
import com.swe.libraryprogram.orm.UserDAO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private MainService mainService;

    @Mock
    private UserDAO userDAO;

    @Mock
    private ElementDAO elementDAO;

    static User user;

    @BeforeEach
    public void setUp() throws SQLException {

        user = new User("test@email.com","TestPassword6!",
                "test name","test surname", "3567878569", false);


        lenient().when(MainService.getInstance()).thenReturn(mainService);
        lenient().when(mainService.getUserDAO()).thenReturn(userDAO);
        lenient().when(mainService.getElementDAO()).thenReturn(elementDAO);

        lenient().when(userDAO.addUser(any(user.getClass()))).thenReturn(true);

    }

    @Test
    @Order(1)
    public void loginTestUC1() throws SQLException {

        System.out.println("-------- TST UC 1 --------");

        when(userDAO.authenticate(user.getEmail(), user.getPassword())).thenReturn(user);

        assertNotNull(userService.login(user.getEmail(), user.getPassword()));

    }

    @Test
    @Order(2)
    public void loginTestUC1F3A() throws SQLException {

        System.out.println("TST UC 1 FLOW 3A:");

        when(userDAO.authenticate(user.getEmail(), user.getPassword())).thenThrow(new RuntimeException("Identificativo non presente."));

        assertNull(userService.login(user.getEmail(),user.getPassword()));

        verify(userDAO).authenticate(user.getEmail(), user.getPassword());

    }

    @Test
    @Order(3)
    public void loginTestUC1F3B() throws SQLException {

        System.out.println("TST UC 1 FLOW 3B:");

        when(userDAO.authenticate(user.getEmail(), user.getPassword())).thenThrow(new RuntimeException("Password errata."));

        assertNull(userService.login(user.getEmail(),user.getPassword()));

        verify(userDAO).authenticate(user.getEmail(), user.getPassword());

    }

    @Test
    @Order(4)
    public void loginTestUC1F3C() throws SQLException {

        System.out.println("TST UC 1 FLOW 3C:");

        when(userDAO.authenticate(user.getEmail(), user.getPassword())).thenThrow(new SQLException());

        assertNull(userService.login(user.getEmail(),user.getPassword()));

        verify(userDAO).authenticate(user.getEmail(), user.getPassword());


    }

    @Test
    @Order(5)
    public void signupTestUC4() throws SQLException {

        System.out.println("-------- TST UC 4 --------");

        lenient().when(userDAO.getUser(user.getEmail())).thenThrow(new RuntimeException());

        assertTrue(userService.signup(user.getEmail(), user.getPassword(), user.getName(),
        user.getSurname(), user.getPhone()));

    }

    @Test
    @Order(6)
    public void signupTestUC4F3A() throws SQLException {

        System.out.println("TST UC 4 FLOW 3A:");

        user.setEmail("badEmailTest");

        lenient().when(userDAO.getUser(user.getEmail())).thenThrow(new RuntimeException());

        assertFalse(userService.signup(user.getEmail(), user.getPassword(), user.getName(),
                user.getSurname(), user.getPhone()));

    }

    @Test
    @Order(7)
    public void signupTestUC4F3B() throws SQLException {

        System.out.println("TST UC 4 FLOW 3B:");

        user.setPassword("badPasswordTest");

        lenient().when(userDAO.getUser(user.getEmail())).thenThrow(new RuntimeException());

        assertFalse(userService.signup(user.getEmail(), user.getPassword(), user.getName(),
                user.getSurname(), user.getPhone()));

    }

    @Test
    @Order(8)
    public void signupTestUC4F3C() throws SQLException {

        System.out.println("TST UC 4 FLOW 3C:");

        user.setName(null);

        lenient().when(userDAO.getUser(user.getEmail())).thenThrow(new RuntimeException());

        assertFalse(userService.signup(user.getEmail(), user.getPassword(), user.getName(),
                user.getSurname(), user.getPhone()));

    }

    @Test
    @Order(9)
    public void signupTestUC4F3D() throws SQLException {

        System.out.println("TST UC 4 FLOW 3D:");

        user.setSurname(null);

        lenient().when(userDAO.getUser(user.getEmail())).thenThrow(new RuntimeException());

        assertFalse(userService.signup(user.getEmail(), user.getPassword(), user.getName(),
                user.getSurname(), user.getPhone()));

    }

    @Test
    @Order(9)
    public void signupTestUC4F3E() throws SQLException {

        System.out.println("TST UC 4 FLOW 3E:");

        user.setPhone("badPhoneTest");

        lenient().when(userDAO.getUser(user.getEmail())).thenThrow(new RuntimeException());

        assertFalse(userService.signup(user.getEmail(), user.getPassword(), user.getName(),
                user.getSurname(), user.getPhone()));

    }

    @Test
    @Order(9)
    public void signupTestUC4F3F() throws SQLException {

        System.out.println("TST UC 4 FLOW 3F:");

        lenient().when(userDAO.getUser(user.getEmail())).thenReturn(null);

        assertFalse(userService.signup(user.getEmail(), user.getPassword(), user.getName(),
                user.getSurname(), user.getPhone()));

    }

    @Test
    @Order(10)
    public void signupTestUC4F3G() throws SQLException {

        System.out.println("TST UC 4 FLOW 3G:");

        lenient().when(userDAO.getUser(user.getEmail())).thenThrow(new SQLException());

        assertFalse(userService.signup(user.getEmail(), user.getPassword(), user.getName(),
                user.getSurname(), user.getPhone()));

    }

    @Test
    @Order(11)
    public void searchElementsTestUC5() throws SQLException {

        System.out.println("-------- TST UC 5 --------");
        System.out.println("Testing di ricerca per ciascun campo...");

        List<Genre> genreList = new ArrayList<>();

        genreList.add(new Genre("Genre 1"));

        Element element1 = new Element("test title 1",2001,"test description 1",
                1,1,10,genreList);

        genreList.add(new Genre("Genre 2"));

        Element element2 = new Element("test title 2",2002,"test description 2",
                2,2,5,genreList);

        List<Element> elementList = new ArrayList<>();

        elementList.add(element1);
        elementList.add(element2);

        List<String> genreFiltersList = new ArrayList<>();

        genreFiltersList.add("Genre 1");

        List<Element> result1 = userService.searchElements(elementList,"test title 1",
                null,null,
                null,false);

        assertEquals(1, result1.size());

        List<Element> result2 = userService.searchElements(elementList,null,
                genreFiltersList,null,
                null,false);

        assertEquals(2, result2.size());

        List<Element> result3 = userService.searchElements(elementList,null,
                null,2001,
                null,false);

        assertEquals(1, result3.size());

        List<Element> result4 = userService.searchElements(elementList,null,
                null,null,
                10,false);

        assertEquals(1, result4.size());

        List<Element> result5 = userService.searchElements(elementList,null,
                null,null,
                null,true);

        assertEquals(2, result5.size());


    }

    @Test
    @Order(12)
    public void searchElementsTestUC5F3A() throws SQLException {

        System.out.println("TST UC 5 FLOW 3A: ricerca di un elemento non esistente");

        List<Element> elementList = new ArrayList<>();

        elementList.add(new Element("test title 1",2001,"test description 1",
                1,1,10,new ArrayList<>()));

        assertTrue(userService.searchElements(elementList, "test title 1999",
                null, null,null,true).isEmpty());

    }

    @Test
    @Order(13)
    public void getAllElementsTestUC6() throws SQLException {

        System.out.println("-------- TST UC 6 --------");

        Element element = new Element("test title",1,"",1,
                1,1,new ArrayList<>());

        List<Element> elementList = new ArrayList<>();
        elementList.add(element);

        lenient().when(elementDAO.getAllElements()).thenReturn(elementList);

        assertFalse(userService.getAllElements().isEmpty());

    }

    @Test
    @Order(13)
    public void getAllElementsTestUC6F3A() throws SQLException {

        System.out.println("TST UC 6 FLOW 3A:");

        lenient().when(elementDAO.getAllElements()).thenReturn(new ArrayList<>());

        assertTrue(userService.getAllElements().isEmpty());

    }

    @Test
    @Order(14)
    public void getAllElementsTestUC6F3B() throws SQLException {

        System.out.println("TST UC 6 FLOW 3B:");

        lenient().when(elementDAO.getAllElements()).thenThrow(SQLException.class);

        assertNull(userService.getAllElements());

    }

}
