package com.mycompany.prj1.DAO;

import com.mycompany.prj1.ConnectDB.DB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho chức năng Quản lý bán vé.
 * Bao gồm: TRẢ VÉ và ĐỔI VÉ.
 *
 * Yêu cầu: bảng Ve đã có cột ghiChu (chạy ALTER TABLE trước):
 *   ALTER TABLE Ve ADD ghiChu NVARCHAR(255) NULL;
 */
public class DAO_BANVE {

    /** DTO chi tiết 1 vé */
    public static class ChiTietVe {
        public String maVe;
        public String trangThaiVe;
        public LocalDateTime ngayBan;
        public long tongTien;
        public String ghiChu;

        // Hành khách
        public String maHanhKhach;
        public String hoTenHanhKhach;
        public String soGiayTo;
        public String sdt;

        // Chuyến
        public String maChuyenTau;
        public String tenChuyen;
        public LocalDateTime ngayKhoiHanh;
        public LocalDateTime ngayDenDuKien;
        public String tenGaDi;
        public String tenGaDen;
        public String maTau;

        // Ghế / loại vé / KM
        public String soGhe;
        public String maLoaiVe;
        public String tenLoaiVe;
        public String maKhuyenMai;

        // Nhân viên đã bán
        public String maNhanVien;
    }

    /** Kết quả tính phí trả vé */
    public static class KetQuaPhiTraVe {
        public boolean choPhep;
        public String lyDoTuChoi;
        public long tienVe;
        public long phiTraVe;
        public long tienHoan;
        public double tyLePhat;
        public Duration thoiGianDenChuyen;
    }

    /** DTO chuyến tàu - dùng cho dropdown đổi vé */
    public static class ChuyenTuongLai {
        public String maChuyenTau;
        public String tenChuyen;
        public LocalDateTime ngayKhoiHanh;
        public String tenGaDi;
        public String tenGaDen;
        public String maTau;
        public String hienThi;

        @Override public String toString() { return hienThi; }
    }

    /** DTO phương thức thanh toán */
    public static class PhuongThuc {
        public String maPhuongThuc;
        public String tenPhuongThuc;
        @Override public String toString() { return tenPhuongThuc; }
    }

    // ==========================================================
    // 1. ĐỌC THÔNG TIN VÉ
    // ==========================================================

    public ChiTietVe layChiTietVe(String maVe) {
        String sql =
                "SELECT v.maVe, v.trangThaiVe, v.ngayBan, v.tongTien, v.ghiChu, " +
                "       v.nhanVien, v.soGhe, v.loaiVe, v.khuyenMai, " +
                "       hk.maHanhKhach, hk.hoTenHK, hk.soGiayTo, hk.sdt, " +
                "       ct.maChuyenTau, ct.tenChuyen, ct.ngayKhoiHanh, ct.ngayDenDuKien, ct.tau, " +
                "       gaDi.tenGa AS tenGaDi, gaDen.tenGa AS tenGaDen, " +
                "       lv.tenLoaiVe " +
                "FROM Ve v " +
                "JOIN HanhKhach hk ON v.hanhKhach = hk.maHanhKhach " +
                "JOIN ChuyenTau ct ON v.chuyenTau = ct.maChuyenTau " +
                "JOIN Ga gaDi  ON ct.gaDi  = gaDi.maGa " +
                "JOIN Ga gaDen ON ct.gaDen = gaDen.maGa " +
                "JOIN LoaiVe lv ON v.loaiVe = lv.maLoaiVe " +
                "WHERE v.maVe = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maVe);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ChiTietVe v = new ChiTietVe();
                    v.maVe = rs.getString("maVe");
                    v.trangThaiVe = rs.getString("trangThaiVe");
                    v.ngayBan = rs.getTimestamp("ngayBan").toLocalDateTime();
                    v.tongTien = rs.getLong("tongTien");
                    v.ghiChu = rs.getString("ghiChu");
                    v.maHanhKhach = rs.getString("maHanhKhach");
                    v.hoTenHanhKhach = rs.getString("hoTenHK");
                    v.soGiayTo = rs.getString("soGiayTo");
                    v.sdt = rs.getString("sdt");
                    v.maChuyenTau = rs.getString("maChuyenTau");
                    v.tenChuyen = rs.getString("tenChuyen");
                    v.ngayKhoiHanh = rs.getTimestamp("ngayKhoiHanh").toLocalDateTime();
                    v.ngayDenDuKien = rs.getTimestamp("ngayDenDuKien").toLocalDateTime();
                    v.tenGaDi = rs.getString("tenGaDi");
                    v.tenGaDen = rs.getString("tenGaDen");
                    v.maTau = rs.getString("tau");
                    v.soGhe = rs.getString("soGhe");
                    v.maLoaiVe = rs.getString("loaiVe");
                    v.tenLoaiVe = rs.getString("tenLoaiVe");
                    v.maKhuyenMai = rs.getString("khuyenMai");
                    v.maNhanVien = rs.getString("nhanVien");
                    return v;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ==========================================================
    // 2. PHƯƠNG THỨC THANH TOÁN (chỉ tiền mặt + chuyển khoản)
    // ==========================================================

    /**
     * Lấy danh sách phương thức KHÔNG bao gồm ví điện tử.
     */
    public List<PhuongThuc> layPhuongThucThanhToan() {
        List<PhuongThuc> ds = new ArrayList<>();
        String sql =
                "SELECT maPhuongThuc, tenPhuongThuc FROM PhuongThucThanhToan " +
                "WHERE maPhuongThuc <> 'VI_DIEN_TU' " +
                "  AND tenPhuongThuc NOT LIKE N'%ví%' " +
                "ORDER BY tenPhuongThuc";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                PhuongThuc p = new PhuongThuc();
                p.maPhuongThuc = rs.getString("maPhuongThuc");
                p.tenPhuongThuc = rs.getString("tenPhuongThuc");
                ds.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    // ==========================================================
    // 3. TÍNH PHÍ TRẢ VÉ
    // ==========================================================

    /**
     * Tính phí trả vé:
     *   T > 24h        → 10% (min 10.000)
     *   4h ≤ T ≤ 24h   → 25%
     *   T < 4h         → KHÔNG hoàn
     */
    public KetQuaPhiTraVe tinhPhiTraVe(ChiTietVe ve) {
        KetQuaPhiTraVe kq = new KetQuaPhiTraVe();
        kq.tienVe = ve.tongTien;

        // Check trạng thái
        if (ve.trangThaiVe == null || !ve.trangThaiVe.startsWith("Đã thanh toán")) {
            kq.choPhep = false;
            kq.lyDoTuChoi = "Chỉ vé đã thanh toán mới được trả. Vé hiện ở trạng thái: \""
                    + ve.trangThaiVe + "\"";
            return kq;
        }

        // Check thời gian khởi hành
        LocalDateTime now = LocalDateTime.now();
        if (!ve.ngayKhoiHanh.isAfter(now)) {
            kq.choPhep = false;
            kq.lyDoTuChoi = "Chuyến tàu đã khởi hành. Không thể trả vé.";
            return kq;
        }

        Duration T = Duration.between(now, ve.ngayKhoiHanh);
        kq.thoiGianDenChuyen = T;
        long tongGio = T.toHours();

        if (tongGio < 4) {
            kq.choPhep = false;
            kq.lyDoTuChoi = "Còn dưới 4 giờ đến giờ tàu chạy. Hệ thống không hoàn tiền.";
            return kq;
        }

        if (tongGio > 24) {
            kq.tyLePhat = 10.0;
            // LÀM TRÒN XUỐNG cho phí phạt → tiền hoàn cao hơn (có lợi cho khách)
            kq.phiTraVe = (long) Math.floor(ve.tongTien * 0.10);
            if (kq.phiTraVe < 10000) kq.phiTraVe = 10000;
        } else {
            // 4h ≤ T ≤ 24h → 25%
            kq.tyLePhat = 25.0;
            kq.phiTraVe = (long) Math.floor(ve.tongTien * 0.25);
        }

        kq.tienHoan = kq.tienVe - kq.phiTraVe;
        if (kq.tienHoan < 0) kq.tienHoan = 0;
        // Làm tròn xuống về bội số 1000 cho dễ giao dịch tiền mặt
        kq.tienHoan = (kq.tienHoan / 1000) * 1000;
        kq.choPhep = true;
        return kq;
    }

    // ==========================================================
    // 4. THỰC HIỆN TRẢ VÉ
    // ==========================================================

    public boolean traVe(String maVe, long tienHoan, String maPhuongThuc, String maNhanVien) {
        String now = LocalDateTime.now().toString().substring(0, 16);
        String ghiChu = String.format(
                "Đã trả ngày %s | Hoàn: %,d VNĐ | PT: %s | NV: %s",
                now, tienHoan, maPhuongThuc, maNhanVien);

        String sql = "UPDATE Ve SET trangThaiVe = N'Đã trả', ghiChu = ? WHERE maVe = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, ghiChu);
            ps.setString(2, maVe);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // ==========================================================
    // 5. KIỂM TRA ĐỔI VÉ
    // ==========================================================

    /**
     * Trả về null nếu OK, hoặc message lỗi.
     */
    public String kiemTraDuocDoiVe(ChiTietVe ve) {
        if (ve == null) return "Không tìm thấy vé.";

        if (ve.trangThaiVe == null || !ve.trangThaiVe.startsWith("Đã thanh toán")) {
            return "Chỉ vé đã thanh toán mới được đổi. Trạng thái hiện tại: \"" + ve.trangThaiVe + "\"";
        }

        if (!ve.ngayKhoiHanh.isAfter(LocalDateTime.now())) {
            return "Chuyến tàu đã khởi hành. Không thể đổi vé.";
        }

        // Check vé này có phải là vé tạo từ đổi không (đã đổi 1 lần rồi)
        if (ve.ghiChu != null && ve.ghiChu.contains("Đổi từ vé")) {
            return "Vé này được tạo từ một lần đổi vé trước đó. Mỗi vé chỉ được đổi tối đa 1 lần.";
        }

        return null;
    }

    // ==========================================================
    // 6. LẤY CHUYẾN ĐỂ ĐỔI VÉ
    // ==========================================================

    public List<ChuyenTuongLai> layChuyenCoTheDoi() {
        List<ChuyenTuongLai> ds = new ArrayList<>();
        String sql =
                "SELECT ct.maChuyenTau, ct.tenChuyen, ct.ngayKhoiHanh, ct.tau, " +
                "       gaDi.tenGa AS tenGaDi, gaDen.tenGa AS tenGaDen " +
                "FROM ChuyenTau ct " +
                "JOIN Ga gaDi  ON ct.gaDi  = gaDi.maGa " +
                "JOIN Ga gaDen ON ct.gaDen = gaDen.maGa " +
                "WHERE ct.ngayKhoiHanh > GETDATE() " +
                "  AND ct.trangThaiChuyen = 1 " +
                "ORDER BY ct.ngayKhoiHanh";

        java.time.format.DateTimeFormatter fmt =
                java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                ChuyenTuongLai c = new ChuyenTuongLai();
                c.maChuyenTau = rs.getString("maChuyenTau");
                c.tenChuyen = rs.getString("tenChuyen");
                c.ngayKhoiHanh = rs.getTimestamp("ngayKhoiHanh").toLocalDateTime();
                c.tenGaDi = rs.getString("tenGaDi");
                c.tenGaDen = rs.getString("tenGaDen");
                c.maTau = rs.getString("tau");
                c.hienThi = String.format("%s - %s (%s → %s) - %s",
                        c.maChuyenTau, c.tenChuyen, c.tenGaDi, c.tenGaDen,
                        c.ngayKhoiHanh.format(fmt));
                ds.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Lấy danh sách ghế CÒN TRỐNG của 1 chuyến.
     * Format ghế phải khớp với cách bán vé hiện tại lưu vào Ve.soGhe:
     *   "<tenToa> - Ghế <số 2 chữ số>"
     * Ví dụ: "Toa 1 - Ghế 01", "Toa 1 - Ghế 02", "Toa 2 - Ghế 01"...
     */
    public List<String> layGheTrong(String maChuyenTau) {
        List<String> ghetrong = new ArrayList<>();

        // Lấy ghế đã bán hoặc đã đổi
        java.util.Set<String> daBan = new java.util.HashSet<>();
        String sqlDaBan =
                "SELECT soGhe FROM Ve " +
                "WHERE chuyenTau = ? " +
                "  AND (trangThaiVe LIKE N'Đã thanh toán%' " +
                "       OR trangThaiVe LIKE N'Đã đổi%')";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sqlDaBan)
        ) {
            ps.setString(1, maChuyenTau);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) daBan.add(rs.getString("soGhe"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Cũng tính cả ghế đang giữ chỗ tạm
        String sqlGiuCho =
                "SELECT soGhe FROM GiuChoGhe " +
                "WHERE maChuyenTau = ? " +
                "  AND thoiGianHetHan > GETDATE()";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sqlGiuCho)
        ) {
            ps.setString(1, maChuyenTau);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) daBan.add(rs.getString("soGhe"));
            }
        } catch (Exception e) {
            // Bảng có thể không có hoặc cột tên khác - bỏ qua
            e.printStackTrace();
        }

        // Lấy danh sách toa của chuyến
        String sqlToa =
                "SELECT tt.maToa, tt.tenToa, tt.sucChua, lg.tenLoaiGhe " +
                "FROM ToaTau tt " +
                "JOIN ChuyenTau ct ON tt.tau = ct.tau " +
                "JOIN LoaiGhe lg ON tt.loaiGhe = lg.maLoaiGhe " +
                "WHERE ct.maChuyenTau = ? " +
                "ORDER BY tt.tenToa";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sqlToa)
        ) {
            ps.setString(1, maChuyenTau);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int sucChua = rs.getInt("sucChua");
                    String tenToa = rs.getString("tenToa");
                    // Format phải khớp với jf1.java dòng 3436:
                    //   maGhe = toa.getTenToa() + " - Ghế " + String.format("%02d", i)
                    // Trong DB tenToa = "1", "2"... (KHÔNG có "Toa " đằng trước)
                    for (int g = 1; g <= sucChua; g++) {
                        String soGhe = tenToa + " - Ghế " + String.format("%02d", g);
                        if (!daBan.contains(soGhe)) {
                            ghetrong.add(soGhe);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ghetrong;
    }

    /**
     * Sinh mã vé mới: V001, V002... (dùng MAX + 1 đơn giản, schema có sequence riêng nhưng để đơn giản)
     */
    public String sinhMaVeMoi() {
        String sql = "SELECT MAX(maVe) FROM Ve WHERE maVe LIKE 'V%'";
        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {
            if (rs.next()) {
                String max = rs.getString(1);
                if (max == null) return "V001";
                try {
                    int n = Integer.parseInt(max.substring(1)) + 1;
                    return String.format("V%03d", n);
                } catch (NumberFormatException e) {
                    // fallback - dùng timestamp
                    return "V" + (System.currentTimeMillis() % 100000);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "V001";
    }

    // ==========================================================
    // 6.A LẤY HỆ SỐ LOẠI GHẾ - dùng để tính giá vé mới khi đổi
    // ==========================================================

    /** Thông tin loại ghế của 1 ghế cụ thể trên 1 chuyến */
    public static class ThongTinLoaiGhe {
        public String tenToa;        // "Toa 1"
        public String maLoaiGhe;     // "LG_NM"
        public String tenLoaiGhe;    // "Ngồi mềm"
        public double heSoLoaiGhe;   // 1.0
    }

    /**
     * Lấy thông tin loại ghế của 1 ghế trên 1 chuyến cụ thể.
     *
     * Cách hoạt động:
     *   - Tách "Toa 1" từ chuỗi "Toa 1 - Ghế 03"
     *   - JOIN ToaTau với chuyến đó để tìm toa có tenToa = "Toa 1"
     *   - JOIN tiếp với LoaiGhe để lấy hệ số
     */
    public ThongTinLoaiGhe layThongTinLoaiGheCuaGhe(String maChuyenTau, String soGhe) {
        // soGhe format: "1 - Ghế 03" hoặc "Toa 1 - Ghế 03" (tùy lịch sử data)
        // → tách ra lấy tên toa để so sánh với ToaTau.tenToa (đang là "1", "2"...)
        String tenToa = soGhe;
        int idx = soGhe.indexOf(" - Ghế ");
        if (idx > 0) {
            tenToa = soGhe.substring(0, idx);
        }
        // Nếu tenToa vẫn có "Toa " ở đầu (data cũ) → bỏ đi để match DB
        if (tenToa.startsWith("Toa ")) {
            tenToa = tenToa.substring(4);
        }

        String sql =
                "SELECT tt.tenToa, lg.maLoaiGhe, lg.tenLoaiGhe, lg.heSoLoaiGhe " +
                "FROM ToaTau tt " +
                "JOIN ChuyenTau ct ON tt.tau = ct.tau " +
                "JOIN LoaiGhe lg ON tt.loaiGhe = lg.maLoaiGhe " +
                "WHERE ct.maChuyenTau = ? AND tt.tenToa = ?";

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql)
        ) {
            ps.setString(1, maChuyenTau);
            ps.setString(2, tenToa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    ThongTinLoaiGhe t = new ThongTinLoaiGhe();
                    t.tenToa = rs.getString("tenToa");
                    t.maLoaiGhe = rs.getString("maLoaiGhe");
                    t.tenLoaiGhe = rs.getString("tenLoaiGhe");
                    t.heSoLoaiGhe = rs.getDouble("heSoLoaiGhe");
                    return t;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Tính giá vé mới khi đổi ghế dựa trên hệ số loại ghế.
     *
     * Công thức: giáMới = giáCũ × (hệSốMới / hệSốCũ)
     *
     * Trả về -1 nếu không tính được (ghế không tồn tại, không lấy được loại).
     */
    public long tinhGiaVeKhiDoiGhe(String maChuyenTau, String soGheCu, long giaCu, String soGheMoi) {
        ThongTinLoaiGhe lgCu = layThongTinLoaiGheCuaGhe(maChuyenTau, soGheCu);
        ThongTinLoaiGhe lgMoi = layThongTinLoaiGheCuaGhe(maChuyenTau, soGheMoi);

        if (lgCu == null || lgMoi == null) return -1;
        if (lgCu.heSoLoaiGhe <= 0) return giaCu;  // tránh chia 0

        // Tính giá mới
        double giaMoi = giaCu * lgMoi.heSoLoaiGhe / lgCu.heSoLoaiGhe;

        // Làm tròn xuống bội số 1000 cho dễ giao dịch
        long giaMoiLong = (long) Math.floor(giaMoi);
        giaMoiLong = (giaMoiLong / 1000) * 1000;
        return giaMoiLong;
    }

    // ==========================================================
    // 6.B TRA CỨU VÉ - tìm theo mã (LIKE)
    // ==========================================================
    /** Tóm tắt vé cho bảng tra cứu */
    public static class TomTatVe {
        public String maVe;
        public LocalDateTime ngayBan;
        public String hoTen;
        public String tenChuyen;
        public LocalDateTime ngayKhoiHanh;
        public String soGhe;
        public long tongTien;
        public String trangThaiVe;
    }

    /**
     * Tìm vé linh hoạt theo nhiều tiêu chí (AND).
     * Để null hoặc rỗng cho tiêu chí nào không muốn dùng.
     *
     * @param maVe        Mã vé (tìm chứa)
     * @param hoTen       Họ tên hành khách (tìm chứa, bỏ qua hoa thường)
     * @param soGiayTo    Số giấy tờ (tìm chứa)
     * @param sdt         SĐT (tìm chứa)
     */
    public List<TomTatVe> tracuuVe(String maVe, String hoTen, String soGiayTo, String sdt) {
        List<TomTatVe> ds = new ArrayList<>();

        StringBuilder sql = new StringBuilder(
                "SELECT v.maVe, v.ngayBan, v.soGhe, v.tongTien, v.trangThaiVe, " +
                "       hk.hoTenHK, hk.soGiayTo, hk.sdt, " +
                "       ct.tenChuyen, ct.ngayKhoiHanh " +
                "FROM Ve v " +
                "JOIN HanhKhach hk ON v.hanhKhach = hk.maHanhKhach " +
                "JOIN ChuyenTau ct ON v.chuyenTau = ct.maChuyenTau " +
                "WHERE 1=1 ");

        List<String> params = new ArrayList<>();
        if (maVe != null && !maVe.trim().isEmpty()) {
            sql.append(" AND v.maVe LIKE ? ");
            params.add("%" + maVe.trim() + "%");
        }
        if (hoTen != null && !hoTen.trim().isEmpty()) {
            sql.append(" AND hk.hoTenHK LIKE ? ");
            params.add("%" + hoTen.trim() + "%");
        }
        if (soGiayTo != null && !soGiayTo.trim().isEmpty()) {
            sql.append(" AND hk.soGiayTo LIKE ? ");
            params.add("%" + soGiayTo.trim() + "%");
        }
        if (sdt != null && !sdt.trim().isEmpty()) {
            sql.append(" AND hk.sdt LIKE ? ");
            params.add("%" + sdt.trim() + "%");
        }

        sql.append(" ORDER BY v.ngayBan DESC ");

        try (
            Connection con = DB.getConnection();
            PreparedStatement ps = con.prepareStatement(sql.toString())
        ) {
            for (int i = 0; i < params.size(); i++) {
                ps.setString(i + 1, params.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    TomTatVe t = new TomTatVe();
                    t.maVe = rs.getString("maVe");
                    t.ngayBan = rs.getTimestamp("ngayBan").toLocalDateTime();
                    t.hoTen = rs.getString("hoTenHK");
                    t.tenChuyen = rs.getString("tenChuyen");
                    t.ngayKhoiHanh = rs.getTimestamp("ngayKhoiHanh").toLocalDateTime();
                    t.soGhe = rs.getString("soGhe");
                    t.tongTien = rs.getLong("tongTien");
                    t.trangThaiVe = rs.getString("trangThaiVe");
                    ds.add(t);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    /**
     * Cũ - giữ lại để code khác (đang gọi) không bị hỏng.
     * Internal: gọi tracuuVe() với chỉ tiêu chí mã.
     */
    public List<TomTatVe> tracuuTheoMa(String tuKhoa) {
        return tracuuVe(tuKhoa, null, null, null);
    }

    // ==========================================================
    // 7. THỰC HIỆN ĐỔI VÉ (transaction)
    // ==========================================================
    /**
     * Đổi vé: vé cũ → vé mới (cùng hành khách, có thể khác chuyến/ghế).
     * @param chenhLech > 0: thu thêm; < 0: hoàn lại; = 0: ngang giá
     * @return mã vé mới nếu thành công, null nếu thất bại
     */
    public String doiVe(
            String maVeCu,
            String maChuyenTauMoi,
            String soGheMoi,
            String maLoaiVeMoi,
            String maKhuyenMaiMoi,
            long giaVeMoi,
            long chenhLech,
            String maPhuongThuc,
            String maNhanVien
    ) {
        Connection con = null;
        try {
            con = DB.getConnection();
            con.setAutoCommit(false);

            // 1) Lấy maHanhKhach từ vé cũ
            String maHanhKhach = null;
            try (PreparedStatement ps = con.prepareStatement(
                    "SELECT hanhKhach FROM Ve WHERE maVe = ?")) {
                ps.setString(1, maVeCu);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) maHanhKhach = rs.getString("hanhKhach");
                }
            }
            if (maHanhKhach == null) { con.rollback(); return null; }

            // 2) Ghi chú
            String now = LocalDateTime.now().toString().substring(0, 16);
            String ghiChuMoi = String.format(
                    "Đổi từ vé %s | Chênh lệch: %s%,d VNĐ | PT: %s | NV: %s",
                    maVeCu,
                    chenhLech >= 0 ? "+" : "",
                    chenhLech,
                    maPhuongThuc, maNhanVien);

            // 3) INSERT vé mới - KHÔNG truyền maVe, để DB tự sinh qua DEFAULT SeqVe
            //    ('VE' + NEXT VALUE FOR SeqVe)
            String sqlThemVe =
                    "INSERT INTO Ve " +
                    "(ngayBan, trangThaiVe, nhanVien, hanhKhach, " +
                    " soGhe, loaiVe, chuyenTau, khuyenMai, tongTien, ghiChu) " +
                    "OUTPUT INSERTED.maVe " +
                    "VALUES (GETDATE(), N'Đã thanh toán', ?, ?, ?, ?, ?, ?, ?, ?)";

            String maVeMoi = null;
            try (PreparedStatement ps = con.prepareStatement(sqlThemVe)) {
                ps.setString(1, maNhanVien);
                ps.setString(2, maHanhKhach);
                ps.setString(3, soGheMoi);
                ps.setString(4, maLoaiVeMoi);
                ps.setString(5, maChuyenTauMoi);
                if (maKhuyenMaiMoi == null || maKhuyenMaiMoi.isEmpty()) {
                    ps.setNull(6, java.sql.Types.VARCHAR);
                } else {
                    ps.setString(6, maKhuyenMaiMoi);
                }
                ps.setLong(7, giaVeMoi);
                ps.setString(8, ghiChuMoi);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) maVeMoi = rs.getString("maVe");
                }
            }

            if (maVeMoi == null) { con.rollback(); return null; }

            // 4) UPDATE vé cũ → "Đã đổi"
            String ghiChuCu = String.format("Đã đổi sang vé %s ngày %s", maVeMoi, now);
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE Ve SET trangThaiVe = N'Đã đổi', ghiChu = ? WHERE maVe = ?")) {
                ps.setString(1, ghiChuCu);
                ps.setString(2, maVeCu);
                int rows = ps.executeUpdate();
                if (rows == 0) { con.rollback(); return null; }
            }

            con.commit();
            return maVeMoi;

        } catch (Exception e) {
            e.printStackTrace();
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            return null;
        } finally {
            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (Exception e) {}
        }
    }
}