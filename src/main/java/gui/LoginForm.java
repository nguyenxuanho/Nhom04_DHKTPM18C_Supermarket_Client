package gui;

import dto.TaiKhoanDTO;
import io.github.cdimascio.dotenv.Dotenv;
import model.TaiKhoan;
import service.TaiKhoanService;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginForm extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblError;
    Dotenv dotenv = Dotenv.load();

    String drivername = dotenv.get("DRIVER_NAME");
    private final Context context = new InitialContext();
    private final TaiKhoanService taiKhoanService = (TaiKhoanService) context.lookup("rmi://" + drivername + ":9090/taiKhoanService");


    public LoginForm() throws NamingException {
        setTitle("Đăng nhập");
        setSize(700, 400); // Giao diện lớn hơn để chứa ảnh
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ==== PANEL TRÁI: ẢNH ====
        JLabel lblImage = new JLabel();
        lblImage.setIcon(new ImageIcon(new ImageIcon(getClass().getResource("/image/sieuthi.jpg"))
                .getImage().getScaledInstance(300, 400, Image.SCALE_SMOOTH)));
        add(lblImage, BorderLayout.WEST);

        // ==== PANEL PHẢI: FORM LOGIN ====
        JPanel panelMain = new JPanel();
        panelMain.setLayout(new GridBagLayout());
        panelMain.setBackground(new Color(240, 248, 255));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelMain.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        panelMain.add(new JLabel("Tên đăng nhập:"), gbc);

        txtUsername = new JTextField();
        txtUsername.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panelMain.add(txtUsername, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        panelMain.add(new JLabel("Mật khẩu:"), gbc);

        txtPassword = new JPasswordField();
        txtPassword.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        panelMain.add(txtPassword, gbc);

        // Error label
        lblError = new JLabel("");
        lblError.setForeground(Color.RED);
        lblError.setFont(new Font("Arial", Font.ITALIC, 12));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panelMain.add(lblError, gbc);

        // Button login
        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setPreferredSize(new Dimension(200, 40));
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setBackground(new Color(30, 144, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy = 4;
        panelMain.add(btnLogin, gbc);

        add(panelMain, BorderLayout.CENTER);

        // ===== XỬ LÝ ĐĂNG NHẬP =====
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText().trim();
                String password = new String(txtPassword.getPassword()).trim();

                try {
                    TaiKhoan taiKhoan = taiKhoanService.verifyTaiKhoan(username, password);
                    if(taiKhoan != null){
                        JOptionPane.showMessageDialog(LoginForm.this, "Đăng nhập thành công!");
                        dispose();

                        TaiKhoanDTO.setTaiKhoan(taiKhoan);

//                        Open giao diện trang chủ
                        new TrangChu().setVisible(true);

                    } else {
                        lblError.setText("Tên đăng nhập hoặc mật khẩu không đúng.");
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
