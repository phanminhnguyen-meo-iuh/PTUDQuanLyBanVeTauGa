/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.prj1.GUI;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.Timer;
import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mycompany.prj1.DAO.DAO_CALAM;
import com.mycompany.prj1.DAO.DAO_VETAU;
import com.mycompany.prj1.ConnectDB.DB;
import com.mycompany.prj1.dao.DAO_ChuyenTau;
import com.mycompany.prj1.DAO.DAO_NHANVIEN;
import com.mycompany.prj1.entity.NhanVien;
import com.mycompany.prj1.DAO.DAO_TAIKHOAN;
import com.mycompany.prj1.DAO.DAO_TOATAU;

import com.mycompany.prj1.GUI.GUI_QuanLyNhanVien;
import com.mycompany.prj1.GUI.GUI_QuanLyTaiKhoan;

import java.io.IOException;
import com.mycompany.prj1.DAO.DAO_HANHKHACH;
import com.mycompany.prj1.entity.HanhKhach;
import java.awt.HeadlessException;
import java.awt.print.PrinterException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
import java.io.FileOutputStream;


/**
 *
 * @author WINDOWS
 */
public class jf1 extends javax.swing.JFrame {
    
    
    private static final int GHE_W = 60;
    private static final int GHE_H = 35;
    private static final int SO_HANG_GHE = 4;
    private static final int GAP_GHE = 8;
    private static class FormHanhKhach {
    String loaiHanhKhach;
    javax.swing.JTextField txtHoTen;
    com.toedter.calendar.JDateChooser dcNgaySinh;
    javax.swing.JComboBox<String> cboLoaiGiayTo;
    javax.swing.JTextField txtSoGiayTo;
    javax.swing.JTextField txtSdt;

    javax.swing.JLabel lblLoiHoTen;
    javax.swing.JLabel lblLoiNgaySinh;
    javax.swing.JLabel lblLoiLoaiGiayTo;
    javax.swing.JLabel lblLoiSoGiayTo;
    javax.swing.JLabel lblLoiSdt;

    String maHanhKhachDaChon = null;
    String giayToDaXacNhan = null;
    boolean duLieuTuDB = false;
}
    
    private String maChiTietCaLamDangMo = null;
    
    private static class GheHanhKhach {
    int stt;
    String loaiHanhKhach;
    String maGhe;
    String tenGhe;
    String tenToa;
    long giaVe;
    String maVe;
    }
    
    private String maCaLamDangMo = null;
    private String tenCaLamDangMo = null;
    private String thoiGianCaLamDangMo = null;
    private java.time.LocalDateTime thoiGianMoCa = null;
    
    private DAO_VETAU daoVeTau = new DAO_VETAU();
    private String maPhienGiuCho;

    private javax.swing.Timer timerGiuCho;
    private int giayGiuChoConLai = 15*60;
    
    private java.util.Set<Integer> dsIndexHanhKhachCanNhapLai = new java.util.HashSet<>();
    private javax.swing.JPanel pnlThongTinVeChieuDi;
    private javax.swing.JScrollPane scrollThongTinVeChieuDi;
    private javax.swing.JPanel pnlThongTinVeChieuVe;
    private javax.swing.JScrollPane scrollThongTinVeChieuVe;
    private DAO_HANHKHACH daoHanhKhach = new DAO_HANHKHACH();
    private java.util.List<GheHanhKhach> dsGheChonDi = new java.util.ArrayList<>();
    private int viTriDangChonGheDi = 0;
    private java.util.List<GheHanhKhach> dsGheChonVe = new java.util.ArrayList<>();
    private int viTriDangChonGheVe = 0;

    private String maToaDangChonVe = null;
    private String maGheDangChonVe = null;

    private final java.awt.Color MAU_MAC_DINH = null;
    private final java.awt.Color MAU_DANG_CHON = new java.awt.Color(0, 120, 215);
    private final java.awt.Color MAU_DAY = new java.awt.Color(220, 53, 69);
    
    private java.util.List<FormHanhKhach> dsFormHanhKhach = new java.util.ArrayList<>();
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(jf1.class.getName());
    private com.toedter.calendar.JDateChooser dcNgayDi;
    private com.toedter.calendar.JDateChooser dcNgayVe;
    private final String[] dsGaDen = {
    "Hà Nội", "Đà Nẵng", "Huế", "Nha Trang", "Vinh", "Phan Thiết", "Quy Nhơn"
};
    private DAO_ChuyenTau daoChuyenTau = new DAO_ChuyenTau();
    private javax.swing.JLabel lblAnhNhanVien;
    private boolean dangLocGa = false;
    private String vaiTroDangNhap;
    
    private String maNhanVienDangNhap;
    private boolean daMoCa = false;
    private long tongTienDauCa = 0;
    
    private GUI_QuanLyTaiKhoan gdQuanLyTaiKhoan;
    private String maChuyenDiDaChon = null;
    private String tenChuyenDiDaChon = null;
    private String gaDiChuyenDi = null;
    private String gaDenChuyenDi = null;

    private String maChuyenVeDaChon = null;
    private String tenChuyenVeDaChon = null;
    private String gaDiChuyenVe = null;
    private String gaDenChuyenVe = null;

    // ===== Các label động cho chiều về (đồng bộ với chiều đi) =====
    private javax.swing.JLabel lblSoLuongToaVe;
    private javax.swing.JLabel lblToaTextVe;
    private javax.swing.JLabel lblTenToaVe;
    private javax.swing.JLabel lblLoaiGheToaVe;
    private javax.swing.JPanel pnlContainerToaVe;
    private javax.swing.JPanel pnlContainerGheVe;
    private boolean daKhoiTaoBoCucChieuVe = false;
// ===== Các label động cho chiều đi =====
    private javax.swing.JLabel lblSoLuongToaDi;
    private javax.swing.JLabel lblToaTextDi;
    private javax.swing.JLabel lblTenToaDi;
    private javax.swing.JLabel lblLoaiGheToaDi;
    private javax.swing.JPanel pnlContainerToaDi;
    private javax.swing.JPanel pnlContainerGheDi;
    private boolean daKhoiTaoBoCucChieuDi = false;
    
    private javax.swing.JButton btnGiamNguoiLon;
    private javax.swing.JButton btnTangNguoiLon;
    private javax.swing.JTextField txtNguoiLon;
    private javax.swing.JTextField txtTreEm;
    private javax.swing.JTextField txtNguoiCaoTuoi;
    private javax.swing.JTextField txtSinhVien;
    private javax.swing.JLabel lblTongSoVe;
    private String tenTauChuyenDi = null;
    private String tenTauChuyenVe = null;
    private boolean dangChonChuyenVe = false;
    private javax.swing.JButton btnQuayLaiSauKhiChonLan1;
    private javax.swing.JScrollPane scrollNhapThongTinHanhKhach;
    
    private com.mycompany.prj1.DAO.DAO_TOATAU daoToaTau = new com.mycompany.prj1.DAO.DAO_TOATAU();

    private String maToaDangChonDi = null;
    private String maGheDangChonDi = null;
    private javax.swing.JButton btnGheDangChonDi = null;
    /**
     * Creates new form jf1
     */
    
    public jf1() {
    this("QUAN_LY","");
}
    public jf1(String vaiTro,String maNhanVien) {
        
        initComponents();
        
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                troVeManHinhLogin();
            }
        });
        
        jPanel9.setLayout(new java.awt.BorderLayout());
        jPanel9.removeAll();

        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        jPanel9.add(jLabel45, java.awt.BorderLayout.NORTH);

        javax.swing.JScrollPane scrollTongKetVe = new javax.swing.JScrollPane(jPanel47);
        scrollTongKetVe.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollTongKetVe.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTongKetVe.getVerticalScrollBar().setUnitIncrement(16);

        jPanel9.add(scrollTongKetVe, java.awt.BorderLayout.CENTER);
        this.vaiTroDangNhap = vaiTro == null ? "" : vaiTro.trim().toUpperCase();
        this.maNhanVienDangNhap = maNhanVien == null ? "" : maNhanVien.trim();
         maPhienGiuCho = java.util.UUID.randomUUID().toString();
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        hienThiDongHo();
        capNhatDoanhThuHomNay();
        capNhatSoVeDaBanHomNay();
        
        chinhMenuTrai();
        chinhThongTinNhanVien();
        jButton10.addActionListener(e -> moGDThongTinNhanVien());
        jButton14.addActionListener(e -> moHopThoaiDoiMatKhau());
        
        dcNgayDi = new com.toedter.calendar.JDateChooser();
        dcNgayVe = new com.toedter.calendar.JDateChooser();

        dcNgayDi.setDateFormatString("dd/MM/yyyy");
        dcNgayVe.setDateFormatString("dd/MM/yyyy");
        
        
        Date homNay = boThoiGian(new Date());
        dcNgayDi.setDate(homNay);
        dcNgayDi.setMinSelectableDate(homNay);

        dcNgayVe.setEnabled(false);
        dcNgayVe.setMinSelectableDate(homNay);
        
        
        chinhPanelSoLuongVeHangNgang();
        btnQuayLaiSauKhiChonLan1 = new javax.swing.JButton("Quay lại");
        btnQuayLaiSauKhiChonLan1.setVisible(true);
        btnQuayLaiSauKhiChonLan1.setEnabled(false);

        btnQuayLaiSauKhiChonLan1.addActionListener(e -> quayLaiChonLan1VaAnNut());
        
        chinhPanelChonThongTinChuyen();
        lamRongBangBanDau();
        
        ButtonGroup group = new ButtonGroup();
        group.add(jRadioButton1);
        group.add(jRadioButton2);
        
        
        
        jButton7.addActionListener(e -> troVeManHinhLogin());

jButton3.addActionListener(e -> {
    if (chuaMoCaThiThongBao("Thống kê")) {
        return;
    }

    // mở Thống kê
});

jButton5.addActionListener(e -> {
    if (chuaMoCaThiThongBao("Quản lí khuyến mãi")) {
        return;
    }

    // mở Quản lí khuyến mãi
});

jButton6.addActionListener(e -> {
    xuLyKetCa();

    
});

jButton9.addActionListener(e -> {
    if (chuaMoCaThiThongBao("Quản lí nhân viên")) {
        return;
    }

    // mở Quản lí nhân viên
});

jButton11.addActionListener(e -> {
    if (chuaMoCaThiThongBao("Quản lí chuyến tàu")) {
        return;
    }

    // mở Quản lí chuyến tàu
});

jButton12.addActionListener(e -> {
    if (chuaMoCaThiThongBao("Quản lí tàu")) {
        return;
    }

    // mở Quản lí tàu
});

jButton15.addActionListener(e -> {
    if (chuaMoCaThiThongBao("Quản lí tài khoản")) {
        return;
    }

    // mở Quản lí tài khoản
});
        jComboBox2.setEditable(true);
        chinhPanelAnhNhanVien();
        chinhPanelThongTinCaNhan();
        phanQuyenTheoVaiTro();
        hienThongTinNhanVienDangNhap();

        khoiPhucCaDangMo();

        if (daMoCa) {
            kiemTraPhienGiuChoCu();
        }
        jButton18.setText("Quay lại");
        jButton18.addActionListener(e -> xuLyNutQuayLai());
        jButton21.addActionListener(e -> xacNhanHuyVaVeTrangChu());
        
        jButton23.addActionListener(e -> quayLaiTuNhapThongTinHanhKhach());
        jButton24.addActionListener(e -> xacNhanHuyVaVeTrangChu());
        
        cauHinhScrollThongTinVeChieuDi();
        canChinhPanelGhiChuVaThongTinVe();
        cauHinhScrollThongTinVeChieuVe();

        cauHinhScrollNhapThongTinHanhKhach();

        thuNhoLaiCacPanelChonGhe();

        capNhatPanel47TongKetVe();

        coDinhNutVaCuonPhanChonGhe();
        dongBoStyleNutChieuVeVoiChieuDi();
//        gdQuanLyTaiKhoan = new GUI_QuanLyTaiKhoan();
//        jPanelNoi_Dung.add(gdQuanLyTaiKhoan, "cardTaiKhoan");
//        jButton15.addActionListener(e -> moGDQuanLyTaiKhoan());

javax.swing.JTextField editorGaDen =
    (javax.swing.JTextField) jComboBox2.getEditor().getEditorComponent();

editorGaDen.setText("Chọn ga đến...");
editorGaDen.setForeground(java.awt.Color.GRAY);

editorGaDen.addFocusListener(new java.awt.event.FocusAdapter() {
    @Override
    public void focusGained(java.awt.event.FocusEvent e) {
        if (editorGaDen.getText().equals("Chọn ga đến...")) {
            editorGaDen.setText("");
            editorGaDen.setForeground(java.awt.Color.BLACK);
        }
    }

    @Override
    public void focusLost(java.awt.event.FocusEvent e) {
        String rawText = editorGaDen.getText();

        if (rawText == null || rawText.isBlank()) {
            editorGaDen.setText("Chọn ga đến...");
            editorGaDen.setForeground(java.awt.Color.GRAY);
        }
    }
});

editorGaDen.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
    private void loc() {
        if (dangLocGa) return;

        javax.swing.SwingUtilities.invokeLater(() -> {
            if (dangLocGa) return;

            String rawText = editorGaDen.getText();     // giữ nguyên text người dùng nhập
            if (rawText.equals("Chọn ga đến...")) return;

            String textTim = rawText.trim();            // chỉ dùng để lọc

            dangLocGa = true;
            try {
                javax.swing.DefaultComboBoxModel<String> model = new javax.swing.DefaultComboBoxModel<>();

                for (String ga : dsGaDen) {
                    if (boDau(ga).contains(boDau(textTim))) {
                        model.addElement(ga);
                    }
                }

                jComboBox2.setModel(model);
                jComboBox2.setEditable(true);

                javax.swing.JTextField editorMoi =
                        (javax.swing.JTextField) jComboBox2.getEditor().getEditorComponent();

                editorMoi.setText(rawText); // giữ nguyên dấu cách người dùng vừa gõ
                editorMoi.setForeground(java.awt.Color.BLACK);

                if (model.getSize() > 0 && editorMoi.hasFocus()) {
                    jComboBox2.showPopup();
                } else {
                    jComboBox2.hidePopup();
                }
            } finally {
                dangLocGa = false;
            }
        });
    }

    @Override
    public void insertUpdate(javax.swing.event.DocumentEvent e) { loc(); }

    @Override
    public void removeUpdate(javax.swing.event.DocumentEvent e) { loc(); }

    @Override
    public void changedUpdate(javax.swing.event.DocumentEvent e) { loc(); }
});
        
        jtgadi.setText("Sài Gòn");
        jtgadi.setEditable(false);

        jButton13.addActionListener(e -> xuLyTimChuyen());

        int lastCol = jTable2.getColumnModel().getColumnCount() - 1;
        jTable2.getColumnModel().getColumn(lastCol).setCellRenderer(new ButtonRenderer());
        jTable2.getColumnModel().getColumn(lastCol).setCellEditor(new ButtonEditor(jTable2));
        jTable2.setRowHeight(30);
        
        // cố định bảng
        jTable2.getColumnModel().getColumn(0).setPreferredWidth(90);   // Mã chuyến
        jTable2.getColumnModel().getColumn(1).setPreferredWidth(140);  // Tên chuyến
        jTable2.getColumnModel().getColumn(2).setPreferredWidth(90);   // Tên tàu
        jTable2.getColumnModel().getColumn(3).setPreferredWidth(100);  // Ga đi
        jTable2.getColumnModel().getColumn(4).setPreferredWidth(100);  // Ga đến
        jTable2.getColumnModel().getColumn(5).setPreferredWidth(140);  // Ngày đi
        jTable2.getColumnModel().getColumn(6).setPreferredWidth(160);  // Ngày đến dự kiến
        jTable2.getColumnModel().getColumn(7).setPreferredWidth(100);  // Số lượng ghế
        jTable2.getColumnModel().getColumn(8).setPreferredWidth(80);   // Ghế trống
        jTable2.getColumnModel().getColumn(9).setPreferredWidth(100);  // Chọn chuyến
        jTable2.getTableHeader().setReorderingAllowed(false); // không cho kéo đổi vị trí cột
        jTable2.getTableHeader().setResizingAllowed(false);   // không cho kéo thay đổi độ rộng cột
    
        jRadioButton1.setSelected(true);

jRadioButton1.addActionListener(e -> {
    dcNgayVe.setEnabled(false);
    dcNgayVe.setDate(null);
});

jRadioButton2.addActionListener(e -> {
    dcNgayVe.setEnabled(true);

    Date ngayDi = dcNgayDi.getDate();
    if (ngayDi != null) {
        Date d = boThoiGian(ngayDi);
        dcNgayVe.setMinSelectableDate(d);
        dcNgayVe.setDate(d);
    }
});

dcNgayDi.getDateEditor().addPropertyChangeListener("date", evt -> {
    Date ngayDi = dcNgayDi.getDate();
    if (ngayDi != null) {
        Date d = boThoiGian(ngayDi);

        dcNgayDi.setMinSelectableDate(boThoiGian(new Date()));
        dcNgayVe.setMinSelectableDate(d);

        kiemTraNgayDi();

        if (jRadioButton2.isSelected()) {
            Date ngayVe = dcNgayVe.getDate();
            if (ngayVe == null || boThoiGian(ngayVe).before(d)) {
                dcNgayVe.setDate(d);
            }
        }
    }
});

dcNgayVe.getDateEditor().addPropertyChangeListener("date", evt -> {
    if (jRadioButton2.isSelected()) {
        kiemTraNgayVe();
    }

});
jScrollPane3.getViewport().addMouseListener(new java.awt.event.MouseAdapter() {
    @Override
    public void mousePressed(java.awt.event.MouseEvent e) {
        jTable2.clearSelection();
    }
});
    if (!daMoCa) {
    hienThiPanelChuaMoCa();
}

    
    
        
        //jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        //jPanel1.setLayout(new BoxLayout(jPanel1, BoxLayout.Y_AXIS));
        //jPanel4.setLayout(new BoxLayout(jPanel4, BoxLayout.Y_AXIS));
        
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("un")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelTrai = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        thongtinnv = new javax.swing.JPanel();
        ten = new javax.swing.JPanel();
        lbltennv = new javax.swing.JLabel();
        hienten = new javax.swing.JLabel();
        manv = new javax.swing.JPanel();
        lblma = new javax.swing.JLabel();
        hienma = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        lblchucvu = new javax.swing.JLabel();
        hienchucvu = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        lblcalam = new javax.swing.JLabel();
        hiencalam = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jButton10 = new javax.swing.JButton();
        jButton25 = new javax.swing.JButton();
        tieudethongtinnv = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel4 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jButton15 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        jButton12 = new javax.swing.JButton();
        jPanelNoi_Dung = new javax.swing.JPanel();
        GD_TRANGCHU = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        lblgadi = new javax.swing.JLabel();
        lblgaden = new javax.swing.JLabel();
        lblloaive = new javax.swing.JLabel();
        lblngaydi = new javax.swing.JLabel();
        lblngayve = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jtgadi = new javax.swing.JTextField();
        jComboBox2 = new javax.swing.JComboBox<>();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jButton13 = new javax.swing.JButton();
        jPanel39 = new javax.swing.JPanel();
        jLabel42 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        dschuyen = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        North = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        tieude = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblDongHo = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        GD_THONGTINNHANVIEN = new javax.swing.JPanel();
        North1 = new javax.swing.JPanel();
        jPanel15 = new javax.swing.JPanel();
        tieude1 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        lblDongHo1 = new javax.swing.JLabel();
        jPanel18 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        ttnv = new javax.swing.JPanel();
        pnNorth_nv = new javax.swing.JPanel();
        lblthongtinnv = new javax.swing.JLabel();
        pnCenter_nv = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton14 = new javax.swing.JButton();
        jPanel19 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jPanel22 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jPanel23 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        GD_TAU = new javax.swing.JPanel();
        GD_chon_TOA_GHE_XUOI = new javax.swing.JPanel();
        jPanel34 = new javax.swing.JPanel();
        jPanel31_chieuxuoi = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jPanel26 = new javax.swing.JPanel();
        jPanel33 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jPanel37 = new javax.swing.JPanel();
        jPanel31 = new javax.swing.JPanel();
        jPanel45 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jPanel49 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        jPanel50 = new javax.swing.JPanel();
        jLabel50 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jPanel46 = new javax.swing.JPanel();
        jPanel35 = new javax.swing.JPanel();
        jButton19 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton16 = new javax.swing.JButton();
        GD_TOA_GHE_NGUOC = new javax.swing.JPanel();
        jPanel32_chieunguoc = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jPanel28 = new javax.swing.JPanel();
        jPanel30 = new javax.swing.JPanel();
        jPanel32 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        jPanel51 = new javax.swing.JPanel();
        jLabel51 = new javax.swing.JLabel();
        jPanel52 = new javax.swing.JPanel();
        jLabel52 = new javax.swing.JLabel();
        jPanel53 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jPanel44 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jButton17 = new javax.swing.JButton();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        GD_NHAP_TT_HK = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jPanel47 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jPanel40 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        jPanel41 = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jButton22 = new javax.swing.JButton();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jPanel54 = new javax.swing.JPanel();
        jLabel54 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelTrai.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelTrai.setName(""); // NOI18N
        jPanelTrai.setPreferredSize(new java.awt.Dimension(240, 100));
        jPanelTrai.setRequestFocusEnabled(false);
        jPanelTrai.setLayout(new java.awt.BorderLayout());

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanel3.setPreferredSize(new java.awt.Dimension(240, 180));
        jPanel3.setLayout(new java.awt.BorderLayout());

        thongtinnv.setLayout(new javax.swing.BoxLayout(thongtinnv, javax.swing.BoxLayout.Y_AXIS));

        ten.setLayout(new javax.swing.BoxLayout(ten, javax.swing.BoxLayout.X_AXIS));

        lbltennv.setText("Họ tên:");
        ten.add(lbltennv);

        hienten.setText("tên");
        ten.add(hienten);

        thongtinnv.add(ten);

        manv.setLayout(new javax.swing.BoxLayout(manv, javax.swing.BoxLayout.X_AXIS));

        lblma.setText("Mã nhân viên:");
        manv.add(lblma);

        hienma.setText("mã");
        manv.add(hienma);

        thongtinnv.add(manv);

        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.X_AXIS));

        lblchucvu.setText("Chức vụ:");
        jPanel5.add(lblchucvu);

        hienchucvu.setText("hiện cv");
        jPanel5.add(hienchucvu);

        thongtinnv.add(jPanel5);

        jPanel10.setLayout(new javax.swing.BoxLayout(jPanel10, javax.swing.BoxLayout.X_AXIS));

        lblcalam.setText("Ca làm:");
        jPanel10.add(lblcalam);

        hiencalam.setText("hienca");
        jPanel10.add(hiencalam);

        jLabel44.setText("hientg");
        jPanel10.add(jLabel44);

        thongtinnv.add(jPanel10);

        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.LINE_AXIS));

        jButton10.setText("Xem thông tin");
        jButton10.addActionListener(this::jButton10ActionPerformed);
        jPanel11.add(jButton10);

        jButton25.setText("Mở ca");
        jButton25.addActionListener(this::jButton25ActionPerformed);
        jPanel11.add(jButton25);

        thongtinnv.add(jPanel11);

        jPanel3.add(thongtinnv, java.awt.BorderLayout.CENTER);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Thông tin nhân viên");

        javax.swing.GroupLayout tieudethongtinnvLayout = new javax.swing.GroupLayout(tieudethongtinnv);
        tieudethongtinnv.setLayout(tieudethongtinnvLayout);
        tieudethongtinnvLayout.setHorizontalGroup(
            tieudethongtinnvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, tieudethongtinnvLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 222, Short.MAX_VALUE)
                .addContainerGap())
        );
        tieudethongtinnvLayout.setVerticalGroup(
            tieudethongtinnvLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        jPanel3.add(tieudethongtinnv, java.awt.BorderLayout.NORTH);

        jPanelTrai.add(jPanel3, java.awt.BorderLayout.NORTH);

        jScrollPane1.setWheelScrollingEnabled(false);

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/TrangChu.jpg"))); // NOI18N
        jButton1.setText("Trang Chủ");
        jButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton1.setIconTextGap(30);
        jButton1.setMinimumSize(new java.awt.Dimension(100, 32));
        jButton1.setPreferredSize(new java.awt.Dimension(50, 32));
        jButton1.addActionListener(this::jButton1ActionPerformed);
        jPanel4.add(jButton1);

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/vetau (1).png"))); // NOI18N
        jButton2.setText("Quản lí bán vé");
        jButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton2.setIconTextGap(20);
        jButton2.addActionListener(this::jButton2ActionPerformed);
        jPanel4.add(jButton2);

        jButton3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/tt (1).jpg"))); // NOI18N
        jButton3.setText("Thống kê");
        jButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton3.setIconTextGap(30);
        jButton3.setInheritsPopupMenu(true);
        jPanel4.add(jButton3);

        jButton4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/kh (1).jpg"))); // NOI18N
        jButton4.setText("Quản lí khách hàng");
        jButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton4.setIconTextGap(20);
        jButton4.addActionListener(this::jButton4ActionPerformed);
        jPanel4.add(jButton4);

        jButton5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/km (1).jpg"))); // NOI18N
        jButton5.setText("Quản lí khuyến mãi");
        jButton5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton5.setIconTextGap(20);
        jPanel4.add(jButton5);

        jButton9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/hanhkhach (1).jpg"))); // NOI18N
        jButton9.setText("Quản lí nhân viên");
        jButton9.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton9.setIconTextGap(20);
        jButton9.addActionListener(this::jButton9ActionPerformed);
        jPanel4.add(jButton9);

        jButton15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/kc (1).png"))); // NOI18N
        jButton15.setText("Quản lí tài khoản");
        jButton15.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton15.setIconTextGap(20);
        jButton15.setInheritsPopupMenu(true);
        jButton15.addActionListener(this::jButton15ActionPerformed);
        jPanel4.add(jButton15);

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/kc (1).png"))); // NOI18N
        jButton6.setText("Kết ca");
        jButton6.setHideActionText(true);
        jButton6.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton6.setIconTextGap(40);
        jPanel4.add(jButton6);

        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/tg (1).jpg"))); // NOI18N
        jButton8.setText("Trợ giúp");
        jButton8.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton8.setIconTextGap(40);
        jButton8.addActionListener(this::jButton8ActionPerformed);
        jPanel4.add(jButton8);

        jButton7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/logout (1).jpg"))); // NOI18N
        jButton7.setText("Đăng xuất");
        jButton7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton7.setIconTextGap(40);
        jPanel4.add(jButton7);

        jButton11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/chuyentau (1).jpg"))); // NOI18N
        jButton11.setText("Quản lí chuyến tàu");
        jButton11.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton11.setIconTextGap(20);
        jPanel4.add(jButton11);

        jButton12.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/tau (1).jpg"))); // NOI18N
        jButton12.setText("Quản lí tàu");
        jButton12.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jButton12.setIconTextGap(30);
        jPanel4.add(jButton12);

        jScrollPane1.setViewportView(jPanel4);

        jPanelTrai.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        jScrollPane1.getAccessibleContext().setAccessibleParent(jPanelTrai);

        getContentPane().add(jPanelTrai, java.awt.BorderLayout.WEST);

        jPanelNoi_Dung.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanelNoi_Dung.setLayout(new java.awt.CardLayout());

        GD_TRANGCHU.setLayout(new java.awt.BorderLayout());

        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel12.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 10, 1));
        jPanel12.setLayout(new javax.swing.BoxLayout(jPanel12, javax.swing.BoxLayout.Y_AXIS));

        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.X_AXIS));

        lblgadi.setText("Ga đi");
        jPanel13.add(lblgadi);

        lblgaden.setText("Ga đến");
        jPanel13.add(lblgaden);

        lblloaive.setText("Loại vé");
        jPanel13.add(lblloaive);

        lblngaydi.setText("Ngày đi");
        jPanel13.add(lblngaydi);

        lblngayve.setText("Ngày về (Khứ hồi)");
        jPanel13.add(lblngayve);

        jPanel12.add(jPanel13);

        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.X_AXIS));

        jtgadi.setText("jTextField1");
        jPanel14.add(jtgadi);

        jComboBox2.setEditable(true);
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Chọn ga đến...", "Hà Nội", "Đà Nẵng", "Huế", "Nha Trang", "Vinh", "Phan Thiết", "Quy Nhơn" }));
        jPanel14.add(jComboBox2);

        jRadioButton1.setText("1 chiều");
        jPanel14.add(jRadioButton1);

        jRadioButton2.setText("khứ hồi");
        jPanel14.add(jRadioButton2);

        jButton13.setBackground(new java.awt.Color(255, 255, 102));
        jButton13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/kinlup (1).jpg"))); // NOI18N
        jButton13.setText("Tìm chuyến");
        jPanel14.add(jButton13);

        jPanel12.add(jPanel14);

        jLabel42.setText("Số lượng vé:");
        jPanel39.add(jLabel42);

        jTextField1.setText("1");
        jTextField1.addActionListener(this::jTextField1ActionPerformed);
        jPanel39.add(jTextField1);

        jLabel43.setText("Hành khách");
        jPanel39.add(jLabel43);

        jPanel12.add(jPanel39);

        jPanel1.add(jPanel12, java.awt.BorderLayout.NORTH);

        dschuyen.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Danh sách chuyến tàu", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        dschuyen.setLayout(new java.awt.BorderLayout());

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã chuyến", "Tên chuyến", "Tên Tàu", "Ga đi", "Ga đến ", "Ngày đi ", "Ngày đến dự kiến", "Số lượng ghế", "Ghế trống", "Chọn chuyến"
            }
        ));
        jScrollPane3.setViewportView(jTable2);

        dschuyen.add(jScrollPane3, java.awt.BorderLayout.CENTER);

        jPanel1.add(dschuyen, java.awt.BorderLayout.CENTER);

        GD_TRANGCHU.add(jPanel1, java.awt.BorderLayout.CENTER);

        North.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        North.setMinimumSize(new java.awt.Dimension(409, 160));
        North.setPreferredSize(new java.awt.Dimension(643, 180));
        North.setRequestFocusEnabled(false);
        North.setLayout(new javax.swing.BoxLayout(North, javax.swing.BoxLayout.Y_AXIS));

        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.Y_AXIS));

        tieude.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)), "THÔNG TIN NHANH", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Segoe UI", 1, 13))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel6.setText("HỆ THỐNG BÁN VÉ TÀU GA SÀI GÒN");
        tieude.add(jLabel6);

        jPanel7.add(tieude);

        jPanel8.setPreferredSize(new java.awt.Dimension(641, 60));

        jLabel8.setText("Số vé đã bán hôm nay:");

        jLabel9.setText("số lượng");

        jLabel7.setText("Ngày/giờ:");

        lblDongHo.setText("hiện giờ");

        jLabel10.setText("Doanh thu hôm nay:");

        jLabel11.setText("doanh thu");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDongHo))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel11)))
                .addContainerGap(570, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblDongHo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addGap(0, 42, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel8Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10))
                .addContainerGap())
        );

        jPanel7.add(jPanel8);

        North.add(jPanel7);

        GD_TRANGCHU.add(North, java.awt.BorderLayout.NORTH);

        jPanelNoi_Dung.add(GD_TRANGCHU, "card2");

        GD_THONGTINNHANVIEN.setLayout(new java.awt.BorderLayout());

        North1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        North1.setMinimumSize(new java.awt.Dimension(409, 160));
        North1.setPreferredSize(new java.awt.Dimension(643, 180));
        North1.setRequestFocusEnabled(false);
        North1.setLayout(new javax.swing.BoxLayout(North1, javax.swing.BoxLayout.Y_AXIS));

        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15, javax.swing.BoxLayout.Y_AXIS));

        tieude1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 1, 0, new java.awt.Color(0, 0, 0)), "THÔNG TIN NHANH", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.BOTTOM, new java.awt.Font("Segoe UI", 1, 13))); // NOI18N

        jLabel12.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel12.setText("HỆ THỐNG BÁN VÉ TÀU GA SÀI GÒN");
        tieude1.add(jLabel12);

        jPanel15.add(tieude1);

        jPanel16.setLayout(new javax.swing.BoxLayout(jPanel16, javax.swing.BoxLayout.X_AXIS));
        jPanel15.add(jPanel16);

        jLabel13.setText("Số vé đã bán hôm nay:");

        jLabel14.setText("số lượng");

        jLabel15.setText("Ngày/giờ:");

        lblDongHo1.setText("hiện giờ");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel13)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel14))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblDongHo1)))
                .addContainerGap(570, Short.MAX_VALUE))
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(lblDongHo1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel15.add(jPanel17);

        jLabel16.setText("Doanh thu hoom nay:");

        jLabel17.setText("doanh thu");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17)
                .addContainerGap(568, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel18Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(jLabel17)))
        );

        jPanel15.add(jPanel18);

        North1.add(jPanel15);

        GD_THONGTINNHANVIEN.add(North1, java.awt.BorderLayout.NORTH);

        ttnv.setLayout(new java.awt.BorderLayout());

        pnNorth_nv.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblthongtinnv.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblthongtinnv.setText("THÔNG TIN CÁ NHÂN");
        pnNorth_nv.add(lblthongtinnv);

        ttnv.add(pnNorth_nv, java.awt.BorderLayout.NORTH);

        pnCenter_nv.setLayout(new javax.swing.BoxLayout(pnCenter_nv, javax.swing.BoxLayout.X_AXIS));

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        jButton14.setText("Đổi mật khẩu");
        jPanel2.add(jButton14);

        pnCenter_nv.add(jPanel2);

        jPanel19.setLayout(new javax.swing.BoxLayout(jPanel19, javax.swing.BoxLayout.Y_AXIS));

        jPanel20.setLayout(new javax.swing.BoxLayout(jPanel20, javax.swing.BoxLayout.X_AXIS));

        jLabel4.setText("Mã nhân viên:");
        jPanel20.add(jLabel4);

        jLabel23.setText("hienma");
        jPanel20.add(jLabel23);

        jLabel24.setText("Chức vụ:");
        jPanel20.add(jLabel24);

        jLabel25.setText("hienchucvu");
        jPanel20.add(jLabel25);

        jPanel19.add(jPanel20);

        jPanel21.setLayout(new javax.swing.BoxLayout(jPanel21, javax.swing.BoxLayout.X_AXIS));

        jLabel2.setText("Tên nhân viên:");
        jPanel21.add(jLabel2);

        jLabel3.setText("hiện tên");
        jPanel21.add(jLabel3);

        jLabel5.setText("Ngày sinh:");
        jPanel21.add(jLabel5);

        jLabel18.setText("hienngaysinh");
        jPanel21.add(jLabel18);

        jLabel26.setText("Giới tính:");
        jPanel21.add(jLabel26);

        jLabel27.setText("hiengt");
        jPanel21.add(jLabel27);

        jPanel19.add(jPanel21);

        jPanel22.setLayout(new javax.swing.BoxLayout(jPanel22, javax.swing.BoxLayout.X_AXIS));

        jLabel19.setText("CCCD:");
        jPanel22.add(jLabel19);

        jLabel20.setText("hiencccd");
        jPanel22.add(jLabel20);

        jLabel21.setText("Số điện thoại:");
        jPanel22.add(jLabel21);

        jLabel22.setText("hiensdt");
        jPanel22.add(jLabel22);

        jPanel19.add(jPanel22);

        jPanel23.setLayout(new javax.swing.BoxLayout(jPanel23, javax.swing.BoxLayout.LINE_AXIS));

        jLabel28.setText("Ngày vào làm:");
        jPanel23.add(jLabel28);

        jLabel29.setText("hienngay");
        jPanel23.add(jLabel29);

        jLabel30.setText("Trạng thái:");
        jPanel23.add(jLabel30);

        jLabel31.setText("hientrangthai");
        jPanel23.add(jLabel31);

        jPanel19.add(jPanel23);

        jPanel24.setLayout(new javax.swing.BoxLayout(jPanel24, javax.swing.BoxLayout.LINE_AXIS));
        jPanel19.add(jPanel24);

        pnCenter_nv.add(jPanel19);

        ttnv.add(pnCenter_nv, java.awt.BorderLayout.CENTER);

        GD_THONGTINNHANVIEN.add(ttnv, java.awt.BorderLayout.CENTER);

        jPanelNoi_Dung.add(GD_THONGTINNHANVIEN, "card3");

        javax.swing.GroupLayout GD_TAULayout = new javax.swing.GroupLayout(GD_TAU);
        GD_TAU.setLayout(GD_TAULayout);
        GD_TAULayout.setHorizontalGroup(
            GD_TAULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 753, Short.MAX_VALUE)
        );
        GD_TAULayout.setVerticalGroup(
            GD_TAULayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1274, Short.MAX_VALUE)
        );

        jPanelNoi_Dung.add(GD_TAU, "card4");

        GD_chon_TOA_GHE_XUOI.setPreferredSize(new java.awt.Dimension(644, 1274));
        GD_chon_TOA_GHE_XUOI.setLayout(new javax.swing.BoxLayout(GD_chon_TOA_GHE_XUOI, javax.swing.BoxLayout.Y_AXIS));

        jPanel34.setLayout(new javax.swing.BoxLayout(jPanel34, javax.swing.BoxLayout.X_AXIS));

        jPanel31_chieuxuoi.setPreferredSize(new java.awt.Dimension(332, 1274));
        jPanel31_chieuxuoi.setLayout(new javax.swing.BoxLayout(jPanel31_chieuxuoi, javax.swing.BoxLayout.Y_AXIS));

        jPanel25.setLayout(new javax.swing.BoxLayout(jPanel25, javax.swing.BoxLayout.Y_AXIS));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel32.setText("Chiều đi");
        jPanel25.add(jLabel32);

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel33.setText("tentau");
        jPanel25.add(jLabel33);

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel34.setText("tengadi-gaden");
        jPanel25.add(jLabel34);

        jPanel31_chieuxuoi.add(jPanel25);

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Danh sách toa tàu", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        jPanel26.setPreferredSize(new java.awt.Dimension(391, 200));
        jPanel26.setLayout(new java.awt.BorderLayout());

        jPanel33.setPreferredSize(new java.awt.Dimension(370, 720));

        javax.swing.GroupLayout jPanel33Layout = new javax.swing.GroupLayout(jPanel33);
        jPanel33.setLayout(jPanel33Layout);
        jPanel33Layout.setHorizontalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 344, Short.MAX_VALUE)
        );
        jPanel33Layout.setVerticalGroup(
            jPanel33Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 416, Short.MAX_VALUE)
        );

        jPanel26.add(jPanel33, java.awt.BorderLayout.CENTER);

        jPanel31_chieuxuoi.add(jPanel26);

        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Danh sách ghế", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        jPanel29.setPreferredSize(new java.awt.Dimension(391, 300));
        jPanel29.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 743, Short.MAX_VALUE)
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 516, Short.MAX_VALUE)
        );

        jPanel29.add(jPanel37, java.awt.BorderLayout.CENTER);

        jPanel31_chieuxuoi.add(jPanel29);

        jPanel31.setPreferredSize(new java.awt.Dimension(376, 200));
        jPanel31.setLayout(new java.awt.GridLayout(1, 0));

        jPanel45.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Ghi chú", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));
        jPanel45.setLayout(new javax.swing.BoxLayout(jPanel45, javax.swing.BoxLayout.Y_AXIS));

        jPanel48.setLayout(new javax.swing.BoxLayout(jPanel48, javax.swing.BoxLayout.LINE_AXIS));

        jLabel47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/mauxanh.jpg"))); // NOI18N
        jLabel47.setText("Đang chọn");
        jLabel47.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel47.setIconTextGap(20);
        jPanel48.add(jLabel47);

        jPanel45.add(jPanel48);

        jPanel49.setLayout(new javax.swing.BoxLayout(jPanel49, javax.swing.BoxLayout.LINE_AXIS));

        jLabel49.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/maudo1 (1).jpg"))); // NOI18N
        jLabel49.setText("Đầy");
        jLabel49.setIconTextGap(20);
        jPanel49.add(jLabel49);

        jPanel45.add(jPanel49);

        jPanel50.setLayout(new javax.swing.BoxLayout(jPanel50, javax.swing.BoxLayout.LINE_AXIS));

        jLabel50.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/mauxam (1).jpg"))); // NOI18N
        jLabel50.setText("Trống");
        jLabel50.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel50.setIconTextGap(20);
        jPanel50.add(jLabel50);

        jPanel45.add(jPanel50);

        jLabel55.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/maunhat1.jpg"))); // NOI18N
        jLabel55.setText("Đang giữ chỗ");
        jLabel55.setIconTextGap(20);
        jPanel45.add(jLabel55);

        jPanel31.add(jPanel45);

        jPanel46.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Thông tin vé", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));

        javax.swing.GroupLayout jPanel46Layout = new javax.swing.GroupLayout(jPanel46);
        jPanel46.setLayout(jPanel46Layout);
        jPanel46Layout.setHorizontalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 167, Short.MAX_VALUE)
        );
        jPanel46Layout.setVerticalGroup(
            jPanel46Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 177, Short.MAX_VALUE)
        );

        jPanel31.add(jPanel46);

        jPanel31_chieuxuoi.add(jPanel31);

        jPanel34.add(jPanel31_chieuxuoi);

        GD_chon_TOA_GHE_XUOI.add(jPanel34);

        jPanel35.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jButton19.setBackground(new java.awt.Color(102, 255, 102));
        jButton19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton19.setForeground(new java.awt.Color(242, 242, 242));
        jButton19.setText("Xác nhận");
        jButton19.addActionListener(this::jButton19ActionPerformed);
        jPanel35.add(jButton19);

        jButton18.setText("Quay lại");
        jButton18.addActionListener(this::jButton18ActionPerformed);
        jPanel35.add(jButton18);

        jButton16.setBackground(new java.awt.Color(255, 0, 51));
        jButton16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jButton16.setForeground(new java.awt.Color(242, 242, 242));
        jButton16.setText("Hủy");
        jButton16.addActionListener(this::jButton16ActionPerformed);
        jPanel35.add(jButton16);

        GD_chon_TOA_GHE_XUOI.add(jPanel35);

        jPanelNoi_Dung.add(GD_chon_TOA_GHE_XUOI, "card5");

        GD_TOA_GHE_NGUOC.setLayout(new javax.swing.BoxLayout(GD_TOA_GHE_NGUOC, javax.swing.BoxLayout.Y_AXIS));

        jPanel32_chieunguoc.setPreferredSize(new java.awt.Dimension(332, 1274));
        jPanel32_chieunguoc.setLayout(new javax.swing.BoxLayout(jPanel32_chieunguoc, javax.swing.BoxLayout.Y_AXIS));

        jPanel27.setLayout(new javax.swing.BoxLayout(jPanel27, javax.swing.BoxLayout.Y_AXIS));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel35.setText("Chiều về ");
        jPanel27.add(jLabel35);

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel36.setText("tentau");
        jPanel27.add(jLabel36);

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel37.setText("tengadi-gaden");
        jPanel27.add(jLabel37);

        jPanel32_chieunguoc.add(jPanel27);

        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Danh sách toa tàu", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        jPanel28.setPreferredSize(new java.awt.Dimension(361, 300));

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 743, Short.MAX_VALUE)
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 414, Short.MAX_VALUE)
        );

        jPanel32_chieunguoc.add(jPanel28);

        jPanel30.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Danh sách ghế", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.TOP, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        jPanel30.setPreferredSize(new java.awt.Dimension(361, 300));

        javax.swing.GroupLayout jPanel30Layout = new javax.swing.GroupLayout(jPanel30);
        jPanel30.setLayout(jPanel30Layout);
        jPanel30Layout.setHorizontalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 743, Short.MAX_VALUE)
        );
        jPanel30Layout.setVerticalGroup(
            jPanel30Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 414, Short.MAX_VALUE)
        );

        jPanel32_chieunguoc.add(jPanel30);

        jPanel32.setPreferredSize(new java.awt.Dimension(376, 200));
        jPanel32.setLayout(new java.awt.GridLayout(1, 0));

        jPanel43.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Ghi chú", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));
        jPanel43.setLayout(new javax.swing.BoxLayout(jPanel43, javax.swing.BoxLayout.Y_AXIS));

        jPanel51.setLayout(new javax.swing.BoxLayout(jPanel51, javax.swing.BoxLayout.X_AXIS));

        jLabel51.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/mauxanh.jpg"))); // NOI18N
        jLabel51.setText("Đang chọn");
        jLabel51.setIconTextGap(20);
        jPanel51.add(jLabel51);

        jPanel43.add(jPanel51);

        jPanel52.setLayout(new javax.swing.BoxLayout(jPanel52, javax.swing.BoxLayout.X_AXIS));

        jLabel52.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/maudo1 (1).jpg"))); // NOI18N
        jLabel52.setText("Đầy");
        jLabel52.setIconTextGap(20);
        jPanel52.add(jLabel52);

        jPanel43.add(jPanel52);

        jPanel53.setLayout(new javax.swing.BoxLayout(jPanel53, javax.swing.BoxLayout.X_AXIS));

        jLabel53.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/mauxam (1).jpg"))); // NOI18N
        jLabel53.setText("Trống");
        jLabel53.setIconTextGap(20);
        jPanel53.add(jLabel53);

        jPanel43.add(jPanel53);

        jLabel56.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/prj1/img/maunhat1.jpg"))); // NOI18N
        jLabel56.setText("Đang giữ chỗ");
        jLabel56.setIconTextGap(20);
        jPanel43.add(jLabel56);

        jPanel32.add(jPanel43);

        jPanel44.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Thông tin vé", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.TOP));

        javax.swing.GroupLayout jPanel44Layout = new javax.swing.GroupLayout(jPanel44);
        jPanel44.setLayout(jPanel44Layout);
        jPanel44Layout.setHorizontalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 366, Short.MAX_VALUE)
        );
        jPanel44Layout.setVerticalGroup(
            jPanel44Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 315, Short.MAX_VALUE)
        );

        jPanel32.add(jPanel44);

        jPanel32_chieunguoc.add(jPanel32);

        GD_TOA_GHE_NGUOC.add(jPanel32_chieunguoc);

        jButton17.setText("Xác nhận");
        jButton17.addActionListener(this::jButton17ActionPerformed);
        jPanel6.add(jButton17);

        jButton20.setText("Quay lại");
        jButton20.addActionListener(this::jButton20ActionPerformed);
        jPanel6.add(jButton20);

        jButton21.setText("Hủy");
        jPanel6.add(jButton21);

        GD_TOA_GHE_NGUOC.add(jPanel6);

        jPanelNoi_Dung.add(GD_TOA_GHE_NGUOC, "card6");

        GD_NHAP_TT_HK.setLayout(new java.awt.BorderLayout());

        jPanel9.setPreferredSize(new java.awt.Dimension(350, 1251));
        jPanel9.setLayout(new java.awt.BorderLayout());

        jLabel45.setText("Chi tiết giá");
        jPanel9.add(jLabel45, java.awt.BorderLayout.NORTH);

        javax.swing.GroupLayout jPanel47Layout = new javax.swing.GroupLayout(jPanel47);
        jPanel47.setLayout(jPanel47Layout);
        jPanel47Layout.setHorizontalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 350, Short.MAX_VALUE)
        );
        jPanel47Layout.setVerticalGroup(
            jPanel47Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel47, java.awt.BorderLayout.CENTER);

        GD_NHAP_TT_HK.add(jPanel9, java.awt.BorderLayout.EAST);

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("Nhập thông tin");
        jLabel46.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel46.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        GD_NHAP_TT_HK.add(jLabel46, java.awt.BorderLayout.NORTH);

        jPanel40.setLayout(new java.awt.BorderLayout());

        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel48.setText("THÔNG TIN HÀNH KHÁCH");
        jPanel40.add(jLabel48, java.awt.BorderLayout.NORTH);

        jPanel41.setLayout(new javax.swing.BoxLayout(jPanel41, javax.swing.BoxLayout.Y_AXIS));
        jPanel40.add(jPanel41, java.awt.BorderLayout.CENTER);

        GD_NHAP_TT_HK.add(jPanel40, java.awt.BorderLayout.CENTER);

        jButton22.setText("TIẾP TỤC ");
        jButton22.addActionListener(this::jButton22ActionPerformed);
        jPanel42.add(jButton22);

        jButton23.setText("QUAY LẠI");
        jPanel42.add(jButton23);

        jButton24.setText("HỦY");
        jPanel42.add(jButton24);

        GD_NHAP_TT_HK.add(jPanel42, java.awt.BorderLayout.SOUTH);

        jPanelNoi_Dung.add(GD_NHAP_TT_HK, "card7");

        jPanel54.setLayout(new java.awt.BorderLayout());

        jLabel54.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(255, 0, 51));
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setText("Hiện chưa mở ca. Vui lòng mở ca để sử dụng...!");
        jLabel54.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel54.add(jLabel54, java.awt.BorderLayout.CENTER);

        jPanelNoi_Dung.add(jPanel54, "card8");

        getContentPane().add(jPanelNoi_Dung, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
          if (chuaMoCaThiThongBao("Trang chủ")) {
        return;
    }

    moGDTrangChu();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
         if (chuaMoCaThiThongBao("Trợ giúp")) {
        return;
    }

    // code mở trợ giúp sau này
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        if (chuaMoCaThiThongBao("Quản lí khách hàng")) {
        return;
    }

    // code mở quản lí khách hàng sau này
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (chuaMoCaThiThongBao("Trang chủ")) {
        return;
    }

    moGDTrangChu();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
        if (!daChonDuGhe()) {
        int conThieu = 0;

        for (GheHanhKhach g : dsGheChonDi) {
            if (g.maGhe == null) {
                conThieu++;
            }
        }

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Bạn chưa chọn đủ ghế chiều đi.\n"
                + "Còn thiếu: " + conThieu + " ghế."
        );
        return;
    }

    // Nếu là vé khứ hồi thì sau khi chọn đủ ghế chiều đi, chuyển sang chọn ghế chiều về
    if (maChuyenVeDaChon != null && !maChuyenVeDaChon.trim().isEmpty()) {
        moManHinhChonToaVaGheNguoc();
    } 
    // Nếu là vé 1 chiều thì chuyển sang nhập thông tin hành khách
    else {
        moManHinhNhapThongTinHanhKhach();
    }
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton16ActionPerformed
        xacNhanHuyVaVeTrangChu();
    }//GEN-LAST:event_jButton16ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
         java.awt.CardLayout cl = (java.awt.CardLayout) jPanelNoi_Dung.getLayout();
         cl.show(jPanelNoi_Dung, "card5");
    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        // TODO add your handling code here:
         if (!kiemTraTatCaThongTinHanhKhach()) {
        return;
    }

    hienThiChonHinhThucThanhToan();
    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        // TODO add your handling code here:
        if (!daChonDuGheChieuVe()) {
        int conThieu = 0;

        for (GheHanhKhach g : dsGheChonVe) {
            if (g.maGhe == null) {
                conThieu++;
            }
        }

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Bạn chưa chọn đủ ghế chiều về.\n"
                + "Còn thiếu: " + conThieu + " ghế."
        );
        return;
    }

    moManHinhNhapThongTinHanhKhach();
    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton10ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
       xuLyMoCa();
         
        
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
         moManHinhQuanLyNhanVien();
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        moManHinhQuanLyTaiKhoan();
    }//GEN-LAST:event_jButton15ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new jf1().setVisible(true));
    }
    private void hienThiDongHo() {
    java.util.Locale localeVN = new java.util.Locale("vi", "VN");
    DateTimeFormatter dinhDang = DateTimeFormatter.ofPattern(
            "EEEE, dd/MM/yyyy HH:mm:ss",
            localeVN
    );

    Timer timer = new Timer(1000, e -> {
        String thoiGian = LocalDateTime.now().format(dinhDang);

        // Viết hoa chữ cái đầu cho đẹp
        thoiGian = thoiGian.substring(0, 1).toUpperCase() + thoiGian.substring(1);

        lblDongHo.setText(thoiGian);
        lblDongHo1.setText(thoiGian);
    });

    timer.start();
}
    private void chinhMenuTrai() {
    java.awt.Dimension size = new java.awt.Dimension(210, 50);

    jPanel4.removeAll();
    jPanel4.setLayout(new BoxLayout(jPanel4, BoxLayout.Y_AXIS));

    javax.swing.JButton[] nhomTren = {
        jButton1,jButton2, jButton4,jButton3,jButton11 ,jButton12 ,jButton5, 
          jButton9,jButton15,jButton6
    };

    javax.swing.JButton[] nhomDuoi = {
        jButton8, jButton7
    };

    for (javax.swing.JButton btn : nhomTren) {
        btn.setPreferredSize(size);
        btn.setMaximumSize(size);
        btn.setMinimumSize(size);
        btn.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        jPanel4.add(btn);
        jPanel4.add(Box.createVerticalStrut(4));
    }

    // khoảng cách lớn giữa 2 nhóm
    jPanel4.add(Box.createVerticalStrut(18));

    for (javax.swing.JButton btn : nhomDuoi) {
        btn.setPreferredSize(size);
        btn.setMaximumSize(size);
        btn.setMinimumSize(size);
        btn.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        jPanel4.add(btn);
        jPanel4.add(Box.createVerticalStrut(4));
    }

    jPanel4.revalidate();
    jPanel4.repaint();

}
    private void chinhThongTinNhanVien() {
    thongtinnv.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 12, 8, 12));

    javax.swing.JPanel[] dsDong = {
        ten, manv, jPanel5, jPanel10, jPanel11
    };

    for (javax.swing.JPanel p : dsDong) {
        p.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        p.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 0, 4, 0));
    }

    lbltennv.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    hienten.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    lblma.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    hienma.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    lblchucvu.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    hienchucvu.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    lblcalam.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    hiencalam.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    
    jButton10.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

    thongtinnv.revalidate();
    thongtinnv.repaint();
    jPanel11.removeAll();
    jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.X_AXIS));

    jPanel11.add(jButton10);
    jPanel11.add(javax.swing.Box.createHorizontalStrut(10)); // khoảng cách giữa 2 nút
    jPanel11.add(jButton25);
}
    private void chinhPanelChonThongTinChuyen() {
    // tạo khoảng cách bên trong panel ngoài
    jPanel12.setBorder(javax.swing.BorderFactory.createCompoundBorder(
        javax.swing.BorderFactory.createTitledBorder(
            javax.swing.BorderFactory.createEtchedBorder(),
            "Chọn thông tin chuyến",
            javax.swing.border.TitledBorder.LEFT,
            javax.swing.border.TitledBorder.TOP
        ),
        javax.swing.BorderFactory.createEmptyBorder(10, 12, 10, 12)
    ));
    java.awt.Dimension sizeBtnQuayLai = new java.awt.Dimension(100, 30);

    btnQuayLaiSauKhiChonLan1.setPreferredSize(sizeBtnQuayLai);
    btnQuayLaiSauKhiChonLan1.setMinimumSize(sizeBtnQuayLai);
    btnQuayLaiSauKhiChonLan1.setMaximumSize(sizeBtnQuayLai);    
    // khoảng cách trên dưới cho từng hàng
    jPanel13.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 5, 0));
    jPanel14.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 0, 5, 0));

    // canh trái
    jPanel13.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    jPanel14.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

    // đặt kích thước cho nhãn hàng trên
    java.awt.Dimension sizeLabel1 = new java.awt.Dimension(100, 25);
    java.awt.Dimension sizeLabel2 = new java.awt.Dimension(100, 25);
    java.awt.Dimension sizeLabel3 = new java.awt.Dimension(120, 25);
    java.awt.Dimension sizeLabel4 = new java.awt.Dimension(100, 25);
    java.awt.Dimension sizeLabel5 = new java.awt.Dimension(140, 25);

    lblgadi.setPreferredSize(sizeLabel1);
    lblgadi.setMinimumSize(sizeLabel1);
    lblgadi.setMaximumSize(sizeLabel1);

    lblgaden.setPreferredSize(sizeLabel2);
    lblgaden.setMinimumSize(sizeLabel2);
    lblgaden.setMaximumSize(sizeLabel2);

    lblloaive.setPreferredSize(sizeLabel3);
    lblloaive.setMinimumSize(sizeLabel3);
    lblloaive.setMaximumSize(sizeLabel3);

    lblngaydi.setPreferredSize(sizeLabel4);
    lblngaydi.setMinimumSize(sizeLabel4);
    lblngaydi.setMaximumSize(sizeLabel4);

    lblngayve.setPreferredSize(sizeLabel5);
    lblngayve.setMinimumSize(sizeLabel5);
    lblngayve.setMaximumSize(sizeLabel5);

    // đặt kích thước cho hàng dưới
    java.awt.Dimension sizeText = new java.awt.Dimension(110, 30);
    java.awt.Dimension sizeText2 = new java.awt.Dimension(90, 30);
    java.awt.Dimension sizeRadio1 = new java.awt.Dimension(80, 30);
    java.awt.Dimension sizeRadio2 = new java.awt.Dimension(80, 30);
    java.awt.Dimension sizeBtn1 = new java.awt.Dimension(100, 30);
    java.awt.Dimension sizeBtn2 = new java.awt.Dimension(120, 30);

    jtgadi.setPreferredSize(sizeText2);
    jtgadi.setMinimumSize(sizeText2);
    jtgadi.setMaximumSize(sizeText2);

    jComboBox2.setPreferredSize(sizeText);
    jComboBox2.setMinimumSize(sizeText);
    jComboBox2.setMaximumSize(sizeText);

    jRadioButton1.setPreferredSize(sizeRadio1);
    jRadioButton1.setMinimumSize(sizeRadio1);
    jRadioButton1.setMaximumSize(sizeRadio1);

    jRadioButton2.setPreferredSize(sizeRadio2);
    jRadioButton2.setMinimumSize(sizeRadio2);
    jRadioButton2.setMaximumSize(sizeRadio2);

    dcNgayDi.setPreferredSize(sizeBtn2);
    dcNgayDi.setMinimumSize(sizeBtn2);
    dcNgayDi.setMaximumSize(sizeBtn2);

    dcNgayVe.setPreferredSize(sizeBtn2);
    dcNgayVe.setMinimumSize(sizeBtn2);
    dcNgayVe.setMaximumSize(sizeBtn2);

    // làm lại hàng tiêu đề
    jPanel13.removeAll();
    jPanel13.setLayout(new BoxLayout(jPanel13, BoxLayout.X_AXIS));
    jPanel13.add(lblgadi);
    jPanel13.add(Box.createHorizontalStrut(15));
    jPanel13.add(lblgaden);
    jPanel13.add(Box.createHorizontalStrut(37));
    jPanel13.add(lblloaive);
    jPanel13.add(Box.createHorizontalStrut(75));
    jPanel13.add(lblngaydi);
    jPanel13.add(Box.createHorizontalStrut(60));
    jPanel13.add(lblngayve);

    // làm lại hàng nhập
    jPanel14.removeAll();
    jPanel14.setLayout(new BoxLayout(jPanel14, BoxLayout.X_AXIS));
    jPanel14.add(jtgadi);
    jPanel14.add(Box.createHorizontalStrut(15));
    jPanel14.add(jComboBox2);
    jPanel14.add(Box.createHorizontalStrut(35));
    jPanel14.add(jRadioButton1);
    jPanel14.add(Box.createHorizontalStrut(3));
    jPanel14.add(jRadioButton2);
    jPanel14.add(Box.createHorizontalStrut(35));
    jPanel14.add(dcNgayDi);
    jPanel14.add(Box.createHorizontalStrut(40));
    jPanel14.add(dcNgayVe);
    jPanel14.add(Box.createHorizontalStrut(45));
    jPanel14.add(jButton13);
    jPanel14.add(Box.createHorizontalStrut(10));
    jPanel14.add(btnQuayLaiSauKhiChonLan1);
    jPanel12.revalidate();
    jPanel12.repaint();
}
    class ButtonRenderer extends javax.swing.JButton implements javax.swing.table.TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
        setText("Chọn");
    }

    @Override
    public java.awt.Component getTableCellRendererComponent(
            javax.swing.JTable table, Object value, boolean isSelected,
            boolean hasFocus, int row, int column) {
        setText(value == null ? "Chọn" : value.toString());
        return this;
    }
}
    class ButtonEditor extends javax.swing.AbstractCellEditor implements javax.swing.table.TableCellEditor {
    private final javax.swing.JButton button;
    private final javax.swing.JTable table;
    private String label;
    private int currentRow = -1;

    public ButtonEditor(javax.swing.JTable table) {
        this.table = table;
        this.button = new javax.swing.JButton();
        button.addActionListener(e -> fireEditingStopped());
    }

    @Override
    public java.awt.Component getTableCellEditorComponent(
            javax.swing.JTable table, Object value, boolean isSelected, int row, int column) {
        currentRow = row;
        label = (value == null) ? "Chọn" : value.toString();
        button.setText(label);
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (!kiemTraTongSoVeHopLe()) {
        return label;
    }
        if (currentRow < 0 || currentRow >= table.getRowCount()) {
            return label;
        }

        String maChuyen = String.valueOf(table.getValueAt(currentRow, 0));
        String tenChuyen = String.valueOf(table.getValueAt(currentRow, 1));
        String gaDi = String.valueOf(table.getValueAt(currentRow, 3));
        String gaDen = String.valueOf(table.getValueAt(currentRow, 4));
        String ngayDiText = String.valueOf(table.getValueAt(currentRow, 5));
        String ngayDenDuKien = String.valueOf(table.getValueAt(currentRow, 6));

        if (jRadioButton1.isSelected()) {
            int chon = javax.swing.JOptionPane.showConfirmDialog(
                    jf1.this,
                    "Xác nhận chọn chuyến này?\n"
                    + "Mã chuyến: " + maChuyen
                    + "\nTên chuyến: " + tenChuyen
                    + "\nGa đi: " + gaDi
                    + "\nGa đến: " + gaDen
                    + "\nNgày đi: " + ngayDiText
                    + "\nNgày đến dự kiến: " + ngayDenDuKien,
                    "Xác nhận chọn chuyến",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (chon == javax.swing.JOptionPane.YES_OPTION) {
                luuChuyenDiTuDongDangChon(currentRow);
                maChuyenVeDaChon = null;
                tenChuyenVeDaChon = null;
                gaDiChuyenVe = null;
                gaDenChuyenVe = null;
                dangChonChuyenVe = false;

                javax.swing.SwingUtilities.invokeLater(() -> moManHinhChonToaVaGhe());
            }

            return label;
        }

        if (jRadioButton2.isSelected()) {
            if (!dangChonChuyenVe) {
                int chon = javax.swing.JOptionPane.showConfirmDialog(
                        jf1.this,
                        "Xác nhận chọn CHUYẾN ĐI này?\n"
                        + "Mã chuyến: " + maChuyen
                        + "\nTên chuyến: " + tenChuyen
                        + "\nGa đi: " + gaDi
                        + "\nGa đến: " + gaDen
                        + "\nNgày đi: " + ngayDiText
                        + "\nNgày đến dự kiến: " + ngayDenDuKien,
                        "Xác nhận chọn chuyến đi",
                        javax.swing.JOptionPane.YES_NO_OPTION
                );

                if (chon == javax.swing.JOptionPane.YES_OPTION) {
                        luuChuyenDiTuDongDangChon(currentRow);
                        dangChonChuyenVe = true;
                        hienNutQuayLaiSauKhiChonLan1();

                    final java.time.LocalDate ngayVeFinal =
                            (dcNgayVe.getDate() == null)
                                    ? null
                                    : dcNgayVe.getDate().toInstant()
                                            .atZone(java.time.ZoneId.systemDefault())
                                            .toLocalDate();

                    javax.swing.SwingUtilities.invokeLater(() -> {
                        javax.swing.table.DefaultTableModel model =
                                daoChuyenTau.timChuyenVeLenBang(gaDenChuyenDi, gaDiChuyenDi, ngayVeFinal);

                        jTable2.setModel(model);
                        cauHinhLaiBangChuyen();

                        if (model.getRowCount() == 0) {
                            anNutQuayLaiSauKhiChonLan1();
                            dangChonChuyenVe = false;
                            maChuyenDiDaChon = null;
                            tenChuyenDiDaChon = null;
                            gaDiChuyenDi = null;
                            gaDenChuyenDi = null;

                            javax.swing.JOptionPane.showMessageDialog(
                                    jf1.this,
                                    "Không tìm thấy chuyến về phù hợp."
                            );
                        }
                    });
                }

                return label;
            }

            int chon = javax.swing.JOptionPane.showConfirmDialog(
                    jf1.this,
                    "Xác nhận chọn CHUYẾN VỀ này?\n"
                    + "Mã chuyến: " + maChuyen
                    + "\nTên chuyến: " + tenChuyen
                    + "\nGa đi: " + gaDi
                    + "\nGa đến: " + gaDen
                    + "\nNgày đi: " + ngayDiText
                    + "\nNgày đến dự kiến: " + ngayDenDuKien,
                    "Xác nhận chọn chuyến về",
                    javax.swing.JOptionPane.YES_NO_OPTION
            );

            if (chon == javax.swing.JOptionPane.YES_OPTION) {
                luuChuyenVeTuDongDangChon(currentRow);
                dangChonChuyenVe = false;

                javax.swing.SwingUtilities.invokeLater(() -> moManHinhChonToaVaGhe());
            }
        }

        return label;
    }

}
   
    
   // --------------------------NGÀY ĐI NGÀY VỀ---------------------//
   private Date boThoiGian(Date date) {
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.setTime(date);
    cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
    cal.set(java.util.Calendar.MINUTE, 0);
    cal.set(java.util.Calendar.SECOND, 0);
    cal.set(java.util.Calendar.MILLISECOND, 0);
    return cal.getTime();
}
   private void kiemTraNgayVe() {
    Date ngayDi = dcNgayDi.getDate();
    Date ngayVe = dcNgayVe.getDate();

    if (ngayDi == null) {
        return;
    }

    ngayDi = boThoiGian(ngayDi);

    if (ngayVe == null) {
        dcNgayVe.setDate(ngayDi);
        return;
    }

    ngayVe = boThoiGian(ngayVe);

    if (ngayVe.before(ngayDi)) {
//        javax.swing.JOptionPane.showMessageDialog(
//            this,
//            "Ngày về không được nhỏ hơn ngày đi."
//        );
        dcNgayVe.setDate(ngayDi);
    }
}
    private void kiemTraNgayDi() {
    Date homNay = boThoiGian(new Date());
    Date ngayDi = dcNgayDi.getDate();

    if (ngayDi == null) {
        dcNgayDi.setDate(homNay);
        return;
    }

    ngayDi = boThoiGian(ngayDi);

    if (ngayDi.before(homNay)) {
        javax.swing.JOptionPane.showMessageDialog(
            this,
            "Ngày đi không được nhỏ hơn ngày hiện tại."
        );
        dcNgayDi.setDate(homNay);
    }

    // nếu đang chọn khứ hồi thì kiểm tra lại ngày về
    if (jRadioButton2.isSelected()) {
        kiemTraNgayVe();
    }
}
    private String boDau(String s) {
    String temp = java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFD);
    return temp.replaceAll("\\p{M}", "").toLowerCase();
}
    private void moGDThongTinNhanVien() {
    if (maNhanVienDangNhap == null || maNhanVienDangNhap.trim().isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Không có mã nhân viên đăng nhập.");
        return;
    }

    DAO_NHANVIEN dao = new DAO_NHANVIEN();
    NhanVien nv = dao.layNhanVienTheoMa(maNhanVienDangNhap);
    //String tenCaLam = dao.layTenCaLamTheoMaNhanVien(maNhanVienDangNhap);

    if (nv != null) {
        jLabel23.setText(nv.getMaNhanVien());                    // Mã nhân viên
        jLabel25.setText(nv.getChucVu());                        // Chức vụ
        jLabel3.setText(nv.getHoTenNV());                        // Tên nhân viên
        jLabel18.setText(nv.getNgaySinh() != null ? nv.getNgaySinh().toString() : "");
        jLabel27.setText(nv.getGioiTinh());                      // Giới tính
        jLabel20.setText(nv.getCccd());                          // CCCD
        jLabel22.setText(nv.getSdt());                           // SĐT
        jLabel29.setText(nv.getNgayVaoLam() != null ? nv.getNgayVaoLam().toString() : "");
        jLabel31.setText(nv.isTrangThaiNV() ? "Đang làm" : "Đã nghỉ");

        hienThiAnhNhanVien(nv.getAnhNhanVien());

        // nếu muốn đồng bộ luôn ô thông tin nhanh bên trái
        hienten.setText(nv.getHoTenNV());
        hienma.setText(nv.getMaNhanVien());
        hienchucvu.setText(nv.getChucVu());
        capNhatHienThiCaBenTrai();
    } else {
        javax.swing.JOptionPane.showMessageDialog(this, "Không tìm thấy thông tin nhân viên.");
    }

    java.awt.CardLayout cl = (java.awt.CardLayout) jPanelNoi_Dung.getLayout();
    cl.show(jPanelNoi_Dung, "card3");
}
    private void moGDTrangChu() {
    java.awt.CardLayout cl = (java.awt.CardLayout) jPanelNoi_Dung.getLayout();
    cl.show(jPanelNoi_Dung, "card2");
}
    private void chinhPanelAnhNhanVien() {
    jPanel2.removeAll();
    jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

    lblAnhNhanVien = new javax.swing.JLabel();
    lblAnhNhanVien.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
    lblAnhNhanVien.setPreferredSize(new java.awt.Dimension(180, 180));
    lblAnhNhanVien.setMaximumSize(new java.awt.Dimension(180, 180));
    lblAnhNhanVien.setMinimumSize(new java.awt.Dimension(180, 180));
    lblAnhNhanVien.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    lblAnhNhanVien.setText("Chưa có ảnh");

    jButton14.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

    jPanel2.add(lblAnhNhanVien);
    jPanel2.add(javax.swing.Box.createVerticalStrut(15));
    jPanel2.add(jButton14);
}
    private void chinhPanelThongTinCaNhan() {
    // panel chứa ảnh + panel thông tin nằm ngang
    pnCenter_nv.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));

    // panel thông tin bên phải
    jPanel19.setLayout(new javax.swing.BoxLayout(jPanel19, javax.swing.BoxLayout.Y_AXIS));
    jPanel19.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 25, 15, 15));

    // tạo khoảng cách giữa panel ảnh (jPanel2) và panel thông tin (jPanel19)
    jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 20));

    javax.swing.JPanel[] dsDong = {jPanel20, jPanel21, jPanel22, jPanel23};

    for (javax.swing.JPanel p : dsDong) {
        p.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
        p.setLayout(new javax.swing.BoxLayout(p, javax.swing.BoxLayout.X_AXIS));
        p.setBorder(javax.swing.BorderFactory.createCompoundBorder(
    javax.swing.BorderFactory.createLineBorder(new java.awt.Color(220, 220, 220)),
    javax.swing.BorderFactory.createEmptyBorder(10, 8, 10, 8)
));
        p.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 45));
    }

    // kích thước chuẩn cho label tiêu đề
    java.awt.Dimension sizeLabelNgan = new java.awt.Dimension(100, 25);
    java.awt.Dimension sizeLabelDai  = new java.awt.Dimension(120, 25);

    // Dòng 1
    jLabel4.setPreferredSize(sizeLabelNgan);
    jLabel4.setMinimumSize(sizeLabelNgan);
    jLabel4.setMaximumSize(sizeLabelNgan);

    jLabel24.setPreferredSize(sizeLabelNgan);
    jLabel24.setMinimumSize(sizeLabelNgan);
    jLabel24.setMaximumSize(sizeLabelNgan);

    // Dòng 2
    jLabel2.setPreferredSize(sizeLabelDai);
    jLabel2.setMinimumSize(sizeLabelDai);
    jLabel2.setMaximumSize(sizeLabelDai);

    jLabel5.setPreferredSize(sizeLabelNgan);
    jLabel5.setMinimumSize(sizeLabelNgan);
    jLabel5.setMaximumSize(sizeLabelNgan);

    jLabel26.setPreferredSize(sizeLabelNgan);
    jLabel26.setMinimumSize(sizeLabelNgan);
    jLabel26.setMaximumSize(sizeLabelNgan);

    // Dòng 3
    jLabel19.setPreferredSize(sizeLabelNgan);
    jLabel19.setMinimumSize(sizeLabelNgan);
    jLabel19.setMaximumSize(sizeLabelNgan);

    jLabel21.setPreferredSize(sizeLabelDai);
    jLabel21.setMinimumSize(sizeLabelDai);
    jLabel21.setMaximumSize(sizeLabelDai);

    // Dòng 4
    jLabel28.setPreferredSize(sizeLabelDai);
    jLabel28.setMinimumSize(sizeLabelDai);
    jLabel28.setMaximumSize(sizeLabelDai);

    jLabel30.setPreferredSize(sizeLabelNgan);
    jLabel30.setMinimumSize(sizeLabelNgan);
    jLabel30.setMaximumSize(sizeLabelNgan);

    // làm đậm label tiêu đề
    java.awt.Font fontLabel = new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12);
    jLabel4.setFont(fontLabel);
    jLabel24.setFont(fontLabel);
    jLabel2.setFont(fontLabel);
    jLabel5.setFont(fontLabel);
    jLabel26.setFont(fontLabel);
    jLabel19.setFont(fontLabel);
    jLabel21.setFont(fontLabel);
    jLabel28.setFont(fontLabel);
    jLabel30.setFont(fontLabel);

    // font value
    java.awt.Font fontValue = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);
    jLabel23.setFont(fontValue);
    jLabel25.setFont(fontValue);
    jLabel3.setFont(fontValue);
    jLabel18.setFont(fontValue);
    jLabel27.setFont(fontValue);
    jLabel20.setFont(fontValue);
    jLabel22.setFont(fontValue);
    jLabel29.setFont(fontValue);
    jLabel31.setFont(fontValue);

    // ====== Dòng 1 ======
    jPanel20.removeAll();
    jPanel20.add(jLabel4);
    jPanel20.add(javax.swing.Box.createHorizontalStrut(10));
    jPanel20.add(jLabel23);
    jPanel20.add(javax.swing.Box.createHorizontalStrut(40));
    jPanel20.add(jLabel24);
    jPanel20.add(javax.swing.Box.createHorizontalStrut(10));
    jPanel20.add(jLabel25);

    // ====== Dòng 2 ======
    jPanel21.removeAll();
    jPanel21.add(jLabel2);
    jPanel21.add(javax.swing.Box.createHorizontalStrut(10));
    jPanel21.add(jLabel3);
    jPanel21.add(javax.swing.Box.createHorizontalStrut(40));
    jPanel21.add(jLabel5);
    jPanel21.add(javax.swing.Box.createHorizontalStrut(10));
    jPanel21.add(jLabel18);
    jPanel21.add(javax.swing.Box.createHorizontalStrut(40));
    jPanel21.add(jLabel26);
    jPanel21.add(javax.swing.Box.createHorizontalStrut(10));
    jPanel21.add(jLabel27);

    // ====== Dòng 3 ======
    jPanel22.removeAll();
    jPanel22.add(jLabel19);
    jPanel22.add(javax.swing.Box.createHorizontalStrut(10));
    jPanel22.add(jLabel20);
    jPanel22.add(javax.swing.Box.createHorizontalStrut(40));
    jPanel22.add(jLabel21);
    jPanel22.add(javax.swing.Box.createHorizontalStrut(10));
    jPanel22.add(jLabel22);

    // ====== Dòng 4 ======
    jPanel23.removeAll();
    jPanel23.add(jLabel28);
    jPanel23.add(javax.swing.Box.createHorizontalStrut(10));
    jPanel23.add(jLabel29);
    jPanel23.add(javax.swing.Box.createHorizontalStrut(40));
    jPanel23.add(jLabel30);
    jPanel23.add(javax.swing.Box.createHorizontalStrut(10));
    jPanel23.add(jLabel31);

    // làm mới jPanel19 theo kiểu các box cách nhau đều
    jPanel19.removeAll();
    jPanel19.add(jPanel20);
    jPanel19.add(javax.swing.Box.createVerticalStrut(12));
    jPanel19.add(jPanel21);
    jPanel19.add(javax.swing.Box.createVerticalStrut(12));
    jPanel19.add(jPanel22);
    jPanel19.add(javax.swing.Box.createVerticalStrut(12));
    jPanel19.add(jPanel23);

    jPanel19.revalidate();
    jPanel19.repaint();
    pnCenter_nv.revalidate();
    pnCenter_nv.repaint();
}
    private void phanQuyenTheoVaiTro() {
    if ("NHAN_VIEN".equalsIgnoreCase(vaiTroDangNhap)) {
        jButton5.setVisible(false);   // Quản lí khuyến mãi
        jButton9.setVisible(false);   // Quản lí nhân viên
        jButton11.setVisible(false);  // Quản lí chuyến tàu
        jButton12.setVisible(false);  // Quản lí tàu
    }

    jPanel4.revalidate();
    jPanel4.repaint();
}
    private void troVeManHinhLogin() {
    if (!xacNhanDangXuat()) {
        return;
    }

    // Nếu đang giữ ghế nhưng chưa thanh toán thì mở ghế ra trước khi đăng xuất
    huyPhienBanVeKhiDangXuat();
    DAO_TAIKHOAN daoTK = new DAO_TAIKHOAN();
    daoTK.capNhatTrangThaiDangNhap(maNhanVienDangNhap, false);
    new login().setVisible(true);
    this.dispose();
}
    private boolean xacNhanDangXuat() {
    Object[] luaChon = {"Đăng xuất", "Hủy"};

    String noiDung;

    if (daMoCa) {
        noiDung =
                "Hiện tại bạn chưa kết ca.\n"
                + "Nếu đăng xuất bây giờ, phiên làm việc vẫn còn đang mở.\n\n"
                + "Bạn có muốn đăng xuất không?";
    } else {
        noiDung = "Bạn có muốn đăng xuất không?";
    }

    int chon = javax.swing.JOptionPane.showOptionDialog(
            this,
            noiDung,
            daMoCa ? "Chưa kết ca" : "Xác nhận đăng xuất",
            javax.swing.JOptionPane.YES_NO_OPTION,
            daMoCa
                    ? javax.swing.JOptionPane.WARNING_MESSAGE
                    : javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,
            luaChon,
            luaChon[1]
    );

    return chon == 0;
}
    private void hienThongTinNhanVienDangNhap() {
    if (maNhanVienDangNhap == null || maNhanVienDangNhap.isEmpty()) {
        return;
    }

    DAO_NHANVIEN dao = new DAO_NHANVIEN();
    NhanVien nv = dao.layNhanVienTheoMa(maNhanVienDangNhap);

    if (nv != null) {
    jLabel23.setText(nv.getMaNhanVien());
    jLabel25.setText(nv.getChucVu());
    jLabel3.setText(nv.getHoTenNV());
    jLabel18.setText(nv.getNgaySinh() != null ? nv.getNgaySinh().toString() : "");
    jLabel27.setText(nv.getGioiTinh());
    jLabel20.setText(nv.getCccd());
    jLabel22.setText(nv.getSdt());
    jLabel29.setText(nv.getNgayVaoLam() != null ? nv.getNgayVaoLam().toString() : "");
    jLabel31.setText(nv.isTrangThaiNV() ? "Đang làm" : "Đã nghỉ");

    hienThiAnhNhanVien(nv.getAnhNhanVien());

    hienten.setText(nv.getHoTenNV());
    hienma.setText(nv.getMaNhanVien());
    hienchucvu.setText(nv.getChucVu());

    // Không lấy ca phân công nữa, giữ đúng trạng thái hiện tại
    capNhatHienThiCaBenTrai();
}
}
    private void hienThiAnhNhanVien(String duongDanAnh) {
    if (duongDanAnh == null || duongDanAnh.trim().isEmpty()) {
        lblAnhNhanVien.setIcon(null);
        lblAnhNhanVien.setText("Chưa có ảnh");
        return;
    }

    try {
        java.awt.Image img;

        java.io.File file = new java.io.File(duongDanAnh);
        if (file.exists()) {
            img = javax.imageio.ImageIO.read(file);
        } else {
            java.net.URL url = getClass().getResource(duongDanAnh);
            if (url == null) {
                lblAnhNhanVien.setIcon(null);
                lblAnhNhanVien.setText("Không tìm thấy ảnh");
                return;
            }
            img = javax.imageio.ImageIO.read(url);
        }

        java.awt.Image scaled = img.getScaledInstance(180, 180, java.awt.Image.SCALE_SMOOTH);
        lblAnhNhanVien.setText("");
        lblAnhNhanVien.setIcon(new javax.swing.ImageIcon(scaled));

    } catch (IOException e) {
        lblAnhNhanVien.setIcon(null);
        lblAnhNhanVien.setText("Lỗi tải ảnh");
    }
}
    private void moHopThoaiDoiMatKhau() {
    if (maNhanVienDangNhap == null || maNhanVienDangNhap.trim().isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Không xác định được tài khoản đang đăng nhập.");
        return;
    }

    javax.swing.JDialog dialog = new javax.swing.JDialog(this, "Đổi mật khẩu", true);
    dialog.setSize(420, 280);
    dialog.setLocationRelativeTo(this);
    dialog.setResizable(false);

    javax.swing.JPanel pnMain = new javax.swing.JPanel(new java.awt.BorderLayout(10, 10));
    pnMain.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

    javax.swing.JLabel lblTitle = new javax.swing.JLabel("ĐỔI MẬT KHẨU");
    lblTitle.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 18));
    lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
    pnMain.add(lblTitle, java.awt.BorderLayout.NORTH);

    javax.swing.JPanel pnForm = new javax.swing.JPanel(new java.awt.GridBagLayout());
    java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
    gbc.insets = new java.awt.Insets(5, 5, 5, 5);
    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;

    javax.swing.JLabel lblMatKhauCu = new javax.swing.JLabel("Mật khẩu hiện tại:");
    javax.swing.JLabel lblMatKhauMoi = new javax.swing.JLabel("Mật khẩu mới:");
    javax.swing.JLabel lblXacNhan = new javax.swing.JLabel("Xác nhận mật khẩu mới:");

    javax.swing.JPasswordField txtMatKhauCu = new javax.swing.JPasswordField(20);
    javax.swing.JPasswordField txtMatKhauMoi = new javax.swing.JPasswordField(20);
    javax.swing.JPasswordField txtXacNhan = new javax.swing.JPasswordField(20);

    gbc.gridx = 0; gbc.gridy = 0;
    pnForm.add(lblMatKhauCu, gbc);
    gbc.gridx = 1;
    pnForm.add(txtMatKhauCu, gbc);

    gbc.gridx = 0; gbc.gridy = 1;
    pnForm.add(lblMatKhauMoi, gbc);
    gbc.gridx = 1;
    pnForm.add(txtMatKhauMoi, gbc);

    gbc.gridx = 0; gbc.gridy = 2;
    pnForm.add(lblXacNhan, gbc);
    gbc.gridx = 1;
    pnForm.add(txtXacNhan, gbc);

    pnMain.add(pnForm, java.awt.BorderLayout.CENTER);

    javax.swing.JPanel pnButton = new javax.swing.JPanel(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 15, 5));
    javax.swing.JButton btnXacNhan = new javax.swing.JButton("Xác nhận");
    javax.swing.JButton btnHuy = new javax.swing.JButton("Hủy");

    pnButton.add(btnXacNhan);
    pnButton.add(btnHuy);
    pnMain.add(pnButton, java.awt.BorderLayout.SOUTH);

    btnHuy.addActionListener(e -> {
    dialog.dispose();

    int xacNhan = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn hủy thanh toán không?\n"
            + "Các ghế đang giữ sẽ được mở lại.",
            "Xác nhận hủy",
            javax.swing.JOptionPane.YES_NO_OPTION
    );

    if (xacNhan == javax.swing.JOptionPane.YES_OPTION) {
        huyGiuChoHienTai();
        daoVeTau.huyPhienDatVe(maPhienGiuCho);
        resetThongTinDatVe();
        moGDTrangChu();
    }
});

    btnXacNhan.addActionListener(e -> {
        String matKhauCu = new String(txtMatKhauCu.getPassword()).trim();
        String matKhauMoi = new String(txtMatKhauMoi.getPassword()).trim();
        String xacNhanMoi = new String(txtXacNhan.getPassword()).trim();

        if (matKhauCu.isEmpty() || matKhauMoi.isEmpty() || xacNhanMoi.isEmpty()) {
            javax.swing.JOptionPane.showMessageDialog(dialog, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        if (!matKhauMoi.equals(xacNhanMoi)) {
            javax.swing.JOptionPane.showMessageDialog(dialog, "Xác nhận mật khẩu mới không khớp.");
            txtXacNhan.requestFocus();
            return;
        }

        if (matKhauMoi.length() < 6) {
            javax.swing.JOptionPane.showMessageDialog(dialog, "Mật khẩu mới phải có ít nhất 6 ký tự.");
            txtMatKhauMoi.requestFocus();
            return;
        }

        if (matKhauCu.equals(matKhauMoi)) {
            javax.swing.JOptionPane.showMessageDialog(dialog, "Mật khẩu mới phải khác mật khẩu hiện tại.");
            txtMatKhauMoi.requestFocus();
            return;
        }

        DAO_TAIKHOAN daoTK = new DAO_TAIKHOAN();

        boolean dungMatKhauCu = daoTK.kiemTraMatKhauHienTai(maNhanVienDangNhap, matKhauCu);
        if (!dungMatKhauCu) {
            javax.swing.JOptionPane.showMessageDialog(dialog, "Mật khẩu hiện tại không đúng.");
            txtMatKhauCu.requestFocus();
            return;
        }

        boolean doiThanhCong = daoTK.capNhatMatKhau(maNhanVienDangNhap, matKhauMoi);
        if (doiThanhCong) {
            javax.swing.JOptionPane.showMessageDialog(dialog, "Đổi mật khẩu thành công.");
            dialog.dispose();
        } else {
            javax.swing.JOptionPane.showMessageDialog(dialog, "Đổi mật khẩu thất bại.");
        }
    });

    dialog.setContentPane(pnMain);
    dialog.setVisible(true);
}
    



private void cauHinhLaiBangChuyen() {
    int lastCol = jTable2.getColumnModel().getColumnCount() - 1;
    jTable2.getColumnModel().getColumn(lastCol).setCellRenderer(new ButtonRenderer());
    jTable2.getColumnModel().getColumn(lastCol).setCellEditor(new ButtonEditor(jTable2));
    jTable2.setRowHeight(30);

    jTable2.getColumnModel().getColumn(0).setPreferredWidth(90);
    jTable2.getColumnModel().getColumn(1).setPreferredWidth(140);
    jTable2.getColumnModel().getColumn(2).setPreferredWidth(90);
    jTable2.getColumnModel().getColumn(3).setPreferredWidth(100);
    jTable2.getColumnModel().getColumn(4).setPreferredWidth(100);
    jTable2.getColumnModel().getColumn(5).setPreferredWidth(140);
    jTable2.getColumnModel().getColumn(6).setPreferredWidth(160);
    jTable2.getColumnModel().getColumn(7).setPreferredWidth(100);
    jTable2.getColumnModel().getColumn(8).setPreferredWidth(80);
    jTable2.getColumnModel().getColumn(9).setPreferredWidth(100);

    jTable2.getTableHeader().setReorderingAllowed(false);
    jTable2.getTableHeader().setResizingAllowed(false);
}

private void timChuyenTauTuDAO() {
    anNutQuayLaiSauKhiChonLan1();
    dangChonChuyenVe = false;

    maChuyenDiDaChon = null;
    tenChuyenDiDaChon = null;
    gaDiChuyenDi = null;
    gaDenChuyenDi = null;

    maChuyenVeDaChon = null;
    tenChuyenVeDaChon = null;
    gaDiChuyenVe = null;
    gaDenChuyenVe = null;
    
    tenTauChuyenDi = null;
    tenTauChuyenVe = null;
    String gaDi = jtgadi.getText().trim();

    javax.swing.JTextField editorGaDen =
            (javax.swing.JTextField) jComboBox2.getEditor().getEditorComponent();
    String gaDen = editorGaDen.getText().trim();

    if (gaDen.isEmpty() || gaDen.equalsIgnoreCase("Chọn ga đến...")) {
        javax.swing.JOptionPane.showMessageDialog(this, "Vui lòng chọn ga đến.");
        return;
    }

    java.time.LocalDate ngayDi = null;
    if (dcNgayDi.getDate() != null) {
        ngayDi = dcNgayDi.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
    }

    javax.swing.table.DefaultTableModel model =
            daoChuyenTau.timChuyenLenBang(gaDi, gaDen, ngayDi);

    jTable2.setModel(model);
    cauHinhLaiBangChuyen();

    if (model.getRowCount() == 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "Không tìm thấy chuyến phù hợp.");
    }
}
private void lamRongBangBanDau() {
    ((javax.swing.table.DefaultTableModel) jTable2.getModel()).setRowCount(0);
}
private void luuChuyenDiTuDongDangChon(int row) {
    xoaLuaChonGheChieuDi();
    maChuyenDiDaChon = jTable2.getValueAt(row, 0).toString();
    tenChuyenDiDaChon = jTable2.getValueAt(row, 1).toString();
    tenTauChuyenDi = jTable2.getValueAt(row, 2).toString();
    gaDiChuyenDi = jTable2.getValueAt(row, 3).toString();
    gaDenChuyenDi = jTable2.getValueAt(row, 4).toString();
}

private void luuChuyenVeTuDongDangChon(int row) {
    maChuyenVeDaChon = jTable2.getValueAt(row, 0).toString();
    tenChuyenVeDaChon = jTable2.getValueAt(row, 1).toString();
    tenTauChuyenVe = jTable2.getValueAt(row, 2).toString();
    gaDiChuyenVe = jTable2.getValueAt(row, 3).toString();
    gaDenChuyenVe = jTable2.getValueAt(row, 4).toString();
}


private void moManHinhChonToaVaGheNguoc() {
    // Khởi tạo bố cục đồng bộ với chiều đi (chỉ chạy 1 lần)
    if (!daKhoiTaoBoCucChieuVe) {
        khoiTaoBoCucChieuVeGiongChieuDi();
        daKhoiTaoBoCucChieuVe = true;
    }

    jLabel36.setText(tenTauChuyenVe != null ? tenTauChuyenVe : "");
    jLabel37.setText(
            (gaDiChuyenVe != null ? gaDiChuyenVe : "")
            + " - "
            + (gaDenChuyenVe != null ? gaDenChuyenVe : "")
    );

    if (dsGheChonVe == null || dsGheChonVe.isEmpty()) {
        khoiTaoDanhSachHanhKhachCanChonGheVe();
    }

    hienThiDanhSachToaChieuVe();
    capNhatPanelThongTinVeChieuVe();

    java.awt.CardLayout cl = (java.awt.CardLayout) jPanelNoi_Dung.getLayout();
    cl.show(jPanelNoi_Dung, "card6");
}
/**
 * Khởi tạo bố cục cho jPanel26 (danh sách toa) và jPanel29 (danh sách ghế)
 * của chiều đi - đối xứng với chiều về.
 */
private void khoiTaoBoCucChieuDi() {
    // ===== jPanel26: danh sách toa tàu chiều đi =====
    jPanel26.removeAll();
    jPanel26.setLayout(new java.awt.BorderLayout());

    javax.swing.JPanel pnlHeaderToaDi = new javax.swing.JPanel(
            new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 6, 6)
    );
    lblSoLuongToaDi = new javax.swing.JLabel("Số lượng toa: 0");
    lblSoLuongToaDi.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
    pnlHeaderToaDi.add(lblSoLuongToaDi);

    pnlContainerToaDi = new javax.swing.JPanel(new java.awt.BorderLayout());

    jPanel26.add(pnlHeaderToaDi, java.awt.BorderLayout.NORTH);
    jPanel26.add(pnlContainerToaDi, java.awt.BorderLayout.CENTER);

    // ===== jPanel29: danh sách ghế chiều đi =====
    jPanel29.removeAll();
    jPanel29.setLayout(new java.awt.BorderLayout());

    javax.swing.JPanel pnlHeaderGheDi = new javax.swing.JPanel();
    pnlHeaderGheDi.setLayout(new javax.swing.BoxLayout(pnlHeaderGheDi, javax.swing.BoxLayout.X_AXIS));
    pnlHeaderGheDi.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8));

    lblToaTextDi = new javax.swing.JLabel("Toa ");
    lblToaTextDi.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

    lblTenToaDi = new javax.swing.JLabel("");
    lblTenToaDi.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

    lblLoaiGheToaDi = new javax.swing.JLabel("");
    lblLoaiGheToaDi.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

    pnlHeaderGheDi.add(lblToaTextDi);
    pnlHeaderGheDi.add(lblTenToaDi);
    pnlHeaderGheDi.add(lblLoaiGheToaDi);
    pnlHeaderGheDi.add(javax.swing.Box.createHorizontalGlue());

    pnlContainerGheDi = new javax.swing.JPanel(new java.awt.BorderLayout());

    jPanel29.add(pnlHeaderGheDi, java.awt.BorderLayout.NORTH);
    jPanel29.add(pnlContainerGheDi, java.awt.BorderLayout.CENTER);

    jPanel26.revalidate();
    jPanel26.repaint();
    jPanel29.revalidate();
    jPanel29.repaint();
}
/**
 * Khởi tạo bố cục cho jPanel28 (danh sách toa) và jPanel30 (danh sách ghế)
 * của chiều về sao cho cấu trúc giống hệt chiều đi.
 */
private void khoiTaoBoCucChieuVeGiongChieuDi() {
    // ===== jPanel28: danh sách toa tàu chiều về =====
    jPanel28.removeAll();
    jPanel28.setLayout(new java.awt.BorderLayout());

    javax.swing.JPanel pnlHeaderToaVe = new javax.swing.JPanel(
            new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 6, 6)
    );
    lblSoLuongToaVe = new javax.swing.JLabel("Số lượng toa: 0");
    lblSoLuongToaVe.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
    pnlHeaderToaVe.add(lblSoLuongToaVe);

    pnlContainerToaVe = new javax.swing.JPanel(new java.awt.BorderLayout());

    jPanel28.add(pnlHeaderToaVe, java.awt.BorderLayout.NORTH);
    jPanel28.add(pnlContainerToaVe, java.awt.BorderLayout.CENTER);

    // ===== jPanel30: danh sách ghế chiều về =====
    jPanel30.removeAll();
    jPanel30.setLayout(new java.awt.BorderLayout());

    javax.swing.JPanel pnlHeaderGheVe = new javax.swing.JPanel();
    pnlHeaderGheVe.setLayout(new javax.swing.BoxLayout(pnlHeaderGheVe, javax.swing.BoxLayout.X_AXIS));
    pnlHeaderGheVe.setBorder(javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8));

    lblToaTextVe = new javax.swing.JLabel("Toa ");
    lblToaTextVe.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

    lblTenToaVe = new javax.swing.JLabel("");
    lblTenToaVe.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

    lblLoaiGheToaVe = new javax.swing.JLabel("");
    lblLoaiGheToaVe.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

    pnlHeaderGheVe.add(lblToaTextVe);
    pnlHeaderGheVe.add(lblTenToaVe);
    pnlHeaderGheVe.add(lblLoaiGheToaVe);
    pnlHeaderGheVe.add(javax.swing.Box.createHorizontalGlue());

    pnlContainerGheVe = new javax.swing.JPanel(new java.awt.BorderLayout());

    jPanel30.add(pnlHeaderGheVe, java.awt.BorderLayout.NORTH);
    jPanel30.add(pnlContainerGheVe, java.awt.BorderLayout.CENTER);

    jPanel28.revalidate();
    jPanel28.repaint();
    jPanel30.revalidate();
    jPanel30.repaint();
}

/**
 * Đồng bộ style nút chiều về (jButton17/20/21) giống chiều đi (jButton19/18/16).
 */
private void dongBoStyleNutChieuVeVoiChieuDi() {
    // jButton17 = Xác nhận chiều về -> xanh lá
    jButton17.setBackground(new java.awt.Color(102, 255, 102));
    jButton17.setForeground(new java.awt.Color(242, 242, 242));
    jButton17.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
    jButton17.setOpaque(true);
    jButton17.setBorderPainted(true);

    // jButton20 = Quay lại
    jButton20.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

    // jButton21 = Hủy chiều về -> đỏ
    jButton21.setBackground(new java.awt.Color(255, 0, 51));
    jButton21.setForeground(new java.awt.Color(242, 242, 242));
    jButton21.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));
    jButton21.setOpaque(true);
    jButton21.setBorderPainted(true);

    jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 12, 8));
}

private void chinhPanelSoLuongVeHangNgang() {
    jPanel39.removeAll();
    jPanel39.setLayout(new javax.swing.BoxLayout(jPanel39, javax.swing.BoxLayout.X_AXIS));
    jPanel39.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
    jPanel39.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createTitledBorder(
                    javax.swing.BorderFactory.createEtchedBorder(),
                    "Số lượng hành khách",
                    javax.swing.border.TitledBorder.LEFT,
                    javax.swing.border.TitledBorder.TOP
            ),
            javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
    ));

    txtNguoiLon = jTextField1;
    txtTreEm = new javax.swing.JTextField("0");
    txtNguoiCaoTuoi = new javax.swing.JTextField("0");
    txtSinhVien = new javax.swing.JTextField("0");

    txtNguoiLon.setText("1");

    chiChoNhapSo(txtNguoiLon);
    chiChoNhapSo(txtTreEm);
    chiChoNhapSo(txtNguoiCaoTuoi);
    chiChoNhapSo(txtSinhVien);

    lblTongSoVe = new javax.swing.JLabel("Tổng số vé: 1");
    lblTongSoVe.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));

    jPanel39.add(taoOLoaiVeNgang("Người lớn", txtNguoiLon, false));
    jPanel39.add(javax.swing.Box.createHorizontalStrut(10));

    jPanel39.add(taoOLoaiVeNgang("Trẻ em (-25%)", txtTreEm, false));
    jPanel39.add(javax.swing.Box.createHorizontalStrut(10));

    jPanel39.add(taoOLoaiVeNgang("Người cao tuổi (-15%)", txtNguoiCaoTuoi, false));
    jPanel39.add(javax.swing.Box.createHorizontalStrut(10));

    jPanel39.add(taoOLoaiVeNgang("Sinh viên (-10%)", txtSinhVien, false));
    jPanel39.add(javax.swing.Box.createHorizontalGlue());
    jPanel39.add(lblTongSoVe);

    capNhatTongSoVe();
    jPanel39.revalidate();
    jPanel39.repaint();
}

private javax.swing.JPanel taoOLoaiVeNgang(String tenLoai, javax.swing.JTextField txtSoLuong, boolean batBuocToiThieu1) {
    javax.swing.JPanel o = new javax.swing.JPanel();
    o.setLayout(new javax.swing.BoxLayout(o, javax.swing.BoxLayout.Y_AXIS));
    o.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 210, 210)),
            javax.swing.BorderFactory.createEmptyBorder(6, 8, 6, 8)
    ));

    javax.swing.JLabel lblLoai = new javax.swing.JLabel(tenLoai);
    lblLoai.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
    lblLoai.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

    javax.swing.JPanel pnDieuKhien = new javax.swing.JPanel();
    pnDieuKhien.setLayout(new javax.swing.BoxLayout(pnDieuKhien, javax.swing.BoxLayout.X_AXIS));

    javax.swing.JButton btnGiam = new javax.swing.JButton("-");
    javax.swing.JButton btnTang = new javax.swing.JButton("+");

    java.awt.Dimension sizeBtn = new java.awt.Dimension(45, 30);
    btnGiam.setPreferredSize(sizeBtn);
    btnGiam.setMaximumSize(sizeBtn);
    btnGiam.setMinimumSize(sizeBtn);

    btnTang.setPreferredSize(sizeBtn);
    btnTang.setMaximumSize(sizeBtn);
    btnTang.setMinimumSize(sizeBtn);

    java.awt.Dimension sizeTxt = new java.awt.Dimension(45, 30);
    txtSoLuong.setHorizontalAlignment(javax.swing.JTextField.CENTER);
    txtSoLuong.setPreferredSize(sizeTxt);
    txtSoLuong.setMaximumSize(sizeTxt);
    txtSoLuong.setMinimumSize(sizeTxt);

    btnGiam.addActionListener(e -> thayDoiSoLuong(txtSoLuong, -1, batBuocToiThieu1));
    btnTang.addActionListener(e -> thayDoiSoLuong(txtSoLuong, 1, batBuocToiThieu1));

    txtSoLuong.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
    private void capNhatSau() {
        javax.swing.SwingUtilities.invokeLater(() -> capNhatTongSoVe());
    }

    @Override
    public void insertUpdate(javax.swing.event.DocumentEvent e) {
        capNhatSau();
    }

    @Override
    public void removeUpdate(javax.swing.event.DocumentEvent e) {
        capNhatSau();
    }

    @Override
    public void changedUpdate(javax.swing.event.DocumentEvent e) {
        capNhatSau();
    }
});

    pnDieuKhien.add(btnGiam);
    pnDieuKhien.add(javax.swing.Box.createHorizontalStrut(5));
    pnDieuKhien.add(txtSoLuong);
    pnDieuKhien.add(javax.swing.Box.createHorizontalStrut(5));
    pnDieuKhien.add(btnTang);

    o.add(lblLoai);
    o.add(javax.swing.Box.createVerticalStrut(6));
    o.add(pnDieuKhien);

    return o;
}

private void thayDoiSoLuong(javax.swing.JTextField txt, int delta, boolean toiThieu1) {
    int so = laySoLuong(txt);
    so += delta;

    if (toiThieu1) {
        if (so < 1) so = 1;
    } else {
        if (so < 0) so = 0;
    }

    txt.setText(String.valueOf(so));
   
}

private void chiChoNhapSo(javax.swing.JTextField txt) {
    txt.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyTyped(java.awt.event.KeyEvent e) {
            char c = e.getKeyChar();
            if (!Character.isDigit(c)) {
                e.consume();
            }
        }
    });
}

private int laySoLuong(javax.swing.JTextField txt) {
    String s = txt.getText().trim();
    if (s.isEmpty()) return 0;

    try {
        return Integer.parseInt(s);
    } catch (NumberFormatException e) {
        return 0;
    }
}

private void capNhatTongSoVe() {
    int nguoiLon = Math.max(0, laySoLuong(txtNguoiLon));
    int treEm = Math.max(0, laySoLuong(txtTreEm));
    int nguoiCaoTuoi = Math.max(0, laySoLuong(txtNguoiCaoTuoi));
    int sinhVien = Math.max(0, laySoLuong(txtSinhVien));

    int tong = nguoiLon + treEm + nguoiCaoTuoi + sinhVien;

    if (lblTongSoVe != null) {
        lblTongSoVe.setText("Tổng số vé: " + tong);
    }
}
private int laySoTrongTextField(javax.swing.JTextField txt) {
    String s = txt.getText().trim();
    if (s.isEmpty()) return 1;

    try {
        return Integer.parseInt(s);
    } catch (NumberFormatException e) {
        return 1;
    }
}
private void quayLaiDanhSachChuyen() {
    java.awt.CardLayout cl = (java.awt.CardLayout) jPanelNoi_Dung.getLayout();
    cl.show(jPanelNoi_Dung, "card2");
}

private void quayLaiChonChuyenDiKhuHoi() {
    dangChonChuyenVe = false;

    maChuyenDiDaChon = null;
    tenChuyenDiDaChon = null;
    gaDiChuyenDi = null;
    gaDenChuyenDi = null;
    tenTauChuyenDi = null;

    maChuyenVeDaChon = null;
    tenChuyenVeDaChon = null;
    gaDiChuyenVe = null;
    gaDenChuyenVe = null;
    tenTauChuyenVe = null;

    timChuyenTauTuDAO();
    quayLaiDanhSachChuyen();
}
private void xuLyNutQuayLai() {
    java.awt.CardLayout cl = (java.awt.CardLayout) jPanelNoi_Dung.getLayout();

    // Nếu đang ở màn hình chiều về thì quay lại màn hình chiều đi
    if (GD_TOA_GHE_NGUOC.isShowing()) {
        cl.show(jPanelNoi_Dung, "card5");
        return;
    }

    // Nếu đang ở màn hình chọn toa ghế chiều đi
    if (GD_chon_TOA_GHE_XUOI.isShowing()) {
        // Nếu là vé khứ hồi thì quay lại màn hình danh sách chuyến để sửa thông tin
        if (jRadioButton2.isSelected()) {
            quayLaiChonChuyenDiKhuHoi();
        } else {
            // Vé 1 chiều thì quay lại danh sách chuyến bình thường
            xoaLuaChonGheChieuDi();

            maChuyenDiDaChon = null;
            tenChuyenDiDaChon = null;
            gaDiChuyenDi = null;
            gaDenChuyenDi = null;
            tenTauChuyenDi = null;

            quayLaiDanhSachChuyen();
        }
        return;
    }

    // Nếu đang ở bước chọn chuyến về (đã chọn chuyến đi nhưng chưa chọn chuyến về)
    if (jRadioButton2.isSelected() && dangChonChuyenVe) {
        quayLaiChonChuyenDiKhuHoi();
    }
}
private void xacNhanHuyVaVeTrangChu() {
    int chon = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Bạn có chắc muốn hủy thao tác đặt vé không?\n"
            + "Các ghế đang giữ sẽ được mở lại.",
            "Xác nhận hủy",
            javax.swing.JOptionPane.YES_NO_OPTION
    );

    if (chon == javax.swing.JOptionPane.YES_OPTION) {
        // 1. Xóa giữ chỗ ghế
        huyGiuChoHienTai();

        // 2. Đánh dấu phiên đặt vé là đã hủy
        daoVeTau.huyPhienDatVe(maPhienGiuCho);

        // 3. Reset dữ liệu giao diện
        resetThongTinDatVe();

        // 4. Tạo phiên mới cho lần bán vé tiếp theo
        maPhienGiuCho = java.util.UUID.randomUUID().toString();

        // 5. Quay về trang chủ
        moGDTrangChu();
    }
}
private void hienNutQuayLaiSauKhiChonLan1() {
    if (btnQuayLaiSauKhiChonLan1 != null) {
        
        btnQuayLaiSauKhiChonLan1.setEnabled(true);
        jPanel14.revalidate();
        jPanel14.repaint();
    }
}
private void quayLaiChonLan1VaAnNut() {
    dangChonChuyenVe = false;

    maChuyenDiDaChon = null;
    tenChuyenDiDaChon = null;
    gaDiChuyenDi = null;
    gaDenChuyenDi = null;
    tenTauChuyenDi = null;

    maChuyenVeDaChon = null;
    tenChuyenVeDaChon = null;
    gaDiChuyenVe = null;
    gaDenChuyenVe = null;
    tenTauChuyenVe = null;

    String gaDi = jtgadi.getText().trim();

    javax.swing.JTextField editorGaDen =
            (javax.swing.JTextField) jComboBox2.getEditor().getEditorComponent();
    String gaDen = editorGaDen.getText().trim();

    java.time.LocalDate ngayDi = null;
    if (dcNgayDi.getDate() != null) {
        ngayDi = dcNgayDi.getDate().toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
    }

    javax.swing.table.DefaultTableModel model =
            daoChuyenTau.timChuyenLenBang(gaDi, gaDen, ngayDi);

    jTable2.setModel(model);
    cauHinhLaiBangChuyen();

    anNutQuayLaiSauKhiChonLan1();

    java.awt.CardLayout cl = (java.awt.CardLayout) jPanelNoi_Dung.getLayout();
    cl.show(jPanelNoi_Dung, "card2");
}
private void anNutQuayLaiSauKhiChonLan1() {
    if (btnQuayLaiSauKhiChonLan1 != null) {
        
        btnQuayLaiSauKhiChonLan1.setEnabled(false);
        jPanel14.revalidate();
        jPanel14.repaint();
    }
}
private void xuLyTimChuyen() {
    
    if (chuaMoCaThiThongBao("Tìm chuyến")) {
        return;
    }
     if (!kiemTraTongSoVeHopLe()) {
        return;
    }

   
    boolean daChonChuyenDi =
            maChuyenDiDaChon != null && !maChuyenDiDaChon.trim().isEmpty();

    if (jRadioButton2.isSelected() && daChonChuyenDi) {
        int chon = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Bạn đã chọn chuyến đi trước đó.\n"
                + "Nếu tìm chuyến mới, hệ thống sẽ xóa lựa chọn cũ và tìm lại từ đầu.\n"
                + "Bạn có muốn tiếp tục không?",
                "Xác nhận tìm lại chuyến",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (chon != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }
    }

    timChuyenTauTuDAO();
    anNutQuayLaiSauKhiChonLan1();
}
private void resetThongTinDatVe(){ 
        
    dungDemNguocGiuCho();
    khoiPhucTextNutTiepTuc();
    daoVeTau.xoaTatCaGiuChoTheoPhien(maPhienGiuCho);    
        
    anNutQuayLaiSauKhiChonLan1();
    dangChonChuyenVe = false;
    daLuuVeVaoDB = false;
    maChuyenDiDaChon = null;
    tenChuyenDiDaChon = null;
    gaDiChuyenDi = null;
    gaDenChuyenDi = null;
    tenTauChuyenDi = null;

    maChuyenVeDaChon = null;
    tenChuyenVeDaChon = null;
    gaDiChuyenVe = null;
    gaDenChuyenVe = null;
    tenTauChuyenVe = null;

    jLabel33.setText("tentau");
    jLabel34.setText("tengadi-gaden");
    jLabel36.setText("tentau");
    jLabel37.setText("tengadi-gaden");

    jButton19.setText("Xác nhận");

    if (txtNguoiLon != null) txtNguoiLon.setText("1");
    if (txtTreEm != null) txtTreEm.setText("0");
    if (txtNguoiCaoTuoi != null) txtNguoiCaoTuoi.setText("0");
    if (txtSinhVien != null) txtSinhVien.setText("0");
    capNhatTongSoVe();

    jRadioButton1.setSelected(true);
    dcNgayVe.setEnabled(false);
    dcNgayVe.setDate(null);

    Date homNay = boThoiGian(new Date());
    dcNgayDi.setDate(homNay);
    dcNgayDi.setMinSelectableDate(homNay);
    dcNgayVe.setMinSelectableDate(homNay);

    javax.swing.JTextField editorGaDen =
            (javax.swing.JTextField) jComboBox2.getEditor().getEditorComponent();
    editorGaDen.setText("Chọn ga đến...");
    editorGaDen.setForeground(java.awt.Color.GRAY);

    lamRongBangBanDau();
    dsGheChonDi.clear();
    viTriDangChonGheDi = 0;

    if (pnlThongTinVeChieuDi != null) {
    pnlThongTinVeChieuDi.removeAll();
    pnlThongTinVeChieuDi.revalidate();
    pnlThongTinVeChieuDi.repaint();
}

    if (pnlThongTinVeChieuVe != null) {
    pnlThongTinVeChieuVe.removeAll();
    pnlThongTinVeChieuVe.revalidate();
    pnlThongTinVeChieuVe.repaint();
    
    dsGheChonVe.clear();
    viTriDangChonGheVe = 0;
    maToaDangChonVe = null;
    maGheDangChonVe = null;

if (pnlThongTinVeChieuVe != null) {
    pnlThongTinVeChieuVe.removeAll();
    pnlThongTinVeChieuVe.revalidate();
    pnlThongTinVeChieuVe.repaint();
    }
if (jPanel47 != null) {
    jPanel47.removeAll();
    jPanel47.revalidate();
    jPanel47.repaint();
}
    }
}
private void hienThiDanhSachToaChieuDi() {
    if (!daKhoiTaoBoCucChieuDi) {
        khoiTaoBoCucChieuDi();
        daKhoiTaoBoCucChieuDi = true;
    }

    pnlContainerToaDi.removeAll();
    pnlContainerToaDi.setLayout(new java.awt.BorderLayout());

    javax.swing.JPanel pnlToa = new javax.swing.JPanel(
            new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 8)
    );
    pnlToa.setOpaque(false);

    pnlContainerGheDi.removeAll();
    pnlContainerGheDi.revalidate();
    pnlContainerGheDi.repaint();

    lblTenToaDi.setText("");
    lblLoaiGheToaDi.setText("");

    String maTau = jLabel33.getText().trim();

    if (maTau.isEmpty() || maTau.equalsIgnoreCase("tentau")) {
        lblSoLuongToaDi.setText("Chưa có tàu được chọn");
        pnlContainerToaDi.revalidate();
        pnlContainerToaDi.repaint();
        return;
    }

    int soLuongToa = daoToaTau.laySoLuongToaTheoTau(maTau);
    java.util.List<com.mycompany.prj1.entity.ToaTau> dsToa =
            daoToaTau.layDanhSachToaTheoTau(maTau);

    lblSoLuongToaDi.setText("Số lượng toa: " + soLuongToa);

    for (com.mycompany.prj1.entity.ToaTau toa : dsToa) {
        javax.swing.JButton btnToa = new javax.swing.JButton(
                toa.getTenToa() + "-" + toa.getLoaiGhe().getTenLoaiGhe()
        );

        lamDepNutToa(btnToa);

        capNhatMauNutToa(
                btnToa,
                toa,
                maToaDangChonDi,
                dsGheChonDi,
                maChuyenDiDaChon
        );

        btnToa.addActionListener(e -> {
            maToaDangChonDi = toa.getMaToa();

            hienThiDanhSachToaChieuDi();

            lblTenToaDi.setText(toa.getTenToa());
            if (toa.getLoaiGhe() != null) {
                lblLoaiGheToaDi.setText(" - " + toa.getLoaiGhe().getTenLoaiGhe());
            } else {
                lblLoaiGheToaDi.setText("");
            }

            hienThiDanhSachGheTheoToaChieuDi(toa);
        });

        pnlToa.add(btnToa);
    }

    javax.swing.JScrollPane scrollToa = taoScrollNgangChoPanelToa(pnlToa);
    pnlContainerToaDi.add(scrollToa, java.awt.BorderLayout.CENTER);

    pnlContainerToaDi.revalidate();
    pnlContainerToaDi.repaint();
}
    private void hienThiDanhSachGheTheoToaChieuDi(com.mycompany.prj1.entity.ToaTau toa) {
    if (!daKhoiTaoBoCucChieuDi) {
        khoiTaoBoCucChieuDi();
        daKhoiTaoBoCucChieuDi = true;
    }

    pnlContainerGheDi.removeAll();
    pnlContainerGheDi.setLayout(new java.awt.BorderLayout());

    int sucChua = toa.getSucChua();
    javax.swing.JPanel pnlGhe = taoPanelGheCoDinh(sucChua);

    java.util.Set<String> dsGheDaBan =
            daoVeTau.layDanhSachGheDaBan(maChuyenDiDaChon);

    java.util.Set<String> dsGheDangGiu =
            daoVeTau.layDanhSachGheDangGiuNguoiKhac(
                    maChuyenDiDaChon,
                    maPhienGiuCho
            );

    for (int i = 1; i <= sucChua; i++) {
        String tenGhe = String.format("%02d", i);
        String maGhe = toa.getTenToa() + " - Ghế " + tenGhe;

        javax.swing.JButton btnGhe = new javax.swing.JButton(tenGhe);

        java.awt.Dimension sizeGhe = new java.awt.Dimension(GHE_W, GHE_H);
        btnGhe.setPreferredSize(sizeGhe);
        btnGhe.setMinimumSize(sizeGhe);
        btnGhe.setMaximumSize(sizeGhe);

        boolean gheDaBan = dsGheDaBan.contains(maGhe);
        boolean gheDangGiu = dsGheDangGiu.contains(maGhe);

        if (gheDaBan) {
            btnGhe.setBackground(new java.awt.Color(220, 53, 69));
            btnGhe.setForeground(java.awt.Color.WHITE);
            btnGhe.setText(tenGhe + " X");
            btnGhe.setToolTipText("Ghế đã được bán.");
            btnGhe.setEnabled(false);

        } else if (gheDangGiu) {
            btnGhe.setBackground(new java.awt.Color(255, 243, 205));
            btnGhe.setForeground(java.awt.Color.BLACK);
            btnGhe.setText(tenGhe + " G");
            btnGhe.setToolTipText("Ghế đang được giữ chỗ.");
            btnGhe.setEnabled(false);

        } else {
            int indexGheDaChon = timIndexTheoMaGhe(maGhe);

            if (indexGheDaChon >= 0) {
                btnGhe.setBackground(new java.awt.Color(0, 120, 215));
                btnGhe.setForeground(java.awt.Color.WHITE);
                btnGhe.setText(tenGhe + " ✓");
            }

            btnGhe.addActionListener(e -> {
                xuLyChonGheChoHanhKhach(toa, maGhe, tenGhe);
            });
        }

        pnlGhe.add(btnGhe);
    }

    javax.swing.JScrollPane scrollGhe = taoScrollNgangChoPanelGhe(pnlGhe);
    pnlContainerGheDi.add(scrollGhe, java.awt.BorderLayout.CENTER);

    pnlContainerGheDi.revalidate();
    pnlContainerGheDi.repaint();
}

private int timIndexTheoMaGhe(String maGhe) {
    for (int i = 0; i < dsGheChonDi.size(); i++) {
        if (maGhe.equals(dsGheChonDi.get(i).maGhe)) {
            return i;
        }
    }
    return -1;
}

private void xuLyChonGheChoHanhKhach(
        com.mycompany.prj1.entity.ToaTau toa,
        String maGhe,
        String tenGhe
) {
    if (dsGheChonDi.isEmpty()) {
        khoiTaoDanhSachHanhKhachCanChonGheDi();
    }

    int indexGheDaCoNguoiChon = timIndexTheoMaGhe(maGhe);

    // ==========================
    // 1. Nếu ghế đã được chọn rồi
    //    Bấm lần nữa thì hủy ghế đó
    // ==========================
    if (indexGheDaCoNguoiChon >= 0) {
        GheHanhKhach hkDaChon = dsGheChonDi.get(indexGheDaCoNguoiChon);

        int chon = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Ghế " + tenGhe + " - Toa " + toa.getTenToa()
                + " đang được chọn cho Hành khách "
                + hkDaChon.stt + " - " + hkDaChon.loaiHanhKhach
                + "\nBạn có muốn hủy lựa chọn ghế này không?",
                "Xác nhận hủy ghế",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (chon != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }
        daoVeTau.xoaGiuChoGhe(
        maChuyenDiDaChon,
        hkDaChon.maGhe,
        maPhienGiuCho
        );


        // Xóa ghế khỏi hành khách
        hkDaChon.maGhe = null;
        hkDaChon.tenGhe = null;
        hkDaChon.tenToa = null;
        hkDaChon.giaVe = 0;
        danhDauNhapLaiHanhKhach(indexGheDaCoNguoiChon);


        // Sau khi hủy, chuyển con trỏ chọn ghế về đúng hành khách đó
        viTriDangChonGheDi = indexGheDaCoNguoiChon;

        // Vẽ lại danh sách ghế để ghế vừa hủy trở về màu bình thường
        hienThiDanhSachGheTheoToaChieuDi(toa);
        lblTenToaDi.setText(toa.getTenToa());
        if (toa.getLoaiGhe() != null) {
            lblLoaiGheToaDi.setText(" - " + toa.getLoaiGhe().getTenLoaiGhe());
        } else {
            lblLoaiGheToaDi.setText("");
        }
        chuyenSangHanhKhachTiepTheo();

        hienThiDanhSachGheTheoToaChieuDi(toa);
        capNhatPanelThongTinVeChieuDi();
        capNhatPanel47TongKetVe();

        return;
    }

    // ==========================
    // 2. Nếu chưa đủ ghế thì mới cho chọn tiếp
    // ==========================
    if (daChonDuGhe()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Bạn đã chọn đủ ghế.");
        return;
    }

    if (viTriDangChonGheDi < 0 || viTriDangChonGheDi >= dsGheChonDi.size()) {
        viTriDangChonGheDi = timIndexHanhKhachChuaCoGhe();
    }

    if (viTriDangChonGheDi < 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "Không còn hành khách nào cần chọn ghế.");
        return;
    }

    GheHanhKhach hk = dsGheChonDi.get(viTriDangChonGheDi);

    // ==========================
    // 3. Nếu hành khách hiện tại đã có ghế
    //    thì hỏi có muốn đổi ghế không
    // ==========================
    if (hk.maGhe != null) {
        int chon = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Hành khách " + hk.stt + " - " + hk.loaiHanhKhach
                + " đã có ghế: Toa " + hk.tenToa + " - Ghế " + hk.tenGhe
                + "\nBạn có muốn đổi sang ghế " + tenGhe + " không?",
                "Xác nhận đổi ghế",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (chon != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }
    }
    if (daoVeTau.gheDaDuocBan(maChuyenDiDaChon, maGhe)
        || daoVeTau.gheDangBiNguoiKhacGiu(maChuyenDiDaChon, maGhe, maPhienGiuCho)) {

    javax.swing.JOptionPane.showMessageDialog(
            this,
            "Ghế này vừa được người khác chọn hoặc đã bán.\n"
            + "Danh sách ghế sẽ được cập nhật lại."
    );

    hienThiDanhSachGheTheoToaChieuDi(toa);
    return;
}

    // ==========================
    // 4. Xác nhận chọn ghế mới
    // ==========================
    long giaVe = tinhGiaVe(
        maChuyenDiDaChon,
        toa,
        hk.loaiHanhKhach,
        dcNgayDi.getDate()
);

String hienThiGiaVe = dinhDangTien(giaVe);

int xacNhan = javax.swing.JOptionPane.showConfirmDialog(
        this,
        "Chọn ghế " + tenGhe
        + " - Toa " + toa.getTenToa()
        + "\ncho Hành khách " + hk.stt
        + " - " + hk.loaiHanhKhach
        + "\nGiá vé: " + hienThiGiaVe
        + "?",
        "Xác nhận chọn ghế",
        javax.swing.JOptionPane.YES_NO_OPTION
);

    if (xacNhan != javax.swing.JOptionPane.YES_OPTION) {
    return;
}

String gheCu = hk.maGhe;

boolean giuThanhCong = daoVeTau.datGiuChoGhe(
        maChuyenDiDaChon,
        maGhe,
        maPhienGiuCho,
        maNhanVienDangNhap
);

if (!giuThanhCong) {
    hienThiDanhSachGheTheoToaChieuDi(toa);
    return;
}

if (gheCu != null && !gheCu.equals(maGhe)) {
    daoVeTau.xoaGiuChoGhe(
            maChuyenDiDaChon,
            gheCu,
            maPhienGiuCho
    );
}

hk.maGhe = maGhe;
hk.tenGhe = tenGhe;
hk.tenToa = toa.getTenToa();
hk.giaVe = giaVe;
daoVeTau.capNhatChiTietGiuCho(
        maChuyenDiDaChon,
        maGhe,
        maPhienGiuCho,
        "DI",
        hk.stt,
        hk.loaiHanhKhach,
        hk.tenToa,
        hk.tenGhe,
        hk.giaVe
);

luuTrangThaiPhienDangLam("CHON_GHE_DI");
    danhDauNhapLaiHanhKhach(viTriDangChonGheDi);
    // Vẽ lại danh sách ghế để nếu đổi ghế thì ghế cũ tự mất màu
    


maToaDangChonDi = toa.getMaToa();

hienThiDanhSachToaChieuDi();

// Set lại thông tin toa đang chọn (dùng label động mới)
lblTenToaDi.setText(toa.getTenToa());
if (toa.getLoaiGhe() != null) {
    lblLoaiGheToaDi.setText(" - " + toa.getLoaiGhe().getTenLoaiGhe());
} else {
    lblLoaiGheToaDi.setText("");
}

hienThiDanhSachGheTheoToaChieuDi(toa);

chuyenSangHanhKhachTiepTheo();
capNhatPanelThongTinVeChieuDi();
capNhatPanel47TongKetVe();
}

    private boolean daChonDuGhe() {
        for (GheHanhKhach g : dsGheChonDi) {
            if (g.maGhe == null) {
                return false;
            }
        }
        return true;
    }

private int timIndexHanhKhachChuaCoGhe() {
    for (int i = 0; i < dsGheChonDi.size(); i++) {
        if (dsGheChonDi.get(i).maGhe == null) {
            return i;
        }
    }
    return -1;
}

private void chuyenSangHanhKhachTiepTheo() {
    int index = timIndexHanhKhachChuaCoGhe();

    if (index >= 0) {
        viTriDangChonGheDi = index;

//        GheHanhKhach hk = dsGheChonDi.get(index);
//
//        javax.swing.JOptionPane.showMessageDialog(
//                this,
//                "Tiếp tục chọn ghế cho Hành khách "
//                + hk.stt
//                + " - "
//                + hk.loaiHanhKhach
//        );
    } else {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Đã chọn đủ ghế cho tất cả hành khách."
        );
    }
}

private void hienThiFormHanhKhachVaoJPanel41() {
    jPanel41.removeAll();
    dsFormHanhKhach.clear();

    jPanel41.setLayout(new javax.swing.BoxLayout(jPanel41, javax.swing.BoxLayout.Y_AXIS));
    jPanel41.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));

    int nguoiLon = Math.max(0, laySoLuong(txtNguoiLon));
    int treEm = Math.max(0, laySoLuong(txtTreEm));
    int nguoiCaoTuoi = Math.max(0, laySoLuong(txtNguoiCaoTuoi));
    int sinhVien = Math.max(0, laySoLuong(txtSinhVien));

    int stt = 1;

    for (int i = 1; i <= nguoiLon; i++) {
        jPanel41.add(taoBoxNhapHanhKhach(stt++, "Người lớn"));
        jPanel41.add(javax.swing.Box.createVerticalStrut(12));
    }

    for (int i = 1; i <= treEm; i++) {
        jPanel41.add(taoBoxNhapHanhKhach(stt++, "Trẻ em(-25%)"));
        jPanel41.add(javax.swing.Box.createVerticalStrut(12));
    }

    for (int i = 1; i <= nguoiCaoTuoi; i++) {
        jPanel41.add(taoBoxNhapHanhKhach(stt++, "Người cao tuổi(-15%)"));
        jPanel41.add(javax.swing.Box.createVerticalStrut(12));
    }

    for (int i = 1; i <= sinhVien; i++) {
        jPanel41.add(taoBoxNhapHanhKhach(stt++, "Sinh viên(-10%)"));
        jPanel41.add(javax.swing.Box.createVerticalStrut(12));
    }

    jPanel41.revalidate();
    jPanel41.repaint();
    
    if (scrollNhapThongTinHanhKhach != null) {
    scrollNhapThongTinHanhKhach.revalidate();
    scrollNhapThongTinHanhKhach.repaint();

    // Mỗi lần tạo lại form thì kéo thanh cuộn về đầu
    javax.swing.SwingUtilities.invokeLater(() -> {
        scrollNhapThongTinHanhKhach.getVerticalScrollBar().setValue(0);
    });
}
}
    private javax.swing.JPanel taoBoxNhapHanhKhach(int stt, String loaiHanhKhach) {
    javax.swing.JPanel box = new javax.swing.JPanel(new java.awt.GridBagLayout());
    box.setBackground(java.awt.Color.WHITE);
    box.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(210, 210, 210)),
            javax.swing.BorderFactory.createEmptyBorder(18, 24, 18, 24)
    ));

    box.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 320));

    java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
    gbc.insets = new java.awt.Insets(1, 8, 8, 8);
    gbc.fill = java.awt.GridBagConstraints.HORIZONTAL;
    gbc.weightx = 1;

    javax.swing.JLabel lblLoai = new javax.swing.JLabel("Hành khách " + stt + " - " + loaiHanhKhach);
    lblLoai.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));

    javax.swing.JTextField txtHoTen = new javax.swing.JTextField();
    addPlaceholder(txtHoTen, "Họ và tên(ví dụ: Nguyễn Văn A)");

    com.toedter.calendar.JDateChooser dcNgaySinh = new com.toedter.calendar.JDateChooser();
    dcNgaySinh.setDateFormatString("dd/MM/yyyy");
    
    dcNgaySinh.setMaxSelectableDate(new java.util.Date());

    
    


    javax.swing.JComboBox<String> cboLoaiGiayTo = new javax.swing.JComboBox<>();

    if (loaiHanhKhach.toLowerCase().contains("trẻ em")) {
        cboLoaiGiayTo.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"KHONG_CO"}
        ));
    } else {
        cboLoaiGiayTo.setModel(new javax.swing.DefaultComboBoxModel<>(
                new String[]{"CCCD", "PASSPORT"}
        ));
    }

    javax.swing.JTextField txtSoGiayTo = new javax.swing.JTextField();
    addPlaceholder(txtSoGiayTo, "Số giấy tờ(CCCD/PASSPORT)");
    
    
    
    

    javax.swing.JTextField txtSdt = new javax.swing.JTextField();
    addPlaceholder(txtSdt, "Số điện thoại");
    chiChoNhapSo(txtSdt);

//txtSoGiayTo.addFocusListener(new java.awt.event.FocusAdapter() {
//    @Override
//    public void focusLost(java.awt.event.FocusEvent e) {
//        String soGT = txtSoGiayTo.getText().trim();
//
//        if (soGT.isEmpty() || soGT.equals("Số giấy tờ(CCCD/PASSPORT)")) {
//            return;
//        }
//
//        boolean hopLe = kiemTraGiayToHopLe(
//                loaiHanhKhach,
//                cboLoaiGiayTo,
//                txtSoGiayTo
//        );
//
//        if (!hopLe) {
//            return;
//        }
//
//        tuDongDienHanhKhachTheoGiayTo(
//                cboLoaiGiayTo,
//                txtSoGiayTo,
//                txtHoTen,
//                txtSdt,
//                dcNgaySinh
//        );
//    }
//});

//txtSoGiayTo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
//    private void kiemTra() {
//        javax.swing.SwingUtilities.invokeLater(() -> {
//            String loaiGT = cboLoaiGiayTo.getSelectedItem() == null
//                    ? ""
//                    : cboLoaiGiayTo.getSelectedItem().toString().trim();
//
//            String soGT = txtSoGiayTo.getText().trim();
//
//            if (soGT.equals("Số giấy tờ(CCCD/PASSPORT)")) {
//                return;
//            }
//
//            if (loaiGT.equalsIgnoreCase("CCCD") && soGT.length() == 12) {
//                if (kiemTraGiayToHopLe(loaiHanhKhach, cboLoaiGiayTo, txtSoGiayTo)) {
//                    tuDongDienHanhKhachTheoGiayTo(
//                            cboLoaiGiayTo,
//                            txtSoGiayTo,
//                            txtHoTen,
//                            txtSdt,
//                            dcNgaySinh
//                    );
//                }
//            }
//
//            if (loaiGT.equalsIgnoreCase("PASSPORT") && soGT.length() >= 6) {
//                tuDongDienHanhKhachTheoGiayTo(
//                        cboLoaiGiayTo,
//                        txtSoGiayTo,
//                        txtHoTen,
//                        txtSdt,
//                        dcNgaySinh
//                );
//            }
//        });
//    }
//
//    @Override
//    public void insertUpdate(javax.swing.event.DocumentEvent e) {
//        kiemTra();
//    }
//
//    @Override
//    public void removeUpdate(javax.swing.event.DocumentEvent e) {
//        kiemTra();
//    }
//
//    @Override
//    public void changedUpdate(javax.swing.event.DocumentEvent e) {
//        kiemTra();
//    }
//});

coDinhSize(txtHoTen, 260, 32);
coDinhSize(dcNgaySinh, 180, 32);
coDinhSize(cboLoaiGiayTo, 180, 32);
coDinhSize(txtSoGiayTo, 260, 32);
coDinhSize(txtSdt, 260, 32);
    
    
    // Nếu trẻ em chọn KHONG_CO thì khóa ô số giấy tờ
    cboLoaiGiayTo.addActionListener(e -> {
    String loaiGT = cboLoaiGiayTo.getSelectedItem().toString();

    if (loaiGT.equals("KHONG_CO")) {
        txtSoGiayTo.setText("");
        txtSoGiayTo.setEnabled(false);
    } else {
        txtSoGiayTo.setEnabled(true);
        txtSoGiayTo.setText("");
        txtSoGiayTo.setForeground(java.awt.Color.BLACK);

        if (loaiGT.equals("CCCD")) {
            txtSoGiayTo.setToolTipText("CCCD gồm đúng 12 chữ số");
        } else if (loaiGT.equals("PASSPORT")) {
            txtSoGiayTo.setToolTipText("PASSPORT từ 6 đến 9 ký tự A-Z hoặc 0-9");
        }
    }
});

    if (loaiHanhKhach.toLowerCase().contains("trẻ em")) {
    txtSoGiayTo.setText("");
    txtSoGiayTo.setEnabled(false);
}

    // Lưu lại để sau này lấy dữ liệu
    FormHanhKhach form = new FormHanhKhach();
    form.loaiHanhKhach = loaiHanhKhach;
    form.txtHoTen = txtHoTen;
    form.dcNgaySinh = dcNgaySinh;
    form.cboLoaiGiayTo = cboLoaiGiayTo;
    form.txtSoGiayTo = txtSoGiayTo;
    form.txtSdt = txtSdt;
    
    

    form.lblLoiHoTen = taoLabelLoi();
    form.lblLoiNgaySinh = taoLabelLoi();
    form.lblLoiLoaiGiayTo = taoLabelLoi();
    form.lblLoiSoGiayTo = taoLabelLoi();
    form.lblLoiSdt = taoLabelLoi();
    
    form.dcNgaySinh.getDateEditor().addPropertyChangeListener("date", evt -> {
    javax.swing.SwingUtilities.invokeLater(() -> {
        if (form.dcNgaySinh.getDate() == null) {
            xoaLoi(form.dcNgaySinh, form.lblLoiNgaySinh);
            return;
        }

        kiemTraNgaySinhInline(form);
    });
});
    form.txtSoGiayTo.addFocusListener(new java.awt.event.FocusAdapter() {
    @Override
    public void focusLost(java.awt.event.FocusEvent e) {
        String soGT = laySoGiayTo(form);

        if (soGT.isEmpty()) {
            return;
        }

        // Chỉ hiện lỗi đỏ, không JOptionPane
        if (!kiemTraGiayToInline(form)) {
            return;
        }

        kiemTraDoiGiayToSauKhiDoDuLieu(form);

        if (!form.duLieuTuDB) {
            xuLyTraCuuHanhKhachTheoGiayTo(form);
        }
    }
});

dsFormHanhKhach.add(form);
ganSuKienTuDongXoaLoi(form);

    // Dòng 1: tiêu đề
gbc.gridx = 0;
gbc.gridy = 0;
gbc.gridwidth = 2;
box.add(lblLoai, gbc);

// Dòng 2: họ tên + ngày sinh
gbc.gridwidth = 1;

gbc.gridx = 0;
gbc.gridy = 1;
box.add(txtHoTen, gbc);

gbc.gridx = 1;
gbc.gridy = 1;
box.add(dcNgaySinh, gbc);

// Dòng 3: lỗi họ tên + lỗi ngày sinh
gbc.gridx = 0;
gbc.gridy = 2;
box.add(form.lblLoiHoTen, gbc);

gbc.gridx = 1;
gbc.gridy = 2;
box.add(form.lblLoiNgaySinh, gbc);

// Dòng 4: loại giấy tờ + số giấy tờ
gbc.gridx = 0;
gbc.gridy = 3;
box.add(cboLoaiGiayTo, gbc);

gbc.gridx = 1;
gbc.gridy = 3;
box.add(txtSoGiayTo, gbc);

// Dòng 5: lỗi loại giấy tờ + lỗi số giấy tờ
gbc.gridx = 0;
gbc.gridy = 4;
box.add(form.lblLoiLoaiGiayTo, gbc);

gbc.gridx = 1;
gbc.gridy = 4;
box.add(form.lblLoiSoGiayTo, gbc);

// Dòng 6: số điện thoại
gbc.gridx = 0;
gbc.gridy = 5;
gbc.gridwidth = 2;
box.add(txtSdt, gbc);

// Dòng 7: lỗi số điện thoại
gbc.gridx = 0;
gbc.gridy = 6;
gbc.gridwidth = 2;
box.add(form.lblLoiSdt, gbc);

return box;
}
    private void tuDongDienHanhKhachTheoGiayTo(
        javax.swing.JComboBox<String> cboLoaiGiayTo,
        javax.swing.JTextField txtSoGiayTo,
        javax.swing.JTextField txtHoTen,
        javax.swing.JTextField txtSdt,
        com.toedter.calendar.JDateChooser dcNgaySinh
) {
    String loaiGiayTo = cboLoaiGiayTo.getSelectedItem() == null
            ? ""
            : cboLoaiGiayTo.getSelectedItem().toString().trim();

    String soGiayTo = txtSoGiayTo.getText().trim();

    if (soGiayTo.equals("Số giấy tờ(CCCD/PASSPORT)") || soGiayTo.isEmpty()) {
        return;
    }

    if (loaiGiayTo.equalsIgnoreCase("KHONG_CO")) {
        return;
    }

    // Chỉ tự tìm khi CCCD đủ 12 số
    if (loaiGiayTo.equalsIgnoreCase("CCCD") && !soGiayTo.matches("\\d{12}")) {
        return;
    }

    // Nếu là PASSPORT thì chỉ tìm khi đủ tối thiểu 6 ký tự
    if (loaiGiayTo.equalsIgnoreCase("PASSPORT") && soGiayTo.length() < 6) {
        return;
    }

    HanhKhach hk = daoHanhKhach.timHanhKhachTheoGiayTo(loaiGiayTo, soGiayTo);

    if (hk == null) {
        return;
    }

    txtHoTen.setText(hk.getHoTenHK());
    txtHoTen.setForeground(java.awt.Color.BLACK);

    if (hk.getSdt() != null) {
        txtSdt.setText(hk.getSdt());
        txtSdt.setForeground(java.awt.Color.BLACK);
    }

    if (hk.getNgaySinh() != null) {
        java.util.Date ngaySinh = java.sql.Date.valueOf(hk.getNgaySinh());
        dcNgaySinh.setDate(ngaySinh);
    }

    
}
    private void addPlaceholder(javax.swing.JTextField textField, String placeholder) {
    textField.setText(placeholder);
    textField.setForeground(java.awt.Color.GRAY);

    textField.addFocusListener(new java.awt.event.FocusAdapter() {
        @Override
        public void focusGained(java.awt.event.FocusEvent evt) {
            if (textField.getText().equals(placeholder)) {
                textField.setText("");
                textField.setForeground(java.awt.Color.BLACK);
            }
        }

        @Override
        public void focusLost(java.awt.event.FocusEvent evt) {
            if (textField.getText().trim().isEmpty()) {
                textField.setText(placeholder);
                textField.setForeground(java.awt.Color.GRAY);
            }
        }
        
        
        
        
        
    });     
}
    private void coDinhSize(java.awt.Component c, int w, int h) {
    java.awt.Dimension d = new java.awt.Dimension(w, h);
    c.setPreferredSize(d);
    c.setMinimumSize(d);
    c.setMaximumSize(d);
}
  private void quayLaiTuNhapThongTinHanhKhach() {
    java.awt.CardLayout cl = (java.awt.CardLayout) jPanelNoi_Dung.getLayout();

    // Nếu là vé khứ hồi thì quay lại màn hình chọn ghế chiều về
    if (jRadioButton2.isSelected()
            && maChuyenVeDaChon != null
            && !maChuyenVeDaChon.trim().isEmpty()) {

        cl.show(jPanelNoi_Dung, "card6");
    } 
    // Nếu là vé một chiều thì quay lại màn hình chọn ghế chiều đi
    else {
        cl.show(jPanelNoi_Dung, "card5");
    }
}  
  private void khoiTaoDanhSachHanhKhachCanChonGheDi() {
    dsGheChonDi.clear();
    viTriDangChonGheDi = 0;

    int stt = 1;

    int nguoiLon = Math.max(0, laySoLuong(txtNguoiLon));
    int treEm = Math.max(0, laySoLuong(txtTreEm));
    int nguoiCaoTuoi = Math.max(0, laySoLuong(txtNguoiCaoTuoi));
    int sinhVien = Math.max(0, laySoLuong(txtSinhVien));

    for (int i = 0; i < nguoiLon; i++) {
        dsGheChonDi.add(taoDongGhe(stt++, "Người lớn"));
    }

    for (int i = 0; i < treEm; i++) {
        dsGheChonDi.add(taoDongGhe(stt++, "Trẻ em"));
    }

    for (int i = 0; i < nguoiCaoTuoi; i++) {
        dsGheChonDi.add(taoDongGhe(stt++, "Người cao tuổi"));
    }

    for (int i = 0; i < sinhVien; i++) {
        dsGheChonDi.add(taoDongGhe(stt++, "Sinh viên"));
    }

    capNhatPanelThongTinVeChieuDi();
}

private GheHanhKhach taoDongGhe(int stt, String loai) {
    GheHanhKhach g = new GheHanhKhach();
    g.stt = stt;
    g.loaiHanhKhach = loai;
    return g;
}
    private void capNhatPanelThongTinVe() {
    javax.swing.JPanel panelHienThi;

    // Nếu đang ở màn hình chọn ghế chiều đi thì hiển thị vào jPanel46
    if (GD_chon_TOA_GHE_XUOI.isShowing()) {
        panelHienThi = jPanel46;
    } 
    // Nếu đang ở màn hình chọn ghế chiều về thì hiển thị vào jPanel44
    else {
        panelHienThi = jPanel44;
    }

    panelHienThi.removeAll();
    panelHienThi.setLayout(new javax.swing.BoxLayout(panelHienThi, javax.swing.BoxLayout.Y_AXIS));

    panelHienThi.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createTitledBorder(
                    javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)),
                    "Thông tin vé"
            ),
            javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
    ));

    for (int i = 0; i < dsGheChonDi.size(); i++) {
        GheHanhKhach g = dsGheChonDi.get(i);

        String gheText;
        if (g.maGhe == null) {
            gheText = "Chưa chọn ghế";
        } else {
            gheText = "Toa " + g.tenToa + " - Ghế " + g.tenGhe;
        }

        javax.swing.JButton btn = new javax.swing.JButton(
                "HK " + g.stt + " - " + g.loaiHanhKhach + " | " + gheText
        );

        btn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 35));

        final int index = i;
        btn.addActionListener(e -> {
            viTriDangChonGheDi = index;
            capNhatPanelThongTinVe();

            
        });

        if (i == viTriDangChonGheDi) {
            btn.setBackground(new java.awt.Color(255, 230, 150));
        }

        panelHienThi.add(btn);
        panelHienThi.add(javax.swing.Box.createVerticalStrut(5));
    }

    panelHienThi.revalidate();
    panelHienThi.repaint();
}

    private void moManHinhChonToaVaGhe() {
    jLabel33.setText(tenTauChuyenDi != null ? tenTauChuyenDi : "");
    jLabel34.setText(
            (gaDiChuyenDi != null ? gaDiChuyenDi : "")
            + " - "
            + (gaDenChuyenDi != null ? gaDenChuyenDi : "")
    );

    if (maChuyenVeDaChon != null && !maChuyenVeDaChon.trim().isEmpty()) {
        jButton19.setText("Sang chiều về");
    } else {
        jButton19.setText("Xác nhận");
    }

    // Mỗi lần chọn chuyến mới thì tạo lại danh sách hành khách cần chọn ghế
    khoiTaoDanhSachHanhKhachCanChonGheDi();

    hienThiDanhSachToaChieuDi();

    java.awt.CardLayout cl = (java.awt.CardLayout) jPanelNoi_Dung.getLayout();
    cl.show(jPanelNoi_Dung, "card5");

    capNhatPanelThongTinVeChieuDi();
}
    private void capNhatPanelThongTinVeChieuDi() {
    if (pnlThongTinVeChieuDi == null) {
        cauHinhScrollThongTinVeChieuDi();
    }

    pnlThongTinVeChieuDi.removeAll();

    for (int i = 0; i < dsGheChonDi.size(); i++) {
        GheHanhKhach g = dsGheChonDi.get(i);

        String gheText = g.maGhe == null
        ? "Chưa chọn ghế"
        : "Toa " + g.tenToa
            + " - Ghế " + g.tenGhe
            + " - " + dinhDangTien(g.giaVe);

        javax.swing.JButton btn = new javax.swing.JButton(
                "HK " + g.stt + " - " + g.loaiHanhKhach + " | " + gheText
        );

        btn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 35));
        btn.setPreferredSize(new java.awt.Dimension(160, 35));
        btn.setMinimumSize(new java.awt.Dimension(160, 35));

        final int index = i;
        btn.addActionListener(e -> {
            viTriDangChonGheDi = index;
            capNhatPanelThongTinVeChieuDi();

            
        });

        if (i == viTriDangChonGheDi) {
            btn.setBackground(new java.awt.Color(255, 230, 150));
        }

        pnlThongTinVeChieuDi.add(btn);
        pnlThongTinVeChieuDi.add(javax.swing.Box.createVerticalStrut(5));
    }

    pnlThongTinVeChieuDi.revalidate();
    pnlThongTinVeChieuDi.repaint();

    scrollThongTinVeChieuDi.revalidate();
    scrollThongTinVeChieuDi.repaint();

    jPanel46.revalidate();
    jPanel46.repaint();
}
    private void xoaLuaChonGheChieuDi() {
    xoaGiuChoTheoDanhSach(dsGheChonDi, maChuyenDiDaChon);    
    dsGheChonDi.clear();
    viTriDangChonGheDi = 0;

    maToaDangChonDi = null;
    maGheDangChonDi = null;
    btnGheDangChonDi = null;

    if (pnlContainerGheDi != null) {
        pnlContainerGheDi.removeAll();
        pnlContainerGheDi.revalidate();
        pnlContainerGheDi.repaint();
    }
    if (lblTenToaDi != null) lblTenToaDi.setText("");
    if (lblLoaiGheToaDi != null) lblLoaiGheToaDi.setText("");

    if (pnlThongTinVeChieuDi != null) {
    pnlThongTinVeChieuDi.removeAll();
    pnlThongTinVeChieuDi.revalidate();
    pnlThongTinVeChieuDi.repaint();
}

    
}
    private void cauHinhScrollThongTinVeChieuDi() {
    jPanel46.removeAll();
    jPanel46.setLayout(new java.awt.BorderLayout());

    pnlThongTinVeChieuDi = new javax.swing.JPanel();
    pnlThongTinVeChieuDi.setLayout(
            new javax.swing.BoxLayout(pnlThongTinVeChieuDi, javax.swing.BoxLayout.Y_AXIS)
    );
    pnlThongTinVeChieuDi.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
    );

    scrollThongTinVeChieuDi = new javax.swing.JScrollPane(pnlThongTinVeChieuDi);
    scrollThongTinVeChieuDi.setVerticalScrollBarPolicy(
            javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );
    scrollThongTinVeChieuDi.setHorizontalScrollBarPolicy(
            javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );

    scrollThongTinVeChieuDi.getVerticalScrollBar().setUnitIncrement(16);

    jPanel46.add(scrollThongTinVeChieuDi, java.awt.BorderLayout.CENTER);

    jPanel46.revalidate();
    jPanel46.repaint();
}
    private void cauHinhScrollThongTinVeChieuVe() {
    jPanel44.removeAll();
    jPanel44.setLayout(new java.awt.BorderLayout());

    pnlThongTinVeChieuVe = new javax.swing.JPanel();
    pnlThongTinVeChieuVe.setLayout(
            new javax.swing.BoxLayout(pnlThongTinVeChieuVe, javax.swing.BoxLayout.Y_AXIS)
    );
    pnlThongTinVeChieuVe.setBorder(
            javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
    );

    scrollThongTinVeChieuVe = new javax.swing.JScrollPane(pnlThongTinVeChieuVe);
    scrollThongTinVeChieuVe.setVerticalScrollBarPolicy(
            javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );
    scrollThongTinVeChieuVe.setHorizontalScrollBarPolicy(
            javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );

    scrollThongTinVeChieuVe.getVerticalScrollBar().setUnitIncrement(16);

    jPanel44.add(scrollThongTinVeChieuVe, java.awt.BorderLayout.CENTER);

    jPanel44.revalidate();
    jPanel44.repaint();
}
    private void canChinhPanelGhiChuVaThongTinVe() {
    // =========================
    // CHIỀU ĐI: jPanel31
    // =========================
    jPanel31.removeAll();
    jPanel31.setLayout(new java.awt.GridBagLayout());
    jPanel31.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));

    java.awt.GridBagConstraints gbcDi = new java.awt.GridBagConstraints();
    gbcDi.insets = new java.awt.Insets(0, 5, 0, 5);
    gbcDi.fill = java.awt.GridBagConstraints.BOTH;
    gbcDi.gridy = 0;
    gbcDi.weighty = 1.0;

    // Ghi chú bên trái
    gbcDi.gridx = 0;
    gbcDi.weightx = 0.35;
    jPanel31.add(jPanel45, gbcDi);

    // Thông tin vé bên phải
    gbcDi.gridx = 1;
    gbcDi.weightx = 0.65;
    jPanel31.add(jPanel46, gbcDi);

    jPanel31.setPreferredSize(new java.awt.Dimension(376, 220));
    jPanel31.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 240));
    jPanel31.setMinimumSize(new java.awt.Dimension(300, 180));


    // =========================
    // CHIỀU VỀ: jPanel32
    // =========================
    jPanel32.removeAll();
    jPanel32.setLayout(new java.awt.GridBagLayout());
    jPanel32.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8));

    java.awt.GridBagConstraints gbcVe = new java.awt.GridBagConstraints();
    gbcVe.insets = new java.awt.Insets(0, 5, 0, 5);
    gbcVe.fill = java.awt.GridBagConstraints.BOTH;
    gbcVe.gridy = 0;
    gbcVe.weighty = 1.0;

    // Ghi chú bên trái
    gbcVe.gridx = 0;
    gbcVe.weightx = 0.35;
    jPanel32.add(jPanel43, gbcVe);

    // Thông tin vé bên phải
    gbcVe.gridx = 1;
    gbcVe.weightx = 0.65;
    jPanel32.add(jPanel44, gbcVe);

    jPanel32.setPreferredSize(new java.awt.Dimension(376, 220));
    jPanel32.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 240));
    jPanel32.setMinimumSize(new java.awt.Dimension(300, 180));


    // =========================
    // Căn chỉnh bên trong ghi chú
    // =========================
    chinhPanelGhiChu(jPanel45);
    chinhPanelGhiChu(jPanel43);

    jPanel31.revalidate();
    jPanel31.repaint();

    jPanel32.revalidate();
    jPanel32.repaint();
}
    
    private void chinhPanelGhiChu(javax.swing.JPanel panelGhiChu) {
    panelGhiChu.setLayout(new javax.swing.BoxLayout(panelGhiChu, javax.swing.BoxLayout.Y_AXIS));
    panelGhiChu.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createTitledBorder(
                    javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)),
                    "Ghi chú",
                    javax.swing.border.TitledBorder.LEFT,
                    javax.swing.border.TitledBorder.TOP
            ),
            javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10)
    ));

    for (java.awt.Component c : panelGhiChu.getComponents()) {
        if (c instanceof javax.swing.JPanel) {
            javax.swing.JPanel dong = (javax.swing.JPanel) c;
            dong.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
            dong.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 35));
        }
    }
}
    private void khoiTaoDanhSachHanhKhachCanChonGheVe() {
    dsGheChonVe.clear();
    viTriDangChonGheVe = 0;

    int stt = 1;

    int nguoiLon = Math.max(0, laySoLuong(txtNguoiLon));
    int treEm = Math.max(0, laySoLuong(txtTreEm));
    int nguoiCaoTuoi = Math.max(0, laySoLuong(txtNguoiCaoTuoi));
    int sinhVien = Math.max(0, laySoLuong(txtSinhVien));

    for (int i = 0; i < nguoiLon; i++) {
        dsGheChonVe.add(taoDongGhe(stt++, "Người lớn"));
    }

    for (int i = 0; i < treEm; i++) {
        dsGheChonVe.add(taoDongGhe(stt++, "Trẻ em"));
    }

    for (int i = 0; i < nguoiCaoTuoi; i++) {
        dsGheChonVe.add(taoDongGhe(stt++, "Người cao tuổi"));
    }

    for (int i = 0; i < sinhVien; i++) {
        dsGheChonVe.add(taoDongGhe(stt++, "Sinh viên"));
    }

    capNhatPanelThongTinVeChieuVe();
}
    private void hienThiDanhSachToaChieuVe() {
    if (!daKhoiTaoBoCucChieuVe) {
        khoiTaoBoCucChieuVeGiongChieuDi();
        daKhoiTaoBoCucChieuVe = true;
    }

    pnlContainerToaVe.removeAll();
    pnlContainerToaVe.setLayout(new java.awt.BorderLayout());

    javax.swing.JPanel pnlToa = new javax.swing.JPanel(
            new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 8)
    );
    pnlToa.setOpaque(false);

    pnlContainerGheVe.removeAll();
    pnlContainerGheVe.revalidate();
    pnlContainerGheVe.repaint();

    lblTenToaVe.setText("");
    lblLoaiGheToaVe.setText("");

    String maTau = jLabel36.getText().trim();

    if (maTau.isEmpty() || maTau.equalsIgnoreCase("tentau")) {
        lblSoLuongToaVe.setText("Chưa có tàu chiều về được chọn");
        pnlContainerToaVe.revalidate();
        pnlContainerToaVe.repaint();
        return;
    }

    int soLuongToa = daoToaTau.laySoLuongToaTheoTau(maTau);
    java.util.List<com.mycompany.prj1.entity.ToaTau> dsToa =
            daoToaTau.layDanhSachToaTheoTau(maTau);

    lblSoLuongToaVe.setText("Số lượng toa: " + soLuongToa);

    for (com.mycompany.prj1.entity.ToaTau toa : dsToa) {
        javax.swing.JButton btnToa = new javax.swing.JButton(
                toa.getTenToa() + "-" + toa.getLoaiGhe().getTenLoaiGhe()
        );

        lamDepNutToa(btnToa);

        capNhatMauNutToa(
                btnToa,
                toa,
                maToaDangChonVe,
                dsGheChonVe,
                maChuyenVeDaChon
        );

        btnToa.addActionListener(e -> {
            maToaDangChonVe = toa.getMaToa();

            hienThiDanhSachToaChieuVe();

            lblTenToaVe.setText(toa.getTenToa());
            if (toa.getLoaiGhe() != null) {
                lblLoaiGheToaVe.setText(" - " + toa.getLoaiGhe().getTenLoaiGhe());
            } else {
                lblLoaiGheToaVe.setText("");
            }

            hienThiDanhSachGheTheoToaChieuVe(toa);
        });

        pnlToa.add(btnToa);
    }

    javax.swing.JScrollPane scrollToa = taoScrollNgangChoPanelToa(pnlToa);
    pnlContainerToaVe.add(scrollToa, java.awt.BorderLayout.CENTER);

    pnlContainerToaVe.revalidate();
    pnlContainerToaVe.repaint();
}
    private String dinhDangPhutGiay(int tongGiay) {
    int phut = tongGiay / 60;
    int giay = tongGiay % 60;

    return String.format("%02d:%02d", phut, giay);
}
    private void hienThiDanhSachGheTheoToaChieuVe(com.mycompany.prj1.entity.ToaTau toa) {
    if (!daKhoiTaoBoCucChieuVe) {
        khoiTaoBoCucChieuVeGiongChieuDi();
        daKhoiTaoBoCucChieuVe = true;
    }

    pnlContainerGheVe.removeAll();
    pnlContainerGheVe.setLayout(new java.awt.BorderLayout());

    int sucChua = toa.getSucChua();
    javax.swing.JPanel pnlGhe = taoPanelGheCoDinh(sucChua);

    java.util.Set<String> dsGheDaBan =
            daoVeTau.layDanhSachGheDaBan(maChuyenVeDaChon);

    java.util.Set<String> dsGheDangGiu =
            daoVeTau.layDanhSachGheDangGiuNguoiKhac(
                    maChuyenVeDaChon,
                    maPhienGiuCho
            );

    for (int i = 1; i <= sucChua; i++) {
        String tenGhe = String.format("%02d", i);
        String maGhe = toa.getTenToa() + " - Ghế " + tenGhe;

        javax.swing.JButton btnGhe = new javax.swing.JButton(tenGhe);

        java.awt.Dimension sizeGhe = new java.awt.Dimension(GHE_W, GHE_H);
        btnGhe.setPreferredSize(sizeGhe);
        btnGhe.setMinimumSize(sizeGhe);
        btnGhe.setMaximumSize(sizeGhe);

        boolean gheDaBan = dsGheDaBan.contains(maGhe);
        boolean gheDangGiu = dsGheDangGiu.contains(maGhe);

        if (gheDaBan) {
            btnGhe.setBackground(new java.awt.Color(220, 53, 69));
            btnGhe.setForeground(java.awt.Color.WHITE);
            btnGhe.setText(tenGhe + " X");
            btnGhe.setToolTipText("Ghế đã được bán.");
            btnGhe.setEnabled(false);

        } else if (gheDangGiu) {
            int soGiayConLai = daoVeTau.laySoGiayGiuChoConLaiCuaGhe(
                    maChuyenVeDaChon,
                    maGhe,
                    maPhienGiuCho
            );

            btnGhe.setBackground(new java.awt.Color(255, 243, 205));
            btnGhe.setForeground(java.awt.Color.BLACK);
            btnGhe.setText(tenGhe + " G");
            btnGhe.setToolTipText("Ghế đang được giữ chỗ. " + dinhDangPhutGiay(soGiayConLai));
            btnGhe.setEnabled(false);

        } else {
            int indexGheDaChon = timIndexTheoMaGheChieuVe(maGhe);

            if (indexGheDaChon >= 0) {
                btnGhe.setBackground(new java.awt.Color(0, 120, 215));
                btnGhe.setForeground(java.awt.Color.WHITE);
                btnGhe.setText(tenGhe + " ✓");
            }

            btnGhe.addActionListener(e -> {
                xuLyChonGheChoHanhKhachChieuVe(toa, maGhe, tenGhe);
            });
        }

        pnlGhe.add(btnGhe);
    }

    javax.swing.JScrollPane scrollGhe = taoScrollNgangChoPanelGhe(pnlGhe);
    pnlContainerGheVe.add(scrollGhe, java.awt.BorderLayout.CENTER);

    pnlContainerGheVe.revalidate();
    pnlContainerGheVe.repaint();
    
}
    private int timIndexTheoMaGheChieuVe(String maGhe) {
    for (int i = 0; i < dsGheChonVe.size(); i++) {
        if (maGhe.equals(dsGheChonVe.get(i).maGhe)) {
            return i;
        }
    }
    return -1;
}

private boolean daChonDuGheChieuVe() {
    for (GheHanhKhach g : dsGheChonVe) {
        if (g.maGhe == null) {
            return false;
        }
    }
    return true;
}

private int timIndexHanhKhachChuaCoGheChieuVe() {
    for (int i = 0; i < dsGheChonVe.size(); i++) {
        if (dsGheChonVe.get(i).maGhe == null) {
            return i;
        }
    }
    return -1;
}

private void chuyenSangHanhKhachTiepTheoChieuVe() {
    int index = timIndexHanhKhachChuaCoGheChieuVe();

    if (index >= 0) {
        viTriDangChonGheVe = index;

//        GheHanhKhach hk = dsGheChonVe.get(index);
//
//        javax.swing.JOptionPane.showMessageDialog(
//                this,
//                "Tiếp tục chọn ghế chiều về cho Hành khách "
//                + hk.stt
//                + " - "
//                + hk.loaiHanhKhach
//        );
    } else {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Đã chọn đủ ghế chiều về cho tất cả hành khách."
        );
    }
}
private void xuLyChonGheChoHanhKhachChieuVe(
        com.mycompany.prj1.entity.ToaTau toa,
        String maGhe,
        String tenGhe
) {
    if (dsGheChonVe.isEmpty()) {
        khoiTaoDanhSachHanhKhachCanChonGheVe();
    }

    int indexGheDaCoNguoiChon = timIndexTheoMaGheChieuVe(maGhe);

    if (indexGheDaCoNguoiChon >= 0) {
        GheHanhKhach hkDaChon = dsGheChonVe.get(indexGheDaCoNguoiChon);

        int chon = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Ghế " + tenGhe + " - Toa " + toa.getTenToa()
                + " đang được chọn cho Hành khách "
                + hkDaChon.stt + " - " + hkDaChon.loaiHanhKhach
                + "\nBạn có muốn hủy lựa chọn ghế này không?",
                "Xác nhận hủy ghế chiều về",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (chon != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }
        daoVeTau.xoaGiuChoGhe(
        maChuyenVeDaChon,
        hkDaChon.maGhe,
        maPhienGiuCho
        );

        hkDaChon.maGhe = null;
        hkDaChon.tenGhe = null;
        hkDaChon.tenToa = null;
        hkDaChon.giaVe = 0;

        danhDauNhapLaiHanhKhach(indexGheDaCoNguoiChon);

        viTriDangChonGheVe = indexGheDaCoNguoiChon;

        hienThiDanhSachToaChieuVe();

        // Set lại header tên toa
        lblTenToaVe.setText(toa.getTenToa());
        if (toa.getLoaiGhe() != null) {
            lblLoaiGheToaVe.setText(" - " + toa.getLoaiGhe().getTenLoaiGhe());
        } else {
            lblLoaiGheToaVe.setText("");
        }

        hienThiDanhSachGheTheoToaChieuVe(toa);
        capNhatPanelThongTinVeChieuVe();
        capNhatPanel47TongKetVe();
        return;
    }

    if (daChonDuGheChieuVe()) {
        javax.swing.JOptionPane.showMessageDialog(this, "Bạn đã chọn đủ ghế chiều về.");
        return;
    }

    if (viTriDangChonGheVe < 0 || viTriDangChonGheVe >= dsGheChonVe.size()) {
        viTriDangChonGheVe = timIndexHanhKhachChuaCoGheChieuVe();
    }

    if (viTriDangChonGheVe < 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "Không còn hành khách nào cần chọn ghế chiều về.");
        return;
    }

    GheHanhKhach hk = dsGheChonVe.get(viTriDangChonGheVe);

    if (hk.maGhe != null) {
        int chon = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Hành khách " + hk.stt + " - " + hk.loaiHanhKhach
                + " đã có ghế chiều về: Toa " + hk.tenToa + " - Ghế " + hk.tenGhe
                + "\nBạn có muốn đổi sang ghế " + tenGhe + " không?",
                "Xác nhận đổi ghế chiều về",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (chon != javax.swing.JOptionPane.YES_OPTION) {
            return;
        }
    }
    if (daoVeTau.gheDaDuocBan(maChuyenVeDaChon, maGhe)
        || daoVeTau.gheDangBiNguoiKhacGiu(maChuyenVeDaChon, maGhe, maPhienGiuCho)) {

    javax.swing.JOptionPane.showMessageDialog(
            this,
            "Ghế này vừa được người khác chọn hoặc đã bán.\n"
            + "Danh sách ghế chiều về sẽ được cập nhật lại."
    );

    hienThiDanhSachGheTheoToaChieuVe(toa);
    return;
}
    long giaVe = tinhGiaVe(
        maChuyenVeDaChon,
        toa,
        hk.loaiHanhKhach,
        dcNgayVe.getDate()
);

    String hienThiGiaVe = dinhDangTien(giaVe);

    int xacNhan = javax.swing.JOptionPane.showConfirmDialog(
        this,
        "Chọn ghế " + tenGhe
        + " - Toa " + toa.getTenToa()
        + "\ncho Hành khách " + hk.stt
        + " - " + hk.loaiHanhKhach
        + " ở chiều về"
        + "\nGiá vé: " + hienThiGiaVe
        + "?",
        "Xác nhận chọn ghế chiều về",
        javax.swing.JOptionPane.YES_NO_OPTION
);

    if (xacNhan != javax.swing.JOptionPane.YES_OPTION) {
    return;
}

String gheCu = hk.maGhe;

boolean giuThanhCong = daoVeTau.datGiuChoGhe(
        maChuyenVeDaChon,
        maGhe,
        maPhienGiuCho,
        maNhanVienDangNhap
);

if (!giuThanhCong) {
    hienThiDanhSachGheTheoToaChieuVe(toa);
    return;
}

if (gheCu != null && !gheCu.equals(maGhe)) {
    daoVeTau.xoaGiuChoGhe(
            maChuyenVeDaChon,
            gheCu,
            maPhienGiuCho
    );
}

    hk.maGhe = maGhe;
    hk.tenGhe = tenGhe;
    hk.tenToa = toa.getTenToa();
    hk.giaVe = giaVe;
    daoVeTau.capNhatChiTietGiuCho(
        maChuyenVeDaChon,
        maGhe,
        maPhienGiuCho,
        "VE",
        hk.stt,
        hk.loaiHanhKhach,
        hk.tenToa,
        hk.tenGhe,
        hk.giaVe
);
    luuTrangThaiPhienDangLam("CHON_GHE_VE");
    danhDauNhapLaiHanhKhach(viTriDangChonGheVe);
    
    
    maToaDangChonVe = toa.getMaToa();

    // Vẽ lại danh sách toa trước (hàm này sẽ reset header về rỗng)
    hienThiDanhSachToaChieuVe();

    // Sau đó set lại header tên toa đang chọn
    lblTenToaVe.setText(toa.getTenToa());
    if (toa.getLoaiGhe() != null) {
        lblLoaiGheToaVe.setText(" - " + toa.getLoaiGhe().getTenLoaiGhe());
    } else {
        lblLoaiGheToaVe.setText("");
    }

    hienThiDanhSachGheTheoToaChieuVe(toa);

    chuyenSangHanhKhachTiepTheoChieuVe();
    capNhatPanelThongTinVeChieuVe();
    capNhatPanel47TongKetVe();
}
        private void batDauDemNguocGiuCho(int soGiay) {
    dungDemNguocGiuCho();

    giayGiuChoConLai = Math.max(1, soGiay);
    capNhatTextNutTiepTuc();

    timerGiuCho = new javax.swing.Timer(1000, e -> {
        giayGiuChoConLai--;

        if (giayGiuChoConLai <= 0) {
            dungDemNguocGiuCho();
            xuLyHetGioGiuCho();
            return;
        }

        capNhatTextNutTiepTuc();
    });

    timerGiuCho.start();
}
    private void batDauDemNguocGiuChoNeuChuaChay() {
    if (timerGiuCho == null) {
        batDauDemNguocGiuCho();
    } else {
        capNhatTextNutTiepTuc();
    }
}


private void capNhatPanelThongTinVeChieuVe() {
    if (pnlThongTinVeChieuVe == null) {
        cauHinhScrollThongTinVeChieuVe();
    }

    pnlThongTinVeChieuVe.removeAll();

    for (int i = 0; i < dsGheChonVe.size(); i++) {
        GheHanhKhach g = dsGheChonVe.get(i);

        String gheText = g.maGhe == null
                ? "Chưa chọn ghế"
                : "Toa " + g.tenToa + " - Ghế " + g.tenGhe + " - " + dinhDangTien(g.giaVe);

        javax.swing.JButton btn = new javax.swing.JButton(
                "HK " + g.stt + " - " + g.loaiHanhKhach + " | " + gheText
        );

        btn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btn.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 35));
        btn.setPreferredSize(new java.awt.Dimension(160, 35));
        btn.setMinimumSize(new java.awt.Dimension(160, 35));

        final int index = i;
        btn.addActionListener(e -> {
            viTriDangChonGheVe = index;
            capNhatPanelThongTinVeChieuVe();

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Đang chọn ghế chiều về cho: Hành khách "
                    + dsGheChonVe.get(index).stt
                    + " - "
                    + dsGheChonVe.get(index).loaiHanhKhach
            );
        });

        if (i == viTriDangChonGheVe) {
            btn.setBackground(new java.awt.Color(255, 230, 150));
        }

        pnlThongTinVeChieuVe.add(btn);
        pnlThongTinVeChieuVe.add(javax.swing.Box.createVerticalStrut(5));
    }

    pnlThongTinVeChieuVe.revalidate();
    pnlThongTinVeChieuVe.repaint();

    if (scrollThongTinVeChieuVe != null) {
        scrollThongTinVeChieuVe.revalidate();
        scrollThongTinVeChieuVe.repaint();
    }

    jPanel44.revalidate();
    jPanel44.repaint();
}
    private boolean kiemTraTongSoVeHopLe() {
    int nguoiLon = Math.max(0, laySoLuong(txtNguoiLon));
    int treEm = Math.max(0, laySoLuong(txtTreEm));
    int nguoiCaoTuoi = Math.max(0, laySoLuong(txtNguoiCaoTuoi));
    int sinhVien = Math.max(0, laySoLuong(txtSinhVien));

    int tong = nguoiLon + treEm + nguoiCaoTuoi + sinhVien;

    if (tong <= 0) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Vui lòng chọn ít nhất 1 vé."
        );
        return false;
    }

    // Nếu có trẻ em thì phải có ít nhất 1 trong 3 loại vé còn lại
    if (treEm > 0 && nguoiLon == 0 && nguoiCaoTuoi == 0 && sinhVien == 0) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Trẻ em phải đi kèm ít nhất 1 hành khách người lớn, người cao tuổi hoặc sinh viên."
        );
        return false;
    }

    return true;
}
    private void cauHinhScrollNhapThongTinHanhKhach() {
    // jPanel41 là panel chứa các box nhập hành khách
    jPanel41.setLayout(new javax.swing.BoxLayout(jPanel41, javax.swing.BoxLayout.Y_AXIS));

    // Gỡ jPanel41 khỏi jPanel40 vì NetBeans đang add trực tiếp jPanel41 vào CENTER
    jPanel40.remove(jPanel41);

    scrollNhapThongTinHanhKhach = new javax.swing.JScrollPane(jPanel41);
    scrollNhapThongTinHanhKhach.setVerticalScrollBarPolicy(
            javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );
    scrollNhapThongTinHanhKhach.setHorizontalScrollBarPolicy(
            javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );

    // Cuộn mượt hơn
    scrollNhapThongTinHanhKhach.getVerticalScrollBar().setUnitIncrement(16);

    // Add scroll vào lại CENTER của jPanel40
    jPanel40.add(scrollNhapThongTinHanhKhach, java.awt.BorderLayout.CENTER);

    jPanel40.revalidate();
    jPanel40.repaint();
}
    private static final double DON_GIA_KM = 850.0;

private long tinhGiaVe(
        String maChuyen,
        com.mycompany.prj1.entity.ToaTau toa,
        String loaiHanhKhach,
        java.util.Date ngayDi
) {
    int cuLy = daoChuyenTau.layCuLyTheoMaChuyen(maChuyen);

    double heSoLoaiToa = 1.0;

    if (toa.getLoaiGhe() != null) {
        heSoLoaiToa = toa.getLoaiGhe().getHeSoLoaiGhe();
    }

    double heSoThoiGian = tinhHeSoThoiGian(ngayDi);
    double tyLeGiam = layTyLeGiamTheoLoaiHanhKhach(loaiHanhKhach);

    double giaVeGoc = DON_GIA_KM * cuLy * heSoLoaiToa * heSoThoiGian * (1 - tyLeGiam);

    // Làm tròn xuống, bỏ phần thập phân
    return ((long) Math.floor(giaVeGoc / 1000.0)) * 1000;
}
    private double layTyLeGiamTheoLoaiHanhKhach(String loaiHanhKhach) {
    if (loaiHanhKhach == null) {
        return 0.0;
    }

    String loai = loaiHanhKhach.toLowerCase();

    if (loai.contains("trẻ em")) {
        return 0.25;
    }

    if (loai.contains("người cao tuổi")) {
        return 0.15;
    }

    if (loai.contains("sinh viên")) {
        return 0.10;
    }

    return 0.0;
}
    private double tinhHeSoThoiGian(java.util.Date ngayDiDate) {
    if (ngayDiDate == null) {
        return 1.0;
    }

    java.time.LocalDate ngayDi = ngayDiDate.toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate();

    int ngay = ngayDi.getDayOfMonth();
    int thang = ngayDi.getMonthValue();

    // Cao điểm lễ: 29/4 - 3/5
    if ((thang == 4 && ngay >= 29) || (thang == 5 && ngay <= 3)) {
        return 1.2;
    }

    // Quốc khánh 2/9
    if (thang == 9 && ngay == 2) {
        return 1.2;
    }

    // Tạm xử lý cao điểm Tết bằng khoảng dương lịch ví dụ.
    // Muốn chính xác 20 tháng Chạp - 10 tháng Giêng thì cần bảng LichTet theo từng năm.
    if (laNgayCaoDiemTetTamThoi(ngayDi)) {
        return 1.4;
    }

    // Thấp điểm: tháng 3, 4, 5, 9, 10, 11
    if (thang == 3 || thang == 4 || thang == 5
            || thang == 9 || thang == 10 || thang == 11) {
        return 0.9;
    }

    java.time.DayOfWeek thu = ngayDi.getDayOfWeek();

    // Cuối tuần: thứ 6, thứ 7, chủ nhật
    if (thu == java.time.DayOfWeek.FRIDAY
            || thu == java.time.DayOfWeek.SATURDAY
            || thu == java.time.DayOfWeek.SUNDAY) {
        return 1.1;
    }

    // Ngày thường: thứ 2, 3, 4, 5
    return 1.0;
}
    private boolean laNgayCaoDiemTetTamThoi(java.time.LocalDate ngayDi) {
    // Tạm thời chưa tính âm lịch chính xác.
    // Sau này bạn có thể thay bằng bảng LichTet trong DB.

    int nam = ngayDi.getYear();

    // Ví dụ khoảng cao điểm Tết dương lịch giả định:
    // 01/02 đến 20/02 mỗi năm.
    java.time.LocalDate batDau = java.time.LocalDate.of(nam, 2, 1);
    java.time.LocalDate ketThuc = java.time.LocalDate.of(nam, 2, 20);

    return !ngayDi.isBefore(batDau) && !ngayDi.isAfter(ketThuc);
}
    private String dinhDangTien(double soTien) {
    long tienLamTron = ((long) Math.floor(soTien / 1000.0)) * 1000;

    java.text.NumberFormat nf =
            java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));

    return nf.format(tienLamTron) + " VNĐ";
}
    
    private boolean kiemTraNgaySinhTheoLoai(
        String loaiHanhKhach,
        com.toedter.calendar.JDateChooser dcNgaySinh
) {
    java.util.Date ngaySinhDate = dcNgaySinh.getDate();

    if (ngaySinhDate == null) {
        return false;
    }

    int tuoi = tinhTuoi(ngaySinhDate);

    if (tuoi < 0) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Ngày sinh không hợp lệ."
        );
        return false;
    }

    String loai = loaiHanhKhach.toLowerCase();

    if (loai.contains("người lớn") && tuoi < 18) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Hành khách loại Người lớn phải từ 18 tuổi trở lên.\n"
                + "Tuổi hiện tại: " + tuoi + "\n"
                + "Vui lòng kiểm tra lại thông tin và nhập lại ngày sinh."
        );
        return false;
    }

    if (loai.contains("trẻ em") && tuoi >= 18) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Hành khách loại Trẻ em phải nhỏ hơn 18 tuổi.\n"
                + "Tuổi hiện tại: " + tuoi + "\n"
                + "Vui lòng kiểm tra lại thông tin và nhập lại ngày sinh."
        );
        return false;
    }

    if (loai.contains("người cao tuổi") && tuoi < 60) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Hành khách loại Người cao tuổi phải từ 60 tuổi trở lên.\n"
                + "Tuổi hiện tại: " + tuoi + "\n"
                + "Vui lòng kiểm tra lại thông tin và nhập lại ngày sinh."
        );
        return false;
    }
    
    if (loai.contains("sinh viên") && tuoi < 18) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Hành khách loại Sinh viên phải từ 18 tuổi trở lên.\n"
                + "Tuổi hiện tại: " + tuoi + "\n"
                + "Vui lòng kiểm tra lại thông tin và nhập lại ngày sinh."
        );
        return false;
    }

    return true;
}
    private int tinhTuoi(java.util.Date ngaySinhDate) {
    if (ngaySinhDate == null) {
        return -1;
    }

    java.time.LocalDate ngaySinh = ngaySinhDate.toInstant()
            .atZone(java.time.ZoneId.systemDefault())
            .toLocalDate();

    java.time.LocalDate homNay = java.time.LocalDate.now();

    return java.time.Period.between(ngaySinh, homNay).getYears();
}
    private boolean kiemTraTatCaThongTinHanhKhach() {
    xoaTatCaCanhBaoHanhKhach();

    boolean hopLeTatCa = true;
    java.awt.Component componentDauTienBiLoi = null;

    for (int i = 0; i < dsFormHanhKhach.size(); i++) {
        FormHanhKhach f = dsFormHanhKhach.get(i);
        String prefix = "Hành khách " + (i + 1) + ": ";

        // 1. Kiểm tra họ tên
        if (laRongHoacPlaceholder(f.txtHoTen, "Họ và tên(ví dụ: Nguyễn Văn A)")) {
            hienLoi(f.txtHoTen, f.lblLoiHoTen, prefix + "chưa nhập họ tên");
            hopLeTatCa = false;

            if (componentDauTienBiLoi == null) {
                componentDauTienBiLoi = f.txtHoTen;
            }
        }

        // 2. Kiểm tra ngày sinh
        if (!kiemTraNgaySinhInline(f)) {
    hopLeTatCa = false;

    if (componentDauTienBiLoi == null) {
        componentDauTienBiLoi = f.dcNgaySinh;
    }
}

        // 3. Kiểm tra loại giấy tờ
        String loaiGT = f.cboLoaiGiayTo.getSelectedItem() == null
                ? ""
                : f.cboLoaiGiayTo.getSelectedItem().toString().trim();

        if (loaiGT.isEmpty()) {
            hienLoi(f.cboLoaiGiayTo, f.lblLoiLoaiGiayTo, prefix + "chưa chọn loại giấy tờ");
            hopLeTatCa = false;

            if (componentDauTienBiLoi == null) {
                componentDauTienBiLoi = f.cboLoaiGiayTo;
            }
        }

        // 4. Kiểm tra số giấy tờ
        if (!"KHONG_CO".equalsIgnoreCase(loaiGT)) {
            if (laRongHoacPlaceholder(f.txtSoGiayTo, "Số giấy tờ(CCCD/PASSPORT)")) {
                hienLoi(f.txtSoGiayTo, f.lblLoiSoGiayTo, prefix + "chưa nhập số giấy tờ");
                hopLeTatCa = false;

                if (componentDauTienBiLoi == null) {
                    componentDauTienBiLoi = f.txtSoGiayTo;
                }
            } else {
                if (!kiemTraGiayToInline(f)) {
                    hopLeTatCa = false;

                    if (componentDauTienBiLoi == null) {
                        componentDauTienBiLoi = f.txtSoGiayTo;
                    }
}
            }
        }

        // 5. Kiểm tra số điện thoại
        if (laRongHoacPlaceholder(f.txtSdt, "Số điện thoại")) {
            hienLoi(f.txtSdt, f.lblLoiSdt, prefix + "chưa nhập số điện thoại");
            hopLeTatCa = false;

            if (componentDauTienBiLoi == null) {
                componentDauTienBiLoi = f.txtSdt;
            }
        } else {
            if (!kiemTraSdtInline(f)) {
    hopLeTatCa = false;

    if (componentDauTienBiLoi == null) {
        componentDauTienBiLoi = f.txtSdt;
    }
}
        }
    }

    if (!hopLeTatCa) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Chưa nhập đủ thông tin hành khách.\n"
                + "Vui lòng kiểm tra các ô đang được cảnh báo màu đỏ."
        );

        if (componentDauTienBiLoi != null) {
            componentDauTienBiLoi.requestFocus();

            if (scrollNhapThongTinHanhKhach != null) {
                scrollNhapThongTinHanhKhach.getViewport().scrollRectToVisible(
                        componentDauTienBiLoi.getBounds()
                );
            }
        }

        return false;
    }

    return true;
}
    private boolean kiemTraGiayToHopLe(
        String loaiHanhKhach,
        javax.swing.JComboBox<String> cboLoaiGiayTo,
        javax.swing.JTextField txtSoGiayTo
) {
    String loaiGT = cboLoaiGiayTo.getSelectedItem() == null
            ? ""
            : cboLoaiGiayTo.getSelectedItem().toString().trim();

    String soGT = txtSoGiayTo.getText().trim();

    if (soGT.equals("Số giấy tờ")) {
        soGT = "";
    }

    // Trường hợp trẻ em không có giấy tờ
    if (loaiGT.equalsIgnoreCase("KHONG_CO")) {
        if (!loaiHanhKhach.toLowerCase().contains("trẻ em")) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Chỉ hành khách trẻ em mới được chọn KHONG_CO."
            );
            return false;
        }

        return true;
    }

    if (loaiGT.equalsIgnoreCase("CCCD")) {
    if (!soGT.matches("\\d{12}")) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "CCCD không hợp lệ.\n"
                + "CCCD phải gồm đúng 12 chữ số."
        );
        txtSoGiayTo.requestFocus();
        return false;
    }

    if (!laMaTinhCCCDHopLe(soGT)) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "CCCD không hợp lệ.\n"
                + "3 số đầu không thuộc mã tỉnh/thành hợp lệ."
        );
        txtSoGiayTo.requestFocus();
        return false;
    }

    return true;
}

    if (loaiGT.equalsIgnoreCase("PASSPORT")) {
        String passport = soGT.toUpperCase();

        if (!passport.matches("[A-Z0-9]{6,9}")) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "PASSPORT không hợp lệ.\n"
                    + "PASSPORT phải từ 6 đến 9 ký tự,\n"
                    + "chỉ gồm chữ in hoa A-Z và số 0-9."
            );
            txtSoGiayTo.requestFocus();
            return false;
        }

        txtSoGiayTo.setText(passport);
        return true;
    }

    javax.swing.JOptionPane.showMessageDialog(
            this,
            "Loại giấy tờ không hợp lệ."
    );
    return false;
}
    private int demSoGheDaChonTrongToa(java.util.List<GheHanhKhach> ds, String tenToa) {
    int dem = 0;

    if (tenToa == null) {
        return 0;
    }

    for (GheHanhKhach g : ds) {
        if (g.tenToa != null && g.tenToa.equalsIgnoreCase(tenToa)) {
            dem++;
        }
    }

    return dem;
}
    
    private void capNhatMauNutToa(
        javax.swing.JButton btnToa,
        com.mycompany.prj1.entity.ToaTau toa,
        String maToaDangChon,
        java.util.List<GheHanhKhach> ds,
        String maChuyenTau
) {
    boolean day = toaDaDay(toa, ds, maChuyenTau);
    boolean dangChon = toa.getMaToa() != null
            && toa.getMaToa().equals(maToaDangChon);

    if (day) {
        btnToa.setBackground(new java.awt.Color(220, 53, 69));
        btnToa.setForeground(java.awt.Color.WHITE);
        btnToa.setText(
                toa.getTenToa()
                + "-"
                + toa.getLoaiGhe().getTenLoaiGhe()
                + " (Đầy)"
        );
        btnToa.setToolTipText("Toa này đã hết ghế.");
    } else if (dangChon) {
        btnToa.setBackground(new java.awt.Color(0, 120, 215));
        btnToa.setForeground(java.awt.Color.WHITE);
        btnToa.setToolTipText("Toa đang chọn.");
    } else {
        btnToa.setBackground(null);
        btnToa.setForeground(java.awt.Color.BLACK);
        //btnToa.setToolTipText("Toa còn ghế.");
    }

    btnToa.setOpaque(true);
    btnToa.setContentAreaFilled(true);
    btnToa.setBorderPainted(true);
}
    
    
    private boolean toaDaDay(
        com.mycompany.prj1.entity.ToaTau toa,
        java.util.List<GheHanhKhach> ds,
        String maChuyenTau
) {
    int soGheDaChonTrongPhien =
            demSoGheDaChonTrongToa(ds, toa.getTenToa());

    int soGheDaBanHoacDangGiu =
            daoVeTau.demGheDaBanHoacDangGiuTrongToa(
                    maChuyenTau,
                    maPhienGiuCho,
                    toa.getTenToa()
            );

    int tongGheKhongTrong = soGheDaChonTrongPhien + soGheDaBanHoacDangGiu;

    return tongGheKhongTrong >= toa.getSucChua();
}
    
    private boolean laMaTinhCCCDHopLe(String cccd) {
    if (cccd == null || cccd.length() < 3) {
        return false;
    }

    String maTinh = cccd.substring(0, 3);

    String[] dsMaTinh = {
        "001", "002", "004", "006", "008", "010", "011", "012", "014", "015",
        "017", "019", "020", "022", "024", "025", "026", "027", "030", "031",
        "033", "034", "035", "036", "037", "038", "040", "042", "044", "045",
        "046", "048", "049", "051", "052", "054", "056", "058", "060", "062",
        "064", "066", "067", "068", "070", "072", "074", "075", "077", "079",
        "080", "082", "083", "084", "086", "087", "089", "091", "092", "093",
        "094", "095", "096"
    };

    for (String ma : dsMaTinh) {
        if (ma.equals(maTinh)) {
            return true;
        }
    }

    return false;
}
    private boolean kiemTraSdtHopLe(javax.swing.JTextField txtSdt) {
    String sdt = txtSdt.getText().trim();

    if (sdt.equals("Số điện thoại")) {
        sdt = "";
    }

    if (sdt.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Vui lòng nhập số điện thoại."
        );
        txtSdt.requestFocus();
        return false;
    }

    if (!sdt.matches("\\d{10}")) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Số điện thoại không hợp lệ.\n"
                + "Số điện thoại phải gồm đúng 10 chữ số."
        );
        txtSdt.requestFocus();
        return false;
    }

    // Kiểm tra đầu số di động Việt Nam phổ biến
    if (!sdt.matches("^(03|05|07|08|09)\\d{8}$")) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Số điện thoại không hợp lệ.\n"
                + "Số điện thoại phải bắt đầu bằng 03, 05, 07, 08 hoặc 09."
        );
        txtSdt.requestFocus();
        return false;
    }

    return true;
}
    private void capNhatPanel47TongKetVe() {
    jPanel47.removeAll();
    jPanel47.setLayout(new javax.swing.BoxLayout(jPanel47, javax.swing.BoxLayout.Y_AXIS));
    jPanel47.setBackground(java.awt.Color.WHITE);
    jPanel47.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createTitledBorder(
                    javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)),
                    "Tóm tắt vé"
            ),
            javax.swing.BorderFactory.createEmptyBorder(8, 8, 8, 8)
    ));

    double tongTien = 0;

    // =========================
    // CHIỀU ĐI
    // =========================
    if (dsGheChonDi != null && !dsGheChonDi.isEmpty()) {
        javax.swing.JLabel lblChieuDi = new javax.swing.JLabel("CHIỀU ĐI");
        lblChieuDi.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblChieuDi.setForeground(new java.awt.Color(0, 102, 204));
        lblChieuDi.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        jPanel47.add(lblChieuDi);
        jPanel47.add(javax.swing.Box.createVerticalStrut(6));

        for (GheHanhKhach g : dsGheChonDi) {
            if (g.maGhe == null) {
                continue;
            }

            tongTien += g.giaVe;

            javax.swing.JPanel dong = taoDongTomTatVe(g);
            jPanel47.add(dong);
            jPanel47.add(javax.swing.Box.createVerticalStrut(5));
        }

        jPanel47.add(javax.swing.Box.createVerticalStrut(8));
    }

    // =========================
    // CHIỀU VỀ
    // =========================
    if (dsGheChonVe != null && !dsGheChonVe.isEmpty()) {
        javax.swing.JLabel lblChieuVe = new javax.swing.JLabel("CHIỀU VỀ");
        lblChieuVe.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
        lblChieuVe.setForeground(new java.awt.Color(0, 102, 204));
        lblChieuVe.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

        jPanel47.add(lblChieuVe);
        jPanel47.add(javax.swing.Box.createVerticalStrut(6));

        for (GheHanhKhach g : dsGheChonVe) {
            if (g.maGhe == null) {
                continue;
            }

            tongTien += g.giaVe;

            javax.swing.JPanel dong = taoDongTomTatVe(g);
            jPanel47.add(dong);
            jPanel47.add(javax.swing.Box.createVerticalStrut(5));
        }

        jPanel47.add(javax.swing.Box.createVerticalStrut(8));
    }

    // =========================
    // TỔNG TIỀN
    // =========================
    javax.swing.JPanel pnTong = new javax.swing.JPanel(new java.awt.BorderLayout());
    pnTong.setBackground(java.awt.Color.WHITE);
    pnTong.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createMatteBorder(
                    1, 0, 0, 0,
                    new java.awt.Color(200, 200, 200)
            ),
            javax.swing.BorderFactory.createEmptyBorder(10, 0, 0, 0)
    ));
    pnTong.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 40));
    pnTong.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

    javax.swing.JLabel lblTongText = new javax.swing.JLabel("TỔNG TIỀN");
    lblTongText.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));

    javax.swing.JLabel lblTongTien = new javax.swing.JLabel(dinhDangTien(tongTien));
    lblTongTien.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    lblTongTien.setForeground(new java.awt.Color(220, 0, 0));

    pnTong.add(lblTongText, java.awt.BorderLayout.WEST);
    pnTong.add(lblTongTien, java.awt.BorderLayout.EAST);

    jPanel47.add(pnTong);

    jPanel47.revalidate();
    jPanel47.repaint();
}
    private javax.swing.JPanel taoDongTomTatVe(GheHanhKhach g) {
    javax.swing.JPanel dong = new javax.swing.JPanel(new java.awt.BorderLayout());
    dong.setBackground(java.awt.Color.WHITE);
    dong.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(230, 230, 230)),
            javax.swing.BorderFactory.createEmptyBorder(6, 6, 6, 6)
    ));
    dong.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 45));
    dong.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);

    String thongTin =
            g.loaiHanhKhach + " " + g.stt
            + " - Toa " + g.tenToa
            + " - Ghế " + g.tenGhe;

    javax.swing.JLabel lblThongTin = new javax.swing.JLabel(thongTin);
    lblThongTin.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));

    javax.swing.JLabel lblGia = new javax.swing.JLabel(dinhDangTien(g.giaVe));
    lblGia.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 12));

    dong.add(lblThongTin, java.awt.BorderLayout.WEST);
    dong.add(lblGia, java.awt.BorderLayout.EAST);

    return dong;
}
    
    private String layTextKhongPlaceholder(javax.swing.JTextField txt, String placeholder) {
    String text = txt.getText().trim();

    if (text.equals(placeholder)) {
        return "";
    }

    return text;
}
    private void inVeDaChon() {
    String noiDungVe = taoNoiDungVeDeIn();

    javax.swing.JTextArea txtVe = new javax.swing.JTextArea(noiDungVe);
    txtVe.setFont(new java.awt.Font("Monospaced", java.awt.Font.PLAIN, 10));
    txtVe.setEditable(false);

    try {
        boolean daIn = txtVe.print(
                null,
                null,
                true,
                null,
                null,
                true
        );

        if (daIn) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Đã gửi vé tới máy in."
            );
        } else {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Bạn đã hủy thao tác in."
            );
        }

    } catch (HeadlessException | PrinterException e) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Không thể in vé.\nVui lòng kiểm tra máy in."
        );
    }
}
    private boolean xuatVeRaPDF() {
    String noiDungVe = taoNoiDungVeDeIn();

    File thuMucLuu = new File("D:\\NAM3_ki2\\PTUD\\pdf");

    if (!thuMucLuu.exists()) {
        thuMucLuu.mkdirs();
    }

    JFileChooser chooser = new JFileChooser();

    // Đây mới là thư mục mở sẵn
    chooser.setCurrentDirectory(thuMucLuu);

    // Đây chỉ là tiêu đề hộp thoại
    chooser.setDialogTitle("Chọn nơi lưu file PDF");

    chooser.setSelectedFile(new File("ve_tau_" + System.currentTimeMillis() + ".pdf"));
    chooser.setFileFilter(new FileNameExtensionFilter("PDF (*.pdf)", "pdf"));

    int ketQua = chooser.showSaveDialog(this);

    if (ketQua != JFileChooser.APPROVE_OPTION) {
        return false;
    }

    File file = chooser.getSelectedFile();

    if (!file.getName().toLowerCase().endsWith(".pdf")) {
        file = new File(file.getAbsolutePath() + ".pdf");
    }

    try (PDDocument document = new PDDocument()) {
        PDType0Font font = taiFontTiengViet(document);

        float margin = 50;
        float fontSize = 11;
        float leading = 15;

        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream content = new PDPageContentStream(document, page);

        float y = page.getMediaBox().getHeight() - margin;
        float startX = margin;

        content.beginText();
        content.setFont(font, fontSize);
        content.newLineAtOffset(startX, y);

        String[] lines = noiDungVe.split("\n");

        for (String line : lines) {
            if (y <= margin) {
                content.endText();
                content.close();

                page = new PDPage(PDRectangle.A4);
                document.addPage(page);

                content = new PDPageContentStream(document, page);
                y = page.getMediaBox().getHeight() - margin;

                content.beginText();
                content.setFont(font, fontSize);
                content.newLineAtOffset(startX, y);
            }

            content.showText(line);
            content.newLineAtOffset(0, -leading);
            y -= leading;
        }

        content.endText();
        content.close();

        document.save(file);

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Xuất PDF thành công:\n" + file.getAbsolutePath()
        );
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Không thể xuất PDF.\nLỗi: " + e.getMessage()
        );
        return false;
    }
}
    private PDType0Font taiFontTiengViet(PDDocument document) throws IOException {
    String[] dsFont = {
        "C:/Windows/Fonts/arial.ttf",
        "C:/Windows/Fonts/times.ttf",
        "C:/Windows/Fonts/calibri.ttf"
    };

    for (String path : dsFont) {
        File fontFile = new File(path);

        if (fontFile.exists()) {
            return PDType0Font.load(document, fontFile);
        }
    }

    throw new IOException("Không tìm thấy font tiếng Việt trên máy.");
}
    private String taoNoiDungVeDeIn() {
    StringBuilder sb = new StringBuilder();

    double tongTien = 0;

    sb.append("========================================\n");
    sb.append("              VE TAU HOA\n");
    sb.append("              GA SAI GON\n");
    sb.append("========================================\n\n");

    sb.append("THOI GIAN LAP VE: ")
      .append(java.time.LocalDateTime.now()
              .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")))
      .append("\n");

    sb.append("NHAN VIEN: ")
      .append(maNhanVienDangNhap == null || maNhanVienDangNhap.isBlank()
              ? "Khong xac dinh"
              : maNhanVienDangNhap)
      .append("\n");

    sb.append("========================================\n\n");

    // =========================
    // CHIEU DI
    // =========================
    sb.append("CHIEU DI\n");
    sb.append("----------------------------------------\n");
    sb.append("Ma chuyen: ").append(giaTriRongNeuNull(maChuyenDiDaChon)).append("\n");
    sb.append("Ten chuyen: ").append(giaTriRongNeuNull(tenChuyenDiDaChon)).append("\n");
    sb.append("Tau: ").append(giaTriRongNeuNull(tenTauChuyenDi)).append("\n");
    sb.append("Tuyen: ")
      .append(giaTriRongNeuNull(gaDiChuyenDi))
      .append(" -> ")
      .append(giaTriRongNeuNull(gaDenChuyenDi))
      .append("\n");

    if (dcNgayDi != null && dcNgayDi.getDate() != null) {
        sb.append("Ngay di: ")
          .append(new java.text.SimpleDateFormat("dd/MM/yyyy").format(dcNgayDi.getDate()))
          .append("\n");
    }

    sb.append("----------------------------------------\n");

    for (int i = 0; i < dsGheChonDi.size(); i++) {
        GheHanhKhach ghe = dsGheChonDi.get(i);

        if (ghe.maGhe == null) {
            continue;
        }

        FormHanhKhach form = null;
        if (i < dsFormHanhKhach.size()) {
            form = dsFormHanhKhach.get(i);
        }

        sb.append("VE ").append(i + 1).append("\n");
        sb.append("Ma ve: ")
        .append(ghe.maVe == null ? "Chua luu DB" : ghe.maVe)
        .append("\n");

        if (form != null) {
            sb.append("Hanh khach: ")
              .append(layTextKhongPlaceholder(
                      form.txtHoTen,
                      "Họ và tên(ví dụ: Nguyễn Văn A)"
              ))
              .append("\n");

            sb.append("Loai hanh khach: ")
              .append(ghe.loaiHanhKhach)
              .append("\n");

            if (form.dcNgaySinh != null && form.dcNgaySinh.getDate() != null) {
                sb.append("Ngay sinh: ")
                  .append(new java.text.SimpleDateFormat("dd/MM/yyyy")
                          .format(form.dcNgaySinh.getDate()))
                  .append("\n");
            }

            sb.append("So dien thoai: ")
              .append(layTextKhongPlaceholder(
                      form.txtSdt,
                      "Số điện thoại"
              ))
              .append("\n");

            String loaiGiayTo = "";
            if (form.cboLoaiGiayTo.getSelectedItem() != null) {
                loaiGiayTo = form.cboLoaiGiayTo.getSelectedItem().toString();
            }

            String soGiayTo = layTextKhongPlaceholder(
                    form.txtSoGiayTo,
                    "Số giấy tờ(CCCD/PASSPORT)"
            );

            sb.append("Giay to: ")
              .append(loaiGiayTo);

            if (!soGiayTo.isBlank()) {
                sb.append(" - ").append(soGiayTo);
            }

            sb.append("\n");
        }

        sb.append("Toa: ")
          .append(ghe.tenToa)
          .append("    Ghe: ")
          .append(ghe.tenGhe)
          .append("\n");

        sb.append("Gia ve: ")
          .append(dinhDangTien(ghe.giaVe))
          .append("\n");

        sb.append("----------------------------------------\n");

        tongTien += ghe.giaVe;
    }

    // =========================
    // CHIEU VE NEU LA KHU HOI
    // =========================
    if (jRadioButton2.isSelected()
            && dsGheChonVe != null
            && !dsGheChonVe.isEmpty()) {

        sb.append("\nCHIEU VE\n");
        sb.append("----------------------------------------\n");
        sb.append("Ma chuyen: ").append(giaTriRongNeuNull(maChuyenVeDaChon)).append("\n");
        sb.append("Ten chuyen: ").append(giaTriRongNeuNull(tenChuyenVeDaChon)).append("\n");
        sb.append("Tau: ").append(giaTriRongNeuNull(tenTauChuyenVe)).append("\n");
        sb.append("Tuyen: ")
          .append(giaTriRongNeuNull(gaDiChuyenVe))
          .append(" -> ")
          .append(giaTriRongNeuNull(gaDenChuyenVe))
          .append("\n");

        if (dcNgayVe != null && dcNgayVe.getDate() != null) {
            sb.append("Ngay ve: ")
              .append(new java.text.SimpleDateFormat("dd/MM/yyyy").format(dcNgayVe.getDate()))
              .append("\n");
        }

        sb.append("----------------------------------------\n");

        for (int i = 0; i < dsGheChonVe.size(); i++) {
            GheHanhKhach ghe = dsGheChonVe.get(i);

            if (ghe.maGhe == null) {
                continue;
            }

            FormHanhKhach form = null;
            if (i < dsFormHanhKhach.size()) {
                form = dsFormHanhKhach.get(i);
            }

            sb.append("VE ").append(i + 1).append("\n");
            sb.append("Ma ve: ")
              .append(ghe.maVe == null ? "Chua luu DB" : ghe.maVe)
              .append("\n");
            if (form != null) {
                sb.append("Hanh khach: ")
                  .append(layTextKhongPlaceholder(
                          form.txtHoTen,
                          "Họ và tên(ví dụ: Nguyễn Văn A)"
                  ))
                  .append("\n");

                sb.append("Loai hanh khach: ")
                  .append(ghe.loaiHanhKhach)
                  .append("\n");

                if (form.dcNgaySinh != null && form.dcNgaySinh.getDate() != null) {
                    sb.append("Ngay sinh: ")
                      .append(new java.text.SimpleDateFormat("dd/MM/yyyy")
                              .format(form.dcNgaySinh.getDate()))
                      .append("\n");
                }

                sb.append("So dien thoai: ")
                  .append(layTextKhongPlaceholder(
                          form.txtSdt,
                          "Số điện thoại"
                  ))
                  .append("\n");

                String loaiGiayTo = "";
                if (form.cboLoaiGiayTo.getSelectedItem() != null) {
                    loaiGiayTo = form.cboLoaiGiayTo.getSelectedItem().toString();
                }

                String soGiayTo = layTextKhongPlaceholder(
                        form.txtSoGiayTo,
                        "Số giấy tờ(CCCD/PASSPORT)"
                );

                sb.append("Giay to: ")
                  .append(loaiGiayTo);

                if (!soGiayTo.isBlank()) {
                    sb.append(" - ").append(soGiayTo);
                }

                sb.append("\n");
            }

            sb.append("Toa: ")
              .append(ghe.tenToa)
              .append("    Ghe: ")
              .append(ghe.tenGhe)
              .append("\n");

            sb.append("Gia ve: ")
              .append(dinhDangTien(ghe.giaVe))
              .append("\n");

            sb.append("----------------------------------------\n");

            tongTien += ghe.giaVe;
        }
    }

    sb.append("\n========================================\n");
    sb.append("TONG TIEN: ").append(dinhDangTien(tongTien)).append("\n");
    sb.append("========================================\n");
    sb.append("        CAM ON QUY KHACH\n");
    sb.append("     CHUC QUY KHACH THUONG LO\n");
    sb.append("========================================\n");

    return sb.toString();
}
    private String giaTriRongNeuNull(String value) {
    if (value == null || value.trim().isEmpty()) {
        return "";
    }
    return value.trim();
}
    private void hienThiChonHinhThucThanhToan() {
    Object[] luaChon = {"Tiền mặt", "Chuyển khoản MoMo", "Hủy"};

    int chon = javax.swing.JOptionPane.showOptionDialog(
            this,
            "Thông tin hành khách hợp lệ.\nVui lòng chọn hình thức thanh toán:",
            "Chọn hình thức thanh toán",
            javax.swing.JOptionPane.DEFAULT_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,
            luaChon,
            luaChon[0]
    );

    if (chon == 0) {
        xuLyThanhToanTienMat();
    } else if (chon == 1) {
        xuLyThanhToanChuyenKhoan();
    }
}
    private void xuLyThanhToanTienMat() {
    int xacNhan = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Xác nhận khách đã thanh toán bằng tiền mặt?",
            "Xác nhận thanh toán",
            javax.swing.JOptionPane.YES_NO_OPTION
    );

    if (xacNhan != javax.swing.JOptionPane.YES_OPTION) {
        return;
    }

    hienThiLuaChonXuatVe();
}
    private void xuLyThanhToanChuyenKhoan() {
    hienThiQRMoMo();
}
    private void hienThiQRMoMo() {
    javax.swing.JDialog dialog = new javax.swing.JDialog(this, "Thanh toán MoMo", true);
    dialog.setSize(420, 560);
    dialog.setLocationRelativeTo(this);
    dialog.setResizable(false);

    javax.swing.JPanel panel = new javax.swing.JPanel();
    panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
    panel.setBorder(javax.swing.BorderFactory.createEmptyBorder(15, 15, 15, 15));

    javax.swing.JLabel lblTieuDe = new javax.swing.JLabel("QUÉT MÃ QR MOMO ĐỂ THANH TOÁN");
    lblTieuDe.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 16));
    lblTieuDe.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

    javax.swing.JLabel lblSoTien = new javax.swing.JLabel("Số tiền: " + dinhDangTien(tinhTongTienTatCaVe()));
    lblSoTien.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 14));
    lblSoTien.setForeground(new java.awt.Color(220, 0, 0));
    lblSoTien.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

    javax.swing.JLabel lblQR = new javax.swing.JLabel();
    lblQR.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
    lblQR.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    try {
        java.net.URL url = getClass().getResource("/com/mycompany/prj1/img/qr (1).jpg");

        if (url != null) {
            java.awt.Image img = javax.imageio.ImageIO.read(url);
            java.awt.Image scaled = img.getScaledInstance(300, 300, java.awt.Image.SCALE_SMOOTH);
            lblQR.setIcon(new javax.swing.ImageIcon(scaled));
        } else {
            lblQR.setText("Không tìm thấy ảnh QR MoMo.");
        }
    } catch (IOException e) {
        lblQR.setText("Lỗi tải ảnh QR MoMo.");
    }

    javax.swing.JLabel lblHuongDan = new javax.swing.JLabel(
            "<html><div style='text-align:center;'>"
            + "Sau khi khách quét mã và chuyển khoản thành công,<br>"
            + "nhấn nút <b>Tôi đã nhận tiền</b> để xuất vé."
            + "</div></html>"
    );
    lblHuongDan.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

    javax.swing.JButton btnDaNhanTien = new javax.swing.JButton("Tôi đã nhận tiền");
    btnDaNhanTien.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

    javax.swing.JButton btnHuy = new javax.swing.JButton("Hủy");
    btnHuy.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);

    btnDaNhanTien.addActionListener(e -> {
        dialog.dispose();
        hienThiLuaChonXuatVe();
    });

    btnHuy.addActionListener(e -> dialog.dispose());

    panel.add(lblTieuDe);
    panel.add(javax.swing.Box.createVerticalStrut(15));
    panel.add(lblSoTien);
    panel.add(javax.swing.Box.createVerticalStrut(15));
    panel.add(lblQR);
    panel.add(javax.swing.Box.createVerticalStrut(15));
    panel.add(lblHuongDan);
    panel.add(javax.swing.Box.createVerticalStrut(20));
    panel.add(btnDaNhanTien);
    panel.add(javax.swing.Box.createVerticalStrut(8));
    panel.add(btnHuy);

    dialog.setContentPane(panel);
    dialog.setVisible(true);
}
    private double tinhTongTienTatCaVe() {
    long tongTien = 0;

    if (dsGheChonDi != null) {
        for (GheHanhKhach g : dsGheChonDi) {
            if (g.maGhe != null) {
                tongTien += g.giaVe;
            }
        }
    }

    if (jRadioButton2.isSelected() && dsGheChonVe != null) {
        for (GheHanhKhach g : dsGheChonVe) {
            if (g.maGhe != null) {
                tongTien += g.giaVe;
            }
        }
    }

    return tongTien;
}
private void hienThiLuaChonXuatVe() {
    Object[] luaChon = {"Xuất PDF", "In vé", "Hủy"};

    int chon = javax.swing.JOptionPane.showOptionDialog(
            this,
            "Thanh toán thành công.\nBạn muốn xuất vé theo hình thức nào?",
            "Xuất vé",
            javax.swing.JOptionPane.DEFAULT_OPTION,
            javax.swing.JOptionPane.QUESTION_MESSAGE,
            null,
            luaChon,
            luaChon[0]
    );

    if (chon == 0) {
        if (!luuTatCaVeVaoDB()) {
            return;
        }

        huyGiuChoHienTai();
        daoVeTau.hoanThanhPhienDatVe(maPhienGiuCho);
        capNhatDoanhThuHomNay();
        capNhatSoVeDaBanHomNay();
        boolean daXuat = xuatVeRaPDF();

        if (!daXuat) {
            return;
        }
        capNhatLaiGiaoDienGheSauKhiBan();
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Hoàn tất bán vé.\nHệ thống sẽ quay về trang chủ."
        );

        resetThongTinDatVe();
        moGDTrangChu();

    } else if (chon == 1) {
        if (!luuTatCaVeVaoDB()) {
            return;
        }

        huyGiuChoHienTai();
        daoVeTau.hoanThanhPhienDatVe(maPhienGiuCho);
        capNhatDoanhThuHomNay();
        capNhatSoVeDaBanHomNay();
        inVeDaChon();
        capNhatLaiGiaoDienGheSauKhiBan();

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Hoàn tất bán vé.\nHệ thống sẽ quay về trang chủ."
        );

        resetThongTinDatVe();
        moGDTrangChu();

    } else if (chon == 2 || chon == javax.swing.JOptionPane.CLOSED_OPTION) {
        int xacNhan = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Bạn chưa xuất/in vé.\n"
                + "Nếu hủy, các ghế đang giữ sẽ được mở lại.\n"
                + "Bạn có chắc muốn hủy không?",
                "Xác nhận hủy",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (xacNhan == javax.swing.JOptionPane.YES_OPTION) {
            huyGiuChoHienTai();
            daoVeTau.huyPhienDatVe(maPhienGiuCho);
            resetThongTinDatVe();
            moGDTrangChu();
        }
    }
}

    private void capNhatLaiGiaoDienGheSauKhiBan() {
    if (maToaDangChonDi != null) {
        hienThiDanhSachToaChieuDi();
    }

    if (maToaDangChonVe != null) {
        hienThiDanhSachToaChieuVe();
    }
}
    private void moManHinhNhapThongTinHanhKhach() {
    int tongSoHanhKhach = layTongSoHanhKhach();

    /*
     * Nếu chưa có form, hoặc số lượng hành khách thay đổi,
     * thì tạo lại toàn bộ form.
     */
    if (dsFormHanhKhach == null
            || dsFormHanhKhach.isEmpty()
            || dsFormHanhKhach.size() != tongSoHanhKhach) {

        hienThiFormHanhKhachVaoJPanel41();
        dsIndexHanhKhachCanNhapLai.clear();

    } else {
        /*
         * Nếu form đã có rồi thì chỉ xóa thông tin
         * của những hành khách có ghế bị đổi.
         */
        for (Integer index : dsIndexHanhKhachCanNhapLai) {
            xoaThongTinMotHanhKhach(index);
        }

        dsIndexHanhKhachCanNhapLai.clear();
    }
    batDauDemNguocGiuChoNeuChuaChay();
    java.awt.CardLayout cl = (java.awt.CardLayout) jPanelNoi_Dung.getLayout();
    cl.show(jPanelNoi_Dung, "card7");
}
    private boolean daNhapThongTinHanhKhach() {
    return dsFormHanhKhach != null && !dsFormHanhKhach.isEmpty();
}
    private int layTongSoHanhKhach() {
    int nguoiLon = Math.max(0, laySoLuong(txtNguoiLon));
    int treEm = Math.max(0, laySoLuong(txtTreEm));
    int nguoiCaoTuoi = Math.max(0, laySoLuong(txtNguoiCaoTuoi));
    int sinhVien = Math.max(0, laySoLuong(txtSinhVien));

    return nguoiLon + treEm + nguoiCaoTuoi + sinhVien;
}
    private void danhDauNhapLaiHanhKhach(int index) {
    if (daNhapThongTinHanhKhach()) {
        dsIndexHanhKhachCanNhapLai.add(index);
    }
}
    private void xoaThongTinMotHanhKhach(int index) {
    if (index < 0 || index >= dsFormHanhKhach.size()) {
        return;
    }

    FormHanhKhach f = dsFormHanhKhach.get(index);

    f.txtHoTen.setText("Họ và tên(ví dụ: Nguyễn Văn A)");
    f.txtHoTen.setForeground(java.awt.Color.GRAY);

    f.dcNgaySinh.setDate(null);

    if (f.loaiHanhKhach.toLowerCase().contains("trẻ em")) {
        f.cboLoaiGiayTo.setSelectedItem("KHONG_CO");
        f.txtSoGiayTo.setText("");
        f.txtSoGiayTo.setEnabled(false);
    } else {
        f.cboLoaiGiayTo.setSelectedIndex(0);
        f.txtSoGiayTo.setEnabled(true);
        f.txtSoGiayTo.setText("Số giấy tờ(CCCD/PASSPORT)");
        f.txtSoGiayTo.setForeground(java.awt.Color.GRAY);
    }

    f.txtSdt.setText("Số điện thoại");
    f.txtSdt.setForeground(java.awt.Color.GRAY);
}
    private String luuHoacLayMaHanhKhachTuForm(FormHanhKhach f) {
    String hoTen = layTextKhongPlaceholder(
            f.txtHoTen,
            "Họ và tên(ví dụ: Nguyễn Văn A)"
    );

    String sdt = layTextKhongPlaceholder(
            f.txtSdt,
            "Số điện thoại"
    );

    String loaiGiayTo = "";
    if (f.cboLoaiGiayTo.getSelectedItem() != null) {
        loaiGiayTo = f.cboLoaiGiayTo.getSelectedItem().toString();
    }

    String soGiayTo = layTextKhongPlaceholder(
            f.txtSoGiayTo,
            "Số giấy tờ(CCCD/PASSPORT)"
    );

    if ("KHONG_CO".equalsIgnoreCase(loaiGiayTo)) {
        soGiayTo = null;
    }

    java.time.LocalDate ngaySinh = null;

    if (f.dcNgaySinh != null && f.dcNgaySinh.getDate() != null) {
        ngaySinh = f.dcNgaySinh.getDate()
                .toInstant()
                .atZone(java.time.ZoneId.systemDefault())
                .toLocalDate();
    }

    String maLoai = layMaLoaiHanhKhach(f.loaiHanhKhach);

    String maHK = daoHanhKhach.themHoacLayMaHanhKhach(
            hoTen,
            ngaySinh,
            loaiGiayTo,
            soGiayTo,
            sdt,
            maLoai
    );

    if (maHK == null) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Không lấy được mã hành khách.\n"
                + "Họ tên: " + hoTen + "\n"
                + "SĐT: " + sdt + "\n"
                + "Loại giấy tờ: " + loaiGiayTo + "\n"
                + "Số giấy tờ: " + soGiayTo + "\n"
                + "Mã loại: " + maLoai
        );
    }

    return maHK;
}
    private boolean daLuuVeVaoDB = false;

private boolean luuTatCaVeVaoDB() {
    if (daLuuVeVaoDB) {
        return true;
    }

    String loaiVe = jRadioButton2.isSelected() ? "KHU_HOI" : "MOT_CHIEU";
    String maKhuyenMai = null; // tạm thời chưa áp dụng khuyến mãi

    // =========================
    // LƯU VÉ CHIỀU ĐI
    // =========================
    for (int i = 0; i < dsGheChonDi.size(); i++) {
        GheHanhKhach ghe = dsGheChonDi.get(i);

        if (ghe.maGhe == null) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Hành khách " + (i + 1) + " chưa chọn ghế.\n"
                + "Vui lòng chọn đủ ghế trước khi lưu vé."
        );
        return false;
}

        if (i >= dsFormHanhKhach.size()) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Không tìm thấy thông tin hành khách cho vé " + (i + 1)
            );
            return false;
        }

        FormHanhKhach form = dsFormHanhKhach.get(i);
        String maHanhKhach = luuHoacLayMaHanhKhachTuForm(form);
        if (maHanhKhach == null || maHanhKhach.trim().isEmpty()) {
            return false;
        }
        if (maHanhKhach == null) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Không thể lưu thông tin hành khách " + (i + 1)
            );
            return false;
        }

        String maVe = daoVeTau.themVeVaLayMaVe(
                maNhanVienDangNhap,
                maHanhKhach,
                ghe.maGhe,
                loaiVe,
                maChuyenDiDaChon,
                maKhuyenMai,
                ghe.giaVe
        );

        if (maVe == null) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Không thể lưu vé chiều đi cho hành khách " + (i + 1)
            );
            return false;
        }

        ghe.maVe = maVe;
    }

    // =========================
    // LƯU VÉ CHIỀU VỀ NẾU LÀ KHỨ HỒI
    // =========================
    if (jRadioButton2.isSelected() && dsGheChonVe != null) {
        for (int i = 0; i < dsGheChonVe.size(); i++) {
            GheHanhKhach ghe = dsGheChonVe.get(i);

            if (ghe.maGhe == null) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Hành khách " + (i + 1) + " chưa chọn ghế chiều về.\n"
                    + "Vui lòng chọn đủ ghế trước khi lưu vé."
            );
            return false;
}

            if (i >= dsFormHanhKhach.size()) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Không tìm thấy thông tin hành khách cho vé chiều về " + (i + 1)
                );
                return false;
            }

            FormHanhKhach form = dsFormHanhKhach.get(i);
            String maHanhKhach = luuHoacLayMaHanhKhachTuForm(form);

            if (maHanhKhach == null) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Không thể lưu thông tin hành khách " + (i + 1)
                );
                return false;
            }

            String maVe = daoVeTau.themVeVaLayMaVe(
                    maNhanVienDangNhap,
                    maHanhKhach,
                    ghe.maGhe,
                    loaiVe,
                    maChuyenVeDaChon,
                    maKhuyenMai,
                    ghe.giaVe
            );

            if (maVe == null) {
                javax.swing.JOptionPane.showMessageDialog(
                        this,
                        "Không thể lưu vé chiều về cho hành khách " + (i + 1)
                );
                return false;
            }

            ghe.maVe = maVe;
        }
    }

    daLuuVeVaoDB = true;
    return true;
}
private String layMaLoaiHanhKhach(String loaiHanhKhach) {
    if (loaiHanhKhach == null) {
        return "NGUOI_LON";
    }

    String loai = loaiHanhKhach.toLowerCase();

    if (loai.contains("trẻ em")) {
        return "TRE_EM";
    }

    if (loai.contains("người cao tuổi")) {
        return "NGUOI_CAO_TUOI";
    }

    if (loai.contains("sinh viên")) {
        return "SINH_VIEN";
    }

    return "NGUOI_LON";
}
private void batDauDemNguocGiuCho() {
    batDauDemNguocGiuCho(15 * 60);
}

private void dungDemNguocGiuCho() {
    if (timerGiuCho != null) {
        timerGiuCho.stop();
        timerGiuCho = null;
    }
}

private void capNhatTextNutTiepTuc() {
    int phut = giayGiuChoConLai / 60;
    int giay = giayGiuChoConLai % 60;

    jButton22.setText(String.format("TIẾP TỤC (%02d:%02d)", phut, giay));
}

private void khoiPhucTextNutTiepTuc() {
    jButton22.setText("TIẾP TỤC");
}

private void xuLyHetGioGiuCho() {
    daoVeTau.xoaTatCaGiuChoTheoPhien(maPhienGiuCho);

    javax.swing.JOptionPane.showMessageDialog(
            this,
            "Đã hết 15 phút giữ chỗ.\n"
            + "Các ghế vừa chọn đã được mở lại.\n"
            + "Vui lòng chọn ghế lại."
    );

    khoiPhucTextNutTiepTuc();

    xoaLuaChonGheChieuDi();
    dsGheChonVe.clear();
    viTriDangChonGheVe = 0;

    java.awt.CardLayout cl = (java.awt.CardLayout) jPanelNoi_Dung.getLayout();
    cl.show(jPanelNoi_Dung, "card5");

    hienThiDanhSachToaChieuDi();
    capNhatPanelThongTinVeChieuDi();
    capNhatPanel47TongKetVe();
}
    private void xoaGiuChoTheoDanhSach(
        java.util.List<GheHanhKhach> ds,
        String maChuyenTau
) {
    if (ds == null || maChuyenTau == null || maChuyenTau.trim().isEmpty()) {
        return;
    }

    for (GheHanhKhach g : ds) {
        if (g.maGhe != null && !g.maGhe.trim().isEmpty()) {
            daoVeTau.xoaGiuChoGhe(
                    maChuyenTau,
                    g.maGhe,
                    maPhienGiuCho
            );
        }
    }
}
    private void huyGiuChoHienTai() {
    dungDemNguocGiuCho();
    khoiPhucTextNutTiepTuc();
    daoVeTau.xoaTatCaGiuChoTheoPhien(maPhienGiuCho);
}
    private void huyPhienBanVeKhiDangXuat() {
    if (maPhienGiuCho == null || maPhienGiuCho.trim().isEmpty()) {
        return;
    }

    // 1. Dừng đếm ngược giữ chỗ
    dungDemNguocGiuCho();
    khoiPhucTextNutTiepTuc();

    // 2. Trả toàn bộ ghế đang giữ về trống
    daoVeTau.xoaTatCaGiuChoTheoPhien(maPhienGiuCho);

    // 3. Đánh dấu phiên đặt vé là đã hủy
    // Đoạn này rất quan trọng để lần sau đăng nhập không hỏi khôi phục phiên nữa
    daoVeTau.huyPhienDatVe(maPhienGiuCho);

    // 4. Xóa dữ liệu đang chọn trên giao diện
    if (dsGheChonDi != null) {
        dsGheChonDi.clear();
    }

    if (dsGheChonVe != null) {
        dsGheChonVe.clear();
    }

    if (dsFormHanhKhach != null) {
        dsFormHanhKhach.clear();
    }

    if (dsIndexHanhKhachCanNhapLai != null) {
        dsIndexHanhKhachCanNhapLai.clear();
    }

    maChuyenDiDaChon = null;
    tenChuyenDiDaChon = null;
    gaDiChuyenDi = null;
    gaDenChuyenDi = null;
    tenTauChuyenDi = null;

    maChuyenVeDaChon = null;
    tenChuyenVeDaChon = null;
    gaDiChuyenVe = null;
    gaDenChuyenVe = null;
    tenTauChuyenVe = null;

    maToaDangChonDi = null;
    maGheDangChonDi = null;
    maToaDangChonVe = null;
    maGheDangChonVe = null;
}
    private void kiemTraPhienGiuChoCu() {
    if (maNhanVienDangNhap == null || maNhanVienDangNhap.trim().isEmpty()) {
        return;
    }

    String maPhienCu = daoVeTau.timPhienDangGiuTheoNhanVien(maNhanVienDangNhap);

    if (maPhienCu == null) {
        return;
    }

    int chon = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Bạn có một phiên đặt vé trước đó chưa hoàn tất.\n"
            + "Bạn có muốn tiếp tục phiên này không?",
            "Khôi phục phiên giữ chỗ",
            javax.swing.JOptionPane.YES_NO_OPTION
    );

    if (chon == javax.swing.JOptionPane.YES_OPTION) {
        maPhienGiuCho = maPhienCu;
        khoiPhucPhienDatVe(maPhienCu);
    } else {
        daoVeTau.xoaTatCaGiuChoTheoPhien(maPhienCu);
        daoVeTau.huyPhienDatVe(maPhienCu);
    }
}
    
    private void luuTrangThaiPhienDangLam(String manHinh) {
    int nguoiLon = Math.max(0, laySoLuong(txtNguoiLon));
    int treEm = Math.max(0, laySoLuong(txtTreEm));
    int nguoiCaoTuoi = Math.max(0, laySoLuong(txtNguoiCaoTuoi));
    int sinhVien = Math.max(0, laySoLuong(txtSinhVien));

    daoVeTau.luuHoacCapNhatPhienDatVe(
            maPhienGiuCho,
            maNhanVienDangNhap,

            maChuyenDiDaChon,
            tenChuyenDiDaChon,
            tenTauChuyenDi,
            gaDiChuyenDi,
            gaDenChuyenDi,

            maChuyenVeDaChon,
            tenChuyenVeDaChon,
            tenTauChuyenVe,
            gaDiChuyenVe,
            gaDenChuyenVe,

            jRadioButton2.isSelected() ? "KHU_HOI" : "MOT_CHIEU",
            manHinh,

            nguoiLon,
            treEm,
            nguoiCaoTuoi,
            sinhVien
    );
}
    private void khoiPhucPhienDatVe(String maPhien) {
    if (maPhien == null || maPhien.trim().isEmpty()) {
        return;
    }

    java.util.Map<String, Object> phien = daoVeTau.layThongTinPhienDatVe(maPhien);

    if (phien == null || phien.isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Phiên đặt vé cũ đã hết hạn hoặc không còn tồn tại."
        );
        return;
    }

    // Nhận lại mã phiên cũ
    maPhienGiuCho = maPhien;

    // =========================
    // 1. Khôi phục thông tin chuyến
    // =========================
    maChuyenDiDaChon = layStringTuMap(phien, "maChuyenDi");
    tenChuyenDiDaChon = layStringTuMap(phien, "tenChuyenDi");
    tenTauChuyenDi = layStringTuMap(phien, "tenTauDi");
    gaDiChuyenDi = layStringTuMap(phien, "gaDiChuyenDi");
    gaDenChuyenDi = layStringTuMap(phien, "gaDenChuyenDi");

    maChuyenVeDaChon = layStringTuMap(phien, "maChuyenVe");
    tenChuyenVeDaChon = layStringTuMap(phien, "tenChuyenVe");
    tenTauChuyenVe = layStringTuMap(phien, "tenTauVe");
    gaDiChuyenVe = layStringTuMap(phien, "gaDiChuyenVe");
    gaDenChuyenVe = layStringTuMap(phien, "gaDenChuyenVe");

    String loaiVe = layStringTuMap(phien, "loaiVe");
    String manHinhDangDung = layStringTuMap(phien, "manHinhDangDung");

    if ("KHU_HOI".equalsIgnoreCase(loaiVe)) {
        jRadioButton2.setSelected(true);
        dcNgayVe.setEnabled(true);
    } else {
        jRadioButton1.setSelected(true);
        dcNgayVe.setEnabled(false);
    }

    jLabel33.setText(tenTauChuyenDi == null ? "" : tenTauChuyenDi);
    jLabel34.setText(
            giaTriRongNeuNull(gaDiChuyenDi)
            + " - "
            + giaTriRongNeuNull(gaDenChuyenDi)
    );

    jLabel36.setText(tenTauChuyenVe == null ? "" : tenTauChuyenVe);
    jLabel37.setText(
            giaTriRongNeuNull(gaDiChuyenVe)
            + " - "
            + giaTriRongNeuNull(gaDenChuyenVe)
    );

    // =========================
    // 2. Khôi phục số lượng vé
    // =========================
    int nguoiLon = layIntTuMap(phien, "nguoiLon");
    int treEm = layIntTuMap(phien, "treEm");
    int nguoiCaoTuoi = layIntTuMap(phien, "nguoiCaoTuoi");
    int sinhVien = layIntTuMap(phien, "sinhVien");

    if (txtNguoiLon != null) {
        txtNguoiLon.setText(String.valueOf(nguoiLon));
    }

    if (txtTreEm != null) {
        txtTreEm.setText(String.valueOf(treEm));
    }

    if (txtNguoiCaoTuoi != null) {
        txtNguoiCaoTuoi.setText(String.valueOf(nguoiCaoTuoi));
    }

    if (txtSinhVien != null) {
        txtSinhVien.setText(String.valueOf(sinhVien));
    }

    capNhatTongSoVe();

    // =========================
    // 3. Tạo lại danh sách hành khách chọn ghế
    // =========================
    khoiTaoDanhSachHanhKhachCanChonGheDi();

    if (maChuyenVeDaChon != null && !maChuyenVeDaChon.trim().isEmpty()) {
        khoiTaoDanhSachHanhKhachCanChonGheVe();
    }

    // =========================
    // 4. Gán lại các ghế đang giữ
    // =========================
    java.util.List<java.util.Map<String, Object>> dsGiuCho =
            daoVeTau.layDanhSachGiuChoTheoPhien(maPhien);

    for (java.util.Map<String, Object> gMap : dsGiuCho) {
        String chieu = layStringTuMap(gMap, "chieu");
        int sttHanhKhach = layIntTuMap(gMap, "sttHanhKhach");

        if (sttHanhKhach <= 0) {
            continue;
        }

        GheHanhKhach g = null;

        if ("DI".equalsIgnoreCase(chieu)) {
            if (sttHanhKhach <= dsGheChonDi.size()) {
                g = dsGheChonDi.get(sttHanhKhach - 1);
            }
        } else if ("VE".equalsIgnoreCase(chieu)) {
            if (sttHanhKhach <= dsGheChonVe.size()) {
                g = dsGheChonVe.get(sttHanhKhach - 1);
            }
        }

        if (g == null) {
            continue;
        }

        g.maGhe = layStringTuMap(gMap, "soGhe");
        g.tenGhe = layStringTuMap(gMap, "tenGhe");
        g.tenToa = layStringTuMap(gMap, "tenToa");
        g.loaiHanhKhach = layStringTuMap(gMap, "loaiHanhKhach");
        g.giaVe = ((long) Math.floor(layDoubleTuMap(gMap, "giaVe") / 1000.0)) * 1000;
    }

    // =========================
    // 5. Cập nhật giao diện
    // =========================
    hienThiDanhSachToaChieuDi();
    capNhatPanelThongTinVeChieuDi();
    capNhatPanelThongTinVeChieuVe();
    capNhatPanel47TongKetVe();

    // =========================
    // 6. Chạy lại đồng hồ với thời gian còn lại thật
    // =========================
    int soGiayConLai = daoVeTau.laySoGiayConLaiCuaPhien(maPhien);

    if (soGiayConLai <= 0) {
        xuLyHetGioGiuCho();
        return;
    }

    batDauDemNguocGiuCho(soGiayConLai);

    // =========================
    // 7. Quay lại đúng màn hình đang làm
    // =========================
    java.awt.CardLayout cl = (java.awt.CardLayout) jPanelNoi_Dung.getLayout();

    if ("CHON_GHE_VE".equalsIgnoreCase(manHinhDangDung)) {
        hienThiDanhSachToaChieuVe();
        cl.show(jPanelNoi_Dung, "card6");
    } else if ("NHAP_THONG_TIN".equalsIgnoreCase(manHinhDangDung)) {
        hienThiFormHanhKhachVaoJPanel41();
        cl.show(jPanelNoi_Dung, "card7");
    } else {
        cl.show(jPanelNoi_Dung, "card5");
    }

    javax.swing.JOptionPane.showMessageDialog(
            this,
            "Đã khôi phục phiên đặt vé cũ.\n"
            + "Thời gian giữ chỗ còn lại: "
            + String.format("%02d:%02d", soGiayConLai / 60, soGiayConLai % 60)
    );
}
    private String layStringTuMap(java.util.Map<String, Object> map, String key) {
    if (map == null || !map.containsKey(key) || map.get(key) == null) {
        return null;
    }

    return map.get(key).toString();
}

private int layIntTuMap(java.util.Map<String, Object> map, String key) {
    if (map == null || !map.containsKey(key) || map.get(key) == null) {
        return 0;
    }

    Object value = map.get(key);

    if (value instanceof Number) {
        return ((Number) value).intValue();
    }

    try {
        return Integer.parseInt(value.toString());
    } catch (Exception e) {
        return 0;
    }
}

private double layDoubleTuMap(java.util.Map<String, Object> map, String key) {
    if (map == null || !map.containsKey(key) || map.get(key) == null) {
        return 0;
    }

    Object value = map.get(key);

    if (value instanceof Number) {
        return ((Number) value).doubleValue();
    }

    try {
        return Double.parseDouble(value.toString());
    } catch (Exception e) {
        return 0;
    }
}
    private void capNhatDoanhThuHomNay() {
    double tongDoanhThu = daoVeTau.layTongDoanhThuHomNay();

    java.text.NumberFormat nf =
            java.text.NumberFormat.getInstance(new java.util.Locale("vi", "VN"));

    jLabel11.setText(nf.format(tongDoanhThu) + " VNĐ");
}
    private void capNhatSoVeDaBanHomNay() {
    int soVe = daoVeTau.laySoVeDaBanHomNay();
    jLabel9.setText(String.valueOf(soVe));
}
    private void hienThiPanelChuaMoCa() {
    java.awt.CardLayout cl = (java.awt.CardLayout) jPanelNoi_Dung.getLayout();
    cl.show(jPanelNoi_Dung, "card8");
}

private boolean chuaMoCaThiThongBao(String tenChucNang) {
    if (!daMoCa) {
        Object[] luaChon = {"Mở ca", "Hủy"};

        int chon = javax.swing.JOptionPane.showOptionDialog(
                this,
                "Bạn cần mở ca trước khi sử dụng chức năng: " + tenChucNang + ".\n"
                + "Bạn có muốn mở ca ngay bây giờ không?",
                "Chưa mở ca",
                javax.swing.JOptionPane.YES_NO_OPTION,
                javax.swing.JOptionPane.WARNING_MESSAGE,
                null,
                luaChon,
                luaChon[0]
        );

        if (chon == 0) {
            xuLyMoCa();
        } else {
            hienThiPanelChuaMoCa();
        }

        return true;
    }

    return false;
}
    private void xuLyMoCa() {
    if (daMoCa) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Ca làm việc đang được mở !"
        );
        return;
    }

    if (maNhanVienDangNhap == null || maNhanVienDangNhap.trim().isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Không xác định được nhân viên đang đăng nhập."
        );
        return;
    }

    DAO_CALAM.CaLamInfo ca;

    try {
        DAO_CALAM daoCaLam = new DAO_CALAM();
        ca = daoCaLam.layCaTheoGioHienTai();

        if (ca == null) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Không xác định được ca làm hiện tại.\n"
                    + "Vui lòng kiểm tra bảng CaLam.",
                    "Lỗi ca làm",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
            hienThiPanelChuaMoCa();
            return;
        }

        if (laCaNgoaiGio(ca) && !laQuanLy()) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Hiện tại là ca ngoài giờ.\n"
                    + "Chỉ quản lý mới được mở ca ngoài giờ.\n"
                    + "Nhân viên chỉ được sử dụng các ca Sáng, Trưa hoặc Tối.",
                    "Không có quyền mở ca ngoài giờ",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );

            hiencalam.setText("Chưa mở ca");
            jLabel44.setText("");
            hienThiPanelChuaMoCa();
            return;
        }

    } catch (Exception e) {
        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Lỗi khi kiểm tra ca làm:\n"
                + e.getClass().getName() + "\n"
                + e.getMessage()
        );
        return;
    }

    MoCa_ dialog = new MoCa_(this, true, maNhanVienDangNhap);
    dialog.setVisible(true);

    if (dialog.isMoCaThanhCong()) {
    try {
        DAO_CALAM daoCaLam = new DAO_CALAM();

        DAO_CALAM.CaDangMoInfo caDangMo = daoCaLam.moCa(
                ca.maCaLam,
                maNhanVienDangNhap,
                java.time.LocalDateTime.now(),
                dialog.getTongTienDauCa()
        );

        daMoCa = true;
        tongTienDauCa = (long) caDangMo.tienMoCa;

        maChiTietCaLamDangMo = caDangMo.maChiTietCaLam;
        maCaLamDangMo = caDangMo.maCaLam;
        tenCaLamDangMo = caDangMo.tenCaLam;
        thoiGianMoCa = caDangMo.thoiGianMoCa;

        java.time.format.DateTimeFormatter fmt =
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");

        thoiGianCaLamDangMo =
                caDangMo.thoiGianBatDauCa.format(fmt)
                + " - "
                + caDangMo.thoiGianKetThucCa.format(fmt);

        hiencalam.setText(tenCaLamDangMo);
        jLabel44.setText(" (" + thoiGianCaLamDangMo + ")");

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Mở ca thành công.\n"
                + "Mã chi tiết ca: " + maChiTietCaLamDangMo + "\n"
                + "Ca làm: " + tenCaLamDangMo + "\n"
                + "Thời gian ca: " + thoiGianCaLamDangMo
        );

        moGDTrangChu();

    } catch (Exception e) {
        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Không thể lưu ca đang mở:\n" + e.getMessage()
        );
    }
}
    
}
    private boolean laQuanLy() {
    return "QUAN_LY".equalsIgnoreCase(vaiTroDangNhap)
            || "QUANLY".equalsIgnoreCase(vaiTroDangNhap)
            || "QL".equalsIgnoreCase(vaiTroDangNhap);
}
    private boolean laCaNgoaiGio(DAO_CALAM.CaLamInfo ca) {
    return ca != null 
            && ca.maCaLam != null 
            && ca.maCaLam.equalsIgnoreCase("NGOAI_GIO");
}
    private void capNhatHienThiCaBenTrai() {
    if (daMoCa) {
        hiencalam.setText(tenCaLamDangMo == null ? "Đang mở ca" : tenCaLamDangMo);

        if (thoiGianCaLamDangMo != null && !thoiGianCaLamDangMo.trim().isEmpty()) {
            jLabel44.setText(" (" + thoiGianCaLamDangMo + ")");
        } else {
            jLabel44.setText("");
        }

        return;
    }

    try {
        DAO_CALAM daoCaLam = new DAO_CALAM();
        DAO_CALAM.CaLamInfo ca = daoCaLam.layCaTheoGioHienTai();

        if (ca != null
                && ca.maCaLam != null
                && ca.maCaLam.equalsIgnoreCase("NGOAI_GIO")
                && laQuanLy()) {

            hiencalam.setText(ca.tenCaLam);
            jLabel44.setText("");
            return;
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    hiencalam.setText("Chưa mở ca");
    jLabel44.setText("");
}
    private void xuLyKetCa() {
    if (!daMoCa) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Bạn chưa mở ca nên không thể kết ca.",
                "Chưa mở ca",
                javax.swing.JOptionPane.WARNING_MESSAGE
        );

        hienThiPanelChuaMoCa();
        return;
    }

    if (maChiTietCaLamDangMo == null || maChiTietCaLamDangMo.trim().isEmpty()) {
        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Không xác định được mã chi tiết ca làm đang mở.\n"
                + "Vui lòng kiểm tra bảng ChiTietCaLam.",
                "Lỗi kết ca",
                javax.swing.JOptionPane.ERROR_MESSAGE
        );
        return;
    }

    KetCa_ dialog = new KetCa_(
            this,
            true,
            hienten.getText(),
            maNhanVienDangNhap,
            tenCaLamDangMo,
            thoiGianCaLamDangMo,
            thoiGianMoCa
    );

    dialog.setVisible(true);

    if (!dialog.isKetCaThanhCong()) {
        return;
    }

    double tienKetCa = dialog.getTongTienKetCa();

    try {
        DAO_CALAM daoCaLam = new DAO_CALAM();

        boolean ok = daoCaLam.ketCa(
                maChiTietCaLamDangMo,
                maNhanVienDangNhap,
                java.time.LocalDateTime.now(),
                tienKetCa
        );

        if (!ok) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Kết ca thất bại.\nKhông tìm thấy ca đang mở để cập nhật."
            );
            return;
        }

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Kết ca thành công.\nDữ liệu đã được lưu vào bảng ChiTietCaLam."
        );

        daMoCa = false;
        tongTienDauCa = 0;
        maChiTietCaLamDangMo = null;
        maCaLamDangMo = null;
        tenCaLamDangMo = null;
        thoiGianCaLamDangMo = null;
        thoiGianMoCa = null;

        hiencalam.setText("Chưa mở ca");
        jLabel44.setText("");

        hienThiPanelChuaMoCa();

    } catch (Exception e) {
        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Lỗi khi kết ca:\n" + e.getMessage()
        );
    }
}
    private void khoiPhucCaDangMo() {
    if (maNhanVienDangNhap == null || maNhanVienDangNhap.trim().isEmpty()) {
        return;
    }

    try {
        DAO_CALAM daoCaLam = new DAO_CALAM();
        DAO_CALAM.CaDangMoInfo ca = daoCaLam.layCaDangMoTheoNhanVien(maNhanVienDangNhap);

        if (ca == null) {
            daMoCa = false;

            maChiTietCaLamDangMo = null;
            maCaLamDangMo = null;
            tenCaLamDangMo = null;
            thoiGianCaLamDangMo = null;
            thoiGianMoCa = null;
            tongTienDauCa = 0;

            hiencalam.setText("Chưa mở ca");
            jLabel44.setText("");
            hienThiPanelChuaMoCa();
            return;
        }

        daMoCa = true;

        maChiTietCaLamDangMo = ca.maChiTietCaLam;
        maCaLamDangMo = ca.maCaLam;
        tenCaLamDangMo = ca.tenCaLam;
        thoiGianMoCa = ca.thoiGianMoCa;
        tongTienDauCa = (long) ca.tienMoCa;

        java.time.format.DateTimeFormatter fmt =
                java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");

        thoiGianCaLamDangMo =
                ca.thoiGianBatDauCa.format(fmt)
                + " - "
                + ca.thoiGianKetThucCa.format(fmt);

        hiencalam.setText(tenCaLamDangMo);
        jLabel44.setText(" (" + thoiGianCaLamDangMo + ")");

    } catch (Exception e) {
        e.printStackTrace();

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Lỗi khi khôi phục ca đang mở:\n" + e.getMessage()
        );
    }
}
    private javax.swing.JLabel taoLabelLoi() {
    javax.swing.JLabel lbl = new javax.swing.JLabel(" ");
    lbl.setForeground(java.awt.Color.RED);
    lbl.setFont(new java.awt.Font("Segoe UI", java.awt.Font.ITALIC, 11));
    return lbl;
}

private void hienLoi(javax.swing.JComponent comp, javax.swing.JLabel lblLoi, String noiDung) {
    lblLoi.setText(noiDung);
    comp.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.RED));
}

private void xoaLoi(javax.swing.JComponent comp, javax.swing.JLabel lblLoi) {
    lblLoi.setText(" ");

    if (comp instanceof javax.swing.JTextField) {
        comp.setBorder(javax.swing.UIManager.getBorder("TextField.border"));
    } else if (comp instanceof javax.swing.JComboBox) {
        comp.setBorder(javax.swing.UIManager.getBorder("ComboBox.border"));
    } else {
        comp.setBorder(null);
    }
}

private boolean laRongHoacPlaceholder(javax.swing.JTextField txt, String placeholder) {
    String s = txt.getText() == null ? "" : txt.getText().trim();
    return s.isEmpty() || s.equalsIgnoreCase(placeholder);
}

private void xoaTatCaCanhBaoHanhKhach() {
    for (FormHanhKhach f : dsFormHanhKhach) {
        xoaLoi(f.txtHoTen, f.lblLoiHoTen);
        xoaLoi(f.dcNgaySinh, f.lblLoiNgaySinh);
        xoaLoi(f.cboLoaiGiayTo, f.lblLoiLoaiGiayTo);
        xoaLoi(f.txtSoGiayTo, f.lblLoiSoGiayTo);
        xoaLoi(f.txtSdt, f.lblLoiSdt);
    }
}
    private boolean kiemTraTrungGiayToTrongCungLanMua() {
    java.util.Map<String, Integer> mapGiayTo = new java.util.HashMap<>();

    for (int i = 0; i < dsFormHanhKhach.size(); i++) {
        FormHanhKhach f = dsFormHanhKhach.get(i);

        String loaiGiayTo = f.cboLoaiGiayTo.getSelectedItem() == null
                ? ""
                : f.cboLoaiGiayTo.getSelectedItem().toString().trim();

        String soGiayTo = layTextKhongPlaceholder(
                f.txtSoGiayTo,
                "Số giấy tờ(CCCD/PASSPORT)"
        );

        if (loaiGiayTo.isEmpty()
                || soGiayTo.isEmpty()
                || loaiGiayTo.equalsIgnoreCase("KHONG_CO")) {
            continue;
        }

        String key = loaiGiayTo.toUpperCase() + "|" + soGiayTo.trim().toUpperCase();

        if (mapGiayTo.containsKey(key)) {
            int viTriBiTrung = mapGiayTo.get(key);

            FormHanhKhach fTruoc = dsFormHanhKhach.get(viTriBiTrung);

            hienLoi(
                    fTruoc.txtSoGiayTo,
                    fTruoc.lblLoiSoGiayTo,
                    "Trùng " + loaiGiayTo + " với hành khách " + (i + 1)
            );

            hienLoi(
                    f.txtSoGiayTo,
                    f.lblLoiSoGiayTo,
                    "Trùng " + loaiGiayTo + " với hành khách " + (viTriBiTrung + 1)
            );

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Không được nhập trùng số giấy tờ.\n"
                    + "Hành khách " + (viTriBiTrung + 1)
                    + " và hành khách " + (i + 1)
                    + " đang dùng cùng " + loaiGiayTo + ": " + soGiayTo
            );

            f.txtSoGiayTo.requestFocus();
            return false;
        }

        mapGiayTo.put(key, i);
    }

    return true;
}
    private void xuLyTraCuuHanhKhachTheoGiayTo(FormHanhKhach form) {
    String loaiGiayTo = form.cboLoaiGiayTo.getSelectedItem() == null
            ? ""
            : form.cboLoaiGiayTo.getSelectedItem().toString().trim();

    String soGiayTo = layTextKhongPlaceholder(
            form.txtSoGiayTo,
            "Số giấy tờ(CCCD/PASSPORT)"
    );

    if (loaiGiayTo.isEmpty()
            || loaiGiayTo.equalsIgnoreCase("KHONG_CO")
            || soGiayTo.isEmpty()) {
        return;
    }

    HanhKhach hk = daoHanhKhach.timHanhKhachTheoGiayTo(loaiGiayTo, soGiayTo);

    if (hk == null) {
        return;
    }

    int chon = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "Tìm thấy hành khách đã có trong hệ thống:\n\n"
            + "Mã hành khách: " + hk.getMaHanhKhach() + "\n"
            + "Họ tên: " + hk.getHoTenHK() + "\n"
            + "Ngày sinh: " + (hk.getNgaySinh() != null ? hk.getNgaySinh() : "") + "\n"
            + "SĐT: " + (hk.getSdt() != null ? hk.getSdt() : "") + "\n\n"
            + "Bạn có muốn sử dụng thông tin này không?",
            "Tìm thấy hành khách",
            javax.swing.JOptionPane.YES_NO_OPTION
    );

    if (chon == javax.swing.JOptionPane.YES_OPTION) {
        doThongTinHanhKhachLenForm(form, hk);
    }
}
    private void doThongTinHanhKhachLenForm(FormHanhKhach form, HanhKhach hk) {
    form.txtHoTen.setText(hk.getHoTenHK());
    form.txtHoTen.setForeground(java.awt.Color.BLACK);

    if (hk.getNgaySinh() != null) {
        java.util.Date date = java.sql.Date.valueOf(hk.getNgaySinh());
        form.dcNgaySinh.setDate(date);
        form.dcNgaySinh.getDateEditor().getUiComponent()
        .setForeground(java.awt.Color.BLACK);
    }

    if (hk.getLoaiGiayTo() != null) {
        form.cboLoaiGiayTo.setSelectedItem(hk.getLoaiGiayTo());
    }

    if (hk.getSoGiayTo() != null) {
        form.txtSoGiayTo.setEnabled(true);
        form.txtSoGiayTo.setText(hk.getSoGiayTo());
        form.txtSoGiayTo.setForeground(java.awt.Color.BLACK);
    }

    if (hk.getSdt() != null) {
        form.txtSdt.setText(hk.getSdt());
        form.txtSdt.setForeground(java.awt.Color.BLACK);
    }

    form.maHanhKhachDaChon = hk.getMaHanhKhach();
    form.giayToDaXacNhan = taoKeyGiayTo(
            hk.getLoaiGiayTo(),
            hk.getSoGiayTo()
    );
    form.duLieuTuDB = true;

    xoaLoi(form.txtHoTen, form.lblLoiHoTen);
    xoaLoi(form.dcNgaySinh, form.lblLoiNgaySinh);
    xoaLoi(form.cboLoaiGiayTo, form.lblLoiLoaiGiayTo);
    xoaLoi(form.txtSoGiayTo, form.lblLoiSoGiayTo);
    xoaLoi(form.txtSdt, form.lblLoiSdt);
}
    private String taoKeyGiayTo(String loaiGiayTo, String soGiayTo) {
    String loai = loaiGiayTo == null ? "" : loaiGiayTo.trim().toUpperCase();
    String so = soGiayTo == null ? "" : soGiayTo.trim().toUpperCase();
    return loai + "|" + so;
}
    private void kiemTraDoiGiayToSauKhiDoDuLieu(FormHanhKhach form) {
    if (!form.duLieuTuDB) {
        return;
    }

    String loaiGiayTo = form.cboLoaiGiayTo.getSelectedItem() == null
            ? ""
            : form.cboLoaiGiayTo.getSelectedItem().toString().trim();

    String soGiayTo = layTextKhongPlaceholder(
            form.txtSoGiayTo,
            "Số giấy tờ(CCCD/PASSPORT)"
    );

    String keyHienTai = taoKeyGiayTo(loaiGiayTo, soGiayTo);

    if (!keyHienTai.equals(form.giayToDaXacNhan)) {
        form.maHanhKhachDaChon = null;
        form.giayToDaXacNhan = null;
        form.duLieuTuDB = false;

        form.txtHoTen.setText("");
        form.dcNgaySinh.setDate(null);
        form.txtSdt.setText("");

        javax.swing.JOptionPane.showMessageDialog(
                this,
                "Bạn đã thay đổi số giấy tờ.\n"
                + "Thông tin hành khách cũ đã được xóa.\n"
                + "Vui lòng nhập lại thông tin cho hành khách mới."
        );
    }
}
    private void ganSuKienTuDongXoaLoi(FormHanhKhach f) {
    // Họ tên
    f.txtHoTen.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        private void xuLy() {
            javax.swing.SwingUtilities.invokeLater(() -> {
                if (!laRongHoacPlaceholder(f.txtHoTen, "Họ và tên(ví dụ: Nguyễn Văn A)")) {
                    xoaLoi(f.txtHoTen, f.lblLoiHoTen);
                }
            });
        }

        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            xuLy();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            xuLy();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            xuLy();
        }
    });

    // Số giấy tờ
    f.txtSoGiayTo.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        private void xuLy() {
            javax.swing.SwingUtilities.invokeLater(() -> {
                if (!laRongHoacPlaceholder(f.txtSoGiayTo, "Số giấy tờ(CCCD/PASSPORT)")) {
                    xoaLoi(f.txtSoGiayTo, f.lblLoiSoGiayTo);
                }
            });
        }

        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            xuLy();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            xuLy();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            xuLy();
        }
    });

    // Số điện thoại
    f.txtSdt.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
        private void xuLy() {
            javax.swing.SwingUtilities.invokeLater(() -> {
                if (!laRongHoacPlaceholder(f.txtSdt, "Số điện thoại")) {
                    xoaLoi(f.txtSdt, f.lblLoiSdt);
                }
            });
        }

        @Override
        public void insertUpdate(javax.swing.event.DocumentEvent e) {
            xuLy();
        }

        @Override
        public void removeUpdate(javax.swing.event.DocumentEvent e) {
            xuLy();
        }

        @Override
        public void changedUpdate(javax.swing.event.DocumentEvent e) {
            xuLy();
        }
    });

    // Ngày sinh
    f.dcNgaySinh.getDateEditor().addPropertyChangeListener("date", evt -> {
        if (f.dcNgaySinh.getDate() != null) {
            xoaLoi(f.dcNgaySinh, f.lblLoiNgaySinh);
        }
    });

    // Loại giấy tờ
    f.cboLoaiGiayTo.addActionListener(e -> {
        if (f.cboLoaiGiayTo.getSelectedItem() != null) {
            xoaLoi(f.cboLoaiGiayTo, f.lblLoiLoaiGiayTo);
        }
    });
}
    private String layLoaiGiayTo(FormHanhKhach f) {
    return f.cboLoaiGiayTo.getSelectedItem() == null
            ? ""
            : f.cboLoaiGiayTo.getSelectedItem().toString().trim();
}

private String laySoGiayTo(FormHanhKhach f) {
    return layTextKhongPlaceholder(
            f.txtSoGiayTo,
            "Số giấy tờ(CCCD/PASSPORT)"
    );
}
private String laySdt(FormHanhKhach f) {
    return layTextKhongPlaceholder(
            f.txtSdt,
            "Số điện thoại"
    );
}

private boolean kiemTraGiayToInline(FormHanhKhach f) {
    String loaiGT = layLoaiGiayTo(f);
    String soGT = laySoGiayTo(f);

    xoaLoi(f.cboLoaiGiayTo, f.lblLoiLoaiGiayTo);
    xoaLoi(f.txtSoGiayTo, f.lblLoiSoGiayTo);

    if (loaiGT.isEmpty()) {
        hienLoi(
                f.cboLoaiGiayTo,
                f.lblLoiLoaiGiayTo,
                "Chưa chọn loại giấy tờ"
        );
        return false;
    }

    if (loaiGT.equalsIgnoreCase("KHONG_CO")) {
        return true;
    }

    if (soGT.isEmpty()) {
        hienLoi(
                f.txtSoGiayTo,
                f.lblLoiSoGiayTo,
                "Chưa nhập số giấy tờ"
        );
        return false;
    }

    if (loaiGT.equalsIgnoreCase("CCCD")) {
        if (!soGT.matches("\\d+")) {
            hienLoi(
                    f.txtSoGiayTo,
                    f.lblLoiSoGiayTo,
                    "CCCD chỉ được nhập chữ số"
            );
            return false;
        }

        if (soGT.length() < 12) {
            hienLoi(
                    f.txtSoGiayTo,
                    f.lblLoiSoGiayTo,
                    "CCCD phải gồm đúng 12 chữ số"
            );
            return false;
        }

        if (soGT.length() > 12) {
            hienLoi(
                    f.txtSoGiayTo,
                    f.lblLoiSoGiayTo,
                    "CCCD không được quá 12 chữ số"
            );
            return false;
        }

        if (!laMaTinhCCCDHopLe(soGT)) {
            hienLoi(
                    f.txtSoGiayTo,
                    f.lblLoiSoGiayTo,
                    "CCCD không đúng mã tỉnh/thành"
            );
            return false;
        }

        return true;
    }

    if (loaiGT.equalsIgnoreCase("PASSPORT")) {
        String passport = soGT.toUpperCase();

        if (!passport.matches("[A-Z0-9]{6,9}")) {
            hienLoi(
                    f.txtSoGiayTo,
                    f.lblLoiSoGiayTo,
                    "Passport gồm 6-9 ký tự chữ/số"
            );
            return false;
        }

        f.txtSoGiayTo.setText(passport);
        return true;
    }

    hienLoi(
            f.txtSoGiayTo,
            f.lblLoiSoGiayTo,
            "Loại giấy tờ không hợp lệ"
    );
    return false;
}
    private boolean kiemTraSdtInline(FormHanhKhach f) {
    String sdt = layTextKhongPlaceholder(
            f.txtSdt,
            "Số điện thoại"
    );

    xoaLoi(f.txtSdt, f.lblLoiSdt);

    if (sdt.isEmpty()) {
        hienLoi(f.txtSdt, f.lblLoiSdt, "Chưa nhập số điện thoại");
        return false;
    }

    if (!sdt.matches("\\d+")) {
        hienLoi(f.txtSdt, f.lblLoiSdt, "Số điện thoại chỉ được nhập số");
        return false;
    }

    if (sdt.length() != 10) {
        hienLoi(f.txtSdt, f.lblLoiSdt, "Số điện thoại di động phải gồm đúng 10 chữ số");
        return false;
    }

    if (!sdt.startsWith("0")) {
        hienLoi(f.txtSdt, f.lblLoiSdt, "Số điện thoại phải bắt đầu bằng số 0");
        return false;
    }

    if (!laDauSoDiDongHopLe(sdt)) {
        hienLoi(f.txtSdt, f.lblLoiSdt, "Đầu số điện thoại không hợp lệ");
        return false;
    }

    return true;
}
    private boolean laDauSoDiDongHopLe(String sdt) {
    if (sdt == null || sdt.length() < 3) {
        return false;
    }

    String dau3 = sdt.substring(0, 3);

    String[] dsDauSo = {
        // Viettel
        "032", "033", "034", "035", "036", "037", "038", "039",
        "086", "096", "097", "098",

        // VinaPhone
        "081", "082", "083", "084", "085",
        "088", "091", "094",

        // MobiFone
        "070", "076", "077", "078", "079",
        "089", "090", "093",

        // Vietnamobile
        "052", "056", "058", "092",

        // Gmobile
        "059", "099"
    };

    for (String dauSo : dsDauSo) {
        if (dau3.equals(dauSo)) {
            return true;
        }
    }

    return false;
}
    private boolean kiemTraNgaySinhInline(FormHanhKhach f) {
    xoaLoi(f.dcNgaySinh, f.lblLoiNgaySinh);

    java.util.Date ngaySinhDate = f.dcNgaySinh.getDate();

    if (ngaySinhDate == null) {
        hienLoi(
                f.dcNgaySinh,
                f.lblLoiNgaySinh,
                "Chưa chọn ngày sinh"
        );
        return false;
    }

    int tuoi = tinhTuoi(ngaySinhDate);

    if (tuoi < 0) {
        hienLoi(
                f.dcNgaySinh,
                f.lblLoiNgaySinh,
                "Ngày sinh không hợp lệ"
        );
        return false;
    }

    String loai = f.loaiHanhKhach == null
            ? ""
            : f.loaiHanhKhach.toLowerCase();

    if (loai.contains("người lớn") && tuoi < 18) {
        hienLoi(
                f.dcNgaySinh,
                f.lblLoiNgaySinh,
                "Người lớn phải từ 18 tuổi trở lên"
        );
        return false;
    }

    if (loai.contains("trẻ em") && tuoi >= 18) {
        hienLoi(
                f.dcNgaySinh,
                f.lblLoiNgaySinh,
                "Trẻ em phải nhỏ hơn 18 tuổi"
        );
        return false;
    }

    if (loai.contains("người cao tuổi") && tuoi < 60) {
        hienLoi(
                f.dcNgaySinh,
                f.lblLoiNgaySinh,
                "Người cao tuổi phải từ 60 tuổi trở lên"
        );
        return false;
    }

    if (loai.contains("sinh viên") && tuoi < 18) {
        hienLoi(
                f.dcNgaySinh,
                f.lblLoiNgaySinh,
                "Sinh viên phải từ 18 tuổi trở lên"
        );
        return false;
    }

    return true;
}
    private javax.swing.JPanel taoPanelGheCoDinh(int sucChua) {
    int soCot = (int) Math.ceil(sucChua / (double) SO_HANG_GHE);

    javax.swing.JPanel pnlGhe = new javax.swing.JPanel();
    pnlGhe.setLayout(new java.awt.GridLayout(SO_HANG_GHE, soCot, GAP_GHE, GAP_GHE));

    int width = soCot * GHE_W + (soCot - 1) * GAP_GHE;
    int height = SO_HANG_GHE * GHE_H + (SO_HANG_GHE - 1) * GAP_GHE;

    java.awt.Dimension size = new java.awt.Dimension(width, height);
    pnlGhe.setPreferredSize(size);
    pnlGhe.setMinimumSize(size);
    pnlGhe.setMaximumSize(size);

    return pnlGhe;
}
    private javax.swing.JScrollPane taoScrollNgangChoPanelGhe(javax.swing.JPanel pnlGhe) {
    javax.swing.JPanel wrapper = new javax.swing.JPanel(
            new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 8)
    );

    wrapper.add(pnlGhe);

    java.awt.Dimension sizeGhe = pnlGhe.getPreferredSize();

    wrapper.setPreferredSize(new java.awt.Dimension(
            sizeGhe.width + 20,
            sizeGhe.height + 25
    ));

    javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(wrapper);

    scroll.setHorizontalScrollBarPolicy(
            javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );
    scroll.setVerticalScrollBarPolicy(
            javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER
    );

    scroll.getHorizontalScrollBar().setUnitIncrement(20);
    scroll.setBorder(null);

    // QUAN TRỌNG: tăng chiều cao để thanh cuộn không che ghế
    scroll.setPreferredSize(new java.awt.Dimension(100, 235));
    scroll.setMinimumSize(new java.awt.Dimension(100, 235));
    scroll.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 235));

    return scroll;
}
    private void chinhLaiChieuCaoVungGhe() {
    // Chiều đi
    jPanel29.setPreferredSize(new java.awt.Dimension(361, 300));
    jPanel29.setMinimumSize(new java.awt.Dimension(361, 300));
    jPanel29.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 300));

//    jPanel37.setPreferredSize(new java.awt.Dimension(391, 245));
//    jPanel37.setMinimumSize(new java.awt.Dimension(391, 245));
//    jPanel37.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 245));
    jPanel37.setPreferredSize(new java.awt.Dimension(361, 300));
    jPanel37.setMinimumSize(new java.awt.Dimension(361, 300));
    jPanel37.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 300));

    // Chiều về
    jPanel30.setPreferredSize(new java.awt.Dimension(361, 300));
    jPanel30.setMinimumSize(new java.awt.Dimension(361, 300));
    jPanel30.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 300));

    jPanel29.revalidate();
    jPanel37.revalidate();
    jPanel30.revalidate();

    jPanel29.repaint();
    jPanel37.repaint();
    jPanel30.repaint();
}
    private void cauHinhVungToaVaGheDepHon() {
    // Khung danh sách toa chiều đi
    jPanel26.setPreferredSize(new java.awt.Dimension(391, 150));
    jPanel26.setMinimumSize(new java.awt.Dimension(391, 150));
    jPanel26.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 160));

    // Khung danh sách ghế chiều đi
    jPanel29.setPreferredSize(new java.awt.Dimension(391, 310));
    jPanel29.setMinimumSize(new java.awt.Dimension(391, 310));
    jPanel29.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 330));

    // Vùng danh sách ghế chiều đi
    jPanel37.setPreferredSize(new java.awt.Dimension(391, 260));
    jPanel37.setMinimumSize(new java.awt.Dimension(391, 260));
    jPanel37.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 280));

    jPanel26.revalidate();
    jPanel26.repaint();

    jPanel29.revalidate();
    jPanel29.repaint();

    jPanel37.revalidate();
    jPanel37.repaint();
}
    private javax.swing.JScrollPane taoScrollNgangChoPanelToa(javax.swing.JPanel panelToa) {
    javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(panelToa);

    scroll.setHorizontalScrollBarPolicy(
            javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
    );
    scroll.setVerticalScrollBarPolicy(
            javax.swing.JScrollPane.VERTICAL_SCROLLBAR_NEVER
    );

    scroll.getHorizontalScrollBar().setUnitIncrement(20);
    scroll.setBorder(null);

    scroll.setPreferredSize(new java.awt.Dimension(100, 78));
    scroll.setMinimumSize(new java.awt.Dimension(100, 78));
    scroll.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 78));

    scroll.getViewport().setOpaque(false);
    scroll.setOpaque(false);

    return scroll;
}
    private void lamDepNutToa(javax.swing.JButton btnToa) {
    java.awt.Dimension size = new java.awt.Dimension(210, 46);

    btnToa.setPreferredSize(size);
    btnToa.setMinimumSize(size);
    btnToa.setMaximumSize(size);

    btnToa.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
    btnToa.setFocusPainted(false);
    btnToa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
    btnToa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

    btnToa.setBorder(javax.swing.BorderFactory.createCompoundBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(120, 150, 180)),
            javax.swing.BorderFactory.createEmptyBorder(6, 10, 6, 10)
    ));
}
    private void cauHinhBoCucChonGheKhongMatGhiChu() {
    // =========================
    // CHIỀU ĐI
    // =========================

    // Khung danh sách toa tàu
    jPanel26.setPreferredSize(new java.awt.Dimension(391, 145));
    jPanel26.setMinimumSize(new java.awt.Dimension(391, 145));
    jPanel26.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 145));

    // Khung danh sách ghế
    jPanel29.setPreferredSize(new java.awt.Dimension(391, 255));
    jPanel29.setMinimumSize(new java.awt.Dimension(391, 255));
    jPanel29.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 255));

    // Vùng chứa ghế bên trong
    jPanel37.setPreferredSize(new java.awt.Dimension(391, 205));
    jPanel37.setMinimumSize(new java.awt.Dimension(391, 205));
    jPanel37.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 205));

    // Phần Ghi chú + Thông tin vé
    jPanel31.setPreferredSize(new java.awt.Dimension(376, 210));
    jPanel31.setMinimumSize(new java.awt.Dimension(300, 190));
    jPanel31.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 220));


    // =========================
    // CHIỀU VỀ
    // =========================

    // Khung danh sách toa chiều về
    jPanel28.setPreferredSize(new java.awt.Dimension(361, 145));
    jPanel28.setMinimumSize(new java.awt.Dimension(361, 145));
    jPanel28.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 145));

    // Khung danh sách ghế chiều về
    jPanel30.setPreferredSize(new java.awt.Dimension(361, 255));
    jPanel30.setMinimumSize(new java.awt.Dimension(361, 255));
    jPanel30.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 255));

    // Phần Ghi chú + Thông tin vé chiều về
    jPanel32.setPreferredSize(new java.awt.Dimension(376, 210));
    jPanel32.setMinimumSize(new java.awt.Dimension(300, 190));
    jPanel32.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 220));


    jPanel26.revalidate();
    jPanel29.revalidate();
    jPanel37.revalidate();
    jPanel31.revalidate();

    jPanel28.revalidate();
    jPanel30.revalidate();
    jPanel32.revalidate();

    jPanel31_chieuxuoi.revalidate();
    jPanel31_chieuxuoi.repaint();

    jPanel32_chieunguoc.revalidate();
    jPanel32_chieunguoc.repaint();
}
    private void coDinhNutDuoiManHinhChonGhe() {
    // =========================
    // MÀN CHỌN GHẾ CHIỀU ĐI
    // =========================
    GD_chon_TOA_GHE_XUOI.removeAll();
    GD_chon_TOA_GHE_XUOI.setLayout(new java.awt.BorderLayout());

    GD_chon_TOA_GHE_XUOI.add(jPanel34, java.awt.BorderLayout.CENTER);
    GD_chon_TOA_GHE_XUOI.add(jPanel35, java.awt.BorderLayout.SOUTH);

    jPanel35.setPreferredSize(new java.awt.Dimension(100, 50));
    jPanel35.setMinimumSize(new java.awt.Dimension(100, 50));
    jPanel35.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 50));

    // =========================
    // MÀN CHỌN GHẾ CHIỀU VỀ
    // =========================
    GD_TOA_GHE_NGUOC.removeAll();
    GD_TOA_GHE_NGUOC.setLayout(new java.awt.BorderLayout());

    GD_TOA_GHE_NGUOC.add(jPanel32_chieunguoc, java.awt.BorderLayout.CENTER);
    GD_TOA_GHE_NGUOC.add(jPanel6, java.awt.BorderLayout.SOUTH);

    jPanel6.setPreferredSize(new java.awt.Dimension(100, 50));
    jPanel6.setMinimumSize(new java.awt.Dimension(100, 50));
    jPanel6.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 50));

    GD_chon_TOA_GHE_XUOI.revalidate();
    GD_chon_TOA_GHE_XUOI.repaint();

    GD_TOA_GHE_NGUOC.revalidate();
    GD_TOA_GHE_NGUOC.repaint();
}
    private javax.swing.JScrollPane taoScrollChoNoiDung(javax.swing.JComponent content) {
    javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(content);

    scroll.setVerticalScrollBarPolicy(
            javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );
    scroll.setHorizontalScrollBarPolicy(
            javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );

    scroll.getVerticalScrollBar().setUnitIncrement(18);
    scroll.setBorder(null);
    scroll.getViewport().setBackground(java.awt.Color.WHITE);

    return scroll;
}
private void coDinhNutDuoiKhongLamMatNoiDung() {
    // =========================
    // CHIỀU ĐI
    // =========================
    GD_chon_TOA_GHE_XUOI.removeAll();
    GD_chon_TOA_GHE_XUOI.setLayout(new java.awt.BorderLayout());

    // jPanel34 là toàn bộ nội dung chọn toa + ghế + ghi chú + thông tin vé
    GD_chon_TOA_GHE_XUOI.add(jPanel34, java.awt.BorderLayout.CENTER);

    // jPanel35 là cụm nút Xác nhận / Quay lại / Hủy
    jPanel35.setPreferredSize(new java.awt.Dimension(100, 55));
    jPanel35.setMinimumSize(new java.awt.Dimension(100, 55));
    jPanel35.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 55));

    GD_chon_TOA_GHE_XUOI.add(jPanel35, java.awt.BorderLayout.SOUTH);


    // =========================
    // CHIỀU VỀ
    // =========================
    GD_TOA_GHE_NGUOC.removeAll();
    GD_TOA_GHE_NGUOC.setLayout(new java.awt.BorderLayout());

    // jPanel32_chieunguoc mới là nội dung chiều về
    GD_TOA_GHE_NGUOC.add(jPanel32_chieunguoc, java.awt.BorderLayout.CENTER);

    // jPanel6 là cụm nút Xác nhận / Quay lại / Hủy chiều về
    jPanel6.setPreferredSize(new java.awt.Dimension(100, 55));
    jPanel6.setMinimumSize(new java.awt.Dimension(100, 55));
    jPanel6.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 55));

    GD_TOA_GHE_NGUOC.add(jPanel6, java.awt.BorderLayout.SOUTH);


    GD_chon_TOA_GHE_XUOI.revalidate();
    GD_chon_TOA_GHE_XUOI.repaint();

    GD_TOA_GHE_NGUOC.revalidate();
    GD_TOA_GHE_NGUOC.repaint();
}
private javax.swing.JScrollPane taoScrollDocChoPhanChonGhe(javax.swing.JComponent content) {
    javax.swing.JScrollPane scroll = new javax.swing.JScrollPane(content);

    scroll.setVerticalScrollBarPolicy(
            javax.swing.JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED
    );

    scroll.setHorizontalScrollBarPolicy(
            javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
    );

    scroll.getVerticalScrollBar().setUnitIncrement(18);
    scroll.setBorder(null);

    scroll.getViewport().setBackground(new java.awt.Color(242, 242, 242));

    return scroll;
}
private void coDinhNutVaCuonPhanChonGhe() {
    // =========================
    // CHIỀU ĐI
    // =========================
    GD_chon_TOA_GHE_XUOI.removeAll();
    GD_chon_TOA_GHE_XUOI.setLayout(new java.awt.BorderLayout());

    javax.swing.JScrollPane scrollChieuDi =
            taoScrollDocChoPhanChonGhe(jPanel31_chieuxuoi);

    jPanel35.setPreferredSize(new java.awt.Dimension(100, 58));
    jPanel35.setMinimumSize(new java.awt.Dimension(100, 58));
    jPanel35.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 58));

    GD_chon_TOA_GHE_XUOI.add(scrollChieuDi, java.awt.BorderLayout.CENTER);
    GD_chon_TOA_GHE_XUOI.add(jPanel35, java.awt.BorderLayout.SOUTH);


    // =========================
    // CHIỀU VỀ
    // =========================
    GD_TOA_GHE_NGUOC.removeAll();
    GD_TOA_GHE_NGUOC.setLayout(new java.awt.BorderLayout());

    javax.swing.JScrollPane scrollChieuVe =
            taoScrollDocChoPhanChonGhe(jPanel32_chieunguoc);

    jPanel6.setPreferredSize(new java.awt.Dimension(100, 58));
    jPanel6.setMinimumSize(new java.awt.Dimension(100, 58));
    jPanel6.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 58));

    GD_TOA_GHE_NGUOC.add(scrollChieuVe, java.awt.BorderLayout.CENTER);
    GD_TOA_GHE_NGUOC.add(jPanel6, java.awt.BorderLayout.SOUTH);

    GD_chon_TOA_GHE_XUOI.revalidate();
    GD_chon_TOA_GHE_XUOI.repaint();

    GD_TOA_GHE_NGUOC.revalidate();
    GD_TOA_GHE_NGUOC.repaint();
    // Ép header "Toa: tên toa" của chiều đi canh trái
        
}
private void setChieuCaoPanel(javax.swing.JComponent c, int h) {
    c.setPreferredSize(new java.awt.Dimension(100, h));
    c.setMinimumSize(new java.awt.Dimension(100, h));
    c.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, h));
    c.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
}

private void thuNhoLaiCacPanelChonGhe() {
    // =========================
    // CHIỀU ĐI
    // =========================

    // Panel tổng nội dung chiều đi, bỏ chiều cao 1274 quá lớn
    jPanel31_chieuxuoi.setPreferredSize(new java.awt.Dimension(1000, 680));
    jPanel31_chieuxuoi.setMinimumSize(new java.awt.Dimension(500, 680));
    jPanel31_chieuxuoi.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 680));

    // Thông tin chuyến
    setChieuCaoPanel(jPanel25, 75);

    // Danh sách toa
    setChieuCaoPanel(jPanel26, 145);
    setChieuCaoPanel(jPanel33, 95);

    // Danh sách ghế
    setChieuCaoPanel(jPanel29, 260);
    setChieuCaoPanel(jPanel37, 215);

    // Ghi chú + thông tin vé
    setChieuCaoPanel(jPanel31, 210);


    // =========================
    // CHIỀU VỀ
    // =========================

    // Panel tổng nội dung chiều về, bỏ chiều cao 1274 quá lớn
    jPanel32_chieunguoc.setPreferredSize(new java.awt.Dimension(1000, 680));
    jPanel32_chieunguoc.setMinimumSize(new java.awt.Dimension(500, 680));
    jPanel32_chieunguoc.setMaximumSize(new java.awt.Dimension(Integer.MAX_VALUE, 680));

    // Thông tin chuyến về
    setChieuCaoPanel(jPanel27, 75);

    // Danh sách toa chiều về
    setChieuCaoPanel(jPanel28, 145);

    // Danh sách ghế chiều về
    setChieuCaoPanel(jPanel30, 260);

    // Ghi chú + thông tin vé chiều về
    setChieuCaoPanel(jPanel32, 210);


    jPanel31_chieuxuoi.revalidate();
    jPanel31_chieuxuoi.repaint();

    jPanel32_chieunguoc.revalidate();
    jPanel32_chieunguoc.repaint();
}
    private void moManHinhQuanLyNhanVien() {
    // Kiểm tra quyền (chỉ Quản lý mới được vào)
    // BỎ DÒNG NÀY NẾU CHƯA MUỐN PHÂN QUYỀN:
    /*
    if (!laQuanLy()) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Chỉ quản lý mới được truy cập chức năng này.",
            "Không có quyền",
            javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    */

    jPanelNoi_Dung.removeAll();

    GUI_QuanLyNhanVien pnQLNV = new GUI_QuanLyNhanVien();
    jPanelNoi_Dung.setLayout(new java.awt.BorderLayout());
    jPanelNoi_Dung.add(pnQLNV, java.awt.BorderLayout.CENTER);

    jPanelNoi_Dung.revalidate();
    jPanelNoi_Dung.repaint();
}

private void moManHinhQuanLyTaiKhoan() {
    /*
    if (!laQuanLy()) {
        javax.swing.JOptionPane.showMessageDialog(this,
            "Chỉ quản lý mới được truy cập chức năng này.",
            "Không có quyền",
            javax.swing.JOptionPane.WARNING_MESSAGE);
        return;
    }
    */

    jPanelNoi_Dung.removeAll();

    GUI_QuanLyTaiKhoan pnQLTK = new GUI_QuanLyTaiKhoan();
    jPanelNoi_Dung.setLayout(new java.awt.BorderLayout());
    jPanelNoi_Dung.add(pnQLTK, java.awt.BorderLayout.CENTER);

    jPanelNoi_Dung.revalidate();
    jPanelNoi_Dung.repaint();
}
    
    

    // Vé 1 chiều: để đây xử lý bước tiếp theo sau khi chọn ghế
    // TODO: xác nhận vé 1 chiều / sang bước thanh toán


    
//    private void moGDQuanLyTaiKhoan() {
//    java.awt.CardLayout cl = (java.awt.CardLayout) jPanelNoi_Dung.getLayout();
//    cl.show(jPanelNoi_Dung, "cardTaiKhoan");
//}
    



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel GD_NHAP_TT_HK;
    private javax.swing.JPanel GD_TAU;
    private javax.swing.JPanel GD_THONGTINNHANVIEN;
    private javax.swing.JPanel GD_TOA_GHE_NGUOC;
    private javax.swing.JPanel GD_TRANGCHU;
    private javax.swing.JPanel GD_chon_TOA_GHE_XUOI;
    private javax.swing.JPanel North;
    private javax.swing.JPanel North1;
    private javax.swing.JPanel dschuyen;
    private javax.swing.JLabel hiencalam;
    private javax.swing.JLabel hienchucvu;
    private javax.swing.JLabel hienma;
    private javax.swing.JLabel hienten;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton16;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel31;
    private javax.swing.JPanel jPanel31_chieuxuoi;
    private javax.swing.JPanel jPanel32;
    private javax.swing.JPanel jPanel32_chieunguoc;
    private javax.swing.JPanel jPanel33;
    private javax.swing.JPanel jPanel34;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JPanel jPanel41;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel49;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel50;
    private javax.swing.JPanel jPanel51;
    private javax.swing.JPanel jPanel52;
    private javax.swing.JPanel jPanel53;
    private javax.swing.JPanel jPanel54;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JPanel jPanelNoi_Dung;
    private javax.swing.JPanel jPanelTrai;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jtgadi;
    private javax.swing.JLabel lblDongHo;
    private javax.swing.JLabel lblDongHo1;
    private javax.swing.JLabel lblcalam;
    private javax.swing.JLabel lblchucvu;
    private javax.swing.JLabel lblgaden;
    private javax.swing.JLabel lblgadi;
    private javax.swing.JLabel lblloaive;
    private javax.swing.JLabel lblma;
    private javax.swing.JLabel lblngaydi;
    private javax.swing.JLabel lblngayve;
    private javax.swing.JLabel lbltennv;
    private javax.swing.JLabel lblthongtinnv;
    private javax.swing.JPanel manv;
    private javax.swing.JPanel pnCenter_nv;
    private javax.swing.JPanel pnNorth_nv;
    private javax.swing.JPanel ten;
    private javax.swing.JPanel thongtinnv;
    private javax.swing.JPanel tieude;
    private javax.swing.JPanel tieude1;
    private javax.swing.JPanel tieudethongtinnv;
    private javax.swing.JPanel ttnv;
    // End of variables declaration//GEN-END:variables
}
