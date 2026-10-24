package dao;

import database.DBConnection;
import model.Prescription;
import model.PrescriptionItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {

    public Prescription createPrescription(Prescription p) throws SQLException {
        String sql = "INSERT INTO prescriptions (customer_id, file_path, verification_status, reviewer_note) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, p.getCustomerId());
            ps.setString(2, p.getFilePath());
            ps.setString(3, p.getVerificationStatus() != null ? p.getVerificationStatus() : "PENDING");
            ps.setString(4, p.getReviewerNote() != null ? p.getReviewerNote() : "Awaiting review");

            int affected = ps.executeUpdate();
            if (affected > 0) {
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (keys.next()) {
                        p.setPrescriptionId(keys.getInt(1));
                    }
                }
            }
        }
        return p;
    }

    public List<Prescription> getAllPrescriptions() throws SQLException {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT p.prescription_id, p.customer_id, u.full_name AS customer_name, "
                   + "       p.file_path, p.verification_status, p.reviewer_id, "
                   + "       r.full_name AS reviewer_name, p.reviewer_note, p.verified_at, p.created_at "
                   + "FROM prescriptions p "
                   + "JOIN users u ON p.customer_id = u.user_id "
                   + "LEFT JOIN users r ON p.reviewer_id = r.user_id "
                   + "ORDER BY p.prescription_id DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Prescription p = mapRow(rs);
                list.add(p);
            }
        }
        return list;
    }

    public List<Prescription> getPrescriptionsByCustomerId(int customerId) throws SQLException {
        List<Prescription> list = new ArrayList<>();
        String sql = "SELECT p.prescription_id, p.customer_id, u.full_name AS customer_name, "
                   + "       p.file_path, p.verification_status, p.reviewer_id, "
                   + "       r.full_name AS reviewer_name, p.reviewer_note, p.verified_at, p.created_at "
                   + "FROM prescriptions p "
                   + "JOIN users u ON p.customer_id = u.user_id "
                   + "LEFT JOIN users r ON p.reviewer_id = r.user_id "
                   + "WHERE p.customer_id = ? "
                   + "ORDER BY p.prescription_id DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    public Prescription findById(int prescriptionId) throws SQLException {
        String sql = "SELECT p.prescription_id, p.customer_id, u.full_name AS customer_name, "
                   + "       p.file_path, p.verification_status, p.reviewer_id, "
                   + "       r.full_name AS reviewer_name, p.reviewer_note, p.verified_at, p.created_at "
                   + "FROM prescriptions p "
                   + "JOIN users u ON p.customer_id = u.user_id "
                   + "LEFT JOIN users r ON p.reviewer_id = r.user_id "
                   + "WHERE p.prescription_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Prescription p = mapRow(rs);
                    p.setItems(getPrescriptionItems(p.getPrescriptionId()));
                    return p;
                }
            }
        }
        return null;
    }

    public boolean updateStatusAndNote(int prescriptionId, String status, int reviewerId, String note) throws SQLException {
        String sql = "UPDATE prescriptions SET verification_status = ?, reviewer_id = ?, reviewer_note = ?, verified_at = CURRENT_TIMESTAMP "
                   + "WHERE prescription_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, reviewerId);
            ps.setString(3, note);
            ps.setInt(4, prescriptionId);
            return ps.executeUpdate() > 0;
        }
    }

    public List<PrescriptionItem> getPrescriptionItems(int prescriptionId) throws SQLException {
        List<PrescriptionItem> items = new ArrayList<>();
        String sql = "SELECT pi.prescription_item_id, pi.prescription_id, pi.medicine_id, m.name AS medicine_name, "
                   + "       pi.required_quantity, pi.dosage_instructions "
                   + "FROM prescription_items pi "
                   + "JOIN medicines m ON pi.medicine_id = m.medicine_id "
                   + "WHERE pi.prescription_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, prescriptionId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    items.add(new PrescriptionItem(
                            rs.getInt("prescription_item_id"),
                            rs.getInt("prescription_id"),
                            rs.getInt("medicine_id"),
                            rs.getString("medicine_name"),
                            rs.getInt("required_quantity"),
                            rs.getString("dosage_instructions")
                    ));
                }
            }
        }
        return items;
    }

    public void addPrescriptionItem(PrescriptionItem item) throws SQLException {
        String sql = "INSERT INTO prescription_items (prescription_id, medicine_id, required_quantity, dosage_instructions) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, item.getPrescriptionId());
            ps.setInt(2, item.getMedicineId());
            ps.setInt(3, item.getRequiredQuantity());
            ps.setString(4, item.getDosageInstructions());
            ps.executeUpdate();
        }
    }

    private Prescription mapRow(ResultSet rs) throws SQLException {
        Prescription p = new Prescription(
                rs.getInt("prescription_id"),
                rs.getInt("customer_id"),
                rs.getString("customer_name"),
                rs.getString("file_path"),
                rs.getString("verification_status"),
                (Integer) rs.getObject("reviewer_id"),
                rs.getString("reviewer_note"),
                rs.getTimestamp("verified_at"),
                rs.getTimestamp("created_at")
        );
        p.setReviewerName(rs.getString("reviewer_name"));
        return p;
    }
}
