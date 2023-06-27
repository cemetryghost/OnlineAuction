package com.example.onlineauction.model;

public class Bid {
    private int id, lotId, buyerId;
    private double bidAmount;

    public Bid(int id, int lotId, int buyerId, double bidAmount) {
        this.id = id;
        this.lotId = lotId;
        this.buyerId = buyerId;
        this.bidAmount = bidAmount;
    }
    public Bid(int lotId, int buyerId, double bidAmount) {
        this.lotId = lotId;
        this.buyerId = buyerId;
        this.bidAmount = bidAmount;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLotId() {
        return lotId;
    }

    public void setLotId(int lotId) {
        this.lotId = lotId;
    }

    public int getBuyerId() {
        return buyerId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public double getBidAmount() {
        return bidAmount;
    }

    public void setBidAmount(double bidAmount) {
        this.bidAmount = bidAmount;
    }
}
