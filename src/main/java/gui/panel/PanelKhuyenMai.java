package gui.panel;

import gui.components.ComponentUtils;
import io.github.cdimascio.dotenv.Dotenv;
import model.KhuyenMai;
import model.SanPham;
import net.datafaker.Faker;
import service.KhuyenMaiService;
import service.SanPhamService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.List;

import static gui.panel.RmiServiceLocator.khuyenMaiService;
import static gui.panel.RmiServiceLocator.sanPhamService;

public class PanelKhuyenMai extends JPanel {
    private JTextField txtMaSP, txtTenKM, txtTienGiam, txtNgayBatDau, txtNgayKetThuc, txtTimMaKM;
    private JButton btnThem, btnXoa, btnSua, btnReset, btnTim, btnTimSP;
    private JTable table;
    private DefaultTableModel tableModel;
    private final Faker faker = new Faker();


    public PanelKhuyenMai() throws NamingException, RemoteException {
        if(khuyenMaiService.getDanhSachKhuyenMaiHetHan() != null){
            khuyenMaiService.getDanhSachKhuyenMaiHetHan().forEach(khuyenMai -> {
                try {
                    khuyenMaiService.deleteKhuyenMai(khuyenMai.getMaKhuyenMai());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(createInfoPanel(), BorderLayout.NORTH);


        JPanel container = new JPanel(new BorderLayout());
        add(container, BorderLayout.CENTER);

        container.add(createSearchPanel(), BorderLayout.NORTH);
        container.add(createTablePanel(), BorderLayout.CENTER);

        loadDanhSachKhuyenMai();
    }

    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 10, 20),
                ComponentUtils.getTitleBorder("Thông tin khuyến mãi")
        ));

        Box boxForm = Box.createVerticalBox();

        Box box1 = Box.createHorizontalBox();
        box1.add(Box.createHorizontalStrut(50));
        JLabel lblMaSP = new JLabel("Mã SP:");
        lblMaSP.setPreferredSize(new Dimension(90, 25));
        box1.add(lblMaSP);
        txtMaSP = new JTextField(20);
        box1.add(txtMaSP);
        box1.add(Box.createHorizontalStrut(30));
        JLabel lblTenKM = new JLabel("Tên KM:");
        lblTenKM.setPreferredSize(new Dimension(90, 25));
        box1.add(lblTenKM);
        txtTenKM = new JTextField(20);
        box1.add(txtTenKM);
        box1.add(Box.createHorizontalStrut(50));

        Box box1_2 = Box.createHorizontalBox();
        box1_2.add(Box.createHorizontalStrut(50));
        JLabel lblTienGiam = new JLabel("Tiền giảm:");
        lblTienGiam.setPreferredSize(new Dimension(90, 25));
        box1_2.add(lblTienGiam);
        txtTienGiam = new JTextField(20);
        box1_2.add(txtTienGiam);
        box1_2.add(Box.createHorizontalStrut(50));

        Box box2 = Box.createHorizontalBox();
        box2.add(Box.createHorizontalStrut(50));
        JLabel lblNgayBatDau = new JLabel("Ngày bắt đầu:");
        lblNgayBatDau.setPreferredSize(new Dimension(90, 25));
        box2.add(lblNgayBatDau);
        txtNgayBatDau = new JTextField(20);
        box2.add(txtNgayBatDau);
        box2.add(Box.createHorizontalStrut(30));
        JLabel lblNgayKetThuc = new JLabel("Ngày kết thúc:");
        lblNgayKetThuc.setPreferredSize(new Dimension(90, 25));
        box2.add(lblNgayKetThuc);
        txtNgayKetThuc = new JTextField(20);
        box2.add(txtNgayKetThuc);
        box2.add(Box.createHorizontalStrut(50));

        boxForm.add(Box.createVerticalStrut(10));
        boxForm.add(box1);
        boxForm.add(Box.createVerticalStrut(10));
        boxForm.add(box1_2);
        boxForm.add(Box.createVerticalStrut(10));
        boxForm.add(box2);
        boxForm.add(Box.createVerticalStrut(10));

        panel.add(boxForm, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnThem = new JButton("Thêm", new ImageIcon(getClass().getResource("/image/add.png")));
        btnXoa = new JButton("Xóa", new ImageIcon(getClass().getResource("/image/delete.png")));
        btnSua = new JButton("Sửa", new ImageIcon(getClass().getResource("/image/edit.png")));
        btnReset = new JButton("Reset", new ImageIcon(getClass().getResource("/image/clean.png")));

        ComponentUtils.setButton(btnThem, new Color(33, 150, 243));
        ComponentUtils.setButton(btnXoa, new Color(244, 67, 54));
        ComponentUtils.setButton(btnSua, new Color(255, 193, 7));
        ComponentUtils.setButton(btnReset, new Color(76, 175, 80));

        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnReset);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        btnThem.addActionListener(e -> themKhuyenMai());
        btnXoa.addActionListener(e -> xoaKhuyenMai());
        btnSua.addActionListener(e -> suaKhuyenMai());
        btnReset.addActionListener(e -> resetForm());

        return panel;
    }
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBorder(ComponentUtils.getTitleBorder("Tra cứu thông tin khuyến mãi"));

        JLabel lblMaKM = new JLabel("Mã KM:");
        txtTimMaKM = new JTextField(10);
        btnTim = new JButton("Tìm", new ImageIcon(getClass().getResource("/image/search.png")));
        ComponentUtils.setButtonMain(btnTim);

        btnTim.addActionListener(e -> timKhuyenMai());

        panel.add(lblMaKM);
        panel.add(txtTimMaKM);
        panel.add(btnTim);

        return panel;
    }
    private void timKhuyenMai() {
        try {
            String ma = txtTimMaKM.getText().trim();
            if (ma.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khuyến mãi để tìm.");
                return;
            }
            int rowCount = tableModel.getRowCount();
            boolean found = false;
            for (int i = 0; i < rowCount; i++) {
                String maKM = tableModel.getValueAt(i, 0).toString();
                if (ma.equalsIgnoreCase(maKM)) {
                    table.setRowSelectionInterval(i, i);
                    txtTenKM.setText(tableModel.getValueAt(i, 1).toString());
                    txtTienGiam.setText(tableModel.getValueAt(i, 2).toString());
                    txtNgayBatDau.setText(tableModel.getValueAt(i, 3).toString());
                    txtNgayKetThuc.setText(tableModel.getValueAt(i, 4).toString());
                    txtMaSP.setText(tableModel.getValueAt(i, 5).toString().split("_")[0]);
                    table.scrollRectToVisible(table.getCellRect(i, 0, true));
                    found = true;
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy khuyến mãi với mã: " + ma);
                table.clearSelection();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm và bôi đen khuyến mãi: " + e.getMessage());
        }
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnRefresh = new JButton("Refresh", new ImageIcon(getClass().getResource("/image/refresh.png")));
        ComponentUtils.setButtonMain(btnRefresh);
        btnRefresh.addActionListener(e -> loadDanhSachKhuyenMai());
        top.add(btnRefresh);
        panel.add(top, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"Mã KM", "Tên KM","Tiền giảm", "Ngày bắt đầu", "Ngày kết thúc", "Mã SP"}, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        ComponentUtils.setTable(table);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtTimMaKM.setText(tableModel.getValueAt(row, 0).toString());
                    txtTenKM.setText(tableModel.getValueAt(row, 1).toString());
                    txtTienGiam.setText(tableModel.getValueAt(row, 2).toString());
                    txtNgayBatDau.setText(tableModel.getValueAt(row, 3).toString());
                    txtNgayKetThuc.setText(tableModel.getValueAt(row, 4).toString());
                    txtMaSP.setText(tableModel.getValueAt(row, 5).toString().split("_")[0]);
                }
            }
        });

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }
    private void resetForm() {
        txtMaSP.setText("");
        txtTenKM.setText("");
        txtTienGiam.setText("");
        txtNgayBatDau.setText("");
        txtNgayKetThuc.setText("");
        txtTimMaKM.setText("");
    }

    private boolean validateKhuyenMaiForm() {
        if (txtMaSP.getText().trim().isEmpty() ||
                txtTenKM.getText().trim().isEmpty() ||
                txtTienGiam.getText().trim().isEmpty() ||
                txtNgayBatDau.getText().trim().isEmpty() ||
                txtNgayKetThuc.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
            return false;
        }

        try {
            Double value = Double.parseDouble(txtTienGiam.getText());
            if (value < 0 || value > 1) {
                JOptionPane.showMessageDialog(this, "Tiền giảm phải lớn hơn 0 và bé hơn 1");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tiền giảm phải là số hợp lệ.");
            return false;
        }

        try {
            LocalDate start = LocalDate.parse(txtNgayBatDau.getText());
            LocalDate end = LocalDate.parse(txtNgayKetThuc.getText());
            if (end.isBefore(start)) {
                JOptionPane.showMessageDialog(this, "Ngày kết thúc phải sau ngày bắt đầu.");
                return false;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày không hợp lệ. Định dạng đúng: yyyy-MM-dd");
            return false;
        }

        return true;
    }

    private void themKhuyenMai() {
        try {
            if (!validateKhuyenMaiForm()) return;

            SanPham sp = sanPhamService.findOne(txtMaSP.getText());
            String maKM = "KM" + faker.number().digits(5);
            if(sp==null){
                JOptionPane.showMessageDialog(this, "Mã sản phẩm không tồn tại.");
                return;
            }

            KhuyenMai khuyenMai = khuyenMaiService.getKhuyenMaiBySanPhamId(txtMaSP.getText());
            if(khuyenMai != null){
                if(khuyenMai.getNgayKetThuc().isAfter(LocalDate.now())){
                    JOptionPane.showMessageDialog(
                            this,
                            "Khuyến mãi của sản phẩm này chưa kết thúc !!",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            }

            KhuyenMai km = new KhuyenMai(
                    maKM,
                    txtTenKM.getText(),
                    Double.parseDouble(txtTienGiam.getText()),
                    LocalDate.parse(txtNgayBatDau.getText()),
                    LocalDate.parse(txtNgayKetThuc.getText()),
                    sp
            );
            if(khuyenMaiService.insertKhuyenMai(km)){
                JOptionPane.showMessageDialog(
                        this,
                        "Thêm sản phẩm khuyến mãi thành công",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE);
                loadDanhSachKhuyenMai();
                resetForm();
            } else {
                JOptionPane.showMessageDialog(this,
                        "Lỗi khi thêm khuyến mãi: ",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE);
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm khuyến mãi: " + e.getMessage());
        }
    }

    private void suaKhuyenMai() {
        try {
            if (!validateKhuyenMaiForm()) return;

            SanPham sp = sanPhamService.findOne(txtMaSP.getText());
            KhuyenMai km = new KhuyenMai(
                    txtTimMaKM.getText(),
                    txtTenKM.getText(),
                    Double.parseDouble(txtTienGiam.getText()),
                    LocalDate.parse(txtNgayBatDau.getText()),
                    LocalDate.parse(txtNgayKetThuc.getText()),
                    sp
            );
            if(khuyenMaiService.updateKhuyenMai(km)){
                loadDanhSachKhuyenMai();
                resetForm();
                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật khuyến mãi thành công",
                        "Thành công",
                        JOptionPane.INFORMATION_MESSAGE
                );
            } else {
                JOptionPane.showMessageDialog(
                        this,
                        "Cập nhật khuyến mãi thất bại",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa khuyến mãi: " + e.getMessage());
        }
    }

    private void loadDanhSachKhuyenMai() {
        try {
            if (tableModel == null) {
                tableModel = new DefaultTableModel(new String[]{"Mã KM", "Tên KM", "Tiền giảm", "Ngày bắt đầu", "Ngày kết thúc", "Mã SP"}, 0);
                table.setModel(tableModel);
            } else {
                tableModel.setRowCount(0);
            }
            List<KhuyenMai> ds = khuyenMaiService.getAllKhuyenMai();
            for (KhuyenMai km : ds) {
                tableModel.addRow(new Object[]{
                        km.getMaKhuyenMai(),
                        km.getTenKhuyenMai(),
                        km.getTienGiam(),
                        km.getNgayBatDau(),
                        km.getNgayKetThuc(),
                        km.getSanPham().getMaSanPham() + "_" + km.getSanPham().getTenSanPham()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải danh sách khuyến mãi: " + e.getMessage());
        }
    }

    private void xoaKhuyenMai() {
        try {
            String ma = txtTimMaKM.getText().trim();
            if (!ma.isEmpty()) {
                if(khuyenMaiService.deleteKhuyenMai(ma)){
                    JOptionPane.showMessageDialog(
                            this,
                            "Xóa khuyến mãi thành công",
                            "Thành công",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    loadDanhSachKhuyenMai();
                    resetForm();
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Xóa khuyến mãi thất bại",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }else{
                JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi để xóa.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi xoá khuyến mãi: " + e.getMessage());
        }
    }


}
