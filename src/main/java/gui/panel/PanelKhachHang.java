package gui.panel;

import javax.swing.*;
import java.awt.*;

public class PanelKhachHang extends JPanel {
    public PanelKhachHang() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("👥 Giao diện Quản lý Khách hàng", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);
    }
}