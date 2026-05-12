package com.mycompany.prj1.GUI;

import com.mycompany.prj1.DAO.DAO_TAIKHOAN;
import com.mycompany.prj1.DAO.DAO_TAIKHOAN.TaiKhoanInfo;

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
import javax.swing.JPasswordField;
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
 * Giao diện quản lý tài khoản.
 *
 * Bố cục:
 *   - NORTH: form thông tin tài khoản
 *   - CENTER: ô tìm kiếm + JTable danh sách load từ DB
 */
public class GUI_QuanLyTaiKhoan extends JPanel {

    private final DAO_TAIKHOAN dao = new DAO_TAIKHOAN();

    // Form
    private JTextField txtMaTaiKhoan;
    private JComboBox<NhanVienItem> cboNhanVien;
    private JTextField txtHoTenNV;
    private JPasswordField txtMatKhau;
    private JComboBox<String> cboVaiTro;
    private JComboBox<String> cboTrangThai;

    // Nút
    private JButton btnThemMoi;
    private JButton btnLuu;
    private JButton btnCapNhat;
    private JButton btnXoa;
    private JButton btnLamMoi;

    // Tìm kiếm + bảng
    private JTextField txtTimKiem;
    private JButton btnTim;
    private JTable tblTaiKhoan;
    private DefaultTableModel modelTaiKhoan;

    private enum CheDo { XEM, THEM, SUA }
    private CheDo cheDoHienTai = CheDo.XEM;

    private static final String[] COLUMNS = {
        "Mã TK", "Mã NV", "Họ tên nhân viên", "Mật khẩu",
        "Vai trò", "Trạng thái", "Đang đăng nhập"
    };

    /** Wrapper class để hiển thị "NV001 - Nguyễn Văn A" trong combobox */
    private static class NhanVienItem {
        String maNV;
        String hoTen;

        NhanVienItem(String maNV, String hoTen) {
            this.maNV = maNV;
            this.hoTen = hoTen;
        }

        @Override
        public String toString() {
            if (maNV == null) return "-- Chọn nhân viên --";
            return maNV + " - " + (hoTen == null ? "" : hoTen);
        }
    }

    public GUI_QuanLyTaiKhoan() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        khoiTaoForm();
        khoiTaoBangVaTimKiem();
        ganSuKien();

        chuyenSangCheDo(CheDo.XEM);
        loadDanhSachTaiKhoan();
    }

    // ==========================================================
    // PHẦN 1: FORM (NORTH)
    // ==========================================================

    private void khoiTaoForm() {
        JPanel pnNorth = new JPanel(new BorderLayout());
        pnNorth.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 180)),
                        "THÔNG TIN TÀI KHOẢN",
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14)
                ),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        pnNorth.setBackground(Color.WHITE);

        // Khởi tạo các trường nhập với kích thước cố định
        Dimension inputSize = new Dimension(180, 28);

        txtMaTaiKhoan = new JTextField();
        txtMaTaiKhoan.setEditable(false);
        txtMaTaiKhoan.setBackground(new Color(240, 240, 240));
        txtMaTaiKhoan.setPreferredSize(inputSize);

        cboNhanVien = new JComboBox<>();
        cboNhanVien.setPreferredSize(inputSize);

        txtHoTenNV = new JTextField();
        txtHoTenNV.setEditable(false);
        txtHoTenNV.setBackground(new Color(240, 240, 240));
        txtHoTenNV.setPreferredSize(inputSize);

        txtMatKhau = new JPasswordField();
        txtMatKhau.setPreferredSize(inputSize);

        cboVaiTro = new JComboBox<>(new String[]{"NHAN_VIEN", "QUAN_LY"});
        cboVaiTro.setPreferredSize(inputSize);
        cboTrangThai = new JComboBox<>(new String[]{"Đang hoạt động", "Đã khoá"});
        cboTrangThai.setPreferredSize(inputSize);

        // Layout grid
        JPanel pnFields = new JPanel(new GridBagLayout());
        pnFields.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Hàng 1: Mã TK | Nhân viên | Họ tên (auto)
        addRow(pnFields, gbc, 0, "Mã tài khoản:", txtMaTaiKhoan,
                "Nhân viên:", cboNhanVien, "Họ tên:", txtHoTenNV);

        // Hàng 2: Mật khẩu | Vai trò | Trạng thái
        addRow(pnFields, gbc, 1, "Mật khẩu:", txtMatKhau,
                "Vai trò:", cboVaiTro, "Trạng thái:", cboTrangThai);

        pnNorth.add(pnFields, BorderLayout.CENTER);

        // Hàng nút bấm
        JPanel pnButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 4));
        pnButtons.setOpaque(false);

        btnThemMoi = taoNutMau("Thêm mới", new Color(40, 167, 69));
        btnLuu = taoNutMau("Lưu", new Color(0, 123, 255));
        btnCapNhat = taoNutMau("Cập nhật", new Color(255, 193, 7));
        btnXoa = taoNutMau("Khoá TK", new Color(220, 53, 69));
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
                        String lbl1, Component c1,
                        String lbl2, Component c2,
                        String lbl3, Component c3) {
        gbc.gridy = row;

        gbc.gridx = 0; gbc.weightx = 0;
        pn.add(boldLabel(lbl1), gbc);
        gbc.gridx = 1; gbc.weightx = 1;
        pn.add(c1, gbc);

        gbc.gridx = 2; gbc.weightx = 0;
        pn.add(boldLabel(lbl2), gbc);
        gbc.gridx = 3; gbc.weightx = 1;
        pn.add(c2, gbc);

        gbc.gridx = 4; gbc.weightx = 0;
        pn.add(boldLabel(lbl3), gbc);
        gbc.gridx = 5; gbc.weightx = 1;
        pn.add(c3, gbc);
    }

    private JLabel boldLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        // Cố định kích thước label để layout không bị nhảy
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
    // PHẦN 2: TÌM KIẾM + BẢNG
    // ==========================================================

    private void khoiTaoBangVaTimKiem() {
        JPanel pnCenter = new JPanel(new BorderLayout(0, 6));
        pnCenter.setBackground(new Color(245, 247, 250));
        pnCenter.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        // Hàng tìm kiếm
        JPanel pnSearch = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        pnSearch.setOpaque(false);

        JLabel lblTim = new JLabel("Tìm kiếm:");
        lblTim.setFont(new Font("Segoe UI", Font.BOLD, 12));

        txtTimKiem = new JTextField();
        txtTimKiem.setPreferredSize(new Dimension(380, 30));
        txtTimKiem.setToolTipText("Nhập mã TK / mã NV / họ tên / vai trò");

        btnTim = taoNutMau("Tìm", new Color(0, 123, 255));
        btnTim.setPreferredSize(new Dimension(80, 30));

        JLabel lblHint = new JLabel("(Tìm theo mã TK, mã NV, họ tên hoặc vai trò)");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(new Color(120, 120, 120));

        pnSearch.add(lblTim);
        pnSearch.add(txtTimKiem);
        pnSearch.add(btnTim);
        pnSearch.add(lblHint);

        pnCenter.add(pnSearch, BorderLayout.NORTH);

        // Bảng
        modelTaiKhoan = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblTaiKhoan = new JTable(modelTaiKhoan);
        tblTaiKhoan.setRowHeight(28);
        tblTaiKhoan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblTaiKhoan.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblTaiKhoan.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblTaiKhoan.setShowGrid(true);
        tblTaiKhoan.setGridColor(new Color(220, 220, 220));

        // Renderer cột trạng thái (5)
        DefaultTableCellRenderer trangThaiRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String s = value == null ? "" : value.toString();
                    if (s.equalsIgnoreCase("Đang hoạt động")) {
                        c.setForeground(new Color(40, 167, 69));
                    } else if (s.equalsIgnoreCase("Đã khoá")) {
                        c.setForeground(new Color(220, 53, 69));
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };
        tblTaiKhoan.getColumnModel().getColumn(5).setCellRenderer(trangThaiRenderer);

        // Renderer cột "Đang đăng nhập" (6)
        DefaultTableCellRenderer dnnRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String s = value == null ? "" : value.toString();
                    if (s.equalsIgnoreCase("Có")) {
                        c.setForeground(new Color(0, 123, 255));
                    } else {
                        c.setForeground(new Color(120, 120, 120));
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };
        tblTaiKhoan.getColumnModel().getColumn(6).setCellRenderer(dnnRenderer);

        // Renderer cột "Mật khẩu" - hiện ******
        DefaultTableCellRenderer matKhauRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Object hidden = value == null || value.toString().isEmpty() ? "" : "••••••";
                return super.getTableCellRendererComponent(table, hidden, isSelected, hasFocus, row, column);
            }
        };
        tblTaiKhoan.getColumnModel().getColumn(3).setCellRenderer(matKhauRenderer);

        // Độ rộng cột
        int[] widths = {80, 80, 200, 100, 100, 130, 110};
        for (int i = 0; i < widths.length; i++) {
            tblTaiKhoan.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scroll = new JScrollPane(tblTaiKhoan);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        pnCenter.add(scroll, BorderLayout.CENTER);

        add(pnCenter, BorderLayout.CENTER);
    }

    // ==========================================================
    // PHẦN 3: SỰ KIỆN
    // ==========================================================

    private void ganSuKien() {
        btnThemMoi.addActionListener(e -> chuyenSangChedDoThemMoi());

        btnLuu.addActionListener(e -> luuTaiKhoanMoi());

        btnCapNhat.addActionListener(e -> capNhatTaiKhoan());

        btnXoa.addActionListener(e -> khoaTaiKhoan());

        btnLamMoi.addActionListener(e -> {
            xoaTrongForm();
            txtTimKiem.setText("");
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSachTaiKhoan();
        });

        btnTim.addActionListener(e -> timKiem());

        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { timKiem(); }
            @Override public void removeUpdate(DocumentEvent e) { timKiem(); }
            @Override public void changedUpdate(DocumentEvent e) { timKiem(); }
        });

        // Khi đổi nhân viên trong combo (chỉ khi đang THEM)
        cboNhanVien.addActionListener(e -> {
            if (cheDoHienTai == CheDo.THEM) {
                NhanVienItem item = (NhanVienItem) cboNhanVien.getSelectedItem();
                if (item != null && item.maNV != null) {
                    txtHoTenNV.setText(item.hoTen);
                } else {
                    txtHoTenNV.setText("");
                }
            }
        });

        // Click vào dòng -> đổ lên form, vào chế độ SUA
        tblTaiKhoan.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblTaiKhoan.getSelectedRow();
                if (row >= 0) {
                    String maTK = tblTaiKhoan.getValueAt(row, 0).toString();
                    TaiKhoanInfo tk = dao.layTaiKhoanTheoMa(maTK);
                    if (tk != null) {
                        doTaiKhoanLenForm(tk);
                        chuyenSangCheDo(CheDo.SUA);
                    }
                }
            }
        });
    }

    private void chuyenSangChedDoThemMoi() {
        xoaTrongForm();
        txtMaTaiKhoan.setText(dao.sinhMaTaiKhoanMoi());

        // Load danh sách nhân viên chưa có TK
        cboNhanVien.removeAllItems();
        cboNhanVien.addItem(new NhanVienItem(null, null));  // placeholder
        List<String[]> dsNV = dao.layNhanVienChuaCoTaiKhoan();
        if (dsNV.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Tất cả nhân viên đang làm đều đã có tài khoản.",
                    "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        }
        for (String[] nv : dsNV) {
            cboNhanVien.addItem(new NhanVienItem(nv[0], nv[1]));
        }

        cboTrangThai.setSelectedItem("Đang hoạt động");
        cboVaiTro.setSelectedItem("NHANVIEN");

        chuyenSangCheDo(CheDo.THEM);
    }

    private void chuyenSangCheDo(CheDo cd) {
        cheDoHienTai = cd;

        switch (cd) {
            case XEM:
                btnThemMoi.setEnabled(true);
                btnLuu.setEnabled(false);
                btnCapNhat.setEnabled(false);
                btnXoa.setEnabled(false);
                cboNhanVien.setEnabled(false);
                txtMatKhau.setEditable(false);
                cboVaiTro.setEnabled(false);
                cboTrangThai.setEnabled(false);
                break;

            case THEM:
                btnThemMoi.setEnabled(false);
                btnLuu.setEnabled(true);
                btnCapNhat.setEnabled(false);
                btnXoa.setEnabled(false);
                cboNhanVien.setEnabled(true);  // chọn được khi thêm
                txtMatKhau.setEditable(true);
                cboVaiTro.setEnabled(true);
                cboTrangThai.setEnabled(true);
                break;

            case SUA:
                btnThemMoi.setEnabled(true);
                btnLuu.setEnabled(false);
                btnCapNhat.setEnabled(true);
                btnXoa.setEnabled(true);
                cboNhanVien.setEnabled(false);  // không cho đổi NV
                txtMatKhau.setEditable(true);
                cboVaiTro.setEnabled(true);
                cboTrangThai.setEnabled(true);
                break;
        }
    }

    // ==========================================================
    // PHẦN 4: NGHIỆP VỤ
    // ==========================================================

    private void loadDanhSachTaiKhoan() {
        List<TaiKhoanInfo> ds = dao.layTatCaTaiKhoan();
        doDanhSachLenBang(ds);
    }

    private void timKiem() {
        String tuKhoa = txtTimKiem.getText();
        List<TaiKhoanInfo> ds = dao.timTaiKhoan(tuKhoa);
        doDanhSachLenBang(ds);
    }

    private void doDanhSachLenBang(List<TaiKhoanInfo> ds) {
        modelTaiKhoan.setRowCount(0);
        for (TaiKhoanInfo tk : ds) {
            modelTaiKhoan.addRow(new Object[]{
                tk.maTaiKhoan,
                tk.maNhanVien,
                tk.hoTenNhanVien == null ? "" : tk.hoTenNhanVien,
                tk.matKhau,
                tk.vaiTro,
                tk.trangThaiTK ? "Đang hoạt động" : "Đã khoá",
                tk.dangDangNhap ? "Có" : "Không"
            });
        }
    }

    private void doTaiKhoanLenForm(TaiKhoanInfo tk) {
        txtMaTaiKhoan.setText(tk.maTaiKhoan);

        // Khi sửa, combo chỉ cần hiện 1 mục là NV của TK đó
        cboNhanVien.removeAllItems();
        cboNhanVien.addItem(new NhanVienItem(tk.maNhanVien, tk.hoTenNhanVien));

        txtHoTenNV.setText(tk.hoTenNhanVien == null ? "" : tk.hoTenNhanVien);
        txtMatKhau.setText(tk.matKhau);
        cboVaiTro.setSelectedItem(tk.vaiTro);
        cboTrangThai.setSelectedItem(tk.trangThaiTK ? "Đang hoạt động" : "Đã khoá");
    }

    private void xoaTrongForm() {
        txtMaTaiKhoan.setText("");
        cboNhanVien.removeAllItems();
        txtHoTenNV.setText("");
        txtMatKhau.setText("");
        cboVaiTro.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        tblTaiKhoan.clearSelection();
    }

    private String validateForm(boolean laThemMoi) {
        String matKhau = new String(txtMatKhau.getPassword()).trim();
        if (matKhau.isEmpty()) return "Vui lòng nhập mật khẩu.";
        if (matKhau.length() < 4) return "Mật khẩu phải ít nhất 4 ký tự.";
        if (matKhau.length() > 16) return "Mật khẩu không được quá 16 ký tự.";

        if (laThemMoi) {
            NhanVienItem nvItem = (NhanVienItem) cboNhanVien.getSelectedItem();
            if (nvItem == null || nvItem.maNV == null) {
                return "Vui lòng chọn nhân viên gắn với tài khoản này.";
            }
        }

        return null;
    }

    private void luuTaiKhoanMoi() {
        String loi = validateForm(true);
        if (loi != null) {
            JOptionPane.showMessageDialog(this, loi, "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Xác nhận thêm tài khoản mới?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (xacNhan != JOptionPane.YES_OPTION) return;

        TaiKhoanInfo tk = new TaiKhoanInfo();
        tk.maTaiKhoan = txtMaTaiKhoan.getText().trim();
        tk.maNhanVien = ((NhanVienItem) cboNhanVien.getSelectedItem()).maNV;
        tk.matKhau = new String(txtMatKhau.getPassword());
        tk.vaiTro = cboVaiTro.getSelectedItem().toString();
        tk.trangThaiTK = "Đang hoạt động".equals(cboTrangThai.getSelectedItem());

        boolean ok = dao.themTaiKhoan(tk);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Thêm tài khoản thành công.");
            xoaTrongForm();
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSachTaiKhoan();
        } else {
            JOptionPane.showMessageDialog(this, "Thêm tài khoản thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void capNhatTaiKhoan() {
        String loi = validateForm(false);
        if (loi != null) {
            JOptionPane.showMessageDialog(this, loi, "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Xác nhận cập nhật tài khoản?",
                "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (xacNhan != JOptionPane.YES_OPTION) return;

        TaiKhoanInfo tk = new TaiKhoanInfo();
        tk.maTaiKhoan = txtMaTaiKhoan.getText().trim();
        tk.matKhau = new String(txtMatKhau.getPassword());
        tk.vaiTro = cboVaiTro.getSelectedItem().toString();
        tk.trangThaiTK = "Đang hoạt động".equals(cboTrangThai.getSelectedItem());

        boolean ok = dao.capNhatTaiKhoan(tk);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công.");
            loadDanhSachTaiKhoan();
        } else {
            JOptionPane.showMessageDialog(this, "Cập nhật thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void khoaTaiKhoan() {
        String maTK = txtMaTaiKhoan.getText().trim();
        if (maTK.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần khoá.");
            return;
        }

        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Xác nhận khoá tài khoản " + maTK + "?\n"
                + "Người dùng sẽ không đăng nhập được nữa.",
                "Xác nhận", JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (xacNhan != JOptionPane.YES_OPTION) return;

        boolean ok = dao.xoaMemTaiKhoan(maTK);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Đã khoá tài khoản.");
            xoaTrongForm();
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSachTaiKhoan();
        } else {
            JOptionPane.showMessageDialog(this, "Khoá thất bại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}