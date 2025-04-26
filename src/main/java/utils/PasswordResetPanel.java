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
        setLayout(new BorderLayout(10, 10));

        // Title panel at the top
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titleLabel = new JLabel("ĐẶT LẠI MẬT KHẨU");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Main form panel in the center
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));

        // Email row

        JPanel emailPanel = new JPanel(new FlowLayout());
        emailPanel.add(new JLabel("sdt"));
        phoneTxt = new JTextField(20);
        emailPanel.add(phoneTxt);
        emailPanel.add(new JLabel("Email:"));
        emailField = new JTextField(20);
        emailPanel.add(emailField);
        sendButton = new JButton("Gửi OTP");
        sendButton.addActionListener(new SendButtonListener());
        emailPanel.add(sendButton);
        formPanel.add(emailPanel);

        // OTP row (initially hidden)
        JPanel otpPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        otpLabel = new JLabel("Mã OTP:");
        otpLabel.setVisible(false);
        otpPanel.add(otpLabel);
        otpField = new JTextField(20);
        otpField.setVisible(false);
        otpPanel.add(otpField);
        verifyOtpButton = new JButton("Xác nhận OTP");
        verifyOtpButton.addActionListener(new VerifyOtpListener());
        verifyOtpButton.setVisible(false);
        otpPanel.add(verifyOtpButton);
        formPanel.add(otpPanel);

        // New password row (initially hidden)
        JPanel newPassPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        newPassLabel = new JLabel("Mật khẩu mới:");
        newPassLabel.setVisible(false);
        newPassPanel.add(newPassLabel);
        newPasswordField = new JPasswordField(20);
        newPasswordField.setVisible(false);
        newPassPanel.add(newPasswordField);
        formPanel.add(newPassPanel);

        // Confirm password row (initially hidden)
        JPanel confirmPassPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        confirmPassLabel = new JLabel("Xác nhận mật khẩu:");
        confirmPassLabel.setVisible(false);
        confirmPassPanel.add(confirmPassLabel);
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setVisible(false);
        confirmPassPanel.add(confirmPasswordField);
        formPanel.add(confirmPassPanel);

        // Confirm button row (initially hidden)
        JPanel confirmButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        confirmButton = new JButton("Xác nhận đổi mật khẩu");
        confirmButton.addActionListener(new ConfirmButtonListener());
        confirmButton.setVisible(false);
        confirmButtonPanel.add(confirmButton);
        formPanel.add(confirmButtonPanel);

        // Status label
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        statusLabel = new JLabel(" ");
        statusLabel.setForeground(Color.RED);
        statusPanel.add(statusLabel);
        formPanel.add(statusPanel);

        add(formPanel, BorderLayout.CENTER);

        // Back button at the bottom
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        backButton = new JButton("Quay lại đăng nhập");
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) getParent().getLayout();
            cl.show(getParent(), "login");
        });
        bottomPanel.add(backButton);
        add(bottomPanel, BorderLayout.SOUTH);
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