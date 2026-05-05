/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prj1.entity;

/**
 *
 * @author WINDOWS
 */
import java.time.LocalDate;

public class NhanVien {
    private String maNhanVien;
    private String hoTenNV;
    private String cccd;
    private String sdt;
    private LocalDate ngayVaoLam;
    private String chucVu;
    private String gioiTinh;
    private LocalDate ngaySinh;
    private boolean trangThaiNV;
    private String anhNhanVien;

    public NhanVien() {
    }

    public NhanVien(String maNhanVien, String hoTenNV, String cccd, String sdt,
                    LocalDate ngayVaoLam, String chucVu, String gioiTinh,
                    LocalDate ngaySinh, boolean trangThaiNV, String anhNhanVien) {
        this.maNhanVien = maNhanVien;
        this.hoTenNV = hoTenNV;
        this.cccd = cccd;
        this.sdt = sdt;
        this.ngayVaoLam = ngayVaoLam;
        this.chucVu = chucVu;
        this.gioiTinh = gioiTinh;
        this.ngaySinh = ngaySinh;
        this.trangThaiNV = trangThaiNV;
        this.anhNhanVien = anhNhanVien;
    }

    public String getMaNhanVien() {
        return maNhanVien;
    }

    public void setMaNhanVien(String maNhanVien) {
        this.maNhanVien = maNhanVien;
    }

    public String getHoTenNV() {
        return hoTenNV;
    }

    public void setHoTenNV(String hoTenNV) {
        this.hoTenNV = hoTenNV;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public LocalDate getNgayVaoLam() {
        return ngayVaoLam;
    }

    public void setNgayVaoLam(LocalDate ngayVaoLam) {
        this.ngayVaoLam = ngayVaoLam;
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public boolean isTrangThaiNV() {
        return trangThaiNV;
    }

    public void setTrangThaiNV(boolean trangThaiNV) {
        this.trangThaiNV = trangThaiNV;
    }
    public String getAnhNhanVien() {
        return anhNhanVien;
}

    public void setAnhNhanVien(String anhNhanVien) {
        this.anhNhanVien = anhNhanVien;
    }
}
