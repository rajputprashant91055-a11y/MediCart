
package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdminDashboard extends JFrame {

    private final Color primary = new Color(37, 99, 235);
    private final Color green = new Color(22, 163, 74);
    private final Color red = new Color(220, 38, 38);
    private final Color background = new Color(245, 247, 250);
    private final Color navy = new Color(17, 24, 39);

    private JTable prescriptionTable;
    private DefaultTableModel prescriptionModel;

    private JTable orderTable;
    private DefaultTableModel orderModel;

    private JComboBox<String> orderStatusBox;

    private static final String[] ORDER_STATUSES = {
        "Pending", "Confirmed", "Shipped", "Delivered", "Cancelled"
    };

    public AdminDashboard() {
        setTitle("MediCart - Admin Dashboard");
        setSize(1100, 700);
        setMinimumSize(new Dimension(850, 550));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(background);

        JLabel heading = new JLabel("  MEDICART - ADMIN DASHBOARD");
        heading.setFont(new Font("Arial", Font.BOLD, 25));
        heading.setForeground(Color.WHITE);
        heading.setOpaque(true);
        heading.setBackground(primary);
        heading.setBorder(new EmptyBorder(20, 12, 20, 12));

        root.add(heading, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(new Font("Arial", Font.BOLD, 14));

        tabs.addTab("Prescription Verification",
                createPrescriptionPanel());

        tabs.addTab("Order Management", createOrderPanel());

        root.add(tabs, BorderLayout.CENTER);

        setContentPane(root);

        loadPrescriptions();
        loadOrders();
    }

    // ================= PRESCRIPTION MANAGEMENT =================

    private JPanel createPrescriptionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 12));
        panel.setBackground(background);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Prescription Verification");
        title.setFont(new Font("Arial", Font.BOLD, 21));
        title.setForeground(navy);

        String[] columns = {
            "Customer", "Prescription File", "Status", "Admin Note"
        };

        prescriptionModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        prescriptionTable = new JTable(prescriptionModel);
        styleTable(prescriptionTable);

        JScrollPane scrollPane = new JScrollPane(prescriptionTable);

        JButton approveButton = createButton("Approve", green);
        JButton rejectButton = createButton("Reject", red);
        JButton noteButton = createButton("Add Admin Note", primary);
        JButton refreshButton = createButton("Refresh", navy);

        approveButton.addActionListener(e -> updatePrescriptionStatus("Approved"));
        rejectButton.addActionListener(e -> updatePrescriptionStatus("Rejected"));
        noteButton.addActionListener(e -> addAdminNote());
        refreshButton.addActionListener(e -> loadPrescriptions());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 5));
        buttons.setOpaque(false);
        buttons.add(approveButton);
        buttons.add(rejectButton);
        buttons.add(noteButton);
        buttons.add(refreshButton);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(buttons, BorderLayout.EAST);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void loadPrescriptions() {
        prescriptionModel.setRowCount(0);

        List<PrescriptionManager.Prescription> prescriptions =
                PrescriptionManager.getPrescriptions();

        for (PrescriptionManager.Prescription p : prescriptions) {
            prescriptionModel.addRow(new Object[]{
                p.getCustomerName(),
                p.getFileName(),
                p.getStatus(),
                p.getAdminNote()
            });
        }
    }

    private void updatePrescriptionStatus(String status) {
        int selectedRow = prescriptionTable.getSelectedRow();

        if (selectedRow == -1) {
            showMessage("Please select a prescription first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int row = prescriptionTable.convertRowIndexToModel(selectedRow);

        String currentStatus =
                String.valueOf(prescriptionModel.getValueAt(row, 2));

        if (!"Pending".equalsIgnoreCase(currentStatus)) {
            showMessage("This prescription has already been reviewed.",
                    "Already Reviewed", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        List<PrescriptionManager.Prescription> prescriptions =
                PrescriptionManager.getPrescriptions();

        if (row >= prescriptions.size()) {
            showMessage("Prescription data could not be found. Refresh and try again.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String note = JOptionPane.showInputDialog(
                this, "Enter admin review note:");

        if (note == null) {
            return;
        }

        PrescriptionManager.Prescription prescription = prescriptions.get(row);
        prescription.setStatus(status);
        prescription.setAdminNote(note.trim());

        loadPrescriptions();

        showMessage("Prescription status updated to " + status + ".",
                "Updated", JOptionPane.INFORMATION_MESSAGE);
    }

    private void addAdminNote() {
        int selectedRow = prescriptionTable.getSelectedRow();

        if (selectedRow == -1) {
            showMessage("Please select a prescription first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int row = prescriptionTable.convertRowIndexToModel(selectedRow);

        List<PrescriptionManager.Prescription> prescriptions =
                PrescriptionManager.getPrescriptions();

        if (row >= prescriptions.size()) {
            showMessage("Prescription data could not be found.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        PrescriptionManager.Prescription prescription = prescriptions.get(row);

        String note = JOptionPane.showInputDialog(
                this, "Enter admin note:", prescription.getAdminNote());

        if (note != null) {
            prescription.setAdminNote(note.trim());
            loadPrescriptions();
            showMessage("Admin note updated.", "Updated",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // ================= ORDER MANAGEMENT =================

    private JPanel createOrderPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 12));
        panel.setBackground(background);
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JLabel title = new JLabel("Customer Orders");
        title.setFont(new Font("Arial", Font.BOLD, 21));
        title.setForeground(navy);

        String[] columns = {
            "Order ID", "Customer", "Phone", "Pharmacy",
            "Items", "Total", "Status"
        };

        orderModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        orderTable = new JTable(orderModel);
        styleTable(orderTable);
        orderTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        orderTable.getColumnModel().getColumn(0).setPreferredWidth(115);
        orderTable.getColumnModel().getColumn(1).setPreferredWidth(120);
        orderTable.getColumnModel().getColumn(2).setPreferredWidth(105);
        orderTable.getColumnModel().getColumn(3).setPreferredWidth(135);
        orderTable.getColumnModel().getColumn(4).setPreferredWidth(250);
        orderTable.getColumnModel().getColumn(5).setPreferredWidth(85);
        orderTable.getColumnModel().getColumn(6).setPreferredWidth(105);

        JScrollPane scrollPane = new JScrollPane(orderTable);

        orderStatusBox = new JComboBox<>(ORDER_STATUSES);
        orderStatusBox.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton updateButton = createButton("Update Status", primary);
        JButton detailsButton = createButton("View Details", green);
        JButton refreshButton = createButton("Refresh Orders", navy);

        updateButton.addActionListener(e -> updateOrderStatus());
        detailsButton.addActionListener(e -> showOrderDetails());
        refreshButton.addActionListener(e -> loadOrders());

        JPanel controls = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 5));
        controls.setOpaque(false);

        controls.add(new JLabel("New status:"));
        controls.add(orderStatusBox);
        controls.add(updateButton);
        controls.add(detailsButton);
        controls.add(refreshButton);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(controls, BorderLayout.EAST);

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottom, BorderLayout.SOUTH);

        return panel;
    }

    private void loadOrders() {
        orderModel.setRowCount(0);

        ArrayList<OrderManager.Order> orders = OrderManager.getOrders();

        // Show newest orders first.
        for (int i = orders.size() - 1; i >= 0; i--) {
            OrderManager.Order order = orders.get(i);

            orderModel.addRow(new Object[]{
                order.orderId,
                order.customerName,
                order.phone,
                order.pharmacyName,
                formatItems(order.items),
                "₹" + order.total,
                order.status
            });
        }
    }

    private String formatItems(Map<String, Integer> items) {
        if (items == null || items.isEmpty()) {
            return "No items";
        }

        StringBuilder result = new StringBuilder();

        for (Map.Entry<String, Integer> item : items.entrySet()) {
            if (result.length() > 0) {
                result.append(", ");
            }

            result.append(item.getKey())
                    .append(" x")
                    .append(item.getValue());
        }

        return result.toString();
    }

    private OrderManager.Order getSelectedOrder() {
        int selectedRow = orderTable.getSelectedRow();

        if (selectedRow == -1) {
            showMessage("Please select an order first.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return null;
        }

        int row = orderTable.convertRowIndexToModel(selectedRow);
        String orderId = String.valueOf(orderModel.getValueAt(row, 0));

        for (OrderManager.Order order : OrderManager.getOrders()) {
            if (order.orderId.equals(orderId)) {
                return order;
            }
        }

        showMessage("Order not found. Please refresh the orders.",
                "Order Not Found", JOptionPane.ERROR_MESSAGE);
        return null;
    }

    private void updateOrderStatus() {
        OrderManager.Order order = getSelectedOrder();

        if (order == null) {
            return;
        }

        String newStatus = (String) orderStatusBox.getSelectedItem();

        if (newStatus == null) {
            return;
        }

        if (newStatus.equals(order.status)) {
            showMessage("This order is already marked " + newStatus + ".",
                    "No Change", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Update order " + order.orderId + " to " + newStatus + "?",
                "Confirm Status Change",
                JOptionPane.YES_NO_OPTION
        );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        boolean saved = OrderManager.updateStatus(order.orderId, newStatus);

        if (saved) {
            loadOrders();
            showMessage("Order status updated to " + newStatus + ".",
                    "Order Updated", JOptionPane.INFORMATION_MESSAGE);
        } else {
            showMessage("Could not save the status. Please try again.",
                    "Save Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showOrderDetails() {
        OrderManager.Order order = getSelectedOrder();

        if (order == null) {
            return;
        }

        String details =
                "Order ID: " + order.orderId
                + "\nCustomer: " + order.customerName
                + "\nPhone: " + order.phone
                + "\nAddress: " + order.address
                + "\nPharmacy: " + order.pharmacyName
                + "\n\nItems:\n" + formatItems(order.items)
                + "\n\nTotal: ₹" + order.total
                + "\nStatus: " + order.status;

        JTextArea area = new JTextArea(details, 12, 38);
        area.setEditable(false);
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setCaretPosition(0);

        JOptionPane.showMessageDialog(
                this,
                new JScrollPane(area),
                "Order Details",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ================= SHARED UI HELPERS =================

    private void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13));
        table.getTableHeader().setReorderingAllowed(false);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 13, 10, 13));
        return button;
    }

    private void showMessage(String message, String title, int type) {
        JOptionPane.showMessageDialog(this, message, title, type);
    }
}
