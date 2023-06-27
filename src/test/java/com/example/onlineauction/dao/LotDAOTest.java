package com.example.onlineauction.dao;

import com.example.onlineauction.constants.StatusLot;
import com.example.onlineauction.dao.LotDAO;
import com.example.onlineauction.model.Lot;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class LotDAOTest {
    @Test
    public void testDelete() throws SQLException {
        Connection connectionMock = Mockito.mock(Connection.class);
        PreparedStatement statementMock = Mockito.mock(PreparedStatement.class);
        int lotId = 1;

        when(connectionMock.prepareStatement(anyString())).thenReturn(statementMock);

        LotDAO lotDAO = new LotDAO(connectionMock);

        lotDAO.delete(lotId);

        verify(statementMock, times(1)).executeUpdate();
    }
}