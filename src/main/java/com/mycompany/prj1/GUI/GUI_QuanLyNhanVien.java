package com.mycompany.prj1.GUI;

import com.mycompany.prj1.DAO.DAO_NHANVIEN;
import com.mycompany.prj1.entity.NhanVien;
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
import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/**
 * Giao diện quản lý nhân viên.
 *
 * Bố cục:
 *   - NORTH: form nhập thông tin (chia thành các hàng nhập)
 *   - CENTER: ô tìm kiếm + JTable danh sách nhân viên (load từ DB)
 */
public class GUI_QuanLyNhanVien extends JPanel {

    private final DAO_NHANVIEN dao = new DAO_NHANVIEN();

    // Form nhập
    private JTextField txtMaNhanVien;
    private JTextField txtHoTen;
    private JTextField txtCccd;
    private JTextField txtSdt;
    private JDateChooser dcNgaySinh;
    private JDateChooser dcNgayVaoLam;
    private JComboBox<String> cboGioiTinh;
    private JComboBox<String> cboChucVu;
    private JComboBox<String> cboTrangThai;
    private JTextField txtAnhNhanVien;
    private JButton btnChonAnh;

    // Nút thao tác
    private JButton btnThemMoi;
    private JButton btnLuu;
    private JButton btnCapNhat;
    private JButton btnXoa;
    private JButton btnLamMoi;

    // Tìm kiếm + bảng
    private JTextField txtTimKiem;
    private JButton btnTim;
    private JTable tblNhanVien;
    private DefaultTableModel modelNhanVien;

    // Trạng thái UI: đang ở chế độ thêm hay sửa
    private enum CheDo { XEM, THEM, SUA }
    private CheDo cheDoHienTai = CheDo.XEM;

    private static final String[] COLUMNS = {
        "Mã NV", "Họ tên", "CCCD", "SĐT", "Ngày sinh",
        "Giới tính", "Chức vụ", "Ngày vào làm", "Trạng thái"
    };

    public GUI_QuanLyNhanVien() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 247, 250));

        khoiTaoForm();
        khoiTaoBangVaTimKiem();
        ganSuKien();

        chuyenSangCheDo(CheDo.XEM);
        loadDanhSachNhanVien();
    }

    // ==========================================================
    // PHẦN 1: FORM NHẬP THÔNG TIN (NORTH)
    // ==========================================================

    private void khoiTaoForm() {
        JPanel pnNorth = new JPanel(new BorderLayout());
        pnNorth.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(180, 180, 180)),
                        "THÔNG TIN NHÂN VIÊN",
                        javax.swing.border.TitledBorder.LEFT,
                        javax.swing.border.TitledBorder.TOP,
                        new Font("Segoe UI", Font.BOLD, 14)
                ),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        pnNorth.setBackground(Color.WHITE);

        // Khởi tạo các component nhập
        // Set kích thước cố định để layout không bị nhảy khi text dài/ngắn
        Dimension inputSize = new Dimension(180, 28);

        txtMaNhanVien = new JTextField();
        txtMaNhanVien.setEditable(false);  // mã tự sinh
        txtMaNhanVien.setBackground(new Color(240, 240, 240));
        txtMaNhanVien.setPreferredSize(inputSize);

        txtHoTen = new JTextField();
        txtHoTen.setPreferredSize(inputSize);
        txtCccd = new JTextField();
        txtCccd.setPreferredSize(inputSize);
        txtSdt = new JTextField();
        txtSdt.setPreferredSize(inputSize);

        dcNgaySinh = new JDateChooser();
        dcNgaySinh.setDateFormatString("dd/MM/yyyy");
        dcNgaySinh.setPreferredSize(inputSize);

        dcNgayVaoLam = new JDateChooser();
        dcNgayVaoLam.setDateFormatString("dd/MM/yyyy");
        dcNgayVaoLam.setDate(new Date());
        dcNgayVaoLam.setPreferredSize(inputSize);

        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ"});
        cboGioiTinh.setPreferredSize(inputSize);
        cboChucVu = new JComboBox<>(new String[]{"Nhân viên bán vé", "Nhân viên quản lý"});
        cboChucVu.setPreferredSize(inputSize);
        cboTrangThai = new JComboBox<>(new String[]{"Đang làm", "Đã nghỉ"});
        cboTrangThai.setPreferredSize(inputSize);

        txtAnhNhanVien = new JTextField();
        txtAnhNhanVien.setEditable(false);
        txtAnhNhanVien.setPreferredSize(inputSize);
        btnChonAnh = new JButton("...");
        btnChonAnh.setPreferredSize(new Dimension(40, 28));

        // Đặt các ô vào panel grid bag
        JPanel pnFields = new JPanel(new GridBagLayout());
        pnFields.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 8, 6, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Hàng 1: Mã NV | Họ tên | CCCD
        addRow(pnFields, gbc, 0, "Mã nhân viên:", txtMaNhanVien, "Họ tên:", txtHoTen, "CCCD:", txtCccd);

        // Hàng 2: SĐT | Ngày sinh | Giới tính
        addRow(pnFields, gbc, 1, "SĐT:", txtSdt, "Ngày sinh:", dcNgaySinh, "Giới tính:", cboGioiTinh);

        // Hàng 3: Chức vụ | Ngày vào làm | Trạng thái
        addRow(pnFields, gbc, 2, "Chức vụ:", cboChucVu, "Ngày vào làm:", dcNgayVaoLam, "Trạng thái:", cboTrangThai);

        // Hàng 4: Ảnh (chiếm full chiều ngang)
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.weightx = 0;
        pnFields.add(new JLabel("Ảnh nhân viên:"), gbc);

        gbc.gridx = 1;
        gbc.weightx = 1;
        gbc.gridwidth = 4;
        pnFields.add(txtAnhNhanVien, gbc);

        gbc.gridx = 5;
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        pnFields.add(btnChonAnh, gbc);

        pnNorth.add(pnFields, BorderLayout.CENTER);

        // Hàng nút bấm bên dưới form
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
        // Cố định kích thước label để layout không bị nhảy khi đổi text trong các ô input
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
    // PHẦN 2: TÌM KIẾM + BẢNG (CENTER)
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
        txtTimKiem.setToolTipText("Nhập mã nhân viên / họ tên / CCCD / số điện thoại");

        btnTim = taoNutMau("Tìm", new Color(0, 123, 255));
        btnTim.setPreferredSize(new Dimension(80, 30));

        JLabel lblHint = new JLabel("(Tìm theo mã NV, họ tên, CCCD hoặc SĐT)");
        lblHint.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblHint.setForeground(new Color(120, 120, 120));

        pnSearch.add(lblTim);
        pnSearch.add(txtTimKiem);
        pnSearch.add(btnTim);
        pnSearch.add(lblHint);

        pnCenter.add(pnSearch, BorderLayout.NORTH);

        // Bảng danh sách
        modelNhanVien = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tblNhanVien = new JTable(modelNhanVien);
        tblNhanVien.setRowHeight(28);
        tblNhanVien.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tblNhanVien.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        tblNhanVien.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tblNhanVien.setShowGrid(true);
        tblNhanVien.setGridColor(new Color(220, 220, 220));

        // Renderer cho cột trạng thái
        DefaultTableCellRenderer trangThaiRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String s = value == null ? "" : value.toString();
                    if (s.equalsIgnoreCase("Đang làm")) {
                        c.setForeground(new Color(40, 167, 69));
                    } else if (s.equalsIgnoreCase("Đã nghỉ")) {
                        c.setForeground(new Color(220, 53, 69));
                    } else {
                        c.setForeground(Color.BLACK);
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        };
        tblNhanVien.getColumnModel().getColumn(8).setCellRenderer(trangThaiRenderer);

        // Đặt độ rộng cột
        int[] widths = {80, 180, 130, 120, 110, 80, 110, 110, 100};
        for (int i = 0; i < widths.length; i++) {
            tblNhanVien.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }

        JScrollPane scroll = new JScrollPane(tblNhanVien);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));

        pnCenter.add(scroll, BorderLayout.CENTER);

        add(pnCenter, BorderLayout.CENTER);
    }

    // ==========================================================
    // PHẦN 3: SỰ KIỆN
    // ==========================================================

    private void ganSuKien() {
        // Click nút Thêm mới -> bật form sang chế độ THEM
        btnThemMoi.addActionListener(e -> {
            chuyenSangCheDo(CheDo.THEM);
            xoaTrongForm();
            txtMaNhanVien.setText(dao.sinhMaNhanVienMoi());
            cboTrangThai.setSelectedItem("Đang làm");
        });

        // Click nút Lưu -> insert vào DB
        btnLuu.addActionListener(e -> luuNhanVienMoi());

        // Click nút Cập nhật -> update DB
        btnCapNhat.addActionListener(e -> capNhatNhanVien());

        // Click nút Xoá -> xoá mềm
        btnXoa.addActionListener(e -> xoaNhanVien());

        // Làm mới: reset form và bảng
        btnLamMoi.addActionListener(e -> {
            xoaTrongForm();
            txtTimKiem.setText("");
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSachNhanVien();
        });

        // Chọn ảnh
        btnChonAnh.addActionListener(e -> chonAnh());

        // Tìm kiếm khi click nút
        btnTim.addActionListener(e -> timKiem());

        // Tìm kiếm khi gõ (real-time)
        txtTimKiem.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { timKiem(); }
            @Override public void removeUpdate(DocumentEvent e) { timKiem(); }
            @Override public void changedUpdate(DocumentEvent e) { timKiem(); }
        });

        // Click vào dòng bảng -> đổ dữ liệu lên form, vào chế độ SỬA
        tblNhanVien.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = tblNhanVien.getSelectedRow();
                if (row >= 0) {
                    String maNV = tblNhanVien.getValueAt(row, 0).toString();
                    NhanVien nv = dao.layNhanVienTheoMa(maNV);
                    if (nv != null) {
                        doNhanVienLenForm(nv);
                        chuyenSangCheDo(CheDo.SUA);
                    }
                }
            }
        });
    }

    private void chuyenSangCheDo(CheDo cd) {
        cheDoHienTai = cd;

        switch (cd) {
            case XEM:
                btnThemMoi.setEnabled(true);
                btnLuu.setEnabled(false);
                btnCapNhat.setEnabled(false);
                btnXoa.setEnabled(false);
                setFormEditable(false);
                break;
            case THEM:
                btnThemMoi.setEnabled(false);
                btnLuu.setEnabled(true);
                btnCapNhat.setEnabled(false);
                btnXoa.setEnabled(false);
                setFormEditable(true);
                break;
            case SUA:
                btnThemMoi.setEnabled(true);
                btnLuu.setEnabled(false);
                btnCapNhat.setEnabled(true);
                btnXoa.setEnabled(true);
                setFormEditable(true);
                break;
        }
    }

    private void setFormEditable(boolean editable) {
        txtHoTen.setEditable(editable);
        txtCccd.setEditable(editable);
        txtSdt.setEditable(editable);
        dcNgaySinh.setEnabled(editable);
        dcNgayVaoLam.setEnabled(editable);
        cboGioiTinh.setEnabled(editable);
        cboChucVu.setEnabled(editable);
        cboTrangThai.setEnabled(editable);
        btnChonAnh.setEnabled(editable);
    }

    // ==========================================================
    // PHẦN 4: NGHIỆP VỤ (load, lưu, sửa, xoá, validate)
    // ==========================================================

    private void loadDanhSachNhanVien() {
        List<NhanVien> ds = dao.layTatCaNhanVien();
        doDanhSachLenBang(ds);
    }

    private void timKiem() {
        String tuKhoa = txtTimKiem.getText();
        List<NhanVien> ds = dao.timNhanVien(tuKhoa);
        doDanhSachLenBang(ds);
    }

    private void doDanhSachLenBang(List<NhanVien> ds) {
        modelNhanVien.setRowCount(0);
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (NhanVien nv : ds) {
            modelNhanVien.addRow(new Object[]{
                nv.getMaNhanVien(),
                nv.getHoTenNV(),
                nv.getCccd(),
                nv.getSdt(),
                nv.getNgaySinh() == null ? "" : nv.getNgaySinh().format(fmt),
                nv.getGioiTinh(),
                nv.getChucVu(),
                nv.getNgayVaoLam() == null ? "" : nv.getNgayVaoLam().format(fmt),
                nv.isTrangThaiNV() ? "Đang làm" : "Đã nghỉ"
            });
        }
    }

    private void doNhanVienLenForm(NhanVien nv) {
        txtMaNhanVien.setText(nv.getMaNhanVien());
        txtHoTen.setText(nv.getHoTenNV());
        txtCccd.setText(nv.getCccd());
        txtSdt.setText(nv.getSdt());

        if (nv.getNgaySinh() != null) {
            dcNgaySinh.setDate(java.sql.Date.valueOf(nv.getNgaySinh()));
        } else {
            dcNgaySinh.setDate(null);
        }

        if (nv.getNgayVaoLam() != null) {
            dcNgayVaoLam.setDate(java.sql.Date.valueOf(nv.getNgayVaoLam()));
        } else {
            dcNgayVaoLam.setDate(null);
        }

        cboGioiTinh.setSelectedItem(nv.getGioiTinh());
        cboChucVu.setSelectedItem(nv.getChucVu());
        cboTrangThai.setSelectedItem(nv.isTrangThaiNV() ? "Đang làm" : "Đã nghỉ");
        txtAnhNhanVien.setText(nv.getAnhNhanVien() == null ? "" : nv.getAnhNhanVien());
    }

    private void xoaTrongForm() {
        txtMaNhanVien.setText("");
        txtHoTen.setText("");
        txtCccd.setText("");
        txtSdt.setText("");
        dcNgaySinh.setDate(null);
        dcNgayVaoLam.setDate(new Date());
        cboGioiTinh.setSelectedIndex(0);
        cboChucVu.setSelectedIndex(0);
        cboTrangThai.setSelectedIndex(0);
        txtAnhNhanVien.setText("");
        tblNhanVien.clearSelection();
    }

    private NhanVien layNhanVienTuForm() {
        NhanVien nv = new NhanVien();
        nv.setMaNhanVien(txtMaNhanVien.getText().trim());
        nv.setHoTenNV(txtHoTen.getText().trim());
        nv.setCccd(txtCccd.getText().trim());
        nv.setSdt(txtSdt.getText().trim());

        if (dcNgaySinh.getDate() != null) {
            nv.setNgaySinh(dcNgaySinh.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
        }

        if (dcNgayVaoLam.getDate() != null) {
            nv.setNgayVaoLam(dcNgayVaoLam.getDate().toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate());
        }

        nv.setGioiTinh(cboGioiTinh.getSelectedItem().toString());
        nv.setChucVu(cboChucVu.getSelectedItem().toString());
        nv.setTrangThaiNV("Đang làm".equals(cboTrangThai.getSelectedItem()));
        nv.setAnhNhanVien(txtAnhNhanVien.getText().trim());

        return nv;
    }

    /**
     * Kiểm tra dữ liệu form. Trả về null nếu OK, hoặc message lỗi nếu sai.
     */
    private String validateForm(boolean laThemMoi) {
        String hoTen = txtHoTen.getText().trim();
        if (hoTen.isEmpty()) return "Vui lòng nhập họ tên.";
        if (hoTen.length() > 100) return "Họ tên không được quá 100 ký tự.";

        String cccd = txtCccd.getText().trim();
        if (!cccd.matches("\\d{12}")) return "CCCD phải gồm đúng 12 chữ số.";

        String sdt = txtSdt.getText().trim();
        if (!sdt.matches("0\\d{9}")) return "SĐT phải gồm 10 chữ số, bắt đầu bằng 0.";

        if (dcNgaySinh.getDate() == null) return "Vui lòng chọn ngày sinh.";

        LocalDate ngaySinh = dcNgaySinh.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        int tuoi = LocalDate.now().getYear() - ngaySinh.getYear();
        if (tuoi < 18) return "Nhân viên phải đủ 18 tuổi.";
        if (tuoi > 70) return "Tuổi không hợp lệ (>70).";

        if (dcNgayVaoLam.getDate() == null) return "Vui lòng chọn ngày vào làm.";

        LocalDate ngayVL = dcNgayVaoLam.getDate().toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDate();
        if (ngayVL.isBefore(ngaySinh.plusYears(18))) {
            return "Ngày vào làm phải sau khi nhân viên đủ 18 tuổi.";
        }

        // Check trùng CCCD/SĐT
        String maHienTai = laThemMoi ? null : txtMaNhanVien.getText().trim();
        if (dao.cccdDaTonTai(cccd, maHienTai)) return "CCCD đã tồn tại trong hệ thống.";
        if (dao.sdtDaTonTai(sdt, maHienTai)) return "Số điện thoại đã tồn tại trong hệ thống.";

        return null;
    }

    private void luuNhanVienMoi() {
        String loi = validateForm(true);
        if (loi != null) {
            JOptionPane.showMessageDialog(this, loi, "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Xác nhận thêm nhân viên mới?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (xacNhan != JOptionPane.YES_OPTION) return;

        NhanVien nv = layNhanVienTuForm();
        boolean ok = dao.themNhanVien(nv);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công.");
            xoaTrongForm();
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSachNhanVien();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Thêm nhân viên thất bại. Vui lòng thử lại.",
                    "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void capNhatNhanVien() {
        String loi = validateForm(false);
        if (loi != null) {
            JOptionPane.showMessageDialog(this, loi, "Lỗi", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Xác nhận cập nhật thông tin nhân viên?",
                "Xác nhận",
                JOptionPane.YES_NO_OPTION);
        if (xacNhan != JOptionPane.YES_OPTION) return;

        NhanVien nv = layNhanVienTuForm();
        boolean ok = dao.capNhatNhanVien(nv);

        if (ok) {
            JOptionPane.showMessageDialog(this, "Cập nhật thành công.");
            loadDanhSachNhanVien();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Cập nhật thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaNhanVien() {
        String maNV = txtMaNhanVien.getText().trim();
        if (maNV.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhân viên cần xoá.");
            return;
        }

        int xacNhan = JOptionPane.showConfirmDialog(this,
                "Xác nhận chuyển nhân viên " + maNV + " sang trạng thái 'Đã nghỉ'?\n"
                + "Tài khoản đăng nhập của nhân viên cũng sẽ bị khoá.",
                "Xác nhận xoá",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (xacNhan != JOptionPane.YES_OPTION) return;

        boolean ok = dao.xoaMemNhanVien(maNV);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Đã chuyển nhân viên sang trạng thái 'Đã nghỉ'.");
            xoaTrongForm();
            chuyenSangCheDo(CheDo.XEM);
            loadDanhSachNhanVien();
        } else {
            JOptionPane.showMessageDialog(this, "Xoá thất bại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void chonAnh() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Ảnh (jpg, png, jpeg)", "jpg", "png", "jpeg"));
        int kq = chooser.showOpenDialog(this);
        if (kq == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            txtAnhNhanVien.setText(file.getAbsolutePath());
        }
    }
}