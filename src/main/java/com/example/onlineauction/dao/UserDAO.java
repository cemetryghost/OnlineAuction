package com.example.onlineauction.dao;

import com.example.onlineauction.LogManager;
import com.example.onlineauction.constants.Role;
import com.example.onlineauction.constants.Status;
import com.example.onlineauction.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDAO {
    private Connection connection;

    private static final Logger LOGGER = LogManager.getLogger();
    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public UserDAO() {
    }

    public void saveUser(User user) throws SQLException {
        String query = "INSERT INTO users (name, surname, login, password, birth_date, role, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            statement.setString(3, user.getLogin());
            statement.setString(4, user.getPassword());
            statement.setDate(5, java.sql.Date.valueOf(user.getBirth_date()));
            statement.setString(6, user.getRole().toString());
            statement.setString(7, user.getStatus().toString());

            if (isUserExist(user.getLogin())) {
                throw new IllegalArgumentException("Пользователь с таким логином уже существует!");
            }

            statement.executeUpdate();
            LOGGER.log(Level.INFO, "Пользователь " + user.getLogin() + " успешно сохранен в базе данных");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при сохранении пользователя " + user.getLogin() + ": " + e.getMessage(), e);
            throw e;
        }
    }

    public boolean isUserExist(String login) throws SQLException {
        String query = "SELECT COUNT(*) FROM users WHERE login = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при проверке существования пользователя с логином " + login + ": " + e.getMessage(), e);
            throw e;
        }
        return false;
    }

    public List<User> getAllUsers() throws SQLException {
        String query = "SELECT * FROM users";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = createUserFromResultSet(resultSet);
                users.add(user);
            }
            LOGGER.log(Level.INFO, "Получено " + users.size() + " пользователей из базы данных.");
            return users;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении списка пользователей: " + e.getMessage(), e);
            throw e;
        }
    }

    public int getIdByLogin(String login) {
        String query = "SELECT idusers FROM users WHERE login = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int id = resultSet.getInt("idusers");
                    LOGGER.log(Level.INFO, "Получен ID пользователя с логином " + login + ": " + id);
                    return id;
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении ID пользователя с логином " + login + ": " + e.getMessage(), e);
        }
        return 0;
    }

    public User getUserByLogin(String login) throws SQLException {
        String query = "SELECT * FROM users WHERE login = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, login);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = createUserFromResultSet(resultSet);
                    LOGGER.log(Level.INFO, "Получен пользователь с логином " + login);
                    return user;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении пользователя с логином " + login + ": " + e.getMessage(), e);
            throw e;
        }
        return null;
    }

    public void blockUser(int userId) throws SQLException {
        String query = "UPDATE users SET status = 'BLOCK' WHERE idusers = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
            LOGGER.log(Level.INFO, "Пользователь с ID " + userId + " заблокирован успешно.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при блокировке пользователя с ID " + userId + ": " + e.getMessage(), e);
            throw e;
        }
    }

    public void unblockUser(int userId) throws SQLException {
        String query = "UPDATE users SET status = 'ACTIVE' WHERE idusers = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
            LOGGER.log(Level.INFO, "Пользователь с ID " + userId + " разблокирован успешно.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при разблокировке пользователя с ID " + userId + ": " + e.getMessage(), e);
            throw e;
        }
    }

    public void deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM users WHERE idusers = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
            LOGGER.log(Level.INFO, "Пользователь с ID " + userId + " удален успешно.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при удалении пользователя с ID " + userId + ": " + e.getMessage(), e);
            throw e;
        }
    }

    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("idusers");
        String name = resultSet.getString("name");
        String surname = resultSet.getString("surname");
        String login = resultSet.getString("login");
        String password = resultSet.getString("password");
        LocalDate birthDate = resultSet.getDate("birth_date").toLocalDate();
        Role role = Role.valueOf(resultSet.getString("role"));
        Status status = Status.valueOf(resultSet.getString("status"));
        User user = new User(name, surname, login, password, birthDate, role, status);
        user.setId(id);
        LOGGER.log(Level.INFO, "Создан объект User из ResultSet для пользователя с ID " + id);
        return user;
    }

    public Role getUserRole(String username, String password) throws SQLException {
        String query = "SELECT role FROM users WHERE login = ? AND password = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String roleString = resultSet.getString("role");
                    return Role.valueOf(roleString.toUpperCase());
                }
            }
            LOGGER.log(Level.INFO, "Получена роль пользователя " + username);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении роли пользователя " + username + ": " + e.getMessage(), e);
            throw e;
        }
        return null;
    }

    public String getNameAndSurnameById(int id) throws Exception {
        String result = "";
        String query = "SELECT name, surname FROM users WHERE idusers = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result = String.format("%s %s", resultSet.getString("name"), resultSet.getString("surname"));
                }
                LOGGER.log(Level.INFO, "Получено имя и фамилия пользователя с ID " + id);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении имени и фамилии пользователя с ID " + id + ": " + e.getMessage(), e);
            throw e;
        }
        return result;
    }
}