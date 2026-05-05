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

public class ChuyenTau {
    private String maChuyen;
    private String tenChuyen;
    private LocalDateTime ngayKhoiHanh;
    private LocalDateTime ngayDenDuKien;
    private Ga gaDi;
    private Ga gaDen;
    private boolean trangThaiChuyen;
    private Tau tau;

    public ChuyenTau() {
    }

    public ChuyenTau(String maChuyen, String tenChuyen, LocalDateTime ngayKhoiHanh,
                     LocalDateTime ngayDenDuKien, Ga gaDi, Ga gaDen,
                     boolean trangThaiChuyen, Tau tau) {
        this.maChuyen = maChuyen;
        this.tenChuyen = tenChuyen;
        this.ngayKhoiHanh = ngayKhoiHanh;
        this.ngayDenDuKien = ngayDenDuKien;
        this.gaDi = gaDi;
        this.gaDen = gaDen;
        this.trangThaiChuyen = trangThaiChuyen;
        this.tau = tau;
    }

    public String getMaChuyen() {
        return maChuyen;
    }

    public void setMaChuyen(String maChuyen) {
        this.maChuyen = maChuyen;
    }

    public String getTenChuyen() {
        return tenChuyen;
    }

    public void setTenChuyen(String tenChuyen) {
        this.tenChuyen = tenChuyen;
    }

    public LocalDateTime getNgayKhoiHanh() {
        return ngayKhoiHanh;
    }

    public void setNgayKhoiHanh(LocalDateTime ngayKhoiHanh) {
        this.ngayKhoiHanh = ngayKhoiHanh;
    }

    public LocalDateTime getNgayDenDuKien() {
        return ngayDenDuKien;
    }

    public void setNgayDenDuKien(LocalDateTime ngayDenDuKien) {
        this.ngayDenDuKien = ngayDenDuKien;
    }

    public Ga getGaDi() {
        return gaDi;
    }

    public void setGaDi(Ga gaDi) {
        this.gaDi = gaDi;
    }

    public Ga getGaDen() {
        return gaDen;
    }

    public void setGaDen(Ga gaDen) {
        this.gaDen = gaDen;
    }

    public boolean isTrangThaiChuyen() {
        return trangThaiChuyen;
    }

    public void setTrangThaiChuyen(boolean trangThaiChuyen) {
        this.trangThaiChuyen = trangThaiChuyen;
    }
    
    public void setTau(Tau tau) {
        this.tau= tau;
    }

    public Tau getTau() {
        return tau;
    }

}
