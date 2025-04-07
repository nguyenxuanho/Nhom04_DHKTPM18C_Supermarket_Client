package gui.panel;

import javax.swing.*;
import java.awt.*;

public class PanelHoaDon extends JPanel {
    public PanelHoaDon() {
        setLayout(new BorderLayout());
        JLabel label = new JLabel("🧾 Giao diện Quản lý Hóa đơn", SwingConstants.CENTER);
        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
        add(label, BorderLayout.CENTER);
        setBackground(Color.pink);
    }
}
