/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prj1.entity;

/**
 *
 * @author WINDOWS
 */


import java.util.Objects;

public class ToaTau {
    private String maToa;
    private String tenToa;
    private Integer sucChua;
    private LoaiGhe loaiGhe;
    private Tau tau;

    public ToaTau() {
    }

    public ToaTau(String maToa, String tenToa, Integer sucChua, LoaiGhe loaiGhe, Tau tau) {
        this.maToa = maToa;
        this.tenToa = tenToa;
        this.sucChua = sucChua;
        this.loaiGhe = loaiGhe;
        this.tau = tau;
    }

    public String getMaToa() {
        return maToa;
    }

    public void setMaToa(String maToa) {
        this.maToa = maToa;
    }

    public String getTenToa() {
        return tenToa;
    }

    public void setTenToa(String tenToa) {
        this.tenToa = tenToa;
    }

    public Integer getSucChua() {
        return sucChua;
    }

    public void setSucChua(Integer sucChua) {
        this.sucChua = sucChua;
    }

    public LoaiGhe getLoaiGhe() {
        return loaiGhe;
    }

    public void setLoaiGhe(LoaiGhe loaiGhe) {
        this.loaiGhe = loaiGhe;
    }

    public Tau getTau() {
        return tau;
    }

    public void setTau(Tau tau) {
        this.tau = tau;
    }
    
    
}

