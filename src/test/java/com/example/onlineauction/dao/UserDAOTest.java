package com.example.onlineauction.dao;

import com.example.onlineauction.constants.Role;
import com.example.onlineauction.constants.Status;
import com.example.onlineauction.dao.UserDAO;
import com.example.onlineauction.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDAOTest {
    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockStatement;
    @Mock
    private ResultSet mockResultSet;

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDAO = new UserDAO(mockConnection);
    }

    @Test
    void saveUser_WhenUserDoesNotExist_ShouldSaveUser() throws SQLException {
        User user = new User("John", "Doe", "johndoe", "password", LocalDate.now(), Role.SELLER, Status.ACTIVE);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertDoesNotThrow(() -> userDAO.saveUser(user));

        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void blockUser_ShouldUpdateUserStatusToBlock() throws SQLException {
        int userId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        assertDoesNotThrow(() -> userDAO.blockUser(userId));

        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void unblockUser_ShouldUpdateUserStatusToActive() throws SQLException {
        int userId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        assertDoesNotThrow(() -> userDAO.unblockUser(userId));

        verify(mockStatement, times(1)).executeUpdate();
    }

    @Test
    void deleteUser_ShouldDeleteUser() throws SQLException {
        int userId = 1;
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        assertDoesNotThrow(() -> userDAO.deleteUser(userId));

        verify(mockStatement, times(1)).executeUpdate();
    }
}
