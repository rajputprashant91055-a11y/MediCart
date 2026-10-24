package ui;

import service.AuthService;
import service.PrescriptionService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionManager {

    private static final PrescriptionService prescriptionService = new PrescriptionService();

    public static class Prescription {
        private int prescriptionId;
        private final String customerName;
        private final File file;
        private String status;
        private String adminNote;

        public Prescription(String customerName, File file) {
            this.customerName = customerName;
            this.file = file;
            this.status = "Pending";
            this.adminNote = "Awaiting review";
        }

        public Prescription(int prescriptionId, String customerName, File file, String status, String adminNote) {
            this.prescriptionId = prescriptionId;
            this.customerName = customerName;
            this.file = file;
            this.status = status;
            this.adminNote = adminNote;
        }

        public int getPrescriptionId() {
            return prescriptionId;
        }

        public String getCustomerName() {
            return customerName;
        }

        public String getFileName() {
            return file != null ? file.getName() : "prescription.png";
        }

        public File getFile() {
            return file;
        }

        public String getStatus() {
            return status;
        }

        public String getAdminNote() {
            return adminNote;
        }

        public void setStatus(String status) {
            this.status = status;
            saveReview();
        }

        public void setAdminNote(String adminNote) {
            this.adminNote = adminNote;
            saveReview();
        }

        private void saveReview() {
            if (prescriptionId > 0) {
                int reviewerId = 1; // Default admin
                if (AuthService.getCurrentUser() != null && AuthService.getCurrentUser().isAdmin()) {
                    reviewerId = AuthService.getCurrentUser().getUserId();
                }
                try {
                    prescriptionService.reviewPrescription(prescriptionId, status, reviewerId, adminNote);
                } catch (Exception e) {
                    System.err.println("Failed to update prescription review in DB: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Submits and stores a prescription into MySQL and the uploads folder.
     */
    public static void addPrescription(String customerName, File file) {
        try {
            prescriptionService.submitPrescriptionByName(customerName, file);
        } catch (Exception e) {
            System.err.println("Failed to submit prescription to DB: " + e.getMessage());
        }
    }

    /**
     * Retrieves all prescriptions from MySQL.
     */
    public static List<Prescription> getPrescriptions() {
        List<Prescription> list = new ArrayList<>();
        try {
            List<model.Prescription> dbList = prescriptionService.getAllPrescriptions();
            for (model.Prescription p : dbList) {
                File f = p.getFilePath() != null ? new File(p.getFilePath()) : new File(p.getFileName());
                Prescription pm = new Prescription(
                        p.getPrescriptionId(),
                        p.getCustomerName(),
                        f,
                        p.getVerificationStatus(),
                        p.getReviewerNote() != null ? p.getReviewerNote() : "Awaiting review"
                );
                list.add(pm);
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch prescriptions from DB: " + e.getMessage());
        }
        return list;
    }
}
