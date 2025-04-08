package gui.panel;

import InterF.SanPhamDAOInterface;
import InterF.ThuocTinhSanPhamDAOInterface;
import model.SanPham;
import model.ThuocTinhSanPham;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

public class PanelSanPham extends JPanel {
    public PanelSanPham() throws NamingException, RemoteException {
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tạo tiêu đề căn giữa
        JLabel titleLabel = new JLabel("Giao diện quản lý sản phẩm", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // tạo khoảng cách

        add(titleLabel, BorderLayout.NORTH);



//        this.maSanPham = maSanPham;
//        this.tenSanPham = tenSanPham;
//        this.hanSuDung = hanSuDung;
//        this.giaBan = giaBan;
//        this.thueVAT = thueVAT;
//        this.trangThai = trangThai;
//        this.soLuongTon = soLuongTon;
//        this.ngayNhap = ngayNhap;
//        this.moTa = moTa;

        // Tên cột cho bảng
        String[] columnNames = {"Mã SP", "Tên sản phẩm", "Loại sản phẩm", "Giá bán", "Số lượng", "Thuế VAT", "Ngày nhập", "Thuộc tính", "Mô tả", "Hạn sử dụng", "Trạng thái"};

        // Tạo model cho bảng với data
        DefaultTableModel dataModel = new DefaultTableModel(columnNames, 0);


        JTable jTableContent = new JTable(dataModel);


        Context context = new InitialContext();
        SanPhamDAOInterface sanPhamDAO = (SanPhamDAOInterface)context.lookup("rmi://LAPTOP-MB2815MQ:9090/sanPhamDAO");
        ThuocTinhSanPhamDAOInterface thuocTinhSanPhamDAO = (ThuocTinhSanPhamDAOInterface)context.lookup("rmi://LAPTOP-MB2815MQ:9090/thuocTinhSanPhamDAO");
        sanPhamDAO.getList().forEach(sanPham -> {
            try {
                List<ThuocTinhSanPham> thuocTinhSanPhamList =  thuocTinhSanPhamDAO.getListByProductId(sanPham.getMaSanPham());
               String thuocTinh =  thuocTinhSanPhamList.stream().map(thuocTinhSanPham ->
                       thuocTinhSanPham.getTenThuocTinh() + ": " + thuocTinhSanPham.getGiaTriThuocTinh()
               ).collect(Collectors.joining(", "));


                dataModel.addRow ( new Object[]{
                        sanPham.getMaSanPham(),
                        sanPham.getTenSanPham(),
                        sanPham.getDanhMucSanPham().getTenDanhMucSanPham(),
                        sanPham.getGiaBan(),
                        sanPham.getSoLuongTon(),
                        String.format("%.2f", sanPham.getThueVAT()),
                        sanPham.getNgayNhap().toString(),
                        thuocTinh,
                        sanPham.getMoTa(),
                        sanPham.getHanSuDung().toString(),
                        sanPham.getTrangThai()
                });
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });




        // Tùy chỉnh độ cao hàng và font chữ nếu cần
        jTableContent.setRowHeight(30);
        jTableContent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTableContent.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

        // Thêm bảng vào JScrollPane để có thể cuộn
        JScrollPane scrollPane = new JScrollPane(jTableContent);

        Box BoxHozi = Box.createVerticalBox();

        BoxHozi.add(scrollPane);
        add(BoxHozi, BorderLayout.CENTER);

        Box boxGapHeight = Box.createHorizontalBox();
        boxGapHeight.add(Box.createVerticalStrut(20));
        BoxHozi.add(boxGapHeight);

        boxGapHeight.add(Box.createVerticalStrut(100));
        BoxHozi.add(boxGapHeight);

//        JLabel label = new JLabel("📦 Giao diện Quản lý Sản phẩm", SwingConstants.CENTER);
//        label.setFont(new Font("Segoe UI", Font.BOLD, 24));
//        add(label, BorderLayout.CENTER);

        // Panel nhập liệu và nút chức năng
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Thông tin nhập"),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // khoảng cách giữa các thành phần
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

//        GridBagConstraints gbc = new GridBagConstraints();
//        gbc.insets = new Insets(5, 10, 5, 10); // padding giữa các thành phần
//        gbc.anchor = GridBagConstraints.WEST;
//        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Khởi tạo các label và textfield
        JLabel jLabelSP = new JLabel("Mã SP:");
        JTextField txtMaSP = new JTextField(20);

        JLabel jLabelTenSP = new JLabel("Tên sản phẩm:");
        JTextField txtTenSP = new JTextField(20);

        JLabel jLabelLoaiSP = new JLabel("Loại:");
        JTextField txtLoai = new JTextField(20);

        JLabel jLabelGiaSP = new JLabel("Giá:");
        JTextField txtGia = new JTextField(20);

        JLabel jLabelSoLuongSP = new JLabel("Số lượng:");
        JTextField txtSoLuong = new JTextField(20);

        JLabel jLabelNhaCungCap = new JLabel("Nhà cung cấp:");
        JTextField txtNhaCungCap = new JTextField(20);


        Font labelFont = new Font("Arial", Font.BOLD, 14); // Chữ lớn và đậm
        Font textFieldFont = new Font("Arial", Font.PLAIN, 14); // Chữ to cho input

        // Set font cho tất cả label
        jLabelSP.setFont(labelFont);
        jLabelTenSP.setFont(labelFont);
        jLabelLoaiSP.setFont(labelFont);
        jLabelGiaSP.setFont(labelFont);
        jLabelSoLuongSP.setFont(labelFont);
        jLabelNhaCungCap.setFont(labelFont);

        // Set font và size cho textfield
        Dimension textFieldSize = new Dimension(900, 30); // Kích thước to hơn

        txtMaSP.setPreferredSize(textFieldSize);
        txtTenSP.setPreferredSize(textFieldSize);
        txtLoai.setPreferredSize(textFieldSize);
        txtGia.setPreferredSize(textFieldSize);
        txtSoLuong.setPreferredSize(textFieldSize);
        txtNhaCungCap.setPreferredSize(textFieldSize);

        txtMaSP.setFont(textFieldFont);
        txtTenSP.setFont(textFieldFont);
        txtLoai.setFont(textFieldFont);
        txtGia.setFont(textFieldFont);
        txtSoLuong.setFont(textFieldFont);
        txtNhaCungCap.setFont(textFieldFont);


        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Dòng 1
        gbc.gridx = 0; gbc.gridy = 0;
        inputPanel.add(jLabelSP, gbc);
        gbc.gridx = 1;
        inputPanel.add(txtMaSP, gbc);

        gbc.gridx = 2;
        inputPanel.add(jLabelTenSP, gbc);
        gbc.gridx = 3;
        inputPanel.add(txtTenSP, gbc);

        // Dòng 2
        gbc.gridx = 0; gbc.gridy = 1;
        inputPanel.add(jLabelLoaiSP, gbc);
        gbc.gridx = 1;
        inputPanel.add(txtLoai, gbc);

        gbc.gridx = 2;
        inputPanel.add(jLabelGiaSP, gbc);
        gbc.gridx = 3;
        inputPanel.add(txtGia, gbc);

        // Dòng 3
        gbc.gridx = 0; gbc.gridy = 2;
        inputPanel.add(jLabelSoLuongSP, gbc);
        gbc.gridx = 1;
        inputPanel.add(txtSoLuong, gbc);

        gbc.gridx = 2;
        inputPanel.add(jLabelNhaCungCap, gbc);
        gbc.gridx = 3;
        inputPanel.add(txtNhaCungCap, gbc);

        bottomPanel.add(inputPanel, BorderLayout.CENTER);




        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnThem = new JButton("Thêm");
        JButton btnXoa = new JButton("Xóa");
        JButton btnSua = new JButton("Sửa");
        JButton btnReset = new JButton("Reset");

        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnReset);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        BoxHozi.add(bottomPanel);

//        add(bottomPanel, BorderLayout.SOUTH);
    }

    // Hàm tạo panel chứa label và textfield để dùng cho GridLayout
    private JPanel createLabeledField(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel(labelText);
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }
}


