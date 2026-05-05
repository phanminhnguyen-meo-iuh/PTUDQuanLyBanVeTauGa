package com.mycompany.prj1.DAO;

import com.mycompany.prj1.ConnectDB.DB;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DAO_VETAU {

    public String themVeVaLayMaVe(
        String maNhanVien,
        String maHanhKhach,
        String soGhe,
        String loaiVe,
        String maChuyenTau,
        String maKhuyenMai,
        long tongTien
) {
    String maVe = null;

    String sql = """
        INSERT INTO Ve
        (
            ngayBan,
            trangThaiVe,
            nhanVien,
            hanhKhach,
            soGhe,
            loaiVe,
            chuyenTau,
            khuyenMai,
            tongTien
        )
        OUTPUT INSERTED.maVe
        VALUES
        (
            GETDATE(),
            N'Đã thanh toán',
            ?, ?, ?, ?, ?, ?, ?
        )
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maNhanVien);
        ps.setString(2, maHanhKhach);
        ps.setString(3, soGhe);
        ps.setString(4, loaiVe);
        ps.setString(5, maChuyenTau);

        if (maKhuyenMai == null || maKhuyenMai.trim().isEmpty()) {
            ps.setNull(6, java.sql.Types.VARCHAR);
        } else {
            ps.setString(6, maKhuyenMai);
        }

        ps.setLong(7, tongTien);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            maVe = rs.getString("maVe");
        }

    } catch (Exception e) {
        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Lỗi lưu vé:\n" + e.getMessage()
                + "\n\nDữ liệu truyền vào:"
                + "\nNhân viên: " + maNhanVien
                + "\nHành khách: " + maHanhKhach
                + "\nSố ghế: " + soGhe
                + "\nLoại vé: " + loaiVe
                + "\nChuyến tàu: " + maChuyenTau
                + "\nKhuyến mãi: " + maKhuyenMai
                + "\nTổng tiền: " + tongTien
        );
    }

    return maVe;
}

    public boolean gheDaDuocBan(String maChuyenTau, String soGhe) {
    String sql = """
        SELECT COUNT(*) AS SoLuong
        FROM Ve
        WHERE chuyenTau = ?
          AND soGhe = ?
          AND trangThaiVe = N'Đã thanh toán'
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maChuyenTau);
        ps.setString(2, soGhe);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("SoLuong") > 0;
        }

    } catch (Exception e) {
        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Lỗi kiểm tra ghế đã bán:\n" + e.getMessage()
        );
    }

    return false;
}
    public void xoaGiuChoHetHan() {
    String sql = """
        DELETE FROM GiuChoGhe
        WHERE thoiGianHetHan < GETDATE()
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public boolean datGiuChoGhe(
        String maChuyenTau,
        String soGhe,
        String maPhien,
        String maNhanVien
) {
    String sqlXoaHetHan = """
        DELETE FROM GiuChoGhe
        WHERE thoiGianHetHan < GETDATE()
    """;

    String sqlKiemTraVeDaBan = """
        SELECT COUNT(*) AS SoLuong
        FROM Ve
        WHERE chuyenTau = ?
          AND soGhe = ?
          AND trangThaiVe = N'Đã thanh toán'
    """;

    String sqlKiemTraGiuCho = """
        SELECT COUNT(*) AS SoLuong
        FROM GiuChoGhe WITH (UPDLOCK, HOLDLOCK)
        WHERE maChuyenTau = ?
          AND soGhe = ?
          AND thoiGianHetHan >= GETDATE()
    """;

    String sqlInsert = """
        INSERT INTO GiuChoGhe
        (
            maChuyenTau,
            soGhe,
            maPhien,
            maNhanVien,
            thoiGianHetHan
        )
        VALUES
        (
            ?, ?, ?, ?, DATEADD(MINUTE, 15, GETDATE())
        )
    """;

    Connection con = null;

    try {
        con = DB.getConnection();
        con.setAutoCommit(false);

        try (PreparedStatement ps = con.prepareStatement(sqlXoaHetHan)) {
            ps.executeUpdate();
        }

        try (PreparedStatement ps = con.prepareStatement(sqlKiemTraVeDaBan)) {
            ps.setString(1, maChuyenTau);
            ps.setString(2, soGhe);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt("SoLuong") > 0) {
                    con.rollback();
                    javax.swing.JOptionPane.showMessageDialog(
                            null,
                            "Ghế này đã được bán.\nVui lòng chọn ghế khác."
                    );
                    return false;
                }
            }
        }

        try (PreparedStatement ps = con.prepareStatement(sqlKiemTraGiuCho)) {
            ps.setString(1, maChuyenTau);
            ps.setString(2, soGhe);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getInt("SoLuong") > 0) {
                    con.rollback();
                    javax.swing.JOptionPane.showMessageDialog(
                            null,
                            "Ghế này vừa được người khác giữ chỗ.\nVui lòng chọn ghế khác."
                    );
                    return false;
                }
            }
        }

        try (PreparedStatement ps = con.prepareStatement(sqlInsert)) {
            ps.setString(1, maChuyenTau);
            ps.setString(2, soGhe);
            ps.setString(3, maPhien);

            if (maNhanVien == null || maNhanVien.trim().isEmpty()) {
                ps.setNull(4, java.sql.Types.NVARCHAR);
            } else {
                ps.setString(4, maNhanVien);
            }

            ps.executeUpdate();
        }

        con.commit();
        return true;

    } catch (HeadlessException | SQLException e) {
        try {
            if (con != null) {
                con.rollback();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Không thể giữ ghế này.\n"
                + "Ghế có thể vừa được người khác chọn.\n\n"
                + "Lỗi: " + e.getMessage()
        );

        return false;

    } finally {
        try {
            if (con != null) {
                con.setAutoCommit(true);
                con.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
    public void xoaGiuChoGhe(String maChuyenTau, String soGhe, String maPhien) {
    String sql = """
        DELETE FROM GiuChoGhe
        WHERE maChuyenTau = ?
          AND soGhe = ?
          AND maPhien = ?
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maChuyenTau);
        ps.setString(2, soGhe);
        ps.setString(3, maPhien);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public void xoaTatCaGiuChoTheoPhien(String maPhien) {
    String sql = """
        DELETE FROM GiuChoGhe
        WHERE maPhien = ?
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maPhien);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
    public boolean gheDangBiNguoiKhacGiu(
        String maChuyenTau,
        String soGhe,
        String maPhienHienTai
) {
    xoaGiuChoHetHan();

    String sql = """
        SELECT COUNT(*) AS SoLuong
        FROM GiuChoGhe
        WHERE maChuyenTau = ?
          AND soGhe = ?
          AND maPhien <> ?
          AND thoiGianHetHan >= GETDATE()
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maChuyenTau);
        ps.setString(2, soGhe);
        ps.setString(3, maPhienHienTai);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("SoLuong") > 0;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return false;
}
    public String timPhienDangGiuTheoNhanVien(String maNhanVien) {
    xoaGiuChoHetHan();

    String sql = """
        SELECT TOP 1 maPhien
        FROM PhienDatVe
        WHERE maNhanVien = ?
          AND trangThai = N'DANG_GIU'
          AND thoiGianHetHan >= GETDATE()
        ORDER BY thoiGianTao DESC
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maNhanVien);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getString("maPhien");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
}
   public void luuHoacCapNhatPhienDatVe(
        String maPhien,
        String maNhanVien,

        String maChuyenDi,
        String tenChuyenDi,
        String tenTauDi,
        String gaDiChuyenDi,
        String gaDenChuyenDi,

        String maChuyenVe,
        String tenChuyenVe,
        String tenTauVe,
        String gaDiChuyenVe,
        String gaDenChuyenVe,

        String loaiVe,
        String manHinhDangDung,

        int nguoiLon,
        int treEm,
        int nguoiCaoTuoi,
        int sinhVien
) {
    String sql = """
        MERGE PhienDatVe AS target
        USING (SELECT ? AS maPhien) AS source
        ON target.maPhien = source.maPhien

        WHEN MATCHED THEN
            UPDATE SET
                maNhanVien = ?,
                maChuyenDi = ?,
                tenChuyenDi = ?,
                tenTauDi = ?,
                gaDiChuyenDi = ?,
                gaDenChuyenDi = ?,
                maChuyenVe = ?,
                tenChuyenVe = ?,
                tenTauVe = ?,
                gaDiChuyenVe = ?,
                gaDenChuyenVe = ?,
                loaiVe = ?,
                manHinhDangDung = ?,
                nguoiLon = ?,
                treEm = ?,
                nguoiCaoTuoi = ?,
                sinhVien = ?,
                thoiGianHetHan = DATEADD(MINUTE, 15, GETDATE()),
                trangThai = N'DANG_GIU'

        WHEN NOT MATCHED THEN
            INSERT (
                maPhien,
                maNhanVien,
                maChuyenDi,
                tenChuyenDi,
                tenTauDi,
                gaDiChuyenDi,
                gaDenChuyenDi,
                maChuyenVe,
                tenChuyenVe,
                tenTauVe,
                gaDiChuyenVe,
                gaDenChuyenVe,
                loaiVe,
                manHinhDangDung,
                nguoiLon,
                treEm,
                nguoiCaoTuoi,
                sinhVien,
                thoiGianHetHan,
                trangThai
            )
            VALUES (
                ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,
                DATEADD(MINUTE, 15, GETDATE()),
                N'DANG_GIU'
            );
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        int i = 1;

        // source
        ps.setString(i++, maPhien);

        // UPDATE values
        ps.setString(i++, maNhanVien);
        ps.setString(i++, maChuyenDi);
        ps.setString(i++, tenChuyenDi);
        ps.setString(i++, tenTauDi);
        ps.setString(i++, gaDiChuyenDi);
        ps.setString(i++, gaDenChuyenDi);
        ps.setString(i++, maChuyenVe);
        ps.setString(i++, tenChuyenVe);
        ps.setString(i++, tenTauVe);
        ps.setString(i++, gaDiChuyenVe);
        ps.setString(i++, gaDenChuyenVe);
        ps.setString(i++, loaiVe);
        ps.setString(i++, manHinhDangDung);
        ps.setInt(i++, nguoiLon);
        ps.setInt(i++, treEm);
        ps.setInt(i++, nguoiCaoTuoi);
        ps.setInt(i++, sinhVien);

        // INSERT values
        ps.setString(i++, maPhien);
        ps.setString(i++, maNhanVien);
        ps.setString(i++, maChuyenDi);
        ps.setString(i++, tenChuyenDi);
        ps.setString(i++, tenTauDi);
        ps.setString(i++, gaDiChuyenDi);
        ps.setString(i++, gaDenChuyenDi);
        ps.setString(i++, maChuyenVe);
        ps.setString(i++, tenChuyenVe);
        ps.setString(i++, tenTauVe);
        ps.setString(i++, gaDiChuyenVe);
        ps.setString(i++, gaDenChuyenVe);
        ps.setString(i++, loaiVe);
        ps.setString(i++, manHinhDangDung);
        ps.setInt(i++, nguoiLon);
        ps.setInt(i++, treEm);
        ps.setInt(i++, nguoiCaoTuoi);
        ps.setInt(i++, sinhVien);

        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
   public void hoanThanhPhienDatVe(String maPhien) {
    String sql = """
        UPDATE PhienDatVe
        SET trangThai = N'DA_HOAN_THANH'
        WHERE maPhien = ?
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maPhien);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
   public void huyPhienDatVe(String maPhien) {
    String sql = """
        UPDATE PhienDatVe
        SET trangThai = N'DA_HUY'
        WHERE maPhien = ?
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maPhien);
        ps.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
   public java.util.Map<String, Object> layThongTinPhienDatVe(String maPhien) {
    String sql = """
        SELECT *
        FROM PhienDatVe
        WHERE maPhien = ?
          AND trangThai = N'DANG_GIU'
          AND thoiGianHetHan >= GETDATE()
    """;

    java.util.Map<String, Object> map = new java.util.HashMap<>();

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maPhien);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                map.put("maPhien", rs.getString("maPhien"));
                map.put("maNhanVien", rs.getString("maNhanVien"));

                map.put("maChuyenDi", rs.getString("maChuyenDi"));
                map.put("tenChuyenDi", rs.getString("tenChuyenDi"));
                map.put("tenTauDi", rs.getString("tenTauDi"));
                map.put("gaDiChuyenDi", rs.getString("gaDiChuyenDi"));
                map.put("gaDenChuyenDi", rs.getString("gaDenChuyenDi"));

                map.put("maChuyenVe", rs.getString("maChuyenVe"));
                map.put("tenChuyenVe", rs.getString("tenChuyenVe"));
                map.put("tenTauVe", rs.getString("tenTauVe"));
                map.put("gaDiChuyenVe", rs.getString("gaDiChuyenVe"));
                map.put("gaDenChuyenVe", rs.getString("gaDenChuyenVe"));

                map.put("loaiVe", rs.getString("loaiVe"));
                map.put("manHinhDangDung", rs.getString("manHinhDangDung"));

                map.put("nguoiLon", rs.getInt("nguoiLon"));
                map.put("treEm", rs.getInt("treEm"));
                map.put("nguoiCaoTuoi", rs.getInt("nguoiCaoTuoi"));
                map.put("sinhVien", rs.getInt("sinhVien"));

                return map;
            }
        }

    } catch (Exception e) {
        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Lỗi lấy thông tin phiên đặt vé:\n" + e.getMessage()
        );
    }

    return null;
}
   public java.util.List<java.util.Map<String, Object>> layDanhSachGiuChoTheoPhien(String maPhien) {
    String sql = """
        SELECT *
        FROM GiuChoGhe
        WHERE maPhien = ?
          AND thoiGianHetHan >= GETDATE()
        ORDER BY chieu, sttHanhKhach
    """;

    java.util.List<java.util.Map<String, Object>> ds = new java.util.ArrayList<>();

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maPhien);

        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                java.util.Map<String, Object> map = new java.util.HashMap<>();

                map.put("maChuyenTau", rs.getString("maChuyenTau"));
                map.put("soGhe", rs.getString("soGhe"));
                map.put("maPhien", rs.getString("maPhien"));
                map.put("maNhanVien", rs.getString("maNhanVien"));

                map.put("chieu", rs.getString("chieu"));
                map.put("sttHanhKhach", rs.getInt("sttHanhKhach"));
                map.put("loaiHanhKhach", rs.getString("loaiHanhKhach"));
                map.put("tenToa", rs.getString("tenToa"));
                map.put("tenGhe", rs.getString("tenGhe"));
                map.put("giaVe", rs.getDouble("giaVe"));

                ds.add(map);
            }
        }

    } catch (Exception e) {
        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Lỗi lấy danh sách giữ chỗ:\n" + e.getMessage()
        );
    }

    return ds;
}
   public int laySoGiayConLaiCuaPhien(String maPhien) {
    String sql = """
        SELECT DATEDIFF(SECOND, GETDATE(), thoiGianHetHan) AS SoGiayConLai
        FROM PhienDatVe
        WHERE maPhien = ?
          AND trangThai = N'DANG_GIU'
          AND thoiGianHetHan >= GETDATE()
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maPhien);

        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("SoGiayConLai");
            }
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}
   public void capNhatChiTietGiuCho(
        String maChuyenTau,
        String soGhe,
        String maPhien,
        String chieu,
        int sttHanhKhach,
        String loaiHanhKhach,
        String tenToa,
        String tenGhe,
        double giaVe
) {
    String sql = """
        UPDATE GiuChoGhe
        SET chieu = ?,
            sttHanhKhach = ?,
            loaiHanhKhach = ?,
            tenToa = ?,
            tenGhe = ?,
            giaVe = ?
        WHERE maChuyenTau = ?
          AND soGhe = ?
          AND maPhien = ?
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, chieu);
        ps.setInt(2, sttHanhKhach);
        ps.setString(3, loaiHanhKhach);
        ps.setString(4, tenToa);
        ps.setString(5, tenGhe);
        ps.setDouble(6, giaVe);

        ps.setString(7, maChuyenTau);
        ps.setString(8, soGhe);
        ps.setString(9, maPhien);

        ps.executeUpdate();

    } catch (Exception e) {
        e.printStackTrace();
    }
}
   public java.util.Set<String> layDanhSachGheKhongChonDuoc(
        String maChuyenTau,
        String maPhienHienTai
) {
    java.util.Set<String> ds = new java.util.HashSet<>();

    String sql = """
        SELECT soGhe
        FROM Ve
        WHERE chuyenTau = ?
          AND trangThaiVe = N'Đã thanh toán'

        UNION

        SELECT soGhe
        FROM GiuChoGhe
        WHERE maChuyenTau = ?
          AND maPhien <> ?
          AND thoiGianHetHan >= GETDATE()
    """;

    try (
        java.sql.Connection con = DB.getConnection();
        java.sql.PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maChuyenTau);
        ps.setString(2, maChuyenTau);
        ps.setString(3, maPhienHienTai);

        java.sql.ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ds.add(rs.getString("soGhe"));
        }

    } catch (Exception e) {
        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Lỗi lấy danh sách ghế không chọn được:\n" + e.getMessage()
        );
    }

    return ds;
}
   public int laySoGiayGiuChoConLaiCuaGhe(
        String maChuyenTau,
        String soGhe,
        String maPhienHienTai
) {
    xoaGiuChoHetHan();

    String sql = """
        SELECT TOP 1 
               DATEDIFF(SECOND, GETDATE(), thoiGianHetHan) AS SoGiayConLai
        FROM GiuChoGhe
        WHERE maChuyenTau = ?
          AND soGhe = ?
          AND maPhien <> ?
          AND thoiGianHetHan >= GETDATE()
    """;

    try (
        java.sql.Connection con = DB.getConnection();
        java.sql.PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maChuyenTau);
        ps.setString(2, soGhe);
        ps.setString(3, maPhienHienTai);

        java.sql.ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("SoGiayConLai");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}
   public java.util.Set<String> layDanhSachGheDaBan(String maChuyenTau) {
    java.util.Set<String> ds = new java.util.HashSet<>();

    String sql = """
        SELECT soGhe
        FROM Ve
        WHERE chuyenTau = ?
          AND trangThaiVe = N'Đã thanh toán'
    """;

    try (
        java.sql.Connection con = DB.getConnection();
        java.sql.PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maChuyenTau);

        java.sql.ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ds.add(rs.getString("soGhe"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return ds;
}
   public java.util.Set<String> layDanhSachGheDangGiuNguoiKhac(
        String maChuyenTau,
        String maPhienHienTai
) {
    java.util.Set<String> ds = new java.util.HashSet<>();

    xoaGiuChoHetHan();

    String sql = """
        SELECT soGhe
        FROM GiuChoGhe
        WHERE maChuyenTau = ?
          AND maPhien <> ?
          AND thoiGianHetHan >= GETDATE()
    """;

    try (
        java.sql.Connection con = DB.getConnection();
        java.sql.PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maChuyenTau);
        ps.setString(2, maPhienHienTai);

        java.sql.ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            ds.add(rs.getString("soGhe"));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return ds;
}
   public int demGheDaBanHoacDangGiuTrongToa(
        String maChuyenTau,
        String maPhienHienTai,
        String tenToa
) {
    if (maChuyenTau == null || maChuyenTau.trim().isEmpty()
            || tenToa == null || tenToa.trim().isEmpty()) {
        return 0;
    }

    xoaGiuChoHetHan();

    tenToa = tenToa.trim();

String mauGhe = tenToa + " - Ghế %";

    String sql = """
        SELECT COUNT(*) AS SoLuong
        FROM (
            SELECT soGhe
            FROM Ve
            WHERE chuyenTau = ?
              AND trangThaiVe = N'Đã thanh toán'
              AND soGhe LIKE ?

            UNION

            SELECT soGhe
            FROM GiuChoGhe
            WHERE maChuyenTau = ?
              AND maPhien <> ?
              AND thoiGianHetHan >= GETDATE()
              AND soGhe LIKE ?
        ) AS ds
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql)
    ) {
        ps.setString(1, maChuyenTau);
        ps.setString(2, mauGhe);

        ps.setString(3, maChuyenTau);
        ps.setString(4, maPhienHienTai);
        ps.setString(5, mauGhe);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt("SoLuong");
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return 0;
}
   public double layTongDoanhThuHomNay() {
    double tong = 0;

    String sql = """
        SELECT ISNULL(SUM(tongTien), 0) AS TongDoanhThu
        FROM Ve
        WHERE ngayBan >= CAST(GETDATE() AS DATE)
          AND ngayBan < DATEADD(DAY, 1, CAST(GETDATE() AS DATE))
          AND trangThaiVe = N'Đã thanh toán'
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()
    ) {
        if (rs.next()) {
            tong = rs.getDouble("TongDoanhThu");
        }

    } catch (Exception e) {
        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Lỗi lấy doanh thu hôm nay:\n" + e.getMessage()
        );
    }

    return tong;
}
   public int laySoVeDaBanHomNay() {
    int soVe = 0;

    String sql = """
        SELECT COUNT(*) AS SoVeDaBan
        FROM Ve
        WHERE ngayBan >= CAST(GETDATE() AS DATE)
          AND ngayBan < DATEADD(DAY, 1, CAST(GETDATE() AS DATE))
          AND trangThaiVe = N'Đã thanh toán'
    """;

    try (
        Connection con = DB.getConnection();
        PreparedStatement ps = con.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()
    ) {
        if (rs.next()) {
            soVe = rs.getInt("SoVeDaBan");
        }

    } catch (Exception e) {
        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                null,
                "Lỗi lấy số vé đã bán hôm nay:\n" + e.getMessage()
        );
    }

    return soVe;
}
   
}