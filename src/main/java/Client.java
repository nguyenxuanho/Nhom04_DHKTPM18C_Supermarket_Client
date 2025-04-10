import InterF.KhachHangDAOInterface;
import InterF.SanPhamDAOInterface;
import dao.KhachHangDAO;
import io.github.cdimascio.dotenv.Dotenv;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) throws NamingException, RemoteException {
        Dotenv dotenv = Dotenv.load();
        String drivername = dotenv.get("DRIVER_NAME");
        Context context = new InitialContext();
        KhachHangDAOInterface khachHangDAO = (KhachHangDAOInterface)context.lookup("rmi://" + drivername + ":9090/khachHangDAO");
        SanPhamDAOInterface sanPhamDAO = (SanPhamDAOInterface)context.lookup("rmi://" + drivername + ":9090/sanPhamDAO");
        sanPhamDAO.getList().forEach(sanPham -> {
            System.out.println(sanPham);
        });

    }
}
