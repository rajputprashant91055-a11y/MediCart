package dao;

import database.DBConnection;
import model.PharmacyInventory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PharmacyInventoryDAO {

    public int getAvailableStock(int pharmacyId, int medicineId) throws SQLException {
        String sql = "SELECT available_quantity FROM pharmacy_inventory "
                   + "WHERE pharmacy_id = ? AND medicine_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pharmacyId);
            ps.setInt(2, medicineId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("available_quantity");
                }
            }
        }
        return 0;
    }

    public List<PharmacyInventory> getInventoryByPharmacy(int pharmacyId) throws SQLException {
        List<PharmacyInventory> list = new ArrayList<>();
        String sql = "SELECT pi.inventory_id, pi.pharmacy_id, pi.medicine_id, m.name AS medicine_name, "
                   + "       pi.available_quantity, pi.price, pi.updated_at "
                   + "FROM pharmacy_inventory pi "
                   + "JOIN medicines m ON pi.medicine_id = m.medicine_id "
                   + "WHERE pi.pharmacy_id = ? "
                   + "ORDER BY m.name ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pharmacyId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new PharmacyInventory(
                            rs.getInt("inventory_id"),
                            rs.getInt("pharmacy_id"),
                            rs.getInt("medicine_id"),
                            rs.getString("medicine_name"),
                            rs.getInt("available_quantity"),
                            rs.getDouble("price"),
                            rs.getTimestamp("updated_at")
                    ));
                }
            }
        }
        return list;
    }

    /**
     * Verifies if a given pharmacy has at least the required quantity for all required medicine IDs.
     */
    public boolean canFulfillAll(int pharmacyId, Map<Integer, Integer> requiredItems) throws SQLException {
        if (requiredItems == null || requiredItems.isEmpty()) {
            return false;
        }
        String sql = "SELECT available_quantity FROM pharmacy_inventory "
                   + "WHERE pharmacy_id = ? AND medicine_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (Map.Entry<Integer, Integer> entry : requiredItems.entrySet()) {
                int medicineId = entry.getKey();
                int requiredQty = entry.getValue();
                if (requiredQty <= 0) return false;

                ps.setInt(1, pharmacyId);
                ps.setInt(2, medicineId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        return false; // Medicine not stocked at all
                    }
                    int available = rs.getInt("available_quantity");
                    if (available < requiredQty) {
                        return false; // Insufficient stock
                    }
                }
            }
        }
        return true;
    }
}
