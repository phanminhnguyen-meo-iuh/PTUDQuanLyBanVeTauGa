package com.mycompany.prj1.DAO;

import com.mycompany.prj1.ConnectDB.DB;
import com.mycompany.prj1.entity.KhuyenMai;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho Quản lý khuyến mãi.
 * Schema: KhuyenMai (maKhuyenMai, tenKhuyenMai, phanTramGiam, thoiGianBatDau, thoiGianKetThuc)
 */
public class DAO_KHUYENMAI {

    /**
     * Lấy tất cả khuyến mãi.
     */
    public List<KhuyenMai> layTatCa() {
        List<KhuyenMai> ds = new ArrayList<>();
        String sql = "SELECT * FROM KhuyenMai ORDER BY thoiGianBatDau DESC";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                ds.add(doKhuyenMaiTuResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    /**
     * Tìm khuyến mãi theo từ khóa tự do.
     * Match: mã KM, tên KM
     */
    public List<KhuyenMai> tim(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return layTatCa();
        }

        List<KhuyenMai> ds = new ArrayList<>();
        String sql =
                "SELECT * FROM KhuyenMai " +
                "WHERE maKhuyenMai LIKE ? OR tenKhuyenMai LIKE ? " +
                "ORDER BY thoiGianBatDau DESC";

        String pattern = "%" + tuKhoa.trim() + "%";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(doKhuyenMaiTuResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    /**
     * Sinh mã khuyến mãi mới: KM001, KM002, ...
     */
    public String sinhMaMoi() {
        String sql = "SELECT MAX(maKhuyenMai) AS maxMa FROM KhuyenMai WHERE maKhuyenMai LIKE 'KM%'";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                String maxMa = rs.getString("maxMa");
                if (maxMa == null) return "KM001";
                try {
                    int so = Integer.parseInt(maxMa.substring(2)) + 1;
                    return String.format("KM%03d", so);
                } catch (NumberFormatException e) {
                    return "KM001";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "KM001";
    }

    /**
     * Lấy 1 khuyến mãi theo mã.
     */
    public KhuyenMai layTheoMa(String maKM) {
        String sql = "SELECT * FROM KhuyenMai WHERE maKhuyenMai = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maKM);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return doKhuyenMaiTuResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean them(KhuyenMai km) {
        String sql =
                "INSERT INTO KhuyenMai " +
                "(maKhuyenMai, tenKhuyenMai, phanTramGiam, thoiGianBatDau, thoiGianKetThuc) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, km.getMaKhuyenMai());
            ps.setString(2, km.getTenKhuyenMai());
            ps.setDouble(3, km.getPhanTramGiam());
            ps.setTimestamp(4, Timestamp.valueOf(km.getThoiGianBatDau().atStartOfDay()));
            ps.setTimestamp(5, Timestamp.valueOf(km.getThoiGianKetThuc().atTime(23, 59, 59)));
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhat(KhuyenMai km) {
        String sql =
                "UPDATE KhuyenMai SET " +
                "  tenKhuyenMai = ?, " +
                "  phanTramGiam = ?, " +
                "  thoiGianBatDau = ?, " +
                "  thoiGianKetThuc = ? " +
                "WHERE maKhuyenMai = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, km.getTenKhuyenMai());
            ps.setDouble(2, km.getPhanTramGiam());
            ps.setTimestamp(3, Timestamp.valueOf(km.getThoiGianBatDau().atStartOfDay()));
            ps.setTimestamp(4, Timestamp.valueOf(km.getThoiGianKetThuc().atTime(23, 59, 59)));
            ps.setString(5, km.getMaKhuyenMai());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xoá khuyến mãi.
     * Trước khi xoá, kiểm tra xem có vé nào đang dùng KM này không.
     * Nếu có vé tham chiếu → từ chối xoá (vì sẽ vi phạm FK).
     */
    public boolean xoa(String maKM) {
        // Check FK trước
        String sqlCheck = "SELECT COUNT(*) FROM Ve WHERE khuyenMai = ?";
        try (
            Connection con = DB.getConnection();
            PreparedStatement psCheck = con.prepareStatement(sqlCheck)
        ) {
            psCheck.setString(1, maKM);
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // đang được vé tham chiếu, không xoá được
                }
            }

            // Tiến hành xoá
            String sqlDelete = "DELETE FROM KhuyenMai WHERE maKhuyenMai = ?";
            try (PreparedStatement psDelete = con.prepareStatement(sqlDelete)) {
                psDelete.setString(1, maKM);
                return psDelete.executeUpdate() > 0;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Đếm số vé đang dùng khuyến mãi này.
     */
    public int demSoVeDangDung(String maKM) {
        String sql = "SELECT COUNT(*) FROM Ve WHERE khuyenMai = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maKM);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    private KhuyenMai doKhuyenMaiTuResultSet(ResultSet rs) throws SQLException {
        KhuyenMai km = new KhuyenMai();
        km.setMaKhuyenMai(rs.getString("maKhuyenMai"));
        km.setTenKhuyenMai(rs.getString("tenKhuyenMai"));
        km.setPhanTramGiam(rs.getDouble("phanTramGiam"));

        Timestamp tsBD = rs.getTimestamp("thoiGianBatDau");
        if (tsBD != null) km.setThoiGianBatDau(tsBD.toLocalDateTime().toLocalDate());

        Timestamp tsKT = rs.getTimestamp("thoiGianKetThuc");
        if (tsKT != null) km.setThoiGianKetThuc(tsKT.toLocalDateTime().toLocalDate());

        return km;
    }
}