package com.mycompany.prj1.DAO;

import com.mycompany.prj1.ConnectDB.DB;
import com.mycompany.prj1.entity.Tau;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho Quản lý tàu.
 * Schema: Tau (maTau, soLuongToa, trangThaiTau)
 *
 * Chú ý: maTau do user tự nhập (ví dụ: SE5, TN8). Phải check trùng trước khi insert.
 */
public class DAO_TAU {

    /** Wrapper để hiển thị thêm thông tin (số toa thực tế, tổng sức chứa) */
    public static class TauHienThi {
        public String maTau;
        public int soLuongToaKhaiBao;
        public int soLuongToaThuc;
        public int tongSucChua;
        public String trangThaiTau;
    }

    public List<TauHienThi> layTatCa() {
        List<TauHienThi> ds = new ArrayList<>();
        String sql =
                "SELECT t.maTau, t.soLuongToa, t.trangThaiTau, " +
                "       ISNULL((SELECT COUNT(*) FROM ToaTau tt WHERE tt.tau = t.maTau), 0) AS soToaThuc, " +
                "       ISNULL((SELECT SUM(sucChua) FROM ToaTau tt WHERE tt.tau = t.maTau), 0) AS tongSucChua " +
                "FROM Tau t " +
                "ORDER BY t.maTau";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                TauHienThi th = new TauHienThi();
                th.maTau = rs.getString("maTau");
                th.soLuongToaKhaiBao = rs.getInt("soLuongToa");
                th.soLuongToaThuc = rs.getInt("soToaThuc");
                th.tongSucChua = rs.getInt("tongSucChua");
                th.trangThaiTau = rs.getString("trangThaiTau");
                ds.add(th);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    public List<TauHienThi> tim(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return layTatCa();
        }

        List<TauHienThi> ds = new ArrayList<>();
        String sql =
                "SELECT t.maTau, t.soLuongToa, t.trangThaiTau, " +
                "       ISNULL((SELECT COUNT(*) FROM ToaTau tt WHERE tt.tau = t.maTau), 0) AS soToaThuc, " +
                "       ISNULL((SELECT SUM(sucChua) FROM ToaTau tt WHERE tt.tau = t.maTau), 0) AS tongSucChua " +
                "FROM Tau t " +
                "WHERE t.maTau LIKE ? OR t.trangThaiTau LIKE ? " +
                "ORDER BY t.maTau";

        String pattern = "%" + tuKhoa.trim() + "%";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, pattern);
            ps.setString(2, pattern);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TauHienThi th = new TauHienThi();
                    th.maTau = rs.getString("maTau");
                    th.soLuongToaKhaiBao = rs.getInt("soLuongToa");
                    th.soLuongToaThuc = rs.getInt("soToaThuc");
                    th.tongSucChua = rs.getInt("tongSucChua");
                    th.trangThaiTau = rs.getString("trangThaiTau");
                    ds.add(th);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    public Tau layTheoMa(String maTau) {
        String sql = "SELECT * FROM Tau WHERE maTau = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maTau);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Tau t = new Tau();
                    t.setMaTau(rs.getString("maTau"));
                    t.setSoLuongToa(rs.getInt("soLuongToa"));
                    t.setTrangThaiTau(rs.getString("trangThaiTau"));
                    return t;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Kiểm tra mã tàu đã tồn tại chưa - dùng khi user nhập mã thủ công.
     */
    public boolean maTauDaTonTai(String maTau) {
        String sql = "SELECT COUNT(*) FROM Tau WHERE maTau = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maTau);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean them(Tau t) {
        String sql = "INSERT INTO Tau (maTau, soLuongToa, trangThaiTau) VALUES (?, ?, ?)";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, t.getMaTau());
            ps.setInt(2, t.getSoLuongToa());
            ps.setString(3, t.getTrangThaiTau());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean capNhat(Tau t) {
        String sql = "UPDATE Tau SET soLuongToa = ?, trangThaiTau = ? WHERE maTau = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setInt(1, t.getSoLuongToa());
            ps.setString(2, t.getTrangThaiTau());
            ps.setString(3, t.getMaTau());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật cột soLuongToa của tàu = COUNT(ToaTau).
     * Gọi sau mỗi khi thêm/xoá toa để giữ data nhất quán.
     */
    public boolean dongBoSoLuongToa(String maTau) {
        String sql =
                "UPDATE Tau SET soLuongToa = " +
                "  (SELECT COUNT(*) FROM ToaTau WHERE tau = ?) " +
                "WHERE maTau = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maTau);
            ps.setString(2, maTau);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int demSoChuyenDangDung(String maTau) {
        String sql = "SELECT COUNT(*) FROM ChuyenTau WHERE tau = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maTau);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int demSoToa(String maTau) {
        String sql = "SELECT COUNT(*) FROM ToaTau WHERE tau = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maTau);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * "Xoá mềm" tàu: đổi trạng thái sang "Dừng hoạt động".
     * Không xoá data thật để giữ lại lịch sử chuyến tàu, vé đã bán.
     * Cũng không xoá các toa để có thể khôi phục lại sau.
     */
    public boolean dungHoatDong(String maTau) {
        String sql = "UPDATE Tau SET trangThaiTau = N'Dừng hoạt động' WHERE maTau = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maTau);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Khôi phục tàu đã dừng - chuyển về "Hoạt động".
     */
    public boolean khoiPhuc(String maTau) {
        String sql = "UPDATE Tau SET trangThaiTau = N'Hoạt động' WHERE maTau = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maTau);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}