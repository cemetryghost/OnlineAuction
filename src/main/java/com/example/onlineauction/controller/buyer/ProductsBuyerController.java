package com.example.onlineauction.controller.buyer;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.onlineauction.dao.DatabaseConnector;
import com.example.onlineauction.util.LogManager;
import com.example.onlineauction.constants.StatusLot;
import com.example.onlineauction.controller.DetailProductsController;
import com.example.onlineauction.controller.authentication.AuthorizationController;
import com.example.onlineauction.controller.seller.ProductsSellerController;
import com.example.onlineauction.dao.BidDAO;
import com.example.onlineauction.dao.CategoryDAO;
import com.example.onlineauction.dao.LotDAO;
import com.example.onlineauction.model.Category;
import com.example.onlineauction.model.Lot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static com.example.onlineauction.util.AlertUtil.showAlert;

public class ProductsBuyerController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private AnchorPane AnchorPaneLotsBuyer;
    @FXML private TableView<Lot> TableViewLotsBuyer;
    @FXML private TableColumn<Lot, Double> col_betBuyer, col_currentPriceLotsBuyer, col_startPriceLotsBuyer;
    @FXML private TableColumn<Lot, String> col_endDateLots, col_nameLotsBuyer;
    @FXML private Button detailNBetButton;
    @FXML private ComboBox<String> selectCategoriesBuyer;

    private static final Logger LOGGER = LogManager.getLogger();

    private DetailProductsController detailProductsController;
    private CategoryDAO categoryDAO;
    private LotDAO lotDAO;
    Connection connection;
    public static ObservableList<Lot> lots;

    public void setDetailProductsController(DetailProductsController controller) {
        detailProductsController = controller;
    }

    @FXML
    void DetailNBet(ActionEvent event) throws IOException {
        Lot selectedItem = TableViewLotsBuyer.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/onlineauction/AllUsers/details-products.fxml"));
            Parent root = loader.load();

            detailProductsController = loader.getController();
            detailProductsController.setProductsBuyerController(this);

            Stage stageClose = (Stage) detailNBetButton.getScene().getWindow();
            stageClose.close();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Детали лота");
            stage.show();
        } else {
            showAlert(Alert.AlertType.WARNING, "Предупреждение!", "Выберите лот!");
            LOGGER.log(Level.WARNING, "Попытка открытия деталей лота без выбора лота.");
        }
    }

    @FXML
    void SelectCategories(ActionEvent event) throws Exception{
        try {
            categoryDAO = new CategoryDAO();
            String selectedCategory = selectCategoriesBuyer.getSelectionModel().getSelectedItem();
            int category = CategoryDAO.getCategoryIdByString(selectedCategory);
            List<Lot> lots = lotDAO.getLotsByCategory(category);
            ObservableList<Lot> lotus = FXCollections.observableArrayList();
            BidDAO bidDAO = new BidDAO(connection);

            for(Lot lot : lots){
                if(lot.getStatusLot() == StatusLot.ACTIVE){
                    lot.setMyBet(bidDAO.getBetByLotId(lot.getId(), AuthorizationController.userId));
                    lotus.add(lot);
                }
            }

            TableViewLotsBuyer.getItems().clear();
            TableViewLotsBuyer.setItems(lotus);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка при выборе категории: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Ошибка при выборе категории.", e);
        }
    }

    @FXML
    void initialize() throws Exception {
        update();
    }

    public void getSelected() throws Exception{
        ProductsSellerController.lot = TableViewLotsBuyer.getSelectionModel().getSelectedItem();
        ProductsSellerController.lot = lotDAO.getLotById(ProductsSellerController.lot.getId());
    }

    public void update() throws Exception{
        connection = DatabaseConnector.ConnectDb();
        categoryDAO = new CategoryDAO(connection);
        lotDAO = new LotDAO(connection);

        try {
            lots = FXCollections.observableArrayList(lotDAO.getActiveLots());
            ObservableList<String> combo = FXCollections.observableArrayList();
            List<Category> categories = categoryDAO.getAllCategoriesList();

            for(Category category : categories){
                combo.add(category.getName());
            }

            col_nameLotsBuyer.setCellValueFactory(new PropertyValueFactory<>("name"));
            col_startPriceLotsBuyer.setCellValueFactory(new PropertyValueFactory<>("startPrice"));
            col_currentPriceLotsBuyer.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
            col_endDateLots.setCellValueFactory(new PropertyValueFactory<>("publicationDate"));
            col_betBuyer.setCellValueFactory(new PropertyValueFactory<>("myBet"));

            selectCategoriesBuyer.setItems(combo);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка при обновлении: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Ошибка при обновлении.", e);
        }
    }
}