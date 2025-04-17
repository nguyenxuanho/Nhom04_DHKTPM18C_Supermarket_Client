
import gui.TrangChu;

import javax.naming.NamingException;
import javax.swing.*;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) throws NamingException, RemoteException {
        SwingUtilities.invokeLater(() -> {
            try {
                new TrangChu().setVisible(true); // 👈 Mở form đăng nhập trước
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }
}
