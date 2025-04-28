package gui;

import com.formdev.flatlaf.*;
import dto.TaiKhoanDTO;
import gui.panel.*;
import model.NhanVien;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URISyntaxException;
import java.rmi.RemoteException;

import static gui.panel.RmiServiceLocator.nhanVienService;

public class TrangChu extends JFrame {
    private CardLayout cardLayout;
    private JPanel contentPanel;
    private JButton activeButton;

    private static PanelTaiKhoan panelTaiKhoan;
    private static PanelDashboard panelDashboard;
    private static PanelNhanVien panelNhanVien;
    private static PanelThongKe panelThongKe;

    private void setActiveButton(JButton button) {
        if (activeButton != null) {
            activeButton.setBackground(Color.WHITE); // Không active thì trắng
        }
        activeButton = button;
        if (activeButton != null) {
            activeButton.setBackground(new Color(210, 227, 252)); // Màu đang chọn
        }
    }


    public TrangChu() throws NamingException, RemoteException, URISyntaxException {
        NhanVien nhanVien = nhanVienService.getNhanVienById(TaiKhoanDTO.getTaiKhoan().getNhanVien().getMaNhanVien());

        try {
            // Set FlatLaf Light theme
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Button.arc", 60);
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }


        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
//        setUndecorated(true);

        // Thay thế phần JLabel titleLabel bằng đoạn code sau:
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(33, 150, 243));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

// Phần bên trái - Tiêu đề hệ thống
        // Tạo panel chứa tiêu đề
        JPanel titleContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titleContainer.setOpaque(false);
        titleContainer.setBorder(BorderFactory.createEmptyBorder(5, 20, 5, 0));

// Tạo label tiêu đề với hiệu ứng nâng cao
        JLabel titleLabel = new JLabel("HỆ THỐNG QUẢN LÝ SIÊU THỊ") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                // Hiệu ứng bóng đổ
                g2.setColor(new Color(0, 0, 0, 30));
                g2.drawString(getText(), 3, 28);

                // Gradient cho chữ
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255, 255, 255, 240),
                        0, getHeight(), new Color(255, 255, 255, 220));
                g2.setPaint(gp);

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);

                super.paintComponent(g2);
                g2.dispose();
            }
        };

// Cài đặt font và style

        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(255, 255, 255, 220));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 3, 0));

// Thêm icon bên trái tiêu đề (tùy chọn)

        ImageIcon IconHeader = new ImageIcon(getClass().getResource("/image/store.png"));

        // Resize icon (30x30 pixels)
        Image scaledImageHeader = IconHeader.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImageHeader));
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10)); // Khoảng cách với text


        titleContainer.add(iconLabel);
        titleContainer.add(titleLabel);
        headerPanel.add(titleContainer, BorderLayout.WEST);

// Hiệu ứng hover (tùy chọn)
        titleLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                titleLabel.setForeground(Color.WHITE);
                titleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                titleLabel.setForeground(new Color(255, 255, 255, 220));
            }
        });



// Phần bên phải - Thông tin người dùng và thời gian
        JPanel rightPanel = new JPanel();
        rightPanel.setOpaque(false);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS));
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));

// Icon mặt trời
        ImageIcon originalIcon = new ImageIcon(getClass().getResource("/image/sun.png"));
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(24, 24, Image.SCALE_SMOOTH); // 24x24 pixels
        JLabel sunIcon = new JLabel(new ImageIcon(scaledImage));
        sunIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));


// Label chào mừng
       JLabel welcomeLabel = new JLabel(
                nhanVien.getChucVuNhanVien().toString()
                        + ": Xin chào, " + nhanVien.getTenNhanVien());
//        JLabel welcomeLabel = new JLabel(": Xin chào, " );
        welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));

// Label hiển thị thời gian thực
        JLabel timeLabel = new JLabel();
        timeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        timeLabel.setForeground(Color.WHITE);

// Thêm các component vào rightPanel
        rightPanel.add(sunIcon);
        rightPanel.add(welcomeLabel);
        rightPanel.add(timeLabel);

// Thêm rightPanel vào header
        headerPanel.add(rightPanel, BorderLayout.EAST);

// Thêm headerPanel vào frame
        add(headerPanel, BorderLayout.NORTH);

// Tạo timer để cập nhật thời gian thực
        Timer timer = new Timer(1000, e -> {
            String time = new java.text.SimpleDateFormat("HH:mm:ss  dd/MM/yyyy").format(new java.util.Date());
            timeLabel.setText(time);
        });
        timer.start();

        JPanel menuPanel = new JPanel();
        menuPanel.setPreferredSize(new Dimension(260, getHeight()));
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(135, 196, 243));

        JLabel appTitle = new JLabel("MENU", SwingConstants.CENTER);
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        appTitle.setForeground(Color.WHITE);
        appTitle.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        appTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(appTitle);


        JButton btnDashboard = createMenuButton("Trang chủ", "/image/home.png");

        JButton btnSanPham = createMenuButton("Sản phẩm", "/image/product.png");
        JButton btnKhachHang = createMenuButton("Khách hàng", "/image/customer.png");
        JButton btnHoaDon = createMenuButton("Hóa đơn", "/image/invoice.png");
        JButton btnDanhMucSanPham = createMenuButton("Danh mục sản phẩm", "/image/category.png");
        JButton btnNhanVien = createMenuButton("Nhân viên", "/image/staff.png");
        JButton btnTaiKhoan = createMenuButton("Tài khoản", "/image/account.png");
        JButton btnThongKe = createMenuButton("Thống kê", "/image/statistics.png");
        JButton btnTimHoaDon = createMenuButton("Tìm hóa đơn", "/image/searchMenu.png");
        JButton btnKhuyenMai = createMenuButton("Khuyến mãi", "/image/discount.png");




        menuPanel.add(btnDashboard);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(btnTimHoaDon);
        menuPanel.add(Box.createVerticalStrut(10));



        if(nhanVien.getChucVuNhanVien().toString().equalsIgnoreCase("Nhân viên")){
            menuPanel.add(btnSanPham);
            menuPanel.add(Box.createVerticalStrut(10));
            menuPanel.add(btnDanhMucSanPham);
            menuPanel.add(Box.createVerticalStrut(10));
            menuPanel.add(btnKhachHang);
            menuPanel.add(Box.createVerticalStrut(10));
            menuPanel.add(btnHoaDon);
            menuPanel.add(Box.createVerticalStrut(10));
        }

        if(nhanVien.getChucVuNhanVien().toString().equalsIgnoreCase("Người quản lý")){
            menuPanel.add(btnThongKe);
            menuPanel.add(Box.createVerticalStrut(10));
            menuPanel.add(btnNhanVien);
            menuPanel.add(Box.createVerticalStrut(10));
            menuPanel.add(btnTaiKhoan);
            menuPanel.add(Box.createVerticalStrut(10));
            menuPanel.add(btnKhuyenMai);
        }


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
        JLabel name = new JLabel("Đăng xuất tài khoản");

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

        panelTaiKhoan = new PanelTaiKhoan();
        panelDashboard = new PanelDashboard();
        panelNhanVien = new PanelNhanVien();
        panelThongKe = new PanelThongKe();

        contentPanel.add(panelDashboard, "dashboard");
        contentPanel.add(new PanelSanPham(), "sanpham");
        contentPanel.add(new PanelKhachHang(), "khachhang");
        contentPanel.add(new PanelHoaDon(), "hoadon");
        contentPanel.add(panelThongKe, "thongke");
        contentPanel.add(new PanelDanhMucSanPham(), "danhmucsanpham");
        contentPanel.add(panelNhanVien, "nhanvien");
        contentPanel.add(panelTaiKhoan, "taikhoan");
        contentPanel.add(new PanelTimHoaDon(), "timhoadon");
        contentPanel.add(new PanelKhuyenMai(), "khuyenmai");

        add(contentPanel, BorderLayout.CENTER);

        setActiveButton(btnDashboard);
        btnDashboard.addActionListener((ActionEvent e) -> {
            try {
                contentPanel.remove(panelDashboard); // XÓA panel cũ
                panelDashboard = new PanelDashboard(); // TẠO panel mới
                contentPanel.add(panelDashboard, "dashboard"); // ADD panel mới
                cardLayout.show(contentPanel, "dashboard"); // SHOW ra
                setActiveButton(btnDashboard);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        btnSanPham.addActionListener((ActionEvent e) -> {
            cardLayout.show(contentPanel, "sanpham");
            setActiveButton(btnSanPham);
        });
        btnKhachHang.addActionListener((ActionEvent e) -> {
            cardLayout.show(contentPanel, "khachhang");
            setActiveButton(btnKhachHang);
        });
        btnHoaDon.addActionListener((ActionEvent e) -> {
            cardLayout.show(contentPanel, "hoadon");
            setActiveButton(btnHoaDon);
        });
        btnThongKe.addActionListener((ActionEvent e) -> {
            try {
                contentPanel.remove(panelThongKe); // XÓA panel cũ
                panelThongKe = new PanelThongKe(); // TẠO panel mới
                contentPanel.add(panelThongKe, "thongke"); // ADD panel mới
                cardLayout.show(contentPanel, "thongke"); // SHOW ra
                setActiveButton(btnThongKe);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        btnDanhMucSanPham.addActionListener((ActionEvent e) -> {
            cardLayout.show(contentPanel, "danhmucsanpham");
            setActiveButton(btnDanhMucSanPham);
        });
        btnNhanVien.addActionListener((ActionEvent e) -> {
            try {
                contentPanel.remove(panelNhanVien); // XÓA panel cũ
                panelNhanVien = new PanelNhanVien(); // TẠO panel mới
                contentPanel.add(panelNhanVien, "nhanvien"); // ADD panel mới
                cardLayout.show(contentPanel, "nhanvien"); // SHOW ra
                setActiveButton(btnNhanVien);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        btnTaiKhoan.addActionListener((ActionEvent e) -> {
            try {
                contentPanel.remove(panelTaiKhoan); // XÓA panel cũ
                panelTaiKhoan = new PanelTaiKhoan(); // TẠO panel mới
                contentPanel.add(panelTaiKhoan, "taikhoan"); // ADD panel mới
                cardLayout.show(contentPanel, "taikhoan"); // SHOW ra
                setActiveButton(btnTaiKhoan);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        btnTimHoaDon.addActionListener((ActionEvent e) -> {
            cardLayout.show(contentPanel, "timhoadon");
            setActiveButton(btnTimHoaDon);
        });
        btnKhuyenMai.addActionListener((ActionEvent e) -> {
            cardLayout.show(contentPanel, "khuyenmai");
            setActiveButton(btnKhuyenMai);
        });
    }

    private JButton createMenuButton(String text, String iconPath) {
        JButton button = new JButton(text);

        if (iconPath != null && !iconPath.isEmpty()) {
            ImageIcon originalIcon = new ImageIcon(getClass().getResource(iconPath));
            Image scaledImage = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(scaledImage));
            button.setIconTextGap(15);
        }

        button.setMaximumSize(new Dimension(230, 40));
        button.setPreferredSize(new Dimension(getWidth(), 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        button.setFocusPainted(false);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
//        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 15));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(false);

        button.putClientProperty("JButton.buttonType", "roundRect"); // Optional
        button.putClientProperty("JComponent.roundRect", true); // Optional
        button.putClientProperty("Button.arc", 20);

        // Hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(new Color(210, 227, 252))) {
                    button.setBackground(new Color(230, 240, 250)); // Hover nhẹ
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (!button.getBackground().equals(new Color(210, 227, 252))) {
                    button.setBackground(Color.WHITE); // Trở lại trắng
                }
            }
        });

        return button;
    }

    private void refreshPanel(JPanel oldPanel, JPanel newPanel, String name) {
        contentPanel.remove(oldPanel);
        contentPanel.add(newPanel, name);
        cardLayout.show(contentPanel, name);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                FlatLightLaf.setup();
                UIManager.put("Button.arc", 20); // bo góc cho tất cả nút
                new LoginForm().setVisible(true);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}


