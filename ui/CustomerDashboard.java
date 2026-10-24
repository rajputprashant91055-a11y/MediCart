
package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import service.AuthService;
import service.MedicineService;

public class CustomerDashboard extends JFrame {

    private final MedicineService medicineService = new MedicineService();

    private final Color primary = new Color(37, 99, 235);
    private final Color green = new Color(5, 150, 105);
    private final Color background = new Color(246, 248, 251);
    private final Color textColor = new Color(31, 41, 55);
    private final Color navy = new Color(17, 24, 39);
    private final Color muted = new Color(100, 116, 139);
    private final Color red = new Color(220, 38, 38);
    private final Color borderColor = new Color(226, 232, 240);

    private JTextField searchField;
    private JPanel medicinePanel;
    private JButton cartButton;
    private JComboBox<String> sortBox;
    private JLabel resultLabel;

    private String selectedForm = "All Medicines";

    private final Map<String, CartItem> cart = new LinkedHashMap<>();

    /*
     * DEMO DATA ONLY.
     * Stock is not live inventory.
     */
    private final Medicine[] defaultMedicines = {
        new Medicine(
                "Amoxicillin",
                "Antibiotic medicine. Use only under appropriate "
                + "medical guidance.",
                80, "Capsules", "Amoxicillin.png", 20, true
        ),
        new Medicine(
                "Dermadol",
                "Topical medicine. Check the exact formulation, "
                + "ingredients and directions on the product label.",
                120, "Ointments", "Dermadol.png", 12, false
        ),
        new Medicine(
                "Syrup",
                "Liquid medicine. Confirm the product name, "
                + "strength and directions before use.",
                90, "Syrups", "Syrup.png", 0, false
        )
    };

    public CustomerDashboard() {
        setTitle("MediCart - Customer Dashboard");
        setSize(1180, 790);
        setMinimumSize(new Dimension(820, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBackground(background);
        root.setBorder(new EmptyBorder(15, 20, 20, 20));

        root.add(createTopBar(), BorderLayout.NORTH);

        medicinePanel = new JPanel(new GridLayout(0, 3, 16, 16));
        medicinePanel.setBackground(background);
        medicinePanel.setBorder(new EmptyBorder(5, 2, 12, 2));

        JScrollPane scrollPane = new JScrollPane(medicinePanel);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(background);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel catalog = new JPanel(new BorderLayout(0, 12));
        catalog.setOpaque(false);
        catalog.add(createCatalogHeader(), BorderLayout.NORTH);
        catalog.add(scrollPane, BorderLayout.CENTER);

        root.add(catalog, BorderLayout.CENTER);
        setContentPane(root);

        displayMedicines("");
    }

    // ================= TOP BAR =================

    private JPanel createTopBar() {
        JPanel bar = new JPanel(new BorderLayout(10, 10));
        bar.setBackground(Color.WHITE);
        bar.setBorder(new EmptyBorder(12, 16, 12, 16));

        JLabel logo = new JLabel("✚ MediCart");
        logo.setFont(new Font("Arial", Font.BOLD, 25));
        logo.setForeground(green);

        String welcomeTag = "  Your medicine marketplace";
        if (AuthService.getCurrentUser() != null) {
            welcomeTag = "  Welcome, " + AuthService.getCurrentUser().getFullName();
        }
        JLabel tagline = new JLabel(welcomeTag);
        tagline.setFont(new Font("Arial", Font.PLAIN, 12));
        tagline.setForeground(muted);

        JPanel brandPanel = new JPanel(new BorderLayout());
        brandPanel.setOpaque(false);
        brandPanel.add(logo, BorderLayout.NORTH);
        brandPanel.add(tagline, BorderLayout.SOUTH);

        cartButton = createButton("View Cart (0)", primary, Color.WHITE);
        cartButton.addActionListener(e -> showCart());

        JButton myOrdersButton = createButton(
                "My Orders",
                new Color(219, 234, 254),
                primary
        );
        myOrdersButton.addActionListener(e ->
                new MyOrdersFrame().setVisible(true)
        );

        JButton prescriptionButton = createButton(
                "Upload Prescription", green, Color.WHITE
        );
        prescriptionButton.addActionListener(e ->
                new PrescriptionFrame().setVisible(true)
        );

        JButton logoutButton = createButton(
                "Logout", new Color(229, 231, 235), textColor
        );
        logoutButton.addActionListener(e -> {
            dispose();
            new HomeFrame().setVisible(true);
        });

        JPanel actions = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );
        actions.setOpaque(false);
        actions.add(cartButton);
        actions.add(myOrdersButton);
        actions.add(prescriptionButton);
        actions.add(logoutButton);

        bar.add(brandPanel, BorderLayout.WEST);
        bar.add(actions, BorderLayout.EAST);

        return bar;
    }

    // ================= CATALOG HEADER =================

    private JPanel createCatalogHeader() {
        JPanel section = new JPanel(new BorderLayout(15, 14));
        section.setBackground(Color.WHITE);
        section.setBorder(new EmptyBorder(18, 17, 14, 17));

        JPanel titlePanel = new JPanel();
        titlePanel.setOpaque(false);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Medicine Catalog");
        title.setFont(new Font("Arial", Font.BOLD, 24));
        title.setForeground(navy);

        JLabel subtitle = new JLabel(
                "<html>Browse medicines, check demo availability "
                + "and manage your cart.</html>"
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 13));
        subtitle.setForeground(muted);

        titlePanel.add(title);
        titlePanel.add(Box.createVerticalStrut(6));
        titlePanel.add(subtitle);

        JPanel searchPanel = new JPanel(new BorderLayout(8, 0));
        searchPanel.setBackground(new Color(248, 250, 252));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                new EmptyBorder(5, 12, 5, 12)
        ));
        searchPanel.setPreferredSize(new Dimension(255, 40));

        JLabel searchIcon = new JLabel("⌕");
        searchIcon.setFont(new Font("Arial", Font.PLAIN, 24));
        searchIcon.setForeground(muted);

        searchField = new PlaceholderTextField("Search medicines...");
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        searchField.setBorder(null);
        searchField.setOpaque(false);
        searchField.setToolTipText(
                "Search by medicine name or description"
        );

        searchField.getDocument().addDocumentListener(
                new DocumentListener() {
                    @Override
                    public void insertUpdate(DocumentEvent e) {
                        searchMedicines();
                    }

                    @Override
                    public void removeUpdate(DocumentEvent e) {
                        searchMedicines();
                    }

                    @Override
                    public void changedUpdate(DocumentEvent e) {
                        searchMedicines();
                    }
                }
        );

        searchPanel.add(searchIcon, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        String[] sortOptions = {
            "Default Order",
            "Price: Low to High",
            "Price: High to Low",
            "Name: A to Z"
        };

        sortBox = new JComboBox<>(sortOptions);
        sortBox.setFont(new Font("Arial", Font.PLAIN, 13));
        sortBox.setBackground(Color.WHITE);
        sortBox.setPreferredSize(new Dimension(175, 40));
        sortBox.addActionListener(e -> searchMedicines());

        JPanel searchAndSort = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 8, 0)
        );
        searchAndSort.setOpaque(false);
        searchAndSort.add(searchPanel);
        searchAndSort.add(sortBox);

        JPanel top = new JPanel(new BorderLayout(15, 0));
        top.setOpaque(false);
        top.add(titlePanel, BorderLayout.CENTER);
        top.add(searchAndSort, BorderLayout.EAST);

        JPanel forms = new JPanel(new FlowLayout(FlowLayout.LEFT, 7, 0));
        forms.setOpaque(false);

        JLabel formLabel = new JLabel("CATEGORY:");
        formLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formLabel.setForeground(muted);
        forms.add(formLabel);

        String[] formNames = {
            "All Medicines", "Tablets", "Syrups",
            "Capsules", "Ointments", "Drops"
        };

        for (String form : formNames) {
            JButton tab = new JButton(form);
            tab.setFont(new Font("Arial", Font.PLAIN, 12));
            tab.setFocusPainted(false);
            tab.setBorderPainted(false);
            tab.setOpaque(true);
            tab.setCursor(new Cursor(Cursor.HAND_CURSOR));
            tab.setBorder(new EmptyBorder(9, 12, 9, 12));
            styleFormTab(tab, form.equals(selectedForm));

            tab.addActionListener(e -> {
                selectedForm = form;

                for (Component component : forms.getComponents()) {
                    if (component instanceof JButton) {
                        JButton other = (JButton) component;
                        styleFormTab(
                                other,
                                other.getText().equals(selectedForm)
                        );
                    }
                }
                searchMedicines();
            });

            forms.add(tab);
        }

        resultLabel = new JLabel();
        resultLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        resultLabel.setForeground(muted);

        JPanel categoryArea = new JPanel(new BorderLayout(0, 8));
        categoryArea.setOpaque(false);
        categoryArea.add(forms, BorderLayout.CENTER);
        categoryArea.add(resultLabel, BorderLayout.SOUTH);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.setBorder(new EmptyBorder(13, 0, 0, 0));
        bottom.add(categoryArea, BorderLayout.CENTER);

        section.add(top, BorderLayout.CENTER);
        section.add(bottom, BorderLayout.SOUTH);

        return section;
    }

    private void styleFormTab(JButton button, boolean selected) {
        button.setBackground(selected ? navy : Color.WHITE);
        button.setForeground(selected ? Color.WHITE : navy);
    }

    // ================= PLACEHOLDER FIELD =================

    private static class PlaceholderTextField extends JTextField {
        private final String placeholder;

        PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(new Color(148, 163, 184));
                g2.setFont(getFont().deriveFont(Font.PLAIN));

                Insets insets = getInsets();
                FontMetrics metrics = g2.getFontMetrics();
                int y = (getHeight() - metrics.getHeight()) / 2
                        + metrics.getAscent();

                g2.drawString(placeholder, insets.left + 2, y);
                g2.dispose();
            }
        }
    }

    // ================= BUTTON STYLE =================

    private JButton createButton(String text, Color bg, Color fg) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(bg);
        button.setForeground(fg);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 14, 10, 14));
        return button;
    }

    // ================= SEARCH, FILTER AND SORT =================

    private void displayMedicines(String query) {
        medicinePanel.removeAll();

        List<Medicine> filteredMedicines = new ArrayList<>();
        boolean loadedFromDb = false;
        try {
            String sortOption = sortBox != null ? (String) sortBox.getSelectedItem() : "Default Order";
            List<model.Medicine> dbList = medicineService.searchMedicines(query, selectedForm, sortOption);
            for (model.Medicine m : dbList) {
                filteredMedicines.add(new Medicine(
                        m.getName(),
                        m.getDescription(),
                        (int) Math.round(m.getPrice()),
                        m.getCategory(),
                        m.getImageFile(),
                        m.getAvailableStock(),
                        m.isPrescriptionRequired()
                ));
            }
            loadedFromDb = true;
        } catch (Exception e) {
            System.err.println("Database load error, using fallback: " + e.getMessage());
        }

        if (!loadedFromDb) {

        for (Medicine medicine : defaultMedicines) {
            boolean matchesSearch =
                    medicine.name.toLowerCase().contains(query)
                    || medicine.description.toLowerCase().contains(query)
                    || medicine.form.toLowerCase().contains(query);

            boolean matchesForm =
                    selectedForm.equals("All Medicines")
                    || medicine.form.equalsIgnoreCase(selectedForm);

            if (matchesSearch && matchesForm) {
                filteredMedicines.add(medicine);
            }
        }

        }

        if (!loadedFromDb && sortBox != null) {
            String sortOption = (String) sortBox.getSelectedItem();

            if ("Price: Low to High".equals(sortOption)) {
                filteredMedicines.sort(
                        (a, b) -> Integer.compare(a.price, b.price)
                );
            } else if ("Price: High to Low".equals(sortOption)) {
                filteredMedicines.sort(
                        (a, b) -> Integer.compare(b.price, a.price)
                );
            } else if ("Name: A to Z".equals(sortOption)) {
                filteredMedicines.sort(
                        (a, b) -> a.name.compareToIgnoreCase(b.name)
                );
            }
        }

        for (Medicine medicine : filteredMedicines) {
            medicinePanel.add(createMedicineCard(medicine));
        }

        if (filteredMedicines.isEmpty()) {
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.setBorder(new EmptyBorder(35, 20, 35, 20));

            JLabel noResults = new JLabel(
                    "<html><div style='text-align:center'>"
                    + "<b>No medicines found</b><br>"
                    + "Try another search or category."
                    + "</div></html>",
                    SwingConstants.CENTER
            );
            noResults.setFont(new Font("Arial", Font.PLAIN, 16));
            noResults.setForeground(muted);
            emptyPanel.add(noResults, BorderLayout.CENTER);
            medicinePanel.add(emptyPanel);
        }

        if (resultLabel != null) {
            resultLabel.setText(
                    filteredMedicines.size() + " medicine(s) found"
            );
        }

        medicinePanel.revalidate();
        medicinePanel.repaint();
    }

    private void searchMedicines() {
        if (searchField == null || medicinePanel == null) {
            return;
        }

        displayMedicines(searchField.getText().trim().toLowerCase());
    }

    // ================= MEDICINE CARD =================

    private JPanel createMedicineCard(Medicine medicine) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(borderColor),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 115));
        imageLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 115));
        imageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        boolean hasImage = medicine.imageFile != null && !medicine.imageFile.trim().isEmpty();
        File imageFile = hasImage ? new File("images", medicine.imageFile) : null;

        if (hasImage && imageFile.isFile()) {
            ImageIcon original = new ImageIcon(imageFile.getAbsolutePath());
            Image scaled = original.getImage().getScaledInstance(
                    150, 105, Image.SCALE_SMOOTH
            );
            imageLabel.setIcon(new ImageIcon(scaled));
        } else {
            imageLabel.setOpaque(true);
            imageLabel.setBackground(new Color(228, 243, 249));
            imageLabel.setForeground(green);
            imageLabel.setFont(new Font("Arial", Font.BOLD, 21));
            imageLabel.setText("✚ MediCart");
        }

        JLabel nameLabel = new JLabel(medicine.name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 17));
        nameLabel.setForeground(textColor);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel formLabel = new JLabel(medicine.form);
        formLabel.setFont(new Font("Arial", Font.BOLD, 12));
        formLabel.setForeground(green);
        formLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel descriptionLabel = new JLabel(
                "<html><div style='width:220px'>"
                + medicine.description + "</div></html>"
        );
        descriptionLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descriptionLabel.setForeground(muted);
        descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel priceLabel = new JLabel("₹" + medicine.price);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 20));
        priceLabel.setForeground(primary);
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel stockLabel = new JLabel(getStockText(medicine));
        stockLabel.setFont(new Font("Arial", Font.BOLD, 12));
        stockLabel.setForeground(medicine.stock > 0 ? green : red);
        stockLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel quantityPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT, 7, 0)
        );
        quantityPanel.setOpaque(false);
        quantityPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel quantityLabel = new JLabel("Quantity:");
        quantityLabel.setFont(new Font("Arial", Font.PLAIN, 12));

        SpinnerNumberModel quantityModel = new SpinnerNumberModel(
                1, 1, Math.max(1, medicine.stock), 1
        );

        JSpinner quantitySpinner = new JSpinner(quantityModel);
        quantitySpinner.setPreferredSize(new Dimension(65, 29));
        quantitySpinner.setEnabled(medicine.stock > 0);

        quantityPanel.add(quantityLabel);
        quantityPanel.add(quantitySpinner);

        JButton detailsButton = createButton(
                "View Details", new Color(229, 231, 235), textColor
        );
        detailsButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        detailsButton.addActionListener(e -> showMedicineDetails(medicine));

        JButton addButton = createButton(
                "Add to Cart", primary, Color.WHITE
        );
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.setEnabled(medicine.stock > 0);
        addButton.addActionListener(e ->
                addToCart(medicine, (Integer) quantitySpinner.getValue())
        );

        card.add(imageLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(nameLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(formLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(descriptionLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(priceLabel);
        card.add(Box.createVerticalStrut(5));
        card.add(stockLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(quantityPanel);
        card.add(Box.createVerticalStrut(9));
        card.add(detailsButton);
        card.add(Box.createVerticalStrut(7));
        card.add(addButton);

        return card;
    }

    private String getStockText(Medicine medicine) {
        if (medicine.stock <= 0) {
            return "● Out of stock";
        }
        return "● In stock: " + medicine.stock;
    }

    // ================= MEDICINE DETAILS =================

    private void showMedicineDetails(Medicine medicine) {
        JDialog dialog = new JDialog(
                this, medicine.name + " - Details", true
        );
        dialog.setSize(430, 390);
        dialog.setMinimumSize(new Dimension(380, 340));
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(22, 24, 22, 24));

        JLabel title = new JLabel(medicine.name);
        title.setFont(new Font("Arial", Font.BOLD, 23));
        title.setForeground(navy);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel form = new JLabel("Category: " + medicine.form);
        form.setFont(new Font("Arial", Font.PLAIN, 14));
        form.setForeground(green);
        form.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel price = new JLabel("Price: ₹" + medicine.price);
        price.setFont(new Font("Arial", Font.BOLD, 19));
        price.setForeground(primary);
        price.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel stock = new JLabel(getStockText(medicine));
        stock.setFont(new Font("Arial", Font.BOLD, 13));
        stock.setForeground(medicine.stock > 0 ? green : red);
        stock.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea description = new JTextArea(medicine.description);
        description.setEditable(false);
        description.setLineWrap(true);
        description.setWrapStyleWord(true);
        description.setFont(new Font("Arial", Font.PLAIN, 14));
        description.setForeground(textColor);
        description.setBackground(Color.WHITE);
        description.setFocusable(false);
        description.setRows(4);

        JButton closeButton = createButton(
                "Close", new Color(229, 231, 235), textColor
        );
        closeButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        closeButton.addActionListener(e -> dialog.dispose());

        panel.add(title);
        panel.add(Box.createVerticalStrut(12));
        panel.add(form);
        panel.add(Box.createVerticalStrut(10));
        panel.add(price);
        panel.add(Box.createVerticalStrut(10));
        panel.add(stock);
        panel.add(Box.createVerticalStrut(15));
        panel.add(new JLabel("Product information"));
        panel.add(Box.createVerticalStrut(7));
        panel.add(description);
        panel.add(Box.createVerticalStrut(20));
        panel.add(closeButton);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    // ================= CART ACTIONS =================

    private void addToCart(Medicine medicine, int quantity) {
        if (medicine.stock <= 0 || quantity < 1) {
            JOptionPane.showMessageDialog(
                    this,
                    "This item is currently out of stock.",
                    "Unavailable",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        CartItem existing = cart.get(medicine.name);
        int alreadyInCart = existing == null ? 0 : existing.quantity;

        if (alreadyInCart + quantity > medicine.stock) {
            JOptionPane.showMessageDialog(
                    this,
                    "Only " + medicine.stock
                    + " unit(s) are available across registered pharmacies.\n"
                    + "Already in cart: " + alreadyInCart,
                    "Quantity Limit",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        if (existing == null) {
            CartItem newItem = new CartItem(medicine);
            newItem.quantity = quantity;
            cart.put(medicine.name, newItem);
        } else {
            existing.quantity += quantity;
        }

        updateCartButton();

        JOptionPane.showMessageDialog(
                this,
                quantity + " unit(s) of " + medicine.name
                + " added to your cart.",
                "Added to Cart",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void updateCartButton() {
        if (cartButton == null) {
            return;
        }

        cartButton.setText("View Cart (" + getCartQuantity() + ")");
    }

    private int getCartTotal() {
        int total = 0;
        for (CartItem item : cart.values()) {
            total += item.medicine.price * item.quantity;
        }
        return total;
    }

    private int getCartQuantity() {
        int quantity = 0;
        for (CartItem item : cart.values()) {
            quantity += item.quantity;
        }
        return quantity;
    }

    // ================= VIEW CART =================

    private void showCart() {
        JDialog dialog = new JDialog(this, "Your Shopping Cart", true);
        dialog.setSize(760, 490);
        dialog.setMinimumSize(new Dimension(580, 370));
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));

        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBorder(new EmptyBorder(10, 12, 10, 12));
        listPanel.setBackground(Color.WHITE);

        JLabel totalLabel = new JLabel();
        totalLabel.setFont(new Font("Arial", Font.BOLD, 20));
        totalLabel.setForeground(primary);

        JLabel quantitySummary = new JLabel();
        quantitySummary.setFont(new Font("Arial", Font.PLAIN, 12));
        quantitySummary.setForeground(muted);

        JButton continueButton = createButton(
                "Continue Shopping", new Color(229, 231, 235), textColor
        );
        continueButton.addActionListener(e -> dialog.dispose());

        JButton clearButton = createButton(
                "Clear Cart", new Color(254, 226, 226), red
        );

        JButton orderButton = createButton(
                "Continue to Order", primary, Color.WHITE
        );

        JPanel totalArea = new JPanel();
        totalArea.setLayout(new BoxLayout(totalArea, BoxLayout.Y_AXIS));
        totalArea.setOpaque(false);
        totalArea.add(totalLabel);
        totalArea.add(Box.createVerticalStrut(4));
        totalArea.add(quantitySummary);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 0));
        bottomPanel.setBorder(new EmptyBorder(8, 12, 12, 12));
        bottomPanel.add(totalArea, BorderLayout.WEST);

        JPanel bottomButtons = new JPanel(
                new FlowLayout(FlowLayout.RIGHT, 7, 0)
        );
        bottomButtons.add(clearButton);
        bottomButtons.add(continueButton);
        bottomButtons.add(orderButton);
        bottomPanel.add(bottomButtons, BorderLayout.EAST);

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        dialog.add(scrollPane, BorderLayout.CENTER);
        dialog.add(bottomPanel, BorderLayout.SOUTH);

        Runnable[] refreshCart = new Runnable[1];

        refreshCart[0] = () -> {
            listPanel.removeAll();

            if (cart.isEmpty()) {
                JLabel emptyLabel = new JLabel(
                        "<html><b>Your cart is empty.</b><br>"
                        + "Add medicines from the catalog to get started."
                        + "</html>"
                );
                emptyLabel.setFont(new Font("Arial", Font.PLAIN, 16));
                emptyLabel.setForeground(muted);
                emptyLabel.setBorder(new EmptyBorder(25, 15, 25, 15));
                listPanel.add(emptyLabel);
            } else {
                List<CartItem> currentItems =
                        new ArrayList<>(cart.values());

                for (CartItem item : currentItems) {
                    String medicineName = item.medicine.name;

                    JPanel row = new JPanel(new BorderLayout(12, 6));
                    row.setBackground(Color.WHITE);
                    row.setBorder(new EmptyBorder(12, 6, 12, 6));

                    JPanel details = new JPanel();
                    details.setLayout(
                            new BoxLayout(details, BoxLayout.Y_AXIS)
                    );
                    details.setOpaque(false);

                    JLabel name = new JLabel(item.medicine.name);
                    name.setFont(new Font("Arial", Font.BOLD, 15));
                    name.setForeground(navy);

                    JLabel unitPrice = new JLabel(
                            "₹" + item.medicine.price + " per unit"
                    );
                    unitPrice.setFont(new Font("Arial", Font.PLAIN, 12));
                    unitPrice.setForeground(muted);

                    JLabel lineTotal = new JLabel(
                            "Line total: ₹"
                            + (item.medicine.price * item.quantity)
                    );
                    lineTotal.setFont(new Font("Arial", Font.BOLD, 13));
                    lineTotal.setForeground(primary);

                    details.add(name);
                    details.add(Box.createVerticalStrut(4));
                    details.add(unitPrice);
                    details.add(Box.createVerticalStrut(4));
                    details.add(lineTotal);

                    JButton minusButton = new JButton("−");
                    JButton plusButton = new JButton("+");
                    JButton removeButton = new JButton("Remove");

                    minusButton.addActionListener(e -> {
                        CartItem current = cart.get(medicineName);
                        if (current != null) {
                            current.quantity--;
                            if (current.quantity <= 0) {
                                cart.remove(medicineName);
                            }
                        }
                        updateCartButton();
                        refreshCart[0].run();
                    });

                    plusButton.addActionListener(e -> {
                        CartItem current = cart.get(medicineName);
                        if (current != null) {
                            if (current.quantity >= current.medicine.stock) {
                                JOptionPane.showMessageDialog(
                                        dialog,
                                        "Demo stock limit reached.",
                                        "Quantity Limit",
                                        JOptionPane.WARNING_MESSAGE
                                );
                                return;
                            }
                            current.quantity++;
                        }
                        updateCartButton();
                        refreshCart[0].run();
                    });

                    removeButton.addActionListener(e -> {
                        cart.remove(medicineName);
                        updateCartButton();
                        refreshCart[0].run();
                    });

                    JPanel quantityControls = new JPanel(
                            new FlowLayout(FlowLayout.RIGHT, 5, 0)
                    );
                    quantityControls.setOpaque(false);

                    JLabel quantityLabel = new JLabel(
                            "Qty: " + item.quantity
                    );
                    quantityLabel.setFont(
                            new Font("Arial", Font.BOLD, 12)
                    );

                    minusButton.setEnabled(item.quantity > 0);
                    plusButton.setEnabled(
                            item.quantity < item.medicine.stock
                    );

                    quantityControls.add(minusButton);
                    quantityControls.add(quantityLabel);
                    quantityControls.add(plusButton);
                    quantityControls.add(removeButton);

                    row.add(details, BorderLayout.CENTER);
                    row.add(quantityControls, BorderLayout.EAST);

                    listPanel.add(row);
                    listPanel.add(new JSeparator());
                }
            }

            totalLabel.setText("Total: ₹" + getCartTotal());
            quantitySummary.setText("Total items: " + getCartQuantity());

            orderButton.setEnabled(!cart.isEmpty());
            clearButton.setEnabled(!cart.isEmpty());

            listPanel.revalidate();
            listPanel.repaint();
            dialog.revalidate();
            dialog.repaint();
        };

        clearButton.addActionListener(e -> {
            if (cart.isEmpty()) {
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    dialog,
                    "Remove all items from your cart?",
                    "Clear Cart",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
            );

            if (choice == JOptionPane.YES_OPTION) {
                cart.clear();
                updateCartButton();
                refreshCart[0].run();
            }
        });

        orderButton.addActionListener(e -> {
            if (cart.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Your cart is empty.");
                return;
            }

            Map<String, Integer> cartItems = new LinkedHashMap<>();
            Map<String, Integer> itemPrices = new LinkedHashMap<>();

            for (CartItem item : cart.values()) {
                if (item.quantity > item.medicine.stock
                        || item.medicine.stock <= 0) {
                    JOptionPane.showMessageDialog(
                            dialog,
                            "Stock limit exceeded for "
                            + item.medicine.name + ".",
                            "Stock Check",
                            JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                cartItems.put(item.medicine.name, item.quantity);
                itemPrices.put(item.medicine.name, item.medicine.price);
            }

            SmartPharmacyMatcher.MatchResult matchResult =
                    SmartPharmacyMatcher.findFullMatch(cartItems);

            if (!matchResult.isFullMatch()) {
                JOptionPane.showMessageDialog(
                        dialog,
                        matchResult.getMessage(),
                        "No Complete Pharmacy Match",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int choice = JOptionPane.showConfirmDialog(
                    dialog,
                    matchResult.getMessage()
                            + "\n\nEstimated cart total: ₹" + getCartTotal()
                            + "\n\nContinue to order details?",
                    "Pharmacy Matched",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            dialog.dispose();

            OrderFrame orderFrame = new OrderFrame(
                    cartItems,
                    itemPrices,
                    () -> {
                        cart.clear();
                        updateCartButton();
                    },
                    matchResult.getPharmacyName()
            );

            orderFrame.setVisible(true);
        });

        refreshCart[0].run();
        dialog.setVisible(true);
    }

    // ================= MEDICINE MODEL =================

    private static class Medicine {
        String name;
        String description;
        int price;
        String form;
        String imageFile;
        int stock;
        boolean prescriptionRequired;

        Medicine(
                String name,
                String description,
                int price,
                String form,
                String imageFile,
                int stock,
                boolean prescriptionRequired
        ) {
            this.name = name;
            this.description = description;
            this.price = price;
            this.form = form;
            this.imageFile = imageFile;
            this.stock = stock;
            this.prescriptionRequired = prescriptionRequired;
        }
    }

    private static class CartItem {
        Medicine medicine;
        int quantity;

        CartItem(Medicine medicine) {
            this.medicine = medicine;
            this.quantity = 1;
        }
    }

    // ================= OPTIONAL DIRECT TEST =================

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new CustomerDashboard().setVisible(true)
        );
    }
}
