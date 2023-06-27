package com.example.onlineauction.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BidTest {

    @Test
    public void testGettersAndSetters() {

        Bid bid = new Bid(1, 2, 3, 100.0);

        assertEquals(1, bid.getId());
        assertEquals(2, bid.getLotId());
        assertEquals(3, bid.getBuyerId());
        assertEquals(100.0, bid.getBidAmount(), 0.0);

        bid.setId(4);
        bid.setLotId(5);
        bid.setBuyerId(6);
        bid.setBidAmount(200.0);

        assertEquals(4, bid.getId());
        assertEquals(5, bid.getLotId());
        assertEquals(6, bid.getBuyerId());
        assertEquals(200.0, bid.getBidAmount(), 0.0);
    }
}