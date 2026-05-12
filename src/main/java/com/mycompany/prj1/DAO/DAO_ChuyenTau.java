package com.mycompany.prj1.DAO;

import com.mycompany.prj1.entity.ChuyenTau;
import com.mycompany.prj1.entity.Ga;
import com.mycompany.prj1.ConnectDB.DB;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class DAO_ChuyenTau {

    public DefaultTableModel timChuyenLenBang(String tenGaDi, String tenGaDen, LocalDate ngayDi) {
        return doDuLieuLenModel(tenGaDi, tenGaDen, ngayDi);
    }

    public DefaultTableModel timChuyenVeLenBang(String tenGaDi, String tenGaDen, LocalDate ngayVe) {
        return doDuLieuLenModel(tenGaDi, tenGaDen, ngayVe);
    }

    private DefaultTableModel doDuLieuLenModel(String tenGaDi, String tenGaDen, LocalDate ngayDi) {
        StringBuilder sql = new StringBuilder(
            "SELECT ct.maChuyenTau, ct.tenChuyen, ct.ngayKhoiHanh, ct.ngayDenDuKien, ct.trangThaiChuyen, "
          + "ct.tau AS tenTau, "
          + "gdi.maGa AS maGaDi, gdi.tenGa AS tenGaDi, "
          + "gden.maGa AS maGaDen, gden.tenGa AS tenGaDen, "
          + "ISNULL(tt.tongGhe, 0) AS tongGhe, "
          + "CASE "
          + "   WHEN ISNULL(tt.tongGhe, 0) - ISNULL(vd.soVeDaDat, 0) < 0 THEN 0 "
          + "   ELSE ISNULL(tt.tongGhe, 0) - ISNULL(vd.soVeDaDat, 0) "
          + "END AS gheTrong "
          + "FROM ChuyenTau ct "
          + "JOIN Ga gdi ON ct.gaDi = gdi.maGa "
          + "JOIN Ga gden ON ct.gaDen = gden.maGa "
          + "LEFT JOIN ( "
          + "   SELECT tau, SUM(sucChua) AS tongGhe "
          + "   FROM ToaTau "
          + "   GROUP BY tau "
          + ") tt ON ct.tau = tt.tau "
          + "LEFT JOIN ( "
          + "   SELECT chuyenTau, COUNT(*) AS soVeDaDat "
          + "   FROM Ve "
          + "   GROUP BY chuyenTau "
          + ") vd ON ct.maChuyenTau = vd.chuyenTau "
          + "WHERE gdi.tenGa = ? "
          + "AND gden.tenGa = ? "
          + "AND ct.trangThaiChuyen = ? "
        );

        if (ngayDi != null) {
            sql.append("AND CAST(ct.ngayKhoiHanh AS DATE) = ? ");
        }

        sql.append("ORDER BY ct.ngayKhoiHanh");

        DefaultTableModel model = taoModelBang();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql.toString())
        ) {
            ps.setString(1, tenGaDi);
            ps.setString(2, tenGaDen);
            ps.setBoolean(3, true);

            if (ngayDi != null) {
                ps.setDate(4, Date.valueOf(ngayDi));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Timestamp tsKhoiHanh = rs.getTimestamp("ngayKhoiHanh");
                    Timestamp tsDen = rs.getTimestamp("ngayDenDuKien");

                    model.addRow(new Object[]{
                        rs.getString("maChuyenTau"),
                        rs.getString("tenChuyen"),
                        rs.getString("tenTau"),
                        rs.getString("tenGaDi"),
                        rs.getString("tenGaDen"),
                        tsKhoiHanh != null ? sdf.format(tsKhoiHanh) : "",
                        tsDen != null ? sdf.format(tsDen) : "",
                        rs.getInt("tongGhe"),
                        rs.getInt("gheTrong"),
                        "Chọn"
                    });
                }
            }
        } catch (SQLException e) {
        }

        return model;
    }

    private DefaultTableModel taoModelBang() {
        return new DefaultTableModel(
            new Object[][]{},
            new String[]{
                "Mã chuyến", "Tên chuyến", "Tên Tàu", "Ga đi", "Ga đến",
                "Ngày đi", "Ngày đến dự kiến", "Số lượng ghế", "Ghế trống", "Chọn chuyến"
            }
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 9;
            }
        };
    }

    public ArrayList<ChuyenTau> timChuyenTheoGa(String tenGaDi, String tenGaDen) {
        ArrayList<ChuyenTau> ds = new ArrayList<>();

        String sql = "SELECT ct.maChuyenTau, ct.tenChuyen, ct.ngayKhoiHanh, ct.ngayDenDuKien, ct.trangThaiChuyen, "
                   + "ct.tau AS tenTau, "
                   + "gdi.maGa AS maGaDi, gdi.tenGa AS tenGaDi, "
                   + "gden.maGa AS maGaDen, gden.tenGa AS tenGaDen "
                   + "FROM ChuyenTau ct "
                   + "JOIN Ga gdi ON ct.gaDi = gdi.maGa "
                   + "JOIN Ga gden ON ct.gaDen = gden.maGa "
                   + "WHERE gdi.tenGa = ? "
                   + "AND gden.tenGa = ? "
                   + "AND ct.trangThaiChuyen = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, tenGaDi);
            ps.setString(2, tenGaDen);
            ps.setBoolean(3, true);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(mapChuyenTau(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ds;
    }

    private ChuyenTau mapChuyenTau(ResultSet rs) throws SQLException {
        ChuyenTau ct = new ChuyenTau();

        ct.setMaChuyen(rs.getString("maChuyenTau"));
        ct.setTenChuyen(rs.getString("tenChuyen"));

        Timestamp tsKhoiHanh = rs.getTimestamp("ngayKhoiHanh");
        Timestamp tsDen = rs.getTimestamp("ngayDenDuKien");

        LocalDateTime ngayKhoiHanh = tsKhoiHanh != null ? tsKhoiHanh.toLocalDateTime() : null;
        LocalDateTime ngayDenDuKien = tsDen != null ? tsDen.toLocalDateTime() : null;

        ct.setNgayKhoiHanh(ngayKhoiHanh);
        ct.setNgayDenDuKien(ngayDenDuKien);
        ct.setTrangThaiChuyen(rs.getBoolean("trangThaiChuyen"));

        Ga gaDi = new Ga();
        gaDi.setMaGa(rs.getString("maGaDi"));
        gaDi.setTenGa(rs.getString("tenGaDi"));

        Ga gaDen = new Ga();
        gaDen.setMaGa(rs.getString("maGaDen"));
        gaDen.setTenGa(rs.getString("tenGaDen"));

        ct.setGaDi(gaDi);
        ct.setGaDen(gaDen);

        return ct;
    }
    public int layCuLyTheoMaChuyen(String maChuyen) {
    int cuLy = 0;

    String sql = "SELECT cuLy FROM ChuyenTau WHERE maChuyenTau = ?";

    try (
        java.sql.Connection con = com.mycompany.prj1.ConnectDB.DB.getConnection();
        java.sql.PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maChuyen);

        try (java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                cuLy = rs.getInt("cuLy");
            } else {
                System.out.println("Không tìm thấy chuyến: " + maChuyen);
            }
        }

    } catch (Exception e) {
    }

    System.out.println("DEBUG cuLy = " + cuLy + " | maChuyen = " + maChuyen);

    return cuLy;
}
}