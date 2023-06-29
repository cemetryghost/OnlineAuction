package com.example.onlineauction.controller.admin;

import com.example.onlineauction.dao.DatabaseConnector;
import com.example.onlineauction.util.LogManager;
import com.example.onlineauction.controller.WindowsManager;
import com.example.onlineauction.constants.StatusLot;
import com.example.onlineauction.controller.seller.ProductsSellerController;
import com.example.onlineauction.dao.LotDAO;
import com.example.onlineauction.dao.UserDAO;
import com.example.onlineauction.model.Lot;
import com.example.onlineauction.util.СonfirmationDialog;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.example.onlineauction.util.AlertUtil.showAlert;

public class ProductsAdminController implements Initializable {

    @FXML private AnchorPane AnchorPaneLotsAdmin;
    @FXML private TableView<Lot> TableViewAdminLots;
    @FXML private TableColumn<Lot, String> col_categoryLotsAdmin, col_nameLotsAdmin, col_statusLotsAdmin;
    @FXML private TableColumn<Lot, Double> col_currentPriceLotsAdmin, col_startPriceLotsAdmin;
    @FXML private Button deleteLotsButton, editLotsButton, editStatusLotsButton, moreDetailLots;
    @FXML private ComboBox<String> statusLotsComboBox;

    public static Lot lot;
    private int id;
    private LotDAO lotDAO;
    Connection connection = DatabaseConnector.ConnectDb();

    private static final Logger LOGGER = LogManager.getLogger();
    public ProductsAdminController() throws Exception {
    }

    @FXML
    void DeleteLots(ActionEvent event) throws Exception {
        Lot selectedLot = TableViewAdminLots.getSelectionModel().getSelectedItem();
        if (selectedLot != null) {
            if (СonfirmationDialog.showConfirmationDialog(
                    "Предупреждение",
                    "Предупреждение!",
                    "Данные, связанные с лотом будут удалены, продолжить?",
                    "Удалить лот",
                    "Отмена"
            )) {
                int id = selectedLot.getId();
                lotDAO = new LotDAO(connection);
                try {
                    lotDAO.delete(id);
                    showAlert(Alert.AlertType.INFORMATION, "Успешно!", "Лот был успешно удален!");
                    LOGGER.log(Level.INFO, "Лот успешно удален. ID лота: " + id);
                } catch (SQLException e) {
                    showAlert(Alert.AlertType.ERROR, "Ошибка!", "Ошибка при удалении лота.");
                    LOGGER.log(Level.SEVERE, "Ошибка при удалении лота. ID лота: " + id, e);
                }
                update();
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка!", "Выберите лот из таблицы!");
        }
    }

    @FXML
    void EditLots(ActionEvent event) throws Exception {
        Lot selectedLot = TableViewAdminLots.getSelectionModel().getSelectedItem();
        if (selectedLot != null) {
            ProductsSellerController sellerController = new ProductsSellerController();
            try {
                if (sellerController.hasValidBids(selectedLot)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Редактирование запрещено! Покупатель сделал ставку!");
                    alert.showAndWait();
                    LOGGER.log(Level.WARNING, "Попытка редактирования лота с действующими ставками. ID лота: " + selectedLot.getId());
                    return;
                }
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Ошибка при проверке действующих ставок.", e);
                e.printStackTrace();
            }

            if (СonfirmationDialog.showConfirmationDialog(
                    "Предупреждение",
                    "Предупреждение!",
                    "Данные, связанные с лотом будут изменены, продолжить?",
                    "Редактировать лот",
                    "Отмена"
            )) {
                Stage closeStage = (Stage) editLotsButton.getScene().getWindow();
                closeStage.close();

                WindowsManager.openWindow("/com/example/onlineauction/AllUsers/add-edit-products.fxml", "Редактирование лота");
                LOGGER.log(Level.INFO, "Открыто окно редактирования лота. ID лота: " + selectedLot.getId());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка!", "Выберите лот из таблицы!");
            LOGGER.log(Level.WARNING, "Попытка редактирования лота без выбора из таблицы.");
        }
    }

    @FXML
    void EditStatusLots(ActionEvent event) throws Exception {
        lotDAO = new LotDAO(connection);
        String newStatus = statusLotsComboBox.getSelectionModel().getSelectedItem();
        Lot selectedLot = TableViewAdminLots.getSelectionModel().getSelectedItem();
        if (selectedLot != null) {
            if (newStatus != null) {
                Lot lot = lotDAO.getLotById(selectedLot.getId());
                if (newStatus.equals("Ожидает подтверждения")) {
                    lotDAO.updateLotStatus(lot.getId(), StatusLot.AWAITING_CONFIRMATION);
                } else if (newStatus.equals("Завершен")) {
                    lotDAO.updateLotStatus(lot.getId(), StatusLot.COMPLETED);
                } else if (newStatus.equals("Активный")) {
                    lotDAO.updateLotStatus(lot.getId(), StatusLot.ACTIVE);
                }
                showAlert(Alert.AlertType.INFORMATION, "Успешно!", "Статус лота изменен!");
                update();
                LOGGER.log(Level.INFO, "Статус лота успешно изменен. ID лота: " + lot.getId() + ", Новый статус: " + newStatus);
            } else {
                showAlert(Alert.AlertType.ERROR, "Ошибка!", "Выберите статус для лота!");
                LOGGER.log(Level.WARNING, "Попытка изменения статуса лота без выбора статуса. ID лота: " + selectedLot.getId());
            }
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка!", "Выберите лот из таблицы!");
            LOGGER.log(Level.WARNING, "Попытка изменения статуса лота без выбора лота из таблицы.");
        }
    }



    @FXML
    void MoreInfoDetailsLots(ActionEvent event) {
        Lot selectedLot = TableViewAdminLots.getSelectionModel().getSelectedItem();
        if (selectedLot != null) {
            WindowsManager.openWindow("/com/example/onlineauction/administrator/admin-details.fxml","Детали лота");
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка!", "Выберите лот из таблицы!");
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        update();
    }

    public void update(){

        Connection connection = null;
        try {
            connection = DatabaseConnector.ConnectDb();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        LotDAO lotDAO = new LotDAO(connection);
        UserDAO userDAO = new UserDAO(connection);

        col_nameLotsAdmin.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_categoryLotsAdmin.setCellValueFactory(new PropertyValueFactory<>("category"));
        col_startPriceLotsAdmin.setCellValueFactory(new PropertyValueFactory<>("startPrice"));
        col_currentPriceLotsAdmin.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
        col_statusLotsAdmin.setCellValueFactory(new PropertyValueFactory<>("statusString"));

        List<Lot> lots = null;
        ObservableList<String> status = FXCollections.observableArrayList("Ожидает подтверждения", "Активный", "Завершен");
        ObservableList<Lot> lotus = FXCollections.observableArrayList();
        try {
            lots = lotDAO.getAllLots();
            for(Lot lot : lots){
                lotus.add(lot);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        TableViewAdminLots.setItems(lotus);
        statusLotsComboBox.setItems(status);
    }

    public void getSelected(MouseEvent mouseEvent) {
        lot = TableViewAdminLots.getSelectionModel().getSelectedItem();
        id = lot.getId();
        ProductsSellerController.lot = lot;
        ProductsSellerController.booleanEdit = true;
        ProductsSellerController.isAdmin = true;
    }
}

