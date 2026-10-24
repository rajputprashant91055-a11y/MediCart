package dao;

import database.DBConnection;
import model.Medicine;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicineDAO {

    public List<Medicine> getAllMedicines() throws SQLException {
        return searchMedicines(null, "All Medicines", "Default Order");
    }

    public List<Medicine> searchMedicines(String query, String category, String sortOption) throws SQLException {
        List<Medicine> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder(
                "SELECT m.medicine_id, m.name, m.description, m.category, m.dosage_form, "
              + "       m.price, m.prescription_required, m.image_file, m.created_at, "
              + "       COALESCE(MAX(pi.available_quantity), 0) AS max_pharmacy_stock "
              + "FROM medicines m "
              + "LEFT JOIN pharmacy_inventory pi ON m.medicine_id = pi.medicine_id "
              + "WHERE 1=1 "
        );

        List<Object> params = new ArrayList<>();

        if (query != null && !query.trim().isEmpty()) {
            sql.append("AND (LOWER(m.name) LIKE ? OR LOWER(m.description) LIKE ? OR LOWER(m.category) LIKE ?) ");
            String term = "%" + query.trim().toLowerCase() + "%";
            params.add(term);
            params.add(term);
            params.add(term);
        }

        if (category != null && !category.equalsIgnoreCase("All Medicines") && !category.trim().isEmpty()) {
            sql.append("AND LOWER(m.category) = LOWER(?) ");
            params.add(category.trim());
        }

        sql.append("GROUP BY m.medicine_id, m.name, m.description, m.category, m.dosage_form, "
                 + "         m.price, m.prescription_required, m.image_file, m.created_at ");

        if ("Price: Low to High".equalsIgnoreCase(sortOption)) {
            sql.append("ORDER BY m.price ASC, m.name ASC ");
        } else if ("Price: High to Low".equalsIgnoreCase(sortOption)) {
            sql.append("ORDER BY m.price DESC, m.name ASC ");
        } else if ("Name: A to Z".equalsIgnoreCase(sortOption)) {
            sql.append("ORDER BY m.name ASC ");
        } else {
            sql.append("ORDER BY m.medicine_id ASC ");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Medicine m = mapRow(rs);
                    m.setAvailableStock(rs.getInt("max_pharmacy_stock"));
                    list.add(m);
                }
            }
        }
        return list;
    }

    public Medicine findById(int medicineId) throws SQLException {
        String sql = "SELECT m.medicine_id, m.name, m.description, m.category, m.dosage_form, "
                   + "       m.price, m.prescription_required, m.image_file, m.created_at, "
                   + "       COALESCE(MAX(pi.available_quantity), 0) AS max_pharmacy_stock "
                   + "FROM medicines m "
                   + "LEFT JOIN pharmacy_inventory pi ON m.medicine_id = pi.medicine_id "
                   + "WHERE m.medicine_id = ? "
                   + "GROUP BY m.medicine_id, m.name, m.description, m.category, m.dosage_form, "
                   + "         m.price, m.prescription_required, m.image_file, m.created_at";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medicineId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Medicine m = mapRow(rs);
                    m.setAvailableStock(rs.getInt("max_pharmacy_stock"));
                    return m;
                }
            }
        }
        return null;
    }

    public Medicine findByName(String name) throws SQLException {
        String sql = "SELECT m.medicine_id, m.name, m.description, m.category, m.dosage_form, "
                   + "       m.price, m.prescription_required, m.image_file, m.created_at, "
                   + "       COALESCE(MAX(pi.available_quantity), 0) AS max_pharmacy_stock "
                   + "FROM medicines m "
                   + "LEFT JOIN pharmacy_inventory pi ON m.medicine_id = pi.medicine_id "
                   + "WHERE LOWER(m.name) = LOWER(?) "
                   + "GROUP BY m.medicine_id, m.name, m.description, m.category, m.dosage_form, "
                   + "         m.price, m.prescription_required, m.image_file, m.created_at";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name.trim());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Medicine m = mapRow(rs);
                    m.setAvailableStock(rs.getInt("max_pharmacy_stock"));
                    return m;
                }
            }
        }
        return null;
    }

    private Medicine mapRow(ResultSet rs) throws SQLException {
        return new Medicine(
                rs.getInt("medicine_id"),
                rs.getString("name"),
                rs.getString("description"),
                rs.getString("category"),
                rs.getString("dosage_form"),
                rs.getDouble("price"),
                rs.getBoolean("prescription_required"),
                rs.getString("image_file"),
                rs.getTimestamp("created_at")
        );
    }
}
