package gui.panel;

import dto.HoaDonDTO;
import dto.TaiKhoanDTO;
import gui.components.ComponentUtils;
import io.github.cdimascio.dotenv.Dotenv;
import model.*;
import service.*;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static gui.panel.RmiServiceLocator.*;

public class PanelHoaDon extends JPanel {
    // Các trường cho phần chi tiết sản phẩm
    private JTextField txtMaSP, txtDonGia, txtSoLuong, txtThanhTien, txtTimMaHoaDon, txtTimTenKH;
    private JButton btnTimSP, btnThem , btnXoa, btnSua, btnReset, btnTimMaHD, btnTimKHTheoSDT ;

    private DecimalFormat df = new DecimalFormat("#,##0.00");

    // Các thành phần bên phải
    private JTextField txtMaNV;
    private JTextField txtDiemTichLuy;
    private JTextArea txtGhiChu;
    private JButton btnThemHD;

    private JTable table;
    private DefaultTableModel tableModel;


    private final Dotenv dotenv = Dotenv.load();
    private final String drivername = dotenv.get("DRIVER_NAME");
    private JLabel jLableMaSP;
    private JLabel jLableDonGia;
    private JLabel jLabelSoLuong , jLabelThanhTien, jlabelMaVach, lblDiem, lbltongTien;
    private JTextField txtMaKH, txtMaVach;
    private JTextField txtTimSDT;
    private JLabel buttonEmpty;
    private JTextField txtTongTien, txtTim;
    private JTextField txtDiemTichLuyDung;

    public PanelHoaDon() throws NamingException, RemoteException {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel infoPanel = createInfoPanel();
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
        btnTimKHTheoSDT.setForeground(new Color(255, 255, 255));
        btnTimKHTheoSDT.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnTimKHTheoSDT.setBackground(new Color(33, 150, 243));
//        ComponentUtils.setButton(btnTimKHTheoSDT, new Color(33, 150, 243));
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
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = {"Sản phẩm", "Khuyến Mãi", "Thuế VAT", "Đơn giá", "Số lượng", "Thành tiền"};
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
        btnTimSP.setIcon(new ImageIcon(getClass().getResource("/image/search.png")));

        ComponentUtils.setButtonMain(btnTimSP);
        jLableDonGia = new JLabel("Đơn giá:") ;
        jLableDonGia.setPreferredSize(new Dimension(90, 25));
        txtDonGia = new JTextField(20);
        txtDonGia.setEditable(false);
        jLabelSoLuong = new JLabel("Số lượng");
        jLabelSoLuong.setPreferredSize(new Dimension(90, 25));
        txtSoLuong = new JTextField(15);

        buttonEmpty = new JLabel("");
        buttonEmpty.setPreferredSize(new Dimension(95, 0));
        box1.add(Box.createHorizontalStrut(50));
        box1.add(jLableMaSP);
        box1.add(txtMaSP);
        box1.add(Box.createHorizontalStrut(10));
        box1.add(btnTimSP);
        box1.add(Box.createHorizontalStrut(50));
        box1.add(jLableDonGia);
        box1.add(txtDonGia);
        box1.add(Box.createHorizontalStrut(30));
       

        Box box2 = Box.createHorizontalBox();
        box2.add(Box.createHorizontalStrut(50));
        box2.add(jLabelSoLuong);
        box2.add(txtSoLuong);

//        box2.add(buttonEmpty);


        box2.add(Box.createHorizontalStrut(53));
        jLabelThanhTien = new JLabel("Thành tiền:");
        jLabelThanhTien.setPreferredSize(new Dimension(90, 33));
        txtThanhTien = new JTextField(15);
        txtThanhTien.setEditable(false);
        box2.add(jLabelThanhTien);
        box2.add(txtThanhTien);
        box2.add(Box.createHorizontalStrut(28));


        jlabelMaVach = new JLabel("Mã vạch:");
        jlabelMaVach.setPreferredSize(new Dimension(90, 33));
        txtMaVach = new JTextField(15);
        box2.add(jlabelMaVach);
        box2.add(txtMaVach);
        box2.add(Box.createHorizontalStrut(20));
        

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


        txtMaVach.addActionListener(e -> {
            String maVach = txtMaVach.getText().trim();


            try {
                SanPham sanPham = sanPhamService.findOne(maVach);
                if(sanPham.getTrangThai().equalsIgnoreCase("Ngưng bán")){
                    JOptionPane.showMessageDialog(null, "Sản phẩm đang ở trạng thái ngưng bán", "Thông báo", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                double donGia = sanPham.getGiaBan();
                int soLuong = 1;
                double tienGiam = sanPham.getKhuyenMai() == null || sanPham.getKhuyenMai().getNgayKetThuc().isBefore(LocalDate.now()) ? 0.0 : sanPham.getKhuyenMai().getTienGiam();
                double thanhTien = donGia * soLuong * (1 - tienGiam) * (1 + sanPham.getThueVAT());

                boolean isExist = false;
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    String maSanPham = tableModel.getValueAt(i, 0).toString().split("_")[0];
                    if(maSanPham.equalsIgnoreCase(maVach)){
                        isExist = true;
                        break;
                    }
                }



                if(isExist){
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        String maSanPham = tableModel.getValueAt(i, 0).toString().split("_")[0];
                        String soLuongTrongBang = tableModel.getValueAt(i, 4).toString();

                        if(maSanPham.equalsIgnoreCase(maVach) && sanPham.getSoLuongTon() >= (Integer.parseInt(soLuongTrongBang) + 1)){
                            String thanhTienGoc = tableModel.getValueAt(i, 5).toString().split(" VND")[0];
                            Number numberThanhTienGoc = df.parse(thanhTienGoc);
                            tableModel.setValueAt(1 + Integer.parseInt(tableModel.getValueAt(i, 4).toString()) + "", i, 4);
                            tableModel.setValueAt(df.format(thanhTien + numberThanhTienGoc.doubleValue()) + " VND", i, 5);
                            resetForm();
                            calculateTongTien();
                            break;
                        } else if (sanPham.getSoLuongTon() < (Integer.parseInt(soLuongTrongBang) + 1)){
                            JOptionPane.showMessageDialog(null,
                                    "Số lượng tồn không đáp ứng đủ",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
                else {
                    if(sanPham != null && sanPham.getKhuyenMai() == null && sanPham.getSoLuongTon() >= 1){
                        Object[] rowData = {maVach + "_" + sanPham.getTenSanPham(),
                                "0%",
                                String.format("%.2f", sanPham.getThueVAT() * 100) + "%",
                                df.format(donGia) + " VND",
                                soLuong,
                                df.format(thanhTien) + " VND"
                        };
                        tableModel.addRow(rowData);
                        resetForm();
                        calculateTongTien();
                    }
                    else if (sanPham != null  && sanPham.getKhuyenMai() != null && sanPham.getSoLuongTon() >= 1){
                        Object[] rowData = {maVach + "_" + sanPham.getTenSanPham(),
                                String.format("%.2f", (sanPham.getKhuyenMai().getNgayKetThuc().isBefore(LocalDate.now()) ? 0 : sanPham.getKhuyenMai().getTienGiam()) * 100) + "%",
                                String.format("%.2f", sanPham.getThueVAT() * 100) + "%",
                                df.format(donGia) + " VND",
                                soLuong,
                                df.format(thanhTien) + " VND"
                        };
                        tableModel.addRow(rowData);
                        resetForm();
                        calculateTongTien();
                    }
                    else {
                        JOptionPane.showMessageDialog(null,
                                "Số lượng tồn không đáp ứng đủ",
                                "Error",
                                JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            } finally {
                txtMaVach.setText("");
            }

        });

        return panel;
    }
    private void handleTimKHTheoSDT(){
        KhachHang kh = findBySDT();
        if(kh!=null){
            txtMaKH.setText(kh.getMaKhachHang());
            txtTimTenKH.setText(kh.getTenKhachHang());
            txtDiemTichLuy.setText(kh.getDiemTichLuy()+"");
        }else{
            JOptionPane.showMessageDialog(null, "Không tìm thấy khách hàng", "Thông báo", JOptionPane.WARNING_MESSAGE);
        }
    }
    private KhachHang findBySDT(){
        if(txtTimSDT.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null, "Vui lòng nhập số điện thoại", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return null;
        }
        try {
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
    private boolean validateAddHD() throws ParseException, RemoteException {
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
            JOptionPane.showMessageDialog(null, "Điểm tích lũy phải là số nguyên không âm", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(Integer.parseInt(txtDiemTichLuyDung.getText()) > Double.parseDouble(txtDiemTichLuy.getText())){
            JOptionPane.showMessageDialog(null, "Điểm tích lũy không được lớn hơn điểm tích lũy hiện tại", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        if(txtTongTien.getText().trim().equals("") || df.parse(txtTongTien.getText().split(" VND")[0]).doubleValue() == 0.0){
            JOptionPane.showMessageDialog(null, "Vui lòng thêm sản phẩm vào hóa đơn", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }
    private boolean addCTHDVaoTable() throws RemoteException, ParseException {
        if(!validateCTHD()){
            return false;
        }
        String maSP = txtMaSP.getText().trim();
        String donGia = txtDonGia.getText().trim();
        String soLuong = txtSoLuong.getText().trim();
        String thanhTien = txtThanhTien.getText().trim();
        SanPham sanPham = sanPhamService.findOne(maSP);

        if(sanPham.getTrangThai().equalsIgnoreCase("Ngưng bán")){
            JOptionPane.showMessageDialog(null, "Sản phẩm đang ở trạng thái ngưng bán", "Thông báo", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        boolean isExist = false;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String maSanPham = tableModel.getValueAt(i, 0).toString().split("_")[0];
            if(maSanPham.equalsIgnoreCase(maSP)){
                isExist = true;
                break;
            }
        }

        if(isExist){
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String maSanPham = tableModel.getValueAt(i, 0).toString().split("_")[0];
                String soLuongTrongBang = tableModel.getValueAt(i, 4).toString();

                if(maSanPham.equalsIgnoreCase(maSP) && sanPham.getSoLuongTon() >= (Integer.parseInt(soLuongTrongBang) + Integer.parseInt(soLuong))){
                    String thanhTienGoc = tableModel.getValueAt(i, 5).toString().split(" VND")[0];
                    Number numberThanhTienGoc = df.parse(thanhTienGoc);
                    tableModel.setValueAt(Integer.parseInt(soLuong) + Integer.parseInt(tableModel.getValueAt(i, 4).toString()) + "", i, 4);
                    tableModel.setValueAt(df.format(Double.parseDouble(txtThanhTien.getText()) + numberThanhTienGoc.doubleValue()) + " VND", i, 5);
                    resetForm();
                    calculateTongTien();
                    break;
                } else if (sanPham.getSoLuongTon() < (Integer.parseInt(soLuongTrongBang) + Integer.parseInt(soLuong))){
                    JOptionPane.showMessageDialog(null,
                            "Số lượng tồn không đáp ứng đủ",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        }
        else {
            if(sanPham != null && sanPham.getKhuyenMai() == null && sanPham.getSoLuongTon() >= Integer.parseInt(soLuong)){
                Object[] rowData = {maSP + "_" + sanPham.getTenSanPham(),
                        "0%",
                        String.format("%.2f", sanPham.getThueVAT() * 100) + "%",
                        df.format(Double.parseDouble(donGia)) + " VND",
                        soLuong,
                        df.format(Double.parseDouble(thanhTien)) + " VND"
                };
                tableModel.addRow(rowData);
                resetForm();
                calculateTongTien();
            }
            else if (sanPham != null  && sanPham.getKhuyenMai() != null && sanPham.getSoLuongTon() >= Integer.parseInt(soLuong)){
                Object[] rowData = {maSP + "_" + sanPham.getTenSanPham(),
                        String.format("%.2f", (sanPham.getKhuyenMai().getNgayKetThuc().isBefore(LocalDate.now()) ? 0 : sanPham.getKhuyenMai().getTienGiam()) * 100) + "%",
                        String.format("%.2f", sanPham.getThueVAT() * 100) + "%",
                        df.format(Double.parseDouble(donGia)) + " VND",
                        soLuong,
                        df.format(Double.parseDouble(thanhTien)) + " VND"
                };
                tableModel.addRow(rowData);
                resetForm();
                calculateTongTien();
            }
            else {
                JOptionPane.showMessageDialog(null,
                        "Số lượng tồn không đáp ứng đủ",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }

        return false;
    }
    private void calculateTongTien() throws ParseException {
        double tongTien = 0;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String thanhTienStr = tableModel.getValueAt(i, 5).toString().split("VND")[0];
            Number number = df.parse(thanhTienStr);
            double thanhTien = number.doubleValue();
            tongTien += thanhTien;
        }
        txtTongTien.setText(df.format(tongTien) + " VND");
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


        txtDonGia.setText(String.valueOf(sp.getGiaBan()));
        if(!txtSoLuong.getText().isEmpty()){
            SanPham sanPham = sanPhamService.findOne(maSP);
            double donGia = Double.parseDouble(txtDonGia.getText());
            int soLuong = Integer.parseInt(txtSoLuong.getText());
            double tienGiam = sanPham.getKhuyenMai() == null || sanPham.getKhuyenMai().getNgayKetThuc().isBefore(LocalDate.now()) ? 0.0 : sanPham.getKhuyenMai().getTienGiam();
            txtThanhTien.setText(String.valueOf(donGia * soLuong * (1 - tienGiam) * (1 + sanPham.getThueVAT())));
        }




    }
    private void handleSoLuongChange() throws RemoteException {
        if (!validateCTHD()) return;

        try {
            String maSP = txtMaSP.getText();
            SanPham sanPham = sanPhamService.findOne(maSP);
            if(sanPham != null){
                double donGia = Double.parseDouble(txtDonGia.getText());
                int soLuong = Integer.parseInt(txtSoLuong.getText());
                double tienGiam = sanPham.getKhuyenMai() == null || sanPham.getKhuyenMai().getNgayKetThuc().isBefore(LocalDate.now()) ? 0.0 : sanPham.getKhuyenMai().getTienGiam();
                txtThanhTien.setText(String.valueOf(donGia * soLuong * (1 - tienGiam) * (1 + sanPham.getThueVAT())));
            }
        } catch (NumberFormatException e) {
            txtThanhTien.setText("");
        }
    }
    private  void handleClickRowTable() throws ParseException {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String maSP = tableModel.getValueAt(selectedRow, 0).toString().split("_")[0];
            String donGia =  tableModel.getValueAt(selectedRow, 3).toString().split(" VND")[0];
            String soLuong = (String) tableModel.getValueAt(selectedRow, 4);
            String thanhTien =  tableModel.getValueAt(selectedRow, 5).toString().split(" VND")[0];

            Number numberDonGia = df.parse(donGia);
            Number numberThanhTien = df.parse(thanhTien);

            txtMaSP.setText(maSP);
            txtDonGia.setText(numberDonGia.doubleValue() + "");
            txtSoLuong.setText(soLuong);
            txtThanhTien.setText(numberThanhTien.doubleValue() + "");
        }
    }
    private void handleAddHD() throws RemoteException, ParseException, NamingException {
        if(!validateAddHD()){
            return;
        }

        String maHoaDon = "HD";
        HoaDon hoaDon = new HoaDon(LocalDate.now(),
                maHoaDon, Integer.parseInt(txtDiemTichLuyDung.getText()),
                txtGhiChu.getText().trim().toString(), khachHangService.findById(txtMaKH.getText()),
                nhanVienService.getNhanVienById(txtMaNV.getText()));

        List<ChiTietHoaDon> chiTietHoaDonList = new ArrayList<>();
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String maSP = tableModel.getValueAt(i, 0).toString().split("_")[0];
                String donGia = tableModel.getValueAt(i, 3).toString().split(" VND")[0];
                double khuyenMai = Double.parseDouble(tableModel.getValueAt(i, 1).toString().split("%")[0]);
                double thueVAT = Double.parseDouble(tableModel.getValueAt(i, 2).toString().split("%")[0]);
                Number numberDonGia = df.parse(donGia);
                double donGiaMain = numberDonGia.doubleValue();
                double donGiaAfterThueAndKhuyenMai = donGiaMain * (1 - (khuyenMai/100)) * (1 + (thueVAT/100));
                int soLuong = Integer.parseInt((String) tableModel.getValueAt(i, 4));
                ChiTietHoaDon cthd = new ChiTietHoaDon(soLuong, donGiaAfterThueAndKhuyenMai, hoaDon, sanPhamService.findOne(maSP));
                chiTietHoaDonList.add(cthd);
            }
        hoaDon.setChiTietHoaDons(chiTietHoaDonList);
        hoaDon.setDiemTichLuySuDung(Integer.parseInt(txtDiemTichLuyDung.getText()));
        int diemTichLuyCong = (int) Math.ceil(hoaDon.getTongTien() * 0.01);
        if(khachHangService.capNhatDiemTichLuy(hoaDon.getKhachHang().getMaKhachHang(), diemTichLuyCong - hoaDon.getDiemTichLuySuDung())){
            hoaDon.setKhachHang(khachHangService.findById(txtMaKH.getText()));
        }
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


            HoaDonDTO.setHoaDon(hoaDon);
            JFrame frame = new JFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            JDialogHoaDon dialog = new JDialogHoaDon(frame);
            dialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Thêm hóa đơn thất bại", "Lỗi", JOptionPane.ERROR_MESSAGE);

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
                try {
                    if(addCTHDVaoTable()){
                        JOptionPane.showMessageDialog(null, "Thêm thành công", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
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
                } catch (RemoteException | ParseException | NamingException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                try {
                    handleClickRowTable();
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
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
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btnXoa.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteChiTiet();
                } catch (RemoteException | ParseException ex) {
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
    private boolean updateChiTiet() throws RemoteException, ParseException {
        if (!validateCTHD()) {
            return false;
        }

        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
            String maSP = txtMaSP.getText().trim();
            String donGia = txtDonGia.getText().trim();
            String soLuong = txtSoLuong.getText().trim();
            String thanhTien = txtThanhTien.getText().trim();
            SanPham sanPham = sanPhamService.findOne(maSP);
            if(sanPham != null){
                if(Integer.parseInt(soLuong) > sanPham.getSoLuongTon()){
                    JOptionPane.showMessageDialog(
                            null,
                            "Số lượng tồn không đáp ứng đủ",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return false;
                }
                tableModel.setValueAt(maSP + "_" + sanPham.getTenSanPham(), selectedRow, 0);
                tableModel.setValueAt(String.format("%.2f", sanPham.getThueVAT() * 100) + "%", selectedRow, 2);
                tableModel.setValueAt(df.format(Double.parseDouble(donGia)) + " VND", selectedRow, 3);
                tableModel.setValueAt(soLuong, selectedRow, 4);
                tableModel.setValueAt(df.format(Double.parseDouble(thanhTien)) + " VND", selectedRow, 5);
                if(sanPham.getKhuyenMai() != null && sanPham.getKhuyenMai().getNgayKetThuc().isAfter(LocalDate.now())){
                    tableModel.setValueAt(String.format("%.2f", sanPham.getKhuyenMai().getTienGiam() * 100) + "%", selectedRow, 1);
                } else {
                    tableModel.setValueAt( "0%", selectedRow, 1);
                }
            }


            resetForm();
            calculateTongTien();
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn chi tiết để sửa", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }

    }

    private boolean deleteChiTiet() throws RemoteException, ParseException {
        int selectedRow = table.getSelectedRow();
        if (selectedRow != -1) {
                tableModel.removeRow(selectedRow);
                resetForm();
                calculateTongTien();
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "Vui lòng chọn chi tiết để xóa", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return false;
        }
    }

}