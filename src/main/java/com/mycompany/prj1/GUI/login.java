package com.mycompany.prj1.GUI;

import com.mycompany.prj1.ConnectDB.DB;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class login extends JFrame {

    private JTextField txtTaiKhoan;
    private JPasswordField txtMatKhau;
    private JButton btnDangNhap;
     
    public login() {
        initUI();
    }

    private void initUI() {
        setTitle("Đăng nhập");
        setSize(420, 220);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        JLabel lblTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

        JPanel pnCenter = new JPanel(new GridLayout(2, 2, 10, 10));
        pnCenter.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel lblTaiKhoan = new JLabel("Mã nhân viên:");
        JLabel lblMatKhau = new JLabel("Mật khẩu:");

        txtTaiKhoan = new JTextField();
        txtMatKhau = new JPasswordField();

        pnCenter.add(lblTaiKhoan);
        pnCenter.add(txtTaiKhoan);
        pnCenter.add(lblMatKhau);
        pnCenter.add(txtMatKhau);

        btnDangNhap = new JButton("Đăng nhập");

        JPanel pnSouth = new JPanel();
        pnSouth.add(btnDangNhap);

        add(lblTitle, BorderLayout.NORTH);
        add(pnCenter, BorderLayout.CENTER);
        add(pnSouth, BorderLayout.SOUTH);

        btnDangNhap.addActionListener(e -> dangNhap());
        txtTaiKhoan.addActionListener(e -> dangNhap());
        txtMatKhau.addActionListener(e -> dangNhap());
    }

    private void dangNhap() {
    String maNhanVien = txtTaiKhoan.getText().trim();
    String matKhau = new String(txtMatKhau.getPassword()).trim();

    if (maNhanVien.isEmpty() || matKhau.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã nhân viên và mật khẩu.");
        return;
    }

    String sql = "SELECT MaNhanVien, VaiTro, DangDangNhap " +
                 "FROM TaiKhoan " +
                 "WHERE MaNhanVien = ? AND MatKhau = ? AND TrangThaiTK = 1";

    try {
        Connection con = DB.getConnection();

        if (con == null) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối cơ sở dữ liệu.");
            return;
        }

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, maNhanVien);
        ps.setString(2, matKhau);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            String vaiTro = rs.getString("VaiTro");
            String maNV = rs.getString("MaNhanVien");
            boolean dangDangNhap = rs.getBoolean("DangDangNhap");

            // Kiểm tra tài khoản đã đăng nhập ở nơi khác chưa
            if (dangDangNhap) {
                JOptionPane.showMessageDialog(
                        this,
                        "Tài khoản này đang được đăng nhập ở nơi khác.\n"
                        + "Vui lòng đăng xuất trước khi đăng nhập lại."
                );

                rs.close();
                ps.close();
                return;
            }

            // Cập nhật trạng thái đang đăng nhập = 1
            String sqlUpdate = "UPDATE TaiKhoan SET DangDangNhap = 1 WHERE MaNhanVien = ?";
            PreparedStatement psUpdate = con.prepareStatement(sqlUpdate);
            psUpdate.setString(1, maNV);
            psUpdate.executeUpdate();
            psUpdate.close();

            // Mở giao diện chính
            jf1 gd = new jf1(vaiTro, maNV);
            gd.setVisible(true);
            this.dispose();

        } else {
            JOptionPane.showMessageDialog(this, "Sai mã nhân viên hoặc mật khẩu.");
        }

        rs.close();
        ps.close();

    } catch (HeadlessException | SQLException e) {
        JOptionPane.showMessageDialog(this, "Lỗi:\n" + e.getMessage());
    }
}
    

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> new login().setVisible(true));
    }
}