package com.example.onlineauction.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StatusLotTest {

    @Test
    public void getStatus() {
        assertEquals("Ожидает подтверждения", StatusLot.AWAITING_CONFIRMATION.getStatus());
        assertEquals("Активный", StatusLot.ACTIVE.getStatus());
        assertEquals("Завершен", StatusLot.COMPLETED.getStatus());
    }

    @Test
    public void testToString() {
        assertEquals("Ожидает подтверждения", StatusLot.AWAITING_CONFIRMATION.toString());
        assertEquals("Активный", StatusLot.ACTIVE.toString());
        assertEquals("Завершен", StatusLot.COMPLETED.toString());
    }
}