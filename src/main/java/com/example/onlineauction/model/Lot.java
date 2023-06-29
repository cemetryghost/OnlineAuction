package com.example.onlineauction.model;

import com.example.onlineauction.constants.StatusLot;
import com.example.onlineauction.dao.DatabaseConnector;
import com.example.onlineauction.dao.UserDAO;

import java.sql.Date;

public class Lot {
    private int id, sellerId, currentBuyerId, categoryId;
    private String name, description, publicationDate, closingDate, condition, statusString, category, seller, buyer, winner;
    private double startPrice, currentPrice, stepPrice, myBet;
    private StatusLot statusLot;
    private Date DatepublicationDate, DateclosingDate;


    public Lot(int id, String name, String category, double startPrice, double currentPrice, String statusString) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.startPrice = startPrice;
        this.currentPrice = currentPrice;
        this.statusString = statusString;
    }
    public Lot(){

    }

    public Lot(String name, String description, double startPrice, double currentPrice, double stepPrice,
               String publicationDate, String closingDate, String condition) {
        this.name = name;
        this.description = description;
        this.startPrice = startPrice;
        this.currentPrice = currentPrice;
        this.stepPrice = stepPrice;
        this.publicationDate = publicationDate;
        this.closingDate = closingDate;
        this.condition = condition;
        this.statusLot = StatusLot.AWAITING_CONFIRMATION;
    }
    public Lot(int id, String name, String description, double startPrice, double currentPrice, double stepPrice,
               Date DatepublicationDate, Date DateclosingDate, String condition, String statusString, String category, int sellerId, int currentBuyerId){
        this.id = id;
        this.name = name;
        this.description = description;
        this.startPrice = startPrice;
        this.currentPrice = currentPrice;
        this.stepPrice = stepPrice;
        this.DatepublicationDate = DatepublicationDate;
        this.DateclosingDate = DateclosingDate;
        this.condition = condition;
        this.statusString = statusString;
        this.category = category;
        this.sellerId = sellerId;
        this.currentBuyerId = currentBuyerId;

    }

    public int getId() {
        return id;
    }
    public String getStatusString() {
        return statusString;
    }
    public void setStatusString(String statusString){this.statusString = statusString;}
    public String getCategory() {
        return category;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getDatelosingDate(){
        return DateclosingDate.toString();
    }
    public double getMyBet(){
        return myBet;
    }
    public void setMyBet(double myBet){
        this.myBet = myBet;
    }
    public String getDatepublicationDate(){
        return DatepublicationDate.toString();
    }
    public String getBuyer(){
        return buyer;
    }
    public void setBuyer(String buyer){
        this.buyer = buyer;
    }
    public String getName() {
        return name;
    }
    public String getSeller() throws Exception{
        UserDAO userDAO = new UserDAO(DatabaseConnector.ConnectDb());
        if(getCurrentBuyerId() != 0){
            return userDAO.getNameAndSurnameById(getCurrentBuyerId());
        }
        else {
            return seller;
        }
    }
    public void setSeller(String seller){
        this.seller = seller;
    }
    public String getWinner(){
        if(currentBuyerId == 49){
            return "Отсутствует";
        }
        else{
            return winner;
        }
    }
    public void setWinner(String winner){
        this.winner = winner;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public double getStartPrice() {
        return startPrice;
    }
    public void setStartPrice(double startPrice) {
        this.startPrice = startPrice;
    }
    public double getCurrentPrice() {
        return currentPrice;
    }
    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }
    public double getStepPrice() {
        return stepPrice;
    }
    public void setStepPrice(double stepPrice) {
        this.stepPrice = stepPrice;
    }
    public String getPublicationDate() {
        return publicationDate;
    }
    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }
    public String getClosingDate() {
        return closingDate;
    }
    public void setClosingDate(String closingDate) {
        this.closingDate = closingDate;
    }
    public String getCondition() {
        return condition;
    }
    public void setCondition(String condition) {
        this.condition = condition;
    }
    public StatusLot getStatusLot() {
        return statusLot;
    }
    public void setStatusLot(StatusLot statusLot) {
        this.statusLot = statusLot;
    }
    public int getCurrentBuyerId() {
        return currentBuyerId;
    }
    public void setCurrentBuyerId(int currentBuyerId) {
        this.currentBuyerId = currentBuyerId;
    }
    public int getSellerId() {
        return sellerId;
    }
    public String dateToString(Date date){
        return String.valueOf(date);
    }
    public void setSellerId(int sellerId) {
        this.sellerId = sellerId;
    }
    public int getCategoryId() {
        return categoryId;
    }
    public void setCategory(String category){
        this.category = category;
    }
    public int getCategoryIdd(){
        return categoryId;
    }
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String Print() {
        return "Lot{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", startPrice=" + startPrice +
                ", currentPrice=" + currentPrice +
                ", stepPrice=" + stepPrice +
                ", publicationDate='" + publicationDate + '\'' +
                ", closingDate='" + closingDate + '\'' +
                ", condition='" + condition + '\'' +
                ", statusLot=" + statusLot +
                ", sellerId=" + sellerId +
                ", currentBuyerId=" + currentBuyerId +
                ", categoryId=" + categoryId +
                ", category=" + category +
                ", statusString=" + statusString +
                ", DatepublicationDate=" + DateclosingDate.toString() +
                ", DatepublicationDate=" + DatepublicationDate.toString() +
                '}';
    }
}