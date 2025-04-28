package gui.panel;

import com.toedter.calendar.JDateChooser;
import gui.components.ComponentUtils;
import io.github.cdimascio.dotenv.Dotenv;
import model.SanPham;
import model.ThuocTinhSanPham;
import net.datafaker.Faker;
import service.ThuocTinhSanPhamService;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static gui.panel.RmiServiceLocator.*;

public class PanelSanPham extends JPanel implements MouseListener, ActionListener {
    private final JLabel jLabelMaSP, jLabelTenSP, jLabelLoaiSP, jLabelGiaSP,
            jLabelHanSuDung, jLableThueVAT, jLabelSoLuong, jLabelNgayNhap, jLabelTrangThai,
            jLabelMoTa, jLabelThuocTinh, labelFind, labelStatus, labelDanhMuc, labelNgayNhap;
    private final JComboBox<String> JcomboboxLoaiSP, JcomboboxTrangThai, jComboBoxStatus, jComboBoxDanhMuc, jComboBoxMonth, jComboBoxYear;
    private final JTextField txtMaSP, txtTenSP, txtGiaSP, txtThueVAT, txtSoLuong, txtThuocTinh, txtMoTa, txtFind;
    private final JTable jTableContent;

    private final Faker faker = new Faker();

    private final DefaultTableModel dataModel;

    private final JDateChooser dateNgayNhap, dateHanSuDung;

    private final JButton btnThem, btnXoa, btnSua, btnReset, btnResetTable, btnFind;

    public PanelSanPham () throws RemoteException {
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

        ComponentUtils.setTable(jTableContent);

        loadDataTable();

        // Tùy chỉnh độ cao hàng và font chữ nếu cần
        jTableContent.setRowHeight(30);
        jTableContent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTableContent.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Thêm bảng vào JScrollPane để có thể cuộn
        JScrollPane scrollPane = new JScrollPane(jTableContent);

        Box BoxHozi = Box.createVerticalBox();


        Box boxButton = Box.createHorizontalBox();
        boxButton.add(btnResetTable = new JButton("Refresh"));
        boxButton.add(Box.createHorizontalGlue()); // đẩy nút sang trái


        BoxHozi.add(boxButton);
        BoxHozi.add(Box.createVerticalStrut(20));
        BoxHozi.add(scrollPane);
        add(BoxHozi, BorderLayout.CENTER);

        Box boxGapHeight = Box.createHorizontalBox();
        boxGapHeight.add(Box.createVerticalStrut(20));
        BoxHozi.add(boxGapHeight);

        JPanel filterPanel = new JPanel();


        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 10, 20),
                ComponentUtils.getTitleBorder("Bộ lọc và tìm kiếm")
        ));

        Box boxHorizonFilter = Box.createHorizontalBox();

        Box boxSearch = Box.createHorizontalBox();
        labelFind = new JLabel("Nhập mã để tìm: ");
        txtFind = new JTextField(10);
        btnFind = new JButton("Tìm");

        labelDanhMuc = new JLabel("Danh mục: ");
        jComboBoxDanhMuc = new JComboBox<>();

        jComboBoxDanhMuc.addItem("Tất cả");

        danhMucSanPhamService.getList().forEach(danhMucSanPham -> {
            jComboBoxDanhMuc.addItem(danhMucSanPham.getMaDanhMucSanPham() + "_" + danhMucSanPham.getTenDanhMucSanPham());
        });

        labelStatus = new JLabel("Trạng thái: ");
        jComboBoxStatus = new JComboBox<>();
        jComboBoxStatus.addItem("Tất cả");
        jComboBoxStatus.addItem("Còn bán");
        jComboBoxStatus.addItem("Ngưng bán");


        labelNgayNhap = new JLabel("Ngày nhập:");
        jComboBoxMonth = new JComboBox<>();
        jComboBoxMonth.addItem("Tất cả");
        for (int i = 1; i <= 12; i++) {
            jComboBoxMonth.addItem(i+"");
        }
        jComboBoxYear = new JComboBox<>();
        jComboBoxYear.addItem("Tất cả");
        for (int i = 2000; i <= LocalDate.now().getYear(); i++) {
            jComboBoxYear.addItem(i+"");
        }


        boxSearch.add(labelFind);
        boxSearch.add(Box.createHorizontalStrut(10));
        boxSearch.add(txtFind);
        boxSearch.add(Box.createHorizontalStrut(10));
        boxSearch.add(btnFind);
        boxSearch.add(Box.createHorizontalStrut(10));
        boxSearch.add(labelDanhMuc);
        boxSearch.add(Box.createHorizontalStrut(10));
        boxSearch.add(jComboBoxDanhMuc);
        boxSearch.add(Box.createHorizontalStrut(10));
        boxSearch.add(labelStatus);
        boxSearch.add(Box.createHorizontalStrut(10));
        boxSearch.add(jComboBoxStatus);
        boxSearch.add(Box.createHorizontalStrut(10));
        boxSearch.add(labelNgayNhap);
        boxSearch.add(Box.createHorizontalStrut(10));
        boxSearch.add(jComboBoxMonth);
        boxSearch.add(Box.createHorizontalStrut(10));
        boxSearch.add(jComboBoxYear);


        boxHorizonFilter.add(boxSearch);




        filterPanel.add(boxHorizonFilter);
        BoxHozi.add(filterPanel);



        // Panel nhập liệu và nút chức năng
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel();

        inputPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(20, 20, 10, 20),
                ComponentUtils.getTitleBorder("Thông tin nhập")
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

        dateHanSuDung.setPreferredSize(new Dimension(235, 20));




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
        dateNgayNhap.setPreferredSize(new Dimension(235, 20));

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

        danhMucSanPhamService.getList().forEach(danhMucSanPham -> {
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
        btnSua.addActionListener(this);
        btnFind.addActionListener(this);
        btnResetTable.addActionListener(this);


        jComboBoxMonth.addActionListener(this);
        jComboBoxYear.addActionListener(this);
        jComboBoxStatus.addActionListener(this);
        jComboBoxDanhMuc.addActionListener(this);
//        End action


//        Custom GUI
        ComponentUtils.setButton(btnThem, new Color(33, 150, 243));
        ComponentUtils.setButton(btnXoa, new Color(244, 67, 54));
        ComponentUtils.setButton(btnSua, new Color(255, 193, 7));
        ComponentUtils.setButton(btnReset, new Color(76, 175, 80));

        ComponentUtils.setButtonMain(btnFind);
        ComponentUtils.setButtonMain(btnResetTable);

        ComponentUtils.setJcombobox(jComboBoxMonth);
        ComponentUtils.setJcombobox(jComboBoxDanhMuc);
        ComponentUtils.setJcombobox(jComboBoxYear);
        ComponentUtils.setJcombobox(jComboBoxStatus);
        ComponentUtils.setJcombobox(JcomboboxTrangThai);
        ComponentUtils.setJcombobox(JcomboboxLoaiSP);


        btnThem.setIcon(new ImageIcon(getClass().getResource("/image/add.png")));
        btnXoa.setIcon(new ImageIcon(getClass().getResource("/image/delete.png")));
        btnSua.setIcon(new ImageIcon(getClass().getResource("/image/edit.png")));
        btnReset.setIcon(new ImageIcon(getClass().getResource("/image/clean.png")));
        btnResetTable.setIcon(new ImageIcon(getClass().getResource("/image/refresh.png")));
        btnFind.setIcon(new ImageIcon(getClass().getResource("/image/search.png")));
//        End custom GUI

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

            }
            else if (source.equals(btnXoa)) {
                if(jTableContent.getSelectedRow() != -1){
                    String maSP = jTableContent.getValueAt(jTableContent.getSelectedRow(), 0).toString();
                    int xoa = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa dòng này ?");
                    if(xoa == 0) {
                        try {
                            if(sanPhamService.delete(maSP)){
                                loadDataTable();
                                JOptionPane.showMessageDialog(
                                        this,
                                        "Đã xóa thành công sản phẩm này!!",
                                        "Thành công",
                                        JOptionPane.INFORMATION_MESSAGE
                                );
                            } else
                                JOptionPane.showMessageDialog(
                                        this,
                                        "Xóa thất bại sản phẩm này!!",
                                        "Lỗi",
                                        JOptionPane.ERROR_MESSAGE
                                );
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } else
                    JOptionPane.showMessageDialog(
                        this,
                        "Hãy chọn dòng để xóa",
                        "Lỗi",
                        JOptionPane.ERROR_MESSAGE
                    );

            }
            else if (source.equals(btnThem)) {
                if(validateInput()){
                    String maSP = "SP";
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
                        sanPham.setDanhMucSanPham(danhMucSanPhamService.findOne(maDanhMuc));
                        List<ThuocTinhSanPham> thuocTinhSanPhamList = new ArrayList<>();
                        int indexTT = Integer.parseInt(thuocTinhSanPhamService.findMaxID().substring(2));
                        for(String item : listThuocTinh){
                            String key = item.split(":")[0];
                            String value = item.split(":")[1];

                            ThuocTinhSanPham tts = new ThuocTinhSanPham();
                            tts.setTenThuocTinh(key);
                            tts.setGiaTriThuocTinh(value);
                            tts.setSanPham(sanPham);
                            tts.setMaThuocTinhSanPham(String.format("TT%05d", indexTT + 1));
                            indexTT++;

                            thuocTinhSanPhamList.add(tts);
                        }

                        sanPham.setThuocTinhSanPhams(thuocTinhSanPhamList);

                        if(sanPhamService.saveSanPhamVaThuocTinh(sanPham)){
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Thêm sản phẩm thành công",
                                    "Thành công",
                                    JOptionPane.INFORMATION_MESSAGE
                            );

                            loadDataTable();
                        } else {
                            JOptionPane.showMessageDialog(
                                this,
                                "Thêm sản phẩm thất bại",
                                "Thất bại",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }

                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
            else if (source.equals(btnSua)) {
                if(jTableContent.getSelectedRow() != -1){
                   if(validateInput()){
                       String maSP = jTableContent.getValueAt(jTableContent.getSelectedRow(), 0).toString();

                       try {
                           SanPham sanPham = sanPhamService.findOne(maSP);
                           if(sanPham != null){
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

                               sanPham.setTenSanPham(tenSP);
                               sanPham.setGiaBan(giaSP);
                               sanPham.setThueVAT(thueVAT);
                               sanPham.setSoLuongTon(soLuong);
                               sanPham.setMoTa(moTa);
                               sanPham.setTrangThai(trangThai);
                               sanPham.setDanhMucSanPham(danhMucSanPhamService.findOne(maDanhMuc));
                               sanPham.setNgayNhap(localDateNgayNhap);
                               sanPham.setHanSuDung(localDateHanSuDung);

                               if(sanPhamService.update(sanPham)){
                                   List<ThuocTinhSanPham> thuocTinhSanPhamList = thuocTinhSanPhamService.getListByProductId(maSP);

                                   thuocTinhSanPhamList.forEach(ttSP -> {
                                       try {
                                           thuocTinhSanPhamService.delete(ttSP.getMaThuocTinhSanPham());
                                       } catch (RemoteException ex) {
                                           throw new RuntimeException(ex);
                                       }
                                   });

                                   Arrays.stream(listThuocTinh).forEach(item -> {
                                       String itemKey = item.split(":")[0];
                                       String itemValue = item.split(":")[1];
                                       String thuocTinhID = "TT";

                                       ThuocTinhSanPham thuocTinhSanPham = new ThuocTinhSanPham(thuocTinhID, itemKey, itemValue);
                                       thuocTinhSanPham.setSanPham(sanPham);

                                       try {
                                           thuocTinhSanPhamService.save(thuocTinhSanPham);
                                       } catch (RemoteException ex) {
                                           throw new RuntimeException(ex);
                                       }
                                   });

                                   loadDataTable();
                                   JOptionPane.showMessageDialog(
                                           this,
                                           "Sửa sản phẩm thành công",
                                           "Thành công",
                                           JOptionPane.INFORMATION_MESSAGE
                                   );
                               } else {
                                   JOptionPane.showMessageDialog(
                                           this,
                                           "Sửa sản phẩm thất bại",
                                           "Lỗi",
                                           JOptionPane.ERROR_MESSAGE
                                   );
                               }
                           }
                           else
                               JOptionPane.showMessageDialog(
                                       this,
                                       "Không tìm thấy mã sản phẩm để sửa",
                                       "Lỗi",
                                       JOptionPane.ERROR_MESSAGE
                               );

                       }
                       catch (RemoteException ex) {
                           throw new RuntimeException(ex);
                       }
                   }
                }
                else
                    JOptionPane.showMessageDialog(
                            this,
                            "Chọn dòng sản phẩm cần sửa",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE
                    );

            }
            else if (source.equals(btnFind)) {
                String maSP = txtFind.getText();
                try {
                    SanPham sanPham = sanPhamService.findOne(maSP);
                    if(sanPham != null) {
                        dataModel.setRowCount(0);
                        String thuocTinh =  thuocTinhSanPhamService.getListByProductId(sanPham.getMaSanPham())
                                .stream().map(thuocTinhSanPham ->
                                thuocTinhSanPham.getTenThuocTinh() + ":" + thuocTinhSanPham.getGiaTriThuocTinh()
                        ).collect(Collectors.joining(","));
                        dataModel.addRow(new Object[] {
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
                    }
                    else
                        JOptionPane.showMessageDialog(
                                this,
                                "Không tìm thấy mã sản phẩm",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE
                        );
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }

            }
            else if (source.equals(btnResetTable)){
                loadDataTable();
            }
            else if (source.equals(jComboBoxDanhMuc) || source.equals(jComboBoxMonth) || source.equals(jComboBoxStatus) || source.equals(jComboBoxYear)){
                String danhMuc = jComboBoxDanhMuc.getSelectedItem().toString();
                String month = jComboBoxMonth.getSelectedItem().toString();
                String status = jComboBoxStatus.getSelectedItem().toString();
                String year = jComboBoxYear.getSelectedItem().toString();
                try {
                    dataModel.setRowCount(0);
                    sanPhamService.getListByFitter(danhMuc, month, year, status).forEach(sanPham -> {
                        try {
                            String thuocTinh = thuocTinhSanPhamService.getListByProductId(sanPham.getMaSanPham())
                                    .stream().map(thuocTinhSanPham ->
                                            thuocTinhSanPham.getTenThuocTinh() + ":" + thuocTinhSanPham.getGiaTriThuocTinh()
                                    ).collect(Collectors.joining(","));

                            dataModel.addRow(new Object[] {
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
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                    });
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }


    private boolean validateInput() {
        if (txtTenSP.getText().trim().isEmpty() || txtGiaSP.getText().trim().isEmpty()
                || txtSoLuong.getText().trim().isEmpty() || txtThueVAT.getText().trim().isEmpty()
                || dateNgayNhap.getDate() == null || dateHanSuDung.getDate() == null
                || txtThuocTinh.getText().trim().isEmpty())  {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể để trống các trường trừ Mô Tả",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }

        if(!txtGiaSP.getText().matches("\\d+(\\.\\d+)?")){
            JOptionPane.showMessageDialog(
                    this,
                    "Giá sản phẩm phải lớn hơn 0",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            txtGiaSP.requestFocus();
            return false;
        }

        if(!txtSoLuong.getText().matches("\\d+")){
            JOptionPane.showMessageDialog(
                    this,
                    "Số lượng phải là số nguyên lớn hơn 0",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            txtSoLuong.requestFocus();
            return false;
        }

        if(!txtThueVAT.getText().matches("0(\\.\\d+)?|1(\\.0+)?")){
            JOptionPane.showMessageDialog(
                    this,
                    "Thuế phải có giá trị từ 0.0 đến 1.0 (0% -> 100%) ",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            txtThueVAT.requestFocus();
            return false;
        }

        if (!txtThuocTinh.getText().matches("([\\p{L} ]+:[^:,]+)(,\\s*[\\p{L} ]+:[^:,]+)*")) {
            JOptionPane.showMessageDialog(
                    this,
                    "Mỗi thuộc tính phải có dạng KEY:VALUE và cách nhau bằng dấu phẩy",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            txtThuocTinh.requestFocus();
            return false;
        }

        if(dateNgayNhap.getDate().after(new java.util.Date())){
            JOptionPane.showMessageDialog(
                    this,
                    "Ngày nhập phải trước ngày hiện tại",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            dateNgayNhap.requestFocus();
            return false;
        }

        if(dateNgayNhap.getDate().after(dateHanSuDung.getDate())){
            JOptionPane.showMessageDialog(
                    this,
                    "Hạn sử dụng phải sau ngày nhập",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );
            dateNgayNhap.requestFocus();
            return false;
        }




        return true;
    }


    private void loadDataTable(){
        try {
            dataModel.setRowCount(0);
            sanPhamService.getList().forEach(sanPham -> {
                List<ThuocTinhSanPham> thuocTinhSanPhamList = null;
                try {
                    thuocTinhSanPhamList = thuocTinhSanPhamService.getListByProductId(sanPham.getMaSanPham());
                    String thuocTinh =  thuocTinhSanPhamList.stream().map(thuocTinhSanPham ->
                            thuocTinhSanPham.getTenThuocTinh() + ":" + thuocTinhSanPham.getGiaTriThuocTinh()
                    ).collect(Collectors.joining(","));


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
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
            });
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() != null) {
            Object source = e.getSource();
            if(source.equals(jTableContent)) {
                String maSP = jTableContent.getValueAt(jTableContent.getSelectedRow(), 0).toString();
                try {
                    SanPham sanPham = sanPhamService.findOne(maSP);
                    txtMaSP.setText(sanPham.getMaSanPham());
                    txtTenSP.setText(sanPham.getTenSanPham());
                    txtGiaSP.setText(sanPham.getGiaBan() + "");
                    Date dateHSD = java.sql.Date.valueOf(sanPham.getHanSuDung());
                    dateHanSuDung.setDate(dateHSD);
                    txtThueVAT.setText(String.format("%.2f", sanPham.getThueVAT()));
                    txtSoLuong.setText(sanPham.getSoLuongTon() + "");
                    Date dateNgaynhap = java.sql.Date.valueOf(sanPham.getNgayNhap());
                    dateNgayNhap.setDate(dateNgaynhap);
                    String thuocTinh =  thuocTinhSanPhamService.getListByProductId(sanPham.getMaSanPham()).stream().map(thuocTinhSanPham ->
                            thuocTinhSanPham.getTenThuocTinh() + ":" + thuocTinhSanPham.getGiaTriThuocTinh()
                    ).collect(Collectors.joining(","));


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


