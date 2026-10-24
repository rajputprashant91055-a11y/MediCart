package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Prescription {
    private int prescriptionId;
    private int customerId;
    private String customerName;
    private String filePath;
    private String verificationStatus; // PENDING, VERIFIED, REJECTED
    private Integer reviewerId;
    private String reviewerName;
    private String reviewerNote;
    private Timestamp verifiedAt;
    private Timestamp createdAt;
    private List<PrescriptionItem> items = new ArrayList<>();

    public Prescription() {}

    public Prescription(int prescriptionId, int customerId, String customerName, String filePath,
                        String verificationStatus, Integer reviewerId, String reviewerNote,
                        Timestamp verifiedAt, Timestamp createdAt) {
        this.prescriptionId = prescriptionId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.filePath = filePath;
        this.verificationStatus = verificationStatus;
        this.reviewerId = reviewerId;
        this.reviewerNote = reviewerNote;
        this.verifiedAt = verifiedAt;
        this.createdAt = createdAt;
    }

    public int getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(int prescriptionId) { this.prescriptionId = prescriptionId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public String getFileName() {
        if (filePath == null) return "";
        int idx = Math.max(filePath.lastIndexOf('/'), filePath.lastIndexOf('\\'));
        return idx >= 0 ? filePath.substring(idx + 1) : filePath;
    }

    public String getVerificationStatus() { return verificationStatus; }
    public void setVerificationStatus(String verificationStatus) { this.verificationStatus = verificationStatus; }

    public Integer getReviewerId() { return reviewerId; }
    public void setReviewerId(Integer reviewerId) { this.reviewerId = reviewerId; }

    public String getReviewerName() { return reviewerName; }
    public void setReviewerName(String reviewerName) { this.reviewerName = reviewerName; }

    public String getReviewerNote() { return reviewerNote; }
    public void setReviewerNote(String reviewerNote) { this.reviewerNote = reviewerNote; }

    public Timestamp getVerifiedAt() { return verifiedAt; }
    public void setVerifiedAt(Timestamp verifiedAt) { this.verifiedAt = verifiedAt; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public List<PrescriptionItem> getItems() { return items; }
    public void setItems(List<PrescriptionItem> items) { this.items = items; }
}
