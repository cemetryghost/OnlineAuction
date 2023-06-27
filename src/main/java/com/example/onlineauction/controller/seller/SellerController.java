package com.example.onlineauction.controller.seller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.onlineauction.WindowsManager;
import com.example.onlineauction.util.DateTimeUtil;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SellerController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Button LotsSellerButton;
    @FXML private AnchorPane anchorPaneRulesSeller;
    @FXML private Label dateTimeLabel;
    @FXML private Button exitSellerButton, rulesSellerButton, winningSellerButton, exitAccountSeller;
    private Timeline timeline;

    @FXML
    void ExitSeller(ActionEvent event) {
        System.exit(1);
    }

    @FXML
    public void GoToSellerLots(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/onlineauction/seller/products-seller.fxml"));
            AnchorPane newPane = fxmlLoader.load();
            anchorPaneRulesSeller.getChildren().setAll(newPane.getChildren());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void GoToSellerWinning(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/onlineauction/seller/finish-products-seller.fxml"));
            AnchorPane newPane = fxmlLoader.load();
            anchorPaneRulesSeller.getChildren().setAll(newPane.getChildren());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void rulesSeller(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/onlineauction/seller/rulesSeller.fxml"));
            AnchorPane newPane = fxmlLoader.load();
            anchorPaneRulesSeller.getChildren().setAll(newPane.getChildren());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        DateTimeUtil.setupDateTimeUpdate(dateTimeLabel);
    }

    @FXML
    void ExitAccountSeller(ActionEvent event) {
        Stage stageClose = (Stage) exitAccountSeller.getScene().getWindow();
        stageClose.close();

        WindowsManager.openWindow("AllUsers/authorization.fxml","Авторизация");
    }
}
