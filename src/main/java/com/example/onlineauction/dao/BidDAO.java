package com.example.onlineauction.dao;

import com.example.onlineauction.DatabaseConnector;
import com.example.onlineauction.LogManager;
import com.example.onlineauction.model.Bid;
import com.example.onlineauction.model.Lot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BidDAO {
    private Connection connection;
    private static final Logger LOGGER = LogManager.getLogger();

    public BidDAO(Connection connection) {
        this.connection = connection;
    }

    public void addBid(Bid bid) throws SQLException {
        String query = "INSERT INTO bids (lot_id, buyer_id, bid_amount) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bid.getLotId());
            statement.setInt(2, bid.getBuyerId());
            statement.setDouble(3, bid.getBidAmount());
            statement.executeUpdate();
            LOGGER.info("Добавлена новая ставка: " + bid.toString());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при добавлении ставки: " + bid.toString(), e);
            throw e;
        }
    }

    public boolean existBidByIdLot(int idLot, int idUser) throws Exception {
        boolean result;
        int id = 0;

        String query = "SELECT idbids FROM bids WHERE lot_id=? AND buyer_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idLot);
            statement.setInt(2, idUser);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    id = resultSet.getInt("idbids");
                }
                LOGGER.info("Ставка для lotId: " + idLot + ", userId: " + idUser);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при проверке наличия ставки для lotId: " + idLot + ", userId: " + idUser, e);
            throw e;
        }

        result = id != 0;
        return result;
    }

    public void setBidAmountByIdLot(int id, double amount, int idUser) throws Exception {
        String query = "UPDATE bids SET bid_amount = ? WHERE lot_id = ? AND buyer_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, amount);
            statement.setInt(2, id);
            statement.setInt(3, idUser);
            statement.executeUpdate();
            LOGGER.info("Обновлена сумма ставки для bidId: " + id + ", userId: " + idUser + ", новая сумма: " + amount);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при обновлении суммы ставки для bidId: " + id + ", userId: " + idUser, e);
            throw e;
        }
    }

    public double getBetByLotId(int lotId, int userId) throws Exception {
        String query = "SELECT bid_amount FROM bids WHERE lot_id=? AND buyer_id=?";
        double result = 0;
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, lotId);
            statement.setInt(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result = resultSet.getDouble("bid_amount");
                }
                LOGGER.info("Полученная сумма ставки для lotId: " + + lotId + ", userId: " + userId);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении суммы ставки для lotId: " + lotId + ", userId: " + userId, e);
            throw e;
        }
        return result;
    }

    public List<Lot> getLotsByBuyerid(int id) throws Exception {
        LotDAO lotDAO = new LotDAO(DatabaseConnector.ConnectDb());
        List<Lot> lots = new ArrayList<>();
        String query = "SELECT lot_id FROM bids WHERE buyer_id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    lots.add(lotDAO.getLotById(resultSet.getInt("lot_id")));
                }
                LOGGER.info("Полученный список лотов для buyerId: " + id);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении списка лотов для buyerId: " + id, e);
            throw e;
        }
        return lots;
    }

    public List<Bid> getBidsByLotId(int lotId) throws SQLException {
        List<Bid> bids = new ArrayList<>();
        String query = "SELECT * FROM bids WHERE lot_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, lotId);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Bid bid = new Bid(
                            resultSet.getInt("idbids"),
                            resultSet.getInt("lot_id"),
                            resultSet.getInt("buyer_id"),
                            resultSet.getDouble("bid_amount")
                    );
                    bids.add(bid);
                }
                LOGGER.info("Полученный список ставок для lotId: " + lotId);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Ошибка при получении списка ставок для lotId: " + lotId, e);
            throw e;
        }
        return bids;
    }
}
