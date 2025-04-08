package gui.panel;

import InterF.DanhMucSanPhamDAOInterface;
import InterF.SanPhamDAOInterface;
import InterF.ThuocTinhSanPhamDAOInterface;
import com.toedter.calendar.JDateChooser;
import dao.DanhMucSanPhamDAO;
import model.DanhMucSanPham;
import model.SanPham;
import model.ThuocTinhSanPham;
import net.datafaker.Faker;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

public class PanelSanPham extends JPanel implements MouseListener, ActionListener {
    private JLabel jLabelMaSP, jLabelTenSP, jLabelLoaiSP, jLabelGiaSP,
            jLabelHanSuDung, jLableThueVAT, jLabelSoLuong, jLabelNgayNhap, jLabelTrangThai, jLabelMoTa, jLabelThuocTinh;
    private JComboBox JcomboboxLoaiSP, JcomboboxTrangThai;
    private JTextField txtMaSP, txtTenSP, txtGiaSP, txtThueVAT, txtSoLuong, txtThuocTinh, txtMoTa;

    private JTable jTableContent;

    private final Faker faker = new Faker();

    private DefaultTableModel dataModel;

    private JDateChooser dateNgayNhap, dateHanSuDung;

    private JButton btnThem, btnXoa, btnSua, btnReset;

    private Context context = new InitialContext();
    private SanPhamDAOInterface sanPhamDAO = (SanPhamDAOInterface)context.lookup("rmi://LAPTOP-MB2815MQ:9090/sanPhamDAO");
    private ThuocTinhSanPhamDAOInterface thuocTinhSanPhamDAO = (ThuocTinhSanPhamDAOInterface)context.lookup("rmi://LAPTOP-MB2815MQ:9090/thuocTinhSanPhamDAO");
    private DanhMucSanPhamDAOInterface danhMucSanPhamDAO = (DanhMucSanPhamDAOInterface) context.lookup("rmi://LAPTOP-MB2815MQ:9090/danhMucSanPhamDAO");

    public PanelSanPham () throws NamingException, RemoteException {
        setLayout(new BorderLayout());

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tạo tiêu đề căn giữa
        JLabel titleLabel = new JLabel("Giao diện quản lý sản phẩm", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // tạo khoảng cách

        add(titleLabel, BorderLayout.NORTH);

        // Tên cột cho bảng
        String[] columnNames = {"Mã SP", "Tên sản phẩm", "Loại sản phẩm", "Giá bán", "Số lượng", "Thuế VAT", "Ngày nhập", "Thuộc tính", "Mô tả", "Hạn sử dụng", "Trạng thái"};

        // Tạo model cho bảng với data
        dataModel = new DefaultTableModel(columnNames, 0);


        jTableContent = new JTable(dataModel);

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

        // Panel nhập liệu và nút chức năng
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Thông tin nhập"),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        Font labelFont = new Font("Arial", Font.BOLD, 14); // Chữ lớn và đậm
        Font textFieldFont = new Font("Arial", Font.PLAIN, 14); // Chữ to cho input



        Box boxVerticalFormGroup = Box.createVerticalBox();

        // Row 1
        Box row1 = Box.createHorizontalBox();
        row1.add(jLabelMaSP = new JLabel("Mã SP:"));
        jLabelMaSP.setPreferredSize(new Dimension(90, 25));
        row1.add(Box.createHorizontalStrut(10));
        row1.add(txtMaSP = new JTextField(20));
        txtMaSP.setEditable(false);

        row1.add(Box.createHorizontalStrut(30));
        row1.add(jLabelTenSP = new JLabel("Tên SP:"));
        jLabelTenSP.setPreferredSize(new Dimension(90, 25));
        row1.add(Box.createHorizontalStrut(10));
        row1.add(txtTenSP = new JTextField(20));

        row1.add(Box.createHorizontalStrut(30));
        row1.add(jLabelHanSuDung = new JLabel("Hạn SD:"));
        jLabelHanSuDung.setPreferredSize(new Dimension(90, 25));
        row1.add(Box.createHorizontalStrut(10));
        row1.add(dateHanSuDung = new JDateChooser());

        dateHanSuDung.setDateFormatString("dd/MM/yyyy");

        dateHanSuDung.setPreferredSize(new Dimension(225, 20));




//        this.ngayNhap = ngayNhap; (Generate)

        // Row 2
        Box row2 = Box.createHorizontalBox();
        row2.add(jLabelSoLuong = new JLabel("Số lượng:"));
        jLabelSoLuong.setPreferredSize(new Dimension(90, 25));
        row2.add(Box.createHorizontalStrut(10));
        row2.add(txtSoLuong = new JTextField(20));

        row2.add(Box.createHorizontalStrut(30));
        row2.add(jLabelGiaSP = new JLabel("Giá: "));
        jLabelGiaSP.setPreferredSize(new Dimension(90, 25));
        row2.add(Box.createHorizontalStrut(10));
        row2.add(txtGiaSP = new JTextField(20));

        row2.add(Box.createHorizontalStrut(30));
        row2.add(jLableThueVAT = new JLabel("Thuế VAT: "));
        jLableThueVAT.setPreferredSize(new Dimension(90, 25));
        row2.add(Box.createHorizontalStrut(10));
        row2.add(txtThueVAT = new JTextField(20));

        // Row 3
        Box row3 = Box.createHorizontalBox();
        row3.add(jLabelNgayNhap = new JLabel("Ngày nhập:"));
        jLabelNgayNhap.setPreferredSize(new Dimension(90, 25));
        row3.add(Box.createHorizontalStrut(10));
        row3.add(dateNgayNhap = new JDateChooser());
        dateNgayNhap.setDateFormatString("dd/MM/yyyy");
        dateNgayNhap.setPreferredSize(new Dimension(225, 20));

        row3.add(Box.createHorizontalStrut(30));
        row3.add(jLabelMoTa = new JLabel("Mô tả: "));
        jLabelMoTa.setPreferredSize(new Dimension(90, 25));
        row3.add(Box.createHorizontalStrut(10));
        row3.add(txtMoTa = new JTextField(20));

        row3.add(Box.createHorizontalStrut(30));
        row3.add(jLabelThuocTinh = new JLabel("Thuộc tính: "));
        jLabelThuocTinh.setPreferredSize(new Dimension(90, 25));
        row3.add(Box.createHorizontalStrut(10));
        row3.add(txtThuocTinh = new JTextField(20));

        // Row 4
        Box row4 = Box.createHorizontalBox();
        row4.add(jLabelTrangThai = new JLabel("Trạng thái:"));
        jLabelTrangThai.setPreferredSize(new Dimension(90, 25));
        row4.add(Box.createHorizontalStrut(10));
        row4.add(JcomboboxTrangThai = new JComboBox<String>());
//        JcomboboxTrangThai.setEditable(false);

        JcomboboxTrangThai.addItem("Còn bán");
        JcomboboxTrangThai.addItem("Ngưng bán");

        row4.add(Box.createHorizontalStrut(30));
        row4.add(jLabelLoaiSP = new JLabel("Loại SP: "));
        jLabelLoaiSP.setPreferredSize(new Dimension(90, 25));
        row4.add(Box.createHorizontalStrut(10));
        row4.add(JcomboboxLoaiSP = new JComboBox<String>());
//        JcomboboxLoaiSP.setEditable(false);

        danhMucSanPhamDAO.getList().forEach(danhMucSanPham -> {
            JcomboboxLoaiSP.addItem(danhMucSanPham.getMaDanhMucSanPham() + "_" + danhMucSanPham.getTenDanhMucSanPham());
        });



        jLabelMaSP.setFont(labelFont);
        jLabelTenSP.setFont(labelFont);
        jLabelLoaiSP.setFont(labelFont);
        jLabelGiaSP.setFont(labelFont);
        jLabelHanSuDung.setFont(labelFont);
        jLableThueVAT.setFont(labelFont);
        jLabelSoLuong.setFont(labelFont);
        jLabelNgayNhap.setFont(labelFont);
        jLabelTrangThai.setFont(labelFont);
        jLabelMoTa.setFont(labelFont);
        jLabelThuocTinh.setFont(labelFont);

        txtMaSP.setFont(textFieldFont);
        txtTenSP.setFont(textFieldFont);
        txtGiaSP.setFont(textFieldFont);

        txtThueVAT.setFont(textFieldFont);
        txtSoLuong.setFont(textFieldFont);

        txtThuocTinh.setFont(textFieldFont);
        txtMoTa.setFont(textFieldFont);


//      Thêm các row
        boxVerticalFormGroup.add(row1);
        boxVerticalFormGroup.add(Box.createVerticalStrut(10));
        boxVerticalFormGroup.add(row2);
        boxVerticalFormGroup.add(Box.createVerticalStrut(10));
        boxVerticalFormGroup.add(row3);
        boxVerticalFormGroup.add(Box.createVerticalStrut(10));
        boxVerticalFormGroup.add(row4);
        boxVerticalFormGroup.add(Box.createVerticalStrut(10));


//        End table
        inputPanel.add(boxVerticalFormGroup);





        bottomPanel.add(inputPanel, BorderLayout.CENTER);




        // Panel chứa các nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnThem = new JButton("Thêm");
        btnXoa = new JButton("Xóa");
        btnSua = new JButton("Sửa");
        btnReset = new JButton("Reset");

        buttonPanel.add(btnThem);
        buttonPanel.add(btnXoa);
        buttonPanel.add(btnSua);
        buttonPanel.add(btnReset);

        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        BoxHozi.add(bottomPanel);



//        Action
        jTableContent.addMouseListener(this);
        btnReset.addActionListener(this);
        btnXoa.addActionListener(this);
        btnThem.addActionListener(this);
//        End action

    }

    // Hàm tạo panel chứa label và textfield để dùng cho GridLayout
    private JPanel createLabeledField(String labelText, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel(labelText);
        panel.add(label, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        return panel;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() != null) {
            Object source = e.getSource();
            if(source.equals(btnReset)) {
                txtMaSP.setText("");
                txtTenSP.setText("");
                txtGiaSP.setText("");
                dateHanSuDung.setDate(null);
                txtThueVAT.setText("");
                txtSoLuong.setText("");
                dateNgayNhap.setDate(null);
                txtThuocTinh.setText("");
                txtMoTa.setText("");

                JcomboboxLoaiSP.setSelectedIndex(0);
                JcomboboxTrangThai.setSelectedIndex(0);
                txtTenSP.requestFocus();

            } else if (source.equals(btnXoa)) {
                String maSP = jTableContent.getValueAt(jTableContent.getSelectedRow(), 0).toString();
                int xoa = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa dòng này ?");
                if(xoa == 0) {
                    try {
                        sanPhamDAO.delete(maSP);
                        dataModel.removeRow(jTableContent.getSelectedRow());
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            } else if (source.equals(btnThem)) {

//                txtHanSuDung.setText("");
//                txtNgayNhap.setText("");

                String maSP = "SP" + faker.number().digits(5);
                String tenSP = txtTenSP.getText();
                double giaSP = Double.parseDouble(txtGiaSP.getText());
                double thueVAT = Double.parseDouble(txtThueVAT.getText());
                int soLuong = Integer.parseInt(txtSoLuong.getText());
                String moTa = txtMoTa.getText();

                String[] listThuocTinh = txtThuocTinh.getText().split(",");

                String trangThai = JcomboboxTrangThai.getSelectedItem().toString();
                String maDanhMuc = JcomboboxLoaiSP.getSelectedItem().toString().split("_")[0];


                java.util.Date dateNgaynhap = dateNgayNhap.getDate(); // Lấy ngày từ JDateChooser
                LocalDate localDateNgayNhap = dateNgaynhap.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();

                java.util.Date dateHSD =  dateHanSuDung.getDate(); // Lấy ngày từ JDateChooser
                LocalDate localDateHanSuDung = dateHSD.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate();


                SanPham sanPham = new SanPham(maSP, tenSP,
                        localDateHanSuDung, giaSP, thueVAT, trangThai, soLuong, localDateNgayNhap, moTa);

                try {
                    sanPham.setDanhMucSanPham(danhMucSanPhamDAO.findOne(maDanhMuc));
                    sanPhamDAO.save(sanPham);
                    dataModel.addRow(new Object[] {
                            sanPham.getMaSanPham(),
                            sanPham.getTenSanPham(),
                            sanPham.getDanhMucSanPham().getTenDanhMucSanPham(),
                            sanPham.getGiaBan(),
                            sanPham.getSoLuongTon(),
                            String.format("%.2f", sanPham.getThueVAT()),
                            sanPham.getNgayNhap().toString(),
                            "",
                            sanPham.getMoTa(),
                            sanPham.getHanSuDung().toString(),
                            sanPham.getTrangThai()
                    });
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }



//                dslp.updateLop(lp);
//                for(int i = 0; i < table.getRowCount(); i++) {
//                    if(table.getValueAt(i, 0).toString().equals(maLop)) {
//                        table.setValueAt(tenLop, i, 1);
//                        table.setValueAt(maGV, i, 2);
//                        table.setValueAt(siSo, i, 3);
//                    }
//                }

            } else if (source.equals(btnSua)) {
//                String maLop = txtMaLop.getText();
//                String tenLop = txtTenLop.getText();
//                String maGV = cboGVCN.getSelectedItem().toString();
//                int siSo = Integer.parseInt(txtSiSo.getText());
//                if(dslp.themLopHoc(new LopHoc(maLop, tenLop, new GiaoVien(maGV), siSo))) {
//                    dataModel.addRow(new Object[] {maLop, tenLop, maGV, siSo});
//                } else JOptionPane.showMessageDialog(null, "Trùng mã");

            }
//            else if (source.equals(btnTimLop)) {
////                String maLop = txtMaLop.getText();
////                LopHoc lp = dslp.timLopHoc(maLop);
////                if(lp != null) {
////                    for(int i = 0; i < table.getRowCount(); i++) {
////                        if(table.getValueAt(i, 0).toString().equals(lp.getMaLop())) {
////                            table.setRowSelectionInterval(i, i);
////                        }
////                    }
////                }
//            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() != null) {
            Object source = e.getSource();
            if(source.equals(jTableContent)) {
                String maSP = jTableContent.getValueAt(jTableContent.getSelectedRow(), 0).toString();
                try {
                    SanPham sanPham = sanPhamDAO.findOne(maSP);
                    txtMaSP.setText(sanPham.getMaSanPham());
                    txtTenSP.setText(sanPham.getTenSanPham());
                    txtGiaSP.setText(sanPham.getGiaBan() + "");
//                    txtHanSuDung.setText(sanPham.getHanSuDung().toString());
                    Date dateHSD = java.sql.Date.valueOf(sanPham.getHanSuDung());
                    dateHanSuDung.setDate(dateHSD);
                    txtThueVAT.setText(sanPham.getThueVAT() + "");
                    txtSoLuong.setText(sanPham.getSoLuongTon() + "");
//                    txtNgayNhap.setText(sanPham.getNgayNhap().toString());
                    Date dateNgaynhap = java.sql.Date.valueOf(sanPham.getNgayNhap());
                    dateNgayNhap.setDate(dateNgaynhap);
                    String thuocTinh =  thuocTinhSanPhamDAO.getListByProductId(sanPham.getMaSanPham()).stream().map(thuocTinhSanPham ->
                            thuocTinhSanPham.getTenThuocTinh() + ":" + thuocTinhSanPham.getGiaTriThuocTinh()
                    ).collect(Collectors.joining(", "));


                    txtThuocTinh.setText(thuocTinh);
                    txtMoTa.setText(sanPham.getMoTa());
                    JcomboboxTrangThai.setSelectedItem(sanPham.getTrangThai());
                    JcomboboxLoaiSP.setSelectedItem(sanPham.getDanhMucSanPham().getMaDanhMucSanPham() + "_" + sanPham.getDanhMucSanPham().getTenDanhMucSanPham());
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }

            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}


