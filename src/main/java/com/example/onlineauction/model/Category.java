package com.example.onlineauction.model;

import com.example.onlineauction.dao.CategoryDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.Date;

public class Category {
    private int id;
    private StringProperty name;
    private String name1;
    private double startPrice, myBet, currentPrice;
    private Date date;


    public Category(int id, String name) {
        this.id = id;
        this.name = new SimpleStringProperty(name);
    }

    public Category(String name1, double startPrice, double currentPrice, Date date, double myBet){
        this.name1 = name1;
        this.startPrice = startPrice;
        this.currentPrice = currentPrice;
        this.date = date;
        this.myBet = myBet;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    @Override
    public String toString() {
        return name.get();
    }
}