package com.swe.libraryprogram.orm;


import com.swe.libraryprogram.service.MainService;
import com.swe.libraryprogram.domainmodel.User;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserDAOTest {

    @InjectMocks
    private UserDAO userDAO;

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
        mockStatic(MainService.class);

        ConnectionManager mockConnectionManager = mock(ConnectionManager.class);
        when(ConnectionManager.getInstance()).thenReturn(mockConnectionManager);
        when(mockConnectionManager.getConnection()).thenReturn(connection);

        MainService mockMainService = mock(MainService.class);
        when(MainService.getInstance()).thenReturn(mockMainService);

    }

    @AfterAll
    static void tearDown() throws SQLException {
        connection.rollback();
        connection.close();
    }


    @Test
    void addUserTest() throws SQLException {
        assertTrue(userDAO.addUser(new User("testInsertedUser", "testInsertedPassword",
                "testInsertedName", "testInsertedSurname", "testInsertedPhone", false)));
    }


}
