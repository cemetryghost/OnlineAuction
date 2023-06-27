package com.example.onlineauction.controller.buyer;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.onlineauction.DatabaseConnector;
import com.example.onlineauction.LogManager;
import com.example.onlineauction.controller.admin.AccountsController;
import com.example.onlineauction.controller.authentication.AuthorizationController;
import com.example.onlineauction.dao.BidDAO;
import com.example.onlineauction.dao.CategoryDAO;
import com.example.onlineauction.dao.UserDAO;
import com.example.onlineauction.model.Bid;
import com.example.onlineauction.model.Category;
import com.example.onlineauction.model.Lot;
import com.example.onlineauction.dao.LotDAO;
import javafx.collections.FXCollections;
import com.example.onlineauction.controller.authentication.RegistrationController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import static com.example.onlineauction.util.AlertUtil.showAlert;

public class ParticipationsBuyerController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private AnchorPane AnchorPaneparticipationsBuyer;
    @FXML private TableView<Lot> TableViewParticipationsBuyer;
    @FXML private TableColumn<Lot, Double> col_betParticipationsBuyer, col_currentPriceLotsParticipationsBuyer;
    @FXML private TableColumn<Lot, String> col_nameLotsParticipationsBuyer, col_nameSellerLotsParticipationsBuyer, col_statusLotsParticipationsBuyer;
    @FXML private Label dateTimeField;
    @FXML private TextField betUpFieldBuyer;
    @FXML private Button upBetBuyer;

    private static final Logger LOGGER = LogManager.getLogger();

    @FXML
    void UpBetBuyer(ActionEvent event) {
        Lot selectedLot = TableViewParticipationsBuyer.getSelectionModel().getSelectedItem();
        if (selectedLot != null) {
            String betInput = betUpFieldBuyer.getText().trim();

            if (betInput.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Предупреждение", "Введите значение ставки!");
                LOGGER.log(Level.WARNING, "Попытка увеличения ставки с пустым значением.");
                return;
            }

            double newBet;
            try {
                newBet = Double.parseDouble(betInput);
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.WARNING, "Предупреждение", "Значение ставки должно быть числом!");
                LOGGER.log(Level.WARNING, "Попытка увеличения ставки с некорректным числовым значением: " + betInput);
                return;
            }

            int userId = (AuthorizationController.userId == 0) ? RegistrationController.registeredUserId : AuthorizationController.userId;

            if (selectedLot.getStatusString().equals("Завершен")) {
                showAlert(Alert.AlertType.WARNING, "Предупреждение", "Ставку нельзя увеличивать для завершенного лота!");
                LOGGER.log(Level.WARNING, "Попытка увеличения ставки для завершенного лота. Лот ID: " + selectedLot.getId());
                return;
            }

            if (newBet >= selectedLot.getCurrentPrice() + selectedLot.getStepPrice()) {
                try {
                    LotDAO lotDAO = new LotDAO(DatabaseConnector.ConnectDb());
                    BidDAO bidDAO = new BidDAO(DatabaseConnector.ConnectDb());

                    lotDAO.updateCurrentPriceById(newBet, selectedLot.getId());
                    lotDAO.updateBuyerIdByLotId(userId, selectedLot.getId());

                    if (bidDAO.existBidByIdLot(selectedLot.getId(), userId)) {
                        bidDAO.setBidAmountByIdLot(selectedLot.getId(), newBet, userId);
                    } else {
                        Bid bid = new Bid(selectedLot.getId(), userId, newBet);
                        bidDAO.addBid(bid);
                    }
                    showAlert(Alert.AlertType.INFORMATION, "Успешно!", "Ставка успешно увеличена!");
                    LOGGER.log(Level.INFO, "Успешное увеличение ставки для лота ID: " + selectedLot.getId());
                    refreshParticipationsBuyerTable();
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка при обновлении ставки: " + e.getMessage());
                    LOGGER.log(Level.SEVERE, "Ошибка при обновлении ставки.", e);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Ошибка!");
                alert.setHeaderText(null);

                ScrollPane scrollPane = new ScrollPane();
                scrollPane.setContent(new Label("Учитывайте текущую ставку и шаг цены!\nТекущая цена: " + selectedLot.getCurrentPrice() + "\nШаг цены: " + selectedLot.getStepPrice()));
                scrollPane.setFitToWidth(true);
                scrollPane.setPrefSize(270, 100);

                alert.getDialogPane().setContent(scrollPane);
                alert.showAndWait();
                LOGGER.log(Level.WARNING, "Попытка увеличения ставки ниже текущего значения. Лот ID: " + selectedLot.getId());
            }
        } else {
            showAlert(Alert.AlertType.WARNING, "Предупреждение", "Выберите лот для увеличения ставки!");
            LOGGER.log(Level.WARNING, "Попытка увеличения ставки без выбора лота.");
        }
    }

    private void refreshParticipationsBuyerTable() {
        try {
            ObservableList<Lot> lots = FXCollections.observableArrayList();
            UserDAO userDAO = new UserDAO(DatabaseConnector.ConnectDb());
            BidDAO bidDAO = new BidDAO(DatabaseConnector.ConnectDb());

            for (Lot lot : bidDAO.getLotsByBuyerid(AuthorizationController.userId)) {
                lot.setMyBet(bidDAO.getBetByLotId(lot.getId(), AuthorizationController.userId));
                if (lot.getMyBet() != 0) {
                    lot.setSeller(userDAO.getNameAndSurnameById(lot.getSellerId()));
                    lots.add(lot);
                }
            }
            TableViewParticipationsBuyer.setItems(lots);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка при обновлении таблицы участия покупателя: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Ошибка при обновлении таблицы участия покупателя.", e);
        }
    }

    @FXML
    void initialize() throws Exception {
        try {
            ObservableList<Lot> lots = FXCollections.observableArrayList();
            UserDAO userDAO = new UserDAO(DatabaseConnector.ConnectDb());
            BidDAO bidDAO = new BidDAO(DatabaseConnector.ConnectDb());
            for (Lot lot : bidDAO.getLotsByBuyerid(AuthorizationController.userId)) {
                lot.setMyBet(bidDAO.getBetByLotId(lot.getId(), AuthorizationController.userId));
                if (lot.getMyBet() != 0) {
                    lot.setSeller(userDAO.getNameAndSurnameById(lot.getSellerId()));
                    lots.add(lot);
                }
            }

            col_nameLotsParticipationsBuyer.setCellValueFactory(new PropertyValueFactory<>("name"));
            col_currentPriceLotsParticipationsBuyer.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
            col_betParticipationsBuyer.setCellValueFactory(new PropertyValueFactory<>("myBet"));
            col_nameSellerLotsParticipationsBuyer.setCellValueFactory(new PropertyValueFactory<>("seller"));
            col_statusLotsParticipationsBuyer.setCellValueFactory(new PropertyValueFactory<>("statusString"));

            TableViewParticipationsBuyer.setItems(lots);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Ошибка", "Ошибка при инициализации таблицы участия покупателя: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Ошибка при инициализации таблицы участия покупателя.", e);
        }
    }

}
