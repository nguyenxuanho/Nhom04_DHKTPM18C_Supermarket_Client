package gui.panel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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

	public PanelTaiKhoan() {
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

		// Thêm dữ liệu mẫu
		addSampleData();
	}

	private JPanel createInfoPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		Border border = BorderFactory.createLineBorder(new Color(33, 150, 243), 2);
		TitledBorder titledBorder = BorderFactory.createTitledBorder(border,
				"Thông tin tài khoản",
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
		jLableMaTK = new JLabel("Mã TK:") ;
		jLableMaTK.setPreferredSize(new Dimension(90, 25));
		txtMaTK = new JTextField(20);
		txtMaTK.setEditable(false);
		jLableDangNhap = new JLabel("Tên đăng nhập:") ;
		jLableDangNhap.setPreferredSize(new Dimension(90, 25));
		txtTenDangNhap = new JTextField(20);

		box1.add(Box.createHorizontalStrut(50));
		box1.add(jLableMaTK);
		box1.add(txtMaTK);
		box1.add(Box.createHorizontalStrut(30));
		box1.add(jLableDangNhap);
		box1.add(txtTenDangNhap);
		box1.add(Box.createHorizontalStrut(50));


		Box box2 = Box.createHorizontalBox();
		JLabel jLabelPwd = new JLabel("Mật khẩu:");
		jLabelPwd.setPreferredSize(new Dimension(90, 25));
		txtPassword = new JPasswordField();
		JLabel jLabelTrangThai = new JLabel("Trạng thái:");
		jLabelTrangThai.setPreferredSize(new Dimension(90, 25));
		cboTrangThai = new JComboBox<>(new String[]{"Đang hoạt động", "Không còn hoạt đông"});
		cboTrangThai.setPreferredSize(new Dimension(445, 25));

		box2.add(Box.createHorizontalStrut(50));
		box2.add(jLabelPwd);
		box2.add(txtPassword);
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

		String[] columns = {"Mã tài khoản", "Tên đăng nhập", "Password", "Trạng thái"};
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
				displaySelectedTaiKhoan();
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		panel.add(scrollPane, BorderLayout.CENTER);

		return panel;
	}

	private void addButtonListeners() {
		btnThem.addActionListener(e -> addEmployee());
		btnXoa.addActionListener(e -> deleteTaiKhoan());
		btnSua.addActionListener(e -> updateTaiKhoan());
		btnReset.addActionListener(e -> resetForm());
	}

	private void addEmployee() {
		// Kiểm tra dữ liệu đầu vào
		if (validateInput()) {
			String[] rowData = {
					generateEmployeeId(),
					txtTenNV.getText(),
					txtNgaySinh.getText(),
					txtSDT.getText(),
					txtDiaChi.getText(),
					txtSoDinhDanh.getText(),
					cboGioiTinh.getSelectedItem().toString(),
					cboChucVu.getSelectedItem().toString()
			};
			tableModel.addRow(rowData);
			resetForm();
			JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
		}
	}

	private void deleteTaiKhoan() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow >= 0) {
			tableModel.removeRow(selectedRow);
			resetForm();
			JOptionPane.showMessageDialog(this, "Xóa tài khoản thành công!");
		} else {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void updateTaiKhoan() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow >= 0) {
			if (validateInput()) {
				tableModel.setValueAt(txtTenDangNhap.getText(), selectedRow, 1);
				tableModel.setValueAt(txtPassword.getPassword(), selectedRow, 2);
				tableModel.setValueAt(cboTrangThai.getSelectedItem().toString(), selectedRow, 3);
				resetForm();
				JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
			}
		} else {
			JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần sửa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
		}
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