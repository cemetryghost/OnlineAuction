package com.example.onlineauction.dao;

import com.example.onlineauction.DatabaseConnector;
import com.example.onlineauction.LogManager;
import com.example.onlineauction.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryDAO {

    private static final Logger LOGGER = LogManager.getLogger();
    private static Connection connection;

    static {
        try {
            connection = DatabaseConnector.ConnectDb();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public CategoryDAO(Connection connection) {
        this.connection = connection;
    }
    public CategoryDAO(){

    }

    public void create(Category category) throws SQLException {
        String query = "INSERT INTO category (name_category) VALUES (?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category.getName());
            statement.executeUpdate();
            LOGGER.info("Добавлена новая категория: " + category.toString());
        }
        catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Ошибка при добавлении категории: " + category.toString(), e);
            throw e;
        }
    }

    public void update(Category category) throws SQLException {
        String query = "UPDATE category SET name_category = ? WHERE idcategory = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category.getName());
            statement.setInt(2, category.getId());
            statement.executeUpdate();
            LOGGER.info("Обновление категория: " + category.toString());
        }
        catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Ошибка при обновлении категории: " + category.toString(), e);
            throw e;
        }
    }

    public void delete(int categoryId) throws SQLException {
        String query = "DELETE FROM category WHERE idcategory = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            statement.executeUpdate();

            LOGGER.info("Удалена категория с ID: " + categoryId);
        }
        catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Ошибка при удалении категории с ID: " + categoryId, e);
            throw e;
        }
    }

    public boolean isCategoryUsed(int categoryId) throws SQLException {
        String query = "SELECT COUNT(*) FROM lots WHERE category_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
            LOGGER.info("Категория с этим ID уже используется: " + categoryId);
        }
        catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Ошибка при проверке использования категории с ID: " + categoryId, e);
            throw e;
        }
        return false;
    }

    public List<Category> getAllCategoriesList() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String query = "SELECT * FROM category";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("idcategory");
                String name = resultSet.getString("name_category");

                Category category = new Category(id, name);
                categories.add(category);
            }
            LOGGER.info("Все категории: " + categories);
        }
        catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Ошибка при получении списка всех категорий", e);
            throw e;
        }
        return categories;
    }
    public ObservableList<Category> getAllCategoriesObservable() throws SQLException {
        ObservableList<Category> categories = FXCollections.observableArrayList();
        String query = "SELECT * FROM category";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int id = resultSet.getInt("idcategory");
                String name = resultSet.getString("name_category");

                Category category = new Category(id, name);
                categories.add(category);
            }
            LOGGER.info("Все категории (ObservableList): " + categories);
        }
        catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Ошибка при получении списка всех категорий (ObservableList)", e);
            throw e;
        }
        return categories;
    }

    public List<String> getAllStringCategories() throws SQLException {
        List<String> categories = new ArrayList<>();
        String query = "SELECT * FROM category";

        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String name = resultSet.getString("name_category");

                String category = name;
                categories.add(category);
            }
            LOGGER.info("Все категории (List<String>): " + categories);
        }
        catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Ошибка при получении списка всех категорий (List<String>)", e);
            throw e;
        }
        return categories;
    }

    public static String getCategoryById(int id) throws SQLException{
        String query = "SELECT name_category FROM category where idcategory= " + id;
        String result = "";
        try (PreparedStatement statement = connection.prepareStatement(query); ResultSet resultSet = statement.executeQuery()){
            while (resultSet.next()){
                result = resultSet.getString("name_category");
                return result;


            }
            LOGGER.info("ID полученной категории: " + id);
        }
        catch (SQLException e){
            LOGGER.log(Level.SEVERE, "Ошибка при получении категории по ID: " + id, e);
            throw e;
        }
        return result;
    }

    public static int getCategoryIdByString(String category) throws Exception{
        int result = 0;
        String query = "SELECT idcategory FROM category where name_category=?";

        try (PreparedStatement statement = connection.prepareStatement(query);){
            statement.setString(1, category);
            try(ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    result = resultSet.getInt("idcategory");
                }
            }
            LOGGER.info("ID полученной категории по названию: " + category);
        }
        catch (Exception e){
            LOGGER.log(Level.SEVERE, "Ошибка при получении ID категории по названию: " + category, e);
            throw e;
        }
        return result;
    }
}
