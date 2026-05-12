package com.mycompany.prj1.GUI;

import com.mycompany.prj1.DAO.DAO_HANHKHACH_QL;
import com.mycompany.prj1.entity.HanhKhach;
import com.mycompany.prj1.entity.LoaiHanhKhach;
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

public class GUI_QuanLyKhachHang extends JPanel {

    private final DAO_HANHKHACH_QL dao = new DAO_HANHKHACH_QL();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Form
    private JTextField txtMa;
    private JTextField txtHoTen;
    private JComboBox<String> cboLoaiGiayTo;
    private JTextField txtSoGiayTo;
    private JTextField txtSdt;
    private JDateChooser dcNgaySinh;
    private JComboBox<LoaiHKItem> cboLoaiHK;

    // Buttons
    private JButton btnThemMoi;
    private JButton btnLuu;
    private JButton btnCapNhat;
    private JButton btnXoa;
    private JButton btnLamMoi;

    // Search
    private JTextField txtTimKiem;
    private JButton btnTim;
    private JTable tbl;
    private DefaultTableModel model;

    private enum CheDo { XEM, THEM, SUA }
    private CheDo cheDo = CheDo.XEM;

    private static final String[] COLUMNS = {
        "Mã KH", "Họ tên", "Loại GT", "Số GT", "SĐT", "Ngày sinh", "Loại khách"
    };

    private static final String[] DS_LOAI_GT = {
        "CCCD", "PASSPORT", "KHONG_CO"
    };

    /** Wrapper hiển thị tên loại HK trong combo */
    private static class LoaiHKItem {
        LoaiHanhKhach lhk;
        LoaiHKItem(LoaiHanhKhach lhk) { this.lhk = lhk; }
        @Override public String toString() {
            if (lhk == null) return "-- Chọn loại --";
            return lhk.getTenLoai();
        }
    }

    public GUI_QuanLyKhachHang() {
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
                        "THÔNG TIN HÀNH KHÁCH",
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

        txtHoTen = new JTextField();
        txtHoTen.setPreferredSize(inputSize);

        cboLoaiGiayTo = new JComboBox<>(DS_LOAI_GT);
        cboLoaiGiayTo.setPreferredSize(inputSize);

        txtSoGiayTo = new JTextField();
        txtSoGiayTo.setPreferredSize(inputSize);

        txtSdt = new JTextField();
        txtSdt.setPreferredSize(inputSize);

        dcNgaySinh = new JDateChooser();
        dcNgaySinh.setDateFormatString("dd/MM/yyyy");
        dcNgaySinh.setPreferredSize(inputSize);

        cboLoaiHK = new JComboBox<>();
        cboLoaiHK.setPreferredSize(inputSize);
        loadLoaiHK();

        // Layout
        JPanel pnFields = new JPanel(new GridBagLayout());
        pnFields.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addRow(pnFields, gbc, 0, "Mã KH:", txtMa, "Họ tên:", txtHoTen, "Loại giấy tờ:", cboLoaiGiayTo);
        addRow(pnFields, gbc, 1, "Số giấy tờ:", txtSoGiayTo, "SĐT:", txtSdt, "Ngày sinh:", dcNgaySinh);

        gbc.gridy = 2;
        gbc.gridx = 0; gbc.weightx = 0;
        pnFields.add(boldLabel("Loại khách:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        pnFields.add(cboLoaiHK, gbc);

        gbc.gridx = 2; gbc.gridwidth = 4;
        gbc.weightx = 1;
        JLabel lblHint = new JLabel(
                "Loại giấy tờ \"KHONG_CO\" → bỏ trống số giấy tờ. Loại khách quyết định mức giá vé.");
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

    private void addRow(JPanel pn, GridBagConstraints gbc, int row,
                        String l1, Component c1, String l2, Component c2, String l3, Component c3) {
        gbc.gridy = row;
        gbc.gridx = 0; gbc.weightx = 0;
        pn.add(boldLabel(l1), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        pn.add(c1, gbc);
        gbc.gridx = 2; gbc.weightx = 0;
        pn.add(boldLabel(l2), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        pn.add(c2, gbc);
        gbc.gridx = 4; gbc.weightx = 0;
        pn.add(boldLabel(l3), gbc);
        gbc.gridx = 5; gbc.weightx = 1;
        pn.add(c3, gbc);
    }

    private JLabel boldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        Dimension labelSize = new Dimension(110, 28);
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
    // SEARCH + TABLE
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

        JLabel lblHint = new JLabel("(Tìm theo mã KH, họ tên, số giấy tờ hoặc SĐT)");
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

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i : new int[]{0, 2, 4, 5}) {
            tbl.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        int[] widths = {80, 200, 90, 130, 110, 100, 130};
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
            cboLoaiGiayTo.setSelectedItem("CCCD");
            if (cboLoaiHK.getItemCount() > 1) {
                cboLoaiHK.setSelectedIndex(1);
            }
            txtHoTen.requestFocus();
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

        // Khi đổi loại giấy tờ → enable/disable ô số giấy tờ
        cboLoaiGiayTo.addActionListener(e -> {
            String loai = (String) cboLoaiGiayTo.getSelectedItem();
            boolean canSo = !"KHONG_CO".equals(loai);
            txtSoGiayTo.setEditable(canSo && cheDo != CheDo.XEM);
            if (!canSo) txtSoGiayTo.setText("");
        });

        tbl.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tbl.getSelectedRow();
                if (row >= 0) {
                    String maHK = tbl.getValueAt(row, 0).toString();
                    HanhKhach hk = dao.layTheoMa(maHK);
                    if (hk != null) {
                        doHKLenForm(hk);
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
        txtHoTen.setEditable(ed);
        cboLoaiGiayTo.setEnabled(ed);
        boolean canSoGT = !"KHONG_CO".equals(cboLoaiGiayTo.getSelectedItem());
        txtSoGiayTo.setEditable(ed && canSoGT);
        txtSdt.setEditable(ed);
        dcNgaySinh.setEnabled(ed);
        cboLoaiHK.setEnabled(ed);
    }

    // ==========================================================
    // NGHIỆP VỤ
    // ==========================================================

    private void loadLoaiHK() {
        cboLoaiHK.removeAllItems();
        cboLoaiHK.addItem(new LoaiHKItem(null));
        for (LoaiHanhKhach lhk : dao.layDanhSachLoaiHanhKhach()) {
            cboLoaiHK.addItem(new LoaiHKItem(lhk));
        }
    }

    private void loadDanhSach() {
        doDanhSachLenBang(dao.layTatCa());
    }

    private void timKiem() {
        doDanhSachLenBang(dao.tim(txtTimKiem.getText()));
    }

    private void doDanhSachLenBang(List<HanhKhach> ds) {
        model.setRowCount(0);
        for (HanhKhach hk : ds) {
            model.addRow(new Object[]{
                hk.getMaHanhKhach(),
                hk.getHoTenHK(),
                hk.getLoaiGiayTo(),
                hk.getSoGiayTo() == null ? "" : hk.getSoGiayTo(),
                hk.getSdt() == null ? "" : hk.getSdt(),
                hk.getNgaySinh() == null ? "" : hk.getNgaySinh().format(fmt),
                hk.getLoaiHanhKhach() == null ? "" : hk.getLoaiHanhKhach().getTenLoai()
            });
        }
    }

    private void doHKLenForm(HanhKhach hk) {
        txtMa.setText(hk.getMaHanhKhach());
        txtHoTen.setText(hk.getHoTenHK());
        cboLoaiGiayTo.setSelectedItem(hk.getLoaiGiayTo());
        txtSoGiayTo.setText(hk.getSoGiayTo() == null ? "" : hk.getSoGiayTo());
        txtSdt.setText(hk.getSdt() == null ? "" : hk.getSdt());

        if (hk.getNgaySinh() != null) {
            dcNgaySinh.setDate(java.sql.Date.valueOf(hk.getNgaySinh()));
        } else {
            dcNgaySinh.setDate(null);
        }

        // Set loại HK trong combo
        if (hk.getLoaiHanhKhach() != null) {
            for (int i = 0; i < cboLoaiHK.getItemCount(); i++) {
                LoaiHKItem item = cboLoaiHK.getItemAt(i);
                if (item.lhk != null && item.lhk.getMaLoai().equals(hk.getLoaiHanhKhach().getMaLoai())) {
                    cboLoaiHK.setSelectedIndex(i);
                    break;
                }
            }
        }
    }

    private void xoaTrongForm() {
        txtMa.setText("");
        txtHoTen.setText("");
        cboLoaiGiayTo.setSelectedIndex(0);
        txtSoGiayTo.setText("");
        txtSdt.setText("");
        dcNgaySinh.setDate(null);
        if (cboLoaiHK.getItemCount() > 0) cboLoaiHK.setSelectedIndex(0);
        tbl.clearSelection();
    }

    private HanhKhach layHKTuForm() {
        HanhKhach hk = new HanhKhach();
        hk.setMaHanhKhach(txtMa.getText().trim());
        hk.setHoTenHK(txtHoTen.getText().trim());
        hk.setLoaiGiayTo(cboLoaiGiayTo.getSelectedItem().toString());
        hk.setSoGiayTo(txtSoGiayTo.getText().trim());
        hk.setSdt(txtSdt.getText().trim());

        if (dcNgaySinh.getDate() != null) {
            hk.setNgaySinh(dcNgaySinh.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
        }

        LoaiHKItem item = (LoaiHKItem) cboLoaiHK.getSelectedItem();
        if (item != null) hk.setLoaiHanhKhach(item.lhk);

        return hk;
    }

    private String validateForm(boolean laThemMoi) {
        String hoTen = txtHoTen.getText().trim();
        if (hoTen.isEmpty()) return "Vui lòng nhập họ tên.";
        if (hoTen.length() > 100) return "Họ tên không được quá 100 ký tự.";

        String loaiGT = (String) cboLoaiGiayTo.getSelectedItem();
        String soGT = txtSoGiayTo.getText().trim();
        if (!"KHONG_CO".equals(loaiGT)) {
            if (soGT.isEmpty()) return "Vui lòng nhập số giấy tờ.";
            if ("CCCD".equals(loaiGT) && !soGT.matches("\\d{9,12}")) {
                return "CCCD phải có 9-12 chữ số.";
            }
            if ("PASSPORT".equals(loaiGT) && (soGT.length() < 6 || soGT.length() > 12)) {
                return "Số hộ chiếu phải có 6-12 ký tự.";
            }
            // Check trùng
            String maHienTai = laThemMoi ? null : txtMa.getText().trim();
            if (dao.soGiayToDaTonTai(soGT, maHienTai)) {
                return "Số giấy tờ này đã tồn tại trong hệ thống.";
            }
        }

        String sdt = txtSdt.getText().trim();
        if (!sdt.isEmpty() && !sdt.matches("0\\d{9}")) {
            return "SĐT phải gồm 10 chữ số bắt đầu bằng 0.";
        }

        if (dcNgaySinh.getDate() == null) return "Vui lòng chọn ngày sinh.";

        LocalDate ns = dcNgaySinh.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        if (ns.isAfter(LocalDate.now())) return "Ngày sinh không hợp lệ.";

        LoaiHKItem item = (LoaiHKItem) cboLoaiHK.getSelectedItem();
        if (item == null || item.lhk == null) return "Vui lòng chọn loại khách.";

        return null;
    }

    private void luuMoi() {
        String loi = validateForm(true);
        if (loi != null) {
            JOptionPane.showMessageDialog(this, loi, "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Xác nhận thêm hành khách mới?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.them(layHKTuForm())) {
            JOptionPane.showMessageDialog(this, "Thêm hành khách thành công.");
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

        if (JOptionPane.showConfirmDialog(this, "Xác nhận cập nhật?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.capNhat(layHKTuForm())) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công.");
            loadDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoa() {
        String maHK = txtMa.getText().trim();
        if (maHK.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hành khách cần xoá.");
            return;
        }

        int soVe = dao.demSoVeDangLienKet(maHK);
        if (soVe > 0) {
            JOptionPane.showMessageDialog(this,
                    "Không thể xoá! Hành khách này có " + soVe + " vé liên kết.\n"
                    + "Cần giữ lại để đối soát hoá đơn.",
                    "Không thể xoá", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this,
                "Xác nhận xoá hành khách " + maHK + "?",
                "Xác nhận xoá", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.xoa(maHK)) {
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