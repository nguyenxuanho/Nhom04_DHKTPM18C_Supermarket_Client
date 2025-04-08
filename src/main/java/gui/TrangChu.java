package gui;

import gui.panel.PanelHoaDon;
import gui.panel.PanelKhachHang;
import gui.panel.PanelSanPham;

import javax.naming.NamingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;

public class TrangChu extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public TrangChu() throws NamingException, RemoteException {
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
//        setUndecorated(true);

        JLabel titleLabel = new JLabel("Hệ Thống Quản Lý Siêu Thị", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        titleLabel.setOpaque(true);
        titleLabel.setBackground(new Color(33, 150, 243));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        add(titleLabel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(260, getHeight()));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(0, 123, 255));

        JLabel appTitle = new JLabel("MENU", SwingConstants.CENTER);
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        appTitle.setForeground(Color.WHITE);
        appTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(appTitle);


        JButton btnSanPham = createMenuButton("Sản phẩm");
        JButton btnKhachHang = createMenuButton("Khách hàng");
        JButton btnHoaDon = createMenuButton("Hóa đơn");

        menuPanel.add(btnSanPham);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnKhachHang);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnHoaDon);

        menuPanel.add(Box.createVerticalGlue());

        JPanel userPanel = new JPanel();
        userPanel.setOpaque(false);
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.X_AXIS));
        userPanel.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JLabel name = new JLabel("Mach Ngoc Xuan");
        name.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        userPanel.add(name);

        menuPanel.add(userPanel);
        add(menuPanel, BorderLayout.WEST);


//        Chỗ thay đổi cái content của Dữ liệu.....
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new PanelSanPham(), "sanpham");
        contentPanel.add(new PanelKhachHang(), "khachhang");
        contentPanel.add(new PanelHoaDon(), "hoadon");

        add(contentPanel, BorderLayout.CENTER);

        btnSanPham.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "sanpham"));
        btnKhachHang.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "khachhang"));
        btnHoaDon.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "hoadon"));
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(230, 45));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(0, 123, 255));
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 102, 204));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(0, 123, 255));
            }
        });

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new TrangChu().setVisible(true);
            } catch (NamingException e) {
                throw new RuntimeException(e);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
