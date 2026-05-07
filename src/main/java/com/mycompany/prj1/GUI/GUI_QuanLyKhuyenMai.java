package com.mycompany.prj1.GUI;

import com.mycompany.prj1.DAO.DAO_KHUYENMAI;
import com.mycompany.prj1.entity.KhuyenMai;
import com.toedter.calendar.JDateChooser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Giao diện quản lý khuyến mãi.
 *
 *   - NORTH: form nhập (mã / tên / % giảm / từ ngày / đến ngày)
 *   - CENTER: ô tìm kiếm + bảng danh sách
 */
public class GUI_QuanLyKhuyenMai extends JPanel {

    private final DAO_KHUYENMAI dao = new DAO_KHUYENMAI();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Form
    private JTextField txtMa;
    private JTextField txtTen;
    private JTextField txtPhanTram;
    private JDateChooser dcBatDau;
    private JDateChooser dcKetThuc;

    // Buttons
    private JButton btnThemMoi;
    private JButton btnLuu;
    private JButton btnCapNhat;
    private JButton btnXoa;
    private JButton btnLamMoi;

    // Search + table
    private JTextField txtTimKiem;
    private JButton btnTim;
    private JTable tbl;
    private DefaultTableModel model;

    private enum CheDo { XEM, THEM, SUA }
    private CheDo cheDo = CheDo.XEM;

    private static final String[] COLUMNS = {
        "Mã KM", "Tên khuyến mãi", "% giảm", "Bắt đầu", "Kết thúc", "Trạng thái"
    };

    public GUI_QuanLyKhuyenMai() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        khoiTaoForm();
        khoiTaoBangVaTimKiem();
        ganSuKien();

        chuyenSangCheDo(CheDo.XEM);
        loadDanhSach();
    }

    // ==========================================================
    // FORM (NORTH)
    // ==========================================================

    private void khoiTaoForm() {
        JPanel pnNorth = new JPanel(new BorderLayout());
        pnNorth.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 180)),
                        "THÔNG TIN KHUYẾN MÃI",
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14)
                ),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        pnNorth.setBackground(Color.WHITE);

        Dimension inputSize = new Dimension(180, 28);

        txtMa = new JTextField();
        txtMa.setEditable(false);
        txtMa.setBackground(new Color(240, 240, 240));
        txtMa.setPreferredSize(inputSize);

        txtTen = new JTextField();
        txtTen.setPreferredSize(inputSize);
        txtPhanTram = new JTextField();
        txtPhanTram.setPreferredSize(inputSize);

        dcBatDau = new JDateChooser();
        dcBatDau.setDateFormatString("dd/MM/yyyy");
        dcBatDau.setDate(new Date());
        dcBatDau.setPreferredSize(inputSize);

        dcKetThuc = new JDateChooser();
        dcKetThuc.setDateFormatString("dd/MM/yyyy");
        dcKetThuc.setPreferredSize(inputSize);

        // Layout
        JPanel pnFields = new JPanel(new GridBagLayout());
        pnFields.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Hàng 1: Mã | Tên (rộng) | % giảm
        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0;
        pnFields.add(boldLabel("Mã KM:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        pnFields.add(txtMa, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        pnFields.add(boldLabel("Tên KM:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        pnFields.add(txtTen, gbc);
        gbc.gridx = 4; gbc.weightx = 0;
        pnFields.add(boldLabel("% giảm:"), gbc);
        gbc.gridx = 5; gbc.weightx = 0.3;
        pnFields.add(txtPhanTram, gbc);

        // Hàng 2: Bắt đầu | Kết thúc
        gbc.gridy = 1;
        gbc.gridx = 0; gbc.weightx = 0;
        pnFields.add(boldLabel("Từ ngày:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        pnFields.add(dcBatDau, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        pnFields.add(boldLabel("Đến ngày:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        pnFields.add(dcKetThuc, gbc);
        // 2 ô cuối để trống cho cân
        gbc.gridx = 4; gbc.weightx = 0;
        pnFields.add(new JLabel(""), gbc);
        gbc.gridx = 5; gbc.weightx = 0.3;
        pnFields.add(new JLabel(""), gbc);

        pnNorth.add(pnFields, BorderLayout.CENTER);

        // Buttons row
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        pnButtons.setOpaque(false);

        btnThemMoi = taoNutMau("Thêm mới", new Color(40, 167, 69));
        btnLuu = taoNutMau("Lưu", new Color(0, 123, 255));
        btnCapNhat = taoNutMau("Cập nhật", new Color(255, 193, 7));
        btnXoa = taoNutMau("Xoá", new Color(220, 53, 69));
        btnLamMoi = taoNut("Làm mới");

        pnButtons.add(btnThemMoi);
        pnButtons.add(btnLuu);
        pnButtons.add(btnCapNhat);
        pnButtons.add(btnXoa);
        pnButtons.add(btnLamMoi);

        pnNorth.add(pnButtons, BorderLayout.SOUTH);
        add(pnNorth, BorderLayout.NORTH);
    }

    private JLabel boldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        Dimension labelSize = new Dimension(90, 28);
        lbl.setPreferredSize(labelSize);
        lbl.setMinimumSize(labelSize);
        return lbl;
    }

    private JButton taoNut(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btn.setPreferredSize(new Dimension(110, 32));
        return btn;
    }

    private JButton taoNutMau(String text, Color bg) {
        JButton btn = taoNut(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        return btn;
    }

    // ==========================================================
    // SEARCH + TABLE (CENTER)
    // ==========================================================

    private void khoiTaoBangVaTimKiem() {
        JPanel pnCenter = new JPanel(new BorderLayout(0, 6));
        pnCenter.setBackground(new Color(245, 247, 250));
        pnCenter.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Search
        JPanel pnSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pnSearch.setOpaque(false);

        JLabel lblTim = new JLabel("Tìm kiếm:");
        lblTim.setFont(new Font("Segoe UI", Font.BOLD, 12));

        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(380, 30));
        txtTimKiem.setToolTipText("Nhập mã KM hoặc tên khuyến mãi");

        btnTim = taoNutMau("Tìm", new Color(0, 123, 255));
        btnTim.setPreferredSize(new Dimension(80, 30));

        JLabel lblHint = new JLabel("(Tìm theo mã KM hoặc tên khuyến mãi)");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(new Color(120, 120, 120));

        pnSearch.add(lblTim);
        pnSearch.add(txtTimKiem);
        pnSearch.add(btnTim);
        pnSearch.add(lblHint);

        pnCenter.add(pnSearch, BorderLayout.NORTH);

        // Table
        model = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tbl = new JTable(model);
        tbl.setRowHeight(28);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Cột trạng thái có màu
        tbl.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) {
                    String s = v == null ? "" : v.toString();
                    if (s.equalsIgnoreCase("Đang chạy")) {
                        comp.setForeground(new Color(40, 167, 69));
                    } else if (s.equalsIgnoreCase("Chưa bắt đầu")) {
                        comp.setForeground(new Color(0, 123, 255));
                    } else if (s.equalsIgnoreCase("Đã kết thúc")) {
                        comp.setForeground(new Color(120, 120, 120));
                    } else {
                        comp.setForeground(Color.BLACK);
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return comp;
            }
        });

        // Cột % giảm canh giữa
        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tbl.getColumnModel().getColumn(2).setCellRenderer(center);
        tbl.getColumnModel().getColumn(3).setCellRenderer(center);
        tbl.getColumnModel().getColumn(4).setCellRenderer(center);

        int[] widths = {100, 280, 80, 110, 110, 130};
        for (int i = 0; i < widths.length; i++) {
            tbl.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        pnCenter.add(scroll, BorderLayout.CENTER);
        add(pnCenter, BorderLayout.CENTER);
    }

    // ==========================================================
    // SỰ KIỆN
    // ==========================================================

    private void ganSuKien() {
        btnThemMoi.addActionListener(e -> {
            chuyenSangCheDo(CheDo.THEM);
            xoaTrongForm();
            txtMa.setText(dao.sinhMaMoi());
            dcBatDau.setDate(new Date());
            // Mặc định kết thúc = bắt đầu + 30 ngày
            dcKetThuc.setDate(java.sql.Date.valueOf(LocalDate.now().plusDays(30)));
        });

        btnLuu.addActionListener(e -> luuMoi());
        btnCapNhat.addActionListener(e -> capNhat());
        btnXoa.addActionListener(e -> xoa());

        btnLamMoi.addActionListener(e -> {
            xoaTrongForm();
            txtTimKiem.setText("");
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSach();
        });

        btnTim.addActionListener(e -> timKiem());

        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { timKiem(); }
            @Override public void removeUpdate(DocumentEvent e) { timKiem(); }
            @Override public void changedUpdate(DocumentEvent e) { timKiem(); }
        });

        tbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tbl.getSelectedRow();
                if (row >= 0) {
                    String maKM = tbl.getValueAt(row, 0).toString();
                    KhuyenMai km = dao.layTheoMa(maKM);
                    if (km != null) {
                        doKhuyenMaiLenForm(km);
                        chuyenSangCheDo(CheDo.SUA);
                    }
                }
            }
        });
    }

    private void chuyenSangCheDo(CheDo cd) {
        cheDo = cd;
        switch (cd) {
            case XEM:
                btnThemMoi.setEnabled(true);
                btnLuu.setEnabled(false);
                btnCapNhat.setEnabled(false);
                btnXoa.setEnabled(false);
                setEditable(false);
                break;
            case THEM:
                btnThemMoi.setEnabled(false);
                btnLuu.setEnabled(true);
                btnCapNhat.setEnabled(false);
                btnXoa.setEnabled(false);
                setEditable(true);
                break;
            case SUA:
                btnThemMoi.setEnabled(true);
                btnLuu.setEnabled(false);
                btnCapNhat.setEnabled(true);
                btnXoa.setEnabled(true);
                setEditable(true);
                break;
        }
    }

    private void setEditable(boolean ed) {
        txtTen.setEditable(ed);
        txtPhanTram.setEditable(ed);
        dcBatDau.setEnabled(ed);
        dcKetThuc.setEnabled(ed);
    }

    // ==========================================================
    // NGHIỆP VỤ
    // ==========================================================

    private void loadDanhSach() {
        doDanhSachLenBang(dao.layTatCa());
    }

    private void timKiem() {
        doDanhSachLenBang(dao.tim(txtTimKiem.getText()));
    }

    private void doDanhSachLenBang(List<KhuyenMai> ds) {
        model.setRowCount(0);
        LocalDate hienTai = LocalDate.now();

        for (KhuyenMai km : ds) {
            String trangThai;
            if (km.getThoiGianBatDau() != null && hienTai.isBefore(km.getThoiGianBatDau())) {
                trangThai = "Chưa bắt đầu";
            } else if (km.getThoiGianKetThuc() != null && hienTai.isAfter(km.getThoiGianKetThuc())) {
                trangThai = "Đã kết thúc";
            } else {
                trangThai = "Đang chạy";
            }

            model.addRow(new Object[]{
                km.getMaKhuyenMai(),
                km.getTenKhuyenMai(),
                String.format("%.1f%%", km.getPhanTramGiam()),
                km.getThoiGianBatDau() == null ? "" : km.getThoiGianBatDau().format(fmt),
                km.getThoiGianKetThuc() == null ? "" : km.getThoiGianKetThuc().format(fmt),
                trangThai
            });
        }
    }

    private void doKhuyenMaiLenForm(KhuyenMai km) {
        txtMa.setText(km.getMaKhuyenMai());
        txtTen.setText(km.getTenKhuyenMai());
        txtPhanTram.setText(String.valueOf(km.getPhanTramGiam()));

        if (km.getThoiGianBatDau() != null) {
            dcBatDau.setDate(java.sql.Date.valueOf(km.getThoiGianBatDau()));
        } else {
            dcBatDau.setDate(null);
        }

        if (km.getThoiGianKetThuc() != null) {
            dcKetThuc.setDate(java.sql.Date.valueOf(km.getThoiGianKetThuc()));
        } else {
            dcKetThuc.setDate(null);
        }
    }

    private void xoaTrongForm() {
        txtMa.setText("");
        txtTen.setText("");
        txtPhanTram.setText("");
        dcBatDau.setDate(null);
        dcKetThuc.setDate(null);
        tbl.clearSelection();
    }

    private KhuyenMai layKMTuForm() {
        KhuyenMai km = new KhuyenMai();
        km.setMaKhuyenMai(txtMa.getText().trim());
        km.setTenKhuyenMai(txtTen.getText().trim());
        try {
            km.setPhanTramGiam(Double.parseDouble(txtPhanTram.getText().trim()));
        } catch (NumberFormatException e) {
            km.setPhanTramGiam(0);
        }

        if (dcBatDau.getDate() != null) {
            km.setThoiGianBatDau(dcBatDau.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
        }
        if (dcKetThuc.getDate() != null) {
            km.setThoiGianKetThuc(dcKetThuc.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
        }
        return km;
    }

    private String validateForm() {
        String ten = txtTen.getText().trim();
        if (ten.isEmpty()) return "Vui lòng nhập tên khuyến mãi.";
        if (ten.length() > 100) return "Tên không được quá 100 ký tự.";

        String pt = txtPhanTram.getText().trim();
        if (pt.isEmpty()) return "Vui lòng nhập % giảm giá.";
        try {
            double v = Double.parseDouble(pt);
            if (v < 0 || v > 100) return "% giảm phải nằm trong khoảng 0 - 100.";
        } catch (NumberFormatException e) {
            return "% giảm không hợp lệ (phải là số).";
        }

        if (dcBatDau.getDate() == null) return "Vui lòng chọn ngày bắt đầu.";
        if (dcKetThuc.getDate() == null) return "Vui lòng chọn ngày kết thúc.";

        LocalDate bd = dcBatDau.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate kt = dcKetThuc.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();

        if (!kt.isAfter(bd)) {
            return "Ngày kết thúc phải sau ngày bắt đầu.";
        }

        return null;
    }

    private void luuMoi() {
        String loi = validateForm();
        if (loi != null) {
            JOptionPane.showMessageDialog(this, loi, "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Xác nhận thêm khuyến mãi mới?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.them(layKMTuForm())) {
            JOptionPane.showMessageDialog(this, "Thêm khuyến mãi thành công.");
            xoaTrongForm();
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void capNhat() {
        String loi = validateForm();
        if (loi != null) {
            JOptionPane.showMessageDialog(this, loi, "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Xác nhận cập nhật?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.capNhat(layKMTuForm())) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công.");
            loadDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoa() {
        String maKM = txtMa.getText().trim();
        if (maKM.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khuyến mãi cần xoá.");
            return;
        }

        // Check trước có vé nào đang dùng không
        int soVe = dao.demSoVeDangDung(maKM);
        if (soVe > 0) {
            JOptionPane.showMessageDialog(this,
                    "Không thể xoá! Đã có " + soVe + " vé sử dụng khuyến mãi này.\n"
                    + "Khuyến mãi cần được giữ lại để đối soát hoá đơn.",
                    "Không thể xoá", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this,
                "Xác nhận xoá khuyến mãi " + maKM + "?",
                "Xác nhận xoá",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.xoa(maKM)) {
            JOptionPane.showMessageDialog(this, "Xoá thành công.");
            xoaTrongForm();
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "Xoá thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}