package gui.panel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PanelKhachHang extends JPanel {
    private JTextField txtMaNV, txtTenNV, txtNgaySinh, txtSDT, txtDiaChi, txtSoDinhDanh, txtTimMaNV, txtTimTenNV;
    private JComboBox<String> cboGioiTinh, cboChucVu;
    private JButton btnThem, btnXoa, btnSua, btnReset, btnTim;
    private JTable table;
    private DefaultTableModel tableModel;
    private JLabel jLableMaNV;
    private JLabel jLableTenNV;
    private JDateChooser dateNgaySinh;
    private JComboBox<String> cboChucVuLoc;
    private JLabel jLableMaKH;
    private JTextField txtMaKH;
    private JTextField txtTenKH;
    private JLabel jLabelSDT;
    private JTextField txtDiemTichluy;
    private JTextField txtTimMaKH;
    private JTextField txtTimTenKH;
    private JLabel lblMaNV;
    private JTextField txtTimSDT;

    public PanelKhachHang() {
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

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        Border border = BorderFactory.createLineBorder(new Color(33, 150, 243), 2);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(border,
                "Thông tin khách hàng",
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
        btnThem.setPreferredSize(new Dimension(100, 40));
        btnXoa = new JButton("Xóa");
        btnXoa.setPreferredSize(new Dimension(100, 40));
        btnSua = new JButton("Sửa");
        btnSua.setPreferredSize(new Dimension(100, 40));
        btnReset = new JButton("Reset");
        btnReset.setPreferredSize(new Dimension(100, 40));


        btnThem.setBackground(new Color(33, 150, 243));
        btnThem.setForeground(Color.WHITE);
        btnXoa.setBackground(new Color(244, 67, 54));
        btnXoa.setForeground(Color.WHITE);
        btnSua.setBackground(new Color(255, 193, 7));
        btnSua.setForeground(Color.WHITE);
        btnReset.setBackground(new Color(76, 175, 80));
        btnReset.setForeground(Color.WHITE);

        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnReset);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        addButtonListeners();

        return panel;
    }

    private JPanel createSearchPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 240, 240));

        Border border = BorderFactory.createLineBorder(new Color(33, 150, 243), 2);
        TitledBorder titledBorder = BorderFactory.createTitledBorder(border,
                "Tra cứu thông tin khách hàng",
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

        btnTim.addActionListener(e -> searchEmployee());


        txtTimTenKH.getDocument().addDocumentListener(new DocumentListener() {
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
//				getListKHByPhone(txtTimKiem.getText().trim());

            }
        });

        txtTimSDT.getDocument().addDocumentListener(new DocumentListener() {
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
//				getListKHByPhone(txtTimKiem.getText().trim());

            }
        });


        return panel;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        String[] columns = {"Mã khách hàng", "Tên khách hàng", "Số điện thoại", "Giới tính", "Điểm tích lũy"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
        table.setFocusable(false);
        table.setDefaultEditor(Object.class, null);
        table.setAutoCreateRowSorter(true);
        table.getTableHeader().setFont(new Font("Arial", Font.PLAIN, 20));
        table.getTableHeader().setBackground(new Color(33, 150, 243));
        table.setRowHeight(40);
        table.setShowHorizontalLines(true);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(33, 150, 243));
        table.setSelectionForeground(Color.WHITE);
        table.setGridColor(Color.LIGHT_GRAY);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 40));


        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displaySelectedCustomer();
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private void addButtonListeners() {
        btnThem.addActionListener(e -> addCustomer());
        btnXoa.addActionListener(e -> deleteCustomer());
        btnSua.addActionListener(e -> updateCustomer());
        btnReset.addActionListener(e -> resetForm());
    }

    private void addCustomer() {
        if (validateInput()) {
            String[] rowData = {
                    generateEmployeeId(),
                    txtTenKH.getText(),
                    txtSDT.getText(),
                    cboGioiTinh.getSelectedItem().toString(),
                    txtDiemTichluy.getText()
            };
            tableModel.addRow(rowData);
            resetForm();
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
        }
    }

    private void deleteCustomer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
            resetForm();
            JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa cần xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomer() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            if (validateInput()) {
                tableModel.setValueAt(txtTenKH.getText(), selectedRow, 1);
                tableModel.setValueAt(txtSDT.getText(), selectedRow, 2);
                tableModel.setValueAt(cboGioiTinh.getSelectedItem().toString(), selectedRow, 3);
                tableModel.setValueAt(txtDiemTichluy.getText(), selectedRow, 4);

                JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
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
                displaySelectedCustomer();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên phù hợp!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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

    private String generateEmployeeId() {
        return "NV" + (tableModel.getRowCount() + 1);
    }

    private void addSampleData() {
        String[][] sampleData = {
                {"NV1", "Nguyễn Văn A", "15/05/1990", "0987654321", "Hà Nội", "123456789012", "Nam", "Nhân viên"},
                {"NV2", "Trần Thị B", "20/10/1985", "0912345678", "Hồ Chí Minh", "987654321098", "Nữ", "Quản lý"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"},
                {"NV3", "Lê Văn C", "03/03/1995", "0967890123", "Đà Nẵng", "456789012345", "Nam", "Nhân viên"}
        };

        for (String[] row : sampleData) {
            tableModel.addRow(row);
        }
    }
}