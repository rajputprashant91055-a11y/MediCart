
package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class HomeFrame extends JFrame {

    private final Color blue = new Color(37, 99, 235);
    private final Color green = new Color(5, 150, 105);
    private final Color dark = new Color(20, 42, 75);
    private final Color muted = new Color(99, 116, 139);
    private final Color background = new Color(246, 249, 253);

    public HomeFrame() {
        setTitle("MediCart | Online Medicine Ordering");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1050, 700);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(background);

        // Top navigation
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(Color.WHITE);
        nav.setBorder(new EmptyBorder(18, 30, 18, 30));

        JLabel logo = new JLabel("✚ MediCart");
        logo.setFont(new Font("SansSerif", Font.BOLD, 27));
        logo.setForeground(green);

        JLabel navText = new JLabel("Your Health, Our Priority");
        navText.setFont(new Font("SansSerif", Font.PLAIN, 14));
        navText.setForeground(muted);

        nav.add(logo, BorderLayout.WEST);
        nav.add(navText, BorderLayout.EAST);

        // Main hero area
        JPanel hero = new JPanel(new GridLayout(1, 2, 20, 20));
        hero.setBackground(background);
        hero.setBorder(new EmptyBorder(35, 35, 25, 35));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(background);

        JLabel tag = new JLabel("  SMARTER MEDICINE SHOPPING  ");
        tag.setOpaque(true);
        tag.setBackground(new Color(220, 247, 238));
        tag.setForeground(green);
        tag.setFont(new Font("SansSerif", Font.BOLD, 12));
        tag.setBorder(new EmptyBorder(9, 8, 9, 8));
        tag.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel heading = new JLabel(
                "<html>Healthcare made<br>simpler with <font color='#059669'>MediCart</font></html>"
        );
        heading.setFont(new Font("SansSerif", Font.BOLD, 34));
        heading.setForeground(dark);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel description = new JLabel(
                "<html>Find medicines, explore pharmacy availability,<br>"
                + "and upload your doctor's prescription in one place.</html>"
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 15));
        description.setForeground(muted);
        description.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton login = makeButton("LOGIN", blue);
        JButton register = makeButton("CREATE ACCOUNT", green);

        login.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });

        register.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose();
        });

        left.add(Box.createVerticalStrut(25));
        left.add(tag);
        left.add(Box.createVerticalStrut(25));
        left.add(heading);
        left.add(Box.createVerticalStrut(18));
        left.add(description);
        left.add(Box.createVerticalStrut(30));
        left.add(login);
        left.add(Box.createVerticalStrut(14));
        left.add(register);
        left.add(Box.createVerticalStrut(28));

        JLabel note = new JLabel(
                "<html><font color='#059669'>✓</font> Easy to use"
                + "&nbsp;&nbsp;&nbsp; <font color='#059669'>✓</font> "
                + "Prescription support</html>"
        );
        note.setFont(new Font("SansSerif", Font.PLAIN, 13));
        note.setForeground(dark);
        note.setAlignmentX(Component.LEFT_ALIGNMENT);
        left.add(note);

        // Medicine illustration panel
        JPanel visual = new JPanel(new BorderLayout(10, 15));
        visual.setBackground(new Color(228, 243, 249));
        visual.setBorder(new EmptyBorder(25, 20, 25, 20));

        JLabel visualTitle = new JLabel(
                "<html><div style='text-align:center'>"
                + "<font color='#123456' size='5'><b>Medicines Made Easier</b></font>"
                + "<br><font color='#52677D' size='3'>Your everyday health companion</font>"
                + "</div></html>",
                SwingConstants.CENTER
        );

        JPanel illustration = new MedicineIllustration();
        illustration.setOpaque(false);

        JLabel visualFooter = new JLabel(
                "Find it. Order it. Get it.", SwingConstants.CENTER
        );
        visualFooter.setFont(new Font("SansSerif", Font.BOLD, 16));
        visualFooter.setForeground(green);

        visual.add(visualTitle, BorderLayout.NORTH);
        visual.add(illustration, BorderLayout.CENTER);
        visual.add(visualFooter, BorderLayout.SOUTH);

        hero.add(left);
        hero.add(visual);

        // Feature strip
        JPanel features = new JPanel(new GridLayout(1, 3, 15, 0));
        features.setBackground(Color.WHITE);
        features.setBorder(new EmptyBorder(20, 30, 20, 30));

        features.add(makeFeature("Medicine Search",
                "Explore medicine details and options"));
        features.add(makeFeature("Prescription Support",
                "Upload a doctor's prescription"));
        features.add(makeFeature("Pharmacy Response",
                "Check availability and order updates"));

        root.add(nav, BorderLayout.NORTH);
        root.add(hero, BorderLayout.CENTER);
        root.add(features, BorderLayout.SOUTH);

        setContentPane(root);
    }

    private JButton makeButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setPreferredSize(new Dimension(230, 45));
        button.setMaximumSize(new Dimension(250, 45));
        button.setMinimumSize(new Dimension(200, 45));
        button.setMargin(new Insets(12, 20, 12, 20));
        return button;
    }

    private JPanel makeFeature(String title, String detail) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel heading = new JLabel(title);
        heading.setFont(new Font("SansSerif", Font.BOLD, 15));
        heading.setForeground(dark);

        JLabel description = new JLabel(
                "<html>" + detail + "</html>"
        );
        description.setFont(new Font("SansSerif", Font.PLAIN, 12));
        description.setForeground(muted);

        panel.add(heading);
        panel.add(Box.createVerticalStrut(7));
        panel.add(description);
        return panel;
    }

    // Draws a simple medicine illustration using Java Swing.
    private class MedicineIllustration extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
            );

            int w = getWidth();
            int h = getHeight();
            int cx = w / 2;

            // Soft background circle
            g2.setColor(new Color(207, 234, 241));
            g2.fillOval(cx - 125, h / 2 - 120, 250, 250);

            // Medicine bottle
            int bottleX = cx - 52;
            int bottleY = h / 2 - 45;

            g2.setColor(new Color(8, 145, 178));
            g2.fillRoundRect(bottleX + 16, bottleY - 28,
                    72, 28, 8, 8);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(bottleX, bottleY, 104, 155,
                    20, 20);

            g2.setColor(new Color(205, 222, 232));
            g2.drawRoundRect(bottleX, bottleY, 104, 155,
                    20, 20);

            g2.setColor(new Color(5, 150, 105));
            g2.fillRoundRect(bottleX + 10, bottleY + 45,
                    84, 57, 8, 8);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 24));
            g2.drawString("+", bottleX + 38, bottleY + 80);

            // Left medicine box
            g2.setColor(new Color(37, 99, 235));
            g2.fillRoundRect(cx - 130, h / 2 + 65,
                    90, 75, 12, 12);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("SansSerif", Font.BOLD, 15));
            g2.drawString("MED", cx - 117, h / 2 + 108);

            // Right first-aid box
            g2.setColor(new Color(16, 185, 129));
            g2.fillRoundRect(cx + 62, h / 2 + 70,
                    85, 68, 12, 12);

            g2.setColor(Color.WHITE);
            g2.fillRect(cx + 96, h / 2 + 85, 16, 38);
            g2.fillRect(cx + 85, h / 2 + 96, 38, 16);

            // Pills
            g2.setColor(new Color(251, 191, 36));
            g2.fillRoundRect(cx - 85, h / 2 + 155,
                    46, 20, 20, 20);

            g2.setColor(new Color(96, 165, 250));
            g2.fillRoundRect(cx + 35, h / 2 + 155,
                    46, 20, 20, 20);

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new HomeFrame().setVisible(true)
        );
    }
}
