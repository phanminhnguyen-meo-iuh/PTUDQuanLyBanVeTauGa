/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prj1.entity;

/**
 *
 * @author WINDOWS
 */
public class Ghe {
    private Integer maGhe;
    private String trangThai;
    private LoaiGhe loaiGhe;

    public Ghe() {
    }

    public Ghe(Integer maGhe, String trangThai, LoaiGhe loaiGhe) {
        this.maGhe = maGhe;
        this.trangThai = trangThai;
        this.loaiGhe = loaiGhe;
    }

    public Integer getMaGhe() {
        return maGhe;
    }

    public void setMaGhe(Integer maGhe) {
        this.maGhe = maGhe;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public LoaiGhe getLoaiGhe() {
        return loaiGhe;
    }

    public void setLoaiGhe(LoaiGhe loaiGhe) {
        this.loaiGhe = loaiGhe;
    }
}
