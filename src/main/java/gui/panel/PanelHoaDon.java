package gui.panel;

import dto.TaiKhoanDTO;
import gui.components.ComponentUtils;
import io.github.cdimascio.dotenv.Dotenv;
import model.ChiTietHoaDon;
import model.HoaDon;
import model.KhachHang;
import model.SanPham;
import service.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class PanelHoaDon extends JPanel {
    // Các trường cho phần chi tiết sản phẩm
    private JTextField txtMaSP, txtDonGia, txtSoLuong, txtThanhTien, txtTimMaHoaDon, txtTimTenKH;
    private JButton btnTimSP, btnThem , btnXoa, btnSua, btnReset, btnTimMaHD, btnTimKHTheoSDT ;

    // Các thành phần bên phải
    private JTextField txtMaNV;
    private JTextField txtDiemTichLuy;
    private JTextArea txtGhiChu;
    private JButton btnThemHD;

    private JTable table;
    private DefaultTableModel tableModel;
    private final HoaDonService hoaDonService;
    private  final ChiTietHoaDonService chiTietHoaDonService;
    private  final KhachHangService khachHangService;
    private  final TaiKhoanService taiKhoanService;
    private  final NhanVienService nhanVienService;
    private  final SanPhamService sanPhamService;
    private  final KhuyenMaiService khuyenMaiService;

    private final Dotenv dotenv = Dotenv.load();
    private final String drivername = dotenv.get("DRIVER_NAME");
    private JLabel jLableMaSP;
    private JLabel jLableDonGia;
    private JLabel jLabelSoLuong , jLabelThanhTien, lblMaHoaDon, lblDiem, lbltongTien;
    private JTextField txtMaKH;
    private JTextField txtTimSDT;
    private JLabel buttonEmpty;
    private JTextField txtTongTien, txtTim;
    private JTextField txtDiemTichLuyDung;

    public PanelHoaDon() throws NamingException, RemoteException {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel infoPanel = createInfoPanel();
        Context context = new InitialContext();
        this.hoaDonService = (HoaDonService) context.lookup("rmi://" + drivername + ":9090/hoaDonService");
        this.khachHangService = (KhachHangService) context.lookup("rmi://" + drivername + ":9090/khachHangService");
        this.taiKhoanService = (TaiKhoanService) context.lookup("rmi://" + drivername + ":9090/taiKhoanService");
        this.nhanVienService = (NhanVienService) context.lookup("rmi://" + drivername + ":9090/nhanVienService");
        this.sanPhamService = (SanPhamService) context.lookup("rmi://" + drivername + ":9090/sanPhamService");
        this.khuyenMaiService = (KhuyenMaiService) context.lookup("rmi://" + drivername + ":9090/khuyenMaiService");
        this.chiTietHoaDonService = (ChiTietHoaDonService) context.lookup("rmi://" + drivername + ":9090/chiTietHoaDonService");


        // Phần nhập thông tin sản phẩm


        add(infoPanel, BorderLayout.NORTH);


        // Container chính chứa bảng và panel phải
        JPanel mainContainer = new JPanel(new BorderLayout(10, 10));

        // Bảng chi tiết
        JPanel tablePanel = createTablePanel();

        mainContainer.add(tablePanel, BorderLayout.CENTER);
//        JPanel searchPanel = createSearchPanel();
//        mainContainer.add(searchPanel, BorderLayout.NORTH);

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
        if(TaiKhoanDTO.getTaiKhoan()!=null){
            txtMaNV.setText(taiKhoanService.getNhanVienByTaiKhoan(TaiKhoanDTO.getTaiKhoan().getMaTaiKhoan()).getMaNhanVien());
        }
        gbc.gridx = 0; gbc.gridy = 0;

        panel.add(lblMaNV, gbc);
        gbc.gridx = 1;
        panel.add(txtMaNV, gbc);
        JLabel lblSDT = new JLabel("Số điện thoại: ");
        txtTimSDT = new JTextField(20);
        
       
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(lblSDT, gbc);
        gbc.gridx = 1;
        panel.add(txtTimSDT, gbc);
         btnTimKHTheoSDT = new JButton("Tìm");
        ComponentUtils.setButton(btnTimKHTheoSDT, new Color(33, 150, 243));
        gbc.gridx = 2;
        panel.add(btnTimKHTheoSDT, gbc);
        JLabel lblDiemTichLuy = new JLabel("Điểm tích lũy:");
         txtDiemTichLuy = new JTextField(20);
        txtDiemTichLuy.setEditable(false);
//        txtDiemTichLuy.setText(khachHangService.findById(txtMaKH.getText()).getDiemTichLuy()+"");
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(lblDiemTichLuy, gbc);
        gbc.gridx = 1;
        panel.add(txtDiemTichLuy, gbc);
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
        panel.add(lblDiem, gbc);
        gbc.gridx = 1;
        panel.add(txtDiemTichLuyDung, gbc);

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
        btnThemHD = new JButton("Thêm hóa đơn");
        ComponentUtils.setButton(btnThemHD, new Color(33, 150, 243));
        gbc.gridx = 0; gbc.gridy = 9;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(btnThemHD, gbc);



        return panel;
    }
//    private JPanel createSearchPanel() {
//        JPanel panel = new JPanel();
//        panel.setLayout(new BorderLayout(10, 10));
//        panel.setBackground(new Color(240, 240, 240));
//
//
//
//        panel.setBorder(BorderFactory.createCompoundBorder(
//                BorderFactory.createEmptyBorder(15, 15, 15, 15),
//                ComponentUtils.getTitleBorder("Tra cứu thông tin hóa đơn")
//        ));
//
//        JPanel searchByIDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
//        searchByIDPanel.setBackground(new Color(240, 240, 240));
//
//        lblMaHoaDon = new JLabel("Mã HD:");
//        lblMaHoaDon.setFont(new Font("Arial", Font.PLAIN, 14));
//        txtTimMaHoaDon = new JTextField(10);
//        txtTimMaHoaDon.setFont(new Font("Arial", Font.PLAIN, 14));
//
//        btnTimMaHD = new JButton("Tìm kiếm");
//        btnTimMaHD.setFont(new Font("Arial", Font.BOLD, 14));
//        btnTimMaHD.setBackground(new Color(33, 150, 243));
//        btnTimMaHD.setForeground(Color.WHITE);
//        btnTimMaHD.setFocusPainted(false);
//        btnTimMaHD.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
//
//        searchByIDPanel.add(lblMaHoaDon);
//        searchByIDPanel.add(txtTimMaHoaDon);
//        searchByIDPanel.add(btnTimMaHD);
//
//
//        // Panel lọc theo tên
//        JPanel filterByNamePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
//        filterByNamePanel.setBackground(new Color(240, 240, 240));
//
//
//
////        filterByNamePanel.add(lblTenKH);
////        filterByNamePanel.add(txtTimTenKH);
////        filterByNamePanel.add(lblSDT);
////        filterByNamePanel.add(txtTimSDT);
////
//        panel.add(searchByIDPanel, BorderLayout.WEST);
////        panel.add(filterByNamePanel, BorderLayout.EAST);
////
////        searchByIDPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
////        filterByNamePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
//
//
//
//
////        txtTimTenKH.getDocument().addDocumentListener(new DocumentListener() {
////            @Override
////            public void insertUpdate(DocumentEvent e) {
////                timKiemTheoTen();
////            }
////
////            @Override
////            public void removeUpdate(DocumentEvent e) {
////                timKiemTheoTen();
////            }
////
////            @Override
////            public void changedUpdate(DocumentEvent e) {
////            }
////
////            private void timKiemTheoTen() {
////                String tenKH = txtTimTenKH.getText().trim();
////                if (!tenKH.isEmpty()) {
////                    try {
////                        List<ChiTietHoaDon> dscthd = chiTietHoaDonService.getListByHDID(t);
//////                        List<KhachHang> danhSachHoaDonTheoTenKhachHang = dsKH.stream()
//////                                .filter(kh -> kh.getKhachHang().getTenKhachHang().contains(tenKH))
//////                                .collect(Collectors.toList());
//////                        hienThiKetQua(danhSachHoaDonTheoTenKhachHang);
////                    } catch (RemoteException ex) {
////                        ex.printStackTrace();
////                        JOptionPane.showMessageDialog(null,
////                                "Lỗi kết nối máy chủ: " + ex.getMessage(),
////                                "Lỗi", JOptionPane.ERROR_MESSAGE);
////                    }
////                } else {
////                    resetTable();
////                }
////            }
////        });
////
////        txtTimSDT.getDocument().addDocumentListener(new DocumentListener() {
////            @Override
////            public void insertUpdate(DocumentEvent e) {
////                timKiemTheoSDT();
////            }
////
////            @Override
////            public void removeUpdate(DocumentEvent e) {
////                timKiemTheoSDT();
////            }
////
////            @Override
////            public void changedUpdate(DocumentEvent e) {
////            }
////
////            private void timKiemTheoSDT() {
////                String sdt = txtTimSDT.getText().trim();
////                if (!sdt.isEmpty()) {
////                    try {
////                        List<KhachHang> allKhachHang = khachHangService.findAll();
////
////                        List<KhachHang> khachHangTheoSoDienThoai = allKhachHang.stream()
////                                .filter(kh -> kh.getSoDienThoai().contains(sdt))
////                                .collect(Collectors.toList());
////
////                        hienThiKetQuaTimKiem(khachHangTheoSoDienThoai);
////
////                    } catch (RemoteException ex) {
////                        ex.printStackTrace();
////                        JOptionPane.showMessageDialog(PanelKhachHang.this,
////                                "Lỗi kết nối máy chủ: " + ex.getMessage(),
////                                "Lỗi", JOptionPane.ERROR_MESSAGE);
////                    }
////                } else {
////                    resetTable();
////                }
////            }
////        });
//
//        btnTimMaHD.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                try {
//                    String maHD = txtTimMaHoaDon.getText().trim();
//                    if (maHD.isEmpty()) {
//                        JOptionPane.showMessageDialog(null, "Vui lòng nhập mã hóa đơn", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                        return;
//                    }
//                    HoaDon hoaDon = hoaDonService.findById(maHD);
//                    if (hoaDon == null) {
//                        JOptionPane.showMessageDialog(null, "Không tìm thấy hóa đơn", "Thông báo", JOptionPane.WARNING_MESSAGE);
//                        return;
//                    }
//                    List<ChiTietHoaDon> dscthd = handleTimChiTietTheoHoaDon(maHD);
//                    hienThiKetQua(dscthd);
//                } catch (RemoteException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        });
//
//
//
//
//        return panel;
//    }
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

    private JPanel createInfoPanel() throws RemoteException{
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());


        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 10, 20),
                ComponentUtils.getTitleBorder("Thông tin chi tiết hóa đơn")
        ));

        Box boxFormInput = Box.createVerticalBox();

        Box box1 = Box.createHorizontalBox();
        jLableMaSP = new JLabel("Mã SP:") ;
        jLableMaSP.setPreferredSize(new Dimension(90, 25));
        txtMaSP = new JTextField(20);
        btnTimSP = new JButton("Tìm");
        btnTimSP.setPreferredSize(new Dimension(60, 25));
        ComponentUtils.setButton(btnTimSP, new Color(33, 150, 243));
        jLableDonGia = new JLabel("Đơn giá:") ;
        jLableDonGia.setPreferredSize(new Dimension(90, 25));
        txtDonGia = new JTextField(20);
        txtDonGia.setEditable(false);
        jLabelSoLuong = new JLabel("Số lượng");
        jLabelSoLuong.setPreferredSize(new Dimension(90, 25));
        txtSoLuong = new JTextField(20);

        buttonEmpty = new JLabel("");
        buttonEmpty.setPreferredSize(new Dimension(95, 0));
        box1.add(Box.createHorizontalStrut(50));
        box1.add(jLableMaSP);
        box1.add(txtMaSP);
        box1.add(btnTimSP);
        box1.add(Box.createHorizontalStrut(50));
        box1.add(jLableDonGia);
        box1.add(txtDonGia);
        box1.add(Box.createHorizontalStrut(50));
       

        Box box2 = Box.createHorizontalBox();

        box2.add(Box.createHorizontalStrut(50));
        box2.add(jLabelSoLuong);
        box2.add(txtSoLuong);
        box2.add(buttonEmpty);
        box2.add(Box.createHorizontalStrut(50));
        jLabelThanhTien = new JLabel("Thành tiền:");
        jLabelThanhTien.setPreferredSize(new Dimension(90, 25));
        txtThanhTien = new JTextField(20);
        txtThanhTien.setEditable(false);
        box2.add(jLabelThanhTien);
        box2.add(txtThanhTien);
        box2.add(Box.createHorizontalStrut(50));
        

        boxFormInput.add(Box.createVerticalStrut(10));
        boxFormInput.add(box1);
        boxFormInput.add(Box.createVerticalStrut(10));
        boxFormInput.add(box2);
        boxFormInput.add(Box.createVerticalStrut(10));

        panel.add(boxFormInput, BorderLayout.CENTER);


        // Panel nút chức năng
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnReset = new JButton("Reset");

        ComponentUtils.setButton(btnThem, new Color(33, 150, 243));
        ComponentUtils.setButton(btnXoa, new Color(244, 67, 54));
        ComponentUtils.setButton(btnSua, new Color(255, 193, 7));
        ComponentUtils.setButton(btnReset, new Color(76, 175, 80));

        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnReset);

        btnThem.setIcon(new ImageIcon(getClass().getResource("/image/add.png")));
        btnXoa.setIcon(new ImageIcon(getClass().getResource("/image/delete.png")));
        btnSua.setIcon(new ImageIcon(getClass().getResource("/image/edit.png")));
        btnReset.setIcon(new ImageIcon(getClass().getResource("/image/clean.png")));
//        btnResetTable.setIcon(new ImageIcon(getClass().getResource("/image/refresh.png")));
//        btnFind.setIcon(new ImageIcon(getClass().getResource("/image/search.png")));

        panel.add(buttonPanel, BorderLayout.SOUTH);

//        addButtonListeners();

        return panel;
    }
    private void handleTimKHTheoSDT(){
        KhachHang kh = findBySDT();
        System.out.println("Khach hang: " + kh);
        if(kh!=null){
            txtMaKH.setText(kh.getMaKhachHang());
            txtTimTenKH.setText(kh.getTenKhachHang());
            txtDiemTichLuy.setText(kh.getDiemTichLuy()+"");
        }else{
            JOptionPane.showMessageDialog(null, "Không tìm thấy khách hàng", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    private KhachHang findBySDT(){
        try {
            System.out.println("SDT: " + txtTimSDT.getText());
            return khachHangService.findBySoDienThoai(txtTimSDT.getText());
        } catch (Exception e) {
            return null;


        }
    }

    private void resetForm(){
        txtMaSP.setText("");
        txtDonGia.setText("");
        txtSoLuong.setText("");
        txtThanhTien.setText("");
    }
    private List<ChiTietHoaDon>  handleTimChiTietTheoHoaDon(String HDID) throws RemoteException {
        return chiTietHoaDonService.getListByHDID(HDID);
    }

    private void hienThiKetQua(List<ChiTietHoaDon> dscthd){
        tableModel.setRowCount(0);
        for (ChiTietHoaDon cthd : dscthd) {
            Object[] rowData = {cthd.getSanPham().getMaSanPham(), cthd.getDonGia(), cthd.getSoLuong(), cthd.getThanhTien()};
            tableModel.addRow(rowData);
        }
    }
    private  boolean validateMaSP(){
        if(txtMaSP.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Vui lòng nhập mã sản phẩm", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    private  boolean validateCTHD(){
        if(txtSoLuong.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số lượng", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if (!txtSoLuong.getText().trim().matches(
                "^[0-9][0-9]*$")) {
            JOptionPane.showMessageDialog(null, "Số lượng phải là số nguyên dương", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(txtMaSP.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Vui lòng nhập mã sản phẩm", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    private boolean validateAddHD(){
        if(txtMaKH.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Vui lòng nhập mã khách hàng", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }else if(txtMaNV.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Vui lòng nhập mã nhân viên", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if(txtDiemTichLuyDung.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Vui lòng nhập điểm tích lũy", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        } else if (!txtDiemTichLuyDung.getText().trim().matches(
                "^[0-9][0-9]*$")) {
            JOptionPane.showMessageDialog(null, "Điểm tích lũy phải là số nguyên dương", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(Integer.parseInt(txtDiemTichLuyDung.getText()) > Double.parseDouble(txtDiemTichLuy.getText())){
            JOptionPane.showMessageDialog(null, "Điểm tích lũy không được lớn hơn điểm tích lũy hiện tại", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(txtTongTien.getText().trim().equals("") || Double.parseDouble(txtTongTien.getText()) == 0.0){
            JOptionPane.showMessageDialog(null, "Vui lòng thêm sản phẩm vào hóa đơn", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }
    private boolean addCTHDVaoTable(){
        if(!validateCTHD()){
            return false;
        }
        String maSP = txtMaSP.getText().trim();
        String donGia = txtDonGia.getText().trim();
        String soLuong = txtSoLuong.getText().trim();
        String thanhTien = txtThanhTien.getText().trim();

        Object[] rowData = {maSP, donGia, soLuong, thanhTien};
        tableModel.addRow(rowData);
        resetForm();
        calculateTongTien();
        return true;
    }
    private void calculateTongTien(){
        double tongTien = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String thanhTienStr = (String) tableModel.getValueAt(i, 3);
            double thanhTien = Double.parseDouble(thanhTienStr);
            tongTien += thanhTien;
        }
        txtTongTien.setText(String.valueOf(tongTien));
    }
    private void handleTimSP() throws RemoteException {
        if(!validateMaSP()){
            return;
        }
        String maSP = txtMaSP.getText().trim();
        SanPham sp = sanPhamService.findOne(maSP);
        if(sp == null){
            JOptionPane.showMessageDialog(null, "Không tìm thấy sản phẩm", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        txtMaSP.setText(maSP);
        if(khuyenMaiService.getKhuyenMaiBySanPhamId(maSP) == null){
            txtDonGia.setText(String.valueOf(sp.getGiaBan()));}
        else {
            txtDonGia.setText(String.valueOf(sp.getGiaBan() - khuyenMaiService.getKhuyenMaiBySanPhamId(maSP).getTienGiam()));
        }

    }

    private void handleSoLuongChange() throws RemoteException {
        if (!validateCTHD()) return;

        try {
            double donGia = Double.parseDouble(txtDonGia.getText());
            int soLuong = Integer.parseInt(txtSoLuong.getText());
            txtThanhTien.setText(String.valueOf(donGia * soLuong));
        } catch (NumberFormatException e) {
            txtThanhTien.setText("");
        }
    }

    private  void handleClickRowTable() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String maSP = (String) tableModel.getValueAt(selectedRow, 0);
            String donGia = (String) tableModel.getValueAt(selectedRow, 1);
            String soLuong = (String) tableModel.getValueAt(selectedRow, 2);
            String thanhTien = (String) tableModel.getValueAt(selectedRow, 3);

            txtMaSP.setText(maSP);
            txtDonGia.setText(donGia);
            txtSoLuong.setText(soLuong);
            txtThanhTien.setText(thanhTien);
        }
    }
    private void handleAddHD() throws RemoteException {
        if(!validateAddHD()){
            return;
        }
        String maHoaDon ="HD" + (String.format("%05d",Integer.parseInt(hoaDonService.findAll().get(hoaDonService.findAll().size()-1).getMaHoaDon().replace("HD",""))+1)) ;
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon(maHoaDon);
        hoaDon.setNgayLapHoaDon(LocalDate.now());
        hoaDon.setKhachHang(khachHangService.findById(txtMaKH.getText()));
        hoaDon.setDiemTichLuySuDung(Integer.parseInt(txtDiemTichLuyDung.getText()));
        hoaDon.setNhanVien(nhanVienService.getNhanVienById(txtMaNV.getText()));
        hoaDon.setGhiChu(txtGhiChu.getText());
        hoaDon.setTongTien(Double.parseDouble(txtTongTien.getText()));
        List<ChiTietHoaDon> chiTietHoaDonList = new ArrayList<>();
        System.out.println("oke1");
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String maSP = (String) tableModel.getValueAt(i, 0);
                double donGia = Double.parseDouble((String) tableModel.getValueAt(i, 1));
                int soLuong = Integer.parseInt((String) tableModel.getValueAt(i, 2));
                ChiTietHoaDon cthd = new ChiTietHoaDon(soLuong, donGia, maHoaDon, maSP);
                chiTietHoaDonList.add(cthd);
            }
        hoaDon.setChiTietHoaDons(chiTietHoaDonList);

        if(hoaDonService.lapHoaDon(hoaDon)){
            JOptionPane.showMessageDialog(null, "Thêm hóa đơn thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            resetTable();
            resetForm();
            txtMaKH.setText("");
            txtTimSDT.setText("");
            txtTimTenKH.setText("");
            txtDiemTichLuy.setText("");
            txtDiemTichLuyDung.setText("");
            txtTongTien.setText("");
        }
    }
    private void resetTable(){
        tableModel.setRowCount(0);
        txtMaSP.setText("");
        txtDonGia.setText("");
        txtSoLuong.setText("");
        txtThanhTien.setText("");
    }
    private void addEvent(){
        btnThem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(addCTHDVaoTable()){
                    JOptionPane.showMessageDialog(null, "Thêm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });
        btnTimSP.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handleTimSP();
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetForm();
            }
        });
        btnThemHD.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    handleAddHD();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handleClickRowTable();
            }
        });
        txtSoLuong.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                try {
                    handleSoLuongChange();
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnSua.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    updateChiTiet();
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteChiTiet();
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });

        btnTimKHTheoSDT.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleTimKHTheoSDT();
            }
        });

    }
    private boolean updateChiTiet() throws RemoteException {
        if (!validateCTHD()) {
            return false;
        }

        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String maSP = txtMaSP.getText().trim();
            String donGia = txtDonGia.getText().trim();
            String soLuong = txtSoLuong.getText().trim();
            String thanhTien = txtThanhTien.getText().trim();

            tableModel.setValueAt(maSP, selectedRow, 0);
            tableModel.setValueAt(donGia, selectedRow, 1);
            tableModel.setValueAt(soLuong, selectedRow, 2);
            tableModel.setValueAt(thanhTien, selectedRow, 3);

            resetForm();
            calculateTongTien();
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm để sửa", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

    }

    private boolean deleteChiTiet() throws RemoteException {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
                resetForm();
                calculateTongTien();
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn sản phẩm để xóa", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

}