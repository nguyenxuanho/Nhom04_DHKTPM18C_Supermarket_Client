package gui.panel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatIntelliJLaf;
import gui.LoginForm;
import gui.TrangChu;
import service.*;


import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JProgressBar;

public class Loading extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JProgressBar progress;
    private JLabel txtLoad;
    private JLabel value;
    private JLabel label;

    public Loading() {
        FlatIntelliJLaf.setup();
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 900, 550);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(102, 149, 151));
        panel.setBounds(0, 0, 900, 550);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel = new JLabel("");
        lblNewLabel.setHorizontalAlignment(SwingConstants.TRAILING);
        lblNewLabel.setIcon(new ImageIcon(Loading.class.getResource("/image/sieuthi.jpg")));
        lblNewLabel.setBounds(85, 104, 713, 300);
        panel.add(lblNewLabel);

        txtLoad = new JLabel("Loading...");
        txtLoad.setFont(new Font("Tahoma", Font.PLAIN, 15));
        txtLoad.setForeground(new Color(64, 0, 128));
        txtLoad.setBounds(96, 442, 240, 30);
        panel.add(txtLoad);

        progress = new JProgressBar();
        progress.setBounds(96, 482, 706, 14);
        panel.add(progress);

        label = new JLabel("Hệ thống quản lý siêu thị co.op mart Nguyễn Kiệm");
        label.setFont(new Font("JetBrains Mono Light", Font.PLAIN, 25));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 41, 900, 53);
        panel.add(label);

        value = new JLabel("0 %");
        value.setForeground(new Color(64, 0, 128));
        value.setHorizontalAlignment(SwingConstants.CENTER);
        value.setFont(new Font("Arial", Font.PLAIN, 15));
        value.setBounds(712, 442, 86, 30);
        panel.add(value);


        System.out.println("Đang kết nối đến RMI Server...");



        ChiTietHoaDonService chiTietHoaDonService = RmiServiceLocator.getChiTietHoaDonService();
        DanhMucSanPhamService danhMucSanPhamService = RmiServiceLocator.getDanhMucSanPhamService();
        HoaDonService hoaDonService = RmiServiceLocator.getHoaDonService();
        KhachHangService khachHangService = RmiServiceLocator.getKhachHangService();
        NhanVienService nhanVienService = RmiServiceLocator.getNhanVienService();
        SanPhamService sanPhamService = RmiServiceLocator.getSanPhamService();
        TaiKhoanService taiKhoanService = RmiServiceLocator.getTaiKhoanService();
        KhuyenMaiService khuyenMaiService = RmiServiceLocator.getKhuyenMaiService();
        ThuocTinhSanPhamService thuocTinhSanPhamService = RmiServiceLocator.getThuocTinhSanPhamService();

        if (chiTietHoaDonService != null && khachHangService != null) {
            System.out.println("Client đã kết nối thành công đến RMI Server!");

        } else {
            System.err.println("Không thể kết nối đến một số service!");
        }



    }

    public void updateProgress(int value) throws Exception {
        this.value.setText(value + " %");
        this.progress.setValue(value);

        // Cập nhật trạng thái dựa trên giá trị của value
        switch (value) {
            case 10:
                txtLoad.setText("Turning On Modules ...");
                break;
            case 20:
                txtLoad.setText("Loading Modules ...");
                break;
            case 50:
                txtLoad.setText("Connecting to database ...");
                break;
            case 70:
                txtLoad.setText("Connection Successful!");
                break;
            case 90:
                txtLoad.setText("Launching Application ...");
                break;
            case 100:
                this.dispose(); // Đóng cửa sổ tải

                new LoginForm().setVisible(true);
                break;
        }
    }
}
