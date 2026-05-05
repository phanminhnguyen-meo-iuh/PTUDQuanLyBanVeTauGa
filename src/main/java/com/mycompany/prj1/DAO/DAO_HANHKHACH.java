package com.mycompany.prj1.DAO;

import com.mycompany.prj1.ConnectDB.DB;
import com.mycompany.prj1.entity.HanhKhach;
import com.mycompany.prj1.entity.LoaiHanhKhach;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DAO_HANHKHACH {

    public HanhKhach timHanhKhachTheoSdt(String sdt) {
        String sql =
            "SELECT hk.maHanhKhach, hk.hoTenHK, hk.loaiGiayTo, hk.soGiayTo, " +
            "       hk.sdt, hk.ngaySinh, hk.maLoai, lhk.tenLoai " +
            "FROM HanhKhach hk " +
            "LEFT JOIN LoaiHanhKhach lhk ON hk.maLoai = lhk.maLoai " +
            "WHERE hk.sdt = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, sdt);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return taoHanhKhachTuResultSet(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Lỗi tìm hành khách theo SĐT:\n" + e.getMessage()
            );
        }

        return null;
    }

    public HanhKhach timHanhKhachTheoGiayTo(String loaiGiayTo, String soGiayTo) {
        String sql =
            "SELECT hk.maHanhKhach, hk.hoTenHK, hk.loaiGiayTo, hk.soGiayTo, " +
            "       hk.sdt, hk.ngaySinh, hk.maLoai, lhk.tenLoai " +
            "FROM HanhKhach hk " +
            "LEFT JOIN LoaiHanhKhach lhk ON hk.maLoai = lhk.maLoai " +
            "WHERE hk.loaiGiayTo = ? AND hk.soGiayTo = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, loaiGiayTo);
            ps.setString(2, soGiayTo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return taoHanhKhachTuResultSet(rs);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

            javax.swing.JOptionPane.showMessageDialog(
                    null,
                    "Lỗi tìm hành khách theo giấy tờ:\n" + e.getMessage()
            );
        }

        return null;
    }

    public String themHoacLayMaHanhKhach(
        String hoTen,
        java.time.LocalDate ngaySinh,
        String loaiGiayTo,
        String soGiayTo,
        String sdt,
        String maLoai
) {
    // CCCD/PASSPORT mới là định danh chính của hành khách.
    // KHÔNG dùng SĐT để quyết định là cùng một hành khách,
    // vì nhiều hành khách có thể dùng chung một SĐT liên hệ.

    if (loaiGiayTo != null
            && soGiayTo != null
            && !soGiayTo.trim().isEmpty()
            && !loaiGiayTo.equalsIgnoreCase("KHONG_CO")) {

        HanhKhach hkTheoGiayTo = timHanhKhachTheoGiayTo(
                loaiGiayTo.trim(),
                soGiayTo.trim()
        );

        if (hkTheoGiayTo != null && hkTheoGiayTo.getMaHanhKhach() != null) {
    if (thongTinKhacNhau(hkTheoGiayTo, hoTen, ngaySinh, sdt, maLoai)) {
        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Số giấy tờ " + soGiayTo + " đã tồn tại trong hệ thống,\n"
                + "nhưng họ tên hoặc ngày sinh không khớp.\n\n"
                + "Vui lòng kiểm tra lại CCCD/PASSPORT."
        );

        return null;
    }

    // Nếu đúng là hành khách cũ nhưng đổi SĐT,
    // cập nhật lại SĐT mới cho hành khách đó.
    capNhatSdtNeuThayDoi(
            hkTheoGiayTo.getMaHanhKhach(),
            sdt
    );

    return hkTheoGiayTo.getMaHanhKhach();
}
    }

    String sql = """
        INSERT INTO HanhKhach
        (
            hoTenHK,
            loaiGiayTo,
            soGiayTo,
            sdt,
            ngaySinh,
            maLoai
        )
        OUTPUT INSERTED.maHanhKhach
        VALUES (?, ?, ?, ?, ?, ?)
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, hoTen);

        if (loaiGiayTo == null || loaiGiayTo.trim().isEmpty()) {
            ps.setString(2, "KHONG_CO");
        } else {
            ps.setString(2, loaiGiayTo.trim());
        }

        if (soGiayTo == null || soGiayTo.trim().isEmpty()
                || "KHONG_CO".equalsIgnoreCase(loaiGiayTo)) {
            ps.setNull(3, java.sql.Types.NVARCHAR);
        } else {
            ps.setString(3, soGiayTo.trim());
        }

        if (sdt == null || sdt.trim().isEmpty()) {
            ps.setNull(4, java.sql.Types.VARCHAR);
        } else {
            ps.setString(4, sdt.trim());
        }

        if (ngaySinh == null) {
            ps.setNull(5, java.sql.Types.DATE);
        } else {
            ps.setDate(5, java.sql.Date.valueOf(ngaySinh));
        }

        ps.setString(6, maLoai);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getString("maHanhKhach");
            }
        }

    } catch (Exception e) {
        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Lỗi lưu hành khách:\n" + e.getMessage()
        );
    }

    return null;
}

    private HanhKhach taoHanhKhachTuResultSet(ResultSet rs) throws Exception {
        HanhKhach hk = new HanhKhach();

        hk.setMaHanhKhach(rs.getString("maHanhKhach"));
        hk.setHoTenHK(rs.getString("hoTenHK"));
        hk.setLoaiGiayTo(rs.getString("loaiGiayTo"));
        hk.setSoGiayTo(rs.getString("soGiayTo"));
        hk.setSdt(rs.getString("sdt"));

        java.sql.Date ns = rs.getDate("ngaySinh");
        if (ns != null) {
            hk.setNgaySinh(ns.toLocalDate());
        }

        LoaiHanhKhach loai = new LoaiHanhKhach();
        loai.setMaLoai(rs.getString("maLoai"));
        loai.setTenLoai(rs.getString("tenLoai"));
        hk.setLoaiHanhKhach(loai);

        return hk;
    }
    private String chuanHoa(String s) {
    return s == null ? "" : s.trim();
}

private boolean thongTinKhacNhau(
        HanhKhach hkCu,
        String hoTenMoi,
        java.time.LocalDate ngaySinhMoi,
        String sdtMoi,
        String maLoaiMoi
) {
    if (hkCu == null) {
        return false;
    }

    String tenCu = chuanHoa(hkCu.getHoTenHK());
    String tenMoi = chuanHoa(hoTenMoi);

    if (!tenCu.equalsIgnoreCase(tenMoi)) {
        return true;
    }

    java.time.LocalDate ngaySinhCu = hkCu.getNgaySinh();

    if (ngaySinhCu == null && ngaySinhMoi != null) {
        return true;
    }

    if (ngaySinhCu != null && ngaySinhMoi == null) {
        return true;
    }

    if (ngaySinhCu != null && ngaySinhMoi != null && !ngaySinhCu.equals(ngaySinhMoi)) {
        return true;
    }

    // Không so sánh SĐT ở đây.
    // SĐT là thông tin liên hệ, khách có thể đổi số điện thoại.

    return false;
}
    private void capNhatSdtNeuThayDoi(String maHanhKhach, String sdtMoi) {
    if (maHanhKhach == null || maHanhKhach.trim().isEmpty()) {
        return;
    }

    if (sdtMoi == null || sdtMoi.trim().isEmpty()) {
        return;
    }

    String sql = """
        UPDATE HanhKhach
        SET sdt = ?
        WHERE maHanhKhach = ?
          AND (sdt IS NULL OR sdt <> ?)
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, sdtMoi.trim());
        ps.setString(2, maHanhKhach.trim());
        ps.setString(3, sdtMoi.trim());

        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Lỗi cập nhật số điện thoại hành khách:\n" + e.getMessage()
        );
    }
}
    
}
    


