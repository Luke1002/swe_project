package com.swe.libraryprogram.controller;

import com.swe.libraryprogram.dao.BorrowsManager;
import com.swe.libraryprogram.dao.ConnectionManagerTest;
import com.swe.libraryprogram.domainmodel.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MainControllerTest {

    @InjectMocks
    private MainController mainController;

    //private static Connection connection;

    private static User user;

    private static User userAdmin;

    @Mock
    private static UserController mockUserController;

    @Mock
    private static BorrowsManager mockBorrowsManager;

    @BeforeAll
    static void setUp() throws SQLException {

        //connection = ConnectionManagerTest.getInstance().getConnection();

        user = new User("emailTest", "passwordTest", "nameTest", "surnameTest", "phoneTest", false);

        userAdmin = new User("adminEmailTest", "adminPasswordTest", "adminNameTest", "adminSurnameTest", "adminPhoneTest", true);

        MockitoAnnotations.openMocks(MainControllerTest.class);

    }

    @Test
    void setUserStateForUserTest() throws SQLException {

        when(mockUserController.login(anyString(), anyString())).thenReturn(user);
        when(mockBorrowsManager.getBorrowedElementsForUser(anyString())).thenReturn(new ArrayList<>());

        assertTrue(mainController.setUserState(user.getEmail(), user.getPassword()));

        assertTrue(mainController.getUserController() instanceof LibraryUserController);
    }

    @Test
    void setUserStateForAdminTest() throws SQLException {

        when(mockUserController.login(anyString(), anyString())).thenReturn(userAdmin);

        assertTrue(mainController.setUserState(userAdmin.getEmail(), userAdmin.getPassword()));

        assertTrue(mainController.getUserController() instanceof LibraryAdminController);
    }

    @AfterAll
    static void tearDown() {
    }

}
