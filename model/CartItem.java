package model;

import java.sql.Timestamp;

public class CartItem {
    private int cartItemId;
    private int customerId;
    private int medicineId;
    private String medicineName;
    private String category;
    private double unitPrice;
    private int quantity;
    private int stockAvailable;
    private Timestamp createdAt;

    public CartItem() {}

    public CartItem(int cartItemId, int customerId, int medicineId, String medicineName,
                    String category, double unitPrice, int quantity, int stockAvailable,
                    Timestamp createdAt) {
        this.cartItemId = cartItemId;
        this.customerId = customerId;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.category = category;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.stockAvailable = stockAvailable;
        this.createdAt = createdAt;
    }

    public int getCartItemId() { return cartItemId; }
    public void setCartItemId(int cartItemId) { this.cartItemId = cartItemId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public int getStockAvailable() { return stockAvailable; }
    public void setStockAvailable(int stockAvailable) { this.stockAvailable = stockAvailable; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public double getLineTotal() {
        return unitPrice * quantity;
    }
}
