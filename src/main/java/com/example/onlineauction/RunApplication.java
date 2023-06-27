package com.example.onlineauction;

import javafx.application.Application;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class RunApplication extends Application {
    private static final Logger LOGGER = LogManager.getLogger();
    @Override
    public void start(Stage stage)  {
        LOGGER.log(Level.INFO, "Приложение запущено");
        WindowsManager.openWindow("AllUsers/authorization.fxml", "Окно авторизации");
    }

    public static void main(String[] args) {
        launch();
    }
}