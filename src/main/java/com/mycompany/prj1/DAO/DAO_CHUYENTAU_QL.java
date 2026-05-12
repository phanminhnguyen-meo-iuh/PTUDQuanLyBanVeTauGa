package com.mycompany.prj1.DAO;

import com.mycompany.prj1.ConnectDB.DB;
import com.mycompany.prj1.entity.ChuyenTau;
import com.mycompany.prj1.entity.Ga;
import com.mycompany.prj1.entity.Tau;
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
 * DAO quản lý chuyến tàu.
 *
 * Tên class: DAO_CHUYENTAU_QL để KHÔNG xung đột với DAO_ChuyenTau cũ
 * (đã có sẵn trong project và dùng cho bán vé).
 *
 * Logic quan trọng - chống tạo trùng:
 *   1 tàu trong 1 NGÀY KHỞI HÀNH chỉ được có 1 chuyến.
 *   Ngày đến không quan tâm (có thể qua đêm).
 */
public class DAO_CHUYENTAU_QL {

    /**
     * Kiểm tra trong NGÀY KHỞI HÀNH này, tàu này đã có chuyến chưa.
     * Trả về true nếu có chuyến trùng (nên chặn user tạo mới).
     *
     * @param maTau         mã tàu
     * @param ngayKhoiHanh  ngày khởi hành (chỉ tính phần ngày, không tính giờ)
     * @param maChuyenLoaiTru mã chuyến được loại trừ (khi đang sửa, không tính chính nó). Truyền null khi tạo mới.
     */
    public boolean coChuyenTrongNgay(String maTau, LocalDate ngayKhoiHanh, String maChuyenLoaiTru) {
        String sql;
        if (maChuyenLoaiTru == null) {
            sql = "SELECT COUNT(*) FROM ChuyenTau " +
                  "WHERE tau = ? " +
                  "  AND CAST(ngayKhoiHanh AS DATE) = ?";
        } else {
            sql = "SELECT COUNT(*) FROM ChuyenTau " +
                  "WHERE tau = ? " +
                  "  AND CAST(ngayKhoiHanh AS DATE) = ? " +
                  "  AND maChuyenTau <> ?";
        }

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maTau);
            ps.setDate(2, java.sql.Date.valueOf(ngayKhoiHanh));
            if (maChuyenLoaiTru != null) {
                ps.setString(3, maChuyenLoaiTru);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Lấy mã chuyến trùng (nếu có) - dùng để hiển thị trong message lỗi.
     */
    public String layMaChuyenTrungTrongNgay(String maTau, LocalDate ngayKhoiHanh, String maChuyenLoaiTru) {
        String sql;
        if (maChuyenLoaiTru == null) {
            sql = "SELECT TOP 1 maChuyenTau FROM ChuyenTau " +
                  "WHERE tau = ? AND CAST(ngayKhoiHanh AS DATE) = ?";
        } else {
            sql = "SELECT TOP 1 maChuyenTau FROM ChuyenTau " +
                  "WHERE tau = ? AND CAST(ngayKhoiHanh AS DATE) = ? AND maChuyenTau <> ?";
        }

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maTau);
            ps.setDate(2, java.sql.Date.valueOf(ngayKhoiHanh));
            if (maChuyenLoaiTru != null) ps.setString(3, maChuyenLoaiTru);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getString(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Sinh mã chuyến tàu mới: CT001, CT002, ...
     */
    public String sinhMaMoi() {
        String sql = "SELECT MAX(maChuyenTau) FROM ChuyenTau WHERE maChuyenTau LIKE 'CT%'";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                String max = rs.getString(1);
                if (max == null) return "CT001";
                try {
                    int n = Integer.parseInt(max.substring(2)) + 1;
                    return String.format("CT%03d", n);
                } catch (NumberFormatException e) {
                    return "CT001";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "CT001";
    }

    /**
     * Lấy tất cả chuyến tàu (có JOIN để có tên ga, tàu).
     */
    public List<ChuyenTau> layTatCa() {
        List<ChuyenTau> ds = new ArrayList<>();
        String sql =
                "SELECT ct.maChuyenTau, ct.tenChuyen, ct.ngayKhoiHanh, ct.ngayDenDuKien, " +
                "       ct.cuLy, ct.trangThaiChuyen, " +
                "       ct.tau AS maTau, t.soLuongToa, t.trangThaiTau, " +
                "       gaDi.maGa AS gaDi_ma, gaDi.tenGa AS gaDi_ten, " +
                "       gaDi.diaChi AS gaDi_diaChi, gaDi.tinhThanh AS gaDi_tinhThanh, " +
                "       gaDi.trangThaiGa AS gaDi_tt, " +
                "       gaDen.maGa AS gaDen_ma, gaDen.tenGa AS gaDen_ten, " +
                "       gaDen.diaChi AS gaDen_diaChi, gaDen.tinhThanh AS gaDen_tinhThanh, " +
                "       gaDen.trangThaiGa AS gaDen_tt " +
                "FROM ChuyenTau ct " +
                "JOIN Ga gaDi  ON ct.gaDi  = gaDi.maGa " +
                "JOIN Ga gaDen ON ct.gaDen = gaDen.maGa " +
                "JOIN Tau t    ON ct.tau   = t.maTau " +
                "ORDER BY ct.ngayKhoiHanh DESC";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                ds.add(doChuyenTuResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Tìm theo từ khoá: mã chuyến, tên chuyến, mã tàu, tên ga.
     */
    public List<ChuyenTau> tim(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return layTatCa();
        }

        List<ChuyenTau> ds = new ArrayList<>();
        String sql =
                "SELECT ct.maChuyenTau, ct.tenChuyen, ct.ngayKhoiHanh, ct.ngayDenDuKien, " +
                "       ct.cuLy, ct.trangThaiChuyen, " +
                "       ct.tau AS maTau, t.soLuongToa, t.trangThaiTau, " +
                "       gaDi.maGa AS gaDi_ma, gaDi.tenGa AS gaDi_ten, " +
                "       gaDi.diaChi AS gaDi_diaChi, gaDi.tinhThanh AS gaDi_tinhThanh, " +
                "       gaDi.trangThaiGa AS gaDi_tt, " +
                "       gaDen.maGa AS gaDen_ma, gaDen.tenGa AS gaDen_ten, " +
                "       gaDen.diaChi AS gaDen_diaChi, gaDen.tinhThanh AS gaDen_tinhThanh, " +
                "       gaDen.trangThaiGa AS gaDen_tt " +
                "FROM ChuyenTau ct " +
                "JOIN Ga gaDi  ON ct.gaDi  = gaDi.maGa " +
                "JOIN Ga gaDen ON ct.gaDen = gaDen.maGa " +
                "JOIN Tau t    ON ct.tau   = t.maTau " +
                "WHERE ct.maChuyenTau LIKE ? " +
                "   OR ct.tenChuyen LIKE ? " +
                "   OR ct.tau LIKE ? " +
                "   OR gaDi.tenGa LIKE ? " +
                "   OR gaDen.tenGa LIKE ? " +
                "ORDER BY ct.ngayKhoiHanh DESC";

        String pattern = "%" + tuKhoa.trim() + "%";
        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            for (int i = 1; i <= 5; i++) ps.setString(i, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(doChuyenTuResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public ChuyenTau layTheoMa(String maChuyen) {
        String sql =
                "SELECT ct.maChuyenTau, ct.tenChuyen, ct.ngayKhoiHanh, ct.ngayDenDuKien, " +
                "       ct.cuLy, ct.trangThaiChuyen, " +
                "       ct.tau AS maTau, t.soLuongToa, t.trangThaiTau, " +
                "       gaDi.maGa AS gaDi_ma, gaDi.tenGa AS gaDi_ten, " +
                "       gaDi.diaChi AS gaDi_diaChi, gaDi.tinhThanh AS gaDi_tinhThanh, " +
                "       gaDi.trangThaiGa AS gaDi_tt, " +
                "       gaDen.maGa AS gaDen_ma, gaDen.tenGa AS gaDen_ten, " +
                "       gaDen.diaChi AS gaDen_diaChi, gaDen.tinhThanh AS gaDen_tinhThanh, " +
                "       gaDen.trangThaiGa AS gaDen_tt " +
                "FROM ChuyenTau ct " +
                "JOIN Ga gaDi  ON ct.gaDi  = gaDi.maGa " +
                "JOIN Ga gaDen ON ct.gaDen = gaDen.maGa " +
                "JOIN Tau t    ON ct.tau   = t.maTau " +
                "WHERE ct.maChuyenTau = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maChuyen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return doChuyenTuResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Lấy danh sách ga đang hoạt động (cho dropdown).
     */
    public List<Ga> layDanhSachGa() {
        List<Ga> ds = new ArrayList<>();
        String sql = "SELECT * FROM Ga WHERE trangThaiGa = 1 ORDER BY tenGa";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Ga g = new Ga();
                g.setMaGa(rs.getString("maGa"));
                g.setTenGa(rs.getString("tenGa"));
                g.setDiaChi(rs.getString("diaChi"));
                g.setTinhThanh(rs.getString("tinhThanh"));
                g.setTrangThaiGa(rs.getBoolean("trangThaiGa"));
                ds.add(g);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Lấy danh sách tàu đang hoạt động (cho dropdown).
     */
    public List<Tau> layDanhSachTauHoatDong() {
        List<Tau> ds = new ArrayList<>();
        String sql = "SELECT * FROM Tau WHERE trangThaiTau <> N'Dừng hoạt động' ORDER BY maTau";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                Tau t = new Tau();
                t.setMaTau(rs.getString("maTau"));
                t.setSoLuongToa(rs.getInt("soLuongToa"));
                t.setTrangThaiTau(rs.getString("trangThaiTau"));
                ds.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    public boolean them(ChuyenTau ct) {
        String sql =
                "INSERT INTO ChuyenTau " +
                "(maChuyenTau, tenChuyen, ngayKhoiHanh, ngayDenDuKien, " +
                " gaDi, gaDen, tau, cuLy, trangThaiChuyen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, ct.getMaChuyen());
            ps.setString(2, ct.getTenChuyen());
            ps.setTimestamp(3, Timestamp.valueOf(ct.getNgayKhoiHanh()));
            ps.setTimestamp(4, Timestamp.valueOf(ct.getNgayDenDuKien()));
            ps.setString(5, ct.getGaDi().getMaGa());
            ps.setString(6, ct.getGaDen().getMaGa());
            ps.setString(7, ct.getTau().getMaTau());
            ps.setInt(8, ct.getCuLy());
            ps.setBoolean(9, ct.isTrangThaiChuyen());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhat(ChuyenTau ct) {
        String sql =
                "UPDATE ChuyenTau SET " +
                "  tenChuyen = ?, ngayKhoiHanh = ?, ngayDenDuKien = ?, " +
                "  gaDi = ?, gaDen = ?, tau = ?, cuLy = ?, trangThaiChuyen = ? " +
                "WHERE maChuyenTau = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, ct.getTenChuyen());
            ps.setTimestamp(2, Timestamp.valueOf(ct.getNgayKhoiHanh()));
            ps.setTimestamp(3, Timestamp.valueOf(ct.getNgayDenDuKien()));
            ps.setString(4, ct.getGaDi().getMaGa());
            ps.setString(5, ct.getGaDen().getMaGa());
            ps.setString(6, ct.getTau().getMaTau());
            ps.setInt(7, ct.getCuLy());
            ps.setBoolean(8, ct.isTrangThaiChuyen());
            ps.setString(9, ct.getMaChuyen());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Đếm số vé liên kết với chuyến.
     */
    public int demSoVeLienKet(String maChuyen) {
        String sql = "SELECT COUNT(*) FROM Ve WHERE chuyenTau = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maChuyen);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * "Xoá mềm": chuyển trangThaiChuyen = 0 (Đã huỷ).
     * Không xoá thật để giữ lịch sử.
     */
    public boolean huyChuyen(String maChuyen) {
        String sql = "UPDATE ChuyenTau SET trangThaiChuyen = 0 WHERE maChuyenTau = ?";
        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maChuyen);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean khoiPhuc(String maChuyen) {
        String sql = "UPDATE ChuyenTau SET trangThaiChuyen = 1 WHERE maChuyenTau = ?";
        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maChuyen);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xoá hẳn (chỉ cho khi không có vé).
     */
    public boolean xoaHan(String maChuyen) {
        if (demSoVeLienKet(maChuyen) > 0) return false;

        String sql = "DELETE FROM ChuyenTau WHERE maChuyenTau = ?";
        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maChuyen);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================================
    // HELPERS
    // ==========================================================

    private ChuyenTau doChuyenTuResultSet(ResultSet rs) throws SQLException {
        Ga gaDi = new Ga();
        gaDi.setMaGa(rs.getString("gaDi_ma"));
        gaDi.setTenGa(rs.getString("gaDi_ten"));
        gaDi.setDiaChi(rs.getString("gaDi_diaChi"));
        gaDi.setTinhThanh(rs.getString("gaDi_tinhThanh"));
        gaDi.setTrangThaiGa(rs.getBoolean("gaDi_tt"));

        Ga gaDen = new Ga();
        gaDen.setMaGa(rs.getString("gaDen_ma"));
        gaDen.setTenGa(rs.getString("gaDen_ten"));
        gaDen.setDiaChi(rs.getString("gaDen_diaChi"));
        gaDen.setTinhThanh(rs.getString("gaDen_tinhThanh"));
        gaDen.setTrangThaiGa(rs.getBoolean("gaDen_tt"));

        Tau tau = new Tau();
        tau.setMaTau(rs.getString("maTau"));
        tau.setSoLuongToa(rs.getInt("soLuongToa"));
        tau.setTrangThaiTau(rs.getString("trangThaiTau"));

        ChuyenTau ct = new ChuyenTau();
        ct.setMaChuyen(rs.getString("maChuyenTau"));
        ct.setTenChuyen(rs.getString("tenChuyen"));
        ct.setNgayKhoiHanh(rs.getTimestamp("ngayKhoiHanh").toLocalDateTime());
        ct.setNgayDenDuKien(rs.getTimestamp("ngayDenDuKien").toLocalDateTime());
        ct.setGaDi(gaDi);
        ct.setGaDen(gaDen);
        ct.setTau(tau);
        ct.setCuLy(rs.getInt("cuLy"));
        ct.setTrangThaiChuyen(rs.getBoolean("trangThaiChuyen"));
        return ct;
    }
}