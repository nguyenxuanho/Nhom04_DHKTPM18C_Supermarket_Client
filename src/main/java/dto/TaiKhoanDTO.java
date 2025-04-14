package dto;

import lombok.NoArgsConstructor;
import model.TaiKhoan;

@NoArgsConstructor
public class TaiKhoanDTO {
    private static TaiKhoan taiKhoan;

    public static TaiKhoan getTaiKhoan() {
        return taiKhoan;
    }

    public static void setTaiKhoan(TaiKhoan tk) {
        taiKhoan = tk;
    }
}
