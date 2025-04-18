package dto;

import model.HoaDon;
import model.TaiKhoan;

public class HoaDonDTO {
    private static HoaDon hoaDon;

    public static HoaDon getHoaDon() {
        return hoaDon;
    }

    public static void setHoaDon(HoaDon hd) {
        hoaDon = hd;
    }
}
