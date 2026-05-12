package com.mycompany.prj1.GUI;

import com.mycompany.prj1.DAO.DAO_BANVE;
import com.mycompany.prj1.DAO.DAO_BANVE.ChiTietVe;
import com.mycompany.prj1.DAO.DAO_BANVE.ChuyenTuongLai;
import com.mycompany.prj1.DAO.DAO_BANVE.KetQuaPhiTraVe;
import com.mycompany.prj1.DAO.DAO_BANVE.PhuongThuc;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * Quản lý bán vé - chứa 2 chức năng con: Trả vé và Đổi vé.
 *
 * Layout:
 *   - Trang chủ: 2 thẻ chức năng
 *   - Mỗi thẻ click → mở panel chi tiết, có nút "← Quay lại"
 */
public class GUI_QuanLyBanVe extends JPanel {

    private final DAO_BANVE dao = new DAO_BANVE();
    private final String maNhanVienDangNhap;

    private final NumberFormat dinhDangTien =
            NumberFormat.getInstance(new Locale("vi", "VN"));
    private final DateTimeFormatter fmtDateTime =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private CardLayout cardLayout;
    private JPanel pnContent;

    public GUI_QuanLyBanVe(String maNhanVien) {
        this.maNhanVienDangNhap = maNhanVien;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        khoiTaoNoiDung();
    }

    private void khoiTaoNoiDung() {
        cardLayout = new CardLayout();
        pnContent = new JPanel(cardLayout);
        pnContent.setBackground(new Color(245, 247, 250));

        pnContent.add(new PanelTrangChu(), "trangChu");
        pnContent.add(new PanelTracuu(), "tracuu");
        pnContent.add(new PanelTraVe(), "traVe");
        pnContent.add(new PanelDoiVe(), "doiVe");

        cardLayout.show(pnContent, "trangChu");
        add(pnContent, BorderLayout.CENTER);
    }

    // ==========================================================
    // SHARED HELPERS
    // ==========================================================

    private JPanel taoHeaderCoNutQuayLai(String tieuDe) {
        JPanel pn = new JPanel(new BorderLayout(12, 0));
        pn.setOpaque(false);
        pn.setBorder(BorderFactory.createEmptyBorder(0, 0, 12, 0));

        JButton btnQuay = new JButton("← Quay lại");
        btnQuay.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnQuay.setBackground(new Color(108, 117, 125));
        btnQuay.setForeground(Color.WHITE);
        btnQuay.setFocusPainted(false);
        btnQuay.setBorderPainted(false);
        btnQuay.setOpaque(true);
        btnQuay.setPreferredSize(new Dimension(120, 36));
        btnQuay.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnQuay.addActionListener(e -> cardLayout.show(pnContent, "trangChu"));

        JPanel pnNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnNut.setOpaque(false);
        pnNut.add(btnQuay);

        JLabel lblTitle = new JLabel(tieuDe);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(new Color(33, 37, 41));

        pn.add(pnNut, BorderLayout.WEST);
        pn.add(lblTitle, BorderLayout.CENTER);
        return pn;
    }

    private JLabel boldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        Dimension d = new Dimension(140, 28);
        lbl.setPreferredSize(d);
        lbl.setMinimumSize(d);
        return lbl;
    }

    private JButton taoNutMau(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setPreferredSize(new Dimension(110, 32));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        return btn;
    }

    // ==========================================================
    // SHARED PDF EXPORT (dùng chung cho tra cứu + đổi vé)
    // ==========================================================

    /**
     * Hỏi user và xuất PDF cho 1 vé. Trả về true nếu xuất thành công.
     */
    boolean hoiVaXuatPDF(java.awt.Component owner, ChiTietVe ve) {
        if (ve == null) return false;

        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        fc.setDialogTitle("Lưu file PDF");
        fc.setSelectedFile(new java.io.File("Ve_" + ve.maVe + ".pdf"));
        fc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "PDF Files (*.pdf)", "pdf"));

        if (fc.showSaveDialog(owner) != javax.swing.JFileChooser.APPROVE_OPTION) {
            return false;
        }

        java.io.File file = fc.getSelectedFile();
        if (!file.getName().toLowerCase().endsWith(".pdf")) {
            file = new java.io.File(file.getAbsolutePath() + ".pdf");
        }

        try {
            xuatVeRaPDF(ve, file);
            JOptionPane.showMessageDialog(owner,
                    "Đã xuất vé ra file:\n" + file.getAbsolutePath());

            // Mở file luôn
            if (java.awt.Desktop.isDesktopSupported()) {
                try {
                    java.awt.Desktop.getDesktop().open(file);
                } catch (Exception ex) { /* ignore */ }
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(owner,
                    "Xuất PDF thất bại:\n" + ex.getMessage(),
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /** Xuất 1 vé ra PDF dùng PDFBox (fonts cơ bản, không hỗ trợ tiếng Việt) */
    void xuatVeRaPDF(ChiTietVe v, java.io.File file) throws Exception {
        org.apache.pdfbox.pdmodel.PDDocument doc =
                new org.apache.pdfbox.pdmodel.PDDocument();
        try {
            org.apache.pdfbox.pdmodel.PDPage page =
                    new org.apache.pdfbox.pdmodel.PDPage(
                            org.apache.pdfbox.pdmodel.common.PDRectangle.A4);
            doc.addPage(page);

            org.apache.pdfbox.pdmodel.PDPageContentStream cs =
                    new org.apache.pdfbox.pdmodel.PDPageContentStream(doc, page);

            org.apache.pdfbox.pdmodel.font.PDFont fontTieuDe =
                    org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD;
            org.apache.pdfbox.pdmodel.font.PDFont fontText =
                    org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA;

            float yStart = page.getMediaBox().getHeight() - 60;
            float xLeft = 50;

            cs.beginText();
            cs.setFont(fontTieuDe, 18);
            cs.newLineAtOffset(xLeft, yStart);
            cs.showText("VE TAU - QUAN LY VE TAU");
            cs.endText();

            cs.beginText();
            cs.setFont(fontText, 10);
            cs.newLineAtOffset(xLeft, yStart - 20);
            cs.showText("(Phieu thong tin ve - khong co gia tri thanh toan)");
            cs.endText();

            cs.setLineWidth(1);
            cs.moveTo(xLeft, yStart - 30);
            cs.lineTo(page.getMediaBox().getWidth() - xLeft, yStart - 30);
            cs.stroke();

            float y = yStart - 60;
            float lineHeight = 18;

            String[][] data = {
                {"Ma ve:", v.maVe},
                {"Trang thai:", boDau(v.trangThaiVe)},
                {"Ngay ban:", v.ngayBan.format(fmtDateTime)},
                {"", ""},
                {"--- Hanh khach ---", ""},
                {"Ho ten:", boDau(v.hoTenHanhKhach)},
                {"So giay to:", v.soGiayTo == null ? "-" : v.soGiayTo},
                {"SDT:", v.sdt == null ? "-" : v.sdt},
                {"", ""},
                {"--- Chuyen tau ---", ""},
                {"Ma chuyen:", v.maChuyenTau},
                {"Ten chuyen:", boDau(v.tenChuyen)},
                {"Tuyen:", boDau(v.tenGaDi) + " -> " + boDau(v.tenGaDen)},
                {"Khoi hanh:", v.ngayKhoiHanh.format(fmtDateTime)},
                {"Den du kien:", v.ngayDenDuKien.format(fmtDateTime)},
                {"", ""},
                {"--- Ve ---", ""},
                {"So ghe:", boDau(v.soGhe)},
                {"Loai ve:", boDau(v.tenLoaiVe)},
                {"Tong tien:", dinhDangTien.format(v.tongTien) + " VND"},
            };

            for (String[] row : data) {
                if (row[0].isEmpty() && row[1].isEmpty()) {
                    y -= lineHeight / 2;
                    continue;
                }

                boolean isHeader = row[0].startsWith("---");

                cs.beginText();
                cs.setFont(isHeader ? fontTieuDe : fontText, isHeader ? 12 : 11);
                cs.newLineAtOffset(xLeft, y);
                cs.showText(row[0]);
                cs.endText();

                if (!row[1].isEmpty()) {
                    cs.beginText();
                    cs.setFont(fontText, 11);
                    cs.newLineAtOffset(xLeft + 130, y);
                    cs.showText(row[1]);
                    cs.endText();
                }

                y -= lineHeight;
            }

            cs.beginText();
            cs.setFont(fontText, 9);
            cs.newLineAtOffset(xLeft, 50);
            cs.showText("In luc: " + java.time.LocalDateTime.now()
                    .format(java.time.format.DateTimeFormatter
                            .ofPattern("dd/MM/yyyy HH:mm:ss")));
            cs.endText();

            cs.close();
            doc.save(file);
        } finally {
            doc.close();
        }
    }

    /** Bỏ dấu tiếng Việt cho PDF */
    String boDau(String s) {
        if (s == null) return "";
        String norm = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
        norm = norm.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        norm = norm.replace('đ', 'd').replace('Đ', 'D');
        return norm;
    }

    // ==========================================================
    // PANEL TRANG CHỦ
    // ==========================================================

    private class PanelTrangChu extends JPanel {
        public PanelTrangChu() {
            setLayout(new BorderLayout());
            setBackground(new Color(245, 247, 250));
            setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

            JPanel pnHeader = new JPanel();
            pnHeader.setLayout(new BoxLayout(pnHeader, BoxLayout.Y_AXIS));
            pnHeader.setOpaque(false);
            pnHeader.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));

            JLabel lblTitle = new JLabel("Quản lý bán vé");
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
            lblTitle.setForeground(new Color(33, 37, 41));
            lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel lblSub = new JLabel("Chọn thao tác bạn muốn thực hiện");
            lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            lblSub.setForeground(new Color(108, 117, 125));
            lblSub.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
            lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

            pnHeader.add(lblTitle);
            pnHeader.add(lblSub);
            add(pnHeader, BorderLayout.NORTH);

            JPanel pnGrid = new JPanel(new GridLayout(0, 3, 16, 16));
            pnGrid.setOpaque(false);

            pnGrid.add(taoTheChucNang(
                    "Q", "Tra cứu vé",
                    "Tìm và xem chi tiết vé theo mã. Có thể xuất vé ra file PDF.",
                    new Color(108, 117, 125), "tracuu"
            ));
            pnGrid.add(taoTheChucNang(
                    "T", "Trả vé / Hoàn tiền",
                    "Hoàn tiền cho khách khi trả vé. Phí phạt theo thời gian còn lại.",
                    new Color(220, 53, 69), "traVe"
            ));
            pnGrid.add(taoTheChucNang(
                    "D", "Đổi vé",
                    "Đổi ghế trong cùng chuyến. Tính chênh lệch giá theo loại ghế.",
                    new Color(0, 123, 255), "doiVe"
            ));

            JPanel pnWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            pnWrap.setOpaque(false);
            pnWrap.add(pnGrid);
            pnGrid.setPreferredSize(new Dimension(1000, 180));

            JScrollPane scroll = new JScrollPane(pnWrap);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            scroll.setBorder(null);

            add(scroll, BorderLayout.CENTER);
        }

        private JPanel taoTheChucNang(String icon, String tieuDe, String moTa,
                                      Color mauNhan, String cardName) {
            JPanel pnThe = new JPanel(new BorderLayout(0, 12));
            pnThe.setBackground(Color.WHITE);
            pnThe.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                    BorderFactory.createEmptyBorder(24, 24, 24, 24)
            ));
            pnThe.setPreferredSize(new Dimension(320, 160));
            pnThe.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            JLabel lblIcon = new JLabel(icon);
            lblIcon.setFont(new Font("Segoe UI", Font.BOLD, 32));
            lblIcon.setOpaque(true);
            lblIcon.setBackground(mauNhan);
            lblIcon.setForeground(Color.WHITE);
            lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
            lblIcon.setPreferredSize(new Dimension(64, 64));

            JPanel pnIconWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            pnIconWrap.setOpaque(false);
            pnIconWrap.add(lblIcon);
            pnThe.add(pnIconWrap, BorderLayout.NORTH);

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

            // Click events
            MouseAdapter clickListener = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    cardLayout.show(pnContent, cardName);
                }
                @Override
                public void mouseEntered(MouseEvent e) {
                    pnThe.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(mauNhan, 2),
                            BorderFactory.createEmptyBorder(23, 23, 23, 23)
                    ));
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    pnThe.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                            BorderFactory.createEmptyBorder(24, 24, 24, 24)
                    ));
                }
            };
            pnThe.addMouseListener(clickListener);
            for (Component c : new Component[]{lblIcon, lblTitle, lblDesc, pnText, pnIconWrap}) {
                c.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        cardLayout.show(pnContent, cardName);
                    }
                });
                c.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            return pnThe;
        }
    }

    // ==========================================================
    // PANEL TRẢ VÉ
    // ==========================================================

    private class PanelTraVe extends JPanel {
        private JTextField txtMaVe;
        private JButton btnTimVe;
        private JTextArea taThongTinVe;

        // Kết quả tính phí
        private JLabel lblTienVe;
        private JLabel lblTyLePhat;
        private JLabel lblPhiPhat;
        private JLabel lblTienHoan;
        private JComboBox<PhuongThuc> cboPhuongThuc;
        private JButton btnXacNhanTra;

        private ChiTietVe veHienTai;
        private KetQuaPhiTraVe ketQuaHienTai;

        public PanelTraVe() {
            setLayout(new BorderLayout());
            setBackground(new Color(245, 247, 250));
            setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            add(taoHeaderCoNutQuayLai("Trả vé / Hoàn tiền"), BorderLayout.NORTH);

            JPanel pnBody = new JPanel(new BorderLayout(0, 12));
            pnBody.setOpaque(false);

            // Search box
            pnBody.add(taoSearchBox(), BorderLayout.NORTH);

            // 2 cột: thông tin vé | tính phí
            JPanel pnRow = new JPanel(new GridLayout(1, 2, 16, 0));
            pnRow.setOpaque(false);
            pnRow.add(taoPanelThongTinVe());
            pnRow.add(taoPanelTinhPhi());
            pnBody.add(pnRow, BorderLayout.CENTER);

            add(pnBody, BorderLayout.CENTER);

            // Init
            xoaTrang();
        }

        private JPanel taoSearchBox() {
            JPanel pn = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
            pn.setOpaque(false);
            pn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            pn.setBackground(Color.WHITE);

            JLabel lbl = new JLabel("Mã vé:");
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));

            txtMaVe = new JTextField();
            txtMaVe.setPreferredSize(new Dimension(200, 32));
            txtMaVe.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            btnTimVe = taoNutMau("Tìm vé", new Color(0, 123, 255));
            btnTimVe.setPreferredSize(new Dimension(100, 32));
            btnTimVe.addActionListener(e -> timVe());
            txtMaVe.addActionListener(e -> timVe());

            pn.add(lbl);
            pn.add(txtMaVe);
            pn.add(btnTimVe);
            return pn;
        }

        private JPanel taoPanelThongTinVe() {
            JPanel pn = new JPanel(new BorderLayout(0, 8));
            pn.setBackground(Color.WHITE);
            pn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(new Color(180, 180, 180)),
                            "Thông tin vé",
                            javax.swing.border.TitledBorder.LEFT,
                            javax.swing.border.TitledBorder.TOP,
                            new Font("Segoe UI", Font.BOLD, 13)
                    ),
                    BorderFactory.createEmptyBorder(8, 12, 12, 12)
            ));

            taThongTinVe = new JTextArea();
            taThongTinVe.setEditable(false);
            taThongTinVe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            taThongTinVe.setLineWrap(true);
            taThongTinVe.setWrapStyleWord(true);
            taThongTinVe.setBackground(Color.WHITE);

            pn.add(new JScrollPane(taThongTinVe), BorderLayout.CENTER);
            return pn;
        }

        private JPanel taoPanelTinhPhi() {
            JPanel pn = new JPanel(new BorderLayout(0, 8));
            pn.setBackground(Color.WHITE);
            pn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(new Color(180, 180, 180)),
                            "Tính phí trả vé",
                            javax.swing.border.TitledBorder.LEFT,
                            javax.swing.border.TitledBorder.TOP,
                            new Font("Segoe UI", Font.BOLD, 13)
                    ),
                    BorderFactory.createEmptyBorder(8, 12, 12, 12)
            ));

            JPanel pnRows = new JPanel(new GridBagLayout());
            pnRows.setOpaque(false);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(8, 4, 8, 4);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            lblTienVe = taoLabelGiaTri("0 ₫", new Color(33, 37, 41));
            lblTyLePhat = taoLabelGiaTri("0%", new Color(33, 37, 41));
            lblPhiPhat = taoLabelGiaTri("0 ₫", new Color(220, 53, 69));
            lblTienHoan = taoLabelGiaTriBig("0 ₫", new Color(40, 167, 69));

            int row = 0;
            themHang(pnRows, gbc, row++, "Tiền vé gốc:", lblTienVe);
            themHang(pnRows, gbc, row++, "Tỷ lệ phạt:", lblTyLePhat);
            themHang(pnRows, gbc, row++, "Phí trả vé:", lblPhiPhat);
            themHang(pnRows, gbc, row++, "Tiền hoàn lại:", lblTienHoan);

            // Phương thức thanh toán
            cboPhuongThuc = new JComboBox<>();
            cboPhuongThuc.setPreferredSize(new Dimension(200, 30));
            for (PhuongThuc p : dao.layPhuongThucThanhToan()) {
                cboPhuongThuc.addItem(p);
            }

            gbc.gridy = row++;
            gbc.gridx = 0;
            gbc.weightx = 0;
            pnRows.add(boldLabel("Phương thức:"), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1;
            pnRows.add(cboPhuongThuc, gbc);

            pn.add(pnRows, BorderLayout.NORTH);

            // Nút xác nhận
            btnXacNhanTra = taoNutMau("Xác nhận trả vé", new Color(220, 53, 69));
            btnXacNhanTra.setPreferredSize(new Dimension(180, 40));
            btnXacNhanTra.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btnXacNhanTra.addActionListener(e -> xacNhanTraVe());

            JPanel pnNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            pnNut.setOpaque(false);
            pnNut.add(btnXacNhanTra);
            pn.add(pnNut, BorderLayout.SOUTH);

            return pn;
        }

        private JLabel taoLabelGiaTri(String text, Color color) {
            JLabel lbl = new JLabel(text);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            lbl.setForeground(color);
            return lbl;
        }

        private JLabel taoLabelGiaTriBig(String text, Color color) {
            JLabel lbl = new JLabel(text);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lbl.setForeground(color);
            return lbl;
        }

        private void themHang(JPanel pn, GridBagConstraints gbc, int row, String label, JLabel value) {
            gbc.gridy = row;
            gbc.gridx = 0;
            gbc.weightx = 0;
            pn.add(boldLabel(label), gbc);
            gbc.gridx = 1;
            gbc.weightx = 1;
            pn.add(value, gbc);
        }

        // -- nghiệp vụ --

        private void xoaTrang() {
            taThongTinVe.setText("Nhập mã vé và bấm \"Tìm vé\" để xem thông tin.");
            lblTienVe.setText("0 ₫");
            lblTyLePhat.setText("0%");
            lblPhiPhat.setText("0 ₫");
            lblTienHoan.setText("0 ₫");
            btnXacNhanTra.setEnabled(false);
            veHienTai = null;
            ketQuaHienTai = null;
        }

        private void timVe() {
            String maVe = txtMaVe.getText().trim();
            if (maVe.isEmpty()) {
                JOptionPane.showMessageDialog(PanelTraVe.this, "Vui lòng nhập mã vé.");
                return;
            }

            ChiTietVe ve = dao.layChiTietVe(maVe);
            if (ve == null) {
                JOptionPane.showMessageDialog(PanelTraVe.this,
                        "Không tìm thấy vé với mã: " + maVe,
                        "Lỗi", JOptionPane.WARNING_MESSAGE);
                xoaTrang();
                return;
            }

            veHienTai = ve;
            taThongTinVe.setText(formatThongTinVe(ve));

            // Tính phí trả
            ketQuaHienTai = dao.tinhPhiTraVe(ve);

            if (!ketQuaHienTai.choPhep) {
                lblTienVe.setText(dinhDangTien.format(ketQuaHienTai.tienVe) + " ₫");
                lblTyLePhat.setText("—");
                lblPhiPhat.setText("—");
                lblTienHoan.setText("Không hoàn");
                lblTienHoan.setForeground(new Color(220, 53, 69));
                btnXacNhanTra.setEnabled(false);
                JOptionPane.showMessageDialog(PanelTraVe.this,
                        ketQuaHienTai.lyDoTuChoi,
                        "Không thể trả vé", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Hiển thị
            lblTienVe.setText(dinhDangTien.format(ketQuaHienTai.tienVe) + " ₫");
            lblTyLePhat.setText(String.format("%.0f%%", ketQuaHienTai.tyLePhat));
            lblPhiPhat.setText(dinhDangTien.format(ketQuaHienTai.phiTraVe) + " ₫");
            lblTienHoan.setText(dinhDangTien.format(ketQuaHienTai.tienHoan) + " ₫");
            lblTienHoan.setForeground(new Color(40, 167, 69));
            btnXacNhanTra.setEnabled(true);
        }

        private String formatThongTinVe(ChiTietVe v) {
            StringBuilder sb = new StringBuilder();
            sb.append("══ THÔNG TIN VÉ ══\n");
            sb.append("Mã vé:           ").append(v.maVe).append("\n");
            sb.append("Trạng thái:      ").append(v.trangThaiVe).append("\n");
            sb.append("Ngày bán:        ").append(v.ngayBan.format(fmtDateTime)).append("\n\n");

            sb.append("══ HÀNH KHÁCH ══\n");
            sb.append("Họ tên:          ").append(v.hoTenHanhKhach).append("\n");
            sb.append("Số giấy tờ:      ").append(v.soGiayTo == null ? "-" : v.soGiayTo).append("\n");
            sb.append("SĐT:             ").append(v.sdt == null ? "-" : v.sdt).append("\n\n");

            sb.append("══ CHUYẾN TÀU ══\n");
            sb.append("Mã chuyến:       ").append(v.maChuyenTau).append("\n");
            sb.append("Tên chuyến:      ").append(v.tenChuyen).append("\n");
            sb.append("Tuyến:           ").append(v.tenGaDi).append(" → ").append(v.tenGaDen).append("\n");
            sb.append("Khởi hành:       ").append(v.ngayKhoiHanh.format(fmtDateTime)).append("\n");
            sb.append("Đến dự kiến:     ").append(v.ngayDenDuKien.format(fmtDateTime)).append("\n\n");

            sb.append("══ VÉ ══\n");
            sb.append("Số ghế:          ").append(v.soGhe).append("\n");
            sb.append("Loại vé:         ").append(v.tenLoaiVe).append("\n");
            sb.append("Tổng tiền:       ").append(dinhDangTien.format(v.tongTien)).append(" ₫\n");

            if (v.ghiChu != null && !v.ghiChu.isEmpty()) {
                sb.append("\n══ GHI CHÚ ══\n").append(v.ghiChu).append("\n");
            }
            return sb.toString();
        }

        private void xacNhanTraVe() {
            if (veHienTai == null || ketQuaHienTai == null || !ketQuaHienTai.choPhep) {
                return;
            }

            PhuongThuc pt = (PhuongThuc) cboPhuongThuc.getSelectedItem();
            if (pt == null) {
                JOptionPane.showMessageDialog(PanelTraVe.this, "Vui lòng chọn phương thức.");
                return;
            }

            String thongBao = String.format(
                    "Xác nhận trả vé %s?\n\n"
                    + "  Tiền vé:      %s ₫\n"
                    + "  Phí phạt:     %s ₫ (%.0f%%)\n"
                    + "  Tiền hoàn:    %s ₫\n"
                    + "  Phương thức:  %s",
                    veHienTai.maVe,
                    dinhDangTien.format(ketQuaHienTai.tienVe),
                    dinhDangTien.format(ketQuaHienTai.phiTraVe),
                    ketQuaHienTai.tyLePhat,
                    dinhDangTien.format(ketQuaHienTai.tienHoan),
                    pt.tenPhuongThuc);

            if (JOptionPane.showConfirmDialog(PanelTraVe.this, thongBao,
                    "Xác nhận trả vé", JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
                return;
            }

            boolean ok = dao.traVe(
                    veHienTai.maVe,
                    ketQuaHienTai.tienHoan,
                    pt.maPhuongThuc,
                    maNhanVienDangNhap);

            if (ok) {
                JOptionPane.showMessageDialog(PanelTraVe.this,
                        "Đã trả vé thành công.\nTiền hoàn: "
                        + dinhDangTien.format(ketQuaHienTai.tienHoan) + " ₫");
                xoaTrang();
                txtMaVe.setText("");
            } else {
                JOptionPane.showMessageDialog(PanelTraVe.this,
                        "Trả vé thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ==========================================================
    // PANEL ĐỔI VÉ
    // ==========================================================

    // ==========================================================
    // PANEL ĐỔI VÉ - Chỉ đổi GHẾ TRONG CÙNG CHUYẾN
    // ==========================================================

    private class PanelDoiVe extends JPanel {
        private JTextField txtMaVeCu;
        private JButton btnTimVeCu;
        private JTextArea taThongTinVeCu;

        // Form ghế mới (cùng chuyến)
        private JComboBox<String> cboGheMoi;
        private JTextField txtGiaVeMoi;
        private JButton btnLayDanhSachGhe;
        private JButton btnTinhChenhLech;

        // Tính chênh lệch
        private JLabel lblGiaVeCu;
        private JLabel lblGiaVeMoi;
        private JLabel lblChenhLech;
        private JLabel lblPhuongThucLabel;   // Label động: "PT thu thêm" / "PT hoàn lại"
        private JLabel lblGhiChuNganh;        // Hiện "Không phát sinh giao dịch" khi ngang giá
        private JComboBox<PhuongThuc> cboPhuongThuc;
        private JButton btnXacNhanDoi;

        private ChiTietVe veCuHienTai;

        public PanelDoiVe() {
            setLayout(new BorderLayout());
            setBackground(new Color(245, 247, 250));
            setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            add(taoHeaderCoNutQuayLai("Đổi vé (cùng chuyến, đổi ghế)"), BorderLayout.NORTH);

            JPanel pnBody = new JPanel(new BorderLayout(0, 12));
            pnBody.setOpaque(false);

            pnBody.add(taoSearchBox(), BorderLayout.NORTH);

            JPanel pnRow = new JPanel(new GridLayout(1, 2, 16, 0));
            pnRow.setOpaque(false);
            pnRow.add(taoPanelVeCu());
            pnRow.add(taoPanelVeMoi());
            pnBody.add(pnRow, BorderLayout.CENTER);

            add(pnBody, BorderLayout.CENTER);
            xoaTrang();
        }

        private JPanel taoSearchBox() {
            JPanel pn = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
            pn.setOpaque(false);
            pn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
            ));
            pn.setBackground(Color.WHITE);

            JLabel lbl = new JLabel("Mã vé cần đổi:");
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));

            txtMaVeCu = new JTextField();
            txtMaVeCu.setPreferredSize(new Dimension(200, 32));
            txtMaVeCu.setFont(new Font("Segoe UI", Font.PLAIN, 13));

            btnTimVeCu = taoNutMau("Tìm vé", new Color(0, 123, 255));
            btnTimVeCu.setPreferredSize(new Dimension(100, 32));
            btnTimVeCu.addActionListener(e -> timVeCu());
            txtMaVeCu.addActionListener(e -> timVeCu());

            pn.add(lbl);
            pn.add(txtMaVeCu);
            pn.add(btnTimVeCu);
            return pn;
        }

        private JPanel taoPanelVeCu() {
            JPanel pn = new JPanel(new BorderLayout(0, 8));
            pn.setBackground(Color.WHITE);
            pn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(new Color(180, 180, 180)),
                            "Vé cũ",
                            javax.swing.border.TitledBorder.LEFT,
                            javax.swing.border.TitledBorder.TOP,
                            new Font("Segoe UI", Font.BOLD, 13)
                    ),
                    BorderFactory.createEmptyBorder(8, 12, 12, 12)
            ));

            taThongTinVeCu = new JTextArea();
            taThongTinVeCu.setEditable(false);
            taThongTinVeCu.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            taThongTinVeCu.setLineWrap(true);
            taThongTinVeCu.setWrapStyleWord(true);
            taThongTinVeCu.setBackground(Color.WHITE);

            pn.add(new JScrollPane(taThongTinVeCu), BorderLayout.CENTER);
            return pn;
        }

        private JPanel taoPanelVeMoi() {
            JPanel pn = new JPanel(new BorderLayout(0, 8));
            pn.setBackground(Color.WHITE);
            pn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(new Color(180, 180, 180)),
                            "Ghế mới (cùng chuyến)",
                            javax.swing.border.TitledBorder.LEFT,
                            javax.swing.border.TitledBorder.TOP,
                            new Font("Segoe UI", Font.BOLD, 13)
                    ),
                    BorderFactory.createEmptyBorder(8, 12, 12, 12)
            ));

            JPanel pnFields = new JPanel(new GridBagLayout());
            pnFields.setOpaque(false);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(6, 4, 6, 4);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            btnLayDanhSachGhe = new JButton("Tải danh sách ghế trống");
            btnLayDanhSachGhe.setPreferredSize(new Dimension(220, 30));
            btnLayDanhSachGhe.addActionListener(e -> taiGheTrong());

            cboGheMoi = new JComboBox<>();
            cboGheMoi.setPreferredSize(new Dimension(380, 30));
            cboGheMoi.addActionListener(e -> tuTinhGiaTheoGhe());

            txtGiaVeMoi = new JTextField();
            txtGiaVeMoi.setPreferredSize(new Dimension(380, 30));
            txtGiaVeMoi.setEditable(false);  // Giá tự tính theo loại ghế, không cho sửa
            txtGiaVeMoi.setBackground(new Color(240, 240, 240));
            txtGiaVeMoi.setToolTipText("Giá vé mới được tự tính theo hệ số loại ghế");

            int row = 0;

            // Nút tải ghế
            gbc.gridy = row++;
            gbc.gridx = 0; gbc.weightx = 1;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.WEST;
            pnFields.add(btnLayDanhSachGhe, gbc);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            // Hàng ghế
            gbc.gridy = row++;
            gbc.gridx = 0; gbc.weightx = 0;
            pnFields.add(boldLabel("Ghế mới:"), gbc);
            gbc.gridy = row++; gbc.weightx = 1;
            pnFields.add(cboGheMoi, gbc);

            // Hàng giá
            gbc.gridy = row++;
            gbc.gridx = 0; gbc.weightx = 0;
            pnFields.add(boldLabel("Giá vé mới (tự tính):"), gbc);
            gbc.gridy = row++; gbc.weightx = 1;
            pnFields.add(txtGiaVeMoi, gbc);

            // Nút tính chênh lệch
            btnTinhChenhLech = taoNutMau("Tính chênh lệch", new Color(108, 117, 125));
            btnTinhChenhLech.setPreferredSize(new Dimension(160, 32));
            btnTinhChenhLech.addActionListener(e -> tuTinhChenhLech());

            gbc.gridy = row++;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.EAST;
            pnFields.add(btnTinhChenhLech, gbc);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            lblGiaVeCu = taoLabelGiaTri("0 ₫");
            lblGiaVeMoi = taoLabelGiaTri("0 ₫");
            lblChenhLech = taoLabelGiaTriBig("0 ₫");

            gbc.gridy = row++;
            gbc.gridx = 0; gbc.weightx = 0;
            pnFields.add(boldLabel("Giá vé cũ:"), gbc);
            gbc.gridy = row++; gbc.weightx = 1;
            pnFields.add(lblGiaVeCu, gbc);

            gbc.gridy = row++;
            gbc.gridx = 0; gbc.weightx = 0;
            pnFields.add(boldLabel("Giá vé mới:"), gbc);
            gbc.gridy = row++; gbc.weightx = 1;
            pnFields.add(lblGiaVeMoi, gbc);

            gbc.gridy = row++;
            gbc.gridx = 0; gbc.weightx = 0;
            pnFields.add(boldLabel("Chênh lệch:"), gbc);
            gbc.gridy = row++; gbc.weightx = 1;
            pnFields.add(lblChenhLech, gbc);

            // Phương thức (label động + dropdown + ghi chú khi ngang giá)
            cboPhuongThuc = new JComboBox<>();
            cboPhuongThuc.setPreferredSize(new Dimension(380, 30));
            for (PhuongThuc p : dao.layPhuongThucThanhToan()) {
                cboPhuongThuc.addItem(p);
            }

            // Label động: ban đầu để rỗng, sẽ update khi tính chênh lệch
            lblPhuongThucLabel = boldLabel("Phương thức:");

            // Label ghi chú khi ngang giá (ẩn ban đầu)
            lblGhiChuNganh = new JLabel("(Không phát sinh giao dịch)");
            lblGhiChuNganh.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            lblGhiChuNganh.setForeground(new Color(108, 117, 125));
            lblGhiChuNganh.setVisible(false);

            gbc.gridy = row++;
            gbc.gridx = 0; gbc.weightx = 0;
            pnFields.add(lblPhuongThucLabel, gbc);
            gbc.gridy = row++; gbc.weightx = 1;
            pnFields.add(cboPhuongThuc, gbc);

            // Hàng ghi chú
            gbc.gridy = row++;
            gbc.gridx = 0; gbc.weightx = 1;
            pnFields.add(lblGhiChuNganh, gbc);

            JScrollPane scroll = new JScrollPane(pnFields);
            scroll.setBorder(null);
            scroll.getVerticalScrollBar().setUnitIncrement(16);
            scroll.setOpaque(false);
            scroll.getViewport().setOpaque(false);
            pn.add(scroll, BorderLayout.CENTER);

            btnXacNhanDoi = taoNutMau("Xác nhận đổi vé", new Color(0, 123, 255));
            btnXacNhanDoi.setPreferredSize(new Dimension(180, 40));
            btnXacNhanDoi.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btnXacNhanDoi.addActionListener(e -> xacNhanDoiVe());

            JPanel pnNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            pnNut.setOpaque(false);
            pnNut.add(btnXacNhanDoi);
            pn.add(pnNut, BorderLayout.SOUTH);

            return pn;
        }

        private JLabel taoLabelGiaTri(String text) {
            JLabel lbl = new JLabel(text);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
            return lbl;
        }

        private JLabel taoLabelGiaTriBig(String text) {
            JLabel lbl = new JLabel(text);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
            return lbl;
        }

        // -- nghiệp vụ --

        private void xoaTrang() {
            taThongTinVeCu.setText("Nhập mã vé và bấm \"Tìm vé\" để xem thông tin.");
            cboGheMoi.removeAllItems();
            txtGiaVeMoi.setText("");
            lblGiaVeCu.setText("0 ₫");
            lblGiaVeMoi.setText("0 ₫");
            lblChenhLech.setText("0 ₫");
            lblChenhLech.setForeground(new Color(33, 37, 41));
            btnXacNhanDoi.setEnabled(false);
            cboGheMoi.setEnabled(false);
            txtGiaVeMoi.setEditable(false);
            btnLayDanhSachGhe.setEnabled(false);
            btnTinhChenhLech.setEnabled(false);
            // Reset phương thức về trạng thái mặc định
            lblPhuongThucLabel.setText("Phương thức:");
            lblPhuongThucLabel.setVisible(true);
            cboPhuongThuc.setVisible(true);
            lblGhiChuNganh.setVisible(false);
            veCuHienTai = null;
        }

        private void timVeCu() {
            String maVe = txtMaVeCu.getText().trim();
            if (maVe.isEmpty()) {
                JOptionPane.showMessageDialog(PanelDoiVe.this, "Vui lòng nhập mã vé.");
                return;
            }

            ChiTietVe ve = dao.layChiTietVe(maVe);
            if (ve == null) {
                JOptionPane.showMessageDialog(PanelDoiVe.this,
                        "Không tìm thấy vé với mã: " + maVe,
                        "Lỗi", JOptionPane.WARNING_MESSAGE);
                xoaTrang();
                return;
            }

            String loi = dao.kiemTraDuocDoiVe(ve);
            if (loi != null) {
                JOptionPane.showMessageDialog(PanelDoiVe.this,
                        loi, "Không thể đổi vé", JOptionPane.WARNING_MESSAGE);
                xoaTrang();
                taThongTinVeCu.setText(formatThongTinVe(ve));
                return;
            }

            veCuHienTai = ve;
            taThongTinVeCu.setText(formatThongTinVe(ve));
            lblGiaVeCu.setText(dinhDangTien.format(ve.tongTien) + " ₫");

            // Hiện hệ số loại ghế của ghế cũ trong tooltip
            DAO_BANVE.ThongTinLoaiGhe lgCu = dao.layThongTinLoaiGheCuaGhe(ve.maChuyenTau, ve.soGhe);
            if (lgCu != null) {
                lblGiaVeCu.setToolTipText(String.format(
                        "Loại ghế: %s (hệ số %.2f)", lgCu.tenLoaiGhe, lgCu.heSoLoaiGhe));
            }

            btnLayDanhSachGhe.setEnabled(true);
            btnTinhChenhLech.setEnabled(true);

            // Tự load luôn ghế trống của chính chuyến này
            taiGheTrong();
        }

        private void taiGheTrong() {
            if (veCuHienTai == null) return;

            cboGheMoi.removeAllItems();
            List<String> ghetrong = dao.layGheTrong(veCuHienTai.maChuyenTau);

            // Loại bỏ ghế cũ ra khỏi danh sách (vì đổi sang chính nó là vô nghĩa)
            for (String g : ghetrong) {
                if (!g.equals(veCuHienTai.soGhe)) {
                    cboGheMoi.addItem(g);
                }
            }

            if (cboGheMoi.getItemCount() == 0) {
                JOptionPane.showMessageDialog(PanelDoiVe.this,
                        "Chuyến này không còn ghế trống nào khác để đổi.",
                        "Hết ghế", JOptionPane.WARNING_MESSAGE);
                cboGheMoi.setEnabled(false);
                btnXacNhanDoi.setEnabled(false);
            } else {
                cboGheMoi.setEnabled(true);
            }
        }

        /**
         * Tự tính giá vé mới khi user chọn ghế khác trong combobox.
         * Dùng hệ số loại ghế để tính lại giá theo công thức:
         *   giáMới = giáCũ × (hệSố mới / hệSố cũ)
         */
        private void tuTinhGiaTheoGhe() {
            if (veCuHienTai == null) return;

            String gheMoi = (String) cboGheMoi.getSelectedItem();
            if (gheMoi == null) {
                txtGiaVeMoi.setText("");
                return;
            }

            // Gọi DAO tính giá theo hệ số loại ghế
            long giaMoi = dao.tinhGiaVeKhiDoiGhe(
                    veCuHienTai.maChuyenTau,
                    veCuHienTai.soGhe,
                    veCuHienTai.tongTien,
                    gheMoi);

            if (giaMoi < 0) {
                txtGiaVeMoi.setText("(Lỗi tính giá)");
                lblChenhLech.setText("—");
                btnXacNhanDoi.setEnabled(false);
                return;
            }

            txtGiaVeMoi.setText(String.valueOf(giaMoi));
            // Tự tính chênh lệch luôn
            tuTinhChenhLech();
        }

        /** Tự tính chênh lệch khi click nút "Tính chênh lệch" hoặc tự gọi từ tuTinhGiaTheoGhe */
        private void tuTinhChenhLech() {
            if (veCuHienTai == null) return;

            long giaMoi;
            try {
                giaMoi = Long.parseLong(txtGiaVeMoi.getText().trim());
                if (giaMoi < 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                lblGiaVeMoi.setText("—");
                lblChenhLech.setText("Giá không hợp lệ");
                lblChenhLech.setForeground(new Color(220, 53, 69));
                btnXacNhanDoi.setEnabled(false);
                return;
            }

            lblGiaVeMoi.setText(dinhDangTien.format(giaMoi) + " ₫");
            long chenh = giaMoi - veCuHienTai.tongTien;

            if (chenh > 0) {
                lblChenhLech.setText("+" + dinhDangTien.format(chenh) + " ₫ (thu thêm)");
                lblChenhLech.setForeground(new Color(220, 53, 69));
                // Hiện dropdown phương thức + label "thu thêm"
                lblPhuongThucLabel.setText("PT thu thêm:");
                lblPhuongThucLabel.setVisible(true);
                cboPhuongThuc.setVisible(true);
                lblGhiChuNganh.setVisible(false);

            } else if (chenh < 0) {
                lblChenhLech.setText(dinhDangTien.format(chenh) + " ₫ (hoàn lại)");
                lblChenhLech.setForeground(new Color(40, 167, 69));
                // Hiện dropdown phương thức + label "hoàn lại"
                lblPhuongThucLabel.setText("PT hoàn lại:");
                lblPhuongThucLabel.setVisible(true);
                cboPhuongThuc.setVisible(true);
                lblGhiChuNganh.setVisible(false);

            } else {
                lblChenhLech.setText("0 ₫ (ngang giá)");
                lblChenhLech.setForeground(new Color(33, 37, 41));
                // ẨN dropdown - không phát sinh giao dịch
                lblPhuongThucLabel.setVisible(false);
                cboPhuongThuc.setVisible(false);
                lblGhiChuNganh.setVisible(true);
            }

            // Refresh layout sau khi thay đổi visibility
            revalidate();
            repaint();

            btnXacNhanDoi.setEnabled(cboGheMoi.getSelectedItem() != null);
        }

        private void xacNhanDoiVe() {
            if (veCuHienTai == null) return;

            String gheMoi = (String) cboGheMoi.getSelectedItem();

            if (gheMoi == null) {
                JOptionPane.showMessageDialog(PanelDoiVe.this, "Vui lòng chọn ghế mới.");
                return;
            }

            long giaMoi;
            try {
                giaMoi = Long.parseLong(txtGiaVeMoi.getText().trim());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(PanelDoiVe.this, "Giá vé mới không hợp lệ.");
                return;
            }

            if (gheMoi.equals(veCuHienTai.soGhe)) {
                JOptionPane.showMessageDialog(PanelDoiVe.this,
                        "Ghế mới giống ghế cũ. Không thể đổi.");
                return;
            }

            long chenh = giaMoi - veCuHienTai.tongTien;

            // Chỉ check phương thức nếu có chênh lệch (không phải ngang giá)
            PhuongThuc pt = null;
            String tenPT = "Không phát sinh giao dịch";
            String maPT = "NGANG_GIA";

            if (chenh != 0) {
                pt = (PhuongThuc) cboPhuongThuc.getSelectedItem();
                if (pt == null) {
                    JOptionPane.showMessageDialog(PanelDoiVe.this,
                            "Vui lòng chọn phương thức thanh toán.");
                    return;
                }
                tenPT = pt.tenPhuongThuc;
                maPT = pt.maPhuongThuc;
            }

            // Tạo dòng phương thức trong thông báo (chỉ nếu có giao dịch)
            String dongPT = chenh != 0
                    ? String.format("  Phương thức:  %s\n", tenPT)
                    : "  (Ngang giá - không phát sinh giao dịch)\n";

            String thongBao = String.format(
                    "Xác nhận đổi vé?\n\n"
                    + "  Vé cũ:        %s\n"
                    + "    %s, ghế %s, %s ₫\n\n"
                    + "  Vé mới:       (sẽ tạo)\n"
                    + "    %s, ghế %s, %s ₫\n\n"
                    + "  Chênh lệch:   %s%s ₫\n"
                    + "%s",
                    veCuHienTai.maVe,
                    veCuHienTai.tenChuyen, veCuHienTai.soGhe,
                    dinhDangTien.format(veCuHienTai.tongTien),
                    veCuHienTai.tenChuyen, gheMoi,
                    dinhDangTien.format(giaMoi),
                    chenh > 0 ? "+" : "",
                    dinhDangTien.format(chenh),
                    dongPT);

            if (JOptionPane.showConfirmDialog(PanelDoiVe.this, thongBao,
                    "Xác nhận đổi vé", JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE) != JOptionPane.YES_OPTION) {
                return;
            }

            // Đổi vé - cùng chuyến
            String maVeMoi = dao.doiVe(
                    veCuHienTai.maVe,
                    veCuHienTai.maChuyenTau,
                    gheMoi,
                    veCuHienTai.maLoaiVe,
                    veCuHienTai.maKhuyenMai,
                    giaMoi,
                    chenh,
                    maPT,
                    maNhanVienDangNhap);

            if (maVeMoi != null) {
                // Hỏi user có muốn xuất PDF cho vé mới không
                int chon = JOptionPane.showConfirmDialog(PanelDoiVe.this,
                        "Đổi vé thành công.\nMã vé mới: " + maVeMoi
                        + "\n\nBạn có muốn xuất PDF cho vé mới không?",
                        "Đổi vé thành công",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.INFORMATION_MESSAGE);

                if (chon == JOptionPane.YES_OPTION) {
                    // Lấy chi tiết vé mới và xuất PDF
                    ChiTietVe veMoi = dao.layChiTietVe(maVeMoi);
                    if (veMoi != null) {
                        hoiVaXuatPDF(PanelDoiVe.this, veMoi);
                    }
                }

                xoaTrang();
                txtMaVeCu.setText("");
            } else {
                JOptionPane.showMessageDialog(PanelDoiVe.this,
                        "Đổi vé thất bại.",
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }

        private String formatThongTinVe(ChiTietVe v) {
            StringBuilder sb = new StringBuilder();
            sb.append("══ THÔNG TIN VÉ ══\n");
            sb.append("Mã vé:           ").append(v.maVe).append("\n");
            sb.append("Trạng thái:      ").append(v.trangThaiVe).append("\n\n");

            sb.append("══ HÀNH KHÁCH ══\n");
            sb.append("Họ tên:          ").append(v.hoTenHanhKhach).append("\n");
            sb.append("Số GT:           ").append(v.soGiayTo == null ? "-" : v.soGiayTo).append("\n\n");

            sb.append("══ CHUYẾN ══\n");
            sb.append("Chuyến:          ").append(v.tenChuyen).append("\n");
            sb.append("Tuyến:           ").append(v.tenGaDi).append(" -> ").append(v.tenGaDen).append("\n");
            sb.append("Khởi hành:       ").append(v.ngayKhoiHanh.format(fmtDateTime)).append("\n\n");

            sb.append("══ VÉ ══\n");
            sb.append("Số ghế:          ").append(v.soGhe).append("\n");
            sb.append("Loại vé:         ").append(v.tenLoaiVe).append("\n");
            sb.append("Tổng tiền:       ").append(dinhDangTien.format(v.tongTien)).append(" ₫\n");

            if (v.ghiChu != null && !v.ghiChu.isEmpty()) {
                sb.append("\n══ GHI CHÚ ══\n").append(v.ghiChu).append("\n");
            }
            return sb.toString();
        }
    }

    // ==========================================================
    // PANEL TRA CỨU VÉ
    // ==========================================================

    private class PanelTracuu extends JPanel {
        private JTextField txtMaVe;
        private JTextField txtHoTen;
        private JTextField txtSoGiayTo;
        private JTextField txtSdt;
        private JButton btnTim;
        private JButton btnLamMoi;
        private javax.swing.JTable tbl;
        private javax.swing.table.DefaultTableModel model;
        private JTextArea taChiTiet;
        private JButton btnXuatPDF;
        private JLabel lblTongKetQua;

        private ChiTietVe veDangXem;

        private final String[] COLUMNS = {
            "Mã vé", "Ngày bán", "Hành khách", "Chuyến", "Khởi hành",
            "Ghế", "Tổng tiền", "Trạng thái"
        };

        public PanelTracuu() {
            setLayout(new BorderLayout());
            setBackground(new Color(245, 247, 250));
            setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

            add(taoHeaderCoNutQuayLai("Tra cứu vé"), BorderLayout.NORTH);

            JPanel pnBody = new JPanel(new BorderLayout(0, 12));
            pnBody.setOpaque(false);

            pnBody.add(taoSearchBox(), BorderLayout.NORTH);

            JPanel pnRow = new JPanel(new GridLayout(1, 2, 16, 0));
            pnRow.setOpaque(false);
            pnRow.add(taoBangKetQua());
            pnRow.add(taoChiTiet());
            pnBody.add(pnRow, BorderLayout.CENTER);

            add(pnBody, BorderLayout.CENTER);

            timKiem();
        }

        private JPanel taoSearchBox() {
            JPanel pn = new JPanel(new GridBagLayout());
            pn.setOpaque(false);
            pn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(220, 220, 220)),
                    BorderFactory.createEmptyBorder(10, 12, 10, 12)
            ));
            pn.setBackground(Color.WHITE);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(4, 4, 4, 8);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;

            // Tạo 4 ô tìm kiếm
            txtMaVe = taoOTimKiem("Mã vé...");
            txtHoTen = taoOTimKiem("Họ tên hành khách...");
            txtSoGiayTo = taoOTimKiem("CCCD/Passport...");
            txtSdt = taoOTimKiem("SĐT...");

            // 2 hàng x 2 cột
            // Hàng 1: Mã vé | Họ tên
            gbc.gridy = 0;
            gbc.gridx = 0; gbc.weightx = 0;
            pn.add(taoLabelTimKiem("Mã vé:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            pn.add(txtMaVe, gbc);
            gbc.gridx = 2; gbc.weightx = 0;
            pn.add(taoLabelTimKiem("Họ tên:"), gbc);
            gbc.gridx = 3; gbc.weightx = 1;
            pn.add(txtHoTen, gbc);

            // Hàng 2: Số GT | SĐT
            gbc.gridy = 1;
            gbc.gridx = 0; gbc.weightx = 0;
            pn.add(taoLabelTimKiem("Số GT:"), gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            pn.add(txtSoGiayTo, gbc);
            gbc.gridx = 2; gbc.weightx = 0;
            pn.add(taoLabelTimKiem("SĐT:"), gbc);
            gbc.gridx = 3; gbc.weightx = 1;
            pn.add(txtSdt, gbc);

            // Hàng 3: 2 nút + hint
            btnTim = taoNutMau("Tìm", new Color(0, 123, 255));
            btnTim.setPreferredSize(new Dimension(100, 32));
            btnTim.addActionListener(e -> timKiem());

            btnLamMoi = new JButton("Làm mới");
            btnLamMoi.setPreferredSize(new Dimension(100, 32));
            btnLamMoi.addActionListener(e -> {
                txtMaVe.setText("");
                txtHoTen.setText("");
                txtSoGiayTo.setText("");
                txtSdt.setText("");
                timKiem();
            });

            // Enter key trên bất kỳ ô nào → tìm kiếm
            java.awt.event.ActionListener enterTim = e -> timKiem();
            txtMaVe.addActionListener(enterTim);
            txtHoTen.addActionListener(enterTim);
            txtSoGiayTo.addActionListener(enterTim);
            txtSdt.addActionListener(enterTim);

            JLabel lblHint = new JLabel("(Bỏ trống tất cả ô để xem mọi vé. Nhập nhiều ô cùng lúc → kết hợp AND)");
            lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
            lblHint.setForeground(new Color(120, 120, 120));

            JPanel pnNut = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
            pnNut.setOpaque(false);
            pnNut.add(btnTim);
            pnNut.add(btnLamMoi);
            pnNut.add(lblHint);

            gbc.gridy = 2;
            gbc.gridx = 0; gbc.gridwidth = 4; gbc.weightx = 1;
            gbc.insets = new Insets(8, 0, 0, 0);
            pn.add(pnNut, gbc);

            return pn;
        }

        private JLabel taoLabelTimKiem(String text) {
            JLabel lbl = new JLabel(text);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            Dimension d = new Dimension(70, 28);
            lbl.setPreferredSize(d);
            return lbl;
        }

        private JTextField taoOTimKiem(String tooltip) {
            JTextField txt = new JTextField();
            txt.setPreferredSize(new Dimension(180, 28));
            txt.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            txt.setToolTipText(tooltip);
            return txt;
        }

        private JPanel taoBangKetQua() {
            JPanel pn = new JPanel(new BorderLayout(0, 8));
            pn.setBackground(Color.WHITE);
            pn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(new Color(180, 180, 180)),
                            "Danh sách vé",
                            javax.swing.border.TitledBorder.LEFT,
                            javax.swing.border.TitledBorder.TOP,
                            new Font("Segoe UI", Font.BOLD, 13)
                    ),
                    BorderFactory.createEmptyBorder(8, 12, 12, 12)
            ));

            model = new javax.swing.table.DefaultTableModel(COLUMNS, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };

            tbl = new javax.swing.JTable(model);
            tbl.setRowHeight(26);
            tbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
            tbl.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);

            // Render trạng thái có màu
            tbl.getColumnModel().getColumn(7).setCellRenderer(
                    new javax.swing.table.DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(javax.swing.JTable t, Object v,
                        boolean sel, boolean foc, int r, int c) {
                    Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                    if (!sel) {
                        String s = v == null ? "" : v.toString();
                        if (s.startsWith("Đã thanh toán")) {
                            comp.setForeground(new Color(40, 167, 69));
                        } else if (s.equals("Đã trả")) {
                            comp.setForeground(new Color(220, 53, 69));
                        } else if (s.startsWith("Đã đổi")) {
                            comp.setForeground(new Color(255, 153, 0));
                        } else {
                            comp.setForeground(Color.BLACK);
                        }
                    }
                    setHorizontalAlignment(SwingConstants.CENTER);
                    return comp;
                }
            });

            int[] widths = {70, 130, 150, 150, 130, 130, 110, 130};
            for (int i = 0; i < widths.length; i++) {
                tbl.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
            }

            // Click row → load chi tiết
            tbl.getSelectionModel().addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int row = tbl.getSelectedRow();
                    if (row >= 0) {
                        String maVe = tbl.getValueAt(row, 0).toString();
                        ChiTietVe v = dao.layChiTietVe(maVe);
                        if (v != null) {
                            veDangXem = v;
                            taChiTiet.setText(formatChiTiet(v));
                            btnXuatPDF.setEnabled(true);
                        }
                    }
                }
            });

            JScrollPane scroll = new JScrollPane(tbl);
            scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
            pn.add(scroll, BorderLayout.CENTER);
            return pn;
        }

        private JPanel taoChiTiet() {
            JPanel pn = new JPanel(new BorderLayout(0, 8));
            pn.setBackground(Color.WHITE);
            pn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(
                            BorderFactory.createLineBorder(new Color(180, 180, 180)),
                            "Chi tiết vé",
                            javax.swing.border.TitledBorder.LEFT,
                            javax.swing.border.TitledBorder.TOP,
                            new Font("Segoe UI", Font.BOLD, 13)
                    ),
                    BorderFactory.createEmptyBorder(8, 12, 12, 12)
            ));

            taChiTiet = new JTextArea();
            taChiTiet.setEditable(false);
            taChiTiet.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            taChiTiet.setLineWrap(true);
            taChiTiet.setWrapStyleWord(true);
            taChiTiet.setBackground(Color.WHITE);
            taChiTiet.setText("Click vào 1 vé trong bảng bên trái để xem chi tiết.");

            pn.add(new JScrollPane(taChiTiet), BorderLayout.CENTER);

            // Nút xuất PDF
            btnXuatPDF = taoNutMau("Xuất PDF", new Color(40, 167, 69));
            btnXuatPDF.setPreferredSize(new Dimension(160, 36));
            btnXuatPDF.setFont(new Font("Segoe UI", Font.BOLD, 13));
            btnXuatPDF.setEnabled(false);
            btnXuatPDF.addActionListener(e -> xuatPDF());

            JPanel pnNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
            pnNut.setOpaque(false);
            pnNut.add(btnXuatPDF);
            pn.add(pnNut, BorderLayout.SOUTH);
            return pn;
        }

        private void timKiem() {
            String maVe = txtMaVe.getText().trim();
            String hoTen = txtHoTen.getText().trim();
            String soGT = txtSoGiayTo.getText().trim();
            String sdt = txtSdt.getText().trim();

            List<DAO_BANVE.TomTatVe> ds = dao.tracuuVe(maVe, hoTen, soGT, sdt);

            model.setRowCount(0);
            for (DAO_BANVE.TomTatVe t : ds) {
                model.addRow(new Object[]{
                    t.maVe,
                    t.ngayBan == null ? "" : t.ngayBan.format(fmtDateTime),
                    t.hoTen,
                    t.tenChuyen,
                    t.ngayKhoiHanh == null ? "" : t.ngayKhoiHanh.format(fmtDateTime),
                    t.soGhe,
                    dinhDangTien.format(t.tongTien) + " ₫",
                    t.trangThaiVe
                });
            }

            // Reset chi tiết
            if (ds.isEmpty()) {
                taChiTiet.setText("Không tìm thấy vé phù hợp với tiêu chí tra cứu.");
            } else {
                taChiTiet.setText("Tìm thấy " + ds.size() + " vé. "
                        + "Click vào 1 vé trong bảng bên trái để xem chi tiết.");
            }
            btnXuatPDF.setEnabled(false);
            veDangXem = null;
        }

        private String formatChiTiet(ChiTietVe v) {
            StringBuilder sb = new StringBuilder();
            sb.append("══ THÔNG TIN VÉ ══\n");
            sb.append("Mã vé:           ").append(v.maVe).append("\n");
            sb.append("Trạng thái:      ").append(v.trangThaiVe).append("\n");
            sb.append("Ngày bán:        ").append(v.ngayBan.format(fmtDateTime)).append("\n\n");

            sb.append("══ HÀNH KHÁCH ══\n");
            sb.append("Họ tên:          ").append(v.hoTenHanhKhach).append("\n");
            sb.append("Số giấy tờ:      ").append(v.soGiayTo == null ? "-" : v.soGiayTo).append("\n");
            sb.append("SĐT:             ").append(v.sdt == null ? "-" : v.sdt).append("\n\n");

            sb.append("══ CHUYẾN TÀU ══\n");
            sb.append("Mã chuyến:       ").append(v.maChuyenTau).append("\n");
            sb.append("Tên chuyến:      ").append(v.tenChuyen).append("\n");
            sb.append("Tuyến:           ").append(v.tenGaDi).append(" -> ").append(v.tenGaDen).append("\n");
            sb.append("Khởi hành:       ").append(v.ngayKhoiHanh.format(fmtDateTime)).append("\n");
            sb.append("Đến dự kiến:     ").append(v.ngayDenDuKien.format(fmtDateTime)).append("\n\n");

            sb.append("══ VÉ ══\n");
            sb.append("Số ghế:          ").append(v.soGhe).append("\n");
            sb.append("Loại vé:         ").append(v.tenLoaiVe).append("\n");
            sb.append("Tổng tiền:       ").append(dinhDangTien.format(v.tongTien)).append(" ₫\n");

            if (v.ghiChu != null && !v.ghiChu.isEmpty()) {
                sb.append("\n══ GHI CHÚ ══\n").append(v.ghiChu).append("\n");
            }
            return sb.toString();
        }

        /** Xuất PDF dùng method shared của outer class */
        private void xuatPDF() {
            if (veDangXem == null) return;
            hoiVaXuatPDF(PanelTracuu.this, veDangXem);
        }
    }
}