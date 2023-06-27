package com.example.onlineauction.controller.admin;

import com.example.onlineauction.LogManager;
import com.example.onlineauction.dao.BidDAO;
import com.example.onlineauction.model.Category;
import com.example.onlineauction.dao.CategoryDAO;
import com.example.onlineauction.DatabaseConnector;
import com.example.onlineauction.util.СonfirmationDialog;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import static com.example.onlineauction.util.AlertUtil.showAlert;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CategoryAdminController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private AnchorPane AnchorPaneCategory;
    @FXML private TableView<Category> TableViewCategory;
    @FXML private Button addCategoryButton, deleteCategoryButton, editCategoryButton;
    @FXML private TableColumn<Category, String> col_nameCategory;
    @FXML private TextField nameCategoryField;

    private CategoryDAO categoryDAO;

    private ObservableList<Category> categoryList;

    private static final Logger LOGGER = LogManager.getLogger();

    @FXML
    void AddCategory(ActionEvent event) {
        String categoryName = nameCategoryField.getText();

        if (categoryName != null && !categoryName.isEmpty()) {
            if (isCategoryNameUnique(categoryName)) {
                Category category = new Category(0, categoryName);

                try {
                    categoryDAO.create(category);
                    showAlert(Alert.AlertType.CONFIRMATION, "Успешно", "Категория добавлена!");
                    LOGGER.log(Level.INFO, "Категория добавлена: {0}", category.getName());
                    nameCategoryField.clear();
                    loadCategories();
                } catch (SQLException e) {
                    e.printStackTrace();
                    LOGGER.log(Level.SEVERE, "Ошибка при добавлении категории: {0}", e.getMessage());
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Предупреждение", "Категория с таким именем уже существует!");
                LOGGER.log(Level.WARNING, "Попытка добавить категорию с уже существующим именем: {0}", categoryName);
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Введите имя категории!");
            LOGGER.log(Level.WARNING, "Попытка добавить категорию с пустым именем");
        }
    }

    private boolean isCategoryNameUnique(String categoryName) {
        try {
            List<Category> categories = categoryDAO.getAllCategoriesList();
            for (Category category : categories) {
                if (category.getName().equalsIgnoreCase(categoryName)) {
                    return false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Ошибка при проверке уникальности имени категории: {0}", e.getMessage());
        }
        return true;
    }

    @FXML
    void EditCategory(ActionEvent actionEvent) {
        Category selectedCategory = TableViewCategory.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            String newName = nameCategoryField.getText();
            if (newName != null && !newName.isEmpty()) {
                Category updatedCategory = new Category(selectedCategory.getId(), newName);
                try {
                    if (categoryDAO.isCategoryUsed(selectedCategory.getId())) {
                        showAlert(Alert.AlertType.WARNING, "Предупреждение", "Редактирование запрещено! Категория уже используется!");
                        LOGGER.log(Level.WARNING, "Попытка редактировать категорию, которая уже используется: {0}", selectedCategory.getName());
                        return;
                    }

                    if (СonfirmationDialog.showConfirmationDialog(
                            "Предупреждение",
                            "Предупреждение!",
                            "Вы уверены, что хотите отредактировать категорию?",
                            "Редактировать",
                            "Отмена"
                    )) {
                        selectedCategory.setName(newName);
                        categoryDAO.update(updatedCategory);
                        showAlert(Alert.AlertType.CONFIRMATION, "Успешно", "Категория отредактирована!");
                        LOGGER.log(Level.INFO, "Категория отредактирована: {0}", selectedCategory.getName());
                        TableViewCategory.refresh();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    LOGGER.log(Level.SEVERE, "Ошибка при редактировании категории: {0}", e.getMessage());
                }
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Выберите категорию для редактирования!");
            LOGGER.warning("Попытка редактировать категорию без выбора");
        }
    }

    @FXML
    void DeleteCategory(ActionEvent event) {
        Category selectedCategory = TableViewCategory.getSelectionModel().getSelectedItem();

        if (selectedCategory != null) {
            try {
                if (categoryDAO.isCategoryUsed(selectedCategory.getId())) {
                    showAlert(Alert.AlertType.WARNING, "Предупреждение", "Удаление запрещено! Категория уже используется!");
                    LOGGER.log(Level.WARNING, "Попытка удалить категорию, которая уже используется: {0}", selectedCategory.getName());
                    return;
                }
                if (СonfirmationDialog.showConfirmationDialog(
                        "Предупреждение",
                        "Предупреждение!",
                        "Все связанные данные будут удалены, продолжить?",
                        "Удалить",
                        "Отмена"
                )) {
                    categoryDAO.delete(selectedCategory.getId());
                    showAlert(Alert.AlertType.CONFIRMATION, "Успешно", "Категория удалена!");
                    LOGGER.log(Level.INFO, "Категория удалена: {0}", selectedCategory.getName());
                    loadCategories();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                LOGGER.log(Level.SEVERE, "Ошибка при удалении категории: {0}", e.getMessage());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Предупреждение","Выберите категорию для удаления!");
            LOGGER.warning("Попытка удалить категорию без выбора");
        }
    }

    @FXML
    void initialize() throws Exception {
        Connection connection = DatabaseConnector.ConnectDb();

        categoryDAO = new CategoryDAO(connection);

        col_nameCategory.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

        categoryList = FXCollections.observableArrayList();
        loadCategories();
        TableViewCategory.setItems(categoryList);

        TableViewCategory.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                nameCategoryField.setText(newValue.getName());
            }
        });
    }

    private void loadCategories() {
        try {
            List<Category> categories = categoryDAO.getAllCategoriesList();
            categoryList.clear();
            categoryList.addAll(categories);
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, "Ошибка при загрузке категорий: {0}", e.getMessage());
        }
    }
}
