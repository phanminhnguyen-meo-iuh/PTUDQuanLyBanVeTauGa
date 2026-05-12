package com.mycompany.prj1.entity;

/**
 * Entity Ga - khớp với bảng Ga trong DB.
 *
 * Schema:
 *   maGa         varchar(10)    PK
 *   tenGa        nvarchar(100)  NOT NULL
 *   diaChi       nvarchar(255)  NULL
 *   tinhThanh    nvarchar(100)  NOT NULL
 *   trangThaiGa  bit            NOT NULL
 */
public class Ga {

    private String maGa;
    private String tenGa;
    private String diaChi;
    private String tinhThanh;
    private boolean trangThaiGa;

    public Ga() {
    }

    /** Constructor cũ - GIỮ LẠI để không phá code khác đã dùng */
    public Ga(String maGa, String tenGa, String diaChi) {
        this.maGa = maGa;
        this.tenGa = tenGa;
        this.diaChi = diaChi;
        this.trangThaiGa = true;  // default
    }

    /** Constructor đầy đủ */
    public Ga(String maGa, String tenGa, String diaChi, String tinhThanh, boolean trangThaiGa) {
        this.maGa = maGa;
        this.tenGa = tenGa;
        this.diaChi = diaChi;
        this.tinhThanh = tinhThanh;
        this.trangThaiGa = trangThaiGa;
    }

    public String getMaGa() {
        return maGa;
    }

    public void setMaGa(String maGa) {
        this.maGa = maGa;
    }

    public String getTenGa() {
        return tenGa;
    }

    public void setTenGa(String tenGa) {
        this.tenGa = tenGa;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getTinhThanh() {
        return tinhThanh;
    }

    public void setTinhThanh(String tinhThanh) {
        this.tinhThanh = tinhThanh;
    }

    public boolean isTrangThaiGa() {
        return trangThaiGa;
    }

    public void setTrangThaiGa(boolean trangThaiGa) {
        this.trangThaiGa = trangThaiGa;
    }
}