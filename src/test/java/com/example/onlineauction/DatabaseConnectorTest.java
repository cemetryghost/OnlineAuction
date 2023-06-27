package com.example.onlineauction;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectorTest {

    @Test
    public void testConnectDb() {
        try {
            Connection connection = DatabaseConnector.ConnectDb();
            Assertions.assertNotNull(connection);
            connection.close(); // Закрытие соединения после теста
        } catch (Exception e) {
            Assertions.fail("Exception occurred: " + e.getMessage());
        }
    }

}