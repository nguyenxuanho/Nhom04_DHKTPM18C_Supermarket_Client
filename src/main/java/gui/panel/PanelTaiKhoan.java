package gui.panel;


import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;
import lombok.extern.slf4j.Slf4j;
import model.*;

import gui.components.ComponentUtils;


import java.awt.*;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.List;
import java.util.stream.Collectors;

import static gui.panel.RmiServiceLocator.nhanVienService;
import static gui.panel.RmiServiceLocator.taiKhoanService;

@Slf4j
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

		loadMaNVcbbox();

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
		cbbMaNV.setPreferredSize(new Dimension(168, 25));
		JLabel jLabelTrangThai = new JLabel("Trạng thái:");
		jLabelTrangThai.setPreferredSize(new Dimension(90, 25));
		cboTrangThai = new JComboBox<>(new String[]{"hoạt động", "dừng hoạt động"});
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
		btnTim.setIcon(new ImageIcon(getClass().getResource("/image/search.png")));

		searchByIDPanel.add(lblMaNV);
		searchByIDPanel.add(txtTimMaTK);
		searchByIDPanel.add(btnTim);

		// Panel lọc theo tên
		JPanel filterByNamePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 20));
		filterByNamePanel.setBackground(new Color(240, 240, 240));

		JLabel lblTenNV = new JLabel("Tên đăng nhập: ");
		lblTenNV.setFont(new Font("Arial", Font.PLAIN, 15));
		txtTimTenDangNhap = new JTextField(15);
		txtTimTenDangNhap.setPreferredSize(new Dimension(205, 30));
		txtTimTenDangNhap.setFont(new Font("Arial", Font.PLAIN, 15));
		jLableMaNV = new JLabel("Trạng thái: ");
		jLableMaNV.setFont(new Font("Arial", Font.PLAIN, 15));
		cboChucVuLoc = new JComboBox<>(new String[]{"Tất cả", "hoạt động", "dừng hoạt động"});
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

		btnTim.addActionListener(e -> searchTaiKhoan());


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
				String tendanhnhap = txtTimTenDangNhap.getText().trim();
                if (!tendanhnhap.isEmpty()) {
                    try {
                       List<TaiKhoan> taiKhoans = taiKhoanService.getAllTaiKhoan();
                        List<TaiKhoan> taiKhoanList = taiKhoans.stream()
                                        .filter(tk -> tk.getTenDangNhap().contains(tendanhnhap))
                                        .collect(Collectors.toList());
                        hienThiKetQuaTimKiem(taiKhoanList);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                                "Lỗi kết nối máy chủ: " + ex.getMessage(),
                                "Lỗi", JOptionPane.ERROR_MESSAGE);
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
							List<TaiKhoan> allTaiKhoan = taiKhoanService.getAllTaiKhoan();
							List<TaiKhoan> filteredList = allTaiKhoan.stream()
									.filter(taiKhoan -> taiKhoan.getTrangThai().equalsIgnoreCase(selectedChucVu))
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

	private JPanel createTablePanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		JPanel jPanelLamMoi = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
		JButton lammoi = new JButton("Refresh");
		ComponentUtils.setButtonMain(lammoi);
		jPanelLamMoi.add(lammoi);
		lammoi.setIcon(new ImageIcon(getClass().getResource("/image/refresh.png")));

		String[] columns = {"Mã tài khoản", "Tên đăng nhập", "Password", "Nhân viên", "Trạng thái"};
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

		lammoi.addActionListener(e -> resetTable());

		return panel;
	}

	private void resetTable() {
		try{
			tableModel.setRowCount(0);
			addSampleData();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
    }

	private void addButtonListeners() {
		btnThem.addActionListener(e -> addTaiKhoan());
		btnXoa.addActionListener(e -> deleteTaiKhoan());
		btnSua.addActionListener(e -> updateTaiKhoan());
		btnReset.addActionListener(e -> resetForm());
	}



	private void addTaiKhoan() {
		try {
			if(validateInput()) {
//				String maTK = "TK" + faker.number().digits(5);
				String maTK = "";
				String tenDangNhap = txtTenDangNhap.getText();
				String password = txtPassword.getText();
//				String hashPassword = PasswordUtil.hashPassword(password);
				String trangthai = cboTrangThai.getSelectedItem().toString();
				String maNV = cbbMaNV.getSelectedItem().toString();

				NhanVien nhanVien = nhanVienService.getNhanVienById(maNV);

				TaiKhoan taiKhoan = new TaiKhoan(maTK, tenDangNhap, password, trangthai);
				taiKhoan.setNhanVien(nhanVien);

				if(taiKhoanService.insertTaiKhoan(taiKhoan)) {
					resetTable();
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
						resetTable();
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
		try {
			int row = table.getSelectedRow();
			if (row >= 0) {
				if(validateInput()) {
					String maTK = table.getValueAt(row, 0).toString();
					String tenDangNhap = txtTenDangNhap.getText().toString();
					NhanVien nhanVien = taiKhoanService.getNhanVienByTaiKhoan(maTK);
					String password = txtPassword.getText().toString();
					String trangthai = cboTrangThai.getSelectedItem().toString();

					TaiKhoan taiKhoanHienTai = taiKhoanService.getTaiKhoanById(maTK);
					TaiKhoan taiKhoan = new TaiKhoan();
					taiKhoan.setMaTaiKhoan(maTK);
					taiKhoan.setTenDangNhap(tenDangNhap);
//					if(!password.trim().equals("")) {
//						taiKhoan.setMatKhau(password);
//					} else {
//						taiKhoan.setMatKhau(taiKhoanHienTai.getMatKhau());
//					}

//					String trangthai = taiKhoanHienTai.getTrangThai();
					taiKhoan.setMatKhau(password);
					taiKhoan.setTrangThai(trangthai);
					taiKhoan.setNhanVien(nhanVien);

					int confirm = JOptionPane.showConfirmDialog(
							this,
							"Bạn có chắc muốn cập nhật tài khoản của " + tenDangNhap,
							"Xác nhận cập nhật",
							JOptionPane.YES_NO_OPTION
					);

					if(confirm == JOptionPane.YES_OPTION) {
						if(taiKhoanService.updateTaiKhoan(taiKhoan)) {
							resetTable();
							resetForm();
							JOptionPane.showMessageDialog(
									this,
									"Cập nhật tài khoản thành công",
									"Thành công",
									JOptionPane.INFORMATION_MESSAGE
							);
						} else {
							JOptionPane.showMessageDialog(
									this,
									"Không thể cập nhật tài khoản" ,
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
		txtMaTK.setText("");
		txtTenDangNhap.setText("");
		cboTrangThai.setSelectedIndex(0);
		txtPassword.setText("");
		table.clearSelection();
	}

	private void searchTaiKhoan(){
		String maTaiKhoan = txtTimMaTK.getText().trim();

		try {
			TaiKhoan taiKhoan = taiKhoanService.getTaiKhoanById(maTaiKhoan);
			if(taiKhoan != null) {
				NhanVien nhanVien = taiKhoanService.getNhanVienByTaiKhoan(taiKhoan.getMaTaiKhoan());
				tableModel.setRowCount(0);
				tableModel.addRow(new Object[]{
						taiKhoan.getMaTaiKhoan(),
						taiKhoan.getTenDangNhap(),
						"...",
						nhanVien.getMaNhanVien(),
						taiKhoan.getTrangThai()
				});
			} else {
				JOptionPane.showMessageDialog(null, "Mã " + maTaiKhoan + " không tồn tài");
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

	private void displaySelectedTaiKhoan() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow >= 0) {

			txtMaTK.setText(tableModel.getValueAt(selectedRow, 0).toString());
			txtTenDangNhap.setText(tableModel.getValueAt(selectedRow, 1).toString());
			txtPassword.setText(tableModel.getValueAt(selectedRow, 2).toString());
			String trangthai = tableModel.getValueAt(selectedRow, 3).toString();
			for (int i = 0; i < cboTrangThai.getItemCount(); i++) {
				if (cboTrangThai.getItemAt(i).equals(trangthai)) {
					cboTrangThai.setSelectedIndex(i);
					break;
				}
			}
		}
	}

	private boolean validateInput() throws RemoteException {
		if (txtTenDangNhap.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Vui lòng nhập tên đăng nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			txtTenDangNhap.requestFocus();
			return false;
		}

		List<TaiKhoan> taiKhoans = RmiServiceLocator.getTaiKhoanService().getAllTaiKhoan();
		for (TaiKhoan taiKhoan : taiKhoans) {
			if(taiKhoan.getTenDangNhap().equals(txtTenDangNhap.getText().trim())) {
				JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
				txtTenDangNhap.requestFocus();
				txtTenDangNhap.selectAll();
				return false;
			}
		}

		if (txtPassword.getPassword().equals("")) {
			JOptionPane.showMessageDialog(this, "Vui lòng password!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			txtSDT.requestFocus();
			return false;
		}

		if(txtPassword.getPassword().length < 6) {
			JOptionPane.showMessageDialog(this, "Mật khẩu phải có ít nhất 6 ký tự!", "Lỗi", JOptionPane.ERROR_MESSAGE);
			txtPassword.requestFocus();
			return false;
		}

		return true;
	}

	private void addSampleData() throws RemoteException{

		List<TaiKhoan> taiKhoans = taiKhoanService.getAllTaiKhoan();

//		String[] columns = {"Mã tài khoản", "Tên đăng nhập", "Password", "Trạng thái"};
		for(TaiKhoan taiKhoan : taiKhoans) {


			NhanVien nhanVien = taiKhoanService.getNhanVienByTaiKhoan(taiKhoan.getMaTaiKhoan());

			tableModel.addRow(new Object[]{
					taiKhoan.getMaTaiKhoan(),
					taiKhoan.getTenDangNhap(),
					"...",
					nhanVien.getMaNhanVien(),
					taiKhoan.getTrangThai(),
			});
		}
	}

	private void loadMaNVcbbox() throws RemoteException {
		List<NhanVien> dsNhanVienChuaCoTaiKhoan = taiKhoanService.getNhanVienChuaCoTaiKhoan();

		for (NhanVien nhanVien : dsNhanVienChuaCoTaiKhoan) {
			cbbMaNV.addItem(nhanVien.getMaNhanVien());
		}
	}

	private void hienThiKetQuaTimKiem(List<TaiKhoan> taiKhoans) throws RemoteException{
		tableModel.setRowCount(0);
		if (taiKhoans != null && !taiKhoans.isEmpty()) {
			for (TaiKhoan taiKhoan : taiKhoans) {
				NhanVien nhanVien = taiKhoanService.getNhanVienByTaiKhoan(taiKhoan.getMaTaiKhoan());
				tableModel.addRow(new Object[]{
						taiKhoan.getMaTaiKhoan(),
						taiKhoan.getTenDangNhap(),
						"...",
						nhanVien.getMaNhanVien(),
						taiKhoan.getTrangThai(),
				});
			}
		}
	}
}