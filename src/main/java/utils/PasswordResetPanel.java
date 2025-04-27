package utils;

import gui.panel.RmiServiceLocator;
import model.NhanVien;
import model.TaiKhoan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PasswordResetPanel extends JPanel {
    private final JTextField phoneTxt;
    private JTextField emailField;
    private JButton sendButton;
    private JTextField otpField;
    private JPasswordField newPasswordField;
    private JPasswordField confirmPasswordField;
    private JButton confirmButton;
    private JLabel statusLabel;
    private JButton backButton;
    private NhanVien nhanVien;

    private JLabel otpLabel;
    private JButton verifyOtpButton;
    private JLabel newPassLabel;
    private JLabel confirmPassLabel;

    private String generatedOTP;
    private boolean otpVerified = false;

    public PasswordResetPanel() {
        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(245, 245, 245)); // Màu nền sáng

        // Title panel with logo and title
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(0, 102, 204)); // Màu xanh Co.opmart
        JLabel titleLabel = new JLabel("ĐẶT LẠI MẬT KHẨU", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titlePanel, BorderLayout.NORTH);

        // Main form panel in the center
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(245, 245, 245));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Khoảng cách giữa các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Phone and Email row
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        phoneTxt = new JTextField(20);
        phoneTxt.setPreferredSize(new Dimension(200, 30));
        phoneTxt.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        formPanel.add(phoneTxt, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(200, 30));


        emailField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        formPanel.add(emailField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        sendButton = new JButton("Gửi OTP");
        sendButton.setPreferredSize(new Dimension(200, 40));
        sendButton.setFont(new Font("Arial", Font.BOLD, 16));
        sendButton.setBackground(new Color(30, 144, 255));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFocusPainted(false);
        sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        sendButton.addActionListener(new SendButtonListener());
        formPanel.add(sendButton, gbc);

        // OTP row (initially hidden)
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        otpLabel = new JLabel("Mã OTP:");
        otpLabel.setVisible(false);
        formPanel.add(otpLabel, gbc);
        gbc.gridx = 1;
        otpField = new JTextField(20);
        otpField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        otpField.setVisible(false);
        formPanel.add(otpField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        verifyOtpButton = new JButton("Xác nhận OTP");

        verifyOtpButton.setPreferredSize(new Dimension(200, 40));
        verifyOtpButton.setFont(new Font("Arial", Font.BOLD, 16));
        verifyOtpButton.setBackground(new Color(30, 144, 255));
        verifyOtpButton.setForeground(Color.WHITE);
        verifyOtpButton.setFocusPainted(false);
        verifyOtpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));



        verifyOtpButton.addActionListener(new VerifyOtpListener());
        verifyOtpButton.setVisible(false);
        formPanel.add(verifyOtpButton, gbc);

        // New password row (initially hidden)
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 1;
        newPassLabel = new JLabel("Mật khẩu mới:");
        newPassLabel.setVisible(false);
        formPanel.add(newPassLabel, gbc);
        gbc.gridx = 1;
        newPasswordField = new JPasswordField(20);
        newPasswordField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        newPasswordField.setPreferredSize(new Dimension(200, 30));
        newPasswordField.setVisible(false);
        formPanel.add(newPasswordField, gbc);

        // Confirm password row (initially hidden)
        gbc.gridx = 0;
        gbc.gridy = 6;
        confirmPassLabel = new JLabel("Xác nhận mật khẩu:");
        confirmPassLabel.setVisible(false);
        formPanel.add(confirmPassLabel, gbc);
        gbc.gridx = 1;
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setPreferredSize(new Dimension(200, 30));
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(new Color(0, 102, 204), 1));
        confirmPasswordField.setVisible(false);
        formPanel.add(confirmPasswordField, gbc);

        // Confirm button row (initially hidden)
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        confirmButton = new JButton("Xác nhận đổi mật khẩu");
        confirmButton.setPreferredSize(new Dimension(200, 40));
        confirmButton.setFont(new Font("Arial", Font.BOLD, 16));
        confirmButton.setBackground(new Color(30, 144, 255));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setFocusPainted(false);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        confirmButton.addActionListener(new ConfirmButtonListener());
        confirmButton.setVisible(false);
        formPanel.add(confirmButton, gbc);

        // Status label
        gbc.gridx = 0;
        gbc.gridy = 8;
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        formPanel.add(statusLabel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // Back button at the bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(new Color(245, 245, 245));
        backButton = new JButton("Quay lại đăng nhập");
        backButton.setBorderPainted(false);
        backButton.setContentAreaFilled(false);
        backButton.setForeground(Color.BLUE);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));


        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) getParent().getLayout();
            cl.show(getParent(), "login");
        });
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);

        // Optional: Add Co.opmart logo on the left (if you have the image file)
        /*
        JLabel logoLabel = new JLabel(new ImageIcon("path/to/coopmart_logo.png"));
        add(logoLabel, BorderLayout.WEST);
        */
    }

    private class SendButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String email = emailField.getText().trim();
            String phone = phoneTxt.getText().trim();

            if(phone.equalsIgnoreCase("")) {
                JOptionPane.showMessageDialog(null, "Vui long nhap sdt");
            }

            // Simple email validation
            if (!email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                statusLabel.setText("Email không hợp lệ!");
                return;
            }

            try {
                nhanVien = RmiServiceLocator.getTaiKhoanService().getUserByPhone(phone);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            if(nhanVien == null) {
                JOptionPane.showMessageDialog(null, "Không nhân viên có số điện thoại này");
            } else {
                generatedOTP = EmailSender.sendOtp(email);
                // Show OTP fields
                otpLabel.setVisible(true);
                otpField.setVisible(true);
                verifyOtpButton.setVisible(true);
                statusLabel.setText("OTP đã được gửi đến email " + email);

                revalidate();
                repaint();
            }
        }
    }

    private class VerifyOtpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String enteredOTP = otpField.getText().trim();

            if (enteredOTP.equals(generatedOTP)) {
                otpVerified = true;

                newPassLabel.setVisible(true);
                newPasswordField.setVisible(true);
                confirmPassLabel.setVisible(true);
                confirmPasswordField.setVisible(true);
                confirmButton.setVisible(true);

                statusLabel.setText("OTP chính xác. Vui lòng nhập mật khẩu mới.");
            } else {
                statusLabel.setText("OTP không chính xác! Vui lòng thử lại.");
            }

            revalidate();
            repaint();
        }
    }

    private class ConfirmButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            if (newPassword.isEmpty()) {
                statusLabel.setText("Vui lòng nhập mật khẩu mới!");
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                statusLabel.setText("Mật khẩu xác nhận không khớp!");
                return;
            }

            if (newPassword.length() < 8) {
                statusLabel.setText("Mật khẩu phải có ít nhất 8 ký tự!");
                return;
            }

            try {
                TaiKhoan taiKhoan = RmiServiceLocator.getTaiKhoanService().taiKhoanByManv(nhanVien.getMaNhanVien());
                boolean resetpass = RmiServiceLocator.getTaiKhoanService().resetPassword(taiKhoan.getMaTaiKhoan(), newPassword);

                if (resetpass) {
                    JOptionPane.showMessageDialog(null, "Cập nhật mật khẩu thành công");
                    resetForm();
                    CardLayout cl = (CardLayout) getParent().getLayout();
                    cl.show(getParent(), "login");
                } else {
                    JOptionPane.showMessageDialog(null, "Không thể cập nhật mật khẩu");
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }

            // Password change logic would go here
            statusLabel.setText("Mật khẩu đã được thay đổi thành công!");
            statusLabel.setForeground(new Color(0, 100, 0));

            // Reset form
            resetForm();
        }
    }

    private void resetForm() {
        emailField.setText("");
        otpField.setText("");
        newPasswordField.setText("");
        confirmPasswordField.setText("");

        otpLabel.setVisible(false);
        otpField.setVisible(false);
        verifyOtpButton.setVisible(false);
        newPassLabel.setVisible(false);
        newPasswordField.setVisible(false);
        confirmPassLabel.setVisible(false);
        confirmPasswordField.setVisible(false);
        confirmButton.setVisible(false);

        revalidate();
        repaint();
    }

    private String generateOTP() {
        // Generate a 6-digit OTP
        return String.format("%06d", new java.util.Random().nextInt(999999));
    }
}