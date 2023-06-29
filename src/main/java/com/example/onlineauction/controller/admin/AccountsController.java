package com.example.onlineauction.controller.admin;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.onlineauction.dao.DatabaseConnector;
import com.example.onlineauction.util.LogManager;
import com.example.onlineauction.constants.Role;
import com.example.onlineauction.constants.Status;
import com.example.onlineauction.model.User;
import com.example.onlineauction.dao.UserDAO;
import com.example.onlineauction.util.СonfirmationDialog;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

import static com.example.onlineauction.util.AlertUtil.showAlert;

public class AccountsController {

    @FXML private ResourceBundle resources;
    @FXML private Label dateTimeLabel;
    @FXML private URL location;
    @FXML private AnchorPane AnchorPaneAccountsAdmin;
    @FXML private TableView<User> TableViewAccounts;
    @FXML private Button blockAccountsButton, deleteAccountsButton, unblockAccountsButton;
    @FXML private TableColumn<User, String> col_loginUser, col_roleUser, col_nameUser, col_passwordUser, col_statusUser, col_surnameUser;
    private UserDAO userDAO;

    private static final Logger LOGGER = LogManager.getLogger();

    private ObservableList<User> usersList;
    @FXML
    void BlockAccounts(ActionEvent event) {
        LOGGER.log(Level.INFO, "Метод BlockAccounts вызван");

        User selectedUser = TableViewAccounts.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            if (!selectedUser.getLogin().equals("admin")) {
                if (СonfirmationDialog.showConfirmationDialog(
                        "Подтверждение",
                        "Подтверждение блокировки",
                        "Вы уверены, что хотите заблокировать аккаунт?",
                        "Заблокировать",
                        "Отмена"
                )) {
                    try {
                        showAlert(Alert.AlertType.CONFIRMATION, "Успешно", "Аккаунт заблокирован");
                        userDAO.blockUser(selectedUser.getId());
                        refreshUserList();
                        LOGGER.log(Level.INFO, "Аккаунт успешно заблокирован: " + selectedUser.getLogin());
                    } catch (SQLException e) {
                        LOGGER.log(Level.SEVERE, "Ошибка при блокировке аккаунта", e);
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка при блокировке аккаунта!");
                    }
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Предупреждение", "Блокировка аккаунта администратора невозможна!");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Выберите аккаунт для блокировки!");
        }
    }

    @FXML
    void DeleteAccounts(ActionEvent event) {
        LOGGER.log(Level.INFO, "Метод DeleteAccounts вызван");

        User selectedUser = TableViewAccounts.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            if (!selectedUser.getLogin().equals("admin")) {
                if (СonfirmationDialog.showConfirmationDialog(
                        "Предупреждение",
                        "Предупреждение!",
                        "Будут удалены все данные, связанные с аккаунтом, продолжить?",
                        "Удалить аккаунт",
                        "Отмена"
                )) {
                    try {
                        userDAO.deleteUser(selectedUser.getId());
                        showAlert(Alert.AlertType.INFORMATION, "Информация", "Аккаунт был успешно удален!");
                        refreshUserList();
                        LOGGER.log(Level.INFO, "Аккаунт успешно удален: " + selectedUser.getLogin());
                    } catch (SQLException e) {
                        LOGGER.log(Level.SEVERE, "Ошибка при удалении аккаунта", e);
                        e.printStackTrace();
                        showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка при удалении аккаунта!");
                    }
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Предупреждение", "Удаление данного аккаунта запрещено!");
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Выберите аккаунт для удаления!");
        }
    }

    @FXML
    void UnblockAccounts(ActionEvent event) {
        LOGGER.log(Level.INFO, "Метод UnblockAccounts вызван");

        User selectedUser = TableViewAccounts.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            if (СonfirmationDialog.showConfirmationDialog(
                    "Подтверждение",
                    "Подтверждение разблокировки",
                    "Вы уверены, что хотите разблокировать аккаунт?",
                    "Разблокировать",
                    "Отмена"
            )) {
                try {
                    showAlert(Alert.AlertType.CONFIRMATION, "Успешно", "Аккаунт разблокирован");
                    userDAO.unblockUser(selectedUser.getId());
                    refreshUserList();
                    LOGGER.log(Level.INFO, "Аккаунт успешно разблокирован: " + selectedUser.getLogin());
                } catch (SQLException e) {
                    LOGGER.log(Level.SEVERE, "Ошибка при разблокировке аккаунта", e);
                    e.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка при разблокировке аккаунта!");
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Выберите аккаунт для разблокировки!");
        }
    }

    @FXML
    void initialize() throws Exception {
        LOGGER.log(Level.INFO, "Метод initialize вызван");

        Connection connection = DatabaseConnector.ConnectDb();
        userDAO = new UserDAO(connection);

        try {
            List<User> userList = userDAO.getAllUsers();

            col_loginUser.setCellValueFactory(new PropertyValueFactory<>("login"));
            col_nameUser.setCellValueFactory(new PropertyValueFactory<>("name"));
            col_surnameUser.setCellValueFactory(new PropertyValueFactory<>("surname"));
            col_passwordUser.setCellValueFactory(new PropertyValueFactory<>("password"));

            col_statusUser.setCellValueFactory(cellData -> {
                Status status = cellData.getValue().getStatus();
                String russianStatus = getStatusRussian(String.valueOf(status));
                return new SimpleStringProperty(russianStatus);
            });

            col_roleUser.setCellValueFactory(cellData -> {
                Role role = cellData.getValue().getRole();
                String russianRole = getRoleRussian(String.valueOf(role));
                return new SimpleStringProperty(russianRole);
            });

            usersList = FXCollections.observableArrayList(userList);

            TableViewAccounts.setItems(usersList);

            LOGGER.log(Level.INFO, "Список аккаунтов успешно загружен");

            refreshUserList();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при инициализации", e);
            e.printStackTrace();
        }
    }

    private String getRoleRussian(String role) {
        if (role.equals("ADMIN")) {
            return "Админ";
        } else if (role.equals("BUYER")) {
            return "Покупатель";
        } else if (role.equals("SELLER")) {
            return "Продавец";
        } else {
            return "Неизвестная роль";
        }
    }

    private String getStatusRussian(String status) {
        if (status.equals("ACTIVE")) {
            return "Активный";
        } else if (status.equals("BLOCK")) {
            return "Заблокирован";
        } else {
            return "Неизвестный статус";
        }
    }

    private void refreshUserList() {
        try {
            List<User> userList = userDAO.getAllUsers();
            usersList.clear();
            for (User user : userList) {
                if (!user.getLogin().equals("unknown")) {
                    usersList.add(user);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при обновлении списка пользователей", e);
            e.printStackTrace();
        }
    }
}
