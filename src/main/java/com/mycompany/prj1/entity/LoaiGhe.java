/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prj1.entity;

/**
 *
 * @author WINDOWS
 */
public class LoaiGhe {
    private String maLoaiGhe;
    private String tenLoaiGhe;
    private double heSoLoaiGhe;
    private boolean trangThai;

    public LoaiGhe() {
    }

    public LoaiGhe(String maLoaiGhe, String tenLoaiGhe, double heSoLoaiGhe, boolean trangThai) {
        this.maLoaiGhe = maLoaiGhe;
        this.tenLoaiGhe = tenLoaiGhe;
        this.heSoLoaiGhe = heSoLoaiGhe;
        this.trangThai = trangThai;
    }

    public String getMaLoaiGhe() {
        return maLoaiGhe;
    }

    public void setMaLoaiGhe(String maLoaiGhe) {
        this.maLoaiGhe = maLoaiGhe;
    }

    public String getTenLoaiGhe() {
        return tenLoaiGhe;
    }

    public void setTenLoaiGhe(String tenLoaiGhe) {
        this.tenLoaiGhe = tenLoaiGhe;
    }

    public double getHeSoLoaiGhe() {
        return heSoLoaiGhe;
    }

    public void setHeSoLoaiGhe(double heSoLoaiGhe) {
        this.heSoLoaiGhe = heSoLoaiGhe;
    }

    public boolean isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(boolean trangThai) {
        this.trangThai = trangThai;
    }
}
