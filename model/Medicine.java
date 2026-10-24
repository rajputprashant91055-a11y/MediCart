package model;

import java.sql.Timestamp;

public class Medicine {
    private int medicineId;
    private String name;
    private String description;
    private String category;
    private String dosageForm;
    private double price;
    private boolean prescriptionRequired;
    private String imageFile;
    private Timestamp createdAt;

    // Optional field computed from inventory queries
    private int availableStock;

    public Medicine() {}

    public Medicine(int medicineId, String name, String description, String category,
                    String dosageForm, double price, boolean prescriptionRequired,
                    String imageFile, Timestamp createdAt) {
        this.medicineId = medicineId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.dosageForm = dosageForm;
        this.price = price;
        this.prescriptionRequired = prescriptionRequired;
        this.imageFile = imageFile;
        this.createdAt = createdAt;
    }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDosageForm() { return dosageForm; }
    public void setDosageForm(String dosageForm) { this.dosageForm = dosageForm; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public boolean isPrescriptionRequired() { return prescriptionRequired; }
    public void setPrescriptionRequired(boolean prescriptionRequired) { this.prescriptionRequired = prescriptionRequired; }

    public String getImageFile() { return imageFile; }
    public void setImageFile(String imageFile) { this.imageFile = imageFile; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public int getAvailableStock() { return availableStock; }
    public void setAvailableStock(int availableStock) { this.availableStock = availableStock; }
}
