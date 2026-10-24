package model;

public class OrderItem {
    private int orderItemId;
    private String orderId;
    private int medicineId;
    private String medicineName;
    private int quantity;
    private double priceAtOrder;

    public OrderItem() {}

    public OrderItem(int orderItemId, String orderId, int medicineId, String medicineName, int quantity, double priceAtOrder) {
        this.orderItemId = orderItemId;
        this.orderId = orderId;
        this.medicineId = medicineId;
        this.medicineName = medicineName;
        this.quantity = quantity;
        this.priceAtOrder = priceAtOrder;
    }

    public int getOrderItemId() { return orderItemId; }
    public void setOrderItemId(int orderItemId) { this.orderItemId = orderItemId; }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getMedicineId() { return medicineId; }
    public void setMedicineId(int medicineId) { this.medicineId = medicineId; }

    public String getMedicineName() { return medicineName; }
    public void setMedicineName(String medicineName) { this.medicineName = medicineName; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getPriceAtOrder() { return priceAtOrder; }
    public void setPriceAtOrder(double priceAtOrder) { this.priceAtOrder = priceAtOrder; }

    public double getLineTotal() {
        return priceAtOrder * quantity;
    }
}
