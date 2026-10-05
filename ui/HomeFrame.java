package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomeFrame extends JFrame {

    // ==============================
    // COLORS
    // ==============================

    private final Color PRIMARY_COLOR = new Color(37, 99, 235);
    private final Color SECONDARY_COLOR = new Color(16, 185, 129);
    private final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    private final Color TEXT_COLOR = new Color(31, 41, 55);
    private final Color WHITE_COLOR = Color.WHITE;


    public HomeFrame() {

        // ==============================
        // WINDOW SETTINGS
        // ==============================

        setTitle("MediCart - Online Medicine Shopping");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);


        // ==============================
        // MAIN PANEL
        // ==============================

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(
                new EmptyBorder(30, 40, 25, 40)
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

        headerPanel.setBackground(BACKGROUND_COLOR);


        JLabel titleLabel = new JLabel("MEDICART");

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        42
                )
        );

        titleLabel.setForeground(PRIMARY_COLOR);
        titleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel subtitleLabel = new JLabel(
                "Online Medicine Shopping System"
        );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        18
                )
        );

        subtitleLabel.setForeground(TEXT_COLOR);
        subtitleLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        headerPanel.add(titleLabel);

        headerPanel.add(
                Box.createVerticalStrut(5)
        );

        headerPanel.add(subtitleLabel);


        // ==============================
        // CENTER CARD
        // ==============================

        JPanel cardPanel = new JPanel();

        cardPanel.setLayout(
                new BoxLayout(
                        cardPanel,
                        BoxLayout.Y_AXIS
                )
        );

        cardPanel.setBackground(WHITE_COLOR);

        cardPanel.setBorder(
                BorderFactory.createCompoundBorder(

                        BorderFactory.createLineBorder(
                                new Color(220, 225, 230)
                        ),

                        new EmptyBorder(
                                35,
                                50,
                                35,
                                50
                        )
                )
        );


        // ==============================
        // WELCOME TEXT
        // ==============================

        JLabel welcomeLabel = new JLabel(
                "Welcome to MediCart"
        );

        welcomeLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        26
                )
        );

        welcomeLabel.setForeground(TEXT_COLOR);

        welcomeLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        JLabel descriptionLabel = new JLabel(
                "Your simple and convenient medicine shopping solution."
        );

        descriptionLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15
                )
        );

        descriptionLabel.setForeground(
                new Color(107, 114, 128)
        );

        descriptionLabel.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );


        cardPanel.add(welcomeLabel);

        cardPanel.add(
                Box.createVerticalStrut(8)
        );

        cardPanel.add(descriptionLabel);

        cardPanel.add(
                Box.createVerticalStrut(30)
        );


        // ==============================
        // LOGIN BUTTON
        // ==============================

        JButton loginButton = new JButton(
                "LOGIN"
        );

        loginButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        loginButton.setForeground(WHITE_COLOR);
        loginButton.setBackground(PRIMARY_COLOR);

        loginButton.setFocusPainted(false);

        loginButton.setBorderPainted(false);

        loginButton.setPreferredSize(
                new Dimension(280, 45)
        );

        loginButton.setMaximumSize(
                new Dimension(280, 45)
        );

        loginButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        loginButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        // ==============================
        // REGISTER BUTTON
        // ==============================

        JButton registerButton = new JButton(
                "REGISTER"
        );

        registerButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        registerButton.setForeground(
                WHITE_COLOR
        );

        registerButton.setBackground(
                SECONDARY_COLOR
        );

        registerButton.setFocusPainted(false);

        registerButton.setBorderPainted(false);

        registerButton.setPreferredSize(
                new Dimension(280, 45)
        );

        registerButton.setMaximumSize(
                new Dimension(280, 45)
        );

        registerButton.setAlignmentX(
                Component.CENTER_ALIGNMENT
        );

        registerButton.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );


        // ==============================
        // LOGIN ACTION
        // ==============================

        loginButton.addActionListener(e -> {

            LoginFrame loginFrame =
                    new LoginFrame();

            loginFrame.setVisible(true);

            dispose();
        });


        // ==============================
        // REGISTER ACTION
        // ==============================

        registerButton.addActionListener(e -> {

        RegisterFrame registerFrame =
            new RegisterFrame();

        registerFrame.setVisible(true);

        dispose();
    });


        // ==============================
        // ADD BUTTONS
        // ==============================

        cardPanel.add(loginButton);

        cardPanel.add(
                Box.createVerticalStrut(15)
        );

        cardPanel.add(registerButton);


        // ==============================
        // FOOTER
        // ==============================

        JLabel footerLabel = new JLabel(
                "Secure • Simple • Convenient"
        );

        footerLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        footerLabel.setForeground(
                new Color(107, 114, 128)
        );

        footerLabel.setHorizontalAlignment(
                SwingConstants.CENTER
        );


        // ==============================
        // ADD COMPONENTS TO MAIN PANEL
        // ==============================

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        mainPanel.add(
                cardPanel,
                BorderLayout.CENTER
        );

        mainPanel.add(
                footerLabel,
                BorderLayout.SOUTH
        );


        // ==============================
        // ADD MAIN PANEL TO WINDOW
        // ==============================

        add(mainPanel);
    }
}
