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

import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;
import io.github.cdimascio.dotenv.Dotenv;
import model.*;
import net.datafaker.Faker;
import gui.components.ComponentUtils;
import service.NhanVienService;
import service.TaiKhoanService;

import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.List;

public class PanelTaiKhoan extends JPanel {
	private JTextField txtMaNV, txtTenNV, txtNgaySinh, txtSDT, txtDiaChi, txtSoDinhDanh, txtTimMaNV, txtTimTenNV;
	private JComboBox<String> cboGioiTinh, cboChucVu;
	private JButton btnThem, btnXoa, btnSua, btnReset, btnTim;
	private JTable table;
	private DefaultTableModel tableModel;
	private JLabel jLableMaNV;
	private JLabel jLableTenNV;
	private JLabel jLableNS;
	private JDateChooser dateNgaySinh;
	private JComboBox<String> cboChucVuLoc;
	private JLabel jLableMaTK;
	private JTextField txtMaTK;
	private JLabel jLableDangNhap;
	private JTextField txtTenDangNhap;
	private JComboBox cboTrangThai;
	private JPasswordField txtPassword;
	private JTextField txtTimMaTK;
	private JTextField txtTimTenDangNhap;


	private final Faker faker = new Faker();
	Dotenv dotenv = Dotenv.load();
	String drivername = dotenv.get("DRIVER_NAME");

	private final Context context = new InitialContext();
	private final TaiKhoanService taiKhoanService = (TaiKhoanService) context.lookup("rmi://" + drivername + ":9090/taiKhoanService");
	private final NhanVienService nhanVienService = (NhanVienService) context.lookup("rmi://" + drivername + ":9090/nhanVienService");
	private JComboBox<String> cbbMaNV;

	public PanelTaiKhoan() throws NamingException, RemoteException {
		FlatLightLaf.setup();
		UIManager.put("PasswordField.showRevealButton", true);
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

//		loadMaNVcbbox();

		// Thêm dữ liệu mẫu
		addSampleData();
	}

	private JPanel createInfoPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());


		panel.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(20, 20, 10, 20),
				ComponentUtils.getTitleBorder("Thông tin tài khoản")
		));

		Box boxFormInput = Box.createVerticalBox();

		Box box1 = Box.createHorizontalBox();
		jLableMaTK = new JLabel("Mã TK:") ;
		jLableMaTK.setPreferredSize(new Dimension(90, 25));
		txtMaTK = new JTextField(20);
		txtMaTK.setEditable(false);
		jLableDangNhap = new JLabel("Tên đăng nhập:") ;
		jLableDangNhap.setPreferredSize(new Dimension(90, 25));
		txtTenDangNhap = new JTextField(20);
		JLabel jLabelPwd = new JLabel("Mật khẩu:");
		jLabelPwd.setPreferredSize(new Dimension(90, 25));
		txtPassword = new JPasswordField();

		box1.add(Box.createHorizontalStrut(50));
		box1.add(jLableMaTK);
		box1.add(txtMaTK);
		box1.add(Box.createHorizontalStrut(30));
		box1.add(jLableDangNhap);
		box1.add(txtTenDangNhap);
		box1.add(Box.createHorizontalStrut(30));
		box1.add(jLabelPwd);
		box1.add(txtPassword);
		box1.add(Box.createHorizontalStrut(50));


		Box box2 = Box.createHorizontalBox();
		JLabel jLabelmaNV = new JLabel("Mã nhân viên:");
		jLabelmaNV.setPreferredSize(new Dimension(90, 25));
		cbbMaNV = new JComboBox<String>();
		JLabel jLabelTrangThai = new JLabel("Trạng thái:");
		jLabelTrangThai.setPreferredSize(new Dimension(90, 25));
		cboTrangThai = new JComboBox<>(new String[]{"Đang hoạt động", "Không còn hoạt đông"});
		cboTrangThai.setPreferredSize(new Dimension(445, 25));

		box2.add(Box.createHorizontalStrut(50));
		box2.add(jLabelmaNV);
		box2.add(cbbMaNV);
		box2.add(Box.createHorizontalStrut(30));
		box2.add(jLabelTrangThai);
		box2.add(cboTrangThai);
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
				"Tra cứu tài khoản",
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

		JLabel lblMaNV = new JLabel("Mã tài khoản:");
		lblMaNV.setFont(new Font("Arial", Font.PLAIN, 14));
		txtTimMaTK = new JTextField(10);
		txtTimMaTK.setFont(new Font("Arial", Font.PLAIN, 14));

		btnTim = new JButton("Tìm kiếm");
		btnTim.setFont(new Font("Arial", Font.BOLD, 14));
		btnTim.setBackground(new Color(33, 150, 243));
		btnTim.setForeground(Color.WHITE);
		btnTim.setFocusPainted(false);
		btnTim.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

		searchByIDPanel.add(lblMaNV);
		searchByIDPanel.add(txtTimMaTK);
		searchByIDPanel.add(btnTim);

		// Panel lọc theo tên
		JPanel filterByNamePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
		filterByNamePanel.setBackground(new Color(240, 240, 240));

		JLabel lblTenNV = new JLabel("Tên tài khoản: ");
		lblTenNV.setFont(new Font("Arial", Font.PLAIN, 15));
		txtTimTenDangNhap = new JTextField(15);
		txtTimTenDangNhap.setPreferredSize(new Dimension(205, 30));
		txtTimTenDangNhap.setFont(new Font("Arial", Font.PLAIN, 15));
		jLableMaNV = new JLabel("Trạng thái: ");
		jLableMaNV.setFont(new Font("Arial", Font.PLAIN, 15));
		cboChucVuLoc = new JComboBox<>(new String[]{"Tất cả", "Đang hoạt động", "Không còn hoạt động"});
		cboChucVuLoc.setFont(new Font("Arial", Font.PLAIN, 15));
		cboChucVuLoc.setPreferredSize(new Dimension(205, 30));

		filterByNamePanel.add(lblTenNV);
		filterByNamePanel.add(txtTimTenDangNhap);
		filterByNamePanel.add(jLableMaNV);
		filterByNamePanel.add(cboChucVuLoc);

		panel.add(searchByIDPanel, BorderLayout.WEST);
		panel.add(filterByNamePanel, BorderLayout.EAST);

		searchByIDPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		filterByNamePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

		btnTim.addActionListener(e -> searchEmployee());


		txtTimTenDangNhap.getDocument().addDocumentListener(new DocumentListener() {
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

		JPanel jPanelLamMoi = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		JButton lammoi = new JButton("Làm mới");
		lammoi.setPreferredSize(new Dimension(100, 30));
		jPanelLamMoi.add(lammoi);

		String[] columns = {"Mã tài khoản", "Tên đăng nhập", "Password", "Trạng thái"};
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
				displaySelectedTaiKhoan();
			}
		});

		panel.add(jPanelLamMoi, BorderLayout.NORTH);
		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane, BorderLayout.CENTER);

		lammoi.addActionListener(e -> {
            try {
                resetTable();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
        });

		return panel;
	}

	private void resetTable() throws RemoteException {
        tableModel.setRowCount(0);
        addSampleData();
    }

	private void addButtonListeners() {
		btnThem.addActionListener(e -> addTaiKhoan());
		btnXoa.addActionListener(e -> deleteTaiKhoan());
		btnSua.addActionListener(e -> updateTaiKhoan());
		btnReset.addActionListener(e -> resetForm());
	}

	private void loadMaNVcbbox() throws RemoteException {
		System.out.println("hlnll");
		List<NhanVien> dsNhanVien = nhanVienService.getAllNhanVien();
		System.out.println(dsNhanVien);
//		List<NhanVien> nhanVienDaCoTaiKhoan = taiKhoanDAO.getNhanVien();
//		Set<String> maNVCoTaiKhoan = nhanVienDaCoTaiKhoan.stream()
//				.map(NhanVien::getMaNhanVien)
//				.collect(Collectors.toSet());
//
//
//		List<NhanVien> nhanVienChuaCoTaiKhoan = allNhanViens.stream()
//				.filter(nv -> !maNVCoTaiKhoan.contains(nv.getMaNhanVien()))
//				.collect(Collectors.toList());
//
//		System.out.println(nhanVienDaCoTaiKhoan);
//
//		for (NhanVien nhanVien :  nhanVienChuaCoTaiKhoan) {
//			cbbMaNV.addItem(nhanVien.getMaNhanVien());
//		}
	}

	private void addTaiKhoan() {
		try {
			if(validateInput()) {
				String maNV = "TK" + faker.number().digits(5);
				String tenDangNhap = txtTenDangNhap.getText();
				String password = txtPassword.getText();
				String trangthai = cboTrangThai.getSelectedItem().toString();

				TaiKhoan taiKhoan = new TaiKhoan(maNV, tenDangNhap, password, trangthai);

				if(taiKhoanService.insertTaiKhoan(taiKhoan)) {
					tableModel.addRow(new Object[]{
							taiKhoan.getMaTaiKhoan(),
							taiKhoan.getTenDangNhap(),
							taiKhoan.getMatKhau(),
							taiKhoan.getTrangThai()
					});
					resetForm();
					JOptionPane.showMessageDialog(this, "Đã thêm tài khoản thành công");
				} else {
					JOptionPane.showMessageDialog(this, "Không thể thêm tài khoản mới");
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

	private void deleteTaiKhoan() {
		try {
			int selectedRow = table.getSelectedRow();
			if (selectedRow >= 0) {
				String maTK = table.getValueAt(selectedRow, 0).toString();

				int confirm = JOptionPane.showConfirmDialog(
						this,
						"Bạn có chắc chắn muốn xóa tài khoản này?",
						"Xác nhận xóa",
						JOptionPane.YES_NO_OPTION);

				if (confirm == JOptionPane.YES_OPTION) {
					if (taiKhoanService.deleteTaiKhoan(maTK)) {
						tableModel.removeRow(selectedRow);
						resetForm();
						JOptionPane.showMessageDialog(
								this,
								"Xóa tài khoản thành công!",
								"Thành công",
								JOptionPane.INFORMATION_MESSAGE);
					} else {
						JOptionPane.showMessageDialog(
								this,
								"Không thể xóa tài khoản. Vui lòng thử lại!",
								"Lỗi",
								JOptionPane.ERROR_MESSAGE);
					}
				}
			} else {
				JOptionPane.showMessageDialog(
						this,
						"Vui lòng chọn tài khoản cần xóa!",
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

	private void updateTaiKhoan() {
//		try {
//			int row = table.getSelectedRow();
//			if (row >= 0) {
//				if(validateInput()) {
//					String maTK = table.getValueAt(row, 0).toString();
//					String tenDangNhap = txtTenDangNhap.getText().toString();
//					String sdt = txtSDT.getText();
//					String gioiTinh = cboGioiTinh.getSelectedItem().toString();
//					String enumGioiTinh = gioiTinh.equals("Nam") ? "NAM" : "NU";
//					String diemTichLuy = txtDiemTichluy.getText().trim();
//					int diem = Integer.parseInt(diemTichLuy);
//
//					GioiTinh enumGT = GioiTinh.valueOf(enumGioiTinh);
//					KhachHang khachHang = new KhachHang(maKH, tenKH, sdt, enumGT, diem);
//
//					int confirm = JOptionPane.showConfirmDialog(
//							this,
//							"Bạn có chắc muốn cập nhật khách hàng " + tenKH,
//							"Xác nhận cập nhật",
//							JOptionPane.YES_NO_OPTION
//					);
//
//					if(confirm == JOptionPane.YES_OPTION) {
//						if(khachHangDAO.suaKhachHang(maKH, khachHang)) {
//							tableModel.setValueAt(maKH, row, 0);
//							tableModel.setValueAt(tenKH, row, 1);
//							tableModel.setValueAt(sdt, row, 2);
//							tableModel.setValueAt(gioiTinh, row, 3);
//							tableModel.setValueAt(diemTichLuy, row, 4);
//							resetForm();
//							JOptionPane.showMessageDialog(
//									this,
//									"Cập nhật khách hàng "+tenKH+"thành công",
//									"Thành công",
//									JOptionPane.INFORMATION_MESSAGE
//							);
//						} else {
//							JOptionPane.showMessageDialog(
//									this,
//									"Không thể cập nhật khách hàng" + tenKH,
//									"Lỗi",
//									JOptionPane.ERROR_MESSAGE
//							);
//						}
//					}
//				}
//			} else {
//				JOptionPane.showMessageDialog(
//						this,
//						"Vui lòng chọn khách cần cập nhật",
//						"Thông báo",
//						JOptionPane.ERROR_MESSAGE
//				);
//			}
//		} catch (RemoteException e) {
//			e.printStackTrace();
//			JOptionPane.showMessageDialog(
//					this,
//					"Lỗi kết nối máy chủ: " + e.getMessage(),
//					"Lỗi kết nối",
//					JOptionPane.ERROR_MESSAGE);
//		}
	}

	private void resetForm() {
		txtMaTK.setText("");
		txtTenDangNhap.setText("");
		cboTrangThai.setSelectedIndex(0);
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
				displaySelectedTaiKhoan();
				return;
			}
		}

		JOptionPane.showMessageDialog(this, "Không tìm thấy nhân viên phù hợp!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
	}

	private void displaySelectedTaiKhoan() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow >= 0) {

			txtMaTK.setText(tableModel.getValueAt(selectedRow, 0).toString());
			txtTenDangNhap.setText(tableModel.getValueAt(selectedRow, 1).toString());
			String trangthai = tableModel.getValueAt(selectedRow, 3).toString();
			for (int i = 0; i < cboTrangThai.getItemCount(); i++) {
				if (cboTrangThai.getItemAt(i).equals(trangthai)) {
					cboTrangThai.setSelectedIndex(i);
					break;
				}
			}
		}
	}

	private boolean validateInput() {
		if (txtTenDangNhap.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập tên nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			txtTenNV.requestFocus();
			return false;
		}

		if (txtPassword.getPassword().equals("")) {
			JOptionPane.showMessageDialog(this, "Vui lòng password!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			txtSDT.requestFocus();
			return false;
		}

		return true;
	}

	private String generateEmployeeId() {
		return "TK" + (tableModel.getRowCount() + 1);
	}

	private void addSampleData() throws RemoteException{

		List<TaiKhoan> taiKhoans = taiKhoanService.getAllTaiKhoan();

//		String[] columns = {"Mã tài khoản", "Tên đăng nhập", "Password", "Trạng thái"};
		for(TaiKhoan taiKhoan : taiKhoans) {
			tableModel.addRow(new Object[]{
					taiKhoan.getMaTaiKhoan(),
					taiKhoan.getTenDangNhap(),
					taiKhoan.getMatKhau(),
					taiKhoan.getTrangThai(),
			});
		}
	}
}