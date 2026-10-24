package ui;

import service.AuthService;
import model.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    private final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private final Color TEXT_COLOR = new Color(31, 41, 55);
    private final Color WHITE_COLOR = Color.WHITE;

    private final AuthService authService = new AuthService();

    public LoginFrame() {

        setTitle("MediCart - Login");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 15));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(25, 35, 25, 35));

        // HEADER
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBackground(BACKGROUND_COLOR);

        JLabel titleLabel = new JLabel("MEDICART");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 32));
        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Login to your account");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitleLabel.setForeground(TEXT_COLOR);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(titleLabel);
        headerPanel.add(Box.createVerticalStrut(5));
        headerPanel.add(subtitleLabel);

        // LOGIN CARD
        JPanel loginCard = new JPanel(new GridBagLayout());
        loginCard.setBackground(WHITE_COLOR);
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(20, 25, 20, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.gridx = 0;

        // LOGIN TYPE
        JLabel roleLabel = new JLabel("Login As");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roleLabel.setForeground(TEXT_COLOR);

        String[] roles = {"Customer", "Admin"};
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 15));
        roleComboBox.setPreferredSize(new Dimension(300, 35));

        gbc.gridy = 0;
        loginCard.add(roleLabel, gbc);

        gbc.gridy = 1;
        loginCard.add(roleComboBox, gbc);

        // EMAIL
        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(TEXT_COLOR);

        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 15));
        emailField.setPreferredSize(new Dimension(300, 38));

        gbc.gridy = 2;
        loginCard.add(emailLabel, gbc);

        gbc.gridy = 3;
        loginCard.add(emailField, gbc);

        // PASSWORD
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_COLOR);

        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordField.setPreferredSize(new Dimension(300, 38));

        gbc.gridy = 4;
        loginCard.add(passwordLabel, gbc);

        gbc.gridy = 5;
        loginCard.add(passwordField, gbc);

        // SHOW PASSWORD
        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        showPassword.setBackground(WHITE_COLOR);

        showPassword.addActionListener(e -> {
            if (showPassword.isSelected()) {
                passwordField.setEchoChar((char) 0);
            } else {
                passwordField.setEchoChar('\u2022');
            }
        });

        gbc.gridy = 6;
        loginCard.add(showPassword, gbc);

        // LOGIN BUTTON
        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setForeground(WHITE_COLOR);
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(300, 42));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridy = 7;
        gbc.insets = new Insets(15, 8, 8, 8);
        loginCard.add(loginButton, gbc);

        // BACK BUTTON
        JButton backButton = new JButton("← Back to Home");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(PRIMARY_COLOR);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridy = 8;
        gbc.insets = new Insets(5, 8, 5, 8);
        loginCard.add(backButton, gbc);

        // LOGIN ACTION
        loginButton.addActionListener(e -> {

            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleComboBox.getSelectedItem();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter email and password.",
                        "Login Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // --- Authenticate ---
            User user;
            try {
                user = authService.login(email, password, role);
            } catch (AuthService.AuthenticationException authEx) {
                JOptionPane.showMessageDialog(
                        this,
                        authEx.getMessage(),
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            } catch (Exception dbEx) {
                dbEx.printStackTrace();
                String msg = dbEx.getMessage();
                if (msg == null || msg.isEmpty()) msg = dbEx.getClass().getName();
                JOptionPane.showMessageDialog(
                        this,
                        "Database connection error: " + msg,
                        "Connection Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // --- Login successful — open dashboard ---
            JOptionPane.showMessageDialog(
                    this,
                    role + " login successful! Welcome, " + user.getFullName() + ".",
                    "MediCart",
                    JOptionPane.INFORMATION_MESSAGE
            );

            dispose();

            try {
                if ("Admin".equalsIgnoreCase(role) || user.isAdmin()) {
                    AdminDashboard adminDashboard = new AdminDashboard();
                    adminDashboard.setVisible(true);
                } else {
                    CustomerDashboard dashboard = new CustomerDashboard();
                    dashboard.setVisible(true);
                }
            } catch (Exception dashEx) {
                dashEx.printStackTrace();
                String msg = dashEx.getMessage();
                if (msg == null || msg.isEmpty()) msg = dashEx.getClass().getName();
                JOptionPane.showMessageDialog(
                        null,
                        "Error opening dashboard: " + msg + "\nCheck console for details.",
                        "Dashboard Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // BACK ACTION
        backButton.addActionListener(e -> {
            dispose();
            HomeFrame homeFrame = new HomeFrame();
            homeFrame.setVisible(true);
        });

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(loginCard, BorderLayout.CENTER);

        add(mainPanel);
    }
}
