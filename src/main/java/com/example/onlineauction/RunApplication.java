package com.example.onlineauction;

import com.example.onlineauction.controller.WindowsManager;
import com.example.onlineauction.util.LogManager;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RunApplication extends Application {
    private static final Logger LOGGER = LogManager.getLogger();
    @Override
    public void start(Stage stage)  {
        LOGGER.log(Level.INFO, "Приложение запущено");
        WindowsManager.openWindow("/com/example/onlineauction/AllUsers/authorization.fxml", "Окно авторизации");
    }

    public static void main(String[] args) {
        launch();
    }
}