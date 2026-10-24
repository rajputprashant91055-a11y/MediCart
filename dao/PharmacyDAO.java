package dao;

import database.DBConnection;
import model.Pharmacy;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PharmacyDAO {

    public List<Pharmacy> getAllActivePharmacies() throws SQLException {
        List<Pharmacy> list = new ArrayList<>();
        String sql = "SELECT pharmacy_id, pharmacy_name, address, contact_number, email, "
                   + "       registration_status, created_at "
                   + "FROM pharmacies "
                   + "WHERE registration_status = 'ACTIVE' "
                   + "ORDER BY pharmacy_id ASC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    public Pharmacy findById(int pharmacyId) throws SQLException {
        String sql = "SELECT pharmacy_id, pharmacy_name, address, contact_number, email, "
                   + "       registration_status, created_at "
                   + "FROM pharmacies WHERE pharmacy_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, pharmacyId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    public Pharmacy findByName(String name) throws SQLException {
        String sql = "SELECT pharmacy_id, pharmacy_name, address, contact_number, email, "
                   + "       registration_status, created_at "
                   + "FROM pharmacies WHERE LOWER(pharmacy_name) = LOWER(?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        }
        return null;
    }

    private Pharmacy mapRow(ResultSet rs) throws SQLException {
        return new Pharmacy(
                rs.getInt("pharmacy_id"),
                rs.getString("pharmacy_name"),
                rs.getString("address"),
                rs.getString("contact_number"),
                rs.getString("email"),
                rs.getString("registration_status"),
                rs.getTimestamp("created_at")
        );
    }
}
