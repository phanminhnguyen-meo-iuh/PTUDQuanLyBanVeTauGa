package com.mycompany.prj1.DAO;

import com.mycompany.prj1.ConnectDB.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO chuyên cho thống kê - chỉ đọc dữ liệu, không sửa.
 * Dùng schema hiện có, không cần đổi DB.
 */
public class DAO_THONGKE {

    // ==========================================================
    // CÁC LỚP DTO ĐỂ TRẢ KẾT QUẢ VỀ GUI
    // ==========================================================

    /** 1 dòng trong báo cáo doanh thu theo ngày */
    public static class DoanhThuTheoNgay {
        public LocalDate ngay;
        public int soVe;
        public long doanhThu;
    }

    /** 1 dòng trong báo cáo hiệu suất nhân viên */
    public static class HieuSuatNhanVien {
        public String maNhanVien;
        public String hoTenNV;
        public String chucVu;
        public int soVeBan;
        public long doanhThu;
    }

    /** 1 dòng trong báo cáo chuyến tàu */
    public static class ThongKeChuyen {
        public String maChuyenTau;
        public String tenChuyen;
        public LocalDateTime ngayKhoiHanh;
        public String gaDi;
        public String gaDen;
        public int tongGhe;
        public int daBan;
        public double tyLeLapDay; // %
        public long doanhThu;
    }

    /** Tổng kết để hiển thị trên 3 card */
    public static class TomTat {
        public int tongVe;
        public long tongDoanhThu;
        public long trungBinhMoiVe;
    }

    // ==========================================================
    // 1. DOANH THU THEO NGÀY (cho Quản lý)
    // ==========================================================

    /**
     * Lấy doanh thu từng ngày trong khoảng [tuNgay, denNgay].
     */
    public List<DoanhThuTheoNgay> layDoanhThuTheoNgay(LocalDate tuNgay, LocalDate denNgay) {
        List<DoanhThuTheoNgay> ds = new ArrayList<>();

        String sql =
                "SELECT CAST(ngayBan AS DATE) AS ngay, " +
                "       COUNT(*) AS soVe, " +
                "       SUM(tongTien) AS doanhThu " +
                "FROM Ve " +
                "WHERE ngayBan >= ? AND ngayBan < DATEADD(DAY, 1, ?) " +
                "  AND trangThaiVe LIKE N'Đã thanh toán%' " +
                "GROUP BY CAST(ngayBan AS DATE) " +
                "ORDER BY ngay";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setTimestamp(1, Timestamp.valueOf(tuNgay.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(denNgay.atStartOfDay()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DoanhThuTheoNgay d = new DoanhThuTheoNgay();
                    d.ngay = rs.getDate("ngay").toLocalDate();
                    d.soVe = rs.getInt("soVe");
                    d.doanhThu = rs.getLong("doanhThu");
                    ds.add(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    /**
     * Lấy tóm tắt tổng quát trong khoảng thời gian.
     * Dùng cho 3 card đầu màn hình.
     */
    public TomTat layTomTatDoanhThu(LocalDate tuNgay, LocalDate denNgay) {
        TomTat tt = new TomTat();

        String sql =
                "SELECT COUNT(*) AS tongVe, ISNULL(SUM(tongTien), 0) AS tongTien " +
                "FROM Ve " +
                "WHERE ngayBan >= ? AND ngayBan < DATEADD(DAY, 1, ?) " +
                "  AND trangThaiVe LIKE N'Đã thanh toán%'";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setTimestamp(1, Timestamp.valueOf(tuNgay.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(denNgay.atStartOfDay()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tt.tongVe = rs.getInt("tongVe");
                    tt.tongDoanhThu = rs.getLong("tongTien");
                    tt.trungBinhMoiVe = tt.tongVe == 0 ? 0 : tt.tongDoanhThu / tt.tongVe;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tt;
    }

    // ==========================================================
    // 2. HIỆU SUẤT NHÂN VIÊN (cho Quản lý)
    // ==========================================================

    /**
     * Lấy danh sách nhân viên kèm số vé bán & doanh thu trong khoảng thời gian.
     * Sắp xếp theo doanh thu giảm dần.
     */
    public List<HieuSuatNhanVien> layHieuSuatNhanVien(LocalDate tuNgay, LocalDate denNgay) {
        List<HieuSuatNhanVien> ds = new ArrayList<>();

        String sql =
                "SELECT nv.maNhanVien, nv.hoTenNV, nv.chucVu, " +
                "       COUNT(v.maVe) AS soVeBan, " +
                "       ISNULL(SUM(v.tongTien), 0) AS doanhThu " +
                "FROM NhanVien nv " +
                "LEFT JOIN Ve v ON nv.maNhanVien = v.nhanVien " +
                "    AND v.ngayBan >= ? " +
                "    AND v.ngayBan < DATEADD(DAY, 1, ?) " +
                "    AND v.trangThaiVe LIKE N'Đã thanh toán%' " +
                "WHERE nv.trangThaiNV = 1 " +
                "GROUP BY nv.maNhanVien, nv.hoTenNV, nv.chucVu " +
                "ORDER BY doanhThu DESC, soVeBan DESC";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setTimestamp(1, Timestamp.valueOf(tuNgay.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(denNgay.atStartOfDay()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    HieuSuatNhanVien h = new HieuSuatNhanVien();
                    h.maNhanVien = rs.getString("maNhanVien");
                    h.hoTenNV = rs.getString("hoTenNV");
                    h.chucVu = rs.getString("chucVu");
                    h.soVeBan = rs.getInt("soVeBan");
                    h.doanhThu = rs.getLong("doanhThu");
                    ds.add(h);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    // ==========================================================
    // 3. TOP CHUYẾN TÀU ĐÔNG KHÁCH (cho Quản lý)
    // ==========================================================

    /**
     * Lấy thống kê các chuyến tàu trong khoảng thời gian.
     * Sắp theo số vé bán giảm dần.
     */
    public List<ThongKeChuyen> layThongKeChuyenTau(LocalDate tuNgay, LocalDate denNgay) {
        List<ThongKeChuyen> ds = new ArrayList<>();

        String sql =
                "SELECT ct.maChuyenTau, ct.tenChuyen, ct.ngayKhoiHanh, " +
                "       gaDi.tenGa AS gaDi, gaDen.tenGa AS gaDen, " +
                "       ISNULL((SELECT SUM(tt.sucChua) FROM ToaTau tt " +
                "               WHERE tt.tau = ct.tau), 0) AS tongGhe, " +
                "       ISNULL((SELECT COUNT(*) FROM Ve v " +
                "               WHERE v.chuyenTau = ct.maChuyenTau " +
                "                 AND v.trangThaiVe LIKE N'Đã thanh toán%'), 0) AS daBan, " +
                "       ISNULL((SELECT SUM(v.tongTien) FROM Ve v " +
                "               WHERE v.chuyenTau = ct.maChuyenTau " +
                "                 AND v.trangThaiVe LIKE N'Đã thanh toán%'), 0) AS doanhThu " +
                "FROM ChuyenTau ct " +
                "JOIN Ga gaDi ON ct.gaDi = gaDi.maGa " +
                "JOIN Ga gaDen ON ct.gaDen = gaDen.maGa " +
                "WHERE ct.ngayKhoiHanh >= ? " +
                "  AND ct.ngayKhoiHanh < DATEADD(DAY, 1, ?) " +
                "ORDER BY daBan DESC, doanhThu DESC";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setTimestamp(1, Timestamp.valueOf(tuNgay.atStartOfDay()));
            ps.setTimestamp(2, Timestamp.valueOf(denNgay.atStartOfDay()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ThongKeChuyen c = new ThongKeChuyen();
                    c.maChuyenTau = rs.getString("maChuyenTau");
                    c.tenChuyen = rs.getString("tenChuyen");
                    c.ngayKhoiHanh = rs.getTimestamp("ngayKhoiHanh").toLocalDateTime();
                    c.gaDi = rs.getString("gaDi");
                    c.gaDen = rs.getString("gaDen");
                    c.tongGhe = rs.getInt("tongGhe");
                    c.daBan = rs.getInt("daBan");
                    c.tyLeLapDay = c.tongGhe == 0 ? 0 : (c.daBan * 100.0 / c.tongGhe);
                    c.doanhThu = rs.getLong("doanhThu");
                    ds.add(c);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    // ==========================================================
    // 4. HIỆU SUẤT CỦA TÔI (cho Nhân viên)
    // ==========================================================

    /**
     * Tóm tắt cho 1 nhân viên cụ thể.
     */
    public TomTat layTomTatNhanVien(String maNhanVien, LocalDate tuNgay, LocalDate denNgay) {
        TomTat tt = new TomTat();

        String sql =
                "SELECT COUNT(*) AS tongVe, ISNULL(SUM(tongTien), 0) AS tongTien " +
                "FROM Ve " +
                "WHERE nhanVien = ? " +
                "  AND ngayBan >= ? AND ngayBan < DATEADD(DAY, 1, ?) " +
                "  AND trangThaiVe LIKE N'Đã thanh toán%'";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maNhanVien);
            ps.setTimestamp(2, Timestamp.valueOf(tuNgay.atStartOfDay()));
            ps.setTimestamp(3, Timestamp.valueOf(denNgay.atStartOfDay()));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tt.tongVe = rs.getInt("tongVe");
                    tt.tongDoanhThu = rs.getLong("tongTien");
                    tt.trungBinhMoiVe = tt.tongVe == 0 ? 0 : tt.tongDoanhThu / tt.tongVe;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tt;
    }

    /**
     * Doanh thu theo ngày cho 1 nhân viên cụ thể.
     */
    public List<DoanhThuTheoNgay> layDoanhThuTheoNgayCuaNhanVien(
            String maNhanVien, LocalDate tuNgay, LocalDate denNgay) {

        List<DoanhThuTheoNgay> ds = new ArrayList<>();

        String sql =
                "SELECT CAST(ngayBan AS DATE) AS ngay, " +
                "       COUNT(*) AS soVe, " +
                "       SUM(tongTien) AS doanhThu " +
                "FROM Ve " +
                "WHERE nhanVien = ? " +
                "  AND ngayBan >= ? AND ngayBan < DATEADD(DAY, 1, ?) " +
                "  AND trangThaiVe LIKE N'Đã thanh toán%' " +
                "GROUP BY CAST(ngayBan AS DATE) " +
                "ORDER BY ngay DESC";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maNhanVien);
            ps.setTimestamp(2, Timestamp.valueOf(tuNgay.atStartOfDay()));
            ps.setTimestamp(3, Timestamp.valueOf(denNgay.atStartOfDay()));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DoanhThuTheoNgay d = new DoanhThuTheoNgay();
                    d.ngay = rs.getDate("ngay").toLocalDate();
                    d.soVe = rs.getInt("soVe");
                    d.doanhThu = rs.getLong("doanhThu");
                    ds.add(d);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ds;
    }

    // ==========================================================
    // CÁC METHOD CHO DASHBOARD TRANG CHỦ
    // ==========================================================

    /**
     * Đếm số vé bị đổi hoặc trả trong NGÀY HÔM NAY.
     */
    public int laySoVeDoiTraHomNay() {
        String sql =
                "SELECT COUNT(*) FROM Ve " +
                "WHERE CAST(ngayBan AS DATE) = CAST(GETDATE() AS DATE) " +
                "  AND (trangThaiVe = N'Đã trả' OR trangThaiVe LIKE N'Đã đổi%')";

        try (
            java.sql.Connection con = com.mycompany.prj1.ConnectDB.DB.getConnection();
            java.sql.PreparedStatement ps = con.prepareStatement(sql);
            java.sql.ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Tính tỉ lệ lấp đầy trung bình của tất cả chuyến trong ngày được chọn.
     * = SUM(số ghế đã bán) / SUM(tổng sức chứa) * 100
     */
    public double layTiLeLapDay(LocalDate ngay) {
        String sql =
                "SELECT " +
                "  ISNULL(SUM(daBan), 0) AS tongDaBan, " +
                "  ISNULL(SUM(tongGhe), 0) AS tongGhe " +
                "FROM ( " +
                "  SELECT " +
                "    ct.maChuyenTau, " +
                "    ISNULL((SELECT SUM(tt.sucChua) FROM ToaTau tt WHERE tt.tau = ct.tau), 0) AS tongGhe, " +
                "    ISNULL((SELECT COUNT(*) FROM Ve v " +
                "            WHERE v.chuyenTau = ct.maChuyenTau " +
                "              AND v.trangThaiVe LIKE N'Đã thanh toán%'), 0) AS daBan " +
                "  FROM ChuyenTau ct " +
                "  WHERE CAST(ct.ngayKhoiHanh AS DATE) = ? " +
                ") t";

        try (
            java.sql.Connection con = com.mycompany.prj1.ConnectDB.DB.getConnection();
            java.sql.PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setDate(1, java.sql.Date.valueOf(ngay));
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    long daBan = rs.getLong("tongDaBan");
                    long tongGhe = rs.getLong("tongGhe");
                    if (tongGhe == 0) return 0;
                    return (double) daBan * 100 / tongGhe;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Lấy doanh thu theo 4 khung giờ trong ngày:
     *   [0] Sáng  6-10h
     *   [1] Trưa 11-13h
     *   [2] Chiều 14-17h
     *   [3] Tối  18-21h
     */
    public long[] layDoanhThuTheoKhungGio(LocalDate ngay) {
        long[] kq = new long[4];

        String sql =
                "SELECT DATEPART(HOUR, ngayBan) AS gio, ISNULL(SUM(tongTien), 0) AS doanhThu " +
                "FROM Ve " +
                "WHERE CAST(ngayBan AS DATE) = ? " +
                "  AND trangThaiVe LIKE N'Đã thanh toán%' " +
                "GROUP BY DATEPART(HOUR, ngayBan)";

        try (
            java.sql.Connection con = com.mycompany.prj1.ConnectDB.DB.getConnection();
            java.sql.PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setDate(1, java.sql.Date.valueOf(ngay));
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int gio = rs.getInt("gio");
                    long dt = rs.getLong("doanhThu");
                    if (gio >= 6 && gio <= 10) kq[0] += dt;
                    else if (gio >= 11 && gio <= 13) kq[1] += dt;
                    else if (gio >= 14 && gio <= 17) kq[2] += dt;
                    else if (gio >= 18 && gio <= 21) kq[3] += dt;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kq;
    }
}