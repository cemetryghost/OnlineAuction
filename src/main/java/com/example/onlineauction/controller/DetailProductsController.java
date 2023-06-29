package com.example.onlineauction.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.onlineauction.dao.DatabaseConnector;
import com.example.onlineauction.util.LogManager;
import com.example.onlineauction.constants.Role;
import com.example.onlineauction.controller.authentication.AuthorizationController;
import com.example.onlineauction.controller.authentication.RegistrationController;
import com.example.onlineauction.controller.buyer.ProductsBuyerController;
import com.example.onlineauction.controller.seller.ProductsSellerController;
import com.example.onlineauction.dao.BidDAO;
import com.example.onlineauction.dao.CategoryDAO;
import com.example.onlineauction.dao.LotDAO;
import com.example.onlineauction.dao.UserDAO;
import com.example.onlineauction.model.Bid;
import com.example.onlineauction.model.Lot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import static com.example.onlineauction.util.AlertUtil.showAlert;

public class DetailProductsController implements Initializable {

    @FXML private ResourceBundle resources;
    @FXML private Label currentLotsPriceLabel, categoryLotsLabel, conditionLotsLabel, dateLotsLabel, finishLotsLabel, nameLotsLabel, sellerLotsLabel, startPriceLotsLabel, statusLotsLabel, stepPriceLotsLabel;
    @FXML private URL location;
    @FXML private Pane PaneBettingBuyerVisible;
    @FXML private Button backDetailLots, buyerBettingButton;
    @FXML private TextField buyerBettingField;
    @FXML private TextArea descriptionLotsLabel;

    private static final Logger LOGGER = LogManager.getLogger();

    public static Lot lot;
    int id = 0;
    private ProductsBuyerController productsBuyerController;
    Connection connection = DatabaseConnector.ConnectDb();
    BidDAO bidDAO = new BidDAO(connection);

    public DetailProductsController() throws Exception {
    }

    public void setProductsBuyerController(ProductsBuyerController controller) {
        productsBuyerController = controller;
    }

    public void setRole(Role role) {
        boolean isBuyer = role == Role.BUYER;
        PaneBettingBuyerVisible.setVisible(isBuyer);
    }

    @FXML
    void BackDetail(ActionEvent event) {
        LOGGER.info("Вызван метод BackDetail");
        Stage stageClose = (Stage) backDetailLots.getScene().getWindow();
        stageClose.close();
    }

    @FXML
    void BuyerBet(ActionEvent event) throws Exception {
        LOGGER.info("Вызван метод BuyerBet");
        LotDAO lotDAO = new LotDAO(DatabaseConnector.ConnectDb());
        String betText = buyerBettingField.getText();

        if (betText.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Введите ставку!");
            LOGGER.warning("Попытка сделать ставку без указания значения ставки.");
            return;
        }
        double bet;
        try {
            bet = Double.parseDouble(betText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Некорректное значение ставки!");
            LOGGER.warning("Попытка сделать ставку с некорректным значением ставки.");
            return;
        }
        int id = 0;
        if (AuthorizationController.userId == 0) {
            id = RegistrationController.registeredUserId;
        } else {
            id = AuthorizationController.userId;
        }

        if (bet >= lot.getCurrentPrice() + lot.getStepPrice()) {
            lotDAO.updateCurrentPriceById(bet, lot.getId());
            lotDAO.updateBuyerIdByLotId(id, lot.getId());

            if (bidDAO.existBidByIdLot(lot.getId(), id)) {
                bidDAO.setBidAmountByIdLot(lot.getId(), bet, id);
            } else {
                Bid bid = new Bid(lot.getId(), id, bet);
                bidDAO.addBid(bid);
            }

            Stage stageClose = (Stage) buyerBettingButton.getScene().getWindow();
            stageClose.close();

            WindowsManager.openWindow("/com/example/onlineauction/buyer/buyer-view.fxml", "Окно покупателя");
            LOGGER.info("Ставка успешно сделана. ID лота: " + lot.getId() + ", ID покупателя: " + id);
        } else {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Учитывайте текущую ставку и шаг цены!");
            LOGGER.warning("Попытка сделать ставку меньше текущей ставки и шага цены. ID лота: " + lot.getId() + ", ID покупателя: " + id);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            UserDAO userDAO = new UserDAO(DatabaseConnector.ConnectDb());

            lot = ProductsSellerController.lot;
            nameLotsLabel.setText(lot.getName());
            descriptionLotsLabel.setText(lot.getDescription());
            sellerLotsLabel.setText(userDAO.getNameAndSurnameById(lot.getSellerId()));
            categoryLotsLabel.setText(CategoryDAO.getCategoryById(Integer.parseInt(lot.getCategory())));
            startPriceLotsLabel.setText(String.valueOf(lot.getStartPrice()));
            stepPriceLotsLabel.setText(String.valueOf(lot.getStepPrice()));
            dateLotsLabel.setText(lot.getDatepublicationDate());
            finishLotsLabel.setText(lot.getDatelosingDate());
            conditionLotsLabel.setText(lot.getCondition());
            currentLotsPriceLabel.setText(String.valueOf(lot.getCurrentPrice()));
            statusLotsLabel.setText(lot.getStatusString());

            LOGGER.info("Контроллер DetailProductsController успешно инициализирован.");
        } catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Ошибка при инициализации контроллера DetailProductsController", exception);
            exception.printStackTrace();
        }
    }
}