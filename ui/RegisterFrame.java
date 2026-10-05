package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class RegisterFrame extends JFrame {

    // ==============================
    // COLORS
    // ==============================

    private final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private final Color SECONDARY_COLOR = new Color(16, 185, 129);
    private final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private final Color TEXT_COLOR = new Color(31, 41, 55);
    private final Color WHITE_COLOR = Color.WHITE;


    public RegisterFrame() {

        // ==============================
        // WINDOW SETTINGS
        // ==============================

        setTitle("MediCart - Registration");
        setSize(550, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        // ==============================
        // MAIN PANEL
        // ==============================

        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(new BorderLayout());

        mainPanel.setBackground(BACKGROUND_COLOR);

        mainPanel.setBorder(
                new EmptyBorder(
                        25,
                        40,
                        25,
                        40
                )
        );


        // ==============================
        // HEADER
        // ==============================

        JPanel headerPanel = new JPanel();

        headerPanel.setLayout(
                new BoxLayout(
                        headerPanel,
                        BoxLayout.Y_AXIS
                )
        );

        headerPanel.setBackground(
                BACKGROUND_COLOR
        );


        JLabel titleLabel = new JLabel(
                "CREATE ACCOUNT"
        );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        titleLabel.setForeground(
                PRIMARY_COLOR
        );

        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel subtitleLabel = new JLabel(
                "Join MediCart today"
        );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        subtitleLabel.setForeground(
                TEXT_COLOR
        );

        subtitleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        headerPanel.add(titleLabel);

        headerPanel.add(
                Box.createVerticalStrut(5)
        );

        headerPanel.add(subtitleLabel);


        // ==============================
        // REGISTRATION CARD
        // ==============================

        JPanel registerCard = new JPanel();

        registerCard.setLayout(
                new GridBagLayout()
        );

        registerCard.setBackground(
                WHITE_COLOR
        );

        registerCard.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                new Color(220, 225, 230)
                        ),

                        new EmptyBorder(
                                20,
                                25,
                                20,
                                25
                        )
                )
        );


        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(5, 8, 5, 8);

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1.0;


        // ==============================
        // FULL NAME
        // ==============================

        JLabel nameLabel =
                new JLabel("Full Name");

        nameLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        nameLabel.setForeground(
                TEXT_COLOR
        );


        JTextField nameField =
                new JTextField();

        nameField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        nameField.setPreferredSize(
                new Dimension(
                        300,
                        35
                )
        );


        gbc.gridx = 0;
        gbc.gridy = 0;

        registerCard.add(
                nameLabel,
                gbc
        );


        gbc.gridy = 1;

        registerCard.add(
                nameField,
                gbc
        );


        // ==============================
        // EMAIL
        // ==============================

        JLabel emailLabel =
                new JLabel("Email Address");

        emailLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        emailLabel.setForeground(
                TEXT_COLOR
        );


        JTextField emailField =
                new JTextField();

        emailField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        emailField.setPreferredSize(
                new Dimension(
                        300,
                        35
                )
        );


        gbc.gridy = 2;

        registerCard.add(
                emailLabel,
                gbc
        );


        gbc.gridy = 3;

        registerCard.add(
                emailField,
                gbc
        );


        // ==============================
        // PASSWORD
        // ==============================

        JLabel passwordLabel =
                new JLabel("Password");

        passwordLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        passwordLabel.setForeground(
                TEXT_COLOR
        );


        JPasswordField passwordField =
                new JPasswordField();

        passwordField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        passwordField.setPreferredSize(
                new Dimension(
                        300,
                        35
                )
        );


        gbc.gridy = 4;

        registerCard.add(
                passwordLabel,
                gbc
        );


        gbc.gridy = 5;

        registerCard.add(
                passwordField,
                gbc
        );


        // ==============================
        // CONFIRM PASSWORD
        // ==============================

        JLabel confirmPasswordLabel =
                new JLabel("Confirm Password");

        confirmPasswordLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        confirmPasswordLabel.setForeground(
                TEXT_COLOR
        );


        JPasswordField confirmPasswordField =
                new JPasswordField();

        confirmPasswordField.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        confirmPasswordField.setPreferredSize(
                new Dimension(
                        300,
                        35
                )
        );


        gbc.gridy = 6;

        registerCard.add(
                confirmPasswordLabel,
                gbc
        );


        gbc.gridy = 7;

        registerCard.add(
                confirmPasswordField,
                gbc
        );


        // ==============================
        // ROLE
        // ==============================

        JLabel roleLabel =
                new JLabel("Account Type");

        roleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        roleLabel.setForeground(
                TEXT_COLOR
        );


        String[] roles = {
                "Customer",
                "Admin"
        };


        JComboBox<String> roleComboBox =
                new JComboBox<>(roles);

        roleComboBox.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        14
                )
        );

        roleComboBox.setPreferredSize(
                new Dimension(
                        300,
                        35
                )
        );


        gbc.gridy = 8;

        registerCard.add(
                roleLabel,
                gbc
        );


        gbc.gridy = 9;

        registerCard.add(
                roleComboBox,
                gbc
        );


        // ==============================
        // REGISTER BUTTON
        // ==============================

        JButton registerButton =
                new JButton("CREATE ACCOUNT");

        registerButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        registerButton.setForeground(
                WHITE_COLOR
        );

        registerButton.setBackground(
                SECONDARY_COLOR
        );

        registerButton.setFocusPainted(
                false
        );

        registerButton.setBorderPainted(
                false
        );

        registerButton.setPreferredSize(
                new Dimension(
                        300,
                        42
                )
        );

        registerButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        gbc.gridy = 10;

        gbc.insets =
                new Insets(
                        15,
                        8,
                        5,
                        8
                );

        registerCard.add(
                registerButton,
                gbc
        );


        // ==============================
        // BACK BUTTON
        // ==============================

        JButton backButton =
                new JButton("← Back to Home");

        backButton.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        backButton.setForeground(
                PRIMARY_COLOR
        );

        backButton.setFocusPainted(
                false
        );

        backButton.setBorderPainted(
                false
        );

        backButton.setContentAreaFilled(
                false
        );

        backButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        gbc.gridy = 11;

        gbc.insets =
                new Insets(
                        2,
                        8,
                        5,
                        8
                );

        registerCard.add(
                backButton,
                gbc
        );


        // ==============================
        // REGISTER ACTION
        // ==============================

        registerButton.addActionListener(e -> {

            String name =
                    nameField.getText().trim();

            String email =
                    emailField.getText().trim();

            String password =
                    new String(
                            passwordField.getPassword()
                    );

            String confirmPassword =
                    new String(
                            confirmPasswordField
                                    .getPassword()
                    );


            // Empty field validation
            if (
                    name.isEmpty()
                    || email.isEmpty()
                    || password.isEmpty()
                    || confirmPassword.isEmpty()
            ) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please fill all fields.",
                        "Registration Error",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }


            // Password validation
            if (!password.equals(confirmPassword)) {

                JOptionPane.showMessageDialog(
                        this,
                        "Passwords do not match.",
                        "Registration Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }


            // Registration success for now
            JOptionPane.showMessageDialog(
                    this,
                    "Registration details received!",
                    "MediCart",
                    JOptionPane.INFORMATION_MESSAGE
            );
        });


        // ==============================
        // BACK ACTION
        // ==============================

        backButton.addActionListener(e -> {

            dispose();

            HomeFrame homeFrame =
                    new HomeFrame();

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
                registerCard,
                BorderLayout.CENTER
        );


        // ==============================
        // ADD MAIN PANEL
        // ==============================

        add(mainPanel);
    }
}
