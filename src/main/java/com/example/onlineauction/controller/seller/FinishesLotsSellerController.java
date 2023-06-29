package com.example.onlineauction.controller.seller;

import java.net.URL;
import java.util.ResourceBundle;

import com.example.onlineauction.dao.DatabaseConnector;
import com.example.onlineauction.controller.authentication.AuthorizationController;
import com.example.onlineauction.controller.authentication.RegistrationController;
import com.example.onlineauction.dao.LotDAO;
import com.example.onlineauction.dao.UserDAO;
import com.example.onlineauction.model.Lot;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

public class FinishesLotsSellerController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private AnchorPane AnchorPaneWinningSeller;
    @FXML private TableView<Lot> TableViewFinishesLots;
    @FXML private TableColumn<Lot, String> col_PriceLotsFinish, col_categoryLotsFinish, col_finalStatusLotsFinish, col_nameBuyerLotsFinish, col_nameLotsFinish;

    @FXML
    void initialize() throws Exception{
        LotDAO lotDAO = new LotDAO(DatabaseConnector.ConnectDb());
        int id = 0;
        if(AuthorizationController.userId != 0){
            id = AuthorizationController.userId;
        }
        else{
            id = RegistrationController.registeredUserId;
        }
        ObservableList<Lot> lots = FXCollections.observableArrayList(lotDAO.getInactiveLotsBySellerId(id));
        UserDAO userDAO = new UserDAO(DatabaseConnector.ConnectDb());
        for(Lot lot : lots){
            if(lot.getCurrentBuyerId() != 49){
                lot.setWinner(userDAO.getNameAndSurnameById(lot.getCurrentBuyerId()));
            }
        }

        col_nameLotsFinish.setCellValueFactory(new PropertyValueFactory<>("name"));
        col_categoryLotsFinish.setCellValueFactory(new PropertyValueFactory<>("category"));
        col_PriceLotsFinish.setCellValueFactory(new PropertyValueFactory<>("currentPrice"));
        col_nameBuyerLotsFinish.setCellValueFactory(new PropertyValueFactory<>("winner"));
        col_finalStatusLotsFinish.setCellValueFactory(new PropertyValueFactory<>("statusString"));

        TableViewFinishesLots.setItems(lots);
    }

}
