package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class LoginFrame extends JFrame {

    // Colors
    private final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private final Color TEXT_COLOR = new Color(31, 41, 55);
    private final Color WHITE_COLOR = Color.WHITE;

    public LoginFrame() {

        // ==============================
        // WINDOW SETTINGS
        // ==============================

        setTitle("MediCart - Login");
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        // ==============================
        // MAIN PANEL
        // ==============================

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(25, 35, 25, 35));


        // ==============================
        // HEADER
        // ==============================

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


        // ==============================
        // LOGIN CARD
        // ==============================

        JPanel loginCard = new JPanel();
        loginCard.setLayout(new GridBagLayout());
        loginCard.setBackground(WHITE_COLOR);
        loginCard.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                new Color(220, 225, 230)
                        ),
                        new EmptyBorder(25, 25, 25, 25)
                )
        );


        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;


        // ==============================
        // EMAIL
        // ==============================

        JLabel emailLabel = new JLabel("Email Address");
        emailLabel.setFont(new Font("Arial", Font.BOLD, 14));
        emailLabel.setForeground(TEXT_COLOR);


        JTextField emailField = new JTextField();
        emailField.setFont(new Font("Arial", Font.PLAIN, 15));
        emailField.setPreferredSize(new Dimension(300, 40));


        gbc.gridx = 0;
        gbc.gridy = 0;
        loginCard.add(emailLabel, gbc);


        gbc.gridy = 1;
        loginCard.add(emailField, gbc);


        // ==============================
        // PASSWORD
        // ==============================

        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
        passwordLabel.setForeground(TEXT_COLOR);


        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Arial", Font.PLAIN, 15));
        passwordField.setPreferredSize(new Dimension(300, 40));


        gbc.gridy = 2;
        loginCard.add(passwordLabel, gbc);


        gbc.gridy = 3;
        loginCard.add(passwordField, gbc);


        // ==============================
        // SHOW PASSWORD
        // ==============================

        JCheckBox showPassword = new JCheckBox("Show Password");
        showPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        showPassword.setBackground(WHITE_COLOR);


        showPassword.addActionListener(e -> {

            if (showPassword.isSelected()) {

                passwordField.setEchoChar((char) 0);

            } else {

                passwordField.setEchoChar('•');
            }
        });


        gbc.gridy = 4;
        loginCard.add(showPassword, gbc);


        // ==============================
        // LOGIN BUTTON
        // ==============================

        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Arial", Font.BOLD, 16));
        loginButton.setForeground(WHITE_COLOR);
        loginButton.setBackground(PRIMARY_COLOR);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(300, 45));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));


        gbc.gridy = 5;
        gbc.insets = new Insets(20, 8, 8, 8);
        loginCard.add(loginButton, gbc);


        // ==============================
        // BACK BUTTON
        // ==============================

        JButton backButton = new JButton("← Back to Home");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(PRIMARY_COLOR);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));


        gbc.gridy = 6;
        gbc.insets = new Insets(5, 8, 8, 8);
        loginCard.add(backButton, gbc);


        // ==============================
        // LOGIN ACTION
        // ==============================

        loginButton.addActionListener(e -> {

            String email = emailField.getText().trim();

            String password =
                    new String(passwordField.getPassword());


            if (email.isEmpty() || password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter email and password.",
                        "Login Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            JOptionPane.showMessageDialog(
                    this,
                    "Login details received!",
                    "MediCart",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });


        // ==============================
        // BACK ACTION
        // ==============================

        backButton.addActionListener(e -> {

            dispose();

            HomeFrame homeFrame = new HomeFrame();
            homeFrame.setVisible(true);
        });


        // ==============================
        // ADD COMPONENTS
        // ==============================

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                loginCard,
                BorderLayout.CENTER
        );


        add(mainPanel);
    }
}