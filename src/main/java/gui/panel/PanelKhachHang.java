package gui.panel;

import gui.components.ComponentUtils;
import model.GioiTinh;
import model.KhachHang;
import net.datafaker.Faker;

import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

import static gui.panel.RmiServiceLocator.khachHangService;

public class PanelKhachHang extends JPanel {
    private JTextField txtSDT;
    private JComboBox<String> cboGioiTinh;
    private JButton btnThem, btnXoa, btnSua, btnReset, btnTim;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel jLableTenNV;
    private JLabel jLableMaKH;
    private JTextField txtMaKH;
    private JTextField txtTenKH;
    private JLabel jLabelSDT;
    private JTextField txtDiemTichluy;
    private JTextField txtTimMaKH;
    private JTextField txtTimTenKH;
    private JLabel lblMaNV;
    private JTextField txtTimSDT;

    private final Faker faker = new Faker();


    public PanelKhachHang() throws NamingException, RemoteException {

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JPanel infoPanel = createInfoPanel();
        add(infoPanel, BorderLayout.NORTH);

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        add(container, BorderLayout.CENTER);

        JPanel searchPanel = createSearchPanel();
        container.add(searchPanel, BorderLayout.NORTH);

        JPanel tablePanel = createTablePanel();
        container.add(tablePanel, BorderLayout.CENTER);

        addSampleData();
    }

    private JPanel createInfoPanel() throws RemoteException{
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());


        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 10, 20),
                ComponentUtils.getTitleBorder("Thông tin khách hàng")
        ));

        Box boxFormInput = Box.createVerticalBox();

        Box box1 = Box.createHorizontalBox();
        jLableMaKH = new JLabel("Mã NV:") ;
        jLableMaKH.setPreferredSize(new Dimension(90, 25));
        txtMaKH = new JTextField(20);
        txtMaKH.setEditable(false);
        jLableTenNV = new JLabel("Tên khách hàng:") ;
        jLableTenNV.setPreferredSize(new Dimension(90, 25));
        txtTenKH = new JTextField(20);
        jLabelSDT = new JLabel("Số điện thoại");
        jLabelSDT.setPreferredSize(new Dimension(90, 25));
        txtSDT = new JTextField(20);

        box1.add(Box.createHorizontalStrut(50));
        box1.add(jLableMaKH);
        box1.add(txtMaKH);
        box1.add(Box.createHorizontalStrut(30));
        box1.add(jLableTenNV);
        box1.add(txtTenKH);
        box1.add(Box.createHorizontalStrut(30));
        box1.add(jLabelSDT);
        box1.add(txtSDT);
        box1.add(Box.createHorizontalStrut(50));

        Box box2 = Box.createHorizontalBox();
        JLabel jLabelGioiTinh = new JLabel("Giới tính:");
        jLabelGioiTinh.setPreferredSize(new Dimension(90, 25));
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        cboGioiTinh.setPreferredSize(new Dimension(257, 25));
        JLabel jLabelChucvu = new JLabel("Điểm tích lũy:");
        jLabelChucvu.setPreferredSize(new Dimension(90, 25));
        txtDiemTichluy = new JTextField(20);
        txtDiemTichluy.setPreferredSize(new Dimension(205, 25));


        box2.add(Box.createHorizontalStrut(50));
        box2.add(jLabelGioiTinh);
        box2.add(cboGioiTinh);
        box2.add(Box.createHorizontalStrut(30));
        box2.add(jLabelChucvu);
        box2.add(txtDiemTichluy);
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

        panel.add(buttonPanel, BorderLayout.SOUTH);

        addButtonListeners();

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 240, 240));



        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                ComponentUtils.getTitleBorder("Tra cứu thông tin khách hàng")
        ));

        JPanel searchByIDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchByIDPanel.setBackground(new Color(240, 240, 240));

        lblMaNV = new JLabel("Mã KH:");
        lblMaNV.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTimMaKH = new JTextField(10);
        txtTimMaKH.setFont(new Font("Arial", Font.PLAIN, 14));

        btnTim = new JButton("Tìm kiếm");
        btnTim.setFont(new Font("Arial", Font.BOLD, 14));
        btnTim.setBackground(new Color(33, 150, 243));
        btnTim.setForeground(Color.WHITE);
        btnTim.setFocusPainted(false);
        btnTim.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnTim.setIcon(new ImageIcon(getClass().getResource("/image/search.png")));

        searchByIDPanel.add(lblMaNV);
        searchByIDPanel.add(txtTimMaKH);
        searchByIDPanel.add(btnTim);

        // Panel lọc theo tên
        JPanel filterByNamePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        filterByNamePanel.setBackground(new Color(240, 240, 240));

        JLabel lblTenKH = new JLabel("Tên KH: ");
        lblTenKH.setFont(new Font("Arial", Font.PLAIN, 15));
        txtTimTenKH = new JTextField(15);
        txtTimTenKH.setPreferredSize(new Dimension(205, 30));
        txtTimTenKH.setFont(new Font("Arial", Font.PLAIN, 15));

        JLabel lblSDT = new JLabel("Số điện thoại: ");
        lblSDT.setFont(new Font("Arial", Font.PLAIN, 15));
        txtTimSDT = new JTextField(15);
        txtTimSDT.setPreferredSize(new Dimension(205, 30));
        txtTimSDT.setFont(new Font("Arial", Font.PLAIN, 15));

        filterByNamePanel.add(lblTenKH);
        filterByNamePanel.add(txtTimTenKH);
        filterByNamePanel.add(lblSDT);
        filterByNamePanel.add(txtTimSDT);

        panel.add(searchByIDPanel, BorderLayout.WEST);
        panel.add(filterByNamePanel, BorderLayout.EAST);

        searchByIDPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        filterByNamePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        btnTim.addActionListener(e -> searchCustomer());


        txtTimTenKH.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    timKiemTheoTen();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    timKiemTheoTen();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            private void timKiemTheoTen() throws RemoteException {
                String tenKH = txtTimTenKH.getText().trim();
                if (!tenKH.isEmpty()) {
                    try {
                        List<KhachHang> dsKH = khachHangService.findAll();
                        List<KhachHang> khachHangTheoTen = dsKH.stream()
                                        .filter(kh -> kh.getTenKhachHang().contains(tenKH))
                                        .collect(Collectors.toList());
                        hienThiKetQuaTimKiem(khachHangTheoTen);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(PanelKhachHang.this,
                                "Lỗi kết nối máy chủ: " + ex.getMessage(),
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    resetTable();
                }
            }
        });

        txtTimSDT.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    timKiemTheoSDT();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                try {
                    timKiemTheoSDT();
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            private void timKiemTheoSDT() throws RemoteException {
                String sdt = txtTimSDT.getText().trim();
                if (!sdt.isEmpty()) {
                    try {
                        List<KhachHang> allKhachHang = khachHangService.findAll();

                        List<KhachHang> khachHangTheoSoDienThoai = allKhachHang.stream()
                                .filter(kh -> kh.getSoDienThoai().contains(sdt))
                                .collect(Collectors.toList());

                        hienThiKetQuaTimKiem(khachHangTheoSoDienThoai);

                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(PanelKhachHang.this,
                                "Lỗi kết nối máy chủ: " + ex.getMessage(),
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    resetTable();
                }
            }
        });


        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel jPanelLamMoi = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton lammoi = new JButton("Refresh");

        ComponentUtils.setButtonMain(lammoi);
        jPanelLamMoi.add(lammoi);
        lammoi.setIcon(new ImageIcon(getClass().getResource("/image/refresh.png")));


        lammoi.addActionListener(e -> {
            try {
                resetTable();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });

        String[] columns = {"Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Giới tính", "Điểm tích lũy"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ComponentUtils.setTable(table);


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displaySelectedCustomer();
            }
        });

        panel.add(jPanelLamMoi, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addButtonListeners() throws RemoteException{
        btnThem.addActionListener(e -> addCustomer());
        btnXoa.addActionListener(e -> deleteCustomer());
        btnSua.addActionListener(e -> updateCustomer());
        btnReset.addActionListener(e -> resetForm());
    }

    private void addCustomer() {
        try {
            if (validateInput()) {
                String maKH = "KH" + faker.number().digits(5);
                String tenKH = txtTenKH.getText();
                String sdt = txtSDT.getText();
                String gioiTinh = cboGioiTinh.getSelectedItem().toString();
                String enumGioiTinh = gioiTinh.equals("Nam") ? "NAM" : "NU";
                String diemTichLuy = txtDiemTichluy.getText().trim();
                int diem = Integer.parseInt(diemTichLuy);

                GioiTinh enumGT = GioiTinh.valueOf(enumGioiTinh);
                KhachHang khachHang = new KhachHang(maKH, tenKH, sdt, enumGT, diem);

                if(khachHangService.themKhachHang(khachHang)) {
                    addSampleData();
                    resetForm();
                    JOptionPane.showMessageDialog(this, "Đã thêm khách hàng thành công");
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể thêm khách hàng");
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String maKH = table.getValueAt(selectedRow, 0).toString();

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Bạn có chắc chắn muốn xóa khách hàng này?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (khachHangService.delete(maKH)) {
                        addSampleData();
                        resetForm();
                        JOptionPane.showMessageDialog(
                                this,
                                "Xóa khách hàng thành công!",
                                "Thành công",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "Không thể xóa khách hàng. Vui lòng thử lại!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn khách hàng cần xóa!",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi kết nối máy chủ: " + e.getMessage(),
                    "Lỗi kết nối",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomer() {

        try {
            int row = table.getSelectedRow();
            if (row >= 0) {
                if(validateInput()) {
                    String maKH = table.getValueAt(row, 0).toString();
                    String tenKH = txtTenKH.getText().toString();
                    String sdt = txtSDT.getText();
                    String gioiTinh = cboGioiTinh.getSelectedItem().toString();
                    String enumGioiTinh = gioiTinh.equals("Nam") ? "NAM" : "NU";
                    String diemTichLuy = txtDiemTichluy.getText().trim();
                    int diem = Integer.parseInt(diemTichLuy);

                    GioiTinh enumGT = GioiTinh.valueOf(enumGioiTinh);
                    KhachHang khachHang = new KhachHang(maKH, tenKH, sdt, enumGT, diem);

                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Bạn có chắc muốn cập nhật khách hàng " + tenKH,
                            "Xác nhận cập nhật",
                            JOptionPane.YES_NO_OPTION
                    );

                    if(confirm == JOptionPane.YES_OPTION) {
                        if(khachHangService.suaKhachHang(maKH, khachHang)) {
                            addSampleData();
                            resetForm();
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Cập nhật khách hàng "+tenKH+"thành công",
                                    "Thành công",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Không thể cập nhật khách hàng" + tenKH,
                                    "Lỗi",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn khách cần cập nhật",
                        "Thông báo",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi kết nối máy chủ: " + e.getMessage(),
                    "Lỗi kết nối",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        txtMaKH.setText("");
        txtTenKH.setText("");
        txtSDT.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtDiemTichluy.setText("");
        table.clearSelection();
    }

    private void searchCustomer() {
        String makh = txtTimMaKH.getText().trim();

        try {
            KhachHang khachHang = khachHangService.findById(makh);
            if(khachHang != null) {
                tableModel.setRowCount(0);
                tableModel.addRow(new Object[]{
                        khachHang.getMaKhachHang(),
                        khachHang.getTenKhachHang(),
                        khachHang.getSoDienThoai(),
                        khachHang.getGioiTinh(),
                        khachHang.getDiemTichLuy()
                });
            } else {
                JOptionPane.showMessageDialog(null, "Mã " + makh + " không tồn tài");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "Lỗi kết nối máy chủ: " + e.getMessage(),
                    "Lỗi kết nối",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displaySelectedCustomer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {

            txtMaKH.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenKH.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtSDT.setText(tableModel.getValueAt(selectedRow, 2).toString());

            // Set giới tính
            String gioiTinh = tableModel.getValueAt(selectedRow, 3).toString();
            for (int i = 0; i < cboGioiTinh.getItemCount(); i++) {
                if (cboGioiTinh.getItemAt(i).equals(gioiTinh)) {
                    cboGioiTinh.setSelectedIndex(i);
                    break;
                }
            }
            txtDiemTichluy.setText(tableModel.getValueAt(selectedRow, 4).toString());
        }
    }

    private boolean validateInput() {
        if (txtTenKH.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên khách hàng!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenKH.requestFocus();
            return false;
        }

        if (txtSDT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }

        if (!txtSDT.getText().matches("^(03|05|07|08|09)\\d{8}$")) {
            JOptionPane.showMessageDialog(this,
                    "Số điện thoại phải có 10 chữ số và bắt đầu bằng 03, 05, 07, 08 hoặc 09",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            txtSDT.selectAll();
            return false;
        }

        try {
            int diem = Integer.parseInt(txtDiemTichluy.getText().trim());
            if (diem <= 0) {
                JOptionPane.showMessageDialog(this, "Điểm tích lũy phải là số lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtDiemTichluy.requestFocus();
                txtDiemTichluy.selectAll();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Điểm tích lũy phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtDiemTichluy.requestFocus();
            txtDiemTichluy.selectAll();
            return false;
        }

        return true;
    }

    private void resetTable() throws RemoteException {
       addSampleData();
    }

    private void addSampleData() throws RemoteException{
        tableModel.setRowCount(0);

        List<KhachHang> khachHangs = khachHangService.findAll();

//        String[] columns = {"Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Giới tính", "Điểm tích lũy"};
        for (KhachHang khachHang : khachHangs) {
            tableModel.addRow(new Object[]{
                    khachHang.getMaKhachHang(),
                    khachHang.getTenKhachHang(),
                    khachHang.getSoDienThoai(),
                    khachHang.getGioiTinh(),
                    khachHang.getDiemTichLuy()
            });
        }
    }

    private void hienThiKetQuaTimKiem(List<KhachHang> dsKH) {
        tableModel.setRowCount(0);
        if (dsKH != null && !dsKH.isEmpty()) {
            for (KhachHang khachHang : dsKH) {
                tableModel.addRow(new Object[]{
                        khachHang.getMaKhachHang(),
                        khachHang.getTenKhachHang(),
                        khachHang.getSoDienThoai(),
                        khachHang.getGioiTinh(),
                        khachHang.getDiemTichLuy()
                });
            }
        }
    }
}