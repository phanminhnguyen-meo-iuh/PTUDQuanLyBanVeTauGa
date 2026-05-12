package com.mycompany.prj1.DAO;

import com.mycompany.prj1.ConnectDB.DB;
import com.mycompany.prj1.entity.HanhKhach;
import com.mycompany.prj1.entity.LoaiHanhKhach;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO quản lý hành khách (CRUD).
 *
 * Để TRÁNH XUNG ĐỘT với DAO_HANHKHACH cũ (đã có trong project),
 * class này có TÊN KHÁC: DAO_HANHKHACH_QL.
 */
public class DAO_HANHKHACH_QL {

    /**
     * Lấy tất cả hành khách + JOIN với LoaiHanhKhach để có tên loại hiển thị.
     */
    public List<HanhKhach> layTatCa() {
        List<HanhKhach> ds = new ArrayList<>();
        String sql =
                "SELECT hk.*, lhk.tenLoai " +
                "FROM HanhKhach hk " +
                "JOIN LoaiHanhKhach lhk ON hk.maLoai = lhk.maLoai " +
                "ORDER BY hk.maHanhKhach DESC";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                ds.add(doHanhKhachTuResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    /**
     * Tìm theo từ khoá: mã, họ tên, số giấy tờ, sđt.
     */
    public List<HanhKhach> tim(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return layTatCa();
        }

        List<HanhKhach> ds = new ArrayList<>();
        String sql =
                "SELECT hk.*, lhk.tenLoai " +
                "FROM HanhKhach hk " +
                "JOIN LoaiHanhKhach lhk ON hk.maLoai = lhk.maLoai " +
                "WHERE hk.maHanhKhach LIKE ? " +
                "   OR hk.hoTenHK LIKE ? " +
                "   OR hk.soGiayTo LIKE ? " +
                "   OR hk.sdt LIKE ? " +
                "ORDER BY hk.maHanhKhach DESC";

        String pattern = "%" + tuKhoa.trim() + "%";
        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            ps.setString(3, pattern);
            ps.setString(4, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(doHanhKhachTuResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public HanhKhach layTheoMa(String maHK) {
        String sql =
                "SELECT hk.*, lhk.tenLoai " +
                "FROM HanhKhach hk " +
                "JOIN LoaiHanhKhach lhk ON hk.maLoai = lhk.maLoai " +
                "WHERE hk.maHanhKhach = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maHK);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return doHanhKhachTuResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sinh mã hành khách mới: HK001, HK002, ...
     */
    public String sinhMaMoi() {
        String sql = "SELECT MAX(maHanhKhach) FROM HanhKhach WHERE maHanhKhach LIKE 'HK%'";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                String max = rs.getString(1);
                if (max == null) return "HK001";
                try {
                    int n = Integer.parseInt(max.substring(2)) + 1;
                    return String.format("HK%03d", n);
                } catch (NumberFormatException e) {
                    return "HK001";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "HK001";
    }

    /**
     * Lấy danh sách loại hành khách (cho dropdown).
     */
    public List<LoaiHanhKhach> layDanhSachLoaiHanhKhach() {
        List<LoaiHanhKhach> ds = new ArrayList<>();
        String sql = "SELECT * FROM LoaiHanhKhach ORDER BY tenLoai";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                LoaiHanhKhach lhk = new LoaiHanhKhach();
                lhk.setMaLoai(rs.getString("maLoai"));
                lhk.setTenLoai(rs.getString("tenLoai"));
                ds.add(lhk);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Kiểm tra số giấy tờ (CCCD/Passport) đã tồn tại.
     */
    public boolean soGiayToDaTonTai(String soGiayTo, String maHKHienTai) {
        if (soGiayTo == null || soGiayTo.trim().isEmpty()) return false;

        String sql;
        if (maHKHienTai == null) {
            sql = "SELECT COUNT(*) FROM HanhKhach WHERE soGiayTo = ?";
        } else {
            sql = "SELECT COUNT(*) FROM HanhKhach WHERE soGiayTo = ? AND maHanhKhach <> ?";
        }

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, soGiayTo);
            if (maHKHienTai != null) ps.setString(2, maHKHienTai);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean them(HanhKhach hk) {
        String sql =
                "INSERT INTO HanhKhach " +
                "(maHanhKhach, hoTenHK, loaiGiayTo, soGiayTo, sdt, ngaySinh, maLoai) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, hk.getMaHanhKhach());
            ps.setString(2, hk.getHoTenHK());
            ps.setString(3, hk.getLoaiGiayTo());

            if (hk.getSoGiayTo() == null || hk.getSoGiayTo().trim().isEmpty()) {
                ps.setNull(4, java.sql.Types.VARCHAR);
            } else {
                ps.setString(4, hk.getSoGiayTo());
            }

            if (hk.getSdt() == null || hk.getSdt().trim().isEmpty()) {
                ps.setNull(5, java.sql.Types.VARCHAR);
            } else {
                ps.setString(5, hk.getSdt());
            }

            ps.setDate(6, java.sql.Date.valueOf(hk.getNgaySinh()));
            ps.setString(7, hk.getLoaiHanhKhach().getMaLoai());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhat(HanhKhach hk) {
        String sql =
                "UPDATE HanhKhach SET " +
                "  hoTenHK = ?, loaiGiayTo = ?, soGiayTo = ?, " +
                "  sdt = ?, ngaySinh = ?, maLoai = ? " +
                "WHERE maHanhKhach = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, hk.getHoTenHK());
            ps.setString(2, hk.getLoaiGiayTo());

            if (hk.getSoGiayTo() == null || hk.getSoGiayTo().trim().isEmpty()) {
                ps.setNull(3, java.sql.Types.VARCHAR);
            } else {
                ps.setString(3, hk.getSoGiayTo());
            }

            if (hk.getSdt() == null || hk.getSdt().trim().isEmpty()) {
                ps.setNull(4, java.sql.Types.VARCHAR);
            } else {
                ps.setString(4, hk.getSdt());
            }

            ps.setDate(5, java.sql.Date.valueOf(hk.getNgaySinh()));
            ps.setString(6, hk.getLoaiHanhKhach().getMaLoai());
            ps.setString(7, hk.getMaHanhKhach());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Đếm số vé liên kết với hành khách.
     */
    public int demSoVeDangLienKet(String maHK) {
        String sql = "SELECT COUNT(*) FROM Ve WHERE hanhKhach = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maHK);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Xoá hành khách.
     * Chỉ cho xoá khi không có vé nào tham chiếu (để tránh lỗi FK).
     */
    public boolean xoa(String maHK) {
        if (demSoVeDangLienKet(maHK) > 0) {
            return false;  // có vé tham chiếu -> không xoá được
        }

        String sql = "DELETE FROM HanhKhach WHERE maHanhKhach = ?";
        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maHK);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private HanhKhach doHanhKhachTuResultSet(ResultSet rs) throws SQLException {
        LoaiHanhKhach lhk = new LoaiHanhKhach();
        lhk.setMaLoai(rs.getString("maLoai"));
        lhk.setTenLoai(rs.getString("tenLoai"));

        HanhKhach hk = new HanhKhach();
        hk.setMaHanhKhach(rs.getString("maHanhKhach"));
        hk.setHoTenHK(rs.getString("hoTenHK"));
        hk.setLoaiGiayTo(rs.getString("loaiGiayTo"));
        hk.setSoGiayTo(rs.getString("soGiayTo"));
        hk.setSdt(rs.getString("sdt"));

        java.sql.Date ns = rs.getDate("ngaySinh");
        if (ns != null) hk.setNgaySinh(ns.toLocalDate());

        hk.setLoaiHanhKhach(lhk);
        return hk;
    }
}