package gui.panel;

import service.*;
import javax.naming.Context;
import javax.naming.InitialContext;

public class RmiServiceLocator {
    private static String hostname = "XuanHo"; // Hoặc hostname cụ thể
    private static final String RMI_URL = "rmi://" + hostname + ":9090/";
    private static Context context;

    // Các biến static để lưu trữ dịch vụ
    public static ChiTietHoaDonService chiTietHoaDonService;
    public static DanhMucSanPhamService danhMucSanPhamService;
    public static HoaDonService hoaDonService;
    public static KhachHangService khachHangService;
    public static NhanVienService nhanVienService;
    public static SanPhamService sanPhamService;
    public static TaiKhoanService taiKhoanService;
    public static KhuyenMaiService khuyenMaiService;
    public static ThuocTinhSanPhamService thuocTinhSanPhamService;

    static {
        try {
            // Khởi tạo context
            context = new InitialContext();
            // Khởi tạo tất cả các dịch vụ một lần
            danhMucSanPhamService = lookupService("danhMucSanPhamService", DanhMucSanPhamService.class);
            hoaDonService = lookupService("hoaDonService", HoaDonService.class);
            khachHangService = lookupService("khachHangService", KhachHangService.class);
            nhanVienService = lookupService("nhanVienService", NhanVienService.class);
            sanPhamService = lookupService("sanPhamService", SanPhamService.class);
            taiKhoanService = lookupService("taiKhoanService", TaiKhoanService.class);
            khuyenMaiService = lookupService("khuyenMaiService", KhuyenMaiService.class);
            thuocTinhSanPhamService = lookupService("thuocTinhSanPhamService", ThuocTinhSanPhamService.class);
            chiTietHoaDonService = lookupService("chiTietHoaDonService", ChiTietHoaDonService.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Các phương thức static để truy cập dịch vụ
    public static ChiTietHoaDonService getChiTietHoaDonService() {
        return chiTietHoaDonService;
    }

    public static DanhMucSanPhamService getDanhMucSanPhamService() {
        return danhMucSanPhamService;
    }

    public static HoaDonService getHoaDonService() {
        return hoaDonService;
    }

    public static KhachHangService getKhachHangService() {
        return khachHangService;
    }

    public static NhanVienService getNhanVienService() {
        return nhanVienService;
    }

    public static SanPhamService getSanPhamService() {
        return sanPhamService;
    }

    public static TaiKhoanService getTaiKhoanService() {
        return taiKhoanService;
    }

    public static KhuyenMaiService getKhuyenMaiService() {
        return khuyenMaiService;
    }

    public static ThuocTinhSanPhamService getThuocTinhSanPhamService() {
        return thuocTinhSanPhamService;
    }

    @SuppressWarnings("unchecked")
    private static <T> T lookupService(String serviceName, Class<T> clazz) {
        try {
            System.out.println("Connect to Class" + serviceName);
            return (T) context.lookup(RMI_URL + serviceName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}