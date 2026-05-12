package com.mycompany.prj1.DAO;

import com.mycompany.prj1.ConnectDB.DB;
import com.mycompany.prj1.entity.NhanVien;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DAO_NHANVIEN {

    // ==================================================================
    // CÁC HÀM CŨ (giữ nguyên để không phá code khác)
    // ==================================================================

    public NhanVien layNhanVienTheoMa(String maNhanVien) {
        NhanVien nv = null;

        String sql = "SELECT * FROM NhanVien WHERE MaNhanVien = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            if (con == null) return null;

            ps.setString(1, maNhanVien);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    nv = doNhanVienTuResultSet(rs);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return nv;
    }

    public String layTenCaLamTheoMaNhanVien(String maNhanVien) {
        String[] thongTinCa = layThongTinCaLamTheoMaNhanVien(maNhanVien);
        if (thongTinCa == null) return "";
        return thongTinCa[0];
    }

    public String[] layThongTinCaLamTheoMaNhanVien(String maNhanVien) {
        String sql =
                "SELECT TOP 1 ca.tenCaLam, ca.thoiGianBatDau, ca.thoiGianKetThuc " +
                "FROM ChiTietCaLam ct " +
                "JOIN CaLam ca ON ct.caLam = ca.maCaLam " +
                "WHERE ct.nhanVien = ? " +
                "ORDER BY ct.thoiGianBatDau DESC";

        java.time.format.DateTimeFormatter fmt =
                java.time.format.DateTimeFormatter.ofPattern("HH:mm");

        try (
                Connection con = DB.getConnection();
                PreparedStatement ps = con.prepareStatement(sql)
        ) {
            if (con == null) return null;

            ps.setString(1, maNhanVien);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String tenCaLam = rs.getString("tenCaLam");
                    java.sql.Time tgBD = rs.getTime("thoiGianBatDau");
                    java.sql.Time tgKT = rs.getTime("thoiGianKetThuc");

                    String gioBatDau = tgBD != null ? tgBD.toLocalTime().format(fmt) : "";
                    String gioKetThuc = tgKT != null ? tgKT.toLocalTime().format(fmt) : "";

                    return new String[]{tenCaLam, gioBatDau, gioKetThuc};
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    // ==================================================================
    // CÁC HÀM MỚI: CRUD CHO QUẢN LÝ NHÂN VIÊN
    // ==================================================================

    /**
     * Lấy toàn bộ nhân viên (cả đang làm và đã nghỉ).
     * Dùng cho danh sách hiển thị.
     */
    public List<NhanVien> layTatCaNhanVien() {
        List<NhanVien> ds = new ArrayList<>();
        String sql = "SELECT * FROM NhanVien ORDER BY maNhanVien";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                ds.add(doNhanVienTuResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    /**
     * Tìm nhân viên theo từ khoá tự do.
     * Match được theo: mã NV, họ tên, CCCD, SĐT.
     * Đây là cách user-friendly nhất - user gõ gì cũng tìm được.
     */
    public List<NhanVien> timNhanVien(String tuKhoa) {
        List<NhanVien> ds = new ArrayList<>();

        if (tuKhoa == null || tuKhoa.trim().isEmpty()) {
            return layTatCaNhanVien();
        }

        String sql =
                "SELECT * FROM NhanVien " +
                "WHERE maNhanVien LIKE ? " +
                "   OR hoTenNV LIKE ? " +
                "   OR cccd LIKE ? " +
                "   OR sdt LIKE ? " +
                "ORDER BY maNhanVien";

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
                    ds.add(doNhanVienTuResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    /**
     * Sinh mã nhân viên mới: NV001, NV002, ...
     */
    public String sinhMaNhanVienMoi() {
        String sql = "SELECT MAX(maNhanVien) AS maxMa FROM NhanVien WHERE maNhanVien LIKE 'NV%'";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                String maxMa = rs.getString("maxMa");
                if (maxMa == null) {
                    return "NV001";
                }
                // Lấy phần số sau NV
                try {
                    int so = Integer.parseInt(maxMa.substring(2)) + 1;
                    return String.format("NV%03d", so);
                } catch (NumberFormatException e) {
                    return "NV001";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "NV001";
    }

    /**
     * Kiểm tra CCCD đã tồn tại (loại trừ chính nhân viên đang sửa).
     * Truyền maNhanVienHienTai = null khi thêm mới.
     */
    public boolean cccdDaTonTai(String cccd, String maNhanVienHienTai) {
        String sql;
        if (maNhanVienHienTai == null) {
            sql = "SELECT COUNT(*) FROM NhanVien WHERE cccd = ?";
        } else {
            sql = "SELECT COUNT(*) FROM NhanVien WHERE cccd = ? AND maNhanVien <> ?";
        }

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, cccd);
            if (maNhanVienHienTai != null) {
                ps.setString(2, maNhanVienHienTai);
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
     * Kiểm tra SĐT đã tồn tại (loại trừ chính nhân viên đang sửa).
     */
    public boolean sdtDaTonTai(String sdt, String maNhanVienHienTai) {
        String sql;
        if (maNhanVienHienTai == null) {
            sql = "SELECT COUNT(*) FROM NhanVien WHERE sdt = ?";
        } else {
            sql = "SELECT COUNT(*) FROM NhanVien WHERE sdt = ? AND maNhanVien <> ?";
        }

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, sdt);
            if (maNhanVienHienTai != null) {
                ps.setString(2, maNhanVienHienTai);
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
     * Thêm nhân viên mới.
     * @return true nếu thêm thành công.
     */
    public boolean themNhanVien(NhanVien nv) {
        String sql =
                "INSERT INTO NhanVien " +
                "(maNhanVien, hoTenNV, cccd, sdt, ngayVaoLam, chucVu, gioiTinh, ngaySinh, trangThaiNV, anhNhanVien) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, nv.getMaNhanVien());
            ps.setString(2, nv.getHoTenNV());
            ps.setString(3, nv.getCccd());
            ps.setString(4, nv.getSdt());
            ps.setDate(5, java.sql.Date.valueOf(nv.getNgayVaoLam()));
            ps.setString(6, nv.getChucVu());
            ps.setString(7, nv.getGioiTinh());
            ps.setDate(8, java.sql.Date.valueOf(nv.getNgaySinh()));
            ps.setBoolean(9, nv.isTrangThaiNV());

            if (nv.getAnhNhanVien() == null || nv.getAnhNhanVien().trim().isEmpty()) {
                ps.setNull(10, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(10, nv.getAnhNhanVien());
            }

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cập nhật thông tin nhân viên (theo mã nhân viên).
     */
    public boolean capNhatNhanVien(NhanVien nv) {
        String sql =
                "UPDATE NhanVien SET " +
                "  hoTenNV = ?, " +
                "  cccd = ?, " +
                "  sdt = ?, " +
                "  ngayVaoLam = ?, " +
                "  chucVu = ?, " +
                "  gioiTinh = ?, " +
                "  ngaySinh = ?, " +
                "  trangThaiNV = ?, " +
                "  anhNhanVien = ? " +
                "WHERE maNhanVien = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, nv.getHoTenNV());
            ps.setString(2, nv.getCccd());
            ps.setString(3, nv.getSdt());
            ps.setDate(4, java.sql.Date.valueOf(nv.getNgayVaoLam()));
            ps.setString(5, nv.getChucVu());
            ps.setString(6, nv.getGioiTinh());
            ps.setDate(7, java.sql.Date.valueOf(nv.getNgaySinh()));
            ps.setBoolean(8, nv.isTrangThaiNV());

            if (nv.getAnhNhanVien() == null || nv.getAnhNhanVien().trim().isEmpty()) {
                ps.setNull(9, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(9, nv.getAnhNhanVien());
            }

            ps.setString(10, nv.getMaNhanVien());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Xoá mềm: chuyển trạng thái nhân viên thành false (đã nghỉ).
     * Đồng thời vô hiệu hoá tài khoản của nhân viên đó.
     */
    public boolean xoaMemNhanVien(String maNhanVien) {
        Connection con = null;
        try {
            con = DB.getConnection();
            con.setAutoCommit(false);

            // 1. Set trangThaiNV = 0
            String sql1 = "UPDATE NhanVien SET trangThaiNV = 0 WHERE maNhanVien = ?";
            try (PreparedStatement ps = con.prepareStatement(sql1)) {
                ps.setString(1, maNhanVien);
                ps.executeUpdate();
            }

            // 2. Khoá luôn tài khoản (nếu có)
            String sql2 = "UPDATE TaiKhoan SET trangThaiTK = 0, DangDangNhap = 0 WHERE maNhanVien = ?";
            try (PreparedStatement ps = con.prepareStatement(sql2)) {
                ps.setString(1, maNhanVien);
                ps.executeUpdate();
            }

            con.commit();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (Exception ex) { }
            return false;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) { }
        }
    }

    /**
     * Khôi phục nhân viên đã xoá (set trangThaiNV = 1).
     */
    public boolean khoiPhucNhanVien(String maNhanVien) {
        String sql = "UPDATE NhanVien SET trangThaiNV = 1 WHERE maNhanVien = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maNhanVien);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==================================================================
    // HÀM TIỆN ÍCH PRIVATE
    // ==================================================================

    private NhanVien doNhanVienTuResultSet(ResultSet rs) throws SQLException {
        NhanVien nv = new NhanVien();
        nv.setMaNhanVien(rs.getString("maNhanVien"));
        nv.setHoTenNV(rs.getString("hoTenNV"));
        nv.setCccd(rs.getString("cccd"));
        nv.setSdt(rs.getString("sdt"));

        java.sql.Date ngayVaoLam = rs.getDate("ngayVaoLam");
        if (ngayVaoLam != null) nv.setNgayVaoLam(ngayVaoLam.toLocalDate());

        nv.setChucVu(rs.getString("chucVu"));
        nv.setGioiTinh(rs.getString("gioiTinh"));

        java.sql.Date ngaySinh = rs.getDate("ngaySinh");
        if (ngaySinh != null) nv.setNgaySinh(ngaySinh.toLocalDate());

        nv.setTrangThaiNV(rs.getBoolean("trangThaiNV"));
        nv.setAnhNhanVien(rs.getString("anhNhanVien"));
        return nv;
    }
}