import dao.KhachHangDAO;
import dao.KhachHangDAOInterface;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;

public class Client {
    public static void main(String[] args) throws NamingException, RemoteException {
        Context context = new InitialContext();
        KhachHangDAOInterface khachHangDAO = (KhachHangDAOInterface)context.lookup("rmi://LAPTOP-MB2815MQ:9090/khachHangDAO");
        System.out.println(khachHangDAO.findAll());
    }
}
