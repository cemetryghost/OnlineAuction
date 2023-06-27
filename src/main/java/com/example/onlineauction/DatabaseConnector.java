package com.example.onlineauction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseConnector {
    private static final Logger LOGGER = LogManager.getLogger();
    public static Connection ConnectDb() throws Exception {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/auction", "root", "6778");
            LOGGER.log(Level.INFO, "Установлено соединение с базой данных");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при подключении к базе данных: " + e.getMessage(), e);
            throw e;
        }
        return connection;
    }

}
