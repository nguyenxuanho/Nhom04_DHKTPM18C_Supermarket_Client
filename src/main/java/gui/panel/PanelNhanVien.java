package gui.panel;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;
import gui.components.ComponentUtils;
import io.github.cdimascio.dotenv.Dotenv;
import model.ChucVuNhanVien;
import model.GioiTinh;
import model.NhanVien;
import net.datafaker.Faker;
import service.NhanVienService;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static gui.panel.RmiServiceLocator.nhanVienService;

public class PanelNhanVien extends JPanel {
    private JTextField txtMaNV, txtTenNV, txtSDT, txtDiaChi, txtSoDinhDanh, txtTimMaNV, txtTimTenNV;
    private JComboBox<String> cboGioiTinh, cboChucVu;
    private JButton btnThem, btnXoa, btnSua, btnReset, btnTim;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel jLableMaNV;
    private JLabel jLableTenNV;
    private JLabel jLableNS;
    private JDateChooser dateNgaySinh;
    private JComboBox<String> cboChucVuLoc;

    private final Faker faker = new Faker();



    public PanelNhanVien() throws NamingException, RemoteException{
        setLayout(new BorderLayout(10, 10));

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel thông tin nhân viên
        JPanel infoPanel = createInfoPanel();
        add(infoPanel, BorderLayout.NORTH);

        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        add(container, BorderLayout.CENTER);

        // Panel tìm kiếm
        JPanel searchPanel = createSearchPanel();
        container.add(searchPanel, BorderLayout.NORTH);

        // Panel bảng dữ liệu
        JPanel tablePanel = createTablePanel();
        container.add(tablePanel, BorderLayout.CENTER);

        // Thêm dữ liệu mẫu
        addSampleData();
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        Border border = BorderFactory.createLineBorder(new Color(33, 150, 243), 2);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(border,
                "Thông tin nhân viên",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                Color.WHITE);

        titledBorder.setTitleColor(new Color(33, 150, 243));
        titledBorder.setBorder(BorderFactory.createLineBorder(new Color(33, 150, 243), 2));

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 10, 20),
                titledBorder
        ));

        Box boxFormInput = Box.createVerticalBox();

        Box box1 = Box.createHorizontalBox();
        jLableMaNV = new JLabel("Mã NV:") ;
        jLableMaNV.setPreferredSize(new Dimension(90, 25));
        txtMaNV = new JTextField(20);
        txtMaNV.setEditable(false);
        jLableTenNV = new JLabel("Tên nhân viên:") ;
        jLableTenNV.setPreferredSize(new Dimension(90, 25));
        txtTenNV = new JTextField(20);
        jLableNS = new JLabel("Ngày sinh:");
        jLableNS.setPreferredSize(new Dimension(90, 25));
        dateNgaySinh = new JDateChooser();
        dateNgaySinh.setDateFormatString("dd - MM - yyyy");
        dateNgaySinh.setPreferredSize(new Dimension(212, 25));

        box1.add(Box.createHorizontalStrut(50));
        box1.add(jLableMaNV);
        box1.add(txtMaNV);
        box1.add(Box.createHorizontalStrut(30));
        box1.add(jLableTenNV);
        box1.add(txtTenNV);
        box1.add(Box.createHorizontalStrut(30));
        box1.add(jLableNS);
        box1.add(dateNgaySinh);
        box1.add(Box.createHorizontalStrut(50));


        Box box2 = Box.createHorizontalBox();
        JLabel jLabelSDT = new JLabel("SĐT:");
        jLabelSDT.setPreferredSize(new Dimension(90, 25));
        txtSDT = new JTextField(20);
        JLabel jLabelDicChi = new JLabel("Địa chỉ:");
        jLabelDicChi.setPreferredSize(new Dimension(90, 25));
        txtDiaChi = new JTextField(20);
        JLabel jLabelSDD = new JLabel("Số định danh:");
        jLabelSDD.setPreferredSize(new Dimension(90, 25));
        txtSoDinhDanh = new JTextField(20);

        box2.add(Box.createHorizontalStrut(50));
        box2.add(jLabelSDT);
        box2.add(txtSDT);
        box2.add(Box.createHorizontalStrut(30));
        box2.add(jLabelDicChi);
        box2.add(txtDiaChi);
        box2.add(Box.createHorizontalStrut(30));
        box2.add(jLabelSDD);
        box2.add(txtSoDinhDanh);
        box2.add(Box.createHorizontalStrut(50));

        Box box3 = Box.createHorizontalBox();
        JLabel jLabelGioiTinh = new JLabel("Giới tính:");
        jLabelGioiTinh.setPreferredSize(new Dimension(90, 25));
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        cboGioiTinh.setPreferredSize(new Dimension(205, 25));
        JLabel jLabelChucvu = new JLabel("Chức vụ:");
        jLabelChucvu.setPreferredSize(new Dimension(90, 25));
        cboChucVu = new JComboBox<>(new String[]{"Người quản lý", "Nhân viên"});
        cboChucVu.setPreferredSize(new Dimension(205, 25));


        box3.add(Box.createHorizontalStrut(50));
        box3.add(jLabelGioiTinh);
        box3.add(cboGioiTinh);
        box3.add(Box.createHorizontalStrut(30));
        box3.add(jLabelChucvu);
        box3.add(cboChucVu);
        box3.add(Box.createHorizontalStrut(50));

        boxFormInput.add(Box.createVerticalStrut(10));
        boxFormInput.add(box1);
        boxFormInput.add(Box.createVerticalStrut(10));
        boxFormInput.add(box2);
        boxFormInput.add(Box.createVerticalStrut(10));
        boxFormInput.add(box3);
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

        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnThem.setIcon(new ImageIcon(getClass().getResource("/image/add.png")));
        btnXoa.setIcon(new ImageIcon(getClass().getResource("/image/delete.png")));
        btnSua.setIcon(new ImageIcon(getClass().getResource("/image/edit.png")));
        btnReset.setIcon(new ImageIcon(getClass().getResource("/image/clean.png")));

        addButtonListeners();

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 240, 240));

        Border border = BorderFactory.createLineBorder(new Color(33, 150, 243), 2);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(border,
                "Tra cứu nhân viên",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 16),
                new Color(33, 150, 243));

        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15),
                titledBorder
        ));

        JPanel searchByIDPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchByIDPanel.setBackground(new Color(240, 240, 240));

        JLabel lblMaNV = new JLabel("Mã NV:");
        lblMaNV.setFont(new Font("Arial", Font.PLAIN, 14));
        txtTimMaNV = new JTextField(10);
        txtTimMaNV.setFont(new Font("Arial", Font.PLAIN, 14));

        btnTim = new JButton("Tìm kiếm");
        btnTim.setFont(new Font("Arial", Font.BOLD, 14));
        btnTim.setBackground(new Color(33, 150, 243));
        btnTim.setForeground(Color.WHITE);
        btnTim.setFocusPainted(false);
        btnTim.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        btnTim.setIcon(new ImageIcon(getClass().getResource("/image/search.png")));

        searchByIDPanel.add(lblMaNV);
        searchByIDPanel.add(txtTimMaNV);
        searchByIDPanel.add(btnTim);

        // Panel lọc theo tên
        JPanel filterByNamePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
        filterByNamePanel.setBackground(new Color(240, 240, 240));

        JLabel lblTenNV = new JLabel("Tên NV: ");
        lblTenNV.setFont(new Font("Arial", Font.PLAIN, 15));
        txtTimTenNV = new JTextField(15);
        txtTimTenNV.setPreferredSize(new Dimension(205, 30));
        txtTimTenNV.setFont(new Font("Arial", Font.PLAIN, 15));
        jLableMaNV = new JLabel("Chức vụ: ");
        jLableMaNV.setFont(new Font("Arial", Font.PLAIN, 15));
        cboChucVuLoc = new JComboBox<>(new String[]{"Tất cả", "Người quản lý", "Nhân viên"});
        cboChucVuLoc.setFont(new Font("Arial", Font.PLAIN, 15));
        cboChucVuLoc.setPreferredSize(new Dimension(205, 30));

        filterByNamePanel.add(lblTenNV);
        filterByNamePanel.add(txtTimTenNV);
        filterByNamePanel.add(jLableMaNV);
        filterByNamePanel.add(cboChucVuLoc);

        panel.add(searchByIDPanel, BorderLayout.WEST);
        panel.add(filterByNamePanel, BorderLayout.EAST);

        searchByIDPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        filterByNamePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        btnTim.addActionListener(e -> searchEmployee());


        txtTimTenNV.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                timKiem();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                timKiem();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }

            private void timKiem() {
                String tenNV = txtTimTenNV.getText();
                if(!tenNV.isEmpty()) {
                    try {
                        List<NhanVien> nhanViens = nhanVienService.getAllNhanVien();
                        List<NhanVien> nhanVientheoten = nhanViens.stream()
                                .filter(nv -> nv.getTenNhanVien().contains(tenNV))
                                .collect(Collectors.toList());

                        hienThiKetQuaTimKiem(nhanVientheoten);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    resetTable();
                }
            }
        });

        cboChucVuLoc.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    String selectedChucVu = cboChucVuLoc.getSelectedItem().toString();

                    if (!selectedChucVu.equals("Tất cả")) {
                        try {
                            ChucVuNhanVien chucVuEnum;
                            if (selectedChucVu.equals("Người quản lý")) {
                                chucVuEnum = ChucVuNhanVien.NGUOIQUANLY;
                            } else if (selectedChucVu.equals("Nhân viên")) {
                                chucVuEnum = ChucVuNhanVien.NHANVIEN;
                            } else {
                                return;
                            }
                            List<NhanVien> allNhanViens = nhanVienService.getAllNhanVien();
                            List<NhanVien> filteredList = allNhanViens.stream()
                                    .filter(nv -> nv.getChucVuNhanVien() == chucVuEnum)
                                    .collect(Collectors.toList());

                            hienThiKetQuaTimKiem(filteredList);

                        } catch (RemoteException ex) {
                            ex.printStackTrace();
                            JOptionPane.showMessageDialog(null,
                                    "Lỗi kết nối: " + ex.getMessage(),
                                    "Lỗi", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        resetTable();
                    }
                }
            }
        });


        return panel;
    }

    private void hienThiKetQuaTimKiem(List<NhanVien> nhanViens) {
        tableModel.setRowCount(0);
        if (nhanViens != null && !nhanViens.isEmpty()) {
            for (NhanVien nhanVien : nhanViens) {
                tableModel.addRow(new Object[]{
                        nhanVien.getMaNhanVien(),
                        nhanVien.getTenNhanVien(),
                        nhanVien.getNgaySinh(),
                        nhanVien.getSoDienThoai(),
                        nhanVien.getDiaChi(),
                        nhanVien.getSoDinhDanh(),
                        nhanVien.getGioiTinh(),
                        nhanVien.getChucVuNhanVien()
                });
            }
        }
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel jPanelLamMoi = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        JButton lammoi = new JButton("Refresh");
        ComponentUtils.setButtonMain(lammoi);
        jPanelLamMoi.add(lammoi);
        lammoi.setIcon(new ImageIcon(getClass().getResource("/image/refresh.png")));

        String[] columns = {"Mã NV", "Tên NV", "Ngày sinh", "SĐT", "Địa chỉ", "Số định danh", "Giới tính", "Chức vụ"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép chỉnh sửa trực tiếp trên bảng
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        ComponentUtils.setTable(table);


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displaySelectedEmployee();
            }
        });

        panel.add(jPanelLamMoi, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        lammoi.addActionListener(e -> resetTable());


        return panel;
    }

    private void addButtonListeners() {
        btnThem.addActionListener(e -> addEmployee());
        btnXoa.addActionListener(e -> deleteEmployee());
        btnSua.addActionListener(e -> updateEmployee());
        btnReset.addActionListener(e -> resetForm());
    }

    private void resetTable(){
        try{
            tableModel.setRowCount(0);
            addSampleData();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    private void addEmployee() {

        try {
            if(validateInput()) {
                String maNV = "NV" + faker.number().digits(5);
                String tenNV = txtTenNV.getText();
                Date ngaySinhDate = dateNgaySinh.getDate();
                LocalDate localDateNgaySinh = ngaySinhDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();
                String sdt = txtSDT.getText();
                String diaChi = txtDiaChi.getText();
                String soDinhDanh = txtSoDinhDanh.getText();
                String textGioiTinh = cboGioiTinh.getSelectedItem().toString();
                String enumGioiTinh = textGioiTinh.equals("Nam") ? "NAM" : "NU";
                GioiTinh enumGT = GioiTinh.valueOf(enumGioiTinh);
                String textChucVu = cboChucVu.getSelectedItem().toString();
                String enumChucVu = textChucVu.equals("Người quản lý") ? "NGUOIQUANLY" : "NHANVIEN";
                ChucVuNhanVien chucVuNhanVien = ChucVuNhanVien.valueOf(enumChucVu);

                NhanVien nhanVien = new NhanVien(maNV, tenNV, localDateNgaySinh, sdt, diaChi, soDinhDanh, enumGT, chucVuNhanVien);

                if(nhanVienService.insertNhanVien(nhanVien)) {
                    resetTable();
                    resetForm();
                    JOptionPane.showMessageDialog(this, "Đã thêm nhân viên thành công", "Thành công", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Không thể thêm nhân viên mới", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
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

    private void deleteEmployee() {

        try {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String maNV = table.getValueAt(selectedRow, 0).toString();

                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Bạn có chắc chắn muốn xóa nhân viên này?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    if (nhanVienService.deleteNhanVien(maNV)) {
                        resetTable();
                        resetForm();
                        JOptionPane.showMessageDialog(
                                this,
                                "Xóa nhân viên thành công!",
                                "Thành công",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(
                                this,
                                "Không thể xóa nhân viên. Vui lòng thử lại!",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn nhân viên cần xóa!",
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

    private void updateEmployee() {
        try {
            int row = table.getSelectedRow();
            if (row >= 0) {
                if(validateInput()) {
                    String maNV = table.getValueAt(row, 0).toString();
                    String tenNV = txtTenNV.getText();
                    Date ngaySinhDate = dateNgaySinh.getDate();
                    LocalDate localDateNgaySinh = ngaySinhDate.toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                    String sdt = txtSDT.getText();
                    String diaChi = txtDiaChi.getText();
                    String soDinhDanh = txtSoDinhDanh.getText();
                    String textGioiTinh = cboGioiTinh.getSelectedItem().toString();
                    String enumGioiTinh = textGioiTinh.equals("Nam") ? "NAM" : "NU";
                    GioiTinh enumGT = GioiTinh.valueOf(enumGioiTinh);
                    String textChucVu = cboChucVu.getSelectedItem().toString();
                    String enumChucVu = textChucVu.equals("Người quản lý") ? "NGUOIQUANLY" : "NHANVIEN";
                    ChucVuNhanVien chucVuNhanVien = ChucVuNhanVien.valueOf(enumChucVu);

                    NhanVien nhanVien = new NhanVien(maNV, tenNV, localDateNgaySinh, sdt, diaChi, soDinhDanh, enumGT, chucVuNhanVien);

                    int confirm = JOptionPane.showConfirmDialog(
                            this,
                            "Bạn có chắc muốn cập nhật nhân viên" + tenNV,
                            "Xác nhận cập nhật",
                            JOptionPane.YES_NO_OPTION
                    );

                    if(confirm == JOptionPane.YES_OPTION) {
                        if(nhanVienService.updateNhanVien(nhanVien)) {
                            resetTable();
                            resetForm();
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Cập nhật nhân viên "+tenNV+" thành công",
                                    "Thành công",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Không thể cập nhật nhân viên" + tenNV,
                                    "Lỗi",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        }
                    }
                }
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Vui lòng chọn nhân viên cần cập nhật",
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
        txtMaNV.setText("");
        txtTenNV.setText("");
        dateNgaySinh.setDate(null);
        txtSDT.setText("");
        txtDiaChi.setText("");
        txtSoDinhDanh.setText("");
        cboGioiTinh.setSelectedIndex(0);
        cboChucVu.setSelectedIndex(0);
        table.clearSelection();
    }

    private void searchEmployee() {
        String maNV = txtTimMaNV.getText().trim().toLowerCase();

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String currentMaNV = tableModel.getValueAt(i, 0).toString().toLowerCase();

            boolean match = true;

            if (!maNV.isEmpty() && !currentMaNV.contains(maNV)) {
                match = false;
            }

            if (match) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                displaySelectedEmployee();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên phù hợp!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    private void displaySelectedEmployee() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {

            txtMaNV.setText(tableModel.getValueAt(selectedRow, 0).toString());
            txtTenNV.setText(tableModel.getValueAt(selectedRow, 1).toString());

            String strDate = tableModel.getValueAt(selectedRow, 2).toString();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date ngaySinh = dateFormat.parse(strDate);
                dateNgaySinh.setDate(ngaySinh);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            txtSDT.setText(tableModel.getValueAt(selectedRow, 3).toString());
            txtDiaChi.setText(tableModel.getValueAt(selectedRow, 4).toString());
            txtSoDinhDanh.setText(tableModel.getValueAt(selectedRow, 5).toString());

            // Set giới tính
            String gioiTinh = tableModel.getValueAt(selectedRow, 6).toString();
            for (int i = 0; i < cboGioiTinh.getItemCount(); i++) {
                if (cboGioiTinh.getItemAt(i).equals(gioiTinh)) {
                    cboGioiTinh.setSelectedIndex(i);
                    break;
                }
            }

            // Set chức vụ
            String chucVu = tableModel.getValueAt(selectedRow, 7).toString();
            for (int i = 0; i < cboChucVu.getItemCount(); i++) {
                if (cboChucVu.getItemAt(i).equals(chucVu)) {
                    cboChucVu.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private boolean isOver18(Date birthDate) {
        if (birthDate == null) {
            return false;
        }

        Calendar today = Calendar.getInstance();
        Calendar birthDay = Calendar.getInstance();
        birthDay.setTime(birthDate);

        // Tính tuổi
        int age = today.get(Calendar.YEAR) - birthDay.get(Calendar.YEAR);

        // Nếu chưa đến sinh nhật năm nay thì trừ đi 1 tuổi
        if (today.get(Calendar.DAY_OF_YEAR) < birthDay.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age >= 18;
    }

    private boolean validateInput() throws RemoteException{
        if (txtTenNV.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtTenNV.requestFocus();
            return false;
        }

        if (txtSDT.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            return false;
        }

        Date ngaySinh = dateNgaySinh.getDate();
        if (ngaySinh == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày sinh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            dateNgaySinh.requestFocus();
            return false;
        }

        // Kiểm tra tuổi >= 18
        if (!isOver18(ngaySinh)) {
            JOptionPane.showMessageDialog(this, "Nhân viên phải từ 18 tuổi trở lên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            dateNgaySinh.requestFocus();
            return false;
        }

        if (!txtSDT.getText().matches("^(03|04|05|07|08|09)\\d{8}$")) {
            JOptionPane.showMessageDialog(this,
                    "Số điện thoại phải có 10 chữ số và bắt đầu bằng 03, 04, 05, 07, 08 hoặc 09",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSDT.requestFocus();
            txtSDT.selectAll();
            return false;
        }

        List<NhanVien> dsNhanViens = RmiServiceLocator.getNhanVienService().getAllNhanVien();
        for (NhanVien nhanVien : dsNhanViens) {
            if(nhanVien.getSoDienThoai().equalsIgnoreCase(txtSDT.getText().trim())) {
                JOptionPane.showMessageDialog(this,
                        "Số điện thoại đã tồn tại.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSDT.requestFocus();
                txtSDT.selectAll();
                return false;
            }
        }

        String soDinhDanh = txtSoDinhDanh.getText().trim();
        if (soDinhDanh.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số định danh!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoDinhDanh.requestFocus();
            txtSoDinhDanh.selectAll();
            return false;
        }

        for (NhanVien nhanVien : dsNhanViens) {
            if(nhanVien.getSoDinhDanh().equalsIgnoreCase(soDinhDanh)) {
                JOptionPane.showMessageDialog(this,
                        "Số định danh đã tồn tại. Vui lòng thử lại.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                txtSoDinhDanh.requestFocus();
                txtSoDinhDanh.selectAll();
                return false;
            }
        }

        // Kiểm tra số định danh phải là 12 chữ số
        if (!soDinhDanh.matches("\\d{12}")) {
            JOptionPane.showMessageDialog(this,
                    "Số định danh phải gồm 12 chữ số!",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            txtSoDinhDanh.requestFocus();
            txtSoDinhDanh.selectAll();
            return false;
        }

        return true;
    }

    private void addSampleData() throws RemoteException{

        List<NhanVien> dsNhanVien = nhanVienService.getAllNhanVien();


//        String[] columns = {"Mã NV", "Tên NV", "Ngày sinh", "SĐT", "Địa chỉ", "Số định danh", "Giới tính", "Chức vụ"};
        for (NhanVien nhanVien : dsNhanVien) {
            tableModel.addRow(new Object[] {
                    nhanVien.getMaNhanVien(),
                    nhanVien.getTenNhanVien(),
                    nhanVien.getNgaySinh(),
                    nhanVien.getSoDienThoai(),
                    nhanVien.getDiaChi(),
                    nhanVien.getSoDinhDanh(),
                    nhanVien.getGioiTinh(),
                    nhanVien.getChucVuNhanVien()
            });
        }
    }
}