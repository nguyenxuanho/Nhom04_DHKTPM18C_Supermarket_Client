package gui.panel;

import javax.swing.*;
import java.awt.*;

public class PanelSanPham extends JPanel {
    public PanelSanPham() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("📦 Giao diện Quản lý Sản phẩm", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);
    }
}
