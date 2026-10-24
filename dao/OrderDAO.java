package dao;

import database.DBConnection;
import model.Order;
import model.OrderItem;
import model.Payment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderDAO {

    /**
     * Executes a strict transactional order placement with row-level stock locking (SELECT ... FOR UPDATE).
     * Prevents partial fulfillment and guarantees that inventory cannot become negative.
     */
    public boolean placeOrderWithTransaction(Order order, List<OrderItem> items, String paymentMethod) throws SQLException {
        if (order == null || items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Order and items must not be empty.");
        }

        String lockInventorySql = "SELECT available_quantity FROM pharmacy_inventory "
                                + "WHERE pharmacy_id = ? AND medicine_id = ? FOR UPDATE";

        String updateInventorySql = "UPDATE pharmacy_inventory "
                                  + "SET available_quantity = available_quantity - ? "
                                  + "WHERE pharmacy_id = ? AND medicine_id = ? AND available_quantity >= ?";

        String insertOrderSql = "INSERT INTO orders (order_id, customer_id, pharmacy_id, order_status, "
                              + "                   total_amount, customer_name, customer_phone, delivery_address) "
                              + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        String insertOrderItemSql = "INSERT INTO order_items (order_id, medicine_id, quantity, price_at_order) "
                                  + "VALUES (?, ?, ?, ?)";

        String insertPaymentSql = "INSERT INTO payments (payment_reference, order_id, amount, payment_method, payment_status) "
                                + "VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // Begin transaction

            // Step 1: Lock and check stock for all items
            try (PreparedStatement lockStmt = conn.prepareStatement(lockInventorySql)) {
                for (OrderItem item : items) {
                    lockStmt.setInt(1, order.getPharmacyId());
                    lockStmt.setInt(2, item.getMedicineId());
                    try (ResultSet rs = lockStmt.executeQuery()) {
                        if (!rs.next()) {
                            conn.rollback();
                            throw new SQLException("Selected pharmacy does not stock medicine ID: " + item.getMedicineId());
                        }
                        int currentStock = rs.getInt("available_quantity");
                        if (currentStock < item.getQuantity()) {
                            conn.rollback();
                            throw new SQLException("Insufficient stock for medicine ID: " + item.getMedicineId()
                                    + " (Required: " + item.getQuantity() + ", Available: " + currentStock + ")");
                        }
                    }
                }
            }

            // Step 2: Deduct stock atomically
            try (PreparedStatement updateStmt = conn.prepareStatement(updateInventorySql)) {
                for (OrderItem item : items) {
                    updateStmt.setInt(1, item.getQuantity());
                    updateStmt.setInt(2, order.getPharmacyId());
                    updateStmt.setInt(3, item.getMedicineId());
                    updateStmt.setInt(4, item.getQuantity()); // Guard against concurrent overdraft
                    int rows = updateStmt.executeUpdate();
                    if (rows == 0) {
                        conn.rollback();
                        throw new SQLException("Concurrent modification error: insufficient stock during deduction.");
                    }
                }
            }

            // Step 3: Insert Order
            try (PreparedStatement orderStmt = conn.prepareStatement(insertOrderSql)) {
                orderStmt.setString(1, order.getOrderId());
                orderStmt.setInt(2, order.getCustomerId());
                orderStmt.setInt(3, order.getPharmacyId());
                orderStmt.setString(4, order.getOrderStatus() != null ? order.getOrderStatus() : "Pending");
                orderStmt.setDouble(5, order.getTotalAmount());
                orderStmt.setString(6, order.getCustomerName());
                orderStmt.setString(7, order.getCustomerPhone());
                orderStmt.setString(8, order.getDeliveryAddress());
                orderStmt.executeUpdate();
            }

            // Step 4: Insert Order Items
            try (PreparedStatement itemStmt = conn.prepareStatement(insertOrderItemSql)) {
                for (OrderItem item : items) {
                    itemStmt.setString(1, order.getOrderId());
                    itemStmt.setInt(2, item.getMedicineId());
                    itemStmt.setInt(3, item.getQuantity());
                    itemStmt.setDouble(4, item.getPriceAtOrder());
                    itemStmt.addBatch();
                }
                itemStmt.executeBatch();
            }

            // Step 5: Insert Payment record
            try (PreparedStatement payStmt = conn.prepareStatement(insertPaymentSql)) {
                String payRef = "PAY-" + UUID.randomUUID().toString().replace("-", "").substring(0, 10).toUpperCase();
                payStmt.setString(1, payRef);
                payStmt.setString(2, order.getOrderId());
                payStmt.setDouble(3, order.getTotalAmount());
                payStmt.setString(4, paymentMethod != null ? paymentMethod : "Cash on Delivery");
                payStmt.setString(5, "Completed");
                payStmt.executeUpdate();
            }

            conn.commit(); // Commit transaction
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rbEx) {
                    rbEx.printStackTrace();
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    closeEx.printStackTrace();
                }
            }
        }
    }

    public List<Order> getAllOrders() throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.order_id, o.customer_id, o.pharmacy_id, p.pharmacy_name, "
                   + "       o.order_status, o.total_amount, o.customer_name, o.customer_phone, "
                   + "       o.delivery_address, o.created_at, o.updated_at "
                   + "FROM orders o "
                   + "JOIN pharmacies p ON o.pharmacy_id = p.pharmacy_id "
                   + "ORDER BY o.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Order order = mapOrderRow(rs);
                order.setItems(getOrderItems(order.getOrderId()));
                list.add(order);
            }
        }
        return list;
    }

    public List<Order> getOrdersByCustomerId(int customerId) throws SQLException {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.order_id, o.customer_id, o.pharmacy_id, p.pharmacy_name, "
                   + "       o.order_status, o.total_amount, o.customer_name, o.customer_phone, "
                   + "       o.delivery_address, o.created_at, o.updated_at "
                   + "FROM orders o "
                   + "JOIN pharmacies p ON o.pharmacy_id = p.pharmacy_id "
                   + "WHERE o.customer_id = ? "
                   + "ORDER BY o.created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Order order = mapOrderRow(rs);
                    order.setItems(getOrderItems(order.getOrderId()));
                    list.add(order);
                }
            }
        }
        return list;
    }

    public Order getOrderById(String orderId) throws SQLException {
        String sql = "SELECT o.order_id, o.customer_id, o.pharmacy_id, p.pharmacy_name, "
                   + "       o.order_status, o.total_amount, o.customer_name, o.customer_phone, "
                   + "       o.delivery_address, o.created_at, o.updated_at "
                   + "FROM orders o "
                   + "JOIN pharmacies p ON o.pharmacy_id = p.pharmacy_id "
                   + "WHERE o.order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrderRow(rs);
                    order.setItems(getOrderItems(order.getOrderId()));
                    return order;
                }
            }
        }
        return null;
    }

    public boolean updateOrderStatus(String orderId, String newStatus) throws SQLException {
        String sql = "UPDATE orders SET order_status = ?, updated_at = CURRENT_TIMESTAMP WHERE order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setString(2, orderId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<OrderItem> getOrderItems(String orderId) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT oi.order_item_id, oi.order_id, oi.medicine_id, m.name AS medicine_name, "
                   + "       oi.quantity, oi.price_at_order "
                   + "FROM order_items oi "
                   + "JOIN medicines m ON oi.medicine_id = m.medicine_id "
                   + "WHERE oi.order_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new OrderItem(
                            rs.getInt("order_item_id"),
                            rs.getString("order_id"),
                            rs.getInt("medicine_id"),
                            rs.getString("medicine_name"),
                            rs.getInt("quantity"),
                            rs.getDouble("price_at_order")
                    ));
                }
            }
        }
        return items;
    }

    private Order mapOrderRow(ResultSet rs) throws SQLException {
        return new Order(
                rs.getString("order_id"),
                rs.getInt("customer_id"),
                rs.getInt("pharmacy_id"),
                rs.getString("pharmacy_name"),
                rs.getString("order_status"),
                rs.getDouble("total_amount"),
                rs.getString("customer_name"),
                rs.getString("customer_phone"),
                rs.getString("delivery_address"),
                rs.getTimestamp("created_at"),
                rs.getTimestamp("updated_at")
        );
    }
}
