import InterF.KhachHangDAOInterface;
import InterF.SanPhamDAOInterface;
import dao.KhachHangDAO;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) throws NamingException, RemoteException {
        Context context = new InitialContext();
        KhachHangDAOInterface khachHangDAO = (KhachHangDAOInterface)context.lookup("rmi://LAPTOP-MB2815MQ:9090/khachHangDAO");
        SanPhamDAOInterface sanPhamDAO = (SanPhamDAOInterface)context.lookup("rmi://LAPTOP-MB2815MQ:9090/sanPhamDAO");
        sanPhamDAO.getList().forEach(sanPham -> {
            System.out.println(sanPham);
        });

    }
}
