package service;

import dao.CartDAO;
import dao.MedicineDAO;
import dao.OrderDAO;
import dao.PharmacyDAO;
import model.Medicine;
import model.Order;
import model.OrderItem;
import model.Pharmacy;

import java.sql.SQLException;
import java.util.*;

public class OrderService {

    private final OrderDAO orderDAO;
    private final CartDAO cartDAO;
    private final MedicineDAO medicineDAO;
    private final PharmacyDAO pharmacyDAO;

    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.cartDAO = new CartDAO();
        this.medicineDAO = new MedicineDAO();
        this.pharmacyDAO = new PharmacyDAO();
    }

    public boolean placeOrder(String orderId, int customerId, String pharmacyName,
                              String customerName, String phone, String address,
                              Map<String, Integer> items, Map<String, Integer> itemPrices) throws SQLException {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Cart cannot be empty.");
        }

        Pharmacy pharmacy = pharmacyDAO.findByName(pharmacyName);
        if (pharmacy == null) {
            throw new SQLException("Matched pharmacy '" + pharmacyName + "' could not be found in database.");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0.0;

        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String medName = entry.getKey();
            int qty = entry.getValue();

            Medicine med = medicineDAO.findByName(medName);
            if (med == null) {
                throw new SQLException("Medicine '" + medName + "' not found in database.");
            }

            double price = itemPrices != null && itemPrices.containsKey(medName)
                    ? itemPrices.get(medName) : med.getPrice();

            orderItems.add(new OrderItem(0, orderId, med.getMedicineId(), med.getName(), qty, price));
            total += (qty * price);
        }

        Order order = new Order();
        order.setOrderId(orderId);
        order.setCustomerId(customerId > 0 ? customerId : 2); // Fallback to demo customer if needed
        order.setPharmacyId(pharmacy.getPharmacyId());
        order.setPharmacyName(pharmacy.getPharmacyName());
        order.setOrderStatus("Pending");
        order.setTotalAmount(total);
        order.setCustomerName(customerName);
        order.setCustomerPhone(phone);
        order.setDeliveryAddress(address);

        boolean placed = orderDAO.placeOrderWithTransaction(order, orderItems, "Cash on Delivery");
        if (placed) {
            try {
                cartDAO.clearCart(order.getCustomerId());
            } catch (Exception ignored) {}
        }
        return placed;
    }

    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.getAllOrders();
    }

    public List<Order> getCustomerOrders(int customerId) throws SQLException {
        return orderDAO.getOrdersByCustomerId(customerId);
    }

    public boolean updateOrderStatus(String orderId, String newStatus) throws SQLException {
        return orderDAO.updateOrderStatus(orderId, newStatus);
    }
}
