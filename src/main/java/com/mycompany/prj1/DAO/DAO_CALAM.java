package com.mycompany.prj1.DAO;

import com.mycompany.prj1.ConnectDB.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class DAO_CALAM {

    public static class CaLamInfo {
        public String maCaLam;
        public String tenCaLam;
        public java.time.LocalTime thoiGianBatDau;
        public java.time.LocalTime thoiGianKetThuc;
    }

    public static class CaDangMoInfo {
        public String maChiTietCaLam;
        public String maCaLam;
        public String tenCaLam;
        public java.time.LocalTime thoiGianBatDauCa;
        public java.time.LocalTime thoiGianKetThucCa;
        public LocalDateTime thoiGianMoCa;
        public double tienMoCa;
    }

    public CaLamInfo layCaTheoGioHienTai() throws SQLException {
        String sql =
                "DECLARE @gio time = CAST(GETDATE() AS time); " +
                "SELECT TOP 1 maCaLam, tenCaLam, thoiGianBatDau, thoiGianKetThuc " +
                "FROM CaLam " +
                "WHERE " +
                "(" +
                "    thoiGianBatDau < thoiGianKetThuc " +
                "    AND @gio >= thoiGianBatDau " +
                "    AND @gio < thoiGianKetThuc " +
                ") " +
                "OR " +
                "(" +
                "    thoiGianBatDau > thoiGianKetThuc " +
                "    AND (@gio >= thoiGianBatDau OR @gio < thoiGianKetThuc) " +
                ")";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (con == null) {
                throw new SQLException("Không thể kết nối cơ sở dữ liệu.");
            }

            if (rs.next()) {
                CaLamInfo ca = new CaLamInfo();
                ca.maCaLam = rs.getString("maCaLam");
                ca.tenCaLam = rs.getString("tenCaLam");
                ca.thoiGianBatDau = rs.getTime("thoiGianBatDau").toLocalTime();
                ca.thoiGianKetThuc = rs.getTime("thoiGianKetThuc").toLocalTime();
                return ca;
            }
        }

        return null;
    }

    public CaDangMoInfo layCaDangMoTheoNhanVien(String maNhanVien) throws SQLException {
        String sql =
                "SELECT TOP 1 " +
                "ct.maChiTietCaLam, ct.caLam, c.tenCaLam, " +
                "c.thoiGianBatDau, c.thoiGianKetThuc, " +
                "ct.thoiGianBatDau AS thoiGianMoCa, ct.tienMoCa " +
                "FROM ChiTietCaLam ct " +
                "JOIN CaLam c ON ct.caLam = c.maCaLam " +
                "WHERE ct.nhanVien = ? " +
                "AND ct.thoiGianKetThuc IS NULL " +
                "AND ct.tienKetCa IS NULL " +
                "ORDER BY ct.thoiGianBatDau DESC";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            if (con == null) {
                throw new SQLException("Không thể kết nối cơ sở dữ liệu.");
            }

            ps.setString(1, maNhanVien);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    CaDangMoInfo info = new CaDangMoInfo();

                    info.maChiTietCaLam = rs.getString("maChiTietCaLam");
                    info.maCaLam = rs.getString("caLam");
                    info.tenCaLam = rs.getString("tenCaLam");
                    info.thoiGianBatDauCa = rs.getTime("thoiGianBatDau").toLocalTime();
                    info.thoiGianKetThucCa = rs.getTime("thoiGianKetThuc").toLocalTime();
                    info.thoiGianMoCa = rs.getTimestamp("thoiGianMoCa").toLocalDateTime();
                    info.tienMoCa = rs.getDouble("tienMoCa");

                    return info;
                }
            }
        }

        return null;
    }

    public CaDangMoInfo moCa(
            String maCaLam,
            String maNhanVien,
            LocalDateTime thoiGianMoCa,
            double tienMoCa
    ) throws SQLException {

        // Kiểm tra trước có ca nào chưa kết không
        CaDangMoInfo caDangMo = layCaDangMoTheoNhanVien(maNhanVien);
        if (caDangMo != null) {
            throw new SQLException("Nhân viên này đang có ca chưa kết.");
        }

        String sql =
                "INSERT INTO ChiTietCaLam " +
                "(caLam, nhanVien, thoiGianBatDau, thoiGianKetThuc, tienMoCa, tienKetCa) " +
                "OUTPUT INSERTED.maChiTietCaLam " +
                "VALUES (?, ?, ?, NULL, ?, NULL)";

        String maChiTietMoi = null;

        // SỬA: dùng try-with-resources để tự đóng connection (không bị leak)
        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            if (con == null) {
                throw new SQLException("Không thể kết nối cơ sở dữ liệu.");
            }

            ps.setString(1, maCaLam);
            ps.setString(2, maNhanVien);
            ps.setTimestamp(3, Timestamp.valueOf(thoiGianMoCa));
            ps.setDouble(4, tienMoCa);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    maChiTietMoi = rs.getString("maChiTietCaLam");
                }
            }
        }

        if (maChiTietMoi == null || maChiTietMoi.trim().isEmpty()) {
            throw new SQLException("Không lấy được mã chi tiết ca làm vừa tạo.");
        }

        return layCaDangMoTheoNhanVien(maNhanVien);
    }

    public boolean ketCa(
            String maChiTietCaLam,
            String maNhanVien,
            LocalDateTime thoiGianKetCa,
            double tienKetCa
    ) throws SQLException {

        String sql =
                "UPDATE ChiTietCaLam " +
                "SET thoiGianKetThuc = ?, tienKetCa = ? " +
                "WHERE maChiTietCaLam = ? " +
                "AND nhanVien = ? " +
                "AND thoiGianKetThuc IS NULL";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            if (con == null) {
                throw new SQLException("Không thể kết nối cơ sở dữ liệu.");
            }

            ps.setTimestamp(1, Timestamp.valueOf(thoiGianKetCa));
            ps.setDouble(2, tienKetCa);
            ps.setString(3, maChiTietCaLam);
            ps.setString(4, maNhanVien);

            return ps.executeUpdate() > 0;
        }
    }
}