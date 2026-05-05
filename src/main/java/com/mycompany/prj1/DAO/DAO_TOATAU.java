package com.mycompany.prj1.DAO;

import com.mycompany.prj1.ConnectDB.DB;
import com.mycompany.prj1.entity.LoaiGhe;
import com.mycompany.prj1.entity.Tau;
import com.mycompany.prj1.entity.ToaTau;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAO_TOATAU {

    public int laySoLuongToaTheoTau(String maTau) {
    int soLuong = 0;

    String sql = "SELECT COUNT(*) AS soLuongToa FROM ToaTau WHERE tau = ?";

    try (
        java.sql.Connection con = com.mycompany.prj1.ConnectDB.DB.getConnection();
        java.sql.PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maTau);

        try (java.sql.ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                soLuong = rs.getInt("soLuongToa");
            }
        }

    } catch (Exception e) {
    }

    return soLuong;
}

    public java.util.List<ToaTau> layDanhSachToaTheoTau(String maTau) {
    java.util.List<ToaTau> ds = new java.util.ArrayList<>();

    String sql =
        "SELECT " +
        "   tt.maToa, " +
        "   tt.tenToa, " +
        "   tt.sucChua, " +
        "   tt.tau, " +
        "   lg.maLoaiGhe, " +
        "   lg.tenLoaiGhe, " +
        "   lg.heSoLoaiGhe, " +
        "   lg.trangThai " +
        "FROM ToaTau tt " +
        "JOIN LoaiGhe lg ON tt.loaiGhe = lg.maLoaiGhe " +
        "WHERE tt.tau = ? " +
        "ORDER BY tt.tenToa";

    try (
        java.sql.Connection con = com.mycompany.prj1.ConnectDB.DB.getConnection();
        java.sql.PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maTau);

        try (java.sql.ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                com.mycompany.prj1.entity.LoaiGhe loaiGhe =
                        new com.mycompany.prj1.entity.LoaiGhe();

                loaiGhe.setMaLoaiGhe(rs.getString("maLoaiGhe"));
                loaiGhe.setTenLoaiGhe(rs.getString("tenLoaiGhe"));
                loaiGhe.setHeSoLoaiGhe(rs.getDouble("heSoLoaiGhe"));
                loaiGhe.setTrangThai(rs.getBoolean("trangThai"));

                com.mycompany.prj1.entity.ToaTau toa =
                        new com.mycompany.prj1.entity.ToaTau();

                toa.setMaToa(rs.getString("maToa"));
                toa.setTenToa(rs.getString("tenToa"));
                toa.setSucChua(rs.getInt("sucChua"));
                toa.setLoaiGhe(loaiGhe);

                ds.add(toa);
            }
        }

    } catch (Exception e) {
    }

    return ds;
}
}