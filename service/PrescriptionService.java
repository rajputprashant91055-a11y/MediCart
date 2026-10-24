package service;

import dao.PrescriptionDAO;
import dao.UserDAO;
import model.Prescription;
import model.User;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PrescriptionService {

    private final PrescriptionDAO prescriptionDAO;
    private final UserDAO userDAO;
    private static final String UPLOAD_DIR = "uploads/prescriptions";

    public PrescriptionService() {
        this.prescriptionDAO = new PrescriptionDAO();
        this.userDAO = new UserDAO();
        ensureUploadDir();
    }

    private void ensureUploadDir() {
        File dir = new File(UPLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Saves an uploaded prescription file and creates a PENDING prescription entry in MySQL.
     */
    public Prescription submitPrescription(int customerId, File sourceFile) throws IOException, SQLException {
        if (sourceFile == null || !sourceFile.isFile()) {
            throw new IllegalArgumentException("Valid prescription file is required.");
        }

        ensureUploadDir();

        String originalName = sourceFile.getName();
        String extension = "";
        int dot = originalName.lastIndexOf('.');
        if (dot >= 0) {
            extension = originalName.substring(dot);
        }

        String storedFileName = "rx_" + customerId + "_" + System.currentTimeMillis() + "_"
                + UUID.randomUUID().toString().substring(0, 6) + extension;

        Path targetPath = Paths.get(UPLOAD_DIR, storedFileName);
        Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        Prescription prescription = new Prescription();
        prescription.setCustomerId(customerId);
        prescription.setFilePath(targetPath.toString());
        prescription.setVerificationStatus("PENDING");
        prescription.setReviewerNote("Awaiting pharmacist/admin review");

        return prescriptionDAO.createPrescription(prescription);
    }

    /**
     * Submits a prescription by customer name (for backwards compatibility with existing UI prompt).
     */
    public Prescription submitPrescriptionByName(String customerName, File sourceFile) throws IOException, SQLException {
        int customerId = 2; // Default demo customer if not logged in
        if (AuthService.getCurrentUser() != null) {
            customerId = AuthService.getCurrentUser().getUserId();
        } else if (customerName != null && !customerName.trim().isEmpty()) {
            User existing = userDAO.findByEmail(customerName.trim());
            if (existing != null) {
                customerId = existing.getUserId();
            }
        }
        return submitPrescription(customerId, sourceFile);
    }

    public List<Prescription> getAllPrescriptions() throws SQLException {
        return prescriptionDAO.getAllPrescriptions();
    }

    public List<Prescription> getCustomerPrescriptions(int customerId) throws SQLException {
        return prescriptionDAO.getPrescriptionsByCustomerId(customerId);
    }

    public boolean reviewPrescription(int prescriptionId, String status, int reviewerId, String adminNote) throws SQLException {
        String normalizedStatus = "VERIFIED";
        if ("Rejected".equalsIgnoreCase(status) || "REJECTED".equalsIgnoreCase(status)) {
            normalizedStatus = "REJECTED";
        }
        return prescriptionDAO.updateStatusAndNote(prescriptionId, normalizedStatus, reviewerId, adminNote);
    }

    public Prescription getById(int prescriptionId) throws SQLException {
        return prescriptionDAO.findById(prescriptionId);
    }
}
