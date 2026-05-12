package com.mycompany.prj1.GUI;

import com.mycompany.prj1.DAO.DAO_CHUYENTAU_QL;
import com.mycompany.prj1.entity.ChuyenTau;
import com.mycompany.prj1.entity.Ga;
import com.mycompany.prj1.entity.Tau;
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
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Giao diện quản lý chuyến tàu.
 *
 * Logic chống trùng:
 *   1 tàu trong 1 NGÀY KHỞI HÀNH chỉ được có 1 chuyến.
 *   Tàu chạy qua đêm: vẫn tính theo ngày khởi hành, không tính ngày đến.
 */
public class GUI_QuanLyChuyenTau extends JPanel {

    private final DAO_CHUYENTAU_QL dao = new DAO_CHUYENTAU_QL();
    private final DateTimeFormatter fmtDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter fmtDateTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    // Form
    private JTextField txtMa;
    private JTextField txtTenChuyen;
    private JComboBox<TauItem> cboTau;
    private JComboBox<GaItem> cboGaDi;
    private JComboBox<GaItem> cboGaDen;
    private JDateChooser dcNgayKhoi;
    private JSpinner spGioKhoi;
    private JSpinner spPhutKhoi;
    private JDateChooser dcNgayDen;
    private JSpinner spGioDen;
    private JSpinner spPhutDen;
    private JTextField txtCuLy;
    private JComboBox<String> cboTrangThai;

    // Buttons
    private JButton btnThemMoi;
    private JButton btnLuu;
    private JButton btnCapNhat;
    private JButton btnHuyChuyen;
    private JButton btnKhoiPhuc;
    private JButton btnLamMoi;

    // Search + table
    private JTextField txtTimKiem;
    private JButton btnTim;
    private JTable tbl;
    private DefaultTableModel model;

    private enum CheDo { XEM, THEM, SUA }
    private CheDo cheDo = CheDo.XEM;

    private static final String[] COLUMNS = {
        "Mã chuyến", "Tên chuyến", "Tàu", "Khởi hành", "Đến dự kiến",
        "Tuyến", "Cự ly (km)", "Trạng thái"
    };

    /** Wrapper Tau cho combo */
    private static class TauItem {
        Tau tau;
        TauItem(Tau t) { this.tau = t; }
        @Override public String toString() {
            if (tau == null) return "-- Chọn tàu --";
            return tau.getMaTau() + " (" + tau.getTrangThaiTau() + ")";
        }
    }

    /** Wrapper Ga cho combo */
    private static class GaItem {
        Ga ga;
        GaItem(Ga g) { this.ga = g; }
        @Override public String toString() {
            if (ga == null) return "-- Chọn ga --";
            return ga.getMaGa() + " - " + ga.getTenGa();
        }
    }

    public GUI_QuanLyChuyenTau() {
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
                        "THÔNG TIN CHUYẾN TÀU",
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14)
                ),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        pnNorth.setBackground(Color.WHITE);

        Dimension inputSize = new Dimension(180, 28);
        Dimension shortInput = new Dimension(110, 28);
        Dimension spinnerSize = new Dimension(60, 28);

        txtMa = new JTextField();
        txtMa.setEditable(false);
        txtMa.setBackground(new Color(240, 240, 240));
        txtMa.setPreferredSize(inputSize);

        txtTenChuyen = new JTextField();
        txtTenChuyen.setPreferredSize(inputSize);

        cboTau = new JComboBox<>();
        cboTau.setPreferredSize(inputSize);

        cboGaDi = new JComboBox<>();
        cboGaDi.setPreferredSize(inputSize);

        cboGaDen = new JComboBox<>();
        cboGaDen.setPreferredSize(inputSize);

        dcNgayKhoi = new JDateChooser();
        dcNgayKhoi.setDateFormatString("dd/MM/yyyy");
        dcNgayKhoi.setPreferredSize(shortInput);

        spGioKhoi = new JSpinner(new SpinnerNumberModel(7, 0, 23, 1));
        spGioKhoi.setPreferredSize(spinnerSize);
        spPhutKhoi = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));
        spPhutKhoi.setPreferredSize(spinnerSize);

        dcNgayDen = new JDateChooser();
        dcNgayDen.setDateFormatString("dd/MM/yyyy");
        dcNgayDen.setPreferredSize(shortInput);

        spGioDen = new JSpinner(new SpinnerNumberModel(15, 0, 23, 1));
        spGioDen.setPreferredSize(spinnerSize);
        spPhutDen = new JSpinner(new SpinnerNumberModel(0, 0, 59, 5));
        spPhutDen.setPreferredSize(spinnerSize);

        txtCuLy = new JTextField();
        txtCuLy.setPreferredSize(inputSize);

        cboTrangThai = new JComboBox<>(new String[]{"Đang hoạt động", "Đã huỷ"});
        cboTrangThai.setPreferredSize(inputSize);

        loadDuLieuCombo();

        // Layout
        JPanel pnFields = new JPanel(new GridBagLayout());
        pnFields.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Hàng 1: Mã | Tên chuyến | Tàu
        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0;
        pnFields.add(boldLabel("Mã chuyến:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        pnFields.add(txtMa, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        pnFields.add(boldLabel("Tên chuyến:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        pnFields.add(txtTenChuyen, gbc);

        gbc.gridx = 4; gbc.weightx = 0;
        pnFields.add(boldLabel("Tàu:"), gbc);
        gbc.gridx = 5; gbc.weightx = 1;
        pnFields.add(cboTau, gbc);

        // Hàng 2: Ga đi | Ga đến | Cự ly
        gbc.gridy = 1;
        gbc.gridx = 0; gbc.weightx = 0;
        pnFields.add(boldLabel("Ga đi:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        pnFields.add(cboGaDi, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        pnFields.add(boldLabel("Ga đến:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        pnFields.add(cboGaDen, gbc);

        gbc.gridx = 4; gbc.weightx = 0;
        pnFields.add(boldLabel("Cự ly (km):"), gbc);
        gbc.gridx = 5; gbc.weightx = 1;
        pnFields.add(txtCuLy, gbc);

        // Hàng 3: Khởi hành (Ngày + Giờ : Phút)
        gbc.gridy = 2;
        gbc.gridx = 0; gbc.weightx = 0;
        pnFields.add(boldLabel("Khởi hành:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        pnFields.add(taoPanelNgayGio(dcNgayKhoi, spGioKhoi, spPhutKhoi), gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        pnFields.add(boldLabel("Đến dự kiến:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        pnFields.add(taoPanelNgayGio(dcNgayDen, spGioDen, spPhutDen), gbc);

        gbc.gridx = 4; gbc.weightx = 0;
        pnFields.add(boldLabel("Trạng thái:"), gbc);
        gbc.gridx = 5; gbc.weightx = 1;
        pnFields.add(cboTrangThai, gbc);

        // Hint
        gbc.gridy = 3;
        gbc.gridx = 0; gbc.gridwidth = 6;
        gbc.weightx = 1;
        JLabel lblHint = new JLabel(
                "Lưu ý: 1 tàu chỉ có 1 chuyến trong cùng 1 ngày khởi hành. Tàu chạy qua đêm vẫn tính theo ngày khởi hành.");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(new Color(120, 120, 120));
        pnFields.add(lblHint, gbc);
        gbc.gridwidth = 1;

        pnNorth.add(pnFields, BorderLayout.CENTER);

        // Buttons
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        pnButtons.setOpaque(false);

        btnThemMoi = taoNutMau("Thêm mới", new Color(40, 167, 69));
        btnLuu = taoNutMau("Lưu", new Color(0, 123, 255));
        btnCapNhat = taoNutMau("Cập nhật", new Color(255, 193, 7));
        btnHuyChuyen = taoNutMau("Huỷ chuyến", new Color(220, 53, 69));
        btnKhoiPhuc = taoNutMau("Khôi phục", new Color(40, 167, 69));
        btnLamMoi = taoNut("Làm mới");

        btnHuyChuyen.setPreferredSize(new Dimension(130, 32));

        pnButtons.add(btnThemMoi);
        pnButtons.add(btnLuu);
        pnButtons.add(btnCapNhat);
        pnButtons.add(btnHuyChuyen);
        pnButtons.add(btnKhoiPhuc);
        pnButtons.add(btnLamMoi);

        pnNorth.add(pnButtons, BorderLayout.SOUTH);
        add(pnNorth, BorderLayout.NORTH);
    }

    private JPanel taoPanelNgayGio(JDateChooser dc, JSpinner spGio, JSpinner spPhut) {
        JPanel pn = new JPanel(new FlowLayout(FlowLayout.LEFT, 4, 0));
        pn.setOpaque(false);
        pn.add(dc);
        pn.add(new JLabel(" "));
        pn.add(spGio);
        pn.add(new JLabel(":"));
        pn.add(spPhut);
        return pn;
    }

    private JLabel boldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        Dimension labelSize = new Dimension(95, 28);
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
    // TABLE
    // ==========================================================

    private void khoiTaoBangVaTimKiem() {
        JPanel pnCenter = new JPanel(new BorderLayout(0, 6));
        pnCenter.setBackground(new Color(245, 247, 250));
        pnCenter.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel pnSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pnSearch.setOpaque(false);

        JLabel lblTim = new JLabel("Tìm kiếm:");
        lblTim.setFont(new Font("Segoe UI", Font.BOLD, 12));

        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(380, 30));

        btnTim = taoNutMau("Tìm", new Color(0, 123, 255));
        btnTim.setPreferredSize(new Dimension(80, 30));

        JLabel lblHint = new JLabel("(Tìm theo mã chuyến, tên, mã tàu hoặc tên ga)");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(new Color(120, 120, 120));

        pnSearch.add(lblTim);
        pnSearch.add(txtTimKiem);
        pnSearch.add(btnTim);
        pnSearch.add(lblHint);

        pnCenter.add(pnSearch, BorderLayout.NORTH);

        model = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tbl = new JTable(model);
        tbl.setRowHeight(28);
        tbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tbl.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tbl.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i : new int[]{0, 2, 3, 4, 6}) {
            tbl.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        // Render trạng thái màu
        tbl.getColumnModel().getColumn(7).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) {
                    String s = v == null ? "" : v.toString();
                    if (s.equalsIgnoreCase("Đang hoạt động")) {
                        comp.setForeground(new Color(40, 167, 69));
                    } else if (s.equalsIgnoreCase("Đã huỷ")) {
                        comp.setForeground(new Color(220, 53, 69));
                    } else {
                        comp.setForeground(Color.BLACK);
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return comp;
            }
        });

        int[] widths = {90, 180, 80, 130, 130, 200, 90, 130};
        for (int i = 0; i < widths.length; i++) {
            tbl.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scroll = new JScrollPane(tbl);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        pnCenter.add(scroll, BorderLayout.CENTER);
        add(pnCenter, BorderLayout.CENTER);
    }

    // ==========================================================
    // EVENTS
    // ==========================================================

    private void ganSuKien() {
        btnThemMoi.addActionListener(e -> {
            chuyenSangCheDo(CheDo.THEM);
            xoaTrongForm();
            txtMa.setText(dao.sinhMaMoi());
            dcNgayKhoi.setDate(new Date());
            dcNgayDen.setDate(new Date());
            cboTrangThai.setSelectedItem("Đang hoạt động");
        });

        btnLuu.addActionListener(e -> luuMoi());
        btnCapNhat.addActionListener(e -> capNhat());
        btnHuyChuyen.addActionListener(e -> huyChuyen());
        btnKhoiPhuc.addActionListener(e -> khoiPhuc());

        btnLamMoi.addActionListener(e -> {
            xoaTrongForm();
            txtTimKiem.setText("");
            chuyenSangCheDo(CheDo.XEM);
            loadDuLieuCombo();
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
                    String maChuyen = tbl.getValueAt(row, 0).toString();
                    ChuyenTau ct = dao.layTheoMa(maChuyen);
                    if (ct != null) {
                        doChuyenLenForm(ct);
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
                btnHuyChuyen.setEnabled(false);
                btnKhoiPhuc.setEnabled(false);
                setEditable(false);
                break;
            case THEM:
                btnThemMoi.setEnabled(false);
                btnLuu.setEnabled(true);
                btnCapNhat.setEnabled(false);
                btnHuyChuyen.setEnabled(false);
                btnKhoiPhuc.setEnabled(false);
                setEditable(true);
                break;
            case SUA:
                btnThemMoi.setEnabled(true);
                btnLuu.setEnabled(false);
                btnCapNhat.setEnabled(true);
                setEditable(true);

                String tt = (String) cboTrangThai.getSelectedItem();
                if ("Đã huỷ".equals(tt)) {
                    btnHuyChuyen.setEnabled(false);
                    btnKhoiPhuc.setEnabled(true);
                } else {
                    btnHuyChuyen.setEnabled(true);
                    btnKhoiPhuc.setEnabled(false);
                }
                break;
        }
    }

    private void setEditable(boolean ed) {
        txtTenChuyen.setEditable(ed);
        cboTau.setEnabled(ed);
        cboGaDi.setEnabled(ed);
        cboGaDen.setEnabled(ed);
        dcNgayKhoi.setEnabled(ed);
        spGioKhoi.setEnabled(ed);
        spPhutKhoi.setEnabled(ed);
        dcNgayDen.setEnabled(ed);
        spGioDen.setEnabled(ed);
        spPhutDen.setEnabled(ed);
        txtCuLy.setEditable(ed);
        cboTrangThai.setEnabled(ed);
    }

    // ==========================================================
    // BUSINESS
    // ==========================================================

    private void loadDuLieuCombo() {
        cboTau.removeAllItems();
        cboTau.addItem(new TauItem(null));
        for (Tau t : dao.layDanhSachTauHoatDong()) {
            cboTau.addItem(new TauItem(t));
        }

        cboGaDi.removeAllItems();
        cboGaDen.removeAllItems();
        cboGaDi.addItem(new GaItem(null));
        cboGaDen.addItem(new GaItem(null));
        for (Ga g : dao.layDanhSachGa()) {
            cboGaDi.addItem(new GaItem(g));
            cboGaDen.addItem(new GaItem(g));
        }
    }

    private void loadDanhSach() {
        doDanhSachLenBang(dao.layTatCa());
    }

    private void timKiem() {
        doDanhSachLenBang(dao.tim(txtTimKiem.getText()));
    }

    private void doDanhSachLenBang(List<ChuyenTau> ds) {
        model.setRowCount(0);
        for (ChuyenTau ct : ds) {
            model.addRow(new Object[]{
                ct.getMaChuyen(),
                ct.getTenChuyen(),
                ct.getTau() == null ? "" : ct.getTau().getMaTau(),
                ct.getNgayKhoiHanh() == null ? "" : ct.getNgayKhoiHanh().format(fmtDateTime),
                ct.getNgayDenDuKien() == null ? "" : ct.getNgayDenDuKien().format(fmtDateTime),
                (ct.getGaDi() == null ? "" : ct.getGaDi().getTenGa())
                    + " → "
                    + (ct.getGaDen() == null ? "" : ct.getGaDen().getTenGa()),
                ct.getCuLy(),
                ct.isTrangThaiChuyen() ? "Đang hoạt động" : "Đã huỷ"
            });
        }
    }

    private void doChuyenLenForm(ChuyenTau ct) {
        txtMa.setText(ct.getMaChuyen());
        txtTenChuyen.setText(ct.getTenChuyen());

        // Tau
        if (ct.getTau() != null) {
            for (int i = 0; i < cboTau.getItemCount(); i++) {
                TauItem item = cboTau.getItemAt(i);
                if (item.tau != null && item.tau.getMaTau().equals(ct.getTau().getMaTau())) {
                    cboTau.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Ga di
        if (ct.getGaDi() != null) {
            for (int i = 0; i < cboGaDi.getItemCount(); i++) {
                GaItem item = cboGaDi.getItemAt(i);
                if (item.ga != null && item.ga.getMaGa().equals(ct.getGaDi().getMaGa())) {
                    cboGaDi.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Ga den
        if (ct.getGaDen() != null) {
            for (int i = 0; i < cboGaDen.getItemCount(); i++) {
                GaItem item = cboGaDen.getItemAt(i);
                if (item.ga != null && item.ga.getMaGa().equals(ct.getGaDen().getMaGa())) {
                    cboGaDen.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Ngày + giờ khởi hành
        if (ct.getNgayKhoiHanh() != null) {
            LocalDateTime kh = ct.getNgayKhoiHanh();
            dcNgayKhoi.setDate(java.sql.Date.valueOf(kh.toLocalDate()));
            spGioKhoi.setValue(kh.getHour());
            spPhutKhoi.setValue(kh.getMinute());
        }
        if (ct.getNgayDenDuKien() != null) {
            LocalDateTime den = ct.getNgayDenDuKien();
            dcNgayDen.setDate(java.sql.Date.valueOf(den.toLocalDate()));
            spGioDen.setValue(den.getHour());
            spPhutDen.setValue(den.getMinute());
        }

        txtCuLy.setText(String.valueOf(ct.getCuLy()));
        cboTrangThai.setSelectedItem(ct.isTrangThaiChuyen() ? "Đang hoạt động" : "Đã huỷ");
    }

    private void xoaTrongForm() {
        txtMa.setText("");
        txtTenChuyen.setText("");
        if (cboTau.getItemCount() > 0) cboTau.setSelectedIndex(0);
        if (cboGaDi.getItemCount() > 0) cboGaDi.setSelectedIndex(0);
        if (cboGaDen.getItemCount() > 0) cboGaDen.setSelectedIndex(0);
        dcNgayKhoi.setDate(null);
        dcNgayDen.setDate(null);
        spGioKhoi.setValue(7);
        spPhutKhoi.setValue(0);
        spGioDen.setValue(15);
        spPhutDen.setValue(0);
        txtCuLy.setText("");
        cboTrangThai.setSelectedIndex(0);
        tbl.clearSelection();
    }

    private LocalDateTime layNgayGioKhoi() {
        if (dcNgayKhoi.getDate() == null) return null;
        LocalDate ngay = dcNgayKhoi.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        return ngay.atTime(LocalTime.of((int) spGioKhoi.getValue(), (int) spPhutKhoi.getValue()));
    }

    private LocalDateTime layNgayGioDen() {
        if (dcNgayDen.getDate() == null) return null;
        LocalDate ngay = dcNgayDen.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        return ngay.atTime(LocalTime.of((int) spGioDen.getValue(), (int) spPhutDen.getValue()));
    }

    private ChuyenTau layChuyenTuForm() {
        ChuyenTau ct = new ChuyenTau();
        ct.setMaChuyen(txtMa.getText().trim());
        ct.setTenChuyen(txtTenChuyen.getText().trim());

        TauItem tauItem = (TauItem) cboTau.getSelectedItem();
        if (tauItem != null && tauItem.tau != null) ct.setTau(tauItem.tau);

        GaItem gaDiItem = (GaItem) cboGaDi.getSelectedItem();
        if (gaDiItem != null) ct.setGaDi(gaDiItem.ga);

        GaItem gaDenItem = (GaItem) cboGaDen.getSelectedItem();
        if (gaDenItem != null) ct.setGaDen(gaDenItem.ga);

        ct.setNgayKhoiHanh(layNgayGioKhoi());
        ct.setNgayDenDuKien(layNgayGioDen());

        try {
            ct.setCuLy(Integer.parseInt(txtCuLy.getText().trim()));
        } catch (NumberFormatException e) {
            ct.setCuLy(0);
        }

        ct.setTrangThaiChuyen("Đang hoạt động".equals(cboTrangThai.getSelectedItem()));
        return ct;
    }

    /**
     * Validate form. Trả về null nếu OK, hoặc message lỗi.
     */
    private String validateForm(boolean laThemMoi) {
        String ten = txtTenChuyen.getText().trim();
        if (ten.isEmpty()) return "Vui lòng nhập tên chuyến.";

        TauItem tauItem = (TauItem) cboTau.getSelectedItem();
        if (tauItem == null || tauItem.tau == null) return "Vui lòng chọn tàu.";

        GaItem gaDiItem = (GaItem) cboGaDi.getSelectedItem();
        if (gaDiItem == null || gaDiItem.ga == null) return "Vui lòng chọn ga đi.";

        GaItem gaDenItem = (GaItem) cboGaDen.getSelectedItem();
        if (gaDenItem == null || gaDenItem.ga == null) return "Vui lòng chọn ga đến.";

        if (gaDiItem.ga.getMaGa().equals(gaDenItem.ga.getMaGa())) {
            return "Ga đi và ga đến không được giống nhau.";
        }

        LocalDateTime kh = layNgayGioKhoi();
        if (kh == null) return "Vui lòng chọn ngày khởi hành.";

        LocalDateTime den = layNgayGioDen();
        if (den == null) return "Vui lòng chọn ngày đến dự kiến.";

        if (!den.isAfter(kh)) {
            return "Thời gian đến dự kiến phải sau thời gian khởi hành.";
        }

        // Cự ly
        try {
            int cl = Integer.parseInt(txtCuLy.getText().trim());
            if (cl < 1 || cl > 10000) {
                return "Cự ly phải từ 1 đến 10000 km.";
            }
        } catch (NumberFormatException e) {
            return "Cự ly phải là số nguyên.";
        }

        // Check trùng theo NGÀY KHỞI HÀNH
        String maChuyenLoaiTru = laThemMoi ? null : txtMa.getText().trim();
        LocalDate ngayKhoi = kh.toLocalDate();

        if (dao.coChuyenTrongNgay(tauItem.tau.getMaTau(), ngayKhoi, maChuyenLoaiTru)) {
            String maTrung = dao.layMaChuyenTrungTrongNgay(
                    tauItem.tau.getMaTau(), ngayKhoi, maChuyenLoaiTru);
            return "Tàu " + tauItem.tau.getMaTau() + " đã có chuyến trong ngày "
                    + ngayKhoi.format(fmtDate)
                    + (maTrung != null ? "\n(Mã chuyến trùng: " + maTrung + ")" : "")
                    + "\n\nLưu ý: 1 tàu chỉ được có 1 chuyến trong cùng 1 ngày khởi hành.";
        }

        return null;
    }

    private void luuMoi() {
        String loi = validateForm(true);
        if (loi != null) {
            JOptionPane.showMessageDialog(this, loi, "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Xác nhận thêm chuyến tàu mới?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.them(layChuyenTuForm())) {
            JOptionPane.showMessageDialog(this, "Thêm chuyến tàu thành công.");
            xoaTrongForm();
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void capNhat() {
        String loi = validateForm(false);
        if (loi != null) {
            JOptionPane.showMessageDialog(this, loi, "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Xác nhận cập nhật chuyến tàu?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.capNhat(layChuyenTuForm())) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công.");
            loadDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void huyChuyen() {
        String maChuyen = txtMa.getText().trim();
        if (maChuyen.isEmpty()) return;

        int soVe = dao.demSoVeLienKet(maChuyen);
        String thongBao = "Xác nhận huỷ chuyến " + maChuyen + "?";
        if (soVe > 0) {
            thongBao += "\n\nLưu ý: Chuyến này có " + soVe + " vé đã bán. "
                    + "Cần liên hệ khách hàng để hoàn tiền.";
        }

        if (JOptionPane.showConfirmDialog(this, thongBao,
                "Xác nhận huỷ", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.huyChuyen(maChuyen)) {
            JOptionPane.showMessageDialog(this, "Đã huỷ chuyến.");
            xoaTrongForm();
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "Huỷ thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void khoiPhuc() {
        String maChuyen = txtMa.getText().trim();
        if (maChuyen.isEmpty()) return;

        if (JOptionPane.showConfirmDialog(this,
                "Xác nhận khôi phục chuyến " + maChuyen + "?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.khoiPhuc(maChuyen)) {
            JOptionPane.showMessageDialog(this, "Khôi phục thành công.");
            xoaTrongForm();
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "Khôi phục thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}