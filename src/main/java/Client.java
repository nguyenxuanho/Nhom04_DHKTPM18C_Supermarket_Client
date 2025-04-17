
import dao.KhachHangDAO;
import gui.LoginForm;
import gui.TrangChu;
import io.github.cdimascio.dotenv.Dotenv;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) throws NamingException, RemoteException {
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginForm().setVisible(true); // 👈 Mở form đăng nhập trước
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }
}
