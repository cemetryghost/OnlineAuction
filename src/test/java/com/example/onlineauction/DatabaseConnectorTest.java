package com.example.onlineauction;

import com.example.onlineauction.dao.DatabaseConnector;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

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