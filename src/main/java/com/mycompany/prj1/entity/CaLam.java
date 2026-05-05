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

public class CaLam {
    private String maCaLam;
    private String tenCaLam;
    private LocalDateTime thoiGianBatDau;
    private LocalDateTime thoiGianKetThuc;

    public CaLam() {
    }

    public CaLam(String maCaLam, String tenCaLam, LocalDateTime thoiGianBatDau, LocalDateTime thoiGianKetThuc) {
        this.maCaLam = maCaLam;
        this.tenCaLam = tenCaLam;
        this.thoiGianBatDau = thoiGianBatDau;
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    public String getMaCaLam() {
        return maCaLam;
    }

    public void setMaCaLam(String maCaLam) {
        this.maCaLam = maCaLam;
    }

    public String getTenCaLam() {
        return tenCaLam;
    }

    public void setTenCaLam(String tenCaLam) {
        this.tenCaLam = tenCaLam;
    }

    public LocalDateTime getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public void setThoiGianBatDau(LocalDateTime thoiGianBatDau) {
        this.thoiGianBatDau = thoiGianBatDau;
    }

    public LocalDateTime getThoiGianKetThuc() {
        return thoiGianKetThuc;
    }

    public void setThoiGianKetThuc(LocalDateTime thoiGianKetThuc) {
        this.thoiGianKetThuc = thoiGianKetThuc;
    }

    @Override
    public String toString() {
        return tenCaLam;
    }
}
