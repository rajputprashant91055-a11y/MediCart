package service;

import dao.MedicineDAO;
import dao.PharmacyDAO;
import database.DBConnection;
import model.Medicine;
import model.Pharmacy;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class PharmacyMatcherService {

    private final PharmacyDAO pharmacyDAO;
    private final MedicineDAO medicineDAO;

    public PharmacyMatcherService() {
        this.pharmacyDAO = new PharmacyDAO();
        this.medicineDAO = new MedicineDAO();
    }

    public static class MatchResult {
        private final boolean fullMatch;
        private final Integer pharmacyId;
        private final String pharmacyName;
        private final String message;
        private final List<String> checkedPharmacies;
        private final Map<String, Integer> availableStockInMatchedPharmacy;

        public MatchResult(boolean fullMatch, Integer pharmacyId, String pharmacyName,
                           String message, List<String> checkedPharmacies,
                           Map<String, Integer> availableStockInMatchedPharmacy) {
            this.fullMatch = fullMatch;
            this.pharmacyId = pharmacyId;
            this.pharmacyName = pharmacyName;
            this.message = message;
            this.checkedPharmacies = checkedPharmacies != null ? checkedPharmacies : new ArrayList<>();
            this.availableStockInMatchedPharmacy = availableStockInMatchedPharmacy != null ? availableStockInMatchedPharmacy : new HashMap<>();
        }

        public boolean isFullMatch() { return fullMatch; }
        public Integer getPharmacyId() { return pharmacyId; }
        public String getPharmacyName() { return pharmacyName; }
        public String getMessage() { return message; }
        public List<String> getCheckedPharmacies() { return checkedPharmacies; }
        public Map<String, Integer> getAvailableStockInMatchedPharmacy() { return availableStockInMatchedPharmacy; }
    }

    /**
     * Database-backed matching by medicine names and required quantities.
     */
    public MatchResult findFullMatch(Map<String, Integer> requiredMedicines) throws SQLException {
        if (requiredMedicines == null || requiredMedicines.isEmpty()) {
            return new MatchResult(false, null, null,
                    "No medicines were provided for matching.", Collections.emptyList(), null);
        }

        // Map medicine names to IDs and verify they exist in the catalog
        Map<Integer, Integer> requiredById = new LinkedHashMap<>();
        Map<Integer, String> idToName = new HashMap<>();

        for (Map.Entry<String, Integer> entry : requiredMedicines.entrySet()) {
            String medName = entry.getKey();
            Integer qty = entry.getValue();
            if (qty == null || qty <= 0) {
                return new MatchResult(false, null, null,
                        "Invalid quantity requested for " + medName + ".", Collections.emptyList(), null);
            }

            Medicine med = medicineDAO.findByName(medName);
            if (med == null) {
                return new MatchResult(false, null, null,
                        "Medicine '" + medName + "' is not registered in MediCart catalog.",
                        Collections.emptyList(), null);
            }
            requiredById.put(med.getMedicineId(), qty);
            idToName.put(med.getMedicineId(), med.getName());
        }

        return findFullMatchByIds(requiredById, idToName);
    }

    /**
     * Core smart matching engine that searches all registered active pharmacies in MySQL.
     */
    public MatchResult findFullMatchByIds(Map<Integer, Integer> requiredById, Map<Integer, String> idToName) throws SQLException {
        List<Pharmacy> activePharmacies = pharmacyDAO.getAllActivePharmacies();
        if (activePharmacies.isEmpty()) {
            return new MatchResult(false, null, null,
                    "No registered active pharmacies found in the system.", Collections.emptyList(), null);
        }

        List<String> checkedPharmacyNames = new ArrayList<>();

        String stockQuery = "SELECT medicine_id, available_quantity FROM pharmacy_inventory "
                          + "WHERE pharmacy_id = ? AND medicine_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(stockQuery)) {

            for (Pharmacy pharmacy : activePharmacies) {
                checkedPharmacyNames.add(pharmacy.getPharmacyName());
                boolean pharmacyFulfillsAll = true;
                Map<String, Integer> availableMap = new LinkedHashMap<>();

                for (Map.Entry<Integer, Integer> entry : requiredById.entrySet()) {
                    int medicineId = entry.getKey();
                    int requiredQty = entry.getValue();

                    ps.setInt(1, pharmacy.getPharmacyId());
                    ps.setInt(2, medicineId);

                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) {
                            pharmacyFulfillsAll = false;
                            break; // Medicine not stocked in this pharmacy
                        }
                        int stock = rs.getInt("available_quantity");
                        String medName = idToName != null && idToName.containsKey(medicineId)
                                ? idToName.get(medicineId) : ("Medicine #" + medicineId);
                        availableMap.put(medName, stock);

                        if (stock < requiredQty) {
                            pharmacyFulfillsAll = false;
                            break; // Insufficient quantity
                        }
                    }
                }

                // If pharmacy can fulfill 100% of all required items and quantities:
                if (pharmacyFulfillsAll) {
                    String message;
                    if (checkedPharmacyNames.size() == 1) {
                        message = "100% match found!\n"
                                + "Selected pharmacy: " + pharmacy.getPharmacyName();
                    } else {
                        message = "Previous checked pharmacies could not fulfill the complete order.\n"
                                + "100% match found!\n"
                                + "Selected pharmacy: " + pharmacy.getPharmacyName();
                    }

                    return new MatchResult(
                            true,
                            pharmacy.getPharmacyId(),
                            pharmacy.getPharmacyName(),
                            message,
                            checkedPharmacyNames,
                            availableMap
                    );
                }
            }
        }

        // No registered pharmacy had 100% stock
        return new MatchResult(
                false,
                null,
                null,
                "No single pharmacy currently has 100% of the complete required stock.\n"
                + "Checked pharmacies: " + checkedPharmacyNames + "\n\n"
                + "Partial orders and automatic medicine substitutions are strictly not allowed.",
                checkedPharmacyNames,
                null
        );
    }
}
