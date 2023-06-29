package com.example.onlineauction.controller.authentication;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

import com.example.onlineauction.constants.Role;
import com.example.onlineauction.constants.Status;
import com.example.onlineauction.controller.WindowsManager;
import com.example.onlineauction.dao.DatabaseConnector;
import com.example.onlineauction.dao.UserDAO;
import com.example.onlineauction.model.User;
import com.example.onlineauction.util.LogManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import static com.example.onlineauction.util.AlertUtil.showAlert;
import java.util.logging.Level;


public class AuthorizationController {

    @FXML private Button authorizationButtonUser, exitButtonAuthorization, goToRegistrationButton;
    @FXML private TextField loginUserFieldAuth;
    @FXML private PasswordField passwordUserFieldAuth;

    private static final Logger LOGGER = LogManager.getLogger();

    public static int userId;
    public static String login;
    public static String password;

    @FXML
    void Authorization(ActionEvent event) throws Exception {
        login = loginUserFieldAuth.getText();
        password = passwordUserFieldAuth.getText();

        if (login.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Пожалуйста, заполните все поля!");
            LOGGER.log(Level.WARNING, "Попытка авторизации с незаполненными полями.");
            return;
        }

        Connection connection = DatabaseConnector.ConnectDb();
        UserDAO userDAO = new UserDAO(connection);

        try {
            Role userRole = userDAO.getUserRole(login, password);
            if (userRole != null) {
                User user = userDAO.getUserByLogin(login);
                if (user != null) {
                    if (user.getStatus() == Status.BLOCK) {
                        showAlert(Alert.AlertType.ERROR, "Ошибка!",
                                "Ваш аккаунт заблокирован! Свяжитесь с администратором!");
                        LOGGER.log(Level.WARNING, "Попытка авторизации с заблокированным аккаунтом. Логин: " + login);
                        return;
                    }

                    String fxmlPath;
                    String title;
                    switch (userRole) {
                        case BUYER:
                            fxmlPath = "/com/example/onlineauction/buyer/buyer-view.fxml";
                            title = "Окно покупателя";
                            break;
                        case SELLER:
                            fxmlPath = "/com/example/onlineauction/seller/seller-view.fxml";
                            title = "Окно продавца";
                            break;
                        case ADMIN:
                            fxmlPath = "/com/example/onlineauction/administrator/administrator-view.fxml";
                            title = "Окно администратора";
                            break;
                        default:
                            LOGGER.log(Level.WARNING, "Неизвестная роль пользователя: " + userRole);
                            return;
                    }
                    userId = user.getId();
                    WindowsManager.openWindow(fxmlPath, title);
                    Stage stageClose = (Stage) goToRegistrationButton.getScene().getWindow();
                    stageClose.close();
                    LOGGER.log(Level.INFO, "Успешная авторизация. Логин: " + login + ", Роль: " + userRole);
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Ошибка", "Неверный логин или пароль!");
                LOGGER.log(Level.WARNING, "Попытка авторизации с неверными логином или паролем. Логин: " + login);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при выполнении SQL-запроса.", e);
            e.printStackTrace();
        }
    }

    @FXML
    void ExitAuthorization(ActionEvent event) throws IOException {
        System.exit(1);
    }

    @FXML
    void GoToRegistraton(ActionEvent event) throws IOException {
        Stage stageCLose = (Stage) goToRegistrationButton.getScene().getWindow();
        stageCLose.close();
        WindowsManager.openWindow("/com/example/onlineauction/AllUsers/registration-view.fxml",
                "Окно регистрации");
        LOGGER.log(Level.INFO, "Открыто окно регистрации.");
    }
}