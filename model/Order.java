package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String orderId;
    private int customerId;
    private int pharmacyId;
    private String pharmacyName;
    private String orderStatus; // Pending, Confirmed, Shipped, Delivered, Cancelled
    private double totalAmount;
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private List<OrderItem> items = new ArrayList<>();

    public Order() {}

    public Order(String orderId, int customerId, int pharmacyId, String pharmacyName,
                 String orderStatus, double totalAmount, String customerName,
                 String customerPhone, String deliveryAddress,
                 Timestamp createdAt, Timestamp updatedAt) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.pharmacyId = pharmacyId;
        this.pharmacyName = pharmacyName;
        this.orderStatus = orderStatus;
        this.totalAmount = totalAmount;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.deliveryAddress = deliveryAddress;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getOrderId() { return orderId; }
    public void setOrderId(String orderId) { this.orderId = orderId; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public int getPharmacyId() { return pharmacyId; }
    public void setPharmacyId(int pharmacyId) { this.pharmacyId = pharmacyId; }

    public String getPharmacyName() { return pharmacyName; }
    public void setPharmacyName(String pharmacyName) { this.pharmacyName = pharmacyName; }

    public String getOrderStatus() { return orderStatus; }
    public void setOrderStatus(String orderStatus) { this.orderStatus = orderStatus; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getCustomerPhone() { return customerPhone; }
    public void setCustomerPhone(String customerPhone) { this.customerPhone = customerPhone; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
}
