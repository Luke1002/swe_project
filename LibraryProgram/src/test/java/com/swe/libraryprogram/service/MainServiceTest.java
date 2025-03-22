package com.swe.libraryprogram.service;

import com.swe.libraryprogram.domainmodel.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.postgresql.hostchooser.HostRequirement.any;

@ExtendWith(MockitoExtension.class)
public class MainServiceTest {

    @InjectMocks
    private MainService mainService;

    @Mock
    private UserService userService;

    private User user = new User("testemail","testpassword","testname","testsurname","testphone", false);

    private User admin = new User("testemail","testpassword","testname","testsurname","testphone", true);

    @Test
    public void setUserStateTest() throws SQLException {

        when(userService.login(anyString(),anyString())).thenReturn(user);

        assertTrue(mainService.setUserState(user.getEmail(),user.getPassword()));
        assertInstanceOf(LibraryUserService.class, mainService.userService);

    }

    @Test
    public void setUserStateAdminTest() throws SQLException {

        when(userService.login(anyString(),anyString())).thenReturn(admin);

        assertTrue(mainService.setUserState(admin.getEmail(),admin.getPassword()));
        assertInstanceOf(LibraryAdminService.class, mainService.userService);

    }

}
