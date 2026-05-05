package com.mycompany.prj1.entity;

import java.time.LocalDate;

public class HanhKhach {

    private String maHanhKhach;
    private String hoTenHK;
    private String loaiGiayTo;
    private String soGiayTo;
    private String sdt;
    private LocalDate ngaySinh;
    private LoaiHanhKhach loaiHanhKhach;

    public HanhKhach() {
    }

    public HanhKhach(String maHanhKhach, String hoTenHK, String loaiGiayTo,
                     String soGiayTo, String sdt, LocalDate ngaySinh,
                     LoaiHanhKhach loaiHanhKhach) {
        this.maHanhKhach = maHanhKhach;
        this.hoTenHK = hoTenHK;
        this.loaiGiayTo = loaiGiayTo;
        this.soGiayTo = soGiayTo;
        this.sdt = sdt;
        this.ngaySinh = ngaySinh;
        this.loaiHanhKhach = loaiHanhKhach;
    }

    public String getMaHanhKhach() {
        return maHanhKhach;
    }

    public void setMaHanhKhach(String maHanhKhach) {
        this.maHanhKhach = maHanhKhach;
    }

    public String getHoTenHK() {
        return hoTenHK;
    }

    public void setHoTenHK(String hoTenHK) {
        this.hoTenHK = hoTenHK;
    }

    public String getLoaiGiayTo() {
        return loaiGiayTo;
    }

    public void setLoaiGiayTo(String loaiGiayTo) {
        this.loaiGiayTo = loaiGiayTo;
    }

    public String getSoGiayTo() {
        return soGiayTo;
    }

    public void setSoGiayTo(String soGiayTo) {
        this.soGiayTo = soGiayTo;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public LocalDate getNgaySinh() {
        return ngaySinh;
    }

    public void setNgaySinh(LocalDate ngaySinh) {
        this.ngaySinh = ngaySinh;
    }

    public LoaiHanhKhach getLoaiHanhKhach() {
        return loaiHanhKhach;
    }

    public void setLoaiHanhKhach(LoaiHanhKhach loaiHanhKhach) {
        this.loaiHanhKhach = loaiHanhKhach;
    }
}