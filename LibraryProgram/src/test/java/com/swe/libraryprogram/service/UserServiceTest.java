package com.swe.libraryprogram.service;

import com.swe.libraryprogram.domainmodel.User;
import com.swe.libraryprogram.orm.UserDAO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private MainService mainService;

    @Mock
    private UserDAO userDAO;

    static User user;

    @BeforeAll
    public static void setUp() {

        user = new User("test email","test password",
                "test name","test surname", "test phone", false);

    }

    @Test
    public void loginTestUC1() throws SQLException {

        System.out.println("-------- TST UC 1 --------");

        when(MainService.getInstance()).thenReturn(mainService);
        when(mainService.getUserDAO()).thenReturn(userDAO);
        when(userDAO.authenticate(user.getEmail(), user.getPassword())).thenReturn(user);

        assertNotNull(userService.login(user.getEmail(), user.getPassword()));

    }

    @Test
    public void loginTestUC1F3A() throws SQLException {

        System.out.println("TST UC 1 FLOW 3A:");



    }

    @Test
    public void loginTestUC1F3B() throws SQLException {

        System.out.println("TST UC 1 FLOW 3B:");



    }

    @Test
    public void loginTestUC1F3C() throws SQLException {

        System.out.println("TST UC 1 FLOW 3C:");


    }

}
