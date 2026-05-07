package com.mycompany.prj1.GUI;

import com.mycompany.prj1.DAO.DAO_TAU;
import com.mycompany.prj1.DAO.DAO_TAU.TauHienThi;
import com.mycompany.prj1.entity.Tau;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
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
 * Giao diện quản lý tàu.
 *
 *   - NORTH: form (Mã tàu / Trạng thái) + nút "Quản lý toa"
 *   - CENTER: ô tìm + bảng danh sách
 *
 * Mã tàu: user TỰ NHẬP (không tự sinh)
 * Xoá: SOFT DELETE - chuyển trạng thái thành "Dừng hoạt động"
 */
public class GUI_QuanLyTau extends JPanel {

    private final DAO_TAU dao = new DAO_TAU();

    // Form
    private JTextField txtMa;
    private JComboBox<String> cboTrangThai;

    // Buttons
    private JButton btnThemMoi;
    private JButton btnLuu;
    private JButton btnCapNhat;
    private JButton btnDungHoatDong;
    private JButton btnKhoiPhuc;
    private JButton btnLamMoi;
    private JButton btnQuanLyToa;

    // Search + Table
    private JTextField txtTimKiem;
    private JButton btnTim;
    private JTable tbl;
    private DefaultTableModel model;

    private enum CheDo { XEM, THEM, SUA }
    private CheDo cheDo = CheDo.XEM;

    private static final String[] COLUMNS = {
        "Mã tàu", "Số toa khai báo", "Số toa thực tế", "Tổng sức chứa", "Trạng thái"
    };

    private static final String[] DS_TRANG_THAI = {
        "Hoạt động", "Bảo trì", "Dừng hoạt động"
    };

    public GUI_QuanLyTau() {
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
                        "THÔNG TIN TÀU",
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14)
                ),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        pnNorth.setBackground(Color.WHITE);

        Dimension inputSize = new Dimension(180, 28);

        txtMa = new JTextField();
        txtMa.setPreferredSize(inputSize);
        txtMa.setToolTipText("Nhập mã tàu (ví dụ: SE5, TN8, NA1)");

        cboTrangThai = new JComboBox<>(DS_TRANG_THAI);
        cboTrangThai.setPreferredSize(inputSize);

        JPanel pnFields = new JPanel(new GridBagLayout());
        pnFields.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0;
        pnFields.add(boldLabel("Mã tàu:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.5;
        pnFields.add(txtMa, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        pnFields.add(boldLabel("Trạng thái:"), gbc);
        gbc.gridx = 3; gbc.weightx = 0.5;
        pnFields.add(cboTrangThai, gbc);

        // Hint
        gbc.gridy = 1;
        gbc.gridx = 0; gbc.gridwidth = 4;
        gbc.weightx = 1;
        JLabel lblHint = new JLabel(
                "Nhập mã tàu thủ công (ví dụ: SE5, TN8). Sau khi tạo, dùng nút \"Quản lý toa\" để khai báo các toa cho tàu.");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(new Color(120, 120, 120));
        pnFields.add(lblHint, gbc);
        gbc.gridwidth = 1;

        pnNorth.add(pnFields, BorderLayout.CENTER);

        // Buttons row
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        pnButtons.setOpaque(false);

        btnThemMoi = taoNutMau("Thêm mới", new Color(40, 167, 69));
        btnLuu = taoNutMau("Lưu", new Color(0, 123, 255));
        btnCapNhat = taoNutMau("Cập nhật", new Color(255, 193, 7));
        btnDungHoatDong = taoNutMau("Dừng hoạt động", new Color(220, 53, 69));
        btnKhoiPhuc = taoNutMau("Khôi phục", new Color(40, 167, 69));
        btnQuanLyToa = taoNutMau("Quản lý toa", new Color(108, 117, 125));
        btnLamMoi = taoNut("Làm mới");

        // Cho rộng hơn vì label dài
        btnDungHoatDong.setPreferredSize(new Dimension(140, 32));
        btnQuanLyToa.setPreferredSize(new Dimension(140, 32));

        pnButtons.add(btnThemMoi);
        pnButtons.add(btnLuu);
        pnButtons.add(btnCapNhat);
        pnButtons.add(btnDungHoatDong);
        pnButtons.add(btnKhoiPhuc);
        pnButtons.add(btnQuanLyToa);
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
        txtTimKiem.setToolTipText("Nhập mã tàu hoặc trạng thái");

        btnTim = taoNutMau("Tìm", new Color(0, 123, 255));
        btnTim.setPreferredSize(new Dimension(80, 30));

        JLabel lblHint = new JLabel("(Tìm theo mã tàu hoặc trạng thái)");
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
        for (int i = 1; i < 4; i++) {
            tbl.getColumnModel().getColumn(i).setCellRenderer(center);
        }

        // Render cột trạng thái với màu
        tbl.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                if (!sel) {
                    String s = v == null ? "" : v.toString();
                    if (s.equalsIgnoreCase("Hoạt động")) {
                        comp.setForeground(new Color(40, 167, 69));
                    } else if (s.equalsIgnoreCase("Bảo trì")) {
                        comp.setForeground(new Color(255, 153, 0));
                    } else if (s.equalsIgnoreCase("Dừng hoạt động")) {
                        comp.setForeground(new Color(220, 53, 69));
                    } else {
                        comp.setForeground(Color.BLACK);
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return comp;
            }
        });

        // Highlight nếu số toa khai báo khác thực tế
        tbl.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                Component comp = super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                setHorizontalAlignment(SwingConstants.CENTER);
                if (!sel) {
                    try {
                        int khaiBao = Integer.parseInt(t.getValueAt(r, 1).toString());
                        int thuc = Integer.parseInt(v == null ? "0" : v.toString());
                        if (khaiBao != thuc) {
                            comp.setForeground(new Color(220, 53, 69));
                            setFont(getFont().deriveFont(Font.BOLD));
                        } else {
                            comp.setForeground(new Color(40, 167, 69));
                        }
                    } catch (NumberFormatException ex) { }
                }
                return comp;
            }
        });

        int[] widths = {100, 140, 140, 140, 160};
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
            cboTrangThai.setSelectedItem("Hoạt động");
            txtMa.requestFocus();
        });

        btnLuu.addActionListener(e -> luuMoi());
        btnCapNhat.addActionListener(e -> capNhat());
        btnDungHoatDong.addActionListener(e -> dungHoatDong());
        btnKhoiPhuc.addActionListener(e -> khoiPhuc());
        btnQuanLyToa.addActionListener(e -> moDialogQuanLyToa());

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
                    String maTau = tbl.getValueAt(row, 0).toString();
                    Tau t = dao.layTheoMa(maTau);
                    if (t != null) {
                        doTauLenForm(t);
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
                btnDungHoatDong.setEnabled(false);
                btnKhoiPhuc.setEnabled(false);
                btnQuanLyToa.setEnabled(false);
                txtMa.setEditable(false);
                cboTrangThai.setEnabled(false);
                break;
            case THEM:
                btnThemMoi.setEnabled(false);
                btnLuu.setEnabled(true);
                btnCapNhat.setEnabled(false);
                btnDungHoatDong.setEnabled(false);
                btnKhoiPhuc.setEnabled(false);
                btnQuanLyToa.setEnabled(false);  // chưa có data trong DB
                txtMa.setEditable(true);          // CHO USER NHẬP MÃ
                cboTrangThai.setEnabled(true);
                break;
            case SUA:
                btnThemMoi.setEnabled(true);
                btnLuu.setEnabled(false);
                btnCapNhat.setEnabled(true);
                btnQuanLyToa.setEnabled(true);   // có thể quản lý toa
                txtMa.setEditable(false);         // KHÔNG cho sửa mã PK
                cboTrangThai.setEnabled(true);

                // Bật nút Dừng/Khôi phục theo trạng thái hiện tại
                String tt = (String) cboTrangThai.getSelectedItem();
                if ("Dừng hoạt động".equals(tt)) {
                    btnDungHoatDong.setEnabled(false);
                    btnKhoiPhuc.setEnabled(true);
                } else {
                    btnDungHoatDong.setEnabled(true);
                    btnKhoiPhuc.setEnabled(false);
                }
                break;
        }
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

    private void doDanhSachLenBang(List<TauHienThi> ds) {
        model.setRowCount(0);
        for (TauHienThi t : ds) {
            model.addRow(new Object[]{
                t.maTau,
                t.soLuongToaKhaiBao,
                t.soLuongToaThuc,
                t.tongSucChua,
                t.trangThaiTau
            });
        }
    }

    private void doTauLenForm(Tau t) {
        txtMa.setText(t.getMaTau());
        cboTrangThai.setSelectedItem(t.getTrangThaiTau());

        if (cboTrangThai.getSelectedIndex() < 0 && t.getTrangThaiTau() != null) {
            cboTrangThai.addItem(t.getTrangThaiTau());
            cboTrangThai.setSelectedItem(t.getTrangThaiTau());
        }
    }

    private void xoaTrongForm() {
        txtMa.setText("");
        cboTrangThai.setSelectedIndex(0);
        tbl.clearSelection();
    }

    /**
     * Validate khi thêm tàu mới (user nhập mã thủ công).
     */
    private String validateThemMoi() {
        String ma = txtMa.getText().trim().toUpperCase();
        if (ma.isEmpty()) return "Vui lòng nhập mã tàu.";
        if (ma.length() < 2 || ma.length() > 20) {
            return "Mã tàu phải từ 2 đến 20 ký tự.";
        }
        if (!ma.matches("[A-Z0-9_]+")) {
            return "Mã tàu chỉ được chứa chữ in hoa, số và dấu gạch dưới (vd: SE5, TN8, NA1).";
        }
        if (dao.maTauDaTonTai(ma)) {
            return "Mã tàu \"" + ma + "\" đã tồn tại trong hệ thống.";
        }
        return null;
    }

    private void luuMoi() {
        String loi = validateThemMoi();
        if (loi != null) {
            JOptionPane.showMessageDialog(this, loi, "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Xác nhận thêm tàu mới?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        Tau t = new Tau();
        t.setMaTau(txtMa.getText().trim().toUpperCase());
        t.setSoLuongToa(0);  // ban đầu chưa có toa nào
        t.setTrangThaiTau(cboTrangThai.getSelectedItem().toString());

        if (dao.them(t)) {
            JOptionPane.showMessageDialog(this,
                    "Thêm tàu \"" + t.getMaTau() + "\" thành công.\n\n"
                    + "Bước tiếp theo: Click vào tàu trong bảng → bấm \"🔧 Quản lý toa\" "
                    + "để khai báo các toa cho tàu này.");
            xoaTrongForm();
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void capNhat() {
        if (JOptionPane.showConfirmDialog(this, "Xác nhận cập nhật?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        Tau t = new Tau();
        t.setMaTau(txtMa.getText().trim());
        // Lấy số toa thực tế từ ToaTau để đồng bộ
        t.setSoLuongToa(dao.demSoToa(t.getMaTau()));
        t.setTrangThaiTau(cboTrangThai.getSelectedItem().toString());

        if (dao.capNhat(t)) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công.");
            loadDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void dungHoatDong() {
        String maTau = txtMa.getText().trim();
        if (maTau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tàu.");
            return;
        }

        int soChuyen = dao.demSoChuyenDangDung(maTau);
        String thongBao = "Xác nhận chuyển tàu " + maTau + " sang trạng thái 'Dừng hoạt động'?\n";
        if (soChuyen > 0) {
            thongBao += "\nLưu ý: Tàu này đang có " + soChuyen + " chuyến tàu liên kết.\n"
                    + "Việc dừng hoạt động không xoá các chuyến đó, "
                    + "nhưng nên kiểm tra lại các chuyến trong tương lai.";
        }

        if (JOptionPane.showConfirmDialog(this, thongBao,
                "Xác nhận", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.dungHoatDong(maTau)) {
            JOptionPane.showMessageDialog(this, "Đã chuyển tàu sang trạng thái 'Dừng hoạt động'.");
            xoaTrongForm();
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "Thao tác thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void khoiPhuc() {
        String maTau = txtMa.getText().trim();
        if (maTau.isEmpty()) return;

        if (JOptionPane.showConfirmDialog(this,
                "Xác nhận khôi phục tàu " + maTau + " về trạng thái 'Hoạt động'?",
                "Xác nhận", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }

        if (dao.khoiPhuc(maTau)) {
            JOptionPane.showMessageDialog(this, "Khôi phục thành công.");
            xoaTrongForm();
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSach();
        } else {
            JOptionPane.showMessageDialog(this, "Khôi phục thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void moDialogQuanLyToa() {
        String maTau = txtMa.getText().trim();
        if (maTau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tàu.");
            return;
        }

        java.awt.Window owner = javax.swing.SwingUtilities.getWindowAncestor(this);
        DialogQuanLyToa dialog;
        if (owner instanceof java.awt.Frame) {
            dialog = new DialogQuanLyToa((java.awt.Frame) owner, maTau);
        } else if (owner instanceof java.awt.Dialog) {
            dialog = new DialogQuanLyToa((java.awt.Dialog) owner, maTau);
        } else {
            dialog = new DialogQuanLyToa((java.awt.Frame) null, maTau);
        }
        dialog.setVisible(true);

        // Sau khi đóng dialog, refresh bảng để cập nhật số toa, tổng sức chứa
        loadDanhSach();
    }
}