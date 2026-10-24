
package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;

public class MyOrdersFrame extends JFrame {

    private final Color primary = new Color(37, 99, 235);
    private final Color background = new Color(245, 247, 250);

    private JPanel ordersPanel;

    public MyOrdersFrame() {
        setTitle("MediCart - My Orders");
        setSize(850, 650);
        setMinimumSize(new Dimension(650, 500));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(background);
        main.setBorder(new EmptyBorder(22, 25, 22, 25));

        JLabel title = new JLabel("My Orders");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(primary);

        JLabel subtitle = new JLabel(
                "View your order history and track order status."
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(new Color(100, 110, 125));

        JPanel heading = new JPanel(new BorderLayout(5, 5));
        heading.setOpaque(false);
        heading.add(title, BorderLayout.NORTH);
        heading.add(subtitle, BorderLayout.SOUTH);

        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BoxLayout(
                ordersPanel, BoxLayout.Y_AXIS));
        ordersPanel.setBackground(background);

        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(background);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadOrders());

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(refreshButton);
        buttons.add(closeButton);

        main.add(heading, BorderLayout.NORTH);
        main.add(scrollPane, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);

        add(main);

        loadOrders();
    }

    private void loadOrders() {
        ordersPanel.removeAll();

        ArrayList<OrderManager.Order> orders =
                OrderManager.getOrders();

        if (orders.isEmpty()) {
            JLabel emptyLabel = new JLabel(
                    "No orders yet. Place an order to see it here."
            );
            emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            emptyLabel.setBorder(new EmptyBorder(25, 10, 25, 10));
            ordersPanel.add(emptyLabel);
        } else {
            for (int i = orders.size() - 1; i >= 0; i--) {
                ordersPanel.add(createOrderCard(orders.get(i)));
                ordersPanel.add(Box.createVerticalStrut(12));
            }
        }

        ordersPanel.revalidate();
        ordersPanel.repaint();
    }

    private JPanel createOrderCard(OrderManager.Order order) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(225, 230, 235)),
                new EmptyBorder(16, 18, 16, 18)
        ));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        JPanel details = new JPanel();
        details.setLayout(new BoxLayout(details, BoxLayout.Y_AXIS));
        details.setOpaque(false);

        JLabel idLabel = new JLabel("Order ID: " + order.orderId);
        idLabel.setFont(new Font("Arial", Font.BOLD, 16));
        idLabel.setForeground(primary);
        details.add(idLabel);
        details.add(Box.createVerticalStrut(8));

        details.add(new JLabel("Customer: " + order.customerName));
        details.add(new JLabel("Phone: " + order.phone));

        if (order.pharmacyName != null
                && !order.pharmacyName.trim().isEmpty()) {
            details.add(new JLabel(
                    "Pharmacy: " + order.pharmacyName));
        }

        details.add(Box.createVerticalStrut(8));
        details.add(new JLabel("Medicines:"));

        for (String medicine : order.items.keySet()) {
            int quantity = order.items.get(medicine);
            int price = order.prices.getOrDefault(medicine, 0);

            details.add(new JLabel(
                    "  • " + medicine + " x " + quantity
                            + " — ₹" + (quantity * price)));
        }

        details.add(Box.createVerticalStrut(8));

        JLabel totalLabel = new JLabel("Total: ₹" + order.total);
        totalLabel.setFont(new Font("Arial", Font.BOLD, 15));
        details.add(totalLabel);

        JLabel statusLabel = new JLabel(
                "Status: " + order.status);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setForeground(new Color(22, 130, 80));

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(statusLabel, BorderLayout.WEST);

        JButton detailsButton = new JButton("View Details");
        detailsButton.addActionListener(e -> showOrderDetails(order));
        bottom.add(detailsButton, BorderLayout.EAST);

        card.add(details, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        return card;
    }

    private void showOrderDetails(OrderManager.Order order) {
        StringBuilder message = new StringBuilder();

        message.append("Order ID: ").append(order.orderId)
                .append("\nStatus: ").append(order.status)
                .append("\nCustomer: ").append(order.customerName)
                .append("\nPhone: ").append(order.phone)
                .append("\nAddress: ").append(order.address);

        if (order.pharmacyName != null
                && !order.pharmacyName.trim().isEmpty()) {
            message.append("\nPharmacy: ").append(order.pharmacyName);
        }

        message.append("\n\nMedicines:\n");

        for (String medicine : order.items.keySet()) {
            int quantity = order.items.get(medicine);
            int price = order.prices.getOrDefault(medicine, 0);

            message.append(medicine)
                    .append(" x ").append(quantity)
                    .append(" = ₹").append(quantity * price)
                    .append("\n");
        }

        message.append("\nTotal Amount: ₹").append(order.total);

        JOptionPane.showMessageDialog(
                this,
                message.toString(),
                "Order Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}
