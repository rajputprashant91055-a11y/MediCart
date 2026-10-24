package ui;

import model.OrderItem;
import service.AuthService;
import service.OrderService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderManager {

    private static final OrderService orderService = new OrderService();

    public static class Order implements Serializable {
        private static final long serialVersionUID = 1L;

        public String orderId;
        public String customerName;
        public String phone;
        public String address;
        public String pharmacyName;
        public Map<String, Integer> items;
        public Map<String, Integer> prices;
        public int total;
        public String status;

        public Order(
                String orderId,
                String customerName,
                String phone,
                String address,
                String pharmacyName,
                Map<String, Integer> items,
                Map<String, Integer> prices,
                int total
        ) {
            this.orderId = orderId;
            this.customerName = customerName;
            this.phone = phone;
            this.address = address;
            this.pharmacyName = pharmacyName;
            this.items = new LinkedHashMap<>(items);
            this.prices = new LinkedHashMap<>(prices);
            this.total = total;
            this.status = "Pending";
        }
    }

    /**
     * Loads live orders from the MySQL database.
     */
    public static ArrayList<Order> getOrders() {
        ArrayList<Order> list = new ArrayList<>();
        try {
            List<model.Order> dbOrders = orderService.getAllOrders();
            for (model.Order o : dbOrders) {
                Map<String, Integer> itemsMap = new LinkedHashMap<>();
                Map<String, Integer> pricesMap = new LinkedHashMap<>();
                if (o.getItems() != null) {
                    for (OrderItem item : o.getItems()) {
                        itemsMap.put(item.getMedicineName(), item.getQuantity());
                        pricesMap.put(item.getMedicineName(), (int) Math.round(item.getPriceAtOrder()));
                    }
                }
                Order ord = new Order(
                        o.getOrderId(),
                        o.getCustomerName(),
                        o.getCustomerPhone(),
                        o.getDeliveryAddress(),
                        o.getPharmacyName(),
                        itemsMap,
                        pricesMap,
                        (int) Math.round(o.getTotalAmount())
                );
                ord.status = o.getOrderStatus();
                list.add(ord);
            }
        } catch (Exception e) {
            System.err.println("Could not load database orders: " + e.getMessage());
        }
        return list;
    }

    /**
     * Places the order transactionally in MySQL, locking stock and decrementing inventory.
     */
    public static boolean saveOrder(Order order) {
        try {
            int customerId = 2; // Default customer
            if (AuthService.getCurrentUser() != null) {
                customerId = AuthService.getCurrentUser().getUserId();
            }

            return orderService.placeOrder(
                    order.orderId,
                    customerId,
                    order.pharmacyName,
                    order.customerName,
                    order.phone,
                    order.address,
                    order.items,
                    order.prices
            );
        } catch (Exception e) {
            System.err.println("Database order placement failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates order status in MySQL.
     */
    public static boolean updateStatus(String orderId, String newStatus) {
        try {
            return orderService.updateOrderStatus(orderId, newStatus);
        } catch (Exception e) {
            System.err.println("Database status update failed: " + e.getMessage());
            return false;
        }
    }
}
