package ui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;

public class PrescriptionFrame extends JFrame {

    private final Color primary = new Color(37, 99, 235);
    private final Color background = new Color(245, 247, 250);

    private JLabel fileLabel;
    private File selectedFile;

    public PrescriptionFrame() {

        setTitle("MediCart - Prescription Upload");
        setSize(600, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 20));
        mainPanel.setBackground(background);
        mainPanel.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel title = new JLabel("Upload Prescription");
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(primary);

        JLabel subtitle = new JLabel(
                "<html>Select a clear prescription file for review.<br>"
                + "A pharmacist must verify it where required.</html>"
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setOpaque(false);
        heading.add(title);
        heading.add(Box.createVerticalStrut(10));
        heading.add(subtitle);

        JPanel uploadCard = new JPanel();
        uploadCard.setLayout(new BoxLayout(uploadCard, BoxLayout.Y_AXIS));
        uploadCard.setBackground(Color.WHITE);
        uploadCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 225, 230)),
                new EmptyBorder(25, 20, 25, 20)
        ));

        JLabel uploadIcon = new JLabel("\u2B06");
        uploadIcon.setFont(new Font("Arial", Font.BOLD, 38));
        uploadIcon.setForeground(primary);
        uploadIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instruction = new JLabel(
                "Choose a prescription from your device");
        instruction.setFont(new Font("Arial", Font.BOLD, 15));
        instruction.setAlignmentX(Component.CENTER_ALIGNMENT);

        fileLabel = new JLabel("No file selected");
        fileLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        fileLabel.setForeground(Color.GRAY);
        fileLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton chooseButton = new JButton("Choose File");
        chooseButton.setBackground(primary);
        chooseButton.setForeground(Color.WHITE);
        chooseButton.setFocusPainted(false);
        chooseButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        chooseButton.addActionListener(e -> chooseFile());

        JLabel formats = new JLabel(
                "Supported formats: PDF, JPG, JPEG, PNG");
        formats.setFont(new Font("Arial", Font.PLAIN, 12));
        formats.setForeground(Color.GRAY);
        formats.setAlignmentX(Component.CENTER_ALIGNMENT);

        uploadCard.add(uploadIcon);
        uploadCard.add(Box.createVerticalStrut(15));
        uploadCard.add(instruction);
        uploadCard.add(Box.createVerticalStrut(12));
        uploadCard.add(fileLabel);
        uploadCard.add(Box.createVerticalStrut(15));
        uploadCard.add(chooseButton);
        uploadCard.add(Box.createVerticalStrut(12));
        uploadCard.add(formats);

        JButton submitButton = new JButton("Submit for Review");
        submitButton.setBackground(primary);
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);

        submitButton.addActionListener(e -> submitPrescription());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> dispose());

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttons.setOpaque(false);
        buttons.add(backButton);
        buttons.add(submitButton);

        mainPanel.add(heading, BorderLayout.NORTH);
        mainPanel.add(uploadCard, BorderLayout.CENTER);
        mainPanel.add(buttons, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void chooseFile() {

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Prescription");

        chooser.setFileFilter(
                new javax.swing.filechooser.FileNameExtensionFilter(
                        "Prescription files (PDF, JPG, JPEG, PNG)",
                        "pdf", "jpg", "jpeg", "png"
                )
        );

        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {

            File file = chooser.getSelectedFile();
            String name = file.getName().toLowerCase();

            boolean validFormat =
                    name.endsWith(".pdf")
                    || name.endsWith(".jpg")
                    || name.endsWith(".jpeg")
                    || name.endsWith(".png");

            if (!validFormat || !file.isFile()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please select a valid PDF, JPG, JPEG, or PNG file.",
                        "Invalid File",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            selectedFile = file;
            fileLabel.setText(file.getName());
        }
    }

    private void submitPrescription() {

        if (selectedFile == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please choose a prescription file first.",
                    "No File Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String customerName = JOptionPane.showInputDialog(
                this,
                "Enter customer name:", (service.AuthService.getCurrentUser() != null ? service.AuthService.getCurrentUser().getFullName() : "")
        );

        if (customerName == null || customerName.trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Customer name is required.",
                    "Missing Name",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        PrescriptionManager.addPrescription(
                customerName.trim(),
                selectedFile
        );

        JOptionPane.showMessageDialog(
                this,
                "Prescription submitted successfully!\n"
                + "Status: Pending\n"
                + "File: " + selectedFile.getName(),
                "Submitted for Review",
                JOptionPane.INFORMATION_MESSAGE
        );

        selectedFile = null;
        fileLabel.setText("No file selected");
    }
}

