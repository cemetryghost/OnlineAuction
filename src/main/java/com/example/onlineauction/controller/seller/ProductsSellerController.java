package com.example.onlineauction.controller.seller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import com.example.onlineauction.DatabaseConnector;
import com.example.onlineauction.LogManager;
import com.example.onlineauction.WindowsManager;
import com.example.onlineauction.constants.Role;
import com.example.onlineauction.constants.StatusLot;
import com.example.onlineauction.controller.DetailProductsController;
import com.example.onlineauction.controller.ManagementProductsController;
import com.example.onlineauction.controller.admin.AccountsController;
import com.example.onlineauction.controller.authentication.AuthorizationController;
import com.example.onlineauction.controller.authentication.RegistrationController;
import com.example.onlineauction.dao.BidDAO;
import com.example.onlineauction.util.СonfirmationDialog;
import com.example.onlineauction.dao.LotDAO;
import com.example.onlineauction.model.Bid;
import com.example.onlineauction.model.Lot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static com.example.onlineauction.util.AlertUtil.showAlert;

public class ProductsSellerController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private AnchorPane AnchorPaneLotsSeller;
    @FXML private Button addLotsSeller, deleteLotsSeller, editLotsSeller, finishLotsSeller, moreDetailLots;
    @FXML private TableColumn<Lot, String> col_categoryLotsSeller, col_nameLotsSeller, col_statusLotsSeller;
    @FXML private TableColumn<Lot, Double> col_currentPriceLotsSeller, col_startPriceLotsSeller;
    @FXML private TableView<Lot> tableViewLotsSeller;

    public static Lot lot;
    LotDAO lotDAO = new LotDAO(DatabaseConnector.ConnectDb());
    public static boolean booleanAdd = false;
    public static boolean booleanEdit = false;
    public static boolean isAdmin = false;
    public static List<Lot> closeLots;
    private int id;
    public static int userId;
    BidDAO bidDAO = new BidDAO(DatabaseConnector.ConnectDb());

    private static final Logger LOGGER = LogManager.getLogger();

    public ProductsSellerController() throws Exception {
    }

    @FXML
    void AddLotsSeller(ActionEvent event) throws Exception{
        Stage stageClose = (Stage) addLotsSeller.getScene().getWindow();
        stageClose.close();
        ProductsSellerController.lot = null;
        WindowsManager.openWindow("/com/example/onlineauction/AllUsers/add-edit-products.fxml","Добавление лота");
        booleanAdd = true;
    }


    @FXML
    void DeleteLotsSeller(ActionEvent event) throws Exception {
        LOGGER.info("Вызван метод DeleteLotsSeller");
        Lot selectedLot = tableViewLotsSeller.getSelectionModel().getSelectedItem();
        if (selectedLot != null) {
            if (СonfirmationDialog.showConfirmationDialog(
                    "Предупреждение",
                    "Удаление лота",
                    "Действительно удалить выбранный лот?",
                    "Удалить",
                    "Отмена"
            )) {
                if (hasValidBids(selectedLot)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Удаление запрещено! Покупатель сделал ставку!");
                    alert.showAndWait();
                    LOGGER.warning("Удаление лота запрещено. Покупатель сделал ставку.");
                    return;
                }

                int id = selectedLot.getId();
                lotDAO.delete(id);
                LOGGER.info("Лот успешно удален. ID лота: " + id);

                update();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Выберите лот!");
            alert.showAndWait();
            LOGGER.warning("Не выбран лот для удаления.");
        }
    }

    @FXML
    void EditLotsSeller(ActionEvent event) throws SQLException {
        LOGGER.info("Вызван метод EditLotsSeller");
        Lot selectedLot = tableViewLotsSeller.getSelectionModel().getSelectedItem();
        if (selectedLot != null) {
            if (СonfirmationDialog.showConfirmationDialog(
                    "Предупреждение",
                    "Редактирование лота",
                    "Продолжить редактирование выбранного лота?",
                    "Редактировать",
                    "Отмена"
            )) {
                if (hasValidBids(selectedLot)) {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Редактирование запрещено! Покупатель сделал ставку!");
                    alert.showAndWait();
                    LOGGER.warning("Редактирование лота запрещено. Покупатель сделал ставку.");
                    return;
                }
                Stage closeStage = (Stage) editLotsSeller.getScene().getWindow();
                closeStage.close();

                WindowsManager.openWindow("AllUsers/add-edit-products.fxml", "Редактирование лота");
                booleanEdit = true;
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Выберите лот!");
            alert.showAndWait();
            LOGGER.warning("Не выбран лот для редактирования.");
        }
    }
    public boolean hasValidBids(Lot lot) throws SQLException {
        List<Bid> bids = bidDAO.getBidsByLotId(lot.getId());

        for (Bid bid : bids) {
            if (bid.getBuyerId() != 49) {
                return true;
            }
        }
        return false;
    }

    @FXML
    void FinishLotsSeller(ActionEvent event) throws Exception {
        Lot selectedLot = tableViewLotsSeller.getSelectionModel().getSelectedItem();
        if (selectedLot != null) {
            int id = selectedLot.getId();
            lotDAO = new LotDAO(DatabaseConnector.ConnectDb());
            lotDAO.updateLotStatus(id, StatusLot.COMPLETED);
            update();
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка!", "Выберите лот из таблицы!");
        }
    }

    @FXML
    void MoreInfoDetailsLots(ActionEvent event) throws IOException {
        Lot selectedItem = tableViewLotsSeller.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/onlineauction/AllUsers/details-products.fxml"));
            Parent root = loader.load();

            DetailProductsController detailProductsController = loader.getController();
            detailProductsController.setRole(Role.SELLER);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Детали лота");
            stage.show();
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Выберите лот!");
        }
    }

    @FXML
    void initialize() throws Exception {
        update();
    }

    public void getSelected() throws Exception {
        lot = tableViewLotsSeller.getSelectionModel().getSelectedItem();
        lot = lotDAO.getLotById(lot.getId());
        id = lot.getId();
    }

    public void update() throws Exception{
        Connection connection = DatabaseConnector.ConnectDb();
        LotDAO lotDAO = new LotDAO(connection);
        List<Lot> lotus = new ArrayList<>();
        closeLots = new ArrayList<>();
        if(AuthorizationController.userId != 0){
            List<Lot> temp = lotDAO.getLotsBySellerId(AuthorizationController.userId);
            for(Lot lot : temp){
                if(!lot.getStatusString().equals("Завершен")){
                    lotus.add(lot);
                }
                else{
                    closeLots.add(lot);
                }
            }
        }
        else if(RegistrationController.registeredUserId != 0){
            List<Lot> temp = lotDAO.getLotsBySellerId(RegistrationController.registeredUserId);
            for(Lot lot : temp){
                if(!lot.getStatusString().equals("Завершен")){
                    lotus.add(lot);
                }
                else{
                    closeLots.add(lot);
                }
            }
        }
        ObservableList<Lot> lots = FXCollections.observableArrayList(lotus);

        col_categoryLotsSeller.setCellValueFactory(new PropertyValueFactory<>("category"));
        col_currentPriceLotsSeller.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
        col_nameLotsSeller.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_startPriceLotsSeller.setCellValueFactory(new PropertyValueFactory<>("startPrice"));
        col_statusLotsSeller.setCellValueFactory(new PropertyValueFactory<>("statusString"));

        tableViewLotsSeller.setItems(lots);
        tableViewLotsSeller.refresh();
    }
}