package model;

public class PrescriptionItem {
    private int prescriptionItemId;
    private int prescriptionId;
    private int medicineId;
    private String medicineName;
    private int requiredQuantity;
    private String dosageInstructions;

    public PrescriptionItem() {}

    public PrescriptionItem(int prescriptionItemId, int prescriptionId, int medicineId, String medicineName, int requiredQuantity, String dosageInstructions) {
        this.prescriptionItemId = prescriptionItemId;
        this.prescriptionId = prescriptionId;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.requiredQuantity = requiredQuantity;
        this.dosageInstructions = dosageInstructions;
    }

    public int getPrescriptionItemId() { return prescriptionItemId; }
    public void setPrescriptionItemId(int prescriptionItemId) { this.prescriptionItemId = prescriptionItemId; }

    public int getPrescriptionId() { return prescriptionId; }
    public void setPrescriptionId(int prescriptionId) { this.prescriptionId = prescriptionId; }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public int getRequiredQuantity() { return requiredQuantity; }
    public void setRequiredQuantity(int requiredQuantity) { this.requiredQuantity = requiredQuantity; }

    public String getDosageInstructions() { return dosageInstructions; }
    public void setDosageInstructions(String dosageInstructions) { this.dosageInstructions = dosageInstructions; }
}
