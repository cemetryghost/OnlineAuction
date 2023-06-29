package com.example.onlineauction.controller.admin;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.onlineauction.controller.WindowsManager;
import com.example.onlineauction.util.DateTimeUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class AdminController {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private Label dateTimeLabel;
    @FXML private AnchorPane AnchorPaneRulesAdmin;
    @FXML private Button accountsAdminButton, allLotsAdminButton, categoryAdminButton, exitAdminButton, rulesAdminButton, exitAccountAdmin;

    @FXML
    void AccountsButton(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/onlineauction/administrator/accounts-administrator.fxml"));
            AnchorPane newPane = fxmlLoader.load();
            AnchorPaneRulesAdmin.getChildren().setAll(newPane.getChildren());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void AllLotsAdmin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/onlineauction/administrator/all-products-administrator.fxml"));
            AnchorPane newPane = fxmlLoader.load();
            AnchorPaneRulesAdmin.getChildren().setAll(newPane.getChildren());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void CategoryAdmin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/onlineauction/administrator/category-view.fxml"));
            AnchorPane newPane = fxmlLoader.load();
            AnchorPaneRulesAdmin.getChildren().setAll(newPane.getChildren());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ExitAdmin(ActionEvent event) {
        System.exit(1);
    }

    @FXML
    void RulesAdmin(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/onlineauction/administrator/rulesAdmin.fxml"));
            AnchorPane newPane = fxmlLoader.load();
            AnchorPaneRulesAdmin.getChildren().setAll(newPane.getChildren());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void initialize() {
        DateTimeUtil.setupDateTimeUpdate(dateTimeLabel);
    }

    public void ExitAccountAdmin(ActionEvent actionEvent) {
        Stage stageClose = (Stage) exitAccountAdmin.getScene().getWindow();
        stageClose.close();

        WindowsManager.openWindow("/com/example/onlineauction/AllUsers/authorization.fxml","Авторизация");
    }
}
