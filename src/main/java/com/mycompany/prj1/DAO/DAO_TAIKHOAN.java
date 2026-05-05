package com.mycompany.prj1.DAO;

import com.mycompany.prj1.ConnectDB.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;

public class DAO_TAIKHOAN {

    /**
     * DTO đơn giản để truyền dữ liệu tài khoản giữa GUI và DAO.
     * Đặt tĩnh ở đây cho gọn, không cần entity riêng.
     */
    public static class TaiKhoanInfo {
        public String maTaiKhoan;
        public String maNhanVien;
        public String matKhau;
        public String vaiTro;
        public boolean trangThaiTK;
        public boolean dangDangNhap;

        // Bonus: ghép sẵn họ tên nhân viên cho GUI hiển thị
        public String hoTenNhanVien;
    }

    // ==================================================================
    // CÁC HÀM CŨ (GIỮ LẠI ĐỂ KHÔNG PHÁ CODE KHÁC)
    // ==================================================================

    public void napDuLieuLenBang(DefaultTableModel model) {
        String sql = "SELECT MaTaiKhoan, MaNhanVien, MatKhau, TrangThaiTK, VaiTro FROM TaiKhoan";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (con == null) {
                System.out.println("Không kết nối được CSDL.");
                return;
            }

            model.setRowCount(0);

            while (rs.next()) {
                model.addRow(new Object[]{
                    false,
                    rs.getString("MaTaiKhoan"),
                    rs.getString("MaNhanVien"),
                    rs.getString("MatKhau"),
                    rs.getString("VaiTro"),
                    rs.getBoolean("TrangThaiTK"),
                    ""
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean kiemTraMatKhauHienTai(String maNhanVien, String matKhauCu) {
        String sql = "SELECT MatKhau FROM TaiKhoan WHERE MaNhanVien = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return matKhauCu.equals(rs.getString("MatKhau"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean capNhatMatKhau(String maNhanVien, String matKhauMoi) {
        String sql = "UPDATE TaiKhoan SET MatKhau = ? WHERE MaNhanVien = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, matKhauMoi);
            ps.setString(2, maNhanVien);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean dangDangNhap(String maNhanVien) {
        String sql = "SELECT DangDangNhap FROM TaiKhoan WHERE MaNhanVien = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getBoolean("DangDangNhap");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean capNhatTrangThaiDangNhap(String maNhanVien, boolean trangThai) {
        String sql = "UPDATE TaiKhoan SET DangDangNhap = ? WHERE MaNhanVien = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setBoolean(1, trangThai);
            ps.setString(2, maNhanVien);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // ==================================================================
    // CÁC HÀM MỚI: CRUD QUẢN LÝ TÀI KHOẢN
    // ==================================================================

    /**
     * Lấy toàn bộ tài khoản, JOIN với NhanVien để có thêm họ tên hiển thị.
     */
    public List<TaiKhoanInfo> layTatCaTaiKhoan() {
        List<TaiKhoanInfo> ds = new ArrayList<>();
        String sql =
                "SELECT tk.maTaiKhoan, tk.maNhanVien, tk.matKhau, tk.VaiTro, " +
                "       tk.trangThaiTK, tk.DangDangNhap, nv.hoTenNV " +
                "FROM TaiKhoan tk " +
                "LEFT JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien " +
                "ORDER BY tk.maTaiKhoan";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                ds.add(doTaiKhoanTuResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    /**
     * Tìm tài khoản theo từ khoá tự do.
     * Match theo: mã TK, mã NV, họ tên NV, vai trò.
     */
    public List<TaiKhoanInfo> timTaiKhoan(String tuKhoa) {
        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return layTatCaTaiKhoan();
        }

        List<TaiKhoanInfo> ds = new ArrayList<>();
        String sql =
                "SELECT tk.maTaiKhoan, tk.maNhanVien, tk.matKhau, tk.VaiTro, " +
                "       tk.trangThaiTK, tk.DangDangNhap, nv.hoTenNV " +
                "FROM TaiKhoan tk " +
                "LEFT JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien " +
                "WHERE tk.maTaiKhoan LIKE ? " +
                "   OR tk.maNhanVien LIKE ? " +
                "   OR nv.hoTenNV LIKE ? " +
                "   OR tk.VaiTro LIKE ? " +
                "ORDER BY tk.maTaiKhoan";

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
                    ds.add(doTaiKhoanTuResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    /**
     * Sinh mã tài khoản mới: TK001, TK002, ...
     */
    public String sinhMaTaiKhoanMoi() {
        String sql = "SELECT MAX(maTaiKhoan) AS maxMa FROM TaiKhoan WHERE maTaiKhoan LIKE 'TK%'";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                String maxMa = rs.getString("maxMa");
                if (maxMa == null) return "TK001";
                try {
                    int so = Integer.parseInt(maxMa.substring(2)) + 1;
                    return String.format("TK%03d", so);
                } catch (NumberFormatException e) {
                    return "TK001";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "TK001";
    }

    /**
     * Kiểm tra nhân viên đã có tài khoản chưa.
     * Mỗi nhân viên chỉ được có 1 tài khoản (UQ_TaiKhoan_MaNhanVien).
     */
    public boolean nhanVienDaCoTaiKhoan(String maNhanVien) {
        String sql = "SELECT COUNT(*) FROM TaiKhoan WHERE maNhanVien = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maNhanVien);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Lấy danh sách nhân viên CHƯA có tài khoản (dùng cho dropdown khi tạo TK mới).
     */
    public List<String[]> layNhanVienChuaCoTaiKhoan() {
        List<String[]> ds = new ArrayList<>();
        String sql =
                "SELECT nv.maNhanVien, nv.hoTenNV " +
                "FROM NhanVien nv " +
                "LEFT JOIN TaiKhoan tk ON nv.maNhanVien = tk.maNhanVien " +
                "WHERE tk.maTaiKhoan IS NULL " +
                "  AND nv.trangThaiNV = 1 " +
                "ORDER BY nv.maNhanVien";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                ds.add(new String[]{
                    rs.getString("maNhanVien"),
                    rs.getString("hoTenNV")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    /**
     * Thêm tài khoản mới.
     */
    public boolean themTaiKhoan(TaiKhoanInfo tk) {
        String sql =
                "INSERT INTO TaiKhoan " +
                "(maTaiKhoan, maNhanVien, matKhau, trangThaiTK, VaiTro, DangDangNhap) " +
                "VALUES (?, ?, ?, ?, ?, 0)";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, tk.maTaiKhoan);
            ps.setString(2, tk.maNhanVien);
            ps.setString(3, tk.matKhau);
            ps.setBoolean(4, tk.trangThaiTK);
            ps.setString(5, tk.vaiTro);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật tài khoản (vai trò, trạng thái, mật khẩu).
     * Không cho đổi mã nhân viên - vì đó là ràng buộc tạo TK ban đầu.
     */
    public boolean capNhatTaiKhoan(TaiKhoanInfo tk) {
        String sql =
                "UPDATE TaiKhoan SET " +
                "  matKhau = ?, " +
                "  VaiTro = ?, " +
                "  trangThaiTK = ? " +
                "WHERE maTaiKhoan = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, tk.matKhau);
            ps.setString(2, tk.vaiTro);
            ps.setBoolean(3, tk.trangThaiTK);
            ps.setString(4, tk.maTaiKhoan);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xoá mềm: set trangThaiTK = 0, đồng thời ép logout (DangDangNhap = 0).
     */
    public boolean xoaMemTaiKhoan(String maTaiKhoan) {
        String sql = "UPDATE TaiKhoan SET trangThaiTK = 0, DangDangNhap = 0 WHERE maTaiKhoan = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maTaiKhoan);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Khôi phục tài khoản đã khoá (set trangThaiTK = 1).
     */
    public boolean khoiPhucTaiKhoan(String maTaiKhoan) {
        String sql = "UPDATE TaiKhoan SET trangThaiTK = 1 WHERE maTaiKhoan = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maTaiKhoan);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public TaiKhoanInfo layTaiKhoanTheoMa(String maTaiKhoan) {
        String sql =
                "SELECT tk.*, nv.hoTenNV " +
                "FROM TaiKhoan tk " +
                "LEFT JOIN NhanVien nv ON tk.maNhanVien = nv.maNhanVien " +
                "WHERE tk.maTaiKhoan = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maTaiKhoan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return doTaiKhoanTuResultSet(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ==================================================================
    // HÀM TIỆN ÍCH PRIVATE
    // ==================================================================

    private TaiKhoanInfo doTaiKhoanTuResultSet(ResultSet rs) throws java.sql.SQLException {
        TaiKhoanInfo tk = new TaiKhoanInfo();
        tk.maTaiKhoan = rs.getString("maTaiKhoan");
        tk.maNhanVien = rs.getString("maNhanVien");
        tk.matKhau = rs.getString("matKhau");
        tk.vaiTro = rs.getString("VaiTro");
        tk.trangThaiTK = rs.getBoolean("trangThaiTK");
        tk.dangDangNhap = rs.getBoolean("DangDangNhap");
        tk.hoTenNhanVien = rs.getString("hoTenNV");
        return tk;
    }
}