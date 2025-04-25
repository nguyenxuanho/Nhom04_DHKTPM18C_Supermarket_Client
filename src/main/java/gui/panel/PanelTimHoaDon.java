package gui.panel;

import gui.components.ComponentUtils;
import model.ChiTietHoaDon;
import model.HoaDon;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.List;

import static gui.panel.RmiServiceLocator.chiTietHoaDonService;
import static gui.panel.RmiServiceLocator.hoaDonService;

public class PanelTimHoaDon extends JPanel {
    // Các trường cho phần chi tiết sản phẩm
    private JTextField txtTimMaHoaDon, txtTimTenKH;
    private JButton btnTimMaHD ;
    private DecimalFormat df = new DecimalFormat("#,##0.00");


    // Các thành phần bên phải
    private JTextField txtMaNV;
    private JTextField txtDiemTichLuy;
    private JTextArea txtGhiChu;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel lblMaHoaDon, lbltongTien, lblDiem;
    private JTextField txtMaKH,txtMaSP, txtDonGia, txtSoLuong, txtThanhTien;
    private JTextField txtTimSDT;
    private JTextField txtTongTien, txtTim;
    private JTextField txtDiemTichLuyDung;
    public PanelTimHoaDon() throws NamingException, RemoteException {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        // Container chính chứa bảng và panel phải
        JPanel mainContainer = new JPanel(new BorderLayout(10, 10));

        // Bảng chi tiết
        JPanel tablePanel = createTablePanel();

        mainContainer.add(tablePanel, BorderLayout.CENTER);
        JPanel searchPanel = createSearchPanel();
        mainContainer.add(searchPanel, BorderLayout.NORTH);

        // Panel bên phải
        JPanel rightPanel = createRightPanel();
        mainContainer.add(rightPanel, BorderLayout.EAST);

        add(mainContainer, BorderLayout.CENTER);

        addEvent();
    }
    private JPanel createRightPanel() throws RemoteException {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Thông tin thanh toán"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nhân viên
        JLabel lblMaNV = new JLabel("Mã NV:");
        txtMaNV = new JTextField(20);
        txtMaNV.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 0;

        panel.add(lblMaNV, gbc);
        gbc.gridx = 1;
        panel.add(txtMaNV, gbc);
        JLabel lblSDT = new JLabel("Số điện thoại: ");
        txtTimSDT = new JTextField(20);
        txtTimSDT.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblSDT, gbc);
        gbc.gridx = 1;
        panel.add(txtTimSDT, gbc);
//        btnTimKHTheoSDT = new JButton("Tìm");
//        ComponentUtils.setButton(btnTimKHTheoSDT, new Color(33, 150, 243));
//        gbc.gridx = 2;
//        panel.add(btnTimKHTheoSDT, gbc);
//        JLabel lblDiemTichLuy = new JLabel("Điểm tích lũy:");
        txtDiemTichLuy = new JTextField(20);
        txtDiemTichLuy.setEditable(false);
//        txtDiemTichLuy.setText(khachHangService.findById(txtMaKH.getText()).getDiemTichLuy()+"");
        gbc.gridx = 0; gbc.gridy = 2;
//        panel.add(lblDiemTichLuy, gbc);
        gbc.gridx = 1;
//        panel.add(txtDiemTichLuy, gbc);
        JLabel lblTenKH = new JLabel("Tên KH: ");
        txtTimTenKH = new JTextField(20);
        txtTimTenKH.setEditable(false);
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(lblTenKH, gbc);
        gbc.gridx = 1;
        panel.add(txtTimTenKH, gbc);
        JLabel lblMaKH = new JLabel("Mã KH:");
        txtMaKH = new JTextField(20);
        txtMaKH.setEditable(false);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(lblMaKH, gbc);
        gbc.gridx = 1;
        panel.add(txtMaKH, gbc);

        lbltongTien = new JLabel("Tổng tiền:");
        txtTongTien = new JTextField(20);
        txtTongTien.setEditable(false);
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(lbltongTien, gbc);
        gbc.gridx = 1;
        panel.add(txtTongTien, gbc);


        // Điểm tích lũy
        lblDiem = new JLabel("Điểm dùng:");
        txtDiemTichLuyDung = new JTextField(20);
        gbc.gridx = 0; gbc.gridy = 6;
//        panel.add(lblDiem, gbc);
        gbc.gridx = 1;
//        panel.add(txtDiemTichLuyDung, gbc);
        txtDiemTichLuyDung.setEditable(false);

        // Ghi chú
        JLabel lblGhiChu = new JLabel("Ghi chú:");
        txtGhiChu = new JTextArea(5,30);
        gbc.gridx = 0; gbc.gridy = 7;

        panel.add(lblGhiChu, gbc);
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        panel.add(txtGhiChu, gbc);

        // Nút Thêm hóa đơn
//        btnThemHD = new JButton("Thêm hóa đơn");
//        ComponentUtils.setButton(btnThemHD, new Color(33, 150, 243));
//        gbc.gridx = 0; gbc.gridy = 9;
//        gbc.gridwidth = 3;
//        gbc.anchor = GridBagConstraints.CENTER;
//        panel.add(btnThemHD, gbc);



        return panel;
    }
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 240, 240));



        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                ComponentUtils.getTitleBorder("Tra cứu thông tin hóa đơn")
        ));

        JPanel searchByIDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchByIDPanel.setBackground(new Color(240, 240, 240));

        lblMaHoaDon = new JLabel("Mã HD:");
        lblMaHoaDon.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTimMaHoaDon = new JTextField(10);
        txtTimMaHoaDon.setFont(new Font("Arial", Font.PLAIN, 14));

        btnTimMaHD = new JButton("Tìm kiếm");
        btnTimMaHD.setIcon(new ImageIcon(getClass().getResource("/image/search.png")));
        btnTimMaHD.setFont(new Font("Arial", Font.BOLD, 14));
        btnTimMaHD.setBackground(new Color(33, 150, 243));
        btnTimMaHD.setForeground(Color.WHITE);
        btnTimMaHD.setFocusPainted(false);
        btnTimMaHD.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        searchByIDPanel.add(lblMaHoaDon);
        searchByIDPanel.add(txtTimMaHoaDon);
        searchByIDPanel.add(btnTimMaHD);


        // Panel lọc theo tên
        JPanel filterByNamePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        filterByNamePanel.setBackground(new Color(240, 240, 240));



//        filterByNamePanel.add(lblTenKH);
//        filterByNamePanel.add(txtTimTenKH);
//        filterByNamePanel.add(lblSDT);
//        filterByNamePanel.add(txtTimSDT);
//
        panel.add(searchByIDPanel, BorderLayout.WEST);
//        panel.add(filterByNamePanel, BorderLayout.EAST);
//
//        searchByIDPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//        filterByNamePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));




//        txtTimTenKH.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                timKiemTheoTen();
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                timKiemTheoTen();
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//            }
//
//            private void timKiemTheoTen() {
//                String tenKH = txtTimTenKH.getText().trim();
//                if (!tenKH.isEmpty()) {
//                    try {
//                        List<ChiTietHoaDon> dscthd = chiTietHoaDonService.getListByHDID(t);
////                        List<KhachHang> danhSachHoaDonTheoTenKhachHang = dsKH.stream()
////                                .filter(kh -> kh.getKhachHang().getTenKhachHang().contains(tenKH))
////                                .collect(Collectors.toList());
////                        hienThiKetQua(danhSachHoaDonTheoTenKhachHang);
//                    } catch (RemoteException ex) {
//                        ex.printStackTrace();
//                        JOptionPane.showMessageDialog(null,
//                                "Lỗi kết nối máy chủ: " + ex.getMessage(),
//                                "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    }
//                } else {
//                    resetTable();
//                }
//            }
//        });
//
//        txtTimSDT.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                timKiemTheoSDT();
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                timKiemTheoSDT();
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//            }
//
//            private void timKiemTheoSDT() {
//                String sdt = txtTimSDT.getText().trim();
//                if (!sdt.isEmpty()) {
//                    try {
//                        List<KhachHang> allKhachHang = khachHangService.findAll();
//
//                        List<KhachHang> khachHangTheoSoDienThoai = allKhachHang.stream()
//                                .filter(kh -> kh.getSoDienThoai().contains(sdt))
//                                .collect(Collectors.toList());
//
//                        hienThiKetQuaTimKiem(khachHangTheoSoDienThoai);
//
//                    } catch (RemoteException ex) {
//                        ex.printStackTrace();
//                        JOptionPane.showMessageDialog(PanelKhachHang.this,
//                                "Lỗi kết nối máy chủ: " + ex.getMessage(),
//                                "Lỗi", JOptionPane.ERROR_MESSAGE);
//                    }
//                } else {
//                    resetTable();
//                }
//            }
//        });

        btnTimMaHD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String maHD = txtTimMaHoaDon.getText().trim();
                    if (maHD.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "Vui lòng nhập mã hóa đơn", "Thông báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    HoaDon hoaDon = hoaDonService.findById(maHD);
                    if (hoaDon == null) {
                        JOptionPane.showMessageDialog(null, "Không tìm thấy hóa đơn", "Thông báo", JOptionPane.WARNING_MESSAGE);
                        return;
                    }
                    java.util.List<ChiTietHoaDon> dscthd = handleTimChiTietTheoHoaDon(maHD);
                    HoaDon hd = handleTimHoaDonTheoMa(maHD);
                    hienThiKetQua(hd,dscthd);
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });




        return panel;
    }
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Sản phẩm", "Đơn giá", "Số lượng", "Thành tiền"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        ComponentUtils.setTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }





    private void resetForm(){
        txtTimMaHoaDon.setText("");
    }
    private java.util.List<ChiTietHoaDon> handleTimChiTietTheoHoaDon(String HDID) throws RemoteException {
        return chiTietHoaDonService.getListByHDID(HDID);
    }
    private HoaDon handleTimHoaDonTheoMa(String maHD) throws RemoteException {
        return hoaDonService.findById(maHD);
    }
    private void hienThiKetQua(HoaDon hoaDon,List<ChiTietHoaDon> dscthd){
        tableModel.setRowCount(0);
        for (ChiTietHoaDon cthd : dscthd) {
            Object[] rowData = {cthd.getSanPham().getMaSanPham(), df.format(cthd.getDonGia()) + "VND", cthd.getSoLuong(), df.format(cthd.getThanhTien()) + "VND"};
            tableModel.addRow(rowData);
        }
        txtMaKH.setText(hoaDon.getKhachHang().getMaKhachHang());
        txtTimTenKH.setText(hoaDon.getKhachHang().getTenKhachHang());
        txtTimSDT.setText(hoaDon.getKhachHang().getSoDienThoai());
        txtDiemTichLuy.setText(hoaDon.getKhachHang().getDiemTichLuy()+"");
        txtMaNV.setText(hoaDon.getNhanVien().getMaNhanVien());
        txtGhiChu.setText(hoaDon.getGhiChu());
        txtTongTien.setText(df.format(hoaDon.getTongTien())+"VND");
    }






    private void resetTable(){
        tableModel.setRowCount(0);
        txtMaSP.setText("");
        txtDonGia.setText("");
        txtSoLuong.setText("");
        txtThanhTien.setText("");
    }
    private void addEvent(){







    }

}
