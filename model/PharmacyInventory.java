package model;

import java.sql.Timestamp;

public class PharmacyInventory {
    private int inventoryId;
    private int pharmacyId;
    private int medicineId;
    private String medicineName;
    private int availableQuantity;
    private double price;
    private Timestamp updatedAt;

    public PharmacyInventory() {}

    public PharmacyInventory(int inventoryId, int pharmacyId, int medicineId, String medicineName, int availableQuantity, double price, Timestamp updatedAt) {
        this.inventoryId = inventoryId;
        this.pharmacyId = pharmacyId;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.availableQuantity = availableQuantity;
        this.price = price;
        this.updatedAt = updatedAt;
    }

    public int getInventoryId() { return inventoryId; }
    public void setInventoryId(int inventoryId) { this.inventoryId = inventoryId; }

    public int getPharmacyId() { return pharmacyId; }
    public void setPharmacyId(int pharmacyId) { this.pharmacyId = pharmacyId; }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }
}
