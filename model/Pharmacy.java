package model;

import java.sql.Timestamp;

public class Pharmacy {
    private int pharmacyId;
    private String pharmacyName;
    private String address;
    private String contactNumber;
    private String email;
    private String registrationStatus; // PENDING, ACTIVE, SUSPENDED
    private Timestamp createdAt;

    public Pharmacy() {}

    public Pharmacy(int pharmacyId, String pharmacyName, String address, String contactNumber, String email, String registrationStatus, Timestamp createdAt) {
        this.pharmacyId = pharmacyId;
        this.pharmacyName = pharmacyName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.email = email;
        this.registrationStatus = registrationStatus;
        this.createdAt = createdAt;
    }

    public int getPharmacyId() { return pharmacyId; }
    public void setPharmacyId(int pharmacyId) { this.pharmacyId = pharmacyId; }

    public String getPharmacyName() { return pharmacyName; }
    public void setPharmacyName(String pharmacyName) { this.pharmacyName = pharmacyName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getContactNumber() { return contactNumber; }
    public void setContactNumber(String contactNumber) { this.contactNumber = contactNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRegistrationStatus() { return registrationStatus; }
    public void setRegistrationStatus(String registrationStatus) { this.registrationStatus = registrationStatus; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
}
