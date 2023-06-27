package com.example.onlineauction.dao;

import com.example.onlineauction.dao.BidDAO;
import com.example.onlineauction.model.Bid;
import com.example.onlineauction.model.Lot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class BidDAOTest {
    @Mock
    private Connection connectionMock;
    @Mock
    private PreparedStatement statementMock;
    @Mock
    private ResultSet resultSetMock;

    private BidDAO bidDAO;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        bidDAO = new BidDAO(connectionMock);
    }

    @Test
    public void testAddBid() throws Exception {
        Bid bid = new Bid(1, 1, 100.0);
        String query = "INSERT INTO bids (lot_id, buyer_id, bid_amount) VALUES (?, ?, ?)";

        when(connectionMock.prepareStatement(query)).thenReturn(statementMock);

        bidDAO.addBid(bid);

        verify(connectionMock).prepareStatement(query);
        verify(statementMock).setInt(1, 1);
        verify(statementMock).setInt(2, 1);
        verify(statementMock).setDouble(3, 100.0);
        verify(statementMock).executeUpdate();
    }
}
