package com.mycompany.prj1.DAO;

import com.mycompany.prj1.ConnectDB.DB;
import com.mycompany.prj1.entity.LoaiGhe;
import com.mycompany.prj1.entity.ToaTau;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO_TOATAU {

    // ==========================================================
    // CÁC HÀM CŨ - GIỮ NGUYÊN ĐỂ KHÔNG PHÁ CODE KHÁC
    // ==========================================================

    public int laySoLuongToaTheoTau(String maTau) {
        int soLuong = 0;
        String sql = "SELECT COUNT(*) AS soLuongToa FROM ToaTau WHERE tau = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maTau);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    soLuong = rs.getInt("soLuongToa");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return soLuong;
    }

    public List<ToaTau> layDanhSachToaTheoTau(String maTau) {
        List<ToaTau> ds = new ArrayList<>();

        String sql =
            "SELECT tt.maToa, tt.tenToa, tt.sucChua, tt.tau, " +
            "       lg.maLoaiGhe, lg.tenLoaiGhe, lg.heSoLoaiGhe, lg.trangThai " +
            "FROM ToaTau tt " +
            "JOIN LoaiGhe lg ON tt.loaiGhe = lg.maLoaiGhe " +
            "WHERE tt.tau = ? " +
            "ORDER BY tt.tenToa";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maTau);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(doToaTuResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    // ==========================================================
    // CÁC HÀM MỚI - CRUD TOA TÀU
    // ==========================================================

    /**
     * Lấy danh sách tất cả loại ghế (đang hoạt động) cho dropdown.
     */
    public List<LoaiGhe> layDanhSachLoaiGhe() {
        List<LoaiGhe> ds = new ArrayList<>();
        String sql = "SELECT * FROM LoaiGhe WHERE trangThai = 1 ORDER BY tenLoaiGhe";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                LoaiGhe lg = new LoaiGhe();
                lg.setMaLoaiGhe(rs.getString("maLoaiGhe"));
                lg.setTenLoaiGhe(rs.getString("tenLoaiGhe"));
                lg.setHeSoLoaiGhe(rs.getDouble("heSoLoaiGhe"));
                lg.setTrangThai(rs.getBoolean("trangThai"));
                ds.add(lg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    /**
     * Sinh mã toa tự động dạng <maTau>_T<số>.
     * Ví dụ: tàu SE5 đã có 3 toa → toa mới sẽ là SE5_T4.
     * Nếu trùng (do từng xoá toa giữa) → tăng tiếp đến khi không trùng.
     */
    public String sinhMaToa(String maTau) {
        int n = laySoLuongToaTheoTau(maTau) + 1;
        String maToa;
        do {
            maToa = maTau + "_T" + n;
            n++;
        } while (maToaDaTonTai(maToa));
        return maToa;
    }

    /**
     * Đề xuất tên toa tiếp theo: "Toa <số>".
     */
    public String deXuatTenToa(String maTau) {
        int n = laySoLuongToaTheoTau(maTau) + 1;
        return "Toa " + n;
    }

    public boolean maToaDaTonTai(String maToa) {
        String sql = "SELECT COUNT(*) FROM ToaTau WHERE maToa = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maToa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Thêm 1 toa cho tàu.
     */
    public boolean themToa(String maToa, String tenToa, int sucChua,
                           String maLoaiGhe, String maTau) {
        String sql = "INSERT INTO ToaTau (maToa, tenToa, sucChua, loaiGhe, tau) " +
                     "VALUES (?, ?, ?, ?, ?)";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maToa);
            ps.setString(2, tenToa);
            ps.setInt(3, sucChua);
            ps.setString(4, maLoaiGhe);
            ps.setString(5, maTau);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xoá 1 toa.
     * KHÔNG cho xoá nếu toa đó có ghế đang được bán/giữ chỗ.
     * (Hiện schema chưa có FK ToaTau → Ghe rõ ràng, nên check qua chuyến tàu.)
     */
    public boolean xoaToa(String maToa) {
        String sql = "DELETE FROM ToaTau WHERE maToa = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maToa);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Đếm tổng sức chứa của 1 tàu.
     */
    public int demTongSucChua(String maTau) {
        String sql = "SELECT ISNULL(SUM(sucChua), 0) FROM ToaTau WHERE tau = ?";

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

    // ==========================================================
    // HELPERS
    // ==========================================================

    private ToaTau doToaTuResultSet(ResultSet rs) throws SQLException {
        LoaiGhe loaiGhe = new LoaiGhe();
        loaiGhe.setMaLoaiGhe(rs.getString("maLoaiGhe"));
        loaiGhe.setTenLoaiGhe(rs.getString("tenLoaiGhe"));
        loaiGhe.setHeSoLoaiGhe(rs.getDouble("heSoLoaiGhe"));
        loaiGhe.setTrangThai(rs.getBoolean("trangThai"));

        ToaTau toa = new ToaTau();
        toa.setMaToa(rs.getString("maToa"));
        toa.setTenToa(rs.getString("tenToa"));
        toa.setSucChua(rs.getInt("sucChua"));
        toa.setLoaiGhe(loaiGhe);
        return toa;
    }
}