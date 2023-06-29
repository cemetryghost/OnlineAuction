package com.example.onlineauction.controller.admin;

import com.example.onlineauction.dao.DatabaseConnector;
import com.example.onlineauction.dao.CategoryDAO;
import com.example.onlineauction.dao.LotDAO;
import com.example.onlineauction.dao.UserDAO;
import com.example.onlineauction.model.Lot;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminDetailsController implements Initializable {

    @FXML private Label categoryLotsLabel, conditionLotsLabel, currentLotsPriceLabel, dateLotsLabel, finishLotsLabel, nameLotsLabel, sellerLotsLabel, startPriceLotsLabel, statusLotsLabel, stepPriceLotsLabel;
    @FXML private Button backDetailLots;
    @FXML private TextArea descriptionLotsLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Lot lot = ProductsAdminController.lot;

        try{
            UserDAO userDAO = new UserDAO(DatabaseConnector.ConnectDb());
            LotDAO lotDAO = new LotDAO(DatabaseConnector.ConnectDb());

            Lot selectedLot = lotDAO.getLotById(lot.getId());

            nameLotsLabel.setText(selectedLot.getName());
            descriptionLotsLabel.setText(selectedLot.getDescription());
            sellerLotsLabel.setText(userDAO.getNameAndSurnameById(selectedLot.getSellerId()));
            categoryLotsLabel.setText(CategoryDAO.getCategoryById(Integer.parseInt(selectedLot.getCategory())));
            startPriceLotsLabel.setText(String.valueOf(selectedLot.getStartPrice()));
            stepPriceLotsLabel.setText(String.valueOf(selectedLot.getStepPrice()));
            dateLotsLabel.setText(selectedLot.getDatepublicationDate());
            finishLotsLabel.setText(selectedLot.getDatelosingDate());
            currentLotsPriceLabel.setText(String.valueOf(lot.getCurrentPrice()));
            conditionLotsLabel.setText(selectedLot.getCondition());
            statusLotsLabel.setText(selectedLot.getStatusString());
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }

    public void BackDetail(ActionEvent actionEvent) {
        Stage stageClose = (Stage) backDetailLots.getScene().getWindow();
        stageClose.close();
    }
}
