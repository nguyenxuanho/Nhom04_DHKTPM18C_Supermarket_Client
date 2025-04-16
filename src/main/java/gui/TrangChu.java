package gui;

import com.formdev.flatlaf.*;
import dto.TaiKhoanDTO;
import gui.panel.*;
import org.checkerframework.checker.units.qual.C;

import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;

public class TrangChu extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;

    public TrangChu() throws NamingException, RemoteException {

        try {
            // Set FlatLaf Light theme
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
//        setUndecorated(true);

        JLabel titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ SIÊU THỊ", SwingConstants.CENTER);
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


        JButton btnDashboard = createMenuButton("Trang chủ");
        JButton btnSanPham = createMenuButton("Sản phẩm");
        JButton btnKhachHang = createMenuButton("Khách hàng");
        JButton btnHoaDon = createMenuButton("Hóa đơn");
        JButton btnDanhMucSanPham = createMenuButton("Danh mục sản phẩm");
        JButton btnNhanVien = createMenuButton("Nhân viên");
        JButton btnTaiKhoan = createMenuButton("Tài khoản");


        menuPanel.add(btnDashboard);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnSanPham);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnDanhMucSanPham);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnKhachHang);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnHoaDon);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnNhanVien);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnTaiKhoan);

        menuPanel.add(Box.createVerticalGlue());

        JPanel userPanel = new JPanel();
        userPanel.setLayout(null);
        JPanel jPanel = new JPanel();
        jPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 10");
        jPanel.setBackground(new Color(0, 255, 255));
        jPanel.setBounds(10, 10, 240, 47);

        userPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));

        userPanel.setOpaque(false);
        userPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        userPanel.setLayout(null);


        userPanel.add(jPanel);
        jPanel.setLayout(null);

//        JLabel name = new JLabel(TaiKhoanDTO.getTaiKhoan().getNhanVien().getTenNhanVien());
        JLabel name = new JLabel("Xuân Hồ");

        name.setFont(new Font("Tahoma", Font.PLAIN, 15));
        name.setBounds(44, 10, 152, 24);
        name.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        jPanel.add(name);

        JLabel lblNewLabel_1 = new JLabel("");
        lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel_1.setBounds(194, 0, 46, 47);
        jPanel.add(lblNewLabel_1);
        lblNewLabel_1.setAlignmentX(Component.RIGHT_ALIGNMENT);
        lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel_1.setIcon(new ImageIcon(getClass().getResource("/image/logout.png")));

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
        lblNewLabel.setBounds(0, 0, 46, 47);
        jPanel.add(lblNewLabel);
        lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lblNewLabel.setIcon(new ImageIcon(getClass().getResource("/image/avatar.png")));
        menuPanel.add(userPanel);
        getContentPane().add(menuPanel, BorderLayout.WEST);

        jPanel.addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
                jPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn đăng xuất?", "Đăng xuất", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            // Ẩn form hiện tại
                            SwingUtilities.getWindowAncestor(jPanel).dispose();

                            // Mở form đăng nhập
                            try {
                                new LoginForm().setVisible(true);
                            } catch (NamingException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                });
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {}

            @Override
            public void ancestorMoved(AncestorEvent event) {}
        });


//        Chỗ thay đổi cái content của Dữ liệu.....
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        contentPanel.add(new PanelDashboard(), "dashboard");
        contentPanel.add(new PanelSanPham(), "sanpham");
        contentPanel.add(new PanelKhachHang(), "khachhang");
        contentPanel.add(new PanelHoaDon(), "hoadon");
        contentPanel.add(new PanelDanhMucSanPham(), "danhmucsanpham");
        contentPanel.add(new PanelNhanVien(), "nhanvien");
        contentPanel.add(new PanelTaiKhoan(), "taikhoan");

        add(contentPanel, BorderLayout.CENTER);

        btnDashboard.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "dashboard"));
        btnSanPham.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "sanpham"));
        btnKhachHang.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "khachhang"));
        btnHoaDon.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "hoadon"));
        btnDanhMucSanPham.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "danhmucsanpham"));
        btnNhanVien.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "nhanvien"));
        btnTaiKhoan.addActionListener((ActionEvent e) -> cardLayout.show(contentPanel, "taikhoan"));
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
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

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
