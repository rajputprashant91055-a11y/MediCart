
package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class OrderFrame extends JFrame {

    private final Color primary = new Color(37, 99, 235);

    private final Map<String, Integer> cartItems;
    private final Map<String, Integer> itemPrices;
    private final Runnable onOrderPlaced;
    private final String pharmacyName;

    private JTextField nameField;
    private JTextField phoneField;
    private JTextArea addressArea;

    public OrderFrame(
            Map<String, Integer> cartItems,
            Map<String, Integer> itemPrices
    ) {
        this(cartItems, itemPrices, null, null);
    }

    public OrderFrame(
            Map<String, Integer> cartItems,
            Map<String, Integer> itemPrices,
            Runnable onOrderPlaced
    ) {
        this(cartItems, itemPrices, onOrderPlaced, null);
    }

    public OrderFrame(
            Map<String, Integer> cartItems,
            Map<String, Integer> itemPrices,
            Runnable onOrderPlaced,
            String pharmacyName
    ) {
        this.cartItems = cartItems;
        this.itemPrices = itemPrices;
        this.onOrderPlaced = onOrderPlaced;
        this.pharmacyName = pharmacyName;

        setTitle("MediCart - Place Order");
        setSize(720, 650);
        setMinimumSize(new Dimension(600, 550));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(new Color(245, 247, 250));
        main.setBorder(new EmptyBorder(22, 25, 22, 25));

        JLabel title = new JLabel("Complete Your Order");
        title.setFont(new Font("Arial", Font.BOLD, 27));
        title.setForeground(primary);

        JTextArea summary = new JTextArea();
        summary.setEditable(false);
        summary.setFont(new Font("Arial", Font.PLAIN, 14));
        summary.setBackground(Color.WHITE);
        summary.setBorder(new EmptyBorder(12, 12, 12, 12));

        StringBuilder summaryText =
                new StringBuilder("ORDER SUMMARY\n\n");

        if (pharmacyName != null && !pharmacyName.trim().isEmpty()) {
            summaryText.append("Selected Pharmacy: ")
                    .append(pharmacyName)
                    .append("\n\n");
        }

        for (String medicine : cartItems.keySet()) {
            int quantity = cartItems.get(medicine);
            int price = itemPrices.getOrDefault(medicine, 0);

            summaryText.append(medicine)
                    .append(" x ")
                    .append(quantity)
                    .append(" = ₹")
                    .append(quantity * price)
                    .append("\n");
        }

        summaryText.append("\nTotal Amount: ₹")
                .append(calculateTotal());

        summary.setText(summaryText.toString());

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(225, 230, 235)),
                        new EmptyBorder(15, 15, 15, 15)
                )
        );

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(7, 5, 7, 5);
        c.fill = GridBagConstraints.HORIZONTAL;

        nameField = new JTextField(20);
        phoneField = new JTextField(20);
        addressArea = new JTextArea(3, 20);
        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);

        if (service.AuthService.getCurrentUser() != null) {
            model.User u = service.AuthService.getCurrentUser();
            if (u.getFullName() != null) nameField.setText(u.getFullName());
            if (u.getPhone() != null) phoneField.setText(u.getPhone());
            if (u.getAddress() != null) addressArea.setText(u.getAddress());
        }

        addFormRow(form, c, 0, "Full Name", nameField);
        addFormRow(form, c, 1, "Phone Number", phoneField);
        addFormRow(form, c, 2, "Delivery Address",
                new JScrollPane(addressArea));

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());

        JButton confirmButton = new JButton("Confirm Order");
        confirmButton.setBackground(primary);
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFont(new Font("Arial", Font.BOLD, 14));
        confirmButton.setFocusPainted(false);
        confirmButton.addActionListener(e -> confirmOrder());

        JPanel buttons = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttons.setOpaque(false);
        buttons.add(backButton);
        buttons.add(confirmButton);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.add(new JScrollPane(summary), BorderLayout.CENTER);
        center.add(form, BorderLayout.SOUTH);

        main.add(title, BorderLayout.NORTH);
        main.add(center, BorderLayout.CENTER);
        main.add(buttons, BorderLayout.SOUTH);

        add(main);
    }

    private void addFormRow(
            JPanel panel,
            GridBagConstraints c,
            int row,
            String labelText,
            Component input
    ) {
        c.gridy = row;
        c.gridx = 0;
        c.weightx = 0;
        panel.add(new JLabel(labelText), c);

        c.gridx = 1;
        c.weightx = 1;
        panel.add(input, c);
    }

    private void confirmOrder() {
        String name = nameField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();

        if (cartItems == null || cartItems.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Your cart is empty.",
                    "Empty Cart",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please fill in your name, phone number, "
                            + "and delivery address.",
                    "Missing Details",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (!phone.matches("[0-9]{10}")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid 10-digit phone number.",
                    "Invalid Phone Number",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String orderId = "MC" + UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();

        OrderManager.Order order = new OrderManager.Order(
                orderId,
                name,
                phone,
                address,
                pharmacyName,
                cartItems,
                itemPrices,
                calculateTotal()
        );

        // Save the order before showing success.
        boolean saved = OrderManager.saveOrder(order);

        if (!saved) {
            JOptionPane.showMessageDialog(
                    this,
                    "Order could not be saved. Please try again.",
                    "Save Failed",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        StringBuilder confirmation =
                new StringBuilder("Order placed successfully!\n\n");

        confirmation.append("Order ID: ")
                .append(orderId)
                .append("\n");

        if (pharmacyName != null && !pharmacyName.trim().isEmpty()) {
            confirmation.append("Pharmacy: ")
                    .append(pharmacyName)
                    .append("\n");
        }

        confirmation.append("Name: ").append(name)
                .append("\nPhone: ").append(phone)
                .append("\nTotal: ₹").append(calculateTotal())
                .append("\nStatus: Pending")
                .append("\n\nOrder saved to MediCart database.");

        JOptionPane.showMessageDialog(
                this,
                confirmation.toString(),
                "Order Confirmed",
                JOptionPane.INFORMATION_MESSAGE
        );

        if (onOrderPlaced != null) {
            onOrderPlaced.run();
        }

        dispose();
    }

    private int calculateTotal() {
        int total = 0;

        for (String medicine : cartItems.keySet()) {
            int quantity = cartItems.get(medicine);
            int price = itemPrices.getOrDefault(medicine, 0);
            total += quantity * price;
        }

        return total;
    }
}
