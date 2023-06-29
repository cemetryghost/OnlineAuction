package com.example.onlineauction.controller.authentication;

import com.example.onlineauction.constants.Role;
import com.example.onlineauction.constants.Status;
import com.example.onlineauction.controller.ManagementProductsController;
import com.example.onlineauction.controller.WindowsManager;
import com.example.onlineauction.dao.DatabaseConnector;
import com.example.onlineauction.dao.UserDAO;
import com.example.onlineauction.model.User;
import com.example.onlineauction.util.AlertUtil;
import com.example.onlineauction.util.LogManager;
import com.jfoenix.controls.JFXRadioButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.stage.Stage;


public class RegistrationController {

    @FXML private JFXRadioButton buyerRadioButton;
    @FXML private DatePicker dateOfBirthField;
    @FXML private Button exitButtonRegistration;
    @FXML private TextField loginUserFieldReg;
    @FXML private TextField nameUserField;
    @FXML private PasswordField passwordUserFieldReg;
    @FXML private Button registrationButton;
    @FXML private JFXRadioButton sellerRadioButton;
    @FXML private TextField surnameUserField;
    public static int registeredUserId;
    private RegistrationController registrationController;

    private static final Logger LOGGER = LogManager.getLogger();
    public static boolean isRegistred;

    @FXML
    void initialize() {
        buyerRadioButton.setSelected(false);
        sellerRadioButton.setSelected(false);
    }

    @FXML
    void ChoiceBuyer(ActionEvent event) {
        buyerRadioButton.setSelected(true);
        sellerRadioButton.setSelected(false);
    }

    @FXML
    void ChoiceSeller(ActionEvent event) {
        sellerRadioButton.setSelected(true);
        buyerRadioButton.setSelected(false);
    }

    @FXML
    void ExitRegistration(ActionEvent event) {
        System.exit(1);
    }

    @FXML
    void Registration(ActionEvent event) throws IOException, SQLException {
        String firstName = nameUserField.getText();
        String lastName = surnameUserField.getText();
        String username = loginUserFieldReg.getText();
        String password = passwordUserFieldReg.getText();
        LocalDate dateOfBirth = dateOfBirthField.getValue();

        if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() || password.isEmpty() || dateOfBirth == null) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Заполните все поля");
            LOGGER.log(Level.WARNING, "Попытка регистрации с незаполненными полями.");
            return;
        }

        LocalDate currentDate = LocalDate.now();

        if (dateOfBirth.isAfter(currentDate.minusYears(18))) {
            showAlert(Alert.AlertType.ERROR, "Ошибка!", "Принимать участия в аукционах можно только с 18 лет!");
            LOGGER.log(Level.WARNING, "Попытка регистрации лица моложе 18 лет. Логин: " + username);
            return;
        }

        Role role;
        if (buyerRadioButton.isSelected()) {
            role = Role.BUYER;
        } else if (sellerRadioButton.isSelected()) {
            role = Role.SELLER;
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Пожалуйста, выберите роль!");
            LOGGER.log(Level.WARNING, "Попытка регистрации без выбора роли. Логин: " + username);
            return;
        }

        User newUser = new User(firstName, lastName, username, password, dateOfBirth, role, Status.ACTIVE);

        Connection connection = null;
        try {
            connection = DatabaseConnector.ConnectDb();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка соединения с базой данных!");
            LOGGER.log(Level.SEVERE, "Ошибка соединения с базой данных.", e);
            return;
        }

        UserDAO userDAO = new UserDAO(connection);

        try {
            if (userDAO.isUserExist(username)) {
                showAlert(Alert.AlertType.ERROR, "Ошибка регистрации", "Аккаунт с таким логином уже существует!");
                LOGGER.log(Level.WARNING, "Попытка регистрации существующего логина. Логин: " + username);
                return;
            }

            userDAO.saveUser(newUser);

            isRegistred = true;

            registeredUserId = userDAO.getIdByLogin(username);
            ManagementProductsController.sellerId = registeredUserId;

            clearInputFields();

            showAlert(Alert.AlertType.INFORMATION, "Успешно", "Аккаунт зарегистрирован");

            if (role == Role.BUYER) {
                Stage stageClose = (Stage) registrationButton.getScene().getWindow();
                stageClose.close();

                WindowsManager.openWindow("/com/example/onlineauction/buyer/buyer-view.fxml", "Окно покупателя");
            } else if (role == Role.SELLER) {
                Stage stageClose = (Stage) registrationButton.getScene().getWindow();
                stageClose.close();

                WindowsManager.openWindow("/com/example/onlineauction/seller/seller-view.fxml", "Окно продавца");
            }

            LOGGER.log(Level.INFO, "Успешная регистрация. Логин: " + username + ", Роль: " + role);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка регистрации: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Ошибка при выполнении SQL-запроса.", e);
        }
    }

    private void clearInputFields() {
        nameUserField.clear();
        surnameUserField.clear();
        loginUserFieldReg.clear();
        passwordUserFieldReg.clear();
        dateOfBirthField.setValue(null);
    }

    @FXML
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        AlertUtil.showAlert(alertType, title, message);
    }
}
