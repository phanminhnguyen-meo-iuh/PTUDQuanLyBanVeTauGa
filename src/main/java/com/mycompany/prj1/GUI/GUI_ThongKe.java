package com.mycompany.prj1.GUI;

import com.mycompany.prj1.DAO.DAO_THONGKE;
import com.mycompany.prj1.DAO.DAO_THONGKE.DoanhThuTheoNgay;
import com.mycompany.prj1.DAO.DAO_THONGKE.HieuSuatNhanVien;
import com.mycompany.prj1.DAO.DAO_THONGKE.ThongKeChuyen;
import com.mycompany.prj1.DAO.DAO_THONGKE.TomTat;
import com.toedter.calendar.JDateChooser;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * Giao diện thống kê.
 *
 * Bố cục:
 *   - WEST (sidebar):  các tab thống kê
 *   - CENTER:          panel hiển thị nội dung tab đang chọn (CardLayout)
 *
 * Phân quyền:
 *   - Quản lý: thấy 3 tab (Doanh thu / Nhân viên / Chuyến tàu)
 *   - Nhân viên: chỉ thấy 1 tab "Hiệu suất của tôi"
 */
public class GUI_ThongKe extends JPanel {

    private final DAO_THONGKE dao = new DAO_THONGKE();
    private final String maNhanVienHienTai;
    private final boolean laQuanLy;

    // Định dạng tiền VN
    private final NumberFormat dinhDangTien =
            NumberFormat.getInstance(new Locale("vi", "VN"));

    private final DateTimeFormatter fmtNgay = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter fmtNgayGio = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Panel chính
    private JPanel pnContent;
    private CardLayout cardLayout;

    // Các panel con
    private PanelDoanhThu pnDoanhThu;
    private PanelNhanVien pnNhanVien;
    private PanelChuyenTau pnChuyenTau;
    private PanelCuaToi pnCuaToi;

    public GUI_ThongKe(String maNhanVien, boolean laQuanLy) {
        this.maNhanVienHienTai = maNhanVien;
        this.laQuanLy = laQuanLy;

        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        khoiTaoNoiDung();
    }

    // ==========================================================
    // PHẦN NỘI DUNG (CENTER) - dùng CardLayout
    // ==========================================================

    private void khoiTaoNoiDung() {
        cardLayout = new CardLayout();
        pnContent = new JPanel(cardLayout);
        pnContent.setBackground(new Color(245, 247, 250));

        // Trang chủ thống kê - menu chính chọn loại báo cáo
        PanelTrangChu pnTrangChu = new PanelTrangChu();
        pnContent.add(pnTrangChu, "trangChu");

        if (laQuanLy) {
            pnDoanhThu = new PanelDoanhThu();
            pnNhanVien = new PanelNhanVien();
            pnChuyenTau = new PanelChuyenTau();

            pnContent.add(pnDoanhThu, "doanhThu");
            pnContent.add(pnNhanVien, "nhanVien");
            pnContent.add(pnChuyenTau, "chuyenTau");
        } else {
            pnCuaToi = new PanelCuaToi();
            pnContent.add(pnCuaToi, "cuaToi");
        }

        // Mặc định hiển thị Trang chủ thống kê
        cardLayout.show(pnContent, "trangChu");

        add(pnContent, BorderLayout.CENTER);
    }

    // ==========================================================
    // CÁC HÀM TIỆN ÍCH
    // ==========================================================

    /** Tạo card tóm tắt (3 ô đầu màn hình) */
    private JPanel taoCardTomTat(String tieuDe, String giaTri, Color mauNen) {
        JPanel pn = new JPanel(new BorderLayout(8, 8));
        pn.setBackground(mauNen);
        pn.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        JLabel lblTitle = new JLabel(tieuDe);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitle.setForeground(new Color(255, 255, 255, 200));

        JLabel lblValue = new JLabel(giaTri);
        lblValue.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblValue.setForeground(Color.WHITE);

        pn.add(lblTitle, BorderLayout.NORTH);
        pn.add(lblValue, BorderLayout.CENTER);

        return pn;
    }

    /** Bộ lọc thời gian (dùng chung cho các panel) */
    private JPanel taoFilterThoiGian(JDateChooser dcTu, JDateChooser dcDen, JButton btnLoc) {
        JPanel pn = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        pn.setOpaque(false);
        pn.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JLabel lblTu = new JLabel("Từ ngày:");
        lblTu.setFont(new Font("Segoe UI", Font.BOLD, 12));

        JLabel lblDen = new JLabel("Đến ngày:");
        lblDen.setFont(new Font("Segoe UI", Font.BOLD, 12));

        dcTu.setPreferredSize(new Dimension(140, 30));
        dcDen.setPreferredSize(new Dimension(140, 30));
        dcTu.setDateFormatString("dd/MM/yyyy");
        dcDen.setDateFormatString("dd/MM/yyyy");

        // Mặc định: 30 ngày gần đây
        dcTu.setDate(java.sql.Date.valueOf(LocalDate.now().minusDays(30)));
        dcDen.setDate(new Date());

        btnLoc.setBackground(new Color(0, 123, 255));
        btnLoc.setForeground(Color.WHITE);
        btnLoc.setFocusPainted(false);
        btnLoc.setBorderPainted(false);
        btnLoc.setOpaque(true);
        btnLoc.setPreferredSize(new Dimension(80, 30));
        btnLoc.setFont(new Font("Segoe UI", Font.BOLD, 12));

        pn.add(lblTu);
        pn.add(dcTu);
        pn.add(lblDen);
        pn.add(dcDen);
        pn.add(btnLoc);

        // Các nút quick filter
        pn.add(Box.createHorizontalStrut(20));
        pn.add(taoNutQuickFilter("Hôm nay", dcTu, dcDen, btnLoc, 0));
        pn.add(taoNutQuickFilter("7 ngày", dcTu, dcDen, btnLoc, 7));
        pn.add(taoNutQuickFilter("30 ngày", dcTu, dcDen, btnLoc, 30));
        pn.add(taoNutQuickFilter("Tháng này", dcTu, dcDen, btnLoc, -1));

        return pn;
    }

    private JButton taoNutQuickFilter(String text, JDateChooser dcTu, JDateChooser dcDen,
                                      JButton btnLoc, int soNgay) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btn.setPreferredSize(new Dimension(80, 30));
        btn.setFocusPainted(false);

        btn.addActionListener(e -> {
            LocalDate tu, den = LocalDate.now();
            if (soNgay == 0) {
                tu = LocalDate.now();
            } else if (soNgay == -1) {
                // tháng này
                tu = LocalDate.now().withDayOfMonth(1);
            } else {
                tu = LocalDate.now().minusDays(soNgay);
            }
            dcTu.setDate(java.sql.Date.valueOf(tu));
            dcDen.setDate(java.sql.Date.valueOf(den));
            btnLoc.doClick();  // tự bấm Lọc
        });

        return btn;
    }

    private LocalDate layNgayTu(JDateChooser dc) {
        if (dc.getDate() == null) return LocalDate.now().minusDays(30);
        return dc.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * Tạo header có tiêu đề + nút quay lại bên trái.
     * Dùng chung cho 4 panel chi tiết.
     */
    private JPanel taoHeaderCoNutQuayLai(String tieuDe) {
        JPanel pnHeader = new JPanel(new BorderLayout(12, 0));
        pnHeader.setOpaque(false);
        pnHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JButton btnQuayLai = new JButton("← Quay lại");
        btnQuayLai.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnQuayLai.setBackground(new Color(108, 117, 125));
        btnQuayLai.setForeground(Color.WHITE);
        btnQuayLai.setFocusPainted(false);
        btnQuayLai.setBorderPainted(false);
        btnQuayLai.setOpaque(true);
        btnQuayLai.setPreferredSize(new Dimension(120, 36));
        btnQuayLai.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
        btnQuayLai.addActionListener(e -> cardLayout.show(pnContent, "trangChu"));

        // Wrap nút trong panel để không bị stretch
        JPanel pnNut = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
        pnNut.setOpaque(false);
        pnNut.add(btnQuayLai);

        JLabel lblTitle = new JLabel(tieuDe);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(33, 37, 41));

        pnHeader.add(pnNut, BorderLayout.WEST);
        pnHeader.add(lblTitle, BorderLayout.CENTER);

        return pnHeader;
    }

    // ==========================================================
    // PANEL 1: DOANH THU (cho Quản lý)
    // ==========================================================

    private class PanelDoanhThu extends JPanel {
        private JDateChooser dcTu;
        private JDateChooser dcDen;
        private JButton btnLoc;
        private JLabel lblCardVe, lblCardTien, lblCardTB;
        private DefaultTableModel model;

        public PanelDoanhThu() {
            setLayout(new BorderLayout());
            setBackground(new Color(245, 247, 250));
            setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            // === NORTH: tiêu đề + filter + 3 card ===
            JPanel pnTop = new JPanel();
            pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.Y_AXIS));
            pnTop.setOpaque(false);

            JPanel pnHeader = taoHeaderCoNutQuayLai("Doanh thu theo thời gian");
            pnHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
            pnTop.add(pnHeader);

            dcTu = new JDateChooser();
            dcDen = new JDateChooser();
            btnLoc = new JButton("Lọc");
            JPanel pnFilter = taoFilterThoiGian(dcTu, dcDen, btnLoc);
            pnFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
            pnTop.add(pnFilter);

            // 3 card tóm tắt
            JPanel pnCards = new JPanel(new GridLayout(1, 3, 12, 0));
            pnCards.setOpaque(false);
            pnCards.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            pnCards.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel card1 = taoCardTomTat("TỔNG VÉ ĐÃ BÁN", "0", new Color(0, 123, 255));
            JPanel card2 = taoCardTomTat("TỔNG DOANH THU", "0 ₫", new Color(40, 167, 69));
            JPanel card3 = taoCardTomTat("TRUNG BÌNH / VÉ", "0 ₫", new Color(255, 193, 7));

            // Lấy label thứ 2 (lblValue) trong mỗi card để cập nhật sau
            lblCardVe = (JLabel) card1.getComponent(1);
            lblCardTien = (JLabel) card2.getComponent(1);
            lblCardTB = (JLabel) card3.getComponent(1);

            pnCards.add(card1);
            pnCards.add(card2);
            pnCards.add(card3);
            pnTop.add(pnCards);

            add(pnTop, BorderLayout.NORTH);

            // === CENTER: bảng chi tiết ===
            String[] cols = {"Ngày", "Số vé", "Doanh thu"};
            model = new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int r, int c) { return false; }
            };

            JTable tbl = new JTable(model);
            tbl.setRowHeight(28);
            tbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            tbl.getColumnModel().getColumn(0).setPreferredWidth(150);
            tbl.getColumnModel().getColumn(1).setPreferredWidth(100);
            tbl.getColumnModel().getColumn(2).setPreferredWidth(200);

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
            tbl.getColumnModel().getColumn(1).setCellRenderer(rightRenderer);
            tbl.getColumnModel().getColumn(2).setCellRenderer(rightRenderer);

            JScrollPane scroll = new JScrollPane(tbl);
            scroll.setBorder(BorderFactory.createTitledBorder("Chi tiết theo từng ngày"));
            add(scroll, BorderLayout.CENTER);

            btnLoc.addActionListener(e -> capNhat());

            // Tự load lần đầu
            capNhat();
        }

        private void capNhat() {
            LocalDate tu = layNgayTu(dcTu);
            LocalDate den = layNgayTu(dcDen);

            // 3 card
            TomTat tt = dao.layTomTatDoanhThu(tu, den);
            lblCardVe.setText(dinhDangTien.format(tt.tongVe));
            lblCardTien.setText(dinhDangTien.format(tt.tongDoanhThu) + " ₫");
            lblCardTB.setText(dinhDangTien.format(tt.trungBinhMoiVe) + " ₫");

            // Bảng
            model.setRowCount(0);
            List<DoanhThuTheoNgay> ds = dao.layDoanhThuTheoNgay(tu, den);
            for (DoanhThuTheoNgay d : ds) {
                model.addRow(new Object[]{
                    d.ngay.format(fmtNgay),
                    dinhDangTien.format(d.soVe),
                    dinhDangTien.format(d.doanhThu) + " ₫"
                });
            }

            if (ds.isEmpty()) {
                model.addRow(new Object[]{"(Không có dữ liệu)", "", ""});
            }
        }
    }

    // ==========================================================
    // PANEL 2: HIỆU SUẤT NHÂN VIÊN (cho Quản lý)
    // ==========================================================

    private class PanelNhanVien extends JPanel {
        private JDateChooser dcTu;
        private JDateChooser dcDen;
        private JButton btnLoc;
        private DefaultTableModel model;

        public PanelNhanVien() {
            setLayout(new BorderLayout());
            setBackground(new Color(245, 247, 250));
            setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            // NORTH
            JPanel pnTop = new JPanel();
            pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.Y_AXIS));
            pnTop.setOpaque(false);

            JPanel pnHeader = taoHeaderCoNutQuayLai("Hiệu suất nhân viên");
            pnHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
            pnTop.add(pnHeader);

            dcTu = new JDateChooser();
            dcDen = new JDateChooser();
            btnLoc = new JButton("Lọc");
            JPanel pnFilter = taoFilterThoiGian(dcTu, dcDen, btnLoc);
            pnFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
            pnTop.add(pnFilter);

            add(pnTop, BorderLayout.NORTH);

            // CENTER
            String[] cols = {"Hạng", "Mã NV", "Họ tên", "Chức vụ", "Số vé bán", "Doanh thu"};
            model = new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int r, int c) { return false; }
            };

            JTable tbl = new JTable(model);
            tbl.setRowHeight(32);
            tbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

            int[] widths = {60, 80, 200, 120, 100, 180};
            for (int i = 0; i < widths.length; i++) {
                tbl.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            }

            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            centerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 13));
            tbl.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

            DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
            rightRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
            tbl.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);
            tbl.getColumnModel().getColumn(5).setCellRenderer(rightRenderer);

            JScrollPane scroll = new JScrollPane(tbl);
            scroll.setBorder(BorderFactory.createTitledBorder("Xếp hạng nhân viên"));
            add(scroll, BorderLayout.CENTER);

            btnLoc.addActionListener(e -> capNhat());
            capNhat();
        }

        private void capNhat() {
            LocalDate tu = layNgayTu(dcTu);
            LocalDate den = layNgayTu(dcDen);

            model.setRowCount(0);
            List<HieuSuatNhanVien> ds = dao.layHieuSuatNhanVien(tu, den);
            int hang = 1;
            for (HieuSuatNhanVien h : ds) {
                model.addRow(new Object[]{
                    hang,
                    h.maNhanVien,
                    h.hoTenNV,
                    h.chucVu,
                    dinhDangTien.format(h.soVeBan),
                    dinhDangTien.format(h.doanhThu) + " ₫"
                });
                hang++;
            }

            if (ds.isEmpty()) {
                model.addRow(new Object[]{"", "", "(Không có dữ liệu)", "", "", ""});
            }
        }
    }

    // ==========================================================
    // PANEL 3: CHUYẾN TÀU (cho Quản lý)
    // ==========================================================

    private class PanelChuyenTau extends JPanel {
        private JDateChooser dcTu;
        private JDateChooser dcDen;
        private JButton btnLoc;
        private DefaultTableModel model;

        public PanelChuyenTau() {
            setLayout(new BorderLayout());
            setBackground(new Color(245, 247, 250));
            setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            JPanel pnTop = new JPanel();
            pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.Y_AXIS));
            pnTop.setOpaque(false);

            JPanel pnHeader = taoHeaderCoNutQuayLai("Thống kê chuyến tàu");
            pnHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
            pnTop.add(pnHeader);

            dcTu = new JDateChooser();
            dcDen = new JDateChooser();
            btnLoc = new JButton("Lọc");
            JPanel pnFilter = taoFilterThoiGian(dcTu, dcDen, btnLoc);
            pnFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
            pnTop.add(pnFilter);

            add(pnTop, BorderLayout.NORTH);

            // Bảng
            String[] cols = {"Mã chuyến", "Tên chuyến", "Khởi hành", "Tuyến",
                             "Tổng ghế", "Đã bán", "Tỷ lệ lấp đầy", "Doanh thu"};
            model = new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int r, int c) { return false; }
            };

            JTable tbl = new JTable(model);
            tbl.setRowHeight(32);
            tbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

            int[] widths = {80, 150, 130, 180, 80, 80, 130, 140};
            for (int i = 0; i < widths.length; i++) {
                tbl.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            }

            DefaultTableCellRenderer center = new DefaultTableCellRenderer();
            center.setHorizontalAlignment(SwingConstants.CENTER);
            tbl.getColumnModel().getColumn(4).setCellRenderer(center);
            tbl.getColumnModel().getColumn(5).setCellRenderer(center);

            DefaultTableCellRenderer right = new DefaultTableCellRenderer();
            right.setHorizontalAlignment(SwingConstants.RIGHT);
            tbl.getColumnModel().getColumn(7).setCellRenderer(right);

            // Render cột "Tỷ lệ lấp đầy" thành progress bar
            tbl.getColumnModel().getColumn(6).setCellRenderer(new ProgressBarRenderer());

            JScrollPane scroll = new JScrollPane(tbl);
            scroll.setBorder(BorderFactory.createTitledBorder("Danh sách chuyến tàu"));
            add(scroll, BorderLayout.CENTER);

            btnLoc.addActionListener(e -> capNhat());
            capNhat();
        }

        private void capNhat() {
            LocalDate tu = layNgayTu(dcTu);
            LocalDate den = layNgayTu(dcDen);

            model.setRowCount(0);
            List<ThongKeChuyen> ds = dao.layThongKeChuyenTau(tu, den);
            for (ThongKeChuyen c : ds) {
                model.addRow(new Object[]{
                    c.maChuyenTau,
                    c.tenChuyen,
                    c.ngayKhoiHanh.format(fmtNgayGio),
                    c.gaDi + " → " + c.gaDen,
                    c.tongGhe,
                    c.daBan,
                    c.tyLeLapDay,
                    dinhDangTien.format(c.doanhThu) + " ₫"
                });
            }

            if (ds.isEmpty()) {
                model.addRow(new Object[]{"", "", "(Không có dữ liệu)", "", "", "", 0.0, ""});
            }
        }
    }

    /** Render cột tỷ lệ lấp đầy bằng progress bar màu */
    private static class ProgressBarRenderer extends JProgressBar implements TableCellRenderer {
        public ProgressBarRenderer() {
            super(0, 100);
            setStringPainted(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable t, Object v,
                boolean sel, boolean foc, int r, int c) {
            double val = 0;
            if (v instanceof Number) val = ((Number) v).doubleValue();
            int intVal = (int) Math.round(val);

            setValue(intVal);
            setString(String.format("%.1f%%", val));

            // Màu theo mức
            if (val < 30) {
                setForeground(new Color(220, 53, 69));   // đỏ
            } else if (val < 70) {
                setForeground(new Color(255, 193, 7));   // vàng
            } else {
                setForeground(new Color(40, 167, 69));   // xanh
            }

            return this;
        }
    }

    // ==========================================================
    // PANEL 4: HIỆU SUẤT CỦA TÔI (cho Nhân viên)
    // ==========================================================

    private class PanelCuaToi extends JPanel {
        private JDateChooser dcTu;
        private JDateChooser dcDen;
        private JButton btnLoc;
        private JLabel lblCardVe, lblCardTien, lblCardTB;
        private DefaultTableModel model;

        public PanelCuaToi() {
            setLayout(new BorderLayout());
            setBackground(new Color(245, 247, 250));
            setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            // NORTH
            JPanel pnTop = new JPanel();
            pnTop.setLayout(new BoxLayout(pnTop, BoxLayout.Y_AXIS));
            pnTop.setOpaque(false);

            JPanel pnHeader = taoHeaderCoNutQuayLai("Hiệu suất bán vé của tôi");
            pnHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
            pnTop.add(pnHeader);

            dcTu = new JDateChooser();
            dcDen = new JDateChooser();
            btnLoc = new JButton("Lọc");
            JPanel pnFilter = taoFilterThoiGian(dcTu, dcDen, btnLoc);
            pnFilter.setAlignmentX(Component.LEFT_ALIGNMENT);
            pnTop.add(pnFilter);

            // Cards
            JPanel pnCards = new JPanel(new GridLayout(1, 3, 12, 0));
            pnCards.setOpaque(false);
            pnCards.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));
            pnCards.setAlignmentX(Component.LEFT_ALIGNMENT);

            JPanel c1 = taoCardTomTat("VÉ TÔI ĐÃ BÁN", "0", new Color(0, 123, 255));
            JPanel c2 = taoCardTomTat("DOANH THU", "0 ₫", new Color(40, 167, 69));
            JPanel c3 = taoCardTomTat("TRUNG BÌNH / VÉ", "0 ₫", new Color(255, 193, 7));

            lblCardVe = (JLabel) c1.getComponent(1);
            lblCardTien = (JLabel) c2.getComponent(1);
            lblCardTB = (JLabel) c3.getComponent(1);

            pnCards.add(c1);
            pnCards.add(c2);
            pnCards.add(c3);
            pnTop.add(pnCards);

            add(pnTop, BorderLayout.NORTH);

            // CENTER
            String[] cols = {"Ngày", "Số vé bán", "Doanh thu"};
            model = new DefaultTableModel(cols, 0) {
                @Override
                public boolean isCellEditable(int r, int c) { return false; }
            };

            JTable tbl = new JTable(model);
            tbl.setRowHeight(28);
            tbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));

            DefaultTableCellRenderer right = new DefaultTableCellRenderer();
            right.setHorizontalAlignment(SwingConstants.RIGHT);
            tbl.getColumnModel().getColumn(1).setCellRenderer(right);
            tbl.getColumnModel().getColumn(2).setCellRenderer(right);

            JScrollPane scroll = new JScrollPane(tbl);
            scroll.setBorder(BorderFactory.createTitledBorder("Chi tiết theo ngày"));
            add(scroll, BorderLayout.CENTER);

            btnLoc.addActionListener(e -> capNhat());
            capNhat();
        }

        private void capNhat() {
            LocalDate tu = layNgayTu(dcTu);
            LocalDate den = layNgayTu(dcDen);

            // Cards
            TomTat tt = dao.layTomTatNhanVien(maNhanVienHienTai, tu, den);
            lblCardVe.setText(dinhDangTien.format(tt.tongVe));
            lblCardTien.setText(dinhDangTien.format(tt.tongDoanhThu) + " ₫");
            lblCardTB.setText(dinhDangTien.format(tt.trungBinhMoiVe) + " ₫");

            // Bảng
            model.setRowCount(0);
            List<DoanhThuTheoNgay> ds =
                    dao.layDoanhThuTheoNgayCuaNhanVien(maNhanVienHienTai, tu, den);
            for (DoanhThuTheoNgay d : ds) {
                model.addRow(new Object[]{
                    d.ngay.format(fmtNgay),
                    dinhDangTien.format(d.soVe),
                    dinhDangTien.format(d.doanhThu) + " ₫"
                });
            }

            if (ds.isEmpty()) {
                model.addRow(new Object[]{"(Không có dữ liệu)", "", ""});
            }
        }
    }

    // ==========================================================
    // PANEL TRANG CHỦ - menu chọn loại thống kê
    // ==========================================================

    private class PanelTrangChu extends JPanel {

        public PanelTrangChu() {
            setLayout(new BorderLayout());
            setBackground(new Color(245, 247, 250));
            setBorder(BorderFactory.createEmptyBorder(24, 40, 24, 40));

            // NORTH: tiêu đề + 4 KPI cards + bar chart
            JPanel pnNorth = new JPanel();
            pnNorth.setLayout(new BoxLayout(pnNorth, BoxLayout.Y_AXIS));
            pnNorth.setOpaque(false);

            JPanel pnHeader = new JPanel();
            pnHeader.setLayout(new BoxLayout(pnHeader, BoxLayout.Y_AXIS));
            pnHeader.setOpaque(false);
            pnHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 16, 0));
            pnHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblTitle = new JLabel("Báo cáo & Thống kê");
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
            lblTitle.setForeground(new Color(33, 37, 41));
            lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblSub = new JLabel("Tổng quan hôm nay — " +
                    java.time.LocalDate.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lblSub.setForeground(new Color(108, 117, 125));
            lblSub.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
            lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

            pnHeader.add(lblTitle);
            pnHeader.add(lblSub);
            pnNorth.add(pnHeader);

            // 4 KPI cards hôm nay
            LocalDate homNay = java.time.LocalDate.now();
            TomTat tomTatHomNay = dao.layTomTatDoanhThu(homNay, homNay);
            int soDoiTra = dao.laySoVeDoiTraHomNay();
            double tiLeLapDay = dao.layTiLeLapDay(homNay);

            JPanel pnKPI = new JPanel(new GridLayout(1, 4, 12, 0));
            pnKPI.setOpaque(false);
            pnKPI.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));
            pnKPI.setAlignmentX(Component.LEFT_ALIGNMENT);
            pnKPI.setMaximumSize(new Dimension(Integer.MAX_VALUE, 90));

            pnKPI.add(taoCardTomTat("VÉ BÁN HÔM NAY",
                    String.valueOf(tomTatHomNay.tongVe), new Color(0, 123, 255)));
            pnKPI.add(taoCardTomTat("DOANH THU HÔM NAY",
                    dinhDangTien.format(tomTatHomNay.tongDoanhThu) + " ₫", new Color(40, 167, 69)));
            pnKPI.add(taoCardTomTat("ĐỔI / TRẢ HÔM NAY",
                    String.valueOf(soDoiTra), new Color(220, 53, 69)));
            pnKPI.add(taoCardTomTat("TỈ LỆ LẤP ĐẦY",
                    String.format("%.1f%%", tiLeLapDay), new Color(255, 153, 0)));
            pnNorth.add(pnKPI);

            // Bar chart theo khung giờ
            long[] khungGio = dao.layDoanhThuTheoKhungGio(homNay);
            JPanel barChart = new BarChartKhungGio(khungGio);
            barChart.setAlignmentX(Component.LEFT_ALIGNMENT);
            barChart.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
            barChart.setBorder(BorderFactory.createTitledBorder("Doanh thu theo khung giờ hôm nay"));
            pnNorth.add(barChart);
            pnNorth.add(Box.createVerticalStrut(12));

            JLabel lblNav = new JLabel("Chọn loại báo cáo chi tiết:");
            lblNav.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lblNav.setForeground(new Color(33, 37, 41));
            lblNav.setAlignmentX(Component.LEFT_ALIGNMENT);
            pnNorth.add(lblNav);
            pnNorth.add(Box.createVerticalStrut(8));

            add(pnNorth, BorderLayout.NORTH);

            // CENTER: lưới các thẻ
            JPanel pnGrid = new JPanel(new GridLayout(0, 2, 20, 20));
            pnGrid.setOpaque(false);

            if (laQuanLy) {
                pnGrid.add(taoTheChucNang(
                    "💰", "Doanh thu",
                    "Xem doanh thu theo ngày, tuần, tháng. Tổng vé bán và doanh thu tích lũy.",
                    new Color(0, 123, 255), "doanhThu"
                ));
                pnGrid.add(taoTheChucNang(
                    "👥", "Hiệu suất nhân viên",
                    "Xếp hạng nhân viên theo số vé bán và doanh thu trong khoảng thời gian.",
                    new Color(40, 167, 69), "nhanVien"
                ));
                pnGrid.add(taoTheChucNang(
                    "🚂", "Chuyến tàu",
                    "Xem tỷ lệ lấp đầy và doanh thu của từng chuyến tàu.",
                    new Color(255, 153, 0), "chuyenTau"
                ));
            } else {
                pnGrid.add(taoTheChucNang(
                    "👤", "Hiệu suất của tôi",
                    "Xem số vé bạn đã bán và doanh thu cá nhân.",
                    new Color(0, 123, 255), "cuaToi"
                ));
            }

            // Đặt lưới vào panel để không bị stretch full
            JPanel pnWrap = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
            pnWrap.setOpaque(false);
            pnWrap.add(pnGrid);

            // Set kích thước cố định cho grid
            pnGrid.setPreferredSize(new Dimension(700, laQuanLy ? 360 : 180));

            JScrollPane scrollWrap = new JScrollPane(pnWrap);
            scrollWrap.setOpaque(false);
            scrollWrap.getViewport().setOpaque(false);
            scrollWrap.setBorder(null);
            scrollWrap.getVerticalScrollBar().setUnitIncrement(16);

            add(scrollWrap, BorderLayout.CENTER);
        }

        /** Tạo 1 thẻ chức năng có icon, tiêu đề, mô tả, click vào sẽ mở card tương ứng */
        private JPanel taoTheChucNang(String icon, String tieuDe, String moTa,
                                      Color mauNhan, String cardName) {
            JPanel pnThe = new JPanel(new BorderLayout(0, 12));
            pnThe.setBackground(Color.WHITE);
            pnThe.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(24, 24, 24, 24)
            ));
            pnThe.setPreferredSize(new Dimension(320, 160));
            pnThe.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));

            // Icon ở góc trên trái
            JLabel lblIcon = new JLabel(icon);
            lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 36));
            lblIcon.setOpaque(true);
            lblIcon.setBackground(mauNhan);
            lblIcon.setForeground(Color.WHITE);
            lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
            lblIcon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
            lblIcon.setPreferredSize(new Dimension(64, 64));

            JPanel pnIconWrap = new JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));
            pnIconWrap.setOpaque(false);
            pnIconWrap.add(lblIcon);
            pnThe.add(pnIconWrap, BorderLayout.NORTH);

            // Phần text
            JPanel pnText = new JPanel();
            pnText.setLayout(new BoxLayout(pnText, BoxLayout.Y_AXIS));
            pnText.setOpaque(false);

            JLabel lblTitle = new JLabel(tieuDe);
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblTitle.setForeground(new Color(33, 37, 41));
            lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblDesc = new JLabel("<html><div style='width:260px'>" + moTa + "</div></html>");
            lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            lblDesc.setForeground(new Color(108, 117, 125));
            lblDesc.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));
            lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);

            pnText.add(lblTitle);
            pnText.add(lblDesc);
            pnThe.add(pnText, BorderLayout.CENTER);

            // Bắt sự kiện click trên cả panel
            pnThe.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    cardLayout.show(pnContent, cardName);
                }

                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    pnThe.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(mauNhan, 2),
                            BorderFactory.createEmptyBorder(23, 23, 23, 23)
                    ));
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    pnThe.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                            BorderFactory.createEmptyBorder(24, 24, 24, 24)
                    ));
                }
            });

            // Thêm cùng listener cho các component con để click chỗ nào cũng được
            for (Component c : new Component[]{lblIcon, lblTitle, lblDesc, pnText, pnIconWrap}) {
                c.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        cardLayout.show(pnContent, cardName);
                    }
                });
                c.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.HAND_CURSOR));
            }

            return pnThe;
        }
    }

    // ==========================================================
    // BAR CHART KHUNG GIỜ
    // ==========================================================

    private static class BarChartKhungGio extends JPanel {
        private static final String[] LABELS = {"Sáng\n6-10h", "Trưa\n11-13h", "Chiều\n14-17h", "Tối\n18-21h"};
        private static final Color[] COLORS = {
            new Color(0, 123, 255), new Color(255, 153, 0),
            new Color(40, 167, 69), new Color(108, 117, 125)
        };
        private final long[] values;

        BarChartKhungGio(long[] values) {
            this.values = values;
            setOpaque(false);
            setPreferredSize(new java.awt.Dimension(600, 130));
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
            g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                    java.awt.RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth(), h = getHeight();
            int pad = 32, barGap = 20;
            int n = 4;
            int barW = (w - pad * 2 - barGap * (n - 1)) / n;
            int chartH = h - pad - 36; // leave bottom for labels

            long maxVal = 1;
            for (long v : values) if (v > maxVal) maxVal = v;

            java.text.NumberFormat nf = java.text.NumberFormat.getIntegerInstance(new java.util.Locale("vi"));

            for (int i = 0; i < n; i++) {
                int x = pad + i * (barW + barGap);
                int barH = values[i] == 0 ? 2 : (int) (chartH * values[i] / maxVal);
                int y = pad + chartH - barH;

                g2.setColor(COLORS[i]);
                g2.fillRoundRect(x, y, barW, barH, 6, 6);

                // Value label above bar
                if (values[i] > 0) {
                    g2.setColor(new Color(33, 37, 41));
                    g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 10));
                    String val = nf.format(values[i] / 1000) + "K";
                    java.awt.FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(val, x + (barW - fm.stringWidth(val)) / 2, y - 3);
                }

                // X-axis label
                g2.setColor(new Color(108, 117, 125));
                g2.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 10));
                String[] lines = LABELS[i].split("\n");
                java.awt.FontMetrics fm = g2.getFontMetrics();
                for (int li = 0; li < lines.length; li++) {
                    int lx = x + (barW - fm.stringWidth(lines[li])) / 2;
                    g2.drawString(lines[li], lx, pad + chartH + 14 + li * 13);
                }
            }
            g2.dispose();
        }
    }
}
