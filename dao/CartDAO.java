package dao;

import database.DBConnection;
import model.CartItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public List<CartItem> getCartByCustomerId(int customerId) throws SQLException {
        List<CartItem> list = new ArrayList<>();
        String sql = "SELECT c.cart_item_id, c.customer_id, c.medicine_id, m.name AS medicine_name, "
                   + "       m.category, m.price, c.quantity, "
                   + "       COALESCE(MAX(pi.available_quantity), 0) AS stock_available, c.created_at "
                   + "FROM cart_items c "
                   + "JOIN medicines m ON c.medicine_id = m.medicine_id "
                   + "LEFT JOIN pharmacy_inventory pi ON m.medicine_id = pi.medicine_id "
                   + "WHERE c.customer_id = ? "
                   + "GROUP BY c.cart_item_id, c.customer_id, c.medicine_id, m.name, m.category, m.price, c.quantity, c.created_at "
                   + "ORDER BY c.cart_item_id ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new CartItem(
                            rs.getInt("cart_item_id"),
                            rs.getInt("customer_id"),
                            rs.getInt("medicine_id"),
                            rs.getString("medicine_name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getInt("quantity"),
                            rs.getInt("stock_available"),
                            rs.getTimestamp("created_at")
                    ));
                }
            }
        }
        return list;
    }

    public void addToCart(int customerId, int medicineId, int quantity) throws SQLException {
        String sql = "INSERT INTO cart_items (customer_id, medicine_id, quantity) "
                   + "VALUES (?, ?, ?) "
                   + "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setInt(2, medicineId);
            ps.setInt(3, quantity);
            ps.executeUpdate();
        }
    }

    public void updateQuantity(int customerId, int medicineId, int newQuantity) throws SQLException {
        if (newQuantity <= 0) {
            removeFromCart(customerId, medicineId);
            return;
        }
        String sql = "UPDATE cart_items SET quantity = ? WHERE customer_id = ? AND medicine_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, newQuantity);
            ps.setInt(2, customerId);
            ps.setInt(3, medicineId);
            ps.executeUpdate();
        }
    }

    public void removeFromCart(int customerId, int medicineId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE customer_id = ? AND medicine_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setInt(2, medicineId);
            ps.executeUpdate();
        }
    }

    public void clearCart(int customerId) throws SQLException {
        String sql = "DELETE FROM cart_items WHERE customer_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.executeUpdate();
        }
    }
}
