/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prj1.entity;

/**
 *
 * @author WINDOWS
 */


import java.time.LocalDateTime;

public class Ve {
    private String maVe;
    private LocalDateTime ngayBan;
    private String trangThaiVe;
    private NhanVien nhanVien;
    private HanhKhach hanhKhach;
    private Ghe ghe;
    private LoaiVe loaiVe;
    private ChuyenTau chuyenTau;
    private KhuyenMai khuyenMai;
    private double tongTien;

    public Ve() {
    }

    public Ve(String maVe, LocalDateTime ngayBan, String trangThaiVe,
              NhanVien nhanVien, HanhKhach hanhKhach, Ghe ghe,
              LoaiVe loaiVe, ChuyenTau chuyenTau, KhuyenMai khuyenMai,
              double tongTien) {
        this.maVe = maVe;
        this.ngayBan = ngayBan;
        this.trangThaiVe = trangThaiVe;
        this.nhanVien = nhanVien;
        this.hanhKhach = hanhKhach;
        this.ghe = ghe;
        this.loaiVe = loaiVe;
        this.chuyenTau = chuyenTau;
        this.khuyenMai = khuyenMai;
        this.tongTien = tongTien;
    }

    public String getMaVe() {
        return maVe;
    }

    public void setMaVe(String maVe) {
        this.maVe = maVe;
    }

    public LocalDateTime getNgayBan() {
        return ngayBan;
    }

    public void setNgayBan(LocalDateTime ngayBan) {
        this.ngayBan = ngayBan;
    }

    public String getTrangThaiVe() {
        return trangThaiVe;
    }

    public void setTrangThaiVe(String trangThaiVe) {
        this.trangThaiVe = trangThaiVe;
    }

    public NhanVien getNhanVien() {
        return nhanVien;
    }

    public void setNhanVien(NhanVien nhanVien) {
        this.nhanVien = nhanVien;
    }

    public HanhKhach getHanhKhach() {
        return hanhKhach;
    }

    public void setHanhKhach(HanhKhach hanhKhach) {
        this.hanhKhach = hanhKhach;
    }

    public Ghe getGhe() {
        return ghe;
    }

    public void setGhe(Ghe ghe) {
        this.ghe = ghe;
    }

    public LoaiVe getLoaiVe() {
        return loaiVe;
    }

    public void setLoaiVe(LoaiVe loaiVe) {
        this.loaiVe = loaiVe;
    }

    public ChuyenTau getChuyenTau() {
        return chuyenTau;
    }

    public void setChuyenTau(ChuyenTau chuyenTau) {
        this.chuyenTau = chuyenTau;
    }

    public KhuyenMai getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(KhuyenMai khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}
