package com.swe.libraryprogram.controller;

import com.swe.libraryprogram.dao.UserManager;
import com.swe.libraryprogram.domainmodel.User;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private  UserController userController;

    @Mock
    private UserManager mockUserManager;

    private static User user;

    @BeforeAll
    static void setUp() throws SQLException {

        user = new User("emailTest",
                "passwordTest",
                "nameTest",
                "surnameTest",
                "phoneTest",
                false);

        MockitoAnnotations.openMocks(UserControllerTest.class);

    }

    @Test
    void loginTest() throws SQLException {
        when(mockUserManager.authenticate(user.getEmail(), user.getPassword())).thenReturn(user);

        User result = userController.login(user.getEmail(), user.getPassword());

        assertNotNull(result);
    }

    @Test
    void signupWithExistingMailTest() throws SQLException {
       when(mockUserManager.getUser(anyString())).thenReturn(user);

        assertFalse(userController.signup(user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getSurname(),
                user.getPhone()));
    }

    @Test
    void signupWithNotExistingMailTest() throws SQLException {
        when(mockUserManager.addUser(Mockito.any(User.class))).thenReturn(true);
        when(mockUserManager.getUser(anyString())).thenReturn(null);

        assertTrue(userController.signup(user.getEmail(),
                user.getPassword(),
                user.getName(),
                user.getSurname(),
                user.getPhone()));
    }


}
