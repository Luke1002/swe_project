package com.swe.libraryprogram.dao;


import com.swe.libraryprogram.controller.MainController;
import com.swe.libraryprogram.domainmodel.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserManagerTest {

    @InjectMocks
    private UserManager userManager;

    private static Connection connection;

    @BeforeAll
    static void setUp() throws SQLException {

        connection = ConnectionManagerTest.getInstance().getConnection();

        connection.setAutoCommit(false);

        //String insertUserQuery = "INSERT INTO users (email, password, name," +
                //" surname, phone, isadmin " +
                //"VALUES ('testUser', 'testPassword', 'testName', 'testSurname', 'testPhone', false)";

        //String insertAdminQuery = "INSERT INTO users (email, password, name," +
                //" surname, phone, isadmin " +
                //"VALUES ('testAdmin', 'testPassword', 'testName', 'testSurname', 'testPhone', true)";

        //try (Statement stmtInsert = connection.createStatement()) {
            //stmtInsert.executeUpdate(insertUserQuery);
            //stmtInsert.executeUpdate(insertAdminQuery);

        //}
        Mockito.clearAllCaches();

        mockStatic(ConnectionManager.class);
        mockStatic(MainController.class);

        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);
        when(ConnectionManager.getInstance()).thenReturn(mockConnectionManager);
        when(mockConnectionManager.getConnection()).thenReturn(connection);

        MainController mockMainController = mock(MainController.class);
        when(MainController.getInstance()).thenReturn(mockMainController);

    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
    }


    @Test
    void addUserTest() throws SQLException {
        assertTrue(userManager.addUser(new User("testInsertedUser", "testInsertedPassword",
                "testInsertedName", "testInsertedSurname", "testInsertedPhone", false)));
    }


}
