package com.mycompany.prj1.GUI;

import com.mycompany.prj1.DAO.DAO_TAU;
import com.mycompany.prj1.DAO.DAO_TOATAU;
import com.mycompany.prj1.entity.LoaiGhe;
import com.mycompany.prj1.entity.ToaTau;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Dialog quản lý toa của 1 tàu cụ thể.
 *
 *   - TOP: bảng danh sách toa hiện có (kèm nút xoá từng toa)
 *   - BOTTOM: form thêm toa mới (Tên toa / Sức chứa / Loại ghế)
 */
public class DialogQuanLyToa extends JDialog {

    private final DAO_TOATAU daoToa = new DAO_TOATAU();
    private final DAO_TAU daoTau = new DAO_TAU();
    private final String maTau;

    private DefaultTableModel modelToa;
    private JTable tblToa;
    private JButton btnXoaToaDuocChon;

    // Form thêm toa
    private JTextField txtTenToa;
    private JTextField txtSucChua;
    private JComboBox<LoaiGheItem> cboLoaiGhe;
    private JButton btnThemToa;

    /** Wrapper hiển thị tên loại ghế trong combobox */
    private static class LoaiGheItem {
        LoaiGhe lg;
        LoaiGheItem(LoaiGhe lg) { this.lg = lg; }
        @Override public String toString() {
            if (lg == null) return "-- Chọn loại ghế --";
            return lg.getTenLoaiGhe();
        }
    }

    public DialogQuanLyToa(Frame owner, String maTau) {
        super(owner, "Quản lý toa - Tàu " + maTau, true);
        this.maTau = maTau;
        init();
    }

    public DialogQuanLyToa(Dialog owner, String maTau) {
        super(owner, "Quản lý toa - Tàu " + maTau, true);
        this.maTau = maTau;
        init();
    }

    private void init() {
        setSize(800, 600);
        setLocationRelativeTo(getOwner());
        setLayout(new BorderLayout(0, 8));
        getContentPane().setBackground(new Color(245, 247, 250));

        khoiTaoBangToa();
        khoiTaoFormThemToa();
        khoiTaoFooter();

        loadDanhSachToa();
    }

    // ==========================================================
    // TOP: Bảng danh sách toa
    // ==========================================================

    private void khoiTaoBangToa() {
        JPanel pnTop = new JPanel(new BorderLayout(0, 6));
        pnTop.setOpaque(false);
        pnTop.setBorder(BorderFactory.createEmptyBorder(12, 12, 0, 12));

        JLabel lblTitle = new JLabel("Danh sách các toa của tàu " + maTau);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        pnTop.add(lblTitle, BorderLayout.NORTH);

        String[] cols = {"STT", "Mã toa", "Tên toa", "Sức chứa", "Loại ghế"};
        modelToa = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        tblToa = new JTable(modelToa);
        tblToa.setRowHeight(28);
        tblToa.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblToa.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblToa.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        tblToa.getColumnModel().getColumn(0).setCellRenderer(center);
        tblToa.getColumnModel().getColumn(3).setCellRenderer(center);

        int[] widths = {50, 120, 120, 100, 200};
        for (int i = 0; i < widths.length; i++) {
            tblToa.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scroll = new JScrollPane(tblToa);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        scroll.setPreferredSize(new Dimension(750, 250));
        pnTop.add(scroll, BorderLayout.CENTER);

        // Nút xoá toa được chọn
        JPanel pnNutXoa = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 4));
        pnNutXoa.setOpaque(false);

        btnXoaToaDuocChon = new JButton("🗑 Xoá toa được chọn");
        btnXoaToaDuocChon.setBackground(new Color(220, 53, 69));
        btnXoaToaDuocChon.setForeground(Color.WHITE);
        btnXoaToaDuocChon.setFocusPainted(false);
        btnXoaToaDuocChon.setBorderPainted(false);
        btnXoaToaDuocChon.setOpaque(true);
        btnXoaToaDuocChon.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnXoaToaDuocChon.setPreferredSize(new Dimension(180, 32));
        btnXoaToaDuocChon.addActionListener(e -> xoaToa());

        pnNutXoa.add(btnXoaToaDuocChon);
        pnTop.add(pnNutXoa, BorderLayout.SOUTH);

        add(pnTop, BorderLayout.NORTH);
    }

    // ==========================================================
    // CENTER: Form thêm toa mới
    // ==========================================================

    private void khoiTaoFormThemToa() {
        JPanel pnCenter = new JPanel(new BorderLayout());
        pnCenter.setOpaque(false);
        pnCenter.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        JPanel pnForm = new JPanel(new GridBagLayout());
        pnForm.setBackground(Color.WHITE);
        pnForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 180)),
                        "Thêm toa mới",
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 13)
                ),
                BorderFactory.createEmptyBorder(8, 12, 12, 12)
        ));

        Dimension inputSize = new Dimension(180, 28);

        txtTenToa = new JTextField();
        txtTenToa.setPreferredSize(inputSize);

        txtSucChua = new JTextField();
        txtSucChua.setPreferredSize(inputSize);

        cboLoaiGhe = new JComboBox<>();
        cboLoaiGhe.setPreferredSize(inputSize);
        loadLoaiGhe();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Hàng 1: Tên toa | Sức chứa | Loại ghế
        gbc.gridy = 0;
        gbc.gridx = 0; gbc.weightx = 0;
        pnForm.add(boldLabel("Tên toa:"), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        pnForm.add(txtTenToa, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        pnForm.add(boldLabel("Sức chứa:"), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        pnForm.add(txtSucChua, gbc);

        gbc.gridx = 4; gbc.weightx = 0;
        pnForm.add(boldLabel("Loại ghế:"), gbc);
        gbc.gridx = 5; gbc.weightx = 1;
        pnForm.add(cboLoaiGhe, gbc);

        // Hàng 2: nút thêm
        gbc.gridy = 1;
        gbc.gridx = 0; gbc.gridwidth = 5;
        gbc.weightx = 1;
        pnForm.add(new JLabel(""), gbc);

        btnThemToa = new JButton("+ Thêm toa");
        btnThemToa.setBackground(new Color(40, 167, 69));
        btnThemToa.setForeground(Color.WHITE);
        btnThemToa.setFocusPainted(false);
        btnThemToa.setBorderPainted(false);
        btnThemToa.setOpaque(true);
        btnThemToa.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnThemToa.setPreferredSize(new Dimension(140, 32));
        btnThemToa.addActionListener(e -> themToa());

        gbc.gridx = 5; gbc.gridwidth = 1; gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;
        pnForm.add(btnThemToa, gbc);

        pnCenter.add(pnForm, BorderLayout.NORTH);
        add(pnCenter, BorderLayout.CENTER);
    }

    // ==========================================================
    // BOTTOM: Footer (nút Đóng)
    // ==========================================================

    private void khoiTaoFooter() {
        JPanel pnFooter = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        pnFooter.setOpaque(false);
        pnFooter.setBorder(BorderFactory.createEmptyBorder(0, 12, 12, 12));

        JButton btnDong = new JButton("Đóng");
        btnDong.setPreferredSize(new Dimension(120, 32));
        btnDong.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnDong.addActionListener(e -> dispose());

        pnFooter.add(btnDong);
        add(pnFooter, BorderLayout.SOUTH);
    }

    private JLabel boldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        Dimension labelSize = new Dimension(80, 28);
        lbl.setPreferredSize(labelSize);
        lbl.setMinimumSize(labelSize);
        return lbl;
    }

    // ==========================================================
    // NGHIỆP VỤ
    // ==========================================================

    private void loadLoaiGhe() {
        cboLoaiGhe.removeAllItems();
        cboLoaiGhe.addItem(new LoaiGheItem(null));  // placeholder
        List<LoaiGhe> ds = daoToa.layDanhSachLoaiGhe();
        for (LoaiGhe lg : ds) {
            cboLoaiGhe.addItem(new LoaiGheItem(lg));
        }
    }

    private void loadDanhSachToa() {
        modelToa.setRowCount(0);
        List<ToaTau> ds = daoToa.layDanhSachToaTheoTau(maTau);
        int stt = 1;
        for (ToaTau toa : ds) {
            modelToa.addRow(new Object[]{
                stt++,
                toa.getMaToa(),
                toa.getTenToa(),
                toa.getSucChua(),
                toa.getLoaiGhe() == null ? "" : toa.getLoaiGhe().getTenLoaiGhe()
            });
        }

        // Cập nhật đề xuất tên toa cho lần thêm tiếp theo
        txtTenToa.setText(daoToa.deXuatTenToa(maTau));
    }

    private void themToa() {
        // Validate
        String tenToa = txtTenToa.getText().trim();
        if (tenToa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên toa.",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int sucChua;
        try {
            sucChua = Integer.parseInt(txtSucChua.getText().trim());
            if (sucChua < 1 || sucChua > 200) {
                JOptionPane.showMessageDialog(this,
                        "Sức chứa phải từ 1 đến 200.",
                        "Lỗi", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Sức chứa phải là số nguyên.",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LoaiGheItem item = (LoaiGheItem) cboLoaiGhe.getSelectedItem();
        if (item == null || item.lg == null) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn loại ghế.",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Sinh mã toa tự động
        String maToa = daoToa.sinhMaToa(maTau);

        // Thêm vào DB
        boolean ok = daoToa.themToa(maToa, tenToa, sucChua, item.lg.getMaLoaiGhe(), maTau);
        if (ok) {
            // Đồng bộ số toa khai báo trong bảng Tau
            daoTau.dongBoSoLuongToa(maTau);

            JOptionPane.showMessageDialog(this,
                    "Đã thêm \"" + tenToa + "\" (mã " + maToa + ") cho tàu " + maTau + ".");

            // Reset form
            txtSucChua.setText("");
            cboLoaiGhe.setSelectedIndex(0);
            loadDanhSachToa();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Thêm toa thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaToa() {
        int row = tblToa.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn 1 toa trong bảng để xoá.",
                    "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String maToa = tblToa.getValueAt(row, 1).toString();
        String tenToa = tblToa.getValueAt(row, 2).toString();

        if (JOptionPane.showConfirmDialog(this,
                "Xác nhận xoá toa \"" + tenToa + "\" (mã " + maToa + ")?",
                "Xác nhận xoá",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE) != JOptionPane.YES_OPTION) {
            return;
        }

        if (daoToa.xoaToa(maToa)) {
            // Đồng bộ số toa
            daoTau.dongBoSoLuongToa(maTau);

            JOptionPane.showMessageDialog(this, "Đã xoá toa thành công.");
            loadDanhSachToa();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Xoá thất bại. Có thể toa đang được tham chiếu bởi vé hoặc chuyến tàu.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}