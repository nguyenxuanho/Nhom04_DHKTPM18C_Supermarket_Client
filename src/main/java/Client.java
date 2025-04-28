

import gui.panel.Loading;

import javax.naming.NamingException;
import javax.swing.*;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) throws NamingException, RemoteException {
        SwingUtilities.invokeLater(() -> {
            try {
                Loading loading = new Loading();
                loading.setVisible(true); // 👈 Mở form đăng nhập trước
                loading.setLocationRelativeTo(null);
                // Tạo một thread để xử lý tiến trình tải
                new Thread(() -> {
                    try {
                        for (int i = 0; i <= 100; i++) {
                            Thread.sleep(20);
                            loading.updateProgress(i);
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }).start();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }
}
