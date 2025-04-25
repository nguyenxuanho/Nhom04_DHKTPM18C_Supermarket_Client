package gui.panel;

import gui.components.ComponentUtils;
import gui.components.CustomToastNotification;
import io.github.cdimascio.dotenv.Dotenv;
import model.DanhMucSanPham;
import net.datafaker.Faker;
import service.DanhMucSanPhamService;

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
import java.net.URISyntaxException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.List;

public class PanelDanhMucSanPham extends JPanel implements MouseListener, ActionListener {
    private final JLabel jLabelMaDanhMucSP, jLabelTenDanhMucSP, labelFind;
    private final JTextField txtMaDanhMucSP, txtTenDanhMucSP, txtFind;

    private final JTable jTableContent;

    private final Faker faker = new Faker();

    private final DefaultTableModel dataModel;


    private final JButton btnThem, btnXoa, btnSua, btnReset, btnResetTable, btnFind;


    public PanelDanhMucSanPham () throws NamingException, RemoteException, URISyntaxException {
        setLayout(new BorderLayout());
       List<DanhMucSanPham> danhMucSanPhams = RmiServiceLocator.getDanhMucSanPhamService().getList();
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Tạo tiêu đề căn giữa
        JLabel titleLabel = new JLabel("Giao diện quản lý danh mục sản phẩm", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10)); // tạo khoảng cách

        add(titleLabel, BorderLayout.NORTH);


        // Tên cột cho bảng
        String[] columnNames = {"Mã danh mục sản phẩm", "Tên danh mục sản phẩm"};

        // Tạo model cho bảng với data
        dataModel = new DefaultTableModel(columnNames, 0);


        jTableContent = new JTable(dataModel);
        ComponentUtils.setTable(jTableContent);

        RmiServiceLocator.getDanhMucSanPhamService().getList().forEach(danhMucSanPham -> {
                dataModel.addRow ( new Object[]{
                        danhMucSanPham.getMaDanhMucSanPham(),
                        danhMucSanPham.getTenDanhMucSanPham(),
                });
        });

        // Tùy chỉnh độ cao hàng và font chữ nếu cần
        jTableContent.setRowHeight(30);
        jTableContent.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTableContent.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));

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
        txtFind = new JTextField(50);
        btnFind = new JButton("Tìm");



        boxSearch.add(labelFind);
        boxSearch.add(Box.createHorizontalStrut(10));
        boxSearch.add(txtFind);
        boxSearch.add(Box.createHorizontalStrut(10));
        boxSearch.add(btnFind);



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
        row1.add(jLabelMaDanhMucSP = new JLabel("Mã danh mục sản phẩm:"));
        jLabelMaDanhMucSP.setPreferredSize(new Dimension(200, 25));
        row1.add(Box.createHorizontalStrut(10));
        row1.add(txtMaDanhMucSP = new JTextField(30));
        txtMaDanhMucSP.setEditable(false);

        row1.add(Box.createHorizontalStrut(30));
        row1.add(jLabelTenDanhMucSP = new JLabel("Tên danh mục sản phẩm:"));
        jLabelTenDanhMucSP.setPreferredSize(new Dimension(200, 25));
        row1.add(Box.createHorizontalStrut(10));
        row1.add(txtTenDanhMucSP = new JTextField(30));



        jLabelMaDanhMucSP.setFont(labelFont);
        jLabelTenDanhMucSP.setFont(labelFont);


        txtMaDanhMucSP.setFont(textFieldFont);
        txtTenDanhMucSP.setFont(textFieldFont);


//      Thêm các row
        boxVerticalFormGroup.add(row1);
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

        btnThem.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnXoa.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSua.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnReset.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnFind.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnResetTable.setCursor(new Cursor(Cursor.HAND_CURSOR));



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


//        End action


//        Custom GUI
        ComponentUtils.setButton(btnThem, new Color(33, 150, 243));
        ComponentUtils.setButton(btnXoa, new Color(244, 67, 54));
        ComponentUtils.setButton(btnSua, new Color(255, 193, 7));
        ComponentUtils.setButton(btnReset, new Color(76, 175, 80));

        ComponentUtils.setButtonMain(btnFind);
        ComponentUtils.setButtonMain(btnResetTable);


        btnThem.setIcon(new ImageIcon(getClass().getResource("/image/add.png")));
        btnXoa.setIcon(new ImageIcon(getClass().getResource("/image/delete.png")));
        btnSua.setIcon(new ImageIcon(getClass().getResource("/image/edit.png")));
        btnReset.setIcon(new ImageIcon(getClass().getResource("/image/clean.png")));
        btnResetTable.setIcon(new ImageIcon(getClass().getResource("/image/refresh.png")));
        btnFind.setIcon(new ImageIcon(getClass().getResource("/image/search.png")));


//        End custom GUI

    }

    private boolean validateInput() {
        if (txtTenDanhMucSP.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "Không thể để trống",
                    "Lỗi",
                    JOptionPane.ERROR_MESSAGE
            );

            txtTenDanhMucSP.requestFocus();
            return false;
        }

        return true;
    }

    public void loadTable(){
        try {
            dataModel.setRowCount(0);
            RmiServiceLocator.getDanhMucSanPhamService().getList().forEach(danhMucSanPham -> {
                dataModel.addRow ( new Object[]{
                        danhMucSanPham.getMaDanhMucSanPham(),
                        danhMucSanPham.getTenDanhMucSanPham(),
                });

            });
        } catch (RemoteException ex) {
            throw new RuntimeException(ex);
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() != null) {
            Object source = e.getSource();
            if(source.equals(btnReset)) {
                txtMaDanhMucSP.setText("");
                txtTenDanhMucSP.setText("");
            }
            else if (source.equals(btnXoa)) {
                if(jTableContent.getSelectedRow() != -1){
                    String maDanhMucSP = jTableContent.getValueAt(jTableContent.getSelectedRow(), 0).toString();
                    int xoa = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xóa dòng này ?");
                    if(xoa == 0) {
                        try {
                            RmiServiceLocator.getDanhMucSanPhamService().delete(maDanhMucSP);
                            dataModel.removeRow(jTableContent.getSelectedRow());
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Xóa danh mục thành công",
                                    "Thành công",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "Cần chọn dòng để xóa",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            }
            else if (source.equals(btnThem)) {
                if(validateInput()){
                    String maDanhMucSP = "DMSP" + faker.number().digits(5);
                    String tenDanhMucSP = txtTenDanhMucSP.getText();

                    DanhMucSanPham danhMucSanPham = new DanhMucSanPham(maDanhMucSP, tenDanhMucSP);

                    try {
                        if(RmiServiceLocator.getDanhMucSanPhamService().save(danhMucSanPham)){
                            loadTable();
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Thêm danh mục thành công",
                                    "Thành công",
                                    JOptionPane.INFORMATION_MESSAGE
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                    this,
                                    "Thêm danh mục thất bại",
                                    "Lỗi",
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
                        String maDanhMucSP = jTableContent.getValueAt(jTableContent.getSelectedRow(), 0).toString();

                        try {
                            DanhMucSanPham danhMucSanPham = RmiServiceLocator.getDanhMucSanPhamService().findOne(maDanhMucSP);
                            if(danhMucSanPham != null){
                                String tenDanhMucSP = txtTenDanhMucSP.getText();

                                danhMucSanPham.setTenDanhMucSanPham(tenDanhMucSP);

                                if(RmiServiceLocator.getDanhMucSanPhamService().update(danhMucSanPham)){
                                    JOptionPane.showMessageDialog(
                                            this,
                                            "Cập nhật danh mục thành công",
                                            "Thành công",
                                            JOptionPane.INFORMATION_MESSAGE
                                    );
                                    loadTable();
                                } else
                                    JOptionPane.showMessageDialog(
                                            this,
                                            "Cập nhật danh mục thất bại",
                                            "Lỗi",
                                            JOptionPane.ERROR_MESSAGE
                                    );
                            } else JOptionPane.showMessageDialog(null, "Không tìm thấy mã danh mục sản phẩm để sửa");

                        } catch (RemoteException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                } else
                    JOptionPane.showMessageDialog(
                            this,
                            "Chọn dòng danh mục sản phẩm cần sửa",
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE
                    );
            }
            else if (source.equals(btnFind)) {
                String maDanhMucSP = txtFind.getText();
                try {
                    DanhMucSanPham danhMucSanPham = RmiServiceLocator.getDanhMucSanPhamService().findOne(maDanhMucSP);
                    if(danhMucSanPham != null) {
                        dataModel.setRowCount(0);
                        dataModel.addRow(new Object[] {
                                danhMucSanPham.getMaDanhMucSanPham(),
                                danhMucSanPham.getTenDanhMucSanPham(),
                        });
                    } else
                        JOptionPane.showMessageDialog(
                                this,
                                "Không tìm thấy mã danh mục sản phẩm này",
                                "Lỗi",
                                JOptionPane.ERROR_MESSAGE
                        );
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }

            }
            else if (source.equals(btnResetTable)){
                loadTable();
            }

        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource() != null) {
            Object source = e.getSource();
            if(source.equals(jTableContent)) {
                String maDanhMucSP = jTableContent.getValueAt(jTableContent.getSelectedRow(), 0).toString();
                try {
                    DanhMucSanPham danhMucSanPham = RmiServiceLocator.getDanhMucSanPhamService().findOne(maDanhMucSP);
                    txtMaDanhMucSP.setText(danhMucSanPham.getMaDanhMucSanPham());
                    txtTenDanhMucSP.setText(danhMucSanPham.getTenDanhMucSanPham());
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


